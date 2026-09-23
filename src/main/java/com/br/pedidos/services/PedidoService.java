package com.br.pedidos.services;

import com.br.pedidos.clients.ProdutoClient;
import com.br.pedidos.config.RabbitMQConfig;
import com.br.pedidos.dto.pedido.PedidoRequest;
import com.br.pedidos.dto.pedido.PedidoResponse;
import com.br.pedidos.dto.produto.ProdutoResponse;
import com.br.pedidos.dto.rabbit.BaixaEstoqueEvent;
import com.br.pedidos.dto.rabbit.ItemBaixaDTO;
import com.br.pedidos.entities.ItemPedido;
import com.br.pedidos.entities.Pedido;
import com.br.pedidos.entities.Status;
import com.br.pedidos.exception.pedido.PedidoNaoEncontradoException;
import com.br.pedidos.mapper.pedido.PedidoMapper;
import com.br.pedidos.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final ProdutoClient produtoClient;
    private final RabbitTemplate rabbitTemplate;


    private BigDecimal buscarPrecoOficial(UUID produtoId){
        ProdutoResponse produto = produtoClient.buscarProdutoPorId(produtoId);
        return produto.valor();
    }

    @Transactional
    public PedidoResponse criarPedido(PedidoRequest request){
        Pedido pedido = pedidoMapper.toEntity(request);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatusPedido(Status.PENDENTE);

        BigDecimal total = BigDecimal.ZERO;
        List<ItemBaixaDTO> itens = new ArrayList<>();

        for(ItemPedido item: pedido.getItensPedido()){
            BigDecimal preco = buscarPrecoOficial(item.getProdutoId());
            item.setValorUnitario(preco);

            BigDecimal subTotal = preco.multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subTotal);

            itens.add(new ItemBaixaDTO(item.getProdutoId(), item.getQuantidade()));
        }
        pedido.setValorTotal(total);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        BaixaEstoqueEvent payLoad = new BaixaEstoqueEvent(
                pedidoSalvo.getPedidoId(),
                itens
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_BAIXA,
                payLoad
        );
        return pedidoMapper.toResponse(pedidoSalvo);
    }

    public PedidoResponse buscarPorId(UUID id){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado"));

        return pedidoMapper.toResponse(pedido);
    }

    public List<PedidoResponse> listarPedidos(){
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidoMapper.toResponseList(pedidos);
    }

    @Transactional
    public void cancelarPedido(UUID id){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado"));

        pedido.setStatusPedido(Status.CANCELADO);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void atualizarStatus(UUID id, Status status){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado"));

        pedido.setStatusPedido(status);
        pedidoRepository.save(pedido);
    }
}

package com.br.pedidos.services;

import com.br.pedidos.clients.ProdutoClient;
import com.br.pedidos.dto.pedido.PedidoRequest;
import com.br.pedidos.dto.pedido.PedidoResponse;
import com.br.pedidos.dto.produto.ProdutoResponse;
import com.br.pedidos.entities.Pedido;
import com.br.pedidos.entities.Status;
import com.br.pedidos.exception.pedido.PedidoNaoEncontradoException;
import com.br.pedidos.mapper.pedido.PedidoMapper;
import com.br.pedidos.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    private final ProdutoClient produtoClient;

    private BigDecimal buscarPrecoOficial(UUID produtoId){
        try{
            ProdutoResponse produto = produtoClient.buscarProdutoPorId(produtoId);
            return produto.valor();
        }
        catch (Exception ex){
            throw new RuntimeException("Produto não encontrado no sistema de catalogo" + produtoId);
        }
    }

    @Transactional
    public PedidoResponse criarPedido(PedidoRequest request){
        Pedido pedido = pedidoMapper.toEntity(request);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatusPedido(Status.PENDENTE);

        BigDecimal total = pedido.getItensPedido().stream()
                .map(item -> {
                    BigDecimal preco = buscarPrecoOficial(item.getProdutoId());
                    item.setValorUnitario(preco);
                    produtoClient.baixarEstoque(item.getProdutoId(), item.getQuantidade());
                    return preco.multiply(BigDecimal.valueOf(item.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(total);
        return pedidoMapper.toResponse(pedidoRepository.save(pedido));
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

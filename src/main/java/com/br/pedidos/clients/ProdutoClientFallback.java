package com.br.pedidos.clients;

import com.br.pedidos.dto.produto.ProdutoResponse;
import com.br.pedidos.exception.integracao.IntegracaoException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProdutoClientFallback implements ProdutoClient{
    @Override
    public ProdutoResponse buscarProdutoPorId(UUID id) {
        throw new IntegracaoException("O serviço de estoque está fora do ar.");
    }

    @Override
    public void baixarEstoque(UUID produtoId, Integer quantidade) {
        throw new IntegracaoException("O serviço de estoque está fora do ar. Por favor, tente finalizar a compra em alguns minutos.");
    }
}

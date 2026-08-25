package com.br.pedidos.dto.pedido;

import com.br.pedidos.dto.itemPedido.ItemPedidoRequest;
import com.br.pedidos.entities.ItemPedido;

import java.util.List;

public record PedidoRequest(
        List<ItemPedidoRequest> itensPedidos
) {
}

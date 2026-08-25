package com.br.pedidos.dto.pedido;

import com.br.pedidos.entities.ItemPedido;
import com.br.pedidos.entities.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(
        UUID pedidoId,
        LocalDateTime dataPedido,
        Status statusPedido,
        List<ItemPedido> itensPedido,
        BigDecimal valorTotal
) {
}

package com.br.pedidos.dto.itemPedido;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoResponse(
         UUID itemPedidoId,
         UUID produtoId,
         Integer quantidade,
         BigDecimal valorUnitario
) {
}

package com.br.pedidos.dto.itemPedido;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoRequest(
        UUID produtoId,

        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {
}

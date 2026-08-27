package com.br.pedidos.dto.produto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponse(
        UUID id,
        String nome,
        BigDecimal valor
) {
}

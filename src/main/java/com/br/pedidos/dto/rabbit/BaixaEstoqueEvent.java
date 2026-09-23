package com.br.pedidos.dto.rabbit;

import java.util.List;
import java.util.UUID;

public record BaixaEstoqueEvent(
        UUID pedidoId,
        List<ItemBaixaDTO> itens
) {}

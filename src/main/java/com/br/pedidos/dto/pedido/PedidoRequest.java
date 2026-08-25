package com.br.pedidos.dto.pedido;

import com.br.pedidos.dto.itemPedido.ItemPedidoRequest;
import com.br.pedidos.entities.ItemPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequest(
        @NotNull(message = "A lista de itens não pode ser nula")
        @NotEmpty(message = "O pedido deve ter pelo menos um item")
        @Valid
        List<ItemPedidoRequest> itensPedido
) {
}

package com.br.pedidos.mapper.itemPedido;


import com.br.pedidos.dto.itemPedido.ItemPedidoRequest;
import com.br.pedidos.dto.itemPedido.ItemPedidoResponse;
import com.br.pedidos.entities.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemPedidoMapper {

    ItemPedido toEntity(ItemPedidoRequest request);
    ItemPedidoResponse toResponse(ItemPedido ItemPedido);
}

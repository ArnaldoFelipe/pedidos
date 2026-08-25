package com.br.pedidos.mapper.pedido;

import com.br.pedidos.dto.pedido.PedidoRequest;
import com.br.pedidos.dto.pedido.PedidoResponse;
import com.br.pedidos.entities.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PedidoMapper {

    Pedido toEntity(PedidoRequest request);
    PedidoResponse toResponse(Pedido pedido);
    List<PedidoResponse> toResponseList(List<Pedido> pedidos);
}

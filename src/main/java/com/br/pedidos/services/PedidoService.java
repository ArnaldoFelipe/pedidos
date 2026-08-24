package com.br.pedidos.services;

import com.br.pedidos.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
}

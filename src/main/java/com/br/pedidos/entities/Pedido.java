package com.br.pedidos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pedidoId;
    private LocalDateTime dataPedido;
    private Status statusPedido;

    @OneToMany
    private List<ItemPedido> itensPedido;
    private BigDecimal valorTotal;
}

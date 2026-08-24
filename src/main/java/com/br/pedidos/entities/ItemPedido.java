package com.br.pedidos.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemPedidoId;
    private UUID produtoId;
    private Integer quantidade;
    private BigDecimal valorUnitario;
}

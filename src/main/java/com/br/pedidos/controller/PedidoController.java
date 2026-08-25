package com.br.pedidos.controller;

import com.br.pedidos.dto.pedido.PedidoRequest;
import com.br.pedidos.dto.pedido.PedidoResponse;
import com.br.pedidos.entities.Pedido;
import com.br.pedidos.entities.Status;
import com.br.pedidos.services.PedidoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@AllArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@RequestBody @Valid PedidoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(request));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarPedidos(){
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable UUID id){
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable UUID id, @RequestParam Status status){
        pedidoService.atualizarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable UUID id){
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}

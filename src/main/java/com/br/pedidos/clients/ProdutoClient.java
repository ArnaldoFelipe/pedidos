package com.br.pedidos.clients;

import com.br.pedidos.dto.produto.ProdutoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "produto-api", url = "http://localhost:8081")
public interface ProdutoClient {

    @GetMapping("/produtos/{id}")
    ProdutoResponse buscarProdutoPorId(@PathVariable UUID id);

    @PatchMapping("/produtos/{produtoId}/baixar-estoque")
    void baixarEstoque(@PathVariable ("produtoId") UUID produtoId, @RequestParam("quantidade") Integer quantidade);
}

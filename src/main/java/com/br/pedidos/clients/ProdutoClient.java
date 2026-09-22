package com.br.pedidos.clients;

import com.br.pedidos.config.FeingErrorDecoder;
import com.br.pedidos.dto.produto.ProdutoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "produto-api",
        url = "http://estoque-api:8081",
        configuration = FeingErrorDecoder.class,
        fallback = ProdutoClientFallback.class
)
public interface ProdutoClient {

    @GetMapping("/produtos/{id}")
    ProdutoResponse buscarProdutoPorId(@PathVariable UUID id);

    @PutMapping("/produtos/{produtoId}/baixar-estoque")
    void baixarEstoque(@PathVariable ("produtoId") UUID produtoId, @RequestParam("quantidade") Integer quantidade);
}

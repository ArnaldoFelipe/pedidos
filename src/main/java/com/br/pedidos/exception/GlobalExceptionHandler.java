package com.br.pedidos.exception;

import com.br.pedidos.exception.dto.ErroResponse;
import com.br.pedidos.exception.pedido.PedidoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> HandlePedidoNaoEncopntrado(PedidoNaoEncontradoException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(
                        ex.getMessage(),
                        "PEDIDO_NAO_ENCONTRADO",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleErrosDeValidacao(MethodArgumentNotValidException ex) {

        // Pega a mensagem do erro que falhou (ex: "A quantidade deve ser maior que zero")
        String mensagemErro = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(
                        mensagemErro,
                        "ERRO_DE_VALIDACAO_DADOS",
                        LocalDateTime.now()
                ));
    }
}

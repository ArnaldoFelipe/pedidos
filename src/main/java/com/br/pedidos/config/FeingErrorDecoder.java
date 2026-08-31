package com.br.pedidos.config;

import com.br.pedidos.exception.integracao.IntegracaoException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Component
public class FeingErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        try (InputStream bodyIs = response.body().asInputStream()) {
            JsonNode jsonNode = objectMapper.readTree(bodyIs);
            String mensagemOriginal = jsonNode.has("message")
                    ? jsonNode.get("message").asText()
                    : "Erro na comunicação entre Serviços";

            return new IntegracaoException(mensagemOriginal);
        } catch (Exception e) {
            return defaultErrorDecoder.decode(s, response);
        }
    }
}

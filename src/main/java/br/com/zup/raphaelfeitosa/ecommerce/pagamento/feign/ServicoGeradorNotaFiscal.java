package br.com.zup.raphaelfeitosa.ecommerce.pagamento.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "identificador-nota-fiscal", url = "http://localhost:8080/servico-nota-fiscal")
public interface ServicoGeradorNotaFiscal {

    @PostMapping(value = "/{idUsuario}/{idCompra}")
    ResponseEntity<Void> envia(@PathVariable Long idUsuario, @PathVariable Long idCompra);
}


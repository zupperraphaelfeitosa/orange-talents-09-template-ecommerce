package br.com.zup.raphaelfeitosa.ecommerce.pagamento.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "identificador-ranking-vendedores", url = "http://localhost:8080/servico-ranking-vendedores")
public interface ServicoRankingVendedor {

    @PostMapping(value = "/{idUsuario}/{idCompra}")
    ResponseEntity<Void> envia(@PathVariable Long idUsuario, @PathVariable Long idCompra);
}


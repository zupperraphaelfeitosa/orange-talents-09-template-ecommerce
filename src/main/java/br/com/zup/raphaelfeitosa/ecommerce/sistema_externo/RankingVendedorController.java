package br.com.zup.raphaelfeitosa.ecommerce.sistema_externo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingVendedorController {

    @PostMapping(value = "/servico-ranking-vendedores/{idUsuario}/{idCompra}")
    public void enviarRankingVendedores(@PathVariable Long idUsuario, @PathVariable Long idCompra) {
        System.out.println("Ranking Vendedores");
    }
}

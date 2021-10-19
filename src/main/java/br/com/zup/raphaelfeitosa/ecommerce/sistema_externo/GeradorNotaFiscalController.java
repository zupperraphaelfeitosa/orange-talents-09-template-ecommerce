package br.com.zup.raphaelfeitosa.ecommerce.sistema_externo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeradorNotaFiscalController {

    @PostMapping(value = "/servico-nota-fiscal/{idUsuario}/{idCompra}")
    public void enviarNotaFiscal(@PathVariable Long idUsuario, @PathVariable Long idCompra) {
        System.out.println("Nota Fiscal");
    }
}

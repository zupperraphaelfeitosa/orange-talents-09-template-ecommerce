package br.com.zup.raphaelfeitosa.ecommerce.pagamento;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.CompraProduto;
import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.enums.StatusCompra;
import br.com.zup.raphaelfeitosa.ecommerce.envio_email.EnvioEmail;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.enums.StatusPagamento;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.feign.ServicoGeradorNotaFiscal;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.feign.ServicoRankingVendedor;
import br.com.zup.raphaelfeitosa.ecommerce.validacao.handler.exception.ExcecaoPagamentoInvalido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/pagamentos")
public class PagamentoController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EnvioEmail envioEmail;

    @Autowired
    private ServicoGeradorNotaFiscal servicoGeradorNotaFiscal;

    @Autowired
    private ServicoRankingVendedor servicoRankingVendedor;

    @PostMapping("/pagseguro")
    @Transactional
    public void pagamentoPagSeguro(@RequestBody @Valid PagSeguroRequest retornoPagamento) {
        CompraProduto compra = entityManager.find(CompraProduto.class, retornoPagamento.getIdCompra());
        Pagamento pagamento = retornoPagamento.toPagamento(compra);
        processaPagamento(compra, pagamento);

    }

    @PostMapping("/paypal")
    @Transactional
    public void pagamentoPayPal(@RequestBody @Valid PayPalRequest retornoPagamento) {
        CompraProduto compra = entityManager.find(CompraProduto.class, retornoPagamento.getIdCompra());
        Pagamento pagamento = retornoPagamento.toPagamento(compra);
        processaPagamento(compra, pagamento);
    }

    private void processaPagamento(CompraProduto compra, Pagamento pagamento) {
        compra.verificaPagamentoProcessado();
        entityManager.persist(pagamento);
        if (pagamento.getStatusPagamento().equals(StatusPagamento.SUCESSO)) {
            compra.alteraStatusDaCompra(StatusCompra.SUCESSO);
            envioEmail.enviaEmailCompraSucesso(compra);
            servicoGeradorNotaFiscal.envia(compra.getUsuario().getId(), compra.getProduto().getId());
            servicoRankingVendedor.envia(compra.getUsuario().getId(), compra.getProduto().getId());
        } else {
            envioEmail.enviaEmailCompraErro(compra);
        }
    }
}

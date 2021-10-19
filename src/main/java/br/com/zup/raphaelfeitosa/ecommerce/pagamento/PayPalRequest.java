package br.com.zup.raphaelfeitosa.ecommerce.pagamento;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.CompraProduto;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.enums.StatusPagamento;
import br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao.IdNaoExistente;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PayPalRequest {

    @NotNull
    @IdNaoExistente(classeDominio = CompraProduto.class,
            nomeCampo = "id",
            message = "Id da compra invalido!")
    private Long idCompra;

    @NotNull
    private Long idPagamento;

    @NotNull
    @Min(0)
    @Max(1)
    private Integer statusPagamento;

    public PayPalRequest(Long idCompra, Long idPagamento, Integer statusPagamento) {
        this.idCompra = idCompra;
        this.idPagamento = idPagamento;
        this.statusPagamento = statusPagamento;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public Pagamento toPagamento(CompraProduto compra) {
        StatusPagamento status = StatusPagamento.statusPayPal(statusPagamento);
        return new Pagamento(idPagamento, status, compra);
    }
}

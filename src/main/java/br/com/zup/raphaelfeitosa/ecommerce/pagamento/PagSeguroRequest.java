package br.com.zup.raphaelfeitosa.ecommerce.pagamento;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.CompraProduto;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.enums.StatusPagamento;
import br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao.IdNaoExistente;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PagSeguroRequest {

    @NotNull
    @IdNaoExistente(classeDominio = CompraProduto.class,
            nomeCampo = "id",
            message = "Id da compra invalido!")
    private Long idCompra;

    @NotNull
    private Long idPagamento;

    @NotBlank
    private String statusPagamento;

    public PagSeguroRequest(Long idCompra, Long idPagamento, String statusPagamento) {
        this.idCompra = idCompra;
        this.idPagamento = idPagamento;
        this.statusPagamento = statusPagamento;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public Pagamento toPagamento(CompraProduto compra) {
        StatusPagamento status = StatusPagamento.statusPagSeguro(statusPagamento);
        return new Pagamento(idPagamento, status, compra);
    }
}

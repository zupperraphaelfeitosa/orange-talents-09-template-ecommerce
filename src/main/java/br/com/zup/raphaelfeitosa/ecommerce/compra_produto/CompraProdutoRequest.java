package br.com.zup.raphaelfeitosa.ecommerce.compra_produto;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.enums.Gateway;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao.IdNaoExistente;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CompraProdutoRequest {

    @NotNull
    @Positive
    @Min(1)
    private Integer quantidade;

    @NotNull
    @IdNaoExistente(classeDominio = Produto.class, nomeCampo = "id",
            message = "Produto não existe")
    private Long idProduto;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gateway gateway;

    public CompraProdutoRequest(Integer quantidade, Long idProduto, Gateway gateway) {
        this.quantidade = quantidade;
        this.idProduto = idProduto;
        this.gateway = gateway;
    }

    public CompraProduto toCompraProduto(Produto produto, Usuario usuario) {

        return new CompraProduto(quantidade, gateway, produto, usuario);
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Gateway getGateway() {
        return gateway;
    }
}

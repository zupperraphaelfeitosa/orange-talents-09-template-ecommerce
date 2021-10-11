package br.com.zup.raphaelfeitosa.ecommerce.opiniao_produto;

import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;

import javax.validation.constraints.*;

public class OpiniaoProdutoRequest {

    @NotNull
    @Min(value = 1, message = "Nota não pode ser menor que 1")
    @Max(value = 5, message = "Nota não pode ser maior que 5")
    private Integer nota;

    @NotBlank(message = "Titulo é Obrigatorio")
    private String titulo;

    @NotBlank(message = "descricao é obrigatoria")
    @Size(max = 500, message = "Só é permitido 500 caracteres na descrição")
    private String descricao;

    public OpiniaoProdutoRequest(Integer nota, String titulo, String descricao) {
        this.nota = nota;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public OpiniaoProduto toOpiniaoProduto(Produto produto, Usuario usuario) {
        return new OpiniaoProduto(nota, titulo, descricao, produto, usuario);
    }
}

package br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto;

import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class PerguntaProdutoRequest {

    @NotBlank(message = "Titulo da pergunta é obrigatorio")
    private String titulo;

    public PerguntaProdutoRequest(@JsonProperty("titulo") String titulo) {
        this.titulo = titulo;
    }

    public PerguntaProduto toPerguntaProduto(Produto produto, Usuario usuario) {
        return new PerguntaProduto(titulo, produto, usuario);
    }
}

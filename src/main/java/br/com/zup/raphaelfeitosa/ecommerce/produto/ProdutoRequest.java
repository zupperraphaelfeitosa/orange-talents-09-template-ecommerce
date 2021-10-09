package br.com.zup.raphaelfeitosa.ecommerce.produto;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.Categoria;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao.IdNaoExistente;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProdutoRequest {

    @NotBlank(message = "nome não pode ser vazio ou nulo")
    private String nome;

    @NotNull(message = "valor nao pode ser nulo")
    @Positive(message = "valor tem que ser positivo")
    private BigDecimal valor;

    @NotNull(message = "Quantidade não pode ser nula")
    @Min(value = 0, message = "Quantidade tem que ser igual ou maior que zero")
    private Integer quantidade;

    @NotBlank(message = "Descrição do produto é obrigatoria")
    @Size(max = 1000, message = "Só é permitido 1000 caracteres na descrição do produto")
    private String descricao;

    @NotNull
    @Size(min = 3, message = "Deve conter no minuno 3 caracteristicas")
    private Map<String, String> caracteristicas = new HashMap<>();

    @NotNull(message = "Categoria é obrigatorio para cadastrar o produto")
    @IdNaoExistente(classeDominio = Categoria.class,
            nomeCampo = "id",
            message = "Categoria não existente")
    private Long idCategoria;

    public ProdutoRequest(String nome, BigDecimal valor, Integer quantidade, String descricao,
                          Map<String, String> caracteristicas, Long idCategoria) {
        this.nome = nome;
        this.valor = valor;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.caracteristicas = caracteristicas;
        this.idCategoria = idCategoria;
    }

    public Produto toProduto(EntityManager entityManager, UserDetails usuarioLogado) {

        Categoria categoria = entityManager.find(Categoria.class, idCategoria);
        Usuario usuario = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                .setParameter("email", usuarioLogado.getUsername())
                .getSingleResult();

        Assert.state(Objects.nonNull(categoria),
                "Você está querendo cadastrar um produto com uma categoria que não existe, id_categoria: " + idCategoria);
        Assert.state(Objects.nonNull(usuario),
                "Você está querendo cadastrar um produto com um usuário sem permissão: " + usuarioLogado.getUsername());
        return new Produto(nome, valor, quantidade, descricao, caracteristicas, categoria, usuario);

    }
}

package br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto;

import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_perguntas_produtos")
public class PerguntaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "data_de_criacao")
    private LocalDateTime dataDeCadastro = LocalDateTime.now();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Deprecated
    public PerguntaProduto() {
    }

    public PerguntaProduto(String titulo, Produto produto, Usuario usuario) {
        this.titulo = titulo;
        this.produto = produto;
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public Produto getProduto() {
        return produto;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

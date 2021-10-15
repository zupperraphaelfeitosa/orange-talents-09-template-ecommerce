package br.com.zup.raphaelfeitosa.ecommerce.produto;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.Categoria;
import br.com.zup.raphaelfeitosa.ecommerce.imagem_produto.ImagemProduto;
import br.com.zup.raphaelfeitosa.ecommerce.opiniao_produto.OpiniaoProduto;
import br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto.PerguntaProduto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import br.com.zup.raphaelfeitosa.ecommerce.validacao.handler.exception.ExcecaoEstoqueIndisponivel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private Integer quantidadeDisponivel;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @ElementCollection
    private Map<String, String> caracteristicas = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.MERGE)
    private Set<ImagemProduto> imagens = new HashSet<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.MERGE)
    private Set<OpiniaoProduto> opinioes = new HashSet<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.MERGE)
    private List<PerguntaProduto> perguntas = new ArrayList<>();

    @Deprecated
    public Produto() {
    }

    public Produto(String nome, BigDecimal valor, Integer quantidadeDisponivel, String descricao,
                   Map<String, String> caracteristicas, Categoria categoria, Usuario usuario) {
        this.nome = nome;
        this.valor = valor;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.descricao = descricao;
        this.caracteristicas = caracteristicas;
        this.categoria = categoria;
        this.usuario = usuario;
    }

    public void associaImagens(Set<String> links) {
        Set<ImagemProduto> imagens = links.stream()
                .map(link -> new ImagemProduto(this, link))
                .collect(Collectors.toSet());
        this.imagens.addAll(imagens);
    }

    public boolean pertenceAoUsuario(Optional<Usuario> possivelDonoDoProduto) {
        return usuario.equals(possivelDonoDoProduto.get());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Integer getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public Map<String, String> getCaracteristicas() {
        return caracteristicas;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Set<ImagemProduto> getImagens() {
        return imagens;
    }

    public Opinioes getOpinioes() {
        return new Opinioes(this.opinioes);
    }

    public List<PerguntaProduto> getPerguntas() {
        return perguntas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void estoque(Integer quantidade) {
        if (this.quantidadeDisponivel < quantidade) {
            throw new ExcecaoEstoqueIndisponivel(this.quantidadeDisponivel);
        }
        quantidadeDisponivel -= quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Produto produto = (Produto) o;

        if (!nome.equals(produto.nome)) return false;
        return usuario.equals(produto.usuario);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + usuario.hashCode();
        return result;
    }
}

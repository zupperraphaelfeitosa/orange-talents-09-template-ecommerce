package br.com.zup.raphaelfeitosa.ecommerce.detalhe_produto;

import br.com.zup.raphaelfeitosa.ecommerce.imagem_produto.ImagemProduto;
import br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto.PerguntaProduto;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Opinioes;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DetalheProdutoResponse {

    private String nome;

    private BigDecimal preco;

    private String descricao;

    private Integer estoque;

    private Map<String, String> caracteristicas;

    private Double mediaNotas;

    private Integer totalNotas;

    private Set<String> imagens;

    private Set<Map<String, String>> opinioes;

    private List<String> perguntas;

    private String categoria;

    private LocalDateTime dataCriacao;

    public DetalheProdutoResponse(Produto produto) {
        this.nome = produto.getNome();
        this.preco = produto.getValor();
        this.descricao = produto.getDescricao();
        this.estoque = produto.getQuantidadeDisponivel();
        this.caracteristicas = produto.getCaracteristicas();
        this.imagens = produto.getImagens()
                .stream()
                .map(ImagemProduto::getLink)
                .collect(Collectors.toSet());
        this.categoria = produto.getCategoria().getNome();
        this.perguntas = produto.getPerguntas()
                .stream()
                .map(PerguntaProduto::getTitulo)
                .collect(Collectors.toList());

        Opinioes opinioes = produto.getOpinioes();

        this.opinioes = opinioes.mapeiaOpinioes(opiniao ->
                Map.of("titulo", opiniao.getTitulo(),
                        "descricao", opiniao.getDescricao()));

        this.mediaNotas = opinioes.mediaDeNotas();
        this.totalNotas = opinioes.totalDeMedias();

        this.dataCriacao = produto.getDataCriacao();

    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public Map<String, String> getCaracteristicas() {
        return caracteristicas;
    }

    public Set<String> getImagens() {
        return imagens;
    }

    public String getCategoria() {
        return categoria;
    }

    public Set<Map<String, String>> getOpinioes() {
        return opinioes;
    }

    public List<String> getPerguntas() {
        return perguntas;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Double getMediaNotas() {
        return mediaNotas;
    }

    public Integer getTotalNotas() {
        return totalNotas;
    }
}

package br.com.zup.raphaelfeitosa.ecommerce.compra_produto;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.enums.Gateway;
import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.enums.StatusCompra;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import org.hibernate.id.GUIDGenerator;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_compras")
public class CompraProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantidade;

    @Enumerated
    @Column(nullable = false)
    private Gateway gateway;

    @Column(nullable = false)
    private BigDecimal valorMomentoAtual;

    @Column(nullable = false)
    private BigDecimal valorTotalDaCompra;

    @Column(nullable = false)
    private String codigoTransacao;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCompra statusCompra;

    private LocalDateTime dataCompra = LocalDateTime.now();

    public CompraProduto(Integer quantidade, Gateway gateway, Produto produto, Usuario usuario) {
        this.quantidade = quantidade;
        this.gateway = gateway;
        this.valorMomentoAtual = produto.getValor();
        this.produto = produto;
        this.usuario = usuario;
        this.statusCompra = StatusCompra.INICIADA;
        this.valorTotalDaCompra = valorTotal(quantidade, produto.getValor());
        this.codigoTransacao = gerarUUIDTransacao();
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public StatusCompra getStatusCompra() {
        return statusCompra;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public BigDecimal getValorMomentoAtual() {
        return valorMomentoAtual;
    }

    public BigDecimal getValorTotalDaCompra() {
        return valorTotalDaCompra;
    }

    public String getCodigoTransacao() {
        return codigoTransacao;
    }

    public URI urlRedirect(UriComponentsBuilder uriBuilder) {
        return gateway.urlResponse(this, uriBuilder);
    }

    public BigDecimal valorTotal(Integer quantidade, BigDecimal valorMomentoAtual) {
        return valorMomentoAtual.multiply(new BigDecimal(quantidade));
    }

    public String gerarUUIDTransacao() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

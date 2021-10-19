package br.com.zup.raphaelfeitosa.ecommerce.compra_produto;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.enums.Gateway;
import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.enums.StatusCompra;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.Pagamento;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.enums.StatusPagamento;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import br.com.zup.raphaelfeitosa.ecommerce.validacao.handler.exception.ExcecaoPagamentoInvalido;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Enumerated
    @Column(nullable = false)
    private StatusCompra statusCompra;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.MERGE)
    private List<Pagamento> pagamentos = new ArrayList<>();

    private LocalDateTime dataCompra = LocalDateTime.now();

    @Deprecated
    public CompraProduto() {
    }

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

    private BigDecimal valorTotal(Integer quantidade, BigDecimal valorMomentoAtual) {
        return valorMomentoAtual.multiply(new BigDecimal(quantidade));
    }

    private String gerarUUIDTransacao() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public void verificaPagamentoProcessado() {

        if (pagamentos.isEmpty()) {
            return;
        }
        for (Pagamento pagamento : pagamentos) {
            if (pagamento.getStatusPagamento().equals(StatusPagamento.SUCESSO)) {
                throw new ExcecaoPagamentoInvalido("Pagamento já processado!");
            }
        }
    }

    public void alteraStatusDaCompra(StatusCompra statusCompra) {
        this.statusCompra = statusCompra;
    }
}
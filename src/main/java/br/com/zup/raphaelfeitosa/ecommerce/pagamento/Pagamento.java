package br.com.zup.raphaelfeitosa.ecommerce.pagamento;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.CompraProduto;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.enums.StatusPagamento;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idPagamento;

    @Enumerated
    @Column(nullable = false)
    private StatusPagamento statusPagamento;

    @ManyToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private CompraProduto compra;

    private LocalDateTime instantePagamento = LocalDateTime.now();

    @Deprecated
    public Pagamento(){}

    public Pagamento(Long idPagamento, StatusPagamento statusPagamento, CompraProduto compra) {
        this.idPagamento = idPagamento;
        this.statusPagamento = statusPagamento;
        this.compra = compra;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }
}

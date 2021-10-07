package br.com.zup.raphaelfeitosa.ecommerce.categoria;

import javax.persistence.*;

@Entity
@Table(name = "tb_categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToOne
    private Categoria categoria;

    @Deprecated
    public Categoria(){}

    public Categoria(String nome, Categoria categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }
}

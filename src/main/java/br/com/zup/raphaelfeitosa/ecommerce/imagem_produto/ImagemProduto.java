package br.com.zup.raphaelfeitosa.ecommerce.imagem_produto;

import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_imagens_produto")
public class ImagemProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @URL
    @NotNull
    private String link;

    @Deprecated
    public  ImagemProduto(){}

    public ImagemProduto(Produto produto, String link) {
        this.produto = produto;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImagemProduto that = (ImagemProduto) o;

        if (!produto.equals(that.produto)) return false;
        return link.equals(that.link);
    }

    @Override
    public int hashCode() {
        int result = produto.hashCode();
        result = 31 * result + link.hashCode();
        return result;
    }

}

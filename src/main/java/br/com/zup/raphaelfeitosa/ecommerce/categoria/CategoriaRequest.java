package br.com.zup.raphaelfeitosa.ecommerce.categoria;

import br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao.CampoUnicoGenerico;
import br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao.IdNaoExistente;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;

public class CategoriaRequest {

    @NotBlank(message = "nova da categoria é obrigatório")
    @CampoUnicoGenerico(classeDominio = Categoria.class,
            nomeCampo = "nome",
            message = "Categoria existente no banco de dados")
    private String nome;

    @IdNaoExistente(classeDominio = Categoria.class,
            nomeCampo = "id",
            message = "Categoria não existente no banco de dados passe null caso não tenha categoria mãe")
    private Long idCategoriaMae;


    public CategoriaRequest(String nome, Long idCategoriaMae) {
        this.nome = nome;
        this.idCategoriaMae = idCategoriaMae;
    }

    public Categoria toCategoria(EntityManager entityManager) {

        if (idCategoriaMae != null) {
            Categoria categoria = entityManager.find(Categoria.class, idCategoriaMae);
            return new Categoria(nome, categoria);
        }
        return new Categoria(nome, null);
    }
}

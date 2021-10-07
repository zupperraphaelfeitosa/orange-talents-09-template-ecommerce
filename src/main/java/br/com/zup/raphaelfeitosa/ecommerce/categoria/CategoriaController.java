package br.com.zup.raphaelfeitosa.ecommerce.categoria;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @PersistenceContext
    EntityManager entityManager;

    @PostMapping
    @Transactional
    public void cadastrarCategoria(@RequestBody @Valid CategoriaRequest categoriaRequest) {
        Categoria novaCategoria = categoriaRequest.toCategoria(entityManager);
        entityManager.persist(novaCategoria);
    }
}

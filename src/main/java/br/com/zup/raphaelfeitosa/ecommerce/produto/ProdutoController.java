package br.com.zup.raphaelfeitosa.ecommerce.produto;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    @PersistenceContext
    EntityManager entityManager;

    @PostMapping
    @Transactional
    public void cadastrarProduto(@RequestBody @Valid ProdutoRequest produtoRequest,
                                 @AuthenticationPrincipal UserDetails usuarioLogado) {
        Produto novoProduto = produtoRequest.toProduto(entityManager, usuarioLogado);
        entityManager.persist(novoProduto);
    }
}

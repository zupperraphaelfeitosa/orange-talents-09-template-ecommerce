package br.com.zup.raphaelfeitosa.ecommerce.detalhe_produto;

import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/produtos")
public class DetalheProtudoController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/{id}")
    public DetalheProdutoResponse detalheProduto(@PathVariable Long id) {
        Produto produto = Optional.ofNullable(entityManager.find(Produto.class, id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não existe"));

            return new DetalheProdutoResponse(produto);
    }
}

package br.com.zup.raphaelfeitosa.ecommerce.opiniao_produto;


import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/produtos")
public class OpiniaoProdutoController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/{id}/opiniao")
    @Transactional
    public void cadastrarOpniao(@PathVariable Long id, @RequestBody @Valid OpiniaoProdutoRequest opiniaoProdutoRequest,
                                @AuthenticationPrincipal UserDetails usuarioLogado) {
        Produto produto = Optional.ofNullable(entityManager.find(Produto.class, id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não Existe"));

        Usuario usuario = usuarioRepository.findByEmail(usuarioLogado.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        if (produto.pertenceAoUsuario(usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario não pode da opinião no seu proprio produto");
        }

        OpiniaoProduto novaOpiniaoProduto = opiniaoProdutoRequest.toOpiniaoProduto(produto, usuario);

        entityManager.persist(novaOpiniaoProduto);
    }
}

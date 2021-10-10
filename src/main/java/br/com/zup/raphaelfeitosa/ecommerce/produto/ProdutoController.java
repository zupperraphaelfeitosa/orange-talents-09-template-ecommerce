package br.com.zup.raphaelfeitosa.ecommerce.produto;

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
import java.util.Set;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnvioDeImagens fakeDeEnvioDeImagens;

    @PostMapping
    @Transactional
    public void cadastrarProduto(@RequestBody @Valid ProdutoRequest produtoRequest,
                                 @AuthenticationPrincipal UserDetails usuarioLogado) {
        Produto novoProduto = produtoRequest.toProduto(entityManager, usuarioLogado);
        entityManager.persist(novoProduto);
    }

    @PostMapping("/{id}/imagens")
    @Transactional
    public void adicionarImagemAoProduto(@PathVariable Long id,
                                         @Valid ImagemRequest imagemRequest,
                                         @AuthenticationPrincipal UserDetails usuarioLogado) {

        Produto produto = Optional.ofNullable(entityManager.find(Produto.class, id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!produto.pertenceAoUsuario(usuarioRepository.findByEmail(usuarioLogado.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Set<String> links = fakeDeEnvioDeImagens.enviaFotos(imagemRequest.getImagens());
        produto.associaImagens(links);
        entityManager.merge(produto);
    }
}

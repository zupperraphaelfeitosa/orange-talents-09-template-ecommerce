package br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto;


import br.com.zup.raphaelfeitosa.ecommerce.envio_email.EnvioEmail;
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
public class PerguntaProdutoController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnvioEmail envioEmail;

    @PostMapping("/{id}/perguntas")
    @Transactional
    public void cadastrarPergunta(@PathVariable Long id, @RequestBody
    @Valid PerguntaProdutoRequest perguntaProdutoRequest,
                                  @AuthenticationPrincipal UserDetails usuarioLogado) {

        Produto produto = Optional.ofNullable(entityManager.find(Produto.class, id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não existente"));

        Usuario usuario = usuarioRepository.findByEmail(usuarioLogado.getUsername())
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.FORBIDDEN)));

        if (produto.pertenceAoUsuario(usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario não pode realizar pergunta no seu proprio produto");
        }

        PerguntaProduto novaPergunta = perguntaProdutoRequest.toPerguntaProduto(produto, usuario);

        entityManager.persist(novaPergunta);
        envioEmail.enviaEmailPergunta(novaPergunta);
    }
}

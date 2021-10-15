package br.com.zup.raphaelfeitosa.ecommerce.compra_produto;

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
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/compras")
public class CompraProdutoController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnvioEmail envioEmail;

    @PostMapping
    @ResponseStatus(HttpStatus.FOUND)
    @Transactional
    public URI compraProduto(@RequestBody @Valid CompraProdutoRequest compraProdutoRequest,
                             UriComponentsBuilder uriBuilder,
                             @AuthenticationPrincipal UserDetails usuarioLogado) {

        Produto produto = entityManager.find(Produto.class, compraProdutoRequest.getIdProduto());

        Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLogado.getUsername());

        if(produto.pertenceAoUsuario(usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não pode realizar compra do seu proprio produto");
        }

        CompraProduto novaCompra = compraProdutoRequest.toCompraProduto(produto, usuario.get());

        produto.estoque(compraProdutoRequest.getQuantidade());

        entityManager.persist(novaCompra);
        entityManager.persist(produto);

        envioEmail.enviaEmailCompraIniciada(novaCompra);

        return novaCompra.urlRedirect(uriBuilder);
    }
}

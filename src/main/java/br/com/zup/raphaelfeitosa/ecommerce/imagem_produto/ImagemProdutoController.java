package br.com.zup.raphaelfeitosa.ecommerce.imagem_produto;

import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/produtos")
public class ImagemProdutoController {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnvioDeImagens fakeDeEnvioDeImagens;

    @PostMapping("/{id}/imagens")
    @Transactional
    public void adicionarImagemAoProduto(@PathVariable Long id,
                                         @Valid ImagemRequest imagemRequest,
                                         @AuthenticationPrincipal UserDetails usuarioLogado) {

        Produto produto = Optional.ofNullable(entityManager.find(Produto.class, id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não Existe"));

        Usuario usuario = usuarioRepository.findByEmail(usuarioLogado.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        if (!produto.pertenceAoUsuario(usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,  "Você não é dono deste produto!");
        }

        Set<String> links = fakeDeEnvioDeImagens.enviaFotos(imagemRequest.getImagens());
        produto.associaImagens(links);
        entityManager.merge(produto);
    }
}

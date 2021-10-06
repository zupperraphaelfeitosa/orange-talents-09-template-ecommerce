package br.com.zup.raphaelfeitosa.ecommerce.usuario;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @PersistenceContext
    EntityManager entityManager;

    @PostMapping
    @Transactional
    public void cadastrarUsuario(@RequestBody @Valid UsuarioRequest usuarioRequest) {
        Usuario novoUsuario = usuarioRequest.toUsuario();
        entityManager.persist(novoUsuario);
    }
}

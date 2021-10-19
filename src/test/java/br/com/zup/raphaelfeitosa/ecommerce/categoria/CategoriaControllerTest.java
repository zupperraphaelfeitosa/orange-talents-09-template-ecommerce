package br.com.zup.raphaelfeitosa.ecommerce.categoria;

import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenService;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.UsuarioAutenticacaoRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class CategoriaControllerTest {

    private final String uri = "/api/v1/categorias";
    private Usuario usuario;
    private Categoria categoria;
    private String token;

    Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        this.usuario = new Usuario("johndoe@gmail.com", "123456");
        this. entityManager.persist(this.usuario);

        this.categoria = new Categoria("Telefone", null);
        this. entityManager.persist(this.categoria);

        UsuarioAutenticacaoRequest dadosLoginUsuarioUm = new UsuarioAutenticacaoRequest("johndoe@gmail.com", "123456");
        token = tokenService.gerarToken(authenticationManager.authenticate(dadosLoginUsuarioUm.converter()));
    }

    @Test
    @Order(1)
    void deveriaCadastrarCategoriaSemCategoriaMaeComRetorno200() throws Exception {

        CategoriaRequest novaCategoriaSemCategoriaMae = new CategoriaRequest(
                "Apple",
                null
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + token)
                        .content(gson.toJson(novaCategoriaSemCategoriaMae))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(2)
    void deveriaCadastrarCategoriaComCategoriaMaeComRetorno200() throws Exception {

        CategoriaRequest novaCategoriaComCategoriaMae = new CategoriaRequest(
                "Smartphone",
                this.categoria.getId()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + token)
                        .content(gson.toJson(novaCategoriaComCategoriaMae))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(3)
    void naoDeveriaCadastrarCategoriaExistenteNoBancoDeDadosComRetorno400() throws Exception {

        CategoriaRequest novaCategoriaExistente = new CategoriaRequest(
                "Telefone",
                null
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + token)
                        .content(gson.toJson(novaCategoriaExistente))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

}

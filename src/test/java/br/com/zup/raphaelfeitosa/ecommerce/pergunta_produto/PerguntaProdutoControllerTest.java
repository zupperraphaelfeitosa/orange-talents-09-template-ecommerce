package br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.Categoria;
import br.com.zup.raphaelfeitosa.ecommerce.categoria.CategoriaRequest;
import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenService;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.produto.ProdutoRequest;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.UsuarioAutenticacaoRequest;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.UsuarioRequest;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class PerguntaProdutoControllerTest {

    private final String uri = "/api/v1/produtos/";
    private String tokenUsuarioUm;
    private String tokenUsuarioDois;
    private Usuario usuarioUm;
    private Usuario usuarioDois;
    private Categoria categoria;
    private Produto produto;

    private Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {

        this.usuarioUm = new Usuario("johndoe@gmail.com", "123456");
        entityManager.persist(this.usuarioUm);

        this.usuarioDois = new Usuario("mariadoe@gmail.com", "123456");
        entityManager.persist(this.usuarioDois);

        this.categoria = new Categoria("Telefone", null);
        entityManager.persist(this.categoria);

        UsuarioAutenticacaoRequest dadosLoginUsuarioUm = new UsuarioAutenticacaoRequest("johndoe@gmail.com", "123456");
        tokenUsuarioUm = tokenService.gerarToken(authenticationManager.authenticate(dadosLoginUsuarioUm.converter()));

        UsuarioAutenticacaoRequest dadosLoginUsuarioDois = new UsuarioAutenticacaoRequest("mariadoe@gmail.com", "123456");
        tokenUsuarioDois = tokenService.gerarToken(authenticationManager.authenticate(dadosLoginUsuarioDois.converter()));

        Map<String, String> caracteristicas = new HashMap<String, String>();
        caracteristicas.put("Processador", "A70");
        caracteristicas.put("Memoria Rom", "128GB");
        caracteristicas.put("Memoria Ram", "8GB");

        this.produto = new Produto("Sangung", new BigDecimal(1000), 10, "telefone novo", caracteristicas, this.categoria, this.usuarioUm);
        entityManager.persist(this.produto);
    }

    @Test
    @Order(1)
    @Transactional
    void deveriaCadastrarUmaNovaPerguntaDeProdutoComRetorno200() throws Exception {

        PerguntaProdutoRequest novaPerguntaProduto = new PerguntaProdutoRequest("Nova pergunta para o produto");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + this.produto.getId() + "/perguntas")
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaPerguntaProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(2)
    @Transactional
    void naoDeveriaCadastrarUmaNovaPerguntaDeProdutoComCompoNuloOuInvalidoRetorno400() throws Exception {

        PerguntaProdutoRequest novaPerguntaProduto = new PerguntaProdutoRequest("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + this.produto.getId() + "/perguntas")
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaPerguntaProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(3)
    @Transactional
    void naoDeveriaCadastrarNovaPerguntaDeProdutoComIdProdutoInvalidoComRetorno404() throws Exception {

        PerguntaProdutoRequest novaPerguntaProduto = new PerguntaProdutoRequest("Nova pergunta para o produto");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 50L + "/perguntas")
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novaPerguntaProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(404));
    }

    @Test
    @Order(4)
    @Transactional
    void donoDoProdutoNaoPodeCadastrarOpniaoComRetorno403() throws Exception {

        PerguntaProdutoRequest novaPerguntaProduto = new PerguntaProdutoRequest("Nova pergunta para o produto");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + this.produto.getId() + "/perguntas")
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novaPerguntaProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(403));
    }

    @Test
    @Order(5)
    @Transactional
    void naoDeveriaCadastrarPerguntaComTokenInvalidoComRetorno403() throws Exception {

        PerguntaProdutoRequest novaPerguntaProduto = new PerguntaProdutoRequest("Nova pergunta para o produto");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + this.produto.getId() + "/perguntas")
                        .header("Authorization", "Bearer " + "token_invalido")
                        .content(gson.toJson(novaPerguntaProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(403));
    }
}

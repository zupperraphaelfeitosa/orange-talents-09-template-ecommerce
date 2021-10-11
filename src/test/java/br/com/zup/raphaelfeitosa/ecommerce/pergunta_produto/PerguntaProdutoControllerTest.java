package br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.Categoria;
import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenService;
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

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class PerguntaProdutoControllerTest {

    private final String uri = "/api/v1/produtos/";
    private String tokenUsuarioUm;
    private String tokenUsuarioDois;

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


        UsuarioRequest usuarioUm = new UsuarioRequest("johndoe@gmail.com", "123456");
        entityManager.persist(usuarioUm.toUsuario());

        UsuarioRequest usuarioDois = new UsuarioRequest("mariadoe@gmail.com", "123456");
        entityManager.persist(usuarioDois.toUsuario());

        Categoria novaCategoria = new Categoria("Telefone", null);
        entityManager.persist(novaCategoria);

        UsuarioAutenticacaoRequest dadosLoginUsuarioUm = new UsuarioAutenticacaoRequest("johndoe@gmail.com", "123456");
        tokenUsuarioUm = tokenService.gerarToken(authenticationManager.authenticate(dadosLoginUsuarioUm.converter()));

        UsuarioAutenticacaoRequest dadosLoginUsuarioDois = new UsuarioAutenticacaoRequest("mariadoe@gmail.com", "123456");
        tokenUsuarioDois = tokenService.gerarToken(authenticationManager.authenticate(dadosLoginUsuarioDois.converter()));

        String novoProduto = "{\n" +
                "    \"nome\": \"samgung a30\",\n" +
                "    \"valor\": 1000.00,\n" +
                "    \"quantidade\": 10,\n" +
                "    \"descricao\": \"telefone novo com garantia de 1 ano, processador A8, memoria 8GB, 64GB armazenamento\",\n" +
                "    \"caracteristicas\": {\n" +
                "            \"Processador\": \"A60\",\n" +
                "            \"Memoria Ram\": \"64GB\",\n" +
                "            \"Memoria Rom\": \"8GB\"\n" +
                "    },\n" +
                "    \"idCategoria\": 1\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .header("Authorization", "Bearer " + tokenUsuarioUm)
                .content(novoProduto)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(1)
    @DisplayName("200 - Cadastro de uma nova pergunta para o produto com envio de email")
    void deveriaCadastrarUmaNovaPerguntaDeProdutoComRetorno200() throws Exception {

        PerguntaProdutoRequest novaPerguntaProduto = new PerguntaProdutoRequest("Nova pergunta para o produto");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 1L + "/perguntas")
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaPerguntaProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(2)
    @DisplayName("400 - Erro campo nulo ou invalido")
    void naoDeveriaCadastrarUmaNovaPerguntaDeProdutoComCompoNuloOuInvalidoRetorno400() throws Exception {

        PerguntaProdutoRequest novaPerguntaProduto = new PerguntaProdutoRequest("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 1L + "/perguntas")
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaPerguntaProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(3)
    @DisplayName("404 - Error id do produto na url invalido")
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
    @DisplayName("403 - Usuário dono do produto nao pode realizar pergunta no proprio produto")
    void donoDoProdutoNaoPodeCadastrarOpniaoComRetorno403() throws Exception {

        PerguntaProdutoRequest novaPerguntaProduto = new PerguntaProdutoRequest("Nova pergunta para o produto");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 1L + "/perguntas")
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novaPerguntaProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(403));
    }

    @Test
    @Order(5)
    @DisplayName("403 - Erro forbidden token invalido")
    void naoDeveriaCadastrarPerguntaComTokenInvalidoComRetorno403() throws Exception {

        PerguntaProdutoRequest novaPerguntaProduto = new PerguntaProdutoRequest("Nova pergunta para o produto");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 1L + "/perguntas")
                        .header("Authorization", "Bearer " + "token_invalido")
                        .content(gson.toJson(novaPerguntaProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(403));
    }
}

package br.com.zup.raphaelfeitosa.ecommerce.produto;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.CategoriaRequest;
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
public class ProdutoControllerTest {

    private final String uri = "/api/v1/produtos";

    Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PersistenceContext
    EntityManager entityManager;

    private String token;

    @BeforeEach
    void setUp() {
        UsuarioRequest novoUsuario = new UsuarioRequest("johndoe@gmail.com", "123456");
        entityManager.persist(novoUsuario.toUsuario());

        CategoriaRequest novaCategoria = new CategoriaRequest("Telefone", null);
        entityManager.persist(novaCategoria.toCategoria(entityManager));

        UsuarioAutenticacaoRequest dadosLogin = new UsuarioAutenticacaoRequest("johndoe@gmail.com", "123456");
        token = tokenService.gerarToken(authenticationManager.authenticate(dadosLogin.converter()));
    }

    @Test
    @Order(1)
    @DisplayName("200 - Cadastro de um novo produto")
    void deveriaCadastrarUmNovoProdutoComRetorno200() throws Exception {
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
                        .header("Authorization", "Bearer " + token)
                        .content(novoProduto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(2)
    @DisplayName("400 - Erro Campo nulo ou vazio")
    void naoDeveriaCadastrarUmNovoProdutoErroCampoNuloOuVazioComRetorno400() throws Exception {
        String novoProduto = "{\n" +
                "    \"nome\": \"\",\n" +
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
                        .header("Authorization", "Bearer " + token)
                        .content(novoProduto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(3)
    @DisplayName("400 - Erro Necessita de 3 caractetiristicas")
    void naoDeveriaCadastrarUmNovoProdutoErroNoMinimoTresCaracteristicasRetorno400() throws Exception {
        String novoProduto = "{\n" +
                "    \"nome\": \"samgung a30\",\n" +
                "    \"valor\": 1000.00,\n" +
                "    \"quantidade\": 10,\n" +
                "    \"descricao\": \"telefone novo com garantia de 1 ano, processador A8, memoria 8GB, 64GB armazenamento\",\n" +
                "    \"caracteristicas\": {\n" +
                "            \"Processador\": \"A60\",\n" +
                "            \"Memoria Ram\": \"64GB\",\n" +
                "    },\n" +
                "    \"idCategoria\": 1\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + token)
                        .content(novoProduto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }
    @Test
    @Order(4)
    @DisplayName("400 - Categoria inexistente")
    void naoDeveriaCadastrarUmNovoProdutoComCategoriaInexistenteRetorno400() throws Exception {
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
                "    \"idCategoria\": 10\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + token)
                        .content(novoProduto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(5)
    @DisplayName("403 - Acesso Negado")
    void naoDeveriaCadastrarUmNovoProdutoErroAcessoNegadoComRetorno403() throws Exception {
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
                        .header("Authorization", "Bearer ")
                        .content(novoProduto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(403));
    }
}

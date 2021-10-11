package br.com.zup.raphaelfeitosa.ecommerce.opiniao_produto;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.Categoria;
import br.com.zup.raphaelfeitosa.ecommerce.categoria.CategoriaRequest;
import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenService;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.UsuarioAutenticacaoRequest;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.UsuarioRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class OpiniaoProdutoControllerTest {

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
    @DisplayName("200 - Cadastro de uma nova opiniao")
    void deveriaCadastrarUmaNovaOpiniaoDeProdutoComRetorno200() throws Exception {

        OpiniaoProdutoRequest novaOpiniaoProduto = new OpiniaoProdutoRequest(
                4, "Espetacular", "Produto Excelente");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 1L + "/opiniao")
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaOpiniaoProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(2)
    @DisplayName("400 - Erro campo nulo ou invalido")
    void naoDeveriaCadastrarUmaNovaOpiniaoDeProdutoComCompoNuloOuInvalidoRetorno400() throws Exception {

        OpiniaoProdutoRequest novaOpiniaoProduto = new OpiniaoProdutoRequest(
                4, "", "Produto Excelente");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 1L + "/opiniao")
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaOpiniaoProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(3)
    @DisplayName("404 - Error id do produto na url invalido")
    void naoDeveriaCadastrarNovaOpiniaoDeProdutoComIdProdutoInvalidoComRetorno404() throws Exception {

        OpiniaoProdutoRequest novaOpiniaoProduto = new OpiniaoProdutoRequest(
                4, "Espetacular", "Produto Excelente");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 50L + "/opiniao")
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novaOpiniaoProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(404));
    }

    @Test
    @Order(4)
    @DisplayName("403 - Usuário dono do produto nao pode da opiniao no proprio produto")
    void donoDoProdutoNaoPodeCadastrarOpniaoComRetorno403() throws Exception {

        OpiniaoProdutoRequest novaOpiniaoProduto = new OpiniaoProdutoRequest(
                4, "Espetacular", "Produto Excelente");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 1L + "/opiniao")
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novaOpiniaoProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(403));
    }
}

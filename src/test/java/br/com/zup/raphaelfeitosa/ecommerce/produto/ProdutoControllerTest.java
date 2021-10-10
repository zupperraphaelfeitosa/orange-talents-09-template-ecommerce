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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class ProdutoControllerTest {

    private final String uri = "/api/v1/produtos";
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

        UsuarioRequest usuarioUm = new UsuarioRequest("johndoe@gmail.com", "123456");
        entityManager.persist(usuarioUm.toUsuario());

        UsuarioRequest usuarioDois = new UsuarioRequest("mariadoe@gmail.com", "123456");
        entityManager.persist(usuarioDois.toUsuario());

        CategoriaRequest novaCategoria = new CategoriaRequest("Telefone", null);
        entityManager.persist(novaCategoria.toCategoria(entityManager));

        UsuarioAutenticacaoRequest dadosLoginUsuarioUm = new UsuarioAutenticacaoRequest("johndoe@gmail.com", "123456");
        tokenUsuarioUm = tokenService.gerarToken(authenticationManager.authenticate(dadosLoginUsuarioUm.converter()));

        UsuarioAutenticacaoRequest dadosLoginUsuarioDois = new UsuarioAutenticacaoRequest("mariadoe@gmail.com", "123456");
        tokenUsuarioDois = tokenService.gerarToken(authenticationManager.authenticate(dadosLoginUsuarioDois.converter()));

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .header("Authorization", "Bearer " + tokenUsuarioUm)
                .content(novoProduto)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(1)
    @DisplayName("200 - Cadastro de um novo produto")
    void deveriaCadastrarUmNovoProdutoComRetorno200() throws Exception {
        String novoProduto = "{\n" +
                "    \"nome\": \"samgung a70\",\n" +
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
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
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
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
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
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
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

    @Test
    @Order(6)
    @DisplayName("200 - Upload de Imagens")
    void deveridaEnviarImagensDoProdutoDoUsuarioLogadoComRetorno200() throws Exception {

        String uriImagem = "/api/v1/produtos/" + 1L + "/imagens";

        MockMultipartFile imagem = new MockMultipartFile("imagens", getClass().getResourceAsStream("imagem.png"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(uriImagem)
                        .file(imagem)
                        .header("Authorization", "Bearer " + tokenUsuarioUm))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @Order(7)
    @DisplayName("400 - Erro - necessita enviar ao menos uma imagem")
    void naoDeveridaEnviarImagensSemImagensDoProdutoDoUsuarioLogadoComRetorno400() throws Exception {

        String uriImagem = "/api/v1/produtos/" + 1L + "/imagens";

        MockMultipartFile imagem = new MockMultipartFile("{ }", getClass().getResourceAsStream("{ }"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(uriImagem)
                        .file(imagem)
                        .header("Authorization", "Bearer " + tokenUsuarioUm))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @Order(8)
    @DisplayName("403 - Não envia imagem de outro usuario logado")
    void naoDeveridaEnviarImagensDoProdutoDoUsuarioLogadoComRetorno403() throws Exception {

        String uriImagem = "/api/v1/produtos/" + 1L + "/imagens";

        MockMultipartFile imagem = new MockMultipartFile("imagens", getClass().getResourceAsStream("imagem.png"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(uriImagem)
                        .file(imagem)
                        .header("Authorization", "Bearer " + tokenUsuarioDois))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @Order(9)
    @DisplayName("403 - Token invalido")
    void naoDeveridaEnviarImagensDoProdutoDoUsuarioLogadoComTokenInvalidoComRetorno403() throws Exception {

        String uriImagem = "/api/v1/produtos/" + 1L + "/imagens";

        MockMultipartFile imagem = new MockMultipartFile("imagens", getClass().getResourceAsStream("imagem.png"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(uriImagem)
                        .file(imagem)
                        .header("Authorization", "Bearer " + "token_invalido"))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }
}

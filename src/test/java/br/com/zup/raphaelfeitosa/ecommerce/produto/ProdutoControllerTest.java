package br.com.zup.raphaelfeitosa.ecommerce.produto;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.Categoria;
import br.com.zup.raphaelfeitosa.ecommerce.categoria.CategoriaRequest;
import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenService;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class ProdutoControllerTest {

    private final String uri = "/api/v1/produtos";
    private String tokenUsuarioUm;
    private String tokenUsuarioDois;
    private Categoria categoria;
    private Produto produto;
    private Usuario usuarioUm;

    private Map<String, String> caracteristicas = new HashMap<String, String>();

    private Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {

        UsuarioRequest usuarioUm = new UsuarioRequest("johndoe@gmail.com", "123456");
        this.usuarioUm = usuarioUm.toUsuario();
        entityManager.persist(this.usuarioUm);

        UsuarioRequest usuarioDois = new UsuarioRequest("mariadoe@gmail.com", "123456");
        entityManager.persist(usuarioDois.toUsuario());

        CategoriaRequest novaCategoria = new CategoriaRequest("Telefone", null);
        this.categoria = novaCategoria.toCategoria(entityManager);
        entityManager.persist(categoria);

        UsuarioAutenticacaoRequest dadosLoginUsuarioUm = new UsuarioAutenticacaoRequest("johndoe@gmail.com", "123456");
        tokenUsuarioUm = tokenService.gerarToken(authenticationManager.authenticate(dadosLoginUsuarioUm.converter()));

        UsuarioAutenticacaoRequest dadosLoginUsuarioDois = new UsuarioAutenticacaoRequest("mariadoe@gmail.com", "123456");
        tokenUsuarioDois = tokenService.gerarToken(authenticationManager.authenticate(dadosLoginUsuarioDois.converter()));

        caracteristicas.put("Processador", "A70");
        caracteristicas.put("Memoria Rom", "128GB");
        caracteristicas.put("Memoria Ram", "8GB");

        ProdutoRequest novoProduto = new ProdutoRequest("Sangung", new BigDecimal(1000),
                10, "telefone novo", caracteristicas, this.categoria.getId());

        this.produto = novoProduto.toProduto(entityManager, this.usuarioUm);
        entityManager.persist(this.produto);

    }

    @Test
    @Order(1)
    void deveriaCadastrarUmNovoProdutoComRetorno200() throws Exception {

        ProdutoRequest novoProduto = new ProdutoRequest("Sangung A70", new BigDecimal(1000),
                10, "telefone novo", caracteristicas, 1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novoProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(2)
    void naoDeveriaCadastrarUmNovoProdutoErroCampoNuloOuVazioComRetorno400() throws Exception {

        ProdutoRequest novoProduto = new ProdutoRequest("", new BigDecimal(1000),
                10, "telefone novo", caracteristicas, 1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novoProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(3)
    void naoDeveriaCadastrarUmNovoProdutoErroNoMinimoTresCaracteristicasRetorno400() throws Exception {

        Map<String, String> necessitaTresCaracteristicas = new HashMap<>();
        necessitaTresCaracteristicas.put("Processador", "A70");
        necessitaTresCaracteristicas.put("Memoria Rom", "128GB");

        ProdutoRequest novoProduto = new ProdutoRequest("Sangung A70", new BigDecimal(1000),
                10, "telefone novo", necessitaTresCaracteristicas, 1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novoProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(4)
    void naoDeveriaCadastrarUmNovoProdutoComCategoriaInexistenteRetorno400() throws Exception {

        ProdutoRequest novoProduto = new ProdutoRequest("Sangung A70", new BigDecimal(1000),
                10, "telefone novo", caracteristicas, 10L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novoProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(5)
    void naoDeveriaCadastrarUmNovoProdutoErroAcessoNegadoComRetorno403() throws Exception {
        ProdutoRequest novoProduto = new ProdutoRequest("Sangung A70", new BigDecimal(1000),
                10, "telefone novo", caracteristicas, 1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + "token_invalido")
                        .content(gson.toJson(novoProduto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(403));
    }

    @Test
    @Order(6)
    void deveridaEnviarImagensDoProdutoDoUsuarioLogadoComRetorno200() throws Exception {

        String uriImagem = "/api/v1/produtos/" + this.produto.getId() + "/imagens";

        MockMultipartFile imagem = new MockMultipartFile("imagens", getClass().getResourceAsStream("imagem.png"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(uriImagem)
                        .file(imagem)
                        .header("Authorization", "Bearer " + tokenUsuarioUm))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @Order(7)
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
    void naoDeveridaEnviarImagensDoProdutoDoUsuarioLogadoComRetorno403() throws Exception {

        String uriImagem = "/api/v1/produtos/" + this.produto.getId() + "/imagens";

        MockMultipartFile imagem = new MockMultipartFile("imagens", getClass().getResourceAsStream("imagem.png"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(uriImagem)
                        .file(imagem)
                        .header("Authorization", "Bearer " + tokenUsuarioDois))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @Order(9)
    void naoDeveridaEnviarImagensDoProdutoDoUsuarioLogadoComTokenInvalidoComRetorno403() throws Exception {

        String uriImagem = "/api/v1/produtos/" + 1L + "/imagens";

        MockMultipartFile imagem = new MockMultipartFile("imagens", getClass().getResourceAsStream("imagem.png"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(uriImagem)
                        .file(imagem)
                        .header("Authorization", "Bearer " + "token_invalido"))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }
}

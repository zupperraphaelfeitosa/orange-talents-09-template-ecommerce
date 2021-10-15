package br.com.zup.raphaelfeitosa.ecommerce.compra_produto;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.Categoria;
import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.enums.Gateway;
import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenService;
import br.com.zup.raphaelfeitosa.ecommerce.imagem_produto.ImagemProduto;
import br.com.zup.raphaelfeitosa.ecommerce.opiniao_produto.OpiniaoProduto;
import br.com.zup.raphaelfeitosa.ecommerce.opiniao_produto.OpiniaoProdutoRequest;
import br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto.PerguntaProduto;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class CompraProdutoControllerTest {

    private final String uri = "/api/v1/compras";
    private String tokenUsuarioUm;
    private String tokenUsuarioDois;
    private Usuario usuarioUm;
    private Usuario usuarioDois;
    private Categoria categoria;
    private Produto produto;
    private OpiniaoProduto opiniaoProduto;
    private PerguntaProduto perguntaProduto;
    private ImagemProduto imagemProduto;

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

        this.produto = new Produto("Samgung", new BigDecimal(1000), 10, "telefone novo", caracteristicas, this.categoria, this.usuarioUm);
        entityManager.persist(this.produto);

        this.imagemProduto = new ImagemProduto(this.produto, "https://https://bucket.io/imagem.jpg");
        entityManager.persist(this.imagemProduto);

        System.out.println(this.imagemProduto);
        this.opiniaoProduto = new OpiniaoProduto(4, "Espetacular", "O produto é excelente", this.produto, this.usuarioUm);
        entityManager.persist(this.opiniaoProduto);

        this.perguntaProduto = new PerguntaProduto("O produto é novo?", this.produto, this.usuarioUm);
        entityManager.persist(this.perguntaProduto);
    }

    @Test
    @Order(1)
    void deveriaRealizarCompraProdutoComSucessoComRetorno302() throws Exception {

        CompraProdutoRequest novaCompra = new CompraProdutoRequest(2, this.produto.getId(), Gateway.paypal);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaCompra))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(302));
    }

    @Test
    @Order(2)
    void naoDeveriaRealizarUmaNocaCompraComCompoNuloOuInvalidoComRetorno400() throws Exception {

        CompraProdutoRequest novaCompraComErro = new CompraProdutoRequest(null, this.produto.getId(), Gateway.paypal);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaCompraComErro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(3)
    void naoDeveriaRealizarUmaNocaCompraComQuantidadeProdutoMenorQueZeroComRetorno400() throws Exception {

        CompraProdutoRequest novaCompraComErro = new CompraProdutoRequest(-1, this.produto.getId(), Gateway.paypal);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaCompraComErro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(4)
    void naoDeveriaRealizarUmaNocaCompraComQuantidadeProdutoMaiorQueQuantidadeEstoqueComRetorno400() throws Exception {

        CompraProdutoRequest novaCompraComErro = new CompraProdutoRequest(20, this.produto.getId(), Gateway.paypal);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaCompraComErro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(5)
    void naoDeveriaRealizarUmaNocaCompraComProdutoInexistenteComRetorno400() throws Exception {

        CompraProdutoRequest novaCompraComErro = new CompraProdutoRequest(3, 70L, Gateway.paypal);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioDois)
                        .content(gson.toJson(novaCompraComErro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(6)
    void donoDoProdutoNaoPodeRealizarCompraDoProprioProdutoComRetorno403() throws Exception {

        CompraProdutoRequest novaCompraComErro = new CompraProdutoRequest(3, this.produto.getId(), Gateway.paypal);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .header("Authorization", "Bearer " + tokenUsuarioUm)
                        .content(gson.toJson(novaCompraComErro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(403));
    }
}

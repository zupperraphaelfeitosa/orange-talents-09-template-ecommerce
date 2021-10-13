package br.com.zup.raphaelfeitosa.ecommerce.detalhe_produto;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.Categoria;
import br.com.zup.raphaelfeitosa.ecommerce.categoria.CategoriaRequest;
import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenService;
import br.com.zup.raphaelfeitosa.ecommerce.imagem_produto.ImagemProduto;
import br.com.zup.raphaelfeitosa.ecommerce.opiniao_produto.OpiniaoProduto;
import br.com.zup.raphaelfeitosa.ecommerce.opiniao_produto.OpiniaoProdutoRequest;
import br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto.PerguntaProduto;
import br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto.PerguntaProdutoRequest;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.produto.ProdutoRequest;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.UsuarioAutenticacaoRequest;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.UsuarioRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
public class DetalheProdutoControllerTest {

    private final String uri = "/api/v1/produtos/";
    private Usuario usuario;
    private Categoria categoria;
    private Produto produto;
    private OpiniaoProduto opiniaoProduto;
    private PerguntaProduto perguntaProduto;
    private ImagemProduto imagemProduto;

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {

        this.usuario = new Usuario("johndoe@gmail.com", "123456");
        entityManager.persist(this.usuario);

        this.categoria = new Categoria("Telefone", null);
        entityManager.persist(this.categoria);

        Map<String, String> caracteristicas = new HashMap<String, String>();
        caracteristicas.put("Processador", "A70");
        caracteristicas.put("Memoria Rom", "128GB");
        caracteristicas.put("Memoria Ram", "8GB");

        this.produto = new Produto("Sangung", new BigDecimal(1000), 10, "telefone novo", caracteristicas, this.categoria, this.usuario);
        entityManager.persist(this.produto);

        this.imagemProduto = new ImagemProduto(this.produto, "https://https://bucket.io/imagem.jpg");
        entityManager.persist(this.imagemProduto);

        System.out.println(this.imagemProduto);
        this.opiniaoProduto = new OpiniaoProduto(4, "Espetacular", "O produto é excelente", this.produto, this.usuario);
        entityManager.persist(this.opiniaoProduto);

        this.perguntaProduto = new PerguntaProduto("O produto é novo?", this.produto, this.usuario);
        entityManager.persist(this.perguntaProduto);

    }

    @Test
    @Order(1)
    void deveriaRetornarDetalheDoProdutoComRetorno200() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(uri + this.produto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();

        String resultado = mvcResult.getResponse().getContentAsString();
        System.out.println(resultado);
    }

    @Test
    @Order(2)
    void naoDeveriaRetornarDetalheDoProdutoComIdInexistenteRetorno404() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(uri + 50L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(404))
                .andReturn();

        Assertions.assertTrue(mvcResult.getResponse().getErrorMessage().contains("Produto não existe"));
    }
}

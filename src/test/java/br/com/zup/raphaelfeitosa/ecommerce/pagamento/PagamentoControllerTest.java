package br.com.zup.raphaelfeitosa.ecommerce.pagamento;

import br.com.zup.raphaelfeitosa.ecommerce.categoria.Categoria;
import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.CompraProduto;
import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.enums.Gateway;
import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenService;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.enums.StatusPagamento;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.feign.ServicoGeradorNotaFiscal;
import br.com.zup.raphaelfeitosa.ecommerce.pagamento.feign.ServicoRankingVendedor;
import br.com.zup.raphaelfeitosa.ecommerce.produto.Produto;
import br.com.zup.raphaelfeitosa.ecommerce.usuario.Usuario;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@ActiveProfiles("test")
@Transactional
public class PagamentoControllerTest {

    private final String uri = "/api/v1/pagamentos";

    private Usuario donoProduto;
    private Usuario compradorProduto;

    private Categoria categoria;
    private Produto produto;
    private CompraProduto compra;

    private Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicoGeradorNotaFiscal servicoGeradorNotaFiscal;

    @MockBean
    private ServicoRankingVendedor servicoRankingVendedor;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {

        this.donoProduto = new Usuario("johndoe@gmail.com", "123456");
        this.entityManager.persist(this.donoProduto);

        this.compradorProduto = new Usuario("mariadoe@gmail.com", "123456");
        this.entityManager.persist(this.compradorProduto);

        this.categoria = new Categoria("Telefone", null);
        this.entityManager.persist(this.categoria);

        Map<String, String> caracteristicas = new HashMap<String, String>();
        caracteristicas.put("Processador", "A70");
        caracteristicas.put("Memoria Rom", "128GB");
        caracteristicas.put("Memoria Ram", "8GB");

        this.produto = new Produto("Samsung", new BigDecimal(1000), 10, "telefone novo", caracteristicas, this.categoria, this.donoProduto);
        this.entityManager.persist(this.produto);
    }

    @Test
    @Order(1)
    void deveriaRealizarPagamentoProdutoComPagSeguroEStatusPagamentoComSUCESSOComRetorno200() throws Exception {

        this.compra = new CompraProduto(2, Gateway.pagseguro, this.produto, this.compradorProduto);
        this.entityManager.persist(this.compra);

        Mockito.when(servicoGeradorNotaFiscal.envia(this.compra.getUsuario().getId(), this.compradorProduto.getId()))
                .thenReturn(ResponseEntity.ok().build());
        Mockito.when(servicoRankingVendedor.envia(this.compra.getUsuario().getId(), this.compradorProduto.getId()))
                .thenReturn(ResponseEntity.ok().build());

        PagSeguroRequest retornoPagamentoPagSeguro = new PagSeguroRequest(this.compra.getId(), 1L, "SUCESSO");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/pagseguro")
                        .content(gson.toJson(retornoPagamentoPagSeguro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(2)
    void naoDeveriaRealizarPagamentoProdutoComPagSeguroEStatusPagamentoComErroComRetorno200() throws Exception {

        this.compra = new CompraProduto(2, Gateway.pagseguro, this.produto, this.compradorProduto);
        this.entityManager.persist(this.compra);

        PagSeguroRequest retornoPagamentoPagSeguro = new PagSeguroRequest(this.compra.getId(), 1L, "ERRO");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/pagseguro")
                        .content(gson.toJson(retornoPagamentoPagSeguro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(3)
    void nãoDeveriaRealizarPagamentoPagSeguroComProdutoInvalidoComRetorno400() throws Exception {

        PagSeguroRequest retornoPagamentoPagSeguro = new PagSeguroRequest(15L, 1L, "SUCESSO");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/pagseguro")
                        .content(gson.toJson(retornoPagamentoPagSeguro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(4)
    void nãoDeveriaRealizarPagamentoPagSeguroComCampoNuloOuInvalidoComRetorno400() throws Exception {

        PagSeguroRequest retornoPagamentoPagSeguro = new PagSeguroRequest(null, 1L, "");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/pagseguro")
                        .content(gson.toJson(retornoPagamentoPagSeguro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(6)
    void naoDeveriaRealizarPagamentoJaProcessadoProdutoComPagSeguroRetorno400() throws Exception {

        this.compra = new CompraProduto(2, Gateway.pagseguro, this.produto, this.compradorProduto);
        this.entityManager.persist(this.compra);

        this.entityManager.persist(new Pagamento(2L, StatusPagamento.SUCESSO, this.compra));

        PayPalRequest retornoPagamentoPagSeguro = new PayPalRequest(this.compra.getId(), 2L, 0);

         mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/pagseguro")
                        .content(gson.toJson(retornoPagamentoPagSeguro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(7)
    void deveriaRealizarPagamentoProdutoComPayPalComStatusPagamentoComSucessoComRetorno200() throws Exception {

        this.compra = new CompraProduto(2, Gateway.paypal, this.produto, this.compradorProduto);
        this.entityManager.persist(this.compra);

        PayPalRequest retornoPagamentoPayPal = new PayPalRequest(this.compra.getId(), 1L, 0);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/paypal")
                        .content(gson.toJson(retornoPagamentoPayPal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(8)
    void naoDeveriaRealizarPagamentoProdutoComPayPalEStatusPagamentoComErroComRetorno200() throws Exception {

        this.compra = new CompraProduto(2, Gateway.paypal, this.produto, this.compradorProduto);
        this.entityManager.persist(this.compra);

        PayPalRequest retornoPagamentoPayPal = new PayPalRequest(this.compra.getId(), 1L, 1);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/paypal")
                        .content(gson.toJson(retornoPagamentoPayPal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(9)
    void nãoDeveriaRealizarPagamentoPayPalComProdutoInvalidoComRetorno400() throws Exception {

        PayPalRequest retornoPagamentoPayPal = new PayPalRequest(15L, 1L, 1);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/paypal")
                        .content(gson.toJson(retornoPagamentoPayPal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));

    }

    @Test
    @Order(10)
    void nãoDeveriaRealizarPagamentoPayPalComCampoNuloOuInvalidoComRetorno400() throws Exception {

        PayPalRequest retornoPagamentoPayPal = new PayPalRequest(null, null, null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/paypal")
                        .content(gson.toJson(retornoPagamentoPayPal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(11)
    void naoDeveriaRealizarPagamentoJaProcessadoProdutoComPayPalComRetorno400() throws Exception {

        this.compra = new CompraProduto(2, Gateway.paypal, this.produto, this.compradorProduto);
        this.entityManager.persist(this.compra);

        this.entityManager.persist(new Pagamento(2L, StatusPagamento.SUCESSO, this.compra));

        PayPalRequest retornoPagamentoPagSeguro = new PayPalRequest(this.compra.getId(), 2L, 0);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "/paypal")
                        .content(gson.toJson(retornoPagamentoPagSeguro))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }
}

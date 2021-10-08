package br.com.zup.raphaelfeitosa.ecommerce.usuario;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class UsuarioAutenticacaoControllerTest {

    private final String uri = "/api/v1/autenticacao";

    Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Order(1)
    @DisplayName("200 - usuario autenticado com sucesso")
    @Transactional
    void deveriaRealizarLoginComSucessoRetorno200() throws Exception {

        UsuarioRequest loginUsuario = new UsuarioRequest("johndoe@gmail.com", "123456");
        entityManager.persist(loginUsuario.toUsuario());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(gson.toJson(loginUsuario))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200)).andReturn();

        Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("Bearer"));

    }

    @Test
    @Order(2)
    @DisplayName("400 - Error email invalido")
    @Transactional
    void deveriaRetornarErrorDeEmailInvalidoComRetorno400() throws Exception {

        UsuarioRequest emailInvalido = new UsuarioRequest("johndoegmail.com", "123456");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(gson.toJson(emailInvalido))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(3)
    @DisplayName("400 - Error email ou senha em vazio ou nulo")
    @Transactional
    void deveriaRetornarErrorDeEmailOuSenhaVazioOuNuloComRetorno400() throws Exception {

        UsuarioRequest emailOuSenhaVazioOuNulo = new UsuarioRequest("", null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(gson.toJson(emailOuSenhaVazioOuNulo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }
}

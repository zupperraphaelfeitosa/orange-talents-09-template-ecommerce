package br.com.zup.raphaelfeitosa.ecommerce.usuario;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class UsuarioControllerTest {

    private final String uri = "/api/v1/usuarios";

    Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("200 - Cadastro de um novo usuario")
    void deveriaCadastrarUmNonoUsuarioComRetorno200() throws Exception {

        UsuarioRequest novoUsuario = new UsuarioRequest(
                "john@gmail.com",
                "123456"
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(gson.toJson(novoUsuario))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(2)
    @DisplayName("400 - Erro email invalido")
    void deveriaDaErroDeEmailInvalidoComRetorno400() throws Exception {

        UsuarioRequest emailInvalido = new UsuarioRequest(
                "emailinvalido.gmail.com",
                "123456"
        );

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
    @DisplayName("400 - Erro senha com menos de 6 caracteres")
    void deveriaDaErroDeSenhaComMenosDeSeisCaracteresComRetorno400() throws Exception {

        UsuarioRequest senhaMenorQueSeisCaracteres = new UsuarioRequest(
                "maria@gmail.com",
                "12345"
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(gson.toJson(senhaMenorQueSeisCaracteres))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

    @Test
    @Order(4)
    @DisplayName("400 - Erro email já cadastrado no banco de dados")
    void deveriaDaErroDeEmailJaCadastradoComRetorno400() throws Exception {
        UsuarioRequest emailJaExistente = new UsuarioRequest(
                "john@gmail.com",
                "123456"
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(gson.toJson(emailJaExistente))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }
}

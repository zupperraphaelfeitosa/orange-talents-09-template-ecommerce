package br.com.zup.raphaelfeitosa.ecommerce.categoria;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class CategoriaControllerTest {

    private final String uri = "/api/v1/categorias";

    Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("200 - Cadastro de Categoria sem Categoria Mae")
    void deveriaCadastrarCategoriaSemCategoriaMaeComRetorno200() throws Exception {

        CategoriaRequest novaCategoriaSemCategoriaMae = new CategoriaRequest(
                "Telefone",
                null
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(gson.toJson(novaCategoriaSemCategoriaMae))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(2)
    @DisplayName("200 - Cadastro de Categoria com Categoria Mae")
    void deveriaCadastrarCategoriaComCategoriaMaeComRetorno200() throws Exception {

        CategoriaRequest novaCategoriaComCategoriaMae = new CategoriaRequest(
                "Smartphone",
                1L
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(gson.toJson(novaCategoriaComCategoriaMae))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(3)
    @DisplayName("400 - Error cadastro de categoria existente")
    void naoDeveriaCadastrarCategoriaExistenteNoBancoDeDadosComRetorno400() throws Exception {

        CategoriaRequest novaCategoriaExistente = new CategoriaRequest(
                "Telefone",
                null
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(gson.toJson(novaCategoriaExistente))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400));
    }

}

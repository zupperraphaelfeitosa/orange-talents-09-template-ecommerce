package br.com.zup.raphaelfeitosa.ecommerce.categoria;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class CategoriaControllerTest {

    private final String uri = "/api/v1/categorias";
    private Categoria categoria;

    Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        this.categoria = new Categoria("Telefone", null);
        entityManager.persist(this.categoria);
    }

    @Test
    @Order(1)
    void deveriaCadastrarCategoriaSemCategoriaMaeComRetorno200() throws Exception {

        CategoriaRequest novaCategoriaSemCategoriaMae = new CategoriaRequest(
                "Apple",
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
    void deveriaCadastrarCategoriaComCategoriaMaeComRetorno200() throws Exception {

        CategoriaRequest novaCategoriaComCategoriaMae = new CategoriaRequest(
                "Smartphone",
                this.categoria.getId()
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

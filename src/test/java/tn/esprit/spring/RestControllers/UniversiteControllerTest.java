package tn.esprit.spring.RestControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.RestControllers.UniversiteRestController;
import tn.esprit.spring.Services.Universite.IUniversiteService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = UniversiteRestController.class)
public class UniversiteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUniversiteService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddOrUpdate() throws Exception {
        Universite universite = Universite.builder().idUniversite(1L).nomUniversite("ESPRIT").build();

        when(service.addOrUpdate(Mockito.any(Universite.class))).thenReturn(universite);

        mockMvc.perform(post("/universite/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("ESPRIT"));
    }

    @Test
    void testFindAll() throws Exception {
        List<Universite> list = Collections.singletonList(Universite.builder().idUniversite(1L).nomUniversite("ESPRIT").build());

        when(service.findAll()).thenReturn(list);

        mockMvc.perform(get("/universite/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUniversite").value("ESPRIT"));
    }

    @Test
    void testFindById() throws Exception {
        Universite universite = Universite.builder().idUniversite(1L).nomUniversite("ESPRIT").build();

        when(service.findById(1L)).thenReturn(universite);

        mockMvc.perform(get("/universite/findById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("ESPRIT"));
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/universite/deleteById")
                        .param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        Universite universite = Universite.builder().idUniversite(1L).nomUniversite("ESPRIT").build();

        mockMvc.perform(delete("/universite/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isOk());
    }

    @Test
    void testAjouterUniversiteEtSonFoyer() throws Exception {
        Universite universite = Universite.builder().idUniversite(1L).nomUniversite("ESPRIT").build();

        when(service.ajouterUniversiteEtSonFoyer(Mockito.any(Universite.class))).thenReturn(universite);

        mockMvc.perform(post("/universite/ajouterUniversiteEtSonFoyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("ESPRIT"));
    }

}

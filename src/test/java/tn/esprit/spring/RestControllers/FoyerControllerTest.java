package tn.esprit.spring.RestControllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.Services.Foyer.IFoyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoyerRestController.class)
public class FoyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFoyerService foyerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Foyer foyer;
    private Universite universite;

    @BeforeEach
    void setUp() {
        foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer Test");
        foyer.setCapaciteFoyer(100);

        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Université Test");
    }

    @Test
    void testAddOrUpdate() throws Exception {
        when(foyerService.addOrUpdate(any(Foyer.class))).thenReturn(foyer);

        mockMvc.perform(post("/foyer/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foyer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(1L))
                .andExpect(jsonPath("$.nomFoyer").value("Foyer Test"))
                .andExpect(jsonPath("$.capaciteFoyer").value(100));
    }

    @Test
    void testFindAll() throws Exception {
        when(foyerService.findAll()).thenReturn(List.of(foyer));

        mockMvc.perform(get("/foyer/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idFoyer").value(1L));
    }

    @Test
    void testFindById() throws Exception {
        when(foyerService.findById(1L)).thenReturn(foyer);

        mockMvc.perform(get("/foyer/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomFoyer").value("Foyer Test"));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(foyerService).delete(any(Foyer.class));

        mockMvc.perform(delete("/foyer/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foyer)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(foyerService).deleteById(1L);

        mockMvc.perform(delete("/foyer/deleteById").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testAffecterFoyerAUniversiteWithParams() throws Exception {
        when(foyerService.affecterFoyerAUniversite(1L, "Université Test")).thenReturn(universite);

        mockMvc.perform(put("/foyer/affecterFoyerAUniversite")
                        .param("idFoyer", "1")
                        .param("nomUniversite", "Université Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUniversite").value(1L))
                .andExpect(jsonPath("$.nomUniversite").value("Université Test"));
    }

    @Test
    void testDesaffecterFoyerAUniversite() throws Exception {
        when(foyerService.desaffecterFoyerAUniversite(1L)).thenReturn(universite);

        mockMvc.perform(put("/foyer/desaffecterFoyerAUniversite").param("idUniversite", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUniversite").value(1L));
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() throws Exception {
        when(foyerService.ajouterFoyerEtAffecterAUniversite(any(Foyer.class), eq(1L))).thenReturn(foyer);

        mockMvc.perform(post("/foyer/ajouterFoyerEtAffecterAUniversite")
                        .param("idUniversite", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foyer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(1L));
    }

    @Test
    void testAffecterFoyerAUniversiteWithPathVariables() throws Exception {
        when(foyerService.affecterFoyerAUniversite(1L, 1L)).thenReturn(universite);

        mockMvc.perform(put("/foyer/affecterFoyerAUniversite/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUniversite").value(1L));
    }
}

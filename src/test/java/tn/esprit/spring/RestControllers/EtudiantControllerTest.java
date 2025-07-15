package tn.esprit.spring.RestControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.IEtudiantService;
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

@WebMvcTest(EtudiantRestController.class)
public class EtudiantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEtudiantService etudiantService;

    @Autowired
    private ObjectMapper objectMapper;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("Ali");
        etudiant.setPrenomEt("Ben Salah");
    }

    @Test
    void testAddOrUpdate() throws Exception {
        when(etudiantService.addOrUpdate(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(post("/etudiant/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(1L))
                .andExpect(jsonPath("$.nomEt").value("Ali"))
                .andExpect(jsonPath("$.prenomEt").value("Ben Salah"));
    }

    @Test
    void testFindAll() throws Exception {
        when(etudiantService.findAll()).thenReturn(List.of(etudiant));

        mockMvc.perform(get("/etudiant/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idEtudiant").value(1L));
    }

    @Test
    void testFindById() throws Exception {
        when(etudiantService.findById(1L)).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEt").value("Ali"));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(etudiantService).delete(any(Etudiant.class));

        mockMvc.perform(delete("/etudiant/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(etudiantService).deleteById(1L);

        mockMvc.perform(delete("/etudiant/deleteById").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testSelectJPQL() throws Exception {
        when(etudiantService.selectJPQL("Ali")).thenReturn(List.of(etudiant));

        mockMvc.perform(get("/etudiant/selectJPQL").param("nom", "Ali"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nomEt").value("Ali"));
    }
}

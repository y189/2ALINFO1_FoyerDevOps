package tn.esprit.spring.RestControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.Services.Bloc.IBlocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocRestController.class)
public class BlocControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlocService blocService;

    @Autowired
    private ObjectMapper objectMapper;

    private Bloc bloc;

    @BeforeEach
    void setUp() {
        bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("Bloc A");
    }

    @Test
    void testAddOrUpdate() throws Exception {
        Mockito.when(blocService.addOrUpdate(any(Bloc.class))).thenReturn(bloc);

        mockMvc.perform(post("/bloc/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBloc").value(1L))
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testFindAll() throws Exception {
        Mockito.when(blocService.findAll()).thenReturn(List.of(bloc));

        mockMvc.perform(get("/bloc/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idBloc").value(1L));
    }

    @Test
    void testFindById() throws Exception {
        Mockito.when(blocService.findById(1L)).thenReturn(bloc);

        mockMvc.perform(get("/bloc/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(blocService).delete(any(Bloc.class));

        mockMvc.perform(delete("/bloc/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(blocService).deleteById(1L);

        mockMvc.perform(delete("/bloc/deleteById").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testAffecterChambresABloc() throws Exception {
        List<Long> chambres = List.of(101L, 102L);
        Mockito.when(blocService.affecterChambresABloc(eq(chambres), eq("Bloc A"))).thenReturn(bloc);

        mockMvc.perform(put("/bloc/affecterChambresABloc")
                        .param("nomBloc", "Bloc A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chambres)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testAffecterBlocAFoyer() throws Exception {
        Mockito.when(blocService.affecterBlocAFoyer("Bloc A", "Foyer X")).thenReturn(bloc);

        mockMvc.perform(put("/bloc/affecterBlocAFoyer")
                        .param("nomBloc", "Bloc A")
                        .param("nomFoyer", "Foyer X"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testAffecterBlocAFoyer2() throws Exception {
        Mockito.when(blocService.affecterBlocAFoyer("Bloc A", "Foyer X")).thenReturn(bloc);

        mockMvc.perform(put("/bloc/affecterBlocAFoyer2/Foyer X/Bloc A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testAjouterBlocEtSesChambres() throws Exception {
        Mockito.when(blocService.ajouterBlocEtSesChambres(any(Bloc.class))).thenReturn(bloc);

        mockMvc.perform(post("/bloc/ajouterBlocEtSesChambres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBloc").value(1L));
    }

    @Test
    void testAjouterBlocEtAffecterAFoyer() throws Exception {
        Mockito.when(blocService.ajouterBlocEtAffecterAFoyer(any(Bloc.class), eq("Foyer X"))).thenReturn(bloc);

        mockMvc.perform(post("/bloc/ajouterBlocEtAffecterAFoyer/Foyer X")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }
}

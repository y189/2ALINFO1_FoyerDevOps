package tn.esprit.spring.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Universite.UniversiteService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniversiteServiceTest {

    @Mock
    UniversiteRepository universiteRepository;

    @InjectMocks
    UniversiteService universiteService;

    Universite universite;

    @BeforeEach
    void setUp() {
        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Université Test");

        Foyer foyer = new Foyer();
        foyer.setIdFoyer(100L);
        foyer.setNomFoyer("Foyer Principal");
        universite.setFoyer(foyer);
    }

    @Test
    void testAddOrUpdate() {
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.addOrUpdate(universite);

        assertEquals(universite, result);
        verify(universiteRepository).save(universite);
    }

    @Test
    void testFindAll() {
        List<Universite> universites = Arrays.asList(universite);
        when(universiteRepository.findAll()).thenReturn(universites);

        List<Universite> result = universiteService.findAll();

        assertEquals(1, result.size());
        assertEquals(universite, result.get(0));
        verify(universiteRepository).findAll();
    }

    @Test
    void testFindById_Found() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite result = universiteService.findById(1L);

        assertNotNull(result);
        assertEquals("Université Test", result.getNomUniversite());
        verify(universiteRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(universiteRepository.findById(2L)).thenReturn(Optional.empty());

        Universite result = universiteService.findById(2L);

        assertNull(result);  // car dans ta version, tu ne fais pas orElseThrow
        verify(universiteRepository).findById(2L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(universiteRepository).deleteById(1L);

        universiteService.deleteById(1L);

        verify(universiteRepository).deleteById(1L);
    }

    @Test
    void testDelete() {
        doNothing().when(universiteRepository).delete(universite);

        universiteService.delete(universite);

        verify(universiteRepository).delete(universite);
    }

    @Test
    void testAjouterUniversiteEtSonFoyer() {
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.ajouterUniversiteEtSonFoyer(universite);

        assertNotNull(result);
        assertEquals(universite.getFoyer(), result.getFoyer());
        verify(universiteRepository).save(universite);
    }
}

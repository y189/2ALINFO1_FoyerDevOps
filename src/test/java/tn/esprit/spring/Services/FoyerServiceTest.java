package tn.esprit.spring.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Foyer.FoyerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoyerServiceTest {

    @Mock
    FoyerRepository foyerRepository;

    @Mock
    UniversiteRepository universiteRepository;

    @Mock
    BlocRepository blocRepository;

    @InjectMocks
    FoyerService foyerService;

    private Foyer foyer;
    private Universite universite;
    private Bloc bloc;

    @BeforeEach
    void setUp() {
        foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("FoyerA");

        bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("BlocA");

        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("UniA");
    }

    @Test
    void testAddOrUpdate() {
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addOrUpdate(foyer);

        assertNotNull(result);
        verify(foyerRepository).save(foyer);
    }

    @Test
    void testFindAll() {
        List<Foyer> foyers = List.of(foyer);
        when(foyerRepository.findAll()).thenReturn(foyers);

        List<Foyer> result = foyerService.findAll();

        assertEquals(1, result.size());
        verify(foyerRepository).findAll();
    }

    @Test
    void testFindById() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.findById(1L);

        assertEquals(1L, result.getIdFoyer());
        verify(foyerRepository).findById(1L);
    }

    @Test
    void testDeleteById() {
        foyerService.deleteById(1L);
        verify(foyerRepository).deleteById(1L);
    }

    @Test
    void testDelete() {
        foyerService.delete(foyer);
        verify(foyerRepository).delete(foyer);
    }

    @Test
    void testAffecterFoyerAUniversite_ByName() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findByNomUniversite("UniA")).thenReturn(universite);
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(1L, "UniA");

        assertEquals(foyer, result.getFoyer());
        verify(foyerRepository).findById(1L);
        verify(universiteRepository).findByNomUniversite("UniA");
        verify(universiteRepository).save(universite);
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);
        foyer.setBlocs(blocs);

        when(foyerRepository.save(foyer)).thenReturn(foyer);
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(blocRepository.save(bloc)).thenReturn(bloc);
        when(universiteRepository.save(universite)).thenReturn(universite);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 1L);

        assertEquals(foyer, result);
        assertEquals(foyer, universite.getFoyer());
        verify(foyerRepository).save(foyer);
        verify(blocRepository).save(bloc);
        verify(universiteRepository).save(universite);
    }

    @Test
    void testAjoutFoyerEtBlocs() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);
        foyer.setBlocs(blocs);

        when(foyerRepository.save(foyer)).thenReturn(foyer);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Foyer result = foyerService.ajoutFoyerEtBlocs(foyer);

        assertEquals(foyer, result);
        assertEquals(foyer, bloc.getFoyer());
        verify(foyerRepository).save(foyer);
        verify(blocRepository).save(bloc);
    }

    @Test
    void testAffecterFoyerAUniversite_ByIds() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(1L, 1L);

        assertEquals(foyer, result.getFoyer());
        verify(universiteRepository).findById(1L);
        verify(foyerRepository).findById(1L);
        verify(universiteRepository).save(universite);
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        universite.setFoyer(foyer);

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.desaffecterFoyerAUniversite(1L);

        assertNull(result.getFoyer());
        verify(universiteRepository).findById(1L);
        verify(universiteRepository).save(universite);
    }
}
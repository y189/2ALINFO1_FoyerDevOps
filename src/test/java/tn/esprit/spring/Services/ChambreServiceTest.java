package tn.esprit.spring.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChambreServiceTest {

    @Mock
    ChambreRepository chambreRepository;

    @Mock
    BlocRepository blocRepository;

    @InjectMocks
    ChambreService chambreService;

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setReservations(new ArrayList<>());
    }

    @Test
    void testAddOrUpdate() {
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.addOrUpdate(chambre);

        assertNotNull(result);
        assertEquals(chambre.getIdChambre(), result.getIdChambre());
        verify(chambreRepository).save(chambre);
    }

    @Test
    void testFindAll() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> result = chambreService.findAll();

        assertEquals(1, result.size());
        verify(chambreRepository).findAll();
    }

    @Test
    void testFindById() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Chambre result = chambreService.findById(1L);

        assertEquals(1L, result.getIdChambre());
        verify(chambreRepository).findById(1L);
    }

    @Test
    void testDeleteById() {
        chambreService.deleteById(1L);
        verify(chambreRepository).deleteById(1L);
    }

    @Test
    void testDelete() {
        chambreService.delete(chambre);
        verify(chambreRepository).delete(chambre);
    }

    @Test
    void testGetChambresParNomBloc() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.findByBlocNomBloc("BlocA")).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomBloc("BlocA");

        assertEquals(1, result.size());
        verify(chambreRepository).findByBlocNomBloc("BlocA");
    }

    @Test
    void testNbChambreParTypeEtBloc() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        chambre.setBloc(bloc);

        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.findAll()).thenReturn(chambres);

        long count = chambreService.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, 1L);

        assertEquals(1, count);
        verify(chambreRepository).findAll();
    }

    @Test
    void testGetChambresNonReserveParNomFoyerEtTypeChambre() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("FoyerX");

        Bloc bloc = new Bloc();
        bloc.setFoyer(foyer);
        bloc.setIdBloc(1L);

        Chambre simple = new Chambre();
        simple.setTypeC(TypeChambre.SIMPLE);
        simple.setBloc(bloc);
        simple.setReservations(new ArrayList<>());

        Chambre doubleCh = new Chambre();
        doubleCh.setTypeC(TypeChambre.DOUBLE);
        doubleCh.setBloc(bloc);
        Reservation resDouble = new Reservation();
        resDouble.setAnneeUniversitaire(LocalDate.now());
        doubleCh.setReservations(List.of(resDouble));

        Chambre tripleCh = new Chambre();
        tripleCh.setTypeC(TypeChambre.TRIPLE);
        tripleCh.setBloc(bloc);
        Reservation resTriple1 = new Reservation();
        resTriple1.setAnneeUniversitaire(LocalDate.now());
        Reservation resTriple2 = new Reservation();
        resTriple2.setAnneeUniversitaire(LocalDate.now());
        tripleCh.setReservations(List.of(resTriple1, resTriple2));

        List<Chambre> chambres = List.of(simple, doubleCh, tripleCh);
        when(chambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> resultSimples = chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("FoyerX", TypeChambre.SIMPLE);
        List<Chambre> resultDoubles = chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("FoyerX", TypeChambre.DOUBLE);
        List<Chambre> resultTriples = chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("FoyerX", TypeChambre.TRIPLE);

        assertEquals(1, resultSimples.size());
        assertTrue(resultSimples.contains(simple));

        assertEquals(1, resultDoubles.size());
        assertTrue(resultDoubles.contains(doubleCh));

        assertEquals(1, resultTriples.size());
        assertTrue(resultTriples.contains(tripleCh));

        verify(chambreRepository, times(3)).findAll();
    }

    @Test
    void testGetChambresParNomBlocJava() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");
        bloc.setChambres(List.of(chambre));

        when(blocRepository.findByNomBloc("BlocA")).thenReturn(bloc);

        List<Chambre> result = chambreService.getChambresParNomBlocJava("BlocA");

        assertEquals(1, result.size());
        verify(blocRepository).findByNomBloc("BlocA");
    }

    @Test
    void testGetChambresParNomBlocKeyWord() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.findByBlocNomBloc("BlocA")).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomBlocKeyWord("BlocA");

        assertEquals(1, result.size());
        verify(chambreRepository).findByBlocNomBloc("BlocA");
    }

    @Test
    void testGetChambresParNomBlocJPQL() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.getChambresParNomBlocJPQL("BlocA")).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomBlocJPQL("BlocA");

        assertEquals(1, result.size());
        verify(chambreRepository).getChambresParNomBlocJPQL("BlocA");
    }

    @Test
    void testGetChambresParNomBlocSQL() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.getChambresParNomBlocSQL("BlocA")).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomBlocSQL("BlocA");

        assertEquals(1, result.size());
        verify(chambreRepository).getChambresParNomBlocSQL("BlocA");
    }
}
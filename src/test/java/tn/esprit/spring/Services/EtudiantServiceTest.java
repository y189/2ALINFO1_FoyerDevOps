package tn.esprit.spring.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceTest {

    @Mock
    EtudiantRepository etudiantRepository;

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    EtudiantService etudiantService;

    private Etudiant etudiant;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("Dupont");
        etudiant.setPrenomEt("Jean");
        etudiant.setReservations(new ArrayList<>());

        reservation = new Reservation();
        reservation.setIdReservation("R1");
    }

    @Test
    void testAddOrUpdate() {
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.addOrUpdate(etudiant);

        assertNotNull(result);
        assertEquals(etudiant.getIdEtudiant(), result.getIdEtudiant());
        verify(etudiantRepository).save(etudiant);
    }

    @Test
    void testFindAll() {
        List<Etudiant> etudiants = List.of(etudiant);
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.findAll();

        assertEquals(1, result.size());
        verify(etudiantRepository).findAll();
    }

    @Test
    void testFindById() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.findById(1L);

        assertEquals(1L, result.getIdEtudiant());
        verify(etudiantRepository).findById(1L);
    }

    @Test
    void testDeleteById() {
        etudiantService.deleteById(1L);
        verify(etudiantRepository).deleteById(1L);
    }

    @Test
    void testDelete() {
        etudiantService.delete(etudiant);
        verify(etudiantRepository).delete(etudiant);
    }

    @Test
    void testSelectJPQL() {
        List<Etudiant> etudiants = List.of(etudiant);
        when(etudiantRepository.selectJPQL("Dupont")).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.selectJPQL("Dupont");

        assertEquals(1, result.size());
        verify(etudiantRepository).selectJPQL("Dupont");
    }

    @Test
    void testAffecterReservationAEtudiant() {
        when(reservationRepository.findById("R1")).thenReturn(Optional.of(reservation));
        when(etudiantRepository.getByNomEtAndPrenomEt("Dupont", "Jean")).thenReturn(etudiant);
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        etudiantService.affecterReservationAEtudiant("R1", "Dupont", "Jean");

        assertTrue(etudiant.getReservations().contains(reservation));
        verify(reservationRepository).findById("R1");
        verify(etudiantRepository).getByNomEtAndPrenomEt("Dupont", "Jean");
        verify(etudiantRepository).save(etudiant);
    }

    @Test
    void testDesaffecterReservationAEtudiant() {
        etudiant.getReservations().add(reservation);

        when(reservationRepository.findById("R1")).thenReturn(Optional.of(reservation));
        when(etudiantRepository.getByNomEtAndPrenomEt("Dupont", "Jean")).thenReturn(etudiant);
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        etudiantService.desaffecterReservationAEtudiant("R1", "Dupont", "Jean");

        assertFalse(etudiant.getReservations().contains(reservation));
        verify(reservationRepository).findById("R1");
        verify(etudiantRepository).getByNomEtAndPrenomEt("Dupont", "Jean");
        verify(etudiantRepository).save(etudiant);
    }
}

package tn.esprit.spring.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Reservation.ReservationService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ChambreRepository chambreRepository;

    @Mock
    EtudiantRepository etudiantRepository;

    @InjectMocks
    ReservationService reservationService;

    Chambre chambre;
    Etudiant etudiant;

    @BeforeEach
    void setup() {
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");
        chambre.setBloc(bloc);
        chambre.setReservations(new ArrayList<>());

        etudiant = new Etudiant();
        etudiant.setCin(12345678L);
    }

    @Test
    void testAddOrUpdate() {
        Reservation res = new Reservation();
        when(reservationRepository.save(res)).thenReturn(res);

        Reservation result = reservationService.addOrUpdate(res);

        assertEquals(res, result);
        verify(reservationRepository).save(res);
    }

    @Test
    void testFindAll() {
        List<Reservation> reservations = List.of(new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertEquals(1, result.size());
        verify(reservationRepository).findAll();
    }

    @Test
    void testFindById() {
        Reservation res = new Reservation();
        when(reservationRepository.findById("res1")).thenReturn(Optional.of(res));

        Reservation result = reservationService.findById("res1");

        assertEquals(res, result);
        verify(reservationRepository).findById("res1");
    }

    @Test
    void testDeleteById() {
        reservationService.deleteById("res1");
        verify(reservationRepository).deleteById("res1");
    }

    @Test
    void testDelete() {
        Reservation res = new Reservation();
        reservationService.delete(res);
        verify(reservationRepository).delete(res);
    }

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant_Success() {
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre);
        when(etudiantRepository.findByCin(12345678L)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(anyLong(), any(), any())).thenReturn(0);
        when(reservationRepository.save(any())).thenAnswer(invocation -> {
            Reservation res = invocation.getArgument(0);
            // Sécurité supplémentaire si builder non initialisé
            if (res.getEtudiants() == null) {
                res.setEtudiants(new ArrayList<>());
            }
            return res;
        });
        when(chambreRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(101L, 12345678L);

        assertNotNull(result);
        assertTrue(result.getEtudiants().contains(etudiant));
        assertEquals(result, chambre.getReservations().get(0));
        verify(reservationRepository).save(any());
        verify(chambreRepository).save(chambre);
    }

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant_Full() {
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre);
        when(etudiantRepository.findByCin(12345678L)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(anyLong(), any(), any())).thenReturn(1);

        // Pour une chambre SIMPLE, 1 réservation = complet
        Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(101L, 12345678L);

        assertNull(result);
        verify(reservationRepository, never()).save(any());
        verify(chambreRepository, never()).save(any());
    }

    @Test
    void testGetReservationParAnneeUniversitaire() {
        when(reservationRepository.countByAnneeUniversitaireBetween(any(), any())).thenReturn(5);

        long count = reservationService.getReservationParAnneeUniversitaire(LocalDate.now().minusYears(1), LocalDate.now());

        assertEquals(5L, count);
        verify(reservationRepository).countByAnneeUniversitaireBetween(any(), any());
    }

    @Test
    void testAnnulerReservation() {
        Reservation res = new Reservation();
        res.setIdReservation("res1");
        res.setEstValide(true);
        Chambre c = new Chambre();
        c.setReservations(new ArrayList<>(List.of(res)));

        when(reservationRepository.findByEtudiantsCinAndEstValide(12345678L, true)).thenReturn(res);
        when(chambreRepository.findByReservationsIdReservation("res1")).thenReturn(c);

        String message = reservationService.annulerReservation(12345678L);

        assertEquals("La réservation res1 est annulée avec succés", message);
        assertFalse(c.getReservations().contains(res));
        verify(reservationRepository).delete(res);
        verify(chambreRepository).save(c);
    }

    @Test
    void testAffectReservationAChambre() {
        Reservation res = new Reservation();
        Chambre c = new Chambre();
        c.setReservations(new ArrayList<>());

        when(reservationRepository.findById("res1")).thenReturn(Optional.of(res));
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(c));

        reservationService.affectReservationAChambre("res1", 1L);

        assertTrue(c.getReservations().contains(res));
        verify(chambreRepository).save(c);
    }

    @Test
    void testDeaffectReservationAChambre() {
        Reservation res = new Reservation();
        Chambre c = new Chambre();
        c.setReservations(new ArrayList<>(List.of(res)));

        when(reservationRepository.findById("res1")).thenReturn(Optional.of(res));
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(c));

        reservationService.deaffectReservationAChambre("res1", 1L);

        assertFalse(c.getReservations().contains(res));
        verify(chambreRepository).save(c);
    }

    @Test
    void testAnnulerReservations() {
        Reservation res = new Reservation();
        res.setIdReservation("res1");
        res.setEstValide(true);

        List<Reservation> reservations = List.of(res);

        when(reservationRepository.findByEstValideAndAnneeUniversitaireBetween(eq(true), any(), any()))
                .thenReturn(reservations);
        when(reservationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        reservationService.annulerReservations();

        assertFalse(res.isEstValide());
        verify(reservationRepository).save(res);
    }
}

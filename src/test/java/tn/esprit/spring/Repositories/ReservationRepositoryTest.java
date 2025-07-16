package tn.esprit.spring.Repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    EtudiantRepository etudiantRepository;

    Etudiant etudiant;

    Reservation res1, res2;

    @BeforeEach
    void setUp() {
        etudiant = Etudiant.builder()
                .cin(123456789L)
                .nomEt("Dupont")
                .prenomEt("Jean")
                .build();
        etudiantRepository.save(etudiant);

        res1 = Reservation.builder()
                .idReservation("R1")
                .anneeUniversitaire(LocalDate.of(2022, 9, 1))
                .estValide(true)
                .build();
        // Initialisation manuelle
        res1.setEtudiants(new ArrayList<>());

        res2 = Reservation.builder()
                .idReservation("R2")
                .anneeUniversitaire(LocalDate.of(2023, 3, 1))
                .estValide(false)
                .build();
        // Initialisation manuelle
        res2.setEtudiants(new ArrayList<>());

        // Ajout des étudiants après initialisation
        res1.getEtudiants().add(etudiant);
        res2.getEtudiants().add(etudiant);

        reservationRepository.save(res1);
        reservationRepository.save(res2);
    }


    @Test
    void testCountByAnneeUniversitaireBetween() {
        int count = reservationRepository.countByAnneeUniversitaireBetween(
                LocalDate.of(2022, 1, 1), LocalDate.of(2023, 12, 31));
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testFindByEtudiantsCinAndEstValide() {
        Reservation found = reservationRepository.findByEtudiantsCinAndEstValide(123456789L, true);
        assertThat(found).isNotNull();
        assertThat(found.getIdReservation()).isEqualTo("R1");
        assertThat(found.isEstValide()).isTrue();
    }

    @Test
    void testFindByEstValideAndAnneeUniversitaireBetween() {
        List<Reservation> reservations = reservationRepository.findByEstValideAndAnneeUniversitaireBetween(
                true, LocalDate.of(2021, 1, 1), LocalDate.of(2023, 12, 31));
        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getIdReservation()).isEqualTo("R1");
    }
}

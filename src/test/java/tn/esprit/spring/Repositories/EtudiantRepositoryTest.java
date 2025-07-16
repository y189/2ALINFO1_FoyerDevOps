package tn.esprit.spring.Repositories;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class EtudiantRepositoryTest {

    @Autowired
    EtudiantRepository etudiantRepository;

    Etudiant et1;
    Etudiant et2;

    @BeforeEach
    void setUp() {
        et1 = Etudiant.builder()
                .nomEt("Dupont")
                .prenomEt("Jean")
                .cin(12345678L)
                .dateNaissance(LocalDate.of(2000, 1, 1))
                .ecole("ESPRIT")
                .build();

        et2 = Etudiant.builder()
                .nomEt("Dupont")
                .prenomEt("Marie")
                .cin(87654321L)
                .dateNaissance(LocalDate.of(1999, 5, 15))
                .ecole("ENIT")
                .build();

        etudiantRepository.save(et1);
        etudiantRepository.save(et2);
    }

    @Test
    void testFindByNomEt() {
        List<Etudiant> list = etudiantRepository.findByNomEt("Dupont");
        assertThat(list).hasSize(2);
    }

    @Test
    void testSelectJPQL() {
        List<Etudiant> list = etudiantRepository.selectJPQL("Dupont");
        assertThat(list).hasSize(2);
    }

    @Test
    void testSelectSQL() {
        List<Etudiant> list = etudiantRepository.selectSQL("Dupont");
        assertThat(list).hasSize(2);
    }

    @Test
    void testFindByNomEtAndPrenomEt() {
        List<Etudiant> list = etudiantRepository.findByNomEtAndPrenomEt("Dupont", "Jean");
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getPrenomEt()).isEqualTo("Jean");
    }

    @Test
    void testSelect2JPQL() {
        List<Etudiant> list = etudiantRepository.select2JPQL("Dupont", "Jean");
        assertThat(list).hasSize(1);
    }

    @Test
    void testSelect2SQL() {
        List<Etudiant> list = etudiantRepository.select2SQL("Jean", "Dupont");
        assertThat(list).hasSize(1);
    }

    @Test
    void testGetByNomEtAndPrenomEt() {
        Etudiant e = etudiantRepository.getByNomEtAndPrenomEt("Dupont", "Jean");
        assertThat(e).isNotNull();
        assertThat(e.getPrenomEt()).isEqualTo("Jean");
    }

    @Test
    void testFindByNomEtOrPrenomEt() {
        List<Etudiant> list = etudiantRepository.findByNomEtOrPrenomEt("Dupont", "Marie");
        assertThat(list).hasSize(2);
    }

    @Test
    void testFindByIdEtudiantGreaterThan() {
        List<Etudiant> list = etudiantRepository.findByIdEtudiantGreaterThan(0L);
        assertThat(list).hasSize(2);
    }

    @Test
    void testFindByIdEtudiantLessThan() {
        List<Etudiant> list = etudiantRepository.findByIdEtudiantLessThan(Long.MAX_VALUE);
        assertThat(list).hasSize(2);
    }

    @Test
    void testGetByDateNaissanceBetween() {
        List<Etudiant> list = etudiantRepository.getByDateNaissanceBetween(LocalDate.of(1998, 1, 1), LocalDate.of(2001, 1, 1));
        assertThat(list).hasSize(2);
    }

    @Test
    void testGetByNomEtLike() {
        List<Etudiant> list = etudiantRepository.getByNomEtLike("Dup%");
        assertThat(list).hasSize(2);
    }

    @Test
    void testGetByNomEtContaining() {
        List<Etudiant> list = etudiantRepository.getByNomEtContaining("pon");
        assertThat(list).hasSize(2);
    }

    @Test
    void testGetByNomEtContains() {
        List<Etudiant> list = etudiantRepository.getByNomEtContains("Dup");
        assertThat(list).hasSize(2);
    }

    @Test
    void testGetByNomEtIgnoreCase() {
        List<Etudiant> list = etudiantRepository.getByNomEtIgnoreCase("dupont");
        assertThat(list).hasSize(2);
    }

    @Test
    void testGetByNomEtNotNull() {
        List<Etudiant> list = etudiantRepository.getByNomEtNotNull();
        assertThat(list).hasSize(2);
    }

    @Test
    void testFindByCin() {
        Etudiant e = etudiantRepository.findByCin(12345678L);
        assertThat(e).isNotNull();
        assertThat(e.getPrenomEt()).isEqualTo("Jean");
    }

    @Test
    void testFindByNomEtLike() {
        List<Etudiant> list = etudiantRepository.findByNomEtLike("Dup%");
        assertThat(list).hasSize(2);
    }

    @Test
    void testFindByNomEtContains() {
        List<Etudiant> list = etudiantRepository.findByNomEtContains("Dup");
        assertThat(list).hasSize(2);
    }

    @Test
    void testFindByNomEtContaining() {
        List<Etudiant> list = etudiantRepository.findByNomEtContaining("Dup");
        assertThat(list).hasSize(2);
    }
}

package tn.esprit.spring.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Repositories.BlocRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class BlocRepositoryTest {

    @Autowired
    BlocRepository blocRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void testFindByNomBloc() {
        Bloc b = new Bloc();
        b.setNomBloc("BlocA");
        b.setCapaciteBloc(20);
        blocRepository.save(b);

        Bloc found = blocRepository.findByNomBloc("BlocA");
        assertThat(found).isNotNull();
        assertThat(found.getNomBloc()).isEqualTo("BlocA");
    }

    @Test
    void testSelectByNomBJPQL1() {
        Bloc b = new Bloc();
        b.setNomBloc("BlocJPQL1");
        b.setCapaciteBloc(15);
        blocRepository.save(b);

        Bloc found = blocRepository.selectByNomBJPQL1("BlocJPQL1");
        assertThat(found).isNotNull();
        assertThat(found.getNomBloc()).isEqualTo("BlocJPQL1");
    }

    @Test
    void testSelectByNomBSQL1() {
        Bloc b = new Bloc();
        b.setNomBloc("BlocSQL1");
        b.setCapaciteBloc(15);
        blocRepository.save(b);

        Bloc found = blocRepository.selectByNomBSQL1("BlocSQL1");
        assertThat(found).isNotNull();
        assertThat(found.getNomBloc()).isEqualTo("BlocSQL1");
    }

    @Test
    void testUpdateBlocJPQL() {
        Bloc b = new Bloc();
        b.setNomBloc("OldName");
        b.setCapaciteBloc(5);
        blocRepository.save(b);

        blocRepository.updateBlocJPQL("NewName");
        entityManager.flush();
        entityManager.clear();

        Bloc updated = blocRepository.findById(b.getIdBloc()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getNomBloc()).isEqualTo("NewName");
    }

    @Test
    void testUpdateBlocSQL() {
        Bloc b = new Bloc();
        b.setNomBloc("SQLName");
        b.setCapaciteBloc(5);
        blocRepository.save(b);

        blocRepository.updateBlocSQL("SQLUpdatedName");
        entityManager.flush();
        entityManager.clear();

        Bloc updated = blocRepository.findById(b.getIdBloc()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getNomBloc()).isEqualTo("SQLUpdatedName");
    }

    @Test
    void testGetByCapaciteBlocGreaterThan() {
        Bloc b1 = new Bloc();
        b1.setNomBloc("B1");
        b1.setCapaciteBloc(5);
        Bloc b2 = new Bloc();
        b2.setNomBloc("B2");
        b2.setCapaciteBloc(15);

        blocRepository.save(b1);
        blocRepository.save(b2);

        List<Bloc> result = blocRepository.findByCapaciteBlocGreaterThan(10);
        assertThat(result).extracting(Bloc::getNomBloc).contains("B2");
        assertThat(result).extracting(Bloc::getNomBloc).doesNotContain("B1");
    }

    @Test
    void testGetByNomBlocAndCapaciteBloc() {
        Bloc b = new Bloc();
        b.setNomBloc("BlocX");
        b.setCapaciteBloc(30);
        blocRepository.save(b);

        List<Bloc> result = blocRepository.getByNomBlocAndCapaciteBloc("BlocX", 30);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNomBloc()).isEqualTo("BlocX");
    }

    @Test
    void testFindByNomBlocIgnoreCase() {
        Bloc b = new Bloc();
        b.setNomBloc("BlocY");
        b.setCapaciteBloc(10);
        blocRepository.save(b);

        List<Bloc> result = blocRepository.findByNomBlocIgnoreCase("blocy");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNomBloc()).isEqualTo("BlocY");
    }

    @Test
    void testFindByNomBlocLike() {
        Bloc b = new Bloc();
        b.setNomBloc("BlocTest");
        b.setCapaciteBloc(10);
        blocRepository.save(b);

        List<Bloc> result = blocRepository.findByNomBlocLike("%Test%");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNomBloc()).isEqualTo("BlocTest");
    }

    @Test
    void testGetByNomBlocOrCapaciteBloc() {
        Bloc b1 = new Bloc();
        b1.setNomBloc("BlocA");
        b1.setCapaciteBloc(5);
        Bloc b2 = new Bloc();
        b2.setNomBloc("BlocB");
        b2.setCapaciteBloc(15);

        blocRepository.save(b1);
        blocRepository.save(b2);

        List<Bloc> result = blocRepository.getByNomBlocOrCapaciteBloc("BlocA", 15);
        assertThat(result).extracting(Bloc::getNomBloc).contains("BlocA", "BlocB");
    }

  }

package tn.esprit.spring.Services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.Services.Bloc.BlocService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlocServiceTest {

    @Mock
    BlocRepository repo;

    @Mock
    ChambreRepository chambreRepository;

    @Mock
    FoyerRepository foyerRepository;

    @InjectMocks
    BlocService blocService;

    @Test
    void testAddOrUpdate() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");

        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();

        List<Chambre> chambres = List.of(chambre1, chambre2);
        bloc.setChambres(chambres);

        when(repo.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.addOrUpdate(bloc);

        verify(repo).save(bloc);
        verify(chambreRepository, times(2)).save(any(Chambre.class));

        assertEquals("BlocA", result.getNomBloc());
    }



    @Test
    void testFindById() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.findById(1L);

        verify(repo).findById(1L);
        assertEquals(1L, result.getIdBloc());
    }



    @Test
    void testDelete() {
        Bloc bloc = new Bloc();
        Chambre chambre = new Chambre();
        List<Chambre> chambres = List.of(chambre);
        bloc.setChambres(chambres);

        blocService.delete(bloc);

        verify(chambreRepository).deleteAll(chambres);
        verify(repo).delete(bloc);
    }

    @Test
    void testAffecterChambresABloc() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101L);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102L);

        when(repo.findByNomBloc("BlocA")).thenReturn(bloc);
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre1);
        when(chambreRepository.findByNumeroChambre(102L)).thenReturn(chambre2);

        List<Long> nums = List.of(101L, 102L);
        Bloc result = blocService.affecterChambresABloc(nums, "BlocA");

        verify(repo).findByNomBloc("BlocA");
        verify(chambreRepository, times(2)).save(any(Chambre.class));

        assertEquals("BlocA", result.getNomBloc());
    }


    @Test
    void testFindAll() {
        // Arrange
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(new Bloc());
        when(repo.findAll()).thenReturn(blocs);

        // Act
        List<Bloc> result = blocService.findAll();

        // Assert
        assertEquals(1, result.size());
        verify(repo).findAll();
    }


    @Test
    void testDeleteById() {
        // Arrange
        Long id = 1L;
        Bloc bloc = new Bloc();
        bloc.setChambres(new ArrayList<>());

        when(repo.findById(id)).thenReturn(Optional.of(bloc));

        // Act
        blocService.deleteById(id);

        // Assert
        verify(chambreRepository).deleteAll(anyList());
        verify(repo).delete(bloc);
    }

    @Test
    void testAffecterBlocAFoyer() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("FoyerX");

        when(repo.findByNomBloc("BlocA")).thenReturn(bloc);
        when(foyerRepository.findByNomFoyer("FoyerX")).thenReturn(foyer);
        when(repo.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.affecterBlocAFoyer("BlocA", "FoyerX");

        verify(repo).findByNomBloc("BlocA");
        verify(foyerRepository).findByNomFoyer("FoyerX");
        verify(repo).save(bloc);

        assertEquals("BlocA", result.getNomBloc());
        assertEquals(foyer, result.getFoyer());
    }

    @Test
    void testAjouterBlocEtSesChambres() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");

        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();
        List<Chambre> chambres = List.of(chambre1, chambre2);
        bloc.setChambres(chambres);

        Bloc result = blocService.ajouterBlocEtSesChambres(bloc);

        verify(chambreRepository, times(2)).save(any(Chambre.class));
        assertEquals("BlocA", result.getNomBloc());
    }

    @Test
    void testAjouterBlocEtAffecterAFoyer() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("FoyerX");

        when(foyerRepository.findByNomFoyer("FoyerX")).thenReturn(foyer);
        when(repo.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.ajouterBlocEtAffecterAFoyer(bloc, "FoyerX");

        verify(foyerRepository).findByNomFoyer("FoyerX");
        verify(repo).save(bloc);

        assertEquals("BlocA", result.getNomBloc());
        assertEquals(foyer, result.getFoyer());
    }
}

package tn.esprit.spring.RestControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.Services.Reservation.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.Services.Reservation.IReservationService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    IReservationService service;

    @InjectMocks
    ReservationRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Reservation r = new Reservation();
        when(service.addOrUpdate(r)).thenReturn(r);

        Reservation result = controller.addOrUpdate(r);

        verify(service).addOrUpdate(r);
        assertEquals(r, result);
    }

    @Test
    void testFindAll() {
        List<Reservation> list = Arrays.asList(new Reservation(), new Reservation());
        when(service.findAll()).thenReturn(list);

        List<Reservation> result = controller.findAll();

        verify(service).findAll();
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        String id = "1";
        Reservation r = new Reservation();
        when(service.findById(id)).thenReturn(r);

        Reservation result = controller.findById(id);

        verify(service).findById(id);
        assertEquals(r, result);
    }

    @Test
    void testDeleteById() {
        String id = "1";

        doNothing().when(service).deleteById(id);

        controller.deleteById(id);

        verify(service).deleteById(id);
    }

    @Test
    void testDelete() {
        Reservation r = new Reservation();

        doNothing().when(service).delete(r);

        controller.delete(r);

        verify(service).delete(r);
    }

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant() {
        Long numChambre = 10L;
        long cin = 123456L;
        Reservation r = new Reservation();

        when(service.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin)).thenReturn(r);

        Reservation result = controller.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);

        verify(service).ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);
        assertEquals(r, result);
    }

    @Test
    void testGetReservationParAnneeUniversitaire() {
        LocalDate debut = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 12, 31);
        long count = 5L;

        when(service.getReservationParAnneeUniversitaire(debut, fin)).thenReturn(count);

        long result = controller.getReservationParAnneeUniversitaire(debut, fin);

        verify(service).getReservationParAnneeUniversitaire(debut, fin);
        assertEquals(count, result);
    }

    @Test
    void testAnnulerReservation() {
        long cinEtudiant = 123456L;
        String message = "Reservation annulée";

        when(service.annulerReservation(cinEtudiant)).thenReturn(message);

        String result = controller.annulerReservation(cinEtudiant);

        verify(service).annulerReservation(cinEtudiant);
        assertEquals(message, result);
    }
}


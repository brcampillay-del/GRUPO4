package cl.duocuc.veranum_corereserva.controller;

import cl.duocuc.veranum_corereserva.dto.ReservaRequest;
import cl.duocuc.veranum_corereserva.model.Reserva;
import cl.duocuc.veranum_corereserva.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;

    private ReservaRequest requestValido;
    private Reserva reservaMock;

    @BeforeEach
    void setUp() {
        requestValido = new ReservaRequest();
        requestValido.setRutUsuario("19123456-7");
        requestValido.setHabitacionId(1L);
        requestValido.setCantidadHuespedes(2);
        requestValido.setFechaIngreso(LocalDate.now().plusDays(1));
        requestValido.setFechaSalida(LocalDate.now().plusDays(4));

        reservaMock = new Reserva();
        reservaMock.setId(10L);
        reservaMock.setEstado("CONFIRMADA");
    }

    @Test
    @DisplayName("Debe retornar 201 CREATED al crear reserva")
    void crearReserva_Exito() {
        when(reservaService.crearReserva(any(ReservaRequest.class))).thenReturn(reservaMock);

        ResponseEntity<Map<String, Object>> response = reservaController.crearReserva(requestValido);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Reserva creada exitosamente", response.getBody().get("mensaje"));
        assertEquals(reservaMock, response.getBody().get("data"));
    }

    @Test
    @DisplayName("Debe retornar 200 OK al listar todas")
    void obtenerTodas_Exito() {
        when(reservaService.obtenerTodas()).thenReturn(List.of(reservaMock));
        ResponseEntity<Map<String, Object>> response = reservaController.obtenerTodas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe retornar 200 OK al buscar por ID")
    void obtenerPorId_Exito() {
        when(reservaService.obtenerPorId(10L)).thenReturn(reservaMock);
        ResponseEntity<Map<String, Object>> response = reservaController.obtenerPorId(10L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe retornar 200 OK al buscar historial por RUT")
    void obtenerPorRut_Exito() {
        when(reservaService.obtenerPorRut("19123456-7")).thenReturn(List.of(reservaMock));
        ResponseEntity<Map<String, Object>> response = reservaController.obtenerPorRut("19123456-7");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe retornar 200 OK al cancelar reserva")
    void cancelarReserva_Exito() {
        reservaMock.setEstado("CANCELADA");
        when(reservaService.cancelarReserva(10L)).thenReturn(reservaMock);
        ResponseEntity<Map<String, Object>> response = reservaController.cancelarReserva(10L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
package cl.duocuc.veranum_corereserva.controller;

import cl.duocuc.veranum_corereserva.model.Habitacion;
import cl.duocuc.veranum_corereserva.repository.HabitacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitacionControllerTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private HabitacionController habitacionController;

    private Habitacion habitacionMock;

    @BeforeEach
    void setUp() {
        habitacionMock = new Habitacion();
        habitacionMock.setId(1L);
        habitacionMock.setNumero("101");
        habitacionMock.setTipo("DOBLE");
        habitacionMock.setPrecioPorNoche(50000.0);
        habitacionMock.setEstado("DISPONIBLE");
    }

    @Test
    @DisplayName("Debe retornar 200 OK y la lista de habitaciones")
    void listarTodas_Exito() {
        when(habitacionRepository.findAll()).thenReturn(List.of(habitacionMock));
        ResponseEntity<List<Habitacion>> response = habitacionController.listarTodas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Debe retornar 201 CREATED al guardar habitación")
    void crear_Exito() {
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(habitacionMock);
        ResponseEntity<Habitacion> response = habitacionController.crear(habitacionMock);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Debe retornar 200 OK al actualizar habitación existente")
    void actualizar_Encontrado_Exito() {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacionMock));
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(habitacionMock);

        ResponseEntity<Habitacion> response = habitacionController.actualizar(1L, habitacionMock);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe retornar 404 Not Found si la habitación a actualizar no existe")
    void actualizar_NoEncontrado_Retorna404() {
        when(habitacionRepository.findById(99L)).thenReturn(Optional.empty());
        ResponseEntity<Habitacion> response = habitacionController.actualizar(99L, habitacionMock);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe retornar 204 No Content al eliminar habitación existente")
    void eliminar_Encontrado_Exito() {
        when(habitacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(habitacionRepository).deleteById(1L);

        ResponseEntity<Void> response = habitacionController.eliminar(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(habitacionRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 Not Found si la habitación a eliminar no existe")
    void eliminar_NoEncontrado_Retorna404() {
        when(habitacionRepository.existsById(99L)).thenReturn(false);
        ResponseEntity<Void> response = habitacionController.eliminar(99L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(habitacionRepository, never()).deleteById(anyLong());
    }
}
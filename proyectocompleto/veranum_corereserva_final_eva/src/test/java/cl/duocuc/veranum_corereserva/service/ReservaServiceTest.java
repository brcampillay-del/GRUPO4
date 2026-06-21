package cl.duocuc.veranum_corereserva.service;

import cl.duocuc.veranum_corereserva.client.UsuarioClient;
import cl.duocuc.veranum_corereserva.dto.ReservaRequest;
import cl.duocuc.veranum_corereserva.model.Habitacion;
import cl.duocuc.veranum_corereserva.model.Reserva;
import cl.duocuc.veranum_corereserva.repository.HabitacionRepository;
import cl.duocuc.veranum_corereserva.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

@Mock
private ReservaRepository reservaRepository;

@Mock
private HabitacionRepository habitacionRepository;

@Mock
private UsuarioClient usuarioClient;

@InjectMocks
private ReservaService reservaService;


private ReservaRequest requestValido;
private Habitacion habitacionDoble;
private Reserva reservaConfirmada;

@BeforeEach
void setUp() {

 requestValido = new ReservaRequest();
requestValido.setRutUsuario("19123456-7");
requestValido.setHabitacionId(1L);
requestValido.setCantidadHuespedes(2);
requestValido.setFechaIngreso(LocalDate.now().plusDays(1));
requestValido.setFechaSalida(LocalDate.now().plusDays(4));

habitacionDoble = new Habitacion();
habitacionDoble.setId(1L);
habitacionDoble.setTipo("DOBLE");
habitacionDoble.setPrecioPorNoche(50000.0);
habitacionDoble.setEstado("DISPONIBLE");

reservaConfirmada = new Reserva();
reservaConfirmada.setId(10L);
reservaConfirmada.setEstado("CONFIRMADA");
reservaConfirmada.setHabitacion(habitacionDoble);
}

@Test
@DisplayName("Debe crear una reserva exitosamente y calcular el total (Caso Feliz)")
 void crearReserva_Exito() {

when(usuarioClient.verificarSiExisteRut(requestValido.getRutUsuario())).thenReturn(true);
when(habitacionRepository.findById(requestValido.getHabitacionId())).thenReturn(Optional.of(habitacionDoble));
when(reservaRepository.contarReservasSuperpuestas(anyLong(), any(), any())).thenReturn(0L);

Reserva reservaGuardada = new Reserva();
reservaGuardada.setId(1L);
reservaGuardada.setEstado("CONFIRMADA");
reservaGuardada.setCostoTotal(150000.0);
when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaGuardada);


Reserva resultado = reservaService.crearReserva(requestValido);

assertNotNull(resultado);
assertEquals("CONFIRMADA", resultado.getEstado());
assertEquals(150000.0, resultado.getCostoTotal());

assertEquals("OCUPADA", habitacionDoble.getEstado());
verify(habitacionRepository, times(1)).save(habitacionDoble);
verify(reservaRepository, times(1)).save(any(Reserva.class));
}

@Test
@DisplayName("Debe lanzar excepción si la fecha de salida es anterior a la de ingreso")
void crearReserva_FechasInvalidas_LanzaExcepcion() {

requestValido.setFechaSalida(LocalDate.now().minusDays(1));

RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
     reservaService.crearReserva(requestValido);
});
 assertEquals("La fecha de salida debe ser posterior a la de ingreso.", excepcion.getMessage());
    verify(reservaRepository, never()).save(any(Reserva.class));
}

@Test
@DisplayName("Debe lanzar excepción si el RUT no existe en el microservicio de Usuarios")
void crearReserva_UsuarioNoExiste_LanzaExcepcion() {

when(usuarioClient.verificarSiExisteRut(requestValido.getRutUsuario())).thenReturn(false);

RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
     reservaService.crearReserva(requestValido);
});
assertEquals("El Rut ingresado no existe en el sistema de usuarios.", excepcion.getMessage());
    verify(reservaRepository, never()).save(any(Reserva.class));}

@Test
@DisplayName("Debe lanzar excepción si excede la capacidad máxima de la habitación")
void crearReserva_CapacidadExcedida_LanzaExcepcion() {

requestValido.setCantidadHuespedes(5);
when(usuarioClient.verificarSiExisteRut(requestValido.getRutUsuario())).thenReturn(true);
when(habitacionRepository.findById(requestValido.getHabitacionId())).thenReturn(Optional.of(habitacionDoble));

RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
   reservaService.crearReserva(requestValido);
});
assertTrue(excepcion.getMessage().contains("supera la capacidad máxima"));
    verify(reservaRepository, never()).save(any(Reserva.class));}

@Test
@DisplayName("Debe lanzar excepción cuando la habitación solicitada no existe")
void crearReserva_HabitacionNoExiste_LanzaExcepcion() {
requestValido.setHabitacionId(99L);
when(usuarioClient.verificarSiExisteRut(requestValido.getRutUsuario())).thenReturn(true);
when(habitacionRepository.findById(99L)).thenReturn(Optional.empty());

RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
   reservaService.crearReserva(requestValido);
});

assertEquals("La habitación solicitada no existe.", excepcion.getMessage());
 verify(reservaRepository, never()).save(any(Reserva.class)); // Validación defensiva

}

@Test
@DisplayName("Debe lanzar excepción cuando existe una reserva superpuesta en las fechas")
void crearReserva_FechasSuperpuestas_LanzaExcepcion() {
when(usuarioClient.verificarSiExisteRut(requestValido.getRutUsuario())).thenReturn(true);
when(habitacionRepository.findById(requestValido.getHabitacionId())).thenReturn(Optional.of(habitacionDoble));


when(reservaRepository.contarReservasSuperpuestas(
  requestValido.getHabitacionId(),
  requestValido.getFechaIngreso(),
  requestValido.getFechaSalida())).thenReturn(1L);

RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
reservaService.crearReserva(requestValido);
});

assertEquals("La habitación ya se encuentra reservada en las fechas seleccionadas.", excepcion.getMessage());
verify(reservaRepository, never()).save(any(Reserva.class));
}

@Test
@DisplayName("Debe cancelar la reserva, cambiar estado y liberar habitación")
void cancelarReserva_Exito() {

when(reservaRepository.findById(10L)).thenReturn(Optional.of(reservaConfirmada));
when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaConfirmada);

Reserva resultado = reservaService.cancelarReserva(10L);

assertEquals("CANCELADA", resultado.getEstado());
assertEquals("DISPONIBLE", habitacionDoble.getEstado());
verify(habitacionRepository, times(1)).save(habitacionDoble);
}

@Test
@DisplayName("Debe lanzar excepción si se intenta cancelar una reserva que YA está cancelada")
void cancelarReserva_YaCancelada_LanzaExcepcion() {
reservaConfirmada.setEstado("CANCELADA");
when(reservaRepository.findById(10L)).thenReturn(Optional.of(reservaConfirmada));

RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
   reservaService.cancelarReserva(10L);
});
assertEquals("La reserva ya se encuentra cancelada.", excepcion.getMessage());
}


@Test
@DisplayName("Debe retornar la lista completa de reservas")
void obtenerTodas_Exito() {

when(reservaRepository.findAll()).thenReturn(List.of(reservaConfirmada));

List<Reserva> resultados = reservaService.obtenerTodas();

assertFalse(resultados.isEmpty());
assertEquals(1, resultados.size());
verify(reservaRepository, times(1)).findAll();
}

@Test
@DisplayName("Debe retornar una reserva al buscar por ID válido")
void obtenerPorId_Exito() {
when(reservaRepository.findById(10L)).thenReturn(Optional.of(reservaConfirmada));

Reserva resultado = reservaService.obtenerPorId(10L);

assertNotNull(resultado);
assertEquals(10L, resultado.getId());
assertEquals("CONFIRMADA", resultado.getEstado());
}

@Test
@DisplayName("Debe lanzar excepción si se busca un ID de reserva que no existe")
void obtenerPorId_NoExiste_LanzaExcepcion() {

    when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
    reservaService.obtenerPorId(99L);
});
assertTrue(excepcion.getMessage().contains("Reserva no encontrada con ID: 99"));
}

@Test
@DisplayName("Debe retornar el historial de reservas de un RUT específico")
void obtenerPorRut_Exito() {

when(reservaRepository.findByRutUsuario("19123456-7")).thenReturn(List.of(reservaConfirmada));

List<Reserva> historial = reservaService.obtenerPorRut("19123456-7");

assertFalse(historial.isEmpty());
verify(reservaRepository, times(1)).findByRutUsuario("19123456-7");
 }


}
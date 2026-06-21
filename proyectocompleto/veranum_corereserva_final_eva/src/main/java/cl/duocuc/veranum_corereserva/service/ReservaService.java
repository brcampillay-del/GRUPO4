package cl.duocuc.veranum_corereserva.service;

import cl.duocuc.veranum_corereserva.client.UsuarioClient;
import cl.duocuc.veranum_corereserva.dto.ReservaRequest;
import cl.duocuc.veranum_corereserva.model.Habitacion;
import cl.duocuc.veranum_corereserva.model.Reserva;
import cl.duocuc.veranum_corereserva.repository.HabitacionRepository;
import cl.duocuc.veranum_corereserva.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

private final ReservaRepository reservaRepository;
private final HabitacionRepository habitacionRepository;
private final UsuarioClient usuarioClient;

private static final Logger log = LoggerFactory.getLogger(ReservaService.class);

    public Reserva crearReserva(ReservaRequest request) {
        if (request == null) {
            log.error("Error: Se intento crear una reserva pero la solicitud llegó vacía o nula.");
            throw new IllegalArgumentException("La solicitud de reserva no puede estar vacía.");
        }

        log.info("Iniciando proceso de reserva para el usuario con RUT: {}", request.getRutUsuario());


        if (request.getFechaSalida().isBefore(request.getFechaIngreso()) || request.getFechaSalida().isEqual(request.getFechaIngreso())) {
            log.warn("Rechazo: La fecha de salida debe ser posterior a la de ingreso.");
            throw new RuntimeException("La fecha de salida debe ser posterior a la de ingreso.");
        }

        boolean usuarioExiste = false;
        try {
            log.info("Contactando al microservicio de usuarios para validar RUT: {}", request.getRutUsuario());
            usuarioExiste = usuarioClient.verificarSiExisteRut(request.getRutUsuario());
        } catch (Exception e) {
            log.error("Fallo critico: No se pudo comunicar con el microservicio de Usuarios.");
            throw new RuntimeException("El servicio de validación de usuarios no está disponible.");
        }

        if (!usuarioExiste) {
            log.warn("Creación rechazada: El RUT {} no existe en la base de datos de Usuarios.", request.getRutUsuario());
            throw new RuntimeException("El Rut ingresado no existe en el sistema de usuarios.");
        }

        Habitacion habitacion = habitacionRepository.findById(request.getHabitacionId())
                .orElseThrow(() -> {
                    log.warn("Creación rechazada: La habitación con ID {} no existe.", request.getHabitacionId());
                    return new RuntimeException("La habitación solicitada no existe.");
                });


        int capacidadMax = habitacion.getTipo().equalsIgnoreCase("SIMPLE") ? 1 :
                habitacion.getTipo().equalsIgnoreCase("DOBLE") ? 2 : 4;
        if (request.getCantidadHuespedes() > capacidadMax) {
            log.warn("Creación rechazada: Capacidad excedida. Solicitados: {}, Máximo: {}", request.getCantidadHuespedes(), capacidadMax);
            throw new RuntimeException("La cantidad de huéspedes supera la capacidad máxima de esta habitación (" + capacidadMax + ").");
        }


        long superpuestas = reservaRepository.contarReservasSuperpuestas(request.getHabitacionId(), request.getFechaIngreso(), request.getFechaSalida());
        if (superpuestas > 0) {
            log.warn("Creación rechazada: La habitación {} ya está reservada en esas fechas.", request.getHabitacionId());
            throw new RuntimeException("La habitación ya se encuentra reservada en las fechas seleccionadas.");
        }

        long noches = ChronoUnit.DAYS.between(request.getFechaIngreso(), request.getFechaSalida());
        log.info("Cálculo de estadía: {} noches en habitación tipo {}.", noches, habitacion.getTipo());

        double total = noches * habitacion.getPrecioPorNoche();

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setRutUsuario(request.getRutUsuario());
        nuevaReserva.setHabitacion(habitacion);
        nuevaReserva.setFechaIngreso(request.getFechaIngreso());
        nuevaReserva.setFechaSalida(request.getFechaSalida());
        nuevaReserva.setCantidadHuespedes(request.getCantidadHuespedes());
        nuevaReserva.setCostoTotal(total);
        nuevaReserva.setEstado("CONFIRMADA");

        habitacion.setEstado("OCUPADA");
        habitacionRepository.save(habitacion);

        Reserva reservaGuardada = reservaRepository.save(nuevaReserva);
        log.info("Reserva creada exitosamente con ID: {}. Costo total: ${}", reservaGuardada.getId(), total);

        return reservaGuardada;
    }

public List<Reserva> obtenerTodas() {
    log.info("Consultando el listado completo de reservas...");
    List<Reserva> reservas = reservaRepository.findAll();
    if (reservas.isEmpty()) {
        log.warn("La consulta se realizó, pero actualmente no hay reservas en el sistema.");
    }
    return reservas;
}

public Reserva obtenerPorId(Long id) {
    log.info("Buscando el detalle de la reserva con ID: {}", id);
    return reservaRepository.findById(id).orElseThrow(() -> {
        log.warn("Búsqueda fallida: No existe una reserva con el ID {}", id);
        return new RuntimeException("Reserva no encontrada con ID: " + id);
    });
    }

public List<Reserva> obtenerPorRut(String rut) {
    log.info("Consultando el historial de reservas para el RUT: {}", rut);
    List<Reserva> historial = reservaRepository.findByRutUsuario(rut);
    if (historial.isEmpty()) {
        log.info("El historial está vacío para el RUT: {}", rut);
    }
    return historial;
}

    public Reserva cancelarReserva(Long id) {
        log.info("Iniciando proceso de cancelación para la reserva ID: {}", id);
        Reserva reserva = obtenerPorId(id);

        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            log.warn("Cancelación rechazada: La reserva con ID {} ya se encontraba cancelada previamente.", id);
            throw new RuntimeException("La reserva ya se encuentra cancelada.");
        }

        reserva.setEstado("CANCELADA");


        Habitacion habitacion = reserva.getHabitacion();
        habitacion.setEstado("DISPONIBLE");
        habitacionRepository.save(habitacion);

        Reserva reservaCancelada = reservaRepository.save(reserva);
        log.info("La reserva ID: {} fue cancelada con éxito y la habitación liberada.", id);

        return reservaCancelada;
    }
}
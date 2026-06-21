package cl.duocuc.veranum_corereserva.repository;

import cl.duocuc.veranum_corereserva.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByRutUsuario(String rutUsuario);

    // NUEVO: Regla de negocio para evitar cruce de fechas
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.habitacion.id = :habitacionId AND r.estado != 'CANCELADA' AND (r.fechaIngreso < :salida AND r.fechaSalida > :ingreso)")
    long contarReservasSuperpuestas(@Param("habitacionId") Long habitacionId, @Param("ingreso") LocalDate ingreso, @Param("salida") LocalDate salida);
}
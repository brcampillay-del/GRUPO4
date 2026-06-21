package cl.duocuc.veranum_corereserva.controller;


import cl.duocuc.veranum_corereserva.dto.ReservaRequest;
import cl.duocuc.veranum_corereserva.model.Reserva;
import cl.duocuc.veranum_corereserva.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@Tag(name = "Gestión de Reservas", description = "Operaciones para crear, buscar y cancelar reservas del hotel")
@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor

public class ReservaController {

    private final ReservaService reservaService;

    @Operation(summary = "Crear una nueva reserva", description = "Registra una reserva en el sistema validando disponibilidad y capacidad de la habitación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados o reglas de negocio fallidas")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearReserva(@Valid @RequestBody ReservaRequest request) {
        Reserva nuevaReserva = reservaService.crearReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Reserva creada exitosamente", "data", nuevaReserva));
    }
    @Operation(summary = "Listar todas las reservas", description = "Obtiene una lista completa de todas las reservas registradas en la base de datos.")
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodas() {
        return ResponseEntity.ok(Map.of("mensaje", "Lista de reservas", "data", reservaService.obtenerTodas()));
    }

    @Operation(summary = "Buscar reserva por ID", description = "Devuelve los detalles de una reserva específica basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "La reserva no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("mensaje", "Detalle de reserva", "data", reservaService.obtenerPorId(id)));
    }

    @Operation(summary = "Historial por RUT", description = "Obtiene la lista de todas las reservas asociadas al RUT de un usuario.")
    @GetMapping("/usuario/{rut}")
    public ResponseEntity<Map<String, Object>> obtenerPorRut(@PathVariable String rut) {
        List<Reserva> historial = reservaService.obtenerPorRut(rut);
        return ResponseEntity.ok(Map.of("mensaje", "Historial del usuario", "data", historial));
    }

    @Operation(summary = "Cancelar reserva", description = "Cambia el estado de una reserva a CANCELADA mediante su ID.")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Map<String, Object>> cancelarReserva(@PathVariable Long id) {
        Reserva reservaCancelada = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada", "data", reservaCancelada));
    }

}
package cl.duocuc.veranum_corereserva.controller;

import cl.duocuc.veranum_corereserva.model.Habitacion;
import cl.duocuc.veranum_corereserva.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionRepository habitacionRepository;

    @GetMapping
    public ResponseEntity<List<Habitacion>> listarTodas() {
        return ResponseEntity.ok(habitacionRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Habitacion> crear(@RequestBody Habitacion habitacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitacionRepository.save(habitacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habitacion> actualizar(@PathVariable Long id, @RequestBody Habitacion habitacionDetalles) {
        return habitacionRepository.findById(id)
                .map(habitacion -> {
                    habitacion.setNumero(habitacionDetalles.getNumero());
                    habitacion.setTipo(habitacionDetalles.getTipo());
                    habitacion.setPrecioPorNoche(habitacionDetalles.getPrecioPorNoche());
                    habitacion.setEstado(habitacionDetalles.getEstado());
                    return ResponseEntity.ok(habitacionRepository.save(habitacion));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (habitacionRepository.existsById(id)) {
            habitacionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
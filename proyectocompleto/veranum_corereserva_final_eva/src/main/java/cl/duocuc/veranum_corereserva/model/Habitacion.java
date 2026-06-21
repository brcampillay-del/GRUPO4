package cl.duocuc.veranum_corereserva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "precio_por_noche", nullable = false)
    private Double precioPorNoche;

    @Column(nullable = false)
    @Pattern(regexp = "^(DISPONIBLE|OCUPADA|MANTENIMIENTO)$", message = "El estado debe ser DISPONIBLE, OCUPADA o MANTENIMIENTO")
    private String estado;
}

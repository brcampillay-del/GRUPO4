package cl.duocuc.veranum_corereserva.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
public class ReservaRequest
{

    @NotBlank(message = "El Rut del usuario es obligatorio")
    @Pattern(regexp = "^[0-9]+-[0-9kK]{1}$", message = "Formato de RUT inválido. Debe ser sin puntos y con guion (ej: 12345678-9)")
    private String rutUsuario;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @FutureOrPresent (message = "la fecha de estado no puede estar en pasado")
    private LocalDate fechaIngreso;

    @NotNull(message = "la fecha de salida es obligatoria")
    private LocalDate fechaSalida;

    @NotNull(message = "Debe indicar el ID de la habitación")
    private Long habitacionId;

    @NotNull(message = "La cantidad de huespedes es obligatoria")
    @Min(value =1, message = "Debe haber almenos 1 huesped")
    private Integer CantidadHuespedes;
}

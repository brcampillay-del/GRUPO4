package cl.duocuc.veranum_coreusuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioRequest {

        @NotBlank(message = "El RUT es obligatorio")
        @Pattern(regexp = "^[0-9]+-[0-9kK]{1}$", message = "Formato de RUT inválido. Debe ser sin puntos y con guion (ej: 12345678-9)")
        private String rut;

        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        private String nombre;

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de correo inválido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
        private String password;

        @NotNull(message = "Debe asignar el ID de un rol")
        private Long rolId;
}

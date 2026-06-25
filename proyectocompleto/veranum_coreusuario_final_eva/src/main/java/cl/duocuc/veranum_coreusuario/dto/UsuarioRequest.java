package cl.duocuc.veranum_coreusuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor

public class UsuarioRequest {

@Schema(description = "RUT del cliente sin puntos y con guion. Identificador único en el sistema.", example = "19123456-7")
        @NotBlank(message = "El RUT es obligatorio")
        @Pattern(regexp = "^[0-9]+-[0-9kK]{1}$", message = "Formato de RUT inválido. Debe ser sin puntos y con guion (ej. 12345678-9)")
        private String rut;

@Schema(description = "Nombre completo del huésped o cliente", example = "Angelo Campillay")
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        private String nombre;

@Schema(description = "Correo electrónico de contacto y credencial de acceso", example = "angelo.campillay@correo.com")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de correo inválido")
        private String email;

@Schema(description = "Contraseña de acceso al sistema (minimo 8 caracteres)", example = "Duoc1234")
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
        private String password;

@Schema(description = "ID del rol que tendra el usuario en el sistema (ej. 1: Administrador, 2: Cliente)", example = "2")
        @NotNull(message = "Debe asignar el ID de un rol")
        private Long rolId;
}

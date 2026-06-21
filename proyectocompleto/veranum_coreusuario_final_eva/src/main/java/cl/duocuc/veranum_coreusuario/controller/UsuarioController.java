package cl.duocuc.veranum_coreusuario.controller;


import cl.duocuc.veranum_coreusuario.dto.ApiResponse;
import cl.duocuc.veranum_coreusuario.dto.UsuarioRequest;
import cl.duocuc.veranum_coreusuario.model.Usuario;
import cl.duocuc.veranum_coreusuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Gestión de Usuarios", description = "Operaciones para registrar, buscar y administrar el estado de los clientes y administradores.")
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController{

private final UsuarioService usuarioService;

@Operation(summary = "Registrar un nuevo usuario", description = "Crea un usuario validando que el RUT y el correo electrónico no estén duplicados en el sistema.")
@ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos (ej. RUT mal formateado)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El correo o RUT ya existe")
})
@PostMapping
public ResponseEntity<ApiResponse<Usuario>> crearUsuario(@Valid @RequestBody UsuarioRequest request) {
    Usuario usuario = usuarioService.crearUsuario(request);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>("Usuario creado con éxito", usuario));
    }

@Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista completa de todos los usuarios registrados.")
@GetMapping
public ResponseEntity<ApiResponse<List<Usuario>>> obtenerTodos() {
    return ResponseEntity.ok(new ApiResponse<>("Lista de usuarios", usuarioService.obtenerTodos()));
    }

@Operation(summary = "Buscar usuario", description = "Devuelve los detalles de un usuario específico buscado por su identificador.")
@ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El usuario no existe")
})
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<Usuario>> obtenerPorId(@PathVariable Long id) {
    return ResponseEntity.ok(new ApiResponse<>("Detalle del usuario", usuarioService.obtenerPorId(id)));
    }

@Operation(summary = "Bloquear usuario", description = "Cambia el estado de un usuario a BLOQUEADO por motivos de seguridad o administración.")
@PutMapping("/{id}/bloquear")
public ResponseEntity<ApiResponse<Usuario>> bloquearUsuario(@PathVariable Long id) {
    return ResponseEntity.ok(new ApiResponse<>("Usuario bloqueado", usuarioService.bloquearUsuario(id)));
    }

@Operation(summary = "Verificar existencia por RUT", description = "Consulta interna para validar si un RUT ya está registrado en la base de datos.")
@GetMapping("/existe/{rut}")
public boolean verificarSiExisteRut(@PathVariable String rut) {
    return usuarioService.existePorRut(rut);
    }


@Operation(summary = "Actualizar usuario", description = "Modifica los datos de un usuario existente.")
@PutMapping("/{id}")
public ResponseEntity<ApiResponse<Usuario>> actualizarUsuario(
@PathVariable Long id,
@Valid @RequestBody UsuarioRequest request) {
  Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, request);
    return ResponseEntity.ok(new ApiResponse<>("Usuario actualizado con éxito", usuarioActualizado));
}

@Operation(summary = "Eliminar usuario", description = "Elimina permanentemente a un usuario del sistema.")
@DeleteMapping("/{id}")
public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable Long id) {
  usuarioService.eliminarUsuario(id);
    return ResponseEntity.ok(new ApiResponse<>("Usuario eliminado correctamente", null));
}


}
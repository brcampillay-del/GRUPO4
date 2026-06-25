package cl.duocuc.veranum_coreusuario.service;

import cl.duocuc.veranum_coreusuario.dto.UsuarioRequest;
import cl.duocuc.veranum_coreusuario.model.Rol;
import cl.duocuc.veranum_coreusuario.model.Usuario;
import cl.duocuc.veranum_coreusuario.repository.RolRepository;
import cl.duocuc.veranum_coreusuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

@Mock
private UsuarioRepository usuarioRepository;

@Mock
private RolRepository rolRepository;

@InjectMocks
private UsuarioService usuarioService;

private UsuarioRequest requestValido;
private Usuario usuarioExistente = new Usuario();
private Rol rolCliente;

@BeforeEach
void setUp() {
    requestValido = new UsuarioRequest(
            "19123456-7",
            "Angelo Campillay",
            "angelo@correo.cl",
            "secreta123",
            1L
    );
rolCliente = new Rol();
rolCliente.setId(1L);
rolCliente.setNombre("CLIENTE");

usuarioExistente = new Usuario();
usuarioExistente.setId(10L);
usuarioExistente.setRut("19123456-7");
usuarioExistente.setEmail("angelo@correo.cl");
usuarioExistente.setBloqueado(false);
usuarioExistente.setRol(rolCliente);
}

@Test
@DisplayName("Debe crear un usuario exitosamente (Caso Feliz)")
void crearUsuario_Exito() {
    when(usuarioRepository.findByRut(requestValido.getRut())).thenReturn(Optional.empty());
    when(usuarioRepository.findByEmail(requestValido.getEmail())).thenReturn(Optional.empty());
    when(rolRepository.findById(requestValido.getRolId())).thenReturn(Optional.of(rolCliente));
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);
    Usuario resultado = usuarioService.crearUsuario(requestValido);
    assertNotNull(resultado);
    assertEquals(10L, resultado.getId());
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
}

@Test
@DisplayName("Debe lanzar excepción si el RUT ya está registrado")
void crearUsuario_RutDuplicado_LanzaExcepcion() {
    when(usuarioRepository.findByRut(requestValido.getRut())).thenReturn(Optional.of(usuarioExistente));
    RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
        usuarioService.crearUsuario(requestValido);
    });
    assertEquals("El RUT ya se encuentra registrado.", excepcion.getMessage());
    verify(usuarioRepository, never()).save(any(Usuario.class));
}

@Test
@DisplayName("Debe lanzar excepción si el Email ya está registrado")
void crearUsuario_EmailDuplicado_LanzaExcepcion() {
    when(usuarioRepository.findByRut(requestValido.getRut())).thenReturn(Optional.empty());
    when(usuarioRepository.findByEmail(requestValido.getEmail())).thenReturn(Optional.of(usuarioExistente));
    RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
        usuarioService.crearUsuario(requestValido);
    });
    assertEquals("El Email ya se encuentra registrado.", excepcion.getMessage());
    verify(usuarioRepository, never()).save(any(Usuario.class));
}

@Test
@DisplayName("Debe bloquear a un usuario correctamente")
void bloquearUsuario_Exito() {
    when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioExistente));
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);
    Usuario resultado = usuarioService.bloquearUsuario(10L);
    assertTrue(resultado.isBloqueado());
    verify(usuarioRepository, times(1)).save(usuarioExistente);
}

@Test
@DisplayName("Debe lanzar excepción si se intenta bloquear un usuario ya bloqueado")
void bloquearUsuario_YaBloqueado_LanzaExcepcion() {
    usuarioExistente.setBloqueado(true);
    when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioExistente));
    RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
        usuarioService.bloquearUsuario(10L);
    });
    assertEquals("El usuario ya se encuentra bloqueado.", excepcion.getMessage());
    verify(usuarioRepository, never()).save(any(Usuario.class));
}

@Test
@DisplayName("Debe devolver true si el RUT existe en el sistema")
void existePorRut_Existe_RetornaTrue() {
    when(usuarioRepository.findByRut("19123456-7")).thenReturn(Optional.of(usuarioExistente));
    boolean existe = usuarioService.existePorRut("19123456-7");
    assertTrue(existe);
}

@Test
@DisplayName("Debe devolver false si el RUT no existe en el sistema")
void existePorRut_NoExiste_RetornaFalse() {
    when(usuarioRepository.findByRut("99999999-9")).thenReturn(Optional.empty());
    boolean existe = usuarioService.existePorRut("99999999-9");
    assertFalse(existe);
}

@Test
@DisplayName("Debe actualizar un usuario exitosamente")
void actualizarUsuario_Exito() {
    when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioExistente));
    when(usuarioRepository.findByRut(requestValido.getRut())).thenReturn(Optional.of(usuarioExistente));
    when(usuarioRepository.findByEmail(requestValido.getEmail())).thenReturn(Optional.empty());
    when(rolRepository.findById(requestValido.getRolId())).thenReturn(Optional.of(rolCliente));
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);
    Usuario resultado = usuarioService.actualizarUsuario(10L, requestValido);
    assertNotNull(resultado);
    verify(usuarioRepository, times(1)).save(usuarioExistente);
}

@Test
@DisplayName("Debe lanzar excepción si al actualizar, el RUT pertenece a otro usuario")
void actualizarUsuario_ConflictoRut_LanzaExcepcion() {
    Usuario otroUsuario = new Usuario();
    otroUsuario.setId(99L);
    when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioExistente));
    when(usuarioRepository.findByRut(requestValido.getRut())).thenReturn(Optional.of(otroUsuario));
    IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
        usuarioService.actualizarUsuario(10L, requestValido);
    });
    assertEquals("Conflicto: El RUT ya está en uso por otro usuario.", excepcion.getMessage());
    verify(usuarioRepository, never()).save(any(Usuario.class));
}

@Test
@DisplayName("Debe eliminar el usuario correctamente")
void eliminarUsuario_Exito() {
    when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioExistente));
    usuarioService.eliminarUsuario(10L);
    verify(usuarioRepository, times(1)).delete(usuarioExistente);
}

@Test
@DisplayName("Debe retornar la lista completa de usuarios")
void obtenerTodos_Exito() {
    when(usuarioRepository.findAll()).thenReturn(List.of(usuarioExistente));
    List<Usuario> resultados = usuarioService.obtenerTodos();
    assertFalse(resultados.isEmpty());
    assertEquals(1, resultados.size());
}
@Test
@DisplayName("Debe lanzar la Exepcion si el rol indicado no existe ")
void crearUsuario_RolNoExistente_LanzaException(){
    when(usuarioRepository.findByRut(requestValido.getRut())).thenReturn(Optional.empty());
    when(usuarioRepository.findByEmail(requestValido.getEmail())).thenReturn(Optional.empty());
    when(rolRepository.findById(requestValido.getRolId())).thenReturn(Optional.empty());


    RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
        usuarioService.crearUsuario(requestValido);
    });

    assertEquals("El Rol especificado no existe.", excepcion.getMessage());
    verify(usuarioRepository, never()).save(any(Usuario.class));
}

}
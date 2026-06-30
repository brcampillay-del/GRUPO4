package cl.duocuc.veranum_coreusuario.controller;

import cl.duocuc.veranum_coreusuario.dto.ApiResponse;
import cl.duocuc.veranum_coreusuario.dto.UsuarioRequest;
import cl.duocuc.veranum_coreusuario.model.Usuario;
import cl.duocuc.veranum_coreusuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioRequest requestValido;
    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        requestValido = new UsuarioRequest("19123456-7", "Angelo Campillay", "angelo@correo.cl", "secreta123", 1L);
        usuarioMock = new Usuario();
        usuarioMock.setId(10L);
        usuarioMock.setRut("19123456-7");
    }

    @Test
    @DisplayName("Debe retornar 201 CREATED al crear un usuario exitosamente")
    void crearUsuario_Exito() {
        when(usuarioService.crearUsuario(any(UsuarioRequest.class))).thenReturn(usuarioMock);

        ResponseEntity<ApiResponse<Usuario>> response = usuarioController.crearUsuario(requestValido);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(usuarioService, times(1)).crearUsuario(any(UsuarioRequest.class));
    }

    @Test
    @DisplayName("Debe retornar 200 OK y la lista de usuarios")
    void obtenerTodos_Exito() {
        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuarioMock));

        ResponseEntity<ApiResponse<List<Usuario>>> response = usuarioController.obtenerTodos();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(usuarioService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("Debe retornar 200 OK al bloquear un usuario")
    void bloquearUsuario_Exito() {
        usuarioMock.setBloqueado(true);
        when(usuarioService.bloquearUsuario(10L)).thenReturn(usuarioMock);

        ResponseEntity<ApiResponse<Usuario>> response = usuarioController.bloquearUsuario(10L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService, times(1)).bloquearUsuario(10L);
    }

    @Test
    @DisplayName("Debe retornar 200 OK al buscar un usuario por ID")
    void obtenerPorId_Exito() {
        when(usuarioService.obtenerPorId(10L)).thenReturn(usuarioMock);

        ResponseEntity<ApiResponse<Usuario>> response = usuarioController.obtenerPorId(10L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioMock, response.getBody().getData());
        verify(usuarioService, times(1)).obtenerPorId(10L);
    }

    @Test
    @DisplayName("Debe retornar true si el RUT existe (Endpoint booleano)")
    void verificarSiExisteRut_Exito() {
        when(usuarioService.existePorRut("19123456-7")).thenReturn(true);

        boolean response = usuarioController.verificarSiExisteRut("19123456-7");

        assertTrue(response);
        verify(usuarioService, times(1)).existePorRut("19123456-7");
    }

    @Test
    @DisplayName("Debe retornar 200 OK al actualizar un usuario")
    void actualizarUsuario_Exito() {
        when(usuarioService.actualizarUsuario(eq(10L), any(UsuarioRequest.class))).thenReturn(usuarioMock);

        ResponseEntity<ApiResponse<Usuario>> response = usuarioController.actualizarUsuario(10L, requestValido);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService, times(1)).actualizarUsuario(eq(10L), any(UsuarioRequest.class));
    }

    @Test
    @DisplayName("Debe retornar 200 OK al eliminar un usuario")
    void eliminarUsuario_Exito() {
        doNothing().when(usuarioService).eliminarUsuario(10L);

        ResponseEntity<ApiResponse<Void>> response = usuarioController.eliminarUsuario(10L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService, times(1)).eliminarUsuario(10L);
    }


}
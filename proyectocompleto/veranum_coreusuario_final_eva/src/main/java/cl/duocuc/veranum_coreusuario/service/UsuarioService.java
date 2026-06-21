package cl.duocuc.veranum_coreusuario.service;

import cl.duocuc.veranum_coreusuario.dto.UsuarioRequest;
import cl.duocuc.veranum_coreusuario.model.Rol;
import cl.duocuc.veranum_coreusuario.model.Usuario;
import cl.duocuc.veranum_coreusuario.repository.RolRepository;
import cl.duocuc.veranum_coreusuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UsuarioService {

private final UsuarioRepository usuarioRepository;
private final RolRepository rolRepository;
private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

public Usuario crearUsuario(UsuarioRequest request) {
    if(request == null){
        log.error("Error: Intentaron enviar una solicitud de registro vacía o nula.");
        throw new IllegalArgumentException("La solicitud de registro no puede estar vacía.");
    }

    log.info("Iniciando el registro para el nuevo usuario con RUT: {}", request.getRut());


    if(usuarioRepository.findByRut(request.getRut()).isPresent()) {
        log.warn("El RUT: {} ya está registrado en el sistema. Deteniendo el proceso.", request.getRut());
        throw new RuntimeException("El RUT ya se encuentra registrado.");
    }

    if(usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
        log.warn("El Email: {} ya existe en la base de datos.", request.getEmail());
        throw new RuntimeException("El Email ya se encuentra registrado.");
    }


    Rol rol = rolRepository.findById(request.getRolId()).orElseThrow(() -> {
        log.warn("No se pudo encontrar el rol con el ID: {}", request.getRolId());
        return new RuntimeException("El Rol especificado no existe.");
    });


    try {
        Usuario usuario = new Usuario();
        usuario.setRut(request.getRut());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());
        usuario.setRol(rol);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario con RUT: {} creado y guardado exitosamente con el ID: {}",request.getRut(),usuarioGuardado.getId());
        return usuarioGuardado;

    }catch(Exception e) {
        log.error("Fallo inesperado al intentar guardar el usuario en MySQL: {}", e.getMessage());
        throw new RuntimeException("Ocurrió un error interno al intentar registrar el usuario en la base de datos.");
    }
    }
public List<Usuario> obtenerTodos() {
    log.info("Consultando la lista completa de usuarios...");
    List<Usuario> usuarios = usuarioRepository.findAll();
    if (usuarios.isEmpty()) {
        log.warn("La consulta se realizó, pero no hay usuarios registrados en la base de datos.");
    }return usuarios;
    }

public Usuario obtenerPorId(Long id) {
    log.info("Buscando al usuario con ID: {}", id);
    return usuarioRepository.findById(id).orElseThrow(() -> {
        log.warn("La búsqueda falló: No existe ningún usuario con el ID {}", id);
        return new RuntimeException("Usuario no encontrado con ID: " + id);
    }
    );
    }

public Usuario bloquearUsuario(Long id) {
    log.info("Iniciando proceso para bloquear al usuario con ID: {}", id);
    Usuario usuario = obtenerPorId(id);


    if (usuario.isBloqueado()) {

        log.warn("El usuario con ID: {} ya estaba bloqueado desde antes.", id);
        throw new RuntimeException("El usuario ya se encuentra bloqueado.");
    }

    usuario.setBloqueado(true);
    Usuario usuarioBloqueado = usuarioRepository.save(usuario);
    log.info("El usuario con ID {} fue bloqueado exitosamente.", id);

       return usuarioBloqueado;
}


public boolean existePorRut(String rut) {
    log.info("Verificando si el RUT: {} existe (Petición recibida desde otro microservicio)", rut);
    boolean existe = usuarioRepository.findByRut(rut).isPresent();

    if (existe) {
        log.info("Confirmado: El RUT {} sí existe en nuestros registros.", rut);
    } else {
        log.warn("Alerta: El RUT {} no existe en la base de datos.", rut);
    }

    return existe;


}

    public Usuario actualizarUsuario(Long id, UsuarioRequest request) {
        log.info("Iniciando actualización general para el usuario ID: {}", id);
        Usuario usuario = obtenerPorId(id);


        usuarioRepository.findByRut(request.getRut()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new IllegalStateException("Conflicto: El RUT ya está en uso por otro usuario.");
            }
        });


        usuarioRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new IllegalStateException("Conflicto: El correo ya está en uso por otro usuario.");
            }
        });

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new EntityNotFoundException("El Rol especificado no existe."));

        usuario.setRut(request.getRut());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());
        usuario.setRol(rol);

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        log.info("Usuario ID: {} actualizado correctamente.", id);
        return usuarioActualizado;
    }


    public void eliminarUsuario(Long id) {
        log.info("Iniciando eliminación del usuario ID: {}", id);
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
        log.info("Usuario ID: {} eliminado correctamente de la base de datos.", id);
    }


}
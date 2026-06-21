package cl.duocuc.veranum_coreusuario.repository;
import cl.duocuc.veranum_coreusuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByRut(String rut);

}

package cl.duocuc.veranum_coreusuario.repository;

import cl.duocuc.veranum_coreusuario.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
}
package cl.duocuc.veranum_corereserva.repository;

import cl.duocuc.veranum_corereserva.model.Habitacion;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

}

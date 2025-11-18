package ar.edu.huergo.rlgastos.billetin.repository.tarea;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.edu.huergo.rlgastos.billetin.entity.tarea.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
}


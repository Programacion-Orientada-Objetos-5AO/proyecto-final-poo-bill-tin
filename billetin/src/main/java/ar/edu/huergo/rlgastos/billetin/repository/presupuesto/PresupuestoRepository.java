package ar.edu.huergo.rlgastos.billetin.repository.presupuesto;

import ar.edu.huergo.rlgastos.billetin.entity.presupuesto.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

}
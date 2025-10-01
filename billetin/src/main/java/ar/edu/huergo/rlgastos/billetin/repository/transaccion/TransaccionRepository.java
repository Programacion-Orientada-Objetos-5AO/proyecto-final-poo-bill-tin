
package ar.edu.huergo.rlgastos.billetin.repository.transaccion;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;


@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long>{
    List<Transaccion> findByFechaBetween(LocalDate inicio, LocalDate fin);
   
}

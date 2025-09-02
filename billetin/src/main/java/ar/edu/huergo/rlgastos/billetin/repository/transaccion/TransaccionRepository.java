
package ar.edu.huergo.rlgastos.billetin.repository.transaccion;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;


@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long>{
   
}

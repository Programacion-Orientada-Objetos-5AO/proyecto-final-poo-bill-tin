
package ar.edu.huergo.rlgastos.billetin.repository.moneda;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import ar.edu.huergo.rlgastos.billetin.entity.moneda.Moneda;



@Repository
public interface MonedaRepository extends JpaRepository<Moneda, Long>{
   
}

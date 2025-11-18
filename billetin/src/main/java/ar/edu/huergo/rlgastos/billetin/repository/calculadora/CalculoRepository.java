package ar.edu.huergo.rlgastos.billetin.repository.calculadora;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.rlgastos.billetin.entity.calculadora.Calculo;
import java.util.List;



@Repository
public interface CalculoRepository extends JpaRepository<Calculo, Long>{
        List<Calculo> findByParametros(Double parametro1, Double parametro2);
        List<Calculo> findByUsuarioId(Long usuarioId);
}
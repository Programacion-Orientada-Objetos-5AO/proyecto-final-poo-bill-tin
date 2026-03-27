package ar.edu.huergo.rlgastos.billetin.repository.objetivo;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ar.edu.huergo.rlgastos.billetin.entity.objetivo.Objetivo;

@Repository
public interface ObjetivoRepository extends JpaRepository<Objetivo, Long> {
    

}
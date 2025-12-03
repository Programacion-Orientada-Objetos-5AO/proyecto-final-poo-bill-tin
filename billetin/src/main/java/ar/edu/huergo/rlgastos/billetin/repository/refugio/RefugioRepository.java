package ar.edu.huergo.rlgastos.billetin.repository.refugio;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.rlgastos.billetin.entity.refugio.Refugio;


@Repository
public interface RefugioRepository extends JpaRepository<Refugio, Long>{
   List<Refugio> findByTipo(String tipo);
}
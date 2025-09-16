package ar.edu.huergo.rlgastos.billetin.repository.categoria;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.rlgastos.billetin.entity.categoria.Categoria;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
   
}

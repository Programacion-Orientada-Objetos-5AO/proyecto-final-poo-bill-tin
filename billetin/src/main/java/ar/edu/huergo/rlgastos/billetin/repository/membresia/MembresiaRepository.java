package ar.edu.huergo.rlgastos.billetin.repository.membresia;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.rlgastos.billetin.entity.membresia.Membresia;

@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Long> {
    Optional<Membresia> findByNombreIgnoreCase(String nombre);
}

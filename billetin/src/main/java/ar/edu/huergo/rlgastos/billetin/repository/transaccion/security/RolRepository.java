package ar.edu.huergo.rlgastos.billetin.repository.transaccion.security;

import ar.edu.huergo.rlgastos.billetin.entity.security.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    // Buscar rol por nombre
    Optional<Rol> findByNombre(String nombre);
}
package ar.edu.huergo.rlgastos.billetin.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.rlgastos.billetin.entity.security.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    // Buscar rol por nombre
    Optional<Rol> findByNombre(String nombre);
}
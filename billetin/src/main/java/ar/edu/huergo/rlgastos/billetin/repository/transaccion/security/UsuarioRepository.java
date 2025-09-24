package ar.edu.huergo.rlgastos.billetin.repository.transaccion.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por username (para login)
    Optional<Usuario> findByUsername(String username);
    
    // Verificar si existe un username (para registro)
    boolean existsByUsername(String username);
    
    // NUEVO: Buscar por email (para verificar duplicados)
    Optional<Usuario> findByEmail(String email);
    
    // NUEVO: Verificar si existe un email (alternativa más eficiente)
    boolean existsByEmail(String email);
}
package ar.edu.huergo.rlgastos.billetin.repository.transaccion.security;

import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por username (para login)
    Optional<Usuario> findByUsername(String username);
    
    // Verificar si existe un username (para registro)
    boolean existsByUsername(String username);
}
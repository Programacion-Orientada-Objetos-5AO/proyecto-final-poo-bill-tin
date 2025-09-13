package ar.edu.huergo.rlgastos.billetin.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
     Optional<Usuario> findByUsername(String username);
   
    boolean existsByUsername(String username);
}
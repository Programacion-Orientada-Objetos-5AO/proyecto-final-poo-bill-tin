package ar.edu.huergo.rlgastos.billetin.service.security;


import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.entity.membresia.Membresia;
import ar.edu.huergo.rlgastos.billetin.entity.security.Rol;
import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import ar.edu.huergo.rlgastos.billetin.repository.membresia.MembresiaRepository;
import ar.edu.huergo.rlgastos.billetin.repository.security.RolRepository;
import ar.edu.huergo.rlgastos.billetin.repository.security.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final MembresiaRepository membresiaRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario registrar(Usuario usuario, String password, String verificacionPassword, String membresiaNombre) {
        if (!password.equals(verificacionPassword)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        usuario.setPassword(passwordEncoder.encode(password));
        Rol rolCliente = rolRepository.findByNombre("CLIENTE").orElseThrow(() -> new IllegalArgumentException("Rol 'CLIENTE' no encontrado"));
        usuario.setRoles(Set.of(rolCliente));

        String membresiaNormalizada = membresiaNombre != null ? membresiaNombre.trim() : null;
        if (membresiaNormalizada == null || membresiaNormalizada.isEmpty()) {
            throw new IllegalArgumentException("La membresía es requerida");
        }

        Membresia membresia = membresiaRepository.findByNombreIgnoreCase(membresiaNormalizada)
                .orElseThrow(() -> new IllegalArgumentException("Membresía '" + membresiaNombre + "' no encontrada"));
        usuario.setMembresia(membresia);
        return usuarioRepository.save(usuario);
    }
}


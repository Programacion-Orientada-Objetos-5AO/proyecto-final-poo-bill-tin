package ar.edu.huergo.rlgastos.billetin.service.security;

import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import ar.edu.huergo.rlgastos.billetin.repository.transaccion.security.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado para cargar usuarios desde la base de datos.
 * Spring Security usa este servicio para autenticar usuarios.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Método que Spring Security llama para cargar un usuario por su username.
     * 
     * @param username El nombre de usuario a buscar
     * @return UserDetails El usuario encontrado (tu entidad Usuario implementa UserDetails)
     * @throws UsernameNotFoundException Si no se encuentra el usuario
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "Usuario no encontrado con username: " + username));
        
        System.out.println("Usuario cargado: " + username + " con roles: " + usuario.getRoles());
        
        return usuario; // Tu entidad Usuario ya implementa UserDetails
    }
}

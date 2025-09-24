package ar.edu.huergo.rlgastos.billetin.service.objetivo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.entity.Objetivo;
import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import ar.edu.huergo.rlgastos.billetin.repository.objetivo.ObjetivoRepository;
import ar.edu.huergo.rlgastos.billetin.repository.security.UsuarioRepository;

@Service
public class ObjetivoSecurityService {

    @Autowired
    private ObjetivoRepository objetivoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean puedeAccederObjetivo(Long objetivoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Si es admin, puede acceder a todo
        if (esAdmin(auth)) {
            return true;
        }
        
        // Si es cliente, solo puede acceder a sus propios objetivos
        return objetivoRepository.findById(objetivoId)
                .map(objetivo -> objetivo.getUsuario().getUsername().equals(username))
                .orElse(false);
    }

    public boolean puedeAccederObjetivosUsuario(Long usuarioId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Si es admin, puede acceder a todo
        if (esAdmin(auth)) {
            return true;
        }
        
        // Si es cliente, solo puede acceder a sus propios objetivos
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getUsername().equals(username))
                .orElse(false);
    }

    public boolean puedeCrearObjetivo(Long usuarioId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Si es admin, puede crear para cualquier usuario
        if (esAdmin(auth)) {
            return true;
        }
        
        // Si es cliente, solo puede crear para sí mismo
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getUsername().equals(username))
                .orElse(false);
    }

    private boolean esAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
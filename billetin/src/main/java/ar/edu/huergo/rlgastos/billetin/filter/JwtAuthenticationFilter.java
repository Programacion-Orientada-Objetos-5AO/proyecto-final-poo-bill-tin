package ar.edu.huergo.rlgastos.billetin.filter;

import ar.edu.huergo.rlgastos.billetin.service.security.CustomUserDetailsService;
import ar.edu.huergo.rlgastos.billetin.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que se ejecuta en cada petición HTTP para validar tokens JWT.
 * Si encuentra un token válido, autentica automáticamente al usuario.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Método que se ejecuta en cada petición HTTP
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {

        // 1. Obtener el header Authorization
        final String authorizationHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwt = null;

        // 2. Verificar si el header contiene un token Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Remover "Bearer " del inicio
            
            try {
                // 3. Extraer el username del token
                username = jwtUtil.extractUsername(jwt);
                log.debug("🔍 Token encontrado para usuario: {}", username);
            } catch (Exception e) {
                log.warn("⚠️ No se pudo extraer el username del token: {}", e.getMessage());
            }
        } else {
            // Log solo para endpoints protegidos
            String path = request.getRequestURI();
            if (!isPublicEndpoint(path)) {
                log.debug("🔓 No se encontró token en la petición a: {}", path);
            }
        }

        // 4. Si tenemos username y no hay autenticación previa, validar el token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            try {
                // 5. Cargar los detalles del usuario
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 6. Validar el token
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    // 7. Crear el objeto de autenticación
                    UsernamePasswordAuthenticationToken authenticationToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );
                    
                    // 8. Agregar detalles de la petición
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 9. Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    
                    log.debug("✅ Usuario autenticado: {} para petición: {}", 
                             username, request.getRequestURI());
                } else {
                    log.warn("❌ Token inválido para usuario: {}", username);
                }
                
            } catch (Exception e) {
                log.error("❌ Error durante la autenticación JWT: {}", e.getMessage());
            }
        }

        // 10. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Verifica si un endpoint es público (no requiere autenticación)
     */
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/auth/") || 
               path.startsWith("/h2-console/") ||
               path.equals("/favicon.ico");
    }

    /**
     * Determina si este filtro debe ejecutarse para la petición actual.
     * Por defecto se ejecuta para todas las peticiones.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // El filtro se ejecuta para todas las peticiones
        // La lógica de endpoints públicos/privados se maneja en doFilterInternal
        return false;
    }
}
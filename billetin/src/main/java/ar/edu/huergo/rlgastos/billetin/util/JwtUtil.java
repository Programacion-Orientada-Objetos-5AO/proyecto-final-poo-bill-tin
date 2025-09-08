package ar.edu.huergo.rlgastos.billetin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utilidad para manejar tokens JWT (JSON Web Tokens).
 * Se encarga de generar, validar y extraer información de los tokens.
 */
@Slf4j
@Component
public class JwtUtil {

    // Clave secreta para firmar tokens (se puede configurar en application.properties)
    @Value("${jwt.secret:mySecretKey12345678901234567890123456789012345678901234567890}")
    private String secret;

    // Tiempo de expiración en milisegundos (24 horas por defecto)
    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    /**
     * Genera la clave secreta para firmar tokens
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un token JWT para un usuario autenticado
     * 
     * @param userDetails Los detalles del usuario autenticado
     * @return String El token JWT generado
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // Agregar los roles del usuario al token
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
                
        log.info("Generando token para usuario: {}", userDetails.getUsername());
        
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Crea el token JWT con los claims especificados
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)                                          // Datos adicionales
                .setSubject(subject)                                        // Username
                .setIssuedAt(new Date(System.currentTimeMillis()))          // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)        // Firmar con clave secreta
                .compact();
    }

    /**
     * Extrae el username del token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico del token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error al extraer claims del token: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Verifica si el token ha expirado
     */
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.warn("Error al verificar expiración del token: {}", e.getMessage());
            return true; 
        }
    }

    /**
     * Valida un token JWT
     * 
     * @param token El token a validar
     * @param userDetails Los detalles del usuario
     * @return Boolean true si el token es válido
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            
            if (isValid) {
                log.debug("Token válido para usuario: {}", username);
            } else {
                log.warn("Token inválido para usuario: {}", username);
            }
            
            return isValid;
        } catch (Exception e) {
            log.error("Error al validar token: {}", e.getMessage());
            return false;
        }
    }
}
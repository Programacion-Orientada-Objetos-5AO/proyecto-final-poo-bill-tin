package ar.edu.huergo.rlgastos.billetin.dto.security;


public record LoginResponseDTO(
    String token,        // El JWT token que el usuario debe usar
    String username,     // Confirmar el usuario logueado
    String email,        // Email del usuario
    String nombre,       // Nombre completo
    String apellido      // Apellido
) {
}
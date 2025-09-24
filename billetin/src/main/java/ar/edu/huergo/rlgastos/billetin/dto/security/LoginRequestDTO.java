package ar.edu.huergo.rlgastos.billetin.dto.security;

import jakarta.validation.constraints.NotBlank;


public record LoginRequestDTO(
    @NotBlank(message = "El username es obligatorio")
    String username,
    
    @NotBlank(message = "La contraseña es obligatoria")
    String password
) {
}
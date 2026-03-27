package ar.edu.huergo.rlgastos.billetin.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistrarDTO(
        @NotBlank(message = "El nombre es requerido")
        String nombre,

        @NotBlank(message = "El nombre de usuario es requerido")
        @Email(message = "El nombre de usuario debe ser un email válido") 
        String username,

        @NotBlank(message = "La contraseña es requerida") 
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{16,}$", 
            message = "La contraseña debe tener al menos 16 caracteres, una mayúscula, una minúscula, un número y un carácter especial"
        )
        String password,

        @NotBlank(message = "La verificación de contraseña es requerida")
        String verificacionPassword,

        @NotBlank(message = "La membresía es requerida")
        String membresia
) {}

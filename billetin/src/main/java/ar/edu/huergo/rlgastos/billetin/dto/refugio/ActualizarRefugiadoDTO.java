package ar.edu.huergo.rlgastos.billetin.dto.refugio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarRefugiadoDTO(
        @NotBlank(message = "El nombre del animal es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @NotBlank(message = "El tipo del animal es obligatorio")
        @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
        String tipo,

        @NotNull(message = "La edad es obligatoria")
        Integer edad,

        @NotBlank(message = "La adopcion del animal es obligatoria")
        Boolean adoptado
){
    
}

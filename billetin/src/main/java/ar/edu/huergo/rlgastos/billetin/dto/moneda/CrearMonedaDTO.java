package ar.edu.huergo.rlgastos.billetin.dto.moneda;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearMonedaDTO(
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 3, message = "El código debe tener máximo 3 caracteres")
    String codigo,

    @NotBlank(message = "El nombre es obligatorio")
    String nombre
) {}

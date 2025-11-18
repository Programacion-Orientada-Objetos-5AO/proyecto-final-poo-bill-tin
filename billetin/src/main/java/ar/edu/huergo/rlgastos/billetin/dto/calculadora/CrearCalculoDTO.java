package ar.edu.huergo.rlgastos.billetin.dto.calculadora;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearCalculoDTO(

        @NotBlank(message = "El nombre de la operacion es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String operacion,

        @NotNull(message = "El parametro1 es obligatorio")
        Double parametro1,

        @NotNull(message = "El parametro2 es obligatorio")
        Double parametro2
        
){}

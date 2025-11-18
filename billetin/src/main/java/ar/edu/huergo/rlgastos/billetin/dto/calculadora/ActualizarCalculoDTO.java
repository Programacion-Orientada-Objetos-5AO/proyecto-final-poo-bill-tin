package ar.edu.huergo.rlgastos.billetin.dto.calculadora;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActualizarCalculoDTO(

        @NotBlank(message = "El nombre de la operacion es obligatorio")
        String operacion,

        @NotNull(message = "El parametro 1 es obligatorio")
        Double parametro1,

        @NotNull(message = "El parametro2 es obligatorio")
        Double parametro2

) {
}
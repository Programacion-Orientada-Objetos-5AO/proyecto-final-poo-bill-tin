package ar.edu.huergo.rlgastos.billetin.dto.meta;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ActualizarMetaDTO (

        @NotBlank(message = "El nombre de la meta es obligatorio")
        String nombre,

        @NotNull(message = "El monto meta es obligatorio")
        Double montoObjetivo,
        
        @NotNull(message = "El plazo es obligatorio")
        LocalDate fechaLimite
){}

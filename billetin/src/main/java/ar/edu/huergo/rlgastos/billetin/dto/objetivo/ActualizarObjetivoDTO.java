
package ar.edu.huergo.rlgastos.billetin.dto.objetivo;

import java.time.LocalDate;

import ar.edu.huergo.rlgastos.billetin.entity.objetivo.EstadoObjetivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActualizarObjetivoDTO(

        @NotBlank(message = "El nombre del objetivo es obligatorio")
        String nombre,

        @NotNull(message = "El monto meta es obligatorio")
        Double montoMeta,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin,

        @NotNull(message = "El estado es obligatorio")
        EstadoObjetivo estado
) {
}



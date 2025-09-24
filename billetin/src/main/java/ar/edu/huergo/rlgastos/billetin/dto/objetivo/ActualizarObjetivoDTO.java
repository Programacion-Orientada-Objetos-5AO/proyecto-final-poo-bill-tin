package ar.edu.huergo.rlgastos.billetin.dto.objetivo;

import java.time.LocalDate;

import ar.edu.huergo.rlgastos.billetin.entity.EstadoObjetivo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarObjetivoDTO(
        @NotBlank(message = "El nombre del objetivo es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre del objetivo debe tener entre 2 y 100 caracteres")
        String nombre,

        @NotNull(message = "El monto meta es obligatorio")
        @DecimalMin(value = "1000.0", message = "El monto meta debe ser mayor o igual a 1000 pesos")
        Double montoMeta,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin,

        @NotNull(message = "El estado del objetivo es obligatorio")
        EstadoObjetivo estado
) {
}
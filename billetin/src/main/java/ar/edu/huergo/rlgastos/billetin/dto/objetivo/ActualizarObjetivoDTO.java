<<<<<<< HEAD
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
=======

package ar.edu.huergo.rlgastos.billetin.dto.objetivo;

import ar.edu.huergo.rlgastos.billetin.entity.objetivo.EstadoObjetivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ActualizarObjetivoDTO(

        @NotBlank(message = "El nombre del objetivo es obligatorio")
        String nombre,

        @NotNull(message = "El monto meta es obligatorio")
>>>>>>> 898dde42ce9c8b08e9d6f8d6cd36003825b6adbe
        Double montoMeta,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin,

<<<<<<< HEAD
        @NotNull(message = "El estado del objetivo es obligatorio")
        EstadoObjetivo estado
) {
}
=======
        @NotNull(message = "El estado es obligatorio")
        EstadoObjetivo estado
) {
}


>>>>>>> 898dde42ce9c8b08e9d6f8d6cd36003825b6adbe

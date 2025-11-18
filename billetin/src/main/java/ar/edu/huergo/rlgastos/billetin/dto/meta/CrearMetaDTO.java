package ar.edu.huergo.rlgastos.billetin.dto.meta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CrearMetaDTO ( 
    
    @NotBlank(message = "El nombre de la meta es obligatorio")
    @Size(max = 100, message = "El nombre de la meta no puede exceder los 100 caracteres")
    String nombre,

    @NotNull(message = "El monto meta es obligatorio")
    @DecimalMin(value = "1000.0", message = "El monto meta debe ser mayor o igual a 1000")
    Double montoObjetivo,

    @NotNull(message = "El plazo es obligatorio")
    LocalDate fechaLimite
){}

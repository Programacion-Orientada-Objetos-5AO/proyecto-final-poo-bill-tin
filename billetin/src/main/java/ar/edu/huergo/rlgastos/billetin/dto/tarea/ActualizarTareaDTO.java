package ar.edu.huergo.rlgastos.billetin.dto.tarea;

import jakarta.validation.constraints.NotBlank;

public record ActualizarTareaDTO ( 
    
    @NotBlank(message = "El título es obligatorio")
    String titulo,

    @NotBlank(message = "La descripción es obligatoria")
    String descripcion,

    @NotBlank(message = "El creador es obligatorio")
    String creador
){}

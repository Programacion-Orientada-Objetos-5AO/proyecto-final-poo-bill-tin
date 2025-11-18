package ar.edu.huergo.rlgastos.billetin.dto.tarea;

import jakarta.validation.constraints.NotBlank;


public record CrearTareaDTO(

        @NotBlank(message = "El titulo de la tarea es obligatorio")
        String titulo,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @NotBlank(message = "El nombre del creador es obligatorio")
        String creador

){}

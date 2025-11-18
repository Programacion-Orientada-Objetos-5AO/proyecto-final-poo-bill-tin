package ar.edu.huergo.rlgastos.billetin.dto.tarea;

public record MostrarTareaDTO(

    Long id,
    String titulo,
    String descripcion,
    String creador,
    boolean completada 
    
){}

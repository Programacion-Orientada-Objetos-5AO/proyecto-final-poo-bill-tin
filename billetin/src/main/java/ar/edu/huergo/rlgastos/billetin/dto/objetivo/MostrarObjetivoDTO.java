package ar.edu.huergo.rlgastos.billetin.dto.objetivo;

import java.time.LocalDate;

import ar.edu.huergo.rlgastos.billetin.entity.EstadoObjetivo;

public record MostrarObjetivoDTO(
        Long idObjetivo,
        String nombre,
        Double montoMeta,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoObjetivo estado,
        String nombreUsuario
) {
}

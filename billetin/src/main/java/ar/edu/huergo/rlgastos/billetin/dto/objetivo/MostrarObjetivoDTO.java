package ar.edu.huergo.rlgastos.billetin.dto.objetivo;

import java.time.LocalDate;

import ar.edu.huergo.rlgastos.billetin.entity.objetivo.EstadoObjetivo;

public record MostrarObjetivoDTO(
        String nombre,
        Double montoMeta,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoObjetivo estado
) {
}

package ar.edu.huergo.rlgastos.billetin.dto.meta;

import java.time.LocalDate;

public record MostrarMetaDTO (
    Long id,
    Double montoObjetivo,
    LocalDate fechaLimite
){}

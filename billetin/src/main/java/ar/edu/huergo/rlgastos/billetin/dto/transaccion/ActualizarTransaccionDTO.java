package ar.edu.huergo.rlgastos.billetin.dto.transaccion;

import java.time.LocalDate;

public record ActualizarTransaccionDTO(
        Double monto,
        String descripcion,
        LocalDate fecha,
        Long idUsuario,
        Long idCategoria
) {}

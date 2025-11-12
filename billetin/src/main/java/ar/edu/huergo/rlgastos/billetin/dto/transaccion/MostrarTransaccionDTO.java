
package ar.edu.huergo.rlgastos.billetin.dto.transaccion;


import java.time.LocalDate;



public record MostrarTransaccionDTO(
        Long id,
        Double monto,
        LocalDate fecha,
        String usuarioNombre,
        String categoriaNombre,
        String monedaNombre
){
    
}



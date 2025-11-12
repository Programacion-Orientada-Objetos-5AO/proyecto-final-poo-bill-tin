
package ar.edu.huergo.rlgastos.billetin.dto.transaccion;


import java.time.LocalDate;



public record CrearTransaccionDTO(Long id,String nombreUsuario, Double monto,String descripcion,LocalDate fecha,Long idUsuario,Long idCategoria,Long idMoneda) {
}

   

    
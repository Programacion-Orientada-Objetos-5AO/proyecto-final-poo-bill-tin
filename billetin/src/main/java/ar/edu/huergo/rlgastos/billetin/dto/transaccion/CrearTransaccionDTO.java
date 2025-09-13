
package ar.edu.huergo.rlgastos.billetin.dto.transaccion;


import java.time.LocalDate;

import ar.edu.huergo.rlgastos.billetin.entity.TipoTransaccion;


public record CrearTransaccionDTO(Long id, String nombreUsuario,TipoTransaccion tipo,Double monto,String descripcion,LocalDate fecha) {
}
   

    



package ar.edu.huergo.rlgastos.billetin.dto.transaccion;


import java.time.LocalDate;

import ar.edu.huergo.rlgastos.billetin.entity.TipoTransaccion;


public record MostrarTransaccionDTO (Double monto, TipoTransaccion tipo, LocalDate fecha){
   
}



package ar.edu.huergo.rlgastos.billetin.dto.presupuesto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PresupuestoDTO {
    private Long id;
    private String nombre;  
    private Double montoTotal;
    private String periodo;
}

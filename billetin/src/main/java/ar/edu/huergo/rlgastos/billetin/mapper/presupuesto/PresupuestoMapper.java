package ar.edu.huergo.rlgastos.billetin.mapper.presupuesto;

import ar.edu.huergo.rlgastos.billetin.dto.presupuesto.PresupuestoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.presupuesto.Presupuesto;

public class PresupuestoMapper {
    
    public static PresupuestoDTO toDTO(Presupuesto presupuesto) {
        return new PresupuestoDTO(
            presupuesto.getId(),
            presupuesto.getNombre(),
            presupuesto.getMontoTotal(),
            presupuesto.getPeriodo()
        );
    }

    public static Presupuesto toEntity(PresupuestoDTO presupuestoDTO) {
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setId(presupuestoDTO.getId());
        presupuesto.setNombre(presupuestoDTO.getNombre());
        presupuesto.setMontoTotal(presupuestoDTO.getMontoTotal());
        presupuesto.setPeriodo(presupuestoDTO.getPeriodo());
        return presupuesto;
    };
}

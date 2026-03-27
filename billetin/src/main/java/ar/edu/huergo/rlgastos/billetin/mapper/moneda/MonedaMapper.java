package ar.edu.huergo.rlgastos.billetin.mapper.moneda;

import java.util.List;

import org.springframework.stereotype.Component;

import ar.edu.huergo.rlgastos.billetin.dto.moneda.ActualizarMonedaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.moneda.CrearMonedaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.moneda.MostrarMonedaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.moneda.Moneda;

@Component
public class MonedaMapper {

    public Moneda toEntity(CrearMonedaDTO dto) {
        Moneda moneda = new Moneda();
        moneda.setCodigo(dto.codigo());
        moneda.setNombre(dto.nombre());
        return moneda;
    }

    public MostrarMonedaDTO toMostrarDTO(Moneda moneda) {
        return new MostrarMonedaDTO(
            moneda.getIdMoneda(),
            moneda.getCodigo(),
            moneda.getNombre()
        );
    }

    public void actualizarEntity(Moneda moneda, ActualizarMonedaDTO dto) {
        moneda.setCodigo(dto.codigo());
        moneda.setNombre(dto.nombre());
    }

    public List<MostrarMonedaDTO> toMostrarDtoList(List<Moneda> monedas) {
        return monedas.stream().map(this::toMostrarDTO).toList();
    }
}

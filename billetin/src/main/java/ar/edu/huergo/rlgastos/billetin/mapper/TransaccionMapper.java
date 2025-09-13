package ar.edu.huergo.rlgastos.billetin.mapper;


import java.util.List;

import org.springframework.stereotype.Component;

import ar.edu.huergo.rlgastos.billetin.dto.transaccion.ActualizarTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.dto.transaccion.CrearTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.dto.transaccion.MostrarTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;


@Component
public class TransaccionMapper {

    public Transaccion toEntity(CrearTransaccionDTO transaccionDto) {
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario(transaccionDto.nombreUsuario());
        transaccion.setMonto(transaccionDto.monto());
        transaccion.setDescripcion(transaccionDto.descripcion());
        transaccion.setTipo(transaccionDto.tipo());
        transaccion.setFecha(transaccionDto.fecha());
        return transaccion;
    }

    public MostrarTransaccionDTO toMostrarDTO(Transaccion transaccion) {
        return new MostrarTransaccionDTO(
                transaccion.getMonto(),
                transaccion.getTipo(),
                transaccion.getFecha()
        );
    }

    public List<MostrarTransaccionDTO> toMostrarDtoList(List<Transaccion> transaccion) {
        return transaccion.stream()
                .map(this::toMostrarDTO)
                .toList();
    }   
}


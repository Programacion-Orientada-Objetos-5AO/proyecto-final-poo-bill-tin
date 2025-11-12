package ar.edu.huergo.rlgastos.billetin.mapper.membresia;

import java.util.List;

import org.springframework.stereotype.Component;

import ar.edu.huergo.rlgastos.billetin.dto.membresia.CrearMembresiaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.membresia.MostrarMembresiaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.membresia.Membresia;

@Component
public class MembresiaMapper {

    public Membresia toEntity(CrearMembresiaDTO dto) {
        Membresia membresia = new Membresia();
        membresia.setNombre(dto.nombre());
        membresia.setPrecio(dto.precio());
        membresia.setBeneficios(dto.beneficios());
        membresia.setDuracion(dto.duracion());
        return membresia;
    }

    public MostrarMembresiaDTO toMostrarDTO(Membresia membresia) {
        return new MostrarMembresiaDTO(
            membresia.getIdMembresia(),
            membresia.getNombre(),
            membresia.getPrecio(),
            membresia.getBeneficios(),
            membresia.getDuracion()
        );
    }

    public List<MostrarMembresiaDTO> toMostrarDtoList(List<Membresia> membresias) {
        return membresias.stream().map(this::toMostrarDTO).toList();
    }
}

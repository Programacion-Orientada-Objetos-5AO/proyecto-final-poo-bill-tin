package ar.edu.huergo.rlgastos.billetin.mapper.refugio;

import java.util.List;
import org.springframework.stereotype.Component;


import ar.edu.huergo.rlgastos.billetin.dto.refugio.ActualizarRefugiadoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.refugio.CrearRefugiadoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.refugio.MostrarRefugiadoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.refugio.Refugio;


@Component
public class RefugioMapper {

    
    public Refugio toEntity(CrearRefugiadoDTO dto) {
        Refugio refugiado = new Refugio();
        refugiado.setNombre(dto.nombre());
        refugiado.setTipo(dto.tipo());
        refugiado.setEdad(dto.edad());

        return refugiado;
    }
    
    public MostrarRefugiadoDTO toMostrarDTO(Refugio refugiado) {
        return new MostrarRefugiadoDTO(
            refugiado.getIdRefugiado(),
            refugiado.getNombre(),
            refugiado.getTipo(),
            refugiado.getEdad(),
            refugiado.getAdoptado()
        );
    }

    public void ActualizarEntityFromDTO(ActualizarRefugiadoDTO dto, Refugio refugiado) {
        refugiado.setNombre(dto.nombre());
        refugiado.setTipo(dto.tipo());
        refugiado.setEdad(dto.edad());
        refugiado.setAdoptado(dto.adoptado());
    }

    public List<MostrarRefugiadoDTO> toMostrarDtoList(List<Refugio> refugiados) {
        return refugiados.stream()
                .map(this::toMostrarDTO)
                .toList();
    }


}

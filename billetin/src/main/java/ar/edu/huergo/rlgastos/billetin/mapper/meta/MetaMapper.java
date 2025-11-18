package ar.edu.huergo.rlgastos.billetin.mapper.meta;

import org.springframework.stereotype.Component;
import ar.edu.huergo.rlgastos.billetin.dto.meta.CrearMetaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.meta.MostrarMetaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.meta.ActualizarMetaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.meta.Meta;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MetaMapper {
    public Meta toEntity(CrearMetaDTO dto) {
        Meta meta = new Meta();
        meta.setMontoObjetivo(dto.montoObjetivo());
        meta.setFechaLimite(dto.fechaLimite());
        return meta;
    }

    public MostrarMetaDTO toMostrarDTO(Meta meta) {
        return new MostrarMetaDTO(
            meta.getId(),
            meta.getMontoObjetivo(),
            meta.getFechaLimite()
        );
    }

    public void actualizarEntity(Meta meta, ActualizarMetaDTO dto) {
        meta.setMontoObjetivo(dto.montoObjetivo());
        meta.setFechaLimite(dto.fechaLimite());
    }

    public List <MostrarMetaDTO> toMostrarDTOList(List<Meta> metas) {
        return metas.stream()
                .map(this::toMostrarDTO)
                .collect(Collectors.toList());
    }

}

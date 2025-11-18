package ar.edu.huergo.rlgastos.billetin.mapper.tarea;

import java.util.List;

import org.springframework.stereotype.Component;

import ar.edu.huergo.rlgastos.billetin.dto.tarea.ActualizarTareaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.tarea.MostrarTareaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.tarea.CrearTareaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.tarea.Tarea;


@Component
public class TareaMapper {

    public Tarea toEntity(CrearTareaDTO dto) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(dto.titulo());
        tarea.setDescripcion(dto.descripcion());
        tarea.setCreador(dto.creador());
        return tarea;
    }

    public MostrarTareaDTO toMostrarDTO(Tarea tarea) {
        return new MostrarTareaDTO(
            tarea.getId(),
            tarea.getTitulo(),
            tarea.getDescripcion(),
            tarea.getCreador(),
            tarea.isCompletada()
        );
    }

    public void actualizarEntity(Tarea tarea, ActualizarTareaDTO dto) {
        tarea.setTitulo(dto.titulo());
        tarea.setDescripcion(dto.descripcion());
        tarea.setCreador(dto.creador());
    }

    public List<MostrarTareaDTO> toMostrarDtoList(List<Tarea> tareas) {
        return tareas.stream().map(this::toMostrarDTO).toList();
    }

}

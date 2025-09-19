package ar.edu.huergo.rlgastos.billetin.mapper.objetivo;

import java.util.List;

import org.springframework.stereotype.Component;

import ar.edu.huergo.rlgastos.billetin.dto.objetivo.ActualizarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.objetivo.CrearObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.objetivo.MostrarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.Objetivo;
import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;

@Component
public class ObjetivoMapper {

    public Objetivo toEntity(CrearObjetivoDTO dto) {
        Objetivo objetivo = new Objetivo();
        objetivo.setNombre(dto.nombre());
        objetivo.setMontoMeta(dto.montoMeta());
        objetivo.setFechaInicio(dto.fechaInicio());
        objetivo.setFechaFin(dto.fechaFin());
        objetivo.setEstado(dto.estado());
        
        // Crear usuario con el ID para la relación
        Usuario usuario = new Usuario();
        usuario.setId(dto.idUsuario());
        objetivo.setUsuario(usuario);
        
        return objetivo;
    }

    public MostrarObjetivoDTO toMostrarDTO(Objetivo objetivo) {
        return new MostrarObjetivoDTO(
                objetivo.getIdObjetivo(),
                objetivo.getNombre(),
                objetivo.getMontoMeta(),
                objetivo.getFechaInicio(),
                objetivo.getFechaFin(),
                objetivo.getEstado(),
                objetivo.getUsuario().getUsername()
        );
    }

    public void actualizarEntity(Objetivo objetivo, ActualizarObjetivoDTO dto) {
        objetivo.setNombre(dto.nombre());
        objetivo.setMontoMeta(dto.montoMeta());
        objetivo.setFechaInicio(dto.fechaInicio());
        objetivo.setFechaFin(dto.fechaFin());
        objetivo.setEstado(dto.estado());
    }

    public List<MostrarObjetivoDTO> toMostrarDtoList(List<Objetivo> objetivos) {
        return objetivos.stream()
                .map(this::toMostrarDTO)
                .toList();
    }
}
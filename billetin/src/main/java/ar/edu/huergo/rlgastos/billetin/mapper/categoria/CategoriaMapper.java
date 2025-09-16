package ar.edu.huergo.rlgastos.billetin.mapper.categoria;

import java.util.List;

import org.springframework.stereotype.Component;

import ar.edu.huergo.rlgastos.billetin.dto.categoria.ActualizarCategoriaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.categoria.CrearCategoriaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.categoria.MostrarCategoriaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.categoria.Categoria;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CrearCategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.nombre());
        categoria.setTipo(dto.tipo());
        return categoria;
    }

    public MostrarCategoriaDTO toMostrarDTO(Categoria categoria) {
        return new MostrarCategoriaDTO(
            categoria.getIdCategoria(),
            categoria.getNombre(),
            categoria.getTipo()
        );
    }

    public void updateEntityFromDTO(ActualizarCategoriaDTO dto, Categoria categoria) {
        categoria.setNombre(dto.nombre());
        categoria.setTipo(dto.tipo());
    }

    public List<MostrarCategoriaDTO> toMostrarDtoList(List<Categoria> categorias) {
        return categorias.stream()
                .map(this::toMostrarDTO)
                .toList();
    }

    public Object toDTO(List<Categoria> categorias) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toDTO'");
    }
}

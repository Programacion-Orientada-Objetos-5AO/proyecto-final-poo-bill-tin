package ar.edu.huergo.rlgastos.billetin.dto.categoria;

import ar.edu.huergo.rlgastos.billetin.entity.categoria.TipoCategoria;

public record MostrarCategoriaDTO(Long idCategoria, String nombre, TipoCategoria tipo) {
}

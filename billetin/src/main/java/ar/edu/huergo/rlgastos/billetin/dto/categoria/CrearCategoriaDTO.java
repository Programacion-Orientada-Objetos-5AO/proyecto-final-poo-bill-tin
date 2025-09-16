package ar.edu.huergo.rlgastos.billetin.dto.categoria;

import ar.edu.huergo.rlgastos.billetin.entity.categoria.TipoCategoria;

public record CrearCategoriaDTO(Long id, String nombre, TipoCategoria tipo) {
}

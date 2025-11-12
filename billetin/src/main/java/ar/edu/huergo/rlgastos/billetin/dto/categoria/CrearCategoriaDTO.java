package ar.edu.huergo.rlgastos.billetin.dto.categoria;

import ar.edu.huergo.rlgastos.billetin.entity.categoria.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//El campo ID  de este DTO no se usa nunca
public record CrearCategoriaDTO(
        Long id,

        @NotBlank(message = "El nombre de la categoría es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @NotNull(message = "El tipo de categoría es obligatorio")
        TipoCategoria tipo
){
    
}

package ar.edu.huergo.rlgastos.billetin.entity.categoria;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class CategoriaTest {
    @Test
    void testConstructorYGetters() {
        Categoria categoria = new Categoria(1L, "Comida", TipoCategoria.egreso_variable);

        assertEquals(1L, categoria.getIdCategoria());
        assertEquals("Comida", categoria.getNombre());
        assertEquals(TipoCategoria.egreso_variable, categoria.getTipo());
    }

    @Test
    void testSetters() {
        Categoria categoria = new Categoria();

        assertNull(categoria.getIdCategoria());
        assertNull(categoria.getNombre());
        assertNull(categoria.getTipo());

        categoria.setIdCategoria(2L);
        categoria.setNombre("Sueldo");
        categoria.setTipo(TipoCategoria.ingreso_fijo);

        assertEquals(2L, categoria.getIdCategoria());
        assertEquals("Sueldo", categoria.getNombre());
        assertEquals(TipoCategoria.ingreso_fijo, categoria.getTipo());
    }
}

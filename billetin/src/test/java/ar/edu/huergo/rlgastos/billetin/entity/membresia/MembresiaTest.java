package ar.edu.huergo.rlgastos.billetin.entity.membresia;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;


public class MembresiaTest {
    @Test
    void testConstructorYGetters() {
        Membresia membresia = new Membresia(1L, "premium", 20, "acceso a cursos", fecha, null);

        assertEquals(1L, membresia.getIdMembresia());
        assertEquals("Premium", membresia.getNombre());
        assertEquals(999.99, membresia.getPrecio());
        assertEquals("Acceso ilimitado", membresia.getBeneficios());
        assertEquals(fecha, membresia.getDuracion());
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

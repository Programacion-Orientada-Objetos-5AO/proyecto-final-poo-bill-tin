package ar.edu.huergo.rlgastos.billetin.entity.membresia;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;


public class MembresiaTest {
    
    @Test
    public void testConstructorYGetters() {
        Date fecha = new Date();
        Membresia membresia = new Membresia(1L, "Premium", 999.99, "acceso a cursos", fecha, null);

        assertEquals(1L, membresia.getIdMembresia());
        assertEquals("Premium", membresia.getNombre());
        assertEquals(999.99, membresia.getPrecio());
        assertEquals("Acceso ilimitado", membresia.getBeneficios());
        assertEquals(fecha, membresia.getDuracion());
    }

    @Test
    public void testSetters() {
        Membresia membresia = new Membresia();

        Date fecha = new Date();
        membresia.setIdMembresia(2L);
        membresia.setNombre("Básica");
        membresia.setPrecio(199.99);
        membresia.setBeneficios("Acceso limitado");
        membresia.setDuracion(fecha);

        assertEquals(2L, membresia.getIdMembresia());
        assertEquals("Básica", membresia.getNombre());
        assertEquals(199.99, membresia.getPrecio());
        assertEquals("Acceso limitado", membresia.getBeneficios());
        assertEquals(fecha, membresia.getDuracion());
    }
    @Test
    public void testListaUsuariosInicialVacia() {
        Membresia membresia = new Membresia();
        assertNotNull(membresia.getUsuarios());
        assertTrue(membresia.getUsuarios().isEmpty());
    }
}

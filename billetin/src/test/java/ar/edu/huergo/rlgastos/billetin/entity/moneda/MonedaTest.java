package ar.edu.huergo.rlgastos.billetin.entity.moneda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


public class MonedaTest {
    @Test
    public void testConstructorYGetters() {
        Moneda moneda = new Moneda (1L, "USD", "Dolar estadounidense" );
        
        assertEquals(1L, moneda.getIdMoneda());
        assertEquals("USD", moneda.getCodigo());
        assertEquals("Dolar estadounidense", moneda.getNombre());
    }

    @Test
    public void testSetters() {
        Moneda moneda = new Moneda();

        moneda.setIdMoneda(2L);
        moneda.setNombre("Dolar estadounidense");
        moneda.setCodigo("USD");

        assertEquals(2L, moneda.getIdMoneda());
        assertEquals("Dolar estadounidense", moneda.getNombre());
        assertEquals("USD", moneda.getCodigo());

    }
}


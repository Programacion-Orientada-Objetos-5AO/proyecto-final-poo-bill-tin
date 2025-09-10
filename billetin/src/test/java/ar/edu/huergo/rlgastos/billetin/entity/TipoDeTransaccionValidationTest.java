package ar.edu.huergo.rlgastos.billetin.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TipoTransaccionTest {

    @Test
    void deberiaContenerValorEgreso() {
        // When
        TipoTransaccion egreso = TipoTransaccion.Egreso;

        // Then
        assertNotNull(egreso);
        assertEquals("Egreso", egreso.name());
    }

    @Test
    void deberiaContenerValorIngreso() {
        // When
        TipoTransaccion ingreso = TipoTransaccion.Ingreso;

        // Then
        assertNotNull(ingreso);
        assertEquals("Ingreso", ingreso.name());
    }

    @Test
    void deberiaTenerExactamenteDosValores() {
        // When
        TipoTransaccion[] valores = TipoTransaccion.values();

        // Then
        assertEquals(2, valores.length);
    }

    @Test
    void deberiaContenerTodosLosValoresEsperados() {
        // When
        TipoTransaccion[] valores = TipoTransaccion.values();

        // Then
        boolean contieneEgreso = false;
        boolean contieneIngreso = false;

        for (TipoTransaccion tipo : valores) {
            if (tipo == TipoTransaccion.Egreso) {
                contieneEgreso = true;
            } else if (tipo == TipoTransaccion.Ingreso) {
                contieneIngreso = true;
            }
        }

        assertTrue(contieneEgreso, "Debería contener el valor Egreso");
        assertTrue(contieneIngreso, "Debería contener el valor Ingreso");
    }

    @Test
    void deberiaPermitirConversionDesdeString() {
        // When
        TipoTransaccion egresoDesdeString = TipoTransaccion.valueOf("Egreso");
        TipoTransaccion ingresoDesdeString = TipoTransaccion.valueOf("Ingreso");

        // Then
        assertEquals(TipoTransaccion.Egreso, egresoDesdeString);
        assertEquals(TipoTransaccion.Ingreso, ingresoDesdeString);
    }

    @Test
    void deberiaConvertirAStringCorrectamente() {
        // Given
        TipoTransaccion egreso = TipoTransaccion.Egreso;
        TipoTransaccion ingreso = TipoTransaccion.Ingreso;

        // When
        String egresoString = egreso.toString();
        String ingresoString = ingreso.toString();

        // Then
        assertEquals("Egreso", egresoString);
        assertEquals("Ingreso", ingresoString);
    }

    @Test
    void deberiaPermitirComparacionPorIgualdad() {
        // Given
        TipoTransaccion egreso1 = TipoTransaccion.Egreso;
        TipoTransaccion egreso2 = TipoTransaccion.Egreso;
        TipoTransaccion ingreso = TipoTransaccion.Ingreso;

        // Then
        assertEquals(egreso1, egreso2);
        assertTrue(egreso1 == egreso2); // Los enums son singleton
        assertTrue(egreso1 != ingreso);
    }

    @Test
    void deberiaPermitirUsoEnSwitchStatement() {
        // Given
        TipoTransaccion egreso = TipoTransaccion.Egreso;
        TipoTransaccion ingreso = TipoTransaccion.Ingreso;

        // When & Then
        String resultadoEgreso = obtenerDescripcion(egreso);
        String resultadoIngreso = obtenerDescripcion(ingreso);

        assertEquals("Dinero que sale", resultadoEgreso);
        assertEquals("Dinero que entra", resultadoIngreso);
    }

    private String obtenerDescripcion(TipoTransaccion tipo) {
        switch (tipo) {
            case Egreso:
                return "Dinero que sale";
            case Ingreso:
                return "Dinero que entra";
            default:
                return "Tipo desconocido";
        }
    }
}
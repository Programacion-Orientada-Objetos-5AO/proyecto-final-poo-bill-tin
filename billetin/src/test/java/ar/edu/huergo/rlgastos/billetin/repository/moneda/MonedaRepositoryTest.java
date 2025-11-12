package ar.edu.huergo.rlgastos.billetin.repository.moneda;

import ar.edu.huergo.rlgastos.billetin.entity.moneda.Moneda;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MonedaRepositoryTest {

    @Autowired
    private MonedaRepository monedaRepository;

    @Test
    public void testGuardarYBuscarMoneda() {
        Moneda moneda = new Moneda();
        moneda.setCodigo("USD");
        moneda.setNombre("Dólar estadounidense");

        Moneda guardada = monedaRepository.save(moneda);
        Optional<Moneda> encontrada = monedaRepository.findById(guardada.getIdMoneda());

        assertTrue(encontrada.isPresent());
        assertEquals("USD", encontrada.get().getCodigo());
        assertEquals("Dólar estadounidense", encontrada.get().getNombre());
    }
}


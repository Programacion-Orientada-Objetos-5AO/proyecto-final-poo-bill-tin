package ar.edu.huergo.rlgastos.billetin.repository.membresia;

import ar.edu.huergo.rlgastos.billetin.entity.membresia.Membresia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MembresiaRepositoryTest {

    @Autowired
    private MembresiaRepository membresiaRepository;

    @Test
    public void testGuardarYBuscarMembresia() {
        Membresia membresia = new Membresia();
        membresia.setNombre("Premium");
        membresia.setPrecio(199.99);
        membresia.setBeneficios("Acceso a cursos");
        membresia.setDuracion(new Date());

        Membresia guardada = membresiaRepository.save(membresia);

        Optional<Membresia> encontrada = membresiaRepository.findById(guardada.getIdMembresia());

        assertTrue(encontrada.isPresent());
        assertEquals("Premium", encontrada.get().getNombre());
        assertEquals(199.99, encontrada.get().getPrecio());
    }
}

package ar.edu.huergo.rlgastos.billetin.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;
import ar.edu.huergo.rlgastos.billetin.repository.transaccion.TransaccionRepository;

@DataJpaTest
class TransaccionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransaccionRepository transaccionRepository;

    private Transaccion transaccionIngreso;
    private Transaccion transaccionEgreso;

    @BeforeEach
    void setUp() {
        transaccionIngreso = new Transaccion();
        transaccionIngreso.setNombreUsuario("Juan Pérez");
        transaccionIngreso.setMonto(1500.0);
        transaccionIngreso.setDescripcion("Salario mensual");
        transaccionIngreso.setFecha(LocalDate.of(2024, 6, 15));

        transaccionEgreso = new Transaccion();
        transaccionEgreso.setNombreUsuario("María González");
        transaccionEgreso.setMonto(800.0);
        transaccionEgreso.setDescripcion("Compra supermercado");
        transaccionEgreso.setFecha(LocalDate.of(2024, 6, 10));
    }

    @Test
    void deberiaGuardarTransaccionCorrectamente() {
        // When
        Transaccion transaccionGuardada = transaccionRepository.save(transaccionIngreso);

        // Then
        assertNotNull(transaccionGuardada);
        assertNotNull(transaccionGuardada.getId());
        assertEquals("Juan Pérez", transaccionGuardada.getNombreUsuario());
        assertEquals(1500.0, transaccionGuardada.getMonto());
        assertEquals("Salario mensual", transaccionGuardada.getDescripcion());
        assertEquals(LocalDate.of(2024, 6, 15), transaccionGuardada.getFecha());
    }

    @Test
    void deberiaBuscarTransaccionPorId() {
        // Given
        Transaccion transaccionGuardada = entityManager.persistAndFlush(transaccionIngreso);

        // When
        Optional<Transaccion> transaccionEncontrada = transaccionRepository.findById(transaccionGuardada.getId());

        // Then
        assertTrue(transaccionEncontrada.isPresent());
        assertEquals(transaccionGuardada.getId(), transaccionEncontrada.get().getId());
        assertEquals("Juan Pérez", transaccionEncontrada.get().getNombreUsuario());
    }

    @Test
    void deberiaRetornarVacioSiNoExisteTransaccion() {
        // When
        Optional<Transaccion> transaccionEncontrada = transaccionRepository.findById(999L);

        // Then
        assertFalse(transaccionEncontrada.isPresent());
    }

    @Test
    void deberiaEncontrarTodasLasTransacciones() {
        // Given
        entityManager.persist(transaccionIngreso);
        entityManager.persist(transaccionEgreso);
        entityManager.flush();

        // When
        List<Transaccion> todasLasTransacciones = transaccionRepository.findAll();

        // Then
        assertEquals(2, todasLasTransacciones.size());
    }

    @Test
    void deberiaActualizarTransaccionExistente() {
        // Given
        Transaccion transaccionGuardada = entityManager.persistAndFlush(transaccionIngreso);
        Long id = transaccionGuardada.getId();

        // When
        transaccionGuardada.setNombreUsuario("Juan Carlos Pérez");
        transaccionGuardada.setMonto(2000.0);
        transaccionGuardada.setDescripcion("Salario actualizado");
        Transaccion transaccionActualizada = transaccionRepository.save(transaccionGuardada);

        // Then
        assertEquals(id, transaccionActualizada.getId());
        assertEquals("Juan Carlos Pérez", transaccionActualizada.getNombreUsuario());
        assertEquals(2000.0, transaccionActualizada.getMonto());
        assertEquals("Salario actualizado", transaccionActualizada.getDescripcion());
    }

    @Test
    void deberiaEliminarTransaccionPorId() {
        // Given
        Transaccion transaccionGuardada = entityManager.persistAndFlush(transaccionIngreso);
        Long id = transaccionGuardada.getId();

        // When
        transaccionRepository.deleteById(id);

        // Then
        Optional<Transaccion> transaccionEliminada = transaccionRepository.findById(id);
        assertFalse(transaccionEliminada.isPresent());
    }

    @Test
    void deberiaEliminarTransaccionPorEntidad() {
        // Given
        Transaccion transaccionGuardada = entityManager.persistAndFlush(transaccionIngreso);

        // When
        transaccionRepository.delete(transaccionGuardada);

        // Then
        Optional<Transaccion> transaccionEliminada = transaccionRepository.findById(transaccionGuardada.getId());
        assertFalse(transaccionEliminada.isPresent());
    }

    @Test
    void deberiaVerificarSiExisteTransaccionPorId() {
        // Given
        Transaccion transaccionGuardada = entityManager.persistAndFlush(transaccionIngreso);

        // When
        boolean existe = transaccionRepository.existsById(transaccionGuardada.getId());
        boolean noExiste = transaccionRepository.existsById(999L);

        // Then
        assertTrue(existe);
        assertFalse(noExiste);
    }

    @Test
    void deberiaContarTotalDeTransacciones() {
        // Given
        entityManager.persist(transaccionIngreso);
        entityManager.persist(transaccionEgreso);
        entityManager.flush();

        // When
        long total = transaccionRepository.count();

        // Then
        assertEquals(2, total);
    }

    @Test
    void deberiaEliminarTodasLasTransacciones() {
        // Given
        entityManager.persist(transaccionIngreso);
        entityManager.persist(transaccionEgreso);
        entityManager.flush();

        // When
        transaccionRepository.deleteAll();

        // Then
        long total = transaccionRepository.count();
        assertEquals(0, total);
    }

    @Test
    void deberiaGuardarTransaccionConDescripcionNull() {
        // Given
        transaccionIngreso.setDescripcion(null);

        // When
        Transaccion transaccionGuardada = transaccionRepository.save(transaccionIngreso);

        // Then
        assertNotNull(transaccionGuardada);
        assertNotNull(transaccionGuardada.getId());
        assertEquals(null, transaccionGuardada.getDescripcion());
    }



    @Test
    void deberiaGuardarYRecuperarFechasCorrectamente() {
        // Given
        LocalDate fechaEspecifica = LocalDate.of(2024, 12, 25);
        transaccionIngreso.setFecha(fechaEspecifica);

        // When
        Transaccion transaccionGuardada = transaccionRepository.save(transaccionIngreso);
        Optional<Transaccion> transaccionRecuperada = transaccionRepository.findById(transaccionGuardada.getId());

        // Then
        assertTrue(transaccionRecuperada.isPresent());
        assertEquals(fechaEspecifica, transaccionRecuperada.get().getFecha());
    }

    @Test
    void deberiaManejarMultiplesTransaccionesDelMismoUsuario() {
        // Given
        Transaccion segundaTransaccion = new Transaccion();
        segundaTransaccion.setNombreUsuario("Juan Pérez"); // Mismo usuario
        segundaTransaccion.setMonto(600.0);
        segundaTransaccion.setDescripcion("Compra ropa");
        segundaTransaccion.setFecha(LocalDate.of(2024, 6, 20));

        // When
        entityManager.persist(transaccionIngreso);
        entityManager.persist(segundaTransaccion);
        entityManager.flush();

        // Then
        List<Transaccion> todasLasTransacciones = transaccionRepository.findAll();
        assertEquals(2, todasLasTransacciones.size());
        
        long transaccionesJuanPerez = todasLasTransacciones.stream()
                .filter(t -> "Juan Pérez".equals(t.getNombreUsuario()))
                .count();
        assertEquals(2, transaccionesJuanPerez);
    }
}
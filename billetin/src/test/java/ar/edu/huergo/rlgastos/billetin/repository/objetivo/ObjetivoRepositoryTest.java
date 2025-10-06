
package ar.edu.huergo.rlgastos.billetin.repository.objetivo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ar.edu.huergo.rlgastos.billetin.entity.objetivo.EstadoObjetivo;
import ar.edu.huergo.rlgastos.billetin.entity.objetivo.Objetivo;

@DataJpaTest
@DisplayName("Tests de Integración - ObjetivoRepository")
class ObjetivoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ObjetivoRepository objetivoRepository;

    private Objetivo objetivo1;
    private Objetivo objetivo2;
    private Objetivo objetivo3;

    @BeforeEach
    void setUp() {
        // Creacion de los objetivos de prueba
        objetivo1 = new Objetivo();
        objetivo1.setNombre("Ahorrar para vacaciones");
        objetivo1.setMontoMeta(100000.0);
        objetivo1.setFechaInicio(LocalDate.of(2025, 1, 1));
        objetivo1.setFechaFin(LocalDate.of(2025, 12, 31));
        objetivo1.setEstado(EstadoObjetivo.en_proceso);
        objetivo1 = entityManager.persistAndFlush(objetivo1);

        objetivo2 = new Objetivo();
        objetivo2.setNombre("Comprar auto");
        objetivo2.setMontoMeta(500000.0);
        objetivo2.setFechaInicio(LocalDate.of(2025, 1, 1));
        objetivo2.setFechaFin(LocalDate.of(2027, 12, 31));
        objetivo2.setEstado(EstadoObjetivo.en_proceso);
        objetivo2 = entityManager.persistAndFlush(objetivo2);

        objetivo3 = new Objetivo();
        objetivo3.setNombre("Fondo de emergencia");
        objetivo3.setMontoMeta(50000.0);
        objetivo3.setFechaInicio(LocalDate.of(2024, 1, 1));
        objetivo3.setFechaFin(LocalDate.of(2024, 12, 31));
        objetivo3.setEstado(EstadoObjetivo.cumplido);
        objetivo3 = entityManager.persistAndFlush(objetivo3);

        entityManager.clear();
    }

    @Test
    @DisplayName("Debería guardar y recuperar objetivo correctamente")
    void deberiaGuardarYRecuperarObjetivo() {
        // Given
        Objetivo nuevoObjetivo = new Objetivo();
        nuevoObjetivo.setNombre("Invertir en plazo fijo");
        nuevoObjetivo.setMontoMeta(200000.0);
        nuevoObjetivo.setFechaInicio(LocalDate.of(2025, 3, 1));
        nuevoObjetivo.setFechaFin(LocalDate.of(2025, 9, 1));
        nuevoObjetivo.setEstado(EstadoObjetivo.en_proceso);

        // When
        Objetivo objetivoGuardado = objetivoRepository.save(nuevoObjetivo);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertNotNull(objetivoGuardado.getIdObjetivo());

        Optional<Objetivo> objetivoRecuperado =
                objetivoRepository.findById(objetivoGuardado.getIdObjetivo());

        assertTrue(objetivoRecuperado.isPresent());
        assertEquals("Invertir en plazo fijo", objetivoRecuperado.get().getNombre());
        assertEquals(200000.0, objetivoRecuperado.get().getMontoMeta());
        assertEquals(EstadoObjetivo.en_proceso, objetivoRecuperado.get().getEstado());
    }

    @Test
    @DisplayName("Debería actualizar objetivo existente")
    void deberiaActualizarObjetivoExistente() {
        // Given
        String nuevoNombre = "Ahorrar para casa propia";
        Double nuevoMonto = 150000.0;

        // When
        Optional<Objetivo> objetivoOptional =
                objetivoRepository.findById(objetivo1.getIdObjetivo());
        assertTrue(objetivoOptional.isPresent());

        Objetivo objetivo = objetivoOptional.get();
        objetivo.setNombre(nuevoNombre);
        objetivo.setMontoMeta(nuevoMonto);

        Objetivo objetivoActualizado = objetivoRepository.save(objetivo);
        entityManager.flush();

        // Then
        assertEquals(nuevoNombre, objetivoActualizado.getNombre());
        assertEquals(nuevoMonto, objetivoActualizado.getMontoMeta());

        entityManager.clear();
        Optional<Objetivo> objetivoVerificacion =
                objetivoRepository.findById(objetivo1.getIdObjetivo());
        assertTrue(objetivoVerificacion.isPresent());
        assertEquals(nuevoNombre, objetivoVerificacion.get().getNombre());
        assertEquals(nuevoMonto, objetivoVerificacion.get().getMontoMeta());
    }

    @Test
    @DisplayName("Debería eliminar objetivo correctamente")
    void deberiaEliminarObjetivo() {
        // Given
        Long objetivoId = objetivo1.getIdObjetivo();
        assertTrue(objetivoRepository.existsById(objetivoId));

        // When
        objetivoRepository.deleteById(objetivoId);
        entityManager.flush();

        // Then
        assertFalse(objetivoRepository.existsById(objetivoId));
        Optional<Objetivo> objetivoEliminado = objetivoRepository.findById(objetivoId);
        assertFalse(objetivoEliminado.isPresent());
    }

    @Test
    @DisplayName("Debería encontrar todos los objetivos")
    void deberiaEncontrarTodosLosObjetivos() {
        // When
        List<Objetivo> todosLosObjetivos = objetivoRepository.findAll();

        // Then
        assertNotNull(todosLosObjetivos);
        assertEquals(3, todosLosObjetivos.size());

        List<String> nombres = todosLosObjetivos.stream()
                .map(Objetivo::getNombre)
                .toList();
        assertTrue(nombres.contains("Ahorrar para vacaciones"));
        assertTrue(nombres.contains("Comprar auto"));
        assertTrue(nombres.contains("Fondo de emergencia"));
    }

    @Test
    @DisplayName("Debería contar objetivos correctamente")
    void deberiaContarObjetivos() {
        // When
        long cantidadObjetivos = objetivoRepository.count();

        // Then
        assertEquals(3, cantidadObjetivos);

        Objetivo nuevoObjetivo = new Objetivo();
        nuevoObjetivo.setNombre("Pagar deudas");
        nuevoObjetivo.setMontoMeta(80000.0);
        nuevoObjetivo.setFechaInicio(LocalDate.of(2025, 1, 1));
        nuevoObjetivo.setFechaFin(LocalDate.of(2025, 6, 30));
        nuevoObjetivo.setEstado(EstadoObjetivo.en_proceso);
        entityManager.persistAndFlush(nuevoObjetivo);

        assertEquals(4, objetivoRepository.count());
    }

    @Test
    @DisplayName("Debería verificar existencia de objetivo por ID")
    void deberiaVerificarExistenciaPorId() {
        // When & Then
        assertTrue(objetivoRepository.existsById(objetivo1.getIdObjetivo()));
        assertTrue(objetivoRepository.existsById(objetivo2.getIdObjetivo()));
        assertFalse(objetivoRepository.existsById(999L));
    }

    @Test
    @DisplayName("Debería manejar objetivo con fechas específicas")
    void deberiaManejarObjetivoConFechasEspecificas() {
        // Given
        LocalDate fechaInicio = LocalDate.of(2025, 6, 15);
        LocalDate fechaFin = LocalDate.of(2025, 12, 15);

        Objetivo objetivoConFechas = new Objetivo();
        objetivoConFechas.setNombre("Objetivo con fechas");
        objetivoConFechas.setMontoMeta(75000.0);
        objetivoConFechas.setFechaInicio(fechaInicio);
        objetivoConFechas.setFechaFin(fechaFin);
        objetivoConFechas.setEstado(EstadoObjetivo.en_proceso);

        // When
        Objetivo objetivoGuardado = objetivoRepository.save(objetivoConFechas);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Objetivo> objetivoRecuperado =
                objetivoRepository.findById(objetivoGuardado.getIdObjetivo());
        assertTrue(objetivoRecuperado.isPresent());
        assertEquals(fechaInicio, objetivoRecuperado.get().getFechaInicio());
        assertEquals(fechaFin, objetivoRecuperado.get().getFechaFin());
    }

    @Test
    @DisplayName("Debería actualizar estado de objetivo")
    void deberiaActualizarEstadoObjetivo() {
        // Given
        Optional<Objetivo> objetivoOptional =
                objetivoRepository.findById(objetivo1.getIdObjetivo());
        assertTrue(objetivoOptional.isPresent());

        Objetivo objetivo = objetivoOptional.get();
        assertEquals(EstadoObjetivo.en_proceso, objetivo.getEstado());

        // When
        objetivo.setEstado(EstadoObjetivo.cumplido);
        objetivoRepository.save(objetivo);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Objetivo> objetivoActualizado =
                objetivoRepository.findById(objetivo1.getIdObjetivo());
        assertTrue(objetivoActualizado.isPresent());
        assertEquals(EstadoObjetivo.cumplido, objetivoActualizado.get().getEstado());
    }

    @Test
    @DisplayName("Debería manejar diferentes estados de objetivo")
    void deberiaManejarDiferentesEstados() {
        // When
        List<Objetivo> objetivos = objetivoRepository.findAll();

        // Then
        long objetivosEnProceso = objetivos.stream()
                .filter(o -> o.getEstado() == EstadoObjetivo.en_proceso)
                .count();
        long objetivosCumplidos = objetivos.stream()
                .filter(o -> o.getEstado() == EstadoObjetivo.cumplido)
                .count();

        assertEquals(2, objetivosEnProceso);
        assertEquals(1, objetivosCumplidos);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío para ID inexistente")
    void deberiaRetornarOptionalVacioParaIdInexistente() {
        // When
        Optional<Objetivo> resultado = objetivoRepository.findById(999L);

        // Then
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Debería manejar objetivo con monto decimal")
    void deberiaManejarObjetivoConMontoDecimal() {
        // Given
        Objetivo objetivo = new Objetivo();
        objetivo.setNombre("Objetivo con decimal");
        objetivo.setMontoMeta(123456.78);
        objetivo.setFechaInicio(LocalDate.of(2025, 1, 1));
        objetivo.setFechaFin(LocalDate.of(2025, 12, 31));
        objetivo.setEstado(EstadoObjetivo.en_proceso);

        // When
        Objetivo objetivoGuardado = objetivoRepository.save(objetivo);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Objetivo> objetivoRecuperado =
                objetivoRepository.findById(objetivoGuardado.getIdObjetivo());
        assertTrue(objetivoRecuperado.isPresent());
        assertEquals(123456.78, objetivoRecuperado.get().getMontoMeta());
    }

    @Test
    @DisplayName("Debería persistir y recuperar todos los campos del objetivo")
    void deberiaPersistirYRecuperarTodosCampos() {
        // Given
        Objetivo objetivo = new Objetivo();
        objetivo.setNombre("Objetivo completo");
        objetivo.setMontoMeta(250000.0);
        objetivo.setFechaInicio(LocalDate.of(2025, 2, 1));
        objetivo.setFechaFin(LocalDate.of(2026, 2, 1));
        objetivo.setEstado(EstadoObjetivo.no_cumplido);

        // When
        Objetivo objetivoGuardado = objetivoRepository.save(objetivo);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Objetivo> objetivoRecuperado =
                objetivoRepository.findById(objetivoGuardado.getIdObjetivo());
        assertTrue(objetivoRecuperado.isPresent());
        Objetivo obj = objetivoRecuperado.get();
        
        assertNotNull(obj.getIdObjetivo());
        assertEquals("Objetivo completo", obj.getNombre());
        assertEquals(250000.0, obj.getMontoMeta());
        assertEquals(LocalDate.of(2025, 2, 1), obj.getFechaInicio());
        assertEquals(LocalDate.of(2026, 2, 1), obj.getFechaFin());
        assertEquals(EstadoObjetivo.no_cumplido, obj.getEstado());
    }
}
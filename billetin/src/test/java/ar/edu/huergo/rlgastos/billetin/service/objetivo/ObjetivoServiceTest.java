package ar.edu.huergo.rlgastos.billetin.service.objetivo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ar.edu.huergo.rlgastos.billetin.dto.objetivo.ActualizarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.objetivo.EstadoObjetivo;
import ar.edu.huergo.rlgastos.billetin.entity.objetivo.Objetivo;
import ar.edu.huergo.rlgastos.billetin.repository.objetivo.ObjetivoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de Unidad - ObjetivoService")
class ObjetivoServiceTest {

    @Mock
    private ObjetivoRepository objetivoRepository;

    @InjectMocks
    private ObjetivoService objetivoService;

    private Objetivo objetivoEjemplo;

    @BeforeEach
    void setUp() {
        objetivoEjemplo = new Objetivo();
        objetivoEjemplo.setIdObjetivo(1L);
        objetivoEjemplo.setNombre("Ahorrar para vacaciones");
        objetivoEjemplo.setMontoMeta(100000.0);
        objetivoEjemplo.setFechaInicio(LocalDate.of(2025, 1, 1));
        objetivoEjemplo.setFechaFin(LocalDate.of(2025, 12, 31));
        objetivoEjemplo.setEstado(EstadoObjetivo.en_proceso);
    }

    @Test
    @DisplayName("Debería obtener todos los objetivos")
    void deberiaObtenerTodosLosObjetivos() {
        // Given
        List<Objetivo> objetivosEsperados = Arrays.asList(objetivoEjemplo);

        when(objetivoRepository.findAll()).thenReturn(objetivosEsperados);

        // When
        List<Objetivo> resultado = objetivoService.getObjetivos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(objetivoEjemplo.getNombre(), resultado.get(0).getNombre());
        verify(objetivoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería obtener objetivo por ID cuando existe")
    void deberiaObtenerObjetivoPorId() {
        // Given
        Long objetivoId = 1L;
        when(objetivoRepository.findById(objetivoId))
                .thenReturn(Optional.of(objetivoEjemplo));

        // When
        Optional<Objetivo> resultado = objetivoService.getObjetivo(objetivoId);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(objetivoEjemplo.getIdObjetivo(), resultado.get().getIdObjetivo());
        assertEquals(objetivoEjemplo.getNombre(), resultado.get().getNombre());
        verify(objetivoRepository, times(1)).findById(objetivoId);
    }

    @Test
    @DisplayName("Debería devolver vacío cuando objetivo no existe")
    void deberiaRetornarOptionalVacioCuandoObjetivoNoExiste() {
        // Given
        Long objetivoIdInexistente = 999L;
        when(objetivoRepository.findById(objetivoIdInexistente)).thenReturn(Optional.empty());

        // When
        Optional<Objetivo> resultado = objetivoService.getObjetivo(objetivoIdInexistente);

        // Then
        assertTrue(resultado.isEmpty());
        verify(objetivoRepository, times(1)).findById(objetivoIdInexistente);
        
    }

    @Test
    @DisplayName("Debería crear objetivo correctamente")
    void deberiaCrearObjetivo() {
        // Given
        Objetivo nuevoObjetivo = new Objetivo();
        nuevoObjetivo.setNombre("Comprar auto");
        nuevoObjetivo.setMontoMeta(500000.0);
        nuevoObjetivo.setFechaInicio(LocalDate.of(2025, 1, 1));
        nuevoObjetivo.setFechaFin(LocalDate.of(2027, 12, 31));
        nuevoObjetivo.setEstado(EstadoObjetivo.en_proceso);

        when(objetivoRepository.save(nuevoObjetivo)).thenReturn(nuevoObjetivo);

        // When
        assertDoesNotThrow(() -> objetivoService.crearObjetivo(nuevoObjetivo));

        // Then
        verify(objetivoRepository, times(1)).save(nuevoObjetivo);
    }

    @Test
    @DisplayName("Debería actualizar objetivo existente")
    void deberiaActualizarObjetivo() throws NotFoundException {
        // Given
        Long objetivoId = 1L;
        ActualizarObjetivoDTO dto = new ActualizarObjetivoDTO(
                "Ahorrar para casa",
                200000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2026, 12, 31),
                EstadoObjetivo.en_proceso
        );

        when(objetivoRepository.findById(objetivoId))
                .thenReturn(Optional.of(objetivoEjemplo));
        when(objetivoRepository.save(any(Objetivo.class))).thenReturn(objetivoEjemplo);

        // When
        assertDoesNotThrow(() -> objetivoService.actualizarObjetivo(objetivoId, dto));

        // Then
        verify(objetivoRepository, times(1)).findById(objetivoId);
        verify(objetivoRepository, times(1)).save(objetivoEjemplo);
        assertEquals(dto.nombre(), objetivoEjemplo.getNombre());
        assertEquals(dto.montoMeta(), objetivoEjemplo.getMontoMeta());
        assertEquals(dto.fechaInicio(), objetivoEjemplo.getFechaInicio());
        assertEquals(dto.fechaFin(), objetivoEjemplo.getFechaFin());
        assertEquals(dto.estado(), objetivoEjemplo.getEstado());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando objetivo a actualizar no existe")
    void deberiaLanzarExcepcionCuandoObjetivoAActualizarNoExiste() {
        // Given
        Long objetivoIdInexistente = 999L;
        ActualizarObjetivoDTO dto = new ActualizarObjetivoDTO(
                "Objetivo actualizado",
                150000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        when(objetivoRepository.findById(objetivoIdInexistente)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class,
                () -> objetivoService.actualizarObjetivo(objetivoIdInexistente, dto));

        verify(objetivoRepository, times(1)).findById(objetivoIdInexistente);
        verify(objetivoRepository, times(0)).save(any(Objetivo.class));
    }

    @Test
    @DisplayName("Debería eliminar objetivo correctamente")
    void deberiaEliminarObjetivo() {
        // Given
        Long objetivoId = 1L;
        doNothing().when(objetivoRepository).deleteById(objetivoId);

        // When
        assertDoesNotThrow(() -> objetivoService.eliminarObjetivo(objetivoId));

        // Then
        verify(objetivoRepository, times(1)).deleteById(objetivoId);
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay objetivos")
    void deberiaRetornarListaVaciaCuandoNoHayObjetivos() {
        // Given
        when(objetivoRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Objetivo> resultado = objetivoService.getObjetivos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(objetivoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería actualizar estado del objetivo correctamente")
    void deberiaActualizarEstadoCorrectamente() throws NotFoundException {
        // Given
        Long objetivoId = 1L;
        ActualizarObjetivoDTO dto = new ActualizarObjetivoDTO(
                objetivoEjemplo.getNombre(),
                objetivoEjemplo.getMontoMeta(),
                objetivoEjemplo.getFechaInicio(),
                objetivoEjemplo.getFechaFin(),
                EstadoObjetivo.cumplido
        );

        when(objetivoRepository.findById(objetivoId))
                .thenReturn(Optional.of(objetivoEjemplo));
        when(objetivoRepository.save(any(Objetivo.class))).thenReturn(objetivoEjemplo);

        // When
        objetivoService.actualizarObjetivo(objetivoId, dto);

        // Then
        assertEquals(EstadoObjetivo.cumplido, objetivoEjemplo.getEstado());
        verify(objetivoRepository, times(1)).save(objetivoEjemplo);
    }

    @Test
    @DisplayName("Debería manejar múltiples objetivos correctamente")
    void deberiaManejarMultiplesObjetivos() {
        // Given
        Objetivo objetivo2 = new Objetivo();
        objetivo2.setIdObjetivo(2L);
        objetivo2.setNombre("Fondo de emergencia");
        objetivo2.setMontoMeta(50000.0);
        objetivo2.setFechaInicio(LocalDate.of(2025, 1, 1));
        objetivo2.setFechaFin(LocalDate.of(2025, 6, 30));
        objetivo2.setEstado(EstadoObjetivo.en_proceso);

        List<Objetivo> objetivos = Arrays.asList(objetivoEjemplo, objetivo2);
        when(objetivoRepository.findAll()).thenReturn(objetivos);

        // When
        List<Objetivo> resultado = objetivoService.getObjetivos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Ahorrar para vacaciones", resultado.get(0).getNombre());
        assertEquals("Fondo de emergencia", resultado.get(1).getNombre());
    }
}
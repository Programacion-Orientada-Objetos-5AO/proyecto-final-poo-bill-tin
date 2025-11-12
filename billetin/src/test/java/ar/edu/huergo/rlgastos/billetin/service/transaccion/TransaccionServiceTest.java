package ar.edu.huergo.rlgastos.billetin.service.transaccion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ar.edu.huergo.rlgastos.billetin.dto.transaccion.ActualizarTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;
import ar.edu.huergo.rlgastos.billetin.repository.transaccion.TransaccionRepository;
import ar.edu.huergo.rlgastos.billetin.service.TransaccionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de Unidad - TransaccionService")
class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    private Transaccion transaccionEjemplo;
    private ActualizarTransaccionDTO actualizarTransaccionDTO;

    @BeforeEach
    void setUp() {
        transaccionEjemplo = new Transaccion();
        transaccionEjemplo.setId(1L);
        transaccionEjemplo.setNombreUsuario("Juan Perez");
        transaccionEjemplo.setMonto(1500.0);
        transaccionEjemplo.setDescripcion("Compra de supermercado");
        transaccionEjemplo.setFecha(LocalDate.of(2024, 1, 15));

        actualizarTransaccionDTO = new ActualizarTransaccionDTO(
            2000.0,
            "Compra actualizada",
            LocalDate.of(2024, 2, 20), null, null, null
        );
    }

    @Test
    @DisplayName("Debería obtener todas las transacciones")
    void deberiaObtenerTodasLasTransacciones() {
        // Given
        List<Transaccion> transaccionesEsperadas = Arrays.asList(transaccionEjemplo);
        when(transaccionRepository.findAll()).thenReturn(transaccionesEsperadas);

        // When
        List<Transaccion> resultado = transaccionService.getTransaccion();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transaccionEjemplo.getId(), resultado.get(0).getId());
        assertEquals(transaccionEjemplo.getMonto(), resultado.get(0).getMonto());
        assertEquals(transaccionEjemplo.getDescripcion(), resultado.get(0).getDescripcion());
        assertEquals(transaccionEjemplo.getNombreUsuario(), resultado.get(0).getNombreUsuario());
        verify(transaccionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería obtener transacción por ID cuando existe")
    void deberiaObtenerTransaccionPorIdCuandoExiste() {
        // Given
        Long id = 1L;
        when(transaccionRepository.findById(id)).thenReturn(Optional.of(transaccionEjemplo));

        // When
        Optional<Transaccion> resultado = transaccionService.getTransaccion(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(transaccionEjemplo.getId(), resultado.get().getId());
        assertEquals(transaccionEjemplo.getMonto(), resultado.get().getMonto());
        assertEquals(transaccionEjemplo.getDescripcion(), resultado.get().getDescripcion());
        assertEquals(transaccionEjemplo.getNombreUsuario(), resultado.get().getNombreUsuario());
        verify(transaccionRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando la transacción no existe")
    void deberiaRetornarOptionalVacioCuandoTransaccionNoExiste() {
        // Given
        Long id = 999L;
        when(transaccionRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Transaccion> resultado = transaccionService.getTransaccion(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(transaccionRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debería crear transacción correctamente")
    void deberiaCrearTransaccionCorrectamente() {
        // Given
        when(transaccionRepository.save(transaccionEjemplo)).thenReturn(transaccionEjemplo);

        // When
        transaccionService.crearTransaccion(transaccionEjemplo);

        // Then
        verify(transaccionRepository, times(1)).save(transaccionEjemplo);
    }

    @Test
    @DisplayName("Debería actualizar transacción correctamente")
    void deberiaActualizarTransaccionCorrectamente() throws NotFoundException {
        // Given
        Long id = 1L;
        when(transaccionRepository.findById(id)).thenReturn(Optional.of(transaccionEjemplo));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionEjemplo);

        // When
        transaccionService.actualizarTransaccion(id, actualizarTransaccionDTO);

        // Then
        verify(transaccionRepository, times(1)).findById(id);
        verify(transaccionRepository, times(1)).save(transaccionEjemplo);

        // Verificar que los campos fueron actualizados
        assertEquals(actualizarTransaccionDTO.monto(), transaccionEjemplo.getMonto());
        assertEquals(actualizarTransaccionDTO.descripcion(), transaccionEjemplo.getDescripcion());
        assertEquals(actualizarTransaccionDTO.fecha(), transaccionEjemplo.getFecha());
    }

    @Test
    @DisplayName("Debería lanzar NotFoundException cuando actualiza transacción que no existe")
    void deberiaLanzarNotFoundExceptionCuandoActualizaTransaccionQueNoExiste() {
        // Given
        Long id = 999L;
        when(transaccionRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, 
                () -> transaccionService.actualizarTransaccion(id, actualizarTransaccionDTO));

        verify(transaccionRepository, times(1)).findById(id);
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    @DisplayName("Debería eliminar transacción por ID")
    void deberiaEliminarTransaccionPorId() {
        // Given
        Long id = 1L;

        // When
        transaccionService.eliminarTransaccion(id);

        // Then
        verify(transaccionRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Debería manejar lista vacía de transacciones")
    void deberiaManejarListaVaciaDeTransacciones() {
        // Given
        when(transaccionRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Transaccion> resultado = transaccionService.getTransaccion();

        // Then
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        assertTrue(resultado.isEmpty());
        verify(transaccionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería actualizar solo los campos del DTO")
    void deberiaActualizarSoloLosCamposDelDTO() throws NotFoundException {
        // Given
        Long id = 1L;
        String nombreOriginal = transaccionEjemplo.getNombreUsuario();

        
        when(transaccionRepository.findById(id)).thenReturn(Optional.of(transaccionEjemplo));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionEjemplo);

        // When
        transaccionService.actualizarTransaccion(id, actualizarTransaccionDTO);

        // Then
        // Verificar que los campos no incluidos en el DTO no cambiaron
        assertEquals(nombreOriginal, transaccionEjemplo.getNombreUsuario());
        
        // Verificar que los campos del DTO se actualizaron
        assertEquals(actualizarTransaccionDTO.monto(), transaccionEjemplo.getMonto());
        assertEquals(actualizarTransaccionDTO.descripcion(), transaccionEjemplo.getDescripcion());
        assertEquals(actualizarTransaccionDTO.fecha(), transaccionEjemplo.getFecha());
    }

    @Test
    @DisplayName("Debería manejar valores null en DTO de actualización")
    void deberiaManejarValoresNullEnDTODeActualizacion() throws NotFoundException {
        // Given
        Long id = 1L;
        ActualizarTransaccionDTO dtoConNulls = new ActualizarTransaccionDTO(null, null, null, id, id, id);
        
        when(transaccionRepository.findById(id)).thenReturn(Optional.of(transaccionEjemplo));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionEjemplo);

        // When
        transaccionService.actualizarTransaccion(id, dtoConNulls);

        // Then
        verify(transaccionRepository, times(1)).findById(id);
        verify(transaccionRepository, times(1)).save(transaccionEjemplo);
        
        // Los valores null se asignaron (comportamiento actual del servicio)
        assertEquals(null, transaccionEjemplo.getMonto());
        assertEquals(null, transaccionEjemplo.getDescripcion());
        assertEquals(null, transaccionEjemplo.getFecha());
    }

    @Test
    @DisplayName("Debería crear transacción completa con todos los campos")
    void deberiaCrearTransaccionCompletaConTodosLosCampos() {
        // Given
        Transaccion transaccionCompleta = new Transaccion();
        transaccionCompleta.setId(2L);
        transaccionCompleta.setNombreUsuario("Maria Garcia");
        transaccionCompleta.setMonto(3000.0);
        transaccionCompleta.setDescripcion("Salario mensual");
        transaccionCompleta.setFecha(LocalDate.now());
        
        when(transaccionRepository.save(transaccionCompleta)).thenReturn(transaccionCompleta);

        // When
        transaccionService.crearTransaccion(transaccionCompleta);

        // Then
        verify(transaccionRepository, times(1)).save(transaccionCompleta);
    }

    @Test
    @DisplayName("Debería manejar montos mínimos válidos")
    void deberiaManejarMontosMinimosValidos() throws NotFoundException {
        // Given
        Long id = 1L;
        ActualizarTransaccionDTO dtoMontoMinimo = new ActualizarTransaccionDTO(
            500.0, 
            "Transacción mínima",
            LocalDate.now(), id, id, id
        );
        
        when(transaccionRepository.findById(id)).thenReturn(Optional.of(transaccionEjemplo));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionEjemplo);

        // When
        transaccionService.actualizarTransaccion(id, dtoMontoMinimo);

        // Then
        assertEquals(500.0, transaccionEjemplo.getMonto());
        assertEquals("Transacción mínima", transaccionEjemplo.getDescripcion());
        verify(transaccionRepository, times(1)).save(transaccionEjemplo);
    }
}
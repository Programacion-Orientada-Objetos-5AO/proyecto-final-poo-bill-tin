package ar.edu.huergo.rlgastos.billetin.entity.objetivo;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ar.edu.huergo.rlgastos.billetin.dto.objetivo.CrearObjetivoDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("Tests de Validación - CrearObjetivoDTO")
class ObjetivoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debería validar objetivo correcto sin errores")
    void deberiaValidarObjetivoCorrectoSinErrores() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "Ahorrar para vacaciones",
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertTrue(violaciones.isEmpty(),
                "No debería haber violaciones de validación para un objetivo válido");
    }

    @Test
    @DisplayName("Debería fallar validación con nombre null")
    void deberiaFallarValidacionConNombreNull() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                null,
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombre")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    @DisplayName("Debería fallar validación con nombre vacío")
    void deberiaFallarValidacionConNombreVacio() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "",
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombre")));
    }

    @Test
    @DisplayName("Debería fallar validación con nombre solo espacios")
    void deberiaFallarValidacionConNombreSoloEspacios() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "   ",
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombre")));
    }

    @Test
    @DisplayName("Debería fallar validación con nombre muy corto")
    void deberiaFallarValidacionConNombreMuyCorto() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "A",
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombre")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("entre 2 y 100 caracteres")));
    }

    @Test
    @DisplayName("Debería fallar validación con nombre muy largo")
    void deberiaFallarValidacionConNombreMuyLargo() {
        // Given
        String nombreLargo = "A".repeat(101); // 101 caracteres
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                nombreLargo,
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombre")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("entre 2 y 100 caracteres")));
    }

    @Test
    @DisplayName("Debería aceptar nombres en el límite válido")
    void deberiaAceptarNombresEnLimiteValido() {
        // Given - Nombres de exactamente 2 y 100 caracteres
        String nombreMinimo = "AB"; // 2 caracteres
        String nombreMaximo = "A".repeat(100); // 100 caracteres

        CrearObjetivoDTO objetivo1 = new CrearObjetivoDTO(
                nombreMinimo,
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        CrearObjetivoDTO objetivo2 = new CrearObjetivoDTO(
                nombreMaximo,
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones1 = validator.validate(objetivo1);
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones2 = validator.validate(objetivo2);

        // Then
        assertTrue(violaciones1.isEmpty());
        assertTrue(violaciones2.isEmpty());
    }

    @Test
    @DisplayName("Debería fallar validación con monto meta null")
    void deberiaFallarValidacionConMontoMetaNull() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "Ahorrar para vacaciones",
                null,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("montoMeta")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    @DisplayName("Debería fallar validación con monto meta menor a 1000")
    void deberiaFallarValidacionConMontoMetaMenorA1000() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "Ahorrar para vacaciones",
                999.99,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("montoMeta")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("mayor o igual a 1000")));
    }

    @Test
    @DisplayName("Debería aceptar monto meta igual a 1000")
    void deberiaAceptarMontoMetaIgualA1000() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "Objetivo pequeño",
                1000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertTrue(violaciones.isEmpty());
    }

    @Test
    @DisplayName("Debería fallar validación con fecha inicio null")
    void deberiaFallarValidacionConFechaInicioNull() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "Ahorrar para vacaciones",
                100000.0,
                null,
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaInicio")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("obligatoria")));
    }

    @Test
    @DisplayName("Debería fallar validación con fecha fin null")
    void deberiaFallarValidacionConFechaFinNull() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "Ahorrar para vacaciones",
                100000.0,
                LocalDate.of(2025, 1, 1),
                null,
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaFin")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("obligatoria")));
    }

    @Test
    @DisplayName("Debería fallar validación con estado null")
    void deberiaFallarValidacionConEstadoNull() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "Ahorrar para vacaciones",
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                null
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("estado")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    @DisplayName("Debería validar objetivo con todos los estados posibles")
    void deberiaValidarObjetivoConTodosLosEstados() {
        // Given
        CrearObjetivoDTO objetivoEnProceso = new CrearObjetivoDTO(
                "Objetivo en proceso",
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        CrearObjetivoDTO objetivoCumplido = new CrearObjetivoDTO(
                "Objetivo cumplido",
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.cumplido
        );

        CrearObjetivoDTO objetivoNoCumplido = new CrearObjetivoDTO(
                "Objetivo no cumplido",
                100000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.no_cumplido
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones1 = 
                validator.validate(objetivoEnProceso);
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones2 = 
                validator.validate(objetivoCumplido);
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones3 = 
                validator.validate(objetivoNoCumplido);

        // Then
        assertTrue(violaciones1.isEmpty());
        assertTrue(violaciones2.isEmpty());
        assertTrue(violaciones3.isEmpty());
    }

    @Test
    @DisplayName("Debería aceptar monto meta con decimales")
    void deberiaAceptarMontoMetaConDecimales() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "Ahorrar para vacaciones",
                123456.78,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertTrue(violaciones.isEmpty());
    }

    @Test
    @DisplayName("Debería aceptar fechas válidas")
    void deberiaAceptarFechasValidas() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                "Objetivo a largo plazo",
                500000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2030, 12, 31),
                EstadoObjetivo.en_proceso
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertTrue(violaciones.isEmpty());
    }

    @Test
    @DisplayName("Debería fallar validación con múltiples campos null")
    void deberiaFallarValidacionConMultiplesCamposNull() {
        // Given
        CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                null,
                null,
                null,
                null,
                null
        );

        // When
        Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = validator.validate(objetivo);

        // Then
        assertFalse(violaciones.isEmpty());
        // Debería tener al menos 5 violaciones (una por cada campo obligatorio)
        assertTrue(violaciones.size() >= 5);
    }

    @Test
    @DisplayName("Debería aceptar nombres de objetivos comunes")
    void deberiaAceptarNombresDeObjetivosComunes() {
        // Given
        String[] nombresValidos = {
                "Ahorrar para vacaciones",
                "Comprar auto",
                "Fondo de emergencia",
                "Invertir en plazo fijo",
                "Pagar deudas"
        };

        for (String nombre : nombresValidos) {
            CrearObjetivoDTO objetivo = new CrearObjetivoDTO(
                    nombre,
                    100000.0,
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 12, 31),
                    EstadoObjetivo.en_proceso
            );

            // When
            Set<ConstraintViolation<CrearObjetivoDTO>> violaciones = 
                    validator.validate(objetivo);

            // Then
            assertTrue(violaciones.isEmpty(), 
                    "El nombre '" + nombre + "' debería ser válido");
        }
    }
}

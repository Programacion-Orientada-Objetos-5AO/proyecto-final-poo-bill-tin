package ar.edu.huergo.rlgastos.billetin.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TransaccionValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deberiaValidarTransaccionCorrectoSinErrores() {
        // Given
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario("Juan Pérez");
        transaccion.setTipo(TipoTransaccion.Ingreso);
        transaccion.setMonto(1500.0);
        transaccion.setDescripcion("Salario mensual");
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertTrue(violaciones.isEmpty(),
                "No debería haber violaciones de validación para una transacción válida");
    }

    @Test
    void deberiaFallarValidacionConNombreUsuarioNull() {
        // Given
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario(null);
        transaccion.setTipo(TipoTransaccion.Ingreso);
        transaccion.setMonto(1500.0);
        transaccion.setDescripcion("Descripción válida");
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombreUsuario")));
        assertTrue(violaciones.stream().anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    void deberiaFallarValidacionConNombreUsuarioVacio() {
        // Given
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario("");
        transaccion.setTipo(TipoTransaccion.Ingreso);
        transaccion.setMonto(1500.0);
        transaccion.setDescripcion("Descripción válida");
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombreUsuario")));
    }

    @Test
    void deberiaFallarValidacionConNombreUsuarioSoloEspacios() {
        // Given
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario("   ");
        transaccion.setTipo(TipoTransaccion.Ingreso);
        transaccion.setMonto(1500.0);
        transaccion.setDescripcion("Descripción válida");
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombreUsuario")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A"})
    void deberiaFallarValidacionConNombresUsuarioMuyCortos(String nombreCorto) {
        // Given
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario(nombreCorto);
        transaccion.setTipo(TipoTransaccion.Ingreso);
        transaccion.setMonto(1500.0);
        transaccion.setDescripcion("Descripción válida");
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombreUsuario")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("entre 2 y 100 caracteres")));
    }

    @Test
    void deberiaFallarValidacionConNombreUsuarioMuyLargo() {
        // Given
        String nombreLargo = "A".repeat(101); // 101 caracteres
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario(nombreLargo);
        transaccion.setTipo(TipoTransaccion.Ingreso);
        transaccion.setMonto(1500.0);
        transaccion.setDescripcion("Descripción válida");
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombreUsuario")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("entre 2 y 100 caracteres")));
    }

    @Test
    void deberiaAceptarNombresUsuarioEnLimiteValido() {
        // Given - Nombres de exactamente 2 y 100 caracteres
        String nombreMinimo = "AB"; // 2 caracteres
        String nombreMaximo = "A".repeat(100); // 100 caracteres

        Transaccion transaccion1 = new Transaccion();
        transaccion1.setNombreUsuario(nombreMinimo);
        transaccion1.setTipo(TipoTransaccion.Ingreso);
        transaccion1.setMonto(1500.0);
        transaccion1.setDescripcion("Descripción válida");
        transaccion1.setFecha(LocalDate.now());

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setNombreUsuario(nombreMaximo);
        transaccion2.setTipo(TipoTransaccion.Egreso);
        transaccion2.setMonto(1500.0);
        transaccion2.setDescripcion("Descripción válida");
        transaccion2.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones1 = validator.validate(transaccion1);
        Set<ConstraintViolation<Transaccion>> violaciones2 = validator.validate(transaccion2);

        // Then
        assertTrue(violaciones1.isEmpty());
        assertTrue(violaciones2.isEmpty());
    }

    @Test
    void deberiaFallarValidacionConTipoTransaccionNull() {
        // Given
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario("Juan Pérez");
        transaccion.setTipo(null);
        transaccion.setMonto(1500.0);
        transaccion.setDescripcion("Descripción válida");
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("tipo")));
        assertTrue(violaciones.stream().anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 100.0, 499.99})
    void deberiaFallarValidacionConMontosMenoresA500(double montoInvalido) {
        // Given
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario("Juan Pérez");
        transaccion.setTipo(TipoTransaccion.Ingreso);
        transaccion.setMonto(montoInvalido);
        transaccion.setDescripcion("Descripción válida");
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("monto")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("mayor o igual a 500")));
    }

    @ParameterizedTest
    @ValueSource(doubles = {500.0, 1000.0, 5000.0, 10000.99})
    void deberiaAceptarMontosMayoresOIgualesA500(double montoValido) {
        // Given
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario("Juan Pérez");
        transaccion.setTipo(TipoTransaccion.Egreso);
        transaccion.setMonto(montoValido);
        transaccion.setDescripcion("Descripción válida");
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertTrue(violaciones.isEmpty());
    }

    @Test
    void deberiaFallarValidacionConDescripcionMuyLarga() {
        // Given
        String descripcionLarga = "A".repeat(101); // 101 caracteres
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario("Juan Pérez");
        transaccion.setTipo(TipoTransaccion.Ingreso);
        transaccion.setMonto(1500.0);
        transaccion.setDescripcion(descripcionLarga);
        transaccion.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")));
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("no puede superar los 100 caracteres")));
    }

    @Test
    void deberiaAceptarDescripcionNullOVacia() {
        // Given
        Transaccion transaccion1 = new Transaccion();
        transaccion1.setNombreUsuario("Juan Pérez");
        transaccion1.setTipo(TipoTransaccion.Ingreso);
        transaccion1.setMonto(1500.0);
        transaccion1.setDescripcion(null);
        transaccion1.setFecha(LocalDate.now());

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setNombreUsuario("María González");
        transaccion2.setTipo(TipoTransaccion.Egreso);
        transaccion2.setMonto(2000.0);
        transaccion2.setDescripcion("");
        transaccion2.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones1 = validator.validate(transaccion1);
        Set<ConstraintViolation<Transaccion>> violaciones2 = validator.validate(transaccion2);

        // Then
        assertTrue(violaciones1.isEmpty());
        assertTrue(violaciones2.isEmpty());
    }

    @Test
    void deberiaFallarValidacionConFechaNull() {
        // Given
        Transaccion transaccion = new Transaccion();
        transaccion.setNombreUsuario("Juan Pérez");
        transaccion.setTipo(TipoTransaccion.Ingreso);
        transaccion.setMonto(1500.0);
        transaccion.setDescripcion("Descripción válida");
        transaccion.setFecha(null);

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccion);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fecha")));
        assertTrue(violaciones.stream().anyMatch(v -> v.getMessage().contains("obligatoria")));
    }

    @Test
    void deberiaAceptarDiferentesFechasValidas() {
        // Given
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaAnterior = LocalDate.of(2023, 1, 1);
        LocalDate fechaFutura = LocalDate.of(2025, 12, 31);

        Transaccion transaccion1 = new Transaccion();
        transaccion1.setNombreUsuario("Usuario 1");
        transaccion1.setTipo(TipoTransaccion.Ingreso);
        transaccion1.setMonto(1000.0);
        transaccion1.setFecha(fechaHoy);

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setNombreUsuario("Usuario 2");
        transaccion2.setTipo(TipoTransaccion.Egreso);
        transaccion2.setMonto(1500.0);
        transaccion2.setFecha(fechaAnterior);

        Transaccion transaccion3 = new Transaccion();
        transaccion3.setNombreUsuario("Usuario 3");
        transaccion3.setTipo(TipoTransaccion.Ingreso);
        transaccion3.setMonto(2000.0);
        transaccion3.setFecha(fechaFutura);

        // When
        Set<ConstraintViolation<Transaccion>> violaciones1 = validator.validate(transaccion1);
        Set<ConstraintViolation<Transaccion>> violaciones2 = validator.validate(transaccion2);
        Set<ConstraintViolation<Transaccion>> violaciones3 = validator.validate(transaccion3);

        // Then
        assertTrue(violaciones1.isEmpty());
        assertTrue(violaciones2.isEmpty());
        assertTrue(violaciones3.isEmpty());
    }

    @Test
    void deberiaValidarMultiplesErroresSimultaneamente() {
        // Given - Transacción con múltiples errores
        Transaccion transaccionInvalida = new Transaccion();
        transaccionInvalida.setNombreUsuario(""); // Nombre vacío
        transaccionInvalida.setTipo(null); // Tipo null
        transaccionInvalida.setMonto(100.0); // Monto menor a 500
        transaccionInvalida.setDescripcion("A".repeat(150)); // Descripción muy larga
        transaccionInvalida.setFecha(null); // Fecha null

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(transaccionInvalida);

        // Then
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.size() >= 4); // Al menos 4 errores

        List<String> propiedadesConError =
                violaciones.stream().map(v -> v.getPropertyPath().toString()).toList();

        assertTrue(propiedadesConError.contains("nombreUsuario"));
        assertTrue(propiedadesConError.contains("tipo"));
        assertTrue(propiedadesConError.contains("monto"));
        assertTrue(propiedadesConError.contains("descripcion"));
        assertTrue(propiedadesConError.contains("fecha"));
    }

    @Test
    void deberiaValidarTransaccionIngresoCorrectamente() {
        // Given
        Transaccion ingreso = new Transaccion();
        ingreso.setNombreUsuario("Ana López");
        ingreso.setTipo(TipoTransaccion.Ingreso);
        ingreso.setMonto(3500.0);
        ingreso.setDescripcion("Salario");
        ingreso.setFecha(LocalDate.now());

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(ingreso);

        // Then
        assertTrue(violaciones.isEmpty());
    }

    @Test
    void deberiaValidarTransaccionEgresoCorrectamente() {
        // Given
        Transaccion egreso = new Transaccion();
        egreso.setNombreUsuario("Carlos Ruiz");
        egreso.setTipo(TipoTransaccion.Egreso);
        egreso.setMonto(800.0);
        egreso.setDescripcion("Compra supermercado");
        egreso.setFecha(LocalDate.of(2024, 6, 15));

        // When
        Set<ConstraintViolation<Transaccion>> violaciones = validator.validate(egreso);

        // Then
        assertTrue(violaciones.isEmpty());
    }
}
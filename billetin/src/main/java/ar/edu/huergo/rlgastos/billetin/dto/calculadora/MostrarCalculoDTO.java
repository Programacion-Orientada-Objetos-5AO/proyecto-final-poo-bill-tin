package ar.edu.huergo.rlgastos.billetin.dto.calculadora;

public record MostrarCalculoDTO(
        Long idCalculo,
        String operacion,
        Double parametro1,
        Double parametro2,
        Double resultado


) {
}

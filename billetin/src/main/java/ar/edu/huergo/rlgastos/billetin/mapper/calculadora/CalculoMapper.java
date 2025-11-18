package ar.edu.huergo.rlgastos.billetin.mapper.calculadora;

import java.util.List;
import org.springframework.stereotype.Component;
import ar.edu.huergo.rlgastos.billetin.dto.calculadora.ActualizarCalculoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.calculadora.CrearCalculoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.calculadora.MostrarCalculoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.calculadora.Calculo;


@Component
public class CalculoMapper {

    public Calculo toEntity(CrearCalculoDTO dto) {
        Calculo calculo = new Calculo();
        calculo.setOperacion(dto.operacion());
        calculo.setParametro1(dto.parametro1());
        calculo.setParametro2(dto.parametro2());
        return calculo;
    }

    public MostrarCalculoDTO toMostrarDTO(Calculo calculo) {
        return new MostrarCalculoDTO(
            calculo.getIdCalculo(),
            calculo.getOperacion(),
            calculo.getParametro1(),
            calculo.getParametro2(),
            calculo.getResultado()

        );
    }

    public void actualizarEntity(Calculo calculo, ActualizarCalculoDTO dto) {
        calculo.setOperacion(dto.operacion());
        calculo.setParametro1(dto.parametro1());
        calculo.setParametro2(dto.parametro2());
    }

    public List<MostrarCalculoDTO> toMostrarDtoList(List<Calculo> calculos) {
        return calculos.stream().map(this::toMostrarDTO).toList();
    }
}

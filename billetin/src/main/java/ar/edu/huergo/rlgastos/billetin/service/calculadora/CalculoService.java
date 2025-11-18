package ar.edu.huergo.rlgastos.billetin.service.calculadora;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.calculadora.ActualizarCalculoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.calculadora.Calculo;
import ar.edu.huergo.rlgastos.billetin.repository.calculadora.CalculoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculoService {

    private final CalculoRepository calculoRepository;

    public List<Calculo> getCalculos() {
        return calculoRepository.findAll();
    }

    public Optional<Calculo> getCalculo(Long id) {
        return calculoRepository.findById(id);
    }

    public void crearCalculo(Calculo calculo) {
        calculoRepository.save(calculo);
    }

    public void actualizarCalculo(Long id, ActualizarCalculoDTO dto) throws NotFoundException {
        Calculo calculo = calculoRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        calculo.setOperacion(dto.operacion());
        calculo.setParametro1(dto.parametro1());
        calculo.setParametro2(dto.parametro2());

        calculoRepository.save(calculo);
    }

    public void eliminarCalculo(Long id) {
        calculoRepository.deleteById(id);
    }
    
    public Double calcularResultado1(Double parametro1, Double parametro2) {
    return calculoRepository.findByParametros(parametro1, parametro2)
                .stream()
                .mapToDouble(Calculo::getResultado)
                .sum();

    }

    public Map<String, Object> calcularResultado2(Double parametro1, Double parametro2, String operacion) {
        operacion = operacion.toUpperCase().trim();
        
        Double resultado = calcularResultado1(parametro1, parametro2);

        if(operacion=="dividir"){
            resultado = parametro1 % parametro2;

            return Map.of(
                "operacion", operacion,
                "resultado", resultado
        );
        }


        if(operacion=="multiplicar"){
            resultado = parametro1 * parametro2;

            return Map.of(
                "operacion", operacion,
                "resultado", resultado

            ); 
        }


        if(operacion=="sumar"){
           resultado = parametro1 + parametro2;

            return Map.of(
                "operacion", operacion,
                "resultado", resultado

            ); 
        }
            resultado = parametro1 + parametro2;

            return Map.of(
                "operacion", operacion,
                "resultado", resultado

        );

        if(operacion=="restar"){
            resultado = parametro1 - parametro2;

            return Map.of(
                "operacion", operacion,
                "resultado", resultado

        );  
        }

    }

}

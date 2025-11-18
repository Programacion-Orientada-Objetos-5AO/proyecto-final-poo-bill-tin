package ar.edu.huergo.rlgastos.billetin.controller.calculadora;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.rlgastos.billetin.dto.calculadora.ActualizarCalculoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.calculadora.CrearCalculoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.calculadora.MostrarCalculoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.calculadora.Calculo;
import ar.edu.huergo.rlgastos.billetin.mapper.calculadora.CalculoMapper;
import ar.edu.huergo.rlgastos.billetin.service.calculadora.CalculoService;

@RestController
@RequestMapping("/api/calculos")
public class CalculoController {

    @Autowired
    private CalculoService calculoService;

    @Autowired
    private CalculoMapper calculoMapper;

    @GetMapping
    public ResponseEntity<List<MostrarCalculoDTO>> getCalculos() {
        List<Calculo> calculos = calculoService.getCalculos();
        return ResponseEntity.ok(calculoMapper.toMostrarDtoList(calculos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MostrarCalculoDTO> getCalculo(@PathVariable Long id) {
        Optional<Calculo> calculoOpt = calculoService.getCalculo(id);
        if (calculoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(calculoMapper.toMostrarDTO(calculoOpt.get()));
    }

    @PostMapping
    public ResponseEntity<String> crearCalculo(@RequestBody CrearCalculoDTO dto) {
        Calculo calculo = calculoMapper.toEntity(dto);
        calculoService.crearCalculo(calculo);
        return ResponseEntity.ok("Calculo creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCalculo(@PathVariable Long id, @RequestBody ActualizarCalculoDTO dto) {
        try {
            calculoService.actualizarCalculo(id, dto);
            return ResponseEntity.ok("Calculo actualizado correctamente");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCalculo(@PathVariable Long id) {
        calculoService.eliminarCalculo(id);
        return ResponseEntity.ok("Calculo eliminado correctamente");
    }


    @GetMapping("/resultado2")
    public ResponseEntity<Map<String, Object>> calcularResultado2(
        @RequestParam("100.0") Double parametro1,
        @RequestParam("100.0") Double parametro2,
        @RequestParam("sumar") String operacion) {


    Map<String, Object> resultado = calculoService.calcularResultado2(parametro1, parametro2, operacion);
    return ResponseEntity.ok(resultado);
    }

}
package ar.edu.huergo.rlgastos.billetin.controller.moneda;

import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.rlgastos.billetin.dto.moneda.ActualizarMonedaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.moneda.CrearMonedaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.moneda.MostrarMonedaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.moneda.Moneda;
import ar.edu.huergo.rlgastos.billetin.mapper.moneda.MonedaMapper;
import ar.edu.huergo.rlgastos.billetin.service.moneda.MonedaService;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/monedas")
@RequiredArgsConstructor
public class MonedaController {

    private final MonedaService monedaService;
    private final MonedaMapper monedaMapper;

    @GetMapping
    public ResponseEntity<List<MostrarMonedaDTO>> getmonedas() {
        List<Moneda> monedas = monedaService.getMonedas();
        return ResponseEntity.ok(monedaMapper.toMostrarDtoList(monedas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MostrarmonedaDTO> getmoneda(@PathVariable Long id) {
        Optional<Moneda> monedaOpt = monedaService.getmoneda(id);
        if (monedaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(monedaMapper.toMostrarDTO(monedaOpt.get()));
    }

    @PostMapping
    public ResponseEntity<String> crearmoneda(@RequestBody CrearmonedaDTO dto) {
        Moneda moneda = monedaMapper.toEntity(dto);
        monedaService.crearmoneda(moneda);
        return ResponseEntity.ok("moneda creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarmoneda(@PathVariable Long id, @RequestBody ActualizarmonedaDTO dto) {
        try {
            monedaService.actualizarmoneda(id, dto);
            return ResponseEntity.ok("moneda actualizado correctamente");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarmoneda(@PathVariable Long id) {
        monedaService.eliminarmoneda(id);
        return ResponseEntity.ok("moneda eliminado correctamente");
    }
}


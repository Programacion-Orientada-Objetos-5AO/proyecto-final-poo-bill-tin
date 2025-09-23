package ar.edu.huergo.rlgastos.billetin.controller.objetivo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.huergo.rlgastos.billetin.dto.objetivo.ActualizarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.objetivo.CrearObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.objetivo.MostrarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.objetivo.Objetivo;
import ar.edu.huergo.rlgastos.billetin.mapper.objetivo.ObjetivoMapper;
import ar.edu.huergo.rlgastos.billetin.service.objetivo.ObjetivoService;

@RestController
@RequestMapping("/api/objetivos")
public class ObjetivoController {

    @Autowired
    private ObjetivoService objetivoService;

    @Autowired
    private ObjetivoMapper objetivoMapper;

    @GetMapping
    public ResponseEntity<List<MostrarObjetivoDTO>> getObjetivos() {
        List<Objetivo> objetivos = objetivoService.getObjetivos();
        return ResponseEntity.ok(objetivoMapper.toMostrarDtoList(objetivos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MostrarObjetivoDTO> getObjetivo(@PathVariable Long id) {
        Optional<Objetivo> objetivoOpt = objetivoService.getObjetivo(id);
        if (objetivoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(objetivoMapper.toMostrarDTO(objetivoOpt.get()));
    }

    @PostMapping
    public ResponseEntity<String> crearObjetivo(@RequestBody CrearObjetivoDTO dto) {
        Objetivo objetivo = objetivoMapper.toEntity(dto);
        objetivoService.crearObjetivo(objetivo);
        return ResponseEntity.ok("Objetivo creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarObjetivo(@PathVariable Long id, @RequestBody ActualizarObjetivoDTO dto) {
        try {
            objetivoService.actualizarObjetivo(id, dto);
            return ResponseEntity.ok("Objetivo actualizado correctamente");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarObjetivo(@PathVariable Long id) {
        objetivoService.eliminarObjetivo(id);
        return ResponseEntity.ok("Objetivo eliminado correctamente");
    }
}

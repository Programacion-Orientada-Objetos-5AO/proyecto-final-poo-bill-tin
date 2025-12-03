package ar.edu.huergo.rlgastos.billetin.controller.refugio;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.rlgastos.billetin.dto.refugio.ActualizarRefugiadoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.refugio.CrearRefugiadoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.refugio.MostrarRefugiadoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.refugio.PatchRefugiadoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.refugio.Refugio;
import ar.edu.huergo.rlgastos.billetin.mapper.refugio.RefugioMapper;
import ar.edu.huergo.rlgastos.billetin.service.refugio.RefugioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/refugios")
public class RefugioController {

    @Autowired
    private RefugioService refugioService;

    @Autowired
    private RefugioMapper refugioMapper;

    @GetMapping
    public ResponseEntity<List<MostrarRefugiadoDTO>> getRefugiados() {
        List<Refugio> refugios = refugioService.getRefugiados();
        return ResponseEntity.ok(refugioMapper.toMostrarDtoList(refugios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MostrarRefugiadoDTO> getRefugiado(@PathVariable Long id) {
        Optional<Refugio> refugioOpt = this.refugioService.getRefugiado(id);
        if (refugioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Refugio refugio = refugioOpt.get();
        MostrarRefugiadoDTO dto = this.refugioMapper.toMostrarDTO(refugio);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<String> crearRefugio(@Valid@RequestBody CrearRefugiadoDTO dto) {
        Refugio refugio = refugioMapper.toEntity(dto);
        refugioService.crearRefugio(refugio);
        return ResponseEntity.created(null).body("Refugiado creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarRefugio(@PathVariable Long id, @RequestBody ActualizarRefugiadoDTO dto) throws NotFoundException {
        refugioService.actualizarRefugio(id, dto);
        return ResponseEntity.ok("Refugiado actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRefugio(@PathVariable Long id) {
        refugioService.eliminarRefugio(id);
        return ResponseEntity.ok("Refugiado eliminado correctamente");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchRefugiado(@PathVariable Long id,@RequestBody PatchRefugiadoDTO dto) {
    try {
        refugioService.patchRefugiado(id, dto);
        return ResponseEntity.ok("Refugiado actualizado parcialmente");
    } catch (NotFoundException e) {
        return ResponseEntity.notFound().build();
    }
    }

    @GetMapping("/tipo/{tipo}")
    public List<Refugio> obtenerPorTipo(@PathVariable String tipo) {
        return refugioService.obtenerPorTipo(tipo)
                              .stream()
                              .toList();
    }




}

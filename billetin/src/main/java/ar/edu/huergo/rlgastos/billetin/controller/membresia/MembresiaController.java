package ar.edu.huergo.rlgastos.billetin.controller.membresia;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.rlgastos.billetin.dto.membresia.ActualizarMembresiaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.membresia.CrearMembresiaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.membresia.MostrarMembresiaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.membresia.Membresia;
import ar.edu.huergo.rlgastos.billetin.mapper.membresia.MembresiaMapper;
import ar.edu.huergo.rlgastos.billetin.service.membresia.MembresiaService;

@RestController
@RequestMapping("/api/membresias")
public class MembresiaController {

    @Autowired
    private MembresiaService membresiaService;

    @Autowired
    private MembresiaMapper membresiaMapper;

    @GetMapping
    public ResponseEntity<List<MostrarMembresiaDTO>> getMembresias() {
        List<Membresia> membresias = this.membresiaService.getMembresias();
        return ResponseEntity.ok(membresiaMapper.toMostrarDtoList(membresias));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MostrarMembresiaDTO> getMembresia(@PathVariable Long id) {
        Optional<Membresia> membresiaOpt = this.membresiaService.getMembresia(id);
        if (membresiaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Membresia membresia = membresiaOpt.get();
        MostrarMembresiaDTO dto = this.membresiaMapper.toMostrarDTO(membresia);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<String> crearMembresia(@RequestBody CrearMembresiaDTO membresiaDto) {
        Membresia membresia = this.membresiaMapper.toEntity(membresiaDto);
        this.membresiaService.crearMembresia(membresia);
        return ResponseEntity.created(null).body("Membresía creada correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarMembresia(@PathVariable Long id, @RequestBody ActualizarMembresiaDTO membresiaDto) throws NotFoundException {
        this.membresiaService.actualizarMembresia(id, membresiaDto);
        return ResponseEntity.ok("Membresía actualizada correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMembresia(@PathVariable Long id) {
        this.membresiaService.eliminarMembresia(id);
        return ResponseEntity.ok("Membresía eliminada correctamente");
    }
}

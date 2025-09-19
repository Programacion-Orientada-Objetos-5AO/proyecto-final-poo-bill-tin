package ar.edu.huergo.rlgastos.billetin.controller.objetivo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.rlgastos.billetin.dto.objetivo.ActualizarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.objetivo.CrearObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.objetivo.MostrarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.EstadoObjetivo;
import ar.edu.huergo.rlgastos.billetin.entity.Objetivo;
import ar.edu.huergo.rlgastos.billetin.mapper.objetivo.ObjetivoMapper;
import ar.edu.huergo.rlgastos.billetin.service.objetivo.ObjetivoService;
import ar.edu.huergo.rlgastos.billetin.service.objetivo.ObjetivoSecurityService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/objetivos")
public class ObjetivoController {

    @Autowired
    private ObjetivoService objetivoService;

    @Autowired
    private ObjetivoMapper objetivoMapper;

    @Autowired
    private ObjetivoSecurityService objetivoSecurityService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MostrarObjetivoDTO>> getObjetivos() {
        List<Objetivo> objetivos = objetivoService.getObjetivos();
        return ResponseEntity.ok(objetivoMapper.toMostrarDtoList(objetivos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MostrarObjetivoDTO> getObjetivo(@PathVariable Long id) {
        if (!objetivoSecurityService.puedeAccederObjetivo(id)) {
            return ResponseEntity.status(403).build();
        }
        
        Optional<Objetivo> objetivoOpt = objetivoService.getObjetivo(id);
        if (objetivoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Objetivo objetivo = objetivoOpt.get();
        MostrarObjetivoDTO dto = objetivoMapper.toMostrarDTO(objetivo);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MostrarObjetivoDTO>> getObjetivosByUsuario(@PathVariable Long usuarioId) {
        if (!objetivoSecurityService.puedeAccederObjetivosUsuario(usuarioId)) {
            return ResponseEntity.status(403).build();
        }
        
        List<Objetivo> objetivos = objetivoService.getObjetivosByUsuario(usuarioId);
        return ResponseEntity.ok(objetivoMapper.toMostrarDtoList(objetivos));
    }

    @GetMapping("/estado")
    public ResponseEntity<List<MostrarObjetivoDTO>> getObjetivosByEstado(@RequestParam EstadoObjetivo estado) {
        List<Objetivo> objetivos = objetivoService.getObjetivosByEstado(estado);
        return ResponseEntity.ok(objetivoMapper.toMostrarDtoList(objetivos));
    }

    @PostMapping
    public ResponseEntity<String> crearObjetivo(@Valid @RequestBody CrearObjetivoDTO objetivoDto) {
        if (!objetivoSecurityService.puedeCrearObjetivo(objetivoDto.idUsuario())) {
            return ResponseEntity.status(403).body("No tienes permisos para crear objetivos para este usuario");
        }
        
        Objetivo objetivo = objetivoMapper.toEntity(objetivoDto);
        objetivoService.crearObjetivo(objetivo);
        return ResponseEntity.created(null).body("Objetivo creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarObjetivo(@PathVariable Long id, 
                                                    @Valid @RequestBody ActualizarObjetivoDTO objetivoDto) {
        if (!objetivoSecurityService.puedeAccederObjetivo(id)) {
            return ResponseEntity.status(403).body("No tienes permisos para actualizar este objetivo");
        }
        
        objetivoService.actualizarObjetivo(id, objetivoDto);
        return ResponseEntity.ok("Objetivo actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarObjetivo(@PathVariable Long id) {
        if (!objetivoSecurityService.puedeAccederObjetivo(id)) {
            return ResponseEntity.status(403).body("No tienes permisos para eliminar este objetivo");
        }
        
        objetivoService.eliminarObjetivo(id);
        return ResponseEntity.ok("Objetivo eliminado correctamente");
    }

    @PostMapping("/actualizar-estados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarEstadosObjetivos() {
        objetivoService.actualizarEstadosObjetivos();
        return ResponseEntity.ok("Estados de objetivos actualizados correctamente");
    }
}
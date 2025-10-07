package ar.edu.huergo.rlgastos.billetin.controller;



import java.time.LocalDate;
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

import ar.edu.huergo.rlgastos.billetin.dto.transaccion.ActualizarTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.dto.transaccion.CrearTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.dto.transaccion.MostrarTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;
import ar.edu.huergo.rlgastos.billetin.mapper.TransaccionMapper;
import ar.edu.huergo.rlgastos.billetin.service.TransaccionService;



@RestController 
@RequestMapping("/api/transacciones") 
public class TransaccionController {
    @Autowired
    private TransaccionService transaccionService;
    @Autowired
    private TransaccionMapper transaccionMapper;

    @GetMapping 
    public ResponseEntity<List<MostrarTransaccionDTO>> getTransacciones() {
        List<Transaccion> transacciones = this.transaccionService.getTransaccion();
        return ResponseEntity.ok(transaccionMapper.toMostrarDtoList(transacciones));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MostrarTransaccionDTO> getTransaccion(@PathVariable Long id) {
        Optional<Transaccion> transaccionOpt = this.transaccionService.getTransaccion(id);
        if (transaccionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Transaccion transaccion = transaccionOpt.get();
        MostrarTransaccionDTO dto = this.transaccionMapper.toMostrarDTO(transaccion);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<String> crearTransaccion(@RequestBody CrearTransaccionDTO transaccionDto) {
        Transaccion transaccion = this.transaccionMapper.toEntity(transaccionDto);
        this.transaccionService.crearTransaccion(transaccion);
        return ResponseEntity.created(null).body("Has hecho una transaccion correctamente");
        
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarTransaccion(@PathVariable Long id, @RequestBody ActualizarTransaccionDTO transaccionDto) throws NotFoundException{
        this.transaccionService.actualizarTransaccion(id, transaccionDto);
        return ResponseEntity.ok("Datos de la transaccion actualizados correctamente");
       
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTransaccion (@PathVariable Long id){
        this.transaccionService.eliminarTransaccion(id);
        return ResponseEntity.ok("La transaccion ha sido eliminada correctamente");
        
    }

    @GetMapping("/gasto-entre-fechas")
    public ResponseEntity<Double> calcularGasto(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {

        Double total = transaccionService.calcularGastoEntreFechas(inicio, fin);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/gastos-por-categoria/{usuarioId}")
    public ResponseEntity<Map<String, Double>> getGastosPorCategoria(@PathVariable Long usuarioId) {
        Map<String, Double> gastosPorCategoria = transaccionService.calcularGastosPorCategoria(usuarioId);
        return ResponseEntity.ok(gastosPorCategoria);
    }
}

package ar.edu.huergo.rlgastos.billetin.controller.presupuesto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;



import ar.edu.huergo.rlgastos.billetin.entity.presupuesto.Presupuesto;
import ar.edu.huergo.rlgastos.billetin.service.presupuesto.PresupuestoService;

@RestController
@RequestMapping("/api/presupuestos")
public class PresupuestoController {
    
    @Autowired
    private PresupuestoService presupuestoService;

    
    @GetMapping
    public List<Presupuesto> getPresupuestos() {
        return presupuestoService.getPresupuestos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Presupuesto> getPresupuesto(@PathVariable Long id) {
        return presupuestoService.getPresupuesto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public void crearPresupuesto(@RequestBody Presupuesto presupuesto) {
        presupuestoService.crearPresupuesto(presupuesto);
    }

    @PutMapping("/{id}")
    public void actualizarPresupuesto(@PathVariable Long id, @RequestBody Presupuesto nuevoPresupuesto) throws NotFoundException {
        presupuestoService.actualizarPresupuesto(id, nuevoPresupuesto);
    }

    @DeleteMapping("/{id}")
    public void eliminarPresupuesto(@PathVariable Long id) {
        presupuestoService.eliminarPresupuesto(id);
    }
}

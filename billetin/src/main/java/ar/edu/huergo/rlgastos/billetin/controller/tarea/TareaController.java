package ar.edu.huergo.rlgastos.billetin.controller.tarea;

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

import ar.edu.huergo.rlgastos.billetin.dto.categoria.ActualizarCategoriaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.tarea.Tarea;
import ar.edu.huergo.rlgastos.billetin.service.tarea.TareaService;

import java.util.List;
import java.util.Optional; 

import ar.edu.huergo.rlgastos.billetin.dto.tarea.ActualizarTareaDTO;


@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public List<Tarea> getTareas() {
        return tareaService.getTareas();
    }

    @GetMapping("/{id}") 
    public ResponseEntity<Tarea> getTarea(@PathVariable Long id) {
        Optional<Tarea> tarea = tareaService.getTarea(id);
        return tarea.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public void crearTarea(@RequestBody Tarea tarea) {
        tareaService.crearTarea(tarea);
    }

    @PutMapping("/{id}")
    public void actualizarTarea(@PathVariable Long id, @RequestBody ActualizarTareaDTO actualizarTareaDTO) throws Exception {
        tareaService.actualizarTarea(id, actualizarTareaDTO);
    }


    @DeleteMapping("/{id}")  
    public void eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
    }

}

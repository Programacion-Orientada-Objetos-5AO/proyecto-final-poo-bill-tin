package ar.edu.huergo.rlgastos.billetin.service.tarea;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.tarea.ActualizarTareaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.tarea.Tarea;
import ar.edu.huergo.rlgastos.billetin.repository.tarea.TareaRepository;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    public List<Tarea> getTareas() {
        return (List <Tarea>) this.tareaRepository.findAll();
    }

    public Optional<Tarea> getTarea(Long id) {
        return this.tareaRepository.findById(id);
    }

    public void crearTarea(Tarea tarea) {
        this.tareaRepository.save(tarea);
    }


    public void actualizarTarea(Long id, ActualizarTareaDTO dto) throws NotFoundException {
        Tarea tarea = this.tareaRepository.findById(id).orElseThrow(() -> new NotFoundException());
        tarea.setTitulo(dto.titulo());
        tarea.setDescripcion(dto.descripcion());
        tarea.setCreador(dto.creador());
        this.tareaRepository.save(tarea);
    }

    
    public void eliminarTarea(Long id) {
        this.tareaRepository.deleteById(id);
    }
}


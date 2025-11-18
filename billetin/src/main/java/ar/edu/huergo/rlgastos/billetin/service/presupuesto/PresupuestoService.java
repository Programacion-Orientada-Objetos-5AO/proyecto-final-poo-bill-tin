package ar.edu.huergo.rlgastos.billetin.service.presupuesto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.objetivo.ActualizarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.objetivo.Objetivo;
import ar.edu.huergo.rlgastos.billetin.entity.presupuesto.Presupuesto;
import ar.edu.huergo.rlgastos.billetin.repository.presupuesto.PresupuestoRepository;


@Service
public class PresupuestoService {

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    public List<Presupuesto> getPresupuestos() {
        return this.presupuestoRepository.findAll();
    }
    
    public Optional<Presupuesto> getPresupuesto(Long id) {
        return this.presupuestoRepository.findById(id);
    }

    public void crearPresupuesto(Presupuesto presupuesto) {
        this.presupuestoRepository.save(presupuesto);
    }

    public void eliminarPresupuesto(Long id) {
    presupuestoRepository.deleteById(id);
    }

    public void actualizarPresupuesto(Long id, Presupuesto nuevoPresupuesto) throws NotFoundException {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());

    presupuesto.setNombre(nuevoPresupuesto.getNombre());
    presupuesto.setMontoTotal(nuevoPresupuesto.getMontoTotal());
    presupuesto.setPeriodo(nuevoPresupuesto.getPeriodo());

    presupuestoRepository.save(presupuesto);
    }
}

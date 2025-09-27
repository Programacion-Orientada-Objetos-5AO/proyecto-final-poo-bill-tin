package ar.edu.huergo.rlgastos.billetin.service.membresia;

import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.membresia.ActualizarMembresiaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.membresia.Membresia;
import ar.edu.huergo.rlgastos.billetin.repository.membresia.MembresiaRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MembresiaService {

    @Autowired
    private MembresiaRepository membresiaRepository;

    public List<Membresia> getMembresias() {
        return (List<Membresia>) this.membresiaRepository.findAll();
    }

    public Optional<Membresia> getMembresia(Long id) {
        return this.membresiaRepository.findById(id);
    }

    public void crearMembresia(Membresia membresia) {
        this.membresiaRepository.save(membresia);
    }

    public void actualizarMembresia(Long id, ActualizarMembresiaDTO dto) throws NotFoundException {
        Membresia membresia = this.membresiaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
        membresia.setPrecio(dto.precio());
        membresia.setDuracion(dto.duracion());
        membresia.setBeneficios(dto.beneficios());
        this.membresiaRepository.save(membresia);
    }

    public void eliminarMembresia(Long id) {
        this.membresiaRepository.deleteById(id);
    }
}

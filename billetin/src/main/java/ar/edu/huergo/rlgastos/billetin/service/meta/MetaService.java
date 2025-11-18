package ar.edu.huergo.rlgastos.billetin.service.meta;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import ar.edu.huergo.rlgastos.billetin.entity.meta.Meta;
import ar.edu.huergo.rlgastos.billetin.repository.meta.MetaRepository;


@Service
public class MetaService {
    @Autowired
    private MetaRepository metaRepository;

    public List<Meta> getMetas() {
        return this.metaRepository.findAll();
    }

    public Optional<Meta> getMeta(Long id) {
        return this.metaRepository.findById(id);
    }

    public void crearMeta(Meta meta) {
        this.metaRepository.save(meta);
    }

    public void eliminarMeta(Long id) {
        this.metaRepository.deleteById(id);
    }

    public void actualizarMeta (Long id, Meta nuevaMeta) throws NotFoundException {
        Meta meta = metaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
        meta.setMontoObjetivo(nuevaMeta.getMontoObjetivo());
        meta.setFechaLimite(nuevaMeta.getFechaLimite());
        this.metaRepository.save(meta);
    }
}

package ar.edu.huergo.rlgastos.billetin.service.objetivo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.objetivo.ActualizarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.objetivo.Objetivo;

import ar.edu.huergo.rlgastos.billetin.repository.objetivo.ObjetivoRepository;


@Service
public class ObjetivoService {

    @Autowired
    private ObjetivoRepository objetivoRepository;

    public List<Objetivo> getObjetivos() {
        return this.objetivoRepository.findAll();
    }

    public Optional<Objetivo> getObjetivo(Long id) {
        return this.objetivoRepository.findById(id);
    }

    public void crearObjetivo(Objetivo objetivo) {

        this.objetivoRepository.save(objetivo);
    }

    public void actualizarObjetivo(Long id, ActualizarObjetivoDTO dto) throws NotFoundException {
        Objetivo objetivo = objetivoRepository.findById(id).orElseThrow(NotFoundException::new);

        objetivo.setNombre(dto.nombre());
        objetivo.setMontoMeta(dto.montoMeta());
        objetivo.setFechaInicio(dto.fechaInicio());
        objetivo.setFechaFin(dto.fechaFin());
        objetivo.setEstado(dto.estado());

        this.objetivoRepository.save(objetivo);
    }

    public void eliminarObjetivo(Long id) {
        this.objetivoRepository.deleteById(id);
    }
}

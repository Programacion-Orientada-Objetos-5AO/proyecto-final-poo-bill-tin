package ar.edu.huergo.rlgastos.billetin.service.refugio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.refugio.ActualizarRefugiadoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.refugio.PatchRefugiadoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;
import ar.edu.huergo.rlgastos.billetin.entity.refugio.Refugio;
import ar.edu.huergo.rlgastos.billetin.repository.refugio.RefugioRepository;

@Service
public class RefugioService {

    @Autowired
    private RefugioRepository refugioRepository;

    public List<Refugio> getRefugiados() {
       return ((List<Refugio>) this.refugioRepository.findAll());
    }

    public Optional<Refugio> getRefugiado(Long id) {
        return this.refugioRepository.findById(id);
    }

    public void crearRefugio(Refugio refugio) {
        this.refugioRepository.save(refugio);
    }

    public void actualizarRefugio(Long id, ActualizarRefugiadoDTO dto) throws NotFoundException {
        Refugio refugio = refugioRepository.findById(id).orElseThrow(NotFoundException::new);
        refugio.setNombre(dto.nombre());
        refugio.setTipo(dto.tipo());
        refugio.setEdad(dto.edad());
        refugio.setAdoptado(dto.adoptado());
        this.refugioRepository.save(refugio);
    }

    public void eliminarRefugio(Long id) {
        this.refugioRepository.deleteById(id);
    }

    public void patchRefugiado(Long id, PatchRefugiadoDTO dto) throws NotFoundException {
    Refugio refugio = refugioRepository.findById(id)
            .orElseThrow(NotFoundException::new);
    
        if (dto.adoptado() != null) {
        refugio.setAdoptado(dto.adoptado());
    }

    }

    public List<Refugio> obtenerPorTipo(String tipo) {
        return refugioRepository.findByTipo(tipo);
    }

}



package ar.edu.huergo.rlgastos.billetin.service.moneda;


import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.moneda.ActualizarMonedaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.moneda.Moneda;
import ar.edu.huergo.rlgastos.billetin.repository.moneda.MonedaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonedaService {

    private final MonedaRepository monedaRepository;

    public List<Moneda> getMonedas() {
        return monedaRepository.findAll();
    }

    public Optional<Moneda> getMoneda(Long id) {
        return monedaRepository.findById(id);
    }

    public void crearMoneda(Moneda moneda) {
        monedaRepository.save(moneda);
    }

    public void actualizarMoneda(Long id, ActualizarMonedaDTO dto) throws NotFoundException {
        Moneda moneda = monedaRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        moneda.setCodigo(dto.codigo());
        moneda.setNombre(dto.nombre());
        monedaRepository.save(moneda);
    }

    public void eliminarMoneda(Long id) {
        monedaRepository.deleteById(id);
    }
}

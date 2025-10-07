package ar.edu.huergo.rlgastos.billetin.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.transaccion.ActualizarTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;
import ar.edu.huergo.rlgastos.billetin.repository.transaccion.TransaccionRepository;

@Service
public class TransaccionService {
    @Autowired      
    private TransaccionRepository transaccionRepository;

    public List<Transaccion> getTransaccion() { 
        return ((List<Transaccion>) this.transaccionRepository.findAll());
    }

    public Optional<Transaccion> getTransaccion(Long id){
        return this.transaccionRepository.findById(id);
    }

    public void crearTransaccion(Transaccion transaccion) {
        this.transaccionRepository.save(transaccion);
    }

    public void actualizarTransaccion(Long id, ActualizarTransaccionDTO TransaccionDTO) throws NotFoundException {
        Transaccion transaccion = this.transaccionRepository.findById(id).orElseThrow(() -> new NotFoundException());
        transaccion.setMonto(TransaccionDTO.monto());
        transaccion.setDescripcion(TransaccionDTO.descripcion());
        transaccion.setFecha(TransaccionDTO.fecha());
        this.transaccionRepository.save(transaccion);
    }

    public void eliminarTransaccion(Long id) {
        this.transaccionRepository.deleteById(id);
    }

    public Double calcularGastoEntreFechas(LocalDate inicio, LocalDate fin) {
        return transaccionRepository.findByFechaBetween(inicio, fin)
                .stream()
                .mapToDouble(Transaccion::getMonto)
                .sum();
    }

    public Map<String, Double> calcularGastosPorCategoria(Long usuarioId) {
        List<Transaccion> transacciones = transaccionRepository.findByUsuarioId(usuarioId);

        Map<String, Double> resultado = new HashMap<>();
        for (Transaccion t : transacciones) {
            String categoria = t.getCategoria().getNombre();
            resultado.put(categoria, resultado.getOrDefault(categoria, 0.0) + t.getMonto());
        }
        return resultado;
    }

}

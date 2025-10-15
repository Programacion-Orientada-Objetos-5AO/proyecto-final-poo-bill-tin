package ar.edu.huergo.rlgastos.billetin.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public Map<String, Object> calcularGastoConvertido(LocalDate inicio, LocalDate fin, String monedaDestino) {
        monedaDestino = monedaDestino.toUpperCase().trim();
        
        Double total = calcularGastoEntreFechas(inicio, fin);

        if (total == null || total == 0) {
            return Map.of(
                "montoTotalARS", 0,
                "monedaDestino", monedaDestino,
                "convertido", 0.0
            );
        }

        String apiKey = "69be1542f0fa2d318d610dd1";
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/ARS";
        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> body = response.getBody();

        if (body == null || !body.containsKey("conversion_rates")) {
            throw new RuntimeException("No se pudo obtener el tipo de cambio");
        }

        Map<String, Number> rates = (Map<String, Number>) body.get("conversion_rates");
        
        if (!rates.containsKey(monedaDestino)) {
            throw new RuntimeException("Moneda no soportada: " + monedaDestino);
        }
        
        Double rate = rates.get(monedaDestino).doubleValue();
        Double convertido = total * rate;

        return Map.of(
            "montoTotalARS", total,
            "monedaDestino", monedaDestino,
            "tipoCambio", rate,
            "convertido", convertido
        );
    }
}
    



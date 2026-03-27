package ar.edu.huergo.rlgastos.billetin.controller;

import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;
import ar.edu.huergo.rlgastos.billetin.repository.transaccion.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {
    
    @Autowired
    private TransaccionRepository transaccionRepository;
    
    @GetMapping("/saludo")
    public Map<String, String> getSaludo() {
        return Map.of(
            "mensaje", "Hola desde Spring Boot",
            "estado", "success",
            "timestamp", new Date().toString()
        );
    }
    
    @GetMapping("/gastos")
    public List<Transaccion> getGastos() {
        // Devuelve TODAS las transacciones de la base de datos
        return transaccionRepository.findAll();
    }
    
    @PostMapping("/gasto")
    public Transaccion crearGasto(@RequestBody Transaccion transaccion) {
        // Guarda la transacción en la base de datos
        return transaccionRepository.save(transaccion);
    }
    
    @GetMapping("/estado")
    public Map<String, Object> getEstado() {
        return Map.of(
            "servidor", "activo",
            "version", "1.0",
            "database", "H2",
            "security", "enabled",
            "totalTransacciones", transaccionRepository.count()
        );
    }
}
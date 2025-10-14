package ar.edu.huergo.rlgastos.billetin.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {
    
    @GetMapping("/saludo")
    public Map<String, String> getSaludo() {
        return Map.of(
            "mensaje", "Hola desde Spring Boot",
            "estado", "success",
            "timestamp", new Date().toString()
        );
    }
    
    @GetMapping("/gastos")
    public List<Map<String, Object>> getGastos() {
        // Datos de ejemplo - luego conectarás con tu base de datos
        return List.of(
            Map.of("id", 1, "descripcion", "Supermercado", "monto", 5000.0),
            Map.of("id", 2, "descripcion", "Transporte", "monto", 1500.0),
            Map.of("id", 3, "descripcion", "Servicios", "monto", 8000.0)
        );
    }
    
    @PostMapping("/gasto")
    public Map<String, Object> crearGasto(@RequestBody Map<String, Object> gasto) {
        // Aquí procesarías y guardarías el gasto
        return Map.of(
            "mensaje", "Gasto creado exitosamente",
            "gasto", gasto,
            "id", new Random().nextInt(1000)
        );
    }
    
    @GetMapping("/estado")
    public Map<String, Object> getEstado() {
        return Map.of(
            "servidor", "activo",
            "version", "1.0",
            "database", "H2",
            "security", "enabled"
        );
    }
}
package ar.edu.huergo.rlgastos.billetin.service.objetivo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.objetivo.ActualizarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.entity.EstadoObjetivo;
import ar.edu.huergo.rlgastos.billetin.entity.Objetivo;
import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import ar.edu.huergo.rlgastos.billetin.repository.objetivo.ObjetivoRepository;
import ar.edu.huergo.rlgastos.billetin.repository.security.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ObjetivoService {

    @Autowired
    private ObjetivoRepository objetivoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Objetivo> getObjetivos() {
        return objetivoRepository.findAll();
    }

    public Optional<Objetivo> getObjetivo(Long id) {
        return objetivoRepository.findById(id);
    }

    public List<Objetivo> getObjetivosByUsuario(Long usuarioId) {
        return objetivoRepository.findByUsuarioId(usuarioId);
    }

    public List<Objetivo> getObjetivosByEstado(EstadoObjetivo estado) {
        return objetivoRepository.findByEstado(estado);
    }

    public Objetivo crearObjetivo(Objetivo objetivo) {
        // Validar que las fechas sean coherentes
        if (objetivo.getFechaInicio().isAfter(objetivo.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(objetivo.getUsuario().getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        
        objetivo.setUsuario(usuario);
        
        // Si no se especifica estado, por defecto será EN_PROCESO
        if (objetivo.getEstado() == null) {
            objetivo.setEstado(EstadoObjetivo.EN_PROCESO);
        }

        return objetivoRepository.save(objetivo);
    }

    public Objetivo actualizarObjetivo(Long id, ActualizarObjetivoDTO dto) {
        Objetivo objetivo = objetivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Objetivo no encontrado con ID: " + id));

        // Validar fechas
        if (dto.fechaInicio().isAfter(dto.fechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        objetivo.setNombre(dto.nombre());
        objetivo.setMontoMeta(dto.montoMeta());
        objetivo.setFechaInicio(dto.fechaInicio());
        objetivo.setFechaFin(dto.fechaFin());
        objetivo.setEstado(dto.estado());

        return objetivoRepository.save(objetivo);
    }

    public void eliminarObjetivo(Long id) {
        if (!objetivoRepository.existsById(id)) {
            throw new EntityNotFoundException("Objetivo no encontrado con ID: " + id);
        }
        objetivoRepository.deleteById(id);
    }

    // Método para actualizar automáticamente el estado de los objetivos
    public void actualizarEstadosObjetivos() {
        List<Objetivo> objetivos = objetivoRepository.findByEstado(EstadoObjetivo.EN_PROCESO);
        LocalDate hoy = LocalDate.now();

        for (Objetivo objetivo : objetivos) {
            if (objetivo.getFechaFin().isBefore(hoy)) {
                // Si ya pasó la fecha límite y no está cumplido, marcarlo como no cumplido
                objetivo.setEstado(EstadoObjetivo.NO_CUMPLIDO);
                objetivoRepository.save(objetivo);
            }
        }
    }
}
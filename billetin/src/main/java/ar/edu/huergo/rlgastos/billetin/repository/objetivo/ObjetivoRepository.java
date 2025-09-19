package ar.edu.huergo.rlgastos.billetin.repository.objetivo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.rlgastos.billetin.entity.EstadoObjetivo;
import ar.edu.huergo.rlgastos.billetin.entity.Objetivo;

@Repository
public interface ObjetivoRepository extends JpaRepository<Objetivo, Long> {
    
    // Buscar objetivos por usuario
    List<Objetivo> findByUsuarioId(Long usuarioId);
    
    // Buscar objetivos por estado
    List<Objetivo> findByEstado(EstadoObjetivo estado);
    
    // Buscar objetivos por usuario y estado
    @Query("SELECT o FROM Objetivo o WHERE o.usuario.id = :usuarioId AND o.estado = :estado")
    List<Objetivo> findByUsuarioIdAndEstado(@Param("usuarioId") Long usuarioId, @Param("estado") EstadoObjetivo estado);
}
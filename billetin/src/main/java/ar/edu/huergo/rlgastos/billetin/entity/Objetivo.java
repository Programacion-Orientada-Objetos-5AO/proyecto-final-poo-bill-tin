package ar.edu.huergo.rlgastos.billetin.entity;

import java.time.LocalDate;

import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Objetivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idObjetivo;

    @NotBlank(message = "El nombre del objetivo es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del objetivo debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El monto meta es obligatorio")
    @DecimalMin(value = "1000.0", message = "El monto meta debe ser mayor o igual a 1000 pesos")
    private Double montoMeta;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @NotNull(message = "El estado del objetivo es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoObjetivo estado;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;
}
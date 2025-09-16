package ar.edu.huergo.rlgastos.billetin.entity;


import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Transaccion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del usuario debe tener entre 2 y 100 caracteres")
    private String nombreUsuario;


    @NotNull(message = "El tipo de transacción es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;


   
    @DecimalMin(value = "500.0", message = "El monto debe ser mayor o igual a 500 pesos")
    private Double monto;
   
    @Size(max = 100, message = "La descripción no puede superar los 100 caracteres")
    private String descripcion;


    @NotNull(message = "La fecha de la transacción es obligatoria")
    private LocalDate fecha;
}

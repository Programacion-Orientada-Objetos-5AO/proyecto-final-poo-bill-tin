package ar.edu.huergo.rlgastos.billetin.entity.presupuesto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nombre;

    private Double montoTotal;

    private String periodo;
    
}

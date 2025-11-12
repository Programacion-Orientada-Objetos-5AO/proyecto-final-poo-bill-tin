package ar.edu.huergo.rlgastos.billetin.entity.membresia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity

public class Membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMembresia;

    private String nombre;

    private Double precio;

    private String beneficios;

    private Date duracion;

    @OneToMany(mappedBy = "membresia")
    private List<Usuario> usuarios = new ArrayList<>();

}   
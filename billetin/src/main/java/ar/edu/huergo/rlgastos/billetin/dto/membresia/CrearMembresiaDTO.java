package ar.edu.huergo.rlgastos.billetin.dto.membresia;
import java.util.Date;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearMembresiaDTO(

    @NotBlank(message = "El nombre de la membresía es obligatorio")
    String nombre,

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "500.0", message = "El precio debe ser mayor o igual a 500")
    Double precio,

    @NotBlank(message = "Los beneficios son obligatorios")
    String beneficios,

    @NotBlank(message = "La duración es obligatoria")
    Date duracion
) {}
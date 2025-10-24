package ar.edu.huergo.rlgastos.billetin.dto.security;

public record RegistroResponseDTO(
        UsuarioDTO usuario,
        String token,
        DatosInicialesDTO datosIniciales
) {
}

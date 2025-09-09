package ar.edu.huergo.rlgastos.billetin.dto.security;

import java.util.List;

public record UsuarioDTO(String username, List<String> roles) {
    
}


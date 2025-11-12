package ar.edu.huergo.rlgastos.billetin.dto.security;

import java.util.List;

public record UsuarioDTO(String nombre, String username,List<String> roles) {
    
}


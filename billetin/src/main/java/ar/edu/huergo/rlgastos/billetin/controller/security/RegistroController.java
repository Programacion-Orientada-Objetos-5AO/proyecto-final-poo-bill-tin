package ar.edu.huergo.rlgastos.billetin.controller.security;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.rlgastos.billetin.dto.categoria.MostrarCategoriaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.membresia.MostrarMembresiaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.moneda.MostrarMonedaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.objetivo.MostrarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.security.DatosInicialesDTO;
import ar.edu.huergo.rlgastos.billetin.dto.security.RegistrarDTO;
import ar.edu.huergo.rlgastos.billetin.dto.security.RegistroResponseDTO;
import ar.edu.huergo.rlgastos.billetin.dto.security.UsuarioDTO;
import ar.edu.huergo.rlgastos.billetin.dto.transaccion.MostrarTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import ar.edu.huergo.rlgastos.billetin.mapper.TransaccionMapper;
import ar.edu.huergo.rlgastos.billetin.mapper.categoria.CategoriaMapper;
import ar.edu.huergo.rlgastos.billetin.mapper.membresia.MembresiaMapper;
import ar.edu.huergo.rlgastos.billetin.mapper.moneda.MonedaMapper;
import ar.edu.huergo.rlgastos.billetin.mapper.objetivo.ObjetivoMapper;
import ar.edu.huergo.rlgastos.billetin.mapper.security.UsuarioMapper;
import ar.edu.huergo.rlgastos.billetin.service.TransaccionService;
import ar.edu.huergo.rlgastos.billetin.service.categoria.CategoriaService;
import ar.edu.huergo.rlgastos.billetin.service.membresia.MembresiaService;
import ar.edu.huergo.rlgastos.billetin.service.moneda.MonedaService;
import ar.edu.huergo.rlgastos.billetin.service.objetivo.ObjetivoService;
import ar.edu.huergo.rlgastos.billetin.service.security.JwtTokenService;
import ar.edu.huergo.rlgastos.billetin.service.security.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/registro")
@RequiredArgsConstructor
public class RegistroController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;
    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;
    private final MembresiaService membresiaService;
    private final MembresiaMapper membresiaMapper;
    private final MonedaService monedaService;
    private final MonedaMapper monedaMapper;
    private final TransaccionService transaccionService;
    private final TransaccionMapper transaccionMapper;
    private final ObjetivoService objetivoService;
    private final ObjetivoMapper objetivoMapper;

    @PostMapping
    public ResponseEntity<RegistroResponseDTO> registrarCliente(@Valid @RequestBody RegistrarDTO registrarDTO) {
        Usuario usuario = usuarioMapper.toEntity(registrarDTO);
        Usuario nuevoUsuario = usuarioService.registrar(
                usuario,
                registrarDTO.password(),
                registrarDTO.verificacionPassword(),
                registrarDTO.membresia());

        UsuarioDTO usuarioDTO = usuarioMapper.toDTO(nuevoUsuario);
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuarioDTO.username());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String token = jwtTokenService.generarToken(userDetails, roles);

        RegistroResponseDTO response = new RegistroResponseDTO(usuarioDTO, token, obtenerDatosInicialesInterno());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/datos-iniciales")
    public ResponseEntity<DatosInicialesDTO> obtenerDatosIniciales() {
        return ResponseEntity.ok(obtenerDatosInicialesInterno());
    }

    private DatosInicialesDTO obtenerDatosInicialesInterno() {
        List<MostrarCategoriaDTO> categorias = categoriaMapper.toMostrarDtoList(categoriaService.getCategorias());
        List<MostrarMembresiaDTO> membresias = membresiaMapper.toMostrarDtoList(membresiaService.getMembresias());
        List<MostrarMonedaDTO> monedas = monedaMapper.toMostrarDtoList(monedaService.getMonedas());
        List<MostrarTransaccionDTO> transacciones = transaccionMapper.toMostrarDtoList(transaccionService.getTransaccion());
        List<MostrarObjetivoDTO> objetivos = objetivoMapper.toMostrarDtoList(objetivoService.getObjetivos());

        return new DatosInicialesDTO(categorias, membresias, monedas, transacciones, objetivos);
    }
}

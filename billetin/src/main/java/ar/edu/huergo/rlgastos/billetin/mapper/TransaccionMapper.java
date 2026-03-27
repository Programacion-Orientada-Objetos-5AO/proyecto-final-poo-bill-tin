package ar.edu.huergo.rlgastos.billetin.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import ar.edu.huergo.rlgastos.billetin.dto.transaccion.ActualizarTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.dto.transaccion.CrearTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.dto.transaccion.MostrarTransaccionDTO;
import ar.edu.huergo.rlgastos.billetin.entity.Transaccion;
import ar.edu.huergo.rlgastos.billetin.entity.categoria.Categoria;
import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import ar.edu.huergo.rlgastos.billetin.entity.moneda.Moneda;
import ar.edu.huergo.rlgastos.billetin.repository.categoria.CategoriaRepository;
import ar.edu.huergo.rlgastos.billetin.repository.moneda.MonedaRepository;
import ar.edu.huergo.rlgastos.billetin.repository.security.UsuarioRepository;

@Component
public class TransaccionMapper {

    private final MonedaRepository monedaRepository;

    private final  UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public TransaccionMapper(UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository, MonedaRepository monedaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.monedaRepository = monedaRepository;
    }

    public Transaccion toEntity(CrearTransaccionDTO dto) {
        Transaccion transaccion = new Transaccion();
        transaccion.setMonto(dto.monto());
        transaccion.setDescripcion(dto.descripcion());
        transaccion.setFecha(dto.fecha());

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Categoria categoria = categoriaRepository.findById(dto.idCategoria())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        Moneda moneda = monedaRepository.findById(dto.idMoneda())
                .orElseThrow(() -> new RuntimeException("Moneda no encontrada"));

        transaccion.setUsuario(usuario);
        transaccion.setNombreUsuario(usuario.getNombre());
        transaccion.setCategoria(categoria);
        transaccion.setMoneda(moneda);

        return transaccion;
    }

    public MostrarTransaccionDTO toMostrarDTO(Transaccion transaccion) {
        return new MostrarTransaccionDTO(
                transaccion.getId(),
                transaccion.getMonto(),
                transaccion.getFecha(),
                transaccion.getUsuario().getNombre(),
                transaccion.getCategoria().getNombre(),
                transaccion.getMoneda().getNombre()
        );
    }

    public void actualizarEntity(Transaccion transaccion, ActualizarTransaccionDTO dto) {
        transaccion.setDescripcion(dto.descripcion());
        transaccion.setMonto(dto.monto());
        transaccion.setFecha(dto.fecha());

        if (dto.idUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            transaccion.setUsuario(usuario);
        }

        if (dto.idCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.idCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
            transaccion.setCategoria(categoria);
        }
        if (dto.idMoneda() != null) {
            Moneda moneda = monedaRepository.findById(dto.idMoneda())
                    .orElseThrow(() -> new RuntimeException("Moneda no encontrada"));
            transaccion.setMoneda(moneda);
        }
    }

    public List<MostrarTransaccionDTO> toMostrarDtoList(List<Transaccion> transacciones) {
        return transacciones.stream()
                .map(this::toMostrarDTO)
                .toList();
    }
}

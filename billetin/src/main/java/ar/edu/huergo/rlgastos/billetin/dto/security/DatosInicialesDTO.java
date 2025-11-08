package ar.edu.huergo.rlgastos.billetin.dto.security;

import java.util.List;

import ar.edu.huergo.rlgastos.billetin.dto.categoria.MostrarCategoriaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.membresia.MostrarMembresiaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.moneda.MostrarMonedaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.objetivo.MostrarObjetivoDTO;
import ar.edu.huergo.rlgastos.billetin.dto.transaccion.MostrarTransaccionDTO;

public record DatosInicialesDTO(
        List<MostrarCategoriaDTO> categorias,
        List<MostrarMembresiaDTO> membresias,
        List<MostrarMonedaDTO> monedas,
        List<MostrarTransaccionDTO> transacciones,
        List<MostrarObjetivoDTO> objetivos
) {
}

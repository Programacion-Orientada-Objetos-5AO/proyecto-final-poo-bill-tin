package ar.edu.huergo.rlgastos.billetin.controller.categoria;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.rlgastos.billetin.dto.categoria.ActualizarCategoriaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.categoria.CrearCategoriaDTO;
import ar.edu.huergo.rlgastos.billetin.dto.categoria.MostrarCategoriaDTO;
import ar.edu.huergo.rlgastos.billetin.entity.categoria.Categoria;
import ar.edu.huergo.rlgastos.billetin.mapper.categoria.CategoriaMapper;
import ar.edu.huergo.rlgastos.billetin.service.categoria.CategoriaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaMapper categoriaMapper;

    @GetMapping
    public ResponseEntity<List<MostrarCategoriaDTO>> getCategorias() {
        List<Categoria> categorias = categoriaService.getCategorias();
        return ResponseEntity.ok(categoriaMapper.toMostrarDtoList(categorias));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MostrarCategoriaDTO> getCategoria(@PathVariable Long id) {
        Optional<Categoria> categoriaOpt = this.categoriaService.getCategoria(id);
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Categoria categoria = categoriaOpt.get();
        MostrarCategoriaDTO dto = this.categoriaMapper.toMostrarDTO(categoria);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<String> crearCategoria(@Valid@RequestBody CrearCategoriaDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        categoriaService.crearCategoria(categoria);
        return ResponseEntity.created(null).body("Categoría creada correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCategoria(@PathVariable Long id, @RequestBody ActualizarCategoriaDTO dto) throws NotFoundException {
        categoriaService.actualizarCategoria(id, dto);
        return ResponseEntity.ok("Categoría actualizada correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.ok("Categoría eliminada correctamente");
    }
}

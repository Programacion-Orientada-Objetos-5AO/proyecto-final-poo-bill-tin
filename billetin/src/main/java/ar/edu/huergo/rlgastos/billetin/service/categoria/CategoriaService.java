package ar.edu.huergo.rlgastos.billetin.service.categoria;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.rlgastos.billetin.dto.categoria.ActualizarCategoriaDTO;

import ar.edu.huergo.rlgastos.billetin.entity.categoria.Categoria;
import ar.edu.huergo.rlgastos.billetin.repository.categoria.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getCategorias() {
       return ((List<Categoria>) this.categoriaRepository.findAll());
    }

    public Optional<Categoria> getCategoria(Long id) {
        return this.categoriaRepository.findById(id);
    }

    public void crearCategoria(Categoria categoria) {
        this.categoriaRepository.save(categoria);
    }

    public void actualizarCategoria(Long id, ActualizarCategoriaDTO dto) throws NotFoundException {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(NotFoundException::new);
        categoria.setNombre(dto.nombre());
        categoria.setTipo(dto.tipo());
        this.categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Long id) {
        this.categoriaRepository.deleteById(id);
    }
}

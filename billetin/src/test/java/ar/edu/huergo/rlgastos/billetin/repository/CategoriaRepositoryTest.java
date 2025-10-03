package ar.edu.huergo.rlgastos.billetin.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ar.edu.huergo.rlgastos.billetin.entity.categoria.Categoria;
import ar.edu.huergo.rlgastos.billetin.entity.categoria.TipoCategoria;
import ar.edu.huergo.rlgastos.billetin.repository.categoria.CategoriaRepository;

@DataJpaTest
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria categoriaAlimentos;

    @BeforeEach void setUp() {
    categoriaAlimentos = new Categoria();
    categoriaAlimentos.setNombre("Alimentos");
    categoriaAlimentos.setTipo(TipoCategoria.egreso_variable);
}

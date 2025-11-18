package ar.edu.huergo.rlgastos.billetin.controller.meta;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import ar.edu.huergo.rlgastos.billetin.entity.meta.Meta;
import ar.edu.huergo.rlgastos.billetin.service.meta.MetaService;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/metas")
public class MetaController {
    
    @Autowired
    private MetaService metaService;

    @GetMapping
    public List<Meta> getMetas() {
        return metaService.getMetas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meta>getMeta(@PathVariable Long id){
        Optional<Meta> meta = metaService.getMeta(id);
        return meta.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public void crearMeta(@RequestBody Meta meta) {
        metaService.crearMeta(meta);
    }

    @PutMapping("/{id}")
    public void actualizarMeta(@PathVariable Long id, @RequestBody Meta nuevaMeta) throws Exception {
        metaService.actualizarMeta(id, nuevaMeta);
    }

    @DeleteMapping("/{id}")
    public void eliminarMeta(@PathVariable Long id) {
        metaService.eliminarMeta(id);
    }
}

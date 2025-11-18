package ar.edu.huergo.rlgastos.billetin.repository.meta;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.huergo.rlgastos.billetin.entity.meta.Meta;

public interface MetaRepository extends JpaRepository <Meta, Long> {
}

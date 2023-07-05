package br.com.jornada.milhas.repository;

import br.com.jornada.milhas.model.Depoimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepoimentoRepository extends JpaRepository<Depoimento, Long> {
    Page<Depoimento> findAll(Pageable paginacao);
}

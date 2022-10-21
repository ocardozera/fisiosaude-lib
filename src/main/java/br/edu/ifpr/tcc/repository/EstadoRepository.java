package br.edu.ifpr.tcc.repository;

import br.edu.ifpr.tcc.modelo.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Optional<Estado> findByNome(String nome);

    Optional<Estado> findBySigla(String sigla);
}

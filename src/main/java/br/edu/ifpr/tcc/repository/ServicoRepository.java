package br.edu.ifpr.tcc.repository;

import br.edu.ifpr.tcc.modelo.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    Optional<Servico> findByNome(String nome);

}

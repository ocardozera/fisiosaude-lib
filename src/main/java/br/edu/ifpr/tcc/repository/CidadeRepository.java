package br.edu.ifpr.tcc.repository;

import br.edu.ifpr.tcc.modelo.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    Optional<Cidade> findByNome(String nome);

    Optional<Cidade> findCidadeByEstado_Id(Long id);

    

    @Query("SELECT c FROM Cidade c WHERE c.nome = :nomeCidade and c.estado.id = :estadoId")
    Optional<Cidade> obterCidadeByNomeEEstadoId(@Param("nomeCidade") String nomeCidade, @Param("estadoId") Long estadoId);
}

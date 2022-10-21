package br.edu.ifpr.tcc.repository;

import br.edu.ifpr.tcc.dto.ConsultaAgendamentoDTO;
import br.edu.ifpr.tcc.modelo.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
//
//    @Query("SELECT a.id, f.id, p.id, s.id, s.nome, a.status FROM Agendamento a JOIN a.fisioterapeuta f JOIN a.paciente p JOIN a.servico s")
//    Optional<Agendamento> testeJpql();

    @Query("SELECT a.id, f.nome, f.email FROM Agendamento a JOIN a.fisioterapeuta f")
    public List<ConsultaAgendamentoDTO> testeJpql();




}

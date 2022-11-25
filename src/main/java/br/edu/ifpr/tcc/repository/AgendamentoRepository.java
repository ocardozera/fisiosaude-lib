package br.edu.ifpr.tcc.repository;

import br.edu.ifpr.tcc.dto.ConsultaAgendamentoDTO;
import br.edu.ifpr.tcc.modelo.Agendamento;
import br.edu.ifpr.tcc.modelo.Servico;
import br.edu.ifpr.tcc.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
//
//    @Query("SELECT a.id, f.id, p.id, s.id, s.nome, a.status FROM Agendamento a JOIN a.fisioterapeuta f JOIN a.paciente p JOIN a.servico s")
//    Optional<Agendamento> testeJpql();

    @Query("SELECT a.id, f.nome, f.email FROM Agendamento a JOIN a.fisioterapeuta f")
    public List<ConsultaAgendamentoDTO> testeJpql();


    List<Agendamento> findAllByFisioterapeutaIsTrue();

    @Query("SELECT AG FROM Agendamento AG WHERE AG.dataAgendamento = :dataAgendamento AND AG.fisioterapeuta = :fisioterapeuta AND ((AG.inicioAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento) OR (AG.fimAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento))")
    List<Agendamento> consultarSeJaExisteAgendamento(@Param("dataAgendamento") Date dataAgendamento, @Param("inicioAgendamento") Date inicioAgendamento, @Param("fimAgendamento") Date fimAgendamento, @Param("fisioterapeuta") Usuario fisioterapeuta);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.dataAgendamento = :dataAgendamento AND AG.fisioterapeuta = :fisioterapeuta AND ((AG.inicioAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento) OR (AG.fimAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento)) AND AG.id <> :idAgendamento")
    List<Agendamento> consultarSeJaExisteAgendamentoEdicao(@Param("dataAgendamento") Date dataAgendamento, @Param("inicioAgendamento") Date inicioAgendamento, @Param("fimAgendamento") Date fimAgendamento, @Param("fisioterapeuta") Usuario fisioterapeuta, @Param("idAgendamento") Long idAgendamento);


    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente")
    List<Agendamento> consultarRelacaoPacienteAgendamento(@Param("paciente") Usuario paciente);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.fisioterapeuta = :fisioterapeuta")
    List<Agendamento> consultarRelacaoFisioterapeutaAgendamento(@Param("fisioterapeuta") Usuario fisioterapeuta);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.servico = :servico")
    List<Agendamento> consultarRelacaoServicoAgendamento(@Param("servico") Servico servico);


}

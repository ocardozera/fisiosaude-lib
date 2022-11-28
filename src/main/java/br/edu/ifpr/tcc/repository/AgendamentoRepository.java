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

    @Query("SELECT AG FROM Agendamento AG WHERE AG.dataAgendamento = :dataAgendamento AND AG.fisioterapeuta = :fisioterapeuta AND ((AG.inicioAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento) OR (AG.fimAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento)) AND AG.status <> 4")
    List<Agendamento> consultarSeJaExisteAgendamento(@Param("dataAgendamento") Date dataAgendamento, @Param("inicioAgendamento") Date inicioAgendamento, @Param("fimAgendamento") Date fimAgendamento, @Param("fisioterapeuta") Usuario fisioterapeuta);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.dataAgendamento = :dataAgendamento AND AG.fisioterapeuta = :fisioterapeuta AND ((AG.inicioAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento) OR (AG.fimAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento)) AND AG.id <> :idAgendamento AND AG.status <> 4")
    List<Agendamento> consultarSeJaExisteAgendamentoEdicao(@Param("dataAgendamento") Date dataAgendamento, @Param("inicioAgendamento") Date inicioAgendamento, @Param("fimAgendamento") Date fimAgendamento, @Param("fisioterapeuta") Usuario fisioterapeuta, @Param("idAgendamento") Long idAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.dataAgendamento = :dataAgendamento AND AG.paciente = :paciente AND ((AG.inicioAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento) OR (AG.fimAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento)) AND AG.status <> 4")
    List<Agendamento> consultarSeJaExisteAgendamentoPaciente(@Param("dataAgendamento") Date dataAgendamento, @Param("inicioAgendamento") Date inicioAgendamento, @Param("fimAgendamento") Date fimAgendamento, @Param("paciente") Usuario paciente);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.dataAgendamento = :dataAgendamento AND AG.paciente = :paciente AND ((AG.inicioAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento) OR (AG.fimAgendamento BETWEEN :inicioAgendamento AND :fimAgendamento)) AND AG.id <> :idAgendamento AND AG.status <> 4")
    List<Agendamento> consultarSeJaExisteAgendamentoPacienteEdicao(@Param("dataAgendamento") Date dataAgendamento, @Param("inicioAgendamento") Date inicioAgendamento, @Param("fimAgendamento") Date fimAgendamento, @Param("paciente") Usuario paciente, @Param("idAgendamento") Long idAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente")
    List<Agendamento> consultarRelacaoPacienteAgendamento(@Param("paciente") Usuario paciente);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.fisioterapeuta = :fisioterapeuta")
    List<Agendamento> consultarRelacaoFisioterapeutaAgendamento(@Param("fisioterapeuta") Usuario fisioterapeuta);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.servico = :servico")
    List<Agendamento> consultarRelacaoServicoAgendamento(@Param("servico") Servico servico);

    ////
    //Filtros PACIENTE
    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente")
    List<Agendamento> filtroPorPaciente(@Param("paciente") Usuario paciente);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.fisioterapeuta = :fisioterapeuta")
    List<Agendamento> filtroPorPacienteFisioterapeuta(@Param("paciente") Usuario paciente, @Param("fisioterapeuta") Usuario fisioterapeuta);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.fisioterapeuta = :fisioterapeuta AND AG.servico = :servico")
    List<Agendamento> filtroPorPacienteFisioterapeutaServico(@Param("paciente") Usuario paciente, @Param("fisioterapeuta") Usuario fisioterapeuta, @Param("servico") Servico servico);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.fisioterapeuta = :fisioterapeuta AND AG.servico = :servico AND AG.status = :status")
    List<Agendamento> filtroPorPacienteFisioterapeutaServicoStatus(@Param("paciente") Usuario paciente, @Param("fisioterapeuta") Usuario fisioterapeuta, @Param("servico") Servico servico, @Param("status") Integer status);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.fisioterapeuta = :fisioterapeuta AND AG.servico = :servico AND AG.status = :status AND AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorPacienteFisioterapeutaServicoStatusDataAgendamento(@Param("paciente") Usuario paciente, @Param("fisioterapeuta") Usuario fisioterapeuta, @Param("servico") Servico servico, @Param("status") Integer status, @Param("dataAgendamento") Date dataAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.servico = :servico")
    List<Agendamento> filtroPorPacienteServico(@Param("paciente") Usuario paciente, @Param("servico") Servico servico);
    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.servico = :servico AND AG.status = :status")
    List<Agendamento> filtroPorPacienteServicoStatus(@Param("paciente") Usuario paciente, @Param("servico") Servico servico, @Param("status") Integer status);
    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.servico = :servico AND AG.status = :status AND AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorPacienteServicoStatusDataAgendamento(@Param("paciente") Usuario paciente, @Param("servico") Servico servico, @Param("status") Integer status, @Param("dataAgendamento") Date dataAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.status = :status")
    List<Agendamento> filtroPorPacienteStatus(@Param("paciente") Usuario paciente, @Param("status") Integer status);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.status = :status AND AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorPacienteStatusDataAgendamento(@Param("paciente") Usuario paciente, @Param("status") Integer status, @Param("dataAgendamento") Date dataAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.paciente = :paciente AND AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorPacienteDataAgendamento(@Param("paciente") Usuario paciente, @Param("dataAgendamento") Date dataAgendamento);


    ///#############///////##############///

    // FILTROS FISIOTERAPEUTA

    @Query("SELECT AG FROM Agendamento AG WHERE AG.fisioterapeuta = :fisioterapeuta")
    List<Agendamento> filtroPorFisioterapeuta(@Param("fisioterapeuta") Usuario fisioterapeuta);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.fisioterapeuta = :fisioterapeuta AND AG.servico = :servico")
    List<Agendamento> filtroPorFisioterapeutaServico(@Param("fisioterapeuta") Usuario fisioterapeuta, @Param("servico") Servico servico);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.fisioterapeuta = :fisioterapeuta AND AG.servico = :servico AND AG.status = :status")
    List<Agendamento> filtroPorFisioterapeutaServicoStatus(@Param("fisioterapeuta") Usuario fisioterapeuta, @Param("servico") Servico servico, @Param("status") Integer status);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.fisioterapeuta = :fisioterapeuta AND AG.servico = :servico AND AG.status = :status AND AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorFisioterapeutaServicoStatusDataAgendamento(@Param("fisioterapeuta") Usuario fisioterapeuta, @Param("servico") Servico servico, @Param("status") Integer status, @Param("dataAgendamento") Date dataAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.fisioterapeuta = :fisioterapeuta AND AG.status = :status")
    List<Agendamento> filtroPorFisioterapeutaStatus(@Param("fisioterapeuta") Usuario fisioterapeuta, @Param("status") Integer status);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.fisioterapeuta = :fisioterapeuta AND AG.status = :status AND AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorFisioterapeutaStatusDataAgendamento(@Param("fisioterapeuta") Usuario fisioterapeuta, @Param("status") Integer status, @Param("dataAgendamento") Date dataAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.fisioterapeuta = :fisioterapeuta AND AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorFisioterapeutaDataAgendamento(@Param("fisioterapeuta") Usuario fisioterapeuta, @Param("dataAgendamento") Date dataAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.servico = :servico")
    List<Agendamento> filtroPorServico(@Param("servico") Servico servico);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.servico = :servico AND AG.status = :status")
    List<Agendamento> filtroPorServicoStatus(@Param("servico") Servico servico, @Param("status") Integer status);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.servico = :servico AND AG.status = :status AND AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorServicoStatusDataAgendamento(@Param("servico") Servico servico, @Param("status") Integer status, @Param("dataAgendamento") Date dataAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.status = :status")
    List<Agendamento> filtroPorStatus(@Param("status") Integer status);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.status = :status AND AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorStatusDataAgendamento(@Param("status") Integer status, @Param("dataAgendamento") Date dataAgendamento);

    @Query("SELECT AG FROM Agendamento AG WHERE AG.dataAgendamento = :dataAgendamento")
    List<Agendamento> filtroPorDataAgendamento(@Param("dataAgendamento") Date dataAgendamento);

    //########?///################
}

package br.edu.ifpr.tcc.form;

import br.edu.ifpr.tcc.dto.ServicoDTO;
import br.edu.ifpr.tcc.dto.UsuarioDTO;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class AgendamentoForm {

    @NotNull
    private UsuarioDTO fisioterapeuta;

    @NotNull
    private UsuarioDTO paciente;

    @NotNull
    private ServicoDTO servico;

    @NotNull
    private Integer status;

    private Date dataInclusao;

    @NotNull
    private String stDataAgendamento;

    @NotNull
    private String stInicioAgendamento;

    @NotNull
    private String stFimAgendamento;


    public UsuarioDTO getFisioterapeuta() {
        return fisioterapeuta;
    }

    public void setFisioterapeuta(UsuarioDTO fisioterapeuta) {
        this.fisioterapeuta = fisioterapeuta;
    }

    public UsuarioDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(UsuarioDTO paciente) {
        this.paciente = paciente;
    }

    public ServicoDTO getServico() {
        return servico;
    }

    public void setServico(ServicoDTO servico) {
        this.servico = servico;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public String getStDataAgendamento() {
        return stDataAgendamento;
    }

    public void setStDataAgendamento(String stDataAgendamento) {
        this.stDataAgendamento = stDataAgendamento;
    }

    public String getStInicioAgendamento() {
        return stInicioAgendamento;
    }

    public void setStInicioAgendamento(String stInicioAgendamento) {
        this.stInicioAgendamento = stInicioAgendamento;
    }

    public String getStFimAgendamento() {
        return stFimAgendamento;
    }

    public void setStFimAgendamento(String stFimAgendamento) {
        this.stFimAgendamento = stFimAgendamento;
    }
}

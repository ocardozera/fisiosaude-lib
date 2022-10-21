package br.edu.ifpr.tcc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

public class AgendamentoDTO {

    private Long id;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UsuarioDTO fisioterapeuta;
    private UsuarioDTO paciente;
    private ServicoDTO servico;
    private Integer status;
    private Date dataInclusao;
    private Date dataAgendamento;
    private Date inicioAgendamento;
    private Date fimAgendamento;

    public AgendamentoDTO() {
    }

    public AgendamentoDTO(Long id,
                          UsuarioDTO fisioterapeuta,
                          UsuarioDTO paciente,
                          ServicoDTO servico,
                          Integer status,
                          Date dataInclusao,
                          Date dataAgendamento,
                          Date inicioAgendamento,
                          Date fimAgendamento) {
        this.id = id;
        this.fisioterapeuta = fisioterapeuta;
        this.paciente = paciente;
        this.servico = servico;
        this.status = status;
        this.dataInclusao = dataInclusao;
        this.dataAgendamento = dataAgendamento;
        this.inicioAgendamento = inicioAgendamento;
        this.fimAgendamento = fimAgendamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(Date dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public Date getInicioAgendamento() {
        return inicioAgendamento;
    }

    public void setInicioAgendamento(Date inicioAgendamento) {
        this.inicioAgendamento = inicioAgendamento;
    }

    public Date getFimAgendamento() {
        return fimAgendamento;
    }

    public void setFimAgendamento(Date fimAgendamento) {
        this.fimAgendamento = fimAgendamento;
    }
}

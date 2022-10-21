package br.edu.ifpr.tcc.modelo;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Agendamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Usuario fisioterapeuta;

	@ManyToOne
	private Usuario paciente;

	@ManyToOne
	private Servico servico;

	private Integer status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInclusao;

	@Temporal(TemporalType.DATE)
	private Date dataAgendamento;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inicioAgendamento;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fimAgendamento;


	public enum Status {
		Agendado(1),
		Atendido(2),
		Faltou(3),
		Cancelado(4);

		private int id;

		Status(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	public Agendamento() {
	}

	public Agendamento(Long id,
					   Integer status,
					   Usuario fisioterapeuta,
					   Usuario paciente,
					   Servico servico,
					   Date dataInclusao,
					   Date dataAgendamento,
					   Date inicioAgendamento,
					   Date fimAgendamento) {
		this.id = id;
		this.status = status;
		this.fisioterapeuta = fisioterapeuta;
		this.paciente = paciente;
		this.servico = servico;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Usuario getFisioterapeuta() {
		return fisioterapeuta;
	}

	public void setFisioterapeuta(Usuario fisioterapeuta) {
		this.fisioterapeuta = fisioterapeuta;
	}

	public Usuario getPaciente() {
		return paciente;
	}

	public void setPaciente(Usuario paciente) {
		this.paciente = paciente;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agendamento other = (Agendamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

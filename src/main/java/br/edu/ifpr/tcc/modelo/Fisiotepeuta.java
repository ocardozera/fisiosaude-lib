package br.edu.ifpr.tcc.modelo;

import javax.persistence.*;


public class Fisiotepeuta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer registroProfissional;

	@ManyToOne
	private Usuario usuario;

	public Fisiotepeuta() {
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRegistroProfissional() {
		return registroProfissional;
	}

	public void setRegistroProfissional(Integer registroProfissional) {
		this.registroProfissional = registroProfissional;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		Fisiotepeuta other = (Fisiotepeuta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

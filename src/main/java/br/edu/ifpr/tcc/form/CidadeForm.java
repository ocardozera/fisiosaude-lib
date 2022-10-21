package br.edu.ifpr.tcc.form;

import br.edu.ifpr.tcc.dto.EstadoDTO;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CidadeForm {

    @NotNull @NotEmpty
    @Length(min = 3)
    private String nome;

    @NotNull
    private EstadoDTO estado;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EstadoDTO getEstado() {
        return estado;
    }

    public void setEstado(EstadoDTO estado) {
        this.estado = estado;
    }
}

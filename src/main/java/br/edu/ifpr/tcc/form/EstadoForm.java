package br.edu.ifpr.tcc.form;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class EstadoForm {

    @NotNull @NotEmpty
    @Length(min = 3)
    private String nome;

    @NotNull @NotEmpty
    @Length(min = 2, max = 2)
    private String sigla;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}

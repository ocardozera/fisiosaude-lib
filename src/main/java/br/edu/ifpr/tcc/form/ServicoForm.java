package br.edu.ifpr.tcc.form;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ServicoForm {

    @NotNull @NotEmpty
    @Length(min = 3)
    private String nome;

    @NotNull
    private Double valorSessao;

    @NotNull
    private Integer maximoAlunosSessao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValorSessao() {
        return valorSessao;
    }

    public void setValorSessao(Double valorSessao) {
        this.valorSessao = valorSessao;
    }

    public Integer getMaximoAlunosSessao() {
        return maximoAlunosSessao;
    }

    public void setMaximoAlunosSessao(Integer maximoAlunosSessao) {
        this.maximoAlunosSessao = maximoAlunosSessao;
    }
}

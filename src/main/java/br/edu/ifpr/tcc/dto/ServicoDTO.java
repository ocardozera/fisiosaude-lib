package br.edu.ifpr.tcc.dto;

public class ServicoDTO {

    private Long id;
    private String nome;
    private Double valorSessao;
    private Integer maximoAlunosSessao;

    public ServicoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

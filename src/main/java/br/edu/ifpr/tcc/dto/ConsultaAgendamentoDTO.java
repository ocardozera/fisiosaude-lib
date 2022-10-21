package br.edu.ifpr.tcc.dto;

public class ConsultaAgendamentoDTO {

    private String nome;
    private String email;

    public ConsultaAgendamentoDTO() {
    }

    public ConsultaAgendamentoDTO(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package br.edu.ifpr.tcc.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cpf;
    private Date dataNascimento;

    private CidadeDTO cidade;

    private String logradouro;
    private Integer numero;
    private String complemento;
    private String bairro;

    private Boolean administrador;

    private Boolean fisioterapeuta;
    private Integer registroProfissional;

    private Boolean paciente;

    private List<PerfilDTO> perfis = new ArrayList<>();

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nome, String email, String senha, String telefone, String cpf, Date dataNascimento, CidadeDTO cidade, String logradouro, Integer numero, String complemento, String bairro, Boolean administrador, Boolean fisioterapeuta, Integer registroProfissional, Boolean paciente) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.cidade = cidade;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.administrador = administrador;
        this.fisioterapeuta = fisioterapeuta;
        this.registroProfissional = registroProfissional;
        this.paciente = paciente;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public CidadeDTO getCidade() {
        return cidade;
    }

    public void setCidade(CidadeDTO cidade) {
        this.cidade = cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public Boolean getFisioterapeuta() {
        return fisioterapeuta;
    }

    public void setFisioterapeuta(Boolean fisioterapeuta) {
        this.fisioterapeuta = fisioterapeuta;
    }

    public Integer getRegistroProfissional() {
        return registroProfissional;
    }

    public void setRegistroProfissional(Integer registroProfissional) {
        this.registroProfissional = registroProfissional;
    }

    public Boolean getPaciente() {
        return paciente;
    }

    public void setPaciente(Boolean paciente) {
        this.paciente = paciente;
    }

    public List<PerfilDTO> getPerfis() {
        return perfis;
    }

    public void setPerfis(List<PerfilDTO> perfis) {
        this.perfis = perfis;
    }
}

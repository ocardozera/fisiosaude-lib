package br.edu.ifpr.tcc.form;

import br.edu.ifpr.tcc.dto.CidadeDTO;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UsuarioForm {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cpf;
    private String stDataNascimento;
    private CidadeDTO cidade;
    private String logradouro;
    private Integer numero;
    private String complemento;
    private String bairro;
    private Integer registroProfissional;
    private String diagnostico;
    private String sexo;
    private Integer cep;

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

    public String getStDataNascimento() {
        return stDataNascimento;
    }

    public void setStDataNascimento(String stDataNascimento) {
        this.stDataNascimento = stDataNascimento;
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

    public Integer getRegistroProfissional() {
        return registroProfissional;
    }

    public void setRegistroProfissional(Integer registroProfissional) {
        this.registroProfissional = registroProfissional;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }
}

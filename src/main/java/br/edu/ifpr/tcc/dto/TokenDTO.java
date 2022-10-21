package br.edu.ifpr.tcc.dto;

public class TokenDTO {

    private String token;
    private String tipo;

    public TokenDTO(String tipo, String token) {
        this.tipo = tipo;
        this.token = token;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

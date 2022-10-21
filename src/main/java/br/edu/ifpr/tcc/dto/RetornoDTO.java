package br.edu.ifpr.tcc.dto;

public class RetornoDTO {

    private String mensagem;
    private Object objeto;
    private Boolean erro;

    public RetornoDTO() {
    }

    public RetornoDTO(String mensagem, Object objeto, Boolean erro) {
        this.mensagem = mensagem;
        this.objeto = objeto;
        this.erro = erro;
    }

    public static RetornoDTO sucesso(String msg) {
        return new RetornoDTO(msg, null, false);
    }

    public static RetornoDTO sucesso(String msg, Object obj) {
        return new RetornoDTO(msg, obj, false);
    }

    public static RetornoDTO erro(String msg) {
        return new RetornoDTO(msg, null, true);
    }

    public static RetornoDTO erro(String msg, Object obj) {
        return new RetornoDTO(msg, obj, true);
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    public Boolean getErro() {
        return erro;
    }

    public void setErro(Boolean erro) {
        this.erro = erro;
    }
}

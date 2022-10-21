package br.edu.ifpr.tcc.dto;

public class ServicoFisioterapeutaDTO {

    private Long id;
    private ServicoDTO servico;
    private UsuarioDTO fisioterapeuta;

    public ServicoFisioterapeutaDTO() {
    }

    public ServicoFisioterapeutaDTO(Long id, ServicoDTO servico, UsuarioDTO fisioterapeuta) {
        this.id = id;
        this.servico = servico;
        this.fisioterapeuta = fisioterapeuta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicoDTO getServico() {
        return servico;
    }

    public void setServico(ServicoDTO servico) {
        this.servico = servico;
    }

    public UsuarioDTO getFisioterapeuta() {
        return fisioterapeuta;
    }

    public void setFisioterapeuta(UsuarioDTO fisioterapeuta) {
        this.fisioterapeuta = fisioterapeuta;
    }
}

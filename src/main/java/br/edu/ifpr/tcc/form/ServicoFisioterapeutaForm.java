package br.edu.ifpr.tcc.form;

import br.edu.ifpr.tcc.dto.ServicoDTO;
import br.edu.ifpr.tcc.dto.UsuarioDTO;

import javax.validation.constraints.NotNull;

public class ServicoFisioterapeutaForm {

    @NotNull
    private ServicoDTO servico;

    @NotNull
    private UsuarioDTO fisioterapeuta;

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

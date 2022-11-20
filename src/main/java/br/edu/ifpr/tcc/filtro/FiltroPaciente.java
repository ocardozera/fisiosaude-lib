package br.edu.ifpr.tcc.filtro;

import br.edu.ifpr.tcc.form.UsuarioForm;

public class FiltroPaciente {
    private String usuarioId;
    private UsuarioForm usuarioForm;

    public FiltroPaciente(String usuarioId, UsuarioForm usuarioForm) {
        this.usuarioId = usuarioId;
        this.usuarioForm = usuarioForm;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UsuarioForm getUsuarioForm() {
        return usuarioForm;
    }

    public void setUsuarioForm(UsuarioForm usuarioForm) {
        this.usuarioForm = usuarioForm;
    }
}

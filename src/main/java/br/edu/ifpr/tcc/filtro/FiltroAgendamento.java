package br.edu.ifpr.tcc.filtro;

import br.edu.ifpr.tcc.form.UsuarioForm;

public class FiltroAgendamento {
    private Object paciente;

    public FiltroAgendamento(Object paciente) {
        this.paciente = paciente;
    }

    public Object getPaciente() {
        return paciente;
    }

    public void setPaciente(Object paciente) {
        this.paciente = paciente;
    }
}

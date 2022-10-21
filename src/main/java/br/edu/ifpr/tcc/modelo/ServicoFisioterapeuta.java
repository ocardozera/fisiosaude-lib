package br.edu.ifpr.tcc.modelo;

import javax.persistence.*;

@Entity
public class ServicoFisioterapeuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Servico servico;

    @ManyToOne
    private Usuario fisioterapeuta;

    public ServicoFisioterapeuta() {
    }

    public ServicoFisioterapeuta(Long id, Servico servico, Usuario fisioterapeuta) {
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

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Usuario getFisioterapeuta() {
        return fisioterapeuta;
    }

    public void setFisioterapeuta(Usuario fisioterapeuta) {
        this.fisioterapeuta = fisioterapeuta;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServicoFisioterapeuta other = (ServicoFisioterapeuta) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}

package br.edu.ifpr.tcc.config;

import br.edu.ifpr.tcc.modelo.Cidade;
import br.edu.ifpr.tcc.modelo.Estado;
import br.edu.ifpr.tcc.modelo.Usuario;
import br.edu.ifpr.tcc.repository.CidadeRepository;
import br.edu.ifpr.tcc.repository.EstadoRepository;
import br.edu.ifpr.tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class SeederBanco implements CommandLineRunner {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {

        populaEstado();

        populaCidade();

        populaUsuario();



    }

    public void populaEstado() {

        List<Estado> estados = new ArrayList<>();

        Optional<Estado> estado1 = estadoRepository.findByNome("Paraná");
        Optional<Estado> estado2 = estadoRepository.findByNome("Mato Grosso");
        Optional<Estado> estado3 = estadoRepository.findByNome("Santa Catarina");

        if (!estado1.isPresent()) {
            Estado pr = new Estado("Paraná", "PR");
            estados.add(pr);
        }

        if (!estado2.isPresent()) {
            Estado mt = new Estado("Mato Grosso", "MT");
            estados.add(mt);
        }

        if (!estado3.isPresent()) {
            Estado sc = new Estado("Santa Catarina", "SC");
            estados.add(sc);
        }

        if (estados != null && estados.size() > 0) {
            estadoRepository.saveAll(estados);
        }

    }

    public void populaCidade() {


        Optional<Cidade> cidade1 = cidadeRepository.obterCidadeByNomeEEstadoId("Paranavaí", 285L);

        if (!cidade1.isPresent()) {
            Cidade cidade = new Cidade();

            cidade.setNome("Paranavaí");
            cidade.setEstado(new Estado(2L));

            cidadeRepository.save(cidade);
        }


    }

    public void populaUsuario() {

        //consulta p ver se existe o usuario

        Optional<Usuario> usuarioAdmin = usuarioRepository.findByEmail("admin@gmail.com");

        if (!usuarioAdmin.isPresent()) {
            Usuario usuario = new Usuario();

            usuario.setNome("Admin");
            usuario.setEmail("admin@gmail.com");
            usuario.setSenha("$2a$10$L4SH6GlpEaoepv/BdzG/N..vWIUOHZEGUGXDyoOI9FfKj8MgCKAUu");
            usuario.setTelefone("44991361481");
            usuario.setCpf("06565435901");

            Date dataNascimento = new Date(2000, 07, 29);
            usuario.setDataNascimento(dataNascimento);

            usuario.setCidade(new Cidade(4L));
            usuario.setLogradouro("Av Roosevelt");
            usuario.setNumero(217);
            usuario.setComplemento("Casa c/ antena");
            usuario.setBairro("Jd São Jorge");
            usuario.setAdministrador(true);
            usuario.setFisioterapeuta(false);
            usuario.setRegistroProfissional(null);
            usuario.setPaciente(false);

            usuarioRepository.save(usuario);
        }



    }
}

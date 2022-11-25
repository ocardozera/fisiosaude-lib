package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.dto.CidadeDTO;
import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.dto.UsuarioDTO;
import br.edu.ifpr.tcc.filtro.FiltroPaciente;
import br.edu.ifpr.tcc.form.CidadeForm;
import br.edu.ifpr.tcc.form.UsuarioForm;
import br.edu.ifpr.tcc.mapper.CidadeMapper;
import br.edu.ifpr.tcc.mapper.UsuarioMapper;
import br.edu.ifpr.tcc.modelo.Agendamento;
import br.edu.ifpr.tcc.modelo.Cidade;
import br.edu.ifpr.tcc.modelo.Estado;
import br.edu.ifpr.tcc.modelo.Usuario;
import br.edu.ifpr.tcc.repository.AgendamentoRepository;
import br.edu.ifpr.tcc.repository.CidadeRepository;
import br.edu.ifpr.tcc.repository.EstadoRepository;
import br.edu.ifpr.tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/fisioterapeuta")
public class FisioterapeutaController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @GetMapping("/listarFisioterapeutas")
    public ResponseEntity<RetornoDTO> listarFisioterapeutas() {
        RetornoDTO retorno;

//        Optional<Usuario> listaPacientes = usuarioRepository.findByPacienteIsTrue();

        List<Usuario> listaFisioterapeutas = usuarioRepository.findAllByFisioterapeutaIsTrue();

        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", UsuarioMapper.convertToListVo(listaFisioterapeutas));

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @PostMapping("/cadastrarFisioterapeuta")
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid FiltroPaciente filtro)  {
        RetornoDTO retorno;


        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            if (filtro != null) {

                Optional<Usuario> fisioterapeutaCadastrado = usuarioRepository.findByEmail(filtro.getUsuarioForm().getEmail());

                if (!fisioterapeutaCadastrado.isPresent()) {

                    Optional<Usuario> fisioterapeutaCadastradoCPF = usuarioRepository.findByCpf(filtro.getUsuarioForm().getCpf());

                    if (!fisioterapeutaCadastradoCPF.isPresent()) {
                        Usuario fisioterapeutaSalvar = UsuarioMapper.convertFormToEntity(filtro.getUsuarioForm());

                        fisioterapeutaSalvar.setPaciente(false);
                        fisioterapeutaSalvar.setAdministrador(false);
                        fisioterapeutaSalvar.setFisioterapeuta(true);

                        if (fisioterapeutaSalvar.getSenha() != null && !fisioterapeutaSalvar.getSenha().isEmpty()) {
                            PasswordEncoder encoder = new BCryptPasswordEncoder();
                            String senhaCriptografada = encoder.encode(fisioterapeutaSalvar.getSenha());
                            fisioterapeutaSalvar.setSenha(senhaCriptografada);
                        }

                        if (filtro.getUsuarioForm().getStDataNascimento() != null) {
                            Date dataNascimento = formatter.parse(filtro.getUsuarioForm().getStDataNascimento());

                            if (dataNascimento != null) {
                                fisioterapeutaSalvar.setDataNascimento(dataNascimento);
                            }
                        }

                        usuarioRepository.save(fisioterapeutaSalvar);
                        retorno = RetornoDTO.sucesso("Dados cadastrados com sucesso!", UsuarioMapper.convertToVo(fisioterapeutaSalvar));

                        return new ResponseEntity<>(retorno, HttpStatus.CREATED);
                    } else {
                        UsuarioDTO pacienteCadastradoDTO = UsuarioMapper.convertToVo(fisioterapeutaCadastradoCPF.get());

                        retorno = RetornoDTO.erro("Paciente já cadastrado!", pacienteCadastradoDTO);

                        return new ResponseEntity<>(retorno, HttpStatus.BAD_REQUEST);
                    }



                } else {
                    UsuarioDTO pacienteCadastradoDTO = UsuarioMapper.convertToVo(fisioterapeutaCadastrado.get());

                    retorno = RetornoDTO.erro("Paciente já cadastrado!", pacienteCadastradoDTO);

                    return new ResponseEntity<>(retorno, HttpStatus.BAD_REQUEST);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();

            retorno = RetornoDTO.erro("Não foi possível completar sua solicitação!");

            return new ResponseEntity<>(retorno, HttpStatus.BAD_REQUEST);
        }

        retorno = RetornoDTO.erro("Sem conteúdo");

        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/detalhar/{id}")
    public ResponseEntity<RetornoDTO> detalhar(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Usuario> usuario = usuarioRepository.findById(id);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            if (usuario.isPresent()) {
                UsuarioDTO usuarioDTO = UsuarioMapper.convertToVo(usuario.get());

                String stDataNascimento = formatter.format(usuarioDTO.getDataNascimento());

                usuarioDTO.setStDataNascimento(stDataNascimento);

                usuarioDTO.setSenha(null);

                retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", usuarioDTO);

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Usuário não encontrado!");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity<RetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioForm usuarioForm) throws ParseException {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Usuario> usuario = usuarioRepository.findById(id);

            if (usuario.isPresent()) {
                Long cidadeId = usuarioForm.getCidade().getId();

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                Usuario usuarioAtualizar = usuarioRepository.getReferenceById(id);
                usuarioAtualizar.setNome(usuarioForm.getNome());
                usuarioAtualizar.setCpf(usuarioForm.getCpf());


                Date dataNascimento = formatter.parse(usuarioForm.getStDataNascimento());
                if (dataNascimento != null) {
                    usuarioAtualizar.setDataNascimento(dataNascimento);
                }

                usuarioAtualizar.setRegistroProfissional(usuarioForm.getRegistroProfissional());
                usuarioAtualizar.setTelefone(usuarioForm.getTelefone());
                usuarioAtualizar.setSexo(usuarioForm.getSexo());

                if (usuarioForm.getSenha() != null && !usuarioForm.getSenha().isEmpty()) {
                    PasswordEncoder encoder = new BCryptPasswordEncoder();
                    String senhaCriptografada = encoder.encode(usuarioForm.getSenha());
                    usuarioAtualizar.setSenha(senhaCriptografada);
                }

                usuarioAtualizar.setCep(usuarioForm.getCep());
                usuarioAtualizar.setCidade(new Cidade(cidadeId));
                usuarioAtualizar.setLogradouro(usuarioForm.getLogradouro());
                usuarioAtualizar.setNumero(usuarioForm.getNumero());
                usuarioAtualizar.setComplemento(usuarioForm.getComplemento());
                usuarioAtualizar.setBairro(usuarioForm.getBairro());

                retorno = RetornoDTO.sucesso("Dados alterados com sucesso!", UsuarioMapper.convertToVo(usuarioAtualizar));

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Fisioterapeuta não encontrado");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deletar/{id}")
    @Transactional
    public ResponseEntity<RetornoDTO> remover(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Usuario> usuario = usuarioRepository.findById(id);

            if (usuario.isPresent()) {

                Usuario fisioterapeuta = usuario.get();

                List<Agendamento> agendamentosFisioterapeuta = agendamentoRepository.consultarRelacaoFisioterapeutaAgendamento(fisioterapeuta);

                if (agendamentosFisioterapeuta != null && agendamentosFisioterapeuta.size() > 0) {
                    retorno = RetornoDTO.erro("Erro ao deletar! Fisioterapeuta com vínculo em agendamentos!", null);

                    return new ResponseEntity<>(retorno, HttpStatus.OK);
                }

                cidadeRepository.deleteById(id);
                retorno = RetornoDTO.sucesso("Dados alterados com sucesso!");
                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Estado não encontrado");
                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }
}



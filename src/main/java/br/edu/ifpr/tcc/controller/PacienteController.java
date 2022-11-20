package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.dto.CidadeDTO;
import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.dto.UsuarioDTO;
import br.edu.ifpr.tcc.filtro.FiltroPaciente;
import br.edu.ifpr.tcc.form.CidadeForm;
import br.edu.ifpr.tcc.form.UsuarioForm;
import br.edu.ifpr.tcc.mapper.CidadeMapper;
import br.edu.ifpr.tcc.mapper.UsuarioMapper;
import br.edu.ifpr.tcc.modelo.Cidade;
import br.edu.ifpr.tcc.modelo.Estado;
import br.edu.ifpr.tcc.modelo.Usuario;
import br.edu.ifpr.tcc.repository.CidadeRepository;
import br.edu.ifpr.tcc.repository.EstadoRepository;
import br.edu.ifpr.tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@RequestMapping(value = "/paciente")
public class PacienteController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/listarPacientes")
    public ResponseEntity<RetornoDTO> listarPacientes() {
        RetornoDTO retorno;

//        Optional<Usuario> listaPacientes = usuarioRepository.findByPacienteIsTrue();

        List<Usuario> listaPacientes = usuarioRepository.findAllByPacienteIsTrue();

        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", UsuarioMapper.convertToListVo(listaPacientes));

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @PostMapping("/cadastrarPaciente")
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid FiltroPaciente filtro)  {
        RetornoDTO retorno;


        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            if (filtro != null) {

                Optional<Usuario> pacienteCadastrado = usuarioRepository.findByEmail(filtro.getUsuarioForm().getEmail());

                if (!pacienteCadastrado.isPresent()) {

                    Optional<Usuario> pacienteCadastradoCPF = usuarioRepository.findByCpf(filtro.getUsuarioForm().getCpf());

                    if (!pacienteCadastradoCPF.isPresent()) {
                        Usuario pacienteSalvar = UsuarioMapper.convertFormToEntity(filtro.getUsuarioForm());

                        pacienteSalvar.setPaciente(true);

                        if (pacienteSalvar.getSenha() != null && !pacienteSalvar.getSenha().isEmpty()) {
                            PasswordEncoder encoder = new BCryptPasswordEncoder();
                            String senhaCriptografada = encoder.encode(pacienteSalvar.getSenha());
                            pacienteSalvar.setSenha(senhaCriptografada);
                        }

                        if (filtro.getUsuarioForm().getStDataNascimento() != null) {
                            Date dataNascimento = formatter.parse(filtro.getUsuarioForm().getStDataNascimento());

                            if (dataNascimento != null) {
                                pacienteSalvar.setDataNascimento(dataNascimento);
                            }
                        }

                        usuarioRepository.save(pacienteSalvar);
                        retorno = RetornoDTO.sucesso("Dados cadastrados com sucesso!", UsuarioMapper.convertToVo(pacienteSalvar));

                        return new ResponseEntity<>(retorno, HttpStatus.CREATED);
                    } else {
                        UsuarioDTO pacienteCadastradoDTO = UsuarioMapper.convertToVo(pacienteCadastradoCPF.get());

                        retorno = RetornoDTO.erro("Paciente já cadastrado!", pacienteCadastradoDTO);

                        return new ResponseEntity<>(retorno, HttpStatus.BAD_REQUEST);
                    }



                } else {
                    UsuarioDTO pacienteCadastradoDTO = UsuarioMapper.convertToVo(pacienteCadastrado.get());

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
            Optional<Cidade> cidade = cidadeRepository.findById(id);

            if (cidade.isPresent()) {
                CidadeDTO cidadeDTO = CidadeMapper.convertToVo(cidade.get());

                retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", cidadeDTO);

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Cidade não encontrada!");

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

                usuarioAtualizar.setTelefone(usuarioForm.getTelefone());
                usuarioAtualizar.setDiagnostico(usuarioForm.getDiagnostico());
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
                retorno = RetornoDTO.erro("Cidade não encontrada");

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
                usuarioRepository.deleteById(id);
                retorno = RetornoDTO.sucesso("Dados deletados com sucesso!");
                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Paciente não encontrado");
                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }
}



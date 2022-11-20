package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.dto.CidadeDTO;
import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.dto.UsuarioDTO;
import br.edu.ifpr.tcc.filtro.FiltroPaciente;
import br.edu.ifpr.tcc.form.CidadeForm;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
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

    @GetMapping("/listarFisioterapeutas")
    public ResponseEntity<RetornoDTO> listarFisioterapeutas() {
        RetornoDTO retorno;

//        Optional<Usuario> listaPacientes = usuarioRepository.findByPacienteIsTrue();

        List<Usuario> listaFisioterapeutas = usuarioRepository.findAllByFisioterapeutaIsTrue();

        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", UsuarioMapper.convertToListVo(listaFisioterapeutas));

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
    @CacheEvict(value = "listarCidades", allEntries = true)
    public ResponseEntity<RetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CidadeForm cidadeForm) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Cidade> cidade = cidadeRepository.findById(id);

            Long estadoId = cidadeForm.getEstado().getId();

            if (cidade.isPresent()) {
                Cidade cidadeAtualizar = cidadeRepository.getReferenceById(id);
                cidadeAtualizar.setNome(cidadeForm.getNome());
                cidadeAtualizar.setEstado(new Estado(estadoId));

                retorno = RetornoDTO.sucesso("Dados alterados com sucesso!", CidadeMapper.convertToVo(cidadeAtualizar));

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
    @CacheEvict(value = "listarCidades", allEntries = true)
    public ResponseEntity<RetornoDTO> remover(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Cidade> cidade = cidadeRepository.findById(id);

            if (cidade.isPresent()) {
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



package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.dto.AgendamentoDTO;
import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.form.AgendamentoForm;
import br.edu.ifpr.tcc.mapper.AgendamentoMapper;
import br.edu.ifpr.tcc.modelo.Agendamento;
import br.edu.ifpr.tcc.modelo.Servico;
import br.edu.ifpr.tcc.modelo.Usuario;
import br.edu.ifpr.tcc.repository.AgendamentoRepository;
import br.edu.ifpr.tcc.repository.ServicoRepository;
import br.edu.ifpr.tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/agendamento")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicoRepository servicoRepository;


    @GetMapping("/listar")
    public ResponseEntity<RetornoDTO> lista() {
        RetornoDTO retorno;

        List<Agendamento> lista = agendamentoRepository.findAll();

        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", AgendamentoMapper.convertToListVo(lista));

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @PostMapping("/cadastrar")
    @Transactional
    @CacheEvict(value = "listarAgendamentos", allEntries = true)
    public ResponseEntity<?> cadastrar(@RequestBody @Valid AgendamentoForm agendamentoForm) {
        RetornoDTO retorno = null;

        if (agendamentoForm != null) {

            Agendamento agendamento = AgendamentoMapper.convertFormToEntity(agendamentoForm);

            if (agendamento.getFisioterapeuta() != null && agendamento.getPaciente() != null && agendamento.getServico() != null) {

                // CONSULTAR FISIOTERAPEUTA,
                Optional<Usuario> fisioterapeuta = usuarioRepository.findByIdAndFisioterapeutaIsTrue(agendamento.getFisioterapeuta().getId());

                if (!fisioterapeuta.isPresent()) {
                    retorno = RetornoDTO.erro("Fisioterapeuta não encontrado!");
                    return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
                }

                // CONSULTAR PACIENTE,
                Optional<Usuario> paciente = usuarioRepository.findByIdAndPacienteIsTrue(agendamento.getPaciente().getId());

                if (!paciente.isPresent()) {
                    retorno = RetornoDTO.erro("Paciente não encontrado!");
                    return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
                }

                // CONSULTAR SERVICO
                Optional<Servico> servico = servicoRepository.findById(agendamento.getServico().getId());

                if (!servico.isPresent()) {
                    retorno = RetornoDTO.erro("Serviço não encontrado!");
                    return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
                }

                if (agendamento.getStatus() == null || agendamento.getStatus() < 1 || agendamento.getStatus() > 4) {
                    agendamento.setStatus(Agendamento.Status.Agendado.getId());
                }

                agendamento.setDataInclusao(new Date());

                agendamentoRepository.save(agendamento);

                retorno = RetornoDTO.sucesso("Dados cadastrados com sucesso!", AgendamentoMapper.convertToVo(agendamento));

                return new ResponseEntity<>(retorno, HttpStatus.CREATED);

            } else {
                retorno = RetornoDTO.erro("Não foi possível realizar o cadastro!");

                return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
            }
        } else {
            retorno = RetornoDTO.erro("Não foi possível realizar o cadastro!");

            return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/detalhar/{id}")
    public ResponseEntity<RetornoDTO> detalhar(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Agendamento> agendamento = agendamentoRepository.findById(id);

            if (agendamento.isPresent()) {
                AgendamentoDTO agendamentoDTO = AgendamentoMapper.convertToVo(agendamento.get());

                retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", agendamentoDTO);

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Agendamento não encontrado!");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/editar/{id}")
    @Transactional
    @CacheEvict(value = "listarAgendamentos", allEntries = true)
    public ResponseEntity<RetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AgendamentoForm agendamentoForm) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Agendamento> agendamento = agendamentoRepository.findById(id);

//            Long estadoId = cidadeForm.getEstado().getId();
//
//            if (cidade.isPresent()) {
//                Cidade cidadeAtualizar = cidadeRepository.getReferenceById(id);
//                cidadeAtualizar.setNome(cidadeForm.getNome());
//                cidadeAtualizar.setEstado(new Estado(estadoId));
//
//                retorno = RetornoDTO.sucesso("Dados alterados com sucesso!", CidadeMapper.convertToVo(cidadeAtualizar));
//
//                return new ResponseEntity<>(retorno, HttpStatus.OK);
//            } else {
//                retorno = RetornoDTO.erro("Cidade não encontrada");
//
//                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
//            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deletar/{id}")
    @Transactional
    @CacheEvict(value = "listarAgendamentos", allEntries = true)
    public ResponseEntity<RetornoDTO> remover(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Agendamento> agendamento = agendamentoRepository.findById(id);

            if (agendamento.isPresent()) {
                agendamentoRepository.deleteById(id);

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



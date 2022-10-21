package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.dto.ServicoDTO;
import br.edu.ifpr.tcc.form.ServicoForm;
import br.edu.ifpr.tcc.mapper.ServicoMapper;
import br.edu.ifpr.tcc.modelo.Servico;
import br.edu.ifpr.tcc.repository.EstadoRepository;
import br.edu.ifpr.tcc.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/servico")
public class ServicoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @GetMapping("/listar")
    @Cacheable(value = "listarServicos")
    public ResponseEntity<RetornoDTO> lista() {
        RetornoDTO retorno;

        List<Servico> listaServicos = servicoRepository.findAll();

        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", ServicoMapper.convertToListVo(listaServicos));

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @PostMapping("/cadastrar")
    @Transactional
    @CacheEvict(value = "listarServicos", allEntries = true)
    public ResponseEntity<?> cadastrar(@RequestBody @Valid ServicoForm servicoForm) {
        RetornoDTO retorno;

        if (servicoForm != null && servicoForm.getNome() != null && !servicoForm.getNome().isEmpty()
            && servicoForm.getValorSessao() != null && servicoForm.getMaximoAlunosSessao() != null) {
            Optional<Servico> servico = servicoRepository.findByNome(servicoForm.getNome());

            if (!servico.isPresent()) {

                Servico servicoEntity = ServicoMapper.convertFormToEntity(servicoForm);
                servicoRepository.save(servicoEntity);

                ServicoDTO servicoDTO = ServicoMapper.convertToVo(servicoEntity);

                retorno = RetornoDTO.sucesso("Dados cadastrados com sucesso!", servicoDTO);

                return new ResponseEntity<>(retorno, HttpStatus.CREATED);
            } else {
                ServicoDTO servicoDTO = ServicoMapper.convertToVo(servico.get());

                retorno = RetornoDTO.sucesso("Serviço já cadastrado!", servicoDTO);

                return new ResponseEntity<>(retorno, HttpStatus.OK);

            }
        }
        retorno = RetornoDTO.erro("Sem conteúdo");

        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/detalhar/{id}")
    public ResponseEntity<RetornoDTO> detalhar(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Servico> servico = servicoRepository.findById(id);

            if (servico.isPresent()) {
                ServicoDTO servicoDTO = ServicoMapper.convertToVo(servico.get());

                retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", servicoDTO);

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Serviço não encontrado");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/editar/{id}")
    @Transactional
    @CacheEvict(value = "listarServicos", allEntries = true)
    public ResponseEntity<RetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ServicoForm servicoForm) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Servico> servico = servicoRepository.findById(id);

            if (servico.isPresent()) {
                Servico servicoAtualizar = servicoRepository.getReferenceById(id);
                servicoAtualizar.setNome(servicoForm.getNome());
                servicoAtualizar.setValorSessao(servicoForm.getValorSessao());
                servicoAtualizar.setMaximoAlunosSessao(servicoForm.getMaximoAlunosSessao());

                retorno = RetornoDTO.sucesso("Dados excluídos com sucesso!", ServicoMapper.convertToVo(servicoAtualizar));

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Serviço não encontrado");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deletar/{id}")
    @Transactional
    @CacheEvict(value = "listarServicos", allEntries = true)
    public ResponseEntity<RetornoDTO> remover(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Servico> servico = servicoRepository.findById(id);

            if (servico.isPresent()) {
                servicoRepository.deleteById(id);
                retorno = RetornoDTO.sucesso("Dados alterados com sucesso!");
                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Serviço não encontrado");
                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }
}



package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.dto.CidadeDTO;
import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.form.CidadeForm;
import br.edu.ifpr.tcc.mapper.CidadeMapper;
import br.edu.ifpr.tcc.modelo.Cidade;
import br.edu.ifpr.tcc.modelo.Estado;
import br.edu.ifpr.tcc.repository.CidadeRepository;
import br.edu.ifpr.tcc.repository.EstadoRepository;
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
@RequestMapping(value = "/cidade")
public class CidadeController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping("/listar")
    public ResponseEntity<RetornoDTO> lista() {
        RetornoDTO retorno;

        List<Cidade> listaCidades = cidadeRepository.findAll();

        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", CidadeMapper.convertToListVo(listaCidades));

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @PostMapping("/consultarCidadeByNomeUF")
    public ResponseEntity<RetornoDTO> consultarCidadeByNomeUF(@RequestBody @Valid CidadeForm cidadeForm) {
        RetornoDTO retorno;

        Optional<Cidade> cidade = cidadeRepository.consultarCidadeByNomeUF(cidadeForm.getNome(), cidadeForm.getEstado().getSigla());

        if (cidade.isPresent()) {
            CidadeDTO cidadeDTO = CidadeMapper.convertToVo(cidade.get());

            retorno = RetornoDTO.sucesso("Cidade encontrada com sucesso!", cidadeDTO);

            return new ResponseEntity<>(retorno, HttpStatus.OK);
        } else {
            retorno = RetornoDTO.erro("Cidade não encontrada!");

            return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CidadeForm cidadeForm) {
        RetornoDTO retorno;

        if (cidadeForm != null && cidadeForm.getNome() != null && !cidadeForm.getNome().isEmpty()
            && cidadeForm.getEstado() != null && cidadeForm.getEstado().getId() != null
            && cidadeForm.getEstado().getId() > 0) {

            Long estadoId = cidadeForm.getEstado().getId();

            Optional<Cidade> cidade = cidadeRepository.obterCidadeByNomeEEstadoId(cidadeForm.getNome(), estadoId);

            if (!cidade.isPresent()) {

                Cidade cidadeEntity = CidadeMapper.convertFormToEntity(cidadeForm);
                cidadeRepository.save(cidadeEntity);


                retorno = RetornoDTO.sucesso("Dados cadastrados com sucesso!", CidadeMapper.convertToVo(cidadeEntity));

                return new ResponseEntity<>(retorno, HttpStatus.CREATED);
            } else {
                CidadeDTO cidadeDTO = CidadeMapper.convertToVo(cidade.get());

                retorno = RetornoDTO.sucesso("Cidade já cadastrada!", cidadeDTO);

                return new ResponseEntity<>(retorno, HttpStatus.BAD_REQUEST);
            }
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



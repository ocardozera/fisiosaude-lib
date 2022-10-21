package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.dto.EstadoDTO;
import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.form.EstadoForm;
import br.edu.ifpr.tcc.mapper.EstadoMapper;
import br.edu.ifpr.tcc.modelo.Estado;
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
@RequestMapping(value = "/estado")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @GetMapping("/listar")
    @Cacheable(value = "listarEstados")
    public ResponseEntity<RetornoDTO> lista() {
        RetornoDTO retorno;

        List<Estado> listaEstados = estadoRepository.findAll();

        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", EstadoMapper.convertToListVo(listaEstados));

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @PostMapping("/cadastrar")
    @Transactional
    @CacheEvict(value = "listarEstados", allEntries = true)
    public ResponseEntity<?> cadastrar(@RequestBody @Valid EstadoForm estadoForm) {
        RetornoDTO retorno;

        if (estadoForm != null && estadoForm.getSigla() != null && !estadoForm.getSigla().isEmpty()) {
            Optional<Estado> estado = estadoRepository.findBySigla(estadoForm.getSigla());

            if (!estado.isPresent()) {

                Estado estadoEntity = EstadoMapper.convertFormToEntity(estadoForm);
                estadoRepository.save(estadoEntity);

                EstadoDTO estadoDTO = EstadoMapper.convertToVo(estadoEntity);

                retorno = RetornoDTO.sucesso("Dados cadastrados com sucesso!", estadoDTO);

                return new ResponseEntity<>(retorno, HttpStatus.CREATED);
            } else {
                EstadoDTO estadoDTO = EstadoMapper.convertToVo(estado.get());

                retorno = RetornoDTO.sucesso("Estado já cadastrado!", estadoDTO);

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
            Optional<Estado> estado = estadoRepository.findById(id);

            if (estado.isPresent()) {
                EstadoDTO estadoDTO = EstadoMapper.convertToVo(estado.get());

                retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", estadoDTO);

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Estado não encontrado");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/editar/{id}")
    @Transactional
    @CacheEvict(value = "listarEstados", allEntries = true)
    public ResponseEntity<RetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EstadoForm estadoForm) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Estado> estado = estadoRepository.findById(id);

            if (estado.isPresent()) {
                Estado estadoAtualizar = estadoRepository.getReferenceById(id);
                estadoAtualizar.setNome(estadoForm.getNome());
                estadoAtualizar.setSigla(estadoForm.getSigla());

                retorno = RetornoDTO.sucesso("Dados alterados com sucesso!", EstadoMapper.convertToVo(estadoAtualizar));

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Estado não encontrado");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/deletar/{id}")
    @Transactional
    @CacheEvict(value = "listarEstados", allEntries = true)
    public ResponseEntity<RetornoDTO> remover(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Estado> estado = estadoRepository.findById(id);

            if (estado.isPresent()) {
                estadoRepository.deleteById(id);
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



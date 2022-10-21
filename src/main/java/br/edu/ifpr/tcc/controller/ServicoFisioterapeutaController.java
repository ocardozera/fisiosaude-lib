package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.dto.ServicoFisioterapeutaDTO;
import br.edu.ifpr.tcc.form.ServicoFisioterapeutaForm;
import br.edu.ifpr.tcc.mapper.ServicoFisioterapeutaMapper;
import br.edu.ifpr.tcc.mapper.ServicoMapper;
import br.edu.ifpr.tcc.mapper.UsuarioMapper;
import br.edu.ifpr.tcc.modelo.Servico;
import br.edu.ifpr.tcc.modelo.ServicoFisioterapeuta;
import br.edu.ifpr.tcc.modelo.Usuario;
import br.edu.ifpr.tcc.repository.ServicoFisioterapeutaRepository;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/servicoFisioterapeuta")
public class ServicoFisioterapeutaController {

    @Autowired
    private ServicoFisioterapeutaRepository servicoFisioterapeutaRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/listar")
    @Cacheable(value = "listarServicoFisioterapeuta")
    public ResponseEntity<RetornoDTO> lista() {
        RetornoDTO retorno;

        List<ServicoFisioterapeuta> listaServicoFisioterapeuta = servicoFisioterapeutaRepository.findAll();

        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", ServicoFisioterapeutaMapper.convertToListVo(listaServicoFisioterapeuta));

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @PostMapping("/cadastrar")
    @Transactional
    @CacheEvict(value = "listarServicoFisioterapeuta", allEntries = true)
    public ResponseEntity<?> cadastrar(@RequestBody @Valid ServicoFisioterapeutaForm servicoFisioterapeutaForm) {
        RetornoDTO retorno;

        if (servicoFisioterapeutaForm != null && servicoFisioterapeutaForm.getServico() != null
            && servicoFisioterapeutaForm.getServico().getId() != null && servicoFisioterapeutaForm.getServico().getId() > 0
            && servicoFisioterapeutaForm.getFisioterapeuta() != null && servicoFisioterapeutaForm.getFisioterapeuta().getId() != null
            && servicoFisioterapeutaForm.getFisioterapeuta().getId() != null) {

            Optional<Servico> servico = servicoRepository.findById(servicoFisioterapeutaForm.getServico().getId());
            Optional<Usuario> fisioterapeuta = usuarioRepository.findById(servicoFisioterapeutaForm.getFisioterapeuta().getId());

            if (servico.isPresent() && fisioterapeuta.isPresent()) {
                // caminho feliz > tem servico e tem fisio, cadastrar no bd

                ServicoFisioterapeuta servicoFisioterapeutaEntity = ServicoFisioterapeutaMapper.convertFormToEntity(servicoFisioterapeutaForm);
                servicoFisioterapeutaRepository.save(servicoFisioterapeutaEntity);


                retorno = RetornoDTO.sucesso("Dados cadastrados com sucesso!", ServicoFisioterapeutaMapper.convertToVo(servicoFisioterapeutaEntity));

                return new ResponseEntity<>(retorno, HttpStatus.CREATED);

            } else {
                // caminho triste
                retorno = RetornoDTO.erro("Dados não localizados.");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");

        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/detalhar/{id}")
    public ResponseEntity<RetornoDTO> detalhar(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<ServicoFisioterapeuta> servicoFisioterapeuta = servicoFisioterapeutaRepository.findById(id);

            if (servicoFisioterapeuta.isPresent()) {
                ServicoFisioterapeutaDTO servicoFisioterapeutaDTO = ServicoFisioterapeutaMapper.convertToVo(servicoFisioterapeuta.get());

                retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", servicoFisioterapeutaDTO);

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Serviço do fisioterapeuta não encontrado");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/editar/{id}")
    @Transactional
    @CacheEvict(value = "listarServicoFisioterapeuta", allEntries = true)
    public ResponseEntity<RetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ServicoFisioterapeutaForm servicoFisioterapeutaForm) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<ServicoFisioterapeuta> servicoFisioterapeuta = servicoFisioterapeutaRepository.findById(id);

            if (servicoFisioterapeuta.isPresent()) {
                ServicoFisioterapeuta servicoFisioterapeutaAtualizar = servicoFisioterapeutaRepository.getReferenceById(id);
                servicoFisioterapeutaAtualizar.setServico(ServicoMapper.convertToEntity(servicoFisioterapeutaForm.getServico()));
                servicoFisioterapeutaAtualizar.setFisioterapeuta(UsuarioMapper.convertToEntity(servicoFisioterapeutaForm.getFisioterapeuta()));

                retorno = RetornoDTO.sucesso("Dados editados com sucesso!", ServicoFisioterapeutaMapper.convertToVo(servicoFisioterapeutaAtualizar));

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Serviço do fisioterapeuta não encontrado");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deletar/{id}")
    @Transactional
    @CacheEvict(value = "listarServicoFisioterapeuta", allEntries = true)
    public ResponseEntity<RetornoDTO> remover(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<ServicoFisioterapeuta> servicoFisioterapeuta = servicoFisioterapeutaRepository.findById(id);

            if (servicoFisioterapeuta.isPresent()) {
                servicoFisioterapeutaRepository.deleteById(id);
                retorno = RetornoDTO.sucesso("Dados excluídos com sucesso!");
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



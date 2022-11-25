package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.dto.AgendamentoDTO;
import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.dto.ServicoDTO;
import br.edu.ifpr.tcc.form.AgendamentoForm;
import br.edu.ifpr.tcc.mapper.AgendamentoMapper;
import br.edu.ifpr.tcc.mapper.ServicoMapper;
import br.edu.ifpr.tcc.mapper.UsuarioMapper;
import br.edu.ifpr.tcc.modelo.Agendamento;
import br.edu.ifpr.tcc.modelo.Servico;
import br.edu.ifpr.tcc.modelo.Usuario;
import br.edu.ifpr.tcc.repository.AgendamentoRepository;
import br.edu.ifpr.tcc.repository.ServicoRepository;
import br.edu.ifpr.tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
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

        List<Agendamento> lista = agendamentoRepository.findAll(
                Sort.by(Sort.Direction.DESC, "dataAgendamento", "inicioAgendamento")
        );

        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", AgendamentoMapper.convertToListVo(lista));

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid AgendamentoForm agendamentoForm) throws ParseException {
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

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date dataAgendamento = formatter.parse(agendamentoForm.getStDataAgendamento());

                GregorianCalendar gcInicio = new GregorianCalendar();
                gcInicio.setTime(dataAgendamento);
                String[] horaInicio = agendamentoForm.getStInicioAgendamento().split(":");
                gcInicio.set(GregorianCalendar.HOUR_OF_DAY, Integer.parseInt(horaInicio[0]));
                gcInicio.set(GregorianCalendar.MINUTE, Integer.parseInt(horaInicio[1]));
                Date dataHoraInicioAgendamento = gcInicio.getTime();

                agendamento.setInicioAgendamento(dataHoraInicioAgendamento);

                GregorianCalendar gcFim = new GregorianCalendar();
                gcFim.setTime(dataAgendamento);
                String[] horaFim = agendamentoForm.getStFimAgendamento().split(":");
                gcFim.set(GregorianCalendar.HOUR_OF_DAY, Integer.parseInt(horaFim[0]));
                gcFim.set(GregorianCalendar.MINUTE, Integer.parseInt(horaFim[1]));
                Date dataHoraFimAgendamento = gcFim.getTime();

                agendamento.setFimAgendamento(dataHoraFimAgendamento);

                agendamento.setDataAgendamento(dataAgendamento);

                // consultar pra ver se ja tem agendamento criado

                ServicoDTO servicoDTO = ServicoMapper.convertToVo(servico.get());
                Usuario fisioterapeutaAux = fisioterapeuta.get();

                List<Agendamento> agendamentosJaCriados = agendamentoRepository.consultarSeJaExisteAgendamento(dataAgendamento, dataHoraInicioAgendamento, dataHoraFimAgendamento, fisioterapeutaAux);

                if (servicoDTO != null && servicoDTO.getMaximoAlunosSessao() != null) {
                    //Se for 1, é fisioterapia, deixando somente 1 paciente por sessão
                    if (agendamentosJaCriados != null && agendamentosJaCriados.size() > 0 && agendamentosJaCriados.size() >= servicoDTO.getMaximoAlunosSessao()) {
                        //Significa que ja existe agendamento, retornando msg de erro pro usuário

                        retorno = RetornoDTO.erro("Já existem agendamentos cadastrados neste horário");
                        return new ResponseEntity<>(retorno, HttpStatus.OK);
                    }

                }


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

            SimpleDateFormat formatterDataHora = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm");

            if (agendamento.isPresent()) {
                AgendamentoDTO agendamentoDTO = AgendamentoMapper.convertToVo(agendamento.get());

                String stDataAgendamento = formatterDataHora.format(agendamentoDTO.getDataAgendamento());
                agendamentoDTO.setStDataAgendamento(stDataAgendamento);

                String stDataInclusao = formatterDataHora.format(agendamentoDTO.getDataInclusao());
                agendamentoDTO.setStDataInclusao(stDataInclusao);

                String stInicioAgendamento = formatterHora.format(agendamentoDTO.getInicioAgendamento());
                agendamentoDTO.setStInicioAgendamento(stInicioAgendamento);

                String stFimAgendamento = formatterHora.format(agendamentoDTO.getFimAgendamento());
                agendamentoDTO.setStFimAgendamento(stFimAgendamento);

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
    public ResponseEntity<RetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AgendamentoForm agendamentoForm) throws ParseException {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Agendamento> agendamento = agendamentoRepository.findById(id);


            if (agendamento.isPresent()) {
                Agendamento agendamentoAux = agendamento.get();
                Long pacienteId = agendamentoForm.getPaciente().getId();
                Long fisioterapeutaId = agendamentoForm.getFisioterapeuta().getId();
                Long servicoId = agendamentoForm.getServico().getId();

                Agendamento agendamentoFormEntity = AgendamentoMapper.convertFormToEntity(agendamentoForm);




                if (agendamentoFormEntity.getFisioterapeuta() != null && agendamentoFormEntity.getPaciente() != null
                        && agendamentoFormEntity.getServico() != null) {

                    // CONSULTAR FISIOTERAPEUTA,
                    Optional<Usuario> fisioterapeuta = usuarioRepository.findByIdAndFisioterapeutaIsTrue(agendamentoFormEntity.getFisioterapeuta().getId());

                    if (!fisioterapeuta.isPresent()) {
                        retorno = RetornoDTO.erro("Fisioterapeuta não encontrado!");
                        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
                    }

                    // CONSULTAR PACIENTE,
                    Optional<Usuario> paciente = usuarioRepository.findByIdAndPacienteIsTrue(agendamentoFormEntity.getPaciente().getId());

                    if (!paciente.isPresent()) {
                        retorno = RetornoDTO.erro("Paciente não encontrado!");
                        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
                    }

                    // CONSULTAR SERVICO
                    Optional<Servico> servico = servicoRepository.findById(agendamentoFormEntity.getServico().getId());

                    if (!servico.isPresent()) {
                        retorno = RetornoDTO.erro("Serviço não encontrado!");
                        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
                    }

                    Agendamento agendamentoAtualizar = agendamentoRepository.getReferenceById(id);
                    agendamentoAtualizar.setPaciente(new Usuario(pacienteId));
                    agendamentoAtualizar.setFisioterapeuta(new Usuario(fisioterapeutaId));
                    agendamentoAtualizar.setServico(new Servico(servicoId));

                    if (agendamentoAtualizar.getStatus() == null || agendamentoAtualizar.getStatus() < 1 || agendamentoAtualizar.getStatus() > 4) {
                        agendamentoAtualizar.setStatus(Agendamento.Status.Agendado.getId());
                    } else {
                        agendamentoAtualizar.setStatus(agendamentoFormEntity.getStatus());
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataAgendamento = formatter.parse(agendamentoForm.getStDataAgendamento());

                    GregorianCalendar gcInicio = new GregorianCalendar();
                    gcInicio.setTime(dataAgendamento);
                    String[] horaInicio = agendamentoForm.getStInicioAgendamento().split(":");
                    gcInicio.set(GregorianCalendar.HOUR_OF_DAY, Integer.parseInt(horaInicio[0]));
                    gcInicio.set(GregorianCalendar.MINUTE, Integer.parseInt(horaInicio[1]));
                    Date dataHoraInicioAgendamento = gcInicio.getTime();

                    GregorianCalendar gcFim = new GregorianCalendar();
                    gcFim.setTime(dataAgendamento);
                    String[] horaFim = agendamentoForm.getStFimAgendamento().split(":");
                    gcFim.set(GregorianCalendar.HOUR_OF_DAY, Integer.parseInt(horaFim[0]));
                    gcFim.set(GregorianCalendar.MINUTE, Integer.parseInt(horaFim[1]));
                    Date dataHoraFimAgendamento = gcFim.getTime();

                    Usuario fisioterapeutaAux = fisioterapeuta.get();
                    Date dataAgendamentoAntigo = agendamentoAux.getDataAgendamento();
                    Date dataInicioAgendamento = agendamentoAux.getInicioAgendamento();
                    Date dataFimAgendamento = agendamentoAux.getFimAgendamento();

                    List<Agendamento> agendamentosJaCriados = agendamentoRepository.consultarSeJaExisteAgendamentoEdicao(dataAgendamento, dataHoraInicioAgendamento, dataHoraFimAgendamento, fisioterapeutaAux, id);

                    Servico servicoEntity = servico.get();
                    if (servicoEntity != null && servicoEntity.getMaximoAlunosSessao() != null) {
                        //Se for 1, é fisioterapia, deixando somente 1 paciente por sessão
                        if (agendamentosJaCriados != null && agendamentosJaCriados.size() > 0 && agendamentosJaCriados.size() >= servicoEntity.getMaximoAlunosSessao()) {
                            //Significa que ja existe agendamento, retornando msg de erro pro usuário

                            retorno = RetornoDTO.erro("Já existem agendamentos cadastrados neste horário");
                            return new ResponseEntity<>(retorno, HttpStatus.OK);
                        }

                    }

                    agendamentoAtualizar.setDataAgendamento(dataAgendamento);
                    agendamentoAtualizar.setInicioAgendamento(dataHoraInicioAgendamento);
                    agendamentoAtualizar.setFimAgendamento(dataHoraFimAgendamento);

                    retorno = RetornoDTO.sucesso("Agendamento alterado com sucesso!", AgendamentoMapper.convertToVo(agendamentoAtualizar));
                    return new ResponseEntity<>(retorno, HttpStatus.OK);
                }
            } else {
                retorno = RetornoDTO.erro("Agendamento não encontrado");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
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



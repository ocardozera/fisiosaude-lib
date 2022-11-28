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
import java.util.*;

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

    @PostMapping("/listarFiltros")
    public ResponseEntity<RetornoDTO> listarFiltros(@RequestBody AgendamentoForm agendamento) {
        RetornoDTO retorno;
        List<Agendamento> listaAgendamentos = null;
        Boolean utilizouFindAll = false;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (agendamento != null) {

            if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() == null) {

                Usuario paciente = new Usuario(agendamento.getPaciente().getId());

                listaAgendamentos = agendamentoRepository.filtroPorPaciente(paciente);
            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() == null)  {

                Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());

                listaAgendamentos = agendamentoRepository.filtroPorFisioterapeuta(fisioterapeuta);

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() == null)  {

                Servico servico = new Servico(agendamento.getServico().getId());

                listaAgendamentos = agendamentoRepository.filtroPorServico(servico);

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() == null)  {

                Integer status = agendamento.getStatus();

                listaAgendamentos = agendamentoRepository.filtroPorStatus(status);

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() != null)  {

                try {
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorDataAgendamento(dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() == null)  {

                Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());

                listaAgendamentos = agendamentoRepository.filtroPorPacienteFisioterapeuta(paciente, fisioterapeuta);

            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() == null)  {

                Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());
                Servico servico = new Servico(agendamento.getServico().getId());

                listaAgendamentos = agendamentoRepository.filtroPorPacienteFisioterapeutaServico(paciente, fisioterapeuta, servico);

            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() == null)  {

                Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());
                Servico servico = new Servico(agendamento.getServico().getId());
                Integer status = agendamento.getStatus();

                listaAgendamentos = agendamentoRepository.filtroPorPacienteFisioterapeutaServicoStatus(paciente, fisioterapeuta, servico, status);

            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() != null)  {

                try {

                    Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                    Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());
                    Servico servico = new Servico(agendamento.getServico().getId());
                    Integer status = agendamento.getStatus();
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorPacienteFisioterapeutaServicoStatusDataAgendamento(paciente, fisioterapeuta, servico, status, dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() == null)  {

                Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                Servico servico = new Servico(agendamento.getServico().getId());

                listaAgendamentos = agendamentoRepository.filtroPorPacienteServico(paciente, servico);

            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() == null)  {

                Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                Servico servico = new Servico(agendamento.getServico().getId());
                Integer status = agendamento.getStatus();

                listaAgendamentos = agendamentoRepository.filtroPorPacienteServicoStatus(paciente, servico, status);

            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() != null)  {

                try {

                    Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                    Servico servico = new Servico(agendamento.getServico().getId());
                    Integer status = agendamento.getStatus();
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorPacienteServicoStatusDataAgendamento(paciente, servico, status, dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() == null)  {

                Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                Integer status = agendamento.getStatus();

                listaAgendamentos = agendamentoRepository.filtroPorPacienteStatus(paciente, status);

            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() != null)  {

                try {

                    Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                    Integer status = agendamento.getStatus();
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorPacienteStatusDataAgendamento(paciente, status, dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() != null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() != null)  {

                try {

                    Usuario paciente = new Usuario(agendamento.getPaciente().getId());
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorPacienteDataAgendamento(paciente, dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() == null)  {

                Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());
                Servico servico = new Servico(agendamento.getServico().getId());

                listaAgendamentos = agendamentoRepository.filtroPorFisioterapeutaServico(fisioterapeuta, servico);

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() == null)  {

                Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());
                Servico servico = new Servico(agendamento.getServico().getId());
                Integer status = agendamento.getStatus();

                listaAgendamentos = agendamentoRepository.filtroPorFisioterapeutaServicoStatus(fisioterapeuta, servico, status);

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() != null)  {

                try {

                    Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());
                    Servico servico = new Servico(agendamento.getServico().getId());
                    Integer status = agendamento.getStatus();
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorFisioterapeutaServicoStatusDataAgendamento(fisioterapeuta, servico, status, dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() == null)  {

                try {

                    Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());
                    Integer status = agendamento.getStatus();

                    listaAgendamentos = agendamentoRepository.filtroPorFisioterapeutaStatus(fisioterapeuta, status);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() != null)  {

                try {

                    Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());
                    Integer status = agendamento.getStatus();
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorFisioterapeutaStatusDataAgendamento(fisioterapeuta, status, dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() != null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() == null
                    && agendamento.getStDataAgendamento() != null)  {

                try {

                    Usuario fisioterapeuta = new Usuario(agendamento.getFisioterapeuta().getId());
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorFisioterapeutaDataAgendamento(fisioterapeuta, dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() == null)  {

                try {

                    Servico servico = new Servico(agendamento.getServico().getId());
                    Integer status = agendamento.getStatus();

                    listaAgendamentos = agendamentoRepository.filtroPorServicoStatus(servico, status);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() != null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() != null)  {

                try {

                    Servico servico = new Servico(agendamento.getServico().getId());
                    Integer status = agendamento.getStatus();
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorServicoStatusDataAgendamento(servico, status, dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (agendamento.getPaciente() == null
                    && agendamento.getFisioterapeuta() == null
                    && agendamento.getServico() == null
                    && agendamento.getStatus() != null
                    && agendamento.getStDataAgendamento() != null)  {

                try {

                    Integer status = agendamento.getStatus();
                    Date dataAgendamento = formatter.parse(agendamento.getStDataAgendamento());

                    listaAgendamentos = agendamentoRepository.filtroPorStatusDataAgendamento(status, dataAgendamento);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                utilizouFindAll = true;
                listaAgendamentos = agendamentoRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "dataAgendamento", "inicioAgendamento")
                );
            }

        } else {
            utilizouFindAll = true;
            listaAgendamentos = agendamentoRepository.findAll(
                    Sort.by(Sort.Direction.DESC, "dataAgendamento", "inicioAgendamento")
            );
        }

        if (!utilizouFindAll) {

            Collections.sort(listaAgendamentos, Comparator.comparing(Agendamento::getDataAgendamento));

            listaAgendamentos.sort((o1, o2) -> {
                if (o1.getDataAgendamento().getTime() > o2.getDataAgendamento().getTime() && o1.getInicioAgendamento().getTime() > o2.getInicioAgendamento().getTime()) {
                    return -1;
                } else if (o1.getDataAgendamento().getTime() == o2.getDataAgendamento().getTime() && o1.getInicioAgendamento().getTime() > o2.getInicioAgendamento().getTime()) {
                    return -1;
                } else if (o1.getDataAgendamento().getTime() == o2.getDataAgendamento().getTime() && o1.getInicioAgendamento().getTime() < o2.getInicioAgendamento().getTime()) {
                    return 1;
                } else {
                    return 1;
                }
            });

        }



        retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", AgendamentoMapper.convertToListVo(listaAgendamentos));

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

                Usuario pacienteAux = agendamento.getPaciente();
                List<Agendamento> agendamentosJaCriadosPacientes = agendamentoRepository.consultarSeJaExisteAgendamentoPaciente(dataAgendamento, dataHoraInicioAgendamento, dataHoraFimAgendamento, pacienteAux);
                if (agendamentosJaCriadosPacientes != null && agendamentosJaCriadosPacientes.size() > 0) {
                    //Significa que ja existe agendamento, retornando msg de erro pro usuário

                    retorno = RetornoDTO.erro("Já existem agendamentos cadastrados neste horário");
                    return new ResponseEntity<>(retorno, HttpStatus.OK);
                }

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


                    Usuario pacienteAux = agendamentoAtualizar.getPaciente();
                    List<Agendamento> agendamentosJaCriadosPacientes = agendamentoRepository.consultarSeJaExisteAgendamentoPaciente(dataAgendamento, dataHoraInicioAgendamento, dataHoraFimAgendamento, pacienteAux);
                    if (agendamentosJaCriadosPacientes != null && agendamentosJaCriadosPacientes.size() > 0) {
                        //Significa que ja existe agendamento, retornando msg de erro pro usuário

                        retorno = RetornoDTO.erro("Já existem agendamentos cadastrados neste horário");
                        return new ResponseEntity<>(retorno, HttpStatus.OK);
                    }



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



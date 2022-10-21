package br.edu.ifpr.tcc.mapper;

import br.edu.ifpr.tcc.dto.AgendamentoDTO;
import br.edu.ifpr.tcc.form.AgendamentoForm;
import br.edu.ifpr.tcc.modelo.Agendamento;

import java.util.ArrayList;
import java.util.List;

public class AgendamentoMapper {

    public static AgendamentoDTO convertToVo(Agendamento entity) {

        AgendamentoDTO dto = null;
        if (entity != null) {
            dto = new AgendamentoDTO();
            dto.setId(entity.getId());
            dto.setFisioterapeuta(UsuarioMapper.convertToVo(entity.getFisioterapeuta()));
            dto.setPaciente(UsuarioMapper.convertToVo(entity.getPaciente()));
            dto.setServico(ServicoMapper.convertToVo(entity.getServico()));
            dto.setStatus(entity.getStatus());
            dto.setDataInclusao(entity.getDataInclusao());
            dto.setDataAgendamento(entity.getDataAgendamento());
            dto.setInicioAgendamento(entity.getInicioAgendamento());
            dto.setFimAgendamento(entity.getFimAgendamento());
        }
        return dto;
    }

    public static List<AgendamentoDTO> convertToListVo(List<Agendamento> listEntity) {
        List<AgendamentoDTO> listVo = null;
        if (listEntity != null) {
            listVo = new ArrayList<>();
            for (Agendamento entity : listEntity) {
                listVo.add(convertToVo(entity));
            }
        }
        return listVo;
    }

    public static Agendamento convertToEntity(AgendamentoDTO dto) {

        Agendamento entity = null;
        if (dto != null) {
            entity = new Agendamento();
            entity.setId(dto.getId());
            entity.setFisioterapeuta(UsuarioMapper.convertToEntity(dto.getFisioterapeuta()));
            entity.setPaciente(UsuarioMapper.convertToEntity(dto.getPaciente()));
            entity.setServico(ServicoMapper.convertToEntity(dto.getServico()));
            entity.setStatus(dto.getStatus());
            entity.setDataInclusao(dto.getDataInclusao());
            entity.setDataAgendamento(dto.getDataAgendamento());
            entity.setInicioAgendamento(dto.getInicioAgendamento());
            entity.setFimAgendamento(dto.getFimAgendamento());
        }
        return entity;
    }

    public static List<Agendamento> convertToListEntity(List<AgendamentoDTO> listVo) {
        List<Agendamento> listEntity = null;
        if (listVo != null) {
            listEntity = new ArrayList<>();
            for (AgendamentoDTO vo : listVo) {
                listEntity.add(convertToEntity(vo));
            }
        }
        return listEntity;
    }

    public static Agendamento convertFormToEntity(AgendamentoForm form) {

        Agendamento entity = null;
        if (form != null) {
            entity = new Agendamento();
            entity.setFisioterapeuta(UsuarioMapper.convertToEntity(form.getFisioterapeuta()));
            entity.setPaciente(UsuarioMapper.convertToEntity(form.getPaciente()));
            entity.setServico(ServicoMapper.convertToEntity(form.getServico()));
            entity.setStatus(form.getStatus());
//            entity.setDataAgendamento(form.getDataAgendamento());
//            entity.setInicioAgendamento(form.getInicioAgendamento());
//            entity.setFimAgendamento(form.getFimAgendamento());
        }
        return entity;
    }
}

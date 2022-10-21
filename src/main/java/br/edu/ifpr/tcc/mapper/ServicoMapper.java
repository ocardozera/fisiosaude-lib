package br.edu.ifpr.tcc.mapper;

import br.edu.ifpr.tcc.dto.ServicoDTO;
import br.edu.ifpr.tcc.form.ServicoForm;
import br.edu.ifpr.tcc.modelo.Servico;

import java.util.ArrayList;
import java.util.List;

public class ServicoMapper {

    public static ServicoDTO convertToVo(Servico entity) {

        ServicoDTO dto = null;
        if (entity != null) {
            dto = new ServicoDTO();
            dto.setId(entity.getId());
            dto.setNome(entity.getNome());
            dto.setValorSessao(entity.getValorSessao());
            dto.setMaximoAlunosSessao(entity.getMaximoAlunosSessao());
        }
        return dto;
    }

    public static List<ServicoDTO> convertToListVo(List<Servico> listEntity) {
        List<ServicoDTO> listVo = null;
        if (listEntity != null) {
            listVo = new ArrayList<>();
            for (Servico entity : listEntity) {
                listVo.add(convertToVo(entity));
            }
        }
        return listVo;
    }

    public static Servico convertToEntity(ServicoDTO dto) {

        Servico entity = null;
        if (dto != null) {
            entity = new Servico();
            entity.setId(dto.getId());
            entity.setNome(dto.getNome());
            entity.setValorSessao(dto.getValorSessao());
            entity.setMaximoAlunosSessao(dto.getMaximoAlunosSessao());
        }
        return entity;
    }

    public static List<Servico> convertToListEntity(List<ServicoDTO> listVo) {
        List<Servico> listEntity = null;
        if (listVo != null) {
            listEntity = new ArrayList<>();
            for (ServicoDTO vo : listVo) {
                listEntity.add(convertToEntity(vo));
            }
        }
        return listEntity;
    }

    public static Servico convertFormToEntity(ServicoForm form) {

        Servico entity = null;
        if (form != null) {
            entity = new Servico();
            entity.setNome(form.getNome());
            entity.setValorSessao(form.getValorSessao());
            entity.setMaximoAlunosSessao(form.getMaximoAlunosSessao());
        }
        return entity;
    }
}

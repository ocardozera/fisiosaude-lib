package br.edu.ifpr.tcc.mapper;

import br.edu.ifpr.tcc.dto.ServicoFisioterapeutaDTO;
import br.edu.ifpr.tcc.form.ServicoFisioterapeutaForm;
import br.edu.ifpr.tcc.modelo.ServicoFisioterapeuta;

import java.util.ArrayList;
import java.util.List;

public class ServicoFisioterapeutaMapper {

    public static ServicoFisioterapeutaDTO convertToVo(ServicoFisioterapeuta entity) {

        ServicoFisioterapeutaDTO dto = null;
        if (entity != null) {
            dto = new ServicoFisioterapeutaDTO();
            dto.setId(entity.getId());
            dto.setServico(ServicoMapper.convertToVo(entity.getServico()));
            dto.setFisioterapeuta(UsuarioMapper.convertToVo(entity.getFisioterapeuta()));
        }
        return dto;
    }

    public static List<ServicoFisioterapeutaDTO> convertToListVo(List<ServicoFisioterapeuta> listEntity) {
        List<ServicoFisioterapeutaDTO> listVo = null;
        if (listEntity != null) {
            listVo = new ArrayList<>();
            for (ServicoFisioterapeuta entity : listEntity) {
                listVo.add(convertToVo(entity));
            }
        }
        return listVo;
    }

    public static ServicoFisioterapeuta convertToEntity(ServicoFisioterapeutaDTO dto) {

        ServicoFisioterapeuta entity = null;
        if (dto != null) {
            entity = new ServicoFisioterapeuta();
            entity.setId(dto.getId());
            entity.setServico(ServicoMapper.convertToEntity(dto.getServico()));
            entity.setFisioterapeuta(UsuarioMapper.convertToEntity(dto.getFisioterapeuta()));

        }
        return entity;
    }

    public static List<ServicoFisioterapeuta> convertToListEntity(List<ServicoFisioterapeutaDTO> listVo) {
        List<ServicoFisioterapeuta> listEntity = null;
        if (listVo != null) {
            listEntity = new ArrayList<>();
            for (ServicoFisioterapeutaDTO vo : listVo) {
                listEntity.add(convertToEntity(vo));
            }
        }
        return listEntity;
    }

    public static ServicoFisioterapeuta convertFormToEntity(ServicoFisioterapeutaForm form) {

        ServicoFisioterapeuta entity = null;
        if (form != null) {
            entity = new ServicoFisioterapeuta();
            entity.setServico(ServicoMapper.convertToEntity(form.getServico()));
            entity.setFisioterapeuta(UsuarioMapper.convertToEntity(form.getFisioterapeuta()));
        }
        return entity;
    }
}

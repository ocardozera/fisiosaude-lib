package br.edu.ifpr.tcc.mapper;

import br.edu.ifpr.tcc.dto.CidadeDTO;
import br.edu.ifpr.tcc.form.CidadeForm;
import br.edu.ifpr.tcc.modelo.Cidade;

import java.util.ArrayList;
import java.util.List;

public class CidadeMapper {

    public static CidadeDTO convertToVo(Cidade entity) {

        CidadeDTO dto = null;
        if (entity != null) {
            dto = new CidadeDTO();
            dto.setId(entity.getId());
            dto.setNome(entity.getNome());
            dto.setEstado(EstadoMapper.convertToVo(entity.getEstado()));
        }
        return dto;
    }

    public static List<CidadeDTO> convertToListVo(List<Cidade> listEntity) {
        List<CidadeDTO> listVo = null;
        if (listEntity != null) {
            listVo = new ArrayList<>();
            for (Cidade entity : listEntity) {
                listVo.add(convertToVo(entity));
            }
        }
        return listVo;
    }

    public static Cidade convertToEntity(CidadeDTO dto) {

        Cidade entity = null;
        if (dto != null) {
            entity = new Cidade();
            entity.setId(dto.getId());
            entity.setNome(dto.getNome());
            entity.setEstado(EstadoMapper.convertToEntity(dto.getEstado()));
        }
        return entity;
    }

    public static List<Cidade> convertToListEntity(List<CidadeDTO> listVo) {
        List<Cidade> listEntity = null;
        if (listVo != null) {
            listEntity = new ArrayList<>();
            for (CidadeDTO vo : listVo) {
                listEntity.add(convertToEntity(vo));
            }
        }
        return listEntity;
    }

    public static Cidade convertFormToEntity(CidadeForm form) {

        Cidade entity = null;
        if (form != null) {
            entity = new Cidade();
            entity.setNome(form.getNome());
            entity.setEstado(EstadoMapper.convertToEntity(form.getEstado()));
        }
        return entity;
    }
}

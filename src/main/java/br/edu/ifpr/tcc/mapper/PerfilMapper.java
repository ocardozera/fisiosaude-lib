package br.edu.ifpr.tcc.mapper;

import br.edu.ifpr.tcc.dto.PerfilDTO;
import br.edu.ifpr.tcc.form.EstadoForm;
import br.edu.ifpr.tcc.modelo.Estado;
import br.edu.ifpr.tcc.modelo.Perfil;

import java.util.ArrayList;
import java.util.List;

public class PerfilMapper {

    public static PerfilDTO convertToVo(Perfil entity) {

        PerfilDTO dto = null;
        if (entity != null) {
            dto = new PerfilDTO();
            dto.setId(entity.getId());
            dto.setNome(entity.getNome());
        }
        return dto;
    }

    public static List<PerfilDTO> convertToListVo(List<Perfil> listEntity) {
        List<PerfilDTO> listVo = null;
        if (listEntity != null) {
            listVo = new ArrayList<>();
            for (Perfil entity : listEntity) {
                listVo.add(convertToVo(entity));
            }
        }
        return listVo;
    }

    public static Perfil convertToEntity(PerfilDTO dto) {

        Perfil entity = null;
        if (dto != null) {
            entity = new Perfil();
            entity.setId(dto.getId());
            entity.setNome(dto.getNome());
        }
        return entity;
    }

    public static List<Perfil> convertToListEntity(List<PerfilDTO> listVo) {
        List<Perfil> listEntity = null;
        if (listVo != null) {
            listEntity = new ArrayList<>();
            for (PerfilDTO vo : listVo) {
                listEntity.add(convertToEntity(vo));
            }
        }
        return listEntity;
    }

    public static Estado convertFormToEntity(EstadoForm form) {

        Estado entity = null;
        if (form != null) {
            entity = new Estado();
            entity.setNome(form.getNome());
            entity.setSigla(form.getSigla());
        }
        return entity;
    }
}

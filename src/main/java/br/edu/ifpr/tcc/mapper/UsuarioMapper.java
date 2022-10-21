package br.edu.ifpr.tcc.mapper;

import br.edu.ifpr.tcc.dto.UsuarioDTO;
import br.edu.ifpr.tcc.form.CidadeForm;
import br.edu.ifpr.tcc.modelo.Cidade;
import br.edu.ifpr.tcc.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioMapper {

    public static UsuarioDTO convertToVo(Usuario entity) {

        UsuarioDTO dto = null;
        if (entity != null) {
            dto = new UsuarioDTO();
            dto.setId(entity.getId());
            dto.setNome(entity.getNome());
            dto.setEmail(entity.getEmail());
            dto.setSenha(entity.getSenha());

            dto.setTelefone(entity.getTelefone());
            dto.setCpf(entity.getCpf());
            dto.setDataNascimento(entity.getDataNascimento());
            dto.setCidade(CidadeMapper.convertToVo(entity.getCidade()));
            dto.setLogradouro(entity.getLogradouro());
            dto.setNumero(entity.getNumero());
            dto.setComplemento(entity.getComplemento());
            dto.setBairro(entity.getBairro());

            dto.setAdministrador(entity.getAdministrador());

            dto.setFisioterapeuta(entity.getFisioterapeuta());
            dto.setRegistroProfissional(entity.getRegistroProfissional());

            dto.setPaciente(entity.getPaciente());

        }
        return dto;
    }

    public static List<UsuarioDTO> convertToListVo(List<Usuario> listEntity) {
        List<UsuarioDTO> listVo = null;
        if (listEntity != null) {
            listVo = new ArrayList<>();
            for (Usuario entity : listEntity) {
                listVo.add(convertToVo(entity));
            }
        }
        return listVo;
    }

    public static Usuario convertToEntity(UsuarioDTO dto) {

        Usuario entity = null;
        if (dto != null) {
            entity = new Usuario();
            entity.setId(dto.getId());
            entity.setNome(dto.getNome());
            entity.setEmail(dto.getEmail());
            entity.setSenha(dto.getSenha());

            entity.setTelefone(dto.getTelefone());
            entity.setCpf(dto.getCpf());
            entity.setDataNascimento(dto.getDataNascimento());
            entity.setCidade(CidadeMapper.convertToEntity(dto.getCidade()));
            entity.setLogradouro(dto.getLogradouro());
            entity.setNumero(dto.getNumero());
            entity.setComplemento(dto.getComplemento());
            entity.setBairro(dto.getBairro());

            entity.setAdministrador(dto.getAdministrador());

            entity.setFisioterapeuta(dto.getFisioterapeuta());
            entity.setRegistroProfissional(dto.getRegistroProfissional());

            entity.setPaciente(dto.getPaciente());
        }
        return entity;
    }

    public static List<Usuario> convertToListEntity(List<UsuarioDTO> listVo) {
        List<Usuario> listEntity = null;
        if (listVo != null) {
            listEntity = new ArrayList<>();
            for (UsuarioDTO vo : listVo) {
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

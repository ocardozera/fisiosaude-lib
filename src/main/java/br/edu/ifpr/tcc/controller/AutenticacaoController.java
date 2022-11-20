package br.edu.ifpr.tcc.controller;

import br.edu.ifpr.tcc.config.security.TokenService;
import br.edu.ifpr.tcc.dto.CidadeDTO;
import br.edu.ifpr.tcc.dto.RetornoDTO;
import br.edu.ifpr.tcc.dto.TokenDTO;
import br.edu.ifpr.tcc.dto.UsuarioDTO;
import br.edu.ifpr.tcc.form.LoginForm;
import br.edu.ifpr.tcc.mapper.CidadeMapper;
import br.edu.ifpr.tcc.mapper.UsuarioMapper;
import br.edu.ifpr.tcc.modelo.Cidade;
import br.edu.ifpr.tcc.modelo.Usuario;
import br.edu.ifpr.tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> autenticar(@RequestBody @Valid LoginForm form) {

        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try {
            Authentication authentication = authManager.authenticate(dadosLogin);

            String token = tokenService.gerarToken(authentication);



            return ResponseEntity.ok(new TokenDTO("Bearer", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();

        }

    }

    @GetMapping("/detalhar/{id}")
    public ResponseEntity<RetornoDTO> detalhar(@PathVariable Long id) {
        RetornoDTO retorno;

        if (id != null && id > 0) {
            Optional<Usuario> usuario = usuarioRepository.findById(id);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            if (usuario.isPresent()) {
                UsuarioDTO usuarioDTO = UsuarioMapper.convertToVo(usuario.get());

                String stDataNascimento = formatter.format(usuarioDTO.getDataNascimento());

                usuarioDTO.setStDataNascimento(stDataNascimento);

                usuarioDTO.setSenha(null);

                retorno = RetornoDTO.sucesso("Consulta realizada com sucesso!", usuarioDTO);

                return new ResponseEntity<>(retorno, HttpStatus.OK);
            } else {
                retorno = RetornoDTO.erro("Usuário não encontrado!");

                return new ResponseEntity<>(retorno, HttpStatus.NOT_FOUND);
            }
        }

        retorno = RetornoDTO.erro("Sem conteúdo");
        return new ResponseEntity<>(retorno, HttpStatus.NO_CONTENT);
    }

}

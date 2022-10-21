package br.edu.ifpr.tcc.config.security;

import br.edu.ifpr.tcc.modelo.Usuario;
import br.edu.ifpr.tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Implemento a lógica do BD de fazer consulta pelo usuário através do email (username)
        //Para achar este usuario pelo username, pois a senha o Spring faz a validação em memória

        Optional<Usuario> usuario = usuarioRepository.findByEmail(username);

        if (usuario.isPresent()) {
            return usuario.get();
        }

        throw new UsernameNotFoundException("Dados inválidos");
    }
}

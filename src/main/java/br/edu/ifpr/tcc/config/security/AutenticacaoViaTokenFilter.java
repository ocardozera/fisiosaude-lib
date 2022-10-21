package br.edu.ifpr.tcc.config.security;

import br.edu.ifpr.tcc.modelo.Usuario;
import br.edu.ifpr.tcc.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;


    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // recupero o token do cabeçalho
         String token = recuperarToken(request);

        //valido ele
        boolean tokenValido = tokenService.tokenEstaValido(token);

        // Autentico o usuário para o Spring
        if (tokenValido) {
            //Autentico o usuario
            autenticarCliente(token);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticarCliente(String token) {

        //Forço uma autenticação no Spring
        Long idUsuario = tokenService.obterIdUsuarioByToken(token);

        Usuario usuario = usuarioRepository.findById(idUsuario).get();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);


    }


    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }

        token = token.substring(7, token.length());

        return token;
    }
}

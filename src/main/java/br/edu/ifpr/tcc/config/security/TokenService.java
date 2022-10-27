package br.edu.ifpr.tcc.config.security;

import br.edu.ifpr.tcc.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

import java.util.Date;

@Service
public class TokenService {

    @Value("${fisiosaude.jwt.expiration}")
    private String expiration;

    @Value("${fisiosaude.jwt.secret}")
    private String secret;


    public String gerarToken(Authentication authentication) {

        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API do Fórum da Alura") //qual a aplicação que fez a geração do token
                .setSubject(logado.getId().toString()) // para quem é o token/usuario autenticado que possui o token
                .setIssuedAt(hoje) // data de geração do token
                .setExpiration(dataExpiracao) // data de expiração do token
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public boolean tokenEstaValido(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long obterIdUsuarioByToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}

package es.uma.informatica.notificacion.components;

import es.uma.informatica.notificacion.services.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtTokenVerificationFilter extends OncePerRequestFilter {

    @Value("${application.security.jwt.secret.key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if (auth.equals("")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Verificar path del endpoint
        /*if (request.getServletPath().contains("/pendientes/abortar")) {
            filterChain.doFilter(request, response);
            return;
        }*/

        if (!auth.startsWith("Bearer ")) {
            // No es JWT
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer token de la cabecera
        String bearerToken = auth.substring("Bearer ".length());

        // Validar mac del token
        try {
            var token = JwtUtils.verifyToken(secretKey, bearerToken);
            // SecurityContextHolder.getContext().setAuthentication();
            Claims claims = JwtUtils.getClaims(bearerToken, secretKey);
            String username = claims.getSubject();

            if (username == null || username.isEmpty()) {
                throw new BadCredentialsException("El token no contiene un nombre de usuario");
            }

            List<String> roles = claims.get("roles", List.class);

            if (roles == null) {
                throw new BadCredentialsException("El token no contiene ningún rol");
            }

            // Crear una instancia de la clase UsernamePasswordAuthenticationToken con las credenciales del usuario
            var authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (SignatureException e ) {
            System.err.println("[ERROR]\tNo se pudo verificar el MAC del token. Token jwt alterano");
        } finally {
            filterChain.doFilter(request, response);
        }

    }
}

package es.uma.informatica.usuariojpa.components;

import es.uma.informatica.usuariojpa.modelo.Usuario;
import es.uma.informatica.usuariojpa.repositories.TokenRepository;
import es.uma.informatica.usuariojpa.repositories.UsuarioRepository;
import es.uma.informatica.usuariojpa.services.JwtService;
import es.uma.informatica.usuariojpa.services.UsuarioService;
import es.uma.informatica.usuariojpa.services.excepciones.EmailNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UsuarioRepository userRepo;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // No debiera ser necesaria con la config actual
        if (request.getServletPath().contains("/passwordreset") || request.getServletPath().contains("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.equals("")) {  // nuevo: authHeader == null
            filterChain.doFilter(request, response);
            return;
        }
        // Hay token

        final String jwt;
        final String userEmail;
        if (!authHeader.startsWith("Bearer ", 0)) {
            // No es JWT
            filterChain.doFilter(request, response);
            return;
        }

        // Hay token y de tipo jwt
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // El usuario está registrado en la aplicación (con las credenciales del token)
            // Pero sus datos no están cargados en el contexto de la aplicación
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            /*boolean isTokenValid = false;
            try {
                isTokenValid = tokenRepository.findById(Math.toIntExact(userRepo.findByEmail(userEmail).get().getId()))
                        .map(t -> !t.isExpired())
                        .orElse(false);
            } catch (EmailNotFoundException e ) {
                System.err.println(e.toString());
            }*/


             var isTokenValid = tokenRepository.findByToken(jwt)
                             .map(t -> !t.isRevoked())
                             .orElse(false);




            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {// Las credenciales contenidas en el token coinciden con el userDetail, no está expirado, y es válido (ni expir, ni revok)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

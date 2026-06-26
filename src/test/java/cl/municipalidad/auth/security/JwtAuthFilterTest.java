package cl.municipalidad.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    // --- TEST: INTERCEPCIÓN Y AUTORIZACIÓN HTTP ---
    @Test
    void doFilterInternal_CuandoTokenEsValido_DeberiaEstablecerAutenticacionEnSpring() throws Exception {
        String secret = "ClaveUltraSecretaEInviolableParaLaMunicipalidad2026!";
        String issuer = "ms-auth";
        
        ReflectionTestUtils.setField(jwtAuthFilter, "secret", secret);
        ReflectionTestUtils.setField(jwtAuthFilter, "issuer", issuer);

        String tokenReal = JWT.create()
                .withSubject("toto@muni.cl")
                .withIssuer(issuer)
                .withClaim("roles", List.of("ADMIN"))
                .sign(Algorithm.HMAC256(secret));

        when(request.getHeader("Authorization")).thenReturn("Bearer" + tokenReal);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("toto@muni.cl", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
package cl.municipalidad.auth.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import cl.municipalidad.auth.config.JwtProperties;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generarToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withIssuer(jwtProperties.getIssuer())
                .withClaim("roles", List.of("ROLe_" + role))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }
}
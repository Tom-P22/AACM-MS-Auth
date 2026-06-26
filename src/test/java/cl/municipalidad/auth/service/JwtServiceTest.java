package cl.municipalidad.auth.service;

import cl.municipalidad.auth.config.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    // --- TEST: INTEGRIDAD Y REGLAS DEL JWT ---
    @Test
    void generarToken_DeberiaEstructurarClaimsYFirmaCorrectamente() {
        JwtProperties propiedades = new JwtProperties();
        propiedades.setSecret("ClaveUltraSecretaEInviolableParaLaMunicipalidad2026!");
        propiedades.setIssuer("ms-auth");
        propiedades.setExpiration(3600000L);

        JwtService jwtService = new JwtService(propiedades);

        String token = jwtService.generarToken("Toby", "ADMIN", "toto@muni.cl");

        assertNotNull(token);
        DecodedJWT decoded = JWT.decode(token);
        
        assertEquals("Toby", decoded.getSubject());
        assertEquals("ms-auth", decoded.getIssuer());
        assertEquals(List.of("ADMIN"), decoded.getClaim("roles").asList(String.class));
        assertEquals("toto@muni.cl", decoded.getClaim("email").asString());
    }
}
package cl.municipalidad.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import cl.municipalidad.auth.dto.request.DtoAuthRequest;
import cl.municipalidad.auth.dto.response.DtoAuthResponse;
import cl.municipalidad.auth.dto.response.UsuarioDto;
import cl.municipalidad.auth.enums.RolUsuario;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private RestClient restClient;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder passwordEncoder;

    @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private RestClient.ResponseSpec responseSpec;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        
        authService = new AuthService(jwtService, passwordEncoder);
        
        ReflectionTestUtils.setField(authService, "restClient", restClient);
    }

    private void simularFlujoRestClient(UsuarioDto usuarioRetornado) {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(UsuarioDto.class)).thenReturn(usuarioRetornado);
    }

    // --- TEST: LOGiN EXITOSO ---
    @Test
    void login_CuandoCredencialesSonCorrectas_DeberiaRetornarToken() {
        DtoAuthRequest request = new DtoAuthRequest("toto@muni.cl", "clave123");
        
        UsuarioDto usuarioMock = new UsuarioDto();
        usuarioMock.setEmail("toto@muni.cl");
        usuarioMock.setPassword("encodedPassword");
        usuarioMock.setActivo(true);
        usuarioMock.setNombre("Toby");
        usuarioMock.setRolUsuario(RolUsuario.ADMIN);

        simularFlujoRestClient(usuarioMock);
        when(passwordEncoder.matches("clave123", "encodedPassword")).thenReturn(true);
        when(jwtService.generarToken("Toby", "ADMIN", "toto@muni.cl")).thenReturn("token-exitoso");

        DtoAuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("token-exitoso", response.getToken());
    }

    // --- TEST : RECHAZO POR USUARIO INACTIVO ---
    @Test
    void login_CuandoUsuarioEstaDeshabilitado_DeberiaLanzarUnauthorized() {
        DtoAuthRequest request = new DtoAuthRequest("bloqueado@muni.cl", "clave123");
        
        UsuarioDto usuarioMock = new UsuarioDto();
        usuarioMock.setEmail("bloqueado@muni.cl");
        usuarioMock.setActivo(false); // <- Bloqueado

        simularFlujoRestClient(usuarioMock);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.login(request);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Usuario o password incorrecto", exception.getReason());
    }

    // --- TEST: RECHAZO POR CONTRASEÑA INCORRECTA ---
    @Test
    void login_CuandoPasswordEsIncorrecta_DeberiaLanzarUnauthorized() {
        DtoAuthRequest request = new DtoAuthRequest("toto@muni.cl", "claveErronea");
        
        UsuarioDto usuarioMock = new UsuarioDto();
        usuarioMock.setEmail("toto@muni.cl");
        usuarioMock.setPassword("encodedPassword");
        usuarioMock.setActivo(true);

        simularFlujoRestClient(usuarioMock);
        when(passwordEncoder.matches("claveErronea", "encodedPassword")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.login(request);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }
}
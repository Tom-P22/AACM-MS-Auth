package cl.municipalidad.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import cl.municipalidad.auth.dto.request.DtoAuthRequest;
import cl.municipalidad.auth.dto.response.DtoAuthResponse;
import cl.municipalidad.auth.dto.response.UsuarioDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestClient restClient;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(RestClient.Builder restClientBuilder, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.restClient = restClientBuilder.build();
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public DtoAuthResponse login(DtoAuthRequest request) {

        log.info("Iniciando proceso de login para usuario {}", request.getEmail());

        UsuarioDto usuario;
        try {
            log.debug("Llamando a MS-Usuarios...");
            usuario = restClient.get()
            .uri("http://ms-usuarios/api/v1/usuarios/internal/buscar/email/" + request.getEmail())
            .retrieve()
            .body(UsuarioDto.class);

        } catch (Exception e) {
            //Si usuario = 404, entonces:
            log.error("Error al conectar con MS-Usuarios o usuario no encontrado: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o password incorrecto");
        }
        
        if (!usuario.getActivo()) {
            log.error("Login fallido: Usuario {} esta deshabilitado", request.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o password incorrecto");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            log.error("Login fallido: Contraseña incorrecta para el usuario {}", request.getEmail());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario o password incorrecto");
        }

        log.info("Login exitoso para el usuario: {} ({}) con rol {}",
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getRolUsuario());
            

        String token = jwtService.generarToken(
            usuario.getNombre(),
            usuario.getRolUsuario().name(),
            usuario.getEmail());
             
        return new DtoAuthResponse(token);
    }

    
}
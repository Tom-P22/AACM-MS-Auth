package cl.municipalidad.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cl.municipalidad.auth.dto.request.DtoAuthRequest;
import cl.municipalidad.auth.dto.response.DtoAuthResponse;
import cl.municipalidad.auth.model.UsuarioModel;
import cl.municipalidad.auth.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor

public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder; 

    @PostConstruct
public void initAdminUser() {
        java.util.Optional<UsuarioModel> existingAdmin = usuarioRepository.findByUsername("admin");

        if (existingAdmin.isPresent()) {
            UsuarioModel admin = existingAdmin.get();
            admin.setPassword(passwordEncoder.encode("123456")); 
            admin.setEnabled(true);
            usuarioRepository.save(admin);
            System.out.println("[Auth] Contraseña de 'admin' actualizada en la BD con BCrypt.");
        } else {
            UsuarioModel admin = new UsuarioModel();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            usuarioRepository.save(admin);
            System.out.println("[Auth] Usuario 'admin' creado desde cero con BCrypt.");
        }
    }
    public DtoAuthResponse login(DtoAuthRequest request) {

        UsuarioModel usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o password incorrecto"));

        if (!usuario.getEnabled()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario deshabilitado");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario o password incorrecto");
        }

        String token = jwtService.generarToken(usuario.getUsername(), usuario.getRole());
        return new DtoAuthResponse(token);
    }
}
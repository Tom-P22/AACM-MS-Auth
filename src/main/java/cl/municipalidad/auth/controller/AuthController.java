package cl.municipalidad.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.municipalidad.auth.dto.ErrorResponseDTO;
import cl.municipalidad.auth.dto.request.DtoAuthRequest;
import cl.municipalidad.auth.dto.response.DtoAuthResponse;
import cl.municipalidad.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Módulo de Seguridad y Autenticación", description = "Endpoints encargados de emitir credenciales válidas y firmadas para interactuar con la red perimetral de microservicios")

public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario y proveer Token Bearer", 
               description = "Verifica las credenciales enviadas consultando asincrónicamente el catálogo de ms-usuarios, corrobora el estado de activación de la cuenta y el hash de seguridad. Si es exitoso, retorna un JWT válido por 1 hora.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa. Credenciales válidas.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoAuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación. Los campos enviados no cumplen con el formato o están vacíos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Autenticación denegada. El usuario no existe, la contraseña es inválida o la cuenta fue suspendida.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<DtoAuthResponse> login(
        @Valid @RequestBody DtoAuthRequest request) {
        DtoAuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
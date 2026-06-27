package cl.municipalidad.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura de petición requerida para el inicio de sesión único del ecosistema municipal")
public class DtoAuthRequest {

    @NotBlank(message = "El email es obligatorio")
    @Schema(description = "Correo electrónico institucional o ciudadano del usuario registrado", 
            example = "admin.polideportivo@municipalidad.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "El password es obligatorio")
    @Schema(description = "Contraseña en texto plano asociada a la cuenta", 
            example = "MuniSecure2026!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}

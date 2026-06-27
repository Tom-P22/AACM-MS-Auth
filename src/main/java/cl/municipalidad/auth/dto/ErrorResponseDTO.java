package cl.municipalidad.auth.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Estructura unificada de respuesta ante excepciones o fallos del sistema")

public class ErrorResponseDTO {

    @Schema(description = "Marca de tiempo exacta de cuando ocurrió el evento", example = "2026-06-26T16:40:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP representativo", example = "401")
    private int status;

    @Schema(description = "Literal textual del error HTTP", example = "Unauthorized")
    private String error;

    @Schema(description = "Mensaje descriptivo con la causa específica del fallo", example = "Usuario o password incorrecto")
    private String message;
}

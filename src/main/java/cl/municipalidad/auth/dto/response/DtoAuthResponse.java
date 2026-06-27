package cl.municipalidad.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Contenedor del token criptográfico emitido tras una autenticación exitosa")

public class DtoAuthResponse {

    @Schema(description = "Token de acceso firmado por ms-auth en formato JWT (Json Web Token). Debe adjuntarse en las peticiones posteriores mediante la cabecera 'Authorization: Bearer <token>'", 
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWJqZWN0IjoiQWRtaW4iLCJyb2xlcyI6WyJST0xFX0FETUlOIl0sImVtYWlsIjoiYWRtaW5AcG9saWRlcG9ydGl2by5jbCJ9...")
    private String token;
}
package cl.municipalidad.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Servicio Central de Autenticidad e Identidad (ms-auth)")
                .version("1.0.0")
                .description("Microservicio perimetral encargado de la verificación de credenciales ciudadanas, cifrado BCrypt y emisión/firma criptográfica de Tokens JWT (HMAC256) para la Municipalidad.")
                .contact(new Contact()
                    .name("Departamento de Soporte TI - Municipalidad")
                    .email("soporte.ti@municipalidad.cl")));
    }
}
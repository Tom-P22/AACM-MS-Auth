package cl.municipalidad.auth.dto.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cl.municipalidad.auth.enums.RolUsuario;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Ignora los datos que no le sirvan para el login
public class UsuarioDto {
    private Long id;
    private String rut;
    private String password;
    private String email;
    private RolUsuario rolUsuario;
    private Boolean activo;
    private String nombre;
}
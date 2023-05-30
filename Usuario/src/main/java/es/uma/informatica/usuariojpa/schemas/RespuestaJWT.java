package es.uma.informatica.usuariojpa.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaJWT {
    private String jwt;
}

package es.uma.informatica.usuariojpa.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
    @JsonProperty("email")
    private String email;
}

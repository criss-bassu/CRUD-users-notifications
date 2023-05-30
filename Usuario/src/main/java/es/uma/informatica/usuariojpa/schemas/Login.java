package es.uma.informatica.usuariojpa.schemas;

import jdk.jfr.SettingDefinition;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Login {
    private String email;
    private String password;
}

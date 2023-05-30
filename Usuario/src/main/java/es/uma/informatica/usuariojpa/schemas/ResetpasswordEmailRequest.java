package es.uma.informatica.usuariojpa.schemas;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@JsonSerializableSchema
public class ResetpasswordEmailRequest {

    private String asunto;
    private String cuerpo;
    private String emailDestino;
    private String telefonoDestino;
    private Date programacionEnvio;
    private List<String> medios;
    private String tipoNotificacion;
}

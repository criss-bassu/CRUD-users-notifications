import {Medios} from './Medios'
import {TipoNotificacion} from './TipoNotificacion'

export interface NotificacionNueva {

  asunto:string;

  cuerpo:string;

  emailDestino:string;

  telefonoDestino:string;

  programacionEnvio:Date;

  medios:Medios[];

  tipoNotificacion:(TipoNotificacion|string);

}

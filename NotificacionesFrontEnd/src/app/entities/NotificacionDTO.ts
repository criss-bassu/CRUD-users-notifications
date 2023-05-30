import {Medios} from './Medios'
import {TipoNotificacion} from './TipoNotificacion'

class Estado {
}

export interface Notificacion {


  id:number;

  asunto:string;

  cuerpo:string;

  emailDestino:string;

  telefonoDestino:string;

  programacionEnvio:Date;

  medios:Medios[];

  tipoNotificacion:(TipoNotificacion|string);

}

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {environment} from "../environments/environment";
import {Observable} from "rxjs";
import {Notificacion} from "./entities/NotificacionDTO";
import {TipoNotificacion} from "./entities/TipoNotificacion";
import {NotificacionNueva} from "./entities/NotificacionNueva";

@Injectable({
  providedIn: 'root'
})
export class NotificacionesService {

    private apiServerUrl: string = environment.apiBaseUrl;

    private headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '.concat(environment.userAccesToken)
    });

    private  f = (x: (TipoNotificacion | null)): string => {
      let result: string = '';
      if (x === TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE) {
        result = 'ANUNCIO_AULA_ESTUDIANTE';
      } else if (x === TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE) {
        result = 'ANUNCIO_NOTA_ESTUDIANTE';
      } else if (x === TipoNotificacion.ANUNCIO_AULA_VIGILANTE) {
        result = 'ANUNCIO_AULA_VIGILANTE';
      } else if (x === TipoNotificacion.PASSWORD_RESET){
        result = 'PASSWORD_RESET';
      }
      return result;
    };


    constructor(private http: HttpClient) { }

    public addNotificacion(newNotificacion: NotificacionNueva): Observable<String> {
        return this.http.post<String>(
          this.apiServerUrl.concat("/notificaciones"),
          newNotificacion,
           {headers: this.headers}
        );
    }

    public getNotificacionById(id: number): Observable<Notificacion> {
      return this.http.get<Notificacion>(`${this.apiServerUrl}/notificaciones/${id}`, {headers: this.headers});
    }

    public getAllNotificaciones(): Observable<Notificacion[]> {
      return this.http.get<Notificacion[]>(this.apiServerUrl.concat("/notificaciones/all"), {headers: this.headers})
    }

    public updateNotificacionWithId(id: number, newNotificacion: Notificacion): Observable<String> {
      newNotificacion.id = id;

      return this.http.put<String>(`${this.apiServerUrl}/notificaciones/${id}`, newNotificacion, {headers: this.headers});
    }

    public updateNotificacion(newNotificacion: Notificacion) {
      return this.updateNotificacionWithId(newNotificacion.id, newNotificacion);
    }

    public deleteNotificacion(id: number): Observable<String>{
      return this.http.delete<String>(`${this.apiServerUrl}/notificaciones/${id}`, {headers: this.headers});
    }

    public abortaNotificacion(tipoNotificacion: (TipoNotificacion | null)): Observable<HttpResponse<void>>{


        return this.http.post<HttpResponse<void>>(`${this.apiServerUrl}/pendientes/abortar?tipo=${this.f(tipoNotificacion)}`, undefined, {headers: this.headers});
    }
}

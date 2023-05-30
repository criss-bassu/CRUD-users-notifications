import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../environments/environment';
import { NotificacionesService } from './notificaciones.service';
import {NotificacionNueva} from "./entities/NotificacionNueva";
import {TipoNotificacion} from "./entities/TipoNotificacion";
import {Medios} from "./entities/Medios";
import {Notificacion} from "./entities/NotificacionDTO";

describe('NotificacionesService', () => {
  let service: NotificacionesService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [NotificacionesService]
    });
    service = TestBed.inject(NotificacionesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('se crea el servicio', () => {
    expect(service).toBeTruthy();
  });

  it('crea una notificación', () => {
    const newNotificacion: NotificacionNueva = {asunto:'Primera notificación', cuerpo:'Este es el cuerpo de la primera notificación', emailDestino:'cbs@uma.es', telefonoDestino:'606102030', programacionEnvio:new Date('2023-05-16'), medios:[], tipoNotificacion:TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE};
    service.addNotificacion(newNotificacion).subscribe(response => {
      expect(response).toBeDefined();
    });
    const req = httpMock.expectOne(`${environment.apiBaseUrl}/notificaciones`);
    expect(req.request.method).toBe('POST');
    req.flush(newNotificacion);
  });

  it('obtiene una notificación por su ID', () => {
    const id = 1;
    const dummyNotificacion: Notificacion = {
      id: 1,
      asunto: 'Primera notificación',
      cuerpo: 'Este es el cuerpo de la primera notificación',
      emailDestino: 'cbs@uma.es',
      telefonoDestino: '606102030',
      programacionEnvio: new Date('2023-05-16'),
      medios: [Medios.SMS],
      tipoNotificacion: TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE
    };

    service.getNotificacionById(id).subscribe(response => {
      expect(response).toEqual(dummyNotificacion);
    });

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/notificaciones/${id}`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyNotificacion);
  });


  it('obtiene todas las notificaciones', () => {
    const dummyNotificaciones: Notificacion[] = [
      {id:1, asunto:'Primera notificación', cuerpo:'Este es el cuerpo de la primera notificación', emailDestino:'cbs@uma.es', telefonoDestino:'606102030', programacionEnvio:new Date('2023-05-16') , medios:[Medios.SMS], tipoNotificacion:TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE},
      {id:2, asunto:'Segunda notificación', cuerpo:'Este es el cuerpo de la segunda notificación', emailDestino:'rrs@uma.es', telefonoDestino:'606102030', programacionEnvio:new Date('2023-05-17') , medios:[Medios.EMAIL], tipoNotificacion:TipoNotificacion.PASSWORD_RESET}
    ];
    service.getAllNotificaciones().subscribe(response => {
      expect(response.length).toBe(dummyNotificaciones.length);
      expect(response).toEqual(dummyNotificaciones);
    });
    const req = httpMock.expectOne(`${environment.apiBaseUrl}/notificaciones/all`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyNotificaciones);
  });

  it('edita una notificación mediante su ID', () => {
    const id = 1;
    const dummyNotificacion: Notificacion = {
      id: 1,
      asunto: 'Primera notificación',
      cuerpo: 'Este es el cuerpo de la primera notificación',
      emailDestino: 'cbs@uma.es',
      telefonoDestino: '606102030',
      programacionEnvio: new Date('2023-05-16'),
      medios: [Medios.SMS],
      tipoNotificacion: TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE
    };

    service.updateNotificacionWithId(id, dummyNotificacion).subscribe(response => {
      expect(response).toBeDefined();
    });

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/notificaciones/${id}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(dummyNotificacion); // Verificar el cuerpo de la solicitud
    req.flush(dummyNotificacion);
  });


  it('borra una notificacion', () => {
    const mockId = 1;
    service.deleteNotificacion(mockId).subscribe(response => {
      expect(response).toEqual('Deleted successfully');
    });
    const req = httpMock.expectOne(`${environment.apiBaseUrl}/notificaciones/${mockId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush('Deleted successfully');
  });

  afterEach(() => {
    httpMock.verify();
  });

});

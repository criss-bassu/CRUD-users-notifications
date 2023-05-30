import {FormsModule} from '@angular/forms';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {NotificacionesService} from './notificaciones.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {of, throwError} from 'rxjs';
import {Notificacion} from './entities/NotificacionDTO';
import {HttpErrorResponse} from "@angular/common/http";
import {Medios} from "./entities/Medios";
import {TipoNotificacion} from "./entities/TipoNotificacion";
import { NgForm } from '@angular/forms';


describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let service: NotificacionesService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AppComponent ],
      imports: [ HttpClientTestingModule, FormsModule ],
      providers: [ NotificacionesService ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(NotificacionesService);
    fixture.detectChanges();
  });

  it('crea el componente', () => {
    expect(component).toBeTruthy();
  });

  it('llama a getAllNotificaciones() con ngOnInit', () => {
    const notificaciones: Notificacion[] = [
      {id:1, asunto:'Primera notificación', cuerpo:'Este es el cuerpo de la primera notificación', emailDestino:'cbs@uma.es', telefonoDestino:'606102030', programacionEnvio:new Date('2023-05-16') , medios:[Medios.SMS], tipoNotificacion:TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE},
      {id:2, asunto:'Segunda notificación', cuerpo:'Este es el cuerpo de la segunda notificación', emailDestino:'rrs@uma.es', telefonoDestino:'606102030', programacionEnvio:new Date('2023-05-17') , medios:[Medios.EMAIL], tipoNotificacion:TipoNotificacion.PASSWORD_RESET}
    ];
    spyOn(service, 'getAllNotificaciones').and.returnValue(of(notificaciones));
    component.ngOnInit();
    expect(service.getAllNotificaciones).toHaveBeenCalled();
    expect(component.notificaciones).toEqual(notificaciones);
  });

  it('maneja errores de NotificacionesService on init', () => {
    const errorResponse = new HttpErrorResponse({
      error: 'error message',
      status: 500,
      statusText: 'Internal Server Error'
    });
    spyOn(window, 'alert');
    spyOn(service, 'getAllNotificaciones').and.returnValue(throwError(errorResponse));
    component.ngOnInit();
    expect(window.alert).toHaveBeenCalledWith(errorResponse.message);
  });


/*
  it('Crea la noficiación correctamente', () => {
    const dummyNotificacion = {
      id: 1,
      asunto: 'Notificación de prueba',
      cuerpo: 'Cuerpo de prueba',
      emailDestino: 'juan@uma.es',
      telefonoDestino: '1223334444',
      programacionEnvio: new Date(),
      medios: [Medios.SMS],
      tipoNotificacion: 'ANUNCIO_NOTA_ESTUDIANTE'
    };

    // Fuerzo a "esperar" cuando la vista y el formulario esté disponible
    fixture.whenStable().then(() => {
      //Obtenemos el formulario
      const addForm: NgForm = component.addForm!;
      //Los valores de la nueva noti
      addForm.setValue(dummyNotificacion);

      component.onAddOrEditNotificacion(addForm);

      //Comprobamos que se haya creado bien
      expect(component.notificaciones.length).toBe(1);
      expect(component.notificaciones[0]).toEqual(dummyNotificacion);
    });
  });*/

  it('borra la notificacion cuando la llama onDeleteNotificacion', () => {
    const response = 'Notification Deleted';
    const notifId = 1;
    spyOn(service, 'deleteNotificacion').and.returnValue(of(response));
    spyOn(component, 'onReadNotificacion');
    component.onDeleteNotificacion(notifId);
    expect(service.deleteNotificacion).toHaveBeenCalledWith(notifId);
    expect(component.onReadNotificacion).toHaveBeenCalled();
  });

  it('maneja errores cuando deleteNotificacion falla', () => {
    const notifId = 1;
    const error = new HttpErrorResponse({ error: 'Test error' });
    spyOn(service, 'deleteNotificacion').and.returnValue(throwError(error));
    spyOn(window, 'alert');
    component.onDeleteNotificacion(notifId);
    expect(window.alert).toHaveBeenCalledWith(error.message);
  });


  /*it('Actualiza una notificación', () => {
    const notificacion: Notificacion = {
      id: 1,
      asunto: 'Asunto notificación',
      cuerpo: 'Cuerpo de la notificación existente',
      emailDestino: 'juan@example.com',
      telefonoDestino: '123456789',
      programacionEnvio: new Date(),
      medios: [Medios.SMS],
      tipoNotificacion: TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE
    };
    component.notificaciones.push(notificacion);

    const updatedNotificacion: Notificacion = {
      id: 1,
      asunto: 'Notificación actualizada',
      cuerpo: 'Cuerpo de la notificación actualizada',
      emailDestino: 'correo@example.com',
      telefonoDestino: '123456789',
      programacionEnvio: new Date(),
      medios: [Medios.EMAIL],
      tipoNotificacion: TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE
    };

    spyOn(service, 'updateNotificacionWithId').and.returnValue(of('Notificación actualizada'));

    component.editingNotifId = 1;
    component.newNotificacion = updatedNotificacion;

    const form = jasmine.createSpyObj('form', ['reset']);

    component.onAddOrEditNotificacion(form);

    expect(service.updateNotificacionWithId).toHaveBeenCalledWith(1, updatedNotificacion);
    expect(component.notificaciones).toContain(updatedNotificacion);
    expect(component.editingNotifId).toBeNull();
    expect(component.newNotificacion).toEqual(component.emptyNewNotif());
    expect(form.reset).toHaveBeenCalled();
  }); */


});

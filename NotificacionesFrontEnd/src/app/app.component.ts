import {Component, OnInit, ViewChild} from '@angular/core';
import {NotificacionNueva} from './entities/NotificacionNueva';
import {Notificacion} from './entities/NotificacionDTO';
import {NgForm} from '@angular/forms';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Medios} from './entities/Medios';
import {TipoNotificacion} from './entities/TipoNotificacion';
import {NotificacionesService} from './notificaciones.service';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    @ViewChild('addnotificacionform')
    addNotificacionModal!: NgbModalRef;
    @ViewChild('addForm') addForm: NgForm | undefined;

    public tipoSeleccionado: (TipoNotificacion | null) = null;

    public tipoNotificaciones = [TipoNotificacion.ANUNCIO_NOTA_ESTUDIANTE, TipoNotificacion.ANUNCIO_AULA_ESTUDIANTE,  TipoNotificacion.ANUNCIO_AULA_VIGILANTE, TipoNotificacion.PASSWORD_RESET];

  private NotificacionNuevaToNotificacion(notificacionNueva: NotificacionNueva, id: number = 0): Notificacion {
    return {
      id: id,
      asunto: notificacionNueva.asunto,
      cuerpo: notificacionNueva.cuerpo,
      emailDestino: notificacionNueva.emailDestino,
      telefonoDestino: notificacionNueva.telefonoDestino,
      programacionEnvio: notificacionNueva.programacionEnvio,
      medios: notificacionNueva.medios,
      tipoNotificacion: notificacionNueva.tipoNotificacion
    };
  }

  private NotificacionToNotificacionNueva(notificacion: Notificacion): NotificacionNueva {
    return {
      asunto: notificacion.asunto,
      cuerpo: notificacion.cuerpo,
      emailDestino: notificacion.emailDestino,
      telefonoDestino: notificacion.telefonoDestino,
      programacionEnvio: notificacion.programacionEnvio,
      medios: notificacion.medios,
      tipoNotificacion: notificacion.tipoNotificacion
    };
  }

  public borrarNotificacion: Notificacion| undefined;
    public newNotificacion: NotificacionNueva=this.emptyNewNotif();
    public notificaciones: Notificacion[] = [];
    public editNotif: Notificacion=this.emptyNotif();

    constructor(private notifService: NotificacionesService ) {}

    ngOnInit(): void{
        this.onReadNotificacion();
    }

    public onReadNotificacion():void{
        this.notifService.getAllNotificaciones().subscribe((response: Notificacion[]) => {
            this.notificaciones = response;
        }, (error: HttpErrorResponse) => {
            alert(error.message)
        })
    }

    public editingNotifId:number | null = null;

  public onAddOrEditNotificacion(form: NgForm): void {
    document.getElementById('add-notificacion-form')!.click();
    if (this.editingNotifId === null) {
      this.notifService.addNotificacion(this.newNotificacion).subscribe(
        (response: String) => {
          console.log(response);
          this.onReadNotificacion();
          this.newNotificacion = this.emptyNewNotif();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
      if (this.addForm) {
        this.addForm.resetForm();
      }
    } else {
      this.notifService.updateNotificacionWithId(this.editingNotifId, this.NotificacionNuevaToNotificacion(this.newNotificacion, this.editingNotifId)).subscribe(
        (response: String) => {
          console.log('Response: ', response);
          this.onReadNotificacion();
          this.editingNotifId = null;
          this.newNotificacion = this.emptyNewNotif();
        },
        (error: HttpErrorResponse) => {
          console.error('Error: ', error);
        }
      );
    }
    form.reset();
  }

  public onUpdateNotificacion(notificacion: Notificacion): void {
    this.editingNotifId = notificacion.id;
    this.newNotificacion = this.NotificacionToNotificacionNueva(notificacion);
  }

  public onDeleteNotificacion(notificacionId: number): void {
        this.notifService.deleteNotificacion(notificacionId).subscribe(
            (response: String) => {
                console.log(response);
                this.onReadNotificacion();
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public onAbortarNotificacion() {
        this.notifService.abortaNotificacion(this.tipoSeleccionado).subscribe(
            (response: HttpResponse<void>) => {
                alert("Notificación abortada con exito")
                this.onReadNotificacion();
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );

        this.tipoSeleccionado = null;

        this.onReadNotificacion();
    }


    private nextId: number = -1;

    private emptyNotif(): Notificacion {
        return {
            id: this.nextId++,
            asunto: '',
            cuerpo: '',
            emailDestino: '',
            telefonoDestino: '',
            programacionEnvio: new Date(),
            tipoNotificacion: '',
            medios:[]
        };
    }


    public selectedMedios = {
        SMS: false,
        EMAIL: false,
    };

    public emptyNewNotif(): NotificacionNueva {
        return {
            asunto: "",
            cuerpo: "",
            emailDestino: "",
            medios: [],
            programacionEnvio: new Date(),
            telefonoDestino: "",
            tipoNotificacion: ""
        };
    }

    public agregarMedio(medio: Medios) {
        if (!this.newNotificacion.medios.includes(medio)) {
            this.newNotificacion.medios.push(medio);
        } else {
            this.newNotificacion.medios = this.newNotificacion.medios.filter((m) => m !== medio);
        }
    }

    public medioCaster(medio: String): Medios {
      return Medios[medio as keyof typeof Medios]
    }

    protected readonly Medio = Medios;
    protected readonly TipoNotificacion = TipoNotificacion;

    public agregarTipo(tipo: TipoNotificacion) {
        this.newNotificacion.tipoNotificacion = tipo;
    }

    public getMedio(notif: Notificacion): string {
        /*var res = 'SMS';

        if (notif?.medio?.length == 2) {
          res = 'SMS EMAIL';
        } else if (notif?.medio?.pop() === Medio.SMS) {
          res = 'SMS';
        } else {
          res = 'EMAIL';
        }
        return res;*/

        var aux: Medios[] = notif?.medios;
        var res: string = 'SMS';
        if (aux?.length == 2) {
          res = 'SMS y EMAIL';
        } else if (aux?.pop() === Medios.EMAIL) {
          res = 'EMAIL';
        }

        return res;
    }
}



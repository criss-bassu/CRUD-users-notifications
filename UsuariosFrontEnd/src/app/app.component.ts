import {Component, NgModule, OnInit, ViewChild} from '@angular/core';
import { Usuario } from './usuario';
import {FormsModule, NgForm} from '@angular/forms';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {UsuarioService} from "./usuario.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title(title: any) {
      throw new Error('Method not implemented.');
  }
  @ViewChild('userFormModal')
  addUserModal!: NgbModalRef;

  ngOnInit() {
    this.getUsuarios();
  }

  constructor(private usuarioService: UsuarioService) {}

  public editingUserId: number | null = null;
  public usuarios: Usuario[] = [];

  private nextId: number = -1;  // 0

  private emptyUser(): Usuario {
    return {
      id: this.nextId++,
      nombre: '',
      apellido1: '',
      apellido2: '',
      email: ''
    };
  }

  public newUser: Usuario = this.emptyUser();

  public getUsuarios():void {
    this.usuarioService.getAllUsers().subscribe(
      (response: Usuario[]) => {
        this.usuarios = response
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
  }

  public onEditUser(user: Usuario): void {
    this.editingUserId = user.id;
    this.newUser = {...user};
  }

  public onAddUser(form: NgForm): void {
    document.getElementById('user-form-modal')!.click();
    if (this.editingUserId === null) {
      this.usuarioService.addUser(this.newUser).subscribe(
        (response: Usuario) => {
          console.log(response);
          this.getUsuarios();
          this.newUser = this.emptyUser();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
    } else {
      this.usuarioService.updateUserById(this.editingUserId, this.newUser).subscribe(
        (response: Usuario) => {
          console.log(response);
          this.getUsuarios();
          this.editingUserId = null;
          this.newUser = this.emptyUser();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
    }
    form.reset();
  }

  onDeleteUser(deletedUser: Usuario) {
      this.usuarioService.deleteUser(deletedUser.id).subscribe(
        (response: HttpResponse<void>) => {
          this.getUsuarios();
          // alert("Usuario eliminado con exito");
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
      this.getUsuarios();
  }
}

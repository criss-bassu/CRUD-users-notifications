import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AppComponent } from './app.component';
import { UsuarioService } from './usuario.service';
import { of } from 'rxjs';
import { Usuario } from './usuario';
import { FormsModule } from '@angular/forms';
import {HttpResponse} from "@angular/common/http";

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let usuarioService: UsuarioService;
  let spy: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AppComponent ],
      imports: [ HttpClientTestingModule, FormsModule ],
      providers: [ UsuarioService ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    usuarioService = TestBed.inject(UsuarioService);
    fixture.detectChanges();
  });

  it('crea el componente', () => {
    expect(component).toBeTruthy();
  });

  it('llama a getUsuarios() con ngOnInit', () => {
    const getUsuariosSpy = spyOn(component, 'getUsuarios');
    component.ngOnInit();
    expect(getUsuariosSpy).toHaveBeenCalled();
  });

  it('obtiene todos los usuarios', () => {
    const dummyUsers: Usuario[] = [
      { id: 1, nombre: 'anonimous', apellido1: '', apellido2: '', email: 'anonimous@anonimous.com' },
      { id: 2, nombre: 'Cristina', apellido1: 'Barón', apellido2: 'Suárez', email: 'cbaronsuarez@uma.es' },
      { id: 3, nombre: 'Alejandro', apellido1: 'Téllez', apellido2: 'Montiel', email: 'atm@gmail.com' }
    ];

    spyOn(usuarioService, 'getAllUsers').and.returnValue(of(dummyUsers));
    component.getUsuarios();
    expect(component.usuarios.length).toBe(3);
  });

  it('crea un nuevo usuario', () => {
    const user: Usuario = {
      id: 2,
      nombre: 'Cristina',
      apellido1: 'Barón',
      apellido2: 'Suárez',
      email: 'cbaronsuarez@uma.es'
    };
    spyOn(usuarioService, 'addUser').and.returnValue(of(user));
    component.newUser = user;
    component.onAddUser({reset: () => {}} as any);
    expect(usuarioService.addUser).toHaveBeenCalledWith(user);
  });

  it('edita un usuario existente', () => {
    const user: Usuario = {
      id: 2,
      nombre: 'Julia',
      apellido1: 'Barón',
      apellido2: 'Suárez',
      email: 'jbs@gmail.com'
    };
    spyOn(usuarioService, 'updateUserById').and.returnValue(of(user));
    component.newUser = user;
    component.editingUserId = user.id;
    component.onAddUser({reset: () => {}} as any);
    expect(usuarioService.updateUserById).toHaveBeenCalledWith(user.id, user);
  });

  it('borra un usuario', () => {
    const testUser = {id: 2, nombre: 'Cristina', apellido1: 'Barón', apellido2: 'Suárez', email: 'cbaronsuarez@uma.es'};
    spy = spyOn(usuarioService, 'deleteUser').and.returnValue(of(new HttpResponse<void>()));
    component.onDeleteUser(testUser);
    expect(usuarioService.deleteUser).toHaveBeenCalledWith(testUser.id);
  });

});

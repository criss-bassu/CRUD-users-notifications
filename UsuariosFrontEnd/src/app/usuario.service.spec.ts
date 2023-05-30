import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UsuarioService } from './usuario.service';
import { Usuario } from './usuario';
import { environment } from './../environments/environment';
import {AppComponent} from "./app.component";

describe('UsuarioService', () => {
  let service: UsuarioService;
  let httpMock: HttpTestingController;
  let component: AppComponent;
  let spy: any;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UsuarioService],
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(UsuarioService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('recupera todos los usuarios', () => {
    const dummyUsers: Usuario[] = [
      { id: 1, nombre: 'anonimous', apellido1: '', apellido2: '', email: 'anonimous@anonimous.com' },
      { id: 2, nombre: 'Cristina', apellido1: 'Barón', apellido2: 'Suárez', email: 'cbaronsuarez@uma.es' },
      { id: 3, nombre: 'Alejandro', apellido1: 'Téllez', apellido2: 'Montiel', email: 'atm@gmail.com' }
    ];

    service.getAllUsers().subscribe(users => {
      expect(users.length).toBe(3);
      expect(users).toEqual(dummyUsers);
    });

    const request = httpMock.expectOne(`${environment.apiBaseUrl}/usuarios`);
    expect(request.request.method).toBe('GET');
    request.flush(dummyUsers);
  });

  it('crea un usuario', () => {
    const mockUser: Usuario = {id: 4, nombre: 'Samantha', apellido1: 'Thomas', apellido2: '', email: 'nuevo@gmail.com'};

    service.addUser(mockUser).subscribe(user => {
      expect(user.id).toBe(4);
    });

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/usuarios`);
    expect(req.request.method).toBe('POST');
    req.flush(mockUser);
  });


  it('edita un usuario (mediante su ID)', () => {
    const mockUser: Usuario = {id: 2, nombre: 'Julia', apellido1: 'Barón', apellido2: 'Suárez', email: 'jbs@uma.es'};

    service.updateUserById(2, mockUser).subscribe(user => {
      expect(user.id).toBe(2);
    });

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/usuarios/2`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockUser);
  });

  it('devuelve un usuario por su ID', () => {
    const mockUser: Usuario = {id: 2, nombre: 'Cristina', apellido1: 'Barón', apellido2: 'Suárez', email: 'cbaronsuarez@uma.es'};
    service.getUserById(2).subscribe(user => {
      expect(user).toEqual(mockUser);
    });
    const request = httpMock.expectOne(`${environment.apiBaseUrl}/usuarios/2`);
    expect(request.request.method).toBe('GET');
    request.flush(mockUser);
  });

  it('borra un usuario', () => {
    const mockUserId = 2;
    service.deleteUser(mockUserId).subscribe((response) => {
      expect(response).toBeNull();
    });
    const req = httpMock.expectOne(`${environment.apiBaseUrl}/usuarios/${mockUserId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

});

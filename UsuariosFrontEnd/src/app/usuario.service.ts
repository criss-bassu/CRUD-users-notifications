import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import { environment } from './../environments/environment';
import { Usuario } from './usuario';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private apiServerUrl: string = environment.apiBaseUrl;

  private headers: HttpHeaders = new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': 'Bearer '.concat(environment.userAccesToken)
  });

  constructor(private http: HttpClient) {
  }

  public addUser(newUser: Usuario): Observable<Usuario>{

    console.log(newUser.nombre.toString())

    let body = {
      nombre: newUser.nombre.toString(),
      apellido1: newUser.apellido1.toString(),
      apellido2: newUser.apellido2.toString(),
      email: newUser.email.toString()
    };


    console.log(body);

    return this.http.post<Usuario>(
      this.apiServerUrl.concat("/usuarios"),
      body,
      {headers: this.headers});
  }


  public getAllUsers(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiServerUrl.concat("/usuarios"), {headers: this.headers});
  }


  public getUserById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiServerUrl}/usuarios/${id}`, {headers: this.headers});
  }

  public updateUserById(id: number, newUser: Usuario): Observable<Usuario>{
    console.log(newUser.nombre.toString())
    let body = {
      nombre: newUser.nombre.toString(),
      apellido1: newUser.apellido1.toString(),
      apellido2: newUser.apellido2.toString(),
      email: newUser.email.toString()
    };
    console.log(body);
    return this.http.put<Usuario>(`${this.apiServerUrl}/usuarios/${id}`,
      body,
      {headers: this.headers});
  }


  public deleteUser(id: number): Observable<HttpResponse<void>> {
    return this.http.delete<HttpResponse<void>>(this.apiServerUrl.concat(`/usuarios/${id}`), {headers: this.headers});
  }

}

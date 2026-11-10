import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GymApiService {

  private readonly api = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  private headers(): HttpHeaders {
    const token = localStorage.getItem('token');
    return token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : new HttpHeaders();
  }

  // AUTENTICACIÓN
  login(datos: { correo: string; contrasena: string }): Observable<any> {
    return this.http.post(`${this.api}/auth/login`, datos);
  }

  registrar(datos: any): Observable<any> {
    return this.http.post(`${this.api}/auth/registro`, datos);
  }

  // MEMBRESÍAS
  listarMembresias(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/membresias`);
  }

  crearMembresia(datos: any): Observable<any> {
    return this.http.post(`${this.api}/membresias`, datos);
  }

  actualizarMembresia(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.api}/membresias/${id}`, datos);
  }

  // PRODUCTOS
  listarProductos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/productos`);
  }

  crearProducto(datos: any): Observable<any> {
    return this.http.post(`${this.api}/productos`, datos, {
      headers: this.headers()
    });
  }

  actualizarProducto(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.api}/productos/${id}`, datos, {
      headers: this.headers()
    });
  }

  // RUTINAS
  listarRutinas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/rutinas`);
  }

  crearRutina(datos: any): Observable<any> {
    return this.http.post(`${this.api}/rutinas`, datos);
  }

  // USUARIOS
  listarUsuarios(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/usuarios`, {
      headers: this.headers()
    });
  }

  actualizarUsuario(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.api}/usuarios/${id}`, datos, {
      headers: this.headers()
    });
  }
}

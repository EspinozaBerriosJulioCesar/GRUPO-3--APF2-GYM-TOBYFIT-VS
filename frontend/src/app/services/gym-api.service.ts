import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GymApiService {

  private readonly api = 'http://localhost:8081/api';

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
    return this.http.get<any[]>(`${this.api}/membresias`, { headers: this.headers() });
  }

  crearMembresia(datos: any): Observable<any> {
    return this.http.post(`${this.api}/membresias`, datos, { headers: this.headers() });
  }

  actualizarMembresia(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.api}/membresias/${id}`, datos, { headers: this.headers() });
  }

  eliminarMembresia(id: number): Observable<any> {
    return this.http.delete(`${this.api}/membresias/${id}`, { headers: this.headers() });
  }

  // PRODUCTOS
  listarProductos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/productos`, { headers: this.headers() });
  }

  crearProducto(datos: any): Observable<any> {
    return this.http.post(`${this.api}/productos`, datos, { headers: this.headers() });
  }

  actualizarProducto(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.api}/productos/${id}`, datos, { headers: this.headers() });
  }

  eliminarProducto(id: number): Observable<any> {
    return this.http.delete(`${this.api}/productos/${id}`, { headers: this.headers() });
  }

  // RUTINAS
  listarRutinas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/rutinas`, { headers: this.headers() });
  }

  crearRutina(datos: any): Observable<any> {
    return this.http.post(`${this.api}/rutinas`, datos, { headers: this.headers() });
  }

  actualizarRutina(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.api}/rutinas/${id}`, datos, { headers: this.headers() });
  }

  eliminarRutina(id: number): Observable<any> {
    return this.http.delete(`${this.api}/rutinas/${id}`, { headers: this.headers() });
  }

  // PROMOCIONES
  listarPromociones(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/promociones`, { headers: this.headers() });
  }

  crearPromocion(datos: any): Observable<any> {
    return this.http.post(`${this.api}/promociones`, datos, { headers: this.headers() });
  }

  actualizarPromocion(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.api}/promociones/${id}`, datos, { headers: this.headers() });
  }

  eliminarPromocion(id: number): Observable<any> {
    return this.http.delete(`${this.api}/promociones/${id}`, { headers: this.headers() });
  }

  // USUARIOS
  listarUsuarios(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/usuarios`, { headers: this.headers() });
  }

  actualizarUsuario(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.api}/usuarios/${id}`, datos, { headers: this.headers() });
  }

  eliminarUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.api}/usuarios/${id}`, { headers: this.headers() });
  }
}

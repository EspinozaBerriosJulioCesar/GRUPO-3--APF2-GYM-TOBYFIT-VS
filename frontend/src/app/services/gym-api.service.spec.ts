import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { GymApiService } from './gym-api.service';

const cases: [string, string, string, any[]][] = [
  ['login', 'POST', '/auth/login', [{ correo: 'demo@test.local', contrasena: 'Prueba123!' }]],
  ['registrar', 'POST', '/auth/registro', [{ nombre: 'Prueba' }]],
  ['listarUsuarios', 'GET', '/usuarios', []],
  ['actualizarUsuario', 'PUT', '/usuarios/7', [7, { nombre: 'Editado' }]],
  ['eliminarUsuario', 'DELETE', '/usuarios/7', [7]]
];
for (const [singular, plural] of [['Producto','productos'],['Membresia','membresias'],['Rutina','rutinas'],['Promocion','promociones']]) {
  const suffix = plural[0].toUpperCase() + plural.slice(1);
  cases.push(['listar' + suffix, 'GET', '/' + plural, []]);
  cases.push(['crear' + singular, 'POST', '/' + plural, [{ nombre: 'Prueba' }]]);
  cases.push(['actualizar' + singular, 'PUT', '/' + plural + '/7', [7, { nombre: 'Editado' }]]);
  cases.push(['eliminar' + singular, 'DELETE', '/' + plural + '/7', [7]]);
}

describe('GymApiService: 21 operaciones invocadas por Angular', () => {
  let api: any;
  let http: HttpTestingController;
  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    api = TestBed.inject(GymApiService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => { http.verify(); localStorage.clear(); });

  it.each(cases)('%s usa el método HTTP y ruta correctos', (method, verb, path, args) => {
    localStorage.setItem('token', 'token-de-prueba');
    api[method](...args).subscribe();
    const req = http.expectOne('http://localhost:8081/api' + path);
    expect(req.request.method).toBe(verb);
    expect(req.request.headers.get('Authorization')).toBe(path.startsWith('/auth/') ? null : 'Bearer token-de-prueba');
    if (verb === 'POST' || verb === 'PUT') expect(req.request.body).toEqual(args[args.length - 1]);
    req.flush(verb === 'GET' ? [] : {});
  });

  it('consulta el catálogo público sin Authorization cuando no hay sesión', () => {
    api.listarProductos().subscribe();
    const req = http.expectOne('http://localhost:8081/api/productos');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush([]);
  });
});

import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { App } from './app';
import { GymApiService } from './services/gym-api.service';

function token(): string {
  return 'header.' + btoa(JSON.stringify({ exp: Math.floor(Date.now() / 1000) + 3600 })) + '.signature';
}

describe('TobyFit: formularios y permisos', () => {
  let api: any;
  beforeEach(async () => {
    localStorage.clear();
    api = {};
    for (const domain of ['Membresias', 'Productos', 'Rutinas', 'Promociones', 'Usuarios']) api['listar' + domain] = vi.fn(() => of([]));
    for (const domain of ['Membresia', 'Producto', 'Rutina', 'Promocion', 'Usuario']) {
      for (const action of ['crear', 'actualizar', 'eliminar']) api[action + domain] = vi.fn(() => of({}));
    }
    api.login = vi.fn(() => of({ token: token(), rol: 'ADMIN' }));
    api.registrar = vi.fn(() => of({ mensaje: 'Registrado' }));
    await TestBed.configureTestingModule({
      imports: [App], providers: [{ provide: GymApiService, useValue: api }]
    }).compileComponents();
  });
  afterEach(() => localStorage.clear());

  function admin() {
    localStorage.setItem('token', token());
    localStorage.setItem('rol', 'ADMIN');
  }

  it('renderiza el panel real y consulta los cuatro catálogos', async () => {
    const f = TestBed.createComponent(App);
    await f.whenStable();
    expect(f.nativeElement.querySelector('h1').textContent).toContain('Panel principal');
    for (const domain of ['Membresias', 'Productos', 'Rutinas', 'Promociones']) expect(api['listar' + domain]).toHaveBeenCalled();
  });

  it('el invitado consulta productos sin controles administrativos', async () => {
    const f = TestBed.createComponent(App);
    f.componentInstance.mostrar('productos');
    await f.whenStable();
    expect(f.nativeElement.querySelector('form')).toBeNull();
    expect(f.nativeElement.querySelector('.edit-button')).toBeNull();
    expect(f.componentInstance.rol).toBe('INVITADO');
  });

  it('ADMIN inicia sesión y carga usuarios', () => {
    const app = TestBed.createComponent(App).componentInstance;
    app.iniciarSesion();
    expect(app.esAdmin).toBe(true);
    expect(api.listarUsuarios).toHaveBeenCalledOnce();
    expect(app.loginForm.contrasena).toBe('');
  });

  it('USUARIO inicia sesión sin solicitar la lista administrativa', () => {
    api.login.mockReturnValue(of({ token: token(), rol: 'USUARIO' }));
    const app = TestBed.createComponent(App).componentInstance;
    app.iniciarSesion();
    expect(app.rol).toBe('USUARIO');
    expect(api.listarUsuarios).not.toHaveBeenCalled();
    app.mostrar('usuarios');
    expect(app.vista).toBe('inicio');
    expect(app.error).toContain('administrador');
  });

  it('cerrar sesión elimina permisos y datos de usuarios', () => {
    admin();
    const app = TestBed.createComponent(App).componentInstance;
    app.usuarios = [{ nombre: 'Dato privado' }];
    app.cerrarSesion();
    expect(app.usuarios).toEqual([]);
    expect(app.esAdmin).toBe(false);
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('un token vencido no habilita la interfaz administrativa', () => {
    localStorage.setItem('token', 'x.' + btoa(JSON.stringify({ exp: 1 })) + '.x');
    localStorage.setItem('rol', 'ADMIN');
    expect(TestBed.createComponent(App).componentInstance.esAdmin).toBe(false);
  });

  it.each(['login', 'registro', 'usuarios', 'productos', 'membresias', 'rutinas', 'promociones'])(
    'el formulario %s se renderiza y bloquea el envío incompleto', async (vista) => {
      admin();
      const f = TestBed.createComponent(App);
      f.componentInstance.mostrar(vista);
      await f.whenStable();
      f.detectChanges();
      expect(f.nativeElement.querySelector('form')).not.toBeNull();
      expect(f.nativeElement.querySelector('form .primary-button').disabled).toBe(true);
    }
  );

  it.each([
    ['Producto', 'productoForm', 'guardarProducto'],
    ['Membresia', 'membresiaForm', 'guardarMembresia'],
    ['Rutina', 'rutinaForm', 'guardarRutina'],
    ['Promocion', 'promocionForm', 'guardarPromocion']
  ])('el formulario %s distingue creación y edición', (domain, form, action) => {
    admin();
    const app: any = TestBed.createComponent(App).componentInstance;
    app[form] = { ...app[form], id: null, nombre: 'Prueba' };
    app[action]();
    expect(api['crear' + domain]).toHaveBeenCalled();
    app[form] = { ...app[form], id: 7, nombre: 'Editado' };
    app[action]();
    expect(api['actualizar' + domain]).toHaveBeenCalledWith(7, expect.objectContaining({ nombre: 'Editado' }));
  });

  it('presenta el rechazo de autenticación', () => {
    api.login.mockReturnValue(throwError(() => ({ error: { error: 'Credenciales inválidas' } })));
    const app = TestBed.createComponent(App).componentInstance;
    app.iniciarSesion();
    expect(app.error).toBe('Credenciales inválidas');
    expect(app.sesionActiva).toBe(false);
  });

  it('rechaza una promoción con fechas invertidas', () => {
    const app = TestBed.createComponent(App).componentInstance;
    app.promocionForm.inicio = '2026-11-20';
    app.promocionForm.fin = '2026-11-19';
    app.guardarPromocion();
    expect(api.crearPromocion).not.toHaveBeenCalled();
    expect(app.error).toContain('fecha final');
  });
});

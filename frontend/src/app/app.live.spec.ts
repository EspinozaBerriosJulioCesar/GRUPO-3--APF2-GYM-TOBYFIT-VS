import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpInterceptorFn, HttpResponse, provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { tap } from 'rxjs';
import { afterAll, beforeAll, beforeEach, describe, expect, it, vi } from 'vitest';
import { mkdirSync, readFileSync, writeFileSync } from 'node:fs';
import { resolve } from 'node:path';
import { App } from './app';

// Requiere Spring Boot con perfil demo en localhost:8081. No simula HTTP ni servicios.
describe('Integración Angular DOM con Spring Boot real', () => {
  let fixture: ComponentFixture<App>;
  let app: App;
  const http: any[] = [];
  const output = resolve('../docs/evidencias');
  const suffix = Date.now();
  const correo = `evidencia.${suffix}@tobyfit.local`;
  const interceptor: HttpInterceptorFn = (request, next) => next(request).pipe(tap(event => {
    if (event instanceof HttpResponse) http.push({metodo:request.method, ruta:request.url.replace('http://localhost:8081',''), estado:event.status});
  }));

  beforeAll(() => {
    mkdirSync(output, {recursive:true});
    localStorage.clear();
    vi.spyOn(window, 'confirm').mockReturnValue(true);
  });
  beforeEach(async () => {
    await TestBed.configureTestingModule({imports:[App], providers:[provideHttpClient(withFetch(), withInterceptors([interceptor]))]}).compileComponents();
    fixture = TestBed.createComponent(App);
    app = fixture.componentInstance;
    fixture.detectChanges();
    await stable();
  });
  afterAll(() => {
    writeFileSync(resolve(output,'integracion_angular_http.json'),JSON.stringify({
      fecha:new Date().toISOString(), entorno:'Angular TestBed + jsdom + Spring Boot demo H2 real',
      peticiones:http,
      operaciones:[...new Set(http.map(x=>x.metodo+' '+x.ruta.replace(/\/\d+$/,'/{id}')))].sort()
    },null,2));
    vi.restoreAllMocks();
    localStorage.clear();
  });
  async function stable() { await fixture.whenStable(); fixture.detectChanges(); }
  async function view(name:string) {
    const labels:Record<string,string>={registro:'Registrarse',login:'Iniciar sesión',productos:'Productos',membresias:'Membresías',rutinas:'Rutinas',promociones:'Promociones',usuarios:'Usuarios'};
    const button=Array.from(fixture.nativeElement.querySelectorAll('nav button')).find((el:any)=>el.textContent.trim().endsWith(labels[name])) as HTMLButtonElement;
    expect(button).toBeTruthy(); button.click(); await stable();
  }
  async function fill(values:Record<string,string>) {
    for (const [name,value] of Object.entries(values)) {
      const input:HTMLInputElement = fixture.nativeElement.querySelector(`[name="${name}"]`);
      expect(input).toBeTruthy(); input.value=value; input.dispatchEvent(new Event('input',{bubbles:true}));
    }
    await stable();
  }
  async function submit() {
    expect(fixture.nativeElement.querySelector('form .primary-button').disabled).toBe(false);
    fixture.nativeElement.querySelector('form').dispatchEvent(new Event('submit',{bubbles:true,cancelable:true}));
    await stable(); expect(app.error).toBe(''); expect(app.mensaje).not.toBe('');
  }
  function snapshot(name:string) {
    const css=readFileSync(resolve('src/app/app.css'),'utf8')+readFileSync(resolve('src/styles.css'),'utf8');
    const clone:HTMLElement=fixture.nativeElement.cloneNode(true);
    const original=fixture.nativeElement.querySelectorAll('input,textarea,select');
    clone.querySelectorAll('input,textarea,select').forEach((el:any,index:number)=>{
      const source=original[index];
      if(el.tagName==='INPUT') el.setAttribute('value', source.type==='password' ? '' : source.value);
      else if(el.tagName==='TEXTAREA') el.textContent=source.value;
      else Array.from(el.options).forEach((option:any,i:number)=>{if(i===source.selectedIndex)option.setAttribute('selected','');else option.removeAttribute('selected');});
    });
    let dom=clone.outerHTML.replace(/<app-root[^>]*>/,'<div>').replace('</app-root>','</div>');
    dom=dom.replace(/\s_ng(?:content|host)-[\w-]+=""/g,'');
    writeFileSync(resolve(output,name+'.html'), `<!doctype html><html lang="es"><head><meta charset="UTF-8"><title>TobyFit - evidencia DOM</title><style>${css}\nbody{margin:0;font-family:Arial,sans-serif}.evidence-note{padding:12px 24px;background:#fff8e0;color:#3b3420;font:14px Arial}</style></head><body><div class="evidence-note">EVIDENCIA DE PRUEBA INTEGRADA · DOM generado por Angular con datos del servidor real (H2). Vista estática exportada desde jsdom; no es una sesión de navegador conectada al servidor.</div>${dom}</body></html>`);
  }

  it('registro: crea una cuenta USUARIO mediante el formulario', async () => {
    await view('registro');
    expect(fixture.nativeElement.querySelector('.primary-button').disabled).toBe(true);
    await fill({regNombre:'Evidencia',regApellido:'Equipo Tres',regTelefono:'900000099',regEdad:'24',regCorreo:correo,regPassword:'Evidencia2026!'});
    await submit();
    expect(app.mensaje).toContain('registrado');
    snapshot('01_registro');
  });

  it('login: distingue USUARIO y ADMIN con credenciales reales', async () => {
    await view('login');
    await fill({loginCorreo:'usuario.jwt@tobyfit.local',loginPassword:'DemoUsuario2026!'});
    await submit();
    expect(app.rol).toBe('USUARIO');
    await view('productos');
    expect(fixture.nativeElement.querySelector('form')).toBeNull();
    expect(fixture.nativeElement.textContent).not.toContain('♙ Usuarios');
    snapshot('02_usuario_consulta');
    fixture.nativeElement.querySelector('.logout').click(); await stable();
    await view('login');
    await fill({loginCorreo:'admin.jwt@tobyfit.local',loginPassword:'DemoAdmin2026!'});
    snapshot('03_login');
    await submit();
    expect(app.esAdmin).toBe(true);
    expect(app.usuarios.length).toBeGreaterThanOrEqual(3);
    snapshot('04_admin');
  });

  const modules:any[] = [
    ['productos','Producto','prodNombre',{prodNombre:'Polo evidencia',prodCategoria:'Ropa',prodPrecio:'59.90',prodImagen:'polo-demo.jpg'}],
    ['membresias','Membresia','memNombre',{memNombre:'Plan evidencia',memDuracion:'30',memPrecio:'89.90',memDescripcion:'Ensayo integrado'}],
    ['rutinas','Rutina','rutNombre',{rutNombre:'Rutina evidencia',rutObjetivo:'Fuerza',rutDuracion:'4',rutDescripcion:'Ensayo integrado'}],
    ['promociones','Promocion','promoNombre',{promoNombre:'Oferta evidencia',promoCat:'Membresías',promoPrecio:'69.90',promoInicio:'2026-11-14',promoFin:'2026-11-30',promoDescripcion:'Ensayo integrado'}]
  ];
  it.each(modules)('%s: crea, lista, edita y elimina desde el DOM', async (resource, singular, field, values) => {
    await view(resource);
    await fill(values);
    await submit();
    const item=(app as any)[resource].find((x:any)=>x.nombre===values[field]);
    expect(item?.id).toBeTruthy();
    let row=[...fixture.nativeElement.querySelectorAll('tbody tr')].find((r:any)=>r.textContent.includes(values[field])) as HTMLElement;
    row.querySelector<HTMLButtonElement>('.edit-button')!.click(); await stable();
    await fill({[field]:values[field]+' editado'});
    await submit();
    expect((app as any)[resource].find((x:any)=>x.id===item.id).nombre).toBe(values[field]+' editado');
    // Reflejar el registro confirmado en el formulario para la evidencia.
    row=[...fixture.nativeElement.querySelectorAll('tbody tr')].find((r:any)=>r.textContent.includes(values[field])) as HTMLElement;
    row.querySelector<HTMLButtonElement>('.edit-button')!.click(); await stable();
    snapshot('05_'+resource);
    row.querySelector<HTMLButtonElement>('.delete-button')!.click(); await stable();
    expect((app as any)[resource].some((x:any)=>x.id===item.id)).toBe(false);
  });

  it('usuarios: ADMIN edita y elimina la cuenta creada por la prueba', async () => {
    await view('usuarios');
    let row=[...fixture.nativeElement.querySelectorAll('tbody tr')].find((r:any)=>r.textContent.includes(correo)) as HTMLElement;
    expect(row).toBeTruthy();
    row.querySelector<HTMLButtonElement>('.edit-button')!.click(); await stable();
    await fill({userNombre:'Evidencia editada'});
    await submit();
    expect(app.usuarios.find(x=>x.correo===correo)?.nombre).toBe('Evidencia editada');
    row=[...fixture.nativeElement.querySelectorAll('tbody tr')].find((r:any)=>r.textContent.includes(correo)) as HTMLElement;
    row.querySelector<HTMLButtonElement>('.edit-button')!.click(); await stable();
    snapshot('06_usuarios');
    row.querySelector<HTMLButtonElement>('.delete-button')!.click(); await stable();
    expect(app.usuarios.some(x=>x.correo===correo)).toBe(false);
    expect([...new Set(http.map(x=>x.metodo+' '+x.ruta.replace(/\/\d+$/,'/{id}')))]).toHaveLength(21);
  });
});

import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GymApiService } from './services/gym-api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  changeDetection: ChangeDetectionStrategy.Default,
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {

  vista = 'inicio';
  mensaje = '';
  error = '';

  membresias: any[] = [];
  productos: any[] = [];
  rutinas: any[] = [];
  promociones: any[] = [];
  usuarios: any[] = [];

  loginForm: any = { correo: '', contrasena: '' };

  registroForm: any = {
    nombre: '', apellido: '', telefono: '',
    edad: null, correo: '', contrasena: ''
  };

  usuarioForm: any = {
    id: null, nombre: '', apellido: '',
    telefono: '', edad: null, correo: '', rol: 'USUARIO'
  };

  membresiaForm: any = {
    id: null, nombre: '', duracionDias: 30,
    precio: 0, descripcion: '', activa: true
  };

  productoForm: any = {
    id: null, nombre: '', categoria: '',
    genero: 'UNISEX', precio: 0, imagenPrincipal: '',
    imagenes: '', modelos: '', activo: true
  };

  rutinaForm: any = {
    id: null, nombre: '', objetivo: '',
    nivel: 'Principiante', duracionSemanas: 4,
    descripcion: ''
  };

  promocionForm: any = {
    id: null, nombre: '', categoria: 'Membresías',
    descripcion: '', inicio: '', fin: '',
    precio: 0, activa: true
  };

  constructor(private api: GymApiService, private cdr: ChangeDetectorRef) {}

  get sesionActiva(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
      return Number(payload.exp) * 1000 > Date.now();
    } catch { return false; }
  }

  get rol(): string {
    return this.sesionActiva ? (localStorage.getItem('rol') || 'USUARIO') : 'INVITADO';
  }

  // Solo controla la presentación; el servidor vuelve a comprobar el rol de la BD.
  get esAdmin(): boolean { return this.rol === 'ADMIN'; }


  ngOnInit(): void {
    this.cargarMembresias();
    this.cargarProductos();
    this.cargarRutinas();
    this.cargarPromociones();
  }

  mostrar(vista: string): void {
    if (vista === 'usuarios' && !this.esAdmin) {
      this.error = 'Solo el administrador puede gestionar usuarios.';
      return;
    }
    this.vista = vista;
    this.mensaje = '';
    this.error = '';

    if (vista === 'membresias') this.cargarMembresias();
    if (vista === 'productos') this.cargarProductos();
    if (vista === 'rutinas') this.cargarRutinas();
    if (vista === 'promociones') this.cargarPromociones();
    if (vista === 'usuarios') this.cargarUsuarios();
  }

  private resultado(exito: string, accion: any, alFinalizar?: () => void): void {
    this.error = '';
    this.mensaje = '';
    accion.subscribe({
      next: () => {
        this.cdr.markForCheck();
        this.mensaje = exito;
        if (alFinalizar) alFinalizar();
      },
      error: (e: any) => {
        this.cdr.markForCheck();
        this.mensaje = '';
        this.error = e.error?.error || e.error?.mensaje ||
          e.error?.message || 'No se pudo completar la operación. Verifica tus permisos.';
      }
    });
  }

  iniciarSesion(): void {
    this.error = '';
    this.api.login(this.loginForm).subscribe({
      next: (r: any) => {
        this.cdr.markForCheck();
        localStorage.setItem('token', r.token);
        localStorage.setItem('rol', r.rol);
        this.loginForm.contrasena = '';
        this.mensaje = 'Inicio de sesión correcto.';
        this.vista = 'inicio';
        this.usuarios = [];
        if (this.esAdmin) this.cargarUsuarios();
      },
      error: (e: any) => {
        this.cdr.markForCheck();
        this.error = e.error?.error || 'Correo o contraseña incorrectos.';
      }
    });
  }

  registrarUsuario(): void {
    this.error = '';
    this.api.registrar(this.registroForm).subscribe({
      next: () => {
        this.cdr.markForCheck();
        this.mensaje = 'Usuario registrado correctamente.';
        this.registroForm = {
          nombre: '', apellido: '', telefono: '',
          edad: null, correo: '', contrasena: ''
        };
      },
      error: (e: any) => {
        this.cdr.markForCheck();
        this.error = e.error?.error || 'No se pudo registrar el usuario.';
      }
    });
  }

  cerrarSesion(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('rol');
    this.usuarios = [];
    this.nuevoUsuario();
    this.mostrar('inicio');
    this.mensaje = 'Sesión cerrada.';
  }

  // --- MEMBRESÍAS ---
  cargarMembresias(): void {
    this.api.listarMembresias().subscribe({
      next: (r: any[]) => { this.membresias = r || []; this.cdr.markForCheck(); },
      error: () => { this.error = 'No se pudieron cargar las membresías.'; this.cdr.markForCheck(); }
    });
  }

  guardarMembresia(): void {
    const peticion = this.membresiaForm.id
      ? this.api.actualizarMembresia(this.membresiaForm.id, this.membresiaForm)
      : this.api.crearMembresia(this.membresiaForm);

    this.resultado('Membresía guardada correctamente.', peticion, () => {
      this.cargarMembresias();
      this.nuevaMembresia();
    });
  }

  editarMembresia(item: any): void {
    this.membresiaForm = { ...item };
  }

  eliminarMembresia(id: number): void {
    if (confirm('¿Estás seguro de eliminar esta membresía?')) {
      this.resultado('Membresía eliminada.', this.api.eliminarMembresia(id), () => {
        this.cargarMembresias();
        if (this.membresiaForm.id === id) this.nuevaMembresia();
      });
    }
  }

  nuevaMembresia(): void {
    this.membresiaForm = {
      id: null, nombre: '', duracionDias: 30,
      precio: 0, descripcion: '', activa: true
    };
  }

  // --- PRODUCTOS ---
  cargarProductos(): void {
    this.api.listarProductos().subscribe({
      next: (r: any[]) => { this.productos = r || []; this.cdr.markForCheck(); },
      error: () => { this.error = 'No se pudieron cargar los productos.'; this.cdr.markForCheck(); }
    });
  }

  guardarProducto(): void {
    const peticion = this.productoForm.id
      ? this.api.actualizarProducto(this.productoForm.id, this.productoForm)
      : this.api.crearProducto(this.productoForm);

    this.resultado('Producto guardado correctamente.', peticion, () => {
      this.cargarProductos();
      this.nuevoProducto();
    });
  }

  editarProducto(item: any): void {
    this.productoForm = { ...item };
  }

  eliminarProducto(id: number): void {
    if (confirm('¿Estás seguro de eliminar este producto?')) {
      this.resultado('Producto eliminado.', this.api.eliminarProducto(id), () => {
        this.cargarProductos();
        if (this.productoForm.id === id) this.nuevoProducto();
      });
    }
  }

  nuevoProducto(): void {
    this.productoForm = {
      id: null, nombre: '', categoria: '',
      genero: 'UNISEX', precio: 0, imagenPrincipal: '',
      imagenes: '', modelos: '', activo: true
    };
  }

  // --- RUTINAS ---
  cargarRutinas(): void {
    this.api.listarRutinas().subscribe({
      next: (r: any[]) => { this.rutinas = r || []; this.cdr.markForCheck(); },
      error: () => { this.error = 'No se pudieron cargar las rutinas.'; this.cdr.markForCheck(); }
    });
  }

  guardarRutina(): void {
    const peticion = this.rutinaForm.id
      ? this.api.actualizarRutina(this.rutinaForm.id, this.rutinaForm)
      : this.api.crearRutina(this.rutinaForm);

    this.resultado('Rutina guardada correctamente.', peticion, () => {
      this.cargarRutinas();
      this.nuevaRutina();
    });
  }

  editarRutina(item: any): void {
    this.rutinaForm = { ...item };
  }

  eliminarRutina(id: number): void {
    if (confirm('¿Estás seguro de eliminar esta rutina?')) {
      this.resultado('Rutina eliminada.', this.api.eliminarRutina(id), () => {
        this.cargarRutinas();
        if (this.rutinaForm.id === id) this.nuevaRutina();
      });
    }
  }

  nuevaRutina(): void {
    this.rutinaForm = {
      id: null,
      nombre: '',
      objetivo: '',
      nivel: 'Principiante',
      duracionSemanas: 4,
      descripcion: ''
    };
  }

  // --- PROMOCIONES ---
  cargarPromociones(): void {
    this.api.listarPromociones().subscribe({
      next: (r: any[]) => { this.promociones = r || []; this.cdr.markForCheck(); },
      error: () => { this.error = 'No se pudieron cargar las promociones.'; this.cdr.markForCheck(); }
    });
  }

  guardarPromocion(): void {
    if (this.promocionForm.fin < this.promocionForm.inicio) {
      this.error = 'La fecha final no puede ser anterior a la inicial.';
      return;
    }
    const peticion = this.promocionForm.id
      ? this.api.actualizarPromocion(this.promocionForm.id, this.promocionForm)
      : this.api.crearPromocion(this.promocionForm);

    this.resultado('Promoción guardada correctamente.', peticion, () => {
      this.cargarPromociones();
      this.nuevaPromocion();
    });
  }

  editarPromocion(item: any): void {
    this.promocionForm = { ...item };
  }

  eliminarPromocion(id: number): void {
    if (confirm('¿Estás seguro de eliminar esta promoción?')) {
      this.resultado('Promoción eliminada.', this.api.eliminarPromocion(id), () => {
        this.cargarPromociones();
        if (this.promocionForm.id === id) this.nuevaPromocion();
      });
    }
  }

  nuevaPromocion(): void {
    this.promocionForm = {
      id: null,
      nombre: '',
      categoria: 'Membresías',
      descripcion: '',
      inicio: '',
      fin: '',
      precio: 0,
      activa: true
    };
  }

  // --- USUARIOS ---
  cargarUsuarios(): void {
    if (!this.esAdmin) { this.usuarios = []; return; }
    this.api.listarUsuarios().subscribe({
      next: (r: any[]) => { this.usuarios = r || []; this.cdr.markForCheck(); },
      error: () => { this.error = 'No se pudieron cargar los usuarios. Inicia sesión como administrador.'; this.cdr.markForCheck(); }
    });
  }

  editarUsuario(item: any): void {
    this.usuarioForm = {
      id: item.id,
      nombre: item.nombre,
      apellido: item.apellido,
      telefono: item.telefono,
      edad: item.edad,
      correo: item.correo,
      rol: item.rol
    };
  }

  nuevoUsuario(): void {
    this.usuarioForm = {
      id: null, nombre: '', apellido: '',
      telefono: '', edad: null, correo: '', rol: 'USUARIO'
    };
  }

  actualizarUsuario(): void {
    if (!this.usuarioForm.id) {
      this.error = 'Selecciona un usuario de la lista para editar.';
      return;
    }

    const datos = { ...this.usuarioForm };

    this.resultado('Usuario actualizado correctamente.', this.api.actualizarUsuario(datos.id, datos), () => {
      this.cargarUsuarios();
      this.nuevoUsuario();
    });
  }

  eliminarUsuario(id: number): void {
    if (confirm('¿Estás seguro de eliminar este usuario?')) {
      this.resultado('Usuario eliminado.', this.api.eliminarUsuario(id), () => {
        this.cargarUsuarios();
        if (this.usuarioForm.id === id) this.nuevoUsuario();
      });
    }
  }
}

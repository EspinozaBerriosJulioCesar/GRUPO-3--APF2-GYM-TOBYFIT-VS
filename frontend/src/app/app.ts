import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { GymApiService } from './services/gym-api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  vista = 'inicio';
  mensaje = '';
  error = '';

  membresias: any[] = [];
  productos: any[] = [];
  rutinas: any[] = [];
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
    genero: '', precio: 0, imagenPrincipal: '',
    imagenes: '', modelos: '', activo: true
  };

  rutinaForm: any = {
    id: null, nombre: '', objetivo: '',
    nivel: 'Principiante', duracionSemanas: 4,
    descripcion: ''
  };

  constructor(private api: GymApiService) {}

  mostrar(vista: string): void {
    this.vista = vista;
    this.mensaje = '';
    this.error = '';

    if (vista === 'membresias') this.cargarMembresias();
    if (vista === 'productos') this.cargarProductos();
    if (vista === 'rutinas') this.cargarRutinas();
    if (vista === 'usuarios') this.cargarUsuarios();
  }

  private resultado(exito: string, accion: any): void {
    this.error = '';
    this.mensaje = '';

    accion.subscribe({
      next: () => {
        this.mensaje = exito;
      },
      error: (e: any) => {
        this.mensaje = '';
        this.error = e.error?.error || e.error?.mensaje ||
          e.error?.message || 'No se pudo completar la operación. Verifica el servidor y tus permisos.';
      }
    });
  }

  iniciarSesion(): void {
    this.error = '';
    this.api.login(this.loginForm).subscribe({
      next: (r: any) => {
        localStorage.setItem('token', r.token);
        this.mensaje = 'Inicio de sesión correcto.';
        this.vista = 'inicio';
      },
      error: (e: any) => {
        this.error = e.error?.error || 'Correo o contraseña incorrectos.';
      }
    });
  }

  registrarUsuario(): void {
    this.error = '';
    this.api.registrar(this.registroForm).subscribe({
      next: () => {
        this.mensaje = 'Usuario registrado correctamente.';
        this.registroForm = {
          nombre: '', apellido: '', telefono: '',
          edad: null, correo: '', contrasena: ''
        };
      },
      error: (e: any) => {
        this.error = e.error?.error || 'No se pudo registrar el usuario.';
      }
    });
  }

  cerrarSesion(): void {
    localStorage.removeItem('token');
    this.mostrar('inicio');
    this.mensaje = 'Sesión cerrada.';
  }

  cargarMembresias(): void {
    this.api.listarMembresias().subscribe({
      next: (r: any[]) => this.membresias = r,
      error: () => this.error = 'No se pudieron cargar las membresías.'
    });
  }

  guardarMembresia(): void {
    const peticion = this.membresiaForm.id
      ? this.api.actualizarMembresia(this.membresiaForm.id, this.membresiaForm)
      : this.api.crearMembresia(this.membresiaForm);

    this.resultado('Membresía guardada correctamente.', peticion);
    peticion.subscribe({
      next: () => {
        this.cargarMembresias();
        this.nuevaMembresia();
      }
    });
  }

  editarMembresia(item: any): void {
    this.membresiaForm = { ...item };
  }

  nuevaMembresia(): void {
    this.membresiaForm = {
      id: null, nombre: '', duracionDias: 30,
      precio: 0, descripcion: '', activa: true
    };
  }

  cargarProductos(): void {
    this.api.listarProductos().subscribe({
      next: (r: any[]) => this.productos = r,
      error: () => this.error = 'No se pudieron cargar los productos.'
    });
  }

  guardarProducto(): void {
    const peticion = this.productoForm.id
      ? this.api.actualizarProducto(this.productoForm.id, this.productoForm)
      : this.api.crearProducto(this.productoForm);

    this.resultado('Producto guardado correctamente.', peticion);
    peticion.subscribe({
      next: () => {
        this.cargarProductos();
        this.nuevoProducto();
      }
    });
  }

  editarProducto(item: any): void {
    this.productoForm = { ...item };
  }

  nuevoProducto(): void {
    this.productoForm = {
      id: null, nombre: '', categoria: '',
      genero: '', precio: 0, imagenPrincipal: '',
      imagenes: '', modelos: '', activo: true
    };
  }

  cargarRutinas(): void {
    this.api.listarRutinas().subscribe({
      next: (r: any[]) => this.rutinas = r,
      error: () => this.error = 'No se pudieron cargar las rutinas.'
    });
  }

  guardarRutina(): void {
  this.api.crearRutina(this.rutinaForm).subscribe({
    next: () => {
      this.mensaje = 'Rutina registrada correctamente.';
      this.error = '';
      this.cargarRutinas();

      this.rutinaForm = {
        id: null,
        nombre: '',
        objetivo: '',
        nivel: 'Principiante',
        duracionSemanas: 4,
        descripcion: ''
      };
    },
    error: (e: any) => {
      this.error = e.error?.error || 'No se pudo registrar la rutina.';
    }
  });
}

  cargarUsuarios(): void {
    this.api.listarUsuarios().subscribe({
      next: (r: any[]) => this.usuarios = r,
      error: () => this.error = 'No se pudieron cargar los usuarios. Se requiere acceso de administrador.'
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

  actualizarUsuario(): void {
    if (!this.usuarioForm.id) {
      this.error = 'Selecciona un usuario de la lista.';
      return;
    }

    const datos = { ...this.usuarioForm };

    this.api.actualizarUsuario(datos.id, datos).subscribe({
      next: () => {
        this.mensaje = 'Usuario actualizado correctamente.';
        this.cargarUsuarios();
      },
      error: (e: any) => {
        this.error = e.error?.error || 'No se pudo actualizar el usuario.';
      }
    });
  }
}

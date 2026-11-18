---
name: compilar-tobyfit
description: Compilar, ejecutar y verificar el backend Spring Boot y frontend Angular de Gym TobyFit en PowerShell con guia de operaciones REST y formularios para sustentacion
---

# Skill: Compilar, Ejecutar y Sustentar Gym TobyFit

Instrucciones operativas para compilar, verificar y ejecutar el backend Spring Boot y el frontend Angular en entornos Windows con PowerShell, ademas de la referencia tecnica de formularios y servicios REST para sustentacion academica

## Requisitos Previos
* **Java 21**
* **Node.js y npm**
* Terminal en la raiz del proyecto o en las carpetas especificas

---

## Procedimientos de Ejecucion

### 1. Iniciar Backend en Modo Demo con Base H2 en memoria RAM
No requiere XAMPP ni MySQL instalado o encendido. Crea las tablas y datos demo automaticamente:

```powershell
cd gym-tobyfit-vs\gym-tobyfit
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

### 2. Iniciar Backend con MySQL en XAMPP
1. Encender Apache y MySQL en el panel de XAMPP
2. Importar el archivo unico en phpMyAdmin:
   `gym-tobyfit-vs\gym-tobyfit\sql\gym_tobyfit.sql`
3. Ejecutar el backend:
```powershell
cd gym-tobyfit-vs\gym-tobyfit
.\mvnw.cmd spring-boot:run
```

### 3. Iniciar Frontend Angular
En una segunda terminal:
```powershell
cd frontend
npm start
```
* **URL de la aplicacion:** `http://localhost:4200`

---

## Verificacion y Pruebas Backend
* **Comprobar compilacion:**
  ```powershell
  cd gym-tobyfit-vs\gym-tobyfit
  .\mvnw.cmd clean compile
  ```
* **Ejecutar suite de pruebas unitarias y de integracion:**
  ```powershell
  cd gym-tobyfit-vs\gym-tobyfit
  .\mvnw.cmd test
  ```

---

## Credenciales y Seguridad
* **Backend:** Expone servicios REST en `http://localhost:8080/api/`
* **CORS:** Habilitado para peticiones y cabeceras JWT desde `http://localhost:4200`
* **Credenciales de prueba:**
  * **Admin:** `admin.jwt@tobyfit.local` / `DemoAdmin2026!`
  * **Usuario:** `usuario.jwt@tobyfit.local` / `DemoUsuario2026!`

---

## Formularios Angular
La aplicacion cuenta con 6 o mas formularios controlados con ngModel, validacion en tiempo real y reseteo:

1. **fLogin - loginForm:** Inicio de sesion con correo y contrasena, captura token JWT y rol
2. **fRegistro - registroForm:** Registro de nuevos socios con nombre, apellido, telefono, edad, correo y contrasena minima de 8 caracteres
3. **fProducto - productoForm:** Altas y ediciones de productos del catalogo con nombre, precio, genero, estado e imagen
4. **fMembresia - membresiaForm:** Gestion de planes del gimnasio con nombre, dias de vigencia, costo, descripcion y estado
5. **fRutina - rutinaForm:** Creacion y edicion de rutinas con nombre, objetivo, nivel, semanas y descripcion
6. **fPromocion - promocionForm:** Descuentos y campanas promocionales con nombre, fechas, precio rebajado y categoria

---

## Operaciones REST
Mas de 20 operaciones REST implementadas en 6 controladores y consumidas con GymApiService y HttpClient:

| Controlador | Operacion | Endpoint | Descripcion |
|---|---|---|---|
| **ProductoApiController** | GET | `/api/productos` | Listar productos Publico |
| | POST | `/api/productos` | Crear producto ADMIN |
| | PUT | `/api/productos/{id}` | Modificar producto ADMIN |
| | DELETE | `/api/productos/{id}` | Eliminar producto ADMIN |
| **MembresiaApiController** | GET | `/api/membresias` | Listar membresias Publico |
| | POST | `/api/membresias` | Crear membresia ADMIN |
| | PUT | `/api/membresias/{id}` | Modificar membresia ADMIN |
| | DELETE | `/api/membresias/{id}` | Eliminar membresia ADMIN |
| **RutinaApiController** | GET/POST/PUT/DELETE | `/api/rutinas/**` | CRUD de rutinas de entrenamiento |
| **PromocionApiController** | GET/POST/PUT/DELETE | `/api/promociones/**` | CRUD de promociones y descuentos |
| **UsuarioApiController** | GET/PUT/DELETE | `/api/usuarios/**` | Gestion de cuentas y asignacion de roles |
| **AuthController** | POST | `/api/auth/login` | Autenticacion y generacion de JWT |
| | POST | `/api/auth/registro` | Alta segura de usuarios con BCrypt |

---
name: compilar-tobyfit
description: Compilar, ejecutar y verificar el backend Spring Boot y frontend Angular de Gym TobyFit en PowerShell, con guía de operaciones REST y formularios para sustentación.
---

# Skill: Compilar, Ejecutar y Sustentar Gym TobyFit

Instrucciones operativas para compilar, verificar y ejecutar el backend Spring Boot y el frontend Angular en entornos Windows con PowerShell, además de la referencia técnica de formularios y servicios REST para sustentación académica.

## Requisitos Previos
* **Java 21** (`java -version`)
* **Node.js y npm** (`node -v`, `npm -v`)
* Terminal en la raíz del proyecto o en las carpetas específicas.

---

## Procedimientos de Ejecución

### 1. Iniciar Backend en Modo Demo (Base H2 en memoria RAM)
No requiere XAMPP ni MySQL instalado o encendido. Crea las tablas y datos demo automáticamente:

```powershell
cd gym-tobyfit-vs\gym-tobyfit
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

### 2. Iniciar Backend con MySQL (XAMPP)
1. Encender Apache y MySQL en el panel de XAMPP.
2. Importar el archivo único en phpMyAdmin (`http://localhost/phpmyadmin`):
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
* **URL de la aplicación:** `http://localhost:4200`

---

## Verificación y Pruebas Backend
* **Comprobar compilación:**
  ```powershell
  cd gym-tobyfit-vs\gym-tobyfit
  .\mvnw.cmd clean compile
  ```
* **Ejecutar suite de pruebas unitarias y de integración:**
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

## Rúbrica: Formularios Angular (8 Puntos)
La aplicación cuenta con 6+ formularios controlados con `[(ngModel)]`, validación en tiempo real y reseteo:

1. **`fLogin` (loginForm):** Inicio de sesión con correo y contraseña, captura token JWT y rol.
2. **`fRegistro` (registroForm):** Registro de nuevos socios (nombre, apellido, teléfono, edad, correo, contraseña >= 8 car.).
3. **`fProducto` (productoForm):** Altas y ediciones de productos del catálogo (nombre, precio, género, estado, imagen).
4. **`fMembresia` (membresiaForm):** Gestión de planes del gimnasio (nombre, días de vigencia, costo, descripción, estado).
5. **`fRutina` (rutinaForm):** Creación y edición de rutinas (nombre, objetivo, nivel, semanas, descripción).
6. **`fPromocion` (promocionForm):** Descuentos y campañas promocionales (nombre, fechas, precio rebajado, categoría).

---

## Rúbrica: Operaciones REST (8 Puntos)
Más de 20 operaciones REST implementadas en 6 controladores y consumidas con `GymApiService` (`HttpClient`):

| Controlador | Operación | Endpoint | Descripción |
|---|---|---|---|
| **ProductoApiController** | GET | `/api/productos` | Listar productos (Público) |
| | POST | `/api/productos` | Crear producto (ADMIN) |
| | PUT | `/api/productos/{id}` | Modificar producto (ADMIN) |
| | DELETE | `/api/productos/{id}` | Eliminar producto (ADMIN) |
| **MembresiaApiController** | GET | `/api/membresias` | Listar membresías (Público) |
| | POST | `/api/membresias` | Crear membresía (ADMIN) |
| | PUT | `/api/membresias/{id}` | Modificar membresía (ADMIN) |
| | DELETE | `/api/membresias/{id}` | Eliminar membresía (ADMIN) |
| **RutinaApiController** | GET/POST/PUT/DELETE | `/api/rutinas/**` | CRUD de rutinas de entrenamiento |
| **PromocionApiController** | GET/POST/PUT/DELETE | `/api/promociones/**` | CRUD de promociones y descuentos |
| **UsuarioApiController** | GET/PUT/DELETE | `/api/usuarios/**` | Gestión de cuentas y asignación de roles |
| **AuthController** | POST | `/api/auth/login` | Autenticación y generación de JWT |
| | POST | `/api/auth/registro` | Alta segura de usuarios con BCrypt |

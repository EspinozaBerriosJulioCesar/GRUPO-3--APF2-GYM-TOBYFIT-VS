# Revisión y Cambios de Gym TobyFit

## 1. Alcance y Estado Actual

El proyecto cuenta con el backend Spring Boot (Java 21) completamente operativo y preparado para la integración con **Angular** de cara al **Avance de Proyecto Final 3 (APF3)**.

### Documentación oficial:
* El documento principal para entrega académica es **`docs/Manual_Completo_TobyFit.docx`** (y su exportación en PDF).
* La guía de compilación y ejecución se encuentra en **`COMO_COMPILAR.md`** y registrada como Skill en **`.agents/skills/compilar-tobyfit/SKILL.md`**.

---

## 2. Cambios Recientes Implementados

1. **Habilitación de CORS para Angular (APF3)**:
   * Se configuró `WebSecurityConfig.java` para permitir peticiones HTTP (`GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`, `PATCH`) desde `http://localhost:4200`.
   * Se autorizaron las cabeceras `Authorization` y `Content-Type`, y las peticiones preflight `OPTIONS` sin requerir token previo.

2. **Unificación de Base de Datos**:
   * Se unificaron los scripts SQL en un solo archivo: `gym-tobyfit-vs/gym-tobyfit/sql/gym_tobyfit.sql` (que incluye estructura y usuarios de demostración).
   * Para pruebas sin MySQL/XAMPP, se mantiene el perfil `demo` con base de datos H2 en memoria RAM.

3. **Limpieza de Archivos**:
   * Se eliminaron copias de respaldo obsoletas (`respaldo-seguridad-20261010-200857`) y logs históricos.
   * Se eliminó `LEEME_PRIMERO.md` de la raíz para centralizar toda la documentación en `docs/`.

4. **Sincronización de Autenticación en Frontend Web**:
   * Las páginas web (`productos.html`, `membresias.html`, `rutinas.html`) leen y persisten de forma sincronizada las claves `token` y `tobyfit_token` en `localStorage`, compartiendo la sesión activa.

---

## 3. Estado de la Arquitectura Backend

* **4 Modelos / Entidades JPA**: `Producto` (9 atributos), `Usuario` (8 atributos), `Membresia` (6 atributos), `Rutina` (6 atributos).
* **19 Operaciones REST Disponibles**:
  * 5 en `ProductoApiController` (CRUD completo)
  * 5 en `MembresiaApiController` (CRUD completo)
  * 5 en `RutinaApiController` (CRUD completo)
  * 3 en `UsuarioApiController` (Gestión con rol ADMIN)
  * 1 en `AuthController` (`/api/auth/login`)
* **Cadena de Seguridad JWT**: `WebSecurityConfig` ➔ `JwtFilter` ➔ `JwtUtil` ➔ `MyUserDetailsService`.

---

## 4. Requisitos para el Avance de Proyecto Final 3 (Rúbrica UTP)

* **Operaciones REST (8 pts)**: Cumplido con 19 operaciones (la rúbrica exige al menos 8 operaciones en 2 controladores).
* **Formularios Angular (8 pts)**: Tu compañero debe implementar los 6 formularios planificados (Login, Crear Producto, Editar Producto, Crear Membresía, Editar Membresía, Crear Rutina).
* **Presentación (4 pts)**: Despliegue listo para iniciar mediante perfil demo o MySQL.

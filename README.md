# Equipo 3 · Gym TobyFit

## Estado verificado el 14 de noviembre de 2026

El proyecto expone **26 operaciones REST en 7 controladores**. Angular invoca **21 operaciones** mediante **7 formularios**, todos definidos en `frontend/src/app/app.html` y gestionados por el componente `App` de `app.ts`.

**Resultados:** 115 pruebas backend, 41 pruebas unitarias Angular y 7 pruebas integradas Angular/HTTP aprobadas (163 en total). La prueba integrada registró 66 respuestas exitosas y las 21 operaciones distintas. La base verificada fue H2; MySQL no se ejecutó en esta revisión.

La documentación actual es `docs/Manual_Completo_TobyFit.docx`. El Excel de entrega es `docs/Equipo_3.xlsx`, hoja **Equipo 3**. Los documentos de etapas anteriores se conservan en `docs/historico/`.

## Ejecutar la aplicación

Requisitos usados: **JDK 21**, **Node.js 24** (o superior) y npm. Para compilar Java se necesita el JDK completo (`javac -version`), no solo el JRE.

### Terminal 1: Backend (Spring Boot)

Desde la raíz del proyecto (PowerShell), elige una de las dos modalidades:

#### Opción A: Modo Demo (Base de datos H2 en memoria RAM - No requiere XAMPP)
Ideal para pruebas rápidas y ejecución de tests. Crea tablas y datos demo automáticamente en memoria.
```powershell
cd gym-tobyfit-vs/gym-tobyfit
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

#### Opción B: Modo MySQL (XAMPP - Persistencia real)
1. Iniciar **Apache** y **MySQL** desde el panel de XAMPP.
2. En phpMyAdmin (`http://localhost/phpmyadmin`), crear la base de datos `gym_tobyfit` e importar `gym-tobyfit-vs/gym-tobyfit/sql/gym_tobyfit.sql`.
3. Ejecutar el backend:
```powershell
cd gym-tobyfit-vs/gym-tobyfit
.\mvnw.cmd spring-boot:run
```

---

### Terminal 2: Frontend (Angular)

Desde la raíz del proyecto en una segunda ventana de PowerShell:

```powershell
cd frontend
npm install
npm start
```

Abrir **http://localhost:4200** en el navegador. La API REST responde en **http://localhost:8081/api**.

| Rol demo | Correo | Contraseña demo | Permisos |
|---|---|---|---|
| ADMIN | admin.jwt@tobyfit.local | DemoAdmin2026! | CRUD completo en membresías, productos, promociones, rutinas y gestión de usuarios. |
| USUARIO | usuario.jwt@tobyfit.local | DemoUsuario2026! | Consulta de catálogos y rutinas públicas. |

En el modo demo H2 los datos se reinician al cerrar el backend. En el modo MySQL los datos persisten en la base de datos.

## Permisos

- Invitado y USUARIO: consultar productos, membresías, rutinas y promociones.
- ADMIN: además, crear, editar y eliminar en esos cuatro módulos; listar, editar y eliminar usuarios.
- Registro público: siempre crea USUARIO. No permite elegir ADMIN.
- Sin token en rutas protegidas: 401. USUARIO en acciones de ADMIN: 403.
- El servidor carga el rol vigente desde la base de datos en cada petición. El rol del almacenamiento local solo controla qué muestra Angular.

## Repetir las pruebas

Backend, desde la carpeta del `pom.xml`:

```powershell
.\mvnw.cmd clean verify
```

Angular, desde `frontend`:

```powershell
npm test -- --watch=false
npm run build
```

Con el backend **demo** iniciado en 8081, ejecutar también desde `frontend`:

```powershell
npm run test:live
```

La prueba `test:live` usa Angular TestBed y jsdom con peticiones reales al servidor. Crea datos ficticios, verifica altas, consultas, modificaciones y bajas, y elimina sus registros. Ejecutarla solamente con el perfil demo. Genera el registro HTTP y las vistas HTML estáticas en `docs/evidencias/`.

En Linux/macOS sustituir `.\mvnw.cmd` por `bash mvnw`.

## Evidencias y alcance

`docs/evidencias/` contiene registros de ejecución, resultados Surefire, JSON de las peticiones y tres PNG legibles que reproducen esos resultados. Los HTML son exportaciones estáticas del DOM producido por Angular durante la prueba integrada; sus botones no ejecutan acciones. **Los PNG no son capturas de una sesión de navegador conectada al servidor.**

No se pudo ejecutar esa sesión de navegador en el entorno de revisión. El aislamiento impidió acceder al servidor desde el navegador disponible; la ampliación de permisos fue rechazada automáticamente y el navegador también bloqueó los archivos locales. Para obtener capturas de pantalla de la aplicación funcionando, seguir `docs/Guion_Capturas.md` en el equipo local.

`npm run build` termina correctamente con una advertencia: `app.css` ocupa 6,46 kB y supera el umbral de aviso de 4 kB, sin alcanzar el límite de error de 8 kB.

## MySQL

La configuración principal se conecta a MySQL en `localhost:3306/gym_tobyfit`. Para facilitar la ejecución local con XAMPP, `application.properties` ya incluye valores por defecto para `DB_URL`, `DB_USER` (`root`), `DB_PASSWORD` (vacío) y `JWT_SECRET`. El archivo `gym-tobyfit-vs/gym-tobyfit/sql/gym_tobyfit.sql` contiene las cinco tablas del dominio (`membresias`, `productos`, `promociones`, `rutinas` y `usuarios`). Basta con arrancar sin el perfil demo (`.\mvnw.cmd spring-boot:run`) para utilizar MySQL.

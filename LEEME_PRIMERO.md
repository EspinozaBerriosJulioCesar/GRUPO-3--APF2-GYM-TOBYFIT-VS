# Gym TobyFit — actualización estética

Abre `gym-tobyfit-vs/gym-tobyfit` en Visual Studio Code. La terminal debe estar en la carpeta donde está `pom.xml`.

Lee `docs/Manual_Completo_TobyFit.pdf` o su versión Word. El manual explica la ejecución con MySQL, el perfil demo, JWT, Postman, Maven y GitHub.

## Inicio rápido en Windows / PowerShell

1. Confirma JDK 21 con `java -version` y `.\mvnw.cmd -v`.
2. Genera una clave compatible en la misma terminal:

```powershell
$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
$rng.Dispose()
```

3. Para ensayar sin MySQL, inicia el perfil demo:

```powershell
.\mvnw.cmd clean spring-boot:run "-Dspring-boot.run.profiles=demo"
```

4. Abre `http://localhost:8080/index.html`.
5. Cuenta ficticia ADMIN: `admin.jwt@tobyfit.local` / `DemoAdmin2026!`.
6. Cuenta ficticia USUARIO: `usuario.jwt@tobyfit.local` / `DemoUsuario2026!`.

En demo existe un producto de ensayo. Membresías y rutinas comienzan vacías; el manual incluye cómo agregar registros de práctica mediante Postman. H2 borra esos datos al detenerse.

## Para presentar con MySQL

Prepara una base de desarrollo e importa `gym_tobyfit.sql` (reemplaza tablas productos y usuarios) y luego `gym-tobyfit-vs/gym-tobyfit/sql/01_usuarios_demo_jwt.sql`. Define `DB_URL`, `DB_USER`, `DB_PASSWORD` y `JWT_SECRET` siguiendo el manual. Ejecuta `.\mvnw.cmd clean spring-boot:run` sin perfil demo.

## Qué se cambió

Solo las cuatro páginas en `src/main/resources/static` y tres recursos visuales nuevos:

- `css/tobyfit.css`: colores, estructura, tipografía, tarjetas, formularios y adaptación a pantallas.
- `js/visual.js`: menú y carrusel. No consulta endpoints ni cambia tokens.
- `images/training.svg`: ilustración local de la portada.

Los archivos de Java, JWT, pruebas, Maven, propiedades, SQL, Postman, script HTTP y Git se conservaron. Los bloques JavaScript de autenticación y consulta de las tres páginas mantienen su texto original.

## Antes de exponer

- Ejecuta `.\mvnw.cmd clean verify` en tu computadora con Java 21.
- Prueba `/auth` y `/api/auth/login`; usa la clave generada arriba. Las claves largas pueden provocar una incompatibilidad entre los generadores JWT originales.
- Rutinas conserva `tobyfit_token`; productos y membresías conservan `token`. Puede solicitar un segundo login.
- Revisa la interfaz en computadora y celular: la revisión visual en navegador no pudo completarse en el entorno de edición.
- Los 78 resultados Surefire incluidos son anteriores. No se certifica una nueva ejecución del backend.
- Confirma los cambios y evidencias actuales en GitHub. El ZIP original ya tenía cambios locales pendientes.
- Consulta `docs/Revision_y_Cambios.md` para el alcance y las limitaciones verificadas.

Las diapositivas y la documentación original se mantuvieron; describen una versión anterior. El nuevo manual documenta el código que está actualmente en `src`.

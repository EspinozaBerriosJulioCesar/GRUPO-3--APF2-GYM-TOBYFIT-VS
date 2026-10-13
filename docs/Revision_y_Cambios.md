# Revisión y cambios de Gym TobyFit

## Alcance

La comparación usa el ZIP aportado como referencia; no usa HEAD de Git como referencia. El archivo original ya contenía cambios pendientes del equipo.

Se modificaron exclusivamente estas páginas de presentación:

- `gym-tobyfit-vs/gym-tobyfit/src/main/resources/static/membresias.html`
- `gym-tobyfit-vs/gym-tobyfit/src/main/resources/static/index.html`
- `gym-tobyfit-vs/gym-tobyfit/src/main/resources/static/rutinas.html`
- `gym-tobyfit-vs/gym-tobyfit/src/main/resources/static/productos.html`

Se agregaron CSS compartido, JavaScript de menú/carrusel y una ilustración SVG. También se añadieron el manual y los informes de revisión. El backend y sus configuraciones permanecen sin cambios.

## Integridad

186 archivos originales mantienen el mismo SHA-256. No falta ningún archivo original. Los tres bloques de JavaScript de login y consulta mantienen exactamente el mismo texto (normalizando finales de línea). Los IDs originales de formularios, botones y listados están presentes. Ver `Verificacion_Integridad.json`.

## Resultados comprobados

- Cuatro modelos activos: Producto (9 atributos), Usuario (8), Membresia (6), Rutina (6).
- 18 operaciones REST del dominio y 2 rutas de login.
- 24 @Test originales para 8 operaciones; 20 pruebas REST adicionales para 10 operaciones. Cada operación del dominio tiene al menos dos casos en esas suites.
- Cuatro casos de seguridad solicitados están en JwtSecurityIntegrationTest.
- Reportes Surefire suministrados: 24 + 20 + 26 + 8 = 78 ejecuciones; cero errores, fallos u omitidos. Son reportes previos.
- Sintaxis JavaScript, IDs sin duplicados, enlaces a recursos locales y preservación de los scripts de negocio comprobados.
- Menú, Escape, navegación del carrusel, indicadores y recorrido circular comprobados con un DOM de prueba.
- Login correcto/incorrecto, tarjetas, búsqueda de productos, lista vacía, rechazo de token y logout comprobados con un DOM de prueba y respuestas HTTP simuladas. No son pruebas del backend real.

## Limitaciones de ejecución

Maven se invocó, pero no resolvió el parent de Spring Boot 3.4.10 por falta de acceso a Maven Central. El entorno tenía Java 17 y el proyecto exige Java 21. MySQL y Postman no se ejecutaron aquí. No se comprobó el estado remoto de GitHub ni se publicó ningún cambio.

La revisión visual en navegador no se completó: Chromium no estaba disponible; la instalación no obtuvo un archivo válido; el navegador remoto no accedió al servidor local y prohibió la vista mediante data URL. Se revisó el CSS de forma estática. Debe verificarse el aspecto final en escritorio y celular al iniciar el proyecto.

## Hallazgos previos conservados

1. JwtUtil exige HS256. JwtService usa `signWith(key)` y puede elegir HS384/HS512 con claves largas. La clave demo predeterminada cae fuera del intervalo compatible con HS256. El manual genera 32 bytes aleatorios codificados en Base64 (44 caracteres ASCII) para ejecutar ambos logins sin modificar seguridad.
2. Productos y membresías usan localStorage `token`; rutinas usa `tobyfit_token`. El logout no es global entre ambas claves.
3. SQL original solo crea productos y usuarios; Hibernate con `ddl-auto=update` crea membresías/rutinas al iniciar si dispone de permisos.
4. La colección Postman contiene 24 peticiones de productos y usuarios, sin autenticación global y sin solicitudes para los otros objetos.
5. Los documentos originales describen una etapa anterior (incluyen CarritoCompras); el código activo utiliza Rutina.
6. Escritura de productos y todas las operaciones de usuarios requieren ADMIN. Membresías y rutinas requieren autenticación, pero no añaden restricción ADMIN.
7. Fotos de productos dependen de Unsplash y de la selección original por categoría; la función original no usa imagenPrincipal como primera elección.
8. El ZIP original ya incluye numerosos cambios locales respecto a su commit y el historial local no acredita que estén en el remoto.

## Verificación pendiente antes de presentar

Ejecutar con Java 21 y MySQL; probar ambos logins; ejecutar `clean verify`; realizar los cuatro casos JWT en Postman; comprobar datos reales y los tres catálogos; revisar cuatro páginas en computadora/celular; validar el commit en GitHub y su workflow. No presentar reportes anteriores ni respuestas simuladas como evidencia nueva.

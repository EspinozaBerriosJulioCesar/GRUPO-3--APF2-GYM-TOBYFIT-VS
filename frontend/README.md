# Frontend Angular de TobyFit

Instalación: `npm ci`. Desarrollo: `npm start`. Abrir `http://localhost:4200` con Spring Boot en `http://localhost:8081`.

Pruebas unitarias: `npm test -- --watch=false` (41 casos). Compilación: `npm run build`.

Prueba integrada: `npm run test:live` (7 casos). Requiere el backend con perfil demo en 8081 y realiza peticiones HTTP reales con datos ficticios en H2. No requiere abrir el navegador.

Las siete vistas con formularios están en `src/app/app.html`, dentro del componente `App` (`app.ts`). Las 21 llamadas REST están en `services/gym-api.service.ts`. No existen componentes separados llamados productos.component.ts o usuarios.component.ts.

Consultar `../README.md`, `../docs/Manual_Completo_TobyFit.docx` y `../docs/Equipo_3.xlsx` para la entrega y permisos.

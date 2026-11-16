# Capturas de pantalla pendientes en el equipo local

Estas capturas requieren una sesión real en http://localhost:4200 con Spring Boot demo en 8081. No se adjuntan como ya realizadas.

1. Ejecutar `mvnw clean verify` y capturar el resumen de 115 pruebas aprobadas.
2. Ejecutar `npm test -- --watch=false` y capturar las 41 pruebas aprobadas.
3. Mantener el backend demo iniciado, ejecutar `npm run test:live` y capturar las 7 pruebas aprobadas.
4. Abrir Registro y crear una cuenta de ensayo. Capturar el mensaje de éxito.
5. Iniciar sesión como USUARIO. Capturar el catálogo sin botones administrativos ni módulo Usuarios.
6. Iniciar sesión como ADMIN. Capturar el panel con el módulo Usuarios visible.
7. En Productos, crear un registro de ensayo, editarlo y capturar el formulario y la fila guardada.
8. Repetir el paso anterior en Membresías, Rutinas y Promociones.
9. En Usuarios, editar solo la cuenta de ensayo creada en el paso 4 y capturar el resultado.
10. En Postman o DevTools, capturar GET /api/usuarios sin token (401), con token USUARIO (403) y con ADMIN (200). No incluir JWT completos en las capturas.
11. Mostrar en DevTools las solicitudes GET, POST, PUT y DELETE de Productos y Membresías para sustentar ocho operaciones en dos controladores.

Usar exclusivamente datos de ensayo y guardar capturas antes de eliminar esos registros.

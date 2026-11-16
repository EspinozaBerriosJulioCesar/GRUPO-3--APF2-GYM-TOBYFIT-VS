# Revisión final de TobyFit

## Resultado

26 rutas REST, 7 controladores (5 de negocio y 2 de autenticación), 21 operaciones consumidas por Angular y 7 formularios. Productos y Membresías aportan por sí solos 8 operaciones invocadas desde Angular.

## Problemas encontrados en el ZIP original

- UsuarioApiController autorizaba a USUARIO a gestionar cuentas, incluso cambiar roles.
- Las escrituras de productos, membresías, rutinas y promociones solo exigían autenticación.
- El frontend siempre intentaba cargar usuarios tras iniciar sesión y mostraba las acciones de administración a todos.
- La prueba Angular aún esperaba el título de la plantilla. Las respuestas HTTP asíncronas no notificaban correctamente los cambios de vista.
- Las pruebas JWT y documentos conservaban supuestos de una etapa anterior: catálogo protegido, 19 operaciones, formularios pendientes y puerto 8080.

## Correcciones

- Acciones administrativas restringidas a ADMIN en la cadena de seguridad; UsuarioApiController también exige ADMIN.
- Login devuelve el rol, que Angular usa para presentar menús, formularios y acciones. El backend comprueba el rol real en la base de datos.
- Logout limpia permisos y datos de usuarios. Las cargas de usuarios solo se hacen con ADMIN.
- NgForm bloquea envíos inválidos, se valida el correo y longitud de contraseña de registro, se comprueban fechas de promociones y se asocian etiquetas a controles.
- ChangeDetectorRef.markForCheck() actualiza la vista al recibir respuestas HTTP.
- El manejo de errores 401 se aplica también al contrato /api/auth/login.
- Se corrigieron expectativas de lectura pública y se agregaron pruebas de permisos, persistencia, registro, CORS y consumo Angular.

## Verificación

Original: 78 pruebas backend, con 4 fallos de aserción y 1 error; Angular: 1 aprobada y 1 fallida.

Final: backend 115/115, Angular unitario 41/41, integración Angular con servidor real 7/7. El recorrido integrado registró 66 respuestas exitosas para 21 operaciones distintas. `npm run build` aprobó con aviso de tamaño CSS (6,46 kB frente a 4 kB de aviso; límite de error 8 kB).

Base usada: H2. MySQL y una sesión de navegador conectada al servidor no se verificaron. Las evidencias PNG son reproducciones de los registros; las vistas HTML provienen del DOM de la prueba integrada. La política del entorno bloqueó las capturas de la aplicación en vivo.

La documentación vigente está en Manual_Completo_TobyFit.docx, README.md y Equipo_3.xlsx. Los documentos anteriores se conservan en historico/.

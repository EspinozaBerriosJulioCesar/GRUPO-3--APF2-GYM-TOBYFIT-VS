# Evidencias de ejecución

Resultados obtenidos el 14 de noviembre de 2026.

- `logs/`: salidas de las pruebas y compilación. Los JWT de ensayo del registro inicial se sustituyeron por `[JWT_DE_PRUEBA_OMITIDO]`; los resultados, estados y fallos no se alteraron.
- `surefire/`: resultados individuales de las 115 pruebas backend. Las copias XML omiten propiedades del entorno y salida duplicada; conservan los casos y resultados.
- `integracion_angular_http.json`: 66 respuestas HTTP exitosas, 21 operaciones distintas. No contiene contraseñas ni tokens.
- `resumen_verificacion.json`: resumen de resultados y alcance.
- `Evidencia_*.png`: reproducciones legibles de los registros reales. No son capturas de navegador.
- `*.html`: exportaciones estáticas del DOM producido por Angular TestBed durante la integración con el servidor real. Incluyen un aviso visible; no ejecutan acciones ni representan una sesión de navegador en vivo.

Base ejecutada: H2. MySQL y las capturas de una sesión de navegador siguen pendientes. Ver `../Guion_Capturas.md`.

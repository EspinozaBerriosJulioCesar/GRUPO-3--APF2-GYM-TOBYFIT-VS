# Gym TobyFit: compilar y ejecutar

Requisito: Java 21 (`java -version`).

```bash
cd gym-tobyfit-vs/gym-tobyfit
chmod +x mvnw
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
```

Abrir http://localhost:8080 

Si cambias código y algo falla, recompila todo:

```bash
./mvnw clean spring-boot:run -Dspring-boot.run.profiles=demo
```

Al portal web de la BD (phpMyAdmin), http://localhost/phpmyadmin con XAMPP

Cuentas demo:
- `admin.jwt@tobyfit.local` / `DemoAdmin2026!`
- `usuario.jwt@tobyfit.local` / `DemoUsuario2026!`

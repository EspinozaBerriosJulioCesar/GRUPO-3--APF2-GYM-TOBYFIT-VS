---
name: compilar-tobyfit
description: Compilar, ejecutar y verificar el backend Spring Boot de Gym TobyFit en PowerShell (Modo Demo H2 o MySQL) y preparar conexión con Angular.
---

# Skill: Compilar y Ejecutar Gym TobyFit

Instrucciones operativas para compilar, verificar y ejecutar el backend Spring Boot en entornos Windows con PowerShell.

## Requisitos Previos
* **Java 21** (`java -version`)
* Terminal en la raíz o en la carpeta Maven del proyecto.

---

## Procedimientos de Ejecución

### 1. Iniciar en Modo Demo (Base H2 en memoria RAM)
No requiere tener XAMPP ni MySQL instalado o encendido. Crea las tablas y datos demo automáticamente.

```powershell
cd gym-tobyfit-vs\gym-tobyfit
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

### 2. Iniciar con MySQL (XAMPP)
1. Encender Apache y MySQL en el panel de XAMPP.
2. Importar el archivo único en phpMyAdmin (`http://localhost/phpmyadmin`):
   `gym-tobyfit-vs\gym-tobyfit\sql\gym_tobyfit.sql`
3. Ejecutar el backend:
```powershell
cd gym-tobyfit-vs\gym-tobyfit
.\mvnw.cmd spring-boot:run
```

---

## Verificación y Pruebas
* **Comprobar compilación sin arrancar servidor:**
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

## Integración con Frontend Angular
* **Backend:** Expone servicios REST en `http://localhost:8080/api/`
* **CORS:** Habilitado para recibir peticiones y cabeceras JWT desde `http://localhost:4200`
* **Credenciales de prueba:**
  * Admin: `admin.jwt@tobyfit.local` / `DemoAdmin2026!`
  * Usuario: `usuario.jwt@tobyfit.local` / `DemoUsuario2026!`

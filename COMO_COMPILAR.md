# Gym TobyFit — Compilar y Ejecutar

## 📋 Requisito
* **Java 21** (`java -version`)

---

## 🚀 1. Iniciar en Modo Demo (Recomendado — No requiere MySQL)
Levanta el backend con base de datos en memoria (H2) y datos de prueba precargados automáticamente:

```powershell
cd gym-tobyfit-vs\gym-tobyfit
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

---

## 🗄️ 2. Iniciar con MySQL (XAMPP)
1. Inicia **Apache** y **MySQL** desde XAMPP.
2. Abre phpMyAdmin en: `http://localhost/phpmyadmin`
3. Importa el archivo único de base de datos:
   `gym-tobyfit-vs\gym-tobyfit\sql\gym_tobyfit.sql`
4. Ejecuta el backend:
```powershell
cd gym-tobyfit-vs\gym-tobyfit
.\mvnw.cmd spring-boot:run
```

---

## 🌐 3. Backend y Angular
* **Backend:** `http://localhost:8080`
* **Conexión con Angular:** El backend ya tiene CORS configurado y está 100% listo para empezar a conectarse con Angular (`http://localhost:4200`).

---

## 🔑 Cuentas de Acceso (Demo)
* **Administrador:** `admin.jwt@tobyfit.local` / `DemoAdmin2026!`
* **Usuario:** `usuario.jwt@tobyfit.local` / `DemoUsuario2026!`

---

## 🛠️ Comandos útiles
* **Recompilar si algo falla:**
  ```powershell
  .\mvnw.cmd clean spring-boot:run "-Dspring-boot.run.profiles=demo"
  ```
* **Solo verificar compilación:**
  ```powershell
  .\mvnw.cmd clean compile
  ```

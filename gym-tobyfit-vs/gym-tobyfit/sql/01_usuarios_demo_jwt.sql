-- Cuentas ficticias para probar JWT con la base MySQL/MariaDB del proyecto.
-- Ejecutar DESPUÉS de importar gym_tobyfit.sql, en una base de desarrollo.
-- Conserva los cuatro usuarios originales. Repetir este script no modifica cuentas existentes.
USE gym_tobyfit;

-- admin.jwt@tobyfit.local / DemoAdmin2026! (solo demostración)
INSERT INTO usuarios (nombre, apellido, telefono, edad, correo, contrasena, rol)
SELECT 'Admin', 'Demo', '900000001', 25, 'admin.jwt@tobyfit.local', '$2b$10$J1fZ/LXS6Nmger6luDB0su1sXcwqbX2jGAX4TRgpS6Xx5Kar5tERq', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = 'admin.jwt@tobyfit.local');

-- usuario.jwt@tobyfit.local / DemoUsuario2026! (solo demostración)
INSERT INTO usuarios (nombre, apellido, telefono, edad, correo, contrasena, rol)
SELECT 'Usuario', 'Demo', '900000002', 22, 'usuario.jwt@tobyfit.local', '$2b$10$j1cb0oVJ/OXO8eRYNLkUG.MCHlCwRsmKkmofyVDoTAYbj/tu5EYNW', 'USUARIO'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = 'usuario.jwt@tobyfit.local');

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-11-2026 a las 04:43:14
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gym_tobyfit`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `membresias`
--

CREATE TABLE `membresias` (
  `id` bigint(20) NOT NULL,
  `activa` bit(1) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `duracion_dias` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `membresias`
--

INSERT INTO `membresias` (`id`, `activa`, `descripcion`, `duracion_dias`, `nombre`, `precio`) VALUES
(1, b'1', 'Acceso a sala de entrenamiento. Precio de ejemplo editable por el administrador.', 30, 'Plan mensual esencial', 89),
(2, b'1', 'Tres meses de entrenamiento. Confirma horarios y condiciones en recepción.', 90, 'Plan trimestral progreso', 239),
(3, b'1', 'Acceso anual. Precio de ejemplo; revisar antes de ofrecer a clientes.', 365, 'Plan anual constancia', 799);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `genero` varchar(20) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `imagen_principal` varchar(2048) NOT NULL,
  `imagenes` text DEFAULT NULL,
  `modelos` text DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `stock` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `nombre`, `categoria`, `genero`, `precio`, `imagen_principal`, `imagenes`, `modelos`, `activo`, `stock`) VALUES
(1, 'GYMRAT – Polo Oversize', 'polo', 'hombre', 49.00, '/img/polo_hombre.png', '[\"/img/polo_hombre.png\", \"/img/polo_hombre_1.png\"]', '[\"Negro\"]', 1, 0),
(2, 'GYMRAT – Polo Oversize', 'polo', 'mujer', 45.00, '/img/polo_mujer.webp', '[\"/img/polo_mujer.webp\", \"/img/polo_mujer_1.webp\"]', '[\"Negro\", \"Gris\"]', 1, 0),
(3, 'GymRat – Jogger Oversize', 'buzo', 'hombre', 85.00, '/img/buzo_hombre.jpg', '[\"/img/buzo_hombre.jpg\", \"/img/buzo_hombre_1.jpg\"]', '[\"Negro\", \"Rojo\"]', 1, 0),
(4, 'Training | Running Joggers | Plomo Unisex', 'buzo', 'mujer', 125.00, '/img/buzo_mujer.webp', '[\"/img/buzo_mujer.webp\", \"/img/buzo_mujer_1.webp\"]', '[\"Gris\"]', 1, 0),
(5, 'Ultra Running Shorts', 'short', 'hombre', 110.00, '/img/short_hombre.webp', '[\"/img/short_hombre.webp\", \"/img/short_hombre_1.webp\"]', '[\"Negro\", \"Beige\"]', 1, 0),
(6, 'Short De Licra', 'short', 'mujer', 40.00, '/img/short_mujer.jpg', '[\"/img/short_mujer.jpg\", \"/img/short_mujer_1.jpg\"]', '[\"Gris\", \"Celeste\"]', 1, 0),
(7, 'Spider Man – Polo compresor', 'polo', 'hombre', 65.00, '/img/polo_compresor.jpg', '[\"/img/polo_compresor.jpg\", \"/img/polo_compresor_1.jpg\", \"/img/polo_compresor_2.jpg\", \"/img/polo_compresor_3.jpg\"]', '[\"Negro\", \"Gris\", \"Rojo\", \"Morado\"]', 1, 0),
(8, 'Vividi', 'vividi', 'hombre', 59.00, '/img/vividi_hombre.webp', '[\"/img/vividi_hombre.webp\", \"/img/vividi_hombre_1.webp\"]', '[\"Negro\", \"Gris\"]', 1, 0),
(9, 'Guantes Deportivos con Muñequeras', 'accesorios', 'todos', 60.00, '/img/guantes.webp', '[\"/img/guantes.webp\", \"/img/guantes_1.webp\"]', '[\"Negro\"]', 1, 0),
(10, 'Proteína Whey Gold', 'proteina', 'todos', 200.00, '/img/proteina_whey_gold.webp', '[\"/img/proteina_whey_gold.webp\", \"/img/proteina_whey_gold_1.webp\"]', '[\"3 kg\"]', 1, 0),
(11, 'Proteína BigM', 'proteina', 'todos', 140.00, '/img/proteina_bigm.png', '[\"/img/proteina_bigm.png\", \"/img/proteina_bigm_1.png\"]', '[\"5 kg\"]', 1, 0),
(12, 'CREATINA OPTIMUM NUTRITION MONOHIDRATADA', 'creatina', 'todos', 230.00, '/img/creatina_optimun_nutrition.webp', '[\"/img/creatina_optimun_nutrition.webp\", \"/img/creatina_optimun_nutrition_1.webp\"]', '[\"600 gr\"]', 1, 0),
(13, 'CREATINA ULTIMATE NUTRITION MONOHIDRATADA', 'creatina', 'todos', 225.00, '/img/creatina_ultimate_nutrition.webp', '[\"/img/creatina_ultimate_nutrition.webp\", \"/img/creatina_ultimate_nutrition_1.webp\"]', '[\"1 kg\"]', 1, 0),
(14, 'Quemador HD Paquete 15 Botellas de 135ml L-Carnitina', 'quemadores', 'todos', 30.00, '/img/quemador_L-Carnitina.webp', '[\"/img/quemador_L-Carnitina.webp\", \"/img/quemador_L-Carnitina_1.webp\", \"/img/quemador_L-Carnitina_2.webp\", \"/img/quemador_L-Carnitina_3.webp\"]', '[\"Piña\", \"BlueBerry\", \"Guarana\"]', 1, 0),
(15, 'Pre Entreno Nox Up', 'preentrenos', 'todos', 90.00, '/img/preentreno_noxup.webp', '[\"/img/preentreno_noxup.webp\", \"/img/preentreno_noxup_1.webp\"]', '[\"600 gr\"]', 1, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `promociones`
--

CREATE TABLE `promociones` (
  `id` bigint(20) NOT NULL,
  `activa` bit(1) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `descripcion` varchar(500) NOT NULL,
  `fin` date NOT NULL,
  `inicio` date NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `promociones`
--

INSERT INTO `promociones` (`id`, `activa`, `categoria`, `descripcion`, `fin`, `inicio`, `nombre`, `precio`) VALUES
(1, b'0', 'Membresías', 'Ejemplo: primer mes con tarifa especial para nuevos clientes. El administrador debe revisar y activar.', '2026-11-12', '2026-10-13', 'Bienvenida mensual', 79.00),
(2, b'0', 'Membresías', 'Ejemplo: tres meses de entrenamiento a tarifa promocional. Confirmar condiciones y activar desde la web.', '2026-11-12', '2026-10-13', 'Pack progreso trimestral', 219.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rutinas`
--

CREATE TABLE `rutinas` (
  `id` bigint(20) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `duracion_semanas` int(11) NOT NULL,
  `nivel` varchar(30) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `objetivo` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rutinas`
--

INSERT INTO `rutinas` (`id`, `descripcion`, `duracion_semanas`, `nivel`, `nombre`, `objetivo`) VALUES
(1, 'Tres sesiones por semana: tren superior, tren inferior y cuerpo completo. Adaptar con el entrenador.', 4, 'Principiante', 'Inicio de fuerza', 'Aprender técnica y fuerza general'),
(2, 'Cuatro sesiones por semana alternando tren superior e inferior. Ajustar cargas con supervisión.', 8, 'Intermedio', 'Fuerza progresiva', 'Mejorar fuerza muscular'),
(3, 'Combinar movilidad, cardio moderado y ejercicios generales; adaptar a la evaluación individual.', 6, 'Principiante', 'Movimiento y resistencia', 'Mejorar resistencia general');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `edad` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `rol` enum('ADMIN','USUARIO') DEFAULT NULL,
  `telefono` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `apellido`, `contrasena`, `correo`, `edad`, `nombre`, `rol`, `telefono`) VALUES
(1, 'CARBONELL COLLAZOS', '$2a$10$0M/ufessIxAZA1LnhAYsQe2yA/Xpc.KbVNfTZ1ymQYB4Bs74kyoH6', 'fabrizzio@gmail.com', 20, 'FABRIZZIO EDUARDO', 'USUARIO', '925966285'),
(2, 'CORREA ANGELES', '$2a$10$ExYCr0GnUQ77ma3of5ZDLO00PiHHvRpp8PQ5eLxYVCLqQtnPhuLqm', 'daleth@gmail.com', 20, 'DALETH', 'USUARIO', '972154855'),
(3, 'Tiznado', '$2a$10$KHWGZqfC1C6rRMVUNMcipeIf3DaXzzkhrTYJWx.vb6qabjV2vsudm', 'fabrizio@gmail.com', 20, 'Fabrizio', 'USUARIO', '915990023'),
(4, 'Espinoza Berrios', '$2a$10$3D3/.TxuK.Y.33R0cwdi3uiN0CsDdVg96eANaeZdUhKfCUM2V.CG6', 'juliocesar@gmail.com', 20, 'Julio Cesar', 'USUARIO', '947219716'),
(5, 'Demo', '$2b$10$J1fZ/LXS6Nmger6luDB0su1sXcwqbX2jGAX4TRgpS6Xx5Kar5tERq', 'admin.jwt@tobyfit.local', 25, 'Admin', 'ADMIN', '900000001'),
(6, 'Demo', '$2b$10$j1cb0oVJ/OXO8eRYNLkUG.MCHlCwRsmKkmofyVDoTAYbj/tu5EYNW', 'usuario.jwt@tobyfit.local', 22, 'Usuario', 'USUARIO', '900000002');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `membresias`
--
ALTER TABLE `membresias`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `promociones`
--
ALTER TABLE `promociones`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `rutinas`
--
ALTER TABLE `rutinas`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `membresias`
--
ALTER TABLE `membresias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `promociones`
--
ALTER TABLE `promociones`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `rutinas`
--
ALTER TABLE `rutinas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

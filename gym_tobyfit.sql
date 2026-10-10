-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Base de datos: gym_tobyfit
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";
SET NAMES utf8mb4;

-- --------------------------------------------------------
-- CREAR Y SELECCIONAR BASE DE DATOS
-- --------------------------------------------------------

CREATE DATABASE IF NOT EXISTS `gym_tobyfit`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE `gym_tobyfit`;

-- --------------------------------------------------------
-- TABLA productos
-- --------------------------------------------------------

DROP TABLE IF EXISTS `productos`;

CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `genero` varchar(20) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `imagen_principal` varchar(255) NOT NULL,
  `imagenes` text DEFAULT NULL,
  `modelos` text DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------
-- DATOS productos
-- --------------------------------------------------------

INSERT INTO `productos`
(`id`, `nombre`, `categoria`, `genero`, `precio`, `imagen_principal`, `imagenes`, `modelos`, `activo`)
VALUES
(1, 'GYMRAT – Polo Oversize', 'polo', 'hombre', 49.00, '/img/polo_hombre.png', '[\"/img/polo_hombre.png\", \"/img/polo_hombre_1.png\"]', '[\"Negro\"]', 1),
(2, 'GYMRAT – Polo Oversize', 'polo', 'mujer', 45.00, '/img/polo_mujer.webp', '[\"/img/polo_mujer.webp\", \"/img/polo_mujer_1.webp\"]', '[\"Negro\", \"Gris\"]', 1),
(3, 'GymRat – Jogger Oversize', 'buzo', 'hombre', 85.00, '/img/buzo_hombre.jpg', '[\"/img/buzo_hombre.jpg\", \"/img/buzo_hombre_1.jpg\"]', '[\"Negro\", \"Rojo\"]', 1),
(4, 'Training | Running Joggers | Plomo Unisex', 'buzo', 'mujer', 125.00, '/img/buzo_mujer.webp', '[\"/img/buzo_mujer.webp\", \"/img/buzo_mujer_1.webp\"]', '[\"Gris\"]', 1),
(5, 'Ultra Running Shorts', 'short', 'hombre', 110.00, '/img/short_hombre.webp', '[\"/img/short_hombre.webp\", \"/img/short_hombre_1.webp\"]', '[\"Negro\", \"Beige\"]', 1),
(6, 'Short De Licra', 'short', 'mujer', 40.00, '/img/short_mujer.jpg', '[\"/img/short_mujer.jpg\", \"/img/short_mujer_1.jpg\"]', '[\"Gris\", \"Celeste\"]', 1),
(7, 'Spider Man – Polo compresor', 'polo', 'hombre', 65.00, '/img/polo_compresor.jpg', '[\"/img/polo_compresor.jpg\", \"/img/polo_compresor_1.jpg\", \"/img/polo_compresor_2.jpg\", \"/img/polo_compresor_3.jpg\"]', '[\"Negro\", \"Gris\", \"Rojo\", \"Morado\"]', 1),
(8, 'Vividi', 'vividi', 'hombre', 59.00, '/img/vividi_hombre.webp', '[\"/img/vividi_hombre.webp\", \"/img/vividi_hombre_1.webp\"]', '[\"Negro\", \"Gris\"]', 1),
(9, 'Guantes Deportivos con Muñequeras', 'accesorios', 'todos', 60.00, '/img/guantes.webp', '[\"/img/guantes.webp\", \"/img/guantes_1.webp\"]', '[\"Negro\"]', 1),
(10, 'Proteína Whey Gold', 'proteina', 'todos', 200.00, '/img/proteina_whey_gold.webp', '[\"/img/proteina_whey_gold.webp\", \"/img/proteina_whey_gold_1.webp\"]', '[\"3 kg\"]', 1),
(11, 'Proteína BigM', 'proteina', 'todos', 140.00, '/img/proteina_bigm.png', '[\"/img/proteina_bigm.png\", \"/img/proteina_bigm_1.png\"]', '[\"5 kg\"]', 1),
(12, 'CREATINA OPTIMUM NUTRITION MONOHIDRATADA', 'creatina', 'todos', 230.00, '/img/creatina_optimun_nutrition.webp', '[\"/img/creatina_optimun_nutrition.webp\", \"/img/creatina_optimun_nutrition_1.webp\"]', '[\"600 gr\"]', 1),
(13, 'CREATINA ULTIMATE NUTRITION MONOHIDRATADA', 'creatina', 'todos', 225.00, '/img/creatina_ultimate_nutrition.webp', '[\"/img/creatina_ultimate_nutrition.webp\", \"/img/creatina_ultimate_nutrition_1.webp\"]', '[\"1 kg\"]', 1),
(14, 'Quemador HD Paquete 15 Botellas de 135ml L-Carnitina', 'quemadores', 'todos', 30.00, '/img/quemador_L-Carnitina.webp', '[\"/img/quemador_L-Carnitina.webp\", \"/img/quemador_L-Carnitina_1.webp\", \"/img/quemador_L-Carnitina_2.webp\", \"/img/quemador_L-Carnitina_3.webp\"]', '[\"Piña\", \"BlueBerry\", \"Guarana\"]', 1),
(15, 'Pre Entreno Nox Up', 'preentrenos', 'todos', 90.00, '/img/preentreno_noxup.webp', '[\"/img/preentreno_noxup.webp\", \"/img/preentreno_noxup_1.webp\"]', '[\"600 gr\"]', 1);

-- --------------------------------------------------------
-- TABLA usuarios
-- --------------------------------------------------------

DROP TABLE IF EXISTS `usuarios`;

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `apellido` varchar(50) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `edad` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `rol` enum('ADMIN','USUARIO') DEFAULT NULL,
  `telefono` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_correo` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------
-- DATOS usuarios
-- --------------------------------------------------------

INSERT INTO `usuarios`
(`id`, `apellido`, `contrasena`, `correo`, `edad`, `nombre`, `rol`, `telefono`)
VALUES
(1, 'Alvarez Bacilio', '$2a$10$0M/ufessIxAZA1LnhAYsQe2yA/Xpc.KbVNfTZ1ymQYB4Bs74kyoH6', 'juan@gmail.com', 20, 'Juan Carlos Henry', 'USUARIO', '925966285'),
(2, 'Rivera', '$2a$10$ExYCr0GnUQ77ma3of5ZDLO00PiHHvRpp8PQ5eLxYVCLqQtnPhuLqm', 'elias@gmail.com', 20, 'Elias', 'USUARIO', '972154855'),
(3, 'Tiznado', '$2a$10$KHWGZqfC1C6rRMVUNMcipeIf3DaXzzkhrTYJWx.vb6qabjV2vsudm', 'fabrizio@gmail.com', 20, 'Fabrizio', 'USUARIO', '915990023'),
(4, 'Torrez', '$2a$10$3D3/.TxuK.Y.33R0cwdi3uiN0CsDdVg96eANaeZdUhKfCUM2V.CG6', 'willian@gmail.com', 20, 'Willian', 'USUARIO', '947219716');

-- --------------------------------------------------------
-- FINALIZAR
-- --------------------------------------------------------

COMMIT;

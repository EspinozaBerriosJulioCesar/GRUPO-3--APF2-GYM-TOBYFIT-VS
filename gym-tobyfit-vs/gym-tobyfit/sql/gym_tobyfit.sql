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
(8, 'Vividi', 'vividi', 'hombre', 59.00, '/img/vividi_hombre.webp', '[\"/img/vividi_hombre.webp\", \"/img/vividi_hombre_1.webp\"]', '[\"Negro\", \"Gris\"]', 1);

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
(1, 'Demo', '$2b$10$J1fZ/LXS6Nmger6luDB0su1sXcwqbX2jGAX4TRgpS6Xx5Kar5tERq', 'admin.jwt@tobyfit.local', 25, 'Admin', 'ADMIN', '900000001'),
(2, 'Demo', '$2b$10$j1cb0oVJ/OXO8eRYNLkUG.MCHlCwRsmKkmofyVDoTAYbj/tu5EYNW', 'usuario.jwt@tobyfit.local', 22, 'Usuario', 'USUARIO', '900000002');

-- --------------------------------------------------------
-- FINALIZAR
-- --------------------------------------------------------

COMMIT;

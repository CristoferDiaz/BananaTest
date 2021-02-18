-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-02-2021 a las 11:13:34
-- Versión del servidor: 10.4.14-MariaDB
-- Versión de PHP: 7.2.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bananatest`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bt_categorias`
--

CREATE TABLE `bt_categorias` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `creador` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bt_contiene`
--

CREATE TABLE `bt_contiene` (
  `idExamen` int(11) NOT NULL,
  `idPregunta` int(11) NOT NULL,
  `peso` int(11) NOT NULL
) ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bt_examenes`
--

CREATE TABLE `bt_examenes` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcionGeneral` varchar(255) NOT NULL,
  `clave` varchar(12) NOT NULL,
  `creador` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bt_pertenece`
--

CREATE TABLE `bt_pertenece` (
  `idPregunta` int(11) NOT NULL,
  `idCategoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bt_preguntas`
--

CREATE TABLE `bt_preguntas` (
  `id` int(11) NOT NULL,
  `tipoPregunta` char(5) NOT NULL,
  `contenido` varchar(255) NOT NULL,
  `creador` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bt_respuestas`
--

CREATE TABLE `bt_respuestas` (
  `id` int(11) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `valida` tinyint(1) NOT NULL,
  `idPregunta` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bt_tipopregunta`
--

CREATE TABLE `bt_tipopregunta` (
  `codTipo` char(5) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `bt_tipopregunta`
--

INSERT INTO `bt_tipopregunta` (`codTipo`, `nombre`, `descripcion`) VALUES
('MULT', 'TestMultiple', 'Una pregunta con más de 1 respuesta posible'),
('SIMP', 'TestSimple', 'Una pregunta con 1 respuestas posibles');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bt_usuarios`
--

CREATE TABLE `bt_usuarios` (
  `id` int(11) NOT NULL,
  `codUsuario` varchar(255) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `passwd` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `bt_categorias`
--
ALTER TABLE `bt_categorias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `creador` (`creador`);

--
-- Indices de la tabla `bt_contiene`
--
ALTER TABLE `bt_contiene`
  ADD PRIMARY KEY (`idExamen`,`idPregunta`),
  ADD KEY `idPregunta` (`idPregunta`);

--
-- Indices de la tabla `bt_examenes`
--
ALTER TABLE `bt_examenes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `creador` (`creador`);

--
-- Indices de la tabla `bt_pertenece`
--
ALTER TABLE `bt_pertenece`
  ADD PRIMARY KEY (`idPregunta`,`idCategoria`),
  ADD KEY `idCategoria` (`idCategoria`);

--
-- Indices de la tabla `bt_preguntas`
--
ALTER TABLE `bt_preguntas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tipoPregunta` (`tipoPregunta`),
  ADD KEY `creador` (`creador`);

--
-- Indices de la tabla `bt_respuestas`
--
ALTER TABLE `bt_respuestas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idPregunta` (`idPregunta`),
  ADD KEY `idPregunta_2` (`idPregunta`);

--
-- Indices de la tabla `bt_tipopregunta`
--
ALTER TABLE `bt_tipopregunta`
  ADD PRIMARY KEY (`codTipo`);

--
-- Indices de la tabla `bt_usuarios`
--
ALTER TABLE `bt_usuarios`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `bt_categorias`
--
ALTER TABLE `bt_categorias`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `bt_examenes`
--
ALTER TABLE `bt_examenes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `bt_preguntas`
--
ALTER TABLE `bt_preguntas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT de la tabla `bt_respuestas`
--
ALTER TABLE `bt_respuestas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT de la tabla `bt_usuarios`
--
ALTER TABLE `bt_usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `bt_categorias`
--
ALTER TABLE `bt_categorias`
  ADD CONSTRAINT `bt_categorias_ibfk_1` FOREIGN KEY (`creador`) REFERENCES `bt_usuarios` (`id`);

--
-- Filtros para la tabla `bt_contiene`
--
ALTER TABLE `bt_contiene`
  ADD CONSTRAINT `bt_contiene_ibfk_1` FOREIGN KEY (`idExamen`) REFERENCES `bt_examenes` (`id`),
  ADD CONSTRAINT `bt_contiene_ibfk_2` FOREIGN KEY (`idPregunta`) REFERENCES `bt_preguntas` (`id`);

--
-- Filtros para la tabla `bt_examenes`
--
ALTER TABLE `bt_examenes`
  ADD CONSTRAINT `bt_examenes_ibfk_1` FOREIGN KEY (`creador`) REFERENCES `bt_usuarios` (`id`);

--
-- Filtros para la tabla `bt_pertenece`
--
ALTER TABLE `bt_pertenece`
  ADD CONSTRAINT `bt_pertenece_ibfk_1` FOREIGN KEY (`idPregunta`) REFERENCES `bt_preguntas` (`id`),
  ADD CONSTRAINT `bt_pertenece_ibfk_2` FOREIGN KEY (`idCategoria`) REFERENCES `bt_categorias` (`id`);

--
-- Filtros para la tabla `bt_preguntas`
--
ALTER TABLE `bt_preguntas`
  ADD CONSTRAINT `bt_preguntas_ibfk_1` FOREIGN KEY (`tipoPregunta`) REFERENCES `bt_tipopregunta` (`codTipo`),
  ADD CONSTRAINT `bt_preguntas_ibfk_2` FOREIGN KEY (`creador`) REFERENCES `bt_usuarios` (`id`);

--
-- Filtros para la tabla `bt_respuestas`
--
ALTER TABLE `bt_respuestas`
  ADD CONSTRAINT `bt_respuestas_ibfk_1` FOREIGN KEY (`idPregunta`) REFERENCES `bt_preguntas` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

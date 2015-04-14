-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 04, 2015 alle 01:35
-- Versione del server: 5.5.39
-- PHP Version: 5.4.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `db_es_2_pag181`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `auto`
--

CREATE TABLE IF NOT EXISTS `auto` (
  `targa` char(7) NOT NULL,
  `marca` varchar(15) NOT NULL,
  `modello` varchar(15) NOT NULL,
  `costo_giornallero` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `auto`
--

INSERT INTO `auto` (`targa`, `marca`, `modello`, `costo_giornallero`) VALUES
('PQCA123', 'Fiat', 'Panda', 30),
('PC07O74', 'Toyota', 'Yaris', 40),
('OACS122', 'Fiat', 'Panda', 30),
('ABCD123', 'Fiat', 'Panda', 30),
('ZXC1234', 'Toyota', 'Yaris', 40);

-- --------------------------------------------------------

--
-- Struttura della tabella `noleggi`
--

CREATE TABLE IF NOT EXISTS `noleggi` (
`codice_noleggio` int(5) unsigned NOT NULL,
  `auto` char(7) NOT NULL,
  `socio` char(16) NOT NULL,
  `inizio` date NOT NULL,
  `fine` date NOT NULL,
  `auto_restituita` bit(1) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=41 ;

--
-- Dump dei dati per la tabella `noleggi`
--

INSERT INTO `noleggi` (`codice_noleggio`, `auto`, `socio`, `inizio`, `fine`, `auto_restituita`) VALUES
(1, 'ZXC1234', 'ASDASDG123456789', '2005-12-01', '2005-12-02', b'1'),
(2, 'PQCA123', 'ASDASDG123456789', '2012-01-04', '2012-03-07', b'0'),
(3, 'ABCD123', 'OPOAA34324R12256', '2011-11-18', '2011-12-20', b'1');

-- --------------------------------------------------------

--
-- Struttura della tabella `soci`
--

CREATE TABLE IF NOT EXISTS `soci` (
  `CF` char(16) NOT NULL,
  `cognome` varchar(20) NOT NULL,
  `nome` varchar(20) NOT NULL,
  `indirizzo` varchar(30) NOT NULL,
  `telefono` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `soci`
--

INSERT INTO `soci` (`CF`, `cognome`, `nome`, `indirizzo`, `telefono`) VALUES
('ASDASDG12345ZX89', 'Acanfora1', 'Andrea1', 'Ind1', '123456789')
('OPOAA34324R12256', 'Acanfora3', 'Andrea3', 'Ind3', '123456789'),
('ZQE1231284RT2345', 'Acanfora2', 'Andrea2', 'Ind2', '123456789');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `auto`
--
ALTER TABLE `auto`
 ADD PRIMARY KEY (`targa`);

--
-- Indexes for table `noleggi`
--
ALTER TABLE `noleggi`
 ADD PRIMARY KEY (`codice_noleggio`);

--
-- Indexes for table `soci`
--
ALTER TABLE `soci`
 ADD PRIMARY KEY (`CF`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `noleggi`
--
ALTER TABLE `noleggi`
MODIFY `codice_noleggio` int(5) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

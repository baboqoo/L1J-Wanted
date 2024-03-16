-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 15, 2024 at 04:40 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `l1j_remastered`
--

-- --------------------------------------------------------

--
-- Table structure for table `map_balance`
--

CREATE TABLE `map_balance` (
  `mapId` mediumint(5) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `damageType` enum('both','attack','magic') NOT NULL DEFAULT 'both',
  `damageValue` float NOT NULL DEFAULT 1,
  `reductionType` enum('both','attack','magic') NOT NULL DEFAULT 'both',
  `reductionValue` float NOT NULL DEFAULT 1,
  `expValue` float NOT NULL DEFAULT 1,
  `dropValue` float NOT NULL DEFAULT 1,
  `adenaValue` float NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=euckr COLLATE=euckr_korean_ci;

--
-- Dumping data for table `map_balance`
--

INSERT INTO `map_balance` (`mapId`, `name`, `damageType`, `damageValue`, `reductionType`, `reductionValue`, `expValue`, `dropValue`, `adenaValue`) VALUES
(1, 'Talking Island Dungeon 1F', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(2, 'Talking Island Dungeon 2F', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(3, 'Red Knight Training Camp', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(7, 'Red Knights Swamp', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(8, 'Hidden Path', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(9, 'Talking Island Field', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(10, 'Black Battleship 1F', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(11, 'Black Battleship 2F', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(12, 'Black Battleship 3F', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(25, 'Silver Knight Dungeon 1F', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(26, 'Silver Knight Dungeon 2F', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(27, 'Silver Knight Dungeon 3F', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(28, 'Silver Knight Dungeon 4F', 'both', 0.2, 'both', 1.8, 0.9, 1, 1),
(70, 'Forgotten Island (Local)', 'both', 1.5, 'both', 0.5, 1.2, 1, 1),
(101, 'Tower of Insolence 1F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(102, 'Tower of Insolence 2F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(103, 'Tower of Insolence 3F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(104, 'Tower of Insolence 4F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(105, 'Tower of Insolence 5F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(106, 'Tower of Insolence 6F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(107, 'Tower of Insolence 7F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(108, 'Tower of Insolence 8F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(109, 'Tower of Insolence 9F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(110, 'Tower of Insolence 10F', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(111, 'Tower of Insolence - Summit', 'both', 1.4, 'both', 0.7, 1.1, 1, 1),
(402, 'Hidden Ivory Tower (Boost)', 'both', 1, 'both', 1, 1, 1.5, 1.5),
(403, 'Hidden Land of Castaways Boost', 'both', 1, 'both', 1, 1, 1.5, 1.5),
(404, 'Hidden Eva Kingdom Boost', 'both', 1, 'both', 1, 1, 1.5, 1.5),
(405, 'Hidden Atuba Orc Lair Boost', 'both', 1, 'both', 1, 1, 1.5, 1.5),
(777, 'Land of Abandoned', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(778, 'Land of Abandoned (PC)', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(779, 'Hidden Land of Abandoned', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(1700, 'Forgotten Island', 'both', 1.5, 'both', 0.8, 1, 1, 1),
(1701, 'Forgotten Island', 'both', 1.5, 'both', 0.8, 1, 1, 1),
(1702, 'Forgotten Island', 'both', 1.5, 'both', 0.8, 1, 1, 1),
(1703, 'Golem Lab', 'both', 1.5, 'both', 0.8, 1, 1, 1),
(1704, 'Golem Lab', 'both', 1.5, 'both', 0.8, 1, 1, 1),
(1705, 'Golem Lab', 'both', 1.5, 'both', 0.8, 1, 1, 1),
(1707, 'Golem Lab', 'both', 1.5, 'both', 0.8, 1, 1, 1),
(1708, 'Forgotten Island', 'both', 1.5, 'both', 0.8, 1, 1, 1),
(1709, 'Golem Lab', 'both', 1.5, 'both', 0.8, 1, 1, 1),
(3050, 'Temple of Extreme Cold (Hard) (empty)', 'both', 1.5, 'both', 0.5, 1, 1, 1),
(12146, 'Spider Cave', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(12147, 'Sancutary of Dead 1F', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(12148, 'Sancutary of Dead 2F', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(12149, 'Underworld', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(12150, 'Einhasad Temple', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(12152, 'Claudia Castle', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(12157, 'Temple of Einhasad 1', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(12358, 'Temple of Einhasad 2', 'both', 0.2, 'both', 1.8, 1, 1, 1),
(12852, 'Tower of Doimination 1F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12853, 'Tower of Domination 2F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12854, 'Tower of Domination 3F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12855, 'Tower of Domination 4F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12856, 'Tower of Domination 5F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12857, 'Tower of Domination 6F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12858, 'Tower of Domination 7F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12859, 'Tower of Domination 8F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12860, 'Tower of Domination 9F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12861, 'Tower of Domination 10F', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12862, 'Tower of Domination (Top)', 'both', 1.3, 'both', 0.8, 1.1, 1, 1),
(12900, 'Land of Outcast - West (Inter)', 'both', 0.8, 'both', 1.2, 1, 1, 1),
(15881, 'Hidden Ant Cave - Monarch', 'both', 1.5, 'both', 1, 1, 1, 1),
(15882, 'Hidden Ant Cave - Knight', 'both', 1.5, 'both', 1, 1, 1, 1),
(15883, 'Hidden Ant Cave - Elf', 'both', 1.5, 'both', 1, 1, 1, 1),
(15884, 'Hidden Ant Cave - Wizard', 'both', 1.5, 'both', 1, 1, 1, 1),
(15885, 'Hidden Ant Cave - Dark Elf', 'both', 1.5, 'both', 1, 1, 1, 1),
(15886, 'Hidden Ant Cave - Dragon Knight', 'both', 1.5, 'both', 1, 1, 1, 1),
(15887, 'Hidden Ant Cave - Illusionist', 'both', 1.5, 'both', 1, 1, 1, 1),
(15888, 'Hidden Ant Cave - Warrior', 'both', 1.5, 'both', 1, 1, 1, 1),
(15889, 'Hidden Ant Cave - Fencer', 'both', 1.5, 'both', 1, 1, 1, 1),
(15901, 'Hidden Ant Cave - Lancer', 'both', 1.5, 'both', 1, 1, 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `map_balance`
--
ALTER TABLE `map_balance`
  ADD PRIMARY KEY (`mapId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

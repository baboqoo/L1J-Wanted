-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 15, 2024 at 09:07 AM
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
-- Table structure for table `notification`
--

CREATE TABLE `notification` (
  `notification_id` int(6) NOT NULL DEFAULT 0,
  `notification_type` enum('NORMAL(0)','CHANGE(1)') NOT NULL DEFAULT 'NORMAL(0)',
  `is_use` enum('true','false') NOT NULL DEFAULT 'true',
  `is_hyperlink` enum('true','false') NOT NULL DEFAULT 'false',
  `displaydesc` varchar(50) DEFAULT NULL,
  `displaydesc_kr` varchar(50) DEFAULT NULL,
  `date_type` enum('NONE(0)','CUSTOM(1)','BOSS(2)','DOMINATION_TOWER(3)','COLOSSEUM(4)','TREASURE(5)') NOT NULL DEFAULT 'NONE(0)',
  `date_boss_id` int(10) NOT NULL DEFAULT 0,
  `date_custom_start` datetime DEFAULT NULL,
  `date_custom_end` datetime DEFAULT NULL,
  `teleport_loc` text DEFAULT NULL,
  `rest_gauge_bonus` int(4) NOT NULL DEFAULT 0,
  `is_new` enum('true','false') NOT NULL DEFAULT 'false',
  `animation_type` enum('NO_ANIMATION(0)','ANT_QUEEN(1)','OMAN_MORPH(2)','AI_BATTLE(3)') NOT NULL DEFAULT 'NO_ANIMATION(0)'
) ENGINE=InnoDB DEFAULT CHARSET=euckr COLLATE=euckr_korean_ci;

--
-- Dumping data for table `notification`
--

INSERT INTO `notification` (`notification_id`, `notification_type`, `is_use`, `is_hyperlink`, `displaydesc`, `displaydesc_kr`, `date_type`, `date_boss_id`, `date_custom_start`, `date_custom_end`, `teleport_loc`, `rest_gauge_bonus`, `is_new`, `animation_type`) VALUES
(1, 'NORMAL(0)', 'false', 'true', '$1506', 'Ant Queen', 'BOSS(2)', 5136, NULL, NULL, '4, 32892, 33225', 300, 'false', 'NO_ANIMATION(0)'),
(2, 'NORMAL(0)', 'true', 'true', '$11993', 'Sand Worm', 'BOSS(2)', 5135, NULL, NULL, '4, 32732, 33141', 300, 'false', 'NO_ANIMATION(0)'),
(3, 'NORMAL(0)', 'false', 'false', '$14740', 'Red Knight\'s Adance', 'NONE(0)', 0, NULL, NULL, '4, 32595, 33167', 0, 'false', 'NO_ANIMATION(0)'),
(4, 'NORMAL(0)', 'false', 'false', '$42714', 'Ancient Merchant', 'NONE(0)', 0, NULL, NULL, '4, 32620, 32789', 0, 'false', 'NO_ANIMATION(0)'),
(5, 'NORMAL(0)', 'false', 'false', '$23228', 'Forgotten Island', 'NONE(0)', 0, NULL, NULL, '4, 33431, 33499', 0, 'false', 'NO_ANIMATION(0)'),
(16, 'NORMAL(0)', 'true', 'false', '$29760', 'Domination Tower', 'DOMINATION_TOWER(3)', 0, NULL, NULL, '4, 33930, 33348', 0, 'false', 'NO_ANIMATION(0)'),
(18, 'NORMAL(0)', 'false', 'false', '$42716', 'Doll Race', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)'),
(19, 'NORMAL(0)', 'false', 'false', '$42717', 'Server Shop', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)'),
(20, 'NORMAL(0)', 'true', 'false', '$30708', 'Infinite War', 'COLOSSEUM(4)', 0, NULL, NULL, '4, 33498, 32768', 100, 'false', 'NO_ANIMATION(0)'),
(21, 'NORMAL(0)', 'false', 'false', '$31212', 'Secret Merchant', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)'),
(22, 'NORMAL(0)', 'false', 'false', '$31946', 'Colosseum Battle', 'COLOSSEUM(4)', 0, NULL, NULL, '4, 33498, 32768', 200, 'false', 'NO_ANIMATION(0)'),
(23, 'CHANGE(1)', 'true', 'false', '$32010', 'Ant Queen\'s Nest', 'NONE(0)', 0, NULL, NULL, '4, 32854, 33267', 0, 'false', 'ANT_QUEEN(1)'),
(24, 'NORMAL(0)', 'false', 'false', '$13911', 'Hidden Areas', 'CUSTOM(1)', 0, '2022-09-25 06:00:00', '2022-09-30 06:00:00', NULL, 0, 'true', 'NO_ANIMATION(0)'),
(25, 'NORMAL(0)', 'false', 'false', '$42718', 'Clan Shop', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)'),
(26, 'CHANGE(1)', 'true', 'false', '$33543', 'Tower of Insolence Rift', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'OMAN_MORPH(2)'),
(30, 'NORMAL(0)', 'true', 'false', '$36722', 'Treasure Island', 'TREASURE(5)', 0, NULL, NULL, NULL, 300, 'false', 'NO_ANIMATION(0)'),
(33, 'CHANGE(1)', 'true', 'false', '$35454', 'Heine Conquest', 'NONE(0)', 0, NULL, NULL, '4, 33431, 33500', 0, 'false', 'NO_ANIMATION(0)'),
(35, 'CHANGE(1)', 'true', 'false', '$36529', 'Windawood Conquest', 'NONE(0)', 0, NULL, NULL, '4, 32585, 33438', 0, 'false', 'NO_ANIMATION(0)'),
(100, 'CHANGE(1)', 'true', 'false', '$36108', 'Mirror War', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'AI_BATTLE(3)'),
(101, 'NORMAL(0)', 'false', 'false', '$36348', 'Medal of Valor', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`notification_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

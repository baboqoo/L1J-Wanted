-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 29, 2024 at 05:51 AM
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
-- Table structure for table `hunting_quest`
--

CREATE TABLE `hunting_quest` (
  `area_name` varchar(50) NOT NULL DEFAULT '',
  `map_number` int(6) NOT NULL DEFAULT 0,
  `location_desc` int(6) DEFAULT NULL,
  `quest_id` int(6) NOT NULL DEFAULT 0,
  `is_use` enum('true','false') NOT NULL DEFAULT 'true'
) ENGINE=InnoDB DEFAULT CHARSET=euckr COLLATE=euckr_korean_ci;

--
-- Dumping data for table `hunting_quest`
--

INSERT INTO `hunting_quest` (`area_name`, `map_number`, `location_desc`, `quest_id`, `is_use`) VALUES
('Mirror Forest', 4, 468, 1, 'true'),
('Wilderness', 4, 481, 2, 'true'),
('Black Battleship 1F', 10, NULL, 3, 'true'),
('Black Battleship 2F', 11, NULL, 4, 'true'),
('Black Battleship 3F', 12, NULL, 5, 'true'),
('Ancient Giants Tomb (PC)', 624, NULL, 6, 'true'),
('Tomb of Ancient Spirit', 1209, NULL, 7, 'true'),
('Tomb of Ancient Spirit PC)', 430, NULL, 8, 'true'),
('Quite Forest', 4, 649, 9, 'true'),
('Gludin Dungeon 1F', 807, NULL, 10, 'true'),
('Gludin Dungeon 2F', 808, NULL, 11, 'true'),
('Gludin Dungeon 3F', 809, NULL, 12, 'true'),
('Gludin Dungeon 4F', 810, NULL, 13, 'true'),
('Gludin Dungeon 5F', 811, NULL, 14, 'true'),
('Gludin Dungeon 6F', 812, NULL, 15, 'true'),
('Gludin Dungeon 7F', 813, NULL, 16, 'true'),
('Giran Prison 1F', 53, NULL, 17, 'true'),
('Giran Prison 2F', 54, NULL, 18, 'true'),
('Lindvior\'s Lair', 1182, NULL, 19, 'false'),
('Talking Island Dungeon 1F', 1, NULL, 20, 'true'),
('Talking Island Dungeon 2F', 2, NULL, 21, 'true'),
('Talking Island Field', 9, NULL, 22, 'true'),
('Grave of the Dead', 4, 479, 23, 'true'),
('Dry Desert', 4, 657, 24, 'true'),
('Dream Island', 733, NULL, 25, 'true'),
('Jungle', 4, 466, 26, 'true'),
('Valakas Lair', 1183, NULL, 27, 'false'),
('Land of Abandon', 777, NULL, 28, 'true'),
('Swamp of Red Knights', 7, NULL, 29, 'true'),
('Ivory Tower 4F', 282, NULL, 30, 'true'),
('Ivory Tower 5F', 283, NULL, 31, 'true'),
('Ivory Tower 6F', 284, NULL, 32, 'true'),
('Ivory Tower 7F', 285, NULL, 33, 'true'),
('Training Dungeon 1F', 25, NULL, 34, 'true'),
('Training Dungeon 2F', 26, NULL, 35, 'true'),
('Training Dungeon 3F', 27, NULL, 36, 'true'),
('Training Dungeon 4F', 28, NULL, 37, 'true'),
('Hidden Path', 8, NULL, 38, 'true'),
('Hidden Land of Abandon', 779, NULL, 39, 'true'),
('Hidden Ivory Tower', 293, NULL, 40, 'true'),
('Hidden Atuba Orc Hideout', 494, NULL, 41, 'true'),
('Hidden Eva Kingdom', 62, NULL, 42, 'true'),
('Continent of Aden', 4, 0, 43, 'false'),
('Atuba Orc Hideout 1F', 491, NULL, 44, 'true'),
('Atuba Orc Hideout 2F', 492, NULL, 45, 'true'),
('Atuba Orc Hideout 3F', 493, NULL, 46, 'true'),
('The Secret of Crocodile Island', 732, NULL, 47, 'false'),
('Anthara\'s Lair', 1180, NULL, 48, 'false'),
('Dark Dragon Dungeon 1F', 1318, NULL, 49, 'true'),
('Dark Dragon Dungeon 2F', 1319, NULL, 50, 'true'),
('Dark Dragon Scar', 4, 465, 51, 'true'),
('Eva Kingdom Waterway 1F', 59, NULL, 52, 'true'),
('Eva Kingdom Waterway 2F', 60, NULL, 53, 'true'),
('Eva Kingdom Waterway 3F', 61, NULL, 54, 'true'),
('Elmore Sand', 4, 328, 55, 'true'),
('Queen Ant Nest', 15891, NULL, 56, 'false'),
('Oren Snow', 15420, NULL, 57, 'true'),
('Orim\'s Secret Lab', 731, NULL, 58, 'false'),
('Tower of Insolence 10F', 110, NULL, 59, 'true'),
('Tower of Insolence 1F', 101, NULL, 60, 'true'),
('Tower of Insolence 2F', 102, NULL, 61, 'true'),
('Tower of Insolence 3F', 103, NULL, 62, 'true'),
('Tower of Insolence 4F', 104, NULL, 63, 'true'),
('Tower of Insolence 5F', 105, NULL, 64, 'true'),
('Tower of Insolence 6F', 106, NULL, 65, 'true'),
('Tower of Insolence 7F', 107, NULL, 66, 'true'),
('Tower of Insolence 8F', 108, NULL, 67, 'true'),
('Tower of Insolence 9F', 109, NULL, 68, 'true'),
('Tower of Insolence Top', 111, NULL, 69, 'true'),
('Orc Colony', 4, 463, 70, 'true'),
('Elven Dungeon 1F', 19, NULL, 71, 'true'),
('Elven Dungeon 2F', 20, NULL, 72, 'true'),
('Elven Dungeon 3F', 21, NULL, 73, 'true'),
('Dragon Valley', 15430, NULL, 74, 'true'),
('Dragon Valley Cave 1F', 30, NULL, 75, 'true'),
('Dragon Valley Cave 2F', 31, NULL, 76, 'true'),
('Dragon Valley Cave 3F', 32, NULL, 77, 'true'),
('Dragon Valley Cave 4F', 33, NULL, 78, 'true'),
('Dragon Valley Cave 5F', 35, NULL, 79, 'true'),
('Dragon Valley Cave 6F', 36, NULL, 80, 'true'),
('Dragon Valley Cave 7F', 37, NULL, 81, 'true'),
('Dragon Valley Beach', 4, 653, 82, 'true'),
('Unicorn Temple', 1936, NULL, 83, 'false'),
('Silver Knight Field', 4, 650, 84, 'true'),
('Concealed Ant Cave', 15881, NULL, 85, 'false'),
('Ruins of Death', 4, 464, 86, 'true'),
('Tower of Domination 10F', 12861, NULL, 87, 'true'),
('Tower of Domination 1F', 12852, NULL, 88, 'true'),
('Tower of Domination 2F', 12853, NULL, 89, 'true'),
('Tower of Domination 3F', 12854, NULL, 90, 'true'),
('Tower of Domination 4F', 12855, NULL, 91, 'true'),
('Tower of Domination 5F', 12856, NULL, 92, 'true'),
('Tower of Domination 6F', 12857, NULL, 93, 'true'),
('Tower of Domination 7F', 12858, NULL, 94, 'true'),
('Tower of Domination 8F', 12859, NULL, 95, 'true'),
('Tower of Domination 9F', 12860, NULL, 96, 'true'),
('Tower of Domination Top', 12862, NULL, 97, 'true'),
('Fafurion Lair', 1181, NULL, 98, 'false'),
('Wind Dragon Nest', 15410, NULL, 99, 'true'),
('Heine Forest', 4, 342, 100, 'true'),
('Temple of Extreme Cold', 3000, NULL, 101, 'true'),
('Temple of Extreme Cold (Hard)', 3050, NULL, 102, 'true'),
('Valakas Lair', 15440, NULL, 103, 'true'),
('Badlands', 4, 652, 104, 'true'),
('Ruun Square', 4000, NULL, 105, 'true'),
('Ruun Castle', 4001, NULL, 106, 'true'),
('Cursed Mine', 4, 666, 107, 'true'),
('SKT Dungeon 1F', 7531, NULL, 108, 'true'),
('SKT Dungeon 2F', 7532, NULL, 109, 'true'),
('SKT Dungeon 3F', 7533, NULL, 110, 'true'),
('SKT Dungeon 4F', 7534, NULL, 111, 'true');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `hunting_quest`
--
ALTER TABLE `hunting_quest`
  ADD PRIMARY KEY (`quest_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

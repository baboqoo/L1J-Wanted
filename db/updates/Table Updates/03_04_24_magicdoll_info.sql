-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 29, 2024 at 07:09 AM
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
-- Table structure for table `magicdoll_info`
--

CREATE TABLE `magicdoll_info` (
  `itemId` int(11) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `dollNpcId` int(11) NOT NULL DEFAULT 0,
  `blessItemId` int(11) DEFAULT 0,
  `grade` int(2) NOT NULL DEFAULT 0,
  `bonusItemId` int(11) NOT NULL DEFAULT 0,
  `bonusCount` int(11) NOT NULL DEFAULT 0,
  `bonusInterval` int(11) NOT NULL DEFAULT 0,
  `damageChance` int(3) NOT NULL DEFAULT 0,
  `attackSkillEffectId` int(5) NOT NULL DEFAULT 0,
  `haste` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr COLLATE=euckr_korean_ci;

--
-- Dumping data for table `magicdoll_info`
--

INSERT INTO `magicdoll_info` (`itemId`, `name`, `dollNpcId`, `blessItemId`, `grade`, `bonusItemId`, `bonusCount`, `bonusInterval`, `damageChance`, `attackSkillEffectId`, `haste`) VALUES
(740, 'Magic Doll: Woodcarving', 200074, 0, 1, 0, 0, 0, 0, 0, 'false'),
(741, 'Magic Doll: Lava Golem', 508, 0, 2, 0, 0, 0, 0, 0, 'false'),
(742, 'Magic Doll: Diamond Golem', 509, 2505, 3, 0, 0, 0, 0, 0, 'false'),
(743, 'Magic Doll: Knightvald', 510, 2508, 4, 0, 0, 0, 0, 0, 'false'),
(744, 'Magic Doll: Sheer', 511, 2509, 4, 0, 0, 0, 0, 0, 'false'),
(745, 'Magic Doll: Demon', 512, 2513, 5, 0, 0, 0, 0, 0, 'false'),
(746, 'Magic Doll: Death Knight', 513, 2514, 5, 0, 0, 0, 0, 13576, 'false'),
(750, 'Magic Doll: Snowman', 900233, 0, 2, 0, 0, 0, 0, 0, 'false'),
(751, 'Magic Doll: Mummy Lord', 514, 2512, 4, 0, 0, 0, 0, 0, 'false'),
(752, 'Magic Doll: Corruption', 515, 2516, 5, 0, 0, 0, 0, 0, 'false'),
(753, 'Magic Doll: Jade Rabbit', 516, 0, 0, 31102, 1, 900, 0, 0, 'false'),
(754, 'Magic Doll: Golden Bugbear', 754, 0, 0, 31102, 1, 900, 0, 0, 'false'),
(756, 'Magic Doll: Kylo Shukken', 756, 0, 0, 8004, 1, 900, 0, 0, 'false'),
(757, 'Magic Doll: Dragon Slayer', 757, 0, 0, 0, 0, 0, 0, 0, 'false'),
(775, 'Magic Doll: True Death Knight Emissary', 775, 0, 0, 0, 0, 0, 0, 0, 'false'),
(776, 'Magic Doll: Halfas', 776, 0, 6, 0, 0, 0, 0, 0, 'false'),
(777, 'Magic Doll: Aurakia', 777, 0, 6, 0, 0, 0, 0, 0, 'false'),
(778, 'Magic Doll: Behemoth', 778, 0, 6, 0, 0, 0, 0, 0, 'false'),
(2500, '[B] Magic Doll: Giant', 2500, 0, 3, 0, 0, 0, 0, 0, 'false'),
(2501, '[B] Magic Doll: Black Elder', 2501, 0, 3, 0, 0, 0, 0, 11736, 'false'),
(2502, '[B] Magic Doll: Succubus Queen', 2502, 0, 3, 0, 0, 0, 0, 0, 'false'),
(2503, '[B] Magic Doll: Drake', 2503, 0, 3, 0, 0, 0, 0, 0, 'false'),
(2504, '[B] Magic Doll: King Bugbear', 2504, 0, 3, 0, 0, 0, 0, 0, 'false'),
(2505, '[B] Magic Doll: Diamond Golem', 2505, 0, 3, 0, 0, 0, 0, 0, 'false'),
(2506, '[B] Magic Doll: Lich', 2506, 0, 4, 0, 0, 0, 0, 0, 'false'),
(2507, '[B] Magic Doll: Cyclops', 2507, 0, 4, 0, 0, 0, 0, 0, 'false'),
(2508, '[B] Magic Doll: Knightvald', 2508, 0, 4, 0, 0, 0, 0, 0, 'false'),
(2509, '[B] Magic Doll: Sheer', 2509, 0, 4, 0, 0, 0, 0, 0, 'false'),
(2510, '[B] Magic Doll: Iris', 2510, 0, 4, 0, 0, 0, 0, 0, 'false'),
(2511, '[B] Magic Doll: Vampire', 2511, 0, 4, 0, 0, 0, 0, 0, 'false'),
(2512, '[B] Magic Doll: Mummy Lord', 2512, 0, 4, 0, 0, 0, 0, 0, 'false'),
(2513, '[B] Magic Doll: Demon', 2513, 0, 5, 0, 0, 0, 0, 0, 'false'),
(2514, '[B] Magic Doll: Death Knight', 2514, 0, 5, 0, 0, 0, 0, 13576, 'false'),
(2515, '[B] Magic Doll: Barranca', 2515, 0, 5, 0, 0, 0, 0, 0, 'false'),
(2516, '[B] Magic Doll: Corruption', 2516, 0, 5, 0, 0, 0, 0, 0, 'false'),
(2517, '[B] Magic Doll: Baphomet', 2517, 0, 5, 0, 0, 0, 0, 0, 'false'),
(2518, '[B] Magic Doll: Ice Queen', 2518, 0, 5, 0, 0, 0, 0, 0, 'false'),
(2519, '[B] Magic Doll: Kurtz', 2519, 0, 5, 0, 0, 0, 0, 0, 'false'),
(30022, 'Magic Doll: Balrog', 5074, 0, 0, 0, 0, 0, 0, 1809, 'false'),
(30023, 'Magic Doll: Ledeg', 5075, 0, 0, 0, 0, 0, 0, 1583, 'false'),
(30024, 'Magic Doll: Elreg', 5076, 0, 0, 0, 0, 0, 0, 7331, 'false'),
(30025, 'Magic Doll: Greg', 5077, 0, 0, 0, 0, 0, 0, 4022, 'false'),
(31126, 'Magic Doll: Baphomet', 517, 2517, 5, 0, 0, 0, 0, 0, 'false'),
(31127, 'Magic Doll: Ice Queen', 518, 2518, 5, 0, 0, 0, 0, 0, 'false'),
(31128, 'Magic Doll: Kurtz', 519, 2519, 5, 0, 0, 0, 0, 0, 'false'),
(31155, 'Magic Doll: Antharas', 520, 31673, 5, 0, 0, 0, 0, 0, 'false'),
(31156, 'Magic Doll: Fafurion', 521, 31674, 5, 0, 0, 0, 0, 0, 'false'),
(31157, 'Magic Doll: Lindvior', 522, 31671, 5, 0, 0, 0, 0, 0, 'false'),
(31158, 'Magic Doll: Valakas', 523, 31672, 5, 0, 0, 0, 0, 0, 'false'),
(31671, '[B] Magic Doll: Lindvior', 18760, 0, 5, 0, 0, 0, 0, 0, 'false'),
(31672, '[B] Magic Doll: Valakas', 18761, 0, 5, 0, 0, 0, 0, 0, 'false'),
(31673, '[B] Magic Doll: Antharas', 18762, 0, 5, 0, 0, 0, 0, 0, 'false'),
(31674, '[B] Magic Doll: Fafurion', 18763, 0, 5, 0, 0, 0, 0, 0, 'false'),
(41248, 'Magic Doll: Bugbear', 80106, 0, 1, 0, 0, 0, 0, 0, 'false'),
(41249, 'Magic Doll: Succubus', 80107, 0, 2, 0, 0, 0, 0, 0, 'false'),
(41250, 'Magic Doll: Werewolf', 80108, 0, 1, 0, 0, 0, 5, 0, 'false'),
(131816, 'Magic Doll: Rat', 18693, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131817, 'Magic Doll: Cow', 18817, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131818, 'Magic Doll: Tiger', 18818, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131819, 'Magic Doll: Rabbit', 18819, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131820, 'Magic Doll: Dragon', 18820, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131821, 'Magic Doll: Snake', 18821, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131822, 'Magic Doll: Horse', 18822, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131823, 'Magic Doll: Sheep', 18823, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131824, 'Magic Doll: Monkey', 18824, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131825, 'Magic Doll: Chicken', 18825, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131826, 'Magic Doll: Dog', 18826, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(131827, 'Magic Doll: Pig', 18827, 0, 0, 31680, 1, 120, 0, 0, 'false'),
(210070, 'Magic Doll: Stone Golem', 200017, 0, 1, 0, 0, 0, 0, 0, 'false'),
(210071, 'Magic Doll: Elder', 200018, 0, 2, 0, 0, 0, 0, 0, 'false'),
(210072, 'Magic Doll: Crustacean', 200019, 0, 1, 0, 0, 0, 5, 0, 'false'),
(210086, 'Magic Doll: Seadancer', 200068, 0, 0, 0, 0, 0, 0, 0, 'false'),
(210096, 'Magic Doll: Yeti', 507, 0, 1, 0, 0, 0, 0, 0, 'false'),
(210105, 'Magic Doll: Cockatrice', 200012, 0, 2, 0, 0, 0, 0, 0, 'false'),
(210106, 'Magic Doll: Female Hatchling', 200008, 0, 0, 40024, 1, 240, 0, 0, 'false'),
(210107, 'Magic Doll: Male Hatchling', 200009, 0, 0, 40024, 1, 240, 0, 0, 'false'),
(210108, 'Magic Doll: Female Hatchling (Evolved)', 200010, 0, 0, 40024, 1, 240, 0, 0, 'false'),
(210109, 'Magic Doll: Male Hatchling (Evolved)', 200011, 0, 0, 40024, 1, 240, 0, 0, 'false'),
(410172, 'Magic Doll: Mermaid', 81212, 0, 2, 0, 0, 0, 0, 0, 'false'),
(410173, 'Magic Doll: King Bugbear', 81213, 2504, 3, 0, 0, 0, 0, 0, 'false'),
(410515, '[Templar] Magic Doll: Spartoi', 900500, 0, 0, 0, 0, 0, 0, 0, 'false'),
(447012, 'Magic Doll: Champion', 900220, 0, 0, 0, 0, 0, 0, 0, 'false'),
(447013, 'Magic Doll: New', 900221, 0, 0, 0, 0, 0, 0, 0, 'false'),
(447014, 'Magic Doll: Gangnam Style', 900222, 0, 0, 0, 0, 0, 0, 0, 'false'),
(447015, 'Magic Doll: Gremlin', 900223, 0, 0, 0, 0, 0, 0, 0, 'false'),
(447016, 'Magic Doll: Lich', 900224, 2506, 4, 0, 0, 0, 0, 0, 'false'),
(447017, 'Magic Doll: Drake', 900225, 2503, 3, 0, 0, 0, 0, 0, 'false'),
(500212, 'Magic Doll: Dandi', 900176, 0, 0, 31134, 1, 900, 0, 0, 'false'),
(500213, 'Magic Doll: Ettin', 900178, 0, 0, 0, 0, 0, 0, 0, 'true'),
(500214, 'Magic Doll: Spartoi', 900179, 0, 0, 0, 0, 0, 0, 0, 'false'),
(500215, 'Magic Doll: Scarecrow', 900180, 0, 0, 0, 0, 0, 0, 0, 'false'),
(510216, 'Magic Doll: Snowman A', 900226, 0, 0, 0, 0, 0, 0, 0, 'false'),
(510217, 'Magic Doll: Snowman B', 900227, 0, 0, 0, 0, 0, 0, 0, 'false'),
(510218, 'Magic Doll: Snowman C', 900228, 0, 0, 0, 0, 0, 0, 0, 'false'),
(510219, 'Magic Doll: Giant', 900229, 2500, 3, 0, 0, 0, 0, 0, 'false'),
(510220, 'Magic Doll: Cyclops', 900230, 2507, 4, 0, 0, 0, 0, 0, 'false'),
(510221, 'Magic Doll: Black Elder', 900231, 2501, 3, 0, 0, 0, 0, 11736, 'false'),
(510222, 'Magic Doll: Succubus Queen', 900232, 2502, 3, 0, 0, 0, 0, 0, 'false'),
(510223, 'Magic Doll: Specialization', 900234, 0, 0, 0, 0, 0, 0, 0, 'false'),
(3000086, 'Magic Doll: Iris', 7310082, 2510, 4, 0, 0, 0, 0, 0, 'false'),
(3000087, 'Magic Doll: Vampire', 7310083, 2511, 4, 0, 0, 0, 0, 0, 'false'),
(3000088, 'Magic Doll: Barranca', 7310084, 2515, 5, 0, 0, 0, 0, 0, 'false');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `magicdoll_info`
--
ALTER TABLE `magicdoll_info`
  ADD PRIMARY KEY (`itemId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

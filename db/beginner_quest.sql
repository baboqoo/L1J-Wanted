-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 29, 2024 at 05:52 AM
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
-- Table structure for table `beginner_quest`
--

CREATE TABLE `beginner_quest` (
  `quest_id` int(10) NOT NULL,
  `note` varchar(45) CHARACTER SET euckr COLLATE euckr_korean_ci NOT NULL DEFAULT '',
  `desc_kr` varchar(45) CHARACTER SET euckr COLLATE euckr_korean_ci NOT NULL,
  `use` enum('true','false') CHARACTER SET euckr COLLATE euckr_korean_ci NOT NULL DEFAULT 'true',
  `auto_complete` enum('false','true') CHARACTER SET euckr COLLATE euckr_korean_ci NOT NULL DEFAULT 'false',
  `fastLevel` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `beginner_quest`
--

INSERT INTO `beginner_quest` (`quest_id`, `note`, `desc_kr`, `use`, `auto_complete`, `fastLevel`) VALUES
(256, 'Use the item', '아이템 사용', 'false', 'false', 0),
(257, 'Attack training', '공격 훈련', 'false', 'false', 0),
(258, 'Food Gathering', '식량 수집', 'false', 'false', 0),
(259, 'Open Your Eyes', '눈을 뜨자', 'false', 'false', 0),
(260, 'Polymorph', '변신', 'false', 'false', 0),
(261, 'Fight', '전투', 'false', 'false', 0),
(262, 'Armor Enchantment', '갑옷 인챈', 'false', 'false', 0),
(263, 'Collecting Bone Fragments', '뼈조각 수집', 'false', 'false', 0),
(264, 'Weapon Enchantment', '무기 인챈', 'false', 'false', 0),
(265, 'Black Knight Scouts', '흑기사단 정찰병 제거', 'false', 'false', 30),
(266, 'Read Black Knight Report', '흑기사단 침입 정보', 'false', 'false', 0),
(267, 'Apprentice Black Knight', '견습 흑기사단 방어', 'false', 'false', 0),
(268, 'Escape Scroll Used', '귀환 주문서 사용', 'false', 'false', 0),
(269, 'Theon', '테온', 'false', 'false', 0),
(270, 'Dwarf Kill -1', '드워프 처치 -1', 'false', 'false', 0),
(271, 'Join Pledge', '혈맹 가입', 'false', 'false', 0),
(272, 'Create Clan', '혈맹 창설', 'false', 'false', 0),
(273, 'Dwarf Kill -2', '드워프 처치 -2', 'false', 'false', 0),
(274, 'Dissolving agent used', '용해제 사용', 'false', 'false', 0),
(275, 'Summon Magic Doll', '마법 인형 소환', 'false', 'false', 0),
(276, 'Silver Weapon Crafting', '은 무기 제작', 'false', 'false', 0),
(277, 'Silver Weapon Mounted', '은 무기 장착', 'false', 'false', 0),
(278, 'The Dark Hunter', '어둠의 사냥꾼', 'false', 'false', 50),
(279, 'Reach level 50', '50레벨 달성', 'false', 'false', 0),
(280, 'Talking Island Dungeon', '말하는 섬 던전', 'false', 'false', 0),
(281, 'Mysterious Bone Piece', '이상한 뼛조각', 'false', 'false', 55),
(283, 'Reach Level 55', '레벨 55 달성', 'false', 'false', 0),
(284, 'Orc Horde -1', '오크 무리 -1', 'false', 'false', 0),
(285, 'Orc Horde -2', '오크 무리 -2', 'false', 'false', 60),
(287, 'Reach Level 60', '레벨 60 달성', 'false', 'false', 0),
(288, 'Cannibalistic Spiders', '식인 거미', 'false', 'false', 0),
(289, 'Mysterious Eyeballs', '이상한 눈알', 'false', 'false', 0),
(290, 'Cooking Effects', '요리의 효과', 'false', 'false', 65),
(291, 'Reach Level 65', '레벨 65 달성', 'false', 'false', 0),
(292, 'Scroll of Enhanced Battle', '전투 강화의 주문서', 'false', 'false', 0),
(293, 'Wandering Dark Two-Handed Axeman', '라이칸스로프', 'false', 'false', 70),
(294, 'Reach Level 70', '레벨 70 달성', 'false', 'false', 0),
(295, 'The Fallen Sage', '타락한 현자', 'false', 'false', 0),
(296, 'Black Knight Squad Camp', '흑기사단 캠프', 'false', 'false', 72),
(297, 'Reach Level 72', '레벨 72 달성', 'false', 'false', 0),
(298, 'Ornamental Gems', '보석 회수', 'false', 'false', 0),
(299, 'Black Knight', '검은 기사단', 'false', 'false', 74),
(300, 'Reach Level 74', '레벨 74 달성', 'false', 'false', 0),
(301, 'Kurtz\'s Lieutenant', '커츠의 부관', 'false', 'false', 75),
(302, 'Reach Level 75', '레벨 75 달성', 'false', 'false', 0),
(303, 'To Mainland', '본토로', 'false', 'false', 0),
(304, 'Stage 1 - Crafting Runes', '룬 만들기 1단계', 'false', 'false', 0),
(305, 'Craft Rune Stage 2', '룬 만들기 2단계', 'false', 'false', 0),
(306, 'Craft Rune Stage 3', '룬 만들기 3단계', 'false', 'false', 0),
(307, 'Craft Rune Stage 4', '룬 만들기 4단계', 'false', 'false', 0),
(308, 'Rune making', '룬 만들기', 'false', 'false', 0),
(309, 'Zones Learned', '구역 정보 확인', 'false', 'false', 0),
(361, 'Daily Quest', '일일 퀘스트', 'false', 'false', 0),
(365, 'Go Fishing!', '낚시를 하자', 'false', 'false', 0),
(367, 'Marvin\'s Request', '마빈의 부탁', 'false', 'false', 0),
(368, 'Play Support', '플레이서포트', 'false', 'false', 0),
(370, 'Reach Level 77', '레벨 77 달성', 'false', 'false', 0),
(371, 'Hidden Hunting Grounds', '숨겨진 사냥터', 'false', 'false', 0),
(372, 'Crafting', '통합 제작', 'false', 'false', 0),
(373, 'Instance Dungeon automatic matching', '인던 자동 매칭', 'false', 'false', 0),
(374, 'Einhasad Point', '아인하사드 포인트', 'false', 'false', 0),
(375, 'Land of Abandoned', '버림받은 자들의 땅', 'false', 'false', 0),
(377, 'Hunting Guide', '사냥터 도감', 'false', 'false', 0),
(378, 'Top Hunting Grounds Guide', '상위 사냥터 안내', 'false', 'true', 0),
(379, 'Elixir of Potential Enhancement', '잠재력 강화의 비약', 'false', 'false', 0),
(380, 'EXP Growth', '경험치 성장', 'false', 'false', 0),
(381, 'Medal of Valor', '용맹의 메달', 'false', 'false', 0),
(382, 'Silver Knight Dungeon 1F', '은기사 던전 1층', 'false', 'false', 0),
(383, 'Heroic Equipment EXP', '영웅의 장비 체험', 'false', 'false', 0),
(384, 'Silver Knight Dungeon 2F', '은기사 던전 2층', 'false', 'false', 0),
(385, 'Aden\'s Quick Protection', '아덴의 신속 가호', 'false', 'false', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `beginner_quest`
--
ALTER TABLE `beginner_quest`
  ADD PRIMARY KEY (`quest_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

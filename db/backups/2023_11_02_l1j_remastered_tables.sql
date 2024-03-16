-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 02-11-2023 a las 19:20:16
-- Versión del servidor: 10.4.14-MariaDB
-- Versión de PHP: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `l1j_remastered`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `0_translations`
--

CREATE TABLE `0_translations` (
  `id` int(11) NOT NULL,
  `text_korean` text NOT NULL,
  `text_english` text NOT NULL,
  `source` varchar(200) DEFAULT NULL,
  `line_number` int(11) DEFAULT NULL,
  `validated` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `accounts`
--

CREATE TABLE `accounts` (
  `login` varchar(50) NOT NULL DEFAULT '',
  `password` varchar(50) NOT NULL,
  `lastactive` datetime DEFAULT NULL,
  `lastQuit` datetime DEFAULT NULL,
  `access_level` int(11) NOT NULL DEFAULT 0,
  `ip` varchar(20) NOT NULL DEFAULT '',
  `host` varchar(20) NOT NULL DEFAULT '',
  `banned` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `charslot` int(11) NOT NULL DEFAULT 6,
  `warehouse_password` int(11) NOT NULL DEFAULT 0,
  `notice` varchar(20) DEFAULT '0',
  `quiz` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `hddId` varchar(255) DEFAULT NULL,
  `boardId` varchar(255) DEFAULT NULL,
  `Tam_Point` int(11) NOT NULL DEFAULT 0,
  `Buff_DMG_Time` datetime DEFAULT NULL,
  `Buff_Reduc_Time` datetime DEFAULT NULL,
  `Buff_Magic_Time` datetime DEFAULT NULL,
  `Buff_Stun_Time` datetime DEFAULT NULL,
  `Buff_Hold_Time` datetime DEFAULT NULL,
  `BUFF_PCROOM_Time` datetime DEFAULT NULL,
  `Buff_FireDefence_Time` datetime DEFAULT NULL,
  `Buff_EarthDefence_Time` datetime DEFAULT NULL,
  `Buff_WaterDefence_Time` datetime DEFAULT NULL,
  `Buff_WindDefence_Time` datetime DEFAULT NULL,
  `Buff_SoulDefence_Time` datetime DEFAULT NULL,
  `Buff_Str_Time` datetime DEFAULT NULL,
  `Buff_Dex_Time` datetime DEFAULT NULL,
  `Buff_Wis_Time` datetime DEFAULT NULL,
  `Buff_Int_Time` datetime DEFAULT NULL,
  `Buff_FireAttack_Time` datetime DEFAULT NULL,
  `Buff_EarthAttack_Time` datetime DEFAULT NULL,
  `Buff_WaterAttack_Time` datetime DEFAULT NULL,
  `Buff_WindAttack_Time` datetime DEFAULT NULL,
  `Buff_Hero_Time` datetime DEFAULT NULL,
  `Buff_Life_Time` datetime DEFAULT NULL,
  `second_password` varchar(11) DEFAULT NULL,
  `Ncoin` int(11) NOT NULL DEFAULT 0,
  `Npoint` int(11) NOT NULL DEFAULT 0,
  `Shop_open_count` int(6) NOT NULL DEFAULT 0,
  `DragonRaid_Buff` datetime DEFAULT NULL,
  `Einhasad` int(11) NOT NULL DEFAULT 0,
  `EinDayBonus` int(2) NOT NULL DEFAULT 0,
  `IndunCheckTime` datetime DEFAULT NULL,
  `IndunCount` int(2) NOT NULL DEFAULT 0,
  `app_char` int(10) NOT NULL DEFAULT 0,
  `app_terms_agree` enum('false','true') CHARACTER SET utf8 NOT NULL DEFAULT 'false',
  `PSSTime` int(11) NOT NULL DEFAULT 1800
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `accounts_free_buff_shield`
--

CREATE TABLE `accounts_free_buff_shield` (
  `account_name` varchar(50) NOT NULL,
  `favor_locked_time` int(4) NOT NULL DEFAULT 0,
  `pccafe_favor_remain_count` int(1) NOT NULL DEFAULT 0,
  `free_favor_remain_count` int(1) NOT NULL DEFAULT 0,
  `event_favor_remain_count` int(1) NOT NULL DEFAULT 0,
  `pccafe_reward_item_count` int(3) NOT NULL DEFAULT 0,
  `reset_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `accounts_pcmaster_golden`
--

CREATE TABLE `accounts_pcmaster_golden` (
  `account_name` varchar(50) NOT NULL,
  `index_id` int(1) NOT NULL DEFAULT 0,
  `type` int(1) NOT NULL DEFAULT 1,
  `grade` blob DEFAULT NULL,
  `remain_time` int(6) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `accounts_shop_limit`
--

CREATE TABLE `accounts_shop_limit` (
  `accountName` varchar(50) NOT NULL,
  `buyShopId` int(9) NOT NULL DEFAULT 0,
  `buyItemId` int(9) NOT NULL DEFAULT 0,
  `buyCount` int(9) NOT NULL DEFAULT 0,
  `buyTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  `limitTerm` enum('WEEK','DAY','NONE') NOT NULL DEFAULT 'DAY'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `adshop`
--

CREATE TABLE `adshop` (
  `account` varchar(13) NOT NULL,
  `name` varchar(13) NOT NULL,
  `sex` int(15) NOT NULL,
  `type` int(15) NOT NULL,
  `x` int(15) NOT NULL,
  `y` int(15) NOT NULL,
  `heading` int(15) NOT NULL,
  `map_id` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ai_user`
--

CREATE TABLE `ai_user` (
  `name` varchar(50) NOT NULL,
  `ai_type` enum('AI_BATTLE','COLOSEUM','HUNT','FISHING','TOWN_MOVE','SCARECROW_ATTACK') NOT NULL DEFAULT 'AI_BATTLE',
  `level` int(3) NOT NULL DEFAULT 0,
  `class` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown') DEFAULT 'crown',
  `gender` enum('MALE(0)','FEMALE(1)') NOT NULL DEFAULT 'MALE(0)',
  `str` int(3) NOT NULL DEFAULT 0,
  `con` int(3) NOT NULL DEFAULT 0,
  `dex` int(3) NOT NULL DEFAULT 0,
  `inti` int(3) NOT NULL DEFAULT 0,
  `wis` int(3) NOT NULL DEFAULT 0,
  `cha` int(3) NOT NULL DEFAULT 0,
  `alignment` int(6) NOT NULL DEFAULT 0,
  `hit` int(3) NOT NULL DEFAULT 0,
  `bow_hit` int(3) NOT NULL DEFAULT 0,
  `dmg` int(3) NOT NULL DEFAULT 0,
  `bow_dmg` int(3) NOT NULL DEFAULT 0,
  `reduction` int(3) NOT NULL DEFAULT 0,
  `skill_hit` int(3) NOT NULL DEFAULT 0,
  `spirit_hit` int(3) NOT NULL DEFAULT 0,
  `dragon_hit` int(3) NOT NULL DEFAULT 0,
  `magic_hit` int(3) NOT NULL DEFAULT 0,
  `fear_hit` int(3) NOT NULL DEFAULT 0,
  `skill_regist` int(3) NOT NULL DEFAULT 0,
  `spirit_regist` int(3) NOT NULL DEFAULT 0,
  `dragon_regist` int(3) NOT NULL DEFAULT 0,
  `fear_regist` int(3) NOT NULL DEFAULT 0,
  `dg` int(3) NOT NULL DEFAULT 0,
  `er` int(3) NOT NULL DEFAULT 0,
  `me` int(3) NOT NULL DEFAULT 0,
  `mr` int(3) NOT NULL DEFAULT 0,
  `hp` int(4) NOT NULL DEFAULT 0,
  `mp` int(4) NOT NULL DEFAULT 0,
  `hpr` int(3) NOT NULL DEFAULT 0,
  `mpr` int(3) NOT NULL DEFAULT 0,
  `title` varchar(50) DEFAULT NULL,
  `clanId` int(2) NOT NULL DEFAULT 0,
  `clanname` varchar(50) DEFAULT NULL,
  `elfAttr` int(2) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ai_user_buff`
--

CREATE TABLE `ai_user_buff` (
  `class` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown') NOT NULL DEFAULT 'crown',
  `elfAttr` int(2) NOT NULL DEFAULT 0,
  `buff` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ai_user_drop`
--

CREATE TABLE `ai_user_drop` (
  `class` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown','all') NOT NULL DEFAULT 'all',
  `itemId` int(10) NOT NULL DEFAULT 0,
  `name` varchar(100) DEFAULT NULL,
  `count` int(10) NOT NULL DEFAULT 1,
  `chance` int(3) NOT NULL DEFAULT 100
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ai_user_fish`
--

CREATE TABLE `ai_user_fish` (
  `loc_x` int(5) NOT NULL DEFAULT 0,
  `loc_y` int(5) NOT NULL DEFAULT 0,
  `heading` int(1) NOT NULL DEFAULT 0,
  `fish_x` int(5) NOT NULL DEFAULT 0,
  `fish_y` int(5) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ai_user_item`
--

CREATE TABLE `ai_user_item` (
  `class` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown','all') NOT NULL DEFAULT 'all',
  `itemId` int(10) NOT NULL DEFAULT 0,
  `name` varchar(100) DEFAULT NULL,
  `count` int(10) NOT NULL DEFAULT 1,
  `enchantLevel` int(2) NOT NULL DEFAULT 0,
  `attrLevel` int(2) NOT NULL DEFAULT 0,
  `equip` enum('false','true') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ai_user_ment`
--

CREATE TABLE `ai_user_ment` (
  `id` int(3) NOT NULL,
  `ment` varchar(100) DEFAULT NULL,
  `type` enum('login','logout','kill','death') DEFAULT 'login'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ai_user_skill`
--

CREATE TABLE `ai_user_skill` (
  `class` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown') NOT NULL DEFAULT 'crown',
  `active` text DEFAULT NULL,
  `passive` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_alim_log`
--

CREATE TABLE `app_alim_log` (
  `id` int(11) NOT NULL,
  `account_name` varchar(50) DEFAULT NULL,
  `logContent` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `type` int(2) NOT NULL DEFAULT 0,
  `insertTime` datetime DEFAULT NULL,
  `status` enum('false','true') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_auth_extension`
--

CREATE TABLE `app_auth_extension` (
  `extension` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_board_content`
--

CREATE TABLE `app_board_content` (
  `id` int(10) NOT NULL,
  `name` varchar(16) CHARACTER SET utf8 DEFAULT 'NULL',
  `date` datetime DEFAULT NULL,
  `title` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `content` mediumtext CHARACTER SET utf8 DEFAULT NULL,
  `readcount` int(10) DEFAULT 0,
  `chatype` int(2) DEFAULT 0,
  `chasex` int(1) DEFAULT 0,
  `likenames` text CHARACTER SET utf8 DEFAULT NULL,
  `mainImg` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `top` enum('true','false') CHARACTER SET utf8 NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_board_content_comment`
--

CREATE TABLE `app_board_content_comment` (
  `id` int(10) NOT NULL DEFAULT 0,
  `boardId` int(10) NOT NULL DEFAULT 0,
  `name` varchar(16) CHARACTER SET utf8 NOT NULL,
  `chaType` int(2) NOT NULL DEFAULT 0,
  `chaSex` int(1) NOT NULL DEFAULT 0,
  `date` datetime NOT NULL,
  `content` varchar(1000) CHARACTER SET utf8 NOT NULL,
  `likenames` text CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_board_free`
--

CREATE TABLE `app_board_free` (
  `id` int(10) NOT NULL,
  `name` varchar(16) CHARACTER SET utf8 DEFAULT 'NULL',
  `date` datetime DEFAULT NULL,
  `title` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `content` mediumtext CHARACTER SET utf8 DEFAULT NULL,
  `readcount` int(10) DEFAULT 0,
  `chatype` int(2) DEFAULT 0,
  `chasex` int(1) DEFAULT 0,
  `likenames` text CHARACTER SET utf8 DEFAULT NULL,
  `mainImg` varchar(100) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_board_free_comment`
--

CREATE TABLE `app_board_free_comment` (
  `id` int(10) NOT NULL DEFAULT 0,
  `boardId` int(10) NOT NULL DEFAULT 0,
  `name` varchar(16) CHARACTER SET utf8 NOT NULL,
  `chaType` int(2) NOT NULL DEFAULT 0,
  `chaSex` int(1) NOT NULL DEFAULT 0,
  `date` datetime NOT NULL,
  `content` varchar(1000) CHARACTER SET utf8 NOT NULL,
  `likenames` text CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_board_notice`
--

CREATE TABLE `app_board_notice` (
  `id` int(10) NOT NULL,
  `name` varchar(16) CHARACTER SET utf8 DEFAULT 'NULL',
  `date` datetime DEFAULT NULL,
  `title` varchar(200) CHARACTER SET utf8 DEFAULT 'NULL',
  `content` mediumtext CHARACTER SET utf8 DEFAULT NULL,
  `readcount` int(10) DEFAULT 0,
  `type` int(1) DEFAULT 0,
  `top` enum('false','true') CHARACTER SET utf8 NOT NULL DEFAULT 'false',
  `mainImg` varchar(100) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_board_pitch`
--

CREATE TABLE `app_board_pitch` (
  `id` int(10) NOT NULL,
  `name` varchar(16) CHARACTER SET utf8 DEFAULT 'NULL',
  `date` datetime DEFAULT NULL,
  `title` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `content` mediumtext CHARACTER SET utf8 DEFAULT NULL,
  `readcount` int(10) DEFAULT 0,
  `chatype` int(2) DEFAULT 0,
  `chasex` int(1) DEFAULT 0,
  `likenames` text CHARACTER SET utf8 DEFAULT NULL,
  `mainImg` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `top` enum('true','false') CHARACTER SET utf8 NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_board_pitch_comment`
--

CREATE TABLE `app_board_pitch_comment` (
  `id` int(10) NOT NULL DEFAULT 0,
  `boardId` int(10) NOT NULL DEFAULT 0,
  `name` varchar(16) CHARACTER SET utf8 NOT NULL,
  `chaType` int(2) NOT NULL DEFAULT 0,
  `chaSex` int(1) NOT NULL DEFAULT 0,
  `date` datetime NOT NULL,
  `content` varchar(1000) CHARACTER SET utf8 NOT NULL,
  `likenames` text CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_coupon`
--

CREATE TABLE `app_coupon` (
  `number` varchar(50) CHARACTER SET utf8 NOT NULL,
  `type` enum('수표','NCOIN','NPOINT') NOT NULL DEFAULT 'NCOIN',
  `price` int(11) NOT NULL DEFAULT 0,
  `status` enum('false','true') CHARACTER SET utf8 NOT NULL DEFAULT 'false',
  `useAccount` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `createTime` datetime NOT NULL,
  `useTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_customer`
--

CREATE TABLE `app_customer` (
  `id` int(10) NOT NULL DEFAULT 0,
  `login` varchar(45) CHARACTER SET utf8 NOT NULL,
  `type` int(1) NOT NULL DEFAULT 1,
  `title` varchar(150) CHARACTER SET utf8 NOT NULL,
  `content` text CHARACTER SET utf8 NOT NULL,
  `status` enum('접수 완료','답변 완료') CHARACTER SET utf8 NOT NULL DEFAULT '접수 완료',
  `date` datetime NOT NULL,
  `comment` text CHARACTER SET utf8 DEFAULT NULL,
  `commentDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_customer_normal`
--

CREATE TABLE `app_customer_normal` (
  `id` int(10) NOT NULL DEFAULT 0,
  `title` varchar(150) CHARACTER SET utf8 NOT NULL,
  `content` text CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_dictionary_item`
--

CREATE TABLE `app_dictionary_item` (
  `schar` varchar(50) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_engine_log`
--

CREATE TABLE `app_engine_log` (
  `id` int(10) NOT NULL DEFAULT 0,
  `account` varchar(50) NOT NULL,
  `engine` varchar(100) NOT NULL,
  `time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_guide`
--

CREATE TABLE `app_guide` (
  `id` int(2) NOT NULL DEFAULT 0,
  `title` varchar(100) CHARACTER SET utf8 NOT NULL,
  `content` mediumtext CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_guide_boss`
--

CREATE TABLE `app_guide_boss` (
  `id` int(2) NOT NULL DEFAULT 0,
  `loc` int(2) NOT NULL DEFAULT 0,
  `locName` varchar(50) CHARACTER SET utf8 NOT NULL,
  `number` int(2) NOT NULL DEFAULT 0,
  `bossName` varchar(50) CHARACTER SET utf8 NOT NULL,
  `bossImg` varchar(100) CHARACTER SET utf8 NOT NULL,
  `spawnLoc` varchar(500) CHARACTER SET utf8 NOT NULL,
  `spawnTime` varchar(500) CHARACTER SET utf8 NOT NULL,
  `dropName` varchar(500) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_guide_recommend`
--

CREATE TABLE `app_guide_recommend` (
  `id` int(1) NOT NULL DEFAULT 0,
  `title` varchar(100) CHARACTER SET utf8 NOT NULL,
  `content` varchar(100) CHARACTER SET utf8 NOT NULL,
  `url` varchar(100) CHARACTER SET utf8 NOT NULL,
  `img` varchar(100) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_item_search`
--

CREATE TABLE `app_item_search` (
  `seq` int(11) NOT NULL,
  `item_name` varchar(250) CHARACTER SET utf8 NOT NULL,
  `item_keyword` varchar(250) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_nshop`
--

CREATE TABLE `app_nshop` (
  `id` int(10) NOT NULL,
  `itemid` int(10) NOT NULL DEFAULT 0,
  `itemname` varchar(22) CHARACTER SET utf8 DEFAULT NULL,
  `price` int(10) NOT NULL DEFAULT 0,
  `price_type` enum('NCOIN','NPOINT') CHARACTER SET utf8 NOT NULL DEFAULT 'NCOIN',
  `saved_point` int(10) NOT NULL DEFAULT 0,
  `pack` int(10) NOT NULL DEFAULT 0,
  `enchant` int(10) NOT NULL DEFAULT 0,
  `limitCount` int(10) NOT NULL DEFAULT 50,
  `flag` enum('NONE','DISCOUNT','ESSENTIAL','HOT','LIMIT','LIMIT_MONTH','LIMIT_WEEK','NEW','REDKNIGHT') CHARACTER SET utf8 NOT NULL DEFAULT 'NONE',
  `iteminfo` varchar(500) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_page_info`
--

CREATE TABLE `app_page_info` (
  `uri` varchar(100) NOT NULL,
  `path` varchar(100) DEFAULT NULL,
  `className` varchar(100) NOT NULL,
  `cnbType` int(2) NOT NULL DEFAULT 0,
  `cnbSubType` int(2) NOT NULL DEFAULT 0,
  `needIngame` enum('true','false') NOT NULL DEFAULT 'false',
  `needLauncher` enum('true','false') NOT NULL DEFAULT 'false',
  `needLogin` enum('true','false') NOT NULL DEFAULT 'false',
  `needGm` enum('true','false') NOT NULL DEFAULT 'false',
  `Json` enum('true','false') NOT NULL DEFAULT 'false',
  `fileUpload` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_powerbook`
--

CREATE TABLE `app_powerbook` (
  `id` int(10) NOT NULL,
  `title` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT 'NULL',
  `content` mediumtext CHARACTER SET utf8 DEFAULT NULL,
  `mainImg` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `main` enum('true','false') CHARACTER SET utf8 NOT NULL DEFAULT 'false',
  `guideMain` enum('true','false') CHARACTER SET utf8 NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_powerbook_guide`
--

CREATE TABLE `app_powerbook_guide` (
  `group_type` enum('1.초보자 가이드','2.클래스','3.아이템','4.마법','5.마법인형','6.몬스터&던전','7.파티 컨텐츠','8.월드 컨텐츠','9.전투 시스템','10.커뮤니티','11.주요 시스템','12.서비스') CHARACTER SET utf8 NOT NULL DEFAULT '1.초보자 가이드',
  `id` int(2) NOT NULL DEFAULT 0,
  `title` varchar(100) CHARACTER SET utf8 NOT NULL,
  `uri` varchar(100) CHARACTER SET utf8 NOT NULL,
  `is_new` enum('true','false') CHARACTER SET utf8 NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_promotion`
--

CREATE TABLE `app_promotion` (
  `id` int(2) NOT NULL DEFAULT 0,
  `title` varchar(100) CHARACTER SET utf8 NOT NULL,
  `subText` varchar(100) CHARACTER SET utf8 NOT NULL,
  `promotionDate` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `targetLink` varchar(100) CHARACTER SET utf8 NOT NULL,
  `promotionImg` varchar(100) CHARACTER SET utf8 NOT NULL,
  `listallImg` varchar(100) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_report`
--

CREATE TABLE `app_report` (
  `id` int(10) NOT NULL DEFAULT 0,
  `name` varchar(45) CHARACTER SET utf8 NOT NULL,
  `targetName` varchar(45) CHARACTER SET utf8 NOT NULL,
  `type` enum('음란성','욕설','일반광고','불법프로그램','도배','개인정보','타인비방','기타') CHARACTER SET utf8 NOT NULL DEFAULT '음란성',
  `log` text CHARACTER SET utf8 DEFAULT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_shop_rank`
--

CREATE TABLE `app_shop_rank` (
  `group_type` enum('1.전체','2.무기','3.방어구','4.액세서리','5.기타') CHARACTER SET utf8 NOT NULL DEFAULT '1.전체',
  `shop_type` enum('1.판매','2.구매') CHARACTER SET utf8 NOT NULL DEFAULT '1.판매',
  `id` int(1) NOT NULL DEFAULT 0,
  `item_id` int(10) NOT NULL DEFAULT 0,
  `enchant` int(3) NOT NULL DEFAULT 0,
  `search_rank` int(2) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_support`
--

CREATE TABLE `app_support` (
  `id` int(10) NOT NULL DEFAULT 0,
  `account_name` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `character_name` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `pay_amount` int(10) NOT NULL DEFAULT 0,
  `write_date` datetime DEFAULT NULL,
  `status` enum('STANBY','FINISH') CHARACTER SET utf8 NOT NULL DEFAULT 'STANBY'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_support_message`
--

CREATE TABLE `app_support_message` (
  `type` enum('AGREE','PROGRESS','REWARD') CHARACTER SET utf8 NOT NULL DEFAULT 'AGREE',
  `index_id` int(2) NOT NULL DEFAULT 1,
  `content` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_support_request`
--

CREATE TABLE `app_support_request` (
  `id` int(10) NOT NULL DEFAULT 0,
  `account_name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `character_name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `request_date` datetime NOT NULL,
  `response` text CHARACTER SET utf8 DEFAULT NULL,
  `response_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_trade`
--

CREATE TABLE `app_trade` (
  `id` int(11) NOT NULL DEFAULT 0,
  `title` varchar(100) CHARACTER SET utf8 NOT NULL,
  `content` varchar(1000) CHARACTER SET utf8 NOT NULL,
  `bank` varchar(100) CHARACTER SET utf8 NOT NULL,
  `bankNumber` varchar(50) CHARACTER SET utf8 NOT NULL,
  `status` enum('판매','거래중','거래완료','거래취소') CHARACTER SET utf8 NOT NULL DEFAULT '판매',
  `sellerName` varchar(45) CHARACTER SET utf8 NOT NULL,
  `sellerCharacter` varchar(45) CHARACTER SET utf8 NOT NULL,
  `sellerPhone` varchar(20) CHARACTER SET utf8 NOT NULL,
  `buyerName` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `buyerCharacter` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `buyerPhone` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `writeTime` datetime NOT NULL,
  `send` enum('false','true') CHARACTER SET utf8 NOT NULL DEFAULT 'false',
  `receive` enum('false','true') CHARACTER SET utf8 NOT NULL DEFAULT 'false',
  `completeTime` datetime DEFAULT NULL,
  `sellerCancle` enum('false','true') CHARACTER SET utf8 NOT NULL DEFAULT 'false',
  `buyerCancle` enum('false','true') CHARACTER SET utf8 NOT NULL DEFAULT 'false',
  `top` enum('true','false') CHARACTER SET utf8 NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_uri_block`
--

CREATE TABLE `app_uri_block` (
  `uri` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_uri_pass`
--

CREATE TABLE `app_uri_pass` (
  `uri` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `area`
--

CREATE TABLE `area` (
  `areaid` int(5) NOT NULL DEFAULT 0,
  `mapid` int(5) NOT NULL DEFAULT 0,
  `areaname` varchar(50) DEFAULT NULL,
  `x1` int(6) NOT NULL DEFAULT 0,
  `y1` int(6) NOT NULL DEFAULT 0,
  `x2` int(6) NOT NULL DEFAULT 0,
  `y2` int(6) NOT NULL DEFAULT 0,
  `flag` int(1) NOT NULL DEFAULT 0,
  `restart` int(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `armor`
--

CREATE TABLE `armor` (
  `item_id` int(5) NOT NULL DEFAULT 0,
  `item_name_id` int(10) NOT NULL DEFAULT 0,
  `desc_kr` varchar(70) DEFAULT 'NULL',
  `desc_id` varchar(45) NOT NULL DEFAULT '',
  `itemGrade` enum('ONLY','MYTH','LEGEND','HERO','RARE','ADVANC','NORMAL') NOT NULL DEFAULT 'NORMAL',
  `type` enum('NONE','HELMET','ARMOR','T_SHIRT','CLOAK','GLOVE','BOOTS','SHIELD','AMULET','RING','BELT','RING_2','EARRING','GARDER','RON','PAIR','SENTENCE','SHOULDER','BADGE','PENDANT') NOT NULL DEFAULT 'NONE',
  `grade` int(2) NOT NULL DEFAULT 0,
  `material` enum('NONE(-)','LIQUID(액체)','WAX(밀랍)','VEGGY(식물성)','FLESH(동물성)','PAPER(종이)','CLOTH(천)','LEATHER(가죽)','WOOD(나무)','BONE(뼈)','DRAGON_HIDE(용비늘)','IRON(철)','METAL(금속)','COPPER(구리)','SILVER(은)','GOLD(금)','PLATINUM(백금)','MITHRIL(미스릴)','PLASTIC(블랙미스릴)','GLASS(유리)','GEMSTONE(보석)','MINERAL(광석)','ORIHARUKON(오리하루콘)','DRANIUM(드라니움)') NOT NULL DEFAULT 'NONE(-)',
  `weight` int(7) UNSIGNED NOT NULL DEFAULT 0,
  `iconId` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `spriteId` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `ac` int(3) NOT NULL DEFAULT 0,
  `ac_sub` int(3) NOT NULL DEFAULT 0,
  `safenchant` int(2) NOT NULL DEFAULT 0,
  `use_royal` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_knight` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_mage` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_elf` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_darkelf` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_dragonknight` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_illusionist` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_warrior` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_fencer` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_lancer` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `add_str` int(2) NOT NULL DEFAULT 0,
  `add_con` int(2) NOT NULL DEFAULT 0,
  `add_dex` int(2) NOT NULL DEFAULT 0,
  `add_int` int(2) NOT NULL DEFAULT 0,
  `add_wis` int(2) NOT NULL DEFAULT 0,
  `add_cha` int(2) NOT NULL DEFAULT 0,
  `add_hp` int(6) NOT NULL DEFAULT 0,
  `add_mp` int(6) NOT NULL DEFAULT 0,
  `add_hpr` int(6) NOT NULL DEFAULT 0,
  `add_mpr` int(6) NOT NULL DEFAULT 0,
  `add_sp` int(3) NOT NULL DEFAULT 0,
  `min_lvl` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `max_lvl` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `m_def` int(2) NOT NULL DEFAULT 0,
  `haste_item` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `carryBonus` int(4) UNSIGNED NOT NULL DEFAULT 0,
  `hit_rate` int(2) NOT NULL DEFAULT 0,
  `dmg_rate` int(2) NOT NULL DEFAULT 0,
  `bow_hit_rate` int(2) NOT NULL DEFAULT 0,
  `bow_dmg_rate` int(2) NOT NULL DEFAULT 0,
  `bless` int(2) UNSIGNED NOT NULL DEFAULT 1,
  `trade` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `retrieve` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `specialretrieve` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `retrieveEnchant` int(2) NOT NULL DEFAULT 0,
  `cant_delete` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `cant_sell` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `max_use_time` int(10) NOT NULL DEFAULT 0,
  `defense_water` int(2) NOT NULL DEFAULT 0,
  `defense_wind` int(2) NOT NULL DEFAULT 0,
  `defense_fire` int(2) NOT NULL DEFAULT 0,
  `defense_earth` int(2) NOT NULL DEFAULT 0,
  `attr_all` int(2) NOT NULL DEFAULT 0,
  `regist_stone` int(2) NOT NULL DEFAULT 0,
  `regist_sleep` int(2) NOT NULL DEFAULT 0,
  `regist_freeze` int(2) NOT NULL DEFAULT 0,
  `regist_blind` int(2) NOT NULL DEFAULT 0,
  `regist_skill` int(2) NOT NULL DEFAULT 0,
  `regist_spirit` int(2) NOT NULL DEFAULT 0,
  `regist_dragon` int(2) NOT NULL DEFAULT 0,
  `regist_fear` int(2) NOT NULL DEFAULT 0,
  `regist_all` int(2) NOT NULL DEFAULT 0,
  `hitup_skill` int(2) NOT NULL DEFAULT 0,
  `hitup_spirit` int(2) NOT NULL DEFAULT 0,
  `hitup_dragon` int(2) NOT NULL DEFAULT 0,
  `hitup_fear` int(2) NOT NULL DEFAULT 0,
  `hitup_all` int(2) NOT NULL DEFAULT 0,
  `hitup_magic` int(2) NOT NULL DEFAULT 0,
  `damage_reduction` int(2) NOT NULL DEFAULT 0,
  `MagicDamageReduction` int(2) NOT NULL DEFAULT 0,
  `reductionEgnor` int(2) NOT NULL DEFAULT 0,
  `reductionPercent` int(2) NOT NULL DEFAULT 0,
  `PVPDamage` int(2) NOT NULL DEFAULT 0,
  `PVPDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPDamageReductionPercent` int(2) NOT NULL DEFAULT 0,
  `PVPMagicDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `PVPMagicDamageReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusDamageReduction` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusPVPDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPDamagePercent` int(2) NOT NULL DEFAULT 0,
  `expBonus` int(3) NOT NULL DEFAULT 0,
  `rest_exp_reduce_efficiency` int(3) NOT NULL DEFAULT 0,
  `shortCritical` int(2) NOT NULL DEFAULT 0,
  `longCritical` int(2) NOT NULL DEFAULT 0,
  `magicCritical` int(2) NOT NULL DEFAULT 0,
  `addDg` int(2) NOT NULL DEFAULT 0,
  `addEr` int(2) NOT NULL DEFAULT 0,
  `addMe` int(2) NOT NULL DEFAULT 0,
  `poisonRegist` enum('false','true') NOT NULL DEFAULT 'false',
  `imunEgnor` int(3) NOT NULL DEFAULT 0,
  `stunDuration` int(2) NOT NULL DEFAULT 0,
  `tripleArrowStun` int(2) NOT NULL DEFAULT 0,
  `strangeTimeIncrease` int(4) NOT NULL DEFAULT 0,
  `strangeTimeDecrease` int(4) NOT NULL DEFAULT 0,
  `potionRegist` int(2) NOT NULL DEFAULT 0,
  `potionPercent` int(2) NOT NULL DEFAULT 0,
  `potionValue` int(2) NOT NULL DEFAULT 0,
  `hprAbsol32Second` int(2) NOT NULL DEFAULT 0,
  `mprAbsol64Second` int(2) NOT NULL DEFAULT 0,
  `mprAbsol16Second` int(2) NOT NULL DEFAULT 0,
  `hpPotionDelayDecrease` int(4) NOT NULL DEFAULT 0,
  `hpPotionCriticalProb` int(4) NOT NULL DEFAULT 0,
  `increaseArmorSkillProb` int(4) NOT NULL DEFAULT 0,
  `attackSpeedDelayRate` int(3) NOT NULL DEFAULT 0,
  `moveSpeedDelayRate` int(3) NOT NULL DEFAULT 0,
  `MainId` int(10) NOT NULL DEFAULT 0,
  `MainId2` int(10) NOT NULL DEFAULT 0,
  `MainId3` int(10) NOT NULL DEFAULT 0,
  `Set_Id` int(10) NOT NULL DEFAULT 0,
  `polyDescId` int(6) NOT NULL DEFAULT 0,
  `Magic_name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `armor_set`
--

CREATE TABLE `armor_set` (
  `id` int(10) NOT NULL,
  `note` varchar(45) DEFAULT NULL,
  `sets` varchar(1000) NOT NULL,
  `polyid` int(6) NOT NULL DEFAULT 0,
  `min_enchant` int(2) NOT NULL DEFAULT 0,
  `ac` int(2) NOT NULL DEFAULT 0,
  `hp` int(5) NOT NULL DEFAULT 0,
  `mp` int(5) NOT NULL DEFAULT 0,
  `hpr` int(5) NOT NULL DEFAULT 0,
  `mpr` int(5) NOT NULL DEFAULT 0,
  `mr` int(5) NOT NULL DEFAULT 0,
  `str` int(2) NOT NULL DEFAULT 0,
  `dex` int(2) NOT NULL DEFAULT 0,
  `con` int(2) NOT NULL DEFAULT 0,
  `wis` int(2) NOT NULL DEFAULT 0,
  `cha` int(2) NOT NULL DEFAULT 0,
  `intl` int(2) NOT NULL DEFAULT 0,
  `shorthitup` int(2) NOT NULL DEFAULT 0,
  `shortdmgup` int(2) NOT NULL DEFAULT 0,
  `shortCritical` int(2) NOT NULL DEFAULT 0,
  `longhitup` int(2) NOT NULL DEFAULT 0,
  `longdmgup` int(2) NOT NULL DEFAULT 0,
  `longCritical` int(2) NOT NULL DEFAULT 0,
  `sp` int(2) NOT NULL DEFAULT 0,
  `magichitup` int(2) NOT NULL DEFAULT 0,
  `magicCritical` int(2) NOT NULL DEFAULT 0,
  `earth` int(10) NOT NULL DEFAULT 0,
  `fire` int(10) NOT NULL DEFAULT 0,
  `wind` int(10) NOT NULL DEFAULT 0,
  `water` int(10) NOT NULL DEFAULT 0,
  `reduction` int(2) NOT NULL DEFAULT 0,
  `reductionEgnor` int(2) NOT NULL DEFAULT 0,
  `magicReduction` int(2) NOT NULL DEFAULT 0,
  `PVPDamage` int(2) NOT NULL DEFAULT 0,
  `PVPDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPMagicDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `PVPMagicDamageReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusDamageReduction` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusPVPDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPDamagePercent` int(2) NOT NULL DEFAULT 0,
  `expBonus` int(2) NOT NULL DEFAULT 0,
  `rest_exp_reduce_efficiency` int(2) NOT NULL DEFAULT 0,
  `dg` int(2) NOT NULL DEFAULT 0,
  `er` int(2) NOT NULL DEFAULT 0,
  `me` int(2) NOT NULL DEFAULT 0,
  `toleranceSkill` int(2) NOT NULL DEFAULT 0,
  `toleranceSpirit` int(2) NOT NULL DEFAULT 0,
  `toleranceDragon` int(2) NOT NULL DEFAULT 0,
  `toleranceFear` int(2) NOT NULL DEFAULT 0,
  `toleranceAll` int(2) NOT NULL DEFAULT 0,
  `hitupSkill` int(2) NOT NULL DEFAULT 0,
  `hitupSpirit` int(2) NOT NULL DEFAULT 0,
  `hitupDragon` int(2) NOT NULL DEFAULT 0,
  `hitupFear` int(2) NOT NULL DEFAULT 0,
  `hitupAll` int(2) NOT NULL DEFAULT 0,
  `strangeTimeIncrease` int(4) NOT NULL DEFAULT 0,
  `underWater` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=MyISAM DEFAULT CHARSET=euckr COMMENT='MyISAM free: 10240 kB';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `attendance_accounts`
--

CREATE TABLE `attendance_accounts` (
  `account` varchar(50) NOT NULL DEFAULT '',
  `dailyCount` int(4) NOT NULL DEFAULT 0,
  `isCompleted` enum('true','false') NOT NULL DEFAULT 'false',
  `resetDate` datetime DEFAULT NULL,
  `groupData` blob DEFAULT NULL,
  `groupOpen` blob DEFAULT NULL,
  `randomItems` text DEFAULT NULL,
  `rewardHistory` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `attendance_item`
--

CREATE TABLE `attendance_item` (
  `groupType` int(3) NOT NULL DEFAULT 0,
  `index` int(3) NOT NULL DEFAULT 0,
  `item_id` int(10) NOT NULL DEFAULT 0,
  `item_name` varchar(45) NOT NULL DEFAULT '',
  `desc_kr` varchar(45) NOT NULL,
  `enchant` int(3) DEFAULT 0,
  `count` int(10) DEFAULT 1,
  `broadcast` enum('true','false') NOT NULL DEFAULT 'false',
  `bonus_type` enum('RandomDiceItem(3)','GiveItem(2)','UseItem(1)') NOT NULL DEFAULT 'GiveItem(2)'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `attendance_item_random`
--

CREATE TABLE `attendance_item_random` (
  `groupType` int(1) NOT NULL DEFAULT 0,
  `index` int(3) NOT NULL DEFAULT 0,
  `itemId` int(10) NOT NULL DEFAULT 0,
  `itemName` varchar(45) CHARACTER SET euckr NOT NULL DEFAULT '',
  `desc_kr` varchar(45) NOT NULL,
  `count` int(10) NOT NULL DEFAULT 1,
  `broadcast` enum('false','true') CHARACTER SET euckr NOT NULL DEFAULT 'false',
  `level` enum('1','2','3','4','5') CHARACTER SET euckr NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `auth_ip`
--

CREATE TABLE `auth_ip` (
  `ip` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `autoloot`
--

CREATE TABLE `autoloot` (
  `item_id` int(11) NOT NULL DEFAULT 0,
  `note` varchar(50) DEFAULT NULL,
  `desc_kr` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `balance`
--

CREATE TABLE `balance` (
  `attackerType` enum('npc','lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown') NOT NULL DEFAULT 'crown',
  `targetType` enum('npc','lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown') NOT NULL DEFAULT 'crown',
  `physicalDmg` int(3) NOT NULL DEFAULT 0,
  `physicalHit` int(3) NOT NULL DEFAULT 0,
  `physicalReduction` int(3) NOT NULL DEFAULT 0,
  `magicDmg` int(3) NOT NULL DEFAULT 0,
  `magicHit` int(3) NOT NULL DEFAULT 0,
  `magicReduction` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ban_account`
--

CREATE TABLE `ban_account` (
  `account` varchar(100) NOT NULL,
  `reason` enum('BUG_ABOUS','CHAT_ABOUS','CHEAT','ETC') NOT NULL DEFAULT 'ETC',
  `counter` int(3) NOT NULL DEFAULT 1,
  `limitTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ban_board`
--

CREATE TABLE `ban_board` (
  `number` varchar(100) NOT NULL,
  `account` varchar(50) NOT NULL,
  `registTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ban_hdd`
--

CREATE TABLE `ban_hdd` (
  `number` varchar(100) NOT NULL,
  `account` varchar(50) NOT NULL,
  `registTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ban_ip`
--

CREATE TABLE `ban_ip` (
  `address` varchar(255) NOT NULL DEFAULT '',
  `reason` enum('CONNECTION_OVER','PACKET_ATTACK','BAD_USER','UNSUAL_REQUEST','WEB_URI_LENGTH_OVER','WEB_REQUEST_OVER','SERVER_SLANDER','WELLKNOWN_PORT','BUG_ABOUS','CHEAT','ETC','WEB_ATTACK_REQUEST','WEB_NOT_AUTH_IP') NOT NULL DEFAULT 'ETC',
  `registTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `beginner`
--

CREATE TABLE `beginner` (
  `id` int(10) NOT NULL,
  `item_id` int(6) NOT NULL DEFAULT 0,
  `count` int(10) NOT NULL DEFAULT 0,
  `charge_count` int(10) NOT NULL DEFAULT 0,
  `enchantlvl` int(6) NOT NULL DEFAULT 0,
  `item_name` varchar(50) NOT NULL DEFAULT '',
  `desc_kr` varchar(50) NOT NULL,
  `activate` enum('A','P','K','E','W','D','T','B','J','F','L') NOT NULL DEFAULT 'A'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `beginner_addteleport`
--

CREATE TABLE `beginner_addteleport` (
  `id` int(10) UNSIGNED NOT NULL,
  `num_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `speed_id` int(10) NOT NULL DEFAULT -1,
  `char_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `randomX` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `randomY` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `item_obj_id` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `beginner_box`
--

CREATE TABLE `beginner_box` (
  `itemid` int(20) NOT NULL,
  `count` int(20) NOT NULL DEFAULT 0,
  `enchantlvl` int(6) NOT NULL DEFAULT 0,
  `item_name` varchar(50) NOT NULL,
  `desc_kr` varchar(50) NOT NULL,
  `bless` int(10) NOT NULL DEFAULT 1,
  `activate` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown','all') NOT NULL DEFAULT 'all'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `beginner_quest`
--

CREATE TABLE `beginner_quest` (
  `quest_id` int(10) NOT NULL,
  `note` varchar(45) CHARACTER SET euckr NOT NULL DEFAULT '',
  `desc_kr` varchar(45) CHARACTER SET euckr NOT NULL,
  `use` enum('true','false') CHARACTER SET euckr NOT NULL DEFAULT 'true',
  `auto_complete` enum('false','true') CHARACTER SET euckr NOT NULL DEFAULT 'false',
  `fastLevel` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `beginner_quest_drop`
--

CREATE TABLE `beginner_quest_drop` (
  `classId` int(10) NOT NULL DEFAULT 0,
  `desc` varchar(50) DEFAULT NULL,
  `desc_kr` varchar(50) NOT NULL,
  `mainQuestId` int(3) NOT NULL DEFAULT 0,
  `mainItemNameId` int(10) NOT NULL DEFAULT 0,
  `subQuestId` int(3) NOT NULL DEFAULT 0,
  `subItemNameId` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `beginner_teleport`
--

CREATE TABLE `beginner_teleport` (
  `name` varchar(45) NOT NULL DEFAULT '',
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_armor_element_common`
--

CREATE TABLE `bin_armor_element_common` (
  `type` int(2) NOT NULL DEFAULT 0,
  `enchant` int(2) NOT NULL DEFAULT 0,
  `fr` int(2) NOT NULL DEFAULT 0,
  `wr` int(2) NOT NULL DEFAULT 0,
  `ar` int(2) NOT NULL DEFAULT 0,
  `er` int(2) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_catalyst_common`
--

CREATE TABLE `bin_catalyst_common` (
  `nameId` int(6) NOT NULL DEFAULT 0,
  `nameId_kr` varchar(100) DEFAULT NULL,
  `input` int(6) NOT NULL DEFAULT 0,
  `input_kr` varchar(100) DEFAULT NULL,
  `output` int(6) NOT NULL DEFAULT 0,
  `output_kr` varchar(100) DEFAULT NULL,
  `successProb` int(3) NOT NULL DEFAULT 0,
  `rewardCount` int(6) NOT NULL DEFAULT 0,
  `preserveProb` int(3) NOT NULL DEFAULT 0,
  `failOutput` int(6) NOT NULL DEFAULT 0,
  `failOutput_kr` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_charged_time_map_common`
--

CREATE TABLE `bin_charged_time_map_common` (
  `id` int(1) NOT NULL DEFAULT 0,
  `groups` text DEFAULT NULL,
  `multi_group_list` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_companion_class_common`
--

CREATE TABLE `bin_companion_class_common` (
  `classId` int(6) NOT NULL DEFAULT 0,
  `class` varchar(100) DEFAULT NULL,
  `category` enum('DOG_FIGHT(5)','WILD(4)','PET(3)','DEVINE_BEAST(2)','FIERCE_ANIMAL(1)') NOT NULL DEFAULT 'FIERCE_ANIMAL(1)',
  `element` enum('LIGHT(5)','EARTH(4)','AIR(3)','WATER(2)','FIRE(1)','NONE(0)') NOT NULL DEFAULT 'NONE(0)',
  `skill` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_companion_enchant_common`
--

CREATE TABLE `bin_companion_enchant_common` (
  `tier` int(3) NOT NULL,
  `enchantCost` text DEFAULT NULL,
  `openCost` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_companion_skill_common`
--

CREATE TABLE `bin_companion_skill_common` (
  `id` int(3) NOT NULL DEFAULT 0,
  `descNum` int(6) NOT NULL DEFAULT 0,
  `descKr` varchar(100) DEFAULT NULL,
  `enchantBonus` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_companion_stat_common`
--

CREATE TABLE `bin_companion_stat_common` (
  `id` int(3) NOT NULL DEFAULT 0,
  `statType` enum('INT(2)','CON(1)','STR(0)') NOT NULL DEFAULT 'STR(0)',
  `value` int(3) NOT NULL DEFAULT 0,
  `meleeDmg` int(3) NOT NULL DEFAULT 0,
  `meleeHit` int(3) NOT NULL DEFAULT 0,
  `regenHP` int(3) NOT NULL DEFAULT 0,
  `ac` int(3) NOT NULL DEFAULT 0,
  `spellDmg` int(3) NOT NULL DEFAULT 0,
  `spellHit` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_craft_common`
--

CREATE TABLE `bin_craft_common` (
  `craft_id` int(6) NOT NULL DEFAULT 0,
  `desc_id` int(6) NOT NULL DEFAULT 0,
  `desc_kr` varchar(100) DEFAULT NULL,
  `min_level` int(3) NOT NULL DEFAULT 0,
  `max_level` int(3) NOT NULL DEFAULT 0,
  `required_gender` int(2) NOT NULL DEFAULT 0,
  `min_align` int(6) NOT NULL DEFAULT 0,
  `max_align` int(6) NOT NULL DEFAULT 0,
  `min_karma` int(10) NOT NULL DEFAULT 0,
  `max_karma` int(10) NOT NULL DEFAULT 0,
  `max_count` int(6) NOT NULL DEFAULT 0,
  `is_show` enum('true','false') NOT NULL DEFAULT 'false',
  `PCCafeOnly` enum('true','false') NOT NULL DEFAULT 'false',
  `bmProbOpen` enum('true','false') NOT NULL DEFAULT 'false',
  `required_classes` int(6) NOT NULL DEFAULT 0,
  `required_quests` text DEFAULT NULL,
  `required_sprites` text DEFAULT NULL,
  `required_items` text DEFAULT NULL,
  `inputs_arr_input_item` text DEFAULT NULL,
  `inputs_arr_option_item` text DEFAULT NULL,
  `outputs_success` text DEFAULT NULL,
  `outputs_failure` text DEFAULT NULL,
  `outputs_success_prob_by_million` int(10) NOT NULL DEFAULT 0,
  `batch_delay_sec` int(10) NOT NULL DEFAULT 0,
  `period_list` text DEFAULT NULL,
  `cur_successcount` int(10) NOT NULL DEFAULT 0,
  `max_successcount` int(10) NOT NULL DEFAULT 0,
  `except_npc` enum('true','false') NOT NULL DEFAULT 'false',
  `SuccessCountType` enum('World(0)','Account(1)','Character(2)','AllServers(3)') NOT NULL DEFAULT 'World(0)'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_einpoint_cost_common`
--

CREATE TABLE `bin_einpoint_cost_common` (
  `value` int(3) NOT NULL DEFAULT 0,
  `point` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_einpoint_faith_common`
--

CREATE TABLE `bin_einpoint_faith_common` (
  `GroupId` int(3) NOT NULL DEFAULT 0,
  `spellId` int(10) NOT NULL DEFAULT 0,
  `Index_indexId` int(3) NOT NULL DEFAULT 0,
  `Index_spellId` int(10) NOT NULL DEFAULT 0,
  `Index_cost` int(10) NOT NULL DEFAULT 0,
  `Index_duration` int(10) NOT NULL DEFAULT 0,
  `Index_additional_desc` int(6) NOT NULL DEFAULT 0,
  `Index_additional_desc_kr` varchar(100) DEFAULT NULL,
  `additional_desc` int(6) NOT NULL DEFAULT 0,
  `additional_desc_kr` varchar(100) DEFAULT NULL,
  `BuffInfo_tooltipStrId` int(6) NOT NULL DEFAULT 0,
  `BuffInfo_tooltipStrId_kr` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_einpoint_meta_common`
--

CREATE TABLE `bin_einpoint_meta_common` (
  `index_id` int(3) NOT NULL DEFAULT 0,
  `stat_type` enum('BLESS(0)','LUCKY(1)','VITAL(2)','ITEM_SPELL_PROB(3)','ABSOLUTE_REGEN(4)','POTION(5)','ATTACK_SPELL(6)','PVP_REDUCTION(7)','PVE_REDUCTION(8)','_MAX_(9)') NOT NULL DEFAULT '_MAX_(9)',
  `AbilityMetaData1_token` varchar(100) DEFAULT NULL,
  `AbilityMetaData1_x100` enum('true','false') NOT NULL DEFAULT 'false',
  `AbilityMetaData1_unit` enum('None(1)','Percent(2)') NOT NULL DEFAULT 'None(1)',
  `AbilityMetaData2_token` varchar(100) DEFAULT NULL,
  `AbilityMetaData2_x100` enum('true','false') NOT NULL DEFAULT 'false',
  `AbilityMetaData2_unit` enum('None(1)','Percent(2)') NOT NULL DEFAULT 'None(1)'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_einpoint_normal_prob_common`
--

CREATE TABLE `bin_einpoint_normal_prob_common` (
  `Normal_level` int(3) NOT NULL DEFAULT 0,
  `prob` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_einpoint_overstat_prob_common`
--

CREATE TABLE `bin_einpoint_overstat_prob_common` (
  `over_level` int(3) NOT NULL DEFAULT 0,
  `prob` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_einpoint_prob_table_common`
--

CREATE TABLE `bin_einpoint_prob_table_common` (
  `isLastChance` enum('true','false') NOT NULL DEFAULT 'false',
  `bonusPoint` int(3) NOT NULL DEFAULT 0,
  `prob` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_einpoint_stat_common`
--

CREATE TABLE `bin_einpoint_stat_common` (
  `index_id` int(3) NOT NULL DEFAULT 0,
  `stat_type` enum('BLESS(0)','LUCKY(1)','VITAL(2)','ITEM_SPELL_PROB(3)','ABSOLUTE_REGEN(4)','POTION(5)','ATTACK_SPELL(6)','PVP_REDUCTION(7)','PVE_REDUCTION(8)','_MAX_(9)') NOT NULL DEFAULT '_MAX_(9)',
  `value` int(3) NOT NULL DEFAULT 0,
  `Ability1_minIncValue` int(3) NOT NULL DEFAULT 0,
  `Ability1_maxIncValue` int(3) NOT NULL DEFAULT 0,
  `Ability2_minIncValue` int(3) NOT NULL DEFAULT 0,
  `Ability2_maxIncValue` int(3) NOT NULL DEFAULT 0,
  `StatMaxInfo_level` int(3) NOT NULL DEFAULT 0,
  `StatMaxInfo_statMax` int(3) NOT NULL DEFAULT 0,
  `eachStatMax` int(3) NOT NULL DEFAULT 0,
  `totalStatMax` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_element_enchant_common`
--

CREATE TABLE `bin_element_enchant_common` (
  `prob_index` int(3) NOT NULL DEFAULT 0,
  `type_index` int(3) NOT NULL DEFAULT 0,
  `level` int(3) NOT NULL DEFAULT 0,
  `increaseProb` int(3) NOT NULL DEFAULT 0,
  `decreaseProb` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_enchant_scroll_table_common`
--

CREATE TABLE `bin_enchant_scroll_table_common` (
  `enchantType` int(3) NOT NULL DEFAULT 0,
  `nameid` int(6) NOT NULL DEFAULT 0,
  `desc_kr` varchar(100) DEFAULT NULL,
  `targetEnchant` int(3) NOT NULL DEFAULT 0,
  `noTargetMaterialList` text DEFAULT NULL,
  `target_category` enum('NONE(0)','WEAPON(1)','ARMOR(2)','ACCESSORY(3)','ELEMENT(4)') NOT NULL DEFAULT 'NONE(0)',
  `isBmEnchantScroll` enum('false','true') NOT NULL DEFAULT 'false',
  `elementalType` int(2) NOT NULL DEFAULT 0,
  `useBlesscodeScroll` int(2) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_enchant_table_common`
--

CREATE TABLE `bin_enchant_table_common` (
  `item_index` int(10) NOT NULL DEFAULT 0,
  `bonusLevel_index` int(10) NOT NULL DEFAULT 0,
  `enchantSuccessProb` int(10) NOT NULL DEFAULT 0,
  `enchantTotalProb` int(10) NOT NULL DEFAULT 0,
  `bmEnchantSuccessProb` int(10) NOT NULL DEFAULT 0,
  `bmEnchantRemainProb` int(10) NOT NULL DEFAULT 0,
  `bmEnchantFailDownProb` int(10) NOT NULL DEFAULT 0,
  `bmEnchantTotalProb` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_entermaps_common`
--

CREATE TABLE `bin_entermaps_common` (
  `id` int(6) NOT NULL DEFAULT 0,
  `action_name` varchar(50) NOT NULL DEFAULT '',
  `number_id` int(6) NOT NULL DEFAULT 0,
  `loc_x` int(6) NOT NULL DEFAULT 0,
  `loc_y` int(6) NOT NULL DEFAULT 0,
  `loc_range` int(3) NOT NULL DEFAULT 0,
  `priority_id` int(2) NOT NULL DEFAULT 0,
  `maxUser` int(3) NOT NULL DEFAULT 0,
  `conditions` text DEFAULT NULL,
  `destinations` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_favorbook_common`
--

CREATE TABLE `bin_favorbook_common` (
  `category_id` int(2) NOT NULL DEFAULT 0,
  `desc_id` varchar(50) DEFAULT NULL,
  `desc_kr` varchar(100) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `sort` int(2) NOT NULL DEFAULT 0,
  `slot_id` int(2) NOT NULL DEFAULT 0,
  `state_infos` text DEFAULT NULL,
  `red_dot_notice` int(2) NOT NULL DEFAULT 0,
  `default_display_item_id` int(5) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_general_goods_common`
--

CREATE TABLE `bin_general_goods_common` (
  `NameId` int(6) NOT NULL DEFAULT 0,
  `NameId_kr` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_huntingquest_common`
--

CREATE TABLE `bin_huntingquest_common` (
  `maxQuestCount` int(3) NOT NULL DEFAULT 0,
  `goalKillCount` int(3) NOT NULL DEFAULT 0,
  `reset_HourOfDay` int(2) NOT NULL DEFAULT -1,
  `reward_normal_ConditionalRewards` text DEFAULT NULL,
  `reward_normal_UsedItemID` int(6) NOT NULL,
  `reward_normal_UsedAmount` int(6) NOT NULL DEFAULT 0,
  `reward_dragon_ConditionalRewards` text DEFAULT NULL,
  `reward_dragon_UsedItemID` int(6) NOT NULL DEFAULT 0,
  `reward_dragon_UsedAmount` int(6) NOT NULL DEFAULT 0,
  `reward_hightdragon_ConditionalRewards` text DEFAULT NULL,
  `reward_hightdragon_UsedItemID` int(6) NOT NULL DEFAULT 0,
  `reward_hightdragon_UsedAmount` int(6) NOT NULL DEFAULT 0,
  `requiredCondition_MinLevel` int(3) NOT NULL DEFAULT 0,
  `requiredCondition_MaxLevel` int(3) NOT NULL DEFAULT 0,
  `requiredCondition_Map` int(6) NOT NULL DEFAULT 0,
  `requiredCondition_LocationDesc` int(6) NOT NULL DEFAULT 0,
  `enterMapID` int(6) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_indun_common`
--

CREATE TABLE `bin_indun_common` (
  `mapKind` int(3) NOT NULL DEFAULT 0,
  `keyItemId` int(5) NOT NULL DEFAULT 0,
  `minPlayer` int(2) NOT NULL DEFAULT 0,
  `maxPlayer` int(2) NOT NULL DEFAULT 0,
  `minAdena` int(6) NOT NULL DEFAULT 0,
  `maxAdena` int(6) NOT NULL DEFAULT 0,
  `minLevel` varchar(100) DEFAULT NULL,
  `bmkeyItemId` int(5) NOT NULL DEFAULT 0,
  `eventKeyItemId` int(5) NOT NULL DEFAULT 0,
  `dungeon_type` enum('UNDEFINED(0)','DEFENCE_TYPE(1)','DUNGEON_TYPE(2)') NOT NULL DEFAULT 'UNDEFINED(0)',
  `enable_boost_mode` enum('false','true') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_item_common`
--

CREATE TABLE `bin_item_common` (
  `name_id` int(6) NOT NULL DEFAULT 0,
  `icon_id` int(6) NOT NULL DEFAULT 0,
  `sprite_id` int(6) NOT NULL DEFAULT 0,
  `desc_id` varchar(100) DEFAULT NULL,
  `real_desc` varchar(100) DEFAULT NULL,
  `desc_kr` varchar(100) DEFAULT NULL,
  `material` enum('DRANIUM(23)','ORIHARUKON(22)','MINERAL(21)','GEMSTONE(20)','GLASS(19)','PLASTIC(18)','MITHRIL(17)','PLATINUM(16)','GOLD(15)','SILVER(14)','COPPER(13)','METAL(12)','IRON(11)','DRAGON_HIDE(10)','BONE(9)','WOOD(8)','LEATHER(7)','CLOTH(6)','PAPER(5)','FLESH(4)','VEGGY(3)','WAX(2)','LIQUID(1)','NONE(0)') NOT NULL DEFAULT 'NONE(0)',
  `weight_1000ea` int(10) NOT NULL DEFAULT 0,
  `level_limit_min` int(3) NOT NULL DEFAULT 0,
  `level_limit_max` int(3) NOT NULL DEFAULT 0,
  `prince_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `knight_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `elf_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `magician_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `darkelf_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `dragonknight_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `illusionist_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `warrior_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `fencer_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `lancer_permit` enum('true','false') NOT NULL DEFAULT 'false',
  `equip_bonus_list` text DEFAULT NULL,
  `interaction_type` int(3) NOT NULL DEFAULT 0,
  `real_weight` int(10) NOT NULL DEFAULT 0,
  `spell_range` int(2) NOT NULL DEFAULT 0,
  `item_category` enum('WAND(1013)','AUTO_USED_BY_BUFF_ITEM(1012)','SPELL_EXTRACTOR(1011)','ARROW(1010)','POTION_MANA(1009)','LUCKY_BAG(1008)','WAND_CALL_LIGHTNING(1007)','ARMOR_SERIES_MAIN(1006)','ARMOR_SERIES(1005)','SCROLL(1004)','SCROLL_TELEPORT_HOME(1003)','SCROLL_TELEPORT_TOWN(1002)','POTION_HEAL(1001)','POTION(1000)','ITEM(23)','LIGHT(22)','FOOD(21)','ARMOR(19)','WEAPON(1)','NONE(0)') NOT NULL DEFAULT 'NONE(0)',
  `body_part` enum('BODYPART_ALL(-1)','BP_PENDANT(33554432)','BP_INSIGNIA(16777216)','BP_PAULDRON(8388608)','BP_HERALDRY(4194304)','EXT_SLOTS(2097152)','RUNE(1048576)','L_WRIST(524288)','R_WRIST(262144)','BACK(131072)','L_SHOULDER(65536)','R_SHOULDER(32768)','EAR(16384)','WAIST(8192)','NECK(4096)','R_FINGER(2048)','L_FINGER(1024)','R_HOLD(512)','L_HOLD(256)','R_HAND(128)','L_HAND(64)','FOOT(32)','LEG(16)','CLOAK(8)','SHIRT(4)','TORSO(2)','HEAD(1)','NONE(0)') NOT NULL DEFAULT 'NONE(0)',
  `ac` int(6) NOT NULL DEFAULT 0,
  `extended_weapon_type` enum('WEAPON_EX_NOT_EQUIPPED(13)','WEAPON_EX_KIRINGKU(12)','WEAPON_EX_DOUBLE_AXE(11)','WEAPON_EX_CHAIN_SWORD(10)','WEAPON_EX_GAUNTLET(9)','WEAPON_EX_CRAW(8)','WEAPON_EX_DOUBLE_SWORD(7)','WEAPON_EX_LARGE_SWORD(6)','WEAPON_EX_DAGGER(5)','WEAPON_EX_STAFF(4)','WEAPON_EX_SPEAR(3)','WEAPON_EX_BOW(2)','WEAPON_EX_AXE(1)','WEAPON_EX_ONEHAND_SWORD(0)','NONE(-1)') NOT NULL DEFAULT 'NONE(-1)',
  `large_damage` int(3) NOT NULL DEFAULT 0,
  `small_damage` int(3) NOT NULL DEFAULT 0,
  `hit_bonus` int(3) NOT NULL DEFAULT 0,
  `damage_bonus` int(3) NOT NULL DEFAULT 0,
  `armor_series_info` text DEFAULT NULL,
  `cost` int(10) NOT NULL DEFAULT 0,
  `can_set_mage_enchant` enum('true','false') NOT NULL DEFAULT 'false',
  `merge` enum('true','false') NOT NULL DEFAULT 'false',
  `pss_event_item` enum('true','false') NOT NULL DEFAULT 'false',
  `market_searching_item` enum('true','false') NOT NULL DEFAULT 'false',
  `lucky_bag_reward_list` text DEFAULT NULL,
  `element_enchant_table` int(3) NOT NULL DEFAULT 0,
  `accessory_enchant_table` int(3) NOT NULL DEFAULT 0,
  `bm_prob_open` int(3) NOT NULL DEFAULT 0,
  `enchant_type` int(3) NOT NULL DEFAULT 0,
  `is_elven` enum('true','false') NOT NULL DEFAULT 'false',
  `forced_elemental_enchant` int(3) NOT NULL DEFAULT 0,
  `max_enchant` int(3) NOT NULL DEFAULT 0,
  `energy_lost` enum('true','false') NOT NULL DEFAULT 'false',
  `prob` int(6) NOT NULL DEFAULT 0,
  `pss_heal_item` enum('false','true') NOT NULL DEFAULT 'false',
  `useInterval` bigint(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_ndl_common`
--

CREATE TABLE `bin_ndl_common` (
  `map_number` int(6) NOT NULL DEFAULT 0,
  `npc_classId` int(6) NOT NULL DEFAULT 0,
  `npc_desc_kr` varchar(100) DEFAULT NULL,
  `territory_startXY` int(10) NOT NULL DEFAULT 0,
  `territory_endXY` int(10) NOT NULL DEFAULT 0,
  `territory_location_desc` int(6) NOT NULL DEFAULT 0,
  `territory_average_npc_value` int(10) NOT NULL DEFAULT 0,
  `territory_average_ac` int(10) NOT NULL DEFAULT 0,
  `territory_average_level` int(10) NOT NULL DEFAULT 0,
  `territory_average_wis` int(10) NOT NULL DEFAULT 0,
  `territory_average_mr` int(10) NOT NULL DEFAULT 0,
  `territory_average_magic_barrier` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_npc_common`
--

CREATE TABLE `bin_npc_common` (
  `class_id` int(6) NOT NULL DEFAULT 0,
  `sprite_id` int(6) NOT NULL DEFAULT 0,
  `desc_id` varchar(100) DEFAULT NULL,
  `desc_kr` varchar(100) DEFAULT NULL,
  `level` int(3) NOT NULL DEFAULT 0,
  `hp` int(9) NOT NULL DEFAULT 0,
  `mp` int(9) NOT NULL DEFAULT 0,
  `ac` int(3) NOT NULL DEFAULT 0,
  `str` int(3) NOT NULL DEFAULT 0,
  `con` int(3) NOT NULL DEFAULT 0,
  `dex` int(3) NOT NULL DEFAULT 0,
  `wis` int(3) NOT NULL DEFAULT 0,
  `inti` int(3) NOT NULL DEFAULT 0,
  `cha` int(3) NOT NULL DEFAULT 0,
  `mr` int(3) NOT NULL DEFAULT 0,
  `magic_level` int(3) NOT NULL DEFAULT 0,
  `magic_bonus` int(3) NOT NULL DEFAULT 0,
  `magic_evasion` int(3) NOT NULL DEFAULT 0,
  `resistance_fire` int(3) NOT NULL DEFAULT 0,
  `resistance_water` int(3) NOT NULL DEFAULT 0,
  `resistance_air` int(3) NOT NULL DEFAULT 0,
  `resistance_earth` int(3) NOT NULL DEFAULT 0,
  `alignment` int(6) NOT NULL DEFAULT 0,
  `big` enum('true','false') NOT NULL DEFAULT 'false',
  `drop_items` text DEFAULT NULL,
  `tendency` enum('AGGRESSIVE(2)','PASSIVE(1)','NEUTRAL(0)') NOT NULL DEFAULT 'NEUTRAL(0)',
  `category` int(10) NOT NULL DEFAULT 0,
  `is_bossmonster` enum('true','false') NOT NULL DEFAULT 'false',
  `can_turnundead` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_passivespell_common`
--

CREATE TABLE `bin_passivespell_common` (
  `passive_id` int(10) NOT NULL DEFAULT 0,
  `duration` int(10) NOT NULL DEFAULT 0,
  `spell_bonus_list` text DEFAULT NULL,
  `delay_group_id` int(2) NOT NULL DEFAULT 0,
  `extract_item_name_id` int(6) NOT NULL DEFAULT 0,
  `extract_item_count` int(6) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_pc_master_common`
--

CREATE TABLE `bin_pc_master_common` (
  `utilities` text DEFAULT NULL,
  `pc_bonus_map_infos` text DEFAULT NULL,
  `notification` text DEFAULT NULL,
  `buff_group` text DEFAULT NULL,
  `buff_bonus` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_portrait_common`
--

CREATE TABLE `bin_portrait_common` (
  `asset_id` int(5) NOT NULL DEFAULT 0,
  `desc_id` varchar(100) DEFAULT NULL,
  `desc_kr` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_potential_common`
--

CREATE TABLE `bin_potential_common` (
  `id` int(4) NOT NULL DEFAULT 0,
  `grade` int(2) NOT NULL DEFAULT 0,
  `desc_id` int(6) NOT NULL DEFAULT 0,
  `desc_kr` varchar(100) DEFAULT NULL,
  `material_list` text DEFAULT NULL,
  `event_config` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_ship_common`
--

CREATE TABLE `bin_ship_common` (
  `id` int(2) NOT NULL DEFAULT 0,
  `dockWorld` int(6) NOT NULL DEFAULT 0,
  `shipWorld` int(6) NOT NULL DEFAULT 0,
  `ticket` int(6) NOT NULL DEFAULT 0,
  `levelLimit` int(3) NOT NULL DEFAULT 0,
  `dock_startX` int(5) NOT NULL DEFAULT 0,
  `dock_startY` int(5) NOT NULL DEFAULT 0,
  `dock_endX` int(5) NOT NULL DEFAULT 0,
  `dock_endY` int(5) NOT NULL DEFAULT 0,
  `shipLoc_x` int(5) NOT NULL DEFAULT 0,
  `shipLoc_y` int(5) NOT NULL DEFAULT 0,
  `destWorld` int(6) NOT NULL DEFAULT 0,
  `destLoc_x` int(5) NOT NULL DEFAULT 0,
  `destLoc_y` int(5) NOT NULL DEFAULT 0,
  `destLoc_range` int(3) NOT NULL DEFAULT 0,
  `schedule_day` varchar(100) DEFAULT NULL,
  `schedule_time` blob DEFAULT NULL,
  `schedule_duration` int(2) NOT NULL DEFAULT 0,
  `schedule_ship_operating_duration` int(2) NOT NULL DEFAULT 0,
  `returnWorld` int(6) NOT NULL DEFAULT 0,
  `returnLoc_x` int(5) NOT NULL DEFAULT 0,
  `returnLoc_y` int(5) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_spell_common`
--

CREATE TABLE `bin_spell_common` (
  `spell_id` int(10) NOT NULL DEFAULT 0,
  `spell_category` enum('COMPANION_SPELL_BUFF(2)','SPELL_BUFF(1)','SPELL(0)') NOT NULL DEFAULT 'SPELL(0)',
  `on_icon_id` int(6) NOT NULL DEFAULT 0,
  `off_icon_id` int(6) NOT NULL DEFAULT 0,
  `duration` int(10) NOT NULL DEFAULT 0,
  `tooltip_str_id` int(6) NOT NULL DEFAULT 0,
  `tooltip_str_kr` varchar(200) DEFAULT NULL,
  `spell_bonus_list` text DEFAULT NULL,
  `companion_on_icon_id` int(6) NOT NULL DEFAULT 0,
  `companion_off_icon_id` int(6) NOT NULL DEFAULT 0,
  `companion_icon_priority` int(3) NOT NULL DEFAULT 0,
  `companion_tooltip_str_id` int(6) NOT NULL DEFAULT 0,
  `companion_new_str_id` int(6) NOT NULL DEFAULT 0,
  `companion_end_str_id` int(6) NOT NULL DEFAULT 0,
  `companion_is_good` int(3) NOT NULL DEFAULT 0,
  `companion_duration_show_type` int(3) NOT NULL DEFAULT 0,
  `delay_group_id` int(2) NOT NULL DEFAULT 0,
  `extract_item_name_id` int(6) NOT NULL DEFAULT 0,
  `extract_item_count` int(6) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_timecollection_common`
--

CREATE TABLE `bin_timecollection_common` (
  `buffSelect` text DEFAULT NULL,
  `rewardList` text DEFAULT NULL,
  `enchantSection` text DEFAULT NULL,
  `group_id` int(3) NOT NULL DEFAULT 0,
  `group_desc` int(6) NOT NULL DEFAULT 0,
  `group_desc_kr` varchar(100) DEFAULT NULL,
  `group_level_min` int(3) NOT NULL DEFAULT 0,
  `group_level_max` int(3) NOT NULL DEFAULT 0,
  `group_period_StartDate` varchar(100) DEFAULT NULL,
  `group_period_EndDate` varchar(100) DEFAULT NULL,
  `group_set_id` int(3) NOT NULL DEFAULT 0,
  `group_set_desc` int(6) NOT NULL DEFAULT 0,
  `group_set_desc_kr` varchar(100) DEFAULT NULL,
  `group_set_defaultTime` varchar(100) DEFAULT NULL,
  `group_set_recycle` int(3) NOT NULL DEFAULT 0,
  `group_set_itemSlot` text DEFAULT NULL,
  `group_set_BuffType` text DEFAULT NULL,
  `group_set_endBonus` enum('true','false') NOT NULL DEFAULT 'false',
  `group_set_ExtraTimeId` int(10) NOT NULL DEFAULT 0,
  `group_set_SetType` enum('NONE(-1)','TC_INFINITY(0)','TC_LIMITED(1)','TC_BONUS_INFINITY(2)','TC_BONUS_LIMITED(3)','TC_ADENA_REFILL(4)','TC_ADENA_REFILL_ERROR(5)','TC_BONUS_ADENA_REFILL(6)','TC_BONUS_ADENA_REFILL_ERROR(7)') NOT NULL DEFAULT 'NONE(-1)',
  `ExtraTimeSection` text DEFAULT NULL,
  `NPCDialogInfo` text DEFAULT NULL,
  `AlarmSetting` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_treasureboxreward_common`
--

CREATE TABLE `bin_treasureboxreward_common` (
  `nameid` int(6) NOT NULL DEFAULT 0,
  `desc_kr` varchar(50) DEFAULT NULL,
  `grade` enum('Common(0)','Good(1)','Prime(2)','Legendary(3)') NOT NULL DEFAULT 'Common(0)'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bin_treasurebox_common`
--

CREATE TABLE `bin_treasurebox_common` (
  `id` int(2) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `excavateTime` int(2) NOT NULL DEFAULT 0,
  `desc_id` varchar(45) DEFAULT NULL,
  `desc_kr` varchar(45) DEFAULT NULL,
  `grade` enum('Common(0)','Good(1)','Prime(2)','Legendary(3)') NOT NULL DEFAULT 'Common(0)'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_app_lfc`
--

CREATE TABLE `board_app_lfc` (
  `id` int(10) NOT NULL,
  `name` varchar(16) DEFAULT 'NULL',
  `date` varchar(16) DEFAULT 'NULL',
  `title` varchar(16) DEFAULT 'NULL',
  `content` varchar(100) DEFAULT 'NULL'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_auction`
--

CREATE TABLE `board_auction` (
  `house_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `house_name` varchar(45) NOT NULL DEFAULT '',
  `house_area` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `deadline` datetime DEFAULT NULL,
  `price` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(45) NOT NULL DEFAULT '',
  `old_owner` varchar(45) NOT NULL DEFAULT '',
  `old_owner_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `bidder` varchar(45) NOT NULL DEFAULT '',
  `bidder_id` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_free`
--

CREATE TABLE `board_free` (
  `id` int(10) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(16) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_notice`
--

CREATE TABLE `board_notice` (
  `id` int(10) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(16) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_notice1`
--

CREATE TABLE `board_notice1` (
  `id` int(10) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(16) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_notice2`
--

CREATE TABLE `board_notice2` (
  `id` int(10) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(16) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_notice3`
--

CREATE TABLE `board_notice3` (
  `id` int(10) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_posts_fix`
--

CREATE TABLE `board_posts_fix` (
  `id` int(10) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(16) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_posts_key`
--

CREATE TABLE `board_posts_key` (
  `id` int(10) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(16) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_user`
--

CREATE TABLE `board_user` (
  `id` int(10) NOT NULL DEFAULT 0,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(16) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `board_user1`
--

CREATE TABLE `board_user1` (
  `id` int(10) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(16) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bots`
--

CREATE TABLE `bots` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `x` int(2) NOT NULL DEFAULT 0,
  `y` int(2) NOT NULL DEFAULT 0,
  `heading` int(2) NOT NULL DEFAULT 0,
  `mapId` int(10) NOT NULL,
  `type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `castle`
--

CREATE TABLE `castle` (
  `castle_id` int(11) NOT NULL DEFAULT 0,
  `name` varchar(45) NOT NULL DEFAULT '',
  `desc_kr` varchar(45) NOT NULL,
  `war_time` datetime DEFAULT NULL,
  `tax_rate` int(11) NOT NULL DEFAULT 0,
  `public_money` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `castle_present`
--

CREATE TABLE `castle_present` (
  `itemid` int(20) NOT NULL,
  `count` int(20) NOT NULL DEFAULT 0,
  `memo` varchar(20) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `castle_soldier`
--

CREATE TABLE `castle_soldier` (
  `castle_id` int(2) NOT NULL,
  `soldier1` int(2) NOT NULL DEFAULT 0,
  `soldier1_npcid` int(6) NOT NULL DEFAULT 0,
  `soldier1_name` varchar(10) NOT NULL,
  `soldier2` int(2) NOT NULL DEFAULT 0,
  `soldier2_npcid` int(6) NOT NULL DEFAULT 0,
  `soldier2_name` varchar(10) NOT NULL,
  `soldier3` int(2) NOT NULL DEFAULT 0,
  `soldier3_npcid` int(6) NOT NULL DEFAULT 0,
  `soldier3_name` varchar(10) NOT NULL,
  `soldier4` int(2) NOT NULL DEFAULT 0,
  `soldier4_npcid` int(6) NOT NULL DEFAULT 0,
  `soldier4_name` varchar(10) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalyst`
--

CREATE TABLE `catalyst` (
  `nameId` int(6) NOT NULL DEFAULT 0,
  `nameId_kr` varchar(100) DEFAULT NULL,
  `nameId_en` varchar(100) NOT NULL,
  `input` int(6) NOT NULL DEFAULT 0,
  `input_kr` varchar(100) DEFAULT NULL,
  `input_en` varchar(100) NOT NULL,
  `successProb` int(3) NOT NULL DEFAULT 0,
  `broad` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalyst_custom`
--

CREATE TABLE `catalyst_custom` (
  `itemId` int(10) NOT NULL DEFAULT 0,
  `desc_kr` varchar(100) DEFAULT NULL,
  `desc_en` varchar(100) NOT NULL,
  `input_itemId` int(10) NOT NULL DEFAULT 0,
  `input_enchant` int(3) NOT NULL DEFAULT 0,
  `input_desc_kr` varchar(100) DEFAULT NULL,
  `input_desc` varchar(100) NOT NULL,
  `output_itemId` int(10) NOT NULL DEFAULT 0,
  `output_desc_kr` varchar(100) DEFAULT NULL,
  `output_desc` varchar(100) NOT NULL,
  `successProb` int(3) NOT NULL DEFAULT 100,
  `rewardCount` int(10) NOT NULL DEFAULT 1,
  `rewardEnchant` int(3) NOT NULL DEFAULT 0,
  `broad` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `characters`
--

CREATE TABLE `characters` (
  `account_name` varchar(50) DEFAULT NULL,
  `objid` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `char_name` varchar(45) NOT NULL DEFAULT '',
  `level` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `HighLevel` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `Exp` bigint(11) UNSIGNED NOT NULL DEFAULT 0,
  `MaxHp` int(5) NOT NULL DEFAULT 0,
  `CurHp` int(5) NOT NULL DEFAULT 0,
  `MaxMp` int(5) NOT NULL DEFAULT 0,
  `CurMp` int(5) NOT NULL DEFAULT 0,
  `Ac` int(3) NOT NULL DEFAULT 0,
  `Str` int(3) NOT NULL DEFAULT 0,
  `BaseStr` int(3) NOT NULL DEFAULT 0,
  `Con` int(3) NOT NULL DEFAULT 0,
  `BaseCon` int(3) NOT NULL DEFAULT 0,
  `Dex` int(3) NOT NULL DEFAULT 0,
  `BaseDex` int(3) NOT NULL DEFAULT 0,
  `Cha` int(3) NOT NULL DEFAULT 0,
  `BaseCha` int(3) NOT NULL DEFAULT 0,
  `Intel` int(3) NOT NULL DEFAULT 0,
  `BaseIntel` int(3) NOT NULL DEFAULT 0,
  `Wis` int(3) NOT NULL DEFAULT 0,
  `BaseWis` int(3) NOT NULL DEFAULT 0,
  `Status` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `Class` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `gender` int(1) UNSIGNED NOT NULL DEFAULT 0,
  `Type` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `Heading` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `LocX` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `LocY` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `MapID` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `Food` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Alignment` int(6) NOT NULL DEFAULT 0,
  `Title` varchar(35) NOT NULL DEFAULT '',
  `ClanID` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Clanname` varchar(45) NOT NULL,
  `ClanRank` int(3) NOT NULL DEFAULT 0,
  `ClanContribution` int(8) NOT NULL DEFAULT 0,
  `ClanWeekContribution` int(8) NOT NULL DEFAULT 0,
  `pledgeJoinDate` int(10) NOT NULL DEFAULT 0,
  `pledgeRankDate` int(10) NOT NULL DEFAULT 0,
  `notes` varchar(60) NOT NULL,
  `BonusStatus` int(4) NOT NULL DEFAULT 0,
  `ElixirStatus` int(2) NOT NULL DEFAULT 0,
  `ElfAttr` int(2) NOT NULL DEFAULT 0,
  `PKcount` int(6) NOT NULL DEFAULT 0,
  `ExpRes` int(10) NOT NULL DEFAULT 0,
  `PartnerID` int(10) NOT NULL DEFAULT 0,
  `AccessLevel` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `OnlineStatus` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `HomeTownID` int(2) NOT NULL DEFAULT 0,
  `Contribution` int(10) NOT NULL DEFAULT 0,
  `HellTime` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Banned` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `Karma` int(10) NOT NULL DEFAULT 0,
  `LastPk` datetime DEFAULT NULL,
  `DeleteTime` datetime DEFAULT NULL,
  `ReturnStat` bigint(10) NOT NULL,
  `lastLoginTime` datetime DEFAULT NULL,
  `lastLogoutTime` datetime DEFAULT NULL,
  `BirthDay` int(11) DEFAULT NULL,
  `PC_Kill` int(6) DEFAULT NULL,
  `PC_Death` int(6) DEFAULT NULL,
  `Mark_Count` int(10) NOT NULL DEFAULT 60,
  `TamEndTime` datetime DEFAULT NULL,
  `SpecialSize` int(3) NOT NULL DEFAULT 0,
  `HuntPrice` int(10) DEFAULT NULL,
  `HuntText` varchar(30) DEFAULT NULL,
  `HuntCount` int(10) DEFAULT NULL,
  `RingAddSlot` int(3) DEFAULT 0,
  `EarringAddSlot` int(3) DEFAULT 0,
  `BadgeAddSlot` int(3) DEFAULT 0,
  `ShoulderAddSlot` int(3) DEFAULT 0,
  `fatigue_point` int(3) NOT NULL DEFAULT 0,
  `fatigue_rest_time` datetime DEFAULT NULL,
  `EMETime` datetime DEFAULT NULL,
  `EMETime2` datetime DEFAULT NULL,
  `PUPLETime` datetime DEFAULT NULL,
  `TOPAZTime` datetime DEFAULT NULL,
  `EinhasadGraceTime` datetime DEFAULT NULL,
  `EinPoint` int(11) DEFAULT 0,
  `EinCardLess` int(2) NOT NULL DEFAULT 0,
  `EinCardState` int(3) NOT NULL DEFAULT 0,
  `EinCardBonusValue` int(1) NOT NULL DEFAULT 0,
  `ThirdSkillTime` datetime DEFAULT NULL,
  `FiveSkillTime` datetime DEFAULT NULL,
  `SurvivalTime` datetime DEFAULT NULL,
  `potentialTargetId` int(10) NOT NULL DEFAULT 0,
  `potentialBonusGrade` int(1) NOT NULL DEFAULT 0,
  `potentialBonusId` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_arca`
--

CREATE TABLE `character_arca` (
  `id` int(1) NOT NULL DEFAULT 0,
  `charId` int(10) NOT NULL DEFAULT 0,
  `day` int(3) NOT NULL DEFAULT 0,
  `useItemId` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_beginner_quest`
--

CREATE TABLE `character_beginner_quest` (
  `charId` int(10) NOT NULL DEFAULT 0,
  `info` text CHARACTER SET euckr NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_buddys`
--

CREATE TABLE `character_buddys` (
  `id` int(10) UNSIGNED NOT NULL,
  `char_id` int(10) NOT NULL DEFAULT 0,
  `buddy_name` varchar(45) NOT NULL,
  `buddy_memo` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_buff`
--

CREATE TABLE `character_buff` (
  `char_obj_id` int(10) NOT NULL DEFAULT 0,
  `skill_id` int(10) NOT NULL DEFAULT -1,
  `remaining_time` int(10) NOT NULL DEFAULT 0,
  `poly_id` int(10) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_companion`
--

CREATE TABLE `character_companion` (
  `item_objId` int(10) UNSIGNED NOT NULL,
  `objid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(50) NOT NULL,
  `npcId` int(10) UNSIGNED NOT NULL,
  `level` int(10) UNSIGNED NOT NULL DEFAULT 1,
  `exp` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `maxHp` int(5) UNSIGNED NOT NULL DEFAULT 160,
  `currentHp` int(5) UNSIGNED NOT NULL DEFAULT 160,
  `friend_ship_marble` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `friend_ship_guage` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `add_str` int(3) NOT NULL DEFAULT 0,
  `add_con` int(3) NOT NULL DEFAULT 0,
  `add_int` int(3) NOT NULL DEFAULT 0,
  `remain_stats` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `elixir_use_count` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `dead` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `oblivion` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `tier` int(1) NOT NULL DEFAULT 1,
  `wild` blob NOT NULL,
  `lessExp` int(10) NOT NULL DEFAULT 0,
  `traningTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_companion_buff`
--

CREATE TABLE `character_companion_buff` (
  `objid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `buff_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `duration` int(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_config`
--

CREATE TABLE `character_config` (
  `object_id` int(10) NOT NULL DEFAULT 0,
  `length` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `data` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_death_exp`
--

CREATE TABLE `character_death_exp` (
  `char_id` int(10) NOT NULL DEFAULT 0,
  `delete_time` datetime NOT NULL,
  `death_level` int(3) NOT NULL DEFAULT 0,
  `exp_value` int(11) NOT NULL DEFAULT 0,
  `recovery_cost` int(7) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_death_item`
--

CREATE TABLE `character_death_item` (
  `char_id` int(10) NOT NULL DEFAULT 0,
  `delete_time` datetime NOT NULL,
  `db_id` int(10) NOT NULL DEFAULT 0,
  `itemId` int(11) NOT NULL,
  `count` int(11) NOT NULL DEFAULT 0,
  `enchant` int(6) NOT NULL DEFAULT 0,
  `identi` enum('false','true') NOT NULL DEFAULT 'false',
  `chargeCount` int(11) NOT NULL DEFAULT 0,
  `bless` int(3) NOT NULL DEFAULT 1,
  `attrEnchant` int(2) NOT NULL DEFAULT 0,
  `specialEnchant` int(2) NOT NULL DEFAULT 0,
  `potential_id` int(3) NOT NULL DEFAULT 0,
  `slot_first` int(5) NOT NULL DEFAULT 0,
  `slot_second` int(5) NOT NULL DEFAULT 0,
  `recovery_cost` int(8) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_einhasadfaith`
--

CREATE TABLE `character_einhasadfaith` (
  `objId` int(11) NOT NULL DEFAULT 0,
  `groupId` int(3) NOT NULL DEFAULT 0,
  `indexId` int(3) NOT NULL DEFAULT 0,
  `spellId` int(6) NOT NULL DEFAULT -1,
  `expiredTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_einhasadstat`
--

CREATE TABLE `character_einhasadstat` (
  `objid` int(11) NOT NULL DEFAULT 0,
  `bless` int(3) NOT NULL DEFAULT 0,
  `lucky` int(3) NOT NULL DEFAULT 0,
  `vital` int(3) NOT NULL DEFAULT 0,
  `itemSpellProb` int(3) NOT NULL DEFAULT 0,
  `absoluteRegen` int(3) NOT NULL DEFAULT 0,
  `potion` int(3) NOT NULL DEFAULT 0,
  `bless_efficiency` int(3) NOT NULL DEFAULT 0,
  `bless_exp` int(3) NOT NULL DEFAULT 0,
  `lucky_item` int(3) NOT NULL DEFAULT 0,
  `lucky_adena` int(3) NOT NULL DEFAULT 0,
  `vital_potion` int(3) NOT NULL DEFAULT 0,
  `vital_heal` int(3) NOT NULL DEFAULT 0,
  `itemSpellProb_armor` int(3) NOT NULL DEFAULT 0,
  `itemSpellProb_weapon` int(3) NOT NULL DEFAULT 0,
  `absoluteRegen_hp` int(3) NOT NULL DEFAULT 0,
  `absoluteRegen_mp` int(3) NOT NULL DEFAULT 0,
  `potion_critical` int(3) NOT NULL DEFAULT 0,
  `potion_delay` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_elf_warehouse`
--

CREATE TABLE `character_elf_warehouse` (
  `id` int(11) NOT NULL,
  `account_name` varchar(50) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `is_equipped` int(11) DEFAULT NULL,
  `enchantlvl` int(11) DEFAULT NULL,
  `is_id` int(11) DEFAULT NULL,
  `durability` int(11) DEFAULT NULL,
  `charge_count` int(11) DEFAULT NULL,
  `remaining_time` int(11) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  `attr_enchantlvl` int(11) DEFAULT NULL,
  `special_enchant` int(11) DEFAULT NULL,
  `doll_ablity` int(4) DEFAULT NULL,
  `bless` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_equipset`
--

CREATE TABLE `character_equipset` (
  `charId` int(10) NOT NULL DEFAULT 0,
  `current_set` int(1) NOT NULL DEFAULT 0,
  `slot1_item` text CHARACTER SET euckr DEFAULT NULL,
  `slot2_item` text CHARACTER SET euckr DEFAULT NULL,
  `slot3_item` text CHARACTER SET euckr DEFAULT NULL,
  `slot4_item` text CHARACTER SET euckr DEFAULT NULL,
  `slot1_name` varchar(100) CHARACTER SET euckr NOT NULL DEFAULT '',
  `slot2_name` varchar(100) CHARACTER SET euckr NOT NULL DEFAULT '',
  `slot3_name` varchar(100) CHARACTER SET euckr NOT NULL DEFAULT '',
  `slot4_name` varchar(100) CHARACTER SET euckr NOT NULL DEFAULT '',
  `slot1_color` int(2) NOT NULL DEFAULT 0,
  `slot2_color` int(2) NOT NULL DEFAULT 0,
  `slot3_color` int(2) NOT NULL DEFAULT 0,
  `slot4_color` int(2) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_eventpush`
--

CREATE TABLE `character_eventpush` (
  `push_id` int(10) NOT NULL DEFAULT 0,
  `objId` int(10) NOT NULL DEFAULT 0,
  `subject` varchar(45) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `web_url` varchar(200) DEFAULT '',
  `itemId` int(11) NOT NULL DEFAULT 0,
  `item_amount` int(11) NOT NULL DEFAULT 0,
  `item_enchant` int(6) DEFAULT 0,
  `doll_ablity` int(4) DEFAULT NULL,
  `used_immediately` enum('false','true') NOT NULL DEFAULT 'false',
  `status` int(2) NOT NULL DEFAULT 0,
  `enable_date` datetime NOT NULL,
  `image_id` int(6) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_exclude`
--

CREATE TABLE `character_exclude` (
  `id` int(10) UNSIGNED NOT NULL,
  `char_id` int(10) NOT NULL DEFAULT 0,
  `type` int(2) NOT NULL DEFAULT 0,
  `exclude_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `exclude_name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='MyISAM free: 10240 kB; MyISAM free: 10240 kB';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_fairly_config`
--

CREATE TABLE `character_fairly_config` (
  `object_id` int(10) NOT NULL,
  `data` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_favorbook`
--

CREATE TABLE `character_favorbook` (
  `charObjId` int(10) NOT NULL DEFAULT 0,
  `category` int(3) NOT NULL DEFAULT 0,
  `slotId` int(1) NOT NULL DEFAULT 0,
  `itemObjId` int(10) NOT NULL DEFAULT 0,
  `itemId` int(10) NOT NULL DEFAULT 0,
  `itemName` varchar(255) DEFAULT '',
  `count` int(10) NOT NULL DEFAULT 1,
  `enchantLevel` int(10) NOT NULL DEFAULT 0,
  `attrLevel` int(10) NOT NULL DEFAULT 0,
  `bless` int(3) NOT NULL DEFAULT 1,
  `endTime` datetime DEFAULT NULL,
  `craftId` int(6) NOT NULL DEFAULT 0,
  `awakening` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_hunting_quest`
--

CREATE TABLE `character_hunting_quest` (
  `id` int(10) NOT NULL DEFAULT 0,
  `objID` int(10) NOT NULL DEFAULT 0,
  `map_number` int(6) DEFAULT 0,
  `location_desc` int(3) DEFAULT 0,
  `quest_id` int(3) DEFAULT NULL,
  `kill_count` int(3) DEFAULT NULL,
  `complete` enum('true','false') DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_items`
--

CREATE TABLE `character_items` (
  `id` int(11) NOT NULL DEFAULT 0,
  `item_id` int(11) NOT NULL,
  `char_id` int(11) NOT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `count` int(11) NOT NULL DEFAULT 0,
  `is_equipped` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `enchantlvl` int(11) NOT NULL DEFAULT 0,
  `is_id` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `durability` int(2) NOT NULL DEFAULT 0,
  `charge_count` int(11) NOT NULL DEFAULT 0,
  `remaining_time` int(11) NOT NULL DEFAULT 0,
  `last_used` datetime DEFAULT NULL,
  `bless` int(3) NOT NULL DEFAULT 1,
  `attr_enchantlvl` int(3) NOT NULL DEFAULT 0,
  `special_enchant` int(3) NOT NULL DEFAULT 0,
  `doll_ablity` int(4) NOT NULL DEFAULT 0,
  `end_time` datetime DEFAULT NULL,
  `KeyVal` int(6) NOT NULL DEFAULT 0,
  `package` tinyint(1) NOT NULL DEFAULT 0,
  `engrave` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `scheduled` tinyint(1) NOT NULL DEFAULT 0,
  `slot_0` int(5) NOT NULL DEFAULT 0,
  `slot_1` int(5) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_monsterbooklist`
--

CREATE TABLE `character_monsterbooklist` (
  `id` int(10) UNSIGNED NOT NULL,
  `monsterlist` text CHARACTER SET euckr NOT NULL,
  `monquest` text CHARACTER SET euckr NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_package_warehouse`
--

CREATE TABLE `character_package_warehouse` (
  `id` int(11) NOT NULL,
  `account_name` varchar(50) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `is_equipped` int(11) DEFAULT NULL,
  `enchantlvl` int(11) DEFAULT NULL,
  `is_id` int(11) DEFAULT NULL,
  `durability` int(11) DEFAULT NULL,
  `charge_count` int(11) DEFAULT NULL,
  `remaining_time` int(11) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  `attr_enchantlvl` int(11) DEFAULT NULL,
  `bless` int(11) DEFAULT 0,
  `special_enchant` int(11) DEFAULT NULL,
  `doll_ablity` int(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_present_warehouse`
--

CREATE TABLE `character_present_warehouse` (
  `id` int(11) NOT NULL,
  `account_name` varchar(50) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `is_equipped` int(11) DEFAULT NULL,
  `enchantlvl` int(11) DEFAULT NULL,
  `is_id` int(11) DEFAULT NULL,
  `durability` int(11) DEFAULT NULL,
  `charge_count` int(11) DEFAULT NULL,
  `remaining_time` int(11) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  `attr_enchantlvl` int(11) DEFAULT NULL,
  `special_enchant` int(11) DEFAULT NULL,
  `doll_ablity` int(4) DEFAULT NULL,
  `bless` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_quests`
--

CREATE TABLE `character_quests` (
  `char_id` int(10) UNSIGNED NOT NULL,
  `quest_id` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `quest_step` int(3) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_revenge`
--

CREATE TABLE `character_revenge` (
  `number` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `char_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `result` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `chasestarttime` datetime DEFAULT NULL,
  `chaseendtime` datetime DEFAULT NULL,
  `usecount` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `amount` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `targetobjid` int(10) NOT NULL DEFAULT 0,
  `targetclass` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `targetname` varchar(45) NOT NULL DEFAULT '',
  `targetclanid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `targetclanname` varchar(45) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_shop_limit`
--

CREATE TABLE `character_shop_limit` (
  `characterId` int(10) NOT NULL DEFAULT 0,
  `buyShopId` int(9) NOT NULL DEFAULT 0,
  `buyItemId` int(9) NOT NULL DEFAULT 0,
  `buyCount` int(9) NOT NULL DEFAULT 0,
  `buyTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  `limitTerm` enum('WEEK','DAY','NONE') NOT NULL DEFAULT 'DAY'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_skills_active`
--

CREATE TABLE `character_skills_active` (
  `char_obj_id` int(10) NOT NULL DEFAULT 0,
  `skill_id` int(10) NOT NULL DEFAULT -1,
  `skill_name` varchar(30) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_skills_passive`
--

CREATE TABLE `character_skills_passive` (
  `char_obj_id` int(10) NOT NULL DEFAULT 0,
  `passive_id` int(10) NOT NULL DEFAULT 0,
  `passive_name` varchar(30) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_soldier`
--

CREATE TABLE `character_soldier` (
  `char_id` int(12) NOT NULL,
  `npc_id` int(12) NOT NULL DEFAULT 0,
  `count` int(4) NOT NULL DEFAULT 0,
  `castle_id` int(4) NOT NULL DEFAULT 0,
  `time` int(18) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_special_warehouse`
--

CREATE TABLE `character_special_warehouse` (
  `id` int(11) NOT NULL,
  `account_name` varchar(50) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `is_equipped` int(11) DEFAULT NULL,
  `enchantlvl` int(11) DEFAULT NULL,
  `is_id` int(11) DEFAULT NULL,
  `durability` int(11) DEFAULT NULL,
  `charge_count` int(11) DEFAULT NULL,
  `remaining_time` int(11) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  `attr_enchantlvl` int(11) DEFAULT NULL,
  `doll_ablity` int(4) DEFAULT NULL,
  `bless` int(11) DEFAULT 0,
  `second_id` int(11) DEFAULT NULL,
  `round_id` int(11) DEFAULT NULL,
  `ticket_id` int(11) DEFAULT NULL,
  `maan_time` datetime DEFAULT NULL,
  `regist_level` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_teleport`
--

CREATE TABLE `character_teleport` (
  `id` int(10) UNSIGNED NOT NULL,
  `num_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `speed_id` int(10) NOT NULL DEFAULT -1,
  `char_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `randomX` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `randomY` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `item_obj_id` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_timecollection`
--

CREATE TABLE `character_timecollection` (
  `charObjId` int(10) NOT NULL DEFAULT 0,
  `groupId` int(1) NOT NULL DEFAULT 0,
  `setId` int(1) NOT NULL DEFAULT 0,
  `slots` text DEFAULT NULL,
  `registComplet` enum('false','true') NOT NULL DEFAULT 'false',
  `buffType` enum('SHORT','LONG','MAGIC') NOT NULL DEFAULT 'MAGIC',
  `buffTime` datetime DEFAULT NULL,
  `refillCount` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `character_warehouse`
--

CREATE TABLE `character_warehouse` (
  `id` int(11) NOT NULL,
  `account_name` varchar(50) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `is_equipped` int(11) DEFAULT NULL,
  `enchantlvl` int(11) DEFAULT NULL,
  `is_id` int(11) DEFAULT NULL,
  `durability` int(11) DEFAULT NULL,
  `charge_count` int(11) DEFAULT NULL,
  `remaining_time` int(11) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  `attr_enchantlvl` int(11) DEFAULT NULL,
  `bless` int(11) DEFAULT 0,
  `special_enchant` int(11) DEFAULT NULL,
  `doll_ablity` int(4) DEFAULT NULL,
  `package` tinyint(3) DEFAULT 0,
  `buy_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_bless_buff`
--

CREATE TABLE `clan_bless_buff` (
  `number` int(10) UNSIGNED NOT NULL,
  `buff_id` int(10) NOT NULL DEFAULT -1,
  `map_name` varchar(45) NOT NULL DEFAULT '',
  `teleport_map_id` int(6) UNSIGNED DEFAULT 0,
  `teleport_x` int(6) UNSIGNED DEFAULT 0,
  `teleport_y` int(6) UNSIGNED DEFAULT 0,
  `buff_map_list` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_contribution_buff`
--

CREATE TABLE `clan_contribution_buff` (
  `clan_id` int(10) UNSIGNED NOT NULL,
  `clan_name` varchar(45) NOT NULL DEFAULT '',
  `exp_buff_type` int(1) UNSIGNED DEFAULT 0,
  `exp_buff_time` datetime DEFAULT NULL,
  `battle_buff_type` int(1) UNSIGNED DEFAULT 0,
  `battle_buff_time` datetime DEFAULT NULL,
  `defens_buff_type` int(1) UNSIGNED DEFAULT 0,
  `defens_buff_time` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_data`
--

CREATE TABLE `clan_data` (
  `clan_id` int(10) UNSIGNED NOT NULL,
  `clan_name` varchar(45) NOT NULL DEFAULT '',
  `leader_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `leader_name` varchar(45) NOT NULL DEFAULT '',
  `hascastle` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `hashouse` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `alliance` varchar(100) DEFAULT NULL,
  `clan_birthday` datetime NOT NULL,
  `bot` enum('true','false') NOT NULL DEFAULT 'false',
  `bot_style` tinyint(3) NOT NULL DEFAULT 0,
  `bot_level` tinyint(3) NOT NULL DEFAULT 0,
  `max_online_user` int(10) NOT NULL DEFAULT 0,
  `announcement` varchar(160) NOT NULL,
  `introductionMessage` varchar(160) NOT NULL,
  `enter_notice` varchar(160) NOT NULL,
  `emblem_id` int(10) NOT NULL DEFAULT 0,
  `emblem_status` tinyint(1) NOT NULL DEFAULT 0,
  `contribution` int(10) NOT NULL DEFAULT 0,
  `bless` int(45) NOT NULL DEFAULT 0,
  `bless_count` int(45) NOT NULL DEFAULT 0,
  `attack` int(45) NOT NULL DEFAULT 0,
  `defence` int(45) NOT NULL DEFAULT 0,
  `pvpattack` int(45) NOT NULL DEFAULT 0,
  `pvpdefence` int(45) NOT NULL DEFAULT 0,
  `under_dungeon` tinyint(3) NOT NULL DEFAULT 0,
  `ranktime` int(10) NOT NULL DEFAULT 0,
  `rankdate` datetime DEFAULT NULL,
  `War_point` int(10) NOT NULL DEFAULT 0,
  `enable_join` enum('true','false') NOT NULL DEFAULT 'true',
  `join_type` int(1) NOT NULL DEFAULT 1,
  `total_m` int(10) NOT NULL DEFAULT 0,
  `current_m` int(10) NOT NULL DEFAULT 0,
  `join_password` varchar(45) DEFAULT NULL,
  `EinhasadBlessBuff` int(10) DEFAULT NULL,
  `Buff_List1` int(10) DEFAULT NULL,
  `Buff_List2` int(10) DEFAULT NULL,
  `Buff_List3` int(10) DEFAULT NULL,
  `dayDungeonTime` datetime DEFAULT NULL,
  `weekDungeonTime` datetime DEFAULT NULL,
  `vowTime` datetime DEFAULT NULL,
  `vowCount` int(1) NOT NULL DEFAULT 0,
  `clanNameChange` enum('true','false') NOT NULL DEFAULT 'false',
  `storeAllows` text DEFAULT NULL,
  `limit_level` int(3) NOT NULL DEFAULT 30,
  `limit_user_names` text DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_emblem_attention`
--

CREATE TABLE `clan_emblem_attention` (
  `clanname` varchar(45) NOT NULL DEFAULT '',
  `attentionClanname` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_history`
--

CREATE TABLE `clan_history` (
  `num` int(11) NOT NULL,
  `clan_id` int(11) NOT NULL DEFAULT 0,
  `ckck` int(2) NOT NULL DEFAULT 0,
  `char_name` varchar(50) NOT NULL DEFAULT '',
  `item_name` varchar(50) NOT NULL DEFAULT '',
  `item_count` int(11) NOT NULL DEFAULT 0,
  `time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_joinning`
--

CREATE TABLE `clan_joinning` (
  `pledge_uid` int(10) NOT NULL DEFAULT 0,
  `pledge_name` varchar(50) DEFAULT NULL,
  `user_uid` int(10) NOT NULL DEFAULT 0,
  `user_name` varchar(50) NOT NULL,
  `join_message` varchar(100) DEFAULT NULL,
  `class_type` int(2) NOT NULL DEFAULT 0,
  `join_date` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_matching_apclist`
--

CREATE TABLE `clan_matching_apclist` (
  `pc_name` varchar(45) NOT NULL DEFAULT '',
  `pc_objid` int(10) DEFAULT NULL,
  `clan_name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_matching_list`
--

CREATE TABLE `clan_matching_list` (
  `clanname` varchar(45) NOT NULL DEFAULT '',
  `text` varchar(500) DEFAULT NULL,
  `type` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_warehouse`
--

CREATE TABLE `clan_warehouse` (
  `id` int(11) NOT NULL,
  `clan_name` varchar(45) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `is_equipped` int(11) DEFAULT NULL,
  `enchantlvl` int(11) DEFAULT NULL,
  `is_id` int(11) DEFAULT NULL,
  `durability` int(11) DEFAULT NULL,
  `charge_count` int(11) DEFAULT NULL,
  `remaining_time` int(11) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  `attr_enchantlvl` int(11) DEFAULT NULL,
  `special_enchant` int(11) DEFAULT NULL,
  `doll_ablity` int(4) DEFAULT 0,
  `package` tinyint(3) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_warehouse_list`
--

CREATE TABLE `clan_warehouse_list` (
  `id` int(10) NOT NULL,
  `clanid` int(11) DEFAULT 0,
  `list` varchar(200) DEFAULT '',
  `date` varchar(20) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clan_warehouse_log`
--

CREATE TABLE `clan_warehouse_log` (
  `id` int(1) UNSIGNED NOT NULL,
  `name` varchar(30) NOT NULL DEFAULT '',
  `clan_name` varchar(30) NOT NULL DEFAULT '',
  `item_name` varchar(30) NOT NULL DEFAULT '',
  `item_count` int(1) UNSIGNED NOT NULL,
  `type` bit(1) NOT NULL,
  `time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `commands`
--

CREATE TABLE `commands` (
  `name` varchar(255) NOT NULL,
  `access_level` int(10) NOT NULL DEFAULT 9999,
  `class_name` varchar(255) NOT NULL,
  `description` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `connect_reward`
--

CREATE TABLE `connect_reward` (
  `id` int(3) NOT NULL DEFAULT 0,
  `description` varchar(50) DEFAULT NULL,
  `desc_kr` varchar(50) NOT NULL,
  `reward_type` enum('NORMAL','STANBY_SERVER') NOT NULL DEFAULT 'NORMAL',
  `reward_item_id` int(10) NOT NULL DEFAULT 0,
  `reward_item_count` int(10) NOT NULL DEFAULT 0,
  `reward_interval_minute` int(6) NOT NULL DEFAULT 0,
  `reward_start_date` datetime DEFAULT NULL,
  `reward_finish_date` datetime DEFAULT NULL,
  `is_use` enum('true','false') NOT NULL DEFAULT 'true'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `craft_block`
--

CREATE TABLE `craft_block` (
  `craft_id` int(5) NOT NULL DEFAULT 0,
  `craft_name` varchar(45) DEFAULT NULL,
  `desc_kr` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `craft_info`
--

CREATE TABLE `craft_info` (
  `craft_id` int(10) UNSIGNED NOT NULL,
  `name` varchar(45) NOT NULL DEFAULT '',
  `desc_kr` varchar(45) NOT NULL,
  `output_name_id` int(10) NOT NULL DEFAULT 0,
  `probability_million` int(10) NOT NULL DEFAULT 0,
  `preserve_name_ids` text DEFAULT NULL,
  `success_preserve_count` text DEFAULT NULL,
  `failure_preserve_count` text DEFAULT NULL,
  `is_success_count_type` enum('false','true') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `craft_npcs`
--

CREATE TABLE `craft_npcs` (
  `npc_id` int(10) UNSIGNED NOT NULL,
  `npc_name` varchar(45) NOT NULL DEFAULT '',
  `desc_kr` varchar(45) NOT NULL,
  `craft_id_list` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `craft_success_count_user`
--

CREATE TABLE `craft_success_count_user` (
  `accountName` varchar(50) NOT NULL,
  `charId` int(10) NOT NULL DEFAULT 0,
  `craftId` int(6) NOT NULL DEFAULT 0,
  `success_count_type` enum('World','Account','Character','AllServers') NOT NULL DEFAULT 'World',
  `currentCount` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dogfight_tickets`
--

CREATE TABLE `dogfight_tickets` (
  `item_id` int(10) NOT NULL DEFAULT 0,
  `name` varchar(45) CHARACTER SET euckr NOT NULL,
  `price` int(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `droplist`
--

CREATE TABLE `droplist` (
  `mobId` int(6) NOT NULL DEFAULT 0,
  `mobname_kr` varchar(100) NOT NULL,
  `mobname_en` varchar(100) NOT NULL,
  `moblevel` int(10) NOT NULL DEFAULT 0,
  `itemId` int(6) NOT NULL DEFAULT 0,
  `itemname_kr` varchar(50) NOT NULL,
  `itemname_en` varchar(100) NOT NULL,
  `min` int(4) NOT NULL DEFAULT 0,
  `max` int(4) NOT NULL DEFAULT 0,
  `chance` int(8) NOT NULL DEFAULT 0,
  `Enchant` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `droptype_npc`
--

CREATE TABLE `droptype_npc` (
  `mobId` int(11) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `desc_kr` varchar(45) NOT NULL,
  `type` enum('map','share') NOT NULL DEFAULT 'map'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dungeon`
--

CREATE TABLE `dungeon` (
  `src_x` int(10) NOT NULL DEFAULT 0,
  `src_y` int(10) NOT NULL DEFAULT 0,
  `src_mapid` int(10) NOT NULL DEFAULT 0,
  `new_x` int(10) NOT NULL DEFAULT 0,
  `new_y` int(10) NOT NULL DEFAULT 0,
  `new_mapid` int(10) NOT NULL DEFAULT 0,
  `new_heading` int(10) NOT NULL DEFAULT 1,
  `min_level` int(3) NOT NULL DEFAULT 0,
  `max_level` int(3) NOT NULL DEFAULT 0,
  `note` varchar(50) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dungeon_random`
--

CREATE TABLE `dungeon_random` (
  `src_x` int(10) NOT NULL DEFAULT 0,
  `src_y` int(10) NOT NULL DEFAULT 0,
  `src_mapid` int(10) NOT NULL DEFAULT 0,
  `new_x1` int(10) NOT NULL DEFAULT 0,
  `new_y1` int(10) NOT NULL DEFAULT 0,
  `new_mapid1` int(10) NOT NULL DEFAULT 0,
  `new_x2` int(10) NOT NULL DEFAULT 0,
  `new_y2` int(10) NOT NULL DEFAULT 0,
  `new_mapid2` int(10) NOT NULL DEFAULT 0,
  `new_x3` int(10) NOT NULL DEFAULT 0,
  `new_y3` int(10) NOT NULL DEFAULT 0,
  `new_mapid3` int(10) NOT NULL DEFAULT 0,
  `new_x4` int(10) NOT NULL DEFAULT 0,
  `new_y4` int(10) NOT NULL DEFAULT 0,
  `new_mapid4` int(10) NOT NULL DEFAULT 0,
  `new_x5` int(10) NOT NULL DEFAULT 0,
  `new_y5` int(10) NOT NULL DEFAULT 0,
  `new_mapid5` int(10) NOT NULL DEFAULT 0,
  `new_heading` int(10) NOT NULL DEFAULT 1,
  `note` varchar(50) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dungeon_timer`
--

CREATE TABLE `dungeon_timer` (
  `timerId` int(3) NOT NULL DEFAULT 0,
  `desc` varchar(50) DEFAULT NULL,
  `desc_kr` varchar(50) NOT NULL,
  `descId` varchar(50) DEFAULT NULL,
  `useType` enum('ACCOUNT','CHARACTER') NOT NULL DEFAULT 'ACCOUNT',
  `mapIds` text DEFAULT NULL,
  `timerValue` int(9) NOT NULL DEFAULT 0,
  `bonusLevel` int(3) NOT NULL DEFAULT 0,
  `bonusValue` int(9) NOT NULL DEFAULT 0,
  `pccafeBonusValue` int(9) NOT NULL DEFAULT 0,
  `resetType` enum('DAY','WEEK','NONE') NOT NULL DEFAULT 'DAY',
  `minLimitLevel` int(3) NOT NULL DEFAULT 0,
  `maxLimitLevel` int(3) NOT NULL DEFAULT 0,
  `serialId` int(6) NOT NULL DEFAULT 0,
  `serialDescId` varchar(50) DEFAULT NULL,
  `maxChargeCount` int(3) NOT NULL DEFAULT 0,
  `group` enum('NONE','HIDDEN_FIELD','SILVER_KNIGHT_DUNGEON','HIDDEN_FIELD_BOOST') NOT NULL DEFAULT 'NONE'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dungeon_timer_account`
--

CREATE TABLE `dungeon_timer_account` (
  `account` varchar(50) NOT NULL,
  `timerId` int(10) NOT NULL DEFAULT 0,
  `remainSecond` int(10) NOT NULL DEFAULT 0,
  `chargeCount` int(2) NOT NULL DEFAULT 0,
  `resetTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dungeon_timer_character`
--

CREATE TABLE `dungeon_timer_character` (
  `charId` int(10) NOT NULL DEFAULT 0,
  `timerId` int(10) NOT NULL DEFAULT 0,
  `remainSecond` int(10) NOT NULL DEFAULT 0,
  `chargeCount` int(2) NOT NULL DEFAULT 0,
  `resetTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dungeon_timer_item`
--

CREATE TABLE `dungeon_timer_item` (
  `itemId` int(10) NOT NULL DEFAULT 0,
  `desc` varchar(50) DEFAULT NULL,
  `desc_kr` varchar(50) NOT NULL,
  `timerId` int(3) NOT NULL DEFAULT 0,
  `groupId` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `enchant_result`
--

CREATE TABLE `enchant_result` (
  `item_id` int(10) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `desc_kr` varchar(45) NOT NULL,
  `color_item` enum('false','true') NOT NULL DEFAULT 'false',
  `bm_scroll` enum('false','true') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `etcitem`
--

CREATE TABLE `etcitem` (
  `item_id` int(10) UNSIGNED NOT NULL,
  `item_name_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `desc_kr` varchar(45) NOT NULL DEFAULT '',
  `desc_id` varchar(45) NOT NULL DEFAULT '',
  `itemGrade` enum('ONLY','MYTH','LEGEND','HERO','RARE','ADVANC','NORMAL') NOT NULL DEFAULT 'NORMAL',
  `item_type` enum('ARROW','WAND','LIGHT','GEM','TOTEM','FIRE_CRACKER','POTION','FOOD','SCROLL','QUEST_ITEM','SPELL_BOOK','PET_ITEM','OTHER','MATERIAL','EVENT','STING','TREASURE_BOX') NOT NULL DEFAULT 'OTHER',
  `use_type` enum('NONE','NORMAL','WAND1','WAND','SPELL_LONG','NTELE','IDENTIFY','RES','TELEPORT','INVISABLE','LETTER','LETTER_W','CHOICE','INSTRUMENT','SOSC','SPELL_SHORT','T_SHIRT','CLOAK','GLOVE','BOOTS','HELMET','RING','AMULET','SHIELD','GARDER','DAI','ZEL','BLANK','BTELE','SPELL_BUFF','CCARD','CCARD_W','VCARD','VCARD_W','WCARD','WCARD_W','BELT','SPELL_LONG2','EARRING','FISHING_ROD','RON','RON_2','ACCZEL','PAIR','HEALING','SHOULDER','BADGE','POTENTIAL_SCROLL','SPELLMELT','ELIXER_RON','INVENTORY_BONUS','TAM_FRUIT','RACE_TICKET','PAIR_2','MAGICDOLL','SENTENCE','SHOULDER_2','BADGE_2','PET_POTION','GARDER_2','DOMINATION_POLY','PENDANT','SHOVEL','LEV_100_POLY','SMELTING','PURIFY','CHARGED_MAP_TIME') NOT NULL DEFAULT 'NONE',
  `material` enum('NONE(-)','LIQUID(액체)','WAX(밀랍)','VEGGY(식물성)','FLESH(동물성)','PAPER(종이)','CLOTH(천)','LEATHER(가죽)','WOOD(나무)','BONE(뼈)','DRAGON_HIDE(용비늘)','IRON(철)','METAL(금속)','COPPER(구리)','SILVER(은)','GOLD(금)','PLATINUM(백금)','MITHRIL(미스릴)','PLASTIC(블랙미스릴)','GLASS(유리)','GEMSTONE(보석)','MINERAL(광석)','ORIHARUKON(오리하루콘)','DRANIUM(드라니움)') NOT NULL DEFAULT 'NONE(-)',
  `weight` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `iconId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `spriteId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `merge` enum('true','false') NOT NULL DEFAULT 'false',
  `max_charge_count` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `dmg_small` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `dmg_large` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `ac_bonus` int(3) NOT NULL DEFAULT 0,
  `shortHit` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `shortDmg` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `longHit` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `longDmg` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `add_str` int(2) NOT NULL DEFAULT 0,
  `add_con` int(2) NOT NULL DEFAULT 0,
  `add_dex` int(2) NOT NULL DEFAULT 0,
  `add_int` int(2) NOT NULL DEFAULT 0,
  `add_wis` int(2) NOT NULL DEFAULT 0,
  `add_cha` int(2) NOT NULL DEFAULT 0,
  `add_hp` int(6) NOT NULL DEFAULT 0,
  `add_mp` int(6) NOT NULL DEFAULT 0,
  `add_hpr` int(6) NOT NULL DEFAULT 0,
  `add_mpr` int(6) NOT NULL DEFAULT 0,
  `add_sp` int(2) NOT NULL DEFAULT 0,
  `min_lvl` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `max_lvl` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `m_def` int(2) NOT NULL DEFAULT 0,
  `carryBonus` int(4) NOT NULL DEFAULT 0,
  `defense_water` int(2) NOT NULL DEFAULT 0,
  `defense_wind` int(2) NOT NULL DEFAULT 0,
  `defense_fire` int(2) NOT NULL DEFAULT 0,
  `defense_earth` int(2) NOT NULL DEFAULT 0,
  `attr_all` int(2) NOT NULL DEFAULT 0,
  `regist_stone` int(2) NOT NULL DEFAULT 0,
  `regist_sleep` int(2) NOT NULL DEFAULT 0,
  `regist_freeze` int(2) NOT NULL DEFAULT 0,
  `regist_blind` int(2) NOT NULL DEFAULT 0,
  `regist_skill` int(2) NOT NULL DEFAULT 0,
  `regist_spirit` int(2) NOT NULL DEFAULT 0,
  `regist_dragon` int(2) NOT NULL DEFAULT 0,
  `regist_fear` int(2) NOT NULL DEFAULT 0,
  `regist_all` int(2) NOT NULL DEFAULT 0,
  `hitup_skill` int(2) NOT NULL DEFAULT 0,
  `hitup_spirit` int(2) NOT NULL DEFAULT 0,
  `hitup_dragon` int(2) NOT NULL DEFAULT 0,
  `hitup_fear` int(2) NOT NULL DEFAULT 0,
  `hitup_all` int(2) NOT NULL DEFAULT 0,
  `hitup_magic` int(2) NOT NULL DEFAULT 0,
  `damage_reduction` int(2) NOT NULL DEFAULT 0,
  `MagicDamageReduction` int(2) NOT NULL DEFAULT 0,
  `reductionEgnor` int(2) NOT NULL DEFAULT 0,
  `reductionPercent` int(2) NOT NULL DEFAULT 0,
  `PVPDamage` int(2) NOT NULL DEFAULT 0,
  `PVPDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPDamageReductionPercent` int(2) NOT NULL DEFAULT 0,
  `PVPMagicDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `PVPMagicDamageReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusDamageReduction` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusPVPDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPDamagePercent` int(2) NOT NULL DEFAULT 0,
  `expBonus` int(3) NOT NULL DEFAULT 0,
  `rest_exp_reduce_efficiency` int(3) NOT NULL DEFAULT 0,
  `shortCritical` int(2) NOT NULL DEFAULT 0,
  `longCritical` int(2) NOT NULL DEFAULT 0,
  `magicCritical` int(2) NOT NULL DEFAULT 0,
  `addDg` int(2) NOT NULL DEFAULT 0,
  `addEr` int(2) NOT NULL DEFAULT 0,
  `addMe` int(2) NOT NULL DEFAULT 0,
  `poisonRegist` enum('false','true') NOT NULL DEFAULT 'false',
  `imunEgnor` int(3) NOT NULL DEFAULT 0,
  `stunDuration` int(2) NOT NULL DEFAULT 0,
  `tripleArrowStun` int(2) NOT NULL DEFAULT 0,
  `strangeTimeIncrease` int(4) NOT NULL DEFAULT 0,
  `strangeTimeDecrease` int(4) NOT NULL DEFAULT 0,
  `potionRegist` int(2) NOT NULL DEFAULT 0,
  `potionPercent` int(2) NOT NULL DEFAULT 0,
  `potionValue` int(2) NOT NULL DEFAULT 0,
  `hprAbsol32Second` int(2) NOT NULL DEFAULT 0,
  `mprAbsol64Second` int(2) NOT NULL DEFAULT 0,
  `mprAbsol16Second` int(2) NOT NULL DEFAULT 0,
  `hpPotionDelayDecrease` int(4) NOT NULL DEFAULT 0,
  `hpPotionCriticalProb` int(4) NOT NULL DEFAULT 0,
  `increaseArmorSkillProb` int(4) NOT NULL DEFAULT 0,
  `attackSpeedDelayRate` int(3) NOT NULL DEFAULT 0,
  `moveSpeedDelayRate` int(3) NOT NULL DEFAULT 0,
  `buffDurationSecond` int(8) NOT NULL DEFAULT 0,
  `locx` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `bless` int(2) UNSIGNED NOT NULL DEFAULT 1,
  `trade` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `retrieve` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `specialretrieve` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `cant_delete` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `cant_sell` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `delay_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `delay_time` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `delay_effect` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `food_volume` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `save_at_once` tinyint(1) UNSIGNED NOT NULL DEFAULT 1,
  `Magic_name` varchar(20) DEFAULT NULL,
  `level` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `attr` enum('EARTH','AIR','WATER','FIRE','NONE') NOT NULL DEFAULT 'NONE',
  `alignment` enum('CAOTIC','NEUTRAL','LAWFUL','NONE') NOT NULL DEFAULT 'NONE',
  `use_royal` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_knight` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_mage` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_elf` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_darkelf` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_dragonknight` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_illusionist` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_warrior` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_fencer` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_lancer` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `skill_type` enum('passive','active','none') NOT NULL DEFAULT 'none',
  `etc_value` int(10) NOT NULL DEFAULT 0,
  `limit_type` enum('WORLD_WAR','BEGIN_ZONE','NONE') NOT NULL DEFAULT 'NONE',
  `prob` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `event`
--

CREATE TABLE `event` (
  `event_id` int(10) NOT NULL DEFAULT 0,
  `description` varchar(50) DEFAULT NULL,
  `desc_kr` varchar(50) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `finish_date` datetime DEFAULT NULL,
  `broadcast` enum('true','false') NOT NULL DEFAULT 'false',
  `event_flag` enum('SPAWN_NPC','DROP_ADENA','DROP_ITEM','POLY') NOT NULL DEFAULT 'SPAWN_NPC',
  `spawn_data` text DEFAULT NULL,
  `drop_rate` float NOT NULL DEFAULT 1,
  `finish_delete_item` text DEFAULT NULL,
  `finish_map_rollback` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `exp`
--

CREATE TABLE `exp` (
  `level` int(10) NOT NULL,
  `exp` int(11) NOT NULL DEFAULT 0,
  `panalty` varchar(100) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `favorbook`
--

CREATE TABLE `favorbook` (
  `listId` int(2) NOT NULL DEFAULT 0,
  `category` int(3) NOT NULL DEFAULT 0,
  `slotId` int(1) NOT NULL,
  `itemIds` text DEFAULT NULL,
  `note` varchar(100) DEFAULT '',
  `desc_kr` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `free_pvp_region`
--

CREATE TABLE `free_pvp_region` (
  `worldNumber` int(6) NOT NULL DEFAULT 0,
  `desc` varchar(50) DEFAULT NULL,
  `desc_kr` varchar(50) NOT NULL,
  `isFreePvpZone` enum('true','false') NOT NULL DEFAULT 'true',
  `box_index` int(3) NOT NULL DEFAULT 0,
  `box_sx` int(5) NOT NULL DEFAULT 0,
  `box_sy` int(5) NOT NULL DEFAULT 0,
  `box_ex` int(5) NOT NULL DEFAULT 0,
  `box_ey` int(5) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `getback`
--

CREATE TABLE `getback` (
  `area_x1` int(10) NOT NULL DEFAULT 0,
  `area_y1` int(10) NOT NULL DEFAULT 0,
  `area_x2` int(10) NOT NULL DEFAULT 0,
  `area_y2` int(10) NOT NULL DEFAULT 0,
  `area_mapid` int(10) NOT NULL DEFAULT 0,
  `getback_x1` int(10) NOT NULL DEFAULT 0,
  `getback_y1` int(10) NOT NULL DEFAULT 0,
  `getback_x2` int(10) NOT NULL DEFAULT 0,
  `getback_y2` int(10) NOT NULL DEFAULT 0,
  `getback_x3` int(10) NOT NULL DEFAULT 0,
  `getback_y3` int(10) NOT NULL DEFAULT 0,
  `getback_mapid` int(10) NOT NULL DEFAULT 0,
  `getback_townid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `getback_townid_elf` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `getback_townid_darkelf` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `scrollescape` int(10) NOT NULL DEFAULT 1,
  `note` varchar(50) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `getback_restart`
--

CREATE TABLE `getback_restart` (
  `area` int(10) NOT NULL DEFAULT 0,
  `note` varchar(50) DEFAULT NULL,
  `locx` int(10) NOT NULL DEFAULT 0,
  `locy` int(10) NOT NULL DEFAULT 0,
  `mapid` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `house`
--

CREATE TABLE `house` (
  `house_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `house_name` varchar(45) NOT NULL DEFAULT '',
  `house_area` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(45) NOT NULL DEFAULT '',
  `keeper_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `is_on_sale` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `is_purchase_basement` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `tax_deadline` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hunting_quest`
--

CREATE TABLE `hunting_quest` (
  `area_name` varchar(50) NOT NULL DEFAULT '',
  `map_number` int(6) NOT NULL DEFAULT 0,
  `location_desc` int(6) DEFAULT NULL,
  `quest_id` int(6) NOT NULL DEFAULT 0,
  `is_use` enum('true','false') NOT NULL DEFAULT 'true'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hunting_quest_teleport`
--

CREATE TABLE `hunting_quest_teleport` (
  `action_string` varchar(50) NOT NULL DEFAULT '',
  `tel_mapid` int(6) NOT NULL DEFAULT 0,
  `tel_x` int(4) DEFAULT NULL,
  `tel_y` int(4) DEFAULT NULL,
  `tel_itemid` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inter_race_region`
--

CREATE TABLE `inter_race_region` (
  `id` int(10) NOT NULL,
  `loc_x` int(10) DEFAULT NULL,
  `loc_y` int(10) DEFAULT NULL,
  `loc_mapid` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_bookmark`
--

CREATE TABLE `item_bookmark` (
  `id` int(10) UNSIGNED NOT NULL,
  `book_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `item_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(45) NOT NULL DEFAULT '',
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_box`
--

CREATE TABLE `item_box` (
  `boxId` int(10) NOT NULL DEFAULT 0,
  `desc` varchar(50) DEFAULT NULL,
  `classType` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown','all') NOT NULL DEFAULT 'all',
  `itemId` int(10) NOT NULL DEFAULT 0,
  `count` int(10) NOT NULL DEFAULT 1,
  `enchant` int(2) NOT NULL DEFAULT 0,
  `bless` int(3) NOT NULL DEFAULT 1,
  `attr` int(2) NOT NULL DEFAULT 0,
  `identi` enum('true','false') NOT NULL DEFAULT 'false',
  `limitTime` int(10) NOT NULL DEFAULT 0,
  `limitMaps` varchar(200) DEFAULT NULL,
  `questBox` enum('true','false') NOT NULL DEFAULT 'false',
  `effectId` int(6) NOT NULL DEFAULT 0,
  `chance` int(3) NOT NULL DEFAULT 100,
  `validateItems` text DEFAULT NULL,
  `boxDelete` enum('false','true') NOT NULL DEFAULT 'true'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_buff`
--

CREATE TABLE `item_buff` (
  `item_id` int(10) NOT NULL DEFAULT 0,
  `desc` varchar(100) DEFAULT NULL,
  `skill_ids` varchar(100) NOT NULL DEFAULT '',
  `delete` enum('false','true') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_click_message`
--

CREATE TABLE `item_click_message` (
  `itemId` int(10) NOT NULL,
  `type` enum('true','false') NOT NULL DEFAULT 'false',
  `msg` varchar(500) DEFAULT NULL,
  `delete` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_collection`
--

CREATE TABLE `item_collection` (
  `itemId` int(10) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `type` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_enchant_ablity`
--

CREATE TABLE `item_enchant_ablity` (
  `itemId` int(3) NOT NULL DEFAULT 0,
  `desc` varchar(45) DEFAULT NULL,
  `enchant` int(2) NOT NULL DEFAULT 0,
  `ac_bonus` int(2) NOT NULL DEFAULT 0,
  `ac_sub` int(3) NOT NULL DEFAULT 0,
  `str` int(2) NOT NULL DEFAULT 0,
  `con` int(2) NOT NULL DEFAULT 0,
  `dex` int(2) NOT NULL DEFAULT 0,
  `int` int(2) NOT NULL DEFAULT 0,
  `wis` int(2) NOT NULL DEFAULT 0,
  `cha` int(2) NOT NULL DEFAULT 0,
  `shortDamage` int(2) NOT NULL DEFAULT 0,
  `shortHit` int(2) NOT NULL DEFAULT 0,
  `shortCritical` int(2) NOT NULL DEFAULT 0,
  `longDamage` int(2) NOT NULL DEFAULT 0,
  `longHit` int(2) NOT NULL DEFAULT 0,
  `longCritical` int(2) NOT NULL DEFAULT 0,
  `spellpower` int(2) NOT NULL DEFAULT 0,
  `magicHit` int(2) NOT NULL DEFAULT 0,
  `magicCritical` int(2) NOT NULL DEFAULT 0,
  `magicDamage` int(2) NOT NULL DEFAULT 0,
  `maxHp` int(3) NOT NULL DEFAULT 0,
  `maxMp` int(3) NOT NULL DEFAULT 0,
  `hpRegen` int(2) NOT NULL DEFAULT 0,
  `mpRegen` int(2) NOT NULL DEFAULT 0,
  `baseHpRate` int(2) NOT NULL DEFAULT 0,
  `baseMpRate` int(2) NOT NULL DEFAULT 0,
  `attrFire` int(2) NOT NULL DEFAULT 0,
  `attrWater` int(2) NOT NULL DEFAULT 0,
  `attrWind` int(2) NOT NULL DEFAULT 0,
  `attrEarth` int(2) NOT NULL DEFAULT 0,
  `attrAll` int(2) NOT NULL DEFAULT 0,
  `mr` int(2) NOT NULL DEFAULT 0,
  `carryBonus` int(3) NOT NULL DEFAULT 0,
  `dg` int(2) NOT NULL DEFAULT 0,
  `er` int(2) NOT NULL DEFAULT 0,
  `me` int(2) NOT NULL DEFAULT 0,
  `reduction` int(2) NOT NULL DEFAULT 0,
  `reductionEgnor` int(2) NOT NULL DEFAULT 0,
  `reductionMagic` int(2) NOT NULL DEFAULT 0,
  `reductionPercent` int(2) NOT NULL DEFAULT 0,
  `PVPDamage` int(2) NOT NULL DEFAULT 0,
  `PVPReduction` int(2) NOT NULL DEFAULT 0,
  `PVPReductionPercent` int(2) NOT NULL DEFAULT 0,
  `PVPReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `PVPReductionMagic` int(2) NOT NULL DEFAULT 0,
  `PVPReductionMagicEgnor` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusDamageReduction` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusPVPDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPDamagePercent` int(2) NOT NULL DEFAULT 0,
  `registBlind` int(2) NOT NULL DEFAULT 0,
  `registFreeze` int(2) NOT NULL DEFAULT 0,
  `registSleep` int(2) NOT NULL DEFAULT 0,
  `registStone` int(2) NOT NULL DEFAULT 0,
  `toleranceSkill` int(2) NOT NULL DEFAULT 0,
  `toleranceSpirit` int(2) NOT NULL DEFAULT 0,
  `toleranceDragon` int(2) NOT NULL DEFAULT 0,
  `toleranceFear` int(2) NOT NULL DEFAULT 0,
  `toleranceAll` int(2) NOT NULL DEFAULT 0,
  `hitupSkill` int(2) NOT NULL DEFAULT 0,
  `hitupSpirit` int(2) NOT NULL DEFAULT 0,
  `hitupDragon` int(2) NOT NULL DEFAULT 0,
  `hitupFear` int(2) NOT NULL DEFAULT 0,
  `hitupAll` int(2) NOT NULL DEFAULT 0,
  `potionPlusDefens` int(2) NOT NULL DEFAULT 0,
  `potionPlusPercent` int(2) NOT NULL DEFAULT 0,
  `potionPlusValue` int(2) NOT NULL DEFAULT 0,
  `hprAbsol32Second` int(2) NOT NULL DEFAULT 0,
  `mprAbsol64Second` int(2) NOT NULL DEFAULT 0,
  `mprAbsol16Second` int(2) NOT NULL DEFAULT 0,
  `imunEgnor` int(2) NOT NULL DEFAULT 0,
  `expBonus` int(2) NOT NULL DEFAULT 0,
  `einBlessExp` int(2) NOT NULL DEFAULT 0,
  `rest_exp_reduce_efficiency` int(2) NOT NULL DEFAULT 0,
  `fowSlayerDamage` int(2) NOT NULL DEFAULT 0,
  `titanUp` int(2) NOT NULL DEFAULT 0,
  `stunDuration` int(2) NOT NULL DEFAULT 0,
  `tripleArrowStun` int(2) NOT NULL DEFAULT 0,
  `vanguardTime` int(2) NOT NULL DEFAULT 0,
  `strangeTimeIncrease` int(4) NOT NULL DEFAULT 0,
  `strangeTimeDecrease` int(4) NOT NULL DEFAULT 0,
  `hpPotionDelayDecrease` int(4) NOT NULL DEFAULT 0,
  `hpPotionCriticalProb` int(4) NOT NULL DEFAULT 0,
  `increaseArmorSkillProb` int(4) NOT NULL DEFAULT 0,
  `returnLockDuraion` int(2) NOT NULL DEFAULT 0,
  `attackSpeedDelayRate` int(3) NOT NULL DEFAULT 0,
  `moveSpeedDelayRate` int(3) NOT NULL DEFAULT 0,
  `magicName` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_key_boss`
--

CREATE TABLE `item_key_boss` (
  `item_obj_id` int(11) NOT NULL,
  `key_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_ment`
--

CREATE TABLE `item_ment` (
  `itemId` int(10) NOT NULL DEFAULT 0,
  `itemName` varchar(50) DEFAULT NULL,
  `mentType` enum('treasurebox','craft','drop','pickup') NOT NULL DEFAULT 'pickup'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_selector`
--

CREATE TABLE `item_selector` (
  `itemId` int(11) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `selectItemId` int(11) NOT NULL DEFAULT 0,
  `selectName` varchar(45) DEFAULT NULL,
  `count` int(10) NOT NULL DEFAULT 1,
  `enchant` int(4) NOT NULL DEFAULT 0,
  `attr` enum('5','4','3','2','1','0') NOT NULL DEFAULT '0',
  `bless` int(3) NOT NULL DEFAULT 1,
  `limitTime` int(10) NOT NULL DEFAULT 0,
  `delete` enum('false','true') NOT NULL DEFAULT 'true'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_selector_warehouse`
--

CREATE TABLE `item_selector_warehouse` (
  `itemId` int(11) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `selectItemId` int(11) NOT NULL DEFAULT 0,
  `selectName` varchar(45) DEFAULT NULL,
  `index` int(3) NOT NULL DEFAULT 0,
  `enchantLevel` int(2) NOT NULL DEFAULT 0,
  `attrLevel` int(2) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_terms`
--

CREATE TABLE `item_terms` (
  `itemId` int(9) NOT NULL DEFAULT 0,
  `name` varchar(50) DEFAULT NULL,
  `termMinut` int(9) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `letter`
--

CREATE TABLE `letter` (
  `item_object_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `code` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `sender` varchar(16) DEFAULT NULL,
  `receiver` varchar(16) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `template_id` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `subject` varchar(20) DEFAULT NULL,
  `content` varchar(2000) DEFAULT NULL,
  `isCheck` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `letter_command`
--

CREATE TABLE `letter_command` (
  `id` int(10) NOT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `letter_spam`
--

CREATE TABLE `letter_spam` (
  `no` int(10) NOT NULL DEFAULT 0,
  `name` varchar(16) DEFAULT NULL,
  `spamname` varchar(16) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `levelup_quests_item`
--

CREATE TABLE `levelup_quests_item` (
  `id` int(10) NOT NULL,
  `level` int(10) NOT NULL DEFAULT 0,
  `type` int(5) NOT NULL DEFAULT 0,
  `note` varchar(100) DEFAULT NULL,
  `item_name` varchar(50) NOT NULL DEFAULT '',
  `item_id` int(10) NOT NULL DEFAULT 0,
  `count` int(10) NOT NULL DEFAULT 0,
  `enchant` int(6) NOT NULL DEFAULT 0,
  `attrlevel` int(5) NOT NULL DEFAULT 0,
  `bless` int(5) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `log_adena_monster`
--

CREATE TABLE `log_adena_monster` (
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `accounts` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `count` int(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `log_adena_shop`
--

CREATE TABLE `log_adena_shop` (
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `accounts` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `count` int(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `log_chat`
--

CREATE TABLE `log_chat` (
  `id` int(10) UNSIGNED NOT NULL,
  `account_name` varchar(50) NOT NULL,
  `char_id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `clan_id` int(10) NOT NULL,
  `clan_name` varchar(50) DEFAULT NULL,
  `locx` int(10) NOT NULL,
  `locy` int(10) NOT NULL,
  `mapid` int(10) NOT NULL,
  `type` int(10) NOT NULL,
  `target_account_name` varchar(50) DEFAULT NULL,
  `target_id` int(10) DEFAULT 0,
  `target_name` varchar(50) DEFAULT NULL,
  `target_clan_id` int(10) DEFAULT NULL,
  `target_clan_name` varchar(50) DEFAULT NULL,
  `target_locx` int(10) DEFAULT NULL,
  `target_locy` int(10) DEFAULT NULL,
  `target_mapid` int(10) DEFAULT NULL,
  `content` varchar(256) NOT NULL,
  `datetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `log_cwarehouse`
--

CREATE TABLE `log_cwarehouse` (
  `id` int(10) NOT NULL,
  `datetime` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `type` varchar(45) NOT NULL,
  `clan_id` int(10) DEFAULT NULL,
  `clan_name` varchar(45) DEFAULT NULL,
  `account` varchar(45) DEFAULT NULL,
  `char_id` int(10) DEFAULT NULL,
  `char_name` varchar(45) DEFAULT NULL,
  `item_id` varchar(45) DEFAULT NULL,
  `item_name` varchar(45) DEFAULT NULL,
  `item_enchantlvl` varchar(45) DEFAULT NULL,
  `item_count` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `log_enchant`
--

CREATE TABLE `log_enchant` (
  `id` int(10) UNSIGNED NOT NULL,
  `char_id` int(10) NOT NULL DEFAULT 0,
  `item_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `old_enchantlvl` int(3) NOT NULL DEFAULT 0,
  `new_enchantlvl` int(3) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `log_private_shop`
--

CREATE TABLE `log_private_shop` (
  `id` int(10) NOT NULL,
  `time` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `type` varchar(45) NOT NULL,
  `shop_account` varchar(45) DEFAULT NULL,
  `shop_id` int(10) DEFAULT NULL,
  `shop_name` varchar(45) DEFAULT NULL,
  `user_account` varchar(45) DEFAULT NULL,
  `user_id` int(10) DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  `item_id` int(10) DEFAULT NULL,
  `item_name` varchar(45) DEFAULT NULL,
  `item_enchantlvl` int(10) DEFAULT NULL,
  `price` int(12) DEFAULT NULL,
  `item_count` int(10) DEFAULT NULL,
  `total_price` int(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `log_shop`
--

CREATE TABLE `log_shop` (
  `id` int(10) NOT NULL,
  `time` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `type` varchar(45) NOT NULL,
  `npc_id` varchar(45) DEFAULT NULL,
  `user_account` varchar(45) DEFAULT NULL,
  `user_id` int(10) DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  `item_id` int(10) DEFAULT NULL,
  `item_name` varchar(45) DEFAULT NULL,
  `item_enchantlvl` int(10) DEFAULT NULL,
  `price` int(12) DEFAULT NULL,
  `item_count` int(10) DEFAULT NULL,
  `total_price` int(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `log_warehouse`
--

CREATE TABLE `log_warehouse` (
  `id` int(10) NOT NULL,
  `datetime` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `type` varchar(45) NOT NULL,
  `account` varchar(45) DEFAULT NULL,
  `char_id` int(10) DEFAULT NULL,
  `char_name` varchar(45) DEFAULT NULL,
  `item_id` varchar(45) DEFAULT NULL,
  `item_name` varchar(45) DEFAULT NULL,
  `item_enchantlvl` varchar(45) DEFAULT NULL,
  `item_count` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `magicdoll_info`
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
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `magicdoll_potential`
--

CREATE TABLE `magicdoll_potential` (
  `bonusId` int(3) NOT NULL DEFAULT 0,
  `desc` varchar(45) DEFAULT NULL,
  `isUse` enum('false','true') DEFAULT 'true',
  `ac_bonus` int(2) NOT NULL DEFAULT 0,
  `str` int(2) NOT NULL DEFAULT 0,
  `con` int(2) NOT NULL DEFAULT 0,
  `dex` int(2) NOT NULL DEFAULT 0,
  `int` int(2) NOT NULL DEFAULT 0,
  `wis` int(2) NOT NULL DEFAULT 0,
  `cha` int(2) NOT NULL DEFAULT 0,
  `allStatus` int(2) NOT NULL DEFAULT 0,
  `shortDamage` int(2) NOT NULL DEFAULT 0,
  `shortHit` int(2) NOT NULL DEFAULT 0,
  `shortCritical` int(2) NOT NULL DEFAULT 0,
  `longDamage` int(2) NOT NULL DEFAULT 0,
  `longHit` int(2) NOT NULL DEFAULT 0,
  `longCritical` int(2) NOT NULL DEFAULT 0,
  `spellpower` int(2) NOT NULL DEFAULT 0,
  `magicHit` int(2) NOT NULL DEFAULT 0,
  `magicCritical` int(2) NOT NULL DEFAULT 0,
  `hp` int(3) NOT NULL DEFAULT 0,
  `mp` int(3) NOT NULL DEFAULT 0,
  `hpr` int(2) NOT NULL DEFAULT 0,
  `mpr` int(2) NOT NULL DEFAULT 0,
  `hpStill` int(2) NOT NULL DEFAULT 0,
  `mpStill` int(2) NOT NULL DEFAULT 0,
  `stillChance` int(3) NOT NULL DEFAULT 0,
  `hprAbsol` int(2) NOT NULL DEFAULT 0,
  `mprAbsol` int(2) NOT NULL DEFAULT 0,
  `attrFire` int(2) NOT NULL DEFAULT 0,
  `attrWater` int(2) NOT NULL DEFAULT 0,
  `attrWind` int(2) NOT NULL DEFAULT 0,
  `attrEarth` int(2) NOT NULL DEFAULT 0,
  `attrAll` int(2) NOT NULL DEFAULT 0,
  `mr` int(2) NOT NULL DEFAULT 0,
  `expBonus` int(3) NOT NULL DEFAULT 0,
  `carryBonus` int(3) NOT NULL DEFAULT 0,
  `dg` int(2) NOT NULL DEFAULT 0,
  `er` int(2) NOT NULL DEFAULT 0,
  `me` int(2) NOT NULL DEFAULT 0,
  `reduction` int(2) NOT NULL DEFAULT 0,
  `reductionEgnor` int(2) NOT NULL DEFAULT 0,
  `reductionMagic` int(2) NOT NULL DEFAULT 0,
  `reductionPercent` int(2) NOT NULL DEFAULT 0,
  `PVPDamage` int(2) NOT NULL DEFAULT 0,
  `PVPReduction` int(2) NOT NULL DEFAULT 0,
  `PVPReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `PVPReductionMagic` int(2) NOT NULL DEFAULT 0,
  `PVPReductionMagicEgnor` int(2) NOT NULL DEFAULT 0,
  `toleranceSkill` int(2) NOT NULL DEFAULT 0,
  `toleranceSpirit` int(2) NOT NULL DEFAULT 0,
  `toleranceDragon` int(2) NOT NULL DEFAULT 0,
  `toleranceFear` int(2) NOT NULL DEFAULT 0,
  `toleranceAll` int(2) NOT NULL DEFAULT 0,
  `hitupSkill` int(2) NOT NULL DEFAULT 0,
  `hitupSpirit` int(2) NOT NULL DEFAULT 0,
  `hitupDragon` int(2) NOT NULL DEFAULT 0,
  `hitupFear` int(2) NOT NULL DEFAULT 0,
  `hitupAll` int(2) NOT NULL DEFAULT 0,
  `imunEgnor` int(2) NOT NULL DEFAULT 0,
  `strangeTimeIncrease` int(4) NOT NULL DEFAULT 0,
  `firstSpeed` enum('true','false') NOT NULL DEFAULT 'false',
  `secondSpeed` enum('true','false') NOT NULL DEFAULT 'false',
  `thirdSpeed` enum('true','false') NOT NULL DEFAULT 'false',
  `forthSpeed` enum('true','false') NOT NULL DEFAULT 'false',
  `skilId` int(9) NOT NULL DEFAULT -1,
  `skillChance` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `manager_user_teleport`
--

CREATE TABLE `manager_user_teleport` (
  `name` varchar(45) NOT NULL DEFAULT '',
  `locX` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locY` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locMap` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mapids`
--

CREATE TABLE `mapids` (
  `mapid` int(10) NOT NULL DEFAULT 0,
  `locationname` varchar(45) DEFAULT NULL,
  `desc_kr` varchar(45) NOT NULL,
  `startX` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `endX` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `startY` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `endY` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `monster_amount` float UNSIGNED NOT NULL DEFAULT 0,
  `drop_rate` float UNSIGNED NOT NULL DEFAULT 0,
  `underwater` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `markable` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `teleportable` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `escapable` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `resurrection` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `painwand` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `penalty` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `take_pets` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `recall_pets` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `usable_item` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `usable_skill` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `dungeon` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `dmgModiPc2Npc` int(3) NOT NULL DEFAULT 0,
  `dmgModiNpc2Pc` int(3) NOT NULL DEFAULT 0,
  `decreaseHp` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `dominationTeleport` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `beginZone` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `redKnightZone` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `ruunCastleZone` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `interWarZone` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `geradBuffZone` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `growBuffZone` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `interKind` int(3) NOT NULL DEFAULT 0,
  `script` varchar(50) DEFAULT NULL,
  `cloneStart` int(6) NOT NULL DEFAULT 0,
  `cloneEnd` int(6) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `map_balance`
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
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `map_fix_key`
--

CREATE TABLE `map_fix_key` (
  `locX` smallint(6) UNSIGNED NOT NULL,
  `locY` smallint(6) UNSIGNED NOT NULL,
  `mapId` smallint(6) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `map_type`
--

CREATE TABLE `map_type` (
  `mapId` int(6) NOT NULL DEFAULT 0,
  `desc` varchar(50) DEFAULT NULL,
  `type` enum('COMBAT','SAFETY','NORMAL') NOT NULL DEFAULT 'NORMAL'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `marble`
--

CREATE TABLE `marble` (
  `marble_id` int(10) NOT NULL,
  `char_id` int(10) DEFAULT NULL,
  `char_name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mobgroup`
--

CREATE TABLE `mobgroup` (
  `id` int(10) UNSIGNED NOT NULL,
  `note` varchar(255) NOT NULL DEFAULT '',
  `remove_group_if_leader_die` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `leader_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion1_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion1_count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion2_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion2_count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion3_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion3_count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion4_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion4_count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion5_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion5_count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion6_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion6_count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion7_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion7_count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion8_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `minion8_count` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mobskill`
--

CREATE TABLE `mobskill` (
  `mobid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `actNo` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `mobname` varchar(45) NOT NULL DEFAULT '',
  `type` enum('NONE','ATTACK','SPELL','SUMMON','POLY','LINE_ATTACK','KIRTAS_METEOR','KIRTAS_BARRIER','TITANGOLEM_BARRIER','VALLACAS_FLY','VALLACAS_BRESS') NOT NULL DEFAULT 'NONE',
  `prob` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `enableHp` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `enableCompanionHp` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `range` int(3) NOT NULL DEFAULT 0,
  `limitCount` int(3) NOT NULL DEFAULT 0,
  `ChangeTarget` enum('NO','COMPANION','ME','RANDOM') NOT NULL DEFAULT 'NO',
  `AreaWidth` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `AreaHeight` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `Leverage` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `SkillId` int(10) NOT NULL DEFAULT -1,
  `Gfxid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `ActId` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `SummonId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `SummonMin` int(3) NOT NULL DEFAULT 0,
  `SummonMax` int(3) NOT NULL DEFAULT 0,
  `PolyId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Msg` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `monster_book`
--

CREATE TABLE `monster_book` (
  `monsternumber` int(10) UNSIGNED NOT NULL,
  `monstername` varchar(255) CHARACTER SET euckr NOT NULL,
  `monster_id` int(10) DEFAULT NULL,
  `locx` int(10) UNSIGNED DEFAULT 0,
  `locy` int(10) UNSIGNED DEFAULT 0,
  `mapid` int(10) UNSIGNED DEFAULT 0,
  `type` int(10) DEFAULT NULL,
  `marterial` int(10) DEFAULT NULL,
  `book_step_first` int(10) DEFAULT NULL,
  `book_step_second` int(10) DEFAULT NULL,
  `book_step_third` int(10) DEFAULT NULL,
  `note` varchar(255) CHARACTER SET euckr DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notice`
--

CREATE TABLE `notice` (
  `id` int(30) NOT NULL,
  `message` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notification`
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
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notification_event_npc`
--

CREATE TABLE `notification_event_npc` (
  `notification_id` int(6) NOT NULL DEFAULT 0,
  `is_use` enum('true','false') NOT NULL DEFAULT 'true',
  `order_id` int(2) NOT NULL DEFAULT 0,
  `npc_id` int(10) NOT NULL DEFAULT 0,
  `displaydesc` varchar(50) NOT NULL,
  `displaydesc_kr` varchar(50) DEFAULT NULL,
  `rest_gauge_bonus` int(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `npc`
--

CREATE TABLE `npc` (
  `npcid` int(10) UNSIGNED NOT NULL,
  `classId` int(6) NOT NULL DEFAULT 0,
  `desc_kr` varchar(45) NOT NULL DEFAULT '',
  `desc_id` varchar(45) NOT NULL DEFAULT '',
  `note` varchar(45) NOT NULL DEFAULT '',
  `impl` varchar(45) NOT NULL DEFAULT '',
  `spriteId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `lvl` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `hp` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `mp` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `ac` int(3) NOT NULL DEFAULT 0,
  `str` int(3) NOT NULL DEFAULT 0,
  `con` int(3) NOT NULL DEFAULT 0,
  `dex` int(3) NOT NULL DEFAULT 0,
  `wis` int(3) NOT NULL DEFAULT 0,
  `intel` int(3) NOT NULL DEFAULT 0,
  `mr` int(3) NOT NULL DEFAULT 0,
  `exp` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `alignment` int(10) NOT NULL DEFAULT 0,
  `big` enum('true','false') NOT NULL DEFAULT 'false',
  `weakAttr` enum('NONE','EARTH','FIRE','WATER','WIND') NOT NULL DEFAULT 'NONE',
  `ranged` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `is_taming` enum('true','false') NOT NULL DEFAULT 'false',
  `passispeed` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `atkspeed` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `atk_magic_speed` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `sub_magic_speed` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `undead` enum('NONE','UNDEAD','DEMON','UNDEAD_BOSS','DRANIUM') NOT NULL DEFAULT 'NONE',
  `poison_atk` enum('NONE','DAMAGE','PARALYSIS','SILENCE') NOT NULL DEFAULT 'NONE',
  `is_agro` enum('false','true') NOT NULL DEFAULT 'false',
  `is_agro_poly` enum('false','true') NOT NULL DEFAULT 'false',
  `is_agro_invis` enum('false','true') NOT NULL DEFAULT 'false',
  `family` varchar(20) NOT NULL DEFAULT '',
  `agrofamily` int(1) UNSIGNED NOT NULL DEFAULT 0,
  `agrogfxid1` int(10) NOT NULL DEFAULT -1,
  `agrogfxid2` int(10) NOT NULL DEFAULT -1,
  `is_picupitem` enum('false','true') NOT NULL DEFAULT 'false',
  `digestitem` int(1) UNSIGNED NOT NULL DEFAULT 0,
  `is_bravespeed` enum('false','true') NOT NULL DEFAULT 'false',
  `hprinterval` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `hpr` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `mprinterval` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `mpr` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `is_teleport` enum('true','false') NOT NULL DEFAULT 'false',
  `randomlevel` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `randomhp` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `randommp` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `randomac` int(3) NOT NULL DEFAULT 0,
  `randomexp` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `randomAlign` int(5) NOT NULL DEFAULT 0,
  `damage_reduction` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `is_hard` enum('true','false') NOT NULL DEFAULT 'false',
  `is_bossmonster` enum('true','false') NOT NULL DEFAULT 'false',
  `can_turnundead` enum('true','false') NOT NULL DEFAULT 'false',
  `bowSpritetId` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `karma` int(10) NOT NULL DEFAULT 0,
  `transform_id` int(10) NOT NULL DEFAULT -1,
  `transform_gfxid` int(10) NOT NULL DEFAULT 0,
  `light_size` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `is_amount_fixed` enum('false','true') NOT NULL DEFAULT 'false',
  `is_change_head` enum('false','true') NOT NULL DEFAULT 'false',
  `spawnlist_door` int(10) NOT NULL DEFAULT 0,
  `count_map` int(10) NOT NULL DEFAULT 0,
  `cant_resurrect` enum('false','true') NOT NULL DEFAULT 'false',
  `isHide` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `npcaction`
--

CREATE TABLE `npcaction` (
  `npcid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `normal_action` varchar(45) NOT NULL DEFAULT '',
  `caotic_action` varchar(45) NOT NULL DEFAULT '',
  `teleport_url` varchar(45) NOT NULL DEFAULT '',
  `teleport_urla` varchar(45) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `npcaction_teleport`
--

CREATE TABLE `npcaction_teleport` (
  `npcId` int(10) NOT NULL DEFAULT 0,
  `desc` varchar(50) DEFAULT NULL,
  `actionName` varchar(50) NOT NULL,
  `needLevel` int(3) NOT NULL DEFAULT 0,
  `limitLevel` int(3) NOT NULL DEFAULT 0,
  `needTimerId` int(3) NOT NULL DEFAULT 0,
  `needItem` text DEFAULT NULL,
  `needBuff` text DEFAULT NULL,
  `needPcroomBuff` enum('true','false') NOT NULL DEFAULT 'false',
  `telX` int(5) NOT NULL DEFAULT 0,
  `telY` int(5) NOT NULL DEFAULT 0,
  `telMapId` int(5) NOT NULL DEFAULT 0,
  `telRange` int(3) NOT NULL DEFAULT 0,
  `telType` enum('random','inter','normal') NOT NULL DEFAULT 'normal',
  `randomMap` text DEFAULT NULL,
  `telTownId` int(11) NOT NULL DEFAULT 0,
  `failAlignment` enum('caotic','neutral','lawful','none') NOT NULL DEFAULT 'none',
  `successActionName` varchar(50) DEFAULT NULL,
  `failLevelActionName` varchar(50) DEFAULT NULL,
  `failItemActionName` varchar(50) DEFAULT NULL,
  `failBuffActionName` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `npcchat`
--

CREATE TABLE `npcchat` (
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `chat_timing` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `note` varchar(45) NOT NULL DEFAULT '',
  `start_delay_time` int(10) NOT NULL DEFAULT 0,
  `chat_id1` varchar(45) NOT NULL DEFAULT '',
  `chat_id2` varchar(45) NOT NULL DEFAULT '',
  `chat_id3` varchar(45) NOT NULL DEFAULT '',
  `chat_id4` varchar(45) NOT NULL DEFAULT '',
  `chat_id5` varchar(45) NOT NULL DEFAULT '',
  `chat_interval` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `is_shout` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `is_world_chat` tinyint(1) NOT NULL DEFAULT 0,
  `is_repeat` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `repeat_interval` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `game_time` int(10) NOT NULL DEFAULT 0,
  `percent` int(10) UNSIGNED DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `npc_info`
--

CREATE TABLE `npc_info` (
  `npcId` int(10) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `recall` enum('false','true') NOT NULL DEFAULT 'false',
  `spawnActionId` int(2) NOT NULL DEFAULT 0,
  `reward` enum('false','true') NOT NULL DEFAULT 'false',
  `rewardRange` enum('screen','map','self') NOT NULL DEFAULT 'screen',
  `rewardItemId` int(10) NOT NULL DEFAULT 0,
  `rewardItemCount` int(10) NOT NULL DEFAULT 0,
  `rewardEinhasad` int(4) NOT NULL DEFAULT 0,
  `rewardNcoin` int(10) NOT NULL DEFAULT 0,
  `rewardGfx` int(5) NOT NULL DEFAULT 0,
  `msgRange` enum('screen','map','self') NOT NULL DEFAULT 'screen',
  `spawnMsg` text DEFAULT NULL,
  `dieMsg` text DEFAULT NULL,
  `dieMsgPcList` enum('false','true') NOT NULL DEFAULT 'false',
  `autoLoot` enum('false','true') NOT NULL DEFAULT 'false',
  `transformChance` int(3) NOT NULL DEFAULT 0,
  `transformId` int(9) NOT NULL DEFAULT 0,
  `transformGfxId` int(6) NOT NULL DEFAULT 0,
  `scriptType` enum('text','number','none') NOT NULL DEFAULT 'none',
  `scriptContent` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `npc_night`
--

CREATE TABLE `npc_night` (
  `npcId` int(9) NOT NULL DEFAULT 0,
  `desc` varchar(50) DEFAULT NULL,
  `targetMapId` int(5) NOT NULL DEFAULT 0,
  `targetId` int(9) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `penalty_pass_item`
--

CREATE TABLE `penalty_pass_item` (
  `itemId` int(10) NOT NULL DEFAULT 0,
  `desc` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `penalty_protect_item`
--

CREATE TABLE `penalty_protect_item` (
  `itemId` int(10) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `type` enum('have','equip') DEFAULT 'have',
  `itemPanalty` enum('false','true') DEFAULT 'false',
  `expPanalty` enum('false','true') DEFAULT 'false',
  `dropItemId` int(10) DEFAULT 0,
  `msgId` int(5) DEFAULT NULL,
  `mapIds` text DEFAULT NULL,
  `remove` enum('false','true') DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `playsupport`
--

CREATE TABLE `playsupport` (
  `mapid` int(6) NOT NULL,
  `mapname` varchar(50) DEFAULT NULL,
  `whole` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `surround` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `sub` tinyint(1) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `polyitems`
--

CREATE TABLE `polyitems` (
  `itemId` int(10) NOT NULL DEFAULT 0,
  `desc` varchar(50) DEFAULT NULL,
  `polyId` int(6) NOT NULL DEFAULT 0,
  `duration` int(6) NOT NULL DEFAULT 1800,
  `type` enum('domination','normal') NOT NULL DEFAULT 'normal',
  `delete` enum('false','true') NOT NULL DEFAULT 'true'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `polymorphs`
--

CREATE TABLE `polymorphs` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `polyid` int(11) NOT NULL,
  `minlevel` int(11) NOT NULL,
  `weaponequip` int(11) DEFAULT NULL,
  `armorequip` int(11) DEFAULT NULL,
  `isSkillUse` int(11) NOT NULL,
  `cause` int(11) DEFAULT NULL,
  `bonusPVP` enum('true','false') NOT NULL DEFAULT 'false',
  `formLongEnable` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=MyISAM DEFAULT CHARSET=euckr COMMENT='MyISAM free: 10240 kB';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `polyweapon`
--

CREATE TABLE `polyweapon` (
  `polyId` int(5) NOT NULL DEFAULT 0,
  `weapon` enum('bow','spear','both') NOT NULL DEFAULT 'both'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proto_packet`
--

CREATE TABLE `proto_packet` (
  `code` varchar(6) NOT NULL,
  `code_val` int(6) NOT NULL DEFAULT 0,
  `className` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `race_div_record`
--

CREATE TABLE `race_div_record` (
  `id` int(30) NOT NULL DEFAULT 0,
  `bug_number` int(10) NOT NULL DEFAULT 0,
  `dividend` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `race_record`
--

CREATE TABLE `race_record` (
  `number` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `win` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `lose` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `race_tickets`
--

CREATE TABLE `race_tickets` (
  `item_id` int(10) NOT NULL DEFAULT 0,
  `name` varchar(45) CHARACTER SET euckr NOT NULL,
  `price` int(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `repair_item_cost`
--

CREATE TABLE `repair_item_cost` (
  `itemId` int(11) NOT NULL DEFAULT 0,
  `name` varchar(45) DEFAULT NULL,
  `cost` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `report`
--

CREATE TABLE `report` (
  `target` varchar(100) NOT NULL,
  `reporter` varchar(100) NOT NULL,
  `count` int(11) NOT NULL DEFAULT 1,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resolvent`
--

CREATE TABLE `resolvent` (
  `item_id` int(10) NOT NULL DEFAULT 0,
  `note` varchar(45) NOT NULL,
  `crystal_count` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `robot_fishing`
--

CREATE TABLE `robot_fishing` (
  `x` int(8) DEFAULT NULL,
  `y` int(8) DEFAULT NULL,
  `mapid` int(5) DEFAULT NULL,
  `heading` int(5) DEFAULT NULL,
  `fishingX` int(8) DEFAULT NULL,
  `fishingY` int(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `robot_location`
--

CREATE TABLE `robot_location` (
  `uid` int(10) UNSIGNED NOT NULL,
  `istown` enum('true','false') NOT NULL DEFAULT 'false',
  `x` int(10) NOT NULL,
  `y` int(10) NOT NULL,
  `map` int(10) NOT NULL,
  `etc` text NOT NULL,
  `count` int(10) NOT NULL DEFAULT 1
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `robot_location최신`
--

CREATE TABLE `robot_location최신` (
  `uid` int(10) UNSIGNED NOT NULL,
  `istown` enum('true','false') NOT NULL DEFAULT 'false',
  `x` int(10) NOT NULL,
  `y` int(10) NOT NULL,
  `map` int(10) NOT NULL,
  `etc` text NOT NULL,
  `count` int(10) NOT NULL DEFAULT 1
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `robot_message`
--

CREATE TABLE `robot_message` (
  `uid` int(10) UNSIGNED NOT NULL,
  `type` enum('pvp','die') NOT NULL,
  `ment` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `robot_name`
--

CREATE TABLE `robot_name` (
  `uid` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `robot_teleport_list`
--

CREATE TABLE `robot_teleport_list` (
  `id` int(10) NOT NULL,
  `x` int(10) DEFAULT NULL,
  `y` int(10) DEFAULT NULL,
  `mapid` int(10) DEFAULT NULL,
  `heading` int(1) DEFAULT NULL,
  `note` varchar(50) DEFAULT NULL,
  `isuse` int(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `serverinfo`
--

CREATE TABLE `serverinfo` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `adenmake` bigint(30) DEFAULT 0,
  `adenconsume` bigint(30) DEFAULT 0,
  `adentax` int(10) DEFAULT 0,
  `bugdividend` float(10,0) DEFAULT 0,
  `accountcount` int(10) DEFAULT 0,
  `charcount` int(10) DEFAULT 0,
  `pvpcount` int(10) DEFAULT 0,
  `penaltycount` int(10) DEFAULT 0,
  `clanmaker` int(10) DEFAULT 0,
  `maxuser` int(10) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `server_explain`
--

CREATE TABLE `server_explain` (
  `num` int(10) NOT NULL,
  `subject` varchar(45) DEFAULT '',
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `shop`
--

CREATE TABLE `shop` (
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `item_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `order_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `selling_price` int(10) NOT NULL DEFAULT -1,
  `pack_count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `purchasing_price` int(10) NOT NULL DEFAULT -1,
  `enchant` int(10) NOT NULL DEFAULT 0,
  `pledge_rank` enum('NONE(없음)','RANK_NORMAL_KING(군주)','RANK_NORMAL_PRINCE(부군주)','RANK_NORMAL_KNIGHT(수호)','RANK_NORMAL_ELITE_KNIGHT(정예)','RANK_NORMAL_JUNIOR_KNIGHT(일반)') NOT NULL DEFAULT 'NONE(없음)',
  `note` varbinary(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `shop_aden`
--

CREATE TABLE `shop_aden` (
  `id` int(10) NOT NULL,
  `itemid` int(10) DEFAULT NULL,
  `itemname` varchar(22) DEFAULT NULL,
  `price` int(10) DEFAULT NULL,
  `type` int(10) DEFAULT 0,
  `status` int(10) DEFAULT 0,
  `html` varchar(22) DEFAULT '',
  `pack` int(10) DEFAULT 0,
  `enchant` int(10) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `shop_info`
--

CREATE TABLE `shop_info` (
  `npcId` int(9) NOT NULL DEFAULT 0,
  `name` varchar(50) DEFAULT NULL,
  `type` enum('clan','ein','ncoin','tam','berry','item') NOT NULL DEFAULT 'item',
  `currencyId` int(9) NOT NULL DEFAULT 0,
  `currencyDescId` int(6) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `shop_limit`
--

CREATE TABLE `shop_limit` (
  `shopId` int(9) NOT NULL DEFAULT 0,
  `itemId` int(9) NOT NULL DEFAULT 0,
  `itemName` varchar(50) DEFAULT NULL,
  `limitTerm` enum('WEEK','DAY','NONE') NOT NULL DEFAULT 'DAY',
  `limitCount` int(9) NOT NULL DEFAULT 0,
  `limitType` enum('ACCOUNT','CHARACTER') NOT NULL DEFAULT 'ACCOUNT'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `shop_npc`
--

CREATE TABLE `shop_npc` (
  `npc_id` int(10) NOT NULL,
  `id` int(10) NOT NULL DEFAULT 1,
  `item_id` int(10) NOT NULL DEFAULT 0,
  `memo` text DEFAULT NULL,
  `count` int(10) NOT NULL DEFAULT 1,
  `enchant` int(10) NOT NULL DEFAULT 0,
  `selling_price` int(10) NOT NULL DEFAULT -1,
  `purchasing_price` int(10) NOT NULL DEFAULT -1
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `skills`
--

CREATE TABLE `skills` (
  `skill_id` int(10) NOT NULL DEFAULT -1,
  `name` varchar(45) NOT NULL DEFAULT '',
  `desc_kr` varchar(45) NOT NULL,
  `skill_level` int(10) NOT NULL DEFAULT 0,
  `mpConsume` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `hpConsume` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `itemConsumeId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `itemConsumeCount` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `reuseDelay` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `delayGroupId` int(2) NOT NULL DEFAULT 0,
  `fixDelay` enum('true','false') NOT NULL DEFAULT 'false',
  `buffDuration` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `target` enum('NONE','ATTACK','BUFF') NOT NULL DEFAULT 'NONE',
  `target_to` enum('ME','PC','NPC','ALL','PLEDGE','PARTY','COMPANIION','PLACE') NOT NULL DEFAULT 'ALL',
  `damage_value` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `damage_dice` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `damage_dice_count` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `probability_value` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `probability_dice` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `attr` enum('NONE','EARTH','FIRE','WATER','WIND','RAY') NOT NULL DEFAULT 'NONE',
  `type` enum('NONE','PROB','CHANGE','CURSE','DEATH','HEAL','RESTORE','ATTACK','OTHER') NOT NULL DEFAULT 'NONE',
  `alignment` int(10) NOT NULL DEFAULT 0,
  `ranged` int(3) NOT NULL DEFAULT 0,
  `area` int(3) NOT NULL DEFAULT 0,
  `is_through` enum('true','false') NOT NULL DEFAULT 'false',
  `action_id` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `action_id2` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `action_id3` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `castgfx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `castgfx2` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `castgfx3` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `sysmsgID_happen` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `sysmsgID_stop` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `sysmsgID_fail` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `classType` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown','normal','none') NOT NULL DEFAULT 'none',
  `grade` enum('ONLY','MYTH','LEGEND','RARE','NORMAL') NOT NULL DEFAULT 'NORMAL'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `skills_handler`
--

CREATE TABLE `skills_handler` (
  `skillId` int(9) NOT NULL DEFAULT -1,
  `name` varchar(100) DEFAULT NULL,
  `className` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `skills_info`
--

CREATE TABLE `skills_info` (
  `skillId` int(10) NOT NULL DEFAULT -1,
  `skillname` varchar(20) NOT NULL,
  `desc_kr` varchar(100) NOT NULL,
  `useSkillId` int(10) NOT NULL DEFAULT 0,
  `durationShowType` enum('NONE(0)','PERCENT(1)','MINUTE(2)','PERCENT_ORC_SERVER(3)','EINHASAD_COOLTIME_MINUTE(4)','LEGACY_TIME(5)','VARIABLE_VALUE(6)','DAY_HOUR_MIN(7)','AUTO_DAY_HOUR_MIN_SEC(8)','NSERVICE_TOPPING(9)','UNLIMIT(10)','CUSTOM(11)','COUNT(12)','RATE(13)','EINHASAD_FAVOR(14)','HIDDEN(15)') NOT NULL DEFAULT 'AUTO_DAY_HOUR_MIN_SEC(8)',
  `icon` int(5) NOT NULL DEFAULT 0,
  `onIconId` int(5) NOT NULL DEFAULT 0,
  `offIconId` int(5) NOT NULL DEFAULT 0,
  `simplePck` enum('false','true') NOT NULL DEFAULT 'false',
  `iconPriority` int(3) NOT NULL DEFAULT 3,
  `tooltipStrId` int(5) NOT NULL DEFAULT 0,
  `newStrId` int(5) NOT NULL DEFAULT 0,
  `endStrId` int(5) NOT NULL DEFAULT 0,
  `isGood` enum('true','false') NOT NULL DEFAULT 'true',
  `overlapBuffIcon` int(3) NOT NULL DEFAULT 0,
  `mainTooltipStrId` int(3) NOT NULL DEFAULT 0,
  `buffIconPriority` int(3) NOT NULL DEFAULT 0,
  `buffGroupId` int(3) NOT NULL DEFAULT 0,
  `buffGroupPriority` int(3) NOT NULL DEFAULT 0,
  `expireDuration` int(6) NOT NULL DEFAULT 0,
  `boostType` enum('BOOST_NONE(0)','HP_UI_CHANGE(1)') NOT NULL DEFAULT 'BOOST_NONE(0)',
  `isPassiveSpell` enum('true','false') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `skills_passive`
--

CREATE TABLE `skills_passive` (
  `passive_id` int(3) NOT NULL DEFAULT -1,
  `name` varchar(100) DEFAULT NULL,
  `desc_kr` varchar(100) NOT NULL,
  `duration` int(6) NOT NULL DEFAULT 0,
  `on_icon_id` int(6) NOT NULL DEFAULT 0,
  `tooltip_str_id` int(6) NOT NULL DEFAULT 0,
  `is_good` enum('false','true') NOT NULL DEFAULT 'true',
  `class_type` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown','normal','none') NOT NULL DEFAULT 'none',
  `back_active_skill_id` int(10) NOT NULL DEFAULT -1,
  `back_passive_id` int(3) NOT NULL DEFAULT -1,
  `grade` enum('ONLY','MYTH','LEGEND','RARE','NORMAL') NOT NULL DEFAULT 'NORMAL'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist`
--

CREATE TABLE `spawnlist` (
  `id` int(10) UNSIGNED NOT NULL,
  `location` varchar(45) NOT NULL DEFAULT '',
  `count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `npc_templateid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `group_id` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `randomx` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `randomy` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `locx1` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locy1` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locx2` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locy2` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `min_respawn_delay` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `max_respawn_delay` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `respawn_screen` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `movement_distance` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `rest` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `near_spawn` tinyint(1) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_arrow`
--

CREATE TABLE `spawnlist_arrow` (
  `npc_id` int(10) UNSIGNED NOT NULL,
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) NOT NULL DEFAULT 0,
  `tarx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `tary` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `start_delay` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_boss`
--

CREATE TABLE `spawnlist_boss` (
  `id` int(10) UNSIGNED NOT NULL,
  `note` varchar(45) CHARACTER SET euckr DEFAULT '',
  `npcid` int(10) NOT NULL DEFAULT 0,
  `spawnDay` varchar(100) CHARACTER SET euckr DEFAULT NULL,
  `spawnTime` text CHARACTER SET euckr DEFAULT NULL,
  `spawnX` int(5) NOT NULL DEFAULT 0,
  `spawnY` int(5) NOT NULL DEFAULT 0,
  `spawnMapId` int(5) NOT NULL DEFAULT 0,
  `rndMinut` int(6) NOT NULL DEFAULT 0,
  `rndRange` int(10) NOT NULL DEFAULT 0,
  `heading` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `groupid` int(10) NOT NULL DEFAULT 0,
  `movementDistance` int(3) NOT NULL DEFAULT 0,
  `isYN` enum('true','false') CHARACTER SET euckr NOT NULL DEFAULT 'false',
  `mentType` enum('NONE','WORLD','MAP','SCREEN') NOT NULL,
  `ment` varchar(100) CHARACTER SET euckr DEFAULT '',
  `percent` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `aliveSecond` int(10) NOT NULL DEFAULT 0,
  `spawnType` enum('NORMAL','DOMINATION_TOWER','DRAGON_RAID','POISON_FEILD') CHARACTER SET euckr NOT NULL DEFAULT 'NORMAL'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_boss_sign`
--

CREATE TABLE `spawnlist_boss_sign` (
  `id` int(10) NOT NULL,
  `desc` varchar(100) DEFAULT NULL,
  `bossId` int(10) NOT NULL DEFAULT 0,
  `npcId` int(10) NOT NULL DEFAULT 0,
  `locX` int(6) NOT NULL DEFAULT 0,
  `locY` int(6) NOT NULL DEFAULT 0,
  `locMapId` int(6) NOT NULL DEFAULT 0,
  `rndRange` int(3) NOT NULL DEFAULT 0,
  `aliveSecond` int(6) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_castledoor`
--

CREATE TABLE `spawnlist_castledoor` (
  `id` int(11) NOT NULL DEFAULT 0,
  `location` varchar(25) NOT NULL DEFAULT '',
  `gfxid` int(11) NOT NULL DEFAULT 0,
  `locx` int(11) NOT NULL DEFAULT 0,
  `locy` int(11) NOT NULL DEFAULT 0,
  `mapid` int(11) NOT NULL DEFAULT 0,
  `direction` int(11) NOT NULL DEFAULT 0,
  `entrancex` int(11) NOT NULL DEFAULT 0,
  `entrancey` int(11) NOT NULL DEFAULT 0,
  `doorsize` int(11) NOT NULL DEFAULT 0,
  `keeper` int(11) NOT NULL DEFAULT 0,
  `att` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_clandungeon`
--

CREATE TABLE `spawnlist_clandungeon` (
  `id` int(2) UNSIGNED NOT NULL,
  `type` int(1) UNSIGNED NOT NULL DEFAULT 0,
  `stage` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(19) NOT NULL DEFAULT '',
  `npc_templateid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `count` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `boss` enum('true','false') DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_door`
--

CREATE TABLE `spawnlist_door` (
  `id` int(11) NOT NULL DEFAULT 0,
  `location` varchar(25) NOT NULL DEFAULT '',
  `gfxid` int(11) NOT NULL DEFAULT 0,
  `locx` int(11) NOT NULL DEFAULT 0,
  `locy` int(11) NOT NULL DEFAULT 0,
  `mapid` int(11) NOT NULL DEFAULT 0,
  `direction` int(11) NOT NULL DEFAULT 0,
  `left_edge_location` int(11) NOT NULL DEFAULT 0,
  `right_edge_location` int(11) NOT NULL DEFAULT 0,
  `hp` int(11) NOT NULL DEFAULT 0,
  `keeper` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_fantasyisland`
--

CREATE TABLE `spawnlist_fantasyisland` (
  `id` int(10) UNSIGNED NOT NULL,
  `type` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(19) NOT NULL DEFAULT '',
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(2) NOT NULL DEFAULT 0,
  `count` int(2) NOT NULL DEFAULT 1
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_furniture`
--

CREATE TABLE `spawnlist_furniture` (
  `item_obj_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `npcid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(10) NOT NULL DEFAULT 0,
  `locy` int(10) NOT NULL DEFAULT 0,
  `mapid` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_hadin`
--

CREATE TABLE `spawnlist_hadin` (
  `id` int(10) UNSIGNED NOT NULL,
  `type` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(19) NOT NULL DEFAULT '',
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(2) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_icedungeon`
--

CREATE TABLE `spawnlist_icedungeon` (
  `id` int(10) UNSIGNED NOT NULL,
  `location` varchar(19) NOT NULL DEFAULT '',
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(2) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_indun`
--

CREATE TABLE `spawnlist_indun` (
  `id` int(3) UNSIGNED NOT NULL,
  `type` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(19) NOT NULL DEFAULT '',
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(2) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_lastabard`
--

CREATE TABLE `spawnlist_lastabard` (
  `id` int(10) UNSIGNED NOT NULL,
  `location` varchar(45) NOT NULL DEFAULT '',
  `count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `npc_templateid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `group_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `randomx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `randomy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx1` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy1` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx2` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy2` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `min_respawn_delay` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `max_respawn_delay` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `respawn_screen` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `movement_distance` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `rest` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `near_spawn` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `spawnlist_door` int(10) NOT NULL DEFAULT 0,
  `count_map` int(10) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_light`
--

CREATE TABLE `spawnlist_light` (
  `id` int(10) UNSIGNED NOT NULL,
  `npcid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_npc`
--

CREATE TABLE `spawnlist_npc` (
  `id` int(10) UNSIGNED NOT NULL,
  `location` varchar(19) NOT NULL DEFAULT '',
  `count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `npc_templateid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `randomx` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `randomy` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `respawn_delay` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `movement_distance` int(3) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_npc_cash_shop`
--

CREATE TABLE `spawnlist_npc_cash_shop` (
  `id` int(10) UNSIGNED NOT NULL,
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `memo` text DEFAULT NULL,
  `name` varchar(40) NOT NULL,
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(10) NOT NULL DEFAULT 0,
  `title` varchar(35) NOT NULL DEFAULT '',
  `shop_chat` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_npc_shop`
--

CREATE TABLE `spawnlist_npc_shop` (
  `id` int(10) UNSIGNED NOT NULL,
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `memo` text DEFAULT NULL,
  `name` varchar(40) NOT NULL,
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(10) NOT NULL DEFAULT 0,
  `title` varchar(35) NOT NULL DEFAULT '',
  `shop_chat` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_ruun`
--

CREATE TABLE `spawnlist_ruun` (
  `id` int(3) UNSIGNED NOT NULL,
  `stage` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(45) NOT NULL DEFAULT '',
  `npcId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locX` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locY` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `mapId` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `range` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `count` int(3) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_trap`
--

CREATE TABLE `spawnlist_trap` (
  `id` int(8) NOT NULL,
  `note` varchar(64) DEFAULT NULL,
  `trapId` int(8) NOT NULL,
  `mapId` int(4) NOT NULL,
  `locX` int(4) NOT NULL,
  `locY` int(4) NOT NULL,
  `locRndX` int(4) NOT NULL DEFAULT 0,
  `locRndY` int(4) NOT NULL DEFAULT 0,
  `count` int(4) NOT NULL DEFAULT 1,
  `span` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_ub`
--

CREATE TABLE `spawnlist_ub` (
  `id` int(10) UNSIGNED NOT NULL,
  `ub_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `pattern` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `group_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(19) NOT NULL DEFAULT '',
  `npc_templateid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `spawn_delay` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `seal_count` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `isBoss` enum('false','true') NOT NULL DEFAULT 'false',
  `isGateKeeper` enum('false','true') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_valakas_room`
--

CREATE TABLE `spawnlist_valakas_room` (
  `id` int(10) UNSIGNED NOT NULL,
  `type` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(19) NOT NULL DEFAULT '',
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(2) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spawnlist_worldwar`
--

CREATE TABLE `spawnlist_worldwar` (
  `id` int(3) UNSIGNED NOT NULL,
  `type` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(45) NOT NULL DEFAULT '',
  `npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `locx` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `locy` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `mapid` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `heading` int(2) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spell_melt`
--

CREATE TABLE `spell_melt` (
  `skillId` int(5) NOT NULL DEFAULT -1,
  `skillName` varchar(50) DEFAULT NULL,
  `passiveId` int(3) NOT NULL DEFAULT 0,
  `classType` enum('lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown') NOT NULL DEFAULT 'crown',
  `skillItemId` int(9) NOT NULL DEFAULT 0,
  `meltItemId` int(9) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spr_action`
--

CREATE TABLE `spr_action` (
  `spr_id` int(10) NOT NULL,
  `act_id` int(10) NOT NULL,
  `act_name` varchar(128) DEFAULT NULL,
  `framecount` int(10) DEFAULT NULL,
  `framerate` int(10) DEFAULT NULL,
  `numOfFrame` int(10) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `spr_info`
--

CREATE TABLE `spr_info` (
  `spr_id` int(10) NOT NULL,
  `spr_name` varchar(200) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `shadow` int(6) NOT NULL DEFAULT 0,
  `type` int(6) NOT NULL DEFAULT 0,
  `attr` int(3) NOT NULL DEFAULT 0,
  `width` int(6) NOT NULL DEFAULT 0,
  `height` int(6) NOT NULL DEFAULT 0,
  `flying_type` int(3) NOT NULL DEFAULT 0,
  `action_count` int(10) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_bookquest_compensate`
--

CREATE TABLE `tb_bookquest_compensate` (
  `id` int(10) NOT NULL,
  `difficulty` int(3) DEFAULT 1,
  `type` varchar(20) NOT NULL,
  `num1` int(10) DEFAULT NULL,
  `num2` int(10) DEFAULT NULL,
  `id1` int(10) DEFAULT NULL,
  `id2` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_lfccompensate`
--

CREATE TABLE `tb_lfccompensate` (
  `ID` int(10) NOT NULL,
  `LFCID` int(2) DEFAULT 0,
  `PARTITION` int(10) DEFAULT 0,
  `TYPE` varchar(20) DEFAULT NULL,
  `IDENTITY` int(10) DEFAULT 0,
  `QUANTITY` int(50) DEFAULT 0,
  `LEVEL` int(10) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_lfctypes`
--

CREATE TABLE `tb_lfctypes` (
  `ID` int(2) NOT NULL,
  `TYPE` int(2) DEFAULT 0,
  `NAME` varchar(50) DEFAULT NULL,
  `USE` int(2) DEFAULT 0,
  `BUFF_SPAWN_TIME` int(10) DEFAULT 0,
  `POSSIBLE_LEVEL` int(10) DEFAULT 0,
  `MIN_PARTY` int(10) DEFAULT 0,
  `MAX_PARTY` int(10) DEFAULT 0,
  `NEED_ITEMID` int(10) DEFAULT 0,
  `NEED_ITEMCOUNT` int(10) DEFAULT 0,
  `PLAY_INST` varchar(50) DEFAULT NULL,
  `MAPRT_LEFT` int(10) DEFAULT 0,
  `MAPRT_TOP` int(10) DEFAULT 0,
  `MAPRT_RIGHT` int(10) DEFAULT 0,
  `MAPRT_BOTTOM` int(10) DEFAULT 0,
  `MAPID` int(10) DEFAULT 0,
  `STARTPOS_REDX` int(10) DEFAULT 0,
  `STARTPOS_REDY` int(10) DEFAULT 0,
  `STARTPOS_BLUEX` int(10) DEFAULT 0,
  `STARTPOS_BLUEY` int(10) DEFAULT 0,
  `PLAYTIME` int(10) DEFAULT 0,
  `READYTIME` int(10) DEFAULT 0,
  `RAND_WINNER_RATIO` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_monster_book`
--

CREATE TABLE `tb_monster_book` (
  `npc_id` int(10) DEFAULT NULL,
  `npc_name` varchar(50) DEFAULT NULL,
  `book_id` int(10) NOT NULL DEFAULT 0,
  `book_step_first` int(10) DEFAULT NULL,
  `book_step_second` int(10) DEFAULT NULL,
  `book_step_third` int(10) DEFAULT NULL,
  `book_clear_num` int(3) DEFAULT NULL,
  `week_difficulty` int(3) DEFAULT NULL,
  `week_success_count` int(10) DEFAULT NULL,
  `tel_x` int(3) DEFAULT NULL,
  `tel_y` int(3) DEFAULT NULL,
  `tel_mapId` int(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_monster_book_clearinfo`
--

CREATE TABLE `tb_monster_book_clearinfo` (
  `book_id` int(10) NOT NULL,
  `book_clear_num2` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_user_monster_book`
--

CREATE TABLE `tb_user_monster_book` (
  `char_id` int(10) NOT NULL,
  `book_id` int(10) NOT NULL,
  `difficulty` int(3) DEFAULT 1,
  `step` int(10) DEFAULT 0,
  `completed` int(3) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_user_week_quest`
--

CREATE TABLE `tb_user_week_quest` (
  `char_id` int(10) NOT NULL,
  `bookId` int(10) DEFAULT NULL,
  `difficulty` int(3) NOT NULL,
  `section` int(3) NOT NULL DEFAULT 0,
  `step` int(10) DEFAULT 0,
  `stamp` datetime DEFAULT NULL,
  `completed` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_weekquest_compensate`
--

CREATE TABLE `tb_weekquest_compensate` (
  `button_no` int(3) NOT NULL,
  `ingredient_itemId` int(10) DEFAULT 0,
  `compen_exp` int(10) DEFAULT 0,
  `compen_exp_level` int(10) DEFAULT 0,
  `compen_itemId` int(10) DEFAULT 0,
  `compen_itemCount` int(10) DEFAULT 0,
  `compen_itemLevel` int(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_weekquest_matrix`
--

CREATE TABLE `tb_weekquest_matrix` (
  `difficulty` int(10) NOT NULL,
  `col1` int(10) DEFAULT NULL,
  `col2` int(10) DEFAULT NULL,
  `col3` int(10) DEFAULT NULL,
  `stamp` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_weekquest_updateinfo`
--

CREATE TABLE `tb_weekquest_updateinfo` (
  `id` int(3) NOT NULL,
  `lastTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tj_coupon`
--

CREATE TABLE `tj_coupon` (
  `objId` int(10) NOT NULL DEFAULT 0,
  `charId` int(10) NOT NULL DEFAULT 0,
  `itemId` int(10) NOT NULL DEFAULT 0,
  `count` int(10) NOT NULL DEFAULT 0,
  `enchantLevel` int(9) NOT NULL DEFAULT 0,
  `attrLevel` int(3) NOT NULL DEFAULT 0,
  `bless` int(3) NOT NULL DEFAULT 1,
  `lostTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `town`
--

CREATE TABLE `town` (
  `town_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(45) NOT NULL DEFAULT '',
  `leader_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `leader_name` varchar(45) DEFAULT NULL,
  `tax_rate` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `tax_rate_reserved` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `sales_money` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `sales_money_yesterday` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `town_tax` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `town_fix_tax` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `town_npc`
--

CREATE TABLE `town_npc` (
  `npc_id` int(11) NOT NULL DEFAULT 0,
  `description` varchar(100) DEFAULT NULL,
  `town` enum('TALK_ISLAND','SILVER_KNIGHT','GLUDIO','ORCISH_FOREST','WINDAWOOD','KENT','GIRAN','HEINE','WERLDAN','OREN','ELVEN_FOREST','ADEN','SILENT_CAVERN','BEHEMOTH','SILVERIA','OUM_DUNGEON','RESISTANCE','PIRATE_ISLAND','RECLUSE_VILLAGE','HIDDEN_VALLEY','CLAUDIA','REDSOLDER','SKYGARDEN','RUUN') NOT NULL DEFAULT 'TALK_ISLAND'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trap`
--

CREATE TABLE `trap` (
  `id` int(8) NOT NULL,
  `note` varchar(64) DEFAULT NULL,
  `type` varchar(64) NOT NULL,
  `gfxId` int(4) NOT NULL,
  `isDetectionable` tinyint(1) NOT NULL,
  `base` int(4) NOT NULL,
  `dice` int(4) NOT NULL,
  `diceCount` int(4) NOT NULL,
  `poisonType` char(1) NOT NULL DEFAULT 'n',
  `poisonDelay` int(4) NOT NULL DEFAULT 0,
  `poisonTime` int(4) NOT NULL DEFAULT 0,
  `poisonDamage` int(4) NOT NULL DEFAULT 0,
  `monsterNpcId` int(4) NOT NULL DEFAULT 0,
  `monsterCount` int(4) NOT NULL DEFAULT 0,
  `teleportX` int(4) NOT NULL DEFAULT 0,
  `teleportY` int(4) NOT NULL DEFAULT 0,
  `teleportMapId` int(4) NOT NULL DEFAULT 0,
  `skillId` int(4) NOT NULL DEFAULT -1,
  `skillTimeSeconds` int(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ub_managers`
--

CREATE TABLE `ub_managers` (
  `ub_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `ub_manager_npc_id` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ub_rank`
--

CREATE TABLE `ub_rank` (
  `ub_id` int(10) NOT NULL DEFAULT 0,
  `char_name` varchar(45) CHARACTER SET euckr NOT NULL,
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ub_settings`
--

CREATE TABLE `ub_settings` (
  `ub_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `ub_name` varchar(45) NOT NULL DEFAULT '',
  `ub_mapid` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `ub_area_x1` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `ub_area_y1` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `ub_area_x2` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `ub_area_y2` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `min_lvl` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `max_lvl` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `max_player` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `enter_royal` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_knight` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_mage` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_elf` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_darkelf` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_dragonknight` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_illusionist` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_Warrior` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_Fencer` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_Lancer` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_male` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `enter_female` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `use_pot` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `hpr_bonus` int(10) NOT NULL DEFAULT 0,
  `mpr_bonus` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ub_times`
--

CREATE TABLE `ub_times` (
  `ub_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `ub_time` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `util_fighter`
--

CREATE TABLE `util_fighter` (
  `Num` int(10) UNSIGNED NOT NULL,
  `WinCount` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `LoseCount` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `util_racer`
--

CREATE TABLE `util_racer` (
  `Num` int(10) UNSIGNED NOT NULL,
  `WinCount` int(10) NOT NULL DEFAULT 0,
  `LoseCount` int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `war_time`
--

CREATE TABLE `war_time` (
  `castleId` int(2) NOT NULL,
  `castleName` varchar(255) DEFAULT NULL,
  `day` enum('토요일','금요일','목요일','수요일','화요일','월요일','일요일') NOT NULL DEFAULT '일요일',
  `hour` enum('23','22','21','20','19','18','17','16','15','14','13','12','11','10','9','8','7','6','5','4','3','2','1','0') NOT NULL DEFAULT '0',
  `minute` enum('50','40','30','20','10','0') NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `weapon`
--

CREATE TABLE `weapon` (
  `item_id` int(10) UNSIGNED NOT NULL,
  `item_name_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `desc_kr` varchar(45) NOT NULL DEFAULT '',
  `desc_id` varchar(45) NOT NULL DEFAULT '',
  `itemGrade` enum('ONLY','MYTH','LEGEND','HERO','RARE','ADVANC','NORMAL') NOT NULL DEFAULT 'NORMAL',
  `type` enum('SWORD','DAGGER','TOHAND_SWORD','BOW','SPEAR','BLUNT','STAFF','STING','ARROW','GAUNTLET','CLAW','EDORYU','SINGLE_BOW','SINGLE_SPEAR','TOHAND_BLUNT','TOHAND_STAFF','KEYRINGK','CHAINSWORD') NOT NULL DEFAULT 'SWORD',
  `material` enum('NONE(-)','LIQUID(액체)','WAX(밀랍)','VEGGY(식물성)','FLESH(동물성)','PAPER(종이)','CLOTH(천)','LEATHER(가죽)','WOOD(나무)','BONE(뼈)','DRAGON_HIDE(용비늘)','IRON(철)','METAL(금속)','COPPER(구리)','SILVER(은)','GOLD(금)','PLATINUM(백금)','MITHRIL(미스릴)','PLASTIC(블랙미스릴)','GLASS(유리)','GEMSTONE(보석)','MINERAL(광석)','ORIHARUKON(오리하루콘)','DRANIUM(드라니움)') NOT NULL DEFAULT 'NONE(-)',
  `weight` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `iconId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `spriteId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `dmg_small` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `dmg_large` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `safenchant` int(3) NOT NULL DEFAULT 0,
  `use_royal` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_knight` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_mage` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_elf` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_darkelf` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_dragonknight` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_illusionist` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_warrior` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_fencer` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `use_lancer` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `hitmodifier` int(6) NOT NULL DEFAULT 0,
  `dmgmodifier` int(6) NOT NULL DEFAULT 0,
  `add_str` int(3) NOT NULL DEFAULT 0,
  `add_con` int(3) NOT NULL DEFAULT 0,
  `add_dex` int(3) NOT NULL DEFAULT 0,
  `add_int` int(3) NOT NULL DEFAULT 0,
  `add_wis` int(3) NOT NULL DEFAULT 0,
  `add_cha` int(3) NOT NULL DEFAULT 0,
  `add_hp` int(3) NOT NULL DEFAULT 0,
  `add_mp` int(3) NOT NULL DEFAULT 0,
  `add_hpr` int(3) NOT NULL DEFAULT 0,
  `add_mpr` int(3) NOT NULL DEFAULT 0,
  `add_sp` int(3) NOT NULL DEFAULT 0,
  `m_def` int(3) NOT NULL DEFAULT 0,
  `haste_item` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `double_dmg_chance` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `magicdmgmodifier` int(3) NOT NULL DEFAULT 0,
  `canbedmg` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `min_lvl` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `max_lvl` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `bless` int(2) UNSIGNED NOT NULL DEFAULT 1,
  `trade` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `retrieve` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `specialretrieve` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `cant_delete` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `cant_sell` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `max_use_time` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `regist_skill` int(2) NOT NULL DEFAULT 0,
  `regist_spirit` int(2) NOT NULL DEFAULT 0,
  `regist_dragon` int(2) NOT NULL DEFAULT 0,
  `regist_fear` int(2) NOT NULL DEFAULT 0,
  `regist_all` int(2) NOT NULL DEFAULT 0,
  `hitup_skill` int(2) NOT NULL DEFAULT 0,
  `hitup_spirit` int(2) NOT NULL DEFAULT 0,
  `hitup_dragon` int(2) NOT NULL DEFAULT 0,
  `hitup_fear` int(2) NOT NULL DEFAULT 0,
  `hitup_all` int(2) NOT NULL DEFAULT 0,
  `hitup_magic` int(2) NOT NULL DEFAULT 0,
  `damage_reduction` int(2) NOT NULL DEFAULT 0,
  `MagicDamageReduction` int(2) NOT NULL DEFAULT 0,
  `reductionEgnor` int(2) NOT NULL DEFAULT 0,
  `reductionPercent` int(2) NOT NULL DEFAULT 0,
  `PVPDamage` int(2) NOT NULL DEFAULT 0,
  `PVPDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPDamageReductionPercent` int(2) NOT NULL DEFAULT 0,
  `PVPMagicDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `PVPMagicDamageReductionEgnor` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusDamageReduction` int(2) NOT NULL DEFAULT 0,
  `abnormalStatusPVPDamageReduction` int(2) NOT NULL DEFAULT 0,
  `PVPDamagePercent` int(2) NOT NULL DEFAULT 0,
  `expBonus` int(3) NOT NULL DEFAULT 0,
  `rest_exp_reduce_efficiency` int(3) NOT NULL DEFAULT 0,
  `shortCritical` int(2) NOT NULL DEFAULT 0,
  `longCritical` int(2) NOT NULL DEFAULT 0,
  `magicCritical` int(2) NOT NULL DEFAULT 0,
  `addDg` int(2) NOT NULL DEFAULT 0,
  `addEr` int(2) NOT NULL DEFAULT 0,
  `addMe` int(2) NOT NULL DEFAULT 0,
  `poisonRegist` enum('false','true') NOT NULL DEFAULT 'false',
  `imunEgnor` int(3) NOT NULL DEFAULT 0,
  `stunDuration` int(2) NOT NULL DEFAULT 0,
  `tripleArrowStun` int(2) NOT NULL DEFAULT 0,
  `strangeTimeIncrease` int(4) NOT NULL DEFAULT 0,
  `strangeTimeDecrease` int(4) NOT NULL DEFAULT 0,
  `potionRegist` int(2) NOT NULL DEFAULT 0,
  `potionPercent` int(2) NOT NULL DEFAULT 0,
  `potionValue` int(2) NOT NULL DEFAULT 0,
  `hprAbsol32Second` int(2) NOT NULL DEFAULT 0,
  `mprAbsol64Second` int(2) NOT NULL DEFAULT 0,
  `mprAbsol16Second` int(2) NOT NULL DEFAULT 0,
  `hpPotionDelayDecrease` int(4) NOT NULL DEFAULT 0,
  `hpPotionCriticalProb` int(4) NOT NULL DEFAULT 0,
  `increaseArmorSkillProb` int(4) NOT NULL DEFAULT 0,
  `attackSpeedDelayRate` int(3) NOT NULL DEFAULT 0,
  `moveSpeedDelayRate` int(3) NOT NULL DEFAULT 0,
  `Magic_name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `weapon_damege`
--

CREATE TABLE `weapon_damege` (
  `item_id` int(10) NOT NULL,
  `name` varchar(40) NOT NULL,
  `addDamege` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `weapon_skill`
--

CREATE TABLE `weapon_skill` (
  `weapon_id` int(11) UNSIGNED NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `attackType` enum('PVE','PVP','ALL') NOT NULL DEFAULT 'ALL',
  `probability` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `fix_damage` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `random_damage` int(6) UNSIGNED NOT NULL DEFAULT 0,
  `area` int(3) NOT NULL DEFAULT 0,
  `skill_id` int(11) NOT NULL DEFAULT -1,
  `skill_time` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `effect_id` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `effect_target` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `arrow_type` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `attr` enum('NONE','EARTH','FIRE','WATER','WIND','RAY') NOT NULL DEFAULT 'NONE',
  `enchant_probability` int(3) NOT NULL DEFAULT 0,
  `enchant_damage` int(3) NOT NULL DEFAULT 0,
  `int_damage` int(3) NOT NULL DEFAULT 0,
  `spell_damage` int(3) NOT NULL DEFAULT 0,
  `enchant_limit` int(3) NOT NULL DEFAULT 0,
  `hpStill` enum('true','false') NOT NULL DEFAULT 'false',
  `hpStill_probabliity` int(3) NOT NULL DEFAULT 0,
  `hpStillValue` int(3) NOT NULL DEFAULT 0,
  `mpStill` enum('true','false') NOT NULL DEFAULT 'false',
  `mpStill_probabliity` int(11) NOT NULL DEFAULT 0,
  `mpStillValue` int(3) NOT NULL DEFAULT 0,
  `stillEffectId` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `_nouse_board`
--

CREATE TABLE `_nouse_board` (
  `id` int(10) NOT NULL DEFAULT 0,
  `name` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `title` varchar(16) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `board_id` int(10) NOT NULL DEFAULT 0,
  `remaining_time` datetime DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `삭제금지 board_auction`
--

CREATE TABLE `삭제금지 board_auction` (
  `house_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `house_name` varchar(45) NOT NULL DEFAULT '',
  `house_area` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `deadline` datetime DEFAULT NULL,
  `price` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(45) NOT NULL DEFAULT '',
  `old_owner` varchar(45) NOT NULL DEFAULT '',
  `old_owner_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `bidder` varchar(45) NOT NULL DEFAULT '',
  `bidder_id` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `삭제금지 characters`
--

CREATE TABLE `삭제금지 characters` (
  `account_name` varchar(50) DEFAULT NULL,
  `objid` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `char_name` varchar(45) NOT NULL DEFAULT '',
  `level` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `HighLevel` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `Exp` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `MaxHp` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `CurHp` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `MaxMp` int(10) NOT NULL DEFAULT 0,
  `CurMp` int(10) NOT NULL DEFAULT 0,
  `Ac` int(10) NOT NULL DEFAULT 0,
  `Str` int(3) NOT NULL DEFAULT 0,
  `BaseStr` int(3) NOT NULL DEFAULT 0,
  `Con` int(3) NOT NULL DEFAULT 0,
  `BaseCon` int(3) NOT NULL DEFAULT 0,
  `Dex` int(3) NOT NULL DEFAULT 0,
  `BaseDex` int(3) NOT NULL DEFAULT 0,
  `Cha` int(3) NOT NULL DEFAULT 0,
  `BaseCha` int(3) NOT NULL DEFAULT 0,
  `Intel` int(3) NOT NULL DEFAULT 0,
  `BaseIntel` int(3) NOT NULL DEFAULT 0,
  `Wis` int(3) NOT NULL DEFAULT 0,
  `BaseWis` int(3) NOT NULL DEFAULT 0,
  `Status` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Class` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Sex` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Type` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Heading` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `LocX` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `LocY` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `MapID` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Food` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Lawful` int(10) NOT NULL DEFAULT 0,
  `Title` varchar(35) NOT NULL DEFAULT '',
  `ClanID` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Clanname` varchar(45) NOT NULL DEFAULT '',
  `ClanRank` int(3) NOT NULL DEFAULT 0,
  `notes` varchar(60) NOT NULL,
  `BonusStatus` int(10) NOT NULL DEFAULT 0,
  `ElixirStatus` int(10) NOT NULL DEFAULT 0,
  `ElfAttr` int(10) NOT NULL DEFAULT 0,
  `PKcount` int(10) NOT NULL DEFAULT 0,
  `ExpRes` int(10) NOT NULL DEFAULT 0,
  `PartnerID` int(10) NOT NULL DEFAULT 0,
  `AccessLevel` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `OnlineStatus` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `HomeTownID` int(10) NOT NULL DEFAULT 0,
  `Contribution` int(10) NOT NULL DEFAULT 0,
  `Pay` int(10) NOT NULL DEFAULT 0,
  `HellTime` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `Banned` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `Karma` int(10) NOT NULL DEFAULT 0,
  `LastPk` datetime DEFAULT NULL,
  `DeleteTime` datetime DEFAULT NULL,
  `ReturnStat` int(10) NOT NULL,
  `sealingPW` varchar(10) DEFAULT NULL,
  `sealScrollTime` int(11) NOT NULL DEFAULT 0,
  `sealScrollCount` int(11) NOT NULL DEFAULT 0,
  `lastLoginTime` datetime DEFAULT NULL,
  `lastLogoutTime` datetime DEFAULT NULL,
  `einhasad` int(11) NOT NULL DEFAULT 0,
  `AinState` int(1) NOT NULL DEFAULT 0,
  `SurvivalGauge` int(1) NOT NULL DEFAULT 30,
  `BirthDay` int(11) DEFAULT NULL,
  `PC_Kill` int(10) DEFAULT NULL,
  `PC_Death` int(10) DEFAULT NULL,
  `GiranTime` int(10) NOT NULL DEFAULT 0,
  `OrenTime` int(10) NOT NULL DEFAULT 0,
  `DrageonTime` int(10) NOT NULL DEFAULT 0,
  `RadungeonTime` int(10) NOT NULL DEFAULT 0,
  `SomeTime` int(10) NOT NULL DEFAULT 0,
  `SoulTime` int(10) NOT NULL DEFAULT 0,
  `Mark_Count` int(10) NOT NULL DEFAULT 60,
  `Age` int(2) DEFAULT 0,
  `AddDamage` int(3) DEFAULT 0,
  `AddDamageRate` int(3) DEFAULT 0,
  `AddReduction` int(3) DEFAULT 0,
  `AddReductionRate` int(3) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `삭제금지 clan_data`
--

CREATE TABLE `삭제금지 clan_data` (
  `clan_id` int(10) UNSIGNED NOT NULL,
  `clan_name` varchar(45) NOT NULL DEFAULT '',
  `leader_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `leader_name` varchar(45) NOT NULL DEFAULT '',
  `hascastle` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `hashouse` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `alliance` int(10) NOT NULL DEFAULT 0,
  `clan_birthday` datetime NOT NULL,
  `bot` enum('true','false') DEFAULT 'false',
  `bot_style` tinyint(3) DEFAULT 0,
  `bot_level` tinyint(3) DEFAULT 0,
  `max_online_user` int(10) DEFAULT NULL,
  `announcement` varchar(160) NOT NULL,
  `emblem_id` int(10) NOT NULL DEFAULT 0,
  `emblem_status` tinyint(1) NOT NULL DEFAULT 0,
  `clan_exp` int(10) DEFAULT 0,
  `bless` int(45) NOT NULL DEFAULT 0,
  `bless_count` int(45) NOT NULL DEFAULT 0,
  `attack` int(45) NOT NULL DEFAULT 0,
  `defence` int(45) NOT NULL DEFAULT 0,
  `pvpattack` int(45) NOT NULL DEFAULT 0,
  `pvpdefence` int(45) NOT NULL DEFAULT 0,
  `under_dungeon` tinyint(3) NOT NULL DEFAULT 0,
  `ranktime` int(10) NOT NULL DEFAULT 0,
  `rankdate` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `삭제금지 house`
--

CREATE TABLE `삭제금지 house` (
  `house_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `house_name` varchar(45) NOT NULL DEFAULT '',
  `house_area` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(45) NOT NULL DEFAULT '',
  `keeper_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `is_on_sale` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `is_purchase_basement` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `tax_deadline` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `삭제금지 town`
--

CREATE TABLE `삭제금지 town` (
  `town_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(45) NOT NULL DEFAULT '',
  `leader_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `leader_name` varchar(45) DEFAULT NULL,
  `tax_rate` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `tax_rate_reserved` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `sales_money` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `sales_money_yesterday` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `town_tax` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `town_fix_tax` int(10) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=euckr;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `0_translations`
--
ALTER TABLE `0_translations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `translations_idx_text_korean` (`text_korean`(768));

--
-- Indices de la tabla `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`login`);

--
-- Indices de la tabla `accounts_free_buff_shield`
--
ALTER TABLE `accounts_free_buff_shield`
  ADD PRIMARY KEY (`account_name`);

--
-- Indices de la tabla `accounts_pcmaster_golden`
--
ALTER TABLE `accounts_pcmaster_golden`
  ADD PRIMARY KEY (`account_name`,`index_id`);

--
-- Indices de la tabla `accounts_shop_limit`
--
ALTER TABLE `accounts_shop_limit`
  ADD PRIMARY KEY (`accountName`,`buyShopId`,`buyItemId`);

--
-- Indices de la tabla `ai_user`
--
ALTER TABLE `ai_user`
  ADD PRIMARY KEY (`name`,`ai_type`);

--
-- Indices de la tabla `ai_user_buff`
--
ALTER TABLE `ai_user_buff`
  ADD PRIMARY KEY (`class`,`elfAttr`);

--
-- Indices de la tabla `ai_user_drop`
--
ALTER TABLE `ai_user_drop`
  ADD PRIMARY KEY (`class`,`itemId`);

--
-- Indices de la tabla `ai_user_fish`
--
ALTER TABLE `ai_user_fish`
  ADD PRIMARY KEY (`loc_x`,`loc_y`);

--
-- Indices de la tabla `ai_user_item`
--
ALTER TABLE `ai_user_item`
  ADD PRIMARY KEY (`class`,`itemId`);

--
-- Indices de la tabla `ai_user_ment`
--
ALTER TABLE `ai_user_ment`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `ai_user_skill`
--
ALTER TABLE `ai_user_skill`
  ADD PRIMARY KEY (`class`);

--
-- Indices de la tabla `app_alim_log`
--
ALTER TABLE `app_alim_log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`account_name`);

--
-- Indices de la tabla `app_auth_extension`
--
ALTER TABLE `app_auth_extension`
  ADD PRIMARY KEY (`extension`);

--
-- Indices de la tabla `app_board_content`
--
ALTER TABLE `app_board_content`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_board_content_comment`
--
ALTER TABLE `app_board_content_comment`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_board_free`
--
ALTER TABLE `app_board_free`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_board_free_comment`
--
ALTER TABLE `app_board_free_comment`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_board_notice`
--
ALTER TABLE `app_board_notice`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_board_pitch`
--
ALTER TABLE `app_board_pitch`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_board_pitch_comment`
--
ALTER TABLE `app_board_pitch_comment`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_coupon`
--
ALTER TABLE `app_coupon`
  ADD PRIMARY KEY (`number`);

--
-- Indices de la tabla `app_customer`
--
ALTER TABLE `app_customer`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_customer_normal`
--
ALTER TABLE `app_customer_normal`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_dictionary_item`
--
ALTER TABLE `app_dictionary_item`
  ADD PRIMARY KEY (`schar`);

--
-- Indices de la tabla `app_engine_log`
--
ALTER TABLE `app_engine_log`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_guide`
--
ALTER TABLE `app_guide`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_guide_boss`
--
ALTER TABLE `app_guide_boss`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_guide_recommend`
--
ALTER TABLE `app_guide_recommend`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_item_search`
--
ALTER TABLE `app_item_search`
  ADD PRIMARY KEY (`seq`,`item_name`);

--
-- Indices de la tabla `app_nshop`
--
ALTER TABLE `app_nshop`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_page_info`
--
ALTER TABLE `app_page_info`
  ADD PRIMARY KEY (`uri`);

--
-- Indices de la tabla `app_powerbook`
--
ALTER TABLE `app_powerbook`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_powerbook_guide`
--
ALTER TABLE `app_powerbook_guide`
  ADD PRIMARY KEY (`group_type`,`id`);

--
-- Indices de la tabla `app_promotion`
--
ALTER TABLE `app_promotion`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_report`
--
ALTER TABLE `app_report`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_shop_rank`
--
ALTER TABLE `app_shop_rank`
  ADD PRIMARY KEY (`group_type`,`shop_type`,`id`);

--
-- Indices de la tabla `app_support`
--
ALTER TABLE `app_support`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_support_message`
--
ALTER TABLE `app_support_message`
  ADD PRIMARY KEY (`type`,`index_id`);

--
-- Indices de la tabla `app_support_request`
--
ALTER TABLE `app_support_request`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_trade`
--
ALTER TABLE `app_trade`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `app_uri_block`
--
ALTER TABLE `app_uri_block`
  ADD PRIMARY KEY (`uri`);

--
-- Indices de la tabla `app_uri_pass`
--
ALTER TABLE `app_uri_pass`
  ADD PRIMARY KEY (`uri`);

--
-- Indices de la tabla `area`
--
ALTER TABLE `area`
  ADD PRIMARY KEY (`areaid`);

--
-- Indices de la tabla `armor`
--
ALTER TABLE `armor`
  ADD PRIMARY KEY (`item_id`);

--
-- Indices de la tabla `armor_set`
--
ALTER TABLE `armor_set`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `attendance_accounts`
--
ALTER TABLE `attendance_accounts`
  ADD PRIMARY KEY (`account`);

--
-- Indices de la tabla `attendance_item`
--
ALTER TABLE `attendance_item`
  ADD PRIMARY KEY (`groupType`,`index`);

--
-- Indices de la tabla `attendance_item_random`
--
ALTER TABLE `attendance_item_random`
  ADD PRIMARY KEY (`groupType`,`index`);

--
-- Indices de la tabla `auth_ip`
--
ALTER TABLE `auth_ip`
  ADD PRIMARY KEY (`ip`);

--
-- Indices de la tabla `autoloot`
--
ALTER TABLE `autoloot`
  ADD PRIMARY KEY (`item_id`);

--
-- Indices de la tabla `balance`
--
ALTER TABLE `balance`
  ADD PRIMARY KEY (`attackerType`,`targetType`);

--
-- Indices de la tabla `ban_account`
--
ALTER TABLE `ban_account`
  ADD PRIMARY KEY (`account`,`reason`);

--
-- Indices de la tabla `ban_board`
--
ALTER TABLE `ban_board`
  ADD PRIMARY KEY (`number`);

--
-- Indices de la tabla `ban_hdd`
--
ALTER TABLE `ban_hdd`
  ADD PRIMARY KEY (`number`);

--
-- Indices de la tabla `ban_ip`
--
ALTER TABLE `ban_ip`
  ADD PRIMARY KEY (`address`);

--
-- Indices de la tabla `beginner`
--
ALTER TABLE `beginner`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `beginner_addteleport`
--
ALTER TABLE `beginner_addteleport`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`char_id`);

--
-- Indices de la tabla `beginner_quest`
--
ALTER TABLE `beginner_quest`
  ADD PRIMARY KEY (`quest_id`);

--
-- Indices de la tabla `beginner_quest_drop`
--
ALTER TABLE `beginner_quest_drop`
  ADD PRIMARY KEY (`classId`);

--
-- Indices de la tabla `bin_armor_element_common`
--
ALTER TABLE `bin_armor_element_common`
  ADD PRIMARY KEY (`type`,`enchant`);

--
-- Indices de la tabla `bin_catalyst_common`
--
ALTER TABLE `bin_catalyst_common`
  ADD PRIMARY KEY (`nameId`,`input`,`output`,`failOutput`);

--
-- Indices de la tabla `bin_charged_time_map_common`
--
ALTER TABLE `bin_charged_time_map_common`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `bin_companion_class_common`
--
ALTER TABLE `bin_companion_class_common`
  ADD PRIMARY KEY (`classId`);

--
-- Indices de la tabla `bin_companion_enchant_common`
--
ALTER TABLE `bin_companion_enchant_common`
  ADD PRIMARY KEY (`tier`);

--
-- Indices de la tabla `bin_companion_skill_common`
--
ALTER TABLE `bin_companion_skill_common`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `bin_companion_stat_common`
--
ALTER TABLE `bin_companion_stat_common`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `bin_craft_common`
--
ALTER TABLE `bin_craft_common`
  ADD PRIMARY KEY (`craft_id`);

--
-- Indices de la tabla `bin_einpoint_cost_common`
--
ALTER TABLE `bin_einpoint_cost_common`
  ADD PRIMARY KEY (`value`);

--
-- Indices de la tabla `bin_einpoint_faith_common`
--
ALTER TABLE `bin_einpoint_faith_common`
  ADD PRIMARY KEY (`GroupId`,`Index_indexId`);

--
-- Indices de la tabla `bin_einpoint_meta_common`
--
ALTER TABLE `bin_einpoint_meta_common`
  ADD PRIMARY KEY (`index_id`);

--
-- Indices de la tabla `bin_einpoint_normal_prob_common`
--
ALTER TABLE `bin_einpoint_normal_prob_common`
  ADD PRIMARY KEY (`Normal_level`);

--
-- Indices de la tabla `bin_einpoint_overstat_prob_common`
--
ALTER TABLE `bin_einpoint_overstat_prob_common`
  ADD PRIMARY KEY (`over_level`);

--
-- Indices de la tabla `bin_einpoint_prob_table_common`
--
ALTER TABLE `bin_einpoint_prob_table_common`
  ADD PRIMARY KEY (`isLastChance`,`bonusPoint`);

--
-- Indices de la tabla `bin_einpoint_stat_common`
--
ALTER TABLE `bin_einpoint_stat_common`
  ADD PRIMARY KEY (`index_id`,`value`);

--
-- Indices de la tabla `bin_element_enchant_common`
--
ALTER TABLE `bin_element_enchant_common`
  ADD PRIMARY KEY (`prob_index`,`type_index`,`level`);

--
-- Indices de la tabla `bin_enchant_scroll_table_common`
--
ALTER TABLE `bin_enchant_scroll_table_common`
  ADD PRIMARY KEY (`enchantType`,`nameid`);

--
-- Indices de la tabla `bin_enchant_table_common`
--
ALTER TABLE `bin_enchant_table_common`
  ADD PRIMARY KEY (`item_index`,`bonusLevel_index`);

--
-- Indices de la tabla `bin_entermaps_common`
--
ALTER TABLE `bin_entermaps_common`
  ADD PRIMARY KEY (`id`,`action_name`);

--
-- Indices de la tabla `bin_favorbook_common`
--
ALTER TABLE `bin_favorbook_common`
  ADD PRIMARY KEY (`category_id`,`slot_id`);

--
-- Indices de la tabla `bin_general_goods_common`
--
ALTER TABLE `bin_general_goods_common`
  ADD PRIMARY KEY (`NameId`);

--
-- Indices de la tabla `bin_huntingquest_common`
--
ALTER TABLE `bin_huntingquest_common`
  ADD PRIMARY KEY (`requiredCondition_Map`,`requiredCondition_LocationDesc`);

--
-- Indices de la tabla `bin_indun_common`
--
ALTER TABLE `bin_indun_common`
  ADD PRIMARY KEY (`mapKind`);

--
-- Indices de la tabla `bin_item_common`
--
ALTER TABLE `bin_item_common`
  ADD PRIMARY KEY (`name_id`);

--
-- Indices de la tabla `bin_ndl_common`
--
ALTER TABLE `bin_ndl_common`
  ADD PRIMARY KEY (`map_number`,`npc_classId`,`territory_startXY`,`territory_endXY`);

--
-- Indices de la tabla `bin_npc_common`
--
ALTER TABLE `bin_npc_common`
  ADD PRIMARY KEY (`class_id`);

--
-- Indices de la tabla `bin_passivespell_common`
--
ALTER TABLE `bin_passivespell_common`
  ADD PRIMARY KEY (`passive_id`);

--
-- Indices de la tabla `bin_portrait_common`
--
ALTER TABLE `bin_portrait_common`
  ADD PRIMARY KEY (`asset_id`);

--
-- Indices de la tabla `bin_potential_common`
--
ALTER TABLE `bin_potential_common`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `bin_ship_common`
--
ALTER TABLE `bin_ship_common`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `bin_spell_common`
--
ALTER TABLE `bin_spell_common`
  ADD PRIMARY KEY (`spell_id`);

--
-- Indices de la tabla `bin_timecollection_common`
--
ALTER TABLE `bin_timecollection_common`
  ADD PRIMARY KEY (`group_id`,`group_set_id`);

--
-- Indices de la tabla `bin_treasureboxreward_common`
--
ALTER TABLE `bin_treasureboxreward_common`
  ADD PRIMARY KEY (`nameid`);

--
-- Indices de la tabla `bin_treasurebox_common`
--
ALTER TABLE `bin_treasurebox_common`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `board_app_lfc`
--
ALTER TABLE `board_app_lfc`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `board_auction`
--
ALTER TABLE `board_auction`
  ADD PRIMARY KEY (`house_id`);

--
-- Indices de la tabla `board_free`
--
ALTER TABLE `board_free`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `board_notice`
--
ALTER TABLE `board_notice`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `board_notice1`
--
ALTER TABLE `board_notice1`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `board_notice2`
--
ALTER TABLE `board_notice2`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `board_notice3`
--
ALTER TABLE `board_notice3`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `board_posts_fix`
--
ALTER TABLE `board_posts_fix`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `board_posts_key`
--
ALTER TABLE `board_posts_key`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `board_user`
--
ALTER TABLE `board_user`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `bots`
--
ALTER TABLE `bots`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `castle`
--
ALTER TABLE `castle`
  ADD PRIMARY KEY (`castle_id`);

--
-- Indices de la tabla `castle_present`
--
ALTER TABLE `castle_present`
  ADD PRIMARY KEY (`itemid`);

--
-- Indices de la tabla `castle_soldier`
--
ALTER TABLE `castle_soldier`
  ADD PRIMARY KEY (`castle_id`);

--
-- Indices de la tabla `catalyst`
--
ALTER TABLE `catalyst`
  ADD PRIMARY KEY (`nameId`,`input`);

--
-- Indices de la tabla `catalyst_custom`
--
ALTER TABLE `catalyst_custom`
  ADD PRIMARY KEY (`itemId`,`input_itemId`,`input_enchant`);

--
-- Indices de la tabla `characters`
--
ALTER TABLE `characters`
  ADD PRIMARY KEY (`objid`),
  ADD KEY `key_id` (`account_name`,`char_name`);

--
-- Indices de la tabla `character_arca`
--
ALTER TABLE `character_arca`
  ADD PRIMARY KEY (`id`,`charId`);

--
-- Indices de la tabla `character_beginner_quest`
--
ALTER TABLE `character_beginner_quest`
  ADD PRIMARY KEY (`charId`);

--
-- Indices de la tabla `character_buddys`
--
ALTER TABLE `character_buddys`
  ADD PRIMARY KEY (`char_id`,`buddy_name`),
  ADD KEY `key_id` (`id`);

--
-- Indices de la tabla `character_buff`
--
ALTER TABLE `character_buff`
  ADD PRIMARY KEY (`char_obj_id`,`skill_id`);

--
-- Indices de la tabla `character_companion`
--
ALTER TABLE `character_companion`
  ADD PRIMARY KEY (`item_objId`);

--
-- Indices de la tabla `character_companion_buff`
--
ALTER TABLE `character_companion_buff`
  ADD PRIMARY KEY (`objid`,`buff_id`);

--
-- Indices de la tabla `character_config`
--
ALTER TABLE `character_config`
  ADD PRIMARY KEY (`object_id`);

--
-- Indices de la tabla `character_death_exp`
--
ALTER TABLE `character_death_exp`
  ADD PRIMARY KEY (`char_id`,`delete_time`);

--
-- Indices de la tabla `character_death_item`
--
ALTER TABLE `character_death_item`
  ADD PRIMARY KEY (`db_id`);

--
-- Indices de la tabla `character_einhasadfaith`
--
ALTER TABLE `character_einhasadfaith`
  ADD PRIMARY KEY (`objId`,`indexId`);

--
-- Indices de la tabla `character_einhasadstat`
--
ALTER TABLE `character_einhasadstat`
  ADD PRIMARY KEY (`objid`);

--
-- Indices de la tabla `character_elf_warehouse`
--
ALTER TABLE `character_elf_warehouse`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`account_name`);

--
-- Indices de la tabla `character_equipset`
--
ALTER TABLE `character_equipset`
  ADD PRIMARY KEY (`charId`);

--
-- Indices de la tabla `character_eventpush`
--
ALTER TABLE `character_eventpush`
  ADD PRIMARY KEY (`push_id`);

--
-- Indices de la tabla `character_exclude`
--
ALTER TABLE `character_exclude`
  ADD PRIMARY KEY (`char_id`,`type`,`exclude_id`),
  ADD KEY `key_id` (`id`);

--
-- Indices de la tabla `character_fairly_config`
--
ALTER TABLE `character_fairly_config`
  ADD PRIMARY KEY (`object_id`);

--
-- Indices de la tabla `character_favorbook`
--
ALTER TABLE `character_favorbook`
  ADD PRIMARY KEY (`charObjId`,`category`,`slotId`);

--
-- Indices de la tabla `character_hunting_quest`
--
ALTER TABLE `character_hunting_quest`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `character_items`
--
ALTER TABLE `character_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`char_id`);

--
-- Indices de la tabla `character_monsterbooklist`
--
ALTER TABLE `character_monsterbooklist`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `character_package_warehouse`
--
ALTER TABLE `character_package_warehouse`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`account_name`);

--
-- Indices de la tabla `character_present_warehouse`
--
ALTER TABLE `character_present_warehouse`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`account_name`);

--
-- Indices de la tabla `character_quests`
--
ALTER TABLE `character_quests`
  ADD PRIMARY KEY (`char_id`,`quest_id`);

--
-- Indices de la tabla `character_revenge`
--
ALTER TABLE `character_revenge`
  ADD PRIMARY KEY (`number`);

--
-- Indices de la tabla `character_shop_limit`
--
ALTER TABLE `character_shop_limit`
  ADD PRIMARY KEY (`characterId`,`buyShopId`,`buyItemId`);

--
-- Indices de la tabla `character_skills_active`
--
ALTER TABLE `character_skills_active`
  ADD PRIMARY KEY (`char_obj_id`,`skill_id`);

--
-- Indices de la tabla `character_skills_passive`
--
ALTER TABLE `character_skills_passive`
  ADD PRIMARY KEY (`char_obj_id`,`passive_id`);

--
-- Indices de la tabla `character_soldier`
--
ALTER TABLE `character_soldier`
  ADD PRIMARY KEY (`char_id`,`time`);

--
-- Indices de la tabla `character_special_warehouse`
--
ALTER TABLE `character_special_warehouse`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`account_name`);

--
-- Indices de la tabla `character_teleport`
--
ALTER TABLE `character_teleport`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`char_id`);

--
-- Indices de la tabla `character_timecollection`
--
ALTER TABLE `character_timecollection`
  ADD PRIMARY KEY (`charObjId`,`groupId`,`setId`);

--
-- Indices de la tabla `character_warehouse`
--
ALTER TABLE `character_warehouse`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`account_name`);

--
-- Indices de la tabla `clan_bless_buff`
--
ALTER TABLE `clan_bless_buff`
  ADD PRIMARY KEY (`number`,`buff_id`);

--
-- Indices de la tabla `clan_contribution_buff`
--
ALTER TABLE `clan_contribution_buff`
  ADD PRIMARY KEY (`clan_id`);

--
-- Indices de la tabla `clan_data`
--
ALTER TABLE `clan_data`
  ADD PRIMARY KEY (`clan_id`);

--
-- Indices de la tabla `clan_emblem_attention`
--
ALTER TABLE `clan_emblem_attention`
  ADD PRIMARY KEY (`clanname`,`attentionClanname`);

--
-- Indices de la tabla `clan_history`
--
ALTER TABLE `clan_history`
  ADD PRIMARY KEY (`num`);

--
-- Indices de la tabla `clan_joinning`
--
ALTER TABLE `clan_joinning`
  ADD PRIMARY KEY (`pledge_uid`,`user_uid`);

--
-- Indices de la tabla `clan_matching_list`
--
ALTER TABLE `clan_matching_list`
  ADD PRIMARY KEY (`clanname`);

--
-- Indices de la tabla `clan_warehouse`
--
ALTER TABLE `clan_warehouse`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`clan_name`);

--
-- Indices de la tabla `clan_warehouse_list`
--
ALTER TABLE `clan_warehouse_list`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `clan_warehouse_log`
--
ALTER TABLE `clan_warehouse_log`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `commands`
--
ALTER TABLE `commands`
  ADD PRIMARY KEY (`name`);

--
-- Indices de la tabla `connect_reward`
--
ALTER TABLE `connect_reward`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `craft_block`
--
ALTER TABLE `craft_block`
  ADD PRIMARY KEY (`craft_id`);

--
-- Indices de la tabla `craft_info`
--
ALTER TABLE `craft_info`
  ADD PRIMARY KEY (`craft_id`);

--
-- Indices de la tabla `craft_npcs`
--
ALTER TABLE `craft_npcs`
  ADD PRIMARY KEY (`npc_id`);

--
-- Indices de la tabla `craft_success_count_user`
--
ALTER TABLE `craft_success_count_user`
  ADD PRIMARY KEY (`accountName`,`charId`,`craftId`);

--
-- Indices de la tabla `dogfight_tickets`
--
ALTER TABLE `dogfight_tickets`
  ADD PRIMARY KEY (`item_id`);

--
-- Indices de la tabla `droplist`
--
ALTER TABLE `droplist`
  ADD PRIMARY KEY (`mobId`,`itemId`);

--
-- Indices de la tabla `droptype_npc`
--
ALTER TABLE `droptype_npc`
  ADD PRIMARY KEY (`mobId`);

--
-- Indices de la tabla `dungeon`
--
ALTER TABLE `dungeon`
  ADD PRIMARY KEY (`src_x`,`src_y`,`src_mapid`);

--
-- Indices de la tabla `dungeon_random`
--
ALTER TABLE `dungeon_random`
  ADD PRIMARY KEY (`src_x`,`src_y`,`src_mapid`);

--
-- Indices de la tabla `dungeon_timer`
--
ALTER TABLE `dungeon_timer`
  ADD PRIMARY KEY (`timerId`);

--
-- Indices de la tabla `dungeon_timer_account`
--
ALTER TABLE `dungeon_timer_account`
  ADD PRIMARY KEY (`account`,`timerId`);

--
-- Indices de la tabla `dungeon_timer_character`
--
ALTER TABLE `dungeon_timer_character`
  ADD PRIMARY KEY (`charId`,`timerId`);

--
-- Indices de la tabla `dungeon_timer_item`
--
ALTER TABLE `dungeon_timer_item`
  ADD PRIMARY KEY (`itemId`);

--
-- Indices de la tabla `enchant_result`
--
ALTER TABLE `enchant_result`
  ADD PRIMARY KEY (`item_id`);

--
-- Indices de la tabla `etcitem`
--
ALTER TABLE `etcitem`
  ADD PRIMARY KEY (`item_id`);

--
-- Indices de la tabla `event`
--
ALTER TABLE `event`
  ADD PRIMARY KEY (`event_id`);

--
-- Indices de la tabla `exp`
--
ALTER TABLE `exp`
  ADD PRIMARY KEY (`level`);

--
-- Indices de la tabla `favorbook`
--
ALTER TABLE `favorbook`
  ADD PRIMARY KEY (`category`,`slotId`);

--
-- Indices de la tabla `free_pvp_region`
--
ALTER TABLE `free_pvp_region`
  ADD PRIMARY KEY (`worldNumber`,`box_index`);

--
-- Indices de la tabla `getback`
--
ALTER TABLE `getback`
  ADD PRIMARY KEY (`area_x1`,`area_y1`,`area_x2`,`area_y2`,`area_mapid`);

--
-- Indices de la tabla `getback_restart`
--
ALTER TABLE `getback_restart`
  ADD PRIMARY KEY (`area`);

--
-- Indices de la tabla `house`
--
ALTER TABLE `house`
  ADD PRIMARY KEY (`house_id`);

--
-- Indices de la tabla `hunting_quest`
--
ALTER TABLE `hunting_quest`
  ADD PRIMARY KEY (`quest_id`);

--
-- Indices de la tabla `hunting_quest_teleport`
--
ALTER TABLE `hunting_quest_teleport`
  ADD PRIMARY KEY (`action_string`);

--
-- Indices de la tabla `inter_race_region`
--
ALTER TABLE `inter_race_region`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `item_bookmark`
--
ALTER TABLE `item_bookmark`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`id`);

--
-- Indices de la tabla `item_box`
--
ALTER TABLE `item_box`
  ADD PRIMARY KEY (`boxId`,`classType`,`itemId`);

--
-- Indices de la tabla `item_buff`
--
ALTER TABLE `item_buff`
  ADD PRIMARY KEY (`item_id`);

--
-- Indices de la tabla `item_click_message`
--
ALTER TABLE `item_click_message`
  ADD PRIMARY KEY (`itemId`);

--
-- Indices de la tabla `item_collection`
--
ALTER TABLE `item_collection`
  ADD PRIMARY KEY (`itemId`);

--
-- Indices de la tabla `item_enchant_ablity`
--
ALTER TABLE `item_enchant_ablity`
  ADD PRIMARY KEY (`itemId`,`enchant`);

--
-- Indices de la tabla `item_key_boss`
--
ALTER TABLE `item_key_boss`
  ADD PRIMARY KEY (`item_obj_id`,`key_id`);

--
-- Indices de la tabla `item_ment`
--
ALTER TABLE `item_ment`
  ADD PRIMARY KEY (`itemId`,`mentType`);

--
-- Indices de la tabla `item_selector`
--
ALTER TABLE `item_selector`
  ADD PRIMARY KEY (`itemId`,`selectItemId`);

--
-- Indices de la tabla `item_selector_warehouse`
--
ALTER TABLE `item_selector_warehouse`
  ADD PRIMARY KEY (`itemId`,`selectItemId`);

--
-- Indices de la tabla `item_terms`
--
ALTER TABLE `item_terms`
  ADD PRIMARY KEY (`itemId`);

--
-- Indices de la tabla `letter`
--
ALTER TABLE `letter`
  ADD PRIMARY KEY (`item_object_id`);

--
-- Indices de la tabla `letter_command`
--
ALTER TABLE `letter_command`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `letter_spam`
--
ALTER TABLE `letter_spam`
  ADD PRIMARY KEY (`no`);

--
-- Indices de la tabla `levelup_quests_item`
--
ALTER TABLE `levelup_quests_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `type` (`type`),
  ADD KEY `bid` (`level`);

--
-- Indices de la tabla `log_chat`
--
ALTER TABLE `log_chat`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `log_cwarehouse`
--
ALTER TABLE `log_cwarehouse`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `log_enchant`
--
ALTER TABLE `log_enchant`
  ADD PRIMARY KEY (`id`),
  ADD KEY `key_id` (`char_id`);

--
-- Indices de la tabla `log_private_shop`
--
ALTER TABLE `log_private_shop`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `log_shop`
--
ALTER TABLE `log_shop`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `log_warehouse`
--
ALTER TABLE `log_warehouse`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `magicdoll_info`
--
ALTER TABLE `magicdoll_info`
  ADD PRIMARY KEY (`itemId`);

--
-- Indices de la tabla `magicdoll_potential`
--
ALTER TABLE `magicdoll_potential`
  ADD PRIMARY KEY (`bonusId`);

--
-- Indices de la tabla `mapids`
--
ALTER TABLE `mapids`
  ADD PRIMARY KEY (`mapid`);

--
-- Indices de la tabla `map_balance`
--
ALTER TABLE `map_balance`
  ADD PRIMARY KEY (`mapId`);

--
-- Indices de la tabla `map_fix_key`
--
ALTER TABLE `map_fix_key`
  ADD PRIMARY KEY (`locX`,`locY`,`mapId`);

--
-- Indices de la tabla `map_type`
--
ALTER TABLE `map_type`
  ADD PRIMARY KEY (`mapId`);

--
-- Indices de la tabla `marble`
--
ALTER TABLE `marble`
  ADD PRIMARY KEY (`marble_id`);

--
-- Indices de la tabla `mobgroup`
--
ALTER TABLE `mobgroup`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `mobskill`
--
ALTER TABLE `mobskill`
  ADD PRIMARY KEY (`mobid`,`actNo`);

--
-- Indices de la tabla `monster_book`
--
ALTER TABLE `monster_book`
  ADD PRIMARY KEY (`monsternumber`);

--
-- Indices de la tabla `notice`
--
ALTER TABLE `notice`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`notification_id`);

--
-- Indices de la tabla `notification_event_npc`
--
ALTER TABLE `notification_event_npc`
  ADD PRIMARY KEY (`notification_id`,`order_id`);

--
-- Indices de la tabla `npc`
--
ALTER TABLE `npc`
  ADD PRIMARY KEY (`npcid`);

--
-- Indices de la tabla `npcaction`
--
ALTER TABLE `npcaction`
  ADD PRIMARY KEY (`npcid`);

--
-- Indices de la tabla `npcaction_teleport`
--
ALTER TABLE `npcaction_teleport`
  ADD PRIMARY KEY (`npcId`,`actionName`);

--
-- Indices de la tabla `npcchat`
--
ALTER TABLE `npcchat`
  ADD PRIMARY KEY (`npc_id`,`chat_timing`);

--
-- Indices de la tabla `npc_info`
--
ALTER TABLE `npc_info`
  ADD PRIMARY KEY (`npcId`);

--
-- Indices de la tabla `npc_night`
--
ALTER TABLE `npc_night`
  ADD PRIMARY KEY (`npcId`,`targetMapId`);

--
-- Indices de la tabla `penalty_pass_item`
--
ALTER TABLE `penalty_pass_item`
  ADD PRIMARY KEY (`itemId`);

--
-- Indices de la tabla `penalty_protect_item`
--
ALTER TABLE `penalty_protect_item`
  ADD PRIMARY KEY (`itemId`);

--
-- Indices de la tabla `playsupport`
--
ALTER TABLE `playsupport`
  ADD PRIMARY KEY (`mapid`);

--
-- Indices de la tabla `polyitems`
--
ALTER TABLE `polyitems`
  ADD PRIMARY KEY (`itemId`);

--
-- Indices de la tabla `polymorphs`
--
ALTER TABLE `polymorphs`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `polyweapon`
--
ALTER TABLE `polyweapon`
  ADD PRIMARY KEY (`polyId`);

--
-- Indices de la tabla `proto_packet`
--
ALTER TABLE `proto_packet`
  ADD PRIMARY KEY (`code`);

--
-- Indices de la tabla `race_div_record`
--
ALTER TABLE `race_div_record`
  ADD PRIMARY KEY (`id`,`bug_number`);

--
-- Indices de la tabla `race_record`
--
ALTER TABLE `race_record`
  ADD PRIMARY KEY (`number`);

--
-- Indices de la tabla `race_tickets`
--
ALTER TABLE `race_tickets`
  ADD PRIMARY KEY (`item_id`);

--
-- Indices de la tabla `repair_item_cost`
--
ALTER TABLE `repair_item_cost`
  ADD PRIMARY KEY (`itemId`);

--
-- Indices de la tabla `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`target`,`reporter`);

--
-- Indices de la tabla `resolvent`
--
ALTER TABLE `resolvent`
  ADD PRIMARY KEY (`item_id`);

--
-- Indices de la tabla `robot_location`
--
ALTER TABLE `robot_location`
  ADD PRIMARY KEY (`uid`);

--
-- Indices de la tabla `robot_location최신`
--
ALTER TABLE `robot_location최신`
  ADD PRIMARY KEY (`uid`);

--
-- Indices de la tabla `robot_message`
--
ALTER TABLE `robot_message`
  ADD PRIMARY KEY (`uid`);

--
-- Indices de la tabla `robot_name`
--
ALTER TABLE `robot_name`
  ADD PRIMARY KEY (`uid`);

--
-- Indices de la tabla `robot_teleport_list`
--
ALTER TABLE `robot_teleport_list`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `serverinfo`
--
ALTER TABLE `serverinfo`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `server_explain`
--
ALTER TABLE `server_explain`
  ADD PRIMARY KEY (`num`);

--
-- Indices de la tabla `shop`
--
ALTER TABLE `shop`
  ADD PRIMARY KEY (`npc_id`,`item_id`,`order_id`);

--
-- Indices de la tabla `shop_aden`
--
ALTER TABLE `shop_aden`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `shop_info`
--
ALTER TABLE `shop_info`
  ADD PRIMARY KEY (`npcId`);

--
-- Indices de la tabla `shop_limit`
--
ALTER TABLE `shop_limit`
  ADD PRIMARY KEY (`shopId`,`itemId`);

--
-- Indices de la tabla `shop_npc`
--
ALTER TABLE `shop_npc`
  ADD PRIMARY KEY (`npc_id`,`id`);

--
-- Indices de la tabla `skills`
--
ALTER TABLE `skills`
  ADD PRIMARY KEY (`skill_id`);

--
-- Indices de la tabla `skills_handler`
--
ALTER TABLE `skills_handler`
  ADD PRIMARY KEY (`skillId`);

--
-- Indices de la tabla `skills_info`
--
ALTER TABLE `skills_info`
  ADD PRIMARY KEY (`skillId`);

--
-- Indices de la tabla `skills_passive`
--
ALTER TABLE `skills_passive`
  ADD PRIMARY KEY (`passive_id`);

--
-- Indices de la tabla `spawnlist`
--
ALTER TABLE `spawnlist`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_arrow`
--
ALTER TABLE `spawnlist_arrow`
  ADD PRIMARY KEY (`npc_id`,`locx`,`locy`,`mapid`);

--
-- Indices de la tabla `spawnlist_boss`
--
ALTER TABLE `spawnlist_boss`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_boss_sign`
--
ALTER TABLE `spawnlist_boss_sign`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_castledoor`
--
ALTER TABLE `spawnlist_castledoor`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_clandungeon`
--
ALTER TABLE `spawnlist_clandungeon`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_door`
--
ALTER TABLE `spawnlist_door`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_fantasyisland`
--
ALTER TABLE `spawnlist_fantasyisland`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_furniture`
--
ALTER TABLE `spawnlist_furniture`
  ADD PRIMARY KEY (`item_obj_id`);

--
-- Indices de la tabla `spawnlist_hadin`
--
ALTER TABLE `spawnlist_hadin`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_icedungeon`
--
ALTER TABLE `spawnlist_icedungeon`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_indun`
--
ALTER TABLE `spawnlist_indun`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_lastabard`
--
ALTER TABLE `spawnlist_lastabard`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_light`
--
ALTER TABLE `spawnlist_light`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_npc`
--
ALTER TABLE `spawnlist_npc`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_npc_cash_shop`
--
ALTER TABLE `spawnlist_npc_cash_shop`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_npc_shop`
--
ALTER TABLE `spawnlist_npc_shop`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_ruun`
--
ALTER TABLE `spawnlist_ruun`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_trap`
--
ALTER TABLE `spawnlist_trap`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_ub`
--
ALTER TABLE `spawnlist_ub`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_valakas_room`
--
ALTER TABLE `spawnlist_valakas_room`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spawnlist_worldwar`
--
ALTER TABLE `spawnlist_worldwar`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `spell_melt`
--
ALTER TABLE `spell_melt`
  ADD PRIMARY KEY (`skillId`,`passiveId`);

--
-- Indices de la tabla `spr_action`
--
ALTER TABLE `spr_action`
  ADD PRIMARY KEY (`spr_id`,`act_id`);

--
-- Indices de la tabla `spr_info`
--
ALTER TABLE `spr_info`
  ADD PRIMARY KEY (`spr_id`);

--
-- Indices de la tabla `tb_bookquest_compensate`
--
ALTER TABLE `tb_bookquest_compensate`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `tb_lfccompensate`
--
ALTER TABLE `tb_lfccompensate`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `tb_lfctypes`
--
ALTER TABLE `tb_lfctypes`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `tb_monster_book`
--
ALTER TABLE `tb_monster_book`
  ADD PRIMARY KEY (`book_id`);

--
-- Indices de la tabla `tb_monster_book_clearinfo`
--
ALTER TABLE `tb_monster_book_clearinfo`
  ADD PRIMARY KEY (`book_id`);

--
-- Indices de la tabla `tb_user_monster_book`
--
ALTER TABLE `tb_user_monster_book`
  ADD PRIMARY KEY (`char_id`,`book_id`);

--
-- Indices de la tabla `tb_user_week_quest`
--
ALTER TABLE `tb_user_week_quest`
  ADD PRIMARY KEY (`char_id`,`difficulty`,`section`);

--
-- Indices de la tabla `tb_weekquest_compensate`
--
ALTER TABLE `tb_weekquest_compensate`
  ADD PRIMARY KEY (`button_no`);

--
-- Indices de la tabla `tb_weekquest_matrix`
--
ALTER TABLE `tb_weekquest_matrix`
  ADD PRIMARY KEY (`difficulty`);

--
-- Indices de la tabla `tb_weekquest_updateinfo`
--
ALTER TABLE `tb_weekquest_updateinfo`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `tj_coupon`
--
ALTER TABLE `tj_coupon`
  ADD PRIMARY KEY (`objId`);

--
-- Indices de la tabla `town`
--
ALTER TABLE `town`
  ADD PRIMARY KEY (`town_id`);

--
-- Indices de la tabla `town_npc`
--
ALTER TABLE `town_npc`
  ADD PRIMARY KEY (`npc_id`);

--
-- Indices de la tabla `trap`
--
ALTER TABLE `trap`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `ub_rank`
--
ALTER TABLE `ub_rank`
  ADD PRIMARY KEY (`ub_id`,`char_name`);

--
-- Indices de la tabla `ub_settings`
--
ALTER TABLE `ub_settings`
  ADD PRIMARY KEY (`ub_id`);

--
-- Indices de la tabla `util_fighter`
--
ALTER TABLE `util_fighter`
  ADD PRIMARY KEY (`Num`);

--
-- Indices de la tabla `util_racer`
--
ALTER TABLE `util_racer`
  ADD PRIMARY KEY (`Num`);

--
-- Indices de la tabla `war_time`
--
ALTER TABLE `war_time`
  ADD PRIMARY KEY (`castleId`,`day`);

--
-- Indices de la tabla `weapon`
--
ALTER TABLE `weapon`
  ADD PRIMARY KEY (`item_id`);

--
-- Indices de la tabla `weapon_damege`
--
ALTER TABLE `weapon_damege`
  ADD PRIMARY KEY (`item_id`,`name`);

--
-- Indices de la tabla `weapon_skill`
--
ALTER TABLE `weapon_skill`
  ADD PRIMARY KEY (`weapon_id`,`attackType`);

--
-- Indices de la tabla `삭제금지 board_auction`
--
ALTER TABLE `삭제금지 board_auction`
  ADD PRIMARY KEY (`house_id`);

--
-- Indices de la tabla `삭제금지 characters`
--
ALTER TABLE `삭제금지 characters`
  ADD PRIMARY KEY (`objid`),
  ADD KEY `key_id` (`account_name`,`char_name`);

--
-- Indices de la tabla `삭제금지 clan_data`
--
ALTER TABLE `삭제금지 clan_data`
  ADD PRIMARY KEY (`clan_id`);

--
-- Indices de la tabla `삭제금지 house`
--
ALTER TABLE `삭제금지 house`
  ADD PRIMARY KEY (`house_id`);

--
-- Indices de la tabla `삭제금지 town`
--
ALTER TABLE `삭제금지 town`
  ADD PRIMARY KEY (`town_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `0_translations`
--
ALTER TABLE `0_translations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `app_alim_log`
--
ALTER TABLE `app_alim_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `app_item_search`
--
ALTER TABLE `app_item_search`
  MODIFY `seq` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `app_nshop`
--
ALTER TABLE `app_nshop`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `armor_set`
--
ALTER TABLE `armor_set`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `beginner`
--
ALTER TABLE `beginner`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `bots`
--
ALTER TABLE `bots`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `castle_soldier`
--
ALTER TABLE `castle_soldier`
  MODIFY `castle_id` int(2) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `character_buddys`
--
ALTER TABLE `character_buddys`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `character_elf_warehouse`
--
ALTER TABLE `character_elf_warehouse`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `character_exclude`
--
ALTER TABLE `character_exclude`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `character_monsterbooklist`
--
ALTER TABLE `character_monsterbooklist`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `character_package_warehouse`
--
ALTER TABLE `character_package_warehouse`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `character_present_warehouse`
--
ALTER TABLE `character_present_warehouse`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `character_special_warehouse`
--
ALTER TABLE `character_special_warehouse`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `character_teleport`
--
ALTER TABLE `character_teleport`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `character_warehouse`
--
ALTER TABLE `character_warehouse`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clan_bless_buff`
--
ALTER TABLE `clan_bless_buff`
  MODIFY `number` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clan_contribution_buff`
--
ALTER TABLE `clan_contribution_buff`
  MODIFY `clan_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clan_data`
--
ALTER TABLE `clan_data`
  MODIFY `clan_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clan_history`
--
ALTER TABLE `clan_history`
  MODIFY `num` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clan_warehouse`
--
ALTER TABLE `clan_warehouse`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clan_warehouse_list`
--
ALTER TABLE `clan_warehouse_list`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clan_warehouse_log`
--
ALTER TABLE `clan_warehouse_log`
  MODIFY `id` int(1) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `craft_info`
--
ALTER TABLE `craft_info`
  MODIFY `craft_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `craft_npcs`
--
ALTER TABLE `craft_npcs`
  MODIFY `npc_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `etcitem`
--
ALTER TABLE `etcitem`
  MODIFY `item_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `inter_race_region`
--
ALTER TABLE `inter_race_region`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `item_bookmark`
--
ALTER TABLE `item_bookmark`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `levelup_quests_item`
--
ALTER TABLE `levelup_quests_item`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `log_chat`
--
ALTER TABLE `log_chat`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `log_cwarehouse`
--
ALTER TABLE `log_cwarehouse`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `log_enchant`
--
ALTER TABLE `log_enchant`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `log_private_shop`
--
ALTER TABLE `log_private_shop`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `log_shop`
--
ALTER TABLE `log_shop`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `log_warehouse`
--
ALTER TABLE `log_warehouse`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `mobgroup`
--
ALTER TABLE `mobgroup`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `monster_book`
--
ALTER TABLE `monster_book`
  MODIFY `monsternumber` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `npc`
--
ALTER TABLE `npc`
  MODIFY `npcid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `polymorphs`
--
ALTER TABLE `polymorphs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `robot_location`
--
ALTER TABLE `robot_location`
  MODIFY `uid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `robot_location최신`
--
ALTER TABLE `robot_location최신`
  MODIFY `uid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `robot_message`
--
ALTER TABLE `robot_message`
  MODIFY `uid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `robot_name`
--
ALTER TABLE `robot_name`
  MODIFY `uid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `robot_teleport_list`
--
ALTER TABLE `robot_teleport_list`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `server_explain`
--
ALTER TABLE `server_explain`
  MODIFY `num` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `shop_aden`
--
ALTER TABLE `shop_aden`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist`
--
ALTER TABLE `spawnlist`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_arrow`
--
ALTER TABLE `spawnlist_arrow`
  MODIFY `npc_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_boss`
--
ALTER TABLE `spawnlist_boss`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_boss_sign`
--
ALTER TABLE `spawnlist_boss_sign`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_clandungeon`
--
ALTER TABLE `spawnlist_clandungeon`
  MODIFY `id` int(2) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_fantasyisland`
--
ALTER TABLE `spawnlist_fantasyisland`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_hadin`
--
ALTER TABLE `spawnlist_hadin`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_icedungeon`
--
ALTER TABLE `spawnlist_icedungeon`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_indun`
--
ALTER TABLE `spawnlist_indun`
  MODIFY `id` int(3) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_lastabard`
--
ALTER TABLE `spawnlist_lastabard`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_light`
--
ALTER TABLE `spawnlist_light`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_npc`
--
ALTER TABLE `spawnlist_npc`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_npc_cash_shop`
--
ALTER TABLE `spawnlist_npc_cash_shop`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_npc_shop`
--
ALTER TABLE `spawnlist_npc_shop`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_ruun`
--
ALTER TABLE `spawnlist_ruun`
  MODIFY `id` int(3) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_ub`
--
ALTER TABLE `spawnlist_ub`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_valakas_room`
--
ALTER TABLE `spawnlist_valakas_room`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `spawnlist_worldwar`
--
ALTER TABLE `spawnlist_worldwar`
  MODIFY `id` int(3) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tb_lfccompensate`
--
ALTER TABLE `tb_lfccompensate`
  MODIFY `ID` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tb_lfctypes`
--
ALTER TABLE `tb_lfctypes`
  MODIFY `ID` int(2) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `util_fighter`
--
ALTER TABLE `util_fighter`
  MODIFY `Num` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `util_racer`
--
ALTER TABLE `util_racer`
  MODIFY `Num` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `weapon`
--
ALTER TABLE `weapon`
  MODIFY `item_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `weapon_skill`
--
ALTER TABLE `weapon_skill`
  MODIFY `weapon_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `삭제금지 clan_data`
--
ALTER TABLE `삭제금지 clan_data`
  MODIFY `clan_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

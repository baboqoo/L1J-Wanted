DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `notification_id` int(6) NOT NULL DEFAULT 0,
  `notification_type` enum('NORMAL(0)','CHANGE(1)') NOT NULL DEFAULT 'NORMAL(0)',
  `is_use` enum('true','false') NOT NULL DEFAULT 'true',
  `is_hyperlink` enum('true','false') NOT NULL DEFAULT 'false',
  `displaydesc` varchar(50) DEFAULT NULL,
  `displaydesc_kr` varchar(50) DEFAULT NULL,
  `displaydesc_en` varchar(50) NOT NULL,
  `date_type` enum('NONE(0)','CUSTOM(1)','BOSS(2)','DOMINATION_TOWER(3)','COLOSSEUM(4)','TREASURE(5)','FORGOTTEN(6)') NOT NULL DEFAULT 'NONE(0)',
  `date_boss_id` int(10) NOT NULL DEFAULT 0,
  `date_custom_start` datetime DEFAULT NULL,
  `date_custom_end` datetime DEFAULT NULL,
  `teleport_loc` text DEFAULT NULL,
  `rest_gauge_bonus` int(4) NOT NULL DEFAULT 0,
  `is_new` enum('true','false') NOT NULL DEFAULT 'false',
  `animation_type` enum('NO_ANIMATION(0)','ANT_QUEEN(1)','OMAN_MORPH(2)','AI_BATTLE(3)') NOT NULL DEFAULT 'NO_ANIMATION(0)'
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

INSERT INTO `notification` (`notification_id`, `notification_type`, `is_use`, `is_hyperlink`, `displaydesc`, `displaydesc_kr`, `displaydesc_en`, `date_type`, `date_boss_id`, `date_custom_start`, `date_custom_end`, `teleport_loc`, `rest_gauge_bonus`, `is_new`, `animation_type`) VALUES
(1, 'NORMAL(0)', 'false', 'true', '$1506', '에르자베', 'Giant Ant Queen', 'BOSS(2)', 5136, NULL, NULL, '4, 32892, 33225', 300, 'false', 'NO_ANIMATION(0)'),
(2, 'NORMAL(0)', 'true', 'true', '$11993', '샌드 웜', 'Sand Worm', 'BOSS(2)', 5135, NULL, NULL, '4, 32732, 33141', 300, 'false', 'NO_ANIMATION(0)'),
(3, 'NORMAL(0)', 'false', 'false', '$14740', '붉은 기사단의 진격', 'The Crimson Knights are advancing.', 'NONE(0)', 0, NULL, NULL, '4, 32595, 33167', 0, 'false', 'NO_ANIMATION(0)'),
(4, 'NORMAL(0)', 'false', 'false', '$42714', '고대 물품 상인', 'Ancient item merchant', 'NONE(0)', 0, NULL, NULL, '4, 32620, 32789', 0, 'false', 'NO_ANIMATION(0)'),
(5, 'NORMAL(0)', 'true', 'false', '$23228', '잊혀진 섬', 'Forgotten Island', 'FORGOTTEN(6)', 0, NULL, NULL, '4, 33431, 33499', 0, 'false', 'NO_ANIMATION(0)'),
(16, 'NORMAL(0)', 'true', 'false', '$29760', '지배의 탑', 'Tower of Domination', 'DOMINATION_TOWER(3)', 0, NULL, NULL, '4, 33930, 33348', 0, 'false', 'NO_ANIMATION(0)'),
(18, 'NORMAL(0)', 'true', 'false', '$42716', '인형 경주', 'Doll race', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)'),
(19, 'NORMAL(0)', 'true', 'false', '$42717', '서버 상점', 'Server shop', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)'),
(20, 'NORMAL(0)', 'true', 'false', '$30708', '무한대전', 'Infinity War', 'COLOSSEUM(4)', 0, NULL, NULL, '4, 33498, 32768', 100, 'false', 'NO_ANIMATION(0)'),
(21, 'NORMAL(0)', 'false', 'false', '$31212', '비밀 상인', 'Secret Merchant Appears', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)'),
(22, 'NORMAL(0)', 'false', 'false', '$31946', '격전의 콜로세움', 'Coliseum Battle', 'COLOSSEUM(4)', 0, NULL, NULL, '4, 33498, 32768', 200, 'false', 'NO_ANIMATION(0)'),
(23, 'CHANGE(1)', 'true', 'false', '$32010', '여왕개미의 은신처', 'Ant Queen\'s Hideout', 'NONE(0)', 0, NULL, NULL, '4, 32854, 33267', 0, 'false', 'ANT_QUEEN(1)'),
(24, 'NORMAL(0)', 'false', 'false', '$13911', '숨겨진 사냥터', 'Hidden Hunting Grounds', 'CUSTOM(1)', 0, '2022-09-25 06:00:00', '2022-09-30 06:00:00', NULL, 0, 'true', 'NO_ANIMATION(0)'),
(25, 'NORMAL(0)', 'false', 'false', '$42718', '혈맹 상점', 'Clan shop', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)'),
(26, 'CHANGE(1)', 'true', 'false', '$33543', '균열의 오만의 탑', 'Cracks on TOI %dF', 'NONE(0)', 0, NULL, NULL, '4, 33930, 33348', 0, 'false', 'OMAN_MORPH(2)'),
(30, 'NORMAL(0)', 'true', 'false', '$36722', '만월의 보물섬', 'Full Moon Treasure Island', 'TREASURE(5)', 0, NULL, NULL, '4, 33614, 33247', 300, 'false', 'NO_ANIMATION(0)'),
(33, 'CHANGE(1)', 'true', 'false', '$35454', '하이네 점령전', '[Lv85~]Heine Conquest Battle', 'NONE(0)', 0, NULL, NULL, '4, 33431, 33500', 0, 'false', 'NO_ANIMATION(0)'),
(35, 'CHANGE(1)', 'true', 'false', '$36529', '윈다우드 점령전', '[Lv85~] Windawood Conquest War', 'NONE(0)', 0, NULL, NULL, '4, 32585, 33438', 0, 'false', 'NO_ANIMATION(0)'),
(100, 'CHANGE(1)', 'true', 'false', '$36108', '거울 전쟁', 'Mirror War', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'AI_BATTLE(3)'),
(101, 'NORMAL(0)', 'true', 'false', '$36348', '용맹의 메달', 'Medal of Valor', 'NONE(0)', 0, NULL, NULL, NULL, 0, 'false', 'NO_ANIMATION(0)');

ALTER TABLE `notification`
  ADD PRIMARY KEY (`notification_id`);
COMMIT;


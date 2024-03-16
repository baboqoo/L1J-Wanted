delete from `npcaction` WHERE (`npcaction`.`npcid` = 70012) OR (`npcaction`.`npcid` = 70019) OR (`npcaction`.`npcid` = 70031) OR (`npcaction`.`npcid` = 70054) OR (`npcaction`.`npcid` = 70065) OR (`npcaction`.`npcid` = 70070) OR (`npcaction`.`npcid` = 70075) OR (`npcaction`.`npcid` = 70084) OR (`npcaction`.`npcid` = 70101);

INSERT INTO `npcaction` (`npcid`, `normal_action`, `caotic_action`, `teleport_url`, `teleport_urla`) VALUES
(70012, 'selena', 'selena1', '', ''),
(70019, 'loria', 'loria1', '', ''),
(70031, 'molly', 'molly1', '', ''),
(70054, 'sabin', 'sabin1', '', ''),
(70065, 'enke', 'enke1', '', ''),
(70070, 'velisa', 'velisa1', '', ''),
(70075, 'mirand', 'mirand1', '', ''),
(70084, 'elly', 'elly1', '', ''),
(70101, 'shabin', 'shabin1', '', '');


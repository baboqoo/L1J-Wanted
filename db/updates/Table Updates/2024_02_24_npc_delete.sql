-- Delete the spawn of the npc "Assassin Master Follower": 70824
-- delete from `npc` where npcid = 70824;
delete from `npcaction` where npcid = 70824;
delete from `spawnlist_npc` where npc_templateid = 70824;

-- Delete the spawn of the npc "Ndia": 71094
-- delete from `npc` where npcid = 71094;
delete from `npcaction` where npcid = 71094;
delete from `spawnlist` where npc_templateid = 71094;

-- Delete the spawn of the npc "Flute Crafter^Narhen": 70837
delete from `npcaction` where npcid = 70837;
delete from `spawnlist_npc` where npc_templateid = 70837;

-- Delete the name showed in game for the npc "Templar" located in the Training Camp: 202100 with desc_id = $19274 (Templar)
update `npc` set desc_id = null where npcid = 202100;

-- Update Serene Templar Store to add Templar Arrows
UPDATE `shop` SET `order_id` = '28' WHERE `shop`.`npc_id` = 202056 AND `shop`.`item_id` = 32088 AND `shop`.`order_id` = 27;
DELETE FROM  `shop` WHERE `npc_id` = '202056' and `item_id` = '30088' and `order_id` '27';
INSERT INTO `shop` (`npc_id`, `item_id`, `order_id`, `selling_price`, `pack_count`, `purchasing_price`, `enchant`, `pledge_rank`, `note`) VALUES ('202056', '30088', '27', '38', '10', '-1', '0', 'NONE(None)', 0xb1e2bbe7b4dcc0c720c0ba20c8adbbec);

-- Add Hunter arrows to General Stores
insert into shop (npc_id, item_id, order_id, selling_price, pack_count, purchasing_price, enchant, pledge_rank, note)
SELECT npc_id, 31176, order_id-1, 24, 10, -1, 0, 'NONE(None)', 0xbbe7b3c9b2dbc0c720c0ba20c8adbbec FROM `shop` s1 
WHERE item_id = 31175 and not exists (select null from `shop` s2 where s2.npc_id = s1.npc_id and item_id = 31176); 

-- Update the dialog for Ruth Merchang becouse is ussing an old version
UPDATE `npcaction` SET `caotic_action` = 'rluth', `normal_action` = 'rluth', `teleport_url` = 'rluth3' WHERE `npcaction`.`npcid` = 70021;

-- delete the spawn of Kelton and Charlie (NPC to teleport to Land of Abandoned (INTER)): 146068 146060
delete from `spawnlist_npc` where npc_templateid = 146068;
delete from `spawnlist_npc` where npc_templateid = 146060;

-- delete the spawn of the npc Dilong : 50014
delete from `npcaction` where npcid = 50014;
delete from `spawnlist_npc` where npc_templateid = 50014;

-- delete the spawn of the npc Vandriss : 80128
delete from `npcaction` where npcid = 80128;
delete from `spawnlist_npc` where npc_templateid = 80128;

-- delete the spawn of the npc "Colosseum Caretaker" : 50028, 50041, 50061
delete from `npcaction` where npcid in (50028, 50041, 50061);
delete from `spawnlist_npc` where npc_templateid in (50028, 50041, 50061);

-- delete the spawn of the npc "Colosseum Assistant Manager" : 50029, 50042, 50062
delete from `npcaction` where npcid in (50029, 50042, 50062);
delete from `spawnlist_npc` where npc_templateid in (50029, 50042, 50062);

-- delete the spawn of the npc Mark : 70775
delete from `npcaction` where npcid = 70775;
delete from `spawnlist_npc` where npc_templateid = 70775;

-- delete the spawn of the npc "Goddess of Atonement": 70801, 70611, 70781, 70530, 70823, 70658
delete from `npcaction` where npcid in (70801, 70611, 70781, 70530, 70823, 70658);
delete from `spawnlist_npc` where npc_templateid in (70801, 70611, 70781, 70530, 70823, 70658);

-- modify the item "bag of iris" to be stackable 
UPDATE `etcitem` SET `merge` = 'true' WHERE `etcitem`.`item_id` = 41008;

-- delete the description of the animals in towns
-- Pig (70983)        : $927
-- Duck (45015, 70982): $952
-- Hen (70981)        : $928
-- Milk Cow (70984)   : $929
UPDATE `npc` SET `desc_id` = '' WHERE `npc`.`npcid` in (70983, 45015, 70982, 70981, 70984);

-- update droplist to write the empty field itemname_en
update droplist d set itemname_en = (SELECT desc_en from etcitem e where e.item_Id = d.itemId) where itemname_en = '';
update droplist d set itemname_en = (SELECT desc_en from armor e where e.item_Id = d.itemId) where itemname_en = '';
update droplist d set itemname_en = (SELECT desc_en from weapon e where e.item_Id = d.itemId) where itemname_en = '';
update droplist d set itemname_en = (select text_english from 0_translations t where t.text_korean = itemname_kr) where itemname_en = '' and (select text_english from 0_translations t where t.text_korean = itemname_kr) <> '';
delete from droplist where itemname_en = '';

-- delete old enchant of scroll armor / weapon 
--(30027, 4288, '수련자의 갑옷 마법 주문서', 'Trainee Armor Magic Scroll', '$8430', 'NORMAL', 'SCROLL', 'ZEL', 'PAPER(종이)', 1, 8606, 18149, 'true', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 79, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'false', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, NULL, 0, 'NONE', 'NONE', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'none', 0, 'BEGIN_ZONE', 0),
--(30028, 4289, '수련자의 무기 마법 주문서', 'Trainee Weapon Magic Scroll', '$8429', 'NORMAL', 'SCROLL', 'DAI', 'PAPER(종이)', 1, 8615, 18149, 'true', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 79, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'false', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, NULL, 0, 'NONE', 'NONE', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'none', 0, 'BEGIN_ZONE', 0);

delete from droplist where itemid in (30027, 30028);
delete from etcitem  where item_id in (30027, 30028);

-- also a bag that contain these items
--(30035, 13872, '교관의 선물 주머니', 'Instructor\'s Gift Bag', '$14171', 'NORMAL', 'TREASURE_BOX', 'NORMAL', 'NONE(-)', 10, 957, 18153, 'false', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'false', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 3, 1000, 0, 0, 1, NULL, 0, 'NONE', 'NONE', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'none', 0, 'NONE', 0),
--(30036, 13873, '교관의 선물 주머니', 'Instructor\'s Gift Bag', '$14171', 'NORMAL', 'TREASURE_BOX', 'NORMAL', 'NONE(-)', 10, 957, 18153, 'false', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'false', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 3, 1000, 0, 0, 1, NULL, 0, 'NONE', 'NONE', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'none', 0, 'NONE', 0),
--(30038, 13875, '교관의 선물 주머니', 'Instructor\'s Gift Bag', '$14171', 'NORMAL', 'TREASURE_BOX', 'NORMAL', 'NONE(-)', 10, 957, 18153, 'false', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'false', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 3, 1000, 0, 0, 1, NULL, 0, 'NONE', 'NONE', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'none', 0, 'NONE', 0);

delete from etcitem  where item_id in (30035, 30036, 30038);

-- Add miss teleport to the Dark Dragon Dungeon
DELETE FROM `dungeon` where src_mapid in (1318, 1319) or new_mapid in (1318, 1319);
INSERT INTO `dungeon` (`src_x`, `src_y`, `src_mapid`, `new_x`, `new_y`, `new_mapid`, `new_heading`, `min_level`, `max_level`, `note`) VALUES
(32739, 32868, 1319, 32738, 32920, 1318, 6, 0, 0, 'Dark Dragon\'s Dungeon 2F -> 1F (Left)'),
(32741, 32921, 1318, 32739, 32872, 1319, 4, 0, 0, 'Dark Dragon\'s Dungeon 1F -> 2F (Left)'),
(32855, 32926, 1318, 32856, 32991, 1319, 6, 0, 0, 'Dark Dragon\'s Dungeon 1F -> 2F (Right)'),
(32855, 32987, 1319, 32852, 32925, 1318, 4, 0, 0, 'Dark Dragon\'s Dungeon 2F -> 1F (Right)');


-- Add teleport LOC to Mirror Wars
UPDATE `notification` SET `teleport_loc` = '4, 33430, 32825' WHERE `notification`.`notification_id` = 100;
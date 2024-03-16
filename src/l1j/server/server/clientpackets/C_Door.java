package l1j.server.server.clientpackets;

import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.CommonUtil;

public class C_Door extends ClientBasePacket {
	private static final String C_DOOR = "[C] C_Door";
	
	private L1PcInstance pc;
	private L1DoorInstance door;

	public C_Door(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int locX		= readH();
		int locY		= readH();
		int objectId	= readD();
		L1Object obj	= L1World.getInstance().findObject(objectId);
		if (obj == null || obj instanceof L1DoorInstance == false) {
			return;
		}
		door = (L1DoorInstance) obj;
		if (locX > pc.getX() + 1 || locX < pc.getX() - 1 || locY > pc.getY() + 1 || locY < pc.getY() - 1) {
			return;
		}
		int doorId		= door.getDoorId();
		int npcId		= door.getNpcId();
		int spriteId	= door.getSpriteId();
		if (npcId >= 5147 && npcId <= 5151 || npcId >= 14911 && npcId <= 14912) {
			return;
		}
		if (pc.getMap().getBaseMapId() == 736 && door != null && door.getOpenStatus() == ActionCodes.ACTION_Open 
				&& (spriteId == 20426 || spriteId == 20430 || spriteId == 20344 || spriteId == 20348)) {
			aurakiaStone();
			return;
		}

		if (door != null && !isExistKeeper(door.getKeeperId())) {
			if (doorId == 113 && !pc.getInventory().consumeItem(L1ItemId.BAPOMET_GOLD_KEY, 1)) {// 바포방 정문 금빛 열쇠
				return;
			}
			if (doorId >= 8001 && doorId <= 8010) {
				if (pc.getInventory().checkItem(L1ItemId.GIRANCAVE_BOXKEY, 1)) {
					giranCaveBox();
				}
				return;
			}
			if (npcId >= 900151 && npcId <= 900154) {// 하딘 문
				return;
			}
			if (npcId >= 7210013 && npcId <= 7210015) {// 화룡의 안식처
				return;
			}
			if (doorId >= 6050 && doorId <= 6059) {
				whaleTreasureBox();
				return;
			}
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
				if (npcId >= 7210016 && npcId <= 7210019) {
					return;
				}
				door.close();
			} else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
				door.open();
				if (npcId >= 7210016 && npcId <= 7210019) {
					phoenixEgg();
				}
			}
		}
	}
	
	/**
	 * 굶주린 고래상어 보물방 상자
	 */
	void whaleTreasureBox() {
		if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
			return;
		}
		door.open();
		int prob = CommonUtil.random(1000) + 1;
		int itemId = 0, itemCount = 1;
		if (prob >= 999) {
			itemId = 203021;// 고대 신의 도끼
		} else if (prob >= 997) {
			itemId = 900019;// 실프의 티셔츠
		} else if (prob >= 995) {
			itemId = 40362;// 파푸리온의 숨결
		} else if (prob >= 993) {
			itemId = 40222;// 마법서 (디스인티그레이트)
		} else if (prob >= 990) {
			itemId = 210131;// 전사의 인장 (타이탄: 블릿)
		} else if (prob >= 980) {
			itemId = 37220;// 전사의 인장 (버서크) (소생)
		} else if (prob >= 970) {
			itemId = 37004;// 전사의 인장 (데스페라도) (소생)
		} else if (prob >= 800) {
			itemId = 820018;// 퓨어 엘릭서
		} else if (prob >= 500) {
			itemId = L1ItemId.BRAVE_COIN;// 용맹의 메달
			itemCount = 5;
		} else {
			itemId = 3000028;// 할파스의 집념
			itemCount = 5;
		}
		if (door.getDoorId() == 6050) {
			createItem(31480, 1);// 잭 선장의 수경
		}
		createItem(itemId, itemCount);
	}
	
	/**
	 * 아우라키아의 정화 석상
	 */
	void aurakiaStone(){
		if (pc.isArrowOfAurakiaSkill) {
			return;
		}
		pc.isArrowOfAurakiaSkill = true;
		door.close();
		pc.sendPackets(S_AvailableSpellNoti.ARROW_OF_AURAKIA_ON);// 스킬 습득
	}

	/**
	 * 기란감옥 보물 상자
	 */
	void giranCaveBox() {
		if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
			pc.getInventory().consumeItem(L1ItemId.GIRANCAVE_BOXKEY, 1);
			door.open();
			int ran = CommonUtil.random(100) + 1;
			int itemId = 0, itemCount = 0;
			if (ran >= 0 && ran <= 15) {
				itemId = L1ItemId.ADENA;
				itemCount = 10000;
			} else if (ran >= 16 && ran <= 29) {
				itemId = L1ItemId.ADENA;
				itemCount = 20000;
			} else if (ran >= 30 && ran <= 49) {
				itemId = L1ItemId.ADENA;
				itemCount = 30000;
			} else if (ran >= 50 && ran <= 59) {
				itemId = L1ItemId.ADENA;
				itemCount = 50000;
			} else if (ran >= 60 && ran <= 64) {
				itemId = L1ItemId.ADENA;
				itemCount = 100000;
			} else if (ran >= 65 && ran <= 69) {
				itemId = L1ItemId.ADENA;
				itemCount = 200000;
			} else if (ran >= 70 && ran <= 72) {
				itemId = L1ItemId.ADENA;
				itemCount = 300000;
			} else if (ran >= 73 && ran <= 75) {
				itemId = L1ItemId.ADENA;
				itemCount = 400000;
			} else if (ran >= 76 && ran <= 78) {
				itemId = L1ItemId.ADENA;
				itemCount = 500000;
			} else if (ran >= 79 && ran <= 80) {
				itemId = L1ItemId.ADENA;
				itemCount = 1000000;
			} else if (ran >= 81 && ran <= 90) {
				itemId = L1ItemId.SCROLL_OF_ENCHANT_ARMOR;
				itemCount = 3;
			} else if (ran >= 91 && ran <= 100) {
				itemId = L1ItemId.SCROLL_OF_ENCHANT_WEAPON;
				itemCount = 3;
			}
			createItem(itemId, itemCount);
		}
	}

	/**
	 * 화룡의 안식처 알
	 */
	void phoenixEgg() {
		int ran = CommonUtil.random(100) + 1;
		int itemId = 0, itemCount = 0;
		if (ran >= 0 && ran <= 10) {
			itemId = L1ItemId.POTION_OF_HEALING;
			itemCount = 5;
		} else if (ran >= 11 && ran <= 20) {
			itemId = L1ItemId.POTION_OF_HEALING;
			itemCount = 10;
		} else if (ran >= 21 && ran <= 30) {
			itemId = L1ItemId.POTION_OF_HEALING;
			itemCount = 15;
		} else if (ran >= 31 && ran <= 40) {
			itemId = L1ItemId.POTION_OF_HEALING;
			itemCount = 30;
		} else if (ran >= 41 && ran <= 50) {
			itemId = L1ItemId.POTION_OF_HEALING;
			itemCount = 50;
		} else if (ran >= 51 && ran <= 60) {
			itemId = L1ItemId.POTION_OF_GREATER_HEALING;
			itemCount = 5;
		} else if (ran >= 61 && ran <= 70) {
			itemId = L1ItemId.POTION_OF_GREATER_HEALING;
			itemCount = 10;
		} else if (ran >= 71 && ran <= 80) {
			itemId = L1ItemId.POTION_OF_GREATER_HEALING;
			itemCount = 15;
		} else if (ran >= 81 && ran <= 90) {
			itemId = L1ItemId.POTION_OF_GREATER_HEALING;
			itemCount = 30;
		} else if (ran >= 91 && ran <= 100) {
			itemId = L1ItemId.POTION_OF_GREATER_HEALING;
			itemCount = 50;
		}
		createItem(itemId, itemCount);
	}

	boolean isExistKeeper(int keeperId) {
		if (keeperId == 0) {
			return false;
		}
		L1Clan clan = pc.getClan();
		if (clan == null) {
			return true;
		}
		int houseId = clan.getHouseId();
		if (houseId == 0) {
			return true;
		}
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		if (house != null && keeperId == house.getKeeperId()) {
			return false;
		}
		return true;
	}
	
	void createItem(int itemId, int itemCount){
		if (itemId == 0 || itemCount == 0) {
			return;
		}
		if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(itemId), itemCount) != L1Inventory.OK) return;
		L1ItemInstance item = pc.getInventory().storeItem(itemId, itemCount);
		//pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getDescKr(), itemCount)), true);
		pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getDesc(), itemCount)), true);
	}

	@Override
	public String getType() {
		return C_DOOR;
	}
}


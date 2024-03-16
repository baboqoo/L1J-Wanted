package l1j.server.server.monitor;

import java.io.IOException;
import java.sql.Timestamp;

import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyExpObject;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyItemObject;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.templates.L1UserRanking;

public interface Logger {
	public enum ChatType {// Trade 장사채팅 로그 기록 남기지 않게 변경
		Normal, Global, Clan, Alliance, Guardian, Party, Group, Shouting
	};

	public enum ItemActionType {// AutoLoot 오토루팅 로그 기록 남기지 않게 변경
		Pickup, Drop, Delete ,del, del1
	};

	public enum WarehouseType {
		Private, Clan, Package, Elf, Char
	};
	
	public enum ShopLogType {
		BUY, SELL, PLEDGE_BUY
	}
	
	public enum ConnectType {
		INFO, LOGIN, CREATE, PROCESS
	}
	
	public enum FavorType {
		OFFER, POLL, DELETE
	}
	
	public enum TimeCollectionType {
		REGIST, RESET
	}
	
	public enum SmeltingMakeType {
		SUCCESS, FAILURE
	}
	
	public enum SmeltingSlotType {
		INSERT, EJECT
	}
	
	public enum PotentialType {
		RIGHT, DENALS
	}
	
	public enum WebActionType {
		GOODS_BUY, COUPON_USE, NCOIN_GIFT
	}
	
	public enum DeathPenaltyType {
		LOST, RECOVERY
	}

	public void addChat(ChatType type, L1PcInstance pc, String msg);
	public void addWhisper(L1PcInstance pcfrom, L1PcInstance pcto, String msg);
	public void addCommand(String msg);
	public void addConnection(String msg);
	public void addWarehouse(WarehouseType type, boolean put, L1PcInstance pc, L1ItemInstance item, int count);
	public void addTrade(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count);
	public void addPrivateShop(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count);
	public void addEnchant(L1PcInstance pc, L1ItemInstance item, boolean success);
	public void addCraft(L1PcInstance pc, String message);
	public void addAlchemy(L1PcInstance pc, int number, boolean success, L1ItemInstance item);
	public void addPotential(PotentialType type, L1PcInstance pc, int bonus_grade, int bonus_id, boolean isChange, L1ItemInstance item);
	public void addEinState(L1PcInstance pc, boolean success, int getState, int usePoint);
	public void addItemAction(ItemActionType type, L1PcInstance pc, L1ItemInstance item, int count);
	public void addLevel(L1PcInstance pc, int level);
	public void addShop(ShopLogType type, L1PcInstance pc, int npcId, L1ItemInstance item, int count, int totalPrice);
	public void addRank(java.util.LinkedList<L1UserRanking> list);
	public void addTjCoupon(L1PcInstance pc, L1ItemInstance item);
	public void addDeathPenaltyExp(DeathPenaltyType type, L1PcInstance pc, DeathPenaltyExpObject obj, int recovery_item_id);
	public void addDeathPenaltyItem(DeathPenaltyType type, L1PcInstance pc, DeathPenaltyItemObject obj);
	public void addWebserver(String msg);
	public void addWebAction(WebActionType type, String account_name, String character_name, String msg);
	public void addConnector(ConnectType type, String account, String password, String result);
	public void addFavorBook(FavorType type, L1PcInstance pc, L1FavorBookUserObject user);
	public void addTimeCollection(TimeCollectionType type, L1PcInstance pc, L1TimeCollectionUser user, L1ItemInstance item);
	public void addSmeltingMake(SmeltingMakeType type, L1PcInstance pc, int alchemy_id, L1ItemInstance stone);
	public void addSmeltingSlot(SmeltingSlotType type, L1PcInstance pc, L1ItemInstance targetItem, L1ItemInstance stone);
	public void addEinhasadFaith(L1PcInstance pc, int groupId, int indexId, Timestamp expiredTime);
	public void addPCMasterGoldenBuffEnforce(L1PcInstance pc, int index, int group, int bonus, int grade, int cost, int remainTime);
	public void addAll(String msg);
	public void flush() throws IOException;
}


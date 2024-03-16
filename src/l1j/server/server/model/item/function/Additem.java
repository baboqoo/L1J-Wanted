package l1j.server.server.model.item.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1CharacterInfo.L1Class;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessegeNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class Additem {

	public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {
		if(!isInventoryCheck(pc, 5, 98))return;
		switch (itemId) {
		case 2300080:
			HeroRelicBox(pc, l1iteminstance);
			break;
		case 1000010:
			beginnerBox(pc);
			break;
		case 60411:
			if (!crownReward(pc, l1iteminstance)) {
				return;
			}
			break;
		}
		pc.getInventory().consumeItem(itemId, 1); // 삭제되는 아이템과 수량
	}
	
	private static boolean isInventoryCheck(L1PcInstance pc, int minusSize, int weightPercent){
		if (pc.getInventory().getSize() > L1PcInventory.MAX_SIZE - minusSize || pc.getInventory().getWeightPercent() > weightPercent) {
			pc.sendPackets(L1ServerMessage.sm82); // 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return false;
		}
		return true;
	}
	
	/*private static final int[] 전설ITEM = { 5603, 41148, 40282, 40222, 5559, 5606, 210125, 5607 };
	private static final int[] 영웅ITEM = { 40354, 40362, 40370, 40346, 40466, 900019, 202811 };
	private static final int[] 소생ITEM = { 37057, 37010, 37009, 37011, 37012, 37004, 37001, 37000, 37003, 37103, 37104, 37105, 37102, 37091 };
	private static final int[] 일반ITEM = { 830001, 830002, 830003, 830004, 830005, 830006, 830007, 830008, 830009, 830010, 2300081 };*/
	private static final int[] LEGENDARY_ITEMS = { 5603, 41148, 40282, 40222, 5559, 5606, 210125, 5607 };
	private static final int[] HERO_ITEMS = { 40354, 40362, 40370, 40346, 40466, 900019, 202811 };
	private static final int[] RESURRECTION_ITEMS = { 37057, 37010, 37009, 37011, 37012, 37004, 37001, 37000, 37003, 37103, 37104, 37105, 37102, 37091 };
	private static final int[] NORMAL_ITEMS = { 830001, 830002, 830003, 830004, 830005, 830006, 830007, 830008, 830009, 830010, 2300081 };
	
	//private static void 영웅의유물상자(L1PcInstance pc, L1ItemInstance useItem) {
	private static void HeroRelicBox(L1PcInstance pc, L1ItemInstance useItem) {				
		//int 랜덤 = CommonUtil.random(400000) + 1;
		int _rand = CommonUtil.random(400000) + 1;
		boolean worldMent = false;
		L1ItemInstance item = null;
		try {
			if (_rand >= 1 && _rand <= 10){
				item = pc.getInventory().storeItem(LEGENDARY_ITEMS[CommonUtil.random(LEGENDARY_ITEMS.length)], 1);
				worldMent = true;
			} else if (_rand >= 11 && _rand <= 50){
				item = pc.getInventory().storeItem(HERO_ITEMS[CommonUtil.random(HERO_ITEMS.length)], 1);
				worldMent = true;
			} else if (_rand >= 51 && _rand <= 100){
				item = pc.getInventory().storeItem(RESURRECTION_ITEMS[CommonUtil.random(RESURRECTION_ITEMS.length)], 1);
				worldMent = true;
			} else if (_rand >= 101 && _rand <= 220000){
				item = pc.getInventory().storeItem(NORMAL_ITEMS[CommonUtil.random(NORMAL_ITEMS.length)], 3);
			}
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);// 아이템을 획득 멘트
				if (worldMent) {
					if (pc.getConfig().isGlobalMessege()) {
						pc.sendPackets(new S_MessegeNoti(4509, useItem.getItem().getDesc(), item.getItem().getDesc(), item.getItem().getItemNameId()), true);
					}
					L1World.getInstance().broadcastPacket(pc, new S_MessegeNoti(4509, useItem.getItem().getDesc(), item.getItem().getDesc(), item.getItem().getItemNameId()), true);
				}
			}
			item = pc.getInventory().storeItem(2300072, 3);
			pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);// 아이템을 획득 멘트
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final ConcurrentHashMap<Integer, ArrayList<BeginnerBox>> BEGINNER_BOX_LIST = new ConcurrentHashMap<>();
	private static class BeginnerBox {
		L1Item temp;
		int count, enchant, bless;
	}
	private static void beginnerBox(L1PcInstance pc) {
		ArrayList<BeginnerBox> list = BEGINNER_BOX_LIST.get(pc.getType());
		if (list == null) {
			list					= new ArrayList<>();
			Connection con			= null;
			PreparedStatement pstm	= null;
			ResultSet rs			= null;
			BeginnerBox box			= null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM beginner_box WHERE activate IN (?,?)");
				pstm.setString(1, "all");
				pstm.setString(2, L1Class.getName(pc.getType()));
				rs = pstm.executeQuery();
				while(rs.next()){
					int itemid	= rs.getInt("itemid");
					int count	= rs.getInt("count");
					int enchant	= rs.getInt("enchantlvl");
					int bless	= rs.getInt("bless");
					L1Item temp	= ItemTable.getInstance().getTemplate(itemid);
					if (temp == null) {
						continue;
					}
					box = new BeginnerBox();
					box.temp	= temp;
					box.count	= count;
					box.enchant	= enchant;
					box.bless	= bless;
					list.add(box);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
				BEGINNER_BOX_LIST.put(pc.getType(), list);
			}
		}
		
		if (list.isEmpty()) {
			return;
		}
		
		L1ItemInstance item = null;
		for (BeginnerBox box : list) {
			if (!box.temp.isMerge()) {
				for (int i = 0; i < box.count; i++) {
					item = ItemTable.getInstance().createItem(box.temp);
					item.setEnchantLevel(box.enchant);	
					item.setIdentified(true);
					if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
						item.setBless(box.bless);
						pc.getInventory().storeItem(item);
					} else {
						pc.sendPackets(L1ServerMessage.sm82); 
						return;
					}
				}
			} else {
				item = ItemTable.getInstance().createItem(box.temp);
				item.setCount(box.count);
				item.setIdentified(true); 
				if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
					item.setBless(box.bless);
					pc.getInventory().storeItem(item);
				} else {
					pc.sendPackets(L1ServerMessage.sm82); 
					return;
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private static boolean crownReward(L1PcInstance pc, L1ItemInstance useItem) {
		if (!isInventoryCheck(pc, 2, 98)) {
			return false;
		}
		Calendar rightNow = Calendar.getInstance();
		Timestamp lastUsed = useItem.getLastUsed();
		if (lastUsed == null || rightNow.getTimeInMillis() > lastUsed.getTime() + (60 * 60000)) {
			useItem.setLastUsed(new Timestamp(rightNow.getTimeInMillis()));
			int count = 50 + CommonUtil.random(50);
			L1ItemInstance item = pc.getInventory().storeItem(60412, count);
			pc.sendPackets(new S_ServerMessage(403, item.getItem().getDesc() + " ("+ count + ")"), true);
			return true;
		} else {
			long i = (lastUsed.getTime() + (60 * 60000)) - rightNow.getTimeInMillis();
			Calendar cal = (Calendar) rightNow.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			//pc.sendPackets(new S_SystemMessage(i / 60000 + "분 동안(" + cal.getTime().getHours() + StringUtil.ColonString + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
			//It cannot be used for %d minutes (until %d%s%d).
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(111), String.valueOf(i / 60000), String.valueOf(cal.getTime().getHours()), StringUtil.ColonString, String.valueOf(cal.getTime().getMinutes())), true);
			return false;
		}
	}
}


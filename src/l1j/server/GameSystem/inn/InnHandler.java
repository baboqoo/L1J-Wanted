package l1j.server.GameSystem.inn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.astar.World;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

/**
 * 여관 관리 핸들러
 * @author LinOffice
 */
public class InnHandler {
	private static Logger _log								= Logger.getLogger(InnHandler.class.getName());
	private static final Map<Integer, Boolean> INN			= new ConcurrentHashMap<Integer, Boolean>(0);
	private static final Map<Integer, InnTimer> INN_TIMER	= new ConcurrentHashMap<Integer, InnTimer>(0);
	
	private static final String[] RESULT_ACTIONS = {
		/* 
		"inn6",// 빈방이 없습니다
		"inn16",// 빈홀이 없습니다
		"inn3",// 아데나가 부족합니다
		"inn4"// 같이 쓰실 분에게 나누어 주세요
		*/
		"6",
		"16",
		"3",
		"4"
	};
	
	private static class newInstance {
		public static final InnHandler INSTANCE = new InnHandler();
	}
	public static InnHandler getInstance() {
		return newInstance.INSTANCE;
	}
	private InnHandler(){
	}

	public InnTimer getInnTimer(int id) {
		synchronized (INN_TIMER) {
			return (INN_TIMER.containsKey(id)) ? INN_TIMER.get(id) : null;
		}
	}

	public void setInnTimer(int id, InnTimer timer) {
		synchronized (INN_TIMER) {
			if (timer == null) {
				if (INN_TIMER.containsKey(id)) {
					INN_TIMER.remove(id);
				}
			} else {
				INN_TIMER.put(id, timer);
			}
		}
	}

	public boolean getINN(int id) {
		synchronized (INN) {
			return (INN.containsKey(id)) ? INN.get(id) : false;
		}
	}

	public void setINN(int id, boolean flag) {
		synchronized (INN) {
			INN.put(id, flag);
		}
	}

	public synchronized void INNStart(L1PcInstance pc, int objid, int npcid, InnType type, int count) {
		synchronized (INN) {
			L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(npcid);
			String htmlid = "inn";
			if (talking != null)
				htmlid = talking.getNormalAction();
			
			InnHelper helper = InnHelper.getHelper(npcid);
			if (helper == null) {
				return;
			}
			int statMapId = type == InnType.ROOM ? helper.getRoomIds()[0] : helper.getHallIds()[0];
			if (statMapId == 0) {
				return;
			}
			boolean checkmap = false;
			int cnt = 0;
			for (int i = 1; i <= 100; i++) {
				checkmap = (INN.containsKey(statMapId + i)) ? INN.get(statMapId + i) : false;
				if (checkmap == false) {// 비어잇음
					cnt = i;
					break;
				}
			}
			if (checkmap == true || cnt == 0) {// 전체사용중
				String html_dialog = type == InnType.ROOM ? htmlid + RESULT_ACTIONS[0] : htmlid + RESULT_ACTIONS[1];
				if (pc.isGm())
					pc.sendPackets(new S_SystemMessage("Dialog " + html_dialog), true);								
				pc.sendPackets(new S_NPCTalkReturn(objid, html_dialog), true);// 빈방이 없습니다
				return;
			}
			// 빈방있음
			int price = 0;
			if (type == InnType.ROOM) {
				price = count * 300;
				if (pc.getInventory().countItems(L1ItemId.ADENA) < price) {
					if (pc.isGm())
						pc.sendPackets(new S_SystemMessage("Dialog " + htmlid + RESULT_ACTIONS[2]), true);								
					pc.sendPackets(new S_NPCTalkReturn(objid, htmlid + RESULT_ACTIONS[2]), true);// 아데나가 부족합니다
					return;
				}
			} else {
				price = count * 600;
				if (pc.getInventory().countItems(L1ItemId.ADENA) < price) {
					if (pc.isGm())
						pc.sendPackets(new S_SystemMessage("Dialog " + htmlid + RESULT_ACTIONS[2]), true);								

					pc.sendPackets(new S_NPCTalkReturn(objid, htmlid + RESULT_ACTIONS[2]), true);// 아데나가 부족합니다
					return;
				}
			}
			
			// option 1. We know that the item is NOT stackable, so we can directly check the full inventory
			/* 
			if (pc.getInventory().full()) {
			    pc.sendPackets(L1ServerMessage.sm3560);// 인벤토리 공간이 부족합니다.
				return;
			}
			*/

			// option 2. We dont know about the item, or we want something generic, so we do this:
			/* 
			L1Item tempItem = ItemTable.getInstance().getTemplate(item_id);
			if (pc.getInventory().checkAddItem(tempItem, count) != L1Inventory.OK) 
				return;

			or shorter

			if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(itemId), count) != L1Inventory.OK) return;
			*/

			L1Item tempItem = ItemTable.getInstance().getTemplate(type == InnType.ROOM ? L1ItemId.INN_ROOM_KEY : L1ItemId.INN_HALL_KEY);
			if (pc.getInventory().checkAddItem(tempItem, count) != L1Inventory.OK) 
				return;

			int keyMap = statMapId + cnt;
			pc.getInventory().consumeItem(L1ItemId.ADENA, price);
			INN.put(keyMap, true);
			InnTimer inntimer = new InnTimer(keyMap, count, 14400000);// 4시간후
			inntimer.begin();
			setInnTimer(keyMap, inntimer);

			if (!World.get_map(keyMap)) {
				L1WorldMap.getInstance().cloneMap(statMapId, keyMap);
			}
			L1ItemInstance key = ItemTable.getInstance().createItem(type == InnType.ROOM ? L1ItemId.INN_ROOM_KEY : L1ItemId.INN_HALL_KEY);
			key.setCount(count);
			key.setKey(keyMap);
			key.setIdentified(true);
			pc.getInventory().storeItem(key);
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid + RESULT_ACTIONS[3]), true);								
			pc.sendPackets(new S_NPCTalkReturn(objid, htmlid + RESULT_ACTIONS[3]), true);// 같이 쓰실 분에게 나누어 주세요
		}
	}
	
	public void init(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("DELETE FROM character_items WHERE item_id IN (40312, 49312)");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_elf_warehouse WHERE item_id IN (40312, 49312)");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE item_id IN (40312, 49312)");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_warehouse WHERE item_id IN (40312, 49312)");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_package_warehouse WHERE item_id IN (40312, 49312)");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_special_warehouse WHERE item_id IN (40312, 49312)");
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
}

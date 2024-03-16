package l1j.server.GameSystem.eventpush;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

/**
 * 시스템 편지 시스템 매니저(푸시)
 * 운영자 명령어를 담당한다.
 * @author LinOffice
 */
public class EventPushManager {
	private static class newInstance {
		public static final EventPushManager INSTANCE = new EventPushManager();
	}
	public static EventPushManager getInstance(){
		return newInstance.INSTANCE;
	}
	private EventPushManager(){}
	
//AUTO SRM: 	private static final S_SystemMessage EXPLAN_MSG			= new S_SystemMessage(".푸시 [케릭명/전체] [아이템] [수량] [인챈트]"); // CHECKED OK
	private static final S_SystemMessage EXPLAN_MSG			= new S_SystemMessage(S_SystemMessage.getRefText(25), true);
//AUTO SRM: 	private static final S_SystemMessage TARGET_NULL		= new S_SystemMessage("대상이 발견되지 않았습니다."); // CHECKED OK
	private static final S_SystemMessage TARGET_NULL		= new S_SystemMessage(S_SystemMessage.getRefText(26), true);
//AUTO SRM: 	private static final S_SystemMessage ITEM_EMPTY			= new S_SystemMessage("해당 아이템이 발견되지 않습니다."); // CHECKED OK
	private static final S_SystemMessage ITEM_EMPTY			= new S_SystemMessage(S_SystemMessage.getRefText(27), true);
//AUTO SRM: 	private static final S_SystemMessage ITEM_SEARCH_FAIL	= new S_SystemMessage("지정 ID의 아이템은 존재하지 않습니다"); // CHECKED OK
	private static final S_SystemMessage ITEM_SEARCH_FAIL	= new S_SystemMessage(S_SystemMessage.getRefText(28), true);
	
	/*
	private static final String SUBJECT_FORMAT				= "%s(이)가 도착하였습니다.";
	private static final String CONTENT_FORMAT				= "%s(이)가 도착!\n받기를 눌러 수령하시기 바랍니다.:)";
	private static final String CHARGE_SUBJECT_FORMAT		= "%s 충전으로 오늘 하루도 힘차게 시작!";
	private static final String CHARGE_CONTENT				= "핫타임 이벤트!\n오늘도 아덴월드를 지켜주시는 용사님,\n이번차에는 더 희망찬 일만 가득할 거에요 :)";
	*/
	
	private static final String SUBJECT_FORMAT            = "%s has arrived.";
	private static final String CONTENT_FORMAT            = "%s has arrived!\nPlease press 'Collect' button to receive it. :)";
	private static final String CHARGE_SUBJECT_FORMAT     = "Start your day energetically with %s recharge!";
	private static final String CHARGE_CONTENT            = "Hot Time Event!\nDear guardian of the Aden World:\ntoday will be filled with even more hopeful moments :)";
	
	public void commands(L1PcInstance pc, String param){
		try {
			StringTokenizer st = new StringTokenizer(param);
			String targetName = st.nextToken();
			String desc = st.nextToken();
			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			
			int itemid = 0;
			try {
				itemid = Integer.parseInt(desc);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance(). findItemIdByDescWithoutSpace(desc);
				if (itemid == 0) {
					pc.sendPackets(ITEM_EMPTY);
					return;
				}
			}
			L1Item item = ItemTable.getInstance().getTemplate(itemid);
			if (item == null) {
				pc.sendPackets(ITEM_SEARCH_FAIL);
				return;
			}
			switch(targetName){
			//case "전체":
			case "all":
				for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
					if (target == null || target.getNetConnection() == null || target.noPlayerCK || target.isPrivateShop() || target.isAutoClanjoin()) {
						continue;
					}
					send(target, item, count, enchant);
				}
				//String message_all = String.format("%s를 모든 유저에게 전송하였습니다.", getCountItemName(count, getEnchantItemName(enchant, item.getDescKr()))); // CHECKED OK
				String message_all = String.format("%s " + S_SystemMessage.getRefText(1294), getCountItemName(count, getEnchantItemName(enchant, item.getDesc())));
				pc.sendPackets(new S_SystemMessage(message_all, true), true);
				break;
			default:
				L1PcInstance target = L1World.getInstance().getPlayer(targetName);
				if (target == null || target.getNetConnection() == null || target.noPlayerCK || target.isPrivateShop() || target.isAutoClanjoin()) {
					pc.sendPackets(TARGET_NULL);
					break;
				}
				if (!send(target, item, count, enchant)) {
					//String message_target = String.format("%s를 [%s]에게 전송하지 못하였습니다.", getCountItemName(count, getEnchantItemName(enchant, item.getDescKr())), target.getName());
					//pc.sendPackets(new S_SystemMessage(message_target, true), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(112), getCountItemName(count, getEnchantItemName(enchant, item.getDesc())), target.getName()), true);
					break;
				}
				//String message_target = String.format("%s를 [%s]에게 전송하였습니다.", getCountItemName(count, getEnchantItemName(enchant, item.getDescKr())), target.getName());
				//pc.sendPackets(new S_SystemMessage(message_target, true), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(113), getCountItemName(count, getEnchantItemName(enchant, item.getDesc())), target.getName()), true);
				break;
			}
		} catch(Exception e) {
			pc.sendPackets(EXPLAN_MSG);
		}
	}
	
	public void hot_time_push() {
		int[] id_array		= Config.PUSH.HOT_TIME_ITEM_ID;
		int[] count_array	= Config.PUSH.HOT_TIME_ITEM_COUNT;
		if (id_array == null || count_array == null || id_array.length != count_array.length) {
			return;
		}
		for (int i=0; i<id_array.length; i++) {
			int item_id = id_array[i];
			int count	= count_array[i];
			L1Item item = ItemTable.getInstance().getTemplate(item_id);
			if (item == null) {
				continue;
			}
			for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
				if (target == null || target.getNetConnection() == null || target.noPlayerCK || target.isPrivateShop() || target.isAutoClanjoin()) {
					continue;
				}
				send(target, item, count, 0);
			}
		}
	}
	
	public boolean send(L1PcInstance target, L1Item item, int count, int enchant){
		EventPushLoader push = EventPushLoader.getInstance();
		if (is_used_immediately(item.getItemId())) {
			//return push.create(target, item.getItemId(), String.format(CHARGE_SUBJECT_FORMAT, item.getDescKr()), CHARGE_CONTENT, 1, 0, true);
			return push.create(target, item.getItemId(), String.format(CHARGE_SUBJECT_FORMAT, item.getDescEn()), CHARGE_CONTENT, 1, 0, true);
		}
		if (item.isMerge()) {
			return push.create(target, item.getItemId(), getTile(item, count, enchant), getContent(item, count, enchant), count, enchant, false);
		}
		for (int i=0; i<count; i++) {
			if (!push.create(target, item.getItemId(), getTile(item, 1, enchant), getContent(item, 1, enchant), 1, enchant, false)) {
				return false;
			}
		}
		return true;
	}
	
	String getEnchantItemName(int enchant, String name) {
		if (enchant > 0) {
			return String.format("+%d %s", enchant, name);
		}
		if (enchant < 0) {
			return String.format("-%d %s", enchant, name);
		}
		return name;
	}
	
	String getCountItemName(int count, String name) {
		if (count > 1) {
			// return String.format("%s %d개", name, count);
			return String.format("%s (%d)", name, count);
		}
		return name;
	}
	
	String createMessage(L1Item temp, int count, int enchant){
		//return getCountItemName(count, getEnchantItemName(enchant, temp.getDescKr()));
		return getCountItemName(count, getEnchantItemName(enchant, temp.getDescEn()));
	}
	
	String getTile(L1Item temp, int count, int enchant){
		return String.format(SUBJECT_FORMAT, createMessage(temp, count, enchant));
	}
	
	String getContent(L1Item temp, int count, int enchant){
		return String.format(CONTENT_FORMAT, createMessage(temp, count, enchant));
	}
	
	boolean is_used_immediately(int itemId){
		return itemId == L1ItemId.EIN_BLESS_BONUS_50 || itemId == L1ItemId.EIN_BLESS_BONUS_100;
	}
}



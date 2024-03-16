package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
//import manager.Manager;  // MANAGER DISABLED

public class C_DropItem extends ClientBasePacket {
	private static final String C_DROP_ITEM = "[C] C_DropItem";

	public C_DropItem(byte[] decrypt, GameClient client) throws Exception {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost() || pc.isTwoLogin()) {
			return;
		}
		if (pc.getOnlineStatus() != 1) {
			pc.denals_disconnect(String.format("[C_DropItem] NOT_ONLINE_DISCONNECT : NAME(%s)", pc.getName()));
			return;
		}
		if (!pc.isGm() && pc.getLevel() < Config.ALT.ALT_DROPLEVELLIMIT) {
			//pc.sendPackets(new S_SystemMessage(String.format("레벨 %d부터 버릴 수 있습니다.", Config.ALT.ALT_DROPLEVELLIMIT)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(19), String.valueOf(Config.ALT.ALT_DROPLEVELLIMIT)), true);
			return;
		}
		short mapId = pc.getMapId();
		if (!pc.isGm() && (client.isInterServer() || mapId == 340 || mapId == 350 || mapId == 370 || mapId == 800)) {
			pc.sendPackets(L1SystemMessage.NO_DROP_ZONE_MSG);
			return;
		}
		int length			= readD();
		if (length <= 0 || length > 8) {
			return;
		}
		
		int pcX = pc.getX();
		int pcY = pc.getY();
		
		long nowtime = System.currentTimeMillis();
		for (int i=0; i<length; ++i) {
			int x			= readH();
			int y			= readH();
			int objectId	= readD();
			int count		= readD();
			
			if (Math.abs(pcX - x) > 1 || Math.abs(pcY - y) > 1) {
				pc.sendPackets(L1ServerMessage.sm340);
				return;
			}
			if (x > pcX + 1 || x < pcX - 1 || y > pcY + 1 || y < pcY - 1) {// 거리 체크
				return;
			}
			
			L1ItemInstance item = pc.getInventory().getItem(objectId);
			if (item == null) {
				return;
			}
			if (item.getItemDelay() >= nowtime) {
				continue;
			}
			if (!pc.isGm() 
					&& (!item.getItem().isTradable() 
							|| item.getBless() >= 128 
							|| item.isSlot() 
							|| item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE 
							|| item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE)) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				continue;
			}

			/** 버그 방지 **/
			L1ItemType itemType = item.getItem().getItemType();
			if ((itemType == L1ItemType.WEAPON && count != 1) || (itemType == L1ItemType.ARMOR && count != 1)) {	
				pc.denals_disconnect(String.format("[C_DropItem] NOT_MERGE_ITEM_NOT_ONE : NAME(%s)", pc.getName()));
				return;
			}
			if (item.getCount() <= 0) {
				return;
			}
			if (!item.isMerge() && count != 1) {
				pc.denals_disconnect(String.format("[C_DropItem] NOT_MERGE_ITEM_NOT_ONE : NAME(%s)", pc.getName()));
				return;
			}
			if (item.getCount() < count || count <= 0 || count > L1Inventory.MAX_AMOUNT) {
				pc.denals_disconnect(String.format("[C_DropItem] COUNT_DENALS : NAME(%s)", pc.getName()));
				return;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			if (objectId != item.getId()) {
				pc.denals_disconnect(String.format("[C_DropItem] PACKET_DENALS : NAME(%s)", pc.getName()));
				return;
			}
			
			
			/** 시간제아이템 드랍불가 **/
			if (item.getEndTime() != null 
					&& item.getItemId() != 80500 && item.getItemId() != 31236
					&& item.getItemId() != L1ItemId.INN_ROOM_KEY && item.getItemId() != L1ItemId.INN_HALL_KEY) {
				pc.sendPackets(L1ServerMessage.sm125);
				continue;
			}
			
			/** 착용 아이템 **/
			if (item.isEquipped()) {
				pc.sendPackets(L1ServerMessage.sm125);// \f1삭제할 수 없는 아이템이나 장비 하고 있는 아이템은 버릴 수 없습니다.
				continue;
			}
			
			/** 펫 목걸이 **/
			if (pc.getPet() != null && pc.getPet().getAmuletId() == item.getId()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				continue;
			}

			/** 마법인형 **/
			if (pc.getDoll() != null && item.getId() == pc.getDoll().getItemObjId()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				continue;
			}
			
			if (item != null && item.isMerge() && item.getItemDelay() <= nowtime) {
				item.setItemDelay(nowtime + 2000);// 딜레이 세팅
			}
			
			if (((item.getCount() >= 100) && ((item.getItemId() == L1ItemId.PIXIE_FEATHER) || (item.getItemId() == L1ItemId.SCROLL_OF_ENCHANT_WEAPON) || (item.getItemId() == L1ItemId.SCROLL_OF_ENCHANT_ARMOR)))
					|| (item.getEnchantLevel() > 0)
					|| ((item.getCount() >= 1000000) && (item.getItemId() == L1ItemId.ADENA))
					|| ((item.getCount() >= 1000) && (item.getItemId() != L1ItemId.ADENA))) {
				//Manager.getInstance().PicupAppend(item.getLogName(), pc.getName(), count, 1); // MANAGER DISABLED
			}
			pc.getInventory().tradeItem(item, count, L1World.getInstance().getInventory(x, y, mapId));
			pc.getLight().turnOnOffLight();
			LoggerInstance.getInstance().addItemAction(ItemActionType.Drop, pc, item, count);// 로그 저장
		}
	}
	
	@Override
	public String getType() {
		return C_DROP_ITEM;
	}
}



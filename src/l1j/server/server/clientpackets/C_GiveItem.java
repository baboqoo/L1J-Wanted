package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Npc;

public class C_GiveItem extends ClientBasePacket {
	private static final String C_GIVE_ITEM = "[C] C_GiveItem";

	public C_GiveItem(byte decrypt[], GameClient client) {
		super(decrypt);
		try {
			L1PcInstance pc = client.getActiveChar();
			if (pc == null || pc.isGhost() || pc.isTwoLogin() || client.isInterServer()) {
				return;
			}
			
			int targetId	= readD();
			readP(4);
			int itemObjId	= readD();
			int count		= readD();
			L1Object object = L1World.getInstance().findObject(targetId);
			if (object == null || !(object instanceof L1NpcInstance)) {
				return;
			}

			L1NpcInstance target	= (L1NpcInstance) object;
			L1Inventory targetInv	= target.getInventory();
			L1Inventory inv			= pc.getInventory();
			L1ItemInstance item		= inv.getItem(itemObjId);
			if (item == null) {
				return;
			}
			
			int itemId = item.getItemId();
			if (!isNpcItemReceivable(target.getNpcTemplate()) && (itemId != 40499 || itemId != 40507)) {
				return;
			}
			if (item.isEquipped() || item.getBless() >= 128 || item.getEndTime() != null) {
				pc.sendPackets(L1ServerMessage.sm141);
				return;
			}
			if (!item.getItem().isTradable() || item.isSlot() || itemId == L1ItemId.ADENA) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);
				return;
			}
			
			if (itemObjId != item.getId()) {
				pc.denals_disconnect(String.format("[C_GiveItem] PACKET_DENALS : NAME(%s)", pc.getName()));
				return;
			}
			if (itemId == L1ItemId.INN_ROOM_KEY || itemId == L1ItemId.INN_HALL_KEY) {
				pc.sendPackets(L1SystemMessage.INN_KEY_CANNOT_GIVE);
				return;
			}
			if (!item.isMerge() && count != 1) {
				pc.denals_disconnect(String.format("[C_GiveItem] NOT_MERGE_ITEM_NOT_ONE : NAME(%s)", pc.getName()));
				return;
			}

			if (item.getCount() <= 0 || count <= 0) {
				return;
			}

			if (count >= item.getCount()) {
				count = item.getCount();
			}

			if (pc.getPet() != null && pc.getPet().getAmuletId() == item.getId()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				return;
			}
			
			if (pc.getDoll() != null && item.getId() == pc.getDoll().getItemObjId()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				return;
			}

			if (!pc.isGm() && (targetInv.checkAddItem(item, count) != L1Inventory.OK)) {
				pc.sendPackets(L1ServerMessage.sm942);// 상대의 아이템이 너무 무겁기 (위해)때문에, 더 이상 줄 수 없습니다.
				return;
			}

			item = inv.tradeItem(item, count, targetInv);
			target.onGetItem(item);
			target.getLight().turnOnOffLight();
			pc.getLight().turnOnOffLight();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final String RECEIVABLE_IMPLS_REGEX = "L1Npc|L1Monster|L1Indun|L1BlackKnight|L1Doppelganger|L1Guardian|L1Guard";
	private boolean isNpcItemReceivable(L1Npc npc) {
		return npc.getImpl().matches(RECEIVABLE_IMPLS_REGEX);
	}

	@Override
	public String getType() {
		return C_GIVE_ITEM;
	}
}


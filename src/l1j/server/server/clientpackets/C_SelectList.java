package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;

public class C_SelectList extends ClientBasePacket {

	private static final String C_SELECT_LIST = "[C] C_SelectList";

	public C_SelectList(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		int itemObjectId	= readD();
		int npcObjectId		= readD();
		if (npcObjectId <= 0) {
			return;
		}
		L1Object obj = L1World.getInstance().findObject(npcObjectId);
		if (obj != null && (Math.abs(pc.getX() - obj.getX()) > 3 || Math.abs(pc.getY() - obj.getY()) > 3)) {// 3 매스 이상 떨어졌을 경우 액션 무효
			return;
		}
		L1PcInventory pcInventory = pc.getInventory();
		L1ItemInstance item = pcInventory.getItem(itemObjectId);
		if (!pc.getInventory().consumeItem(L1ItemId.ADENA, item.getDurability() * 200)) {
			return;
		}
		item.setDurability(0);
		pcInventory.updateItem(item, L1PcInventory.COL_DURABILITY);
	}

	@Override
	public String getType() {
		return C_SELECT_LIST;
	}
}


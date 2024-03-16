package l1j.server.server.serverpackets;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_FixWeaponList extends ServerBasePacket {

	private static final String S_FIX_WEAPON_LIST = "[S] S_FixWeaponList";

	public S_FixWeaponList(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_FIXABLE_ITEM_LIST);
		writeD(0x000000c8); // Price

		List<L1ItemInstance> showList = new ArrayList<L1ItemInstance>();
		List<L1ItemInstance> itemList = pc.getInventory().getItems();
		for (L1ItemInstance item : itemList) {
			if (item.getDurability() > 0) {
				showList.add(item);
			}
		}

		writeH(showList.size());// Amount

		for (L1ItemInstance item : showList) {
			writeD(item.getId());// Item ID
			writeC(item.getDurability());// Fix Level
		}
		showList.clear();
		showList = null;
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_FIX_WEAPON_LIST;
	}
}

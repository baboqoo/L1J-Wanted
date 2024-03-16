package l1j.server.server.serverpackets.companion;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PetList extends ServerBasePacket {
	private static final String S_PET_LIST = "[S] S_PetList";
	private byte[] _byte = null;

	public S_PetList(int npcObjId, L1PcInstance pc) {
		buildPacket(npcObjId, pc);
	}

	private void buildPacket(int npcObjId, L1PcInstance pc) {
		List<L1ItemInstance> amuletList = new ArrayList<L1ItemInstance>();
		L1ItemInstance item = null;
		for (Object itemObject : pc.getInventory().getItems()) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == 40314 || item.getItem().getItemId() == 40316) {
				if (!isWithdraw(pc, item)) {
					amuletList.add(item);
				}
			}
		}
		if (amuletList.size() != 0) {
			writeC(Opcodes.S_FIXABLE_ITEM_LIST);
			writeD(0x00000046); // Price
			writeH(amuletList.size());
			for (L1ItemInstance _item : amuletList) {
				writeD(_item.getId());
				writeC(_item.getCount());
			}
		}
	}

	private boolean isWithdraw(L1PcInstance pc, L1ItemInstance item) {
		if (pc.getPet() != null && pc.getPet().getAmuletId() == item.getId()) {
			return true;
		}
		return false;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	@Override
	public String getType() {
		return S_PET_LIST;
	}
}


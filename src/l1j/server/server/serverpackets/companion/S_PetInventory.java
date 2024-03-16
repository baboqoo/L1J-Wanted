package l1j.server.server.serverpackets.companion;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PetInventory extends ServerBasePacket {

	private static final String S_PET_INVENTORY = "[S] S_PetInventory";
	private byte[] _byte = null;

	public S_PetInventory(L1PetInstance pet) {
		List<L1ItemInstance> itemList = pet.getInventory().getItems();

		writeC(Opcodes.S_RETRIEVE_LIST);
		writeD(pet.getId());
		writeH(itemList.size());
		writeC(0x0b);
		L1ItemInstance item = null;
		for (Object itemObject : itemList) {
			item = (L1ItemInstance) itemObject;
			if (item != null) {
				writeD(item.getId());
				writeC(0x16);
				writeH(item.getIconId());
				writeC(item.getItem().getBless());
				writeD(item.getCount());
				writeC(item.isIdentified() ? 1 : 0);
				writeS(item.getViewName());
			}
		}
		writeC(0x0a);
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
		return S_PET_INVENTORY;
	}
}


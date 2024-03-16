package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.companion.S_PetInventory;

public class C_PetMenu extends ClientBasePacket {
	private static final String C_PET_MENU = "[C] C_PetMenu";

	public C_PetMenu(byte abyte0[], GameClient client)throws Exception {
		super(abyte0);
		int petId = readD();
		L1Object obj = L1World.getInstance().findObject(petId);
		if (obj == null || !(obj instanceof L1PetInstance)) {
			return;
		}
		L1PetInstance pet	= (L1PetInstance) L1World.getInstance().findObject(petId);
		L1PcInstance pc		= client.getActiveChar();
		if (pet != null && pc != null) {
			pc.sendPackets(new S_PetInventory(pet), true);
		}
	}

	@Override
	public String getType() {
		return C_PET_MENU;
	}
}


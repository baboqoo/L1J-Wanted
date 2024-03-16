package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1CharName;
import l1j.server.server.utils.StringUtil;

public class C_AddBuddy extends ClientBasePacket {

	private static final String C_ADD_BUDDY = "[C] C_AddBuddy";

	public C_AddBuddy(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		BuddyTable buddyTable	= BuddyTable.getInstance();
		L1Buddy buddyList		= buddyTable.getBuddyTable(pc.getId());
		String charName			= readS();

		if (charName.equalsIgnoreCase(pc.getName())) {
			return;
		}
		if (buddyList.containsName(charName)) {
			pc.sendPackets(new S_ServerMessage(1052, charName), true); //	(은)는 이미 등록되어 있습니다.
			return;
		}

		for (L1CharName cn : CharacterTable.getInstance().getCharNameList()) {
			if (charName.equalsIgnoreCase(cn.getName())) {
				String name = cn.getName();
				if (L1CharacterInfo.isGmName(cn.getName())) {
					return;
				}
				buddyList.add(name, StringUtil.EmptyString);
				buddyTable.addBuddy(pc.getId(), name, StringUtil.EmptyString);
				return;
			}
		}
		pc.sendPackets(new S_ServerMessage(109, charName), true); // %0라는 이름의 사람은 없습니다.
	}

	@Override
	public String getType() {
		return C_ADD_BUDDY;
	}
}

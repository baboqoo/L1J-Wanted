package l1j.server.server.serverpackets.party;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PartyList extends ServerBasePacket {
	private static final String S_PARTY_LIST = "[S] S_PartyList";
	private byte[] _byte = null;

	public S_PartyList(L1PcInstance cha) {
		writeC(Opcodes.S_EVENT);
		writeRaw(0x68);
		if (cha.getParty() == null) {
			writeC(0x00);
		} else {
			L1PcInstance leader = cha.getParty().getLeader();
			L1PcInstance temp[] = cha.getParty().getMembersArray();

			writeRaw(temp.length-1);
			writeD(leader.getId());
			writeS(leader.getName());
			writeRaw(leader.getCurrentHpPercent());
			writeD(leader.getMapId());
			writeH(leader.getX());
			writeH(leader.getY());
			for (int i = 0; i < temp.length; i++) {
				if (cha.getId() == temp[i].getId() || leader.getId() == temp[i].getId()) {
					continue;
				}
				writeD(temp[i].getId());
				writeS(temp[i].getName());
				writeRaw(temp[i].getCurrentHpPercent());
				writeD(temp[i].getMapId());
				writeH(temp[i].getX());
				writeH(temp[i].getY());
				writeRaw(0x00);
			}
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_PARTY_LIST;
	}
}


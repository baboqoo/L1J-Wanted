package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ReturnedStatPetParty extends ServerBasePacket {
	private static final String S_RETURNED_STAT_PET_PARTY = "[S] S_ReturnedStatPetParty";
	private byte[] _byte = null;
	public static final int PET_PARTY	= 0x0c;
	
	public S_ReturnedStatPetParty(int objid, int x, int y, short mapid, String nameId, boolean ck){
		writeC(Opcodes.S_VOICE_CHAT);
		writeRaw(PET_PARTY);
		writeH(0x00);
		writeD(ck ? 0 : 1);
		writeD(objid);
		if (ck) {
			writeD(0x00);
			writeH(x);
			writeH(y);
			writeRaw(mapid);
			writeS(nameId);	
		}
		writeRaw(ck ? 1 : 0);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_RETURNED_STAT_PET_PARTY;
	}
}

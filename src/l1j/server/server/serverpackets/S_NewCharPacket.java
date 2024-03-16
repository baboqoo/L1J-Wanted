package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.StringUtil;

public class S_NewCharPacket extends ServerBasePacket {
	private static final String S_NEWCHARPACK = "[S] New Char Packet";
	private byte[] _byte = null;

	public S_NewCharPacket(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_NEW_CHAR_INFO);
		writeS(pc.getName());
		writeS(StringUtil.EmptyString);
		writeC(pc.getType());
		writeC(pc.getGender().toInt());
		writeH(pc.getAlignment());
		writeH(pc.getMaxHp());
		writeH(pc.getMaxMp());
		writeC(pc.getAC().getAc());
		writeC(pc.getLevel());
		writeC(pc.getAbility().getStr());
		writeC(pc.getAbility().getDex());
		writeC(pc.getAbility().getCon());
		writeC(pc.getAbility().getWis());
		writeC(pc.getAbility().getCha());
		writeC(pc.getAbility().getInt());
		writeC(0);
		writeD(pc.getBirthDay());
		int code = pc.getLevel() ^ pc.getAbility().getStr() ^ pc.getAbility().getDex() ^ pc.getAbility().getCon() ^ pc.getAbility().getWis() ^ pc.getAbility().getCha() ^ pc.getAbility().getInt();
		writeC(code & 0xFF);
		writeD(0000);
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
		return S_NEWCHARPACK;
	}

}


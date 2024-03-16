package l1j.server.server.serverpackets.command;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BuilderAPCHpMpInfo extends ServerBasePacket {
	private static final String S_BUILDER_APC_HP_MP_INFO = "[S] S_BuilderAPCHpMpInfo";
	private byte[] _byte = null;
	
	public static final int HPMP_INFO = 0x0a11;
	
	public S_BuilderAPCHpMpInfo(L1PcInstance target){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(HPMP_INFO);
		
		int length = 5 + getBitSize(target.getId())
				+ getBitSize(target.getMaxHp()) + getBitSize(target.getMaxMp())
				+ getBitSize(target.getCurrentHp()) + getBitSize(target.getCurrentMp());
		
		writeC(0x0a);
		writeC(length);
		
		writeC(0x08);
		writeBit(target.getId());
		
		writeC(0x10);
		writeBit(target.getMaxHp());
		
		writeC(0x18);
		writeBit(target.getMaxMp());
		
		writeC(0x20);
		writeBit(target.getCurrentHp());
		
		writeC(0x28);
		writeBit(target.getCurrentMp());

		writeH(0x00);
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
		return S_BUILDER_APC_HP_MP_INFO;
	}
}


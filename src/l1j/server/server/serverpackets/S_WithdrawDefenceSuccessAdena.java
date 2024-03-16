package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_WithdrawDefenceSuccessAdena extends ServerBasePacket {
	private static final String S_WITHDRAW_DEFENCE_SUCCESS_ADENA = "[S] S_WithdrawDefenceSuccessAdena";
	private byte[] _byte = null;
	
	public S_WithdrawDefenceSuccessAdena(int code, int npcId, long withdrawalAdena){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		
		writeC(0x08);
		writeBit(npcId);
		
		writeC(0x10);
		writeBit(withdrawalAdena);
		
        writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = _bao.toByteArray();
		return _byte;
	}

	@Override
	public String getType() {
		return S_WITHDRAW_DEFENCE_SUCCESS_ADENA;
	}
}


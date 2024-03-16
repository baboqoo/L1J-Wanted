package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_WithdrawDefenceSuccessAdenaNoti extends ServerBasePacket {
	private static final String S_WITHDRAW_DEFENCE_SUCCESS_ADENA_NOTI = "[S] S_WithdrawDefenceSuccessAdenaNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x005A;
	
	public S_WithdrawDefenceSuccessAdenaNoti(int npcId, long withdrawalAdena) {
		write_init();
		write_npcId(npcId);
		write_withdrawalAdena(withdrawalAdena);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_npcId(int npcId) {
		writeRaw(0x08);
		writeBit(npcId);
	}
	
	void write_withdrawalAdena(long withdrawalAdena) {
		writeRaw(0x10);
		writeBit(withdrawalAdena);
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
		return S_WITHDRAW_DEFENCE_SUCCESS_ADENA_NOTI;
	}
}


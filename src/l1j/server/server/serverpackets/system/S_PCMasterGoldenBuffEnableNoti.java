package l1j.server.server.serverpackets.system;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PCMasterGoldenBuffEnableNoti extends ServerBasePacket {
	private static final String S_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI = "[S] S_PCMasterGoldenBuffEnableNoti";
	private byte[] _byte = null;
	public static final int ENABLE_NOTI	= 0x0a85;
	
	public static final S_PCMasterGoldenBuffEnableNoti DISABLE = new S_PCMasterGoldenBuffEnableNoti(false, 0, null);
	
	public S_PCMasterGoldenBuffEnableNoti(boolean enable, int remain_count, java.util.LinkedList<byte[]> desc) {
		write_init();
		write_enable(enable);
		write_remain_count(enable ? remain_count : 0);
		if (enable) {
			write_desc(desc);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ENABLE_NOTI);
	}
	
	void write_enable(boolean enable) {
		writeRaw(0x08);
		writeB(enable);
	}
	
	void write_remain_count(int remain_count) {
		writeRaw(0x10);
		writeBit(remain_count);
	}
	
	void write_desc(java.util.LinkedList<byte[]> desc) {
		for (byte[] val : desc) {
			writeRaw(0x1a);
			writeBytesWithLength(val);
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
		return S_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI;
	}
}


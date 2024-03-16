package l1j.server.server.serverpackets.playsupport;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_FinishPlaySupport extends ServerBasePacket {
	private static final String S_FINISH_PLAY_SUPPORT = "[S] S_FinishPlaySupport";
	private byte[] _byte = null;
	public static final int PLAY_SUPPORT_FINISH		= 0x0838;
	public static final S_FinishPlaySupport FINISH	= new S_FinishPlaySupport(0);
	
	public S_FinishPlaySupport(int remain_time){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PLAY_SUPPORT_FINISH);
		writeC(0x08);// remain_time
		writeC(remain_time);
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
		return S_FINISH_PLAY_SUPPORT;
	}
}


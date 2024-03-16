package l1j.server.server.serverpackets.inter;

import l1j.server.common.data.Result;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_HibreedAuth extends ServerBasePacket {
	private static final String S_HIBREED_AUTH = "[S] S_HibreedAuth";
	private byte[] _byte = null;
	public static final int AUTH	= 0x0074;
	
	public static final S_HibreedAuth SUCCESS	= new S_HibreedAuth(Result.Result_sucess);
	public static final S_HibreedAuth FAILURE	= new S_HibreedAuth(Result.Result_fail);
			
	public S_HibreedAuth(Result result) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(AUTH);
		writeRaw(0x08);// result
		writeRaw(result.toInt());
		writeH(0x00);
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
		return S_HIBREED_AUTH;
	}
}

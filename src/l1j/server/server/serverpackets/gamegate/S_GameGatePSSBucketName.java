package l1j.server.server.serverpackets.gamegate;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_GameGatePSSBucketName extends ServerBasePacket {
	private static final String S_CAME_GATE_PSS_BUCKET_NAME = "[S] S_GameGatePSSBucketName";
	private byte[] _byte = null;
	public static final int BUCKET_NAME = 0x09c5;
	
	public static final S_GameGatePSSBucketName CONFIG	= new S_GameGatePSSBucketName();// PSS CONFIG
	
	private S_GameGatePSSBucketName(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(BUCKET_NAME);
		writeRaw(0x0a);
		writeStringWithLength(Config.PSS.PLAY_SUPPORT_BUCKET_NAME);
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
		return S_CAME_GATE_PSS_BUCKET_NAME;
	}
}


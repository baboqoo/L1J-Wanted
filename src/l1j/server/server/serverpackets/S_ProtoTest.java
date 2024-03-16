package l1j.server.server.serverpackets;

import l1j.server.server.GameServerSetting;
import l1j.server.server.Opcodes;

public class S_ProtoTest extends ServerBasePacket {
	private static final String S_PROTO_TEST = "[S] S_ProtoTest";
	private byte[] _byte = null;

	public S_ProtoTest(int value){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(GameServerSetting.TEST);
		
		writeC(0x08);
		writeBit(value);
		
        writeH(0x00);
	}
	
	public S_ProtoTest(int code, int value){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		
		writeC(0x08);
		writeBit(value);
		
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
		return S_PROTO_TEST;
	}
}


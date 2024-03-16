package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Weather extends ServerBasePacket {
	private static final String S_WEATHER = "[S] S_Weather";
	private byte[] _byte = null;
	
	public static final S_Weather NORMAL = new S_Weather(0);

	public S_Weather(int weather) {
		writeC(Opcodes.S_WEATHER);
		writeC(weather);
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
		return S_WEATHER;
	}
}


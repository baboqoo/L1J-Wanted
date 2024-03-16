package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CurseBlind extends ServerBasePacket {

	private static final String S_CURSE_BLIND = "[S] S_CurseBlind";
	private byte[] _byte = null;
	
	public static final S_CurseBlind BLIND_OFF				= new S_CurseBlind(0);
	public static final S_CurseBlind BLIND_ON				= new S_CurseBlind(1);
	public static final S_CurseBlind BLIND_FLOATING_EYE		= new S_CurseBlind(2);

	public S_CurseBlind(int type) {
		// type 0:OFF 1:자신 이외 안보이는 2:주위의 캐릭터가 보인다
		buildPacket(type);
	}

	private void buildPacket(int type) {
		writeC(Opcodes.S_BLIND);
		writeH(type);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null)
			_byte = getBytes();
		return _byte;
	}
	@Override
	public String getType() {
		return S_CURSE_BLIND;
	}
}


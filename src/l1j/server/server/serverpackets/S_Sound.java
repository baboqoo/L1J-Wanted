package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Sound extends ServerBasePacket {
	private static final String S_SOUND = "[S] S_Sound";
	private byte[] _byte = null;
	
	public static final S_Sound SILVER_FLOT_SOUND	= new S_Sound(10);
	public static final S_Sound LIGHT_SOUND			= new S_Sound(145);
	public static final S_Sound INVIS_SOUND			= new S_Sound(147);
	public static final S_Sound MAGIC_FLUTE_SOUND	= new S_Sound(165);
	public static final S_Sound FANTASY_START_SOUND	= new S_Sound(184);

	/**
	 * 효과음을 울린다(sound 폴더의 wav 파일).
	 * @param sound
	 */
	public S_Sound(int sound) {
		writeC(Opcodes.S_SOUND_EFFECT);
		writeC(0); // repeat
		writeH(sound);
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
		return S_SOUND;
	}
}


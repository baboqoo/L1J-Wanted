package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ReturnedStatElixir extends ServerBasePacket {
	private static final String S_RETURNED_STAT_ELIXIR = "[S] S_ReturnedStatElixir";
	private byte[] _byte = null;
	public static final int ELIXIR	= 0x4c;
	
	public static final S_ReturnedStatElixir ELIXIR_1	= new S_ReturnedStatElixir(1);
	public static final S_ReturnedStatElixir ELIXIR_2	= new S_ReturnedStatElixir(2);
	public static final S_ReturnedStatElixir ELIXIR_3	= new S_ReturnedStatElixir(3);
	public static final S_ReturnedStatElixir ELIXIR_4	= new S_ReturnedStatElixir(4);
	
	public S_ReturnedStatElixir(int value) {
        writeC(Opcodes.S_VOICE_CHAT);
        writeRaw(ELIXIR);
        writeRaw(value);
    }
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_RETURNED_STAT_ELIXIR;
	}
}

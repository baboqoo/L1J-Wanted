package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ArenaCheer extends ServerBasePacket {
	private static final String S_ARENA_CHEER = "[S] S_ArenaCheer";
	private byte[] _byte = null;
	public static final int CHEER	= 0x02e3;
	
	public static final S_ArenaCheer SUCCESS	= new S_ArenaCheer(S_ArenaCheer.eResult.SUCCESS);
	public static final S_ArenaCheer FAIL		= new S_ArenaCheer(S_ArenaCheer.eResult.FAIL);
			
	public S_ArenaCheer(S_ArenaCheer.eResult result) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHEER);
		writeRaw(0x08);// result
		writeRaw(result.toInt());
		writeH(0x00);
	}
	
	public enum eResult{
		SUCCESS(1),
		FAIL(2),
		;
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return FAIL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
			}
		}
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
		return S_ARENA_CHEER;
	}
}

package l1j.server.server.serverpackets.quest;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_QuestShowInWindow extends ServerBasePacket {
	private static final String S_QUEST_SHOW_IN_WINDOW = "[S] S_QuestShowInWindow";
	private byte[] _byte = null;
	public static final int SHOW_WINDOW	= 0x0212;
	
	public S_QuestShowInWindow(S_QuestShowInWindow.eResultCode result, int id, boolean is_show) {
		write_init();
		write_result(result);
		write_id(id);
		write_is_show(is_show);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SHOW_WINDOW);
	}
	
	void write_result(S_QuestShowInWindow.eResultCode result) {
		writeRaw(0x08);
		writeRaw(result.value);
	}
	
	void write_id(int id) {
		writeRaw(0x10);
		writeBit(id);
	}
	
	void write_is_show(boolean is_show) {
		writeRaw(0x18);
		writeB(is_show);
	}
	
	public enum eResultCode{
		SUCCESS(0),
		FAIL(1);
		private int value;
		eResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResultCode v){
			return value == v.value;
		}
		public static eResultCode fromInt(int i){
			switch(i){
			case 0:
				return SUCCESS;
			case 1:
				return FAIL;
			default:
				return null;
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
		return S_QUEST_SHOW_IN_WINDOW;
	}
}

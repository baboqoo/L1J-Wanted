package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AboveHeadMarkEventNoti extends ServerBasePacket {
	private static final String S_ABOVE_HEAD_MARK_EVENT_NOTI = "[S] S_AboveHeadMarkEventNoti";
	private byte[] _byte = null;
	
	public S_AboveHeadMarkEventNoti(int code, int user_id, int mark_id, S_AboveHeadMarkEventNoti.eEVENT_TYPE type) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		write_user_id(user_id);
		write_mark_id(mark_id);
		write_type(type);
		writeH(0x00);
	}
	
	void write_user_id(int user_id) {
		writeRaw(0x08);
		writeBit(user_id);
	}
	
	void write_mark_id(int mark_id) {
		writeRaw(0x10);
		writeBit(mark_id);
	}
	
	void write_type(S_AboveHeadMarkEventNoti.eEVENT_TYPE type) {
		writeRaw(0x18);
		writeRaw(type.value);
	}
	
	public enum eEVENT_TYPE{
		eEVENT_NON(0),
		eEVENT_SHUFFER(1),
		;
		private int value;
		eEVENT_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eEVENT_TYPE v){
			return value == v.value;
		}
		public static eEVENT_TYPE fromInt(int i){
			switch(i){
			case 0:
				return eEVENT_NON;
			case 1:
				return eEVENT_SHUFFER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eEVENT_TYPE, %d", i));
			}
		}
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
		return S_ABOVE_HEAD_MARK_EVENT_NOTI;
	}
}


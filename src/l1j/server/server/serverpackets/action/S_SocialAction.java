package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SocialAction extends ServerBasePacket {
	private static final String S_SOCIAL_ACTION = "[S] S_SocialAction";
	private byte[] _byte = null;
	public static final int ACTION	= 0x0140;
	
	public S_SocialAction(SOCIAL_ACTION_TYPE action_type, int action_code, int object_id) {
		write_init();
		write_object_id(object_id);
		write_action_type(action_type);
		write_action_code(action_code);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ACTION);
	}
	
	void write_object_id(int object_id) {
		writeC(0x08);// object_id
		writeBit(object_id);
	}
	
	void write_action_type(SOCIAL_ACTION_TYPE action_type) {
		writeC(0x10);// action_type
		writeC(action_type.value);
	}
	
	void write_action_code(int action_code) {
		writeC(0x18);// action_code
		writeC(action_code);
	}
	
	public enum SOCIAL_ACTION_TYPE{
		SOCIAL_ACTION_TYPE_NORMAL(1),
		SOCIAL_ACTION_TYPE_EMOTICON(2),
		SOCIAL_ACTION_TYPE_STATUS(3),
		SOCIAL_ACTION_TYPE_NPC_EMOTICON(4),
		;
		private int value;
		SOCIAL_ACTION_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SOCIAL_ACTION_TYPE v){
			return value == v.value;
		}
		public static SOCIAL_ACTION_TYPE fromInt(int i){
			switch(i){
			case 1:
				return SOCIAL_ACTION_TYPE_NORMAL;
			case 2:
				return SOCIAL_ACTION_TYPE_EMOTICON;
			case 3:
				return SOCIAL_ACTION_TYPE_STATUS;
			case 4:
				return SOCIAL_ACTION_TYPE_NPC_EMOTICON;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SOCIAL_ACTION_TYPE, %d", i));
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

	public String getType() {
		return S_SOCIAL_ACTION;
	}
}


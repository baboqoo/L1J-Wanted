package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_MessegeNoti extends ServerBasePacket {
	private static final String S_MESSEGE_NOTI = "[S] S_MessegeNoti";
	private byte[] _byte = null;
	public static final int MESSAGE = 0x0250;
	
	public S_MessegeNoti(int msg_code, String args, int option_a) {
		write_init();
		write_type(eType.CENTER_BROADCAST_WITH_ITEM);
		write_msg_code(msg_code);
		write_args(args);
		if (option_a > 0) {
			write_option_a(option_a);
		}
		writeH(0x00);
	}
	
	public S_MessegeNoti(int msg_code, String args_1, String args_2, int option_a) {
		write_init();
		write_type(option_a > 0 ? eType.CENTER_BROADCAST_WITH_ITEM : eType.REVENGE_BROADCAST);
		write_msg_code(msg_code);
		write_args(args_1);
		write_args(args_2);
		if (option_a > 0) {
			write_option_a(option_a);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MESSAGE);
	}
	
	void write_type(eType type) {
		writeRaw(0x08);// type
		writeRaw(type.value); // 타입
	}
	
	void write_msg_code(int msg_code) {
		writeRaw(0x10);// msg_code
		byteWrite(msg_code);
	}
	
	void write_msg_str(String msg_str) {
		writeRaw(0x1a);// msg_str
		writeStringWithLength(msg_str);
	}
	
	void write_args(String args) {
		writeRaw(0x22);// args
		writeStringWithLength(args);
	}
	
	void write_option_a(int option_a) {
		writeRaw(0x28);// option_a
		writeBit(option_a);
	}
	
	void write_object_id(int object_id) {
		writeRaw(0x30);// object_id
		writeBit(object_id);
	}
	
	public enum eType{
		NORMAL(0),
		CRAFT_SUCCESS_BROADCAST(1),
		ALCHEMY_SUCCESS_BROADCAST(2),
		CENTER_BROADCAST_WITH_ITEM(3),
		REVENGE_BROADCAST(4),
		HIGH_LEVEL_INFINITY_BATTLE_BROADCAST(5),
		POP_UP_WINDOW(6),
		INTER_CONTENTS_PRE_NOTIFICATION(7),
		;
		private int value;
		eType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eType v){
			return value == v.value;
		}
		public static eType fromInt(int i){
			switch(i){
			case 0:
				return NORMAL;
			case 1:
				return CRAFT_SUCCESS_BROADCAST;
			case 2:
				return ALCHEMY_SUCCESS_BROADCAST;
			case 3:
				return CENTER_BROADCAST_WITH_ITEM;
			case 4:
				return REVENGE_BROADCAST;
			case 5:
				return HIGH_LEVEL_INFINITY_BATTLE_BROADCAST;
			case 6:
				return POP_UP_WINDOW;
			case 7:
				return INTER_CONTENTS_PRE_NOTIFICATION;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eType, %d", i));
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
		return S_MESSEGE_NOTI;
	}
}


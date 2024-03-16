package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DashMoveNoti extends ServerBasePacket {
	private static final String S_DASH_MOVE_NOTI = "[S] S_DashMoveNoti";
	private byte[] _byte = null;

	public S_DashMoveNoti(int code, int obj_id, int start_pos, int end_pos, int effect_sprite) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		write_obj_id(obj_id);
		write_show_type(eShowType.ExtraAction);
		write_start_pos(start_pos);
		write_end_pos(end_pos);
		write_effect_sprite(effect_sprite);
		writeH(0x00);
	}
	
	void write_obj_id(int obj_id) {
		writeRaw(0x08);
		writeBit(obj_id);
	}
	
	void write_show_type(eShowType show_type) {
		writeRaw(0x10);
		writeRaw(show_type.value);
	}
	
	void write_start_pos(int start_pos) {
		writeRaw(0x18);
		writeBit(start_pos);
	}
	
	void write_end_pos(int end_pos) {
		writeRaw(0x20);
		writeBit(end_pos);
	}
	
	void write_effect_sprite(int effect_sprite) {
		writeRaw(0x28);
		writeBit(effect_sprite);
	}
	
	public enum eShowType{
		None(0),
		ExtraAction(1),
		;
		private int value;
		eShowType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eShowType v){
			return value == v.value;
		}
		public static eShowType fromInt(int i){
			switch(i){
			case 0:
				return None;
			case 1:
				return ExtraAction;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eShowType, %d", i));
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
		return S_DASH_MOVE_NOTI;
	}
}


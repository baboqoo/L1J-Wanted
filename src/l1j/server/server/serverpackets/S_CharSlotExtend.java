package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.StringUtil;

public class S_CharSlotExtend extends ServerBasePacket {
	private static final String S_CHAR_SLOT_EXTEND = "[S] S_CharSlotExtend";
	private byte[] _byte = null;
	public static final int SLOT_EXTEND = 0x09d5;
	
	public static final S_CharSlotExtend ITEM_EMPTY = new S_CharSlotExtend(-1, null, 8394);
	
	public S_CharSlotExtend(int result_code, String use_item_char, int additional_msg_code) {
		write_init();
		write_result_code(result_code);// -1:태고의옥쇄 없음, 2:성공
		if (!StringUtil.isNullOrEmpty(use_item_char)) {
			write_use_item_char(use_item_char);
		}
		write_additional_msg_code(additional_msg_code);// 8393: 태고의 옥새로 슬롯을 해제했습니다., 8394: 태고의 옥새가 없습니다., 8395: 사용한 캐릭터명 : %s
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SLOT_EXTEND);
	}
	
	void write_result_code(int result_code) {
		writeRaw(0x08);// result_code
		writeBit(result_code);
	}
	
	void write_use_item_char(String use_item_char) {
		writeRaw(0x12);// use_item_char
		writeStringWithLength(use_item_char);
	}
	
	void write_additional_msg_code(int additional_msg_code) {
		writeRaw(0x18);// additional_msg_code
		writeBit(additional_msg_code);
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
		return S_CHAR_SLOT_EXTEND;
	}
}


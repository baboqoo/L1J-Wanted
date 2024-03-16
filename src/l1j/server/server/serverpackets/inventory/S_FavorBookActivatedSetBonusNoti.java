package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_FavorBookActivatedSetBonusNoti extends ServerBasePacket {
	private static final String S_FAVOR_BOOK_ACTIVATED_SET_BONUS_NOTI = "[S] S_FavorBookActivatedSetBonusNoti";
	private byte[] _byte = null;
	public static final int ACTIVATED	= 0x0a5d;
	
	public static final S_FavorBookActivatedSetBonusNoti NOTI = new S_FavorBookActivatedSetBonusNoti();
	
	public S_FavorBookActivatedSetBonusNoti(){
		write_init();
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ACTIVATED);
	}
	
	void write_activated_set_bonus(java.util.LinkedList<Integer> activated_set_bonus) {
		for (int val : activated_set_bonus) {
			writeRaw(0x08);
			writeRaw(val);
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
		return S_FAVOR_BOOK_ACTIVATED_SET_BONUS_NOTI;
	}
}


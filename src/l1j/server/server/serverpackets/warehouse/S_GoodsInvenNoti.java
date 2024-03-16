package l1j.server.server.serverpackets.warehouse;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_GoodsInvenNoti extends ServerBasePacket {
	private static final String S_GOODS_INVEN_NOTI = "[S] S_GoodsInvenNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x040e;
	
	public static final S_GoodsInvenNoti GOODS_INVEN_ON = new S_GoodsInvenNoti(true);// 인벤토리 튤팁 빨간불
	
	public S_GoodsInvenNoti(boolean val) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
		writeRaw(0x08);
		writeB(val);
		writeH(0x00);
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
		return S_GOODS_INVEN_NOTI;
	}

}


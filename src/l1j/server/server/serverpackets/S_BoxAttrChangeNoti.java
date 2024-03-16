package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_BoxAttrChangeNoti extends ServerBasePacket {
	private static final String S_BOX_ATTR_CHANGE_NOTI = "[S] S_BoxAttrChangeNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0081;
	
	public S_BoxAttrChangeNoti(int worldNumber, S_BoxAttrChangeNoti.Box box, int attribute){
		write_init();
		write_worldNumber(worldNumber);
		write_box(box);
		write_attribute(attribute);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_worldNumber(int worldNumber) {
		writeRaw(0x08);
		writeBit(worldNumber);
	}
	
	void write_box(S_BoxAttrChangeNoti.Box box) {
		writeRaw(0x12);
		writeRaw(4 + getBitSize(box._sx) + getBitSize(box._sy) + getBitSize(box._ex) + getBitSize(box._ey));
		
		writeRaw(0x08);
		writeBit(box._sx);
		
		writeRaw(0x10);
		writeBit(box._sy);
		
		writeRaw(0x18);
		writeBit(box._ex);
		
		writeRaw(0x20);
		writeBit(box._ey);
	}
	
	void write_attribute(int attribute) {
		writeRaw(0x18);
		writeBit(attribute);
	}
	
	public static class Box {
		private int _sx;
		private int _sy;
		private int _ex;
		private int _ey;
		
		public Box(int sx, int sy, int ex, int ey) {
			_sx = sx;
			_sy = sy;
			_ex = ex;
			_ey = ey;
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
		return S_BOX_ATTR_CHANGE_NOTI;
	}
}


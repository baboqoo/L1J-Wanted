package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.BinaryOutputStream;

public class S_FreePVPRegionNoti extends ServerBasePacket {
	private static final String S_FREE_PVP_REGION_NOTI = "[S] S_FreePVPRegionNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x009d;
	
	public S_FreePVPRegionNoti(int worldNumber, boolean isFreePvpZone, java.util.LinkedList<S_FreePVPRegionNoti.Box> box) {
		write_init();
		write_worldNumber(worldNumber);
		write_isFreePvpZone(isFreePvpZone);
		if (box != null && !box.isEmpty()) {
			write_box(box);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_worldNumber(int worldNumber) {
		writeRaw(0x08);// worldNumber
		writeBit(worldNumber);
	}
	
	void write_isFreePvpZone(boolean isFreePvpZone) {
		writeRaw(0x10);// isFreePvpZone
		writeB(isFreePvpZone);
	}
	
	void write_box(java.util.LinkedList<S_FreePVPRegionNoti.Box> box) {
		for (S_FreePVPRegionNoti.Box val : box) {
			writeRaw(0x1a);
			writeBytesWithLength(val._bytes);
		}
	}
	
	public static class Box {
		private int _sx;
		private int _sy;
		private int _ex;
		private int _ey;
		private byte[] _bytes;
		
		public Box(int sx, int sy, int ex, int ey) {
			_sx = sx;
			_sy = sy;
			_ex = ex;
			_ey = ey;
			if (_sx > 0) {
				setBytes();
			}
		}

		public int get_sx() {
			return _sx;
		}
		public int get_sy() {
			return _sy;
		}
		public int get_ex() {
			return _ex;
		}
		public int get_ey() {
			return _ey;
		}
		
		void setBytes() {
			BinaryOutputStream os = null;
			try {
				os = new BinaryOutputStream();
				os.writeC(0x08);
				os.writeBit(_sx);
				
				os.writeC(0x10);
				os.writeBit(_sy);
				
				os.writeC(0x18);
				os.writeBit(_ex);
				
				os.writeC(0x20);
				os.writeBit(_ey);
				_bytes = os.getBytes();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close();
						os = null;
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
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
		return S_FREE_PVP_REGION_NOTI;
	}
}


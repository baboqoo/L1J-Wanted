package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.BinaryOutputStream;

public class S_BuilderListNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_BUILDER_LIST_NOTI = "[S] S_BuilderListNoti";
	public static final int NOTI = 0x0079;
	
	public S_BuilderListNoti(java.util.LinkedList<S_BuilderListNoti.BUILDER_INFO> builders) {
		write_init();
		if (builders != null && !builders.isEmpty()) {
			write_builders(builders);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_builders(java.util.LinkedList<S_BuilderListNoti.BUILDER_INFO> builders) {
		for (S_BuilderListNoti.BUILDER_INFO builder : builders) {
			writeC(0x0a);// builders
			writeBytesWithLength(get_builder(builder));
		}
	}
	
	byte[] get_builder(S_BuilderListNoti.BUILDER_INFO builder){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x0a);// buildername
			os.writeBytesWithLength(builder._buildername);
				
			os.writeC(0x10);// serverno
			os.writeBit(builder._serverno);
			
			os.writeC(0x1a);// ip
			os.writeStringWithLength(builder._ip);
			return os.getBytes();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static class BUILDER_INFO {
		private byte[] _buildername;
		private int _serverno;
		private String _ip;
		public BUILDER_INFO(byte[] buildername, int serverno, String ip) {
			this._buildername	= buildername;
			this._serverno		= serverno;
			this._ip			= ip;
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
		return S_BUILDER_LIST_NOTI;
	}
}

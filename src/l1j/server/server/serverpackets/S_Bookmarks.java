package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Bookmarks extends ServerBasePacket {
	private static final String _S_Bookmarks = "[S] S_Bookmarks";
	private byte[] _byte = null;

	public S_Bookmarks(String name, int map, int x, int y, int bookid) {
		buildPacket(name, map, x, y, bookid);
	}

	private void buildPacket(String name, int map, int x, int y, int bookid) {
		writeC(Opcodes.S_ADD_BOOKMARK);
		writeS(name);
		writeH(map);
		writeH(x);
		writeH(y);
		writeD(bookid);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = getBytes();
		return _byte;
	}
	@Override
	public String getType() {
		return _S_Bookmarks;
	}
}

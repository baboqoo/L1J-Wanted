package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_OutputRawString extends ServerBasePacket {
	private static final String S_OUTPUT_RAW_STRING = "[S] S_OutputRawString";
	private static final String HTML_DEPOSIT = "deposit";
	private byte[] _byte = null;

	public S_OutputRawString(int objId, String title, String content) {
		if (content.length() > 0) {
			buildPacket(objId, HTML_DEPOSIT, title, content);
		} else {
			close(objId);
		}
	}
	
	public S_OutputRawString(int objId, String html, String title, String content) {
		if (content.length() > 0) {
			buildPacket(objId, html, title, content);
		} else {
			close(objId);
		}
	}

	public void close(int objId) {
		buildPacket(objId, null, null, null);
	}

	private void buildPacket(int objId, String html, String title, String content) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(objId);
		writeS(html);
		writeH(0x02);
		writeH(0x02);
		writeS(title);
		writeS(content);
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
		return S_OUTPUT_RAW_STRING;
	}
}


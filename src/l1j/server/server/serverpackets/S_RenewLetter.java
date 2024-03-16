package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_RenewLetter extends ServerBasePacket{
	private static final String S_RENEWLETTER = "[S] S_RenewLetter";
	private byte[] _byte = null;
	
	public S_RenewLetter(L1PcInstance pc, int type, int id) {
		buildPacket(pc,type,id);
	}
	private void buildPacket(L1PcInstance pc, int type, int id) {
		writeC(Opcodes. S_MAIL_INFO);
	    writeC(type); // 0:메일함 1:혈맹메일함 2:보관함
	    writeD(id);  // 게시물 넘버
	    writeC(1); 
	}
	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = getBytes();
		return _byte;
	}
	
	public String getType() {
		return S_RENEWLETTER;
	}
}

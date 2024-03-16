package l1j.server.server.serverpackets.polymorph;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ShowPolyList extends ServerBasePacket {
	public S_ShowPolyList(int objid) {
		this(objid, "monlist");
	}
	
	public S_ShowPolyList(int objid, String str){
		writeC(Opcodes.S_HYPERTEXT);
		writeD(objid);
		writeS(str);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


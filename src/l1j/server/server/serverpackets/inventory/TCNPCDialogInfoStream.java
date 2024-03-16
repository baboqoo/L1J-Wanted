package l1j.server.server.serverpackets.inventory;

import l1j.server.server.utils.BinaryOutputStream;

public class TCNPCDialogInfoStream extends BinaryOutputStream {
	
	protected TCNPCDialogInfoStream(int LinkReq, int index, int npcId) {
		super();
		write_LinkReq(LinkReq);
		write_index(index);
		write_npcId(npcId);
	}
	
	void write_LinkReq(int LinkReq) {
		writeC(0x08);// LinkReq
		writeC(LinkReq);
	}
	
	void write_index(int index) {
		writeC(0x10);// index
		writeC(index);
	}
	
	void write_npcId(int npcId) {
		writeC(0x18);// npcId
		writeBit(npcId);
	}
	
}


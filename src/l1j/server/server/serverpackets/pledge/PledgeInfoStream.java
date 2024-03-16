package l1j.server.server.serverpackets.pledge;

import l1j.server.server.model.L1Clan;
import l1j.server.server.utils.BinaryOutputStream;

public class PledgeInfoStream extends BinaryOutputStream {
	
	public PledgeInfoStream(L1Clan pledge) {
		super();
		write_pledge_id(pledge.getClanId());
		write_pledge_name(pledge.getClanName());
		write_master_name(pledge.getLeaderName());
		write_emblem_id(pledge.getEmblemId());
	}
	
	void write_pledge_id(int pledge_id) {
		writeC(0x08);
		writeBit(pledge_id);
	}
	
	void write_pledge_name(String pledge_name) {
		writeC(0x12);
		writeStringWithLength(pledge_name);
	}
	
	void write_master_name(String master_name) {
		writeC(0x1a);
		writeStringWithLength(master_name);
	}
	
	void write_emblem_id(int emblem_id) {
		writeC(0x20);
		writeBit(emblem_id);
	}
}


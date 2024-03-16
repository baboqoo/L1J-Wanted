package l1j.server.server.serverpackets.pledge;

import l1j.server.server.model.L1Clan;
import l1j.server.server.utils.BinaryOutputStream;

public class BloodPledgeListInfoStream extends BinaryOutputStream {
	
	public BloodPledgeListInfoStream(L1Clan pledge) {
		super();
		write_pledge_name(pledge.getClanName());
		write_master_name(pledge.getLeaderName());
		write_emblem_id(pledge.getEmblemId());
		write_member_count(pledge.getClanMemberList().size());
		write_introduction_message(pledge.getIntroductionMessage());
		write_weekly_contribution_total(pledge.getContribution());
		write_join_type(pledge.getJoinType().toInt());
		write_enable_join(pledge.isEnableJoin());
	}
	
	void write_pledge_name(String pledge_name) {
		writeC(0x0a);// pledge_name
		writeStringWithLength(pledge_name);
	}
	
	void write_master_name(String master_name) {
		writeC(0x12);// master_name
		writeStringWithLength(master_name);
	}
	
	void write_emblem_id(int emblem_id) {
		writeC(0x18);// emblem_id
		writeBit(emblem_id);
	}
	
	void write_member_count(int member_count) {
		writeC(0x20);// member_count
		writeC(member_count);
	}
	
	void write_introduction_message(String introduction_message) {
		writeC(0x2a);// introduction_message
		writeStringWithLength(introduction_message);
	}
	
	void write_weekly_contribution_total(int weekly_contribution_total) {
		writeC(0x30);// weekly_contribution_total
		writeBit(weekly_contribution_total);
	}
	
	void write_join_type(int join_type) {
		writeC(0x38);// join_type
		writeC(join_type);
	}
	
	void write_enable_join(boolean enable_join) {
		writeC(0x40);// enable_join
		writeB(enable_join);
	}
}


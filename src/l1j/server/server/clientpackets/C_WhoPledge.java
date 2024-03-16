package l1j.server.server.clientpackets;

import java.util.ArrayList;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.pledge.S_BlessOfBloodPledgeUpdateNoti;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeContribution;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeInfo;

public class C_WhoPledge extends ClientBasePacket {
	private static final String C_WHO_PLEDGE = "[C] C_WhoPledge";

	public C_WhoPledge(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		if (pc.getClanid() <= 0) {
			pc.sendPackets(L1ServerMessage.sm1064);
			return;
		}
		L1Clan clan = pc.getClan();
		pc.sendPackets(new S_BloodPledgeInfo(clan.getClanId()), true);
		ArrayList<ClanMember> members = clan.getClanMemberList();
		ArrayList<ClanMember> page1 = new ArrayList<ClanMember>(127);
		ArrayList<ClanMember> page2 = new ArrayList<ClanMember>(127);
		ArrayList<ClanMember> page3 = new ArrayList<ClanMember>(127);
		int o = 0;
		try {
            o = members.size() / 90;
        } catch (Exception e) {
        }
		if (o > 0) {// 총 127명이 넘었을때..
			for (int i = 0; i < members.size(); i++) {
				if (i < 127) {
					page1.add(members.get(i));
				} else if (i < 256) {
					page2.add(members.get(i));
				} else {
					page3.add(members.get(i));
				}
			}

			if (page3.size() > 0) {
				o = 3;
			} else if (page2.size() > 0) {
				o = 2;
			} else {
				o = 1;
			}
			if (page1.size() > 0) {
				pc.sendPackets(new S_BloodPledgeInfo(pc, o, 0, page1), true);
			}
			if (page2.size() > 0) {
				pc.sendPackets(new S_BloodPledgeInfo(pc, o, 1, page2), true);
			}
			if (page3.size() > 0) {
				pc.sendPackets(new S_BloodPledgeInfo(pc, o, 2, page3), true);
			}
		} else { // 127 이하..
			for (int i = 0; i < members.size(); i++) {
                page1.add(members.get(i));
			}
            pc.sendPackets(new S_BloodPledgeInfo(pc, 1, 0, page1), true);
		}
		page1.clear();
		page2.clear();
		page3.clear();
		page1 = null;
		page2 = null;
		page3 = null;
		
		ServerBasePacket alliancePck = clan.getAlliancePck();// 동맹 정보
		if (alliancePck != null) {
			pc.sendPackets(alliancePck);
		}
		
		pc.sendPackets(new S_BloodPledgeContribution(clan.getContribution()), true);
		pc.sendPackets(new S_BloodPledgeInfo(clan.getOnlineClanMember()), true);
		pc.sendPackets(new S_BlessOfBloodPledgeUpdateNoti(clan), true);
	}

	@Override
	public String getType() {
		return C_WHO_PLEDGE;
	}

}


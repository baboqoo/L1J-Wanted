package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeInfo;
import l1j.server.server.utils.StringUtil;

public class C_PledgeContent extends ClientBasePacket {
	private static final String C_PledgeContent = "[C] C_PledgeContent";
	
	private static final int NOTICE	= 15;
	private static final int MEMO	= 16;

	public C_PledgeContent(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.getClanid() == 0) {
			return;
		}
		int type = readC();
		switch(type){
		case NOTICE:{
			String announce = readS().replaceAll(StringUtil.MinusString, StringUtil.EmptyString);
			if (announce.getBytes().length > 270) {
				pc.sendPackets(L1ServerMessage.sm7996);// 공지 내용이 너무 깁니다. (최대 270 bytes)
				return;
			}
			L1Clan clan = ClanTable.getInstance().getTemplate(pc.getClanid());
			if (clan != null) {
				clan.setAnnouncement(announce);
				ClanTable.getInstance().updateClan(clan);
				pc.sendPackets(new S_BloodPledgeInfo(announce), true);
			} else {
				pc.sendPackets(L1ServerMessage.sm1064);
			}
		}
			break;
		case MEMO:{
			L1Clan clan = pc.getClan();
			if (clan != null) {
				String notes = readS();
				clan.getClanMember(pc.getId()).notes = notes;
				pc.setClanMemberNotes(notes);
				pc.sendPackets(new S_BloodPledgeInfo(pc.getName(), notes), true);
				try {
					pc.save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm1064);
			}
		}
			break;
		}
	}

	@Override
	public String getType() {
		return C_PledgeContent;
	}

}


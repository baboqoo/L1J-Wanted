package l1j.server.server.model;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.construct.L1BeginnerQuest;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanAttentionTable;
import l1j.server.server.datatables.ClanJoinningTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.pledge.S_Pledge;
import l1j.server.server.serverpackets.pledge.S_PledgeWatch;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeEmblem;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeInfo;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoin;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeMemberChanged;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeMemberChanged.MemberChangedReason;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeUserInfo;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.world.BloodPledgeDAO;
import l1j.server.web.dispatcher.response.world.BloodPledgeVO;

public class L1ClanJoin {
	private static Logger _log = Logger.getLogger(L1ClanJoin.class.getName());
	private static class newInstance {
		public static final L1ClanJoin INSTANCE = new L1ClanJoin();
	}
	public static L1ClanJoin getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ClanJoin() { }
	
	public int getLimitMemberCount(int leader_level, boolean quest45, int charisma) {
		if (Config.PLEDGE.PLEDGE_LIMIT_MEMBER_COUNT > 0) {// Clan 인원수의 상한의 설정 있어
			return Config.PLEDGE.PLEDGE_LIMIT_MEMBER_COUNT;
		}
		return leader_level >= 50 ? (quest45 ? charisma * 9 : charisma * 3) : (quest45 ? charisma * 6 : charisma << 1);
	}
	
	public S_BloodPledgeJoin join(L1Clan pldege, L1PcInstance joinPc){
		int pledge_id 		= pldege.getClanId();
		String pledge_name 	= pldege.getClanName();
		
		if (pledge_id != Config.PLEDGE.BEGINNER_PLEDGE_ID) {
			LeaderInfo info = this.getOfflinePledgeLeaderInfo(pldege.getLeaderId());	
			if (info == null) {
				return S_BloodPledgeJoin.NO_PLEDGE_MEMBER_IN_WORLD;
			}
			int maxMember = getLimitMemberCount(info.lvl, info.is45Quest, info.cha);
			if (maxMember <= pldege.getClanMemberList().size()) {
				return S_BloodPledgeJoin.PLEDGE_IS_FULL;
			}
		}
		
		joinPc.setClan(pldege);
		S_ServerMessage join_message = new S_ServerMessage(94, joinPc.getName());// 혈맹: {0}%o 혈맹의 일원으로 받아 들임
		for (L1PcInstance clanMembers : pldege.getOnlineClanMember()) {
			clanMembers.sendPackets(join_message);
		}
		join_message.clear();
		
		int join_date = (int)(System.currentTimeMillis() / 1000);
		
		joinPc.setClanid(pledge_id);
		joinPc.setClanName(pledge_name);
		joinPc.setBloodPledgeRank(eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT);
		joinPc.setClanMemberNotes(StringUtil.EmptyString);
		joinPc.setTitle(StringUtil.EmptyString);
		joinPc.setPledgeJoinDate(join_date);
		joinPc.broadcastPacketWithMe(new S_CharTitle(joinPc.getId(), StringUtil.EmptyString), true);
		try {
			joinPc.save(); // DB에 캐릭터 정보를 기입한다
		} catch(Exception e) {
		}
		pldege.addClanMember(joinPc.getName(), joinPc.getBloodPledgeRank(), joinPc.getLevel(), StringUtil.EmptyString, joinPc.getId(), joinPc.getType(), joinPc.getOnlineStatus(), 
				joinPc.getClanContribution(), joinPc.getClanWeekContribution(), 
				join_date, (joinPc.getLastLogoutTime() == null ? 0 : (int)(joinPc.getLastLogoutTime().getTime() / 1000)),
				joinPc);
		joinPc.sendPackets(new S_BloodPledgeUserInfo(pldege.getClanName(), joinPc.getBloodPledgeRank(), false), true);
		
		for (L1PcInstance player : pldege.getOnlineClanMember()) {
			joinPc.sendPackets(new S_BloodPledgeMemberChanged(MemberChangedReason.ADD_ME, player.getId()), true);// 혈맹원정보
		}
		joinPc.sendPackets(new S_BloodPledgeMemberChanged(MemberChangedReason.ADD, joinPc.getId()), true);// 혈맹원정보
		
		joinPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT.toInt(), joinPc.getName()), true);
		joinPc.sendPackets(new S_Pledge(joinPc, pldege.getEmblemId(), joinPc.getBloodPledgeRank()), true);	
		joinPc.sendPackets(new S_BloodPledgeEmblem(joinPc.getId(), pledge_id), true);
		joinPc.sendPackets(new S_BloodPledgeInfo(pldege.getEmblemStatus() == 1), true);
		joinPc.sendPackets(new S_ServerMessage(95, pledge_name), true); // \f1%0z혈맹에 가입하였습니다.
		
		S_BloodPledgeEmblem emblem = new S_BloodPledgeEmblem(joinPc.getId(), joinPc.getClan().getEmblemId());
		for (L1PcInstance member : pldege.getOnlineClanMember()) {
			member.broadcastPacketWithMe(emblem, false);
		}
		emblem.clear();
		emblem = null;
		
		pledge_join_object(joinPc);

		if (joinPc.getClanid() != 0 && joinPc.getLevel() <= Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL) {
			joinPc.getQuest().questProcess(L1BeginnerQuest.PLEDGE_JOIN);
		}
		L1BuffUtil.pledge_contribution_buff(joinPc);
		
		ClanJoinningTable.getInstance().delete(joinPc);
		if (Config.WEB.WEB_SERVER_ENABLE) {
			BloodPledgeVO webClan = BloodPledgeDAO.getPledge(pledge_id);
			if (webClan != null) {
				webClan.setTotalMember(pldege.getClanMemberList().size());
			}
		}
		return S_BloodPledgeJoin.JOIN_OK;
	}
	
	public boolean join(L1PcInstance pc, L1PcInstance joinPc){
		int pledge_id		= pc.getClanid();
		String pledge_name	= pc.getClanName();
		L1Clan pledge		= pc.getClan();
		if (pledge == null) {
			return false;
		}
		int charisma = pc.getId() != pledge.getLeaderId() ? pc.getAbility().getTotalCha() : getOfflinePledgeLeaderCha(pledge.getLeaderId());
		boolean lv45quest = false;
		if (pc.getQuest().isEnd(L1Quest.QUEST_LEVEL45)) {
			lv45quest = true;
		}
		int maxMember = getLimitMemberCount(pc.getLevel(), lv45quest, charisma);
		
		if (joinPc.getClanid() == 0) { // 크란미가입
			if (maxMember <= pledge.getClanMemberList().size()) {
				joinPc.sendPackets(new S_ServerMessage(188, pc.getName()), true);// 혈맹: {0}%s 당신을 혈맹 일원으로 받아들일 수 없음
				return false;
			}
			
			joinPc.setClan(pledge);
			S_ServerMessage join_message = new S_ServerMessage(94, joinPc.getName());// 혈맹: {0}%o 혈맹의 일원으로 받아 들임
			for (L1PcInstance clanMembers : pledge.getOnlineClanMember()) {
				clanMembers.sendPackets(join_message);
			}
			join_message.clear();
			
			int join_date = (int)(System.currentTimeMillis() / 1000);

			joinPc.setClanid(pledge_id);
			joinPc.setClanName(pledge_name);
			joinPc.setBloodPledgeRank(eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT);
			joinPc.setClanMemberNotes(StringUtil.EmptyString);
			joinPc.setTitle(StringUtil.EmptyString);
			joinPc.setPledgeJoinDate(join_date);
			joinPc.broadcastPacketWithMe(new S_CharTitle(joinPc.getId(), StringUtil.EmptyString), true);
			
			try {
				joinPc.save(); // DB에 캐릭터 정보를 기입한다
			} catch(Exception e) {}
			
			pledge.addClanMember(joinPc.getName(), joinPc.getBloodPledgeRank(), joinPc.getLevel(), StringUtil.EmptyString, joinPc.getId(), joinPc.getType(), joinPc.getOnlineStatus(), 
					joinPc.getClanContribution(), joinPc.getClanWeekContribution(), 
					join_date, (joinPc.getLastLogoutTime() == null ? 0 : (int)(joinPc.getLastLogoutTime().getTime() / 1000)),
					joinPc);
			
			joinPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT.toInt(), joinPc.getName()), true);
			
			joinPc.sendPackets(new S_ServerMessage(95, pledge_name), true); // \f1%0z 혈맹에 가입햇습니다.
			joinPc.sendPackets(new S_Pledge(joinPc, pledge.getEmblemId(), joinPc.getBloodPledgeRank()), true);	

			joinPc.sendPackets(new S_BloodPledgeUserInfo(pledge_name, joinPc.getBloodPledgeRank(), false), true);
			joinPc.sendPackets(new S_BloodPledgeEmblem(joinPc.getId(), pledge_id), true);
			
			joinPc.sendPackets(new S_BloodPledgeInfo(pc.getClan().getEmblemStatus() == 1), true);
			String[] clanList = ClanAttentionTable.getInstance().getAttentionClanlist(pc.getClanName());
			joinPc.sendPackets(new S_PledgeWatch(clanList), true);
			
			S_BloodPledgeEmblem emblem = new S_BloodPledgeEmblem(joinPc.getId(), joinPc.getClan().getEmblemId());
			for (L1PcInstance member : pledge.getOnlineClanMember()) {
				member.broadcastPacketWithMe(emblem, false);
			}
			emblem.clear();
			emblem = null;
			
			pledge_join_object(joinPc);
			
			// 혈맹에 가입했습니다.
			if (joinPc.getClanid() != 0 && joinPc.getLevel() <= Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL) {
				joinPc.getQuest().questProcess(L1BeginnerQuest.PLEDGE_JOIN);
			}
			L1BuffUtil.pledge_contribution_buff(joinPc);
			ClanJoinningTable.getInstance().delete(joinPc);
		} else { // 크란 가입이 끝난 상태(크란 연합)
			if (Config.PLEDGE.CROWN_OTHER_PLEDGE_JOIN_ENABLE) {
				changePledge(pc, joinPc, maxMember);
			} else {
				joinPc.sendPackets(L1ServerMessage.sm89); // \f1당신은벌써혈맹에가입하고있습니다.
			}
		}
		
		if (Config.WEB.WEB_SERVER_ENABLE) {
			BloodPledgeVO webClan = BloodPledgeDAO.getPledge(pledge_id);
			if (webClan != null) {
				webClan.setTotalMember(pledge.getClanMemberList().size());
			}
		}
		return true;
	}
	
	/**
	 * 가입한 캐릭터로 부터 오브젝트 처리
	 * @param joinPc
	 */
	public void pledge_join_object(L1PcInstance joinPc) {
		boolean is_invis		= joinPc.isInvisble();
		boolean is_party		= joinPc.isInParty();
		boolean is_floatingEye	= joinPc.isFloatingEye();
		L1DollInstance doll		= joinPc.getDoll();
		int pledge_id			= joinPc.getClanid();
		S_PCObject obj_pck		= null;
		S_NPCObject doll_pck	= null;
		for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(joinPc, -1)) {// 화면 내 캐릭터
			boolean is_pledge				= pledge_id != 0 && pledge_id == member.getClanid();
			boolean is_party_member 		= is_party && joinPc.getParty().isMember(member);
			boolean is_memberfloatingEye	= member.isFloatingEye();
			
			// 가입한 캐릭터 오브젝트를 인식할 수 있는 상태
			if (!is_invis || (is_invis && (is_pledge || is_party_member || is_memberfloatingEye))) {
				if (obj_pck == null) {
					obj_pck = new S_PCObject(joinPc);
				}
				member.sendPackets(obj_pck);
			}
			
			// 기존 파티원은 오브젝트 인식을 하지 않는다.
			if (is_party_member) {
				continue;
			}
			
			// 가입한 캐릭터의 인형 오브젝트를 인식할 수 있는 상태(기존 인형을 인식 못하였던 경우 인식)
			if (is_invis && doll != null) {
				if (doll_pck == null) {
					doll_pck = new S_NPCObject(doll);
				}
				member.sendPackets(doll_pck);
			}
			
			// 혈맹원이 투명 상태(기존 파티원은 이미 인식 처리를 하고있음)
			if (member.isInvisble()) {
				if (is_floatingEye) {
					// 인형 오브젝트 인식
					if (member.getDoll() != null) {
						joinPc.sendPackets(new S_NPCObject(member.getDoll()), true);
					}
				} else {
					// 혈맹원 오브젝트와 인형 오브젝트 인식
					joinPc.sendPackets(new S_PCObject(member), true);
					if (member.getDoll() != null) {
						joinPc.sendPackets(new S_NPCObject(member.getDoll()), true);
					}
				}
			}
		}
		if (obj_pck != null) {
			obj_pck.clear();
			obj_pck = null;
		}
		if (doll_pck != null) {
			doll_pck.clear();
			doll_pck = null;
		}
	}

	/**
	 * 혈맹 이전
	 * @param pc
	 * @param joinPc
	 * @param maxMember
	 */
	private void changePledge(L1PcInstance pc, L1PcInstance joinPc, int maxMember) {
		int pledge_id		= pc.getClanid();
		String pledge_name	= pc.getClanName();
		L1Clan pledge		= pc.getClan();
		L1Clan old_pledge	= joinPc.getClan();
		if (pledge == null || old_pledge == null || joinPc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING) {
			return;
		}
		
		ArrayList<ClanMember> old_pledge_members = old_pledge.getClanMemberList();
		if (maxMember < pledge.getClanMemberList().size() + old_pledge_members.size()) {// 빈 곳이 없다
			joinPc.sendPackets(new S_ServerMessage(188, pc.getName()), true);// %0는당신을혈맹원으로서받아들일수가없습니다.
			return;
		}
		
		int join_date = (int)(System.currentTimeMillis() / 1000);
		L1PcInstance clanMember[] = pledge.getOnlineClanMember();
		S_ServerMessage king_join_message = new S_ServerMessage(94, joinPc.getName());
		for (L1PcInstance member : clanMember) {
			member.sendPackets(king_join_message);// \f1%0이혈맹의일원으로서받아들여졌습니다.
		}
		king_join_message.clear();
		king_join_message = null;
		
		L1World world = L1World.getInstance();
		
		S_ServerMessage join_message	= new S_ServerMessage(95, pledge_name);
		S_BloodPledgeInfo emblem_status		= new S_BloodPledgeInfo(pledge.getEmblemStatus() == 1);
		
		// 기존 혈맹의 유저들을 이갑시킨다.
		for (ClanMember old_member : old_pledge_members) {
			L1PcInstance oldClanMember = world.getPlayer(old_member.name);
			if (oldClanMember != null) { // 온라인중의 구크란 멤버
				oldClanMember.setClan(pledge);
				oldClanMember.setClanid(pledge_id);
				oldClanMember.setClanName(pledge_name);
				oldClanMember.setBloodPledgeRank(eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT);
				oldClanMember.setPledgeJoinDate(join_date);
				
				try {
					// DB에 캐릭터 정보를 기입한다
					oldClanMember.save();
				} catch (Exception e) {
					_log.log(Level.SEVERE, "C_Attr[changeClan]Error", e);
				}
				pledge.addClanMember(oldClanMember.getName(), oldClanMember.getBloodPledgeRank(), oldClanMember.getLevel(), oldClanMember.getClanMemberNotes(), oldClanMember.getId(), oldClanMember.getType(), oldClanMember.getOnlineStatus(), 
						oldClanMember.getClanContribution(), oldClanMember.getClanWeekContribution(), 
						join_date, (oldClanMember.getLastLogoutTime() == null ? 0 : (int)(oldClanMember.getLastLogoutTime().getTime() / 1000)),
						oldClanMember);

				oldClanMember.sendPackets(join_message);
				oldClanMember.sendPackets(new S_Pledge(oldClanMember, pledge.getEmblemId(), oldClanMember.getBloodPledgeRank()), true);	
				oldClanMember.sendPackets(new S_BloodPledgeEmblem(oldClanMember.getId(), pledge_id), true);

				oldClanMember.sendPackets(emblem_status);
				oldClanMember.sendPackets(new S_BloodPledgeUserInfo(pledge.getClanName(), oldClanMember.getBloodPledgeRank(), false), true);
				
				S_BloodPledgeEmblem emblem = new S_BloodPledgeEmblem(oldClanMember.getId(), oldClanMember.getClan().getEmblemId());
				for (L1PcInstance member : pledge.getOnlineClanMember()) {
					member.broadcastPacketWithMe(emblem, false);
				}
				emblem.clear();
				emblem = null;
				
				pledge_join_object(oldClanMember);
				
				// \f1%0 혈맹에 가입했습니다.
			} else { // 오프 라인중의 구크란 멤버
				try {
					L1PcInstance offClanMember = CharacterTable.getInstance().restoreCharacter(old_member.name);
					offClanMember.setClanid(pledge_id);
					offClanMember.setClanName(pledge_name);
					offClanMember.setBloodPledgeRank(eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT);
					offClanMember.setPledgeJoinDate(join_date);
					offClanMember.save();
					pledge.addClanMember(offClanMember.getName(), offClanMember.getBloodPledgeRank(), offClanMember.getLevel(), offClanMember.getClanMemberNotes(), offClanMember.getId(), offClanMember.getType(), offClanMember.getOnlineStatus(), 
							offClanMember.getClanContribution(), offClanMember.getClanWeekContribution(), 
							join_date, (offClanMember.getLastLogoutTime() == null ? 0 : (int)(offClanMember.getLastLogoutTime().getTime() / 1000)),
							offClanMember);
				} catch (Exception e) {
					_log.log(Level.SEVERE, "C_Attr[changeClan]Error", e);
				}
			}
		}
		
		join_message.clear();
		join_message = null;
		
		emblem_status.clear();
		emblem_status = null;

		// 이전 혈맹 문장 삭제
		String emblem_file = String.valueOf(old_pledge.getEmblemId());
		File file = new File("emblem/" + emblem_file);
		if (file.exists()) {
			file.delete();
		}
		ClanTable.getInstance().deleteClan(old_pledge);
	}
	// 오프라인중의 군주 카리스마
	public int getOfflinePledgeLeaderCha(int member) {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstm = null;
		java.sql.ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT Cha FROM characters WHERE objid=?");
			pstm.setInt(1, member);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return 0;
			}
			return rs.getInt("Cha");
		} catch (SQLException e) {
			_log.warning("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}

	public LeaderInfo getOfflinePledgeLeaderInfo(int objid) {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstm = null;
		java.sql.ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT char_name, level, Cha FROM characters where objid=?");
			pstm.setInt(1, objid);
			rs = pstm.executeQuery();
			if(!rs.next())return null;
			
			LeaderInfo info = new LeaderInfo();
			info.name		= rs.getString("char_name");
			info.cha 		= rs.getInt("Cha");
			info.lvl 		= rs.getInt("level");
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM character_quests where char_id=? and quest_id=?");
			pstm.setInt(1, objid);
			pstm.setInt(2, L1Quest.QUEST_LEVEL45);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				info.is45Quest 	= false;
			} else {
				info.is45Quest	= rs.getInt("quest_step") == 0xff;
			}
			return info;
		} catch (SQLException e) {
			_log.warning("could not check existing charname:" + e.getMessage());
			System.out.println("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	class LeaderInfo {
		public String 	name;
		public int 		lvl;
		public int 		cha;
		public boolean 	is45Quest;
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder(128);
			sb.append("NAME : ").append(name).append("\n");
			sb.append("LEVEL : ").append(lvl).append("\n");
			sb.append("CHARISMA : ").append(cha).append("\n");
			sb.append("QUESTSTATE : ").append(is45Quest).append("\n");
			return sb.toString();
		}
	}
}

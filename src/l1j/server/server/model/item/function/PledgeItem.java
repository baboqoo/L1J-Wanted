package l1j.server.server.model.item.function;

import java.sql.Timestamp;

import l1j.server.IndunSystem.clandungeon.ClanDungeonCreator;
import l1j.server.IndunSystem.clandungeon.ClanDungeonType;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ClanContributionBuffTable;
import l1j.server.server.datatables.ClanContributionBuffTable.ContributionBuff;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1BuffUtil.PLEDGE_CONTRIBUTION_BUFF_TYPE;
import l1j.server.server.templates.L1House;

public class PledgeItem {
	
	/**
	 * 혈맹 공헌도 버프 사용
	 * @param pc
	 * @param item
	 * @param itemid
	 */
	public static void contributionBuff(L1PcInstance pc, L1ItemInstance item, int itemid){ 
		if (pc.getClanid() == 0) {
			pc.sendPackets(L1ServerMessage.sm1064);// 혈맹에 소속되어 있지 않습니다.(/파워북 혈맹매칭)
			return;
		}
		ClanContributionBuffTable contribution	=	ClanContributionBuffTable.getInstance();
		ContributionBuff buff		=	contribution.getContributionBuff(pc.getClanid());
		String pledge_name			=	pc.getClanName();
		int exp_buff_type			=	buff != null ? buff.exp_buff_type : 0;
		int battle_buff_type		=	buff != null ? buff.battle_buff_type : 0;
		int defens_buff_type		=	buff != null ? buff.defens_buff_type : 0;
		Timestamp exp_buff_time		=	buff != null ? buff.exp_buff_time : null;
		Timestamp battle_buff_time	=	buff != null ? buff.battle_buff_time : null;
		Timestamp defens_buff_time	=	buff != null ? buff.defens_buff_time : null;
		PLEDGE_CONTRIBUTION_BUFF_TYPE buff_type = null;
		int time=0;
		long systime				=	System.currentTimeMillis();
		switch(itemid){
		case 5990:// 혈맹 성장 버프 I (1일)
			buff_type = PLEDGE_CONTRIBUTION_BUFF_TYPE.EXP_I;
			exp_buff_type = 1;
			time = (24 * 3600);
			if (exp_buff_time == null) {
				exp_buff_time = new Timestamp(systime + (time * 1000));
			} else {
				exp_buff_time.setTime(systime + (time * 1000));
			}
			break;
		case 5991:// 혈맹 성장 버프 II (1일)
			buff_type = PLEDGE_CONTRIBUTION_BUFF_TYPE.EXP_II;
			exp_buff_type = 2;
			time = (24 * 3600);
			if (exp_buff_time == null) {
				exp_buff_time = new Timestamp(systime + (time * 1000));
			} else {
				exp_buff_time.setTime(systime + (time * 1000));
			}
			break;
		case 5992:// 혈맹 전투 버프 I (2시간)
			buff_type = PLEDGE_CONTRIBUTION_BUFF_TYPE.BATTLE_I;
			battle_buff_type = 1;
			time = (2 * 3600);
			if (battle_buff_time == null) {
				battle_buff_time = new Timestamp(systime + (time * 1000));
			} else {
				battle_buff_time.setTime(systime + (time * 1000));
			}
			break;
		case 5993:// 혈맹 전투 버프 II (2시간)
			buff_type = PLEDGE_CONTRIBUTION_BUFF_TYPE.BATTLE_II;
			battle_buff_type = 2;
			time = (2 * 3600);
			if (battle_buff_time == null) {
				battle_buff_time = new Timestamp(systime + (time * 1000));
			} else {
				battle_buff_time.setTime(systime + (time * 1000));
			}
			break;
		case 5998:// 혈맹 방어 버프 I (2시간)
			buff_type = PLEDGE_CONTRIBUTION_BUFF_TYPE.DEFENS_I;
			defens_buff_type = 1;
			time = (2 * 3600);
			if (defens_buff_time == null) {
				defens_buff_time = new Timestamp(systime + (time * 1000));
			} else {
				defens_buff_time.setTime(systime + (time * 1000));
			}
			break;
		case 5999:// 혈맹 방어 버프 II (2시간)
			buff_type = PLEDGE_CONTRIBUTION_BUFF_TYPE.DEFENS_II;
			defens_buff_type = 2;
			time = (2 * 3600);
			if (defens_buff_time == null) {
				defens_buff_time = new Timestamp(systime + (time * 1000));
			} else {
				defens_buff_time.setTime(systime + (time * 1000));
			}
			break;
		}
		
		L1PcInstance[] clanMembers = pc.getClan().getOnlineClanMember();
		
		if (buff == null) {
			ContributionBuff creatbuff = new ContributionBuff(pledge_name, exp_buff_type, exp_buff_time, battle_buff_type, battle_buff_time, defens_buff_type, defens_buff_time);
			contribution.addContributionBuff(pc.getClanid(), creatbuff);
			contribution.insertBuff(pc.getClanid(), creatbuff);
		} else {
			L1BuffUtil.pledge_contribution_buff_dispose(buff_type, clanMembers);
			buff.exp_buff_type		= exp_buff_type;
			buff.exp_buff_time		= exp_buff_time;
			buff.battle_buff_type	= battle_buff_type;
			buff.battle_buff_time	= battle_buff_time;
			buff.defens_buff_type	= defens_buff_type;
			buff.defens_buff_time	= defens_buff_time;
			contribution.updateBuff(pc.getClanid(), buff);
		}
		
		// 접속중인 혈맹원
		eBloodPledgeRankType rank	= null;
		int buff_id					= 0;
		for (L1PcInstance member : clanMembers) {
			rank	= member.getBloodPledgeRank();
			buff_id	= L1BuffUtil.get_pledge_contribution_buff_id_from_rank(rank, buff_type);
			if (buff_id == 0) {
				continue;
			}
			member.getSkill().setSkillEffect(buff_id, time * 1000);
			L1BuffUtil.pledge_contribution_buff_set(member, buff_id, time, true);
			member.send_effect_self(19406);
		}
		pc.getInventory().removeItem(item, 1);
	}
	
	/**
	 * 혈맹 던전 키 사용
	 * @param pc
	 * @param item
	 * @param itemid
	 * @return boolean
	 */
	public static boolean pledgeDungeonKey(L1PcInstance pc, L1ItemInstance item, int itemid){
		if (pc.getClanid() == 0) {
			pc.sendPackets(L1ServerMessage.sm1064);// 혈맹에 소속되어 있지 않습니다.(/파워북 혈맹매칭)
			return false;
		}
		L1Clan clan = pc.getClan();
		if (clan == null) {
			return false;
		}
		int houseId = clan.getHouseId();
		int catleId = clan.getCastleId();
		if (houseId == 0 && catleId == 0) {
			pc.sendPackets(L1ServerMessage.sm1098);// 아지트: 지하 아지트 없음
			return false;
		}
		int[] loc = null;
		if (catleId > 0) {
			loc = L1CastleLocation.getCastleLoc(pc.getClan().getCastleId());
		} else if (houseId > 0) {
			L1House house = HouseTable.getInstance().getHouseTable(houseId); 
			if (house == null || !house.isPurchaseBasement()) {
				pc.sendPackets(L1ServerMessage.sm1098);// 아지트: 지하 아지트 없음
				return false;
			}
			loc = L1HouseLocation.getBasementLoc(houseId);
		}
		if (pc.getMapId() != loc[2]) {
			pc.sendPackets(L1SystemMessage.USE_NOT_CASTEL_FAIL);
			return false;
		}
		
		ClanDungeonType type = null;
		switch(itemid){
		case 5994:type = ClanDungeonType.DAILY;break;
		case 5995:type = ClanDungeonType.WEEKLY;break;
		case 5997:type = ClanDungeonType.AREA;break;
		default:return false;
		}
		
		ClanDungeonCreator creator = ClanDungeonCreator.getInstance();
		if (creator.getRaidCount(type) > 40) {
			pc.sendPackets(L1SystemMessage.CLAN_DUNGEON_ZONE_MAX);
			return false;
		}
		if (creator.create(pc, type)) {
			pc.getInventory().removeItem(item, 1);
			return true;
		}
		return false;
	}
}


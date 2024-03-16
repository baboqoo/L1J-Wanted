package l1j.server.server.model.skill;

import l1j.server.GameSystem.arca.L1ArcaGrade;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.datatables.ClanContributionBuffTable;
import l1j.server.server.datatables.ClanContributionBuffTable.ContributionBuff;
import l1j.server.server.model.L1AC;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Resistance;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class L1BuffUtil {
	
	public static void haste(L1PcInstance pc, int timeMillis){
		pc.getSkill().setSkillEffect(L1SkillId.STATUS_HASTE, timeMillis);
		pc.broadcastPacketWithMe(new S_SkillHaste(pc.getId(), 1, timeMillis / 1000), true);
		pc.send_effect(191);
		pc.getMoveState().setMoveSpeed(1);
	}

	public static void brave(L1PcInstance pc, int timeMillis){
		pc.getSkill().setSkillEffect(L1SkillId.STATUS_BRAVE, timeMillis);
		pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 1, timeMillis / 1000), true);
		pc.send_effect(751);
		pc.getMoveState().setBraveSpeed(1);
	}
	
	public static void drunken(L1PcInstance pc, int timeMillis){
		pc.broadcastPacketWithMe(new S_Liquor(pc.getId(), 8), true);
		pc.getSkill().setSkillEffect(L1SkillId.STATUS_DRAGON_PEARL, timeMillis);
		pc.getMoveState().setDrunken(8);
		pc.send_effect(13283);
		pc.sendPackets(new S_ServerMessage(1065, timeMillis / 1000), true);
	}
	
	public static void skillAction(L1PcInstance pc, L1Character cha, int skillid, int skillTime){
		L1SkillUse l1skilluse = new L1SkillUse(true);
		l1skilluse.handleCommands(pc, skillid, cha.getId(), cha.getX(), cha.getY(), skillTime, L1SkillUseType.GMBUFF);
		l1skilluse = null;
	}
	
	public static void skillAction(L1PcInstance pc, int skillid){
		L1SkillUse l1skilluse = new L1SkillUse(true);
		l1skilluse.handleCommands(pc, skillid, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
		l1skilluse = null;
	}
	
	public static void skillMotionAction(L1PcInstance pc, int skillid){
		L1SkillUse l1skilluse = new L1SkillUse(true);
		l1skilluse.handleCommands(pc, skillid, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.SPELLSC);
		l1skilluse = null;
	}
	
	public static void skillArrayAction(L1PcInstance pc, int[] skillids){
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (int i = 0; i < skillids.length; i++) {
			l1skilluse.handleCommands(pc, skillids[i], pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
		}
		l1skilluse = null;
		skillids = null;
	}
	
	public static void skillArrayLogin(L1PcInstance pc, int[] skillids){
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (int i = 0; i < skillids.length; i++) {
			l1skilluse.handleCommands(pc, skillids[i], pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.LOGIN);
		}
		l1skilluse = null;
		skillids = null;
	}
	
	public static enum PLEDGE_CONTRIBUTION_BUFF_TYPE {
		EXP_I, EXP_II, BATTLE_I, BATTLE_II, DEFENS_I, DEFENS_II
	}
	
	/**
	 * 혈맹 등급에 따른 버프 아이디 조사
	 * @param rank
	 * @param buff_type
	 * @return buff_id
	 */
	public static int get_pledge_contribution_buff_id_from_rank(eBloodPledgeRankType rank, PLEDGE_CONTRIBUTION_BUFF_TYPE buff_type){
		switch (buff_type) {
		case EXP_I:
			switch (rank) {
			case RANK_NORMAL_KING:
			case RANK_NORMAL_PRINCE:
				return L1SkillId.PLEDGE_EXP_BUFF_I_KING;
			case RANK_NORMAL_KNIGHT:
				return L1SkillId.PLEDGE_EXP_BUFF_I_KNIGHT;
			case RANK_NORMAL_ELITE_KNIGHT:
				return L1SkillId.PLEDGE_EXP_BUFF_I_ELITE;
			default:
				return L1SkillId.PLEDGE_EXP_BUFF_I_NORMAL;
			}
		case EXP_II:
			switch (rank) {
			case RANK_NORMAL_KING:
			case RANK_NORMAL_PRINCE:
				return L1SkillId.PLEDGE_EXP_BUFF_II_KING;
			case RANK_NORMAL_KNIGHT:
				return L1SkillId.PLEDGE_EXP_BUFF_II_KNIGHT;
			case RANK_NORMAL_ELITE_KNIGHT:
				return L1SkillId.PLEDGE_EXP_BUFF_II_ELITE;
			default:
				return L1SkillId.PLEDGE_EXP_BUFF_II_NORMAL;
			}
		case BATTLE_I:
			return eBloodPledgeRankType.isAuthRankAtEliteKnight(rank) ? L1SkillId.PLEDGE_BATTLE_BUFF_I_ELITE : L1SkillId.PLEDGE_BATTLE_BUFF_I_NORMAL;
		case BATTLE_II:
			return eBloodPledgeRankType.isAuthRankAtEliteKnight(rank) ? L1SkillId.PLEDGE_BATTLE_BUFF_II_ELITE : L1SkillId.PLEDGE_BATTLE_BUFF_II_NORMAL;
		case DEFENS_I:
			return eBloodPledgeRankType.isAuthRankAtEliteKnight(rank) ? L1SkillId.PLEDGE_DEFENS_BUFF_I_ELITE : L1SkillId.PLEDGE_DEFENS_BUFF_I_NORMAL;
		case DEFENS_II:
			return eBloodPledgeRankType.isAuthRankAtEliteKnight(rank) ? L1SkillId.PLEDGE_DEFENS_BUFF_II_ELITE : L1SkillId.PLEDGE_DEFENS_BUFF_II_NORMAL;
		default:
			return 0;
		}
	}
	
	public static void pledge_contribution_buff(L1PcInstance pc){
		if (pc.getClanid() == 0) {
			return;
		}
		ContributionBuff clanBuff	= ClanContributionBuffTable.getInstance().getContributionBuff(pc.getClanid());
		if (clanBuff == null) {
			return;
		}
		eBloodPledgeRankType rank = pc.getBloodPledgeRank();
		int expType			= clanBuff.exp_buff_type;
		int battleType		= clanBuff.battle_buff_type;
		int defensType		= clanBuff.defens_buff_type;
		long sysTime		= System.currentTimeMillis();
		if (expType > 0) {
			long expTime		= clanBuff.exp_buff_time.getTime();
			if (expTime > sysTime) {
				PLEDGE_CONTRIBUTION_BUFF_TYPE buff_type = expType == 1 ? PLEDGE_CONTRIBUTION_BUFF_TYPE.EXP_I : PLEDGE_CONTRIBUTION_BUFF_TYPE.EXP_II;
				pledge_contribution_buff_set(pc, get_pledge_contribution_buff_id_from_rank(rank, buff_type), (int)(expTime - sysTime));
			}
		}
		if (battleType > 0) {
			long battleTime		= clanBuff.battle_buff_time.getTime();
			if (battleTime > sysTime) {
				PLEDGE_CONTRIBUTION_BUFF_TYPE buff_type = battleType == 1 ? PLEDGE_CONTRIBUTION_BUFF_TYPE.BATTLE_I : PLEDGE_CONTRIBUTION_BUFF_TYPE.BATTLE_II;
				pledge_contribution_buff_set(pc, get_pledge_contribution_buff_id_from_rank(rank, buff_type), (int)(battleTime - sysTime));
			}
		}
		if (defensType > 0) {
			long defensTime		= clanBuff.defens_buff_time.getTime();
			if (defensTime > sysTime) {
				PLEDGE_CONTRIBUTION_BUFF_TYPE buff_type = defensType == 1 ? PLEDGE_CONTRIBUTION_BUFF_TYPE.DEFENS_I : PLEDGE_CONTRIBUTION_BUFF_TYPE.DEFENS_II;
				pledge_contribution_buff_set(pc, get_pledge_contribution_buff_id_from_rank(rank, buff_type), (int)(defensTime - sysTime));
			}
		}
	}
	
	private static void pledge_contribution_buff_set(L1PcInstance pc, int buff_id, int buff_duration) {
		if (buff_id == 0) {
			return;
		}
		pc.getSkill().setSkillEffect(buff_id, buff_duration);
		pledge_contribution_buff_set(pc, buff_id, buff_duration / 1000, true);
	}
	
	public static void pledge_contribution_buff_set(L1PcInstance pc, int buff_id, int icon_duration, boolean flag){
		int flag_val = flag ? 1 : -1;
		L1Ability ability	= pc.getAbility();
		L1AC ac				= pc.getAC();
		L1Resistance resis	= pc.getResistance();
		switch(buff_id){
		case L1SkillId.PLEDGE_EXP_BUFF_I_NORMAL:
			pc.add_exp_boosting_ratio(10 * flag_val);
			break;
		case L1SkillId.PLEDGE_EXP_BUFF_I_ELITE:
			pc.add_exp_boosting_ratio(13 * flag_val);
			break;
		case L1SkillId.PLEDGE_EXP_BUFF_I_KNIGHT:
			pc.add_exp_boosting_ratio(14 * flag_val);
			break;
		case L1SkillId.PLEDGE_EXP_BUFF_I_KING:
			pc.add_exp_boosting_ratio(15 * flag_val);
			break;
		case L1SkillId.PLEDGE_EXP_BUFF_II_NORMAL:
			pc.add_exp_boosting_ratio(30 * flag_val);
			break;
		case L1SkillId.PLEDGE_EXP_BUFF_II_ELITE:
			pc.add_exp_boosting_ratio(33 * flag_val);
			break;
		case L1SkillId.PLEDGE_EXP_BUFF_II_KNIGHT:
			pc.add_exp_boosting_ratio(34 * flag_val);
			break;
		case L1SkillId.PLEDGE_EXP_BUFF_II_KING:
			pc.add_exp_boosting_ratio(35 * flag_val);
			break;
		case L1SkillId.PLEDGE_BATTLE_BUFF_I_NORMAL:
			pc.addMaxHp(50 * flag_val);
			ability.addShortHitup(1 * flag_val);
			ability.addShortDmgup(1 * flag_val);
			ability.addLongHitup(1 * flag_val);
			ability.addLongDmgup(1 * flag_val);
			ability.addSp(1 * flag_val);
			break;
		case L1SkillId.PLEDGE_BATTLE_BUFF_I_ELITE:
			pc.addMaxHp(50 * flag_val);
			ability.addShortHitup(2 * flag_val);
			ability.addShortDmgup(1 * flag_val);
			ability.addLongHitup(2 * flag_val);
			ability.addLongDmgup(1 * flag_val);
			ability.addMagicHitup(1 * flag_val);
			ability.addSp(1 * flag_val);
			break;
		case L1SkillId.PLEDGE_BATTLE_BUFF_II_NORMAL:
			pc.addMaxHp(100 * flag_val);
			ability.addShortHitup(2 * flag_val);
			ability.addShortDmgup(2 * flag_val);
			ability.addLongHitup(2 * flag_val);
			ability.addLongDmgup(2 * flag_val);
			ability.addSp(2 * flag_val);
			resis.addHitupAll(1 * flag_val);
			break;
		case L1SkillId.PLEDGE_BATTLE_BUFF_II_ELITE:
			pc.addMaxHp(100 * flag_val);
			ability.addShortHitup(3 * flag_val);
			ability.addShortDmgup(2 * flag_val);
			ability.addLongHitup(3 * flag_val);
			ability.addLongDmgup(2 * flag_val);
			ability.addMagicHitup(1 * flag_val);
			ability.addSp(2 * flag_val);
			resis.addHitupAll(1 * flag_val);
			break;
		case L1SkillId.PLEDGE_DEFENS_BUFF_I_NORMAL:
			ac.addAc(-1 * flag_val);
			ability.addDamageReduction(1 * flag_val);
			resis.addMr(5 * flag_val);
			break;
		case L1SkillId.PLEDGE_DEFENS_BUFF_I_ELITE:
			ac.addAc(-1 * flag_val);
			ability.addDamageReduction(2 * flag_val);
			resis.addMr(5 * flag_val);
			pc.addMaxHp(50 * flag_val);
			break;
		case L1SkillId.PLEDGE_DEFENS_BUFF_II_NORMAL:
			ac.addAc(-2 * flag_val);
			ability.addDamageReduction(2 * flag_val);
			resis.addMr(10 * flag_val);
			resis.addToleranceAll(1 * flag_val);
			break;
		case L1SkillId.PLEDGE_DEFENS_BUFF_II_ELITE:
			ac.addAc(-2 * flag_val);
			ability.addDamageReduction(3 * flag_val);
			resis.addMr(10 * flag_val);
			resis.addToleranceAll(1 * flag_val);
			pc.addMaxHp(50 * flag_val);
			break;
		}
		pc.sendPackets(new S_SpellBuffNoti(pc, buff_id, flag, icon_duration), true);
	}
	
	/**
	 * 혈맹 공헌도 버프 타입 종료
	 * @param buff_type
	 * @param members
	 */
	public static void pledge_contribution_buff_dispose(PLEDGE_CONTRIBUTION_BUFF_TYPE buff_type, L1PcInstance[] members) {
		switch (buff_type) {
		case EXP_I:
		case EXP_II:
			for (L1PcInstance member : members) {
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_NORMAL);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_ELITE);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_KNIGHT);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_KING);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_NORMAL);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_ELITE);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_KNIGHT);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_KING);
			}
			break;
		case BATTLE_I:
		case BATTLE_II:
			for (L1PcInstance member : members) {
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_I_NORMAL);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_I_ELITE);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_II_NORMAL);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_II_ELITE);
			}
			break;
		case DEFENS_I:
		case DEFENS_II:
			for (L1PcInstance member : members) {
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_I_NORMAL);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_I_ELITE);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_II_NORMAL);
				member.getSkill().removeSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_II_ELITE);
			}
			break;
		}
	}
	
	/**
	 * 혈맹 공헌도 버프 전체 종료
	 * 혈맹 탈퇴 시 모든 버프를 종료한다.
	 * @param status
	 */
	public static void pledge_contribution_buff_all_dispose_from_user(L1SkillStatus status) {
		if (status.hasSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_NORMAL)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_NORMAL);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_ELITE)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_ELITE);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_KNIGHT)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_KNIGHT);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_KING)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_I_KING);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_NORMAL)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_NORMAL);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_ELITE)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_ELITE);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_KNIGHT)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_KNIGHT);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_KING)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_EXP_BUFF_II_KING);
		}
		if (status.hasSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_I_NORMAL)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_I_NORMAL);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_I_ELITE)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_I_ELITE);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_II_NORMAL)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_II_NORMAL);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_II_ELITE)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_BATTLE_BUFF_II_ELITE);
		}
		if (status.hasSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_I_NORMAL)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_I_NORMAL);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_I_ELITE)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_I_ELITE);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_II_NORMAL)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_II_NORMAL);
		} else if (status.hasSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_II_ELITE)) {
			status.removeSkillEffect(L1SkillId.PLEDGE_DEFENS_BUFF_II_ELITE);
		}
	}
	
	/**
	 * 혈맹 정예 등급 이상 공성전 특수 버프
	 * @param pc
	 * @param flag
	 */
	public static void siege_rank_buff_set(L1PcInstance pc, boolean flag) {
		eBloodPledgeRankType rank = pc.getBloodPledgeRank();
		if (!eBloodPledgeRankType.isAuthRankAtEliteKnight(rank)) {
			return;
		}
		if (flag) {
			if (pc._is_siege_rank_buff) {
				return;
			}
			pc._is_siege_rank_buff = true;
			switch (rank) {
			case RANK_NORMAL_KING:
				pc.getSkill().setSkillEffect(L1SkillId.PLEDGE_SIEGE_BUFF_KING, 0);
				pc.addMaxHp(200);
				pc.getResistance().addToleranceAll(2);
				break;
			case RANK_NORMAL_KNIGHT:
				pc.getSkill().setSkillEffect(L1SkillId.PLEDGE_SIEGE_BUFF_KNIGHT, 0);
				pc.getResistance().addHitupAll(1);
				pc.getAbility().addShortDmgup(1);
				pc.getAbility().addLongDmgup(1);
				pc.getAbility().addSp(1);
				break;
			case RANK_NORMAL_ELITE_KNIGHT:
				pc.getSkill().setSkillEffect(L1SkillId.PLEDGE_SIEGE_BUFF_ELITE, 0);
				pc.getAbility().addShortDmgup(1);
				pc.getAbility().addLongDmgup(1);
				pc.getAbility().addSp(1);
				break;
			default:
				break;
			}
		} else {
			if (!pc._is_siege_rank_buff) {
				return;
			}
			pc._is_siege_rank_buff = false;
			if (pc.getSkill().hasSkillEffect(L1SkillId.PLEDGE_SIEGE_BUFF_KING)) {
				pc.getSkill().removeSkillEffect(L1SkillId.PLEDGE_SIEGE_BUFF_KING);
				pc.addMaxHp(-200);
				pc.getResistance().addToleranceAll(-2);
			} else if (pc.getSkill().hasSkillEffect(L1SkillId.PLEDGE_SIEGE_BUFF_KNIGHT)) {
				pc.getSkill().removeSkillEffect(L1SkillId.PLEDGE_SIEGE_BUFF_KNIGHT);
				pc.getResistance().addHitupAll(-1);
				pc.getAbility().addShortDmgup(-1);
				pc.getAbility().addLongDmgup(-1);
				pc.getAbility().addSp(-1);
			} else if (pc.getSkill().hasSkillEffect(L1SkillId.PLEDGE_SIEGE_BUFF_ELITE)) {
				pc.getSkill().removeSkillEffect(L1SkillId.PLEDGE_SIEGE_BUFF_ELITE);
				pc.getAbility().addShortDmgup(-1);
				pc.getAbility().addLongDmgup(-1);
				pc.getAbility().addSp(-1);
			}
		}
	}
	
	/**
	 * 성장의 고리 활성화
	 * @param pc
	 * @param grade
	 * @param duration
	 */
	public static void arcaBuffEnable(L1PcInstance pc, L1ArcaGrade grade, long duration) {
		pc.getSkill().setSkillEffect(grade.getSpellId(), duration);
		pc.sendPackets(new S_SpellBuffNoti(duration, grade), true);
		arcaBuff(pc, grade.getSpellId(), true);
	}
	
	/**
	 * 성장의 고리 비활성화
	 * @param pc
	 * @param spell_id
	 */
	public static void arcaBuffDisable(L1PcInstance pc, int spell_id) {
		arcaBuff(pc, spell_id, false);
	}
	
	static void arcaBuff(L1PcInstance pc, int spell_id, boolean flag) {
		switch (spell_id) {
		case L1SkillId.TAM_FRUIT_1:
			pc.getAC().addAc(flag ? -1 : 1);
			break;
		case L1SkillId.TAM_FRUIT_2:
			pc.getAC().addAc(flag ? -2 : 2);
			break;
		case L1SkillId.TAM_FRUIT_3:
			pc.getAC().addAc(flag ? -3 : 3);
			pc.getAbility().addDamageReduction(flag ? 2 : -2);
			break;
		case L1SkillId.TAM_FRUIT_4:
			pc.getAC().addAc(flag ? -4 : 4);
			pc.getAbility().addDamageReduction(flag ? 2 : -2);
			break;
		case L1SkillId.TAM_FRUIT_5:
			pc.add_exp_boosting_ratio(flag ? 5 : -5);
			pc.getAC().addAc(flag ? -5 : 5);	
			pc.getAbility().addDamageReduction(flag ? 2 : -2);
			break;
		default:
			break;
		}
	}
	
}


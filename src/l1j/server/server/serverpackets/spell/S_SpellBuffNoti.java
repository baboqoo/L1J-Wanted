package l1j.server.server.serverpackets.spell;

import l1j.server.GameSystem.arca.L1ArcaGrade;
import l1j.server.server.Opcodes;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.datatables.SkillsInfoTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1SkillsInfo;

public class S_SpellBuffNoti extends ServerBasePacket {
	private static final String S_SPELL_BUFF_NOTI = "[S] S_SpellBuffNoti";
	public static final int NOTI	= 0x006e;
	
	public S_SpellBuffNoti(L1PcInstance pc, int spellId, boolean on, long time) {
		L1SkillsInfo info = SkillsInfoTable.getSkillInfo(spellId);
		if (info == null) {
			System.out.println(String.format("[S_SkillIconProto] INFO_NOT_FOUND : SPELL_ID(%d)", spellId));
			return;
		}
		int onIconId = 0, tooltipStrId = 0;
		switch(spellId){
		case L1SkillId.IMMUNE_TO_HARM:
			onIconId		= pc._isImmunToHarmSaint ? 9835 : 1562;
			tooltipStrId	= 314;
			break;
		case L1SkillId.MEDITATION:
			onIconId		= 795;
			tooltipStrId	= pc.isPassiveStatus(L1PassiveId.MEDITATION_BEYOND) ? 8019 : 910;
			break;
		case L1SkillId.PRIME:
			spellId			= pc.getPrimePrinceState() == 15 || pc.warZone ? 4397 : 4398;
			onIconId		= pc.getPrimePrinceState() == 15 || pc.warZone ? 9712 : 9611;
			tooltipStrId	= pc.getPrimePrinceState() == 15 ? 7116 : (pc.getPrimePrinceState() == 5 ? 7115 : (pc.getPrimeDmgState() == 9 ? 7039 : 7006));
			break;
		case L1SkillId.SHADOW_ARMOR:
			onIconId		= 1104;
			tooltipStrId	= pc.isPassiveStatus(L1PassiveId.SHADOW_ARMOR_DESTINY) ? 8023 : 971;
			break;
		case L1SkillId.VANGUARD:
			onIconId		= 10152;
			tooltipStrId	= pc._isLancerForm ? 7744 : 7743;
			break;
		case L1SkillId.PRESSURE:
			onIconId		= 10155;
			tooltipStrId	= pc.isPassiveStatus(L1PassiveId.PRESSURE_DEATH_RECALL) ? 7751 : 7747;
			break;
		case L1SkillId.COUNTER_BARRIER:
			onIconId		= pc.isPassiveStatus(L1PassiveId.COUNTER_BARRIER_MASTER) ? 10545 : pc.isPassiveStatus(L1PassiveId.COUNTER_BARRIER_VETERAN) ? 8848 : 2345;
			tooltipStrId	= pc.isPassiveStatus(L1PassiveId.COUNTER_BARRIER_MASTER) ? 8511 : 1088;
			break;
		case L1SkillId.STRIKER_GALE:
			onIconId		= pc.isStrikerGaleShotHold() ? 10533 : 2357;
			tooltipStrId	= pc.isStrikerGaleShotHold() ? 8477 : 1084;
			break;
		case L1SkillId.SOUL_BARRIER:
			onIconId		= pc.isPassiveStatus(L1PassiveId.SOUL_BARRIER_ARMOR) ? 10521 : 7435;
			tooltipStrId	= pc.isPassiveStatus(L1PassiveId.SOUL_BARRIER_ARMOR) ? 8475 : 4736;
			break;
		default:
			onIconId		= info.getOnIconId() > 0 ? info.getOnIconId() : info.getIcon();
			tooltipStrId	= info.getTooltipStrId();
			break;
		}
		// 임의 스킬번호
		if (info.getUseSkillId() > 0) {
			spellId = info.getUseSkillId();
		}
		write_init();
		write_noti_type(on ? SkillIconNotiType.NEW : SkillIconNotiType.END);
		write_spellId(spellId);
		if (on) {
			write_duration(time);
			write_duration_show_type(info.getDurationShowType());
			write_on_icon_id(onIconId);
		}
		write_off_icon_id(info.getOffIconId());
		if (on) {
			if (!info.isSimplePck()) {
				write_icon_priority(info.getIconPriority());
			}
			write_tooltip_str_id(tooltipStrId);
			write_new_str_id(info.getNewStrId());
		}
		write_end_str_id(info.getEndStrId());
		if (on) {
			write_is_good(info.isGood());
			if (!info.isSimplePck()) {
				write_overlap_buff_icon(info.getOverlapBuffIcon());
				write_main_tooltip_str_id(info.getMainTooltipStrId());
				write_buff_icon_priority(info.getBuffIconPriority());
				if (info.getBuffGroupId() != 0) {
					write_buff_group_id(info.getBuffGroupId());
					write_buff_group_priority(info.getBuffGroupPriority());
				}
			}
		}
		writeH(0x00);
	}
	
	// 드래곤의 축복
	public static final S_SpellBuffNoti DRAGON_BLESS_ON			= new S_SpellBuffNoti(L1SkillId.DRAGON_BLESS, SkillIconNotiType.NEW, -1);
	public static final S_SpellBuffNoti DRAGON_BLESS_OFF		= new S_SpellBuffNoti(L1SkillId.DRAGON_BLESS, SkillIconNotiType.END, -1);
	// 드래곤의 가호
	public static final S_SpellBuffNoti DRAGON_FAVOR_ON			= new S_SpellBuffNoti(L1SkillId.DRAGON_FAVOR, SkillIconNotiType.NEW, -1);
	public static final S_SpellBuffNoti DRAGON_FAVOR_OFF		= new S_SpellBuffNoti(L1SkillId.DRAGON_FAVOR, SkillIconNotiType.END, -1);
	// 드래곤의 가호(PC)
	public static final S_SpellBuffNoti DRAGON_FAVOR_PCCAFE_ON	= new S_SpellBuffNoti(L1SkillId.DRAGON_FAVOR_PCCAFE, SkillIconNotiType.NEW, -1);
	public static final S_SpellBuffNoti DRAGON_FAVOR_PCCAFE_OFF	= new S_SpellBuffNoti(L1SkillId.DRAGON_FAVOR_PCCAFE, SkillIconNotiType.END, -1);
	// 아덴의 신속 가호
	public static final S_SpellBuffNoti ADEN_FAST_GRACE_ON		= new S_SpellBuffNoti(L1SkillId.ADEN_FAST_GRACE, SkillIconNotiType.NEW, -1);
	public static final S_SpellBuffNoti ADEN_FAST_GRACE_OFF		= new S_SpellBuffNoti(L1SkillId.ADEN_FAST_GRACE, SkillIconNotiType.END, -1);
	// 순간이동 지배 반지
	public static final S_SpellBuffNoti DOMINATION_TELEPORT_ON	= new S_SpellBuffNoti(L1SkillId.DOMINATION_TELEPORT, SkillIconNotiType.NEW, -1);
	public static final S_SpellBuffNoti DOMINATION_TELEPORT_OFF	= new S_SpellBuffNoti(L1SkillId.DOMINATION_TELEPORT, SkillIconNotiType.END, -1);
	// 사신의 가호
	public static final S_SpellBuffNoti SASIN_GRACE_ON			= new S_SpellBuffNoti(L1SkillId.SASIN_GRACE, SkillIconNotiType.NEW, -1);
	public static final S_SpellBuffNoti SASIN_GRACE_OFF			= new S_SpellBuffNoti(L1SkillId.SASIN_GRACE, SkillIconNotiType.END, -1);
	
	public S_SpellBuffNoti(int skillId, SkillIconNotiType notiType, long time) {
		L1SkillsInfo info = SkillsInfoTable.getSkillInfo(skillId);
		if (info == null) {
			System.out.println(String.format("[S_SkillIconProto] INFO_NOT_FOUND : SKILLID(%d)", skillId));
			return;
		}

		int onIconId		= info.getOnIconId() > 0 ? info.getOnIconId() : info.getIcon();
		int tooltipStrId	= info.getTooltipStrId();
		// 스킬번호 세팅
		if (info.getUseSkillId() > 0) {
			skillId = info.getUseSkillId();
		}
		write_init();
		write_noti_type(notiType);
		write_spellId(skillId);
		if (notiType == SkillIconNotiType.NEW) {
			write_duration(time);
			write_duration_show_type(info.getDurationShowType());
			write_on_icon_id(onIconId);
		}
		write_off_icon_id(info.getOffIconId());
		if (notiType == SkillIconNotiType.NEW) {
			if (!info.isSimplePck()) {
				write_icon_priority(info.getIconPriority());
			}
			write_tooltip_str_id(tooltipStrId);
			write_new_str_id(info.getNewStrId());
		}
		write_end_str_id(info.getEndStrId());
		if (notiType == SkillIconNotiType.NEW) {
			write_is_good(info.isGood());
			if (!info.isSimplePck()) {
				write_overlap_buff_icon(info.getOverlapBuffIcon());
				write_main_tooltip_str_id(info.getMainTooltipStrId());
				write_buff_icon_priority(info.getBuffIconPriority());
				if (info.getBuffGroupId() != 0) {
					write_buff_group_id(info.getBuffGroupId());
					write_buff_group_priority(info.getBuffGroupPriority());
				}
			}
		}
		writeH(0x00);
	}
	
	public static final S_SpellBuffNoti FAITH_OF_HALPAH_ENABLE	= new S_SpellBuffNoti(true, L1SkillId.FAITH_OF_HALPAH, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti FAITH_OF_HALPAH_DISABLE	= new S_SpellBuffNoti(false, L1SkillId.FAITH_OF_HALPAH, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti FAITH_OF_HALPAH_END		= new S_SpellBuffNoti(false, L1SkillId.FAITH_OF_HALPAH, SkillIconNotiType.END);
	
	// 할파스의 신의
	public S_SpellBuffNoti(boolean isEnable, int spellId, SkillIconNotiType notiType) {
		write_init();
		write_noti_type(notiType);
		write_spellId(spellId);
		if (notiType == SkillIconNotiType.NEW) {
			write_duration(-1);
			write_duration_show_type(SkillIconDurationShowType.TYPE_EFF_UNLIMIT);
			write_on_icon_id(isEnable ? 9943 : 9944);
		}
		write_off_icon_id(0);
		if (notiType == SkillIconNotiType.NEW) {
			int tooltip_str_id = isEnable ? 7465 : 7437;
			write_icon_priority(3);
			write_tooltip_str_id(tooltip_str_id);
			write_new_str_id(tooltip_str_id);
		}
		write_end_str_id(0);
		if (notiType == SkillIconNotiType.NEW) {
			write_is_good(true);
			write_overlap_buff_icon(0);
			write_main_tooltip_str_id(0);
			write_buff_icon_priority(0);
		}
		writeH(0x00);
	}
	
	public static final S_SpellBuffNoti FRAME_ON	= new S_SpellBuffNoti(SkillIconNotiType.NEW, L1PassiveId.FRAME);
	public static final S_SpellBuffNoti FRAME_OFF	= new S_SpellBuffNoti(SkillIconNotiType.END, L1PassiveId.FRAME);
	public S_SpellBuffNoti(SkillIconNotiType notiType, L1PassiveId passive) {
		L1PassiveSkills passiveSkill = SkillsTable.getPassiveTemplate(passive.toInt());
		if (passiveSkill == null) {
			System.out.println(String.format("[S_SkillIconProto] INFO_NOT_FOUND : PASSIVE_ID(%d)", passive.toInt()));
			return;
		}
		write_init();
		write_noti_type(notiType);
		write_spellId(passiveSkill.getPassiveId());
		if (notiType == SkillIconNotiType.NEW) {
			write_duration_show_type(SkillIconDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
		}
		write_on_icon_id(passiveSkill.getOnIconId());
		if (notiType == SkillIconNotiType.NEW) {
			write_tooltip_str_id(passiveSkill.getTooltipStrId());
			write_is_good(passiveSkill.isGood());
		}
		writeH(0x00);
	}
	
	public static final S_SpellBuffNoti ILLUTION_RICH_ON	= new S_SpellBuffNoti(L1PassiveId.ILLUTION_RICH, 209, SkillIconNotiType.NEW, -1);
	public static final S_SpellBuffNoti ILLUTION_RICH_OFF	= new S_SpellBuffNoti(L1PassiveId.ILLUTION_RICH, 209, SkillIconNotiType.END, -1);
	public static final S_SpellBuffNoti ILLUTION_GOLEM_ON	= new S_SpellBuffNoti(L1PassiveId.ILLUTION_GOLEM, 214, SkillIconNotiType.NEW, -1);
	public static final S_SpellBuffNoti ILLUTION_GOLEM_OFF	= new S_SpellBuffNoti(L1PassiveId.ILLUTION_GOLEM, 214, SkillIconNotiType.END, -1);
	
	public S_SpellBuffNoti(L1PassiveId passive, int spellId, SkillIconNotiType notiType, int duration) {
		L1PassiveSkills passiveSkill = SkillsTable.getPassiveTemplate(passive.toInt());
		if (passiveSkill == null) {
			System.out.println(String.format("[S_SkillIconProto] PASSIVE_INFO_NOT_FOUND : PASSIVE_ID(%d)", passive.toInt()));
			return;
		}
		write_init();
		write_noti_type(notiType);
		write_spellId(spellId);
		if (notiType == SkillIconNotiType.NEW) {
			write_duration(duration);
			write_duration_show_type(SkillIconDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
		}
		write_on_icon_id(passiveSkill.getOnIconId());
		if (notiType == SkillIconNotiType.NEW) {
			write_tooltip_str_id(passiveSkill.getTooltipStrId());
			write_is_good(passiveSkill.isGood());
		}
		writeH(0x00);
	}
	
	public static final S_SpellBuffNoti SLAYER_ON				= new S_SpellBuffNoti(L1PassiveId.SLAYER, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti SLAYER_OFF				= new S_SpellBuffNoti(L1PassiveId.SLAYER, SkillIconNotiType.END);
	public static final S_SpellBuffNoti TITAN_LOCK_ON			= new S_SpellBuffNoti(L1PassiveId.TITAN_LOCK, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti TITAN_LOCK_OFF			= new S_SpellBuffNoti(L1PassiveId.TITAN_LOCK, SkillIconNotiType.END);
	public static final S_SpellBuffNoti TITAN_BLICK_ON			= new S_SpellBuffNoti(L1PassiveId.TITAN_BLICK, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti TITAN_BLICK_OFF			= new S_SpellBuffNoti(L1PassiveId.TITAN_BLICK, SkillIconNotiType.END);
	public static final S_SpellBuffNoti TITAN_MAGIC_ON			= new S_SpellBuffNoti(L1PassiveId.TITAN_MAGIC, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti TITAN_MAGIC_OFF			= new S_SpellBuffNoti(L1PassiveId.TITAN_MAGIC, SkillIconNotiType.END);
	public static final S_SpellBuffNoti TACTICAL_ADVANCE_ON		= new S_SpellBuffNoti(L1PassiveId.TACTICAL_ADVANCE, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti TACTICAL_ADVANCE_OFF	= new S_SpellBuffNoti(L1PassiveId.TACTICAL_ADVANCE, SkillIconNotiType.END);
	public static final S_SpellBuffNoti DRAGON_SKIN_ON			= new S_SpellBuffNoti(L1PassiveId.DRAGON_SKIN, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti DRAGON_SKIN_OFF			= new S_SpellBuffNoti(L1PassiveId.DRAGON_SKIN, SkillIconNotiType.END);
	public static final S_SpellBuffNoti ADVANCE_SPIRIT_ON		= new S_SpellBuffNoti(L1PassiveId.ADVANCE_SPIRIT, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti ADVANCE_SPIRIT_OFF		= new S_SpellBuffNoti(L1PassiveId.ADVANCE_SPIRIT, SkillIconNotiType.END);
	public static final S_SpellBuffNoti SHINING_ARMOR_ON		= new S_SpellBuffNoti(L1PassiveId.SHINING_ARMOR, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti SHINING_ARMOR_OFF		= new S_SpellBuffNoti(L1PassiveId.SHINING_ARMOR, SkillIconNotiType.END);
	public static final S_SpellBuffNoti MAJESTY_ON				= new S_SpellBuffNoti(L1PassiveId.MAJESTY, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti MAJESTY_OFF				= new S_SpellBuffNoti(L1PassiveId.MAJESTY, SkillIconNotiType.END);
	public static final S_SpellBuffNoti GIGANTIC_ON				= new S_SpellBuffNoti(L1PassiveId.GIGANTIC, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti GIGANTIC_OFF			= new S_SpellBuffNoti(L1PassiveId.GIGANTIC, SkillIconNotiType.END);
	public static final S_SpellBuffNoti RAIGING_WEAPONE_ON		= new S_SpellBuffNoti(L1PassiveId.RAIGING_WEAPON, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti RAIGING_WEAPONE_OFF		= new S_SpellBuffNoti(L1PassiveId.RAIGING_WEAPON, SkillIconNotiType.END);
	public static final S_SpellBuffNoti DEMOLITION_ON			= new S_SpellBuffNoti(L1PassiveId.DEMOLITION, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti DEMOLITION_OFF			= new S_SpellBuffNoti(L1PassiveId.DEMOLITION, SkillIconNotiType.END);
	public static final S_SpellBuffNoti TITAN_BEAST_ON			= new S_SpellBuffNoti(L1PassiveId.TITAN_BEAST, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti TITAN_BEAST_OFF			= new S_SpellBuffNoti(L1PassiveId.TITAN_BEAST, SkillIconNotiType.END);
	public static final S_SpellBuffNoti MORTAL_BODY_ON			= new S_SpellBuffNoti(L1PassiveId.MORTAL_BODY, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti MORTAL_BODY_OFF			= new S_SpellBuffNoti(L1PassiveId.MORTAL_BODY, SkillIconNotiType.END);
	public static final S_SpellBuffNoti MOEBIUS_ON				= new S_SpellBuffNoti(L1PassiveId.MOEBIUS, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti MOEBIUS_OFF				= new S_SpellBuffNoti(L1PassiveId.MOEBIUS, SkillIconNotiType.END);
	public static final S_SpellBuffNoti CONQUEROR_ON			= new S_SpellBuffNoti(L1PassiveId.CONQUEROR, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti CONQUEROR_OFF			= new S_SpellBuffNoti(L1PassiveId.CONQUEROR, SkillIconNotiType.END);
	
	public S_SpellBuffNoti(L1PassiveId passive, SkillIconNotiType notiType) {// 패시브 스킬
		L1PassiveSkills passiveSkill = SkillsTable.getPassiveTemplate(passive.toInt());
		if (passiveSkill == null) {
			System.out.println(String.format("[S_SkillIconProto] PASSIVESKILL_INFO_NOT_FOUND : PASSIVEID(%d)", passive.toInt()));
			return;
		}
		write_init();
		write_noti_type(notiType);
		write_spellId(passive.toInt());
		write_on_icon_id(passiveSkill.getOnIconId());
		write_tooltip_str_id(passiveSkill.getTooltipStrId());
		write_is_good(passiveSkill.isGood());
		writeH(0x00);
	}
	
	public static final S_SpellBuffNoti BERSERK_NORMAL_ON	= new S_SpellBuffNoti(L1PassiveId.BERSERK, SkillIconNotiType.NEW, eBoostType.BOOST_NONE);
	public static final S_SpellBuffNoti BERSERK_FORCE_ON	= new S_SpellBuffNoti(L1PassiveId.BERSERK, SkillIconNotiType.NEW, eBoostType.HP_UI_CHANGE);
	public static final S_SpellBuffNoti BERSERK_OFF			= new S_SpellBuffNoti(L1PassiveId.BERSERK, SkillIconNotiType.END, eBoostType.BOOST_NONE);
	public S_SpellBuffNoti(L1PassiveId passive, SkillIconNotiType type, eBoostType boostType) {// 패시브 스킬
		L1PassiveSkills passiveSkill = SkillsTable.getPassiveTemplate(passive.toInt());
		if (passiveSkill == null) {
			System.out.println(String.format("[S_SkillIconProto] PASSIVESKILL_INFO_NOT_FOUND : PASSIVEID(%d)", passive.toInt()));
			return;
		}
		int onIconId = passiveSkill.getOnIconId(), tooltipStrId = passiveSkill.getTooltipStrId();
		if (passive == L1PassiveId.BERSERK && boostType == eBoostType.HP_UI_CHANGE) {
			onIconId		= 10717;
			tooltipStrId	= 8614;
		}
		write_init();
		write_noti_type(type);
		write_spellId(passiveSkill.getPassiveId());
		write_on_icon_id(onIconId);
		write_tooltip_str_id(tooltipStrId);
		write_is_good(passiveSkill.isGood());
		write_boost_type(boostType);
		writeH(0x00);
	}
	
	// 점령전 패널티 타이머
	public static final S_SpellBuffNoti PENALTY_ON_60	= new S_SpellBuffNoti(61, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti PENALTY_ON_120	= new S_SpellBuffNoti(121, SkillIconNotiType.NEW);
	public static final S_SpellBuffNoti PENALTY_OFF		= new S_SpellBuffNoti(0, SkillIconNotiType.END);
	public S_SpellBuffNoti(long duration, SkillIconNotiType noti_type){
		write_init();
		write_noti_type(noti_type);
		write_spellId(5226);
		if (noti_type == SkillIconNotiType.NEW) {
			write_duration(duration);
			write_duration_show_type(SkillIconDurationShowType.TYPE_EFF_NONE);
			write_on_icon_id(0);
		}
		write_off_icon_id(0);
		if (noti_type == SkillIconNotiType.NEW) {
			write_icon_priority(-1);
			write_tooltip_str_id(0);
			write_new_str_id(0);
		}
		write_end_str_id(0);
		if (noti_type == SkillIconNotiType.NEW) {
			write_is_good(true);
			write_overlap_buff_icon(0);
			write_main_tooltip_str_id(0);
			write_buff_icon_priority(0);
			write_buff_group_id(50);
			write_buff_group_priority(0);
		}
        writeH(0x00);
	}
	
	// 탐 버프
	public S_SpellBuffNoti(long duration, L1ArcaGrade grade) {
		write_init();
		write_noti_type(SkillIconNotiType.RESTAT);
		write_spellId(grade.getSpellId());
		write_duration(duration / 1000);
		write_duration_show_type(SkillIconDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
		write_on_icon_id(grade.getIcon());
		write_off_icon_id(0);
		write_icon_priority(5);
		write_tooltip_str_id(grade.getMsg());
		write_new_str_id(0);
		write_end_str_id(0);
		write_is_good(true);
		write_overlap_buff_icon(0);
		write_main_tooltip_str_id(0);
		write_buff_icon_priority(0);
		write_buff_group_id(1);
		write_buff_group_priority(0);
		writeH(0x00);
	}
	
	public static enum NSHOP_BUFF_TYPE {
		ACTIVE, ATTACK, DEFFENSE, MAGIC, STUN, HOLD, WIS, INT, STR, DEX, 
		DEFFENSE_SPIRIT, DEFFENSE_FIRE, DEFFENSE_EARTH, DEFFENSE_AIR, DEFFENSE_WATER, PVP,
		ATTACK_FIRE, ATTACK_EARTH, ATTACK_WATER, ATTACK_AIR
	}
	public S_SpellBuffNoti(NSHOP_BUFF_TYPE code, int spell_id, long duration) {
		int tooltip_str_id = 0;
		switch (code) {
		case ACTIVE:tooltip_str_id = 4347;break;
		case ATTACK:tooltip_str_id = 4348;break;
		case DEFFENSE:tooltip_str_id = 4349;break;
		case MAGIC:tooltip_str_id = 4350;break;
		case STUN:tooltip_str_id = 4351;break;
		case HOLD:tooltip_str_id = 4352;break;
		case WIS:tooltip_str_id = 4341;break;
		case INT:tooltip_str_id = 4340;break;
		case DEX:tooltip_str_id = 4339;break;
		case STR:tooltip_str_id = 4338;break;
		case DEFFENSE_SPIRIT:tooltip_str_id = 4337;break;
		case DEFFENSE_FIRE:tooltip_str_id = 4336;break;
		case DEFFENSE_EARTH:tooltip_str_id = 4335;break;
		case DEFFENSE_WATER:tooltip_str_id = 4333;break;
		case DEFFENSE_AIR:tooltip_str_id = 4334;break;
		case PVP:tooltip_str_id = 4346;break;
		case ATTACK_FIRE:tooltip_str_id = 4345;break;
		case ATTACK_EARTH:tooltip_str_id = 4344;break;
		case ATTACK_WATER:tooltip_str_id = 4342;break;
		case ATTACK_AIR:tooltip_str_id = 4343;break;
		}
		write_init();
		write_noti_type(SkillIconNotiType.RESTAT);
		write_spellId(spell_id);
		write_duration(duration / 1000);
		write_duration_show_type(SkillIconDurationShowType.TYPE_EFF_NSERVICE_TOPPING);
		write_on_icon_id(6679);
		write_off_icon_id(0);
		write_icon_priority(0);
		write_tooltip_str_id(tooltip_str_id);
		write_new_str_id(0);
		write_end_str_id(0);
		write_is_good(true);
		write_overlap_buff_icon(1);
		write_main_tooltip_str_id(4328);
		write_buff_icon_priority(1);
		writeH(0x00);
	}
	
	public S_SpellBuffNoti(SkillIconNotiType noti_type, int spellId, int classId, long duration) {
		int spell_id = spellId, on_icon_id = 0, tooltip_str_id = 0;
		switch (spellId) {
		case L1SkillId.RANKING_BUFF_1:
			on_icon_id = 11219;
			tooltip_str_id = 5148;
			break;
		case L1SkillId.RANKING_BUFF_2:
			on_icon_id = 11220;
			tooltip_str_id = 5145;
			break;
		case L1SkillId.RANKING_BUFF_3:
			on_icon_id = 11221;
			tooltip_str_id = 5142;
			break;
		case L1SkillId.RANKING_BUFF_4_10:
			on_icon_id = 10033;
			tooltip_str_id = 5139;
			break;
		case L1SkillId.RANKING_BUFF_11_20:
			on_icon_id = 10032;
			tooltip_str_id = 5138;
			break;
		case L1SkillId.RANKING_BUFF_21_30:
			on_icon_id = 10031;
			tooltip_str_id = 5137;
			break;
		case L1SkillId.RANKING_BUFF_31_60:
			on_icon_id = 8532;
			tooltip_str_id = 5136;
			break;
		case L1SkillId.RANKING_BUFF_61_100:
			on_icon_id = 8532;
			tooltip_str_id = 9294;
			break;
		}
		
		// 클래스 별 스탯에 의해 분리된다.
		if (spellId == L1SkillId.RANKING_BUFF_1
				|| spellId == L1SkillId.RANKING_BUFF_2
				|| spellId == L1SkillId.RANKING_BUFF_3
				|| spellId == L1SkillId.RANKING_BUFF_4_10) {
			spell_id		+= classId == 2 ? 1 : classId == 3 || classId == 6 ? 2 : 0;
			tooltip_str_id	+= classId == 2 ? 1 : classId == 3 || classId == 6 ? 2 : 0;
		}
		write_init();
		write_noti_type(noti_type);
		write_spellId(spell_id);
		write_duration(duration);
		write_duration_show_type(SkillIconDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
		write_on_icon_id(on_icon_id);
		write_off_icon_id(0);
		write_icon_priority(3);
		write_tooltip_str_id(tooltip_str_id);		
		write_new_str_id(0);
		write_end_str_id(0);
		write_is_good(true);
		write_overlap_buff_icon(0);
		write_main_tooltip_str_id(0);
		write_buff_icon_priority(0);
		writeH(0x00);
	}
	
	public static final S_SpellBuffNoti SAFTY_BUFF_ICON_ON			= new S_SpellBuffNoti(2563, SkillIconNotiType.RESTAT, 4017, -1);// 보호모드 버프아이콘
	public static final S_SpellBuffNoti SAFTY_BUFF_ICON_OFF			= new S_SpellBuffNoti(2563, SkillIconNotiType.END, 4017, -1);// 보호모드 버프아이콘
	public static final S_SpellBuffNoti SUCUBUS_QUEEN_BUFF_ICON_ON	= new S_SpellBuffNoti(5934, SkillIconNotiType.RESTAT, 4647, -1);
	public static final S_SpellBuffNoti SUCUBUS_QUEEN_BUFF_ICON_OFF	= new S_SpellBuffNoti(5934, SkillIconNotiType.END, 4647, -1);
	public S_SpellBuffNoti(int spellId, SkillIconNotiType noti_type, int tooltip_str_id, int duration) {
		write_init();
		write_noti_type(noti_type);
		write_spellId(spellId);
		write_duration(duration);
		write_duration_show_type(SkillIconDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
		write_on_icon_id(spellId);
		write_off_icon_id(0);
		write_icon_priority(3);
		write_tooltip_str_id(tooltip_str_id);
		write_new_str_id(0);
		write_end_str_id(0);
		write_is_good(true);
		write_overlap_buff_icon(0);
		write_main_tooltip_str_id(0);
		write_buff_icon_priority(0);
		writeH(0x00);
	}

	public S_SpellBuffNoti(int spellId, SkillIconNotiType noti_type, int duration, SkillIconDurationShowType duration_show_type, int icon_priority) {
		write_init();
		write_noti_type(noti_type);
		write_spellId(spellId);
		write_duration(duration);
		write_duration_show_type(duration_show_type);
		write_on_icon_id(spellId);
		write_off_icon_id(0);
		write_icon_priority(icon_priority);
		write_tooltip_str_id(5789);
		write_new_str_id(0);
		write_end_str_id(0);
		write_is_good(true);
		write_overlap_buff_icon(0);
		write_main_tooltip_str_id(0);
		write_buff_icon_priority(icon_priority);
		writeH(0x00);
	}

	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_noti_type(SkillIconNotiType noti_type) {
		writeRaw(0x08);// noti_type
		writeRaw(noti_type.value);
	}
	
	void write_spellId(int spellId) {
		writeRaw(0x10);// spellId
		writeBit(spellId);
	}
	
	void write_duration(long duration) {
		writeRaw(0x18);// duration
		writeBit(duration);
	}
	
	void write_duration_show_type(SkillIconDurationShowType duration_show_type) {
		writeRaw(0x20);// duration_show_type
		writeRaw(duration_show_type.value);
	}
	
	void write_on_icon_id(int on_icon_id) {
		writeRaw(0x28);// on_icon_id
		writeBit(on_icon_id);
	}
	
	void write_off_icon_id(int off_icon_id) {
		writeRaw(0x30);// off_icon_id
		writeBit(off_icon_id);
	}
	
	void write_icon_priority(int icon_priority) {
		writeRaw(0x38);// icon_priority 아이콘 우선순위
		writeBit(icon_priority);
	}
	
	void write_tooltip_str_id(int tooltip_str_id) {
		writeRaw(0x40);// tooltip_str_id
		writeBit(tooltip_str_id);
	}
	
	void write_new_str_id(int new_str_id) {
		writeRaw(0x48);// new_str_id 시스템 메세지 출력
		writeBit(new_str_id);
	}
	
	void write_end_str_id(int end_str_id) {
		writeRaw(0x50);// end_str_id
		writeBit(end_str_id);
	}
	
	void write_is_good(boolean is_good) {
		writeRaw(0x58);// is_good
		writeB(is_good);
	}
	
	void write_overlap_buff_icon(int overlap_buff_icon) {
		writeRaw(0x60);// overlap_buff_icon 겹치는 아이콘
		writeBit(overlap_buff_icon);
	}
	
	void write_main_tooltip_str_id(int main_tooltip_str_id) {
		writeRaw(0x68);// main_tooltip_str_id
		writeBit(main_tooltip_str_id);
	}
	
	void write_buff_icon_priority(int buff_icon_priority) {
		writeRaw(0x70);// buff_icon_priority 아이콘 우선 순위
		writeBit(buff_icon_priority);
	}
	
	void write_buff_group_id(int buff_group_id) {
		writeRaw(0x78);// buff_group_id
		writeBit(buff_group_id);
	}
	
	void write_buff_group_priority(int buff_group_priority) {
		writeH(0x0180);// buff_group_priority 그룹 우선순위
		writeBit(buff_group_priority);
	}
	
	void write_expire_duration(int expire_duration) {
		writeH(0x0188);// expire_duration 만료 기간
		writeBit(expire_duration);
	}
	
	void write_boost_type(eBoostType boost_type) {
		writeH(0x0190);// boost_type
		writeRaw(boost_type.value);
	}
	
	void write_is_passive_spell(boolean is_passive_spell) {
		writeH(0x0198);
		writeB(is_passive_spell);
	}
	
	public enum SkillIconNotiType {
		NEW(1),
		RESTAT(2),
		END(3),
		TIMEOUT(4),
		;
		private int value;
		SkillIconNotiType(int val) {
			value = val;
		}
		public int toInt() {
			return value;
		}
		public boolean equals(SkillIconNotiType v) {
			return value == v.value;
		}

		public static SkillIconNotiType fromInt(int i) {
			switch (i) {
			case 1:
				return NEW;
			case 2:
				return RESTAT;
			case 3:
				return END;
			case 4:
				return TIMEOUT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SkillIconNotiType, %d", i));
			}
		}
	}
	
	public enum SkillIconDurationShowType {
		TYPE_EFF_NONE(0),
		TYPE_EFF_PERCENT(1),
		TYPE_EFF_MINUTE(2),
		TYPE_EFF_PERCENT_ORC_SERVER(3),
		TYPE_EFF_EINHASAD_COOLTIME_MINUTE(4),
		TYPE_EFF_LEGACY_TIME(5),
		TYPE_EFF_VARIABLE_VALUE(6),
		TYPE_EFF_DAY_HOUR_MIN(7),
		TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC(8),
		TYPE_EFF_NSERVICE_TOPPING(9),
		TYPE_EFF_UNLIMIT(10),
		TYPE_EFF_CUSTOM(11),
		TYPE_EFF_COUNT(12),
		TYPE_EFF_RATE(13),
		TYPE_EFF_EINHASAD_FAVOR(14),
		TYPE_EFF_HIDDEN(15),
		;
		private int value;
		SkillIconDurationShowType(int val) {
			value = val;
		}
		public int toInt() {
			return value;
		}
		public boolean equals(SkillIconDurationShowType v) {
			return value == v.value;
		}

		public static SkillIconDurationShowType fromInt(int i) {
			switch (i) {
			case 0:
				return TYPE_EFF_NONE;
			case 1:
				return TYPE_EFF_PERCENT;
			case 2:
				return TYPE_EFF_MINUTE;
			case 3:
				return TYPE_EFF_PERCENT_ORC_SERVER;
			case 4:
				return TYPE_EFF_EINHASAD_COOLTIME_MINUTE;
			case 5:
				return TYPE_EFF_LEGACY_TIME;
			case 6:
				return TYPE_EFF_VARIABLE_VALUE;
			case 7:
				return TYPE_EFF_DAY_HOUR_MIN;
			case 8:
				return TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC;
			case 9:
				return TYPE_EFF_NSERVICE_TOPPING;
			case 10:
				return TYPE_EFF_UNLIMIT;
			case 11:
				return TYPE_EFF_CUSTOM;
			case 12:
				return TYPE_EFF_COUNT;
			case 13:
				return TYPE_EFF_RATE;
			case 14:
				return TYPE_EFF_EINHASAD_FAVOR;
			case 15:
				return TYPE_EFF_HIDDEN;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SkillIconDurationShowType, %d", i));
			}
		}
	}
	
	public enum eBoostType{
		BOOST_NONE(0),
		HP_UI_CHANGE(1),
		;
		private int value;
		eBoostType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eBoostType v){
			return value == v.value;
		}
		public static eBoostType fromInt(int i){
			switch(i){
			case 0:
				return BOOST_NONE;
			case 1:
				return HP_UI_CHANGE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eBoostType, %d", i));
			}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SPELL_BUFF_NOTI;
	}
}

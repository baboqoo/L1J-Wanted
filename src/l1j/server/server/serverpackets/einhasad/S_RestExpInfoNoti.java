package l1j.server.server.serverpackets.einhasad;

import l1j.server.Config;
import l1j.server.common.data.eEinhasadBonusType;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_RestExpInfoNoti extends ServerBasePacket {
	private static final String S_REST_EXP_INFO_NOTI = "[S] S_RestExpInfoNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x03fc;

	public S_RestExpInfoNoti(L1PcInstance pc) {
		L1SkillStatus skill = pc.getSkill();
		int default_ration	= Config.EIN.REST_EXP_DEFAULT_RATION;
		int rest_gauge		= pc.getAccount().getEinhasad().getPoint() / default_ration;
		int bless_map		= pc.getClanBuffMap();
		int addExp			= get_exp_from_buff(pc, skill);
		write_init();
	    write_rest_gauge(rest_gauge);
	    write_default_ration(rest_gauge >= 1 ? default_ration : 0);
	    write_extra_exp_ratio(0);
	    write_reduce_efficiency(pc.get_rest_exp_reduce_efficiency());
	    if (bless_map != 0 && bless_map == pc.getMapId()) {
	    	write_bless_of_blood_pledge(true);
	    }
	    write_can_charge_count(0);
	    write_type(get_bonus_type(pc, rest_gauge, skill));
	    write_default_ration_ex(addExp);
	    write_einhasad_rest_gauge_bonus_exp(addExp);
		writeH(0x00);
	}
	
	int get_exp_from_buff(L1PcInstance pc, L1SkillStatus skill) {
		int val = 0;
		if (skill.hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
			val += L1ExpPlayer.getTopazExp() * 100;
		} else if (skill.hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
			val += L1ExpPlayer.getDragonPupleExp(pc.getLevel()) * 100;
		} else if (skill.hasSkillEffect(L1SkillId.EMERALD_YES)) {
			val += 5400;
		} else if (skill.hasSkillEffect(L1SkillId.EXP_POTION6)) {
			val += 2000;
		} else if (skill.hasSkillEffect(L1SkillId.EXP_POTION7)) {
			val += 3000;
		}
		if (pc.getTeleport().isGrowBuff()) {
			val += 30000;
		}
		return val;
	}
	
	eEinhasadBonusType get_bonus_type(L1PcInstance pc, int rest_gauge, L1SkillStatus skill) {
		if (rest_gauge < 1 && pc.isPCCafe()) {
	    	return eEinhasadBonusType.PCCafe;// 아인하사드의 가호: pc
	    }
		if (rest_gauge < 1 && skill.hasSkillEffect(L1SkillId.EINHASAD_FAVOR)) {
	    	return eEinhasadBonusType.EinhasadFavor;// 아인하사드의 가호
	    }
	    return eEinhasadBonusType.SectionBonus;
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_rest_gauge(int rest_gauge) {
		writeRaw(0x08);// rest_gauge
		writeBit(rest_gauge);
	}
	
	void write_default_ration(int default_ration) {
		writeRaw(0x10);// default_ration
		writeBit(default_ration);
	}
	
	void write_extra_exp_ratio(int extra_exp_ratio) {
		writeRaw(0x18);// extra_exp_ratio
		writeRaw(extra_exp_ratio);
	}
	
	void write_reduce_efficiency(int reduce_efficiency) {
		writeRaw(0x20);// reduce_efficiency
	    writeBit(reduce_efficiency);
	}
	
	void write_bless_of_blood_pledge(boolean bless_of_blood_pledge) {
		writeRaw(0x28);// bless_of_blood_pledge
		writeB(bless_of_blood_pledge);
	}
	
	void write_can_charge_count(int can_charge_count) {
		writeRaw(0x30);// can_charge_count
		writeRaw(can_charge_count);
	}
	
	void write_type(eEinhasadBonusType type) {
		writeRaw(0x38);// type
		writeRaw(type.toInt());
	}
	
	void write_bonus_exp_map(int bonus_exp_map) {
		writeRaw(0x40);// bonus_exp_mapp
	    writeBit(bonus_exp_map);
	}
	
	void write_default_ration_ex(int default_ration_ex) {
		writeRaw(0x48);// default_ration_ex
	    writeBit(default_ration_ex);
	}
	
	void write_event_exp_map(int event_exp_map) {
		writeRaw(0x50);// event_exp_map
	    writeBit(event_exp_map);
	}
	
	void write_einhasad_rest_gauge_bonus_exp(int einhasad_rest_gauge_bonus_exp) {
		writeRaw(0x58);// einhasad_rest_gauge_bonus_exp
	    writeBit(einhasad_rest_gauge_bonus_exp);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_REST_EXP_INFO_NOTI;
	}
}


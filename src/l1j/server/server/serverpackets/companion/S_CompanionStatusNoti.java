package l1j.server.server.serverpackets.companion;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Pet;

public class S_CompanionStatusNoti extends ServerBasePacket {
	private static final String S_COMPANION_STATUS_NOTI = "[S] S_CompanionStatusNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x07d0;
	
	public static enum STATUS_UPDATE_FLAG {
		ALL,
		COMBO_TIME,
		FRIENDSHIP,
		FRIENDSHIP_GUAGE,
		EXP,
		HP,
		DELAY_REDUCE,
		MOVE_REDUCE,
		ATTACK_REDUCE,
		DEAD,
		LEVELUP,
		STAT,
		NAME_CHANGE
	}
	
	public S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG status, L1PetInstance companion) {
		L1Pet info = companion.getPetInfo();
		write_init();
		write_obj_id(companion.getId());
		switch(status){
		case ALL:
			write_name(companion.getName().getBytes());
		    write_class_id(companion.getNpcTemplate().getClassId());
		    write_level(companion.getLevel());
		    write_exp((int) companion.getExp());
		    write_base_max_hp(companion.getMaxHp());
		    write_temp_max_hp(0);
		    write_hp(companion.getCurrentHp());
		    write_base_str(companion.getAbility().getStr());
		    write_base_con(companion.getAbility().getCon());
		    write_base_int(companion.getAbility().getInt());
		    write_bonus_str(info.getAddStr());
		    write_bonus_con(info.getAddCon());
		    write_bonus_int(info.getAddInt());
		    write_elixir_use_count(info.get_elixir_use_count());
		    write_remain_stats(info.get_remain_stats());
		    write_temp_str(0);
		    write_temp_con(0);
		    write_temp_int(0);
		    write_friend_ship_guage(info.get_friend_ship_guage());
		    write_friend_ship_marble(info.get_friend_ship_marble());
		    write_minus_exp_penalty(companion.is_minus_exp_penalty());
		    write_ac(companion.getAC().getAc());
		    write_mr(companion.getResistance().getEffectedMrBySkill());
		    double movedelay_reduce = companion.isCombo() ? Config.COMPANION.COMBO_DELAY_REDUCE_RATE : companion.get_movedelay_reduce();
		    if (movedelay_reduce != 0) {
		    	write_movedelay_reduce(movedelay_reduce);
		    }
		    double attackdelay_reduce = companion.isCombo() ? Config.COMPANION.COMBO_DELAY_REDUCE_RATE : companion.get_attackdelay_reduce();
		    if (attackdelay_reduce != 0) {
		    	write_attackdelay_reduce(attackdelay_reduce);
		    }
		    write_pvp_dmg_ratio(companion.get_pvp_dmg_ratio());
		    write_is_combo_time(companion.isCombo());
			break;
		case COMBO_TIME:
			write_is_combo_time(companion.isCombo());
			break;
		case FRIENDSHIP_GUAGE:
			write_friend_ship_guage(companion.get_friend_ship_guage());
			break;
		case FRIENDSHIP:
			write_friend_ship_guage(companion.get_friend_ship_guage());
			write_friend_ship_marble(companion.get_friend_ship_marble());
			break;
		case EXP:
			write_exp((int) companion.getExp());
			break;
		case HP:
			write_hp(companion.getCurrentHp());
			break;
		case DELAY_REDUCE:
		    write_movedelay_reduce(companion.isCombo() ? Config.COMPANION.COMBO_DELAY_REDUCE_RATE : companion.get_movedelay_reduce());
		    write_attackdelay_reduce(companion.isCombo() ? Config.COMPANION.COMBO_DELAY_REDUCE_RATE : companion.get_attackdelay_reduce());
			break;
		case MOVE_REDUCE:
		    write_movedelay_reduce(companion.isCombo() ? Config.COMPANION.COMBO_DELAY_REDUCE_RATE : companion.get_movedelay_reduce());
			break;
		case ATTACK_REDUCE:
			write_attackdelay_reduce(companion.isCombo() ? Config.COMPANION.COMBO_DELAY_REDUCE_RATE : companion.get_attackdelay_reduce());
			break;
		case DEAD:
			write_exp((int) companion.getExp());
            write_hp(0);
            write_friend_ship_guage(companion.get_friend_ship_guage());
            write_minus_exp_penalty(companion.is_minus_exp_penalty());
			break;
		case LEVELUP:
			write_level(companion.getLevel());
            write_exp((int)companion.getExp());
            write_base_max_hp(companion.getMaxHp());
            write_temp_max_hp(0);
            write_hp(companion.getCurrentHp());
            write_bonus_str(info.getAddStr());
            write_bonus_con(info.getAddCon());
            write_bonus_int(info.getAddInt());
            write_elixir_use_count(info.get_elixir_use_count());
            write_remain_stats(info.get_remain_stats());
            write_minus_exp_penalty(false);
			break;
		case STAT:
			write_base_max_hp(companion.getMaxHp());
			write_bonus_str(info.getAddStr());
            write_bonus_con(info.getAddCon());
            write_bonus_int(info.getAddInt());
            write_elixir_use_count(info.get_elixir_use_count());
            write_remain_stats(info.get_remain_stats());
		    write_ac(companion.getAC().getAc());
		    write_mr(companion.getResistance().getEffectedMrBySkill());
			break;
		case NAME_CHANGE:
			write_name(companion.getName().getBytes());
			break;
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
	    writeH(NOTI);
	}
	
	void write_obj_id(int obj_id) {
		writeRaw(0x08);
		writeBit(obj_id);
	}
	
	void write_name(byte[] name) {
		writeRaw(0x12);
		writeBytesWithLength(name);
	}
	
	void write_class_id(int class_id) {
		writeRaw(0x18);
		writeBit(class_id);
	}
	
	void write_level(int level) {
		writeRaw(0x20);
		writeRaw(level);
	}
	
	void write_exp(int exp) {
		writeRaw(0x28);
		writeBit(exp);
	}
	
	void write_base_max_hp(int base_max_hp) {
		writeRaw(0x30);
		writeBit(base_max_hp);
	}
	
	void write_temp_max_hp(int temp_max_hp) {
		writeRaw(0x38);
		writeBit(temp_max_hp);
	}
	
	void write_hp(int hp) {
		writeRaw(0x40);
		writeBit(hp);
	}
	
	void write_base_str(int base_str) {
		writeRaw(0x48);
		writeRaw(base_str);
	}
	
	void write_base_con(int base_con) {
		writeRaw(0x50);
		writeRaw(base_con);
	}
	
	void write_base_int(int base_int) {
		writeRaw(0x58);
		writeRaw(base_int);
	}
	
	void write_bonus_str(int bonus_str) {
		writeRaw(0x60);
		writeRaw(bonus_str);
	}
	
	void write_bonus_con(int bonus_con) {
		writeRaw(0x68);
		writeRaw(bonus_con);
	}
	
	void write_bonus_int(int bonus_int) {
		writeRaw(0x70);
		writeRaw(bonus_int);
	}
	
	void write_elixir_use_count(int elixir_use_count) {
		writeRaw(0x78);
		writeRaw(elixir_use_count);
	}
	
	void write_remain_stats(int remain_stats) {
		writeH(0x0180);
		writeRaw(remain_stats);
	}
	
	void write_temp_str(int temp_str) {
		writeH(0x0188);
		writeRaw(temp_str);
	}
	
	void write_temp_con(int temp_con) {
		writeH(0x0190);
		writeRaw(temp_con);
	}
	
	void write_temp_int(int temp_int) {
		writeH(0x0198);
		writeRaw(temp_int);
	}
	
	void write_friend_ship_guage(int friend_ship_guage) {
		writeH(0x01a0);
		writeBit(friend_ship_guage);
	}
	
	void write_friend_ship_marble(int friend_ship_marble) {
		writeH(0x01a8);
		writeBit(friend_ship_marble);
	}
	
	void write_minus_exp_penalty(boolean minus_exp_penalty) {
		writeH(0x01b0);
		writeB(minus_exp_penalty);
	}

	void write_ac(int ac) {
		writeH(0x01f0);
		writeBit(ac);
	}
	
	void write_mr(int mr) {
		writeH(0x01f8);
		writeBit(mr);
	}
	
	void write_movedelay_reduce(double movedelay_reduce) {
		writeH(0x0281);
		writeDouble(movedelay_reduce);
	}
	
	void write_attackdelay_reduce(double attackdelay_reduce) {
		writeH(0x0289);
		writeDouble(attackdelay_reduce);
	}
	
	void write_pvp_dmg_ratio(int pvp_dmg_ratio) {
		writeH(0x0290);
		writeBit(pvp_dmg_ratio);
	}
	
	void write_is_combo_time(boolean is_combo_time) {
		writeH(0x0298);
		writeB(is_combo_time);
	}
	
	public byte[] getContent() {
	    if (_byte == null) {
	    	_byte = getBytes();
	    }
	    return _byte;
	}
  
    @Override
	public String getType() {
		return S_COMPANION_STATUS_NOTI;
    }
}

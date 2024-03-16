package l1j.server.server.serverpackets.object;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class WorldPutObjectStream extends ServerBasePacket {
	protected static final byte[] MINUS_BYTES = { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x01 };
	protected static final int WORLD_PUT_OBJECT = 0x0077;
	
	protected void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(WORLD_PUT_OBJECT);
	}
	
	protected void write_point(int x, int y) {
    	writeRaw(0x08);// point
        writeLongLocationReverse(x, y);
    }
    
	protected void write_objectnumber(int objectnumber) {
    	writeRaw(0x10);// objectnumber
        writeBit(objectnumber);
    }
    
	protected void write_objectsprite(int objectsprite) {
    	writeRaw(0x18);// objectsprite
        writeBit(objectsprite);
    }
    
	protected void write_action(int action) {
    	writeRaw(0x20);// action
    	writeBit(action);
    }
    
	protected void write_direction(int direction) {
    	writeRaw(0x28);// direction
    	writeRaw(direction);
    }
    
	protected void write_lightRadius(int lightRadius) {
    	writeRaw(0x30);// lightRadius
    	writeRaw(lightRadius);
    }
    
	protected void write_objectcount(int objectcount) {
    	writeRaw(0x38);// objectcount
    	writeBit(objectcount);
    }
    
	protected void write_alignment(int alignment) {
    	writeRaw(0x40);// alignment
        writeBit(alignment);
    }
	
	protected void write_desc(String desc) {
    	writeRaw(0x4a);// desc
    	writeStringWithLength(desc);
    }
	
	protected void write_title(String title) {
    	writeRaw(0x52);// title
    	writeStringWithLength(title);
    }
    
	protected void write_speeddata(int speeddata) {
    	writeRaw(0x58);// speeddata
    	writeRaw(speeddata);
    }
    
	protected void write_emotion(int emotion) {
    	writeRaw(0x60);// emotion
    	writeRaw(emotion);
    }
    
	protected void write_drunken(int drunken) {
    	writeRaw(0x68);// drunken
    	writeRaw(drunken);
    }
    
	protected void write_isghost(boolean isghost) {
    	writeRaw(0x70);// isghost
        writeB(isghost);
    }
    
	protected void write_isparalyzed(boolean isparalyzed) {
    	writeRaw(0x78);// isparalyzed
        writeB(isparalyzed);
    }
    
	protected void write_isuser(boolean isuser) {
    	writeH(0x0180);// isuser
        writeB(isuser);
    }
    
	protected void write_isinvisible(boolean isinvisible) {
    	writeH(0x0188);// isinvisible
        writeB(isinvisible);
    }
    
	protected void write_ispoisoned(boolean ispoisoned) {
    	writeH(0x0190);// ispoisoned
		writeB(ispoisoned);
    }
    
	protected void write_emblemid(int emblemid) {
    	writeH(0x0198);// emblemid
		writeBit(emblemid);
    }
	
	protected void write_pledgename(String pledgename) {
    	writeH(0x01a2);// pledgename
    	writeStringWithLength(pledgename);
    }
	
	protected void write_mastername(String mastername) {
    	writeH(0x01aa);// mastername
    	writeStringWithLength(mastername);
    }
    
	protected void write_altitude(int altitude) {
    	writeH(0x01b0);// altitude
    	writeRaw(altitude);
    }
    
	protected void write_hitratio(int hitratio) {
    	writeH(0x01b8);// hitratio
		writeByte(MINUS_BYTES);
    }
    
	protected void write_safelevel(int safelevel) {
    	writeH(0x01c0);// safelevel
        writeBit(safelevel);
    }
    
	protected void write_shoptitle(byte[] shoptitle) {
    	writeH(0x01ca);// shoptitle
    	writeBytesWithLength(shoptitle);
    }
    
	protected void write_weaponsprite(int weaponsprite) {
    	writeH(0x01d0);// weaponsprite
        writeByte(MINUS_BYTES);
    }
    
	protected void write_couplestate(int couplestate) {
    	writeH(0x01d8);// couplestate
    	writeRaw(couplestate);
    }
    
	protected void write_boundarylevelindex(int boundarylevelindex) {
    	writeH(0x01e0);// boundarylevelindex
    	writeRaw(boundarylevelindex);
    }
    
	protected void write_weakelemental(int weakelemental) {
    	writeH(0x01e8);// weakelemental
    	writeRaw(weakelemental);
    }
    
	protected void write_manaratio(int manaratio) {
    	writeH(0x01f0);// manaratio
		writeByte(MINUS_BYTES);
    }
    
	protected void write_botindex(int botindex) {
		writeH(0x01f8);// botindex
		writeBit(botindex);
	}
    
	protected void write_homeserverno(int homeserverno) {
		writeH(0x0280);// homeserverno
		writeBit(homeserverno);
	}
    
	protected void write_team_id(int team_id) {
		writeH(0x0288);// team_id
		writeBit(team_id);
	}
    
	protected void write_dialog_radius(int dialog_radius) {
		writeH(0x0290);// dialog_radius
		writeRaw(dialog_radius);
	}
    
	protected void write_speed_value_flag(int speed_value_flag) {
    	writeH(0x0298);// speed_value_flag
    	writeRaw(speed_value_flag);
    }
	
	protected void write_second_speed_type(int second_speed_type) {
    	writeH(0x02a0);// second_speed_type
    	writeRaw(second_speed_type);
    }
    
	protected void write_explosion_remain_time_ms(long explosion_remain_time_ms) {
    	writeH(0x02a8);// explosion_remain_time_ms
		writeBit(explosion_remain_time_ms);
    }
    
	protected void write_proclamation_siege_mark(boolean proclamation_siege_mark) {
		writeH(0x02b0);// proclamation_siege_mark
		writeB(proclamation_siege_mark);
	}
    
	protected void write_npc_class_id(int npc_class_id) {
    	writeH(0x02b8);// npc_class_id
		writeBit(npc_class_id);
    }
	
	protected void write_companion(L1PetInstance companion) {
		BinaryOutputStream os = new BinaryOutputStream();
		os.writeC(0x09);
		os.writeDouble(companion.get_movedelay_reduce());
		os.writeC(0x11);
		os.writeDouble(companion.get_attackdelay_reduce());
		os.writeC(0x18);
		os.writeBit(companion.get_pvp_dmg_ratio());
		writeH(0x02c2);// companion
    	writeBytesWithLength(os.getBytes());
    	try {
			os.close();
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	protected void write_force_haction(String force_haction) {
    	writeH(0x02ca);// force_haction
    	writeStringWithLength(force_haction);
    }
    
	protected void write_is_portal(boolean is_portal) {
    	writeH(0x02d0);// is_portal
		writeB(is_portal);
    }
	
	protected void write_proclamation_siege_pledge(boolean proclamation_siege_pledge) {
		writeH(0x02d8);// proclamation_siege_pledge
		writeB(proclamation_siege_pledge);
	}
    
	protected void write_drop_info(int get_available_time, int reward_prob) {
    	int length = 1 + getBitSize(get_available_time);
    	if (reward_prob > 0) {
    		length += 1 + getBitSize(reward_prob);
    	}
		writeH(0x02e2);// drop_info
		writeRaw(length);
		
		writeRaw(0x08);// get_available_time
		writeBit(get_available_time);
		
		if (reward_prob > 0) {
			writeRaw(0x10);// reward_prob
			writeBit(reward_prob);
		}
	}
	
	protected void write_is_castle_owner_group(boolean is_castle_owner_group) {
		writeH(0x02e8);// is_castle_owner_group
		writeB(is_castle_owner_group);
	}
    
	protected void write_user_game_class(int user_game_class) {
		writeH(0x02f0);// user_game_class
		writeRaw(user_game_class);
	}
    
	protected void write_anonymity_name(byte[] anonymity_name) {
		writeH(0x02fa);// anonymity_name
		writeBytesWithLength(anonymity_name);
	}
	
	protected void write_anonymity_type(int anonymity_type) {
		writeH(0x0380);// anonymity_type
		writeRaw(anonymity_type);
	}
    
	protected void write_potential_grade(int potental_grade) {
    	writeH(0x0388);// potental_grade
    	writeBit(potental_grade);
    }
    
	protected void write_forth_gear(boolean forth_gear) {
		writeH(0x0390);// forth_gear
		writeB(forth_gear);
	}
	
	protected void write_speed_bonus(L1PcInstance pc) {
		writeH(0x039a);// speed_bonus
		int moveRate	= (int) pc.getMoveSpeedDelayRate();
		int attackRate	= (int) pc.getAttackSpeedDelayRate();
		int spellRate	= (int) pc.getSpellSpeedDelayRate();
		if (moveRate != 0 || attackRate != 0 || spellRate != 0) {
			write_speed_bonus(pc, moveRate, attackRate, spellRate);
		} else {
			writeRaw(0);
		}
	}
	
	protected void write_speed_change_rate(L1PcInstance pc) {
		writeH(0x03a2);// speed_change_rate
		int moveRate	= (int) pc.getMoveSpeedDelayRate();
		int attackRate	= (int) pc.getAttackSpeedDelayRate();
		int spellRate	= (int) pc.getSpellSpeedDelayRate();
		if (moveRate != 0 || attackRate != 0 || spellRate != 0) {
			write_speed_bonus(pc, moveRate, attackRate, spellRate);
		} else {
			writeRaw(0);
		}
	}
	
	void write_speed_bonus(L1PcInstance pc, int moveRate, int attackRate, int spellRate) {
		int length		= 0;
		byte[] moveByte	= null, attackByte = null, spellByte = null;
		if (moveRate != 0) {
			moveByte	= pc.getMoveSpeedDelayByte();
			length		+= moveByte.length;
		}
		if (attackRate != 0) {
			attackByte	= pc.getAttackSpeedDelayByte();
			length		+= attackByte.length;
		}
		if (spellRate != 0) {
			spellByte	= pc.getSpellSpeedDelayByte();
			length		+= spellByte.length;
		}
		writeRaw(length);
		if (moveRate != 0) {
			writeByte(moveByte);
		}
		if (attackRate != 0) {
			writeByte(attackByte);
		}
		if (spellRate != 0) {
			writeByte(spellByte);
		}
	}
	
	protected void write_is_apc(){// ai 인식
		writeH(0x03a8);// is_apc
		writeB(true);
	}
	
	protected void write_category(int category) {
    	writeH(0x03b0);// category
    	writeBit(category);
    }
	
	protected void write_is_excavated(boolean is_excavated) {
		writeH(0x03b8);// is_excavated
		writeB(is_excavated);
	}
	
	protected void write_apc_pledge_icon(int apc_pledge_icon){// ai 혈문장
		writeH(0x03c0);// apc_pledge_icon
		writeBit(apc_pledge_icon);
	}
	
	protected void write_above_head_mark_id(int above_head_mark_id) {
		writeH(0x03c8);// above_head_mark_id
		writeBit(above_head_mark_id);
	}
	
	protected void write_fix_frame_level(int fix_frame_level) {
		writeH(0x03d0);// fix_frame_level
		writeRaw(fix_frame_level);
	}
	
	protected void write_polymorph_effect_id(int polymorph_effect_id) {
		writeH(0x03d8);// polymorph_effect_id
		writeBit(polymorph_effect_id);
	}
	
	protected void write_invisible_level(int invisible_level) {
    	writeH(0x03e0);// invisible_level
    	writeRaw(invisible_level);
    }
	
	protected void write_above_head_effect_id(int above_head_effect_id) {
		writeH(0x03e8);// above_head_effect_id
		writeBit(above_head_effect_id);
	}
	
	protected void write_prev_server_id(int prev_server_id) {
		writeH(0x03f0);// prev_server_id
    	writeBit(prev_server_id);
	}
	
	protected void write_proclamation_siege_mark_castle(int proclamation_siege_mark_castle) {
		writeH(0x03f8);// proclamation_siege_mark_castle
		writeBit(proclamation_siege_mark_castle);
	}

	@Override
	public byte[] getContent() throws IOException {
        return null;
	}

}


package l1j.server.server.serverpackets.indun;

import l1j.server.server.utils.BinaryOutputStream;

public class ArenaPlayerStatusStream extends BinaryOutputStream {
	
	public ArenaPlayerStatusStream() {
		super();
	}
	
	void write_arena_char_id(long arena_char_id) {
		writeC(0x08);// arena_char_id
		writeBit(arena_char_id);
	}
	
	void write_kill_count(int kill_count) {
		writeC(0x10);
		writeC(kill_count);
	}
	
	void write_death_count(int death_count) {
		writeC(0x18);
		writeC(death_count);
	}
	
	void write_hp_ratio(int hp_ratio) {
		writeC(0x20);
		writeC(hp_ratio);
	}
	
	void write_mp_ratio(int mp_ratio) {
		writeC(0x28);
		writeC(mp_ratio);
	}
	
	void write_loc_x(int loc_x) {
		writeC(0x30);
		writeBit(loc_x);
	}
	
	void write_loc_y(int loc_y) {
		writeC(0x38);
		writeBit(loc_y);
	}
	
	void write_attack_amount(int attack_amount) {
		writeC(0x40);
		writeBit(attack_amount);
	}
	
	void wirte_damaged_amount(int damaged_amount) {
		writeC(0x48);
		writeBit(damaged_amount);
	}
	
	void wirte_heal_amount(int heal_amount) {
		writeC(0x50);
		writeBit(heal_amount);
	}
	
	void write_poisoned(boolean poisoned) {
		writeC(0x58);
		writeB(poisoned);
	}
	
	void write_paralysed(boolean paralysed) {
		writeC(0x60);
		writeB(paralysed);
	}
	
	void write_is_live(boolean is_live) {
		writeC(0x68);
		writeB(is_live);
	}
	
	void write_obj_id(int obj_id) {
		writeC(0x70);
		writeBit(obj_id);
	}
	
	void write_is_ready(boolean is_ready) {
		writeC(0x78);
		writeB(is_ready);
	}
	
}


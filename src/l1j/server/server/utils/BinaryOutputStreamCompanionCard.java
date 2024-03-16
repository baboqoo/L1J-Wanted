package l1j.server.server.utils;

import l1j.server.server.templates.L1Pet;

/**
 * 아이템 펫 목걸이 프로토 패킷 Stream
 * @author LinOffice
 */
public class BinaryOutputStreamCompanionCard extends BinaryOutputStream {
	public BinaryOutputStreamCompanionCard() {
		super();
	}
	
	public BinaryOutputStreamCompanionCard(L1Pet companion) {
		super();
		write_oblivion(companion.isOblivion());
		write_is_dead(companion.isDead());
		write_is_summoned(companion.is_summoned());
		write_class_id(companion.getClassT().get_classId());
		write_level(companion.getLevel());
		write_name(companion.getName());
		write_elixir_use_count(companion.get_elixir_use_count());
	}
	
	public void write_oblivion(boolean oblivion) {
		writeC(0x08);// oblivion
    	writeB(oblivion);
	}
	
	public void write_is_dead(boolean is_dead) {
		writeC(0x10);// is_dead
    	writeB(is_dead);
	}
	
	public void write_is_summoned(boolean is_summoned) {
		writeC(0x18);// is_summoned
    	writeB(is_summoned);
	}
	
	public void write_class_id(int class_id) {
		writeC(0x20);// class_id
    	writeBit(class_id);
	}
	
	public void write_level(int level) {
		writeC(0x28);// level
    	writeC(level);
	}
	
	public void write_name(String name) {
		writeC(0x32);// name
    	writeBytesWithLength(name.getBytes());
	}
	
	public void write_elixir_use_count(int elixir_use_count) {
		writeC(0x38);// elixir_use_count
    	writeC(elixir_use_count);
	}
	
}


package l1j.server.server.serverpackets.system;

import l1j.server.server.utils.BinaryOutputStream;

public class DISABLE_FREE_BUFF_SHIELD extends BinaryOutputStream{
	private int favor_locked_time;
	public int get_favor_locked_time() {
		return favor_locked_time;
	}
	public void set_favor_locked_time(int val) {
		favor_locked_time = val;
	}
	public int add_and_get_favor_locked_time(int val) {
		return favor_locked_time += val;
	}
	
	public DISABLE_FREE_BUFF_SHIELD() {
		super();
	}
	
	void write_favor_locked_time() {
		writeC(0x08);
		writeBit(favor_locked_time);
	}
	
	void write_data() {
		reset();
		write_favor_locked_time();
	}
	
	public void dispose() {
		clear();
	}
}


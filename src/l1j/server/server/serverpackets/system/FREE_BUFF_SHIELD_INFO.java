package l1j.server.server.serverpackets.system;

import l1j.server.server.utils.BinaryOutputStream;

public class FREE_BUFF_SHIELD_INFO extends BinaryOutputStream {
	private FREE_BUFF_SHIELD_TYPE _favor_type;
	private int _favor_remain_count;
	
	public FREE_BUFF_SHIELD_TYPE get_favor_type() {
		return _favor_type;
	}
	public void set_favor_type(FREE_BUFF_SHIELD_TYPE val) {
		_favor_type = val;
	}
	
	public int get_favor_remain_count() {
		return _favor_remain_count;
	}
	public void set_favor_remain_count(int val) {
		_favor_remain_count = val;
	}
	public void add_favor_remain_count(int val) {
		_favor_remain_count += val;
	}
	
	public FREE_BUFF_SHIELD_INFO() {
		super();
	}
	
	void write_favor_type(FREE_BUFF_SHIELD_TYPE favor_type) {
		writeC(0x08);
		writeC(favor_type.toInt());
	}
	
	void write_favor_remain_count(int favor_remain_count) {
		writeC(0x10);
		writeC(favor_remain_count);
	}
	
	void write_data() {
		reset();
		write_favor_type(_favor_type);
		write_favor_remain_count(_favor_remain_count);
	}
	
	public void dispose() {
		clear();
	}
}


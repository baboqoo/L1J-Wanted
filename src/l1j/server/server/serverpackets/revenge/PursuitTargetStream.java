package l1j.server.server.serverpackets.revenge;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class PursuitTargetStream extends BinaryOutputStream {
	
	public PursuitTargetStream() {
		super();
	}
	
	public PursuitTargetStream(String user_name, eRevengeResult result) {
		super();
		L1PcInstance target	= L1World.getInstance().getPlayer(user_name);
		write_server_no(Config.VERSION.SERVER_NUMBER);
		write_user_name(user_name);
		write_result(result);
		if (target != null) {
			write_world_number(target.getMapId());
			write_location(target.getX(), target.getY());
		}
	}
	
	void write_server_no(int server_no) {
		writeC(0x08);// server_no
		writeBit(server_no);
	}
	
	void write_user_name(String user_name) {
		writeC(0x12);//	user_name
		writeStringWithLength(user_name);
	}
	
	void write_result(eRevengeResult result) {
		writeC(0x18);// result
		writeC(result.toInt());
	}
	
	void write_world_number(int world_number) {
		writeC(0x20);// world_number
		writeBit(world_number);
	}
	
	void write_location(int x, int y) {
		writeC(0x28);// location
		writeLongLocationReverse(x, y);
	}
	
	void write_class_id(int class_id) {
		writeC(0x30);// class_id
		writeC(class_id);
	}
	
}


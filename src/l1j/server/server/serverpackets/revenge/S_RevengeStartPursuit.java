package l1j.server.server.serverpackets.revenge;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_RevengeStartPursuit extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_REVENGE_START_PURSUIT = "[S] S_RevengeStartPursuit";
	public static final int START_PURSUIT	= 0x0421;
	
	public S_RevengeStartPursuit(String user_name, eRevengeResult result) {
		write_init();
		write_result(result);
		if (result == eRevengeResult.SUCCESS) {
			write_target(user_name);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(START_PURSUIT);
	}
	
	void write_result(eRevengeResult result) {
		writeC(0x08);
		writeC(result.toInt());
	}
	
	void write_target(String user_name) {
		PursuitTargetStream os = null;
		try {
			os = new PursuitTargetStream();
			os.write_server_no(Config.VERSION.SERVER_NUMBER);
			os.write_user_name(user_name);
			L1PcInstance target	= L1World.getInstance().getPlayer(user_name);
			if (target != null) {
				os.write_world_number(target.getMapId());
				os.write_location(target.getX(), target.getY());
			}
			
			writeC(0x12);
			writeBytesWithLength(os.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_REVENGE_START_PURSUIT;
	}
}

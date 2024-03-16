package l1j.server.server.serverpackets.revenge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_RevengePursuitInfo extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_REVENGE_PURSUIT_INFO = "[S] S_RevengePursuitInfo";
	public static final int PURSUIT_INFO	= 0x0423;
	
	public static final S_RevengePursuitInfo FAIL_USER	= new S_RevengePursuitInfo(null, eRevengeResult.FAIL_USER);
	public static final S_RevengePursuitInfo FAIL_OTHER	= new S_RevengePursuitInfo(null, eRevengeResult.FAIL_OTHER);
	
	public S_RevengePursuitInfo(String name, eRevengeResult result) {
		write_init();
		write_result(result);
		if (result == eRevengeResult.SUCCESS) {
			write_target(name, result);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PURSUIT_INFO);
	}
	
	void write_result(eRevengeResult result) {
		writeC(0x08);
		writeC(result.toInt());
	}
	
	void write_target(String name, eRevengeResult result) {
		PursuitTargetStream os = null;
		try {
			os = new PursuitTargetStream(name, result);
			writeC(0x12);// target
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
		return S_REVENGE_PURSUIT_INFO;
	}
}

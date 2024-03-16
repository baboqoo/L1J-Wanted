package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_KeepAlive extends ServerBasePacket {
	private static final String S_KEEP_ALIVE = "[S] S_KeepAlive";
	private byte[] _byte = null;
	public static final int ALIVE = 0x03E7;
	
	public S_KeepAlive(int transaction_id, long client_time, long server_time) {
		write_init();
		write_transaction_id(transaction_id);
		write_client_time(client_time);
		write_server_time(server_time);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ALIVE);
	}
	
	void write_transaction_id(int transaction_id) {
		writeRaw(0x08);
		writeBit(transaction_id);
	}
	
	void write_client_time(long client_time) {
		writeRaw(0x10);
		writeBit(client_time);
	}
	
	void write_server_time(long server_time) {
		writeRaw(0x18);
		writeBit(server_time);
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
		return S_KEEP_ALIVE;
	}
}


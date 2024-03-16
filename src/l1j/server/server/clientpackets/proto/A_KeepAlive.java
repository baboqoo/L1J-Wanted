package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.S_KeepAlive;

public class A_KeepAlive extends ProtoHandler {
	protected A_KeepAlive(){}
	private A_KeepAlive(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _transaction_id;
	private long _client_time;

	@Override
	protected void doWork() throws Exception {
		if (_client == null) {
			return;
		}
		readP(1);// 0x08
		_transaction_id = readBit();
		readP(1);// 0x10
		_client_time	= readLong();
		_pc.sendPackets(new S_KeepAlive(_transaction_id, _client_time, System.currentTimeMillis() / 1000));
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_KeepAlive(data, client);
	}

}


package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.S_DisconnectSocket;

public class A_MoveLogoutNoti extends ProtoHandler {
	protected A_MoveLogoutNoti(){}
	private A_MoveLogoutNoti(byte[] data, GameClient client) {
		super(data, client);
	}
	
	long reservednumber;

	@Override
	protected void doWork() throws Exception {
		try {
			if (_client == null) {
				return;
			}
			readP(1);// 0x08
			reservednumber = readLong();
			_client.sendPacket(S_DisconnectSocket.DISCONNECTED);
			_client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_MoveLogoutNoti(data, client);
	}

}


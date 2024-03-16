package l1j.server.server.utils;

import java.util.LinkedList;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.ServerBasePacket;

public class DelaySender implements Runnable {
	private LinkedList<ServerBasePacket> _pck;
	private GameClient _client;
	
	public DelaySender(GameClient client){
		_pck 	= new LinkedList<ServerBasePacket>();
		_client = client;
	}
	
	public void add(ServerBasePacket pck){
		_pck.add(pck);
	}
	
	@Override
	public void run() {
		if (_client == null || _pck.isEmpty()) {
			return;
		}
		try {
			for (int i = 0; i < _pck.size(); i++) {
				send(_pck.get(i));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			_pck.clear();
			_pck = null;
		}
	}
	
	private void send(ServerBasePacket pck) throws Exception {
		_client.sendPacket(pck);
		pck.clear();
		pck = null;
	}
}


package l1j.server.server.utils;

import l1j.server.server.GameClient;
import l1j.server.server.controller.LoginController;

public class DelayClose implements Runnable {
	private GameClient _client;
	public DelayClose(GameClient client){
		_client = client;
	}
	
	@Override
	public void run() {
		if (_client == null) {
			return;
		}
		try {
			if (_client.getActiveChar() != null) {
				_client.getActiveChar().logout();
			}
			LoginController.getInstance().logout(_client);
			_client.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}


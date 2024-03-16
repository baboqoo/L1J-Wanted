package l1j.server;

import l1j.server.common.data.ChatType;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ChatMessageNoti;

public class ServerChat {
	private static volatile ServerChat uniqueInstance = null;
	
	private ServerChat() {}
	
	static public ServerChat getInstance() {
		if (uniqueInstance == null) {
			synchronized (ServerChat.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new ServerChat();
				}
			}
		}
		
		return uniqueInstance;
	}

	public void sendMessageToAllUser(String message) {
		L1World.getInstance().broadcastServerMessage(message, true);
	}

	public boolean sendMessageToPlayer(String userName, String message) {
		L1PcInstance player = L1World.getInstance().getPlayer(userName);
		if (player != null) {
			player.sendPackets(new S_ChatMessageNoti(player, ChatType.CHAT_WHISPER, message.getBytes(), null, 0), true);
			return true;
		}
		return false;
	}
}


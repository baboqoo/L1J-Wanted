package l1j.server.server.controller.action;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

/**
 * 시스템 메세지 출력 컨트롤러
 * @author LinOffice
 */
public class SystemMessage implements ControllerInterface {
	private static class newInstance {
		public static final SystemMessage INSTANCE = new SystemMessage();
	}
	public static SystemMessage getInstance() {
		return newInstance.INSTANCE;
	}
	
	static final ServerBasePacket[] MESSAGES;
	static {
		int count = 0;
		String[] msgs = { 
				Config.MESSAGE.SYSTEM_MESSAGE_1,
				Config.MESSAGE.SYSTEM_MESSAGE_2,
				Config.MESSAGE.SYSTEM_MESSAGE_3,
				Config.MESSAGE.SYSTEM_MESSAGE_4,
				Config.MESSAGE.SYSTEM_MESSAGE_5,
				Config.MESSAGE.SYSTEM_MESSAGE_6,
				Config.MESSAGE.SYSTEM_MESSAGE_7,
				Config.MESSAGE.SYSTEM_MESSAGE_8,
				Config.MESSAGE.SYSTEM_MESSAGE_9,
				Config.MESSAGE.SYSTEM_MESSAGE_10,
				Config.MESSAGE.SYSTEM_MESSAGE_11,
				Config.MESSAGE.SYSTEM_MESSAGE_12,
				Config.MESSAGE.SYSTEM_MESSAGE_13,
				Config.MESSAGE.SYSTEM_MESSAGE_14,
				Config.MESSAGE.SYSTEM_MESSAGE_15,
				Config.MESSAGE.SYSTEM_MESSAGE_16
		};
		for (String sys : msgs) {
			if (StringUtil.isNullOrEmpty(sys)) {
				break;
			}
			count++;
		}
		MESSAGES = new ServerBasePacket[count];
		for (int i=0; i<MESSAGES.length; i++) {
			MESSAGES[i] = new S_SystemMessage(msgs[i], true);
		}
	}
	int _messageCnt;
	
	private SystemMessage(){}
	
	@Override
	public void execute() {
		sendMessage(MESSAGES[_messageCnt]);
		if (++_messageCnt >= MESSAGES.length) {
			_messageCnt = 0;
		}
	}
	
	void sendMessage(ServerBasePacket pck) {
		L1World.getInstance().broadcastPacketToAll(pck); 
	}

	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}

}


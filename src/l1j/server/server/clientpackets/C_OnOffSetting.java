package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_OnOffSetting extends ClientBasePacket {
	private static final String C_ONOFF_SETTING	= "[C] C_OnOffSetting";
	private static final int WORLD_CHAT			= 0;
	private static final int WHISPER			= 2;
	private static final int SHOP_CHAT			= 6;
	
	private static final int GLOBAL_MESSAGE		= 12;
	private static final int DEFAULT_BUTTON		= 255;

	public C_OnOffSetting(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int type	= readC();
		int button	= readC();

		L1CharacterConfig config = pc.getConfig();
		switch(type){
		case DEFAULT_BUTTON:
			if (button == 95 || button == 127) {
				config.setShowWorldChat(true);// open
				config.setCanWhisper(true);// open
			} else if (button == 91 || button == 123) {
				config.setShowWorldChat(true);// open
				config.setCanWhisper(false);// close
			} else if (button == 94 || button == 126) {
				config.setShowWorldChat(false);// close
				config.setCanWhisper(true);// open
			} else if (button == 90 || button == 122) {
				config.setShowWorldChat(false);// close
				config.setCanWhisper(false);// close
			}
			break;
		case WORLD_CHAT:// 전체 채팅
			config.setShowWorldChat(button == 1);
			break;
		case WHISPER:// Whisper
			config.setCanWhisper(button == 1);
			break;
		case SHOP_CHAT:// 장사 채팅
			config.setShowTradeChat(button == 1);
			break;
		case GLOBAL_MESSAGE:// 글로벌 메세지
			config.setGlobalMessege(button == 1);
			break;
		}
	}

	@Override
	public String getType() {
		return C_ONOFF_SETTING;
	}
}


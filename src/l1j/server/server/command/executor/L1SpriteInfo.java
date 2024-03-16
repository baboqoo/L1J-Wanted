package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.sprite.L1Sprite;
import l1j.server.server.model.sprite.SpriteLoader;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1SpriteInfo implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1SpriteInfo();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1SpriteInfo() {}
	
	private static final String EMPTY_MESSAGE = " - Information for that SPRITE could not be found.";

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null || pc.getNetConnection() == null) {
				return false;
			}
			StringTokenizer st = new StringTokenizer(arg);
			int spriteId	= Integer.parseInt(st.nextToken(), 10);
			String result	= getSpriteInfoToString(spriteId);
			if (StringUtil.isNullOrEmpty(result)) {
				result		= spriteId + EMPTY_MESSAGE;
			}
			pc.sendPackets(new S_SystemMessage(result), true);
			result			= null;
			st				= null;
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [spriteId] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(676), true), true);
			return false;
		}
	}
	
	public String getSpriteInfoToString(int sprite_id){
		L1Sprite spr = SpriteLoader.get_sprite(sprite_id);
		if (spr == null) {
			return null;
		}
		return spr.toString();
	}
}



package l1j.server.server.serverpackets.command;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.StringUtil;

public class S_WhoCharinfo extends ServerBasePacket {
	private static final String S_WHO_CHARINFO = "[S] S_WhoCharinfo";
	private byte[] _byte = null;
	
	private static final String[] ALIGN_NAMES = {
		"(Chaotic)", "(Neutral)", "(Lawful)"
	};

	public S_WhoCharinfo(L1PcInstance pc) {
		String align_desc = StringUtil.EmptyString;
		/* Kill & Death 시스템 */		
		float win, lose, total, winner;		
		win		= pc.getKills();
		lose	= pc.getDeaths();
		total	= win + lose;
		winner	= (win * 100) / total;

		int align = pc.getAlignment();
		if (align >= 500) {
			align_desc = ALIGN_NAMES[2];
		} else if(align >= -499) {
			align_desc = ALIGN_NAMES[1];
		} else {
			align_desc = ALIGN_NAMES[0];
		}

		//writeC(Opcodes.S_MESSAGE);
		//writeC(0x08);
		writeC(Opcodes.S_SAY_CODE);
		writeC(2);
		writeD(0);

		String title	= StringUtil.EmptyString;
		String clan		= StringUtil.EmptyString;

		if (!StringUtil.isNullOrEmpty(pc.getTitle())) {
			title = pc.getTitle() + StringUtil.EmptyOneString;
		}
		if (pc.getClanid() > 0) {
			clan = String.format("[%s]", pc.getClanName());
		}

		if (Config.ALT.SHOW_PVP_KILL) {
			//writeS(title + pc.getName() + StringUtil.EmptyOneString + align_desc + StringUtil.EmptyOneString + clan + "\n\r킬: " + pc.getKills() + " / 데스: "+pc.getDeaths() +" / 승률:" + winner + "%");
			writeS(title + pc.getName() + StringUtil.EmptyOneString + align_desc + StringUtil.EmptyOneString + clan + "\n\r" + S_SystemMessage.getRefText(130) + ": " + pc.getKills() + " / " + S_SystemMessage.getRefText(117) + ": " + pc.getDeaths() + " / $30044: " + winner + "%");
		} else {
			writeS(title + pc.getName() + StringUtil.EmptyOneString + align_desc + StringUtil.EmptyOneString + clan);
		}
		writeD(0);
	}
	
	public S_WhoCharinfo(L1NpcShopInstance shopnpc) { 
		String align_desc = StringUtil.EmptyString;
		/* Kill & Death 시스템 */	
		float win, lose, total, winner;
		win		= shopnpc.getKills();
		lose	= shopnpc.getDeaths();
		total	= win + lose;
		winner	= (win * 100) / total;

		int align = shopnpc.getAlignment();
		if (align >= 500) {
			align_desc = ALIGN_NAMES[2];
		} else if (align >= -499) {
			align_desc = ALIGN_NAMES[1];
		} else {
			align_desc = ALIGN_NAMES[0];
		}

		//writeC(Opcodes.S_MESSAGE);
		//writeC(0x08);
		writeC(Opcodes.S_SAY_CODE);
		writeC(2);
		writeD(0);

		String title = StringUtil.EmptyString;
		if (!StringUtil.isNullOrEmpty(shopnpc.getTitle())) {
			title = shopnpc.getTitle() + StringUtil.EmptyOneString;
		}
		if (Config.ALT.SHOW_PVP_KILL) {
			//writeS(title + shopnpc.getName() + StringUtil.EmptyOneString + align_desc + "\n\r킬: " + shopnpc.getKills() + " / 데스: "+shopnpc.getDeaths() +" / 승률:" + winner + "%");		
			writeS(title + shopnpc.getName() + StringUtil.EmptyOneString + align_desc + "\n\r" + S_SystemMessage.getRefText(130) + ": " + shopnpc.getKills() + " / " + S_SystemMessage.getRefText(117) + ": " + shopnpc.getDeaths() + " / $30044: " + winner + "%");
		} else {
			writeS(title + shopnpc.getName() + StringUtil.EmptyOneString + align_desc);
		}
		writeD(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}
	
	@Override
	public String getType() {
		return S_WHO_CHARINFO;
	}
}


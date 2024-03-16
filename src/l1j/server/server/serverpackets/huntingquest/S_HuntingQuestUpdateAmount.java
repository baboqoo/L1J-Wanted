package l1j.server.server.serverpackets.huntingquest;

import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTemp;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_HuntingQuestUpdateAmount extends ServerBasePacket {
	private static final String S_HUNTING_QUEST_UPDATE_AMOUNT	= "[S] S_HuntingQuestUpdateAmount";
	private byte[] _byte	= null;
	public static final int UPDATE_AMOUNT	= 0x098c;
	
	public S_HuntingQuestUpdateAmount(HuntingQuestUserTemp temp) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(UPDATE_AMOUNT);
		writeRaw(0x0a);// hunting_quest_map
		writeBytesWithLength(get_hunting_quest_map(temp));
		writeH(0x00);
	}
	
	byte[] get_hunting_quest_map(HuntingQuestUserTemp temp){
		HuntingQuestMapInfo os = null;
		try {
			os = new HuntingQuestMapInfo(temp);
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_HUNTING_QUEST_UPDATE_AMOUNT;
	}
}


package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.utils.StringUtil;

public class S_NPCTalkActionTPUrl extends ServerBasePacket {
	private static final String S_NPCTalkActionTPUrl = "[S] S_NPCTalkActionTPUrl";
	private byte[] _byte = null;

	public S_NPCTalkActionTPUrl(L1NpcTalkData cha, Object[] prices, int objid) {
		buildPacket(cha, prices, objid);
	}

	private void buildPacket(L1NpcTalkData npc, Object[] prices, int objid) {
		String htmlid = StringUtil.EmptyString;
		htmlid = npc.getTeleportURL();
		writeC(Opcodes.S_HYPERTEXT);
		writeD(objid);
		writeS(htmlid);
		writeH(0x01); // 불명
		writeH(prices.length); // 인수의 수

		for (Object price : prices) {
			writeS(String.valueOf(((Integer) price).intValue()));
		}
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
		return S_NPCTalkActionTPUrl;
	}
}


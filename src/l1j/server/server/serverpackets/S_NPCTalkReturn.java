package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.NPCTalkConversionTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.utils.StringUtil;


public class S_NPCTalkReturn extends ServerBasePacket {
	private static final String S_NPC_TALK_RETURN = "[S] S_NPCTalkReturn";
	private byte[] _byte = null;

	public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action, String[] data) {
		buildPacket(objid, action == 2 ? npc.getCaoticAction() : npc.getNormalAction(), data);
	}

	public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action) {
		this(npc, objid, action, null);
	}

	public S_NPCTalkReturn(int objid, String htmlid, String[] data) {
		buildPacket(objid, htmlid, data);
	}

	public S_NPCTalkReturn(int objid, String htmlid) {
		buildPacket(objid, htmlid, null);
	}

	public S_NPCTalkReturn(int objid, L1NpcHtml html) {
		buildPacket(objid, html.getName(), html.getArgs());
	}
	
	public S_NPCTalkReturn(int objid, L1NpcHtml html, String[] data) {
		buildPacket(objid, html.getName(), data);
	}
	
	private void buildPacket(int objid, String htmlid, String[] data) {
		String htmlid_converted = NPCTalkConversionTable.getInstance().getTemplate(htmlid);
		if (htmlid_converted != null)
		  htmlid = htmlid_converted;

		//System.out.println("el uml es " + htmlid);
		writeC(Opcodes.S_HYPERTEXT);
		writeD(objid);
		writeS(htmlid);
		if (data != null && 1 <= data.length) {
			writeH(0x01); // 들어오는 npc htmlid 부분의 필요 바이트 
			writeH(data.length); // 인수의 수
			for (String datum : data) {
				writeS(datum);
			}
		} else {
			writeH(0x00);
			writeH(0x00);
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
		return S_NPC_TALK_RETURN;
	}
}


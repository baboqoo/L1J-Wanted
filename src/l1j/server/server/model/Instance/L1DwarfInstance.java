package l1j.server.server.model.Instance;

import java.util.logging.Logger;

import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.StringUtil;

public class L1DwarfInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	private static Logger _log = Logger.getLogger(L1DwarfInstance.class.getName());

	public L1DwarfInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		L1Attack attack = new L1Attack(pc, this);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
		attack = null;
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int npcId = getNpcTemplate().getNpcId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(npcId);
		if (talking != null) {
			int objid = getId();
			String htmlid = null;
			if (npcId == 60028 && !pc.isElf()) {
				htmlid = "elCE1";
			}
			if (htmlid != null) {
				if (pc.isGm())
					pc.sendPackets(new S_SystemMessage("Dialog " + htmlid), true);							
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid), true);
			} else {
				if (pc.isGm())
					pc.sendPackets(new S_SystemMessage("Dialog " + talking.getCaoticAction() + " | " + talking.getNormalAction()), true);											
				pc.sendPackets(new S_NPCTalkReturn(talking, objid, pc.getLevel() < 5 ? 2 : 1), true);
			}
		}
	}

	@Override
	public void onFinalAction(L1PcInstance pc, String action) {
		if (action.equalsIgnoreCase("retrieve")) {
			_log.finest("Retrive items in storage");
			if (pc.getLevel() < 5) {
				if (pc.isGm())
					pc.sendPackets(new S_SystemMessage("Dialog " + "dorin1"), true);														
				pc.sendPackets(new S_NPCTalkReturn(this.getId(), "dorinl", null), true);
			}
		} else if (action.equalsIgnoreCase("retrieve-pledge")) {
			_log.finest("Retrive items in pledge storage");

			if (pc.getClanName().equalsIgnoreCase(StringUtil.EmptyOneString)) {
				_log.finest("pc isnt in a pledge");
				pc.sendPackets(new S_ServerMessage(208, action), true);
			} else {
				_log.finest("pc is in a pledge");
			}
		} else if (action.equalsIgnoreCase("retrieve-char")) {
			_log.finest("Retrive items in char storage");
		}
	}
	
}

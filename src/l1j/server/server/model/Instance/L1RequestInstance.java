package l1j.server.server.model.Instance;

import java.util.logging.Logger;

import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1RequestInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger _log = Logger.getLogger(L1RequestInstance.class.getName());

	public L1RequestInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().getNpcId());
		if (talking != null) {
			if (player.isGm())
				player.sendPackets(new S_SystemMessage("Dialog " + talking.getCaoticAction() + " | " + talking.getNormalAction()), true);											
			player.sendPackets(new S_NPCTalkReturn(talking, objid, player.getAlignment() < -500 ? 2 : 1), true);
		} else {
			_log.finest("No actions for npc id : " + objid);
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
	}

	public void doFinalAction(L1PcInstance player) {
	}
}


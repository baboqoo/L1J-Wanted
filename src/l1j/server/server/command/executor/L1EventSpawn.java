package l1j.server.server.command.executor;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1EventSpawn implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1EventSpawn.class.getName());

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1EventSpawn();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1EventSpawn() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String desc = tok.nextToken();
			String time1 = tok.nextToken();
			int npcId = 0;
			try {
				npcId = Integer.parseInt(desc);
			} catch (NumberFormatException e) {
				npcId = NpcTable.getInstance().findNpcIdByNameWithoutSpace(desc);
				if (npcId == 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("해당 NPC가 발견되지 않습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(398), true), true);
					return false;
				}
			}
			int time = Integer.parseInt(time1);

			desc = NpcTable.getInstance().getTemplate(npcId).getDesc();
			Eventspawn(pc, npcId, 60000 * time);

//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("(" + desc + ") (ID:" + npcId + ") (" + time + ")분 소환 "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage("(" + desc  + ") (ID:" + npcId  + ") summoned for (" + time  + " minutes"), true);
//AUTO SRM: 			L1World.getInstance().broadcastServerMessage("(" + desc + ")  (" + time + ")분 동안 소환됩니다. ", true); // CHECKED OK
			L1World.getInstance().broadcastServerMessage("(" + desc  + ")  (" + time  + S_SystemMessage.getRefText(401), true);
			tok = null;
			desc = null;
			return true;
		} catch (Exception e) {
		//	_log.log(Level.SEVERE, "", e);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + "[NPCID] [시간(분)] "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(400), true), true);
			return false;
		}
	}

	private void Eventspawn(L1PcInstance pc, int npcId, int timeMinToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());

			npc.getLocation().set(pc.getLocation());
			npc.getLocation().forward(pc.getMoveState().getHeading());

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(pc.getMoveState().getHeading());

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMinToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMinToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}



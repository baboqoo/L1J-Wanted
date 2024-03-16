package l1j.server.server.command.executor;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.L1SpawnUtil;

public class L1SpawnCmd implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1SpawnCmd.class.getName());

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1SpawnCmd();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1SpawnCmd() {}

	private void sendErrorMessage(L1PcInstance pc, String cmdName) {
		//String errorMsg = cmdName + " npcid|name [수] [범위] 라고 입력해 주세요.";
		String errorMsg = cmdName + " Please enter npcid|name [number] [range].";
		pc.sendPackets(new S_SystemMessage(errorMsg), true);
	}

	private int parseNpcId(String nameId) {
		int npcid = 0;
		try {
			npcid = Integer.parseInt(nameId);
		} catch (NumberFormatException e) {
			npcid = NpcTable.getInstance().findNpcIdByNameWithoutSpace(nameId);
		}
		return npcid;
	}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null || pc.getNetConnection() == null) {
				return false;
			}
			StringTokenizer tok = new StringTokenizer(arg);
			String desc = tok.nextToken();
			int count = 1;
			if (tok.hasMoreTokens()) {
				count = Integer.parseInt(tok.nextToken());
			}
			int randomrange = 0;
			if (tok.hasMoreTokens()) {
				randomrange = Integer.parseInt(tok.nextToken(), 10);
			}
			int npcid = parseNpcId(desc);
			L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
			if (npc == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("해당 NPC가 발견되지 않습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(678), true), true);
				return false;
			}
			for (int i = 0; i < count; i++) {
				if (npcid == 800157) {
					reper(pc);// 지배의 탑 사신 그림 리퍼
				} else {
					L1SpawnUtil.spawn(pc, npcid, randomrange, 0);
				}
			}
			//String msg = String.format("%s(%d) (%d) 를 소환했습니다. (범위:%d)", npc.getDesc(), npcid, count, randomrange);
			String msg = String.format("%s(%d) (%d) has been summoned. (Range:%d)", npc.getDesc(), npcid, count, randomrange);			
			pc.sendPackets(new S_SystemMessage(msg, true), true);
			return true;
		} catch (NoSuchElementException e) {
			sendErrorMessage(pc, cmdName);
			return false;
		} catch (NumberFormatException e) {
			sendErrorMessage(pc, cmdName);
			return false;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 내부 에러입니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(168), true), true);
			return false;
		}
	}
	
	private void reper(L1PcInstance pc){
		L1SpawnUtil.spawn2(pc.getX(), pc.getY(), (short) pc.getMapId(), 5, 800164, 0, 10000, 0);
		GeneralThreadPool.getInstance().schedule(new Riper(pc.getX(), pc.getY(), pc.getMapId()), 10000L);
	}
	
	private class Riper implements Runnable {
		int _x = 0, _y = 0;
		short _map = 0;
		Riper(int x, int y, short map){
			_x = x;
			_y = y;
			_map = map;
		}
		@Override
		public void run(){
			try {
				L1SpawnUtil.spawn2(_x, _y, (short) _map, 5, 800157, 0, 0, 0);
			} catch(Exception e){}
		}
	}
	
}


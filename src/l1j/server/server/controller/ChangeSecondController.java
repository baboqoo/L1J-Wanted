package l1j.server.server.controller;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class ChangeSecondController implements l1j.server.server.model.gametime.TimeListener {// 1초마다 호출되는 액션
	//private l1j.server.server.controller.action.ControllerInterface arrow 	= l1j.server.server.controller.action.ArrowTrap.getInstance();
	//private l1j.server.server.controller.action.ControllerInterface timeMap	= l1j.server.server.controller.action.TimeMap.getInstance();
	private l1j.server.server.controller.action.ControllerInterface war			= l1j.server.server.controller.action.War.getInstance();
	private l1j.server.server.controller.action.ControllerInterface npcShop		= l1j.server.server.controller.action.NpcShop.getInstance();
	private l1j.server.server.controller.action.ControllerInterface boss		= l1j.server.server.controller.action.BossSpawn.getInstance();
	private l1j.server.server.controller.action.ControllerInterface clanBuff	= l1j.server.server.controller.action.PledgeBuff.getInstance();
	private l1j.server.server.controller.action.ControllerInterface fish		= l1j.server.server.controller.action.Fishing.getInstance();
	private l1j.server.server.controller.action.ControllerInterface dungeonTime	= l1j.server.server.controller.action.DungeonStay.getInstance();
	private l1j.server.server.controller.action.ControllerInterface aura		= l1j.server.server.controller.action.AuraBuff.getInstance();
	//private l1j.server.server.controller.action.ControllerInterface ment		= l1j.server.server.controller.action.AutoClanJoinMent.getInstance();
	private l1j.server.server.controller.action.ControllerInterface access		= l1j.server.server.controller.action.EntranceQueue.getInstance();
	
	
	private static class newInstance {
		public static final ChangeSecondController INSTANCE = new ChangeSecondController();
	}
	public static ChangeSecondController getInstance() {
		return newInstance.INSTANCE;
	}
	
	public void start(){
		l1j.server.server.model.gametime.RealTimeClock.getInstance().addListener(newInstance.INSTANCE, java.util.Calendar.SECOND);
	}

	@Override
	public void onMonthChanged(l1j.server.server.model.gametime.BaseTime time) {}

	@Override
	public void onDayChanged(l1j.server.server.model.gametime.BaseTime time) {}

	@Override
	public void onHourChanged(l1j.server.server.model.gametime.BaseTime time) {}

	@Override
	public void onMinuteChanged(l1j.server.server.model.gametime.BaseTime time) {}

	@Override
	public void onSecondChanged(l1j.server.server.model.gametime.BaseTime time) {
		try {
			int second = time.get(java.util.Calendar.SECOND);
			pcAction();
			//arrow.execute();
			//timeMap.execute();
			war.execute();
			npcShop.execute();
			clanBuff.execute();
			if (second % 10 == 0) {// 10초마다 체크
				boss.execute();
				if (Config.SERVER.ACCESS_STANBY) {
					access.execute();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void pcAction(){
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null) {
				continue;
			}
			fish.execute(pc);
			dungeonTime.execute(pc);
			aura.execute(pc);
			//ment.execute(pc);
		}
	}
}


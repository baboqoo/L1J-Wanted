package l1j.server.IndunSystem.treasureisland;

import java.util.ArrayList;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 만월의 보물섬 타이머
 * @author LinOffice
 */
public class TreasureIslandTimer implements Runnable {
	private final short _mapId;
	private boolean _active = true;
	
	public TreasureIslandTimer(short mapId) {
		_mapId = mapId;
	}
	
	@Override
	public void run() {
		try {
			if (!_active) {
				return;
			}
			ArrayList<L1PcInstance> players = L1World.getInstance().getMapPlayer(_mapId);
			for (L1PcInstance pc : players) {
				if (pc == null) {
					continue;
				}
				TreasureIslandUtil.endReward(pc);
				pc.sendPackets(TreasureIslandUtil.TREASURE_STOP);// 만월의 보물섬 종료. 보물을 발굴하고 섬에 체류한 참여자에게 보상을 지급했습니다.
			}
			Thread.sleep(4000L);
			for (int i=0; i<TreasureIslandUtil.TREASURE_STOP_MENTS.length; i++) {
				for (L1PcInstance pc : players) {
					if (pc == null) {
						continue;
					}
					pc.sendPackets(TreasureIslandUtil.TREASURE_STOP_MENTS[i]);
				}
				Thread.sleep(1000L);
			}
			TreasureIsland.getInstance().end();
			players.clear();
			players = null;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cancel(){
		_active = false;
	}
}


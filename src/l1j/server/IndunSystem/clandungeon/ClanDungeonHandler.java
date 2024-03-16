package l1j.server.IndunSystem.clandungeon;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 혈맹 던전 시스템 핸들러
 * @author LinOffice
 */
public abstract class ClanDungeonHandler implements Runnable {
	private static Logger _log = Logger.getLogger(ClanDungeonHandler.class.getName());
	
	protected final short _map;
	protected final String _clanName;
	protected final ClanDungeonType _type;
	protected final ClanDungeonTable _clantable;
	
	protected int stageCount = 0;
	
	protected boolean running = false;
	public void setRun(boolean flag){running = flag;}
	public boolean isRun(){return running;}
	public String getClanName(){return _clanName;}
	
	protected ClanDungeonHandler(short mapId, String clanName, ClanDungeonType type){
		_map		= mapId;
		_clanName	= clanName;
		_type		= type;
		_clantable	= ClanDungeonTable.getInstance();
	}
	
	@Override
	public void run() {
		startSpawn();
		stageCount = 0;
		while(running){
			try {
				timerCheck();
				process();
			} catch(Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}
		endRaid();
	}
	
	protected abstract void startSetting(L1PcInstance pc);
	protected abstract void startSpawn();
	protected abstract void timerCheck() throws Exception;
	protected abstract void process() throws Exception;
	public abstract void startRaid();
	protected abstract void endRaid();
	
	protected void dispose(){
		ClanDungeonUtill.deleteObject(_map);// 맵에 존재하는 오브젝트 삭제
		ClanDungeonCreator.getInstance().removeRaid(_map);
	}
	
	protected void RETURN_TEL(){
		for (L1PcInstance pc : ClanDungeonUtill.PcStageCK(_map)) {
			int[] loc = pc.getClan().getCastleId() != 0 ? L1CastleLocation.getCastleLoc(pc.getClan().getCastleId()) : L1HouseLocation.getBasementLoc(pc.getClan().getHouseId());
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		}
		running = false;
	}
}


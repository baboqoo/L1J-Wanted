package l1j.server.IndunSystem.dragonraid;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;

/**
 * 드래곤 레이드 핸들러
 * @author LinOffice
 */
public abstract class DragonRaidHandler implements Runnable {
	private static Logger _log = Logger.getLogger(DragonRaidHandler.class.getName());

	protected final int _map;
	protected final DragonRaildType _raidType;
	protected final ArrayList<L1PcInstance> _pcList;
	protected final DragonRaidUtil _raidUtil;
	protected DragonRaidStage _stage;
	
	protected DragonRaidHandler(int mapId, DragonRaildType raidType){
		_map		= mapId;
		_raidType	= raidType;
		_pcList		= new ArrayList<L1PcInstance>();
		_raidUtil	= DragonRaidUtil.getInstance();
		_stage		= DragonRaidStage.READY;
	}
	
	private int limitTime = 3600;
	protected static final int SLEEP = 20;
	
	private boolean running;
	public void setRun(boolean flag){
		running = flag;
	}
	public boolean isRun(){
		return running;
	}
	
	private boolean _wake;
	public boolean isWake() {
		return _wake;
	}
	public void setWake(boolean flag) {
		_wake = flag;
	}
	
	public int countLairUser() {
		return _pcList.size();
	}
	public void addLairUser(L1PcInstance pc) {
		if (_pcList.contains(pc)) {
			return;
		}
		_pcList.add(pc);
	}
	public void removeLairUser(L1PcInstance pc) {
		if (!_pcList.contains(pc)) {
			return;
		}
		_pcList.remove(pc);
	}
	public void clearLairUser() {
		_pcList.clear();
	}
	
	@Override
	public void run() {
		try {
			while(running){
				try {
					timeCheck();
					if (_raidType == DragonRaildType.HALPAS) {
						userCheck();
					}
					switch(_stage){
					case READY:		ready();break;
					case START:		start();break;
					case RESULT:	result();break;
					case END:		end();break;
					case FAIL:		fail();break;
					case CHECK:		check();break;
					default:break;
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
			raidClose();
			_raidUtil.teleporterDelete(_raidType);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void ready() throws Exception;
	protected abstract void start() throws Exception;
	protected abstract void result() throws Exception;
	protected abstract void end() throws Exception;
	protected abstract void fail() throws Exception;
	protected abstract void check() throws Exception;
	protected abstract void createSpwan(L1PcInstance pc);
	public abstract void raidStart();
	protected abstract void raidClose();
	
	protected void dispose(){
		clearLairUser();
		_raidUtil.objectDelete(_map);
		DragonRaidCreator.getInstance().removeRaid(_map);
	}
	
	private void timeCheck(){
		if (limitTime > 0) {
			limitTime--;
		}
		if (limitTime == 600) {
			_raidUtil.mapTimerEndMsgSend(_pcList, 0);
			_raidUtil.teleporterDelete(_raidType);
		} else if (limitTime == 180) {
			_raidUtil.mapTimerEndMsgSend(_pcList, 1);
		} else if (limitTime == 60) {
			_raidUtil.mapTimerEndMsgSend(_pcList, 2);
		} else if (limitTime == 10) {
			_raidUtil.mapTimerEndMsgSend(_pcList, 3);
		} else if (limitTime == 5) {
			_raidUtil.mapTimerEndMsgSend(_pcList, 4);
		} else if (limitTime <= 0) {
			closeTeleport();
		}
	}
	
	protected void closeTeleport(){
		running = false;
		int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_WERLDAN);
		L1PcInstance pc;
		for (int i=0; i<_pcList.size(); i++) {
			pc = _pcList.get(i);
			if (pc == null) {
				continue;
			}
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		}
	}
	
	protected void raidCompleteBuff(){
		ArrayList<L1PcInstance> pcList = _pcList;
		if (pcList == null || pcList.isEmpty()) {
			return;
		}
		int raidTime = 86400 * Config.ETC.DRAGON_RAID_LIMIT_TIME;
		long deleteTime = System.currentTimeMillis() + (raidTime * 1000);// 3일
		Account account = null;
		S_PacketBox buffIcon = new S_PacketBox(S_PacketBox.DRAGON_RAID_BUFF, raidTime);
		L1PcInstance pc;
		for (int i=0; i<pcList.size(); i++) {
			pc = pcList.get(i);
			if (pc == null) {
				continue;
			}
			pc.send_effect(7783);
			pc.getSkill().setSkillEffect(L1SkillId.DRAGONRAID_BUFF, raidTime * 1000);
			pc.sendPackets(buffIcon);
			account = pc.getAccount();
			if (account == null) {
				System.out.println(String.format("[DragonRaidHandler] raidCompleteBuff method account empty [%s]", pc.getName()));
				continue;
			}
			if (account.getDragonRaid() != null) {
				account.getDragonRaid().setTime(deleteTime);
			} else {
				account.setDragonRaid(new Timestamp(deleteTime));
			}
		}
		buffIcon.clear();
		buffIcon = null;
		_raidUtil.raidBuffTimeUpdate(pcList);
	}
	
	private void userCheck(){
		if (_pcList == null || _pcList.isEmpty()) {
			return;
		}
		L1PcInstance pc;
		for (int i=0; i<_pcList.size(); i++) {
			pc = _pcList.get(i);
			if (pc == null) {
				continue;
			}
			if (pc.getMapId() != _map) {
				removeLairUser(pc);
			}
		}
	}
}


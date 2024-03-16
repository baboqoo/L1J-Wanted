package l1j.server.LFCSystem.LFC.Template;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.LFCSystem.InstanceObject;
import l1j.server.LFCSystem.InstanceSpace;
import l1j.server.LFCSystem.InstanceType;
import l1j.server.LFCSystem.InstanceEnums.InstSpcMessages;
import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.LFCSystem.InstanceEnums.LFCMessages;
import l1j.server.LFCSystem.LFC.LFCType;
import l1j.server.LFCSystem.LFC.Creator.LFCCreator;
import l1j.server.LFCSystem.Loader.InstanceLoadManager;
import l1j.server.LFCSystem.Util.LFCTrapThorn;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerFromLfcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.CommonUtil;

public class LFCObject extends InstanceObject{
	public static final boolean 	isTest 	= false;
	
	protected static Logger 		_log 	= Logger.getLogger(LFCObject.class.getName());
	protected static final Object 	_lock 	= new Object();
	public static LFCObject createInstance(){
		return new LFCObject();
	}
	
	protected ArrayList<L1PcInstance> 	_red;
	protected ArrayList<L1PcInstance> 	_blue;
	protected int						_curMapId;
	protected boolean					_isrun;
	protected int						_redPoint;
	protected int						_bluePoint;
	protected int						_time;
	protected LFCType					_lType;
	
	@Override
	public int getMarkStatus(L1PcInstance pc){
		if (_red != null){
			if (_red.contains(pc))
				return 8;
		}
		return 9;
	}
	
	@Override
	public void dispose(){
		if (_red != null){
			_red.clear();
			_red = null;
		}
		if (_blue != null){
			_blue.clear();
			_blue = null;
		}
	}
	
	public LFCObject(){
		super();
		_isrun		= false;
		_redPoint	= 0;
		_bluePoint 	= 0;
	}
	
	public void setRedTeam(ArrayList<L1PcInstance> red){
		_red = red;
	}
	public void setBlueTeam(ArrayList<L1PcInstance> blue){
		_blue = blue;
	}
	@Override
	public void setType(InstanceType type){
		_type = type;
		if (type instanceof LFCType)
			_lType = (LFCType)type;
	}
	
	@Override
	public void init(){
		_time			= _lType.getPlaySecond();
		int size		= _red.size();
		L1PcInstance pc = null;
		
		for (int i = 0; i < size; i++){
			pc = _red.get(i);
			if (pc == null)
				continue;
			
			InstanceSpace.teleportInstance(pc, (short)getCopyMapId(), _lType.getRedXStartup(), _lType.getRedYStartup());
			pc.setDamageFromLfc(0);
		}
		
		size			= _blue.size();
		for (int i = 0; i < size; i++){
			pc = _blue.get(i);
			if (pc == null)
				continue;
			
			InstanceSpace.teleportInstance(pc, (short)getCopyMapId(), _lType.getBlueXStartup(), _lType.getBlueYStartup());
			pc.setDamageFromLfc(0);			
		}
		
		_isrun = true;
	}
	
	@Override
	public void run(){
	}
	
	@Override
	public void closeForGM(){
		if (_red == null || _blue == null){
			_isrun = false;
			InstanceSpace.getInstance().releaseInstance(this);
			return;
		}
		
		LFCMessages.INGAME_CLOSE_FORGM.sendGreenMsgToList(_red);
		LFCMessages.INGAME_CLOSE_FORGM.sendGreenMsgToList(_blue);
		try {
			Thread.sleep(3000);
		} catch(Exception e){
			
		}
		getBack();
		_isrun = false;
		InstanceSpace.getInstance().releaseInstance(this);
	}
	
	@Override
	public void close(){
		if (_red == null || _blue == null){
			_isrun = false;
			InstanceSpace.getInstance().releaseInstance(this);
			return;
		}
		
		int redKill 		= 0;
		int redDmg 			= 0;
		int blueKill		= 0;
		int blueDmg 		= 0;
		ArrayList<L1PcInstance> winners = _red;
		ArrayList<L1PcInstance> losers	= _blue;
		
		int size 			= _red.size();
		L1PcInstance pc 	= null;
		for (int i = 0; i < size; i++){
			pc = _red.get(i);
			if (pc == null)
				continue;
			
			if (pc.isDead())
				blueKill++;
			
			blueDmg += pc.getDamageFromLfc();
		}
		LFCMessages.INGAME_CLOSE.sendGreenMsgToList(_red);
		
		size				= _blue.size();
		for (int i = 0; i < size; i++){
			pc = _blue.get(i);
			if (pc == null)
				continue;
			
			if (pc.isDead())
				redKill++;
			redDmg += pc.getDamageFromLfc();
		}
		LFCMessages.INGAME_CLOSE.sendGreenMsgToList(_blue);
		
		if (redKill > blueKill){
			winners = _red;
			losers	= _blue;
		} else if (redKill < blueKill){
			winners = _blue;
			losers	= _red;
		} else if (redDmg > blueDmg){
			winners = _red;
			losers	= _blue;
		} else if (redDmg < blueDmg){
			winners = _blue;
			losers	= _red;
		}

		try {
			Thread.sleep(3000);
		} catch(Exception e){
			
		}
		
		compensate(winners, losers);
		getBack();
		_isrun = false;
		InstanceSpace.getInstance().releaseInstance(this);
	}
	
	@Override
	public void notifySizeOver(){
		if (_lType.isPvp()){
			InstSpcMessages.INSTANCE_SPACE_FULL.sendSystemMsg(_red.get(0));
			InstSpcMessages.INSTANCE_SPACE_FULL.sendSystemMsg(_blue.get(0));
		} else {
			InstSpcMessages.INSTANCE_SPACE_FULL.sendSystemMsg(_red.get(0).getParty().getLeader());
			InstSpcMessages.INSTANCE_SPACE_FULL.sendSystemMsg(_blue.get(0).getParty().getLeader());
		}
	}
	
	protected void sendInPlayerMessage(String msg){
		S_PacketBox box	= new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg);
		this.broadcastPacket(box);
		box.clear();
	}

	protected void waitCount() throws Exception{
		try {
			//String sec = new StringBuilder().append(_lType.getReadySecond()).append("초 후 경기가 시작됩니다.").toString(); // CHECKED OK
			String sec = String.format(S_SystemMessage.getRefText(1360) + "%d " + S_SystemMessage.getRefText(1361), _lType.getReadySecond());
			LFCMessages.INGAME_NOTIFY_READY.sendGreenMsgToList(_red, sec);
			LFCMessages.INGAME_NOTIFY_READY.sendGreenMsgToList(_blue, sec);
		
			for (int i = _lType.getReadySecond(); i > 0 ; i--){
				if (i <= 10){
					//StringBuilder msg = new StringBuilder();
					//msg.append(i).append("초 후 경기가 시작됩니다."); // CHECKED OK
					String msg = String.format(S_SystemMessage.getRefText(1360) + "%d " + S_SystemMessage.getRefText(1361), i);
					//LFCMessages.INGAME_NOTIFY_READY.sendGreenMsgToList(_red, msg.toString());
					//LFCMessages.INGAME_NOTIFY_READY.sendGreenMsgToList(_blue, msg.toString());
					LFCMessages.INGAME_NOTIFY_READY.sendGreenMsgToList(_red, msg);
					LFCMessages.INGAME_NOTIFY_READY.sendGreenMsgToList(_blue, msg);
				}
				Thread.sleep(1000);
			}
			S_PacketBox mTimer = new S_PacketBox(S_PacketBox.TIME_COUNT, _time);
			this.broadcastPacket(mTimer);
			mTimer.clear();
			LFCMessages.INGAME_NOTIFY_START.sendGreenMsgToList(_red);
			LFCMessages.INGAME_NOTIFY_START.sendGreenMsgToList(_blue);

		} catch(Exception e){
			throw e;
		}
	}
	
	protected boolean checkSecond(){
		if (_time > 0){
			_time--;
			
			if (_time <= 10 && _time != 0){
				//String sec = String.format("%d초 후 경기가 종료됩니다.", _time); // CHECKED OK
				String sec = String.format(S_SystemMessage.getRefText(1362) + "%d " + S_SystemMessage.getRefText(1363), _time);
				LFCMessages.INGAME_NOTIFY_CLOSETIME.sendGreenMsgToList(_red, sec);
				LFCMessages.INGAME_NOTIFY_CLOSETIME.sendGreenMsgToList(_blue, sec);
			}
			
			/* 버프 스폰 */
			if (_lType.getBuffSpawnSecond() != 0 && _time % _lType.getBuffSpawnSecond() == 0)
				spawnBuff(2);
		}
		
		if (_time <= 0)
			return false;
		
		return true;
	}
	
	protected void spawnBuff(int num){
		L1NpcInstance buff 		= null;
		ArrayList<Integer> arr 	= InstanceLoadManager.MIS_SP_BUFF_IDS;
		int rNum;
		try {
			for (int i = 0; i < num; i++){
				rNum = getRandomNum();
				buff = NpcTable.getInstance().newNpcInstance(arr.get(rNum % arr.size()));
				buff.setId(IdFactory.getInstance().nextId());
				buff.setMap((short)getCopyMapId());
				buff.getLocation().forward(0);
				buff.setX(_lType.getMapRect().getRandomX());
				buff.setY(_lType.getMapRect().getRandomY());
				buff.setHomeX(buff.getX());
				buff.setHomeY(buff.getY());
				buff.getMoveState().setHeading(buff.getMoveState().getHeading());
				L1World.getInstance().storeObject(buff);
				L1World.getInstance().addVisibleObject(buff);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, "spawnBuff", e);
			e.printStackTrace();
		}
	}
	
	protected L1NpcInstance spawnBoundary(int x, int y, int heading){
		L1NpcInstance boundary = spawnNpc(InstanceLoadManager.MIS_SP_BOUNDARY_ID, x, y, heading);
		if (boundary == null) _log.log(Level.SEVERE, "spawnBoundary");
		return boundary;
	}
	
	protected L1NpcInstance spawnBox(int x, int y){
		return spawnBox(x, y, 0);
	}
	
	protected L1NpcInstance spawnBox(int x, int y, int heading){
		L1NpcInstance box = spawnNpc(InstanceLoadManager.MIS_SP_BOX_ID, x, y, heading);
		if (box == null) _log.log(Level.SEVERE, "spawnBox");
		return box;		
	}
	
	protected LFCTrapThorn spawnThorn(int x, int y){
		return new LFCTrapThorn(getCopyMapId(), x, y);
	}
	
	protected L1NpcInstance spawnNpc(int npcId, int x, int y, int heading){
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap((short)getCopyMapId());
			npc.getLocation().forward(0);
			npc.setX(x);
			npc.setY(y);
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(heading);
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);	
		} catch (Exception e) {
			e.printStackTrace();
			npc = null;
		}
		return npc;
	}
	
	protected L1TowerFromLfcInstance spawnTower(int x, int y){
		L1NpcInstance tower 		= null;
		try {
			tower = NpcTable.getInstance().newNpcInstance(InstanceLoadManager.MIS_SP_TOWER_ID);
			tower.setId(IdFactory.getInstance().nextId());
			tower.setMap((short)getCopyMapId());
			tower.getLocation().forward(0);
			tower.setX(x);
			tower.setY(y);
			tower.setHomeX(tower.getX());
			tower.setHomeY(tower.getY());
			tower.getMoveState().setHeading(tower.getMoveState().getHeading());
			((L1TowerFromLfcInstance)tower).init();
			L1World.getInstance().storeObject(tower);
			L1World.getInstance().addVisibleObject(tower);	
		} catch (Exception e) {
			_log.log(Level.SEVERE, "spawnTower", e);
			e.printStackTrace();
			tower = null;
		}
		return (L1TowerFromLfcInstance)tower;
	}
	
	protected void deleteNpc(L1NpcInstance npc){
		npc.deleteMe();
	}
	
	protected void deleteTower(L1TowerFromLfcInstance tower){
		tower.deleteMe();
	}
	
	protected void deleteThurn(LFCTrapThorn thorn){
		thorn.delete();
	}
	
	protected boolean getRandomBoolean(){
		if (CommonUtil.random(100) + 1 > 50)
			return true;
		return false;
	}
	
	protected int getRandomNum(){
		return CommonUtil.random(100) + 1;
	}
	
	protected void broadcastPacket(ServerBasePacket pck){
		L1PcInstance pc = null;
		int size		= _red.size();
		for (int i = 0; i < size; i++){
			pc = _red.get(i);
			if (pc != null)	
				pc.sendPackets(pck, false);
		}
		
		size			= _blue.size();
		for (int i = 0; i < size; i++){
			pc = _blue.get(i);
			if (pc != null)
				pc.sendPackets(pck, false);
		}
	}
	
	protected void compensate(ArrayList<L1PcInstance> winners, ArrayList<L1PcInstance> losers){
		int rnum 		= getRandomNum();
		int size 		= winners.size();
		L1PcInstance pc = null;
		for (int i = 0; i < size; i++){
			pc	= winners.get(i);
			if (pc == null)
				continue;
			
			_lType.winnerCompensate(pc);
		}
		LFCMessages.INGAME_NOTIFY_WINNER.sendGreenMsgToList(winners);
		size			= losers.size();
		for (int i = 0; i < size; i++){
			pc	= losers.get(i);
			if (pc == null)
				continue;
			
			_lType.loserCompensate(pc);
		}
		LFCMessages.INGAME_NOTIFY_LOSER.sendGreenMsgToList(losers);

		ArrayList<L1PcInstance> randTeam;
		if (rnum > _lType.getRandomCompensateRatio())
			randTeam = losers;
		else
			randTeam = winners;	
		
		size = randTeam.size();
		for (int i = 0; i < size; i++){
			pc = randTeam.get(i);
			if (pc == null)
				continue;
			
			_lType.randomCompensate(pc);
		}
		
//		LFCMessages.INGAME_NOTIFY_LOTTO.sendGreenMsgToList(randTeam);
	}
	
	protected void getBack(){
		L1PcInstance pc = null;
		int size		= _red.size();
		for (int i = 0; i < size; i++){
			pc = _red.get(i);
			if (pc == null) continue;
			
			LFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_NONE);
			InstanceSpace.teleportInstance(
					pc, 
					(short)InstanceLoadManager.MIS_ERRBACK_MAPID, 
					InstanceLoadManager.MIS_ERRBACK_X, 
					InstanceLoadManager.MIS_ERRBACK_Y);
		}
		
		size = _blue.size();
		for (int i = 0; i < size; i++){
			pc = _blue.get(i);
			if (pc == null) continue;
			LFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_NONE);
			InstanceSpace.teleportInstance(
					pc, 
					(short)InstanceLoadManager.MIS_ERRBACK_MAPID, 
					InstanceLoadManager.MIS_ERRBACK_X, 
					InstanceLoadManager.MIS_ERRBACK_Y);
		}
	}
	
	public String getName(){
		return "LFCObject";
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(128);
		sb.append(super.toString());
		sb.append("name : ").append(getName()).append("\n");
		sb.append("total users : ").append(_red.size() + _blue.size()).append("\n");
		sb.append("red users : ").append(_red.size()).append("\n");
		sb.append("blue users : ").append(_blue.size()).append("\n");
		return sb.toString();
	}
}


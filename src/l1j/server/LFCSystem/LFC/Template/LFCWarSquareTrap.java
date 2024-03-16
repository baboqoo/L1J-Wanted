package l1j.server.LFCSystem.LFC.Template;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.logging.Level;

import l1j.server.LFCSystem.InstanceSpace;
import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.LFCSystem.InstanceEnums.LFCMessages;
import l1j.server.LFCSystem.LFC.Creator.LFCCreator;
import l1j.server.LFCSystem.Loader.InstanceLoadManager;
import l1j.server.LFCSystem.Util.LFCTrapThorn;
import l1j.server.LFCSystem.Util.Rectangle;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_LFCTrapEffect;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.CommonUtil;

public class LFCWarSquareTrap extends LFCObject{
	private static final int					MAX_PHASE 	= 5;
	private static final ServerBasePacket		s_chaos		= new S_DisplayEffect(S_DisplayEffect.RINDVIOR_LIGHT_DISPLAY);
	
	public static LFCWarSquareTrap createInstance(){
		return new LFCWarSquareTrap();
	}
	
	private ArrayList<L1NpcInstance> 			_box;
	private ArrayList<L1NpcInstance> 			_boundary;
	private ArrayList<L1PcInstance> 			_winners;
	private ArrayList<L1PcInstance>				_losers;
	private int									_curPhase;
	private Rectangle							_curRt;
	private L1NpcInstance						_rndTrap;
	private int									_rndTrapCount;
	private ArrayDeque<LFCTrapThorn>			_thorn_Q;
	
	public LFCWarSquareTrap() {
		super();
		_box 				= new ArrayList<L1NpcInstance>();
		_boundary			= new ArrayList<L1NpcInstance>();
		_winners 			= null;
		_losers				= null;
		_curPhase 			= 0;
		_curRt				= new Rectangle(32726, 32856, 32743, 32873);
		_thorn_Q			= new ArrayDeque<LFCTrapThorn>(48);		
	}
	
	@Override
	public void init(){
		L1NpcInstance boundary;
		for (int i = 32726; i <= 32743; i++){
			boundary = spawnBoundary(i, 32865, 2);
			if(boundary != null)
				_boundary.add(boundary);
		}
		super.init();
	}
	
	private void spawnBoxTrap(){
		if(_curPhase >= MAX_PHASE)
			return;
		
		GeneralThreadPool.getInstance().execute(new TrapSpwanThread());
	}
	
	public class TrapSpwanThread implements Runnable{
		@Override
		public void run() {
			LFCTrapThorn thorn;
			while(!_thorn_Q.isEmpty()){
				thorn = _thorn_Q.poll();
				broadcastPacket(new S_LFCTrapEffect(thorn.getthornNpc().getId(), 6132));
				thorn.delete();
			}
			try {
				Thread.sleep(50);
			} catch(Exception e){
				e.printStackTrace();
			}
			int length 	= _curRt.getRight() - _curRt.getLeft();
			for (int i = 0; i < length; i++){
				_box.add(spawnBox(_curRt.getLeft() + i, _curRt.getTop()));
				_box.add(spawnBox(_curRt.getRight(), _curRt.getTop() + i));
				_box.add(spawnBox(_curRt.getRight() - i, _curRt.getBottom()));
				_box.add(spawnBox(_curRt.getLeft(), _curRt.getBottom() - i));
				
				if (i == 0 || i == length - 1)
					continue;
				
				_thorn_Q.offer(spawnThorn(_curRt.getLeft() + i, _curRt.getTop() + 1));
				_thorn_Q.offer(spawnThorn(_curRt.getRight() - 1, _curRt.getTop() + i));
				_thorn_Q.offer(spawnThorn(_curRt.getRight() - i, _curRt.getBottom() -1));
				_thorn_Q.offer(spawnThorn(_curRt.getLeft() + 1, _curRt.getBottom() - i));
				
				try {
					Thread.sleep(30);
				} catch(Exception e){
					e.printStackTrace();
				}		
			}
			_curRt.reduce();
			_curPhase++;
		}
	}
	
	private boolean isAllDead(ArrayList<L1PcInstance> arrs){
		for (L1PcInstance pc : arrs){
			if (!pc.isDead())
				return false;
		}
		return true;
	}

	private void resetRndTrap(){
		if (_rndTrap != null){
			if (_rndTrapCount++ >= 3){
				_rndTrap.deleteMe();
				_rndTrap = null;
				_rndTrapCount = 0;
			}
		}
	}
	
	private void setRndTrap(){
		if (_rndTrap == null && _time % InstanceLoadManager.MIS_SP_RTRAP_TIME == 0 && CommonUtil.random(100) + 1 < InstanceLoadManager.MIS_SP_RTRAP_RAITO){
			//int x = _lType.getMapRect().getRandomX();
			//int y = _lType.getMapRect().getRandomY();
			if (CommonUtil.random(100) + 1 < InstanceLoadManager.MIS_SP_RCTRAP_RATIO)
				broadcastPacket(s_chaos);
			_rndTrapCount = 0;
		}
	}
	
	private void posToDamage(){
		L1PcInstance attacker = null;
		int dmg = (CommonUtil.random(InstanceLoadManager.MIS_EFF_THORNDMG_MAX - InstanceLoadManager.MIS_EFF_THORNDMG_MIN)) + InstanceLoadManager.MIS_EFF_THORNDMG_MIN;
		for (L1PcInstance pc : _red){
			if (pc.isDead() || pc.getCurrentHp() <= 0)
				continue;

			if (pc.getX() == _curRt.getLeft() || pc.getX() == _curRt.getRight() || pc.getY() == _curRt.getTop() || pc.getY() == _curRt.getBottom()){
				for (L1PcInstance dmy : _blue){
					if (dmy != null){
						attacker = dmy;
						break;
					}
				}
				pc.receiveDamage(attacker, dmg);				
			}
		}
		for (L1PcInstance pc : _blue){
			if (pc.isDead() || pc.getCurrentHp() <= 0)
				continue;

			if (pc.getX() == _curRt.getLeft() || pc.getX() == _curRt.getRight() || pc.getY() == _curRt.getTop() || pc.getY() == _curRt.getBottom()){
				for (L1PcInstance dmy : _red){
					if (dmy != null){
						attacker = dmy;
						break;
					}
				}
				pc.receiveDamage(attacker, dmg);				
			}
		}
	}
	
	@Override
	public void run(){
		try {
			waitCount();
			for (L1NpcInstance boundary : _boundary)
				deleteNpc(boundary);
			_boundary.clear();
			_boundary = null;
			
			for (L1PcInstance pc : _red)
				LFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_LFC);
			for (L1PcInstance pc : _blue)
				LFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_LFC);
			
			boolean isRedAllDead 	= false;
			boolean isBlueAllDead 	= false;
			long	msTime			= 0;
			while(_isrun){
				if (msTime < 1000)	Thread.sleep(1000 - msTime);
				msTime = System.currentTimeMillis();
				resetRndTrap();
				posToDamage();
				isRedAllDead 	= isAllDead(_red);
				isBlueAllDead 	= isAllDead(_blue);				
				if (!isRedAllDead && !isBlueAllDead){
					if (!checkSecond()){
						close();
						return;
					}
					if (_time % InstanceLoadManager.MIS_SP_THORN_TIME == 0)
						spawnBoxTrap();
					setRndTrap();
					msTime = System.currentTimeMillis() - msTime;
					continue;
				} else if (isRedAllDead && isBlueAllDead){
					if (getRandomBoolean()){
						_winners = _red;
						_losers = _blue;
					} else {
						_winners = _blue;
						_losers	= _red;
					}
				} else if (isRedAllDead){
					_winners = _blue;
					_losers = _red;
				} else if (isBlueAllDead){
					_winners = _red;
					_losers = _blue;
				}
				close();
			}
		} catch(Exception e){
			close();
			e.printStackTrace();
			_log.log(Level.SEVERE, getName(), e);
		}
	}
	
	@Override
	public void closeForGM(){
		releaseResource();
		super.closeForGM();
	}
	
	@Override
	public void close(){
		if (_winners == null && _losers == null){
			super.close();
			releaseResource();
			return;
		}
		
		LFCMessages.INGAME_CLOSE.sendGreenMsgToList(_red);
		LFCMessages.INGAME_CLOSE.sendGreenMsgToList(_blue);
		try {
			Thread.sleep(3000);
		} catch(Exception e){
			
		}
		
		compensate(_winners, _losers);		
		getBack();
		_isrun = false;
		releaseResource();
		InstanceSpace.getInstance().releaseInstance(this);
	}
	
	private void releaseResource(){
		if (_box != null){
			for (L1NpcInstance box : _box)
				box.deleteMe();
			_box 		= null;
		}
		
		if (_boundary != null){
			for (L1NpcInstance boundary : _boundary)
				boundary.deleteMe();
			_boundary 	= null;
		}
		
		if (_thorn_Q != null){
			while(!_thorn_Q.isEmpty())
				_thorn_Q.poll().delete();
			_thorn_Q = null;
		}
		_curRt 		= null;
	}
	
	@Override
	public String getName(){
		return "LFCWarSquareTrap";
	}
}


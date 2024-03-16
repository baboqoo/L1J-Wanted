package l1j.server.IndunSystem.fantasyisland;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.common.data.ChatType;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;

/**
 * 유니콘 사원 핸들러
 * @author LinOffice
 */
public abstract class FantasylslandHandler implements Runnable {
	private static Logger _log = Logger.getLogger(FantasylslandHandler.class.getName());
	
	protected final FantasyIslandUtil _util;
	protected final short _map;
	protected final FantasylslandType _raidType;
	protected L1PcInstance _pc;
	protected ArrayList<L1NpcInstance> BasicNpcList, NpcList;
	protected L1NpcInstance unicorn, boss;
	
	protected FantasylslandHandler(L1PcInstance pc, short mapId, FantasylslandType raidType){
		_pc			= pc;
		_map		= mapId;
		_raidType	= raidType;
		_util		= FantasyIslandUtil.getInstance();
	}
	
	protected int stage						= 0;
	protected static final int WAIT_STEP	= 0;
	protected static final int FIRST_STEP	= 1;
	protected static final int SECOND_STEP	= 2;
	protected static final int THIRD_STEP	= 3;
	protected static final int LAST_STEP	= 4;
	protected static final int END			= 5;
	protected static final int CLOSE		= 6;
	private int _status;
	
	// 가동 여부
	protected boolean running;
	public void setRun(boolean flag){
		running = flag;
	}
	public boolean isRun(){
		return running;
	}
	
	@Override
	public void run() {
		try{
			createSpwan();
			while (running) {
				try {
					checkCommon();
					checkHp();
					checkPc();
					switch(stage){
					case WAIT_STEP:		waitStep();break;
					case FIRST_STEP:	firstStep();break;
					case SECOND_STEP:	secondStep();break;
					case THIRD_STEP:	thirdStep();break;
					case LAST_STEP:		lastStep();break;
					case END:			end();break;
					default:break;
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					try {
						Thread.sleep(1500L);
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
			raidClose();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected abstract void createSpwan() throws Exception;
	protected abstract void waitStep() throws Exception;
	protected abstract void firstStep() throws Exception;
	protected abstract void secondStep() throws Exception;
	protected abstract void thirdStep() throws Exception;
	protected abstract void lastStep() throws Exception;
	protected abstract void end() throws Exception;
	protected abstract void raidStart();
	protected abstract void raidClose();
	
	protected void dispose(){
		Collection<L1Object> cklist = L1World.getInstance().getVisibleObjects(_map).values();
		for (L1Object obj : cklist) {
			if (obj == null) {
				continue;
			}
			if (obj instanceof L1DollInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
				continue;
			}
			if (obj instanceof L1ItemInstance) {
				L1ItemInstance item = (L1ItemInstance)obj;
				L1Inventory groundInventory = L1World.getInstance().getInventory(item.getX(), item.getY(), item.getMapId());
				groundInventory.removeItem(item);
			} else if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance)obj;
				npc.deleteMe();
			}
		}
		_pc = null;
		FantasyIslandCreator.getInstance().removeRaid(_map);
	}
	
	protected void setting(){
		for (L1NpcInstance npc : BasicNpcList) {
			if (npc == null) {
				continue;
			}
			/*if (npc != null && npc.getName().equalsIgnoreCase("유니콘")) {
				unicorn = npc;
				break;
			}*/
			String[] refs = { "$2488", "$17690", "$28373", "$2488" }; // Unicorn
			for (String ref : refs) {
				if (npc != null && npc.getDesc().equalsIgnoreCase(ref)) {
				  unicorn = npc;
				  break;
				}
			}
		}
	}
	
	private void checkCommon(){
		if (NpcList != null && !NpcList.isEmpty()) {
			ArrayList<L1NpcInstance> dieList = new ArrayList<L1NpcInstance>();
			for (L1NpcInstance npc : NpcList) {
				if (npc == null || npc.isDead()) {
					dieList.add(npc);
				}
			}
			if (!dieList.isEmpty()) {
				for (L1NpcInstance npc : dieList) {
					if (NpcList.contains(npc)) {
						NpcList.remove(npc);
					}
				}
				dieList.clear();
			}
			dieList = null;
		}
		if (unicorn.isDead()) {
			if (_pc != null) {
				_pc.getTeleport().start(33968, 32961, (short)  4, 2, true);
				_pc.getInventory().consumeItem(810006);
				_pc.getInventory().consumeItem(810007);
				_pc.getInventory().consumeItem(810011);
				_pc = null;
			}
			running = false;
			stage = CLOSE;
		}
	}
	
	protected boolean isValidataion(){
		boolean result = _pc != null && _pc.getMapId() == _map;
		if (!result) {
			_pc = null;
		}
		return result;
	}
	
	private void checkPc() {
		if (!isValidataion()) {
			running = false;
			stage = CLOSE;
		}
	}
	
	private void checkHp() {
		if (unicorn == null) {
			return;
		}
		if ((unicorn.getMaxHp() / 5) > unicorn.getCurrentHp()) {// 2000
			if (_status != 4) {
				unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17949", ChatType.CHAT_NORMAL), true);// 더 이상은 힘들 것 같습니다.
				if (_pc != null) {
					_pc.sendPackets(S_PacketBox.LIGHTING);
					_pc.sendPackets(FantasyIslandUtil.UNICORN_DANGEROUS);// 위험한 상황입니다!! 정황 막대를 사용하셔야 합니다!!!
				}
				_status = 4;
			}
		} else if (((unicorn.getMaxHp() << 1) / 5) > unicorn.getCurrentHp()) {// 4000
			if (_status != 3) {
				unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17950", ChatType.CHAT_NORMAL), true);// 조금만 더 버틸 수 있다면...
				if (_pc != null) {
					_pc.sendPackets(S_PacketBox.LIGHTING);
					_pc.sendPackets(FantasyIslandUtil.UNICORN_DANGEROUS);// 위험한 상황입니다!! 정황 막대를 사용하셔야 합니다!!!
				}
				_status = 3;
			}
		} else if ((unicorn.getMaxHp() * 3 / 5) > unicorn.getCurrentHp()) {// 6000
			if (_status != 2) {
				unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17952", ChatType.CHAT_NORMAL), true);// 조금만 더 저들을 막아주세요...
				_status = 2;
			}
		} else if (((unicorn.getMaxHp() << 2) / 5) > unicorn.getCurrentHp()) {// 8000
			if (_status != 1) {
				unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17952", ChatType.CHAT_NORMAL), true);
				_status = 1;
			}
		}
	}
}


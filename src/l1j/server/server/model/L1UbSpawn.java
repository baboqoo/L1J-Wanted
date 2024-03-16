package l1j.server.server.model;

import java.util.ArrayList;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class L1UbSpawn implements Comparable<L1UbSpawn> {
	private int _id;
	private int _ubId;
	private int _pattern;
	private int _group;
	private int _npcTemplateId;
	private int _amount;
	private int _spawnDelay;
	private int _sealCount;
	private String _name;
	private boolean _boss;
	private boolean _gateKeeper;
	
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public int getUbId() {
		return _ubId;
	}

	public void setUbId(int ubId) {
		_ubId = ubId;
	}

	public int getPattern() {
		return _pattern;
	}

	public void setPattern(int pattern) {
		_pattern = pattern;
	}

	public int getGroup() {
		return _group;
	}

	public void setGroup(int group) {
		_group = group;
	}

	public int getNpcTemplateId() {
		return _npcTemplateId;
	}

	public void setNpcTemplateId(int npcTemplateId) {
		_npcTemplateId = npcTemplateId;
	}

	public int getAmount() {
		return _amount;
	}

	public void setAmount(int amount) {
		_amount = amount;
	}

	public int getSpawnDelay() {
		return _spawnDelay;
	}

	public void setSpawnDelay(int spawnDelay) {
		_spawnDelay = spawnDelay;
	}

	public int getSealCount() {
		return _sealCount;
	}

	public void setSealCount(int i) {
		_sealCount = i;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	
	public boolean isBoss() {
		return _boss;
	}

	public void setBoss(boolean boss) {
		_boss = boss;
	}
	
	public boolean isGateKeeper() {
		return _gateKeeper;
	}

	public void setGateKeeper(boolean gateKeeper) {
		_gateKeeper = gateKeeper;
	}

	@SuppressWarnings("unused")
	public void spawnOne() {
		L1UltimateBattle ub = UBTable.getInstance().getUb(_ubId);
		L1Location loc = ub.getLocation().randomLocation((ub.getLocX2() - ub.getLocX1()) >> 1, false);
		L1MonsterInstance mob = new L1MonsterInstance(NpcTable.getInstance().getTemplate(getNpcTemplateId()));
		if (mob == null) {
			_log.warning("mob == null");
			return;
		}

		mob.setId(IdFactory.getInstance().nextId());
		mob.getMoveState().setHeading(5);
		mob.setX(isBoss() ? 33537 : loc.getX());
		mob.setHomeX(isBoss() ? 33537 : loc.getX());
		mob.setY(isBoss() ? 32703 : loc.getY());
		mob.setHomeY(isBoss() ? 32703 : loc.getY());
		mob.setMap((short) loc.getMapId());
		//mob.set_storeDroped(!(3 < getGroup())); //아이템 드랍불가 설정
		mob.setUbSealCount(getSealCount());
		mob.setUbId(getUbId());
		mob.setUpGateKeeper(isGateKeeper());
		mob.setUpBoss(isBoss());
		
		if (getNpcTemplateId() == 30188) {
			reaper(mob);
			return;
		}

		L1World.getInstance().storeObject(mob);
		L1World.getInstance().addVisibleObject(mob);

		ArrayList<L1PcInstance> ll = L1World.getInstance().getRecognizePlayer(mob);
		if (ll.size() > 0) {
			L1PcInstance[] list = (L1PcInstance[])ll.toArray(new L1PcInstance[ll.size()]);
			for (L1PcInstance pc : list) {
				mob.onPerceive(pc);
			}
			mob.setTarget(list[CommonUtil.random(list.length)]);
			list = null;
		}
		
		if (mob.getNpcId() >= 30189 && mob.getNpcId() <= 30189) {
			mob.broadcastPacket(new S_Effect(mob.getId(), 12261), true);
		}
		mob.broadcastPacket(new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Appear), true);
		mob.onNpcAI();
		mob.getLight().turnOnOffLight();
	}

	public void spawnAll() {
		for (int i = 0; i < getAmount(); i++) {
			spawnOne();
		}
	}

	public int compareTo(L1UbSpawn rhs) {
		if (getId() < rhs.getId())
			return -1;
		if (getId() > rhs.getId())
			return 1;
		return 0;
	}

	private void reaper(L1MonsterInstance mob){
		L1SpawnUtil.spawn2(mob.getX(), mob.getY(), (short) mob.getMapId(), 5, 800164, 0, 10000, 0);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			
			@Override
			public void run() {
				L1World world = L1World.getInstance();
				world.storeObject(mob);
				world.addVisibleObject(mob);
				
				ArrayList<L1PcInstance> ll = world.getRecognizePlayer(mob);
				if (ll.size() > 0) {
					L1PcInstance[] list = (L1PcInstance[])ll.toArray(new L1PcInstance[ll.size()]);
					for (L1PcInstance pc : list) {
						mob.onPerceive(pc);
					}
					mob.setTarget(list[CommonUtil.random(list.length)]);
					list = null;
				}
				mob.broadcastPacket(new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Appear), true);
				mob.onNpcAI();
				mob.getLight().turnOnOffLight();
			}
		}, 10000L);
		return;
	}
	
	private static final Logger _log = Logger.getLogger(L1UbSpawn.class.getName());
}


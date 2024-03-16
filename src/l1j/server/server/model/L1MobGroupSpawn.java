package l1j.server.server.model;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.MobGroupTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1MobGroupInfo;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1MobGroup;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;

public class L1MobGroupSpawn {

	private static final Logger _log = Logger.getLogger(L1MobGroupSpawn.class.getName());

	private static L1MobGroupSpawn _instance;

	private boolean _isRespawnScreen;

	private boolean _isInitSpawn;

	private L1MobGroupSpawn() {
	}

	public static L1MobGroupSpawn getInstance() {
		if (_instance == null) {
			_instance = new L1MobGroupSpawn();
		}
		return _instance;
	}

	public void doSpawn(L1NpcInstance leader, int groupId, boolean isRespawnScreen, boolean isInitSpawn) {
		doSpawn(leader, groupId, isRespawnScreen, isInitSpawn, -1);
	}
	
	public void doSpawn(L1NpcInstance leader, int groupId, boolean isRespawnScreen, boolean isInitSpawn, int deleteTime) {
		L1MobGroup mobGroup = MobGroupTable.getInstance().getTemplate(groupId);
		if (mobGroup == null) {
			return;
		}

		L1NpcInstance mob;
		_isRespawnScreen = isRespawnScreen;
		_isInitSpawn = isInitSpawn;

		L1MobGroupInfo mobGroupInfo = new L1MobGroupInfo();
		mobGroupInfo.setRemoveGroup(mobGroup.isRemoveGroupIfLeaderDie());
		mobGroupInfo.addMember(leader);

		if (mobGroup.getMinion1Id() > 0 && mobGroup.getMinion1Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion1Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion1Id(), deleteTime);
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion2Id() > 0 && mobGroup.getMinion2Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion2Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion2Id(), deleteTime);
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion3Id() > 0 && mobGroup.getMinion3Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion3Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion3Id(), deleteTime);
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion4Id() > 0 && mobGroup.getMinion4Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion4Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion4Id(), deleteTime);
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion5Id() > 0 && mobGroup.getMinion5Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion5Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion5Id(), deleteTime);
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion6Id() > 0 && mobGroup.getMinion6Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion6Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion6Id(), deleteTime);
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion7Id() > 0 && mobGroup.getMinion7Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion7Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion7Id(), deleteTime);
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion8Id() > 0 && mobGroup.getMinion8Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion8Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion8Id(), deleteTime);
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
	}
	
	private L1NpcInstance spawn(L1NpcInstance leader, int npcId, int deleteTime) {
		L1NpcInstance mob = null;
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
			if (l1npc == null) {
				return null;
			}

			String s = l1npc.getImpl();
			Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
			Object parameters[] = { l1npc };
			mob = (L1NpcInstance) constructor.newInstance(parameters);
			mob.setId(IdFactory.getInstance().nextId());

			mob.getMoveState().setHeading(leader.getMoveState().getHeading());
			mob.setMap(leader.getMapId());
			mob.setMovementDistance(leader.getMovementDistance());
			mob.setRest(leader.isRest());

			mob.setX(leader.getX() + CommonUtil.random(5) - 2);
			mob.setY(leader.getY() + CommonUtil.random(5) - 2);
			
			if (!isDoSpawn(mob)) {
				mob.setX(leader.getX());
				mob.setY(leader.getY());
			}
			
			mob.setHomeX(mob.getX());
			mob.setHomeY(mob.getY());

			if (mob instanceof L1MonsterInstance) {
				((L1MonsterInstance) mob).initHideForMinion(leader);
			}
			mob.setSpawn(leader.getSpawn());
			mob.setRespawn(leader.isReSpawn());
			mob.setSpawnNumber(leader.getSpawnNumber());

			L1World world = L1World.getInstance();
			world.storeObject(mob);
			world.addVisibleObject(mob);

			if (mob instanceof L1MonsterInstance && !_isInitSpawn && mob.getHiddenStatus() == 0) {
				mob.onNpcAI();
			}
			mob.getLight().turnOnOffLight();
			mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
			
			if (deleteTime > 0) {
				new L1NpcDeleteTimer(mob, deleteTime * 1000).begin();// 자동 삭제 시간
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return mob;
	}

	private boolean isDoSpawn(L1NpcInstance mob) {
		if (mob.getMap().isInMap(mob.getLocation()) && mob.getMap().isPassable(mob.getLocation()) && (_isRespawnScreen || L1World.getInstance().getVisiblePlayer(mob).size() == 0)) {
			return true;
		}
		return false;
	}

}


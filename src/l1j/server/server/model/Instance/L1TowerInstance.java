package l1j.server.server.model.Instance;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.controller.action.War;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1WarSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.templates.L1Npc;

public class L1TowerInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public L1TowerInstance(L1Npc template) {
		super(template);
	}

	protected L1Character _lastattacker;
	protected int _castle_id;
	//앱센터
	protected int _crackStatus;

	@Override
	public void onAction(L1PcInstance player) {
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(player, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(player, this);
			}
			attack.action();
			attack.commit();
			attack = null;
		}
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (_castle_id == 0)
			_castle_id = isSubTower() ? L1CastleLocation.ADEN_CASTLE_ID : L1CastleLocation.getCastleId(getX(), getY(), getMapId());
		if (_castle_id > 0 && War.getInstance().isNowWar(_castle_id)) { 
			if (_castle_id == L1CastleLocation.ADEN_CASTLE_ID && !isSubTower()) {
				int subTowerDeadCount = 0;
				L1TowerInstance tower = null;
				for (L1Object l1object : L1World.getInstance().getObject()) {
					if (l1object instanceof L1TowerInstance) {
						tower = (L1TowerInstance) l1object;
						if (tower.isSubTower() && tower.isDead()) {
							subTowerDeadCount++;
							if (subTowerDeadCount == 4) {
								break;
							}
						}
					}
				}
				if (subTowerDeadCount < 3) {
					return;
				}
			}

			L1PcInstance pc = null;
			if (attacker instanceof L1PcInstance) {
				pc = (L1PcInstance) attacker;	
			} else if (attacker instanceof L1PetInstance) {
				pc = (L1PcInstance) ((L1PetInstance) attacker).getMaster();
			} else if (attacker instanceof L1SummonInstance) {
				pc = (L1PcInstance) ((L1SummonInstance) attacker).getMaster();
			}
			if (pc == null) {
				return;
			}
			
			/** 월드 공성전 **/
			if (_castle_id == L1CastleLocation.OT_CASTLE_ID
					|| _castle_id == L1CastleLocation.GIRAN_CASTLE_ID
					|| _castle_id == L1CastleLocation.HEINE_CASTLE_ID) {
				if (_castle_id == L1CastleLocation.OT_CASTLE_ID) {
					L1MonsterInstance boss = null;
					for (L1Object obj : L1World.getInstance().getVisibleObjects(15483).values()) {
						if (obj instanceof L1MonsterInstance) {
							boss = (L1MonsterInstance) obj;
							if (boss != null && !boss.isDead() && boss.getNpcTemplate().getNpcId() == 800551) {// 아투바 족장 브라크
								return;
							}
						}
					}
				} else if (_castle_id == L1CastleLocation.GIRAN_CASTLE_ID) {
					L1MonsterInstance boss = null;
					for (L1Object obj : L1World.getInstance().getVisibleObjects(15482).values()) {
						if (obj instanceof L1MonsterInstance) {
							boss = (L1MonsterInstance) obj;
							if (boss != null && !boss.isDead() && boss.getNpcTemplate().getNpcId() == 800553) {// 기란성 성주 듀세라
								return;
							}
						}
					}
				} else if (_castle_id == L1CastleLocation.HEINE_CASTLE_ID) {
					L1MonsterInstance boss = null;
					for (L1Object obj : L1World.getInstance().getVisibleObjects(15484).values()) {
						if (obj instanceof L1MonsterInstance) {
							boss = (L1MonsterInstance) obj;
							if (boss != null && !boss.isDead() && boss.getNpcTemplate().getNpcId() == 800554) {// 하이네성 성주 트로돈
								return;
							}
						}
					}
				}
			}
			
			/** 성주 혈맹 **/
			boolean existDefenseClan = false;
			for (L1Clan clan : L1World.getInstance().getAllClans()) {
				int clanCastleId = clan.getCastleId();
				if (clanCastleId == _castle_id) {
					existDefenseClan = true;
					break;
				}
			}
			
			/** 선포 혈맹 **/
			boolean isProclamation = false;
			for (L1War war : L1World.getInstance().getWarList()) {
				if (_castle_id == war.GetCastleId()) {
					isProclamation = war.CheckClanInWar(pc.getClanName());
					break;
				}
			}
			
			if (existDefenseClan == true && isProclamation == false) {
				return;
			}
			if (getCurrentHp() > 0 && !isDead()) {
				int newHp = getCurrentHp() - damage;
				if (newHp <= 0 && !isDead()) {
					setCurrentHp(0);
					setDead(true);
					setActionStatus(ActionCodes.ACTION_TowerDie);
					_lastattacker = attacker;
					_crackStatus = 0;
					Death death = new Death();
					GeneralThreadPool.getInstance().execute(death);
				}
				if (newHp > 0) {
					setCurrentHp(newHp);
					int maxHp = getMaxHp();
					if ((maxHp >> 2) > getCurrentHp()) {
						if (_crackStatus != 3) {
							broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerCrack3), true);
							setActionStatus(ActionCodes.ACTION_TowerCrack3);
							_crackStatus = 3;
						}
					} else if (((maxHp << 1) >> 2) > getCurrentHp()) {
						if (_crackStatus != 2) {
							broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerCrack2), true);
							setActionStatus(ActionCodes.ACTION_TowerCrack2);
							_crackStatus = 2;
						}
					} else if (((maxHp * 3) >> 2) > getCurrentHp()) {
						if (_crackStatus != 1) {
							broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerCrack1), true);
							setActionStatus(ActionCodes.ACTION_TowerCrack1);
							_crackStatus = 1;
						}
					}
				}
			} else if (!isDead()) {
				setDead(true);
				setActionStatus(ActionCodes.ACTION_TowerDie);
				_lastattacker = attacker;
				Death death = new Death();
				GeneralThreadPool.getInstance().execute(death);
				// Death(attacker);
			}
		}
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
	}

	class Death implements Runnable {
		L1Character lastAttacker = _lastattacker;
		L1Object object = L1World.getInstance().findObject(getId());
		L1TowerInstance npc = (L1TowerInstance) object;

		@Override
		public void run() {
			setCurrentHp(0);
			setDead(true);
			setActionStatus(ActionCodes.ACTION_TowerDie);
			npc.getMap().setPassable(npc.getLocation(), true);
			npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_TowerDie), true);
			if (!isSubTower()) {
				L1WarSpawn warspawn = new L1WarSpawn();
				warspawn.SpawnCrown(_castle_id);
			}
		}
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		L1World world = L1World.getInstance();
		world.removeVisibleObject(this);
		world.removeObject(this);
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		removeAllKnownObjects();
	}
	
	public boolean isSubTower() {
		return (getNpcTemplate().getNpcId() == 81190 || getNpcTemplate().getNpcId() == 81191 || getNpcTemplate().getNpcId() == 81192 || getNpcTemplate().getNpcId() == 81193);
	}
}


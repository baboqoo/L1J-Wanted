package l1j.server.server.model.Instance;

import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.templates.L1Npc;

public class L1TowerFromOccupyInstance extends L1TowerInstance {
	private static final long serialVersionUID = 1L;
	private L1PcInstance _lastAttackPc;
	
	public L1TowerFromOccupyInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		L1PcInstance pc = null;
		if (attacker instanceof L1PcInstance) {
			pc = (L1PcInstance) attacker;
		} else if(attacker instanceof L1PetInstance) {
			pc = (L1PcInstance) ((L1PetInstance) attacker).getMaster();
		} else if(attacker instanceof L1SummonInstance) {
			pc = (L1PcInstance) ((L1SummonInstance) attacker).getMaster();
		}
		if (pc == null || pc.getNetConnection() == null || !L1InterServer.isOccupyInter(pc.getNetConnection().getInter()) || pc._occupyTeamType == null) {
			return;
		}
		OccupyHandler ocHandler = OccupyManager.getInstance().getHandler(this.getMapId() == L1TownLocation.MAP_WOLRDWAR_HEINE_TOWER ? OccupyType.HEINE : OccupyType.WINDAWOOD);
		if (ocHandler == null || !ocHandler.isRunning() || ocHandler.isWinnerTeam(pc._occupyTeamType)) {
			return;
		}
		
		if (getCurrentHp() > 0 && !isDead()) {
			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				setCurrentHp(0);
				setDead(true);
				setActionStatus(ActionCodes.ACTION_TowerDie);
				_lastattacker = attacker;
				_lastAttackPc = pc;
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
			_lastAttackPc = pc;
			Death death = new Death();
			GeneralThreadPool.getInstance().execute(death);
		}
	}
	
	public L1PcInstance getLastAttackPc(){
		return _lastAttackPc;
	}
}


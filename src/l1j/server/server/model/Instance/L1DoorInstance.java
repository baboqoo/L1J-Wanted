package l1j.server.server.model.Instance;

import l1j.server.GameSystem.astar.World;
import l1j.server.IndunSystem.ruun.Ruun;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_Door;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;

public class L1DoorInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	public static final int PASS		= 0;
	public static final int NOT_PASS	= 1;
	
	private int _doorId;
	private int _direction;
	private int _leftEdgeLocation;
	private int _rightEdgeLocation;
	private int _openStatus	= ActionCodes.ACTION_Close;
	private int _passable	= NOT_PASS;
	private int _keeperId;
	private int _autostatus;

	public L1DoorInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		if (getMaxHp() <= 1) {
			return;
		}
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(pc, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(pc, this);
			}
			attack.action();
			attack.commit();
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
		sendDoorPacket(perceivedFrom);
		if (!isDead()) {
			switch(_crackStatus) {
			case 1:
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction1), true);
				break;
			case 2:
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction2), true);
				break;
			case 3:
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction3), true);
				break;
			case 4:
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction4), true);
				break;
			case 5:
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction5), true);
				break;
			}
		} else {
			perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorDie), true);
		}
	}

	@Override
	public void deleteMe() {
		setPassable(PASS);
		sendDoorPacket(null);

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
			if (pc == null) {
				continue;
			}
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		removeAllKnownObjects();
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		int maxHp = getMaxHp();
		if (maxHp <= 1) {
			return;
		}
		int spriteId = getSpriteId();
		if (getCurrentHp() > 0 && !isDead()) {
			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				setCurrentHp(0);
				setDead(true);
				setActionStatus(DoorAction(spriteId, ActionCodes.ACTION_DoorDie));
				Death death = new Death(attacker);
				GeneralThreadPool.getInstance().execute(death);
			}
			if (newHp > 0) {
				if (attacker instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) attacker;
					L1Clan clan = pc.getClan();
					if (clan != null) {
						int castleId = clan.getCastleId();
						if (castleId != L1CastleLocation.getCastleIdByArea(pc)) {
							L1CastleGuardInstance guard = null;
							for (L1Object object : L1World.getInstance().getVisibleObjects(pc)) {
								if (object instanceof L1CastleGuardInstance) {
									guard = (L1CastleGuardInstance) object;
									guard.setTarget(pc);
								}
							}
						}
					} else {
						L1CastleGuardInstance guard = null;
						for (L1Object object : L1World.getInstance().getVisibleObjects(pc)) {
							if (object instanceof L1CastleGuardInstance) {
								guard = (L1CastleGuardInstance) object;
								guard.setTarget(pc);
							}
						}
					}
				}
				setCurrentHp(newHp);
				if ((maxHp / 6) > getCurrentHp()) {
					if (_crackStatus != 5) {
						broadcastPacket(new S_DoActionGFX(getId(), DoorAction(spriteId, ActionCodes.ACTION_DoorAction5)), true);
						setActionStatus(DoorAction(spriteId, ActionCodes.ACTION_DoorAction5));
						_crackStatus = 5;
					}
				} else if (((maxHp << 1) / 6) > getCurrentHp()) {
					if (_crackStatus != 4) {
						broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction4), true);
						setActionStatus(ActionCodes.ACTION_DoorAction4);
						_crackStatus = 4;
					}
				} else if ((maxHp * 3 / 6) > getCurrentHp()) {
					if (_crackStatus != 3) {
						broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction3), true);
						setActionStatus(ActionCodes.ACTION_DoorAction3);
						_crackStatus = 3;
					}
				} else if (((maxHp << 2) / 6) > getCurrentHp()) {
					if (_crackStatus != 2) {
						broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction2), true);
						setActionStatus(ActionCodes.ACTION_DoorAction2);
						_crackStatus = 2;
					}
				} else if ((maxHp * 5 / 6) > getCurrentHp()) {
					if (_crackStatus != 1) {
						broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction1), true);
						setActionStatus(ActionCodes.ACTION_DoorAction1);
						_crackStatus = 1;
					}
				}
			}
		} else if (!isDead()) {
			setDead(true);
			setActionStatus(DoorAction(spriteId, ActionCodes.ACTION_DoorDie));
			//isPassibleDoor(true);
			Death death = new Death(attacker);
			GeneralThreadPool.getInstance().execute(death);
		}
	}
		
	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
	}

	class Death implements Runnable {
		L1Character _lastAttacker;

		public Death(L1Character lastAttacker) {
			_lastAttacker = lastAttacker;
		}

		@Override
		public void run() {
			int spriteId = getSpriteId();
			setCurrentHp(0);
			setDead(true);
			isPassibleDoor(true);//하딘 시스템
			setActionStatus(DoorAction(spriteId, ActionCodes.ACTION_DoorDie));
			getMap().setPassable(getLocation(), true);
			broadcastPacket(new S_DoActionGFX(getId(), DoorAction(spriteId, ActionCodes.ACTION_DoorDie)), true);
			setPassable(PASS);
			sendDoorPacket(null);
		}
	}

	public class DoorTimer implements Runnable {
		@Override
		public void run() {
			if (_destroyed) {// 이미 파기되어 있지 않은가 체크
				return;
			}
			close();
		}
	}

	public void sendDoorPacket(L1PcInstance pc) {
		int entranceX = getEntranceX();
		int entranceY = getEntranceY();
		int leftEdgeLocation = getLeftEdgeLocation();
		int rightEdgeLocation = getRightEdgeLocation();

		int size = rightEdgeLocation - leftEdgeLocation;
		if (size == 0) {
			sendPacket(pc, entranceX, entranceY);
			return;
		}
		/** 성문뚫어방지 **/
		if (getDirection() == 0) {
			for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
				if (size >= 5) {
					sendPacket(pc, x, entranceY);
					sendPacket(pc, x, entranceY - 1);
				} else {
					sendPacket(pc, x, entranceY);
				}
			}
		} else {
			for (int y = leftEdgeLocation; y <= rightEdgeLocation; y++) {
				if (size >= 4) {
					sendPacket(pc, entranceX, y);
					sendPacket(pc, entranceX + 1, y);
				} else {
					sendPacket(pc, entranceX, y);
				}
			}
		}
	}

	private void sendPacket(L1PcInstance pc, int x, int y) {
		int passable = !isDead() && getOpenStatus() == ActionCodes.ACTION_Close ? 1 : 0;
		S_Door packet = new S_Door(x, y, getDirection(), passable);
		if (pc != null) {
			pc.sendPackets(packet, true);
		} else {
			broadcastPacket(packet, true);
		}
	}

	public void open() {
		if (isDead()) {
			return;
		}
		if (getOpenStatus() == ActionCodes.ACTION_Close) {
			isPassibleDoor(true);//하딘 시스템
			int doorId = getDoorId();
			if (doorId == 113 || doorId == 125) {
				GeneralThreadPool.getInstance().schedule(new DoorTimer(), 5000L);
			// 말하는 섬 던전 2층 보스문 시간 5분
			} else if (doorId >= 4100 && doorId <= 4111) {
				GeneralThreadPool.getInstance().schedule(new DoorTimer(), 300000L);
			} else if (doorId >= 8001 && doorId <= 8010) {
				GeneralThreadPool.getInstance().schedule(new DoorTimer(), 1800000L);
			} else if (doorId >= 9000 && doorId <= 9056) {// 발라방 불꽃
				GeneralThreadPool.getInstance().schedule(new DoorTimer(), 2700000L);
			} else if (doorId == 231) {// 지배의탑 정상 사슬
				GeneralThreadPool.getInstance().schedule(new DoorTimer(), 3600000L);
			} else if (doorId >= 6008 && doorId <= 6011) {// 루운던전 도어 1분 유지
				GeneralThreadPool.getInstance().schedule(new DoorTimer(), 60000L);
			}
			broadcastPacket(new S_DoActionGFX(this.getId(),ActionCodes.ACTION_Open), true);
			setOpenStatus(ActionCodes.ACTION_Open);
			setPassable(L1DoorInstance.PASS);
			sendDoorPacket(null);
		}
	}

	public void close() {
		if (isDead()) {
			return;
		}
		if (getOpenStatus() == ActionCodes.ACTION_Open) {
			isPassibleDoor(false);//하딘 시스템
			broadcastPacket(new S_DoActionGFX(this.getId(),ActionCodes.ACTION_Close), true);
			setOpenStatus(ActionCodes.ACTION_Close);
			setPassable(L1DoorInstance.NOT_PASS);
			sendDoorPacket(null);
		}
	}

	public int DoorAction(int spriteId, int dooraction) {
		int action = dooraction;
		if (action == ActionCodes.ACTION_DoorDie) {
			if (spriteId == 11987 || spriteId == 11989 || spriteId == 11991 // 켄트성문
					|| spriteId == 12127 || spriteId == 12129 || spriteId == 12133// 기란성문
					|| spriteId == 12163 || spriteId == 12164 || spriteId == 12167 || spriteId == 12170
					|| spriteId == 17923) {
				action = 36;
			}
		} else if (action == ActionCodes.ACTION_DoorAction5) {
			if (spriteId == 11987 || spriteId == 11989 || spriteId == 11991 // 켄트성문
					|| spriteId == 12127 || spriteId == 12129 || spriteId == 12133// 기란성문
					|| spriteId == 12163 || spriteId == 12164 || spriteId == 12167 || spriteId == 12170
					|| spriteId == 17923) {
				action = 35;
			}
		}
		return action;
	}
	
	public void isPassibleDoor(boolean flag) {
		int entranceX			= getEntranceX();
		int entranceY			= getEntranceY();
		int leftEdgeLocation	= this.getLeftEdgeLocation();
		int rightEdgeLocation	= this.getRightEdgeLocation();
		int size				= rightEdgeLocation - leftEdgeLocation;
		int doorId				= getDoorId();
		if (size >= 0) {
			if (getDirection() == 0 || getDirection() == 4) {
				if (doorId == 2010 || doorId == 225 || doorId == 226) {
					for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
						World.isDoorMove(x, entranceY - 1, this.getMapId(), false, flag);
					}
				} else {
					for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
						World.isDoorMove(x, entranceY, this.getMapId(), false, flag);
					}
				}
			} else {
				for (int y = leftEdgeLocation; y <= rightEdgeLocation; y++) {
					World.isDoorMove(entranceX, y, this.getMapId(), true, flag);
				}
			}
		}
		
		// 루운성 던전 도어 액션에 따른 몬스터 스폰처리
		if (!flag && (doorId >= 6009 && doorId <= 6011)) {
			Ruun.getInstance().ruunDoorCloseAction(this);
		}
	}
	
	public void repairGate() {
		if (getMaxHp() > 1) {
			setDead(false);
			setCurrentHp(getMaxHp());
			setActionStatus(0);
			setCrackStatus(0);
			setOpenStatus(ActionCodes.ACTION_Open);
			close();
		}
	}

	public int getDoorId() {
		return _doorId;
	}
	public void setDoorId(int i) {
		_doorId = i;
	}

	public int getDirection() {
		return _direction;
	}
	public void setDirection(int i) {
		if (i == 0 || i == 1) {
			_direction = i;
		}
	}

	public int getEntranceX() {
		return getDirection() == 0 ? getX() : getX() - 1;
	}
	public int getEntranceY() {
		return getDirection() == 0 ? getY() + 1 : getY();
	}

	public int getLeftEdgeLocation() {
		return _leftEdgeLocation;
	}
	public void setLeftEdgeLocation(int i) {
		_leftEdgeLocation = i;
	}

	public int getRightEdgeLocation() {
		return _rightEdgeLocation;
	}
	public void setRightEdgeLocation(int i) {
		_rightEdgeLocation = i;
	}

	public int getOpenStatus() {
		return _openStatus;
	}
	public void setOpenStatus(int i) {
		if (i == ActionCodes.ACTION_Open || i == ActionCodes.ACTION_Close) {
			_openStatus = i;
		}
	}

	public int getPassable() {
		return _passable;
	}
	public void setPassable(int i) {
		if (i == PASS || i == NOT_PASS) {
			_passable = i;
		}
	}

	public int getKeeperId() {
		return _keeperId;
	}
	public void setKeeperId(int i) {
		_keeperId = i;
	}

	private int _crackStatus;
	public int getCrackStatus() {
		return _crackStatus;
	}
	public void setCrackStatus(int i) {
		_crackStatus = i;
	}
	
	public int getAutoStatus() {
		return _autostatus;
	}
	public void setAutoStatus(int i) {
		_autostatus = i;
	}

}

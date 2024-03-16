package l1j.server.server.model.Instance;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.companion.S_PetMenuPacket;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.object.S_SummonObject;
import l1j.server.server.serverpackets.returnedstat.S_ReturnedStatPetParty;
import l1j.server.server.templates.L1Npc;

public class L1SummonInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	private static final Random random = new Random(System.nanoTime());

	private ScheduledFuture<?> _summonFuture;
	private static final long SUMMON_TIME = 3600000L;
	private int _currentStatus;
	
	//private boolean _tamed;
	private boolean _isReturnToNature = false;
	private boolean _isGreatSummon = false;

	@Override
	public boolean noTarget() {
		switch (_currentStatus) {
		case 3:return true;// 휴식모드
		case 4:
			if (_master != null && _master.getMapId() == getMapId() && getLocation().getTileLineDistance(_master.getLocation()) < 5) {
				int dir = targetReverseDirection(_master.getX(), _master.getY());
				dir = checkObject(getX(), getY(), getMapId(), dir);
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			} else {
				_currentStatus = 3;
				return true;
			}
		case 5:// 경계모드
			if (Math.abs(getHomeX() - getX()) > 1 || Math.abs(getHomeY() - getY()) > 1) {
				int dir = moveDirection(getMapId(), getHomeX(), getHomeY());
				if (dir == -1) {
					setHomeX(getX());
					setHomeY(getY());
				} else {
					setDirectionMove(dir);
					setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
				}
			}
			return false;
		default:
			if (_master != null && _master.getMapId() == getMapId()) {
				if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
					int dir = moveDirection(_master.getMapId(), _master.getX(), _master.getY());
					if (dir == -1) {
						allTargetClear();
						teleport(_master.getX(), _master.getY(), getMoveState().getHeading());
						//_currentPetStatus = 3;// 휴식
						//return true;
					} else {
						setDirectionMove(dir);
						setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
					}
				}
			} else {
				_currentStatus = 3;// 휴식
				return true;
			}
			return false;
		}
	}

	class SummonTimer implements Runnable {
		@Override
		public void run() {
			try {
				if (_destroyed) {
					return;
				}
				// if (_tamed) {
				// liberate();
				// } else {
				death(null);
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public L1SummonInstance(L1Npc template, L1Character master, int summonType) {
		super(template);
		setId(IdFactory.getInstance().nextId());
		_summonFuture = GeneralThreadPool.getInstance().schedule(new SummonTimer(), SUMMON_TIME);
		setMaster(master);
		setX(master.getX() + random.nextInt(5) - 2);
		setY(master.getY() + random.nextInt(5) - 2);
		setMap(master.getMapId());
		getMoveState().setHeading(5);
		setLightSize(template.getLightSize());
		
		if (summonType == 1) {// 서먼 몬스터 소환시 소환자의 레벨, sp세팅
			setLevel(master.getLevel());
			getAbility().addSp(master.getAbility().getSp());
		}
		boolean appearAction = false;
		int npcId = getNpcId();
		if (npcId == 810835 || npcId == 810838 || npcId == 810839 || npcId == 810841 || npcId == 810843
				|| npcId == 810844 || npcId == 810853 || npcId == 810854 || npcId == 810855) {
			appearAction = true;
		}
		if (npcId == 810853 || npcId == 810854 || npcId == 810855) {
			_isGreatSummon = true;
		}
		_currentStatus = 3;
		//_tamed = false;

		L1World world = L1World.getInstance();
		world.storeObject(this);
		world.addVisibleObject(this);
		
		if (appearAction) {
			setActionStatus(ActionCodes.ACTION_Appear);
			S_CharVisualUpdate visual = new S_CharVisualUpdate(this, 0);
			for (L1PcInstance pc : world.getVisiblePlayer(this)) {
				onPerceive(pc);
				pc.sendPackets(visual);
			}
			setActionStatus(0);
			visual.clear();
			visual = null;
		} else {
			for (L1PcInstance pc : world.getVisiblePlayer(this)) {
				onPerceive(pc);
			}
		}

		master.addPet(this);
		//Object aobj[] = master.getPetList().values().toArray();
		((L1PcInstance) master).sendPackets(new S_ReturnedStatPetParty(getId(), getX(), getY(), getMapId(), getDesc(), true), true);
		((L1PcInstance) master).sendPackets(new S_HPMeter(this), true);
	}
	
	public L1SummonInstance(L1Npc template, L1PcInstance master, boolean cost) {
		super(template);
		setId(IdFactory.getInstance().nextId());

		_summonFuture = GeneralThreadPool.getInstance().schedule(new SummonTimer(), SUMMON_TIME);

		setMaster(master);
		setX(master.getX() + random.nextInt(5) - 2);
		setY(master.getY() + random.nextInt(5) - 2);
		setMap(master.getMapId());
		getMoveState().setHeading(5);
		setLightSize(template.getLightSize());
		if (cost) {
			setPetcost(6);
		}
		_currentStatus = 3;
		// _tamed = false;

		L1World world = L1World.getInstance();
		world.storeObject(this);
		world.addVisibleObject(this);
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			if (pc != null) {
				onPerceive(pc);
			}
		}
		master.addPet(this);
	}

	public L1SummonInstance(L1NpcInstance target, L1Character master, boolean isCreateZombie) {
		super(null);
		setId(IdFactory.getInstance().nextId());

		if (isCreateZombie) { 
			int npcId = 45065;
			L1PcInstance pc = (L1PcInstance) master;
			int level = pc.getLevel();
			if (pc.isWizard()) {
				if (level >= 24 && level <= 31) {
					npcId = 81183;
				} else if (level >= 32 && level <= 39) {
					npcId = 81184;
				} else if (level >= 40 && level <= 43) {
					npcId = 81185;
				} else if (level >= 44 && level <= 47) {
					npcId = 81186;
				} else if (level >= 48 && level <= 51) {
					npcId = 81187;
				} else if (level >= 52) {
					npcId = 81188;
				}
			} else if (pc.isElf()) {
				if (level >= 48) {
					npcId = 81183;
				}
			}
			L1Npc template = NpcTable.getInstance().getTemplate(npcId).clone();
			setting_template(template);
		} else { 
			setting_template(target.getNpcTemplate());
			setCurrentHp(target.getCurrentHp());
			setCurrentMp(target.getCurrentMp());
		}

		_summonFuture = GeneralThreadPool.getInstance().schedule(new SummonTimer(), SUMMON_TIME);

		setMaster(master);
		setX(target.getX());
		setY(target.getY());
		setMap(target.getMapId());
		getMoveState().setHeading(target.getMoveState().getHeading());
		setLightSize(target.getLightSize());
		setPetcost(6);

		if (target instanceof L1MonsterInstance && !((L1MonsterInstance) target).isStoreDroped()) {
			DropTable.getInstance().setDrop(target, target.getInventory());
		}
		setInventory(target.getInventory());
		target.setInventory(null);

		_currentStatus = 3;
		//_tamed = true;

		for (L1NpcInstance each : master.getPetList().values()) {
			if (each != null) {
				each.targetRemove(target);
			}
		}

		target.deleteMe();
		
		L1World world = L1World.getInstance();
		world.storeObject(this);
		world.addVisibleObject(this);
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			if (pc != null) {
				onPerceive(pc);
			}
		}
		master.addPet(this);
		//Object aobj[] = master.getPetList().values().toArray();
		((L1PcInstance) master).sendPackets(new S_ReturnedStatPetParty(getId(), getX(), getY(), getMapId(), getDesc(), true), true);
		((L1PcInstance) master).sendPackets(new S_HPMeter(this), true);
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) { 
		if (getCurrentHp() > 0) {
			if (damage > 0) {
				setHate(attacker, 0); 
				removeSleepSkill();
				if (!isExsistMaster()) {
					_currentStatus = 1;
					setTarget(attacker);
				}
			}

			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance player = (L1PcInstance) attacker;
				player.setPetTarget(this);
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0) {
				death(attacker);
			} else {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) {
			death(attacker);
		}
	}

	public synchronized void death(L1Character lastAttacker) {
		if (!isDead()) {
			setDead(true);
			setCurrentHp(0);
			setActionStatus(ActionCodes.ACTION_Die);

			getMap().setPassable(getLocation(), true);

			L1Inventory targetInventory = _master.getInventory();
			List<L1ItemInstance> items = _inventory.getItems();
			for (L1ItemInstance item : items) {
				if (item == null) {
					continue;
				}
				if (_master.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
					_inventory.tradeItem(item, item.getCount(), targetInventory);
					((L1PcInstance) _master).sendPackets(new S_ServerMessage(143, getName(), item.getLogNameRef()), true);
				} else {
					targetInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
					_inventory.tradeItem(item, item.getCount(), targetInventory);
				}
			}
//			if (_tamed) {
//				broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
//				startDeleteTimer();
//			} else
				deleteMe();
		}
	}

	public synchronized void returnToNature() {
		if (_isGreatSummon) {
			return;
		}
		_isReturnToNature = true;
		//if (!_tamed) {
			getMap().setPassable(getLocation(), true);
			L1Inventory targetInventory = _master.getInventory();
			List<L1ItemInstance> items = _inventory.getItems();
			for (L1ItemInstance item : items) {
				if (item == null) {
					continue;
				}
				if (_master.getInventory().checkAddItem( item, item.getCount()) == L1Inventory.OK) {
					_inventory.tradeItem(item, item.getCount(), targetInventory);
					((L1PcInstance) _master).sendPackets(new S_ServerMessage(143, getName(), item.getLogNameRef()), true);
				} else {
					targetInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
					_inventory.tradeItem(item, item.getCount(), targetInventory);
				}
			}
			deleteMe();
//		} else
//			liberate();
	}
	
	@Override
	public synchronized void deleteMe() {
		if (_destroyed) {
			return;
		}
		if (/*!_tamed && */!_isReturnToNature) {
			broadcastPacket(new S_Effect(getId(), 169), true);
			/*Object aobj[] = _master.getPetList().values().toArray();
			if (aobj == null) return;
			for (int i = 0; i < aobj.length; i++) {
				if (aobj[i] == this)
					((L1PcInstance) _master).sendPackets(new S_ReturnedStatPetParty(getId(), getX(), getY(), getMapId(), getNameId(), false), true);
			}*/
			((L1PcInstance) _master).sendPackets(new S_ReturnedStatPetParty(getId(), getX(), getY(), getMapId(), getDesc(), false), true);
		}
		_master.getPetList().remove(Integer.valueOf(getId()));
		super.deleteMe();
		if (_summonFuture != null) {
			_summonFuture.cancel(false);
			_summonFuture = null;
		}
		_master = null;
	}
	
	public synchronized void deleteLogout(){
		if (_destroyed) {
			return;
		}
		getMap().setPassable(getLocation(), true);
		_master.getPetList().remove(Integer.valueOf(getId()));
		super.deleteMe();
		if (_summonFuture != null) {
			_summonFuture.cancel(false);
			_summonFuture = null;
		}
		_master = null;
	}

	public void liberate() {
		if (getRegion() == L1RegionStatus.SAFETY) {
			return;
		}
		L1MonsterInstance monster = new L1MonsterInstance(getNpcTemplate());
		monster.setId(IdFactory.getInstance().nextId());

		monster.setX(getX());
		monster.setY(getY());
		monster.setMap(getMapId());
		monster.getMoveState().setHeading(getMoveState().getHeading());
		monster.setStoreDroped(true);
		monster.setInventory(getInventory());
		setInventory(null);
		monster.setCurrentHp(getCurrentHp());
		monster.setCurrentMp(getCurrentMp());
		monster.setExp(0);
		monster.setAlignment(0);
		
		deleteMe();
		L1World world = L1World.getInstance();
		world.storeObject(monster);
		world.addVisibleObject(monster);
	}

	public void setTarget(L1Character target) {
		if (target != null && (_currentStatus == 1 || _currentStatus == 2 || _currentStatus == 5)) {
			setHate(target, 0);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	public void setMasterTarget(L1Character target) {
		if (target != null && (_currentStatus == 1 || _currentStatus == 5)) {
			setHate(target, 0);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	@Override
	public void onAction(L1PcInstance attacker) {
		if (attacker == null) {
			return;
		}
		L1Character cha = this.getMaster();
		if (cha == null) {
			return;
		}
		L1PcInstance master = (L1PcInstance) cha;
		if (master.getTeleport().isTeleport()) {
			return;
		}
		if ((getRegion() == L1RegionStatus.SAFETY || attacker.getRegion() == L1RegionStatus.SAFETY) && isExsistMaster()) {
			L1Attack attack_mortion = new L1Attack(attacker, this);
			attack_mortion.action();
			return;
		}

		if (attacker.checkNonPvP(attacker, this)) {
			return;
		}
		L1Attack attack = new L1Attack(attacker, this);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		if (isDead()) {
			return;
		}
		if (_master.equals(player)) {
			player.sendPackets(new S_PetMenuPacket(this, 0), true);
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
		int status = getActionType(action);
		if (status == 0) {
			return;
		}
		if (status == 6) {
//			if(_tamed)liberate();
//			else
			death(null);
		} else {
			Object[] petList = _master.getPetList().values().toArray();
			L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject == null) {
					continue;
				}
				if (petObject instanceof L1SummonInstance) {
					summon = (L1SummonInstance) petObject;
					summon.setCurrentStatus(status);
				}
			}
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_SummonObject(this), true);
		
		if (_master != null && perceivedFrom == _master) {
			perceivedFrom.sendPackets(new S_HPMeter(this), true);
		}
	}

	@Override
	public void onItemUse() {
		if (!_actived) {
			useItem(USEITEM_HASTE, 100);
		}
		if (getCurrentHpPercent() < 40) {
			useItem(USEITEM_HEAL, 100);
		}
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		if (getNpcTemplate().getDigestItem() > 0) {
			setDigestItem(item);
		}
		Arrays.sort(healPotions);
		Arrays.sort(haestPotions);
		if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
			if (getCurrentHp() != getMaxHp()) {
				useItem(USEITEM_HEAL, 100);
			}
		} else if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
			useItem(USEITEM_HASTE, 100);
		}
	}

	private int getActionType(String action) {
		switch(action){
		case "aggressive":	return 1;// 공격태새
		case "defensive":	return 2;// 방어태새
		case "stay":		return 3;// 휴식
		case "extend":		return 4;// 확장
		case "alert":		return 5;// 줍기
		case "dismiss":		return 6;// 해산
		default:			return 0;
		}
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}
		if (_master instanceof L1PcInstance) {
			L1PcInstance master = (L1PcInstance) _master;
			master.sendPackets(new S_HPMeter(this), true);
		}
	}

	@Override
	public void setCurrentMp(int i) {
		super.setCurrentMp(i);
		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
	}

	public void setCurrentStatus(int i) {
		_currentStatus = i;
		if (_currentStatus == 5) {
			setHomeX(getX());
			setHomeY(getY());
		}
		if (_currentStatus == 3) {
			allTargetClear();
		} else {
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	public int getCurrentStatus() {
		return _currentStatus;
	}

	public boolean isExsistMaster() {
		if (getMaster() != null && L1World.getInstance().getPlayer(getMaster().getName()) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkCondition(){
		if (_master == null) {
			return true;
		}
		if (_master instanceof L1PcInstance && ((L1PcInstance)_master).isInWarArea()) {
			//_master.getPetList().remove(getId());
			//_master = null;
			/*Object aobj[] = _master.getPetList().values().toArray();
			for (int i = 0; i < aobj.length; i++) {
				if(aobj[i] == this)((L1PcInstance) _master).sendPackets(new S_ReturnedStatPetParty(getId(), getX(), getY(), getMapId(), getNameId(), false), true);
			}*/
			((L1PcInstance) _master).sendPackets(new S_ReturnedStatPetParty(getId(), getX(), getY(), getMapId(), getDesc(), false), true);
			_master.getPetList().remove(Integer.valueOf(getId()));
			super.deleteMe();
			return true;
		}
		return false;
	}
}


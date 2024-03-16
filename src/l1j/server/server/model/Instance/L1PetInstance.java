package l1j.server.server.model.Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.GameSystem.astar.World;
import l1j.server.common.bin.CompanionCommonBinLoader;
import l1j.server.common.bin.companion.CompanionT;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.datatables.CharacterCompanionBuffTable;
import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.exp.L1ExpHandler;
import l1j.server.server.model.exp.L1ExpPet;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Alignment;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.companion.S_CompanionBuffNoti;
import l1j.server.server.serverpackets.companion.S_CompanionNameChange;
import l1j.server.server.serverpackets.companion.S_CompanionSkillNoti;
import l1j.server.server.serverpackets.companion.S_CompanionStatusNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.object.S_CompanionObject;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.templates.L1CompanionStatBonus;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.utils.CommonUtil;

public class L1PetInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	private L1PcInstance _master;
	private L1Pet _pet;
	private L1ItemInstance _amulet;
	private L1GroundInventory _groundInven;
	private List<L1GroundInventory> _cancelItemList;
	protected List<L1ItemInstance> _targetItemList = new ArrayList<L1ItemInstance>();
	protected L1ItemInstance _targetItem = null;
	private ScheduledFuture<?> _hpFuture;
	private int _amuletId;
	private CompanionT.eCommand _command;
	private boolean _remove;
	
	private HashMap<Integer, CompanionT.WildSkillT.SkillT.EnchantBonusT> skill_bonus;
	private L1CompanionStatBonus stat_bonus;
	
	private int _comboDmgMulti = 10;
	private int _bloodSuckHit;
	private int _bloodSuckHeal;
	private int _regenHP = 5;
	private int _add_min_HP = 5;
	private int _add_max_HP = 8;
	private int _potionHP;
	private double _movedelay_reduce;
	private double _attackdelay_reduce;
	private int _fireElementalDmg;
	private int _waterElementalDmg;
	private int _airElementalDmg;
	private int _earthElementalDmg;
	private int _lightElementalDmg;
	private int _pvp_dmg_ratio;
	private int _pvp_melee_defense;
	
	private double _bonus_exp_rate = 1.0D;
	private double _bonus_friendship_rate = 1.0D;
	private int _comboCount;
	private boolean _combo;
	private int _comboTarget;
	private boolean _minus_exp_penalty;
	private final L1ExpHandler _expHandler;
	
	public void send_companion_card() {
		_amulet.update_companion_card_cache(_pet);
		_master.sendPackets(_amulet.get_companion_card_cache());
	}

	public L1PetInstance(L1Npc template, L1PcInstance master, L1Pet pet) {
		super(template);
		_master			= master;
		_pet			= pet;
		_amuletId		= _pet.getItemObjId();
		_amulet			= _master.getInventory().getItem(_amuletId);
		_cancelItemList = new FastTable<L1GroundInventory>();
		_expHandler		= new L1ExpPet(this);
		
		setId(_pet.getObjId());
		setName(_pet.getName());
		setLevel(_pet.getLevel());
		setMaxHp(_pet.getMaxHp());
		super.setCurrentHp(_pet.isDead() ? _pet.getMaxHp() >> 1 : _pet.getCurrentHp());
		setExp(_pet.getExp());
		_master.addPet(this);
		setMaster(_master);
		setX(_master.getX() + CommonUtil.random(5) - 2);
		setY(_master.getY() + CommonUtil.random(5) - 2);
		setMap(_master.getMapId());
		getMoveState().setHeading(0);
		ability.addAddedStr(_pet.getAddStr());
		ability.addAddedCon(_pet.getAddCon());
		ability.addAddedInt(_pet.getAddInt());
		
		_pet.setDead(false);
		_pet.set_summoned(true);
		_master.sendPackets(new S_ServerMessage(5274, getName()), true);

		CharacterCompanionBuffTable.LoadBuff(this);

		L1World world = L1World.getInstance();
		world.storeObject(this);
		world.addVisibleObject(this);
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			onPerceive(pc);
			pc.sendPackets(new S_EffectLocation(getLocation(), 5935), true);
		}
		
		// 마이너스 경험치
		if (pet.getExp() < ExpTable.getExpByLevel(pet.getLevel())){
			_minus_exp_penalty = true;
			_master.sendPackets(new S_ServerMessage(5370), true);
		}

		set_command(CompanionT.eCommand.TM_Aggressive);
		doStatBonus(true);
		loadSkill();
		send_companion_card();
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		_actived = false;
		startAI();
	}

	@Override
	protected boolean AIProcess() {
		setSleepTime(300);
		if (masterRangeCheck()) {
			return false;
		}
		if (_command == CompanionT.eCommand.TM_GetItem) {
			checkTarget();
			if (_groundInven == null) {
				return noTarget();
			}
			onTarget();
			return false;
		}
		if (_command == CompanionT.eCommand.TM_Defensive || _target == null) {
			return noTarget();
		}
		onTarget();
		return false;
	}

	boolean masterRangeCheck() {
		if (_master == null) {
			return false;
		}
		if (_master.getMapId() != getMapId()) {
			teleport(_master.getX(), _master.getY(), getMoveState().getHeading());
			return true;
		}
		int distance = getLocation().getTileLineDistance(_master.getLocation());
		if (distance > 20) {
			teleport(_master.getX(), _master.getY(), getMoveState().getHeading());
			return true;
		}
		return false;
	}
	
	void on_getItem() {
		L1GroundInventory inv = _groundInven;
		if (inv == null) {
			_groundInven = null;
			return;
		}
		for (L1ItemInstance item : inv.getItems()) {
			if (item.getItemOwner() != null && item.getItemOwner().getId() != _master.getId()) {
				_cancelItemList.add(_groundInven);
				_groundInven = null;
				return;
			}
		}

		if (!isParalyzed() && !isSleeped()) {
			if (getLocation().getTileLineDistance(inv.getLocation()) == 0) {
				for (L1ItemInstance item : inv.getItems()) {
					if (_master.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
						inv.tradeItem(item, item.getCount(), _master.getInventory());
						_master.saveInventory();
						setSleepTime(1000);
					} else {
						_groundInven = null;
					}
				}
				if (inv == null || inv.getSize() == 0) {
					_groundInven = null;
				}
				setSleepTime(1200);
			} else {
				int dir = moveDirection(inv.getMapId(), inv.getX(), inv.getY());
				if (dir == -1) {
					_cancelItemList.add(_groundInven);
					_groundInven = null;
					setSleepTime(1000);
				} else {
					boolean door = World.isDoorMove(getX(), getY(), getMapId(), calcheading(this, inv.getX(), inv.getY()));
					boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir);
					if (door || !tail) {
						cnt++;
						if (cnt > 5) {
							_cancelItemList.add(_groundInven);
							_groundInven = null;
							cnt = 0;
						}
						return;
					}
					setDirectionMove(dir);
					setSleepTime(calcSleepTime(getPassispeed(), 0));
				}
			}
		} else {
			setSleepTime(500);
		}
	}

	@Override
	public void onTarget() {
		if (_command == CompanionT.eCommand.TM_GetItem) {
			on_getItem();
			return;
		}
		super.onTarget();
		if (_target != null && _target.isDead()) {
			_target = null;
		}
	}

	@Override
	public int calcSleepTime(int sleepTime, int type) {
		if (_combo) {
			sleepTime -= (sleepTime * Config.COMPANION.COMBO_DELAY_REDUCE_RATE * 0.01);
		} else {
			switch (type) {
			case MOVE_SPEED:
				if (_movedelay_reduce > 0) {
					sleepTime -= (sleepTime * _movedelay_reduce * 0.01);
				}
				break;
			case ATTACK_SPEED:
				if (_attackdelay_reduce > 0) {
					sleepTime -= (sleepTime * _attackdelay_reduce * 0.01);
				}
				break;
			}
		}
		return _minus_exp_penalty ? sleepTime << 1 : sleepTime;
	}

	@Override
	public void setHate(L1Character cha, int hate) {
	}

	@Override
	public void checkTarget() {
		if (_groundInven == null) {
			int minDistance = 32767;
			int distance = 0;
			L1GroundInventory inventory = null;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
				if (obj != null && obj instanceof L1GroundInventory) {
					L1GroundInventory target = (L1GroundInventory) obj;
					if (_cancelItemList.contains(target)) {
						continue;
					}
					distance = getLocation().getTileLineDistance(target.getLocation());
					if (minDistance <= distance) {
						continue;
					}
					minDistance = distance;
					inventory = target;
				}
			}
			if (inventory != null) {
				_groundInven = inventory;
			}
		}
	}

	@Override
	public void setTarget(L1Character target) {
		if (target != null && _command == CompanionT.eCommand.TM_Aggressive) {
			_target = target;
		}
	}

	@Override
	public L1Character getTarget() {
		return _target;
	}

	@Override
	public void allTargetClear() {
		_target = null;
		_groundInven = null;
		_cancelItemList.clear();
	}

	@Override
	public boolean noTarget() {
		if (_master == null) {
			return false;
		}
		if (_master.getMapId() == getMapId()) {
			int distance = getLocation().getTileLineDistance(_master.getLocation());
			if (distance > 20) {
				teleport(_master.getX(), _master.getY(), getMoveState().getHeading());
			} else if (distance > 2) {
				int dir = moveDirection(_master.getMapId(), _master.getX(), _master.getY());
				boolean door = World.isDoorMove(getX(), getY(), getMapId(), calcheading(this, _master.getX(), _master.getY()));
				boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir);
				if (door || !tail) {
					cnt++;
					if (cnt > 5) {
						teleport(_master.getX(), _master.getY(), getMoveState().getHeading());
						cnt = 0;
					}
					return false;
				}
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), 0));
			} else {
				setSleepTime(1000);
			}
		} else {
			teleport(_master.getX(), _master.getY(), getMoveState().getHeading());
		}
		return false;
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (getCurrentHp() > 0) {
			if (damage > 0) {
				removeSleepSkill();
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
		if (isDead()) {
			return;
		}
		setCurrentHp(0);
		setDead(true);
		do_exp_penalty();
		if (!_master.isPinkName() && lastAttacker instanceof L1PcInstance) {
			L1PcInstance player = (L1PcInstance) lastAttacker;
			if (getAlignment() >= 0 && !player.isPinkName()) {
				int align = player.getLevel() < 50 ? -1 * (int) (Math.pow(player.getLevel(), 2.0) * 4.0)
						: -1 * (int) (Math.pow(player.getLevel(), 3.0) * 0.08);
				if (player.getAlignment() - 1000 < align) {
					align = player.getAlignment() - 1000;
				}
				if (align <= -32768) {
					align = -32768;
				}
				player.setAlignment(align);
				player.broadcastPacketWithMe(new S_Alignment(player.getId(), player.getAlignment()), true);
			}
		}

		setActionStatus(ActionCodes.ACTION_Die);
		getMap().setPassable(getLocation(), true);
		broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
		_pet.setDead(true);
		send_companion_card();
		_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.DEAD, this), true);
		startDeleteTimer();
	}

	public void addExp(int exp) {
		setExp(getExp() + exp);
	}

	public void setExp(int exp) {
		super.setExp(exp);
		if (_pet != null) {
			_pet.setExp(exp);
		}
	}
	
	void do_exp_penalty() {
		if (getRegion() == L1RegionStatus.SAFETY) {
			return;
		}
		if (_master.getInventory().checkItem(3200014, 1)) { // 대자연의 가호
			_master.getInventory().consumeItem(3200014, 1);
			_master.sendPackets(new S_ServerMessage(5279), true);
			return;
		}
		int level = getLevel();
		if (level > 30) {
			int exp = (int) calc_exp_penalty(level);
			if (exp > 0) {
				addExp(-exp);
			}
			_pet.setLessExp(_pet.getLessExp() + exp);
		} else {
			_pet.setLessExp(0);
		}
		doStatBonus(true);
		CharacterCompanionTable.getInstance().LessExpSave(_pet);
	}

	double calc_exp_penalty(int level) {
		long needExp = ExpTable.getNeedExpNextLevel(level);
		if (level <= 30) {
			return 0;
		}
		if (level <= 44) {
			return (double) (needExp * 0.1);
		}
		if (level == 45) {
			return (double) (needExp * 0.09);
		}
		if (level == 46) {
			return (double) (needExp * 0.08);
		}
		if (level == 47) {
			return (double) (needExp * 0.07);
		}
		if (level == 48) {
			return (double) (needExp * 0.06);
		}
		if (level == 49) {
			return (double) (needExp * 0.05);
		}
		if (level <= 64) {
			return (double) (needExp * 0.045);
		}
		if (level <= 69) {
			return (double) (needExp * 0.04);
		}
		if (level <= 74) {
			return (double) (needExp * 0.035);
		}
		if (level <= 78) {
			return (double) (needExp * 0.03);
		}
		if (level == 79) {
			return (double) (needExp * 0.025);
		}
		if (level == 80) {
			return (double) (needExp * 0.02);
		}
		return (double) (needExp * 0.01);
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_CompanionObject(this), true);
		if (isDead()) {
			perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
		}
		if (_master != null && _master == perceivedFrom) {
			perceivedFrom.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.ALL, this), true);
		}
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Character cha = getMaster();
		L1PcInstance master = (L1PcInstance) cha;
		if (master == null || master.getTeleport().isTeleport()) {
			return;
		}
		if (getRegion() == L1RegionStatus.SAFETY) {
			L1Attack attack_mortion = new L1Attack(player, this);
			attack_mortion.action();
			return;
		}
		if (player.checkNonPvP(player, this)) {
			return;
		}
		L1Attack attack = new L1Attack(player, this);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
		if (getMaxHp() > getCurrentHp()) {
			regenHpStart();
		} else {
			regenHpStop();
		}
		if (_master != null) {
			_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.HP, this), true);
		}
	}

	@Override
	public boolean checkCondition() {
		if (_master == null) {
			return true;
		}
		if (_master.isInWarArea()) {
			delete();
			return true;
		}
		return false;
	}

	public static void summoned(L1PcInstance pc, L1Pet info) {
		if (!pc.getInventory().consumeItem(L1ItemId.GEMSTONE, info.isDead() ? 1000 : 50)) {
			pc.sendPackets(new S_ServerMessage(5275), true);
		}
		L1Npc npcTemp = NpcTable.getInstance().getTemplate(info.getNpcId());
		if (npcTemp == null) {
			return;
		}
		new L1PetInstance(npcTemp, pc, info).onNpcAI();;
	}

	public synchronized void delete() {
		if (!_remove) {
			_remove = true;
			_pet.set_summoned(false);
			if (stat_bonus != null) {
				doStatBonus(stat_bonus, false);
			}
			if (skill_bonus != null) {
				for (CompanionT.WildSkillT.SkillT.EnchantBonusT skillBonus : skill_bonus.values()) {
					doSkillEnchantBonus(skillBonus, false);
				}
			}
			broadcastPacket(new S_EffectLocation(getLocation(), 5936), true);
			CharacterCompanionTable.getInstance().saveAll(this);
			if (_master != null) {
				CharacterCompanionBuffTable.SaveBuff(this);
				send_companion_card();
				_master.getPetList().remove(getId());
				_master = null;
			}
		}
		super.deleteMe();
	}

	@Override
	public void deleteMe() {
		delete();
	}

	public void changeName(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		L1Npc npc = _npcTemplate;
		//if (npc.getDescKr().equals(name) || CharacterCompanionTable.isNameExists(name)) {
		if (npc.getDescEn().equals(name) || CharacterCompanionTable.isNameExists(name)) {
			_master.sendPackets(new S_CompanionNameChange(S_CompanionNameChange.eResult.SameNameExisted, name.getBytes()), true);
			return;
		}
		if (!_master.getInventory().consumeItem(3200013, 1)) {
			return;
		}
		setName(name);
		_pet.setName(name);
		CharacterCompanionTable.getInstance().ChangeName(_pet);
		broadcastPacket(new S_Polymorph(getId(), this.getSpriteId(), 0, name, _pet.getNpc().getClassId()), true);
		_master.sendPackets(new S_ServerMessage(1322, name), true);
		send_companion_card();
		_master.sendPackets(new S_CompanionNameChange(S_CompanionNameChange.eResult.Success, name.getBytes()), true);
		_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.NAME_CHANGE, this), true);
	}

	public synchronized void regenHpStart() {
		if (_hpFuture != null) {
			return;
		}
		_hpFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (_master == null || _destroyed || isDead()
						|| getCurrentHp() == 0
						|| getCurrentHp() >= getMaxHp()) {
					regenHpStop();
				} else {
					setCurrentHp(getCurrentHp() + _regenHP);
				}
			}
		}, 10000L, 10000L);
	}

	public synchronized void regenHpStop() {
		if (_hpFuture == null) {
			return;
		}
		_hpFuture.cancel(false);
		_hpFuture = null;
	}
	
	/**
	 * 스탯 보너스 설정
	 * @param bonus
	 * @param flag
	 */
	void doStatBonus(L1CompanionStatBonus bonus, boolean flag) {
		int flag_val	= flag ? 1 : -1;
		int meleeDmg	= bonus.get_meleeDmg();
		int meleeHit	= bonus.get_meleeHit();
		int meleeCri	= bonus.get_meleeCri();
		int regenHP		= bonus.get_regenHP();
		int add_min_HP	= bonus.get_add_min_HP();
		int add_max_HP	= bonus.get_add_max_HP();
		int AC			= bonus.get_AC();
		int mr			= bonus.get_mr();
		int reduction	= bonus.get_reduction();
		int spellDmg	= bonus.get_spellDmg();
		int spellHit	= bonus.get_spellHit();
		int spellCri	= bonus.get_spellCri();
		if (meleeDmg != 0) {
			ability.addShortDmgup(meleeDmg * flag_val);
		}
		if (meleeHit != 0) {
			ability.addShortHitup(meleeHit * flag_val);
		}
		if (meleeCri != 0) {
			ability.addShortCritical(meleeCri * flag_val);
		}
		if (regenHP != 0) {
			_regenHP += regenHP * flag_val;
		}
		if (add_min_HP != 0) {
			_add_min_HP += add_min_HP * flag_val;
		}
		if (add_max_HP != 0) {
			_add_max_HP += add_max_HP * flag_val;
		}
		if (AC != 0) {
			ac.addAc(AC * flag_val);
		}
		if (mr != 0) {
			resistance.addMr(mr * flag_val);
		}
		if (reduction != 0) {
			ability.addDamageReduction(reduction * flag_val);
		}
		if (spellDmg != 0) {
			ability.addMagicDmgup(spellDmg * flag_val);
		}
		if (spellHit != 0) {
			ability.addMagicHitup(spellHit * flag_val);
		}
		if (spellCri != 0) {
			ability.addMagicCritical(spellCri * flag_val);
		}
	}

	/**
	 * 스탯 보너스 설정
	 * @param sendPacket
	 */
	public void doStatBonus(boolean sendPacket) {
		if (stat_bonus != null) {
			doStatBonus(stat_bonus, false);
			stat_bonus.reset();
		} else {
			stat_bonus = new L1CompanionStatBonus();
		}
		stat_bonus.set(this);
		doStatBonus(stat_bonus, true);
		if (sendPacket && _master != null) {
			_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.STAT, this), true);
		}
	}

	/**
	 * 스탯 초기화
	 */
	public void resetStat() {
		int remain_stats = _pet.getAddStr() + _pet.getAddCon() + _pet.getAddInt() + _pet.get_remain_stats();
		ability.addAddedStr(-_pet.getAddStr());
		ability.addAddedCon(-_pet.getAddCon());
		ability.addAddedInt(-_pet.getAddInt());
		_pet.setAddStr(0);
		_pet.setAddCon(0);
		_pet.setAddInt(0);
		_pet.set_remain_stats(remain_stats);
		doStatBonus(true);
		CharacterCompanionTable.getInstance().saveAll(this);
	}

	/**
	 * 경험치 복구
	 */
	public void repairExp() {
		long totalExp = (long)(_pet.getLessExp() + getExp());
		if (totalExp < 1) {
			return;
		}
		if (totalExp >= ExpTable.getExpByLevel(81)) {
			totalExp = (int) (ExpTable.getExpByLevel(81) - 1);
		}
		setExp(totalExp);
		_minus_exp_penalty = false;
		_pet.setExp(getExp());
		_pet.setLessExp(0);
		if (_master != null) {
			_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.EXP, this), true);
		}
		CharacterCompanionTable.getInstance().LessExpSave(_pet);
	}

	/**
	 * 망각
	 */
	public void oblivion() {
		_pet.setOblivion(true);
		_pet.set_friend_ship_marble(0);
		_pet.set_friend_ship_guage(0);
		_pet.newWild();
		delete();
	}

	/**
	 * 야성 스킬 로드
	 */
	void loadSkill() {
		for (int id : _pet.getWild().keySet()) {
			doSkillEnchantBonus(id, _pet.getWild().get(id));
		}
		_master.sendPackets(new S_CompanionSkillNoti(_pet.getWild()), true);
		_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.ALL, this), true);
	}

	/**
	 * 야성 스킬 업데이트
	 * @param id
	 * @param enchant
	 */
	public void updateSkill(int id, int enchant) {
		doSkillEnchantBonus(id, enchant);
		_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.ALL, this), true);
	}
	
	/**
	 * 야성 보너스 설정
	 * @param bonusT
	 * @param flag
	 */
	void doSkillEnchantBonus(CompanionT.WildSkillT.SkillT.EnchantBonusT bonusT, boolean flag) {
		int flag_val				= flag ? 1 : -1;
		int meleeDmg				= bonusT.get_meleeDmg();
		int meleeHit				= bonusT.get_meleeHit();
		double meleeCriHit			= bonusT.get_meleeCriHit();
		int ignoreReduction			= bonusT.get_ignoreReduction();
		double bloodSuckHit			= bonusT.get_bloodSuckHit();
		int bloodSuckHeal			= bonusT.get_bloodSuckHeal();
		int regenHP					= bonusT.get_regenHP();
		int AC						= bonusT.get_AC();
		int MR						= bonusT.get_MR();
		int potionHP				= bonusT.get_potionHP();
		int dmgReduction			= bonusT.get_dmgReduction();
		int maxHP					= bonusT.get_maxHP();
		int spellDmgMulti			= bonusT.get_spellDmgMulti();
		int spellHit				= bonusT.get_spellHit();
		double moveDelayReduce		= bonusT.get_moveDelayReduce();
		double attackDelayReduce	= bonusT.get_attackDelayReduce();
		int fireElementalDmg		= bonusT.get_fireElementalDmg();
		int waterElementalDmg		= bonusT.get_waterElementalDmg();
		int airElementalDmg			= bonusT.get_airElementalDmg();
		int earthElementalDmg		= bonusT.get_earthElementalDmg();
		int lightElementalDmg		= bonusT.get_lightElementalDmg();
		int comboDmgMulti			= bonusT.get_comboDmgMulti();
		int spellDmgAdd				= bonusT.get_spellDmgAdd();
		
		if (meleeDmg != 0) {
			ability.addShortDmgup(meleeDmg * flag_val);
		}
		if (meleeHit != 0) {
			ability.addShortHitup(meleeHit * flag_val);
		}
		if (meleeCriHit != 0) {
			ability.addShortCritical((int)meleeCriHit * flag_val);
		}
		if (ignoreReduction != 0) {
			ability.addDamageReductionEgnor(ignoreReduction * flag_val);
		}
		if (bloodSuckHit != 0) {
			_bloodSuckHit += (int)bloodSuckHit * flag_val;
		}
		if (bloodSuckHeal != 0) {
			_bloodSuckHeal += bloodSuckHeal * flag_val;
		}
		if (regenHP != 0) {
			_regenHP += regenHP * flag_val;
		}
		if (AC != 0) {
			ac.addAc(AC * flag_val);
		}
		if (MR != 0) {
			resistance.addMr(MR * flag_val);
		}
		if (potionHP != 0) {
			_potionHP += potionHP * flag_val;
		}
		if (dmgReduction != 0) {
			ability.addDamageReduction(dmgReduction * flag_val);
		}
		if (maxHP != 0) {
			addMaxHp(maxHP * flag_val);
		}
		if (spellDmgMulti != 0) {
			ability.addMagicDmgup(spellDmgMulti * flag_val);
		}
		if (spellHit != 0) {
			ability.addMagicHitup(spellHit * flag_val);
		}
		if (moveDelayReduce != 0) {
			_movedelay_reduce += moveDelayReduce * flag_val;
		}
		if (attackDelayReduce != 0) {
			_attackdelay_reduce += attackDelayReduce * flag_val;
		}
		if (fireElementalDmg != 0) {
			_fireElementalDmg += fireElementalDmg * flag_val;
		}
		if (waterElementalDmg != 0) {
			_waterElementalDmg += waterElementalDmg * flag_val;
		}
		if (airElementalDmg != 0) {
			_airElementalDmg += waterElementalDmg * flag_val;
		}
		if (earthElementalDmg != 0) {
			_earthElementalDmg += earthElementalDmg * flag_val;
		}
		if (lightElementalDmg != 0) {
			_lightElementalDmg += lightElementalDmg * flag_val;
		}
		if (comboDmgMulti != 0) {
			_comboDmgMulti += comboDmgMulti * flag_val;
		}
		if (spellDmgAdd != 0) {
			ability.addMagicDmgup(spellDmgAdd * flag_val);
		}
	}

	/**
	 * 야성 보너스 업데이트
	 * @param id
	 * @param enchant
	 */
	void doSkillEnchantBonus(int id, int enchant) {
		if (enchant <= 0) {
			return;
		}
		if (skill_bonus == null) {
			skill_bonus = new HashMap<Integer, CompanionT.WildSkillT.SkillT.EnchantBonusT>();
		}
		CompanionT.WildSkillT.SkillT.EnchantBonusT bonusT = skill_bonus.get(id);
		if (bonusT != null) {
			doSkillEnchantBonus(bonusT, false);// 기존 옵션 제거
		}
		bonusT = CompanionCommonBinLoader.getSkillT(id).get_EnchantBonus().get(enchant - 1);
		skill_bonus.put(id, bonusT);
		doSkillEnchantBonus(bonusT, true);// 새로운 옵션 추가
	}

	/**
	 * 콤보 처리
	 */
	public synchronized void startCombo() {
		if (_combo) {
			return;
		}
		_combo = true;
		_comboTarget = _comboCount = 0;
		if (_master != null) {
			_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.COMBO_TIME, this), true);
		}
		broadcastPacket(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.DELAY_REDUCE, this), true);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				_combo = false;
				if (_master != null) {
					_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.COMBO_TIME, L1PetInstance.this), true);
				}
				L1PetInstance.this.broadcastPacket(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.DELAY_REDUCE, L1PetInstance.this), true);
			}
		}, Config.COMPANION.COMBO_DURATION_SECOND * 1000L);
	}
	
	public void doBuffBonus(int buff_id, int duration, boolean flag) {
		int flag_val = flag ? 1 : -1;
		if (flag) {
			if (buff_id == L1SkillId.PET_BUFF_GROW || buff_id == L1SkillId.PET_BUFF_EIN || buff_id == L1SkillId.PET_BUFF_SKY) {
				if (getSkill().hasSkillEffect(L1SkillId.PET_BUFF_GROW)) {
					getSkill().removeSkillEffect(L1SkillId.PET_BUFF_GROW);
	    		} else if (getSkill().hasSkillEffect(L1SkillId.PET_BUFF_EIN)) {
	    			getSkill().removeSkillEffect(L1SkillId.PET_BUFF_EIN);
	    		} else if (getSkill().hasSkillEffect(L1SkillId.PET_BUFF_SKY)) {
	    			getSkill().removeSkillEffect(L1SkillId.PET_BUFF_SKY);
	    		}
				broadcastPacket(new S_Effect(getId(), 16551), true);
			} else if(buff_id == L1SkillId.PET_BUFF_YEGABAM) {
				broadcastPacket(new S_Effect(getId(), 7382), true);
			}
			getSkill().setSkillEffect(buff_id, duration * 1000);
		}
		switch (buff_id) {
		case L1SkillId.PET_BUFF_GROW:
			_bonus_exp_rate += 0.3D * flag_val;
			break;
		case L1SkillId.PET_BUFF_EIN:
			_bonus_exp_rate += 0.5D * flag_val;
			break;
		case L1SkillId.PET_BUFF_SKY:
			_bonus_exp_rate += 1D * flag_val;
			break;
		case L1SkillId.PET_BUFF_YEGABAM:
			_bonus_friendship_rate += 0.3D * flag_val;
			break;
		case L1SkillId.PET_BUFF_BLOOD:
			_combo = flag;
			_pvp_dmg_ratio += 10 * flag_val;
			_pvp_melee_defense += -2 * flag_val;
			_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.COMBO_TIME, this), true);
			break;
		}
		_master.sendPackets(new S_CompanionBuffNoti(buff_id, duration), true);
	}

	public L1Pet getPetInfo() {
		return _pet;
	}

	public L1ItemInstance getAmulet() {
		return _amulet;
	}

	@Override
	public L1PcInstance getMaster() {
		return _master;
	}

	public void set_command(CompanionT.eCommand val) {
		_command = val;
	}

	public int getAmuletId() {
		return _amuletId;
	}

	public Map<Integer, Integer> getWild() {
		return _pet.getWild();
	}

	public int get_friend_ship_marble() {
		return _pet.get_friend_ship_marble();
	}

	public void add_friend_ship_marble(int i) {
		set_friend_ship_marble(get_friend_ship_marble() + i);
	}

	public void set_friend_ship_marble(int i) {
		_pet.set_friend_ship_marble(i);
		if (_master != null) {
			_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.FRIENDSHIP, this), true);
		}
	}

	public int get_friend_ship_guage() {
		return _pet.get_friend_ship_guage();
	}

	public void add_friend_ship_guage(int i) {
		set_friend_ship_guage(get_friend_ship_guage() + i);
	}

	public void set_friend_ship_guage(int i) {
		_pet.set_friend_ship_guage(i);
		if (get_friend_ship_guage() >= Config.COMPANION.COMBO_ENABLE_POINT) {
			_pet.set_friend_ship_guage(0);
			add_friend_ship_marble(Config.COMPANION.FRIENDSHIP_MARBLE_VALUE);
			startCombo();
		} else {
			if (_master != null) {
				_master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.FRIENDSHIP_GUAGE, this), true);
			}
		}
	}

	public int get_comboDmgMulti() {
		return _comboDmgMulti;
	}

	public void set_comboDmgMulti(int i) {
		_comboDmgMulti = i;
	}

	public int get_bloodSuckHit() {
		return _bloodSuckHit;
	}

	public void set_bloodSuckHit(int i) {
		_bloodSuckHit = i;
	}
	
	public int get_bloodSuckHeal() {
		return _bloodSuckHeal;
	}

	public void set_bloodSuckHeal(int i) {
		_bloodSuckHeal = i;
	}

	public int get_regenHP() {
		return _regenHP;
	}

	public void set_regenHP(int i) {
		_regenHP = i;
	}
	
	public int get_add_min_HP() {
		return _add_min_HP;
	}
	
	public int get_add_max_HP() {
		return _add_max_HP;
	}

	public int get_potionHP() {
		return _potionHP;
	}

	public void set_potionHP(int i) {
		_potionHP = i;
	}

	public double get_movedelay_reduce() {
		return _movedelay_reduce;
	}

	public void set_movedelay_reduce(double i) {
		_movedelay_reduce = i;
	}

	public double get_attackdelay_reduce() {
		return _attackdelay_reduce;
	}

	public void set_attackdelay_reduce(double i) {
		_attackdelay_reduce = i;
	}
	
	public int get_fireElementalDmg() {
		return _fireElementalDmg;
	}
	
	public int get_waterElementalDmg() {
		return _waterElementalDmg;
	}
	
	public int get_airElementalDmg() {
		return _airElementalDmg;
	}
	
	public int get_earthElementalDmg() {
		return _earthElementalDmg;
	}

	public int get_lightElementalDmg() {
		return _lightElementalDmg;
	}
	
	public int get_pvp_dmg_ratio() {
		return _pvp_dmg_ratio;
	}
	
	public int get_pvp_melee_defense() {
		return _pvp_melee_defense;
	}
	
	public double get_bonus_exp_rate() {
		return _bonus_exp_rate;
	}
	
	public double get_bonus_friendship_rate() {
		return _bonus_friendship_rate;
	}
	
	public int getComboCount() {
		return _comboCount;
	}

	public void setComboCount(int i) {
		_comboCount = i;
	}

	public boolean isCombo() {
		return _combo;
	}

	public void setCombo(boolean f) {
		_combo = f;
	}

	public int getComboTarget() {
		return _comboTarget;
	}

	public void setComboTarget(int i) {
		_comboTarget = i;
	}

	public boolean is_minus_exp_penalty() {
		return _minus_exp_penalty;
	}

	public L1ExpHandler getExpHandler() {
		return _expHandler;
	}
}

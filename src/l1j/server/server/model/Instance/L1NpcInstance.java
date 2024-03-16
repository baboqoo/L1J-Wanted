package l1j.server.server.model.Instance;

import static l1j.server.server.model.item.L1ItemId.B_POTION_OF_GREATER_HASTE_SELF;
import static l1j.server.server.model.item.L1ItemId.B_POTION_OF_HASTE_SELF;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_EXTRA_HEALING;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_GREATER_HASTE_SELF;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_GREATER_HEALING;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_HASTE_SELF;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_HEALING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.astar.AStar;
import l1j.server.GameSystem.astar.Node;
import l1j.server.GameSystem.astar.World;
import l1j.server.common.bin.npc.CommonNPCInfo;
import l1j.server.common.data.ElementalResistance;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.item.L1ItemNormalType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.datatables.NpcChatTable;
import l1j.server.server.datatables.NpcInfoTable.NpcInfoData;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1HateList;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1MobGroupInfo;
import l1j.server.server.model.L1MobSkillUse;
import l1j.server.server.model.L1NpcChatTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.S_Effect;
//import l1j.server.server.serverpackets.S_Door;
import l1j.server.server.serverpackets.S_ObjectExplosion;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_MoveCharPacket;
import l1j.server.server.serverpackets.message.S_Notification;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.templates.L1Notification;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1NpcChat;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcDexStat;
import l1j.server.server.utils.CalcIntelStat;
import l1j.server.server.utils.CalcStrStat;

public class L1NpcInstance extends L1Character {
	private static Logger _log = Logger.getLogger(L1NpcInstance.class.getName());
	private static final Random random = new Random(System.nanoTime());
	
	private static final long serialVersionUID				= 1L;
	
	private static final byte[] HEADING_TABLE_X				= { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte[] HEADING_TABLE_Y				= { -1, -1, 0, 1, 1, 1, 0, -1 };
	
	protected static final int DISTANCE_RANGE_VALUE			= 30;

	public static final int MOVE_SPEED						= 0;
	public static final int ATTACK_SPEED					= 1;
	public static final int MAGIC_SPEED						= 2;

	public static final int HIDDEN_STATUS_NONE				= 0;
	public static final int HIDDEN_STATUS_SINK				= 1;
	public static final int HIDDEN_STATUS_FLY				= 2;
	public static final int HIDDEN_STATUS_ANCIENTGUARDIAN	= 3;

	public static final int CHAT_TIMING_APPEARANCE			= 0;
	public static final int CHAT_TIMING_DEAD				= 1;
	public static final int CHAT_TIMING_HIDE				= 2;
	public static final int CHAT_TIMING_GAME_TIME			= 3;
	
	public long NpcDeleteTime;
	private static final long DELETE_TIME = 10000L;

	protected L1Npc _npcTemplate;
	private L1Spawn _spawn;
	private int _spawnNumber;
	private int _petcost;
	
	public boolean _statusEscape;
	public L1Location _locEscape;

	protected L1Inventory _inventory = new L1Inventory();
	protected L1MobSkillUse mobSkill;

	private boolean firstFound = true;
	private static int courceRange = DISTANCE_RANGE_VALUE;
	private int _drainedMana;

	private boolean _rest;
	private boolean _isResurrect;

	private int _randomMoveDistance;

	protected boolean isTeleportDmgAction;
	private boolean _aiRunning; 
	protected boolean _actived; 
	private boolean _firstAttack; 
	private int _sleep_time; 
	protected L1HateList _hateList		= new L1HateList();
	protected L1HateList _dropHateList	= new L1HateList();
	protected List<L1ItemInstance> _targetItemList = new ArrayList<L1ItemInstance>();
	protected L1Character _target; 
	protected L1ItemInstance _targetItem; 
	protected L1Character _master;
	public L1Character _backtarget;
	private boolean _deathProcessing; 
	private L1MobGroupInfo _mobGroupInfo;
	private int _mobGroupId;
	private int num;	/*버경 관련*/
	
	private NpcDeleteTimer _deleteTask;
	private ScheduledFuture<?> _future;
	
	private Map<Integer, Integer> _digestItems;
	public boolean _digestItemRunning;

	protected AStar aStar;// 길찾기 변수
	private int[][] iPath;// 길찾기 변수
	private Node tail;// 길찾기 변수
	private int iCurrentPath;// 길찾기 변수
	
	protected short cnt, cnt2, cnt3, cnt4;// 이동 불가 카운팅
	
	public boolean isEnableHiddenStatus() {
		return _hiddenStatus == HIDDEN_STATUS_NONE || _hiddenStatus == HIDDEN_STATUS_ANCIENTGUARDIAN;
	}
	
	private boolean _hold;
	public boolean isHold() {
		return _hold;
	}
	public void setHold(boolean flag) {
		_hold = flag;
	}
	
	private String spawnLocation;
	public String getSpawnLocation(){
		return spawnLocation;
	}
	public void setSpawnLocation(String st){
		spawnLocation = st;
	}
	
	protected NpcInfoData _info;
	public NpcInfoData getInfo(){
		return _info;
	}

	public L1NpcInstance(L1Npc template) {
		super();
		setActionStatus(0); 
		getMoveState().setMoveSpeed(0);
		setDead(false);
		setRespawn(false);
		if (template != null) {
			_info = template.getInfo();
			if (template.getPassiSpeed() > 0) {// 이동하는 엔피씨에만 길찾기 세팅
				iPath = new int[51][2];// 길찾기 배열 50 적당(높이면 지능이 더 좋아지지만 메모리 증가됨)
				aStar = new AStar();
				aStar.setCha(this);
			}
			setting_template(template);
		}
	}
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		/*if (_npcTemplate.getNpcId() == 900168) {// 보스방 후문 문
			perceivedFrom.sendPackets(new S_Door(getX(), getY(), 0, PASS), true);// 하딘  시스템
		}*/
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
		onNpcAI();
	}
	
	private double calcRandomVal(int seed, int ranval, double rate) {
		return rate * ( ranval - seed );
	}

	protected void setting_template(L1Npc template) {
		_npcTemplate = template;
		double rate = 0; 
		int diff = 0;

		//setName(template.getDescKr());
		setName(template.getDesc());
		setKoreanName(template.getDescKr());
		setDesc(template.getDesc());

		int randomlevel = 0;
		int level = template.getLevel();
		if (template.getRandomLevel() != 0) {			
			randomlevel = random.nextInt(template.getRandomLevel() - level + 1);
			diff = template.getRandomLevel() - level;
			rate = randomlevel / diff;
			randomlevel += template.getLevel();
			level = randomlevel;
		}
		setLevel(level);

		int hp = template.getHp();
		if (template.getRandomHp() != 0) {
			hp = (int) (hp + calcRandomVal(hp, template.getRandomHp(), rate));
		}
		setMaxHp(hp);
		setCurrentHp(hp);

		int mp = template.getMp();
		if (template.getRandomMp() != 0) {
			mp = (int) (mp + calcRandomVal(mp, template.getRandomMp(), rate));
		}
		setMaxMp(mp);
		setCurrentMp(mp);

		int ac = template.getAc();
		if (template.getRandomAc() != 0) {
			ac = (int) (ac +calcRandomVal(ac, template.getRandomAc(), rate)); 
		}
		this.ac.setAc(ac);

		if (template.getRandomLevel() == 0) {
			ability.setStr(template.getStr());
			ability.setCon(template.getCon());
			ability.setDex(template.getDex());
			ability.setInt(template.getInt());
			ability.setWis(template.getWis());
			resistance.setBaseMr(template.getMr());
		} else {
			ability.setStr((byte) Math.min(template.getStr() + diff, 127));
			ability.setCon((byte) Math.min(template.getCon() + diff, 127));
			ability.setDex((byte) Math.min(template.getDex() + diff, 127));
			ability.setInt((byte) Math.min(template.getInt() + diff, 127));
			ability.setWis((byte) Math.min(template.getWis() + diff, 127));
			resistance.setBaseMr((byte) Math.min(template.getMr() + diff, 127));
		}
		
		// 스탯에 대한 보너스 옵션 설정
		int str		= ability.getTotalStr();
		int dex		= ability.getTotalDex();
		int inti	= ability.getTotalInt();
		if (str > 0) {
			ability.addShortHitup(CalcStrStat.shortHitup(str));
			ability.addShortDmgup(CalcStrStat.shortDamage(str));
		}
		if (dex > 0) {
			ability.addLongHitup(CalcDexStat.longHitup(dex));
			ability.addLongDmgup(CalcDexStat.longDamage(dex));
		}
		if (inti > 0) {
			ability.addMagicHitup(CalcIntelStat.magicHitup(inti));
			ability.addMagicDmgup(CalcIntelStat.magicDamage(inti));
		}

		setPassispeed(template.getPassiSpeed());
		setAtkspeed(template.getAtkSpeed());
		setAtkMagicspeed(template.getAtkMagicSpeed());
		setSubMagicspeed(template.getSubMagicSpeed());
		setAgro(template.isAgro());
		setAgroPoly(template.isAgroPoly());
		setAgroInvis(template.isAgroInvis());
		setSpriteId(template.getSpriteId());
		setExp(template.getRandomExp() == 0 ? template.getExp() : template.getRandomExp() + randomlevel);

		int alignment = template.getAlignment();
		if (template.getRandomAlign() != 0) {
			alignment = (int) (alignment + calcRandomVal(alignment, template.getRandomAlign(), rate));
		}
		setAlignment(alignment);

		setPickupItem(template.isPicupItem());
		getMoveState().setBraveSpeed(template.isBraveSpeed() ? 1 : 0);
		getAbility().addDamageReduction(template.getDamageReduction());
		
		if (template.getDigestItem() > 0) {
			_digestItems = new HashMap<Integer, Integer>();
		}
		setKarma(template.getKarma());
		setLightSize(template.getLightSize());
		mobSkill = new L1MobSkillUse(this);
		
		CommonNPCInfo bin = template.getBin();
		if (bin != null) {
			ability.addMe(bin.get_magic_evasion());
			ElementalResistance resis = bin.get_elemental_resistance();
			if (resis != null) {
				resistance.addFire(resis.get_fire());
				resistance.addWater(resis.get_water());
				resistance.addWind(resis.get_air());
				resistance.addEarth(resis.get_earth());
			}
		}
	}

	class NpcAI implements Runnable {
		public void start() {
			setAiRunning(true);
			GeneralThreadPool.getInstance().schedule(NpcAI.this, getSleepTime());
		}

		private void stop() {
			mobSkill.resetAllSkillUseCount();
			GeneralThreadPool.getInstance().execute(new DeathSyncTimer());
		}

		private void schedule(int delay) {
			GeneralThreadPool.getInstance().schedule(NpcAI.this, delay);
		}

		@Override
		public void run() {
			try {
				synchronized(synchObject){
					if (notContinued()) {
						stop();
						return;
					}
					if (0 < _paralysisTime) {
						schedule(_paralysisTime);
						_paralysisTime = 0;
						setParalyzed(false);
						return;
					}
					if (isParalyzed() || isSleeped()) {
						schedule(300);
						return;
					}
					if (!AIProcess()) {
						schedule(getSleepTime());
						return;
					}
				}
				stop();
			} catch (Exception e) {
				System.out.println(String.format("[NPC_AI] NPC ID : %d", getNpcTemplate().getNpcId()));
				//_log.log(Level.WARNING, "NpcAI에 예외가 발생했습니다.", e);
				_log.log(Level.WARNING, "NpcAI encountered an exception.", e);
			}
		}

		private boolean notContinued() {
			return _destroyed || isDead() || getCurrentHp() <= 0 || getHiddenStatus() != HIDDEN_STATUS_NONE;
		}
	}

	protected void startAI() {
		if (this instanceof L1ArrowInstance) {
			return;
		}
		new NpcAI().start();
	}
	
	public Object synchObject = new Object();

	class DeathSyncTimer implements Runnable {
		private void schedule(int delay) {
			GeneralThreadPool.getInstance().schedule(DeathSyncTimer.this, delay);
		}

		@Override
		public void run() {
			if (isDeathProcessing()) {
				schedule(getSleepTime());
				return;
			}
			allTargetClear();
			setAiRunning(false);
		}
	}

	protected boolean AIProcess() {
		if (checkCondition()) {
			return false;
		}
		setSleepTime(300);
		int npcId = getNpcId();
		if (L1MerchantInstance.AI_MERCHANT_ID.contains(npcId)) {
			trapTelePort();
		} else if (npcId == 5067) {// 시장 경비병
			((L1TeleporterInstance) this).headingChangeAction();
			return false;
		}
		checkTarget();
		if (_target == null && _master == null) {
			if (this instanceof L1RedKnightInstance && ((L1RedKnightInstance) this).isAction) {
				((L1RedKnightInstance)this).action();
				return false;
			}
			searchTarget();
		}
		onItemUse();
		if (_target == null) {
			checkTargetItem();
			if (isPickupItem() && _targetItem == null) {
				searchTargetItem();
			}
			if (_targetItem == null) {
				if (noTarget()) {
					return true;
				}
			} else {
				Object groundInventory = L1World.getInstance().getInventory(_targetItem.getX(), _targetItem.getY(), _targetItem.getMapId());
				if (((L1Inventory)groundInventory).checkItem(_targetItem.getItemId())) {
					onTargetItem();
				} else {
					_targetItemList.remove(_targetItem);
					_targetItem = null;
					setSleepTime(1000);
					return false;
				}
			}
		} else {
			if (getHiddenStatus() == HIDDEN_STATUS_NONE) {
				onTarget();
			} else {
				return true;
			}
		}
		return false;
	}

	public void onItemUse() {}
	public void searchTarget() {}
	public boolean checkCondition() {
		return false;
	}

	public void checkTarget() {
		if (_target == null
				|| _target.getMapId() != getMapId()
				|| _target.isDead()
				|| _target.getTileDistance(this) > DISTANCE_RANGE_VALUE
				|| _target.getCurrentHp() <= 0		
				|| (_target.isInvisble() && !isAgroInvis() && !_hateList.containsKey(_target))
				|| (_target instanceof L1PcInstance && ((L1PcInstance)_target).isDestroyed())
				|| (_target instanceof L1NpcInstance && ((L1NpcInstance)_target).isDestroyed())
				|| (_target.getSkill() != null && _target.getSkill().isBlindHidingAssassin())) {
			if (_target != null) {
				tagertClear();
			}
			if (!_hateList.isEmpty()) {
				_target = _hateList.getMaxHateCharacter();
				checkTarget();
			}
		}
	}

	public void checkTargetItem() {
		if (_targetItem == null	|| _targetItem.getMapId() != getMapId() || getLocation().getTileDistance(_targetItem.getLocation()) > 15) {
			if (!_targetItemList.isEmpty()) {
				_targetItem = _targetItemList.get(0);
				_targetItemList.remove(0);
				checkTargetItem();
			} else {
				_targetItem = null;
			}
		}
	}
	
	public int getDistance(int x, int y, int tx, int ty) {// 거리값 추출
		int dx = tx - x;
		int dy = ty - y;
		return (int) Math.sqrt(dx * dx + dy * dy);
	}

	public boolean isDistance(int x, int y, int m, int tx, int ty, int tm, int loc) {// 거리안에 있다면 참
		int distance = getDistance(x, y, tx, ty);
		if (loc < distance) {
			return false;
		}
		if (m != tm) {
			return false;
		}
		return true;
	}

	public int calcheading(L1Object o, int x, int y) {
		return calcheading(o.getX(), o.getY(), x, y);
	}
	
	public void onTarget() {
		try {
			if (_target == null) {
				return;
			}
			int targetX = _target.getX(), targetY = _target.getY();
			_actived = true;
			if (!_targetItemList.isEmpty()) {
				_targetItemList.clear();
			}
			_targetItem = null;
			if (getAtkspeed() == 0 && getPassispeed() == 0) {
				return;
			}
			int distance = getLocation().getTileLineDistance(_target.getLocation());
			if (_target.getMapId() != getMapId() || distance > DISTANCE_RANGE_VALUE) {
				tagertClear();
				return;
			}
			if (isBlind() && distance > 1) {
				tagertClear();
				return;
			}
			if (getAtkspeed() == 0 && getPassispeed() > 0) {
				if (distance > 15) {
					tagertClear();
					return;
				}
				onlyMoveAstar(targetX, targetY);
				return;
			}
			if (_statusEscape) {// 도망 몬스터
				escapeMoveAstar();
				return;
			}
			
			if (mobSkill.isActionDelay() || mobSkill.skillUse(_target)) {
				setSleepTime(calcSleepTime(mobSkill.getSleepTime(), MAGIC_SPEED));
				return;
			}
			
			int range = getNpcTemplate().getRanged();
			if (isAttackPosition(_target, range, _target instanceof L1DoorInstance)
					&& _target.isAttackPosition(this, range, this instanceof L1DoorInstance)) {
				attackTarget();
				return;
			}
			
			// TODO 공격범위가 아니므로 타겟에게 이동 처리
			if (getPassispeed() <= 0) {
				tagertClear();
				return;
			}
			if (isHold()) {
				return;
			}
			if (firstFound && getNpcTemplate().isTeleport() && distance > 3 && distance < 15 && nearTeleport(targetX, targetY) == true) {
				firstFound = false;
				return;
			}
			if (getNpcTemplate().isTeleport() && getCurrentMp() >= 10 && distance > 6 && distance < 15 && 2 > random.nextInt(10)) {
				if (nearTeleport(targetX, targetY) == true) {
					return;
				}
			} else if (isTeleportDmgAction && getCurrentMp() >= 10) {
				teleportDmgAction();
				return;
			}
			onMove(targetX, targetY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void onMove(int targetX, int targetY){
		int dir = moveDirection(_target.getMapId(), targetX, targetY);
		dir = checkObject(getX(), getY(), getMapId(), dir);
		if (dir == -1) {
			failureMove();
			return;
		}
		boolean door = World.isDoorMove(getX(), getY(), getMapId(), calcheading(this, targetX, targetY));
		boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir);
		if (door || !tail) {
			failureMove();
			return;
		}
		setDirectionMove(dir);
		setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
	}
	
	protected void failureMove(){
		if (++cnt > 5) {
			_backtarget = _target;
			tagertClear();
			cnt = 0;
		}
	}
	
	protected void escapeMoveAstar(){
		if (_locEscape == null) {
			_statusEscape = false;
			return;
		}
		if (_locEscape.getX() == getX() && _locEscape.getY() == getY()) {
			_locEscape = null;
			_statusEscape = false;
			return;
		}
		int dir = moveDirection(getMapId(), _locEscape.getX(), _locEscape.getY());
		dir = checkObject(getX(), getY(), getMapId(), dir);
		if (dir == -1) {
			if (getNpcId() == 7800219) {// 몽환의 서큐버스
				teleport(_locEscape.getX(), _locEscape.getY(), getMoveState().getHeading());
			}
			_locEscape = null;
			_statusEscape = false;
		} else {
		    setDirectionMove(dir);
		    setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
		}
	}
	
	protected void onlyMoveAstar(int targetX, int targetY){
		int dir = targetReverseDirection(targetX, targetY);
		dir = checkObject(getX(), getY(), getMapId(), dir);
		if (dir == -1) {
			return;
		}
		setDirectionMove(dir);
		setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
	}

	public void die(L1Character lastAttacker) {
		setDeathProcessing(true);
		setCurrentHp(0);
		setDead(true);
		setActionStatus(ActionCodes.ACTION_Die);
		getMap().setPassable(getLocation(), true);
		Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
		startChat(CHAT_TIMING_DEAD);
		setDeathProcessing(false);
		setExp(0);
		setKarma(0);
		setAlignment(0);
		allTargetClear();
		startDeleteTimer();
	}
	
	/**
	 * 공격 공헌도 설정
	 * @param cha
	 * @param hate
	 */
	public void setHate(L1Character cha, int hate) {
		if (cha == null || cha.getId() == getId()) {
			return;
		}
		// 첫 공격자에게 부여되는 추가 공헌도
		if (!_firstAttack && hate != 0) {
			if (!getNpcTemplate().isBossMonster()) {// 보스 제외
				int maxHp = getMaxHp();
				hate += maxHp >= 100000 ? maxHp / 1000 : maxHp >= 10000 ? maxHp / 100 : maxHp / 10;
			}
			_firstAttack = true;
		}
		_hateList.add(cha, hate);
		_dropHateList.add(cha, hate);
		_target = _hateList.getMaxHateCharacter();// 가장 많이 공격한 대상 어그로
		checkTarget();
	}

	public void setLink(L1Character cha) {}

	public void serchLink(L1PcInstance targetPlayer, int family) {
		List<L1Object> targetKnownObjects = targetPlayer.getKnownObjects();
		L1NpcInstance npc = null;
		L1MobGroupInfo mobGroupInfo = null;
		for (Object knownObject : targetKnownObjects) {
			if (knownObject instanceof L1NpcInstance) {
				npc = (L1NpcInstance) knownObject;
				if (npc.getNpcTemplate().getAgroFamily() > 0) {
					if (npc.getNpcTemplate().getAgroFamily() == 1) {
						if (npc.getNpcTemplate().getFamily() == family) {
							npc.setLink(targetPlayer);
						}
					} else {
						npc.setLink(targetPlayer);
					}
				}
				mobGroupInfo = getMobGroupInfo();
				if (mobGroupInfo != null && getMobGroupId() != 0 && getMobGroupId() == npc.getMobGroupId()) {
					npc.setLink(targetPlayer);
				}
			}
		}
	}

	public void attackTarget() {
		if (_target == null) {
			return;
		}
		if (_target instanceof L1PcInstance) {
			L1PcInstance player = (L1PcInstance) _target;
			if (player.getTeleport().isTeleport()) {
				return;
			}
		} else if (_target instanceof L1NpcInstance) {
			L1Character cha = ((L1NpcInstance) _target).getMaster();
			if (cha instanceof L1PcInstance && ((L1PcInstance) cha).getTeleport().isTeleport()) {
				return;
			}
		}
		if (getMaster() != null && getMaster() instanceof L1PcInstance && ((L1PcInstance) getMaster()).getTeleport().isTeleport()) {
			return;
		}

		if (_target instanceof L1NpcInstance && ((L1NpcInstance) _target).getHiddenStatus() != HIDDEN_STATUS_NONE)  {
			allTargetClear();
			return;
		}

		boolean isCounterBarrier = false, isInferno = false, isHalphas = false, isConqueror = false;
		L1Attack attack = new L1Attack(this, _target);
		if (attack.calcHit()) {
			L1SkillStatus skill = _target.getSkill();
			if (skill != null) {
				if (skill.hasSkillEffect(L1SkillId.COUNTER_BARRIER) && attack.isShortDistance()
						&& new L1Magic(_target, this).calcProbabilityMagic(L1SkillId.COUNTER_BARRIER)) {
					isCounterBarrier = true;
				} else if (skill.hasSkillEffect(L1SkillId.INFERNO) && attack.isShortDistance()
						&& new L1Magic(_target, this).calcProbabilityMagic(L1SkillId.INFERNO)) {
					isInferno = true;
				} else if (skill.hasSkillEffect(L1SkillId.HALPHAS) && attack.isShortDistance()
						&& new L1Magic(_target, this).calcProbabilityMagic(L1SkillId.HALPHAS)) {
					isHalphas = true;
				} else if (_target.isPassiveStatus(L1PassiveId.CONQUEROR) && attack.isShortDistance()
						&& random.nextInt(100) + 1 <= Config.SPELL.CONQUEROR_PROB) {
					isConqueror = true;
				}
			}
			if (!isCounterBarrier && !isInferno && !isHalphas && !isConqueror) {
				attack.calcDamage();
				if (_target instanceof L1ScarecrowInstance) {
					((L1ScarecrowInstance) _target).actionHeading();
				}
			}
		}
		if (isCounterBarrier){
			if (_target.isPassiveStatus(L1PassiveId.COUNTER_BARRIER_MASTER)) {
				attack.actionCounterBarrierMaster();
				_target.setCurrentHp(_target.getCurrentHp() + Config.SPELL.COUNTER_BARRIER_MASTER_HP);
			} else if (_target.isPassiveStatus(L1PassiveId.COUNTER_BARRIER_VETERAN)) {
				attack.actionCounterBarrierBeterang();
			} else {
			    attack.actionCounterBarrier();
			}
			attack.commitCounterBarrier();
		} else if (isInferno) {
			attack.clacInfernoDamage();
			attack.actionInferno();
			attack.commitInferno();
			attack.commit();
		} else if (isHalphas) {
			attack.actionHalphas();
			attack.commitHalphas();
		} else if (isConqueror) {
			attack.actionConcqueror();
			attack.commitConcqueror();
		} else {
			attack.action();
			attack.commit();
		}
		setSleepTime(calcSleepTime(getAtkspeed(), ATTACK_SPEED));
	}

	public void searchTargetItem() {
		ArrayList<L1GroundInventory> gInventorys = new ArrayList<L1GroundInventory>();
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
			if (obj == null) {
				break;
			}
			if (obj instanceof L1GroundInventory) {
				gInventorys.add((L1GroundInventory) obj);
			}
		}
		if (gInventorys.size() == 0) {
			return;
		}
		int pickupIndex = (int) (Math.random() * gInventorys.size());
		L1GroundInventory inventory = gInventorys.get(pickupIndex);
		for (L1ItemInstance item : inventory.getItems()) {
			if (getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) { 
				_targetItem = item;
				_targetItemList.add(_targetItem);
			}
		}
	}

	public void searchItemFromAir() {
		ArrayList<L1GroundInventory> gInventorys = new ArrayList<L1GroundInventory>();
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 4)) {
			if (obj == null) {
				break;
			}
			if (obj instanceof L1GroundInventory && ((L1GroundInventory) obj).getSize() > 0) {
				gInventorys.add((L1GroundInventory) obj);
			}
		}
		if (gInventorys.size() == 0) {
			gInventorys = null;
			return;
		}
		int pickupIndex = (int) (Math.random() * gInventorys.size());
		L1GroundInventory inventory = gInventorys.get(pickupIndex);
		for (L1ItemInstance item : inventory.getItems()) {
			if (item.getItem().getType() == L1ItemNormalType.POTION.getId()
					|| item.getItem().getType() == L1ItemNormalType.FOOD.getId()) {
				if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
					setHiddenStatus(HIDDEN_STATUS_NONE);
					broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Movedown), true);
					setActionStatus(0);
					
					for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
						onPerceive(pc);
					}
					onNpcAI();
					startChat(CHAT_TIMING_HIDE);
					_targetItem = item;
					_targetItemList.add(_targetItem);
					break;
				}
			}
		}
		gInventorys.clear();
		gInventorys = null;
	}
	
	public void searchItemFromGround() {
		ArrayList<L1GroundInventory> gInventorys = new ArrayList<L1GroundInventory>();
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
			if (obj != null && obj instanceof L1GroundInventory && ((L1GroundInventory) obj).getSize() > 0) {
				gInventorys.add((L1GroundInventory) obj);
			}
		}
		if (gInventorys.size() == 0) {
			gInventorys = null;
			return;
		}
		int pickupIndex = (int) (Math.random() * gInventorys.size());
		L1GroundInventory inventory = gInventorys.get(pickupIndex);
		for (L1ItemInstance item : inventory.getItems()) {
			if (item.getItemId() == 60253 && getHiddenStatus() == HIDDEN_STATUS_SINK) {// 스콜피온의 맹독
				setHiddenStatus(HIDDEN_STATUS_NONE);
				broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_SwordWalk), true);
				setActionStatus(0);
				for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
					onPerceive(pc);
				}
				onNpcAI();
				startChat(CHAT_TIMING_HIDE);
				break;
			}
		}
		gInventorys.clear();
		gInventorys = null;
	}

	public static void shuffle(L1Object[] arr) {
		for (int i = arr.length - 1; i > 0; i--) {
			int t = (int) (Math.random() * i);
			L1Object tmp = arr[i];
			arr[i] = arr[t];
			arr[t] = tmp;
		}
	}

	public void onTargetItem() {
		if (getLocation().getTileLineDistance(_targetItem.getLocation()) == 0) {
			if (_targetItem.getItemOwner() == null) {
			    pickupTargetItem(_targetItem);
			} else {
				_targetItemList.remove(_targetItem);
				_targetItem = null;
				setSleepTime(1000);
			}
		} else {
			int dir = moveDirection(_targetItem.getMapId(), _targetItem.getX(), _targetItem.getY());
			dir = checkObject(getX(), getY(), getMapId(), dir);
			if (dir == -1) {
				if (++cnt2 > 60) {
					_targetItemList.remove(_targetItem);
					_targetItem = null;
					cnt2 = 0;
				}
			} else { 
				boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir);
				boolean obj = World.isMapdynamic(aStar.getXY(dir, true) + getX(), aStar.getXY(dir, false) + getY(), getMapId());
				boolean door = World.isDoorMove(getX(), getY(), getMapId(), dir);
				if (this instanceof L1DollInstance) {
					obj = false;
				}
				if (tail && !obj && !door) {
					setDirectionMove(dir);
				}
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			}
		}
	}

	public void pickupTargetItem(L1ItemInstance targetItem) {
		L1Inventory groundInventory = L1World.getInstance().getInventory(targetItem.getX(), targetItem.getY(), targetItem.getMapId());
		L1ItemInstance item = groundInventory.tradeItem(targetItem, targetItem.getCount(), getInventory());
		light.turnOnOffLight();
		onGetItem(item);
		_targetItemList.remove(_targetItem);
		_targetItem = null;
		setSleepTime(1000);
	}
	
	private int random(int lbound, int ubound) {
		return ubound < 0 ? (int) ((Math.random() * (ubound - lbound - 1)) + lbound) : (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
	}

	public boolean noTarget() {
		if (_master != null && _master.getMapId() == getMapId() && getLocation().getTileLineDistance(_master.getLocation()) > 2) {
			return noTargetMaster();
		}
		
		if (L1World.getInstance().getRecognizePlayer(this).size() == 0) {
			return true;
		}
		if (getMovementDistance() > 0
				&& (this instanceof L1GuardInstance || this instanceof L1GuardianInstance || this instanceof L1CastleGuardInstance || this instanceof L1MerchantInstance || this instanceof L1MonsterInstance)
				&& getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > getMovementDistance()) {
			teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
		}

		if (aStar != null && _master == null && getPassispeed() > 0 && !isRest()) {
			L1MobGroupInfo mobGroupInfo = getMobGroupInfo();
			if (mobGroupInfo == null || mobGroupInfo != null && mobGroupInfo.isLeader(this)) {
				noTargetNormal();
			} else {
				return noTargetGroup(mobGroupInfo.getLeader());
			}
		}
		return false;
	}
	
	protected boolean noTargetMaster(){
		int dir = moveDirection(_master.getMapId(), _master.getX(), _master.getY());
		if (dir == -1) {
			return true;
		}
		boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir);
		boolean obj = World.isMapdynamic(aStar.getXY(dir, true) + getX(), aStar.getXY(dir, false) + getY(), getMapId());
		boolean door = World.isDoorMove(getX(), getY(), getMapId(), dir);
		if (this instanceof L1DollInstance) {
			obj = false;
		}
		if (tail && !obj && !door) {
			setDirectionMove(dir);
		}
		setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
		return false;
	}
	
	protected void noTargetNormal(){
		if (_randomMoveDistance > 0) {
			_randomMoveDistance -= 1;
		} else {
			int heading = 0;
			switch (random(0, 5)) {
			case 0:
				_randomMoveDistance = random(5, 10);
				heading = random(0, 7);
				break;
			case 1:
			case 2:
				heading = getMoveState().getHeading() + 1;
				break;
			case 3:
			case 4:
				heading = getMoveState().getHeading() - 1;
				break;
			default:
				heading = random(0, 7);
				break;
			}
			int x = aStar.getXY(heading, true) + getX();
			int y = aStar.getXY(heading, false) + getY();
			int dis = 20;
			if (this instanceof L1MerchantInstance) {
				dis = getNpcId() == 70027 ? 2 : 5;
			}
			if (!isDistance(x, y, getMapId(), getHomeX(), getHomeY(), getMapId(), dis)) {
				heading = calcheading(this, getHomeX(), getHomeY());
				x = aStar.getXY(heading, true) + getX();
				y = aStar.getXY(heading, false) + getY();
			}
			if (getNpcId() == 70848) {// 엔트
				if (++cnt3 > 5) {
					heading = random(0, 7);
					x = aStar.getXY(heading, true) + getX();
					y = aStar.getXY(heading, false) + getY();
					cnt4++;
				}
				if (cnt4 > 10) {
					teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
					cnt4 = 0;
				}
			}

			boolean tail	= World.isThroughObject(getX(), getY(), getMapId(), heading);
			boolean obj		= World.isMapdynamic(x, y, getMapId());
			boolean door	= World.isDoorMove(getX(), getY(), getMapId(), heading);
			if (this instanceof L1DollInstance) {
				obj = false;
			}
			if (tail && !obj && !door) {
				setDirectionMove(heading);
				if (cnt3 > 0) {
					cnt3 = 0;
				}
			}
		}
		setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
	}
	
	protected boolean noTargetGroup(L1NpcInstance leader){
		if (getLocation().getTileLineDistance(leader.getLocation()) > 2) {
			int dir = moveDirection(leader.getMapId(), leader.getX(), leader.getY());
			if (dir == -1) {
				return true;
			}
			boolean tail	= World.isThroughObject(getX(), getY(), getMapId(), dir);
			boolean obj		= World.isMapdynamic(aStar.getXY(dir, true) + getX(), aStar.getXY(dir, false) + getY(), getMapId());
			boolean door	= World.isDoorMove(getX(), getY(), getMapId(), dir);
			if (this instanceof L1DollInstance) {
				obj = false;
			}
			if (tail && !obj && !door) {
				setDirectionMove(dir);
			}
			setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
		}
		return false;
	}
	
	protected boolean noTargetHomeMove() {
		int dir = moveDirection(getMapId(), getHomeX(), getHomeY());
		if (dir == -1) {
			return true;
		}
		boolean tail	= World.isThroughObject(getX(), getY(), getMapId(), dir);
		boolean obj		= World.isMapdynamic(aStar.getXY(dir, true) + getX(), aStar.getXY(dir, false) + getY(), getMapId());
		boolean door	= World.isDoorMove(getX(), getY(), getMapId(), dir);
		if (this instanceof L1DollInstance) {
			obj = false;
		}
		if (tail && !obj && !door) {
			setDirectionMove(dir);
		}
		setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
		return false;
	}

	public void onFinalAction(L1PcInstance pc, String s) {}

	public void tagertClear() {
		if (_target == null) {
			return;
		}
		_hateList.remove(_target);
		_dropHateList.remove(_target);
		_target = null;
	}

	public void targetRemove(L1Character target) {
		_hateList.remove(target);
		_dropHateList.remove(target);
		if (_target != null && _target.equals(target)) {
			_target = null;
		}
	}

	public void allTargetClear() {
		_hateList.clear();
		_dropHateList.clear();
		_target = null;
		_targetItemList.clear();
		_targetItem = null;
	}
	
	public void setTarget(L1Character cha) {
		_target = cha;
	}
	public L1Character getTarget() {
		return _target;
	}

	public void setMaster(L1Character cha) {
		_master = cha;
	}
	public L1Character getMaster() {
		return _master;
	}

	public void onNpcAI() {}

	public void refineItem() {
		int[] materials = null;
		int[] counts = null;
		int[] createitem = null;
		int[] createcount = null;

		if (_npcTemplate.getNpcId() == 45032) {
			if (getExp() != 0 && !_inventory.checkItem(20)) {
				materials = new int[] { 40508, 40521, 40045 };
				counts = new int[] { 150, 3, 3 };
				createitem = new int[] { 20 };
				createcount = new int[] { 1 };
				if (_inventory.checkItem(materials, counts)) {
					for (int i = 0; i < materials.length; i++) {
						_inventory.consumeItem(materials[i], counts[i]);
					}
					for (int j = 0; j < createitem.length; j++) {
						_inventory.storeItem(createitem[j], createcount[j]);
					}
				}
			}
			if (getExp() != 0 && !_inventory.checkItem(19)) {
				materials = new int[] { 40494, 40521 };
				counts = new int[] { 150, 3 };
				createitem = new int[] { 19 };
				createcount = new int[] { 1 };
				if (_inventory.checkItem(materials, counts)) {
					for (int i = 0; i < materials.length; i++) {
						_inventory.consumeItem(materials[i], counts[i]);
					}
					for (int j = 0; j < createitem.length; j++) {
						_inventory.storeItem(createitem[j], createcount[j]);
					}
				}
			}
			if (getExp() != 0 && !_inventory.checkItem(3)) {
				materials = new int[] { 40494, 40521 };
				counts = new int[] { 50, 1 };
				createitem = new int[] { 3 };
				createcount = new int[] { 1 };
				if (_inventory.checkItem(materials, counts)) {
					for (int i = 0; i < materials.length; i++) {
						_inventory.consumeItem(materials[i], counts[i]);
					}
					for (int j = 0; j < createitem.length; j++) {
						_inventory.storeItem(createitem[j], createcount[j]);
					}
				}
			}
			if (getExp() != 0 && !_inventory.checkItem(100)) {
				materials = new int[] { 88, 40508, 40045 };
				counts = new int[] { 4, 80, 3 };
				createitem = new int[] { 100 };
				createcount = new int[] { 1 };
				if (_inventory.checkItem(materials, counts)) {
					for (int i = 0; i < materials.length; i++) {
						_inventory.consumeItem(materials[i], counts[i]);
					}
					for (int j = 0; j < createitem.length; j++) {
						_inventory.storeItem(createitem[j], createcount[j]);
					}
				}
			}
			if (getExp() != 0 && !_inventory.checkItem(89)) {
				materials = new int[] { 88, 40494 };
				counts = new int[] { 2, 80 };
				createitem = new int[] { 89 };
				createcount = new int[] { 1 };
				if (_inventory.checkItem(materials, counts)) {
					for (int i = 0; i < materials.length; i++) {
						_inventory.consumeItem(materials[i], counts[i]);
					}
					L1ItemInstance item = null;
					for (int j = 0; j < createitem.length; j++) {
						item = _inventory.storeItem(createitem[j], createcount[j]);
						if (getNpcTemplate().getDigestItem() > 0) {
							setDigestItem(item);
						}
					}
				}
			}
		} else if (_npcTemplate.getNpcId() == 81069) { 
			if (getExp() != 0 && !_inventory.checkItem(40542)) {
				materials = new int[] { 40032 };
				counts = new int[] { 1 };
				createitem = new int[] { 40542 };
				createcount = new int[] { 1 };
				if (_inventory.checkItem(materials, counts)) {
					for (int i = 0; i < materials.length; i++) {
						_inventory.consumeItem(materials[i], counts[i]);
					}
					for (int j = 0; j < createitem.length; j++) {
						_inventory.storeItem(createitem[j], createcount[j]);
					}
				}
			}
		} else if (_npcTemplate.getNpcId() == 45166 || _npcTemplate.getNpcId() == 45167) {
			if (getExp() != 0 && !_inventory.checkItem(40726)) {
				materials = new int[] { 40725 };
				counts = new int[] { 1 };
				createitem = new int[] { 40726 };
				createcount = new int[] { 1 };
				if (_inventory.checkItem(materials, counts)) {
					for (int i = 0; i < materials.length; i++) {
						_inventory.consumeItem(materials[i], counts[i]);
					}
					for (int j = 0; j < createitem.length; j++) {
						_inventory.storeItem(createitem[j], createcount[j]);
					}
				}
			}
		}
	}

	public L1HateList getHateList() {
		return _hateList;
	}
	
	private int _paralysisTime;
	public void setParalysisTime(int time) {
		_paralysisTime = time;
	}
	public int getParalysisTime() {
		return _paralysisTime;
	}
	
	public final void startHpRegeneration() {
		int hprInterval = getNpcTemplate().getHprInterval();
		int hpr = getNpcTemplate().getHpr();
		if (!_hprRunning && hprInterval > 0 && hpr > 0) {
			if (_hprTimer == null) {
				_hprTimer = new HprTimer(hpr, hprInterval);
			}
			GeneralThreadPool.getInstance().schedule(_hprTimer, hprInterval);
			_hprRunning = true;
		}
	}

	public final void stopHpRegeneration() {
		if (_hprRunning) {
			_hprTimer.cancel();
			_hprRunning = false;
		}
	}

	public final void startMpRegeneration() {
		int mprInterval = getNpcTemplate().getMprInterval();
		int mpr = getNpcTemplate().getMpr();
		if (!_mprRunning && mprInterval > 0 && mpr > 0) {
			if (_mprTimer == null) {
				_mprTimer = new MprTimer(mpr, mprInterval);
			}
			GeneralThreadPool.getInstance().schedule(_mprTimer, mprInterval );
			_mprRunning = true;
		}
	}

	public final void stopMpRegeneration() {
		if (_mprRunning) {
			_mprTimer.cancel();
			_mprRunning = false;
		}
	}

	private boolean _hprRunning;

	private HprTimer _hprTimer;

	class HprTimer implements Runnable {
		private boolean _active;
		private long _interval;
		private int _point;

		@Override
		public void run() {
			try {
				if (!_active) {
					return;
				}
				if ((!_destroyed && !isDead()) && (getCurrentHp() > 0 && getCurrentHp() < getMaxHp())) {
					setCurrentHp(getCurrentHp() + _point);
					GeneralThreadPool.getInstance().schedule(this, _interval);
				} else {
					_hprRunning = false;
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		public HprTimer(int point, long interval) {
			_point		= point < 1 ? 1 : point;
			_active		= true;
			_interval	= interval;
		}
		
		public void cancel(){
			_active	= false;
		}

	}

	private boolean _mprRunning = false;
	private MprTimer _mprTimer;
	class MprTimer implements Runnable {
		private boolean _active;
		private long _interval;
		private int _point;

		@Override
		public void run() {
			try {
				if (!_active) {
					return;
				}
				if ((!_destroyed && !isDead()) && (getCurrentHp() > 0 && getCurrentMp() < getMaxMp())) {
					setCurrentMp(getCurrentMp() + _point);
					GeneralThreadPool.getInstance().schedule(this, _interval);
				} else {
					_mprRunning = false;
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		public MprTimer(int point, long interval) {
			_point		= point < 1 ? 1 : point;
			_active		= true;
			_interval	= interval;
		}

		public void cancel() {
			_active = false;
		}
	}

	class DigestItemTimer implements Runnable {
		@Override
		public void run() {
			if (!_digestItemRunning) {
				_digestItemRunning = true;
			}
			Object[] keys = null;
			L1ItemInstance digestItem = null;
			if (!_destroyed && _digestItems.size() > 0) {
				keys = _digestItems.keySet().toArray();
				Integer key = null;
				Integer digestCounter = null;		
				for (int i = 0; i < keys.length; i++) {
					key = (Integer) keys[i];
					digestCounter = _digestItems.get(key);
					digestCounter -= 1;
					if (digestCounter <= 0) {
						_digestItems.remove(key);
						digestItem = getInventory().getItem(key);
						if (digestItem != null) {
							getInventory().removeItem(digestItem, digestItem.getCount());
						}
					} else {
						_digestItems.put(key, digestCounter);
					}
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} else {
				_digestItemRunning = false;
			}
		}
	}
	
	private long _explosion_remain_time;
	public long getExplosionTime() {
		return _explosion_remain_time;
	}
	public void setExplosionTime(long l) {
		_explosion_remain_time = l;
	}
	
	public void startExplosionTime(long time) {// 할파스의 권속 자폭 그래픽
		setExplosionTime(time);
		GeneralThreadPool.getInstance().schedule(new ExplosionCounter(this), 1000);
	}

	public class ExplosionCounter implements Runnable {
		private L1NpcInstance _npc;
		private boolean _isSending;

		public ExplosionCounter(L1NpcInstance npc) {
			_npc = npc;
			_isSending = false;
		}

		@Override
		public void run() {
			try {
				_explosion_remain_time -= 1000;
				if (!_isSending && _explosion_remain_time <= 30000) {
					_isSending = true;
					Broadcaster.broadcastPacket(_npc, new S_ObjectExplosion(_npc, _explosion_remain_time), true);
				}
			} catch (Exception e) {
			} finally {
				if (_explosion_remain_time <= 0 || isDead()) {
					_explosion_remain_time = 0;
				} else {
					GeneralThreadPool.getInstance().schedule(this, 1000);
				}
			}
		}
	}

	private int _passispeed;
	private int _atkspeed;
	private int _atkmagicspeed;
	private int _submagicspeed;
	protected int _dopelRanged;
	private boolean _pickupItem;

	public int getPassispeed() {
		return _passispeed;
	}
	public void setPassispeed(int i) {
		_passispeed = i;
	}

	public int getAtkspeed() {
		return _atkspeed;
	}
	public void setAtkspeed(int i) {
		_atkspeed = i;
	}
	
	public int getAtkMagicspeed() {
		return _atkmagicspeed;
	}
	public void setAtkMagicspeed(int i) {
		_atkmagicspeed = i;
	}
	
	public int getSubMagicspeed() {
		return _submagicspeed;
	}
	public void setSubMagicspeed(int i) {
		_submagicspeed = i;
	}

	public boolean isPickupItem() {
		return _pickupItem;
	}
	public void setPickupItem(boolean flag) {
		_pickupItem = flag;
	}
	
	public int getDopelRanged() {
		return _dopelRanged;
	}
	public void setDopelRanged(int i) {
		_dopelRanged = i;
	}

	@Override
	public L1Inventory getInventory() {
		return _inventory;
	}
	public void setInventory(L1Inventory inventory) {
		_inventory = inventory;
	}

	public L1Npc getNpcTemplate() {
		return _npcTemplate;
	}
	public int getNpcId() {
		return _npcTemplate.getNpcId();
	}

	public void setPetcost(int i) {
		_petcost = i;
	}
	public int getPetcost() {
		return _petcost;
	}

	public void setSpawn(L1Spawn spawn) {
		_spawn = spawn;
	}
	public L1Spawn getSpawn() {
		return _spawn;
	}

	public void setSpawnNumber(int number) {
		_spawnNumber = number;
	}
	public int getSpawnNumber() {
		return _spawnNumber;
	}

	//TODO 스폰쓰레드 호출
	public void onDecay(boolean isReuseId) {
		_spawn.executeSpawnTask(_spawnNumber, isReuseId ? getId() : 0);
	}

	public int PASS = 1;

	public boolean isPassableNpc() {
		return this instanceof L1DollInstance; 
	}
	
	/**
	 * 알림 엔피씨 출력
	 */
	public void do_notification(boolean flag) {
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(new S_Notification(_notification_info, false), true);
		if (flag) {
			world.broadcastPacketToAll(new S_Notification(_notification_info, true), true);
		} else {
			_notification_info = null;
		}
	}
	
	public void NpcDie() {
		try {
			setDeathProcessing(true);
			setCurrentHp(0);
			setDead(true);
			getMap().setPassable(getLocation(), true);
			setDeathProcessing(false);
			setExp(0);
			setKarma(0);
			setAlignment(0);
			allTargetClear();
			deleteMe2();
		} catch (Exception e) {
		}
	}
	
	public void deleteMe() {
		_destroyed = true;
		if (get_notification_info() != null) {
			do_notification(false);
		}
		
		if (!isDead() && !isPassableNpc()) {
			getMap().setPassable(getLocation(), true);
		}
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		try {
			if (aStar != null) {
				aStar.clear();
				aStar = null;
			}
			iPath = null;
			if (tail != null) {
				tail.clear();
			    tail = null;
			}
		} catch (Exception e) {
		}
		
		L1MobGroupInfo mobGroupInfo = getMobGroupInfo();
		if (mobGroupInfo == null) {
			if (isReSpawn()) {
				onDecay(true);
			}
		} else {
			if (mobGroupInfo.removeMember(this) == 0) {
				setMobGroupInfo(null);
				if (isReSpawn()) {
					onDecay(false);
				}
			}
		}
		
		L1World world = L1World.getInstance();
		List<L1PcInstance> players = world.getRecognizePlayer(this);
		if (players.size() > 0) {
			S_RemoveObject s_deleteNewObject = new S_RemoveObject(this);
			for (L1PcInstance pc : players) {
				if (pc != null) {
					pc.removeKnownObject(this);
					pc.sendPackets(s_deleteNewObject);
				}
			}
			s_deleteNewObject.close();
			s_deleteNewObject = null;
		}
		removeAllKnownObjects();
		skillStatus.clearSkillEffectTimer();
		skillStatus.clearSkillDelay();
		world.removeVisibleObject(this);
		world.removeObject(this);
	}
	
	public void deleteMe2() {
		_destroyed = true;
		if (get_notification_info() != null) {
			do_notification(false);
		}
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		_master = null;
		try {
			if (aStar != null) {
				aStar.clear();
				aStar = null;
			}
			iPath = null;
			if (tail != null) {
				tail.clear();
			    tail = null;
			}
		} catch (Exception e) {
		}
		L1World world = L1World.getInstance();
		List<L1PcInstance> players = world.getRecognizePlayer(this);
		if (players != null && players.size() > 0) {
			S_RemoveObject s_deleteNewObject = new S_RemoveObject(this);
			for (L1PcInstance pc : players) {
				if (pc != null) {
					pc.removeKnownObject(this);
					pc.sendPackets(s_deleteNewObject);
				}
			}
			s_deleteNewObject.close();
			s_deleteNewObject = null;
		}
		removeAllKnownObjects();
		skillStatus.clearSkillEffectTimer();
		skillStatus.clearSkillDelay();
		world.removeVisibleObject(this);
		world.removeObject(this);
	}

	public void receiveManaDamage(L1Character attacker, int damageMp) {
	}

	public void receiveCountingDamage(L1Character attacker, int damage) {
		receiveDamage(attacker, damage);
	}
	
	public void receiveDamage(L1Character attacker, int damage) {
	}

	public void setDigestItem(L1ItemInstance item) {
		if (item == null) {
			return;
		}
		_digestItems.put(new Integer(item.getId()), new Integer(getNpcTemplate().getDigestItem()));
		if (!_digestItemRunning) {
			DigestItemTimer digestItemTimer = new DigestItemTimer();
			GeneralThreadPool.getInstance().execute(digestItemTimer);
		}
	}

	public void onGetItem(L1ItemInstance item) {
		refineItem();
		getInventory().shuffle();
		if (getNpcTemplate().getDigestItem() > 0) {
			setDigestItem(item);
		}
	}

	public void approachPlayer(L1PcInstance pc) {
		if (pc.isInvisble() || pc.isGmInvis()) {
			return;
		}
		switch(getHiddenStatus()){
		case HIDDEN_STATUS_SINK:
			if (getCurrentHp() == getMaxHp()) {
				if (pc.getLocation().getTileLineDistance(this.getLocation()) <= 2) {
					appearOnGround(pc);
				}
			} else {
				if (getNpcId() == 5135) {// 샌드웜
					searchItemFromGround();
				}
			}
			break;
		case HIDDEN_STATUS_FLY:
			if (getCurrentHp() == getMaxHp()) {
				if (pc.getLocation().getTileLineDistance(this.getLocation()) <= 1) {
					appearOnGround(pc);
				}
			} else {
				if (getNpcId() != 45681) {// 구 린드비오르 제외
				    searchItemFromAir();
				}
			}
			break;
		case HIDDEN_STATUS_ANCIENTGUARDIAN:
			if (getCurrentHp() <= getMaxHp() * 0.9) {
				appearOnGround(pc);
			}
			break;
		}
	}

	public void appearOnGround(L1PcInstance pc) {
		setHiddenStatus(HIDDEN_STATUS_NONE);
		setActionStatus(0);
		switch(getHiddenStatus()){
		case HIDDEN_STATUS_SINK:{
			broadcastPacket(new S_RemoveObject(this), true);
			broadcastPacket(new S_NPCObject(this), true);
			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Appear), true);
			onNpcAI();
		}
			break;
		case HIDDEN_STATUS_FLY:{
			S_NPCObject s_npcPack = new S_NPCObject(this);
			for (L1PcInstance newPc : L1World.getInstance().getRecognizePlayer(this)) {
				newPc.sendPackets(s_npcPack);
				newPc.addKnownObject(this);
				addKnownObject(newPc);
			}
			s_npcPack.clear();
			s_npcPack = null;
			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Movedown), true);
			startChat(CHAT_TIMING_HIDE);
		}
			break;
		case HIDDEN_STATUS_ANCIENTGUARDIAN:{
			broadcastPacket(new S_RemoveObject(this), true);
			S_NPCObject s_npcPack = new S_NPCObject(this);
			for (L1PcInstance newPc : L1World.getInstance().getRecognizePlayer(this)) {
				newPc.sendPackets(s_npcPack);
				newPc.addKnownObject(this);
				addKnownObject(newPc);
			}
			s_npcPack.clear();
			s_npcPack = null;
			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Appear), true);
		}
			break;
		}
		if (!pc.isInvisble() && !pc.isGm()) {
			_hateList.add(pc, 0);
			_target = pc;				
		}
		onNpcAI();
	}

	public void setDirectionMove(int dir) {
		if (dir < 0 || dir > 7) {
			return;
		}
		if (isHold()) {
			return;
		}
		getMoveState().setHeading(dir);
		boolean isDoll = this instanceof L1DollInstance;
		if (!isDoll) {
			getMap().setPassable(getLocation(), true);
		}
		getLocation().set(getX() + HEADING_TABLE_X[dir], getY() + HEADING_TABLE_Y[dir]);
		if (!isDoll) {
			getMap().setPassable(getLocation(), false);
		}
		L1World.getInstance().onMoveObject(this);
		broadcastPacket(new S_MoveCharPacket(this), true);
		
		int npcid = getNpcTemplate().getNpcId();
		int x = getX(), y = getY();
		short mapId = getMapId();
		if (getMovementDistance() > 0) {
			if ((this instanceof L1GuardInstance || this instanceof L1GuardianInstance || this instanceof L1CastleGuardInstance || this instanceof L1MerchantInstance || this instanceof L1MonsterInstance)
					&& getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > getMovementDistance()) {
				teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
				return;
			}
		} else if (this instanceof L1MonsterInstance && (mapId == 4 || mapId == 9) 
				&& getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > 100) {// 필드 몬스터 100칸 이상 멀어질시 제자리로 텔
			teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			return;
		}
		
		if ((npcid >= 45912 && npcid <= 45916) && mapId == 4) {// 글루딘 유령 원래 자리로 이동 시키기
			if (!(x >= 32591 && x <= 32644 && y >= 32643 && y <= 32688)) {
				teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}
		} else if ((npcid == 45752 || npcid == 45753) && mapId == 15404) {// 발록 문밖으로 나가면 이동 시키기
			if (!(x >= 32720 && x <= 32742 && y >= 32851 && y <= 32877)) {
				teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}
		} else if (npcid == 7220091 && mapId == 1319) {// 분노한 발록
			if (!(x >= 32719 && x <= 32785 && y >= 32919 && y <= 33002)) {
				teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}
		} else if ((npcid == 800185 || npcid == 800177 || npcid == 800187 || npcid == 800188) && mapId == 12853) {// 감시자 시어
			if (!(x >= 32786 && x <= 32815 && y >= 32781 && y <= 32813)) {
				teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}
		} else if ((npcid == 800186 || npcid == 800178) && mapId == 12854) {// 박쥐 뱀파이어
			if (!(x >= 32787 && x <= 32817 && y >= 32779 && y <= 32810)) {
				teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}
		} else if ((npcid == 800186 || npcid == 800178) && mapId == 12857) {// 모래바람 머미로드
			if (!(x >= 32655 && x <= 32687 && y >= 32838 && y <= 32870)) {
				teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}
		}
	}

	public int moveDirection(int mapid, int x, int y) { 
		return moveDirection(mapid, x, y, getLocation().getLineDistance(new Point(x, y)));
	}

	public int moveDirection(int mapid, int x, int y, double d) {
		int dir = 0;
		int calcx = getX() - x;
		int calcy = getY() - y;
		
		if (this.getMapId() != mapid || Math.abs(calcx) > DISTANCE_RANGE_VALUE || Math.abs(calcy) > DISTANCE_RANGE_VALUE) {
			allTargetClear();
			return -1;
		}
		if (this.isBlind() && d >= 2D) {
			return -1;
		}
		if (d > DISTANCE_RANGE_VALUE) {
			return -1;
		}
		
		if (d > courceRange) {
			dir = targetDirection(x, y);
			dir = checkObject(getX(), getY(), getMapId(), dir);
		} else {
			dir = _astar(x, y, mapid);
			if (dir == -1) {
				dir = targetDirection(x, y);
				if (!isExsistCharacterBetweenTarget(dir)) {
					dir = checkObject(getX(), getY(), getMapId(), dir);
				}
			}
		}
		return dir;
	}
	
	public int moveDirectionIndun(int mapid, int x, int y) { 
		return moveDirectionIndun(mapid, x, y, getLocation().getLineDistance(new Point(x, y)));
	}
	
	public int moveDirectionIndun(int mapid, int x, int y, double d) {
		int dir = 0;
		int calcx = getX() - x;
		int calcy = getY() - y;
		
		if (this.getMapId() != mapid || Math.abs(calcx) > DISTANCE_RANGE_VALUE || Math.abs(calcy) > DISTANCE_RANGE_VALUE) {
			allTargetClear();
			return -1;
		}
		if (this.isBlind() && d >= 2D) {
			return -1;
		}
		if (d > DISTANCE_RANGE_VALUE) {
			return -1;
		}
		if (d > courceRange) {
			dir = targetDirection(x, y);
		} else {
			dir = _astar(x, y, mapid);
			if (dir == -1) {
				dir = targetDirection(x, y);
			}
		}
		return dir;
	}
	
	private boolean isExsistCharacterBetweenTarget(int dir) {
		if (!(this instanceof L1MonsterInstance)) {
			return false;
		}
		if (_target == null) {
			return false;
		}
		L1Character cha = null;
		L1PcInstance pc = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(this, 1)) {
			if (object instanceof L1PcInstance || object instanceof L1SummonInstance || object instanceof L1PetInstance) {
				cha = (L1Character) object;
				if (!cha.isDead()) {
					boolean matched = false;
					for (int i = 0; i < 4; ++i) {
						if (!cha.getMap().isUserPassable(cha.getX(), cha.getY(), i) && !cha.getMap().isUserPassable(cha.getX(), cha.getY(), i + 4)) {
							matched = true;
							break;
						}
					}

					if (!matched) {
						continue;
					}
					if (object instanceof L1PcInstance) {
						pc = (L1PcInstance) object;
						if (pc.isGhost()) {
							continue;
						}
					}
					_hateList.add(cha, 0);
					_target = cha;
					return true;
				}
			}
		}
		return false;
	}

	public int targetReverseDirection(int tx, int ty) { 
		int dir = targetDirection(tx, ty);
		dir += 4;
		if (dir > 7) {
			dir -= 8;
		}
		return dir;
	}

	public static int checkObject(int x, int y, short m, int d) { 
		L1Map map = L1WorldMap.getInstance().getMap(m);
		switch(d){
		case 1:
			if (map.isPassable(x, y, 1)) {
				return 1;
			}
			if (map.isPassable(x, y, 0)) {
				return 0;
			}
			if (map.isPassable(x, y, 2)) {
				return 2;
			}
			return -1;
		case 2:
			if (map.isPassable(x, y, 2)) {
				return 2;
			}
			if (map.isPassable(x, y, 1)) {
				return 1;
			}
			if (map.isPassable(x, y, 3)) {
				return 3;
			}
			return -1;
		case 3:
			if (map.isPassable(x, y, 3)) {
				return 3;
			}
			if (map.isPassable(x, y, 2)) {
				return 2;
			}
			if (map.isPassable(x, y, 4)) {
				return 4;
			}
			return -1;
		case 4:
			if (map.isPassable(x, y, 4)) {
				return 4;
			}
			if (map.isPassable(x, y, 3)) {
				return 3;
			}
			if (map.isPassable(x, y, 5)) {
				return 5;
			}
			return -1;
		case 5:
			if (map.isPassable(x, y, 5)) {
				return 5;
			}
			if (map.isPassable(x, y, 4)) {
				return 4;
			}
			if (map.isPassable(x, y, 6)) {
				return 6;
			}
			return -1;
		case 6:
			if (map.isPassable(x, y, 6)) {
				return 6;
			}
			if (map.isPassable(x, y, 5)) {
				return 5;
			}
			if (map.isPassable(x, y, 7)) {
				return 7;
			}
			return -1;
		case 7:
			if (map.isPassable(x, y, 7)) {
				return 7;
			}
			if (map.isPassable(x, y, 6)) {
				return 6;
			}
			if (map.isPassable(x, y, 0)) {
				return 0;
			}
			return -1;
		case 0:
			if (map.isPassable(x, y, 0)) {
				return 0;
			}
			if (map.isPassable(x, y, 7)) {
				return 7;
			}
			if (map.isPassable(x, y, 1)) {
				return 1;
			}
			return -1;
		default:
			return -1;
		}
	}
	
	private int _astar(int x, int y, int m) {
		try {
			aStar.cleanTail();
			tail = aStar.searchTail(this, x, y, m, true);
			try {
				if (tail != null) {
					iCurrentPath = -1;
					while (tail != null) {
						if (tail.x == getX() && tail.y == getY()) {// 현재위치 라면 종료
							break;
						}
						if (iCurrentPath >= iPath.length - 1) {
							return -1;
						}
						if (_destroyed || isDead()) {
							return -1;
						}
						iPath[++iCurrentPath][0] = tail.x;
						iPath[iCurrentPath][1] = tail.y;
						tail = tail.prev;
					}
					return iCurrentPath != -1 ? aStar.calcheading(getX(), getY(), iPath[iCurrentPath][0], iPath[iCurrentPath][1]) : -1;
				} else {
					aStar.cleanTail();
					tail = aStar.FindPath2(this, x, y, m, true);
					if (tail != null && !(tail.x == getX() && tail.y == getY())) {
						iCurrentPath = -1;
						while (tail != null) {
							if (tail.x == getX() && tail.y == getY()) {// 현재위치 라면 종료
								break;
							}
							if (iCurrentPath >= iPath.length - 1) {
								return -1;
							}
							if (_destroyed || isDead()) {
								return -1;
							}
							iPath[++iCurrentPath][0] = tail.x;
							iPath[iCurrentPath][1] = tail.y;
							tail = tail.prev;
						}
						return iCurrentPath != -1 ? aStar.calcheading(getX(), getY(), iPath[iCurrentPath][0], iPath[iCurrentPath][1]) : -1;
					} else {
						int chdir = calcheading(this, x, y);
						if (getMoveState().getHeading() != chdir) {
							this.getMoveState().setHeading(chdir);
							this.broadcastPacket(new S_ChangeHeading(this), true);
						}
					}
					return getMoveState().getHeading();
				}
			} catch (Exception e) {
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}

	private void useHealPotion(int healHp, int effectId) {
		broadcastPacket(new S_Effect(getId(), effectId), true);
		if (skillStatus.hasSkillEffect(L1SkillId.POLLUTE_WATER) || skillStatus.hasSkillEffect(L1SkillId.MOB_POLLUTE_WATER)) {
			healHp >>= 0x00000001;
		}
		if (this instanceof L1PetInstance) {
			((L1PetInstance) this).setCurrentHp(getCurrentHp() + healHp);
		} else if (this instanceof L1SummonInstance) {
			((L1SummonInstance) this).setCurrentHp(getCurrentHp() + healHp);
		} else {
			setCurrentHp(getCurrentHp() + healHp);
		}
	}

	public void useHastePotion(int time) {
		broadcastPacket(new S_SkillHaste(getId(), 1, time), true);
		broadcastPacket(new S_Effect(getId(), 191), true);
		getMoveState().setMoveSpeed(1);
		skillStatus.setSkillEffect(L1SkillId.STATUS_HASTE, time * 1000);
	}

	public static final int USEITEM_HEAL = 0;
	public static final int USEITEM_HASTE = 1;
	public static int[] healPotions = {
		POTION_OF_GREATER_HEALING, POTION_OF_EXTRA_HEALING, POTION_OF_HEALING
	};
	public static int[] haestPotions = {
		B_POTION_OF_GREATER_HASTE_SELF, POTION_OF_GREATER_HASTE_SELF, B_POTION_OF_HASTE_SELF, POTION_OF_HASTE_SELF
	};

	public void useItem(int type, int chance) {
		if (skillStatus.hasSkillEffect(L1SkillId.DECAY_POTION) || skillStatus.hasSkillEffect(L1SkillId.FATAL_POTION)) {
			return;
		}
		if (random.nextInt(100) > chance) {
			return;
		}
		if (type == USEITEM_HEAL) { 
			if (getInventory().consumeItem(POTION_OF_GREATER_HEALING, 1)) {
				useHealPotion(75, 197);
			} else if (getInventory().consumeItem(POTION_OF_EXTRA_HEALING, 1)) {
				useHealPotion(45, 194);
			} else if (getInventory().consumeItem(POTION_OF_HEALING, 1)) {
				useHealPotion(15, 189);
			}
		} else if (type == USEITEM_HASTE) { 
			if (skillStatus.hasSkillEffect(L1SkillId.STATUS_HASTE)) {
				return; 
			}
			if (getInventory().consumeItem(B_POTION_OF_GREATER_HASTE_SELF, 1)) {
				useHastePotion(2100);
			} else if (getInventory().consumeItem(POTION_OF_GREATER_HASTE_SELF, 1)) {
				useHastePotion(1800);
			} else if (getInventory().consumeItem(B_POTION_OF_HASTE_SELF, 1)) {
				useHastePotion(350);
			} else if (getInventory().consumeItem(POTION_OF_HASTE_SELF, 1)) {
				useHastePotion(300);
			}
		}
	}

	public boolean nearTeleport(int nx, int ny) {
		int rdir = random.nextInt(8);
		int dir;
		for (int i = 0; i < 8; i++) {
			dir = rdir + i;
			if (dir > 7) {
				dir -= 8;
			}
			switch(dir){
			case 1:nx++; ny--;break;
			case 2:nx++;break;
			case 3:nx++; ny++;break;
			case 4:ny++;break;
			case 5:nx--; ny++;break;
			case 6:nx--;break;
			case 7:nx--; ny--;break;
			case 0:ny--;break;
			default:break;
			}
			if (getMap().isPassable(nx, ny)) {
				dir += 4;
				if (dir > 7) {
					dir -= 8;
				}
				teleport(nx, ny, dir);
				setCurrentMp(getCurrentMp() - 10);
				return true;
			}
		}
		return false;
	}
	
	public void teleportDmgAction(){
		isTeleportDmgAction = false;
		L1Location newLoc = getLocation().randomLocation(3, 6, false);
		teleport(newLoc.getX(), newLoc.getY(), getMoveState().getHeading());
		setCurrentMp(getCurrentMp() - 10);
	}
	
	public void teleport(int nx, int ny, int dir, int mapid) {
		getMap().setPassable(getLocation(), true);
		setX(nx);
		setY(ny);
		getMoveState().setHeading(dir);
		getMap().setPassable(getLocation(), false);
	}

	public void teleport(int nx, int ny, int dir) {
		L1World world = L1World.getInstance();
		S_Effect effect = new S_Effect(getId(), 169);
		S_RemoveObject s_deleteNewObject = new S_RemoveObject(this);
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			pc.sendPackets(effect);
			pc.removeKnownObject(this);
			pc.sendPackets(s_deleteNewObject);
		}
		effect.close();
		effect = null;
		s_deleteNewObject.close();
		s_deleteNewObject = null;
		if (!isPassableNpc()) {
			getMap().setPassable(getLocation(), true);
		}
		setX(nx);
		setY(ny);
		getMoveState().setHeading(dir);
		if(!getNpcTemplate().isTeleport())allTargetClear();
		world.onMoveObject(this);
		if (!isPassableNpc()) {
			getMap().setPassable(getLocation(), false);
		}
		setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
	}
	
	public void move(int nx, int ny, int head) {
		getMap().setPassable(getLocation(), true);
		setX(nx);
		setY(ny);
		getMoveState().setHeading(head);
		getMap().setPassable(getLocation(), false);
		L1World.getInstance().onMoveObject(this);
		broadcastPacket(new S_MoveCharPacket(this), true);
		setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
	}
	
	public void trapTelePort(){
		switch(getNpcId()){
		case 5095:// 모래 폭풍
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 2)) {
				if (pc == null || pc.isDead()) {
					continue;
				}
				L1Location newLocation = pc.getLocation().randomLocation(30, true);
				pc.getTeleport().start(newLocation, pc.getMoveState().getHeading(), true);
				newLocation = null;
			}
			break;
		case 7210040:// 하이네필드 트랩
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 2)) {
				if (pc == null || pc.isDead()) {
					continue;
				}
				L1Location newLocation = pc.getLocation().randomLocation(50, true);
				pc.getTeleport().start(newLocation, pc.getMoveState().getHeading(), true);
				newLocation = null;
			}
			break;
		case 800702:// 월드 공성전 기란성 이동
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 2)) {
				if (pc == null || pc.isDead()) {
					continue;
				}
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_GIRAN);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
			}
			break;
		case 800703:// 월드 공성전 오크요새 이동
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 2)) {
				if (pc == null || pc.isDead()) {
					continue;
				}
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_ORC);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
			}
			break;
		case 5178:// 여왕개미방 모래폭풍
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 2)) {
				if (pc == null || pc.isDead()) {
					continue;
				}
				if (!pc.getInventory().checkItemOne(L1ItemId.DEATH_PENALTY_SHIELD_ITEMS)) {
					pc.sendPackets(L1ServerMessage.sm5359);// 입장에 필요한 조건이 맞지 않습니다.
					continue;
				}
				if (Config.DUNGEON.ANT_QUEEN_INCLUDE) {
					int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.ERJABE_CROWN);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
				} else {
					if (pc.isLancer()) {
						int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.ERJABE_LANCER);
						pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
					} else {
						int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.ERJABE_CROWN);
						pc.getTeleport().start(loc[0], loc[1], (short) (loc[2] + pc.getType()), pc.getMoveState().getHeading(), true, true);
					}
				}
			}
			break;
		case 5177:// 여왕개미방 입장 트랩
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 2)) {
				if (pc == null || pc.isDead()) {
					continue;
				}
				if (!pc.getInventory().checkItemOne(L1ItemId.DEATH_PENALTY_SHIELD_ITEMS)) {
					pc.sendPackets(L1ServerMessage.sm5359);// 입장에 필요한 조건이 맞지 않습니다.
					continue;
				}
				L1Location newLocation = new L1Location(32897, 32862, (pc.getMapId() + (Config.DUNGEON.ANT_QUEEN_INCLUDE ? 10 : (pc.isLancer() ? 1 : 10)))).randomLocation(5, true);
				pc.getTeleport().start(newLocation, pc.getMoveState().getHeading(), true);
				newLocation = null;
			}
			break;
		case 800200:// 균열의 틈새 고대 신의 사원
			int serverid = ((L1MerchantInstance) this).getMoveMapId();
			if (serverid <= 0) {
				break;
			}
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 2)) {
				if (pc == null || pc.isDead() || serverid != pc.getClanid()) {
					continue;
				}
				pc.getDungoenTimer().enterAncientGodEmployee();
			}
			break;
		}
		setSleepTime(1000);// 체크할 딜레이(1초)
	}

	// ----------From L1Character-------------
	private String _desc; 
	private boolean _Agro; 
	private boolean _Agro_invis; 
	private boolean _Agro_poly; 
	private int _homeX; 
	private int _homeY; 
	private boolean _reSpawn; 
	private int _lightSize; 
	private boolean _weaponBreaked; 
	private int _hiddenStatus; 
	private int _movementDistance;
	private int _emblemId;

	public String getDesc() {
		return _desc;
	}
	public void setDesc(String val) {
		_desc = val;
	}

	public boolean isAgro() {
		return _Agro;
	}
	public void setAgro(boolean flag) {
		_Agro = flag;
	}
	
	public boolean isAgroInvis() {
		return _Agro_invis;
	}
	public void setAgroInvis(boolean flag) {
		_Agro_invis = flag;
	}
	
	public boolean isAgroPoly() {
		return _Agro_poly;
	}
	public void setAgroPoly(boolean flag) {
		_Agro_poly = flag;
	}

	public int getHomeX() {
		return _homeX;
	}
	public void setHomeX(int i) {
		_homeX = i;
	}

	public int getHomeY() {
		return _homeY;
	}
	public void setHomeY(int i) {
		_homeY = i;
	}

	public boolean isReSpawn() {
		return _reSpawn;
	}
	public void setRespawn(boolean flag) {
		_reSpawn = flag;
	}

	public int getLightSize() {
		return _lightSize;
	}
	public void setLightSize(int i) {
		_lightSize = i;
	}

	public boolean isWeaponBreaked() {
		return _weaponBreaked;
	}
	public void setWeaponBreaked(boolean flag) {
		_weaponBreaked = flag;
	}

	public int getHiddenStatus() {
		return _hiddenStatus;
	}
	public void setHiddenStatus(int i) {
		_hiddenStatus = i;
	}

	public int getMovementDistance() {
		return _movementDistance;
	}
	public void setMovementDistance(int i) {
		_movementDistance = i;
	}
	
	public int getEmblemId() {
		return _emblemId;
	}
	public void setEmblemId(int i) {
		_emblemId = i;
	}

	public int calcSleepTime(int sleepTime, int type) {
		switch (getMoveState().getMoveSpeed()) {
		case 0:break;
		case 1:sleepTime -= (sleepTime >> 2);break;
		case 2:sleepTime <<= 1;break;
		}
		if (getMoveState().getBraveSpeed() == 1 || getMoveState().getBraveSpeed() == 6) {
			sleepTime -= (sleepTime >> 2);
		}
		if (skillStatus.hasSkillEffect(L1SkillId.WIND_SHACKLE)) {
			if (type == ATTACK_SPEED || type == MAGIC_SPEED) {
				sleepTime += (sleepTime >> 2);
			}
		}
		return sleepTime;
	}

	protected void setAiRunning(boolean aiRunning) {
		_aiRunning = aiRunning;
	}
	protected boolean isAiRunning() {
		return _aiRunning;
	}

	protected void setFirstAttack(boolean firstAttack) {
		_firstAttack = firstAttack;
	}
	protected boolean isFirstAttack() {
		return _firstAttack;
	}

	protected void setSleepTime(int sleep_time) {
		_sleep_time = sleep_time;
	}
	protected int getSleepTime() {
		return _sleep_time;
	}

	protected void setDeathProcessing(boolean deathProcessing) {
		_deathProcessing = deathProcessing;
	}
	protected boolean isDeathProcessing() {
		return _deathProcessing;
	}

	public int drainMana(int drain) {
		if (_drainedMana >= GameServerSetting.MANA_DRAIN_LIMIT_PER_NPC) {
			return 0;
		}
		int result = Math.min(drain, getCurrentMp());
		if (_drainedMana + result > GameServerSetting.MANA_DRAIN_LIMIT_PER_NPC) {
			result = GameServerSetting.MANA_DRAIN_LIMIT_PER_NPC - _drainedMana;
		}
		_drainedMana += result;
		return result;
	}

	public boolean _destroyed;
	public boolean isDestroyed() {
		return _destroyed;
	}

	protected void transform(int transformId) {
		stopHpRegeneration();
		stopMpRegeneration();
		int transformGfxId = getNpcTemplate().getTransformGfxId();
		if (transformGfxId != 0) {
			broadcastPacket(new S_Effect(getId(), transformGfxId), true);
		}
		L1Npc npcTemplate = NpcTable.getInstance().getTemplate(transformId);
		if (npcTemplate == null) {
			System.out.println(String.format("[L1NpcInstance] transform template empty! transId: %d", transformId));
			npcTemplate = getNpcTemplate();
		}
		setting_template(npcTemplate);
		broadcastPacket(new S_Polymorph(getId(), npcTemplate.getSpriteId(), 0, npcTemplate.getDesc(), npcTemplate.getClassId()), true);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
	}
	
	public void randomWalk() {
		tagertClear();
		int dir = checkObject(getX(), getY(), getMapId(), random.nextInt(20));
		if (dir != -1) {
			setDirectionMove(dir);
			setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
		}
	}
	
	public void setRest(boolean _rest) {
		this._rest = _rest;
	}
	public boolean isRest() {
		return _rest;
	}

	public boolean isResurrect() {
		return _isResurrect;
	}
	public void setResurrect(boolean flag) {
		_isResurrect = flag;
	}

	@Override
	public synchronized void resurrect(int hp) {
		if (_destroyed) {
			return;
		}
		if (_deleteTask != null) {
			if (!_future.cancel(false)) {
				return;
			}
			_deleteTask = null;
			_future = null;
		}

		getMap().setPassable(getLocation(), false);			
		super.resurrect(hp);
		startHpRegeneration();
		startMpRegeneration();
		L1SkillUse skill = new L1SkillUse(true);
		skill.handleCommands(null, L1SkillId.CANCELLATION, getId(), getX(),	getY(), 0, L1SkillUseType.LOGIN, this);
		skill = null;
	}

	protected synchronized void startDeleteTimer() {
		if (_deleteTask != null) {
			return;
		}
		_deleteTask = new NpcDeleteTimer(this);
		_future = GeneralThreadPool.getInstance().schedule(_deleteTask,	DELETE_TIME);
	}

	protected static class NpcDeleteTimer implements Runnable {
		private L1NpcInstance _npc;
		protected NpcDeleteTimer(L1NpcInstance npc) {
			_npc = npc;
		}

		@Override
		public void run() {
			if (_npc == null || !_npc.isDead() || _npc._destroyed) {
				return;
			}
			try {
				if (_npc instanceof L1PetInstance) {
			        L1PetInstance pet = (L1PetInstance) _npc;
			        if (pet.getMaster() != null) {
			            pet.delete();
			            return;
			        }
			    }
				_npc.deleteMe();
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
	}

	public boolean isInMobGroup() {
		return _mobGroupInfo != null;
	}
	public L1MobGroupInfo getMobGroupInfo() {
		return _mobGroupInfo;
	}
	public void setMobGroupInfo(L1MobGroupInfo m) {
		_mobGroupInfo = m;
	}

	public int getMobGroupId() {
		return _mobGroupId;
	}
	public void setMobGroupId(int i) {
		_mobGroupId = i;
	}

	public void startChat(int chatTiming) {
		if (chatTiming == CHAT_TIMING_APPEARANCE && this.isDead()) {
			return;
		}
		if (chatTiming == CHAT_TIMING_DEAD && !this.isDead()) {
			return;
		}
		if (chatTiming == CHAT_TIMING_HIDE && this.isDead()) {
			return;
		}
		if (chatTiming == CHAT_TIMING_GAME_TIME && this.isDead()) {
			return;
		}

		int npcId = this.getNpcTemplate().getNpcId();
		L1NpcChat npcChat = null;
		switch(chatTiming){
		case CHAT_TIMING_APPEARANCE:npcChat = NpcChatTable.getInstance().getTemplateAppearance(npcId);break;
		case CHAT_TIMING_DEAD:		npcChat = NpcChatTable.getInstance().getTemplateDead(npcId);break;
		case CHAT_TIMING_HIDE:		npcChat = NpcChatTable.getInstance().getTemplateHide(npcId);break;
		case CHAT_TIMING_GAME_TIME:	npcChat = NpcChatTable.getInstance().getTemplateGameTime(npcId);break;
		default:break;
		}
		if (npcChat == null || npcChat.percent <= 0) {
			return;
		}
		
		L1NpcChatTimer npcChatTimer;
		if (random.nextInt(100) < npcChat.percent) {
		    npcChatTimer = npcChat.isRepeat() ? new L1NpcChatTimer(this, npcChat, npcChat.getRepeatInterval()) : new L1NpcChatTimer(this, npcChat);
		    npcChatTimer.startChat(npcChat.getStartDelayTime());
		}
	}

	public void setNum(int n) {
		num = n;
	}
	public int getNum() {
		return num;
	}
	
	private int moveMapId;
	public void setMoveMapId(int id){
		moveMapId = id;
	}
	public int getMoveMapId(){
		return moveMapId;
	}
	
	private L1Notification _notification_info;
	public L1Notification get_notification_info() {
		return _notification_info;
	}
	public void set_notification_info(L1Notification val) {
		_notification_info = val;
	}
}


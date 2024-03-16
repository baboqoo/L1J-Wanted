package l1j.server.server.model.Instance;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.GameSystem.ai.AiLoader;
import l1j.server.GameSystem.ai.bean.AiDropObject;
import l1j.server.GameSystem.ai.brain.AiBrainThread;
import l1j.server.GameSystem.ai.constuct.AiBrainStatus;
import l1j.server.GameSystem.ai.constuct.AiLoc;
import l1j.server.GameSystem.ai.constuct.AiMent;
import l1j.server.GameSystem.ai.constuct.AiPledge;
import l1j.server.GameSystem.ai.constuct.AiArea;
import l1j.server.GameSystem.ai.constuct.AiType;
import l1j.server.GameSystem.astar.AStar;
import l1j.server.GameSystem.astar.Node;
import l1j.server.GameSystem.astar.World;
import l1j.server.common.data.ChatType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.controller.action.GameTimeNight;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.datatables.MagicDollInfoTable.L1DollInfo;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.RevengeTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1HateList;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1World;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.model.sprite.AcceleratorChecker;
import l1j.server.server.serverpackets.S_Alignment;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_Fishing;
import l1j.server.server.serverpackets.action.S_MoveCharPacket;
import l1j.server.server.serverpackets.message.S_ChatMessageNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.object.S_AIObject;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;

/**
 * AI 인스턴스 클래스
 * @author LinOffice
 */
public class L1AiUserInstance extends L1PcInstance {
	private static Logger _log						= Logger.getLogger(L1AiUserInstance.class.getName());
	private static final long serialVersionUID		= 1L;
	
	private static final byte[] HEADING_TABLE_X		= { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte[] HEADING_TABLE_Y		= { -1, -1, 0, 1, 1, 1, 0, -1 };
	
	private AiBrainThread _brain;
	private S_Effect teleportEffect;
	private S_RemoveObject removeObj;
	private boolean _help;
	private FastTable<L1ItemInstance> _passItems;// 제외할 아이템
	private L1Character _aiTarget;
	private L1ItemInstance _aiTargetItem;
	private L1HateList _targetList;
	private FastTable<L1ItemInstance> _targetItemList;
	private long _aiNoTargetTime;
    private int _aiLocX, _aiLocY;
    private int _aiMoveCount;
    private int _aiSleepTime;
    private AiBrainStatus _aiBrainStatus;
    private boolean _damageMotion, _buffMotion;
	private AiPledge _aiPledge;
	private AiType _aiType;
	private L1Skills[] _aiBuffs;
	private L1ItemInstance _dollItem;
	
	// instance 생성시 초기화할 변수
	private L1ItemInstance[] _aiHealPotion;
	private L1ItemInstance _aiGreenPotion, _aiBravePotion;
	private FastTable<AiDropObject> _dropList;
	private AStar _aStar;
	private int[][] _iPath;
	private Node _tail;
	private int _iCurrentPath;
	private HashSet<L1PcInstance> _teleportSubjects;
	
	public L1AiUserInstance() {
		super();
		_aiHealPotion		= new L1ItemInstance[3];
		_dropList			= new FastTable<AiDropObject>();
		_targetList			= new L1HateList();
		_targetItemList 	= new FastTable<L1ItemInstance>();
		_passItems			= new FastTable<L1ItemInstance>();
        _aiMoveCount		= CommonUtil.random(50, 200);
        _teleportSubjects	= new HashSet<L1PcInstance>();
        _iPath				= new int[301][2];
        _aStar				= new AStar();
        _aStar.setCha(this);
	}
	
	public int getAttackRange(){
		if (attackRange == 0) {
			setAttackRange();
		}
		return attackRange;
	}
	
	public void setAttackRange(){
		int range = 1;// 기본 1
		L1ItemInstance weapon = getWeapon();
		if (weapon != null) {
			switch(weapon.getItem().getWeaponType()){
			case BOW:
			case GAUNTLET:
			case SINGLE_BOW:
				range = 15;
				break;
			case SPEAR:
			case SINGLE_SPEAR:
			case CHAINSWORD:
				range = spearPolyRange(getSpriteId());
				break;
			case KEYRINGK:
				range = 4;
				break;
			default:
				break;
			}
		}
		attackRange = range;
	}
	
	public boolean isHelp(){
		return _help;
	}
	
	public boolean isDamageMotion(){
		return _damageMotion;
	}
	public void setDamageMotion(boolean flag){
		_damageMotion = flag;
	}
	
	public boolean isBuffMotion(){
		return _buffMotion;
	}
	public void setBuffMotion(boolean flag){
		_buffMotion = flag;
	}
	
	public FastTable<L1ItemInstance> getPassItems(){
		return _passItems;
	}
	public void addPassItem(L1ItemInstance item){
		if (_passItems.contains(item)) {
			return;
		}
		_passItems.add(item);
	}
	
	public L1Character getAiTarget() {
        return _aiTarget;
    }   
    public void setAiTarget(L1Character cha) {
    	_aiTarget = cha;
    }
    
	public L1ItemInstance getAiTargetItem() {
		return _aiTargetItem;
	}
	public void setAiTargetItem(L1ItemInstance _targetItem) {
		this._aiTargetItem = _targetItem;
	}
	
	public L1HateList getTargetList() {
        return _targetList;
    }
    public void addTargetList(L1Character cha) {
        if (_targetList.containsKey(cha)) {
        	return;
        }
        _targetList.add(cha, 0);
    }
    public void removeTargetList(L1Character cha) {
        if (cha == null || !_targetList.containsKey(cha)) {
        	return;
        }
        _targetList.remove(cha);
    }
    
    public FastTable<L1ItemInstance> getTargetItemList() {
        return _targetItemList;
    }
    public void addTargetItemList(L1ItemInstance item) {
        if (_targetItemList.contains(item)) {
        	return;
        }
        _targetItemList.add(item);
    }
    public void removeTargetItemList(L1ItemInstance item) {
        if (item == null || !_targetItemList.contains(item)) {
        	return;
        }
        _targetItemList.remove(item);
    }
    
    public int getAiLocX() {
        return _aiLocX;
    }
    public void setAiLocX(int i) {
    	_aiLocX = i;
    }
    
    public int getAiLocY() {
        return _aiLocY;
    }
    public void setAiLocY(int i) {
    	_aiLocY = i;
    }
    
    public int getAiMoveCount() {
        return _aiMoveCount;
    }
	
	public int getAiSleepTime() {
		return _aiSleepTime;
	}
	public void setAiSleepTime(int _sleepTime) {
		this._aiSleepTime = _sleepTime;
	}
	
	public AiBrainStatus getAiBrainStatus() {
		return _aiBrainStatus;
	}
	public void setAiBrainStatus(AiBrainStatus _status) {
		this._aiBrainStatus = _status;
	}
	
	public AiPledge getAiPledge() {
		return _aiPledge;
	}
	public void setAiPledge(AiPledge val) {
		this._aiPledge = val;
	}
	
	public AiType getAiType() {
		return _aiType;
	}
	public void setAiType(AiType val) {
		_aiType = val;
	}
	
	public L1Skills[] getAiBuffs(){
		return _aiBuffs;
	}
	public void setAiBuffs(L1Skills[] buffs){
		_aiBuffs = buffs;
	}
	
	public void addDropList(AiDropObject drop){
		_dropList.add(drop);
	}
	
	public L1ItemInstance[] getAiHealPotion(){
		return _aiHealPotion;
	}
	
	public L1ItemInstance getAiGreenPotion(){
		return _aiGreenPotion;
	}
	
	public void setAiGreenPotion(L1ItemInstance potion){
		_aiGreenPotion = potion;
	}
	
	public L1ItemInstance getAiBravePotion(){
		return _aiBravePotion;
	}
	
	public void setAiBravePotion(L1ItemInstance potion){
		_aiBravePotion = potion;
	}
	
	public L1ItemInstance getDollItem(){
		return _dollItem;
	}
	public void setDollItem(L1ItemInstance item){
		_dollItem = item;
	}
	
	public void tagertClear() {
    	L1Character target = getAiTarget();
    	if (target == null) {
    		return;
    	}
    	getTargetList().remove(target);
    	setAiTarget(null);
    }
    
    public void targetAllClear(){
    	getPassItems().clear();
    	getTargetItemList().clear();
    	setAiTargetItem(null);
    	getTargetList().clear();
    	setAiTarget(null);
    }
    
    public L1Character getTarget() {
    	L1Character realTarget = null;
    	try {
    		for (int i=0; i<getTargetList().toTargetArrayList().size(); ++i) {
    			L1Character target = getTargetList().toTargetArrayList().get(i);
                if (target.isDead() || !glanceCheck(15, target.getX(), target.getY(), target instanceof L1DoorInstance)) {
                	removeTargetList(target);
                	setAiTarget(null);
                } else if (realTarget == null || !target.isDead() && getDistance(getX(), getY(), target.getX(), target.getY()) < getDistance(getX(), getY(), realTarget.getX(), realTarget.getY())) {
                	realTarget = target;
                	break;
                }
            }
    		return realTarget;
    	} catch (Exception e) {
    		getTargetList().clear();
    		setAiTarget(null);
    		return realTarget;
    	}
    }
    
    public L1ItemInstance getTargetItem() {
    	L1ItemInstance realTarget = null;
    	try {
    		for (int i=0; i<getTargetItemList().size(); ++i) {
    			L1ItemInstance targetItem = getTargetItemList().get(i);
    			if ((targetItem.getItemOwner() != null && targetItem.getItemOwner().getId() != this.getId()) || _passItems.contains(targetItem) || !glanceCheck(15, targetItem.getX(), targetItem.getY(), false)) {
    				removeTargetItemList(targetItem);
                	setAiTargetItem(null);
    			} else if (realTarget == null || getDistance(getX(), getY(), targetItem.getX(), targetItem.getY()) < getDistance(getX(), getY(), realTarget.getX(), realTarget.getY())) {
                	realTarget = targetItem;
                	break;
    			}
            }
    		return realTarget;
    	} catch (Exception e) {
    		getTargetItemList().clear();
    		setAiTargetItem(null);
    		return realTarget;
    	}
    }
	
	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
		boolean helpHp = this.isHalfHp();
		if (!_help && helpHp) {
			_help = true;
		} else if (_help && !helpHp) {
			_help = false;
		}
	}
	
	@Override
	public void setCurrentMp(int i) {
		if (i < 10) {
			i = getMaxMp();// mp 충전
		}
		super.setCurrentMp(i);
	}
	
	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (getCurrentHp() > 0 && !isDead()) {
			if (attacker == null) {
				return;
			}
			if (attacker != this && !knownsObject(attacker) && attacker.getMapId() == getMapId()) {
				attacker.onPerceive(this);
			}
			if (damage > 0){
				if (getAiTarget() == null || (getAiTarget() instanceof L1MonsterInstance && attacker instanceof L1PcInstance)) {// 타겟 변경
					setAiTarget(attacker);
					addTargetList(attacker);
					L1AiUserInstance aiMember = null;
					if (isInParty()) {
						for (L1PcInstance member : L1World.getInstance().getVisiblePartyPlayer(this, 10)) {
							aiMember = (L1AiUserInstance) member;
							if (aiMember == this || aiMember.getAiTarget() != null && aiMember.getAiTarget() instanceof L1PcInstance) {// 이미 전투중인 멤버 제외
								continue;
							}
							if (aiMember._help || !glanceCheck(10, attacker.getX(), attacker.getY(), false)) {
								continue;
							}
							aiMember.setAiTarget(attacker);
							aiMember.addTargetList(attacker);
						}
					}
				} else if (attacker instanceof L1MonsterInstance && !getTargetList().toTargetArrayList().contains(attacker)) {
					addTargetList(attacker);
				}
				_damageMotion = true;
				if (attacker instanceof L1PcInstance || attacker instanceof L1DoppelgangerInstance) {
					L1PinkName.onAction(this, attacker);
				}
				removeSleepSkill();
			} else if (damage < 0 && attacker instanceof L1PcInstance) {
				L1PinkName.onHelp(this, attacker);
			}
			
			pressureCheck(attacker, (double) damage);
			int newHp = getCurrentHp() - damage;
			if (newHp <= 10 && isElf() && skillStatus.hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
				newHp = soulBarrier(attacker, newHp, damage);
			}
			if (newHp > getMaxHp()) {
				newHp = getMaxHp();
			}
			if (newHp <= 0) {
				death(attacker, true);
			}
			if (newHp > 0) {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) {
			death(attacker, true);
		}
	}
	
	@Override
	public void death(L1Character lastAttacker, boolean deathPenalty) {
		synchronized (this) {
			if (isDead()) {
				return;
			}
			setDead(true);
			setActionStatus(ActionCodes.ACTION_Die);
		}
		if (lastAttacker instanceof L1PcInstance) {
			L1PcInstance _atker = (L1PcInstance) lastAttacker;
			send_ment(AiLoader.getInstance().getMent(AiMent.DEATH));
			
			/** ??가 적 ??를 죽엿습니다 **/
			if (_atker.isInParty()) {
				S_ServerMessage message = new S_ServerMessage(3690, lastAttacker.getName(), getName());
				for (L1PcInstance member : _atker.getParty().getMembersArray()) {
					member.sendPackets(message);
				}
				message.clear();
				message = null;
			} else {
				_atker.sendPackets(new S_ServerMessage(3691, lastAttacker.getName(), getName()), true);
			}
		}
		GeneralThreadPool.getInstance().execute(new Death(lastAttacker, deathPenalty));
	}
	
	private class Death implements Runnable {
		L1Character _lastAttacker;
		boolean _deathPenalty;

		Death(L1Character cha, boolean deathPenalty) {
			_lastAttacker = cha;
			_deathPenalty = deathPenalty;
		}
		
		@Override
		public void run() {
			if (getTeleport().isTeleport()) {
				getTeleport().setTeleport(false);
				GeneralThreadPool.getInstance().schedule(this, 300);
				return;
			}
			L1Character lastAttacker = _lastAttacker;
			_lastAttacker = null;
			setCurrentHp(0);
			getMap().setPassable(getLocation(), true);
			int spriteId = 0;
            if (skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
            	spriteId = getSpriteId();
                setTempCharGfxAtDead(spriteId);
                skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE);
            } else if (skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
            	spriteId = getSpriteId();
                setTempCharGfxAtDead(spriteId);
                skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION);
            } else if (skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {
            	spriteId = getSpriteId();
                setTempCharGfxAtDead(spriteId);
                skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL);
            } else {
                setTempCharGfxAtDead(getClassId());
            }
            L1SkillUse l1skilluse = new L1SkillUse(true);
            l1skilluse.handleCommands(L1AiUserInstance.this, L1SkillId.CANCELLATION, getId(), getX(), getY(), 0, L1SkillUseType.LOGIN);
            l1skilluse = null;
            if (spriteId != 0) {
            	broadcastPacket(new S_Polymorph(getId(), spriteId, getCurrentWeapon()), true);
            }
			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
            GeneralThreadPool.getInstance().schedule(new DeathLateDisconnectTimer(L1AiUserInstance.this), 300000);// 죽음 체크 타이머
            if (!getMap().isEnabledDeathPenalty()) {
            	return;
            }
            if (castleWarResult() == true) {
            	return;// 공성존
            }
            if (getRegion() != L1RegionStatus.NORMAL) {
            	return;// 노말존
            }
            if (lastAttacker instanceof L1PcInstance && getLevel() >= 75 && lastAttacker.getLevel() >= 75) {
            	RevengeTable.getInstance().checkRevenge(L1AiUserInstance.this, (L1PcInstance) lastAttacker);// 복수 시스템
            }
            deathPenalty(lastAttacker);// 아이템 드랍
            L1PcInstance player = null;
            if (lastAttacker instanceof L1PcInstance) {
            	player = (L1PcInstance) lastAttacker;
            }
            if (_deathPenalty && player != null) {// PK멘트
            	if (getAlignment() >= 0 && isPinkName() == false) {
                	if (player.getAlignment() < 30000) {
                        player.setPKcount(player.getPKcount() + 1);
                        player.setLastPk();
                    }
                    int align = player.getLevel() < 50 ? -1 * (int) ((Math.pow(player.getLevel(), 2) * 4)) : -1 * (int) ((Math.pow(player.getLevel(), 3) * 0.08));
                    align = IntRange.ensure(align, -32768, player.getAlignment() - 1000);
                    player.setAlignment(align);
                    player.broadcastPacketWithMe(new S_Alignment(player.getId(), player.getAlignment()), true);
                    player.sendPackets(new S_PacketBox(S_PacketBox.BATTLE_SHOT, L1AiUserInstance.this.getId()), true);
                } else {
                    setPinkName(false);
                }
            }
		}
	}
	
	class DeathLateDisconnectTimer implements Runnable {
		private L1AiUserInstance _pc;
		public DeathLateDisconnectTimer(L1AiUserInstance pc) {
			_pc = pc;
		}

		public void run() {
			if (_pc == null) {
				return;
			}
			try {
				if (!_pc.isDead()) {
					return;
				}
				_pc.logout();
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	private void deathPenalty(L1Character lastAttacker){
		AiDropObject obj = _dropList.get(CommonUtil.random(_dropList.size()));
		if (obj == null) {
			return;
		}
		L1ItemInstance drop = ItemTable.getInstance().createItem(obj.getItemId());// 드랍 시킬 아이템
		if (drop == null) {
			System.out.println(String.format("[L1AiUserInstance] DROP_ITEM_EMPTY : ITEM_ID(%d), CLASS_TYPE(%d)", obj.getItemId(), this.getClassNameEn()));
			return;
		}
		drop.setCount(obj.getCount());
		L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
		if (lastAttacker instanceof L1PcInstance) {
			drop.startItemOwnerTimer((L1PcInstance)lastAttacker);// 마지막 어택자에게 소유권
		}
	}
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_AIObject(this), true);
		if (isPinkName()) {
			perceivedFrom.sendPackets(new S_PinkName(getId(), getPinkNameTime()), true);
		}
	}
	
	@Override
	public void updateObject() {
		;// 지우지 마세요! 부모 메소드 호출 안되도록 override
	}
	
	@Override
	public void save() throws Exception {
		;// 지우지 마세요! 부모 메소드 호출 안되도록 override
	}
	
	@Override
	public void saveInventory() {
		;// 지우지 마세요! 부모 메소드 호출 안되도록 override
	}
	
	// TODO AI 월드 접속
	public void login(AiArea aiArea){
		if (teleportEffect == null) {
			teleportEffect			= new S_Effect(getId(), 169);
		}
		if (removeObj == null) {
			removeObj				= new S_RemoveObject(this);
		}
		
		boolean isBrain				= AiType.isBrain(getAiType());
		boolean isScene				= AiType.isScene(getAiType());
		int[] loc					= AiLoc.getLoc(aiArea);
		L1Location location			= null;
		if (isBrain) {
			location				= new L1Location(loc[0], loc[1], loc[2]).randomLocation(10, true);
		} else {
			location				= new L1Location(loc[0], loc[1], loc[2]);
		}
		setLocation(location);
		setActionStatus(0);
		setCurrentHp(getMaxHp());
		setCurrentMp(getMaxMp());
		setDead(false);
		if (isElf()) {
			_isBloodToSoulAuto = true;
		}
		
		L1World world = L1World.getInstance();
		setClan(world.getClan(getClanid()));
		
		if (isScene) {
			S_SceneNoti portal_scene	= new S_SceneNoti(true, System.currentTimeMillis() / 1000, 540009, location.getX(), location.getY());// 머리위 구름
			S_SceneNoti spawn_scene		= new S_SceneNoti(true, System.currentTimeMillis() / 1000, 540006, location.getX(), location.getY());// 등장
			for (L1PcInstance pc : world.getMapPlayer(location.getMapId())) {
				pc.sendPackets(portal_scene);
				pc.sendPackets(spawn_scene);
			}
			portal_scene.clear();
			spawn_scene.clear();
			portal_scene = spawn_scene = null;
		}
		
		// 오브젝트 출현
		world.storeObject(this);
		world.addVisibleObject(this);
		
		// 쓰레드 시작
		if (isBrain) {
			startHpMpRegeneration();// HP/MP회복 타이머 시작
			if (_brain == null) {
				_brain = new AiBrainThread(this);
			}
			_brain.start();// ai의 액션을 처리할 thread 실행
		}
		
		// 낚시
		if (getAiType() == AiType.FISHING) {
			getMoveState().setHeading(loc[3]);
			_fishingX = loc[4];
			_fishingY = loc[5];
			setFishing(true);
			broadcastPacket(new S_Fishing(getId(), ActionCodes.ACTION_Fishing, _fishingX, _fishingY), true);
		}
	}
	
	@Override
	public void logout() {
		try {
			synchronized(this){
				targetAllClear();
				if (isInParty()) {
					getParty().leaveMember(this);
				}
				getMap().setPassable(getLocation(), true);
				skillStatus.clearSkillEffectTimer();
				skillStatus.clearSkillDelay();
				L1World world = L1World.getInstance();
				world.removeVisibleObject(this);
				world.removeObject(this);// 월드상에서 ai를 제거
                notifyPlayersLogout(getKnownPlayers());
                notifyPlayersLogout(world.getRecognizePlayer(this));
                removeAllKnownObjects();
                stopEquipmentTimer();
				setDead(true);
				setAiBrainStatus(null);
				stopHpMpRegeneration();
				if (teleportEffect != null) {
					teleportEffect.clear();
					teleportEffect = null;
				}
				if (removeObj != null) {
					removeObj.clear();
					removeObj = null;
				}
				if (isElf()) {
					_isBloodToSoulAuto = false;
				}
			}
		}catch(Exception e){
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public boolean doPoly(){
		synchronized(this){
			int polyId = L1CharacterInfo.RANKING_POLYS[getType()][getGender().toInt()];
			skillStatus.setSkillEffect(L1SkillId.SHAPE_CHANGE, 1800000);
			setSpriteId(polyId);
			broadcastPacket(new S_Polymorph(getId(), polyId, 0), true);
			return true;
		}
	}
	
	public boolean useDoll(){
		synchronized(this){
			L1ItemInstance dollItem = this._dollItem;
			if (dollItem == null) {
				return false;
			}
			L1DollInfo info		= MagicDollInfoTable.getDollInfo(dollItem.getItemId());
			if (info == null) {
				return false;
			}
			L1Npc template		= NpcTable.getInstance().getTemplate(info.getDollNpcId());
			L1DollInstance doll	= new L1DollInstance(template, this, info, dollItem);
			doll.broadcastPacket(new S_Effect(doll.getId(), 5935), true);
			return true;
		}
	}
	
	public void searchTarget() {
    	checkTarget();
    	L1GroundInventory inv	= null;
    	L1PcInstance pc			= null;
    	L1MonsterInstance mon	= null;
    	AiType ai_type			= getAiType();
    	for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {// 화면 내 오브젝트
    		if (obj == null || obj instanceof L1AiUserInstance) {
    			continue;
    		}
    		switch (ai_type) {
    		case COLOSEUM:
    			if (obj instanceof L1PcInstance == false) {
        			continue;
        		}
    			break;
    		case HUNT:
    			if (!(obj instanceof L1MonsterInstance || obj instanceof L1GroundInventory)) {
        			continue;
        		}
    			break;
    		case SCARECROW_ATTACK:
    			if (obj instanceof L1ScarecrowInstance == false) {
        			continue;
        		}
    			addTargetList((L1ScarecrowInstance) obj);
    			continue;
    		default:
    			break;
    		}
    		
    		if (!(obj instanceof L1GroundInventory || obj instanceof L1PcInstance || obj instanceof L1MonsterInstance)) {
    			continue;
    		}
    		if (obj instanceof L1GroundInventory) {
    			inv = (L1GroundInventory) obj;
    			for (L1ItemInstance item : inv.getItems()) {
    				if (_passItems.contains(item) || (item.getItemOwner() != null && item.getItemOwner().getId() != this.getId()) || !glanceCheck(15, item.getX(), item.getY(), false)) {
    					continue;
    				}
        			addTargetItemList(item);
    			}
    		} else if (obj instanceof L1PcInstance) {
    			pc = (L1PcInstance)obj;
    			if (pc.isDead() || pc.getCurrentHp() <= 0 || pc.isInvisble() || !glanceCheck(15, pc.getX(), pc.getY(), false)) {
    				continue;
    			}
                addTargetList(pc);
    		} else {
    			mon = (L1MonsterInstance)obj;
    			if (mon.isDead() || mon.getCurrentHp() <= 0 || mon._destroyed || mon.isInvisble() || mon.getNpcTemplate().isBossMonster() || mon.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE || !glanceCheck(15, mon.getX(), mon.getY(), false)) {
    				continue;
    			}
                addTargetList(mon);
    		}
        }
    }
	
	private void checkTarget() {
    	try {
    		L1Character target = getAiTarget();
    		if (target == null || target.getMapId() != getMapId() || target.isDead() || target.getTileDistance(this) > 30 || target.getCurrentHp() <= 0 || (target.isInvisble() && !getTargetList().containsKey(target))) {
    			if (target != null) {
    				tagertClear();
    			}
    			if (!getTargetList().isEmpty()) {
    				setAiTarget(getTargetList().getMaxHateCharacter());
    				checkTarget();
    			}
    		}
    	} catch (Exception ex) {}
    }
	
	public boolean noTargetTeleport() {// 일정 시간 타겟 없을시
		if (getMapId() == 54 && GameTimeNight.isNight()) {
			return false;
		}
    	long systime = System.currentTimeMillis();
    	if (_aiNoTargetTime == 0L) {
    		_aiNoTargetTime = systime;
    	} else if (getTargetList().toTargetArrayList().size() == 0 && systime >= _aiNoTargetTime + 10000L) {
        	_aiNoTargetTime = systime;
        	return randomTeleport();
        }
    	return false;
    }
	
	public boolean randomTeleport(){
    	if (getTeleport().isTeleport() || isNotTeleport()) {
    		return false;
    	}
    	getTeleport().setTeleport(true);
		try {
			L1Location loc = getLocation().randomLocation(100, true);// 무작위 좌표
			broadcastPacket(teleportEffect);
			Runnable teleport = () -> {
				doTeleport(loc.getX(), loc.getY(), (short)loc.getMapId());
			};
			GeneralThreadPool.getInstance().schedule(teleport, 280L);
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
	}
	
	private void doTeleport(int x, int y, short mapId){
		L1World world = L1World.getInstance();
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(removeObj);// 허상 안남게
		}
		world.moveVisibleObject(this, mapId, x, y);
		getMap().setPassable(getLocation(), true);
		setX(x);
		setY(y);
		setMap(mapId);
		getMap().setPassable(getLocation(), false);
		removeAllKnownObjects();
		sendVisualEffectAtTeleport();// poison
		L1DollInstance doll = getDoll();
		if (doll != null) {
			L1Location dollLoc = getLocation().randomLocation(3, false);
			dollTeleport(doll, dollLoc.getX(), dollLoc.getY(), (short) dollLoc.getMapId(), 5);
			for (L1PcInstance visiblePc : world.getVisiblePlayer(doll)) {
				visiblePc.removeKnownObject(doll);
				_teleportSubjects.add(visiblePc);
			}
		}
		for (L1PcInstance updatePc : _teleportSubjects) {
			updatePc.updateObject();
		}
		_teleportSubjects.clear();
		targetAllClear();
		setCurrentHp(getCurrentHp() + 100);
		_aiMoveCount = 0;
		setAiBrainStatus(AiBrainStatus.MOVE);
		setAiSleepTime(1000);
		getTeleport().setTeleport(false);
	}
    
	private void dollTeleport(L1NpcInstance npc, int x, int y, short map, int head) {// 인형 텔레포트
		L1World.getInstance().moveVisibleObject(npc, map, x, y);
		L1WorldMap worldMap = L1WorldMap.getInstance();
		worldMap.getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), true);
		npc.setX(x);
		npc.setY(y);
		npc.setMap(map);
		npc.getMoveState().setHeading(head);
		worldMap.getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), false);
	}
	
	public boolean isAttack(L1Character target) {
    	try {
    		return target != null && !target.isBind() && !target.getMap().isSafetyZone(target.getLocation()) && !target.isDead() && !target.isInvisble() && isDistance(getX(), getY(), getMapId(), target.getX(), target.getY(), target.getMapId(), 16) && glanceCheck(15, target.getX(), target.getY(), false);
    	} catch (Exception e) {
    		return false;
    	}
    }
	
	public boolean isPickUp(L1ItemInstance item) {
    	try {
    		return item != null && !getPassItems().contains(item) && isDistance(getX(), getY(), getMapId(), item.getX(), item.getY(), item.getMapId(), 16) && glanceCheck(15, item.getX(), item.getY(), false);
    	} catch (Exception e) {
    		return false;
    	}
    }
	
	/**
	 * 방향 이동
	 * @param dir
	 */
	public void setDirectionMove(int dir) {
		if (isDesperado() || isOsiris() || isHold()) {
			return;
		}
		if (_damageMotion){// 대미지 모션 발생
			_damageMotion = false;
			setAiSleepTime(getAcceleratorChecker().getRightInterval(AcceleratorChecker.ACT_TYPE.DMG_MOTION) + 10);
			return;
		}
		if (_buffMotion) {// 버프 모션 발생
			_buffMotion = false;
			setAiSleepTime(getAcceleratorChecker().getRightInterval(AcceleratorChecker.ACT_TYPE.SPELL_NODIR) + 20);
			return;
		}
		if (dir >= 0) {
			int newX = getX() + HEADING_TABLE_X[dir];
			int newY = getY() + HEADING_TABLE_Y[dir];
			if (World.isMapdynamic(newX, newY, getMapId())) {
				return;
			}
			// TODO 이동처리
			getMoveState().setHeading(dir);
			getMap().setPassable(getLocation(), true);
			getLocation().set(newX, newY);
			getMap().setPassable(getLocation(), false);
    		L1World.getInstance().onMoveObject(this);
    		broadcastPacket(new S_MoveCharPacket(this), true);
    		if (++_aiMoveCount >= 7) {
    			_aiMoveCount = 0;
    		}
    		setAiSleepTime(getAcceleratorChecker().getRightInterval(AcceleratorChecker.ACT_TYPE.MOVE) + 10);
    		if (isMoveDecrease()) {
    			broadcastPacket(new S_Effect(getId(), 20460), true);// 이속 감소 디버프 이팩트
    		}
		}
	}
	
	public boolean isDistance(int x, int y, int m, int tx, int ty, int tm, int loc) {
    	int distance = getDistance(x, y, tx, ty);
    	return loc >= distance && m == tm;
    }
	
	private int getDistance(int x, int y, int tx, int ty) {
    	long dx = tx - x;
    	long dy = ty - y;
    	return (int)Math.sqrt(dx * dx + dy * dy);
    }
	
	public int getDir(int x, int y){
        return checkObject(getX(), getY(), getMapId(), moveDirection(x, y));
	}
	
	public static int checkObject(int x, int y, short m, int d) { 
		L1Map map = L1WorldMap.getInstance().getMap(m);
		switch(d){
		case 1:
			if (map.isPassable(x, y, 1))
				return 1;
			if (map.isPassable(x, y, 0))
				return 0;
			if (map.isPassable(x, y, 2))
				return 2;
			break;
		case 2:
			if (map.isPassable(x, y, 2))
				return 2;
			if (map.isPassable(x, y, 1))
				return 1;
			if (map.isPassable(x, y, 3))
				return 3;
			break;
		case 3:
			if (map.isPassable(x, y, 3))
				return 3;
			if (map.isPassable(x, y, 2))
				return 2;
			if (map.isPassable(x, y, 4))
				return 4;
			break;
		case 4:
			if (map.isPassable(x, y, 4))
				return 4;
			if (map.isPassable(x, y, 3))
				return 3;
			if (map.isPassable(x, y, 5))
				return 5;
			break;
		case 5:
			if (map.isPassable(x, y, 5))
				return 5;
			if (map.isPassable(x, y, 4))
				return 4;
			if (map.isPassable(x, y, 6))
				return 6;
			break;
		case 6:
			if (map.isPassable(x, y, 6))
				return 6;
			if (map.isPassable(x, y, 5))
				return 5;
			if (map.isPassable(x, y, 7))
				return 7;
			break;
		case 7:
			if (map.isPassable(x, y, 7))
				return 7;
			if (map.isPassable(x, y, 6))
				return 6;
			if (map.isPassable(x, y, 0))
				return 0;
			break;
		case 0:
			if (map.isPassable(x, y, 0))
				return 0;
			if (map.isPassable(x, y, 7))
				return 7;
			if (map.isPassable(x, y, 1))
				return 1;
			break;
		default:break;
		}
		return -1;
	}
	
	public int moveDirection(int x, int y) {
    	return moveDirection(x, y, getLocation().getLineDistance(new Point(x, y)));
    }
    
	private int moveDirection(int x, int y, double d) {
		int dir = 0;
		if (d > 25D) {
			dir = targetDirection(x, y);
			dir = checkObject(getX(), getY(), getMapId(), dir);
		} else {
			dir = astar(x, y, this.getMapId());
			if (dir == -1) {
				dir = targetDirection(x, y);
			}
		}
		return dir;
	}
	
	private int astar(int x, int y, int m) {
		try {
			_aStar.cleanTail();
			_tail = _aStar.searchTail(this, x, y, m, true);
			try {
				if (_tail != null) {
					_iCurrentPath = -1;
					while (_tail != null) {
						if (_tail.x == getX() && _tail.y == getY()) {// 현재위치 라면 종료
							break;
						}
						if (_iCurrentPath >= _iPath.length - 1) {
							return -1;
						}
						if (isDead()) {
							return -1;
						}
						_iPath[++_iCurrentPath][0]	= _tail.x;
						_iPath[_iCurrentPath][1]	= _tail.y;
						_tail = _tail.prev;
					}
					return _iCurrentPath != -1 ? _aStar.calcheading(getX(), getY(), _iPath[_iCurrentPath][0], _iPath[_iCurrentPath][1]) : -1;
				} else {
					_aStar.cleanTail();
					_tail = _aStar.FindPath2(this, x, y, m, true);
					if (_tail != null && !(_tail.x == getX() && _tail.y == getY())) {
						_iCurrentPath = -1;
						while (_tail != null) {
							if (_tail.x == getX() && _tail.y == getY()) {// 현재위치 라면 종료
								break;
							}
							if (_iCurrentPath >= _iPath.length - 1) {
								return -1;
							}
							if (isDead()) {
								return -1;
							}
							_iPath[++_iCurrentPath][0]	= _tail.x;
							_iPath[_iCurrentPath][1]	= _tail.y;
							_tail = _tail.prev;
						}
						return _iCurrentPath != -1 ? _aStar.calcheading(getX(), getY(), _iPath[_iCurrentPath][0], _iPath[_iCurrentPath][1]) : -1;
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
	
	/**
	 * 멘트 출력
	 * @param ai
	 * @param ments
	 */
	public void send_ment(FastTable<String> ments) {
		if (getAiType() != AiType.AI_BATTLE) {
			return;
		}
		broadcastPacket(new S_ChatMessageNoti(this, ChatType.CHAT_NORMAL, ments.get(CommonUtil.random(ments.size())).getBytes(), null, 0), true);
	}
    
	/**
	 * 메모리 정리
	 */
    public void dispose(){
    	targetAllClear();
    	skillStatus.clearSkillEffectTimer();
    	removeAllKnownObjects();
    	stopEquipmentTimer();
		setDead(true);
		setAiBrainStatus(null);
		_aiHealPotion		= null;
		_dropList			= null;
		_targetList			= null;
		_targetItemList		= null;
		_passItems			= null;
		_teleportSubjects	= null;
		try {
			if (_aStar != null) {
				_aStar.clear();
				_aStar = null;
			}
			_iPath = null;
			if (_tail != null) {
				_tail.clear();
				_tail = null;
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
    }
}


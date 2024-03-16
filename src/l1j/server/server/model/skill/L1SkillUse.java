package l1j.server.server.model.skill;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.server.ActionCodes;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.L1Undead;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.controller.action.GameTimeNight;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1CurseParalysis;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1ArrowInstance;
import l1j.server.server.model.Instance.L1AuctionBoardInstance;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1DwarfInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1HousekeeperInstance;
import l1j.server.server.model.Instance.L1IndunInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.sprite.AcceleratorChecker.ACT_TYPE;
import l1j.server.server.model.sprite.L1Sprite;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.action.S_DamageTime;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_RangeSkill;
import l1j.server.server.serverpackets.action.S_UseAttackSkill;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.spell.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellDelay;
import l1j.server.server.serverpackets.spell.S_SpellLateHandlingNoti;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.StringUtil;

public class L1SkillUse extends L1SkillInfo {
	private static Logger _log = Logger.getLogger(L1SkillUse.class.getName());
	private static final Random random = new Random(System.nanoTime());
	
	private L1Skills _skill;
	private L1SkillActionHandler _handler;
	private int _skillId = -1;
	private int _getBuffIconDuration, _getBuffDuration, _changeBuffDuration;
	
	private int _targetID;
	private int _mpConsume, _hpConsume;
	private int _targetX, _targetY;
	private int _skillTime;
	private L1SkillUseType _type;
	private boolean _isPK;
	// private int _bookmarkId;
	private int _itemObjId;
	private boolean _checkedUseSkill;
	private int _leverage = 10;
	private boolean _isCounterMagic = true;
	private boolean _isLearnEnable;
	private boolean _isTargetAttack;
	private boolean _isTargetNone;

	private L1Character _user;
	private L1Character _target;
	private L1PcInstance _player;
	private L1NpcInstance _npc;
	
	private L1SkillStatus status;

	private int _calcType;
	private static final int PC_PC		= 1;
	private static final int PC_NPC		= 2;
	private static final int NPC_PC		= 3;
	private static final int NPC_NPC	= 4;
	private ArrayList<TargetStatus> _targetList;

	private short _bookmark_mapid;
	private int _bookmark_x, _bookmark_y;
	
	private long _currentTime;

	private boolean _isGlanceCheckFail;
	private boolean _isCriticalDamage;
	private boolean _isDamageHit;

	public L1SkillUse() {
		this(false);
	}
	public L1SkillUse(boolean enable) {
		_isLearnEnable = enable;
	}

	private static class TargetStatus {
		private L1Character _target;
		public TargetStatus(L1Character _cha) {
			_target = _cha;
		}
		public L1Character getTarget() {
			return _target;
		}
		
		private boolean _isCalc = true;
		public TargetStatus(L1Character _cha, boolean _flg) {
			_isCalc = _flg;
		}
		public boolean isCalc() {
			return _isCalc;
		}
	}

	public void setLeverage(int i) {
		_leverage = i;
	}
	public int getLeverage() {
		return _leverage;
	}

	private boolean isCheckedUseSkill() {
		return _checkedUseSkill;
	}
	private void setCheckedUseSkill(boolean flg) {
		_checkedUseSkill = flg;
	}

	public boolean checkUseSkill(L1PcInstance player, int skillid, int target_id, int x, int y, int time, L1SkillUseType type, L1Character attacker) {
		if (player instanceof L1PcInstance 
				&& !_isLearnEnable && NO_HAS_SKILL_CHECK_LIST.contains(skillid) 
				&& !player.getSkill().isLearnActive(skillid, false)) {// 스킬 배운지 체크
			return false;
		}
		
		// This is a potion that gives you 1500 MP directly when you drink it, but after that, you can not trink another one for 20 minutes
		// This is the reason becouse we have an skill, to add the delay, but at the end, is not a skill at all and we don't need to put
		// a new icon or give something extra becouse the MP is obtained directly when the potion has been drinked.
		// So, we need to return false becouse we dont have an entry for this "no skill" in the tables.
		// We can let this small modification out of here, and nothin will happen, but we will get a messagge in the server command.	
		if (skillid == STATUS_WITCH_POTION)
		  return false;

		_skill			= SkillsTable.getTemplate(skillid);
		if (_skill == null) {
			System.out.println(String.format("[L1SkillUse] SKILL_TEMPLATE_EMPTY : SKILL_ID(%d)", skillid));
			return false;
		}

		setCheckedUseSkill(true);
		_targetList		= new ArrayList<TargetStatus>();
		_handler		= _skill.getHandler() == null ? null : _skill.getHandler().copyInstance();// 업무 수행 핸들러 생성
		_skillId		= skillid;
		_targetX		= x;
		_targetY		= y;
		_skillTime		= time;
		_type			= type;
		_currentTime	= System.currentTimeMillis();
		_isTargetAttack	= _skill.getTarget() == L1Skills.SKILL_TARGET.ATTACK;
		_isTargetNone	= _skill.getTarget() == L1Skills.SKILL_TARGET.NONE;
		boolean checkedResult = true;
		
		if (attacker == null) {
			// pc
			_player	= player;
			_user	= _player;
		} else {
			// npc
			_npc	= (L1NpcInstance) attacker;
			_user	= _npc;
		}
		
		status = _user.getSkill();
		
		if (_skillId == POS_WAVE && _user instanceof L1PcInstance && _player.isLancer() && _player._isLancerForm) {
			_targetID	= target_id;
		} else if (_isTargetNone) {
			_targetID	= _user.getId();
			_targetX	= _user.getX();
			_targetY	= _user.getY();
		} else {
			_targetID	= target_id;
		}
		
		switch (type) {
		case NORMAL:
			checkedResult = isNormalSkillUsable();
			break;
		case SPELLSC:
			checkedResult = isSpellScrollUsable();
			break;
		case NPCBUFF:
			checkedResult = true;
			break;
		default:break;
		}
		if (!checkedResult) {
			if (_player != null && (skillid == TELEPORT || skillid == MASS_TELEPORT || skillid == TELEPORT_TO_MATHER)) {
				_player.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			}
			return false;
		}
		if (_skillId == FIRE_WALL || _skillId == LIFE_STREAM || _skillId == VISION_TELEPORT) {
			return true;
		}
		
		L1Object l1object = L1World.getInstance().findObject(_targetID);
		if (_skillId == CAL_CLAN_ADVANCE && !isCallClanAdvence(_player, (L1PcInstance)l1object)) {
			return false;
		}
		if (l1object instanceof L1ItemInstance) {
			_log.fine("skill target item name: " + ((L1ItemInstance) l1object).getViewName());
			return false;
		}
		if (_user instanceof L1PcInstance) {
			_calcType	= l1object instanceof L1PcInstance ? PC_PC : PC_NPC;
		} else if (_user instanceof L1NpcInstance) {
			_calcType	= l1object instanceof L1PcInstance || _isTargetNone ? NPC_PC : NPC_NPC;
		}

		if (_skillId == TELEPORT || _skillId == MASS_TELEPORT || _skillId == TRUE_TARGET) {
			_bookmark_mapid	= (short) target_id;
			_bookmark_x		= x;
			_bookmark_y		= y;
		}
		if (_skillId == SUMMON_MONSTER && _player.getInventory().checkItem(20284)) {
			_bookmark_x		= x;
			_bookmark_y		= y;
		}

		if (_skillId == BRING_STONE /*|| _skillId == BLESSED_ARMOR || _skillId == ENCHANT_WEAPON || _skillId == SHADOW_FANG*/) {
			_itemObjId = target_id;
		}
		_target = (L1Character) l1object;
		if (!(_target instanceof L1MonsterInstance) && _isTargetAttack && _user.getId() != target_id) {
			_isPK = true;
		}
		if (!(l1object instanceof L1Character)) {
			checkedResult = false;
		}
		makeTargetList();// 적용할 character을 설정
		if (_targetList.size() == 0 && _user instanceof L1NpcInstance) {
			checkedResult = false;// 적용할 대상이 없다면 false
		}
		return checkedResult;
	}

	/**
	 * 통상의 스킬 사용시에 사용자 상태로부터 스킬이 사용 가능한가 판단한다
	 * 
	 * @return false 스킬이 사용 불가능한 상태인 경우
	 */
	boolean isNormalSkillUsable() {
		if (_user instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _user;
			if (pc.isParalyzed()) {
				return false;
			}
			if ((pc.isInvisble() || pc.isInvisDelay()) && !isInvisUsableSkill()) {
				return false;
			}
			
			/** 사일런스 상태시 사용 불가 **/
			if (pc.isSilence()
					&& !(_skillId >= SHOCK_STUN && _skillId <= BLOW_ATTACK) 
					&& _skillId != EMPIRE
					&& !(_skillId >= HOWL && _skillId <= TITAN_RISING) && _skillId != TEMPEST
					&& !(_skillId >= JUDGEMENT && _skillId <= BLADE)
					&& _skillId != FORCE_STUN
					&& !(_skillId >= ALTERNATE && _skillId <= CRUEL)) {// 사일런스 상태시 사용 불가
				pc.sendPackets(L1ServerMessage.sm285);
				return false;
			}

			if (!isItemConsume() && !_player.isGm()) {
				pc.sendPackets(new S_SystemMessage("[L1SkillUse] isNormalSkillUsable we can not use the skill" , true), true);
				_player.sendPackets(L1ServerMessage.sm299);
				return false;
			}
		} else if (_user instanceof L1NpcInstance) {
			if (status.hasSkillEffect(CONFUSION)) {
				return false;
			}
			if (status.hasSkillEffect(SILENCE)) {
				status.removeSkillEffect(SILENCE);
				return false;
			}
		}
		
		if (!isHPMPConsume()) {
			return false;
		}
		return true;
	}

	boolean isSpellScrollUsable() {
		L1PcInstance pc = (L1PcInstance) _user;
		if (pc.isParalyzed()) {
			return false;
		}
		if ((pc.isInvisble() || pc.isInvisDelay()) && !isInvisUsableSkill()) {
			return false;
		}
		return true;
	}

	boolean isInvisUsableSkill() {
		if (CAST_WITH_INVIS_LIST.contains(_skillId)) {
			return true;
		}
		if (_isTargetAttack && _user instanceof L1PcInstance &&  _player.isDarkelf() && status.hasSkillEffect(L1SkillId.BLIND_HIDING) && _user.getPassiveSkill().isBlindHidingAssassinAttack()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 콜 클랜 어드밴스
	 * @param pc
	 * @param target
	 * @return boolean
	 */
	boolean isCallClanAdvence(L1PcInstance pc, L1PcInstance target){
		int callCalnX	= pc.getX();
		int callCalnY	= pc.getY();
		int locX = 0, locY = 0;
		switch(pc.getMoveState().getHeading()){
		case 0:locX = callCalnX;locY = callCalnY - 1;break;
		case 1:locX = callCalnX + 1;locY = callCalnY - 1;break;
		case 2:locX = callCalnX + 1;locY = callCalnY;break;
		case 3:locX = callCalnX + 1;locY = callCalnY + 1;break;
		case 4:locX = callCalnX;locY = callCalnY + 1;break;
		case 5:locX = callCalnX - 1;locY = callCalnY + 1;break;
		case 6:locX = callCalnX - 1;locY = callCalnY;break;
		case 7:locX = callCalnX - 1;locY = callCalnY - 1;break;
		default:break;
		}
		// 전방 오브젝트 체크
		boolean isExistCharacter = false;
		L1Character arroundCha = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 1)) {
			if (object instanceof L1Character) {
				arroundCha = (L1Character) object;
				if (arroundCha.getX() == locX && arroundCha.getY() == locY && arroundCha.getMapId() == pc.getMapId()) {// 이동시킬 위치에 다른오브젝트 존재하는지 체크
					isExistCharacter = true;
					break;
				}
			}
		}
		if (locX == 0 && locY == 0 || !pc.getMap().isPassable(locX, locY) || isExistCharacter) {
			pc.sendPackets(L1ServerMessage.sm627);
			return false;
		}
		target.getTeleport().startMoveSkill(null, locX, locY, (byte)target.getMoveState().getHeading(), _skillId, null);
		return true;
	}
	
	/**
	 * 상위 스킬 적용 여부
	 * @return boolean
	 */
	boolean isHighSkill() {
		if (_target != null) {
			if (_skillId == HOLY_WEAPON && (_target.getSkill().hasSkillEffect(ENCHANT_WEAPON) || _target.getSkill().hasSkillEffect(BLESS_WEAPON))) {
				useConsume();
				_user.broadcastPacketWithMe(new S_DoActionGFX(_user.getId(), _skill.getActionId()), true);
				return true;
			}
			if (_skillId == ENCHANT_WEAPON && _target.getSkill().hasSkillEffect(BLESS_WEAPON)) {
				useConsume();
				_user.broadcastPacketWithMe(new S_DoActionGFX(_user.getId(), _skill.getActionId()), true);
				return true;
			}
		}
		return false;
	}

	public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, int timeSecs, L1SkillUseType type) {
		handleCommands(player, skillId, targetId, x, y, timeSecs, type, null);
	}

	public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, int timeSecs, L1SkillUseType type, L1Character attacker) {		
		try {
			if (!isCheckedUseSkill() && !checkUseSkill(player, skillId, targetId, x, y, timeSecs, type, attacker)) {
				failSkill();
				return;
			}
			if (player != null && player instanceof L1PcInstance && _skill.getType() != L1Skills.SKILL_TYPE.CHANGE && _skill.getType() != L1Skills.SKILL_TYPE.HEAL && player.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY) {
				return;
			}
			switch (type) {
			case NORMAL:
				if (!_isGlanceCheckFail || _skill.getArea() > 0 || _isTargetNone) {
					if (skillId == HOLY_WEAPON || skillId == ENCHANT_WEAPON) {
						if (!isHighSkill()) {
							runSkill();
							useConsume();
							sendGrfx(true);
						}
					} else if (skillId == DANCING_BLADES) {// 특정스킬 시간초과
						sendGrfx(true);
						runSkill();
						useConsume();
					} else {
						runSkill();
						useConsume();
						sendGrfx(true);
					}
					setDelay();
					sendFailMessageHandle();
				}
				break;
			case LOGIN:
				runSkill();
				break;
			case SPELLSC:
				runSkill();
				sendGrfx(true);
				setDelay();
				break;
			case GMBUFF:
				runSkill();
				sendGrfx(false);
				break;
			case NPCBUFF:
				runSkill();
				sendGrfx(true);
				break;
			default:break;
			}
			setCheckedUseSkill(false);
			_handler = null;
		} catch (Exception e) {
			// System.out.println("skillId : " + skillId + " / attacker : " + (attacker==null ? StringUtil.EmptyString : attacker.getName()));
			// _log.log(Level.SEVERE, StringUtil.EmptyString, e);
		}
	}

	void failSkill() {
		setCheckedUseSkill(false);
		if (_skillId == TELEPORT || _skillId == MASS_TELEPORT || _skillId == TELEPORT_TO_MATHER) {
			_player.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
		}
	}

	/**
	 * 타겟 검증
	 * @param cha
	 * @return
	 * @throws Exception
	 */
	boolean isTarget(L1Character cha) throws Exception {
		boolean _flg = false;
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isGhost() || pc.isGmInvis()) {
				return false;
			}
		}
		if (_calcType == NPC_PC && (cha instanceof L1PcInstance || cha instanceof L1PetInstance || cha instanceof L1SummonInstance)) {
			_flg = true;
		}
		if (cha instanceof L1DoorInstance) {
			if (cha.getMaxHp() == 0 || cha.getMaxHp() == 1) {
				return false;
			}
		    if (cha instanceof L1TowerInstance && (_skillId == ICE_LANCE || _skillId == SHOCK_STUN || _skillId == EMPIRE || _skillId == FORCE_STUN)) {
		    	return false;
		    }
		    if (_skillId == RETURN_TO_NATURE && cha.getRegion() == L1RegionStatus.SAFETY) {
		    	return false;
		    }
		}
		if ((_isTargetAttack || _skill.getType() == L1Skills.SKILL_TYPE.ATTACK) && _calcType == NPC_PC && cha instanceof L1PcInstance) {
			if (_user instanceof L1SummonInstance
					&& (cha.getId() == ((L1SummonInstance) _user).getMaster().getId() || cha.getRegion() == L1RegionStatus.SAFETY)) {
				return false;
			}
			if (_user instanceof L1PetInstance 
					&& (cha.getId() == ((L1PetInstance) _user).getMaster().getId() || cha.getRegion() == L1RegionStatus.SAFETY)) {
				return false;
			}
		}
		if (_skillId == MIND_BREAK && cha.getRegion() == L1RegionStatus.SAFETY) {
			return false;
		}
		if ((cha instanceof L1DollInstance || cha instanceof L1SummonInstance) && _skillId != HASTE) {
			return false;
		}
		if (_calcType == PC_NPC && _target instanceof L1NpcInstance && !(_target instanceof L1PetInstance) && !(_target instanceof L1SummonInstance)
				&& (cha instanceof L1PetInstance || cha instanceof L1SummonInstance || cha instanceof L1PcInstance)) {
			return false;
		}
		
		if (_isTargetAttack || _skill.getType() == L1Skills.SKILL_TYPE.ATTACK) {
			if (_calcType == NPC_PC
					&& !(cha instanceof L1PetInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1PcInstance)) {
				return false;
			}
			if (_calcType == NPC_NPC
					&& _user instanceof L1MonsterInstance && cha instanceof L1MonsterInstance && _user instanceof L1IndunInstance == false) {
				return false;
			}
		}

		if (_isTargetNone && _skill.getType() == L1Skills.SKILL_TYPE.ATTACK
				&& (cha instanceof L1AuctionBoardInstance || cha instanceof L1BoardInstance || cha instanceof L1CrownInstance
						|| cha instanceof L1DwarfInstance || cha instanceof L1EffectInstance || cha instanceof L1FieldObjectInstance
						|| cha instanceof L1FurnitureInstance || cha instanceof L1HousekeeperInstance || cha instanceof L1MerchantInstance
						|| cha instanceof L1TeleporterInstance)) {
			return false;
		}
		/*if (_skill.getType() == L1Skills.TYPE_ATTACK && cha.getId() == _user.getId()) {
			return false;
		}*/
		if (cha.getId() == _user.getId() && _skillId == HEAL_ALL) {
			return false;
		}
		if ((_skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.PC
				|| _skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.PLEDGE
				|| _skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.PARTY
				|| _skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.ALL) && cha.getId() == _user.getId()
				/*&& _skillId != HEAL_ALL*/) {
			return true;
		}
		
		if (_isTargetAttack || _skill.getType() == L1Skills.SKILL_TYPE.ATTACK) {
			if (_user instanceof L1PcInstance && _isPK == false) {
				if (cha instanceof L1SummonInstance && _player.getId() == ((L1SummonInstance) cha).getMaster().getId()) {
					return false;
				}
				if (cha instanceof L1PetInstance && _player.getId() == ((L1PetInstance) cha).getMaster().getId()) {
					return false;
				}
			}
			
			if (!(cha instanceof L1MonsterInstance) && _isPK == false && _target instanceof L1PcInstance) {
				L1PcInstance enemy = (L1PcInstance) cha;
				if (_skillId == COUNTER_DETECTION && enemy.getRegion() != L1RegionStatus.SAFETY && (cha.getSkill().hasSkillEffect(INVISIBILITY) || cha.getSkill().hasSkillEffect(BLIND_HIDING))) {
					return true;
				}
				if (_player.getClanid() != 0 && enemy.getClanid() != 0) {
					for (L1War war : L1World.getInstance().getWarList()) {
						if (war.CheckClanInWar(_player.getClanName()) 
								&& war.CheckClanInSameWar(_player.getClanName(), enemy.getClanName()) 
								&& L1CastleLocation.checkInAllWarArea(enemy.getX(), enemy.getY(), enemy.getMapId())) {
							return true;
						}
					}
				}
				return false;
			}
		}

		if (_user.glanceCheck(15, cha.getX(), cha.getY(), cha instanceof L1DoorInstance) == false && _skill.getIsThrough() == false
				&& (!(_skill.getType() == L1Skills.SKILL_TYPE.CHANGE || _skill.getType() == L1Skills.SKILL_TYPE.RESTORE))) {
			_isGlanceCheckFail = true;
			return false;
		}
		
		/** 아이스랜스 중이라면 디버프 안걸리게 **/
		if ((cha.getSkill().hasSkillEffect(ICE_LANCE)) && (_skillId == ICE_LANCE || _skillId == SHOCK_STUN || _skillId == DECAY_POTION || _skillId == FATAL_POTION
				|| _skillId == WEAPON_BREAK || _skillId == SLOW || _skillId == GREATER_SLOW || _skillId == CURSE_PARALYZE || _skillId == MANA_DRAIN || _skillId == DARKNESS
				|| _skillId == FOG_OF_SLEEPING || _skillId == ARMOR_BREAK || _skillId == EARTH_BIND || _skillId == WIND_SHACKLE
				|| _skillId == POLLUTE_WATER || _skillId == STRIKER_GALE || _skillId == DESTROY
				|| _skillId == PANIC || _skillId == IllUSION_AVATAR || _skillId == DESPERADO || _skillId == POWER_GRIP
				|| _skillId == MOB_ARMOR_BRAKE || _skillId == MOB_POLLUTE_WATER 
				|| _skillId == DEATH_HEAL || _skillId == MOB_DEATH_HEAL || _skillId == MOB_RANGE_DEATH_HEAL 
				|| _skillId == MOB_SHOCKSTUN_18 || _skillId == MOB_SHOCKSTUN_19 || _skillId == MOB_SHOCKSTUN_30 || _skillId == EMPIRE
				|| _skillId == JUDGEMENT || _skillId == PHANTOM || _skillId == PANTERA
				|| _skillId == FORCE_STUN || _skillId == ETERNITY
				|| _skillId == SHADOW_STEP 
				|| _skillId == PRESSURE || _skillId == CRUEL)) {
			return false;
		}
		
		if (cha.getSkill().hasSkillEffect(EARTH_BIND) && _skillId != CANCELLATION) {
			return false;
		}
		if (cha.getSkill().hasSkillEffect(POWER_GRIP) && _skillId == POWER_GRIP) {
			return false;//연그립 안되도록
		}
		/*if (cha.getSkill().hasSkillEffect(SHOCK_STUN) && _skillId == SHOCK_STUN) {
			return false;//연스턴 안되도록
		}*/
		if (cha.getSkill().hasSkillEffect(DESPERADO) && _skillId == DESPERADO) {
			return false;//연데페 안되도록
		}
		if (cha.getSkill().hasSkillEffect(TEMPEST) && _skillId == TEMPEST) {
			return false;//연템페 안되도록
		}
		if (cha.getSkill().hasSkillEffect(ETERNITY) && _skillId == ETERNITY) {
			return false;
		}
		if (cha.getSkill().hasSkillEffect(MOB_BASILL) && _skillId == MOB_BASILL) {
			return false;// 바실굳기중에 바실굳기
		}
		if (cha.getSkill().hasSkillEffect(MOB_COCA) && _skillId == MOB_COCA) {
			return false;// 코카굳기중에 코카굳기
		}
		if (!(cha instanceof L1MonsterInstance) && (_skillId == TAMING_MONSTER || _skillId == CREATE_ZOMBIE)) {
			return false;
		}
		if (!(cha instanceof L1PcInstance) && (_skillId == ARMOR_BREAK || _skillId == AVENGER)) {
			return false;
		}
		if (cha.isDead() && (_skillId != CREATE_ZOMBIE && _skillId != RESURRECTION && _skillId != GREATER_RESURRECTION && _skillId != CALL_OF_NATURE)) {
			return false;
		}
		if (!cha.isDead() && (_skillId == CREATE_ZOMBIE || _skillId == RESURRECTION || _skillId == GREATER_RESURRECTION || _skillId == CALL_OF_NATURE)) {
			return false;
		}
		if ((cha instanceof L1TowerInstance || cha instanceof L1DoorInstance)
				&& (_skillId == CREATE_ZOMBIE || _skillId == RESURRECTION || _skillId == GREATER_RESURRECTION || _skillId == CALL_OF_NATURE)) {
			return false;
		}
		if (cha instanceof L1PcInstance && ((L1PcInstance) cha).getSkill().hasSkillEffect(ABSOLUTE_BARRIER)) {// 앱솔중
			return (_skillId == CURSE_BLIND || _skillId == WEAPON_BREAK || _skillId == DARKNESS || _skillId == WEAKNESS || _skillId == DISEASE
					|| _skillId == FOG_OF_SLEEPING || _skillId == SLOW || _skillId == GREATER_SLOW || _skillId == CANCELLATION
					|| _skillId == SILENCE || _skillId == DECAY_POTION || _skillId == FATAL_POTION || _skillId == MASS_TELEPORT || _skillId == ENTANGLE
					|| _skillId == DETECTION || _skillId == IZE_BREAK || _skillId == COUNTER_DETECTION
					|| _skillId == DESTROY || _skillId == ERASE_MAGIC || _skillId == PHYSICAL_ENCHANT_DEX
					|| _skillId == PHYSICAL_ENCHANT_STR || _skillId == BLESS_WEAPON || _skillId == IMMUNE_TO_HARM || _skillId == MASS_IMMUNE_TO_HARM 
					|| _skillId == REMOVE_CURSE || _skillId == CONFUSION || _skillId == DEATH_HEAL
					|| _skillId == MOB_SLOW_1 || _skillId == MOB_SLOW_18 || _skillId == MOB_WEAKNESS_1 || _skillId == MOB_WEAKNESS_19
					|| _skillId == MOB_DISEASE_1 || _skillId == MOB_DISEASE_30 || _skillId == MOB_BASILL || _skillId == MOB_COCA
					|| _skillId == MOB_SHOCKSTUN_18 || _skillId == MOB_SHOCKSTUN_19 || _skillId == MOB_SHOCKSTUN_30
					|| _skillId == MOB_RANGESTUN_18 || _skillId == MOB_RANGESTUN_19 
					|| _skillId == MOB_RANGESTUN_20 || _skillId == MOB_RANGESTUN_30 
					|| _skillId == MOB_CURSEPARALYZ_19 || _skillId == MOB_CURSEPARALYZ_18
					|| _skillId == MOB_WINDSHACKLE_1 || _skillId == MOB_CANCELLATION 
					|| _skillId == MOB_DEATH_HEAL || _skillId == MOB_RANGE_DEATH_HEAL 
					|| _skillId == MOB_ABSOLUTE_BLADE || _skillId == MOB_IMMUNE_BLADE || _skillId == MOB_ERASE_MAGIC
					|| _skillId == ANTA_MESSAGE_1 || _skillId == ANTA_MESSAGE_2 || _skillId == ANTA_MESSAGE_3 || _skillId == ANTA_MESSAGE_4
					|| _skillId == ANTA_MESSAGE_5 || _skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8
					|| _skillId == ANTA_MESSAGE_9 || _skillId == ANTA_MESSAGE_10
					|| _skillId == VALLAKAS_PREDICATE2 || _skillId == VALLAKAS_PREDICATE4 || _skillId == VALLAKAS_PREDICATE5
					|| _skillId == BUFF_GOLD_FEATHER || _skillId == BUFF_GOLD_FEATHER_GROW
					|| _skillId == BUFF_MISOPIA_GROW || _skillId == BUFF_MISOPIA_DEFFENS || _skillId == BUFF_MISOPIA_ATTACK
					|| _skillId == BUFF_SPECIAL_GROW || _skillId == BUFF_SPECIAL_DEFFENS || _skillId == BUFF_SPECIAL_ATTACK);
		}
		if (cha instanceof L1NpcInstance) {
			int hiddenStatus = ((L1NpcInstance) cha).getHiddenStatus();
			if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
				return (_skillId == DETECTION || _skillId == IZE_BREAK || _skillId == EYE_OF_DRAGON || _skillId == COUNTER_DETECTION);
			}
			if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_FLY) {
				return false;
			}
		}
		if ((_skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.PC || _skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.ALL) && cha instanceof L1PcInstance) {
			_flg = true;
		} else if ((_skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.NPC || _skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.ALL) 
				&& (cha instanceof L1MonsterInstance || cha instanceof L1NpcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance)
				&& !(cha instanceof L1ArrowInstance)) {
			_flg = true;
		} else if (_skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.COMPANIION && _user instanceof L1PcInstance) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (summon.getMaster() != null) {
					if (_player.getId() == summon.getMaster().getId()) {
						if (_skillId != RETURN_TO_NATURE) {
							_flg = true;
						}
					} else {
						if (_skillId == RETURN_TO_NATURE) {
							_flg = true;
						}
					}
				}
			}
			if (cha instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) cha;
				if (pet.getMaster() != null) {
					if (_player.getId() == pet.getMaster().getId()) {
						if (_skillId != RETURN_TO_NATURE) {
							_flg = true;
						}
					} else {
						if (_skillId == RETURN_TO_NATURE) {
							_flg = true;
						}
					}
				}
			}
		}
		if (_calcType == PC_PC && cha instanceof L1PcInstance) {
			if (_skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.PLEDGE && (isTargetToClan((L1PcInstance) cha) || _player.isGm())) {
				return true;
			}
			if (_skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.PARTY && (_player.getParty().isMember((L1PcInstance) cha) || _player.isGm())) {
				return true;
			}
		}
		return _flg;
	}
	
	/**
	 * 대상이 같은 혈맹원에 포함되는지 검증
	 * @param target
	 * @return boolean
	 */
	boolean isTargetToClan(L1PcInstance target) {
		int clanId			= _player.getClanid();
		if (clanId == 0) {
			return false;
		}
		int targetClanId	= target.getClanid();
		if (clanId == targetClanId) {
			return true;// 같은 혈맹원
		}
		// 인터서버에선 동맹도 포함된다.
		if (L1InterServer.isWorldWarInter(_player.getMap().getInter())) {
			L1Clan clan		= _player.getClan();
			if (clan != null && clan.isAlliance(targetClanId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 이팩트 스폰(독구름)
	 */
	void EffectSpawn() {
		int effectId = 5137;
		int xx = 0, yy = 0, xx1 = 0, yy1 = 0, xx2 = 0, yy2 = 0, xx3 = 0, yy3 = 0, xx4 = 0, yy4 = 0;
		int randomxy	= random.nextInt(4);
		int r			= random.nextInt(2) + 1;
		int a1			= 3 + randomxy;
		int a2			= -3 - randomxy;
		int b1			= 2 + randomxy;
		int b2			= -2 - randomxy;
		int heading		= _npc.getMoveState().getHeading();// 몹 방향
		switch (heading) {
		case 1:
			xx = a1 - r;
			yy = a2 + r;
			yy1 = a2;
			xx2 = a1;
			xx3 = a2;
			yy3 = b2;
			xx4 = b1;
			yy4 = a1;
			break;
		case 2:
			xx = a1 + 1;
			xx1 = b1;
			yy1 = a2;
			xx2 = b1;
			yy2 = a1;
			xx3 = b1 - 3;
			yy3 = a2 - 2;
			xx4 = b1 - 2;
			yy4 = a1 + 3;
			break;
		case 3:
			xx = a1 - r;
			yy = a1 - r;
			xx1 = a1;
			yy2 = a1;
			xx3 = a1;
			yy3 = a2;
			xx4 = a2;
			yy4 = b1;
			break;
		case 4:
			yy = a1 + 1;
			xx1 = a1;
			yy1 = b1;
			xx2 = a2;
			yy2 = b1;
			xx3 = a1 + 3;
			yy3 = b1 - 3;
			xx4 = a2 - 3;
			yy4 = b1 - 3;
			break;
		case 5:
			xx = a2 + r;
			yy = a1 - r;
			yy1 = a1;
			xx2 = a2;
			xx3 = a1;
			yy3 = b1;
			xx4 = b2;
			yy4 = a2;
			break;
		case 6:
			xx = a2 - 1;
			xx1 = b2;
			yy1 = a1;
			xx2 = b2;
			yy2 = a2;
			xx3 = b2 + 3;
			yy3 = a1 + 2;
			xx4 = b2 + 2;
			yy4 = a2 - 3;
			break;
		case 7:
			xx = a2 + r;
			yy = a2 + r;
			xx1 = a2;
			yy2 = a2;
			xx3 = a2;
			yy3 = a1;
			xx4 = a1;
			yy4 = b2;
			break;
		case 0:
			yy = a2 - 1;
			xx1 = a2;
			yy1 = b2;
			xx2 = a1;
			yy2 = b2;
			xx3 = a2 - 3;
			yy3 = b2 + 3;
			xx4 = a1 + 3;
			yy4 = b2 + 3;
			break;
		default:break;
		}
		int x = _npc.getX() + xx;
		int y = _npc.getY() + yy;
		// 마름모 4*4픽셀 모양 (몹 기준에서 정면에 출현)
		L1EffectSpawn effectSpawn = L1EffectSpawn.getInstance();
		int duration = _skill.getBuffDuration() * 1000;
		effectSpawn.spawnEffect(effectId, duration, x, y, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x, y + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x, y - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x, y - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x - 1, y, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x - 1, y + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x - 1, y - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x - 1, y - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x + 1, y + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x + 1, y - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x + 1, y, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x + 1, y - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x + 2, y - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x + 2, y - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x + 2, y, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x + 2, y + 1, _user.getMapId());
		int x1 = _npc.getX() + xx1;
		int y1 = _npc.getY() + yy1;
		// 마름모 4*4픽셀 모양 (몹 기준에서 좌측에 출현)
		effectSpawn.spawnEffect(effectId, duration, x1, y1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1, y1 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1, y1 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1, y1 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 - 1, y1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 - 1, y1 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 - 1, y1 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 - 1, y1 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 + 1, y1 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 + 1, y1 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 + 1, y1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 + 1, y1 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 + 2, y1 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 + 2, y1 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 + 2, y1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x1 + 2, y1 + 1, _user.getMapId());
		int x2 = _npc.getX() + xx2;
		int y2 = _npc.getY() + yy2;
		// 마름모 4*4픽셀 모양 (몹 기준에서 우측에 출현)
		effectSpawn.spawnEffect(effectId, duration, x2, y2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2, y2 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2, y2 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2, y2 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 - 1, y2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 - 1, y2 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 - 1, y2 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 - 1, y2 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 + 1, y2 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 + 1, y2 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 + 1, y2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 + 1, y2 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 + 2, y2 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 + 2, y2 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 + 2, y2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x2 + 2, y2 + 1, _user.getMapId());
		int x3 = _npc.getX() + xx3;
		int y3 = _npc.getY() + yy3;
		// 마름모 4*4픽셀 모양 (몹 기준에서 좌측2에 출현)
		effectSpawn.spawnEffect(effectId, duration, x3, y3, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3, y3 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3, y3 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3, y3 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 - 1, y3, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 - 1, y3 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 - 1, y3 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 - 1, y3 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 + 1, y3 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 + 1, y3 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 + 1, y3, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 + 1, y3 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 + 2, y3 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 + 2, y3 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 + 2, y3, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x3 + 2, y3 + 1, _user.getMapId());
		int x4 = _npc.getX() + xx4;
		int y4 = _npc.getY() + yy4;
		// 마름모 4*4픽셀 모양 (몹 기준에서 우측2에 출현)
		effectSpawn.spawnEffect(effectId, duration, x4, y4, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4, y4 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4, y4 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4, y4 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 - 1, y4, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 - 1, y4 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 - 1, y4 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 - 1, y4 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 + 1, y4 + 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 + 1, y4 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 + 1, y4, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 + 1, y4 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 + 2, y4 - 2, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 + 2, y4 - 1, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 + 2, y4, _user.getMapId());
		effectSpawn.spawnEffect(effectId, duration, x4 + 2, y4 + 1, _user.getMapId());
	}
	
	void makeTargetListPosWave(){
		for (L1Object tgobj : L1World.getInstance().getVisibleLineHeading(_player, _player.calcheading(_player.getX(), _player.getY(), _target.getX(), _target.getY()), 5)) {// 직선상 5칸거리
			if (tgobj == null || !isEnemyTarget(tgobj)) {
				continue;
			}
			_targetList.add(new TargetStatus((L1Character) tgobj));
		}
	}
	
	void makeTargetListTempest(){
		for (L1Object tgobj : L1World.getInstance().getVisibleObjects(_player, _skill.getArea())) {
			if (tgobj == null || !isEnemyTarget(tgobj)) {
				continue;
			}
			_targetList.add(new TargetStatus((L1Character) tgobj));
			if (tgobj instanceof L1PcInstance) {
				((L1PcInstance) tgobj).send_effect(20604);
			} else {
				((L1Character) tgobj).broadcastPacket(new S_Effect(tgobj.getId(), 20604), true);
			}
		}
	}
	
	/**
	 * 적으로 간주할 케릭터 검사
	 * @param obj
	 * @return boolean
	 */
	boolean isEnemyTarget(L1Object obj) {
		try {
			if (!(obj instanceof L1MonsterInstance || obj instanceof L1PcInstance || obj instanceof L1PeopleInstance || obj instanceof L1SummonInstance || obj instanceof L1PetInstance)) {
				return false;
			}
			L1Character cha = (L1Character) obj;
			L1PcInstance pc = null;
			if (cha instanceof L1PcInstance) {
				pc = (L1PcInstance)cha;
			}
			if (cha.getId() == _player.getId()) {
				return false;
			}
			if ((pc != null || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) && cha.getRegion() == L1RegionStatus.SAFETY) {
				return false;// 세이프 존 체크
			}
			if (pc != null && _player.isInParty() && _player.getParty().isMember(pc)) {
				return false;// 같은 파티원 제외
			}
			if (pc != null && pc.getClanid() > 0) {
				if (pc.getClanid() == _player.getClanid()) {
					return false;// 같은 혈맹원
				}
				L1Clan clan = _player.getClan();
				if (clan != null && clan.isAlliance(pc.getClanid())) {// 같은 동맹원
					return false;
				}
			}
			if (!isTarget(cha) || !_player.glanceCheck(15, cha.getX(), cha.getY(), cha instanceof L1DoorInstance)) {
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 스킬을 적용할 타겟 생성
	 */
	void makeTargetList() {
		try {
			if (_type == L1SkillUseType.LOGIN) {
				_targetList.add(new TargetStatus(_user));
				return;
			}
			if (_skill.getTargetTo() == L1Skills.SKILL_TARGET_TO.ME && _skill.getType() != L1Skills.SKILL_TYPE.ATTACK) {
				_targetList.add(new TargetStatus(_user));
				return;
			}
			if (_skillId == POS_WAVE && _user instanceof L1PcInstance && _player._isLancerForm) {// 포스 웨이브 타겟 생성
				makeTargetListPosWave();
				return;
			}
			if (_skillId == TEMPEST && _user instanceof L1PcInstance) {// 템페스트 타겟 생성
				makeTargetListTempest();
				return;
			}
			if (_skill.getRanged() != -1) {// 사정거리 -1 화면내 오브젝트만
				int skillranged = _skill.getRanged();
				if (_skillId == PANTERA && _user.isPassiveStatus(L1PassiveId.PANTERA_SHOCK)) {
					skillranged += 1;
				} else if (_skillId == CRUEL && _user.isPassiveStatus(L1PassiveId.CRUEL_CONVICTION)) {
					skillranged += 1;
				} else if (_skillId == SHADOW_STEP && _user.isPassiveStatus(L1PassiveId.SHADOW_STEP_CHASER)) {
					skillranged += 1;
				}
				L1Sprite sprite = _target.getSprite();
				if (sprite != null) {
					skillranged += sprite.get_width();
				}
				if (_user.getLocation().getTileLineDistance(_target.getLocation()) > skillranged) {
					return;// 사정거리를 벗어남
				}
			} else {
				if (!_user.getLocation().isInScreen(_target.getLocation())) {
					return;// 화면 밖
				}
			}
			if (isTarget(_target) == false && !_isTargetNone) {
				return;
			}

			if (_skillId == LIGHTNING) {
				for (L1Object tgobj : L1World.getInstance().getVisibleLineObjects(_user, _target)) {// 타겟과의 진선상
					if (tgobj == null || !(tgobj instanceof L1Character)) {
						continue;
					}
					L1Character cha = (L1Character) tgobj;
					if (isTarget(cha) == false) {
						continue;
					}
					_targetList.add(new TargetStatus(cha));
				}
				return;
			} 
			if (_skillId == IMMUNE_TO_HARM && !_user.glanceCheck(15, _target.getX(), _target.getY(), _target instanceof L1DoorInstance)) {
				return;
			}

			if (_skill.getArea() == 0) {
				if (!_user.glanceCheck(15, _target.getX(), _target.getY(), _target instanceof L1DoorInstance) 
						&& _skill.getType() == L1Skills.SKILL_TYPE.ATTACK) {
					_targetList.add(new TargetStatus(_target, false));
					return;
				}
				_targetList.add(new TargetStatus(_target));
			} else {
				if (!_isTargetNone) {
					_targetList.add(new TargetStatus(_target));
				}
				if (_skillId != HEAL_ALL && !(_isTargetAttack || _skill.getType() == L1Skills.SKILL_TYPE.ATTACK)) {
					_targetList.add(new TargetStatus(_user));
				}
				List<L1Object> objects = _skill.getArea() == -1 ? L1World.getInstance().getVisibleObjects(_user) : L1World.getInstance().getVisibleObjects(_target, _skill.getArea());
				for (L1Object tgobj : objects) {
					if (tgobj == null || !(tgobj instanceof L1Character)) {
						continue;
					}
					L1Character cha = (L1Character) tgobj;
					if (_user instanceof L1SummonInstance && cha instanceof L1PcInstance) {
						continue;
					}
					if (!isTarget(cha)) {
						continue;
					}
					if ((_skillId == METEOR_STRIKE || _skillId == ICE_METEOR) && cha instanceof L1PcInstance && _user instanceof L1PcInstance) {
						boolean isNowWar = false;
						int castleId = L1CastleLocation.getCastleIdByArea((L1PcInstance) _user);
						if (castleId != 0) {
							isNowWar = War.getInstance().isNowWar(castleId);
						}
						if (!isNowWar) {
							continue;
						}
					}
					_targetList.add(new TargetStatus(cha));
				}
				return;
			}
		} catch (Exception e) {
			_log.finest("exception in L1Skilluse makeTargetList" + e);
		}
	}
	
	/**
	 * 메세지 패킷 재사용 처리
	 */
	static final FastMap<Integer, S_ServerMessage> MESSAGES = new FastMap<Integer, S_ServerMessage>();
	S_ServerMessage getMessage(int id){
		S_ServerMessage message = MESSAGES.get(id);
		if (message == null) {
			message = new S_ServerMessage(id);
			MESSAGES.put(id, message);
		}
		return message;
	}

	void sendHappenMessage(L1PcInstance pc) {
		int msgID = _skill.getSysmsgIdHappen();
		if (msgID > 0) {
			pc.sendPackets(getMessage(msgID));
		}
	}

	void sendFailMessageHandle() {
		if (_skill.getType() != L1Skills.SKILL_TYPE.ATTACK && !_isTargetNone && _targetList.size() == 0) {
			sendFailMessage();
		}
	}

	void sendFailMessage() {
		int msgID = _skill.getSysmsgIdFail();
		if (msgID > 0 && (_user instanceof L1PcInstance)) {
			_player.sendPackets(getMessage(msgID));
		}
	}

	/** 스킬사용시 MP HP 소모부분 **/
	boolean isHPMPConsume() {
		_mpConsume	= _skill.getMpConsume();
		_hpConsume	= _skill.getHpConsume();
		int currentMp = 0, currentHp = 0;
		if (_user instanceof L1NpcInstance) {
			currentMp = _npc.getCurrentMp();
			currentHp = _npc.getCurrentHp();
		} else {
			if (_skillId == BURNING_SHOT && _player._isBurningShot) {
				return true;
			}
			currentMp = _player.getCurrentMp();
			currentHp = _player.getCurrentHp();
			if (_player.getAbility().getTotalInt() > 12 && _skillId > HOLY_WEAPON && _skillId <= ICE_SPIKE) {
				_mpConsume--;
			}
			if (_player.getAbility().getTotalInt() > 13 && _skillId > STALAC && _skillId <= ICE_SPIKE) {
				_mpConsume--;
			}
			if (_player.getAbility().getTotalInt() > 14 && _skillId > WEAK_ELEMENTAL && _skillId <= ICE_SPIKE) {
				_mpConsume--;
			}
			if (_player.getAbility().getTotalInt() > 15 && _skillId > MEDITATION && _skillId <= ICE_SPIKE) {
				_mpConsume--;
			}
			if (_player.getAbility().getTotalInt() > 16 && _skillId > DARKNESS && _skillId <= ICE_SPIKE) {
				_mpConsume--;
			}
			if (_player.getAbility().getTotalInt() > 17 && _skillId > BLESS_WEAPON && _skillId <= ICE_SPIKE) {
				_mpConsume--;
			}
			if (_player.getAbility().getTotalInt() > 18 && _skillId > ENCHANT_ACCURACY && _skillId <= ICE_SPIKE) {
				_mpConsume--;
			}
			if (_player.getAbility().getTotalInt() > 12 && _skillId >= SHOCK_STUN && _skillId <= BLOW_ATTACK) {
				_mpConsume -= (_player.getAbility().getTotalInt() - 12);
			}
			if (_player.isCrown() || _player.isWarrior()) {
				if (_player.getAbility().getBaseInt() >= 11) {
					_mpConsume--;
				}
				if (_player.getAbility().getBaseInt() >= 13) {
					_mpConsume--;
				}
			} else if (_player.isKnight() || _player.isFencer() || _player.isLancer()) {
				if (_player.getAbility().getBaseInt() >= 9) {
					_mpConsume--;
				}
				if (_player.getAbility().getBaseInt() >= 11) {
					_mpConsume--;
				}
			} else if (_player.isDarkelf()) {
				if (_player.getAbility().getBaseInt() >= 13) {
					_mpConsume--;
				}
				if (_player.getAbility().getBaseInt() >= 15) {
					_mpConsume--;
				}
			} else if (_player.isIllusionist()) {
				if (_player.getAbility().getBaseInt() >= 14) {
					_mpConsume--;
				}
				if (_player.getAbility().getBaseInt() >= 15) {
					_mpConsume--;
				}
			}
			L1ItemInstance helmet = _player.getInventory().getEquippedHelmet();
			if (_skillId == PHYSICAL_ENCHANT_DEX && helmet != null && helmet.getItemId() == 20013) {
				_mpConsume >>= 1;
			} else if (_skillId == HASTE && helmet != null && helmet.getItemId() == 20013) {
				_mpConsume >>= 1;
			} else if (_skillId == HEAL && helmet != null && helmet.getItemId() == 20014) {
				_mpConsume >>= 1;
			} else if (_skillId == EXTRA_HEAL && helmet != null && helmet.getItemId() == 20014) {
				_mpConsume >>= 1;
			} else if (_skillId == ENCHANT_WEAPON && helmet != null && helmet.getItemId() == 20015) {
				_mpConsume >>= 1;
			} else if (_skillId == DETECTION && helmet != null && helmet.getItemId() == 20015) {
				_mpConsume >>= 1;
			} else if (_skillId == PHYSICAL_ENCHANT_STR && helmet != null && helmet.getItemId() == 20015) {
				_mpConsume >>= 1;
			} else if (_skillId == HASTE && helmet != null && helmet.getItemId() == 20008) {
				_mpConsume >>= 1;
			} else if (_skillId == GREATER_HASTE && helmet != null && helmet.getItemId() == 20023) {
				_mpConsume >>= 1;
			} else if ((_skillId == SCALES_EARTH_DRAGON || _skillId == SCALES_WATER_DRAGON || _skillId == SCALES_FIRE_DRAGON || _skillId == SCALES_WIND_DRAGON) && _user.isPassiveStatus(L1PassiveId.AURAKIA)) {
				_mpConsume -= 5;
			} else if (status.hasSkillEffect(HALPAS_JUJOO_MP_INCREASE)) {
				_mpConsume <<= 1;
			} else if (_skillId == ARMOR_BREAK && _user.isPassiveStatus(L1PassiveId.ARMOR_BREAK_DESTINY)){
				_mpConsume -= 5;
				_hpConsume -= 15;
			}
			
			if (status.hasSkillEffect(MP_REDUCTION_POTION)) {// 마나 절감 울약 20% 감소
				_mpConsume -= (int)(((double)_mpConsume * 0.01D) * 20);
			}
			if (0 < _skill.getMpConsume()) {
				_mpConsume = Math.max(_mpConsume, 1);
			}
		}
		if (currentHp < _hpConsume + 1) {
			if (_user instanceof L1PcInstance) {
				_player.sendPackets(L1ServerMessage.sm279);
			}
			return false;
		} else if (currentMp < _mpConsume) {
			if (_user instanceof L1PcInstance) {
				_player.sendPackets(L1ServerMessage.sm278);
			}
			return false;
		}
		return true;
	}

	boolean isItemConsume() {		
		int itemConsume			= _skill.getItemConsumeId();
		int itemConsumeCount	= _skill.getItemConsumeCount();

		if (_skillId == SHADOW_ARMOR && _user.isPassiveStatus(L1PassiveId.SHADOW_ARMOR_DESTINY)) {
			itemConsume			= L1ItemId.BLACK_ATTR_STONE;
			itemConsumeCount	= 1;
		} else if (_skillId == BLIND_HIDING && _user.isPassiveStatus(L1PassiveId.BLIND_HIDING_ASSASSIN)) {
			itemConsume			= L1ItemId.BLACK_ATTR_STONE;
			itemConsumeCount	= 1;
		} else if (_skillId == BURNING_SHOT && _player._isBurningShot) {
			return true;
		}
		if (itemConsume == 0) {
			return true;
		}
		int itemConsumeSub = consumItemChange(itemConsume);// 재료 변경
		if (itemConsumeSub != 0 && _player.getInventory().checkItem(itemConsumeSub, itemConsumeCount)) {
			return true;
		}
		if (!_player.getInventory().checkItem(itemConsume, itemConsumeCount)) {
			return false;
		}
		return true;
	}

	void useConsume() {
		if (_user instanceof L1NpcInstance || _user instanceof L1AiUserInstance) {// hp, mp만 소모하는 인스턴스
			_user.setCurrentHp(_user.getCurrentHp() - _hpConsume);
			_user.setCurrentMp(_user.getCurrentMp() - _mpConsume);
			return;
		}
		if (_skillId == BURNING_SHOT && !_player._isBurningShot) {
			return;// 버닝샷 해제시 소모 없음
		}
		if (isHPMPConsume()) {
			_player.setCurrentHp(_player.getCurrentHp() - _hpConsume);
			_player.setCurrentMp(_player.getCurrentMp() - _mpConsume);
		}
		if (_skill.getAlignment() != 0) {
			_player.setAlignment(IntRange.ensure(_player.getAlignment() + _skill.getAlignment(), -32767, 32767));
		}

		int itemConsume			= _skill.getItemConsumeId();
		int itemConsumeCount	= _skill.getItemConsumeCount();

		if (_skillId == SHADOW_ARMOR && _user.isPassiveStatus(L1PassiveId.SHADOW_ARMOR_DESTINY)) {
			itemConsume			= L1ItemId.BLACK_ATTR_STONE;
			itemConsumeCount	= 1;
		} else if (_skillId == BLIND_HIDING && _user.isPassiveStatus(L1PassiveId.BLIND_HIDING_ASSASSIN)) {
			itemConsume			= L1ItemId.BLACK_ATTR_STONE;
			itemConsumeCount	= 1;
		}
		
		if (itemConsume == 0) {
			return;
		}
		int itemConsumeSub = consumItemChange(itemConsume);// 재료 변경
		if (itemConsumeSub != 0 && _player.getInventory().checkItem(itemConsumeSub, itemConsumeCount)) {
			_player.getInventory().consumeItem(itemConsumeSub, itemConsumeCount);
			return;
		}
		_player.getInventory().consumeItem(itemConsume, itemConsumeCount);
	}
	
	int consumItemChange(int itemConsume){
		if (_player.getLevel() < 80 && _player.getMap().isBeginZone()) {// 초보존
			switch(itemConsume){
			case L1ItemId.MAGIC_STONE:		return 30079;// 마력의 돌
			case L1ItemId.ELF_ATTR_STONE:	return 30078;// 정령옥
			case L1ItemId.ELBEN_WAFER:		return 30076;// 엘븐와퍼
			case L1ItemId.BLACK_ATTR_STONE:	return 30080;// 흑요석
			case L1ItemId.DRAGONKNIGHT_BONE:return 30081;// 각인의 뼈조각
			case L1ItemId.ATTR_STONE:		return 30082;// 속성석
			}
		} else if (_player.getMap().isInterWarZone()) {
			switch(itemConsume){
			case L1ItemId.MAGIC_STONE:		return 130034;// 마력의 돌
			case L1ItemId.ELF_ATTR_STONE:	return 130033;// 정령옥
			case L1ItemId.ELBEN_WAFER:		return 130031;// 엘븐와퍼
			case L1ItemId.BLACK_ATTR_STONE:	return 130035;// 흑요석
			case L1ItemId.DRAGONKNIGHT_BONE:return 130036;// 각인의 뼈조각
			case L1ItemId.ATTR_STONE:		return 130037;// 속성석
			case L1ItemId.GEMSTONE:			return 130038;// 결정체
			}
		}
		return 0;
	}

	void addMagicList(L1Character cha, boolean repetition) {
		if (_skillTime == 0) {
			if (_skill.getBuffDuration() == 0) {
				if (_skillId == INVISIBILITY) {
					cha.getSkill().setSkillEffect(INVISIBILITY, 0);
				}
				return;
			}
			_getBuffDuration = _skill.getBuffDuration() * 1000;
		} else {
			_getBuffDuration = _skillTime * 1000;
		}
		
		/** 지속시간 설정 **/
		if (_skillId == IMMUNE_TO_HARM && _user.isPassiveStatus(L1PassiveId.IMMUNE_T0_HARM_SAINT) && _player.getId() == cha.getId()) {
			_getBuffDuration = _skill.getAfterPassive().getDuration() * 1000;
		} else if (_skillId != IMMUNE_TO_HARM && _skill.getAfterPassive() != null && _user.isPassiveStatus(_skill.getAfterPassive().getPassive()) 
				&& _skill.getAfterPassive().getBin() != null && _skill.getAfterPassive().getBin().get_duration() > 0) {
			_getBuffDuration = _skill.getAfterPassive().getDuration() * 1000;
		} else if (_skillId == FOG_OF_SLEEPING || _skillId == SHADOW_SLEEP || _skillId == PHANTASM) {
			_getBuffDuration = (random.nextInt(3) + 1) * 1000;
		} else if (_changeBuffDuration > 0) {
			_getBuffDuration = _changeBuffDuration;
		}
		
		if (STRANGE_SKILL_LIST.contains(_skillId)) {// 상태이상 스킬
			if (_getBuffDuration > 1000 && cha.getSkill().hasSkillEffect(LIBERATION)) {
				actionLiberation(cha);// 리버레이션 상태이상 스킬일경우 지속시간 감소처리
			}
			if (_user instanceof L1PcInstance && _player.getAbility().getSpellDurationDecrease() > 0) {
				_getBuffDuration -= _player.getAbility().getSpellDurationDecrease();// 0.1초 기준
			}
		}
		
		if (_skillId == CURSE_POISON || _skillId == TOMAHAWK || _skillId == CURSE_PARALYZE) {
			return;
		}
		cha.getSkill().setSkillEffect(_skillId, _getBuffDuration);// 스킬 타이머 시작
		if (cha instanceof L1PcInstance && repetition) {
			sendIcon((L1PcInstance) cha);// 재시전 아이콘 출력
		}
	}
	
	void actionLiberation(L1Character cha){
		_getBuffDuration = (int)(_getBuffDuration * 0.8D);// 지속시간 20% 감소
		if(cha instanceof L1PcInstance)((L1PcInstance) cha).send_effect(19539);
	}

	void sendIcon(L1PcInstance pc) {
		if (_getBuffIconDuration == 0) {
			_getBuffIconDuration = _getBuffDuration == 0 ? _skill.getBuffDuration() : _getBuffDuration / 1000;
		}
		if (PROTO_ICON_SKILL_LIST.contains(_skillId)) {
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, _getBuffIconDuration), true);
		} else if (_handler != null) {
			_handler.icon(pc, _getBuffIconDuration);
		}
	}
	
	void actionPosWave(){
		L1Character[] cha = new L1Character[_targetList.size()];
		int i = 0;
		for (TargetStatus ts : _targetList) {
			cha[i] = ts.getTarget();
			cha[i].broadcastPacketExceptTargetSight(new S_DoActionGFX(cha[i].getId(), ActionCodes.ACTION_Damage), _player, true);
			i++;
		}
		_player.broadcastPacketWithMe(new S_RangeSkill(_player, cha, 19381, 18, S_RangeSkill.TYPE_DIR), true);
		cha = null;
		_player.getSpeedSync().setAttackSyncInterval(_player.getAcceleratorChecker().getRightInterval(ACT_TYPE.SPELL_DIR) + _currentTime + Config.SPEED.SPELL_SPEED_SYNCHRONIZED);
	}
	
	void directionChange(){
		int dir = _player.targetDirection(_targetX, _targetY);
		if (_player.getMoveState().getHeading() != dir) {
			_player.getMoveState().setHeading(dir);
			_player.broadcastPacketWithMe(new S_ChangeHeading(_player), true);
		}
	}
	
	int getCriticalEffect(int castgfx) {
		switch(_skillId){
		case CALL_LIGHTNING:
			return 11737;
		case SUNBURST:
			return 11760;
		case CONE_OF_COLD:
			return 11742;
		case DISINTEGRATE:
			return _user.isPassiveStatus(L1PassiveId.DISINTEGREATE_NEMESIS) ? 20114 : 11748;
		case ERUPTION:
			return 11754;
		default:
			return castgfx;
		}
	}

	void sendGrfx(boolean isSkillAction) {
		if (_skillId == ALTERNATE) {
			return;
		}
		if (_skillId == POS_WAVE && _user instanceof L1PcInstance && _player._isLancerForm) {
			actionPosWave();
			return;
		}
		int actionId	= _skill.getActionId();
		int actionId2	= _skill.getActionId2();
		int actionId3	= _skill.getActionId3();
		int castgfx		= _skill.getCastGfx();
		int castgfx2	= _skill.getCastGfx2();
		int castgfx3	= _skill.getCastGfx3();

		if (castgfx == 0) {
			return;
		}
		if (_isCriticalDamage) {
			castgfx = getCriticalEffect(castgfx);
		} else {
			// 이팩트 변경
			if (_skillId == UNCANNY_DODGE && _player.getAC().getAc() <= -100) {
				castgfx = 11766;// 황금 닷지
			} else if (_skillId == COUNTER_BARRIER) {
				if (_user.isPassiveStatus(L1PassiveId.COUNTER_BARRIER_MASTER)) {
					castgfx = 20473;// 카운터 배리어:마스터 을 배우고있을경우
				} else if (_user.isPassiveStatus(L1PassiveId.COUNTER_BARRIER_VETERAN)) {
					castgfx = 18965;// 카운터 배리어:베테랑 을 배우고있을경우
				}
			} else if (_skillId == ARMOR_BREAK && _user.isPassiveStatus(L1PassiveId.ARMOR_BREAK_DESTINY)) {
				castgfx = 17226;// 아머 브레이크:데스티니 을 배우고있을경우
			} else if (_skillId == DOUBLE_BRAKE && _user.isPassiveStatus(L1PassiveId.DOUBLE_BREAK_DESTINY)) {
				castgfx = 17224;// 더블 브레이크:데스티니 을 배우고있을경우
			} else if (_skillId == IMMUNE_TO_HARM && _user.isPassiveStatus(L1PassiveId.IMMUNE_T0_HARM_SAINT)) {
				castgfx = 19015;// 이뮨 투 함:세인트 을 배우고있을경우
			} else if (_skillId == LUCIFER && _user.isPassiveStatus(L1PassiveId.LUCIFER_DESTINY)) {
				castgfx = 19017;// 루시퍼:데스티니 을 배우고있을경우
			} else if (_skillId == SHADOW_ARMOR && _user.isPassiveStatus(L1PassiveId.SHADOW_ARMOR_DESTINY)) {
				castgfx = 19596;// 쉐도우아머:데스티니 을 배우고있을경우
			} else if (_skillId == MOVING_ACCELERATION && _user.isPassiveStatus(L1PassiveId.MOVING_ACCELERATION_LAST)) {
				castgfx = 19535;// 무빙악셀레이션:맥시멈 을 배우고있을경우
			} else if (_skillId == MEDITATION && _user.isPassiveStatus(L1PassiveId.MEDITATION_BEYOND)) {
				castgfx = 19595;// 메디테이션:비욘드 을 배우고있을경우
			} else if (_skillId == HOLY_WALK && _user.isPassiveStatus(L1PassiveId.HOLY_WALK_EVOLUTION)) {
				castgfx = 20109;// 홀리워크:에볼루션 을 배우고있을경우
			} else if (_skillId == DISINTEGRATE && _user.isPassiveStatus(L1PassiveId.DISINTEGREATE_NEMESIS)) {
				castgfx = 20112;// 디스인티그레이트:네매시스 을 배우고있을경우
			} else if (_skillId == EMPIRE && _user.isPassiveStatus(L1PassiveId.EMPIRE_OVERLOAD)) {
				castgfx = 20405;// 엠파이어:오버로드 을 배우고있을경우
			} else if (_skillId == SOUL_BARRIER && _user.isPassiveStatus(L1PassiveId.SOUL_BARRIER_ARMOR)) {
				castgfx = 20465;// 소울배리어:아머 을 배우고있을경우
			} else if (_skillId == STRIKER_GALE && _user.isPassiveStatus(L1PassiveId.STRIKER_GALE_SHOT)) {
				castgfx = 20411;// 스트라이커 게일:샷 을 배우고있을경우
			} else if (_skillId == BURNING_SHOT && !_player._isBurningShot) {
				castgfx = 20455;// 버닝샷 해제
			} else if (_skillId == TYRANT && _user.isPassiveStatus(L1PassiveId.TYRANT_EXCUTION)) {
				castgfx = 21437;// 타이런트: 엑스큐션
			}
		}
		
		if (_user instanceof L1PcInstance) {
			// TODO 액션 시간
			_player.getSpeedSync().setAttackSyncInterval(_player.getAcceleratorChecker().getRightInterval(actionId == ActionCodes.ACTION_SkillAttack ? ACT_TYPE.SPELL_DIR : ACT_TYPE.SPELL_NODIR) + _currentTime + Config.SPEED.SPELL_SPEED_SYNCHRONIZED);
			
			if (_skillId == REDUCTION_ARMOR && _user.isPassiveStatus(L1PassiveId.REDUCTION_ARMOR_VETERAN)) {
				_player.send_effect(18966);
			}
			
			boolean pink = false;
			if (_skillId == FIRE_WALL || _skillId == LIFE_STREAM || _skillId == PRESSURE || _skillId == VISION_TELEPORT) {
				if (_skillId == FIRE_WALL || _skillId == PRESSURE) {
					directionChange();// 방향 타겟 전환
				}
				_player.broadcastPacketWithMe(new S_DoActionGFX(_player.getId(), actionId), true);
				return;
			}
			
			if (_skillId == GRACE || _skillId == IMPACT || _skillId == CUBE_OGRE || _skillId == CUBE_GOLEM || _skillId == CUBE_LICH || _skillId == CUBE_AVATAR) {
				if (_player.isInParty()) {
					int effectId = 0;
					switch(_skillId){
					case GRACE:			effectId = 14495;break;
					case IMPACT:		effectId = 14513;break;
					case CUBE_OGRE:		effectId = 17270;break;
					case CUBE_GOLEM:	effectId = 17268;break;
					case CUBE_LICH:		effectId = 17269;break;
					case CUBE_AVATAR:	effectId = 17271;break;
					}
					for (L1PcInstance tar : L1World.getInstance().getVisiblePlayer(_player, 18)) {
						if (!_player.getParty().isMember(tar)) {
							continue;
						}
						if ((_skillId == CUBE_GOLEM && tar.isPassiveStatus(L1PassiveId.ILLUTION_GOLEM)) || (_skillId == CUBE_LICH && tar.isPassiveStatus(L1PassiveId.ILLUTION_RICH))) {
							continue;
						}
						tar.sendPackets(new S_SpellBuffNoti(tar, _skillId, true, _getBuffIconDuration), true);
						tar.send_effect(effectId);
						if (tar.isPinkName()) {
							pink = true;
						}
					}
				}
				if ((_skillId == CUBE_GOLEM && _user.isPassiveStatus(L1PassiveId.ILLUTION_GOLEM)) || (_skillId == CUBE_LICH && _user.isPassiveStatus(L1PassiveId.ILLUTION_RICH))) {
					return;
				}
				_player.sendPackets(new S_SpellBuffNoti(_player, _skillId, true, _getBuffIconDuration), true);
			} else if (_skillId == MASS_IMMUNE_TO_HARM) {
				if (status.hasSkillEffect(IMMUNE_TO_HARM)) {
					status.removeSkillEffect(IMMUNE_TO_HARM);
				}
				L1SkillUse imun = new L1SkillUse();
				imun.handleCommands(_player, IMMUNE_TO_HARM, _player.getId(), _player.getX(), _player.getY(), 0, L1SkillUseType.GMBUFF);
				imun = null;
				for (L1Object obj : L1World.getInstance().getVisiblePlayer(_player, 8)) {
					if (_player.glanceCheck(8, obj.getX(), obj.getY(), obj instanceof L1DoorInstance) == false) {
						continue;// 벽넘어 제외
					}
					if (obj instanceof L1PcInstance) {
						L1PcInstance tar = (L1PcInstance) obj;
						if (tar.getClanid()!=0 && tar.getClanid() == _player.getClanid() && _player.getParty().isMember(tar)) {
							if (tar.getSkill().hasSkillEffect(IMMUNE_TO_HARM)) {
								tar.getSkill().removeSkillEffect(IMMUNE_TO_HARM);
							}
							imun = new L1SkillUse(true);
							imun.handleCommands(tar, IMMUNE_TO_HARM, tar.getId(), tar.getX(), tar.getY(), 0, L1SkillUseType.GMBUFF);
							imun = null;
							if (tar.isPinkName()) {
								pink = true;
							}
						}
					}
				}
			}
			if (pink) {
				L1PinkName.onAction(_player);
			}

			int targetid = _target.getId();
				
			// 시전 이팩트
			if (_skillId == DESPERADO) {// 데스페라도 시전이팩
				if (_target instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _target;
					pc.send_effect(_user.isPassiveStatus(L1PassiveId.DESPERADO_ABSOLUTE) ? 17233 : castgfx);
				} else if (_target instanceof L1NpcInstance) {
					_target.broadcastPacket(new S_Effect(_target.getId(), _user.isPassiveStatus(L1PassiveId.DESPERADO_ABSOLUTE) ? 17233 : castgfx), true);
				}
				return;
			}
			if (_skillId == SHOCK_STUN || _skillId == FORCE_STUN
					|| _skillId == MOB_SHOCKSTUN_18 || _skillId == MOB_SHOCKSTUN_19 || _skillId == MOB_SHOCKSTUN_30
					|| _skillId == MOB_RANGESTUN_18 || _skillId == MOB_RANGESTUN_19 
					|| _skillId == MOB_RANGESTUN_20 || _skillId == MOB_RANGESTUN_30
					|| _skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8
					|| _skillId == VALLAKAS_PREDICATE2 || _skillId == VALLAKAS_PREDICATE4 || _skillId == VALLAKAS_PREDICATE5) {// 쇼크스턴 시전이팩
				if (_target instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _target;
					pc.send_effect(4434);
				} else if (_target instanceof L1NpcInstance) {
					_target.broadcastPacket(new S_Effect(_target.getId(), 4434), true);
				}
				return;
			}
			if (_skillId == EMPIRE) {
				if (_target instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _target;
					pc.send_effect(_user.isPassiveStatus(L1PassiveId.EMPIRE_OVERLOAD) ? 20405 : castgfx);
				} else if (_target instanceof L1NpcInstance) {
					_target.broadcastPacket(new S_Effect(_target.getId(), _user.isPassiveStatus(L1PassiveId.EMPIRE_OVERLOAD) ? 20405 : castgfx), true);
				}
				return;
			}
			if (_skillId == AVENGER) {
				if (_target instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _target;
					pc.send_effect(castgfx);
				} else if (_target instanceof L1NpcInstance) {
					_target.broadcastPacket(new S_Effect(_target.getId(), castgfx), true);
				}
				return;
			}
			if (_skillId == CRUEL) {
				_player.send_effect(_player._isLancerForm ? (_user.isPassiveStatus(L1PassiveId.CRUEL_CONVICTION) ? 19745 : 19323) : (_user.isPassiveStatus(L1PassiveId.CRUEL_CONVICTION) ? 19742 : 19320));
				return;
			}
			if (_skillId == OSIRIS) {
				_player.send_effect(castgfx);
				return;
			}
			if (_skillId == SMASH) {
				if (_targetList.size() == 0) {
					return;
				}
				if (_target instanceof L1PcInstance) {
					((L1PcInstance) _target).send_effect(6526);
				} else if (_target instanceof L1NpcInstance) {
					_target.broadcastPacket(new S_Effect(_target.getId(), 6526), true);
				}
				return;
			}
			
			if (ON_ACTION_SKILL_LIST.contains(_skillId)) {// 타켓 온액션 처리를 위해
				return;
			}
			
			if (_skillId == STRIKER_GALE && _user.isPassiveStatus(L1PassiveId.STRIKER_GALE_SHOT)) {
				_player.send_effect(19575);
			}

			if (_skillId == LIGHT) {
				((L1PcInstance) _target).sendPackets(S_Sound.LIGHT_SOUND);
			}
			if (_skillId == SOUL_OF_FLAME) {
				((L1PcInstance) _target).send_effect(11778);
				((L1PcInstance) _target).sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 11778, 320), true);
			}

			if (_targetList.size() == 0 && !_isTargetNone) {
				int spriteId = _player.getSpriteId();
				if (spriteId == 5727 || spriteId == 5730) {
					actionId = ActionCodes.ACTION_SkillBuff;
				} else if (spriteId == 5733 || spriteId == 5736) {
					actionId = ActionCodes.ACTION_Attack;
				}
				
				if (isSkillAction) {
					_player.broadcastPacketWithMe(new S_DoActionGFX(_player.getId(), actionId), true);
				}
				return;
			}

			if (_isTargetAttack && _skillId != TURN_UNDEAD) {
				if (isPcSummonPet(_target) && (_player.getRegion() == L1RegionStatus.SAFETY || _target.getRegion() == L1RegionStatus.SAFETY || _player.checkNonPvP(_player, _target))) {
					_player.broadcastPacketWithMe(new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId), true);
					return;
				}
				// 단일 공격
				if (_skill.getArea() == 0) {
					if (_skillId == THUNDER_GRAB && _user.isPassiveStatus(L1PassiveId.THUNDER_GRAB_BRAVE)) {
						directionChange();
						_player.broadcastPacketWithMe(new S_DoActionGFX(_player.getId(), _skill.getActionId()), true);
						_player.send_effect(targetid, 17229);
					} else if (_skillId == BLADE || _skillId == TOMAHAWK || _skillId == SHOCK_ATTACK || _skillId == TYRANT || _skillId == ENSNARE) {
						directionChange();
						_player.broadcastPacketWithMe(new S_DoActionGFX(_player.getId(), _skill.getActionId()), true);
						_player.sendPackets(new S_DamageTime(_player.getId(), _targetID), true);
						_player.send_effect(targetid, castgfx);
					} else {
					    _player.broadcastPacketWithMe(new S_UseAttackSkill(_player, targetid, castgfx, _targetX, _targetY, actionId), true);
					}
					if (_isDamageHit) {
						_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _player, true);
					}
				}
				// 범위 공격
				else {
					L1Character[] cha = new L1Character[_targetList.size()];
					int i = 0;
					for (TargetStatus ts : _targetList) {
						cha[i] = ts.getTarget();
						i++;
					}
					_player.broadcastPacketWithMe(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR), true);
				}
			} else if (_isTargetNone && _skill.getType() == L1Skills.SKILL_TYPE.ATTACK) {
				L1Character[] cha = new L1Character[_targetList.size()];
				int i = 0;
				for (TargetStatus ts : _targetList) {
					cha[i] = ts.getTarget();
					if (_isDamageHit) {
						cha[i].broadcastPacketExceptTargetSight(new S_DoActionGFX(cha[i].getId(), ActionCodes.ACTION_Damage), _player, true);
					}
					i++;
				}
				if (_skill.getRanged() == 0) {
					_player.broadcastPacketWithMe(new S_DoActionGFX(_player.getId(), _skill.getActionId()), true);
					_player.send_effect(castgfx);
				} else {
					_player.broadcastPacketWithMe(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), true);
				}
			} else {
				if (_skillId != TELEPORT && _skillId != MASS_TELEPORT && _skillId != TELEPORT_TO_MATHER) {
					if (isSkillAction) {
						_player.broadcastPacketWithMe(new S_DoActionGFX(_player.getId(), _skill.getActionId()), true);
					}
					if (_skillId == COUNTER_MAGIC || _skillId == COUNTER_BARRIER) {
						_player.send_effect(targetid, castgfx);
					} else if (_skillId == TRUE_TARGET) {
						return;
					} else if (_skillId == ARMOR_BREAK) {
						S_Effect effect = new S_Effect(targetid, castgfx);
						if (_player.isInParty()) {
							for (L1PcInstance member : _player.getParty().getMembersArray()) {
								member.sendPackets(effect);
							}
						} else {
							_player.sendPackets(effect);
						}
						effect.clear();
						effect = null;
					} else if (_skillId == ASSASSIN) {
						_player.sendPackets(new S_Effect(targetid, castgfx), true);
					} else {
						_player.send_effect(targetid, castgfx);
					}
					if (_skillId == SHINING_SHIELD) {
						_player.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 3941, _getBuffIconDuration), true);
					}
				}
				for (TargetStatus ts : _targetList) {
					L1Character cha = ts.getTarget();
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_OwnCharStatus(pc), true);
					}
				}
			}
		} else if (_user instanceof L1NpcInstance) {
			int targetid = _target.getId();
			if (_skillId == BLACKELDER) {
				_user.broadcastPacket(new S_Effect(_user.getId(), 4848), true);
				_user.broadcastPacket(new S_Effect(_user.getId(), 2552), true);
			}
			if (_user instanceof L1MerchantInstance) {
				_user.broadcastPacket(new S_Effect(targetid, castgfx), true);
				return;
			}
			
			/** 몬스터 스턴 모션 **/
			if (_user instanceof L1MonsterInstance && (_skillId == MOB_SHOCKSTUN_18 || _skillId == MOB_SHOCKSTUN_19)) {
				L1MonsterInstance mob = (L1MonsterInstance) _user;
				if (mob.getNpcTemplate().getNpcId() == 8506 || mob.getNpcTemplate().getNpcId() == 8512) {
					mob.broadcastPacket(new S_DoActionGFX(mob.getId(), 1), true);
					mob.broadcastPacket(new S_Effect(targetid, 4434), true);
					return;
				}
			}

			if (_targetList.size() == 0 && !_isTargetNone) {
				_user.broadcastPacket(new S_DoActionGFX(_user.getId(), _skill.getActionId()), true);
				return;
			}
			
			/** 펫 속성 스킬 이펙트 타겟 지정 **/
			if (_user instanceof L1PetInstance) {
				if (_skillId == PET_ATTR_FIRE) {
					_target.broadcastPacket(new S_Effect(_target.getId(), 17322), true);// 불속성 공격
				} else if (_skillId == PET_ATTR_WATER) {
					_target.broadcastPacket(new S_Effect(_target.getId(), 17320), true);// 물속성 공격
				} else if (_skillId == PET_ATTR_WIND) {
					_target.broadcastPacket(new S_Effect(_target.getId(), 17318), true);// 바람속성 공격
				} else if (_skillId == PET_ATTR_EARTH) {
					_target.broadcastPacket(new S_Effect(_target.getId(), 17324), true);// 땅속성 공격
				}
			}
			
			if (_isTargetAttack && _skillId != TURN_UNDEAD) {
				if (_skill.getArea() == 0) {
					_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx, _targetX, _targetY, actionId), true);
					if (actionId2 > 0 && castgfx2 > 0) {
						_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx2, _targetX, _targetY, actionId2), true);
					}
					if (actionId3 > 0 && castgfx3 > 0) {
						_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx3, _targetX, _targetY, actionId3), true);
					}
					if (_isDamageHit) {
						_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user, true);
					}
				} else {
					L1Character[] cha = new L1Character[_targetList.size()];
					int i = 0;
					for (TargetStatus ts : _targetList) {
						cha[i] = ts.getTarget();
						if (_isDamageHit) {
							cha[i].broadcastPacketExceptTargetSight(new S_DoActionGFX(cha[i].getId(), ActionCodes.ACTION_Damage), _user, true);
						}
						i++;
					}
					_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR), true);
					if (actionId2 > 0 && castgfx2 > 0) {
						_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx2, actionId2, S_RangeSkill.TYPE_DIR), true);
						if (_isDamageHit) {
							_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user, true);
						}
					}
					if (actionId3 > 0 && castgfx3 > 0) {
						_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx3, actionId3, S_RangeSkill.TYPE_DIR), true);
						if (_isDamageHit) {
							_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user, true);
						}
					}
				}
			} else if (_isTargetNone && _skill.getType() == L1Skills.SKILL_TYPE.ATTACK) {
				L1Character[] cha = new L1Character[_targetList.size()];
				int i = 0;
				for (TargetStatus ts : _targetList) {
					cha[i] = ts.getTarget();
					i++;
				}
				_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), true);
			} else {
				if (_skillId != TELEPORT && _skillId != MASS_TELEPORT && _skillId != TELEPORT_TO_MATHER) {
					_user.broadcastPacket(new S_DoActionGFX(_user.getId(), _skill.getActionId()), true);
					_user.broadcastPacket(new S_Effect(targetid, castgfx), true);
					if (actionId2 > 0 && castgfx2 > 0) {
						_user.broadcastPacket(new S_DoActionGFX(_user.getId(), _skill.getActionId2()), true);
						_user.broadcastPacket(new S_Effect(targetid, castgfx2), true);
					}
					if (actionId3 > 0 && castgfx3 > 0) {
						_user.broadcastPacket(new S_DoActionGFX(_user.getId(), _skill.getActionId3()), true);
						_user.broadcastPacket(new S_Effect(targetid, castgfx3), true);
					}
				}
			}
		}
	}

	/**
	 * 중복할 수 없는 스킬
	 * @param cha
	 */
	void deleteRepeatedSkills(L1Character cha) {
		for (int[] skills : REPEATED_SKILLS) {
			for (int id : skills) {
				if (id == _skillId) {
					stopSkillList(cha, skills);
				}
			}
		}
	}

	void stopSkillList(L1Character cha, int[] repeat_skill) {
		for (int skillId : repeat_skill) {
			if (skillId != _skillId) {
				if (!cha.getSkill().hasSkillEffect(skillId)) {
					continue;
				}
				cha.getSkill().removeSkillEffect(skillId);
				if (cha instanceof L1PcInstance && PROTO_ICON_SKILL_LIST.contains(skillId)) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SpellBuffNoti(pc, skillId, false, -1), true);
				}
			}
		}
	}

	/**
	 * 스킬사용 딜레이
	 */
	void setDelay() {
		if (_player == null) {
			return;
		}
		int group_id		= _skill.getDelayGroupId();
		double[] interval	= _player.getAcceleratorChecker().getRightSkillInterval(_skill);
		int delay			= (int)interval[0];
		int global_delay	= (int)interval[1];
		if (_skillId == PANTERA && _player.isPassiveStatus(L1PassiveId.PANTERA_SHOCK)) {
			delay += 1000;
		} else if (_skillId == VANGUARD && _player.getAbility().getVanguardDecrease() > 0) {
			delay *= 0.8D;
		}
		if (delay > 60000) {// 1분 초과 딜레이가 발생 시 디비 저장 설정
			setDelayOneMinutOver(group_id, (long)delay);
		}
		int set_delay = delay + Config.SPEED.SPELL_DELAY_SYNCHRONIZED;
		if (set_delay <= 0) {
			System.out.println(String.format("[L1SkillUse] ZERO_DELAY : CHAR(%s), SPELL_ID(%d), GROUP(%d)", _player.getName(), _skillId, group_id));
		} else {
			L1SkillDelay.onSkillUse(_player, set_delay, group_id);
			_player.getSkill().setSkillDelayDuration(group_id, (long)(delay + _currentTime));
		}
		_player.sendPackets(S_SpellLateHandlingNoti.NOT_CORRECTION);
		_player.sendPackets(new S_SpellDelay(group_id, delay, global_delay), true);
	}
	
	/**
	 * 1분 초과 딜레이 DB저장 설정
	 * @param group_id
	 * @param delay
	 */
	void setDelayOneMinutOver(int group_id, long delay) {
		switch(group_id){
		case 0:
			if (status.getDefaultSkillDelay() == null) {
				status.setDefaultSkillDelay(new Timestamp(_currentTime + delay));
			} else {
				status.getDefaultSkillDelay().setTime(_currentTime + delay);
			}
			break;
		case 1:
			if (status.getFirstSkillDelay() == null) {
				status.setFirstSkillDelay(new Timestamp(_currentTime + delay));
			} else {
				status.getFirstSkillDelay().setTime(_currentTime + delay);
			}
			break;
		case 2:
			if (status.getSecondSkillDelay() == null) {
				status.setSecondSkillDelay(new Timestamp(_currentTime + delay));
			} else {
				status.getSecondSkillDelay().setTime(_currentTime + delay);
			}
			break;
		case 3:
			if (status.getThirdSkillDelay() == null) {
				status.setThirdSkillDelay(new Timestamp(_currentTime + delay));
			} else {
				status.getThirdSkillDelay().setTime(_currentTime + delay);
			}
			break;
		case 4:
			if (status.getFourthSkillDelay() == null) {
				status.setFourthSkillDelay(new Timestamp(_currentTime + delay));
			} else {
				status.getFourthSkillDelay().setTime(_currentTime + delay);
			}
			break;
		case 5:
			if (status.getFiveSkillDelay() == null) {
				status.setFiveSkillDelay(new Timestamp(_currentTime + delay));
			} else {
				status.getFiveSkillDelay().setTime(_currentTime + delay);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 스킬 작동
	 */
	void runSkill() {
		if (_player != null && _player.isInvisble()
				&& (_skill.getType() == L1Skills.SKILL_TYPE.ATTACK || _skill.getType() == L1Skills.SKILL_TYPE.CURSE || _skill.getType() == L1Skills.SKILL_TYPE.PROB)
				&& _skillId != AREA_OF_SILENCE) {// 에싸는 안풀림
			_player.delInvis();
		}

		if (_skillId == DISINTEGRATE && _target instanceof L1PcInstance) {
			if (_target.getSkill().hasSkillEffect(ANTI_DISINTEGRATE)) {
				return;
			}
			_target.getSkill().setSkillEffect(ANTI_DISINTEGRATE, 2000);
		} else if (_skillId == METEOR_STRIKE && _target instanceof L1PcInstance) {
			if (_target.getSkill().hasSkillEffect(ANTI_METEOR)) {
				return;
			}
			_target.getSkill().setSkillEffect(ANTI_METEOR, 2000);
		} else if (_skillId == LIFE_STREAM){
			L1EffectSpawn.getInstance().spawnEffect(81169, _skill.getBuffDuration() * 1000, _targetX, _targetY, _user.getMapId());		
			return;
		} else if (_skillId == FIRE_WALL){
			L1EffectSpawn.getInstance().doSpawnFireWall(_user, _targetX, _targetY);
			return;
		} else if (_skillId == VISION_TELEPORT){
			_player.getTeleport().startMoveSkill(null, _targetX, _targetY, (byte)_user.getMoveState().getHeading(), _skillId, null);
			return;
		}
		
		/** 카운터 매직 방어 체크 */
		if (EXCEPT_COUNTER_MAGIC_LIST.contains(_skillId)) {
			_isCounterMagic = false;
		}
		if (_skillId == DISINTEGRATE && _user.isPassiveStatus(L1PassiveId.DISINTEGREATE_NEMESIS)) {
			_isCounterMagic = false;// 카운터 매직 무시
		}

		/** 타겟 공격 액션 취하기 **/
		if (ON_ACTION_SKILL_LIST.contains(_skillId) && _user instanceof L1PcInstance) {
			_target.onAction(_player);
		}

		if (!isTargetCalc(_target)) {
			return;
		}

		// 독 구름
		if (_skillId == DESERT_SKILL4 || _skillId == STATE_POISON || _skillId == STATE_POISON1 || _skillId == STATE_POISON2) {
			EffectSpawn();
		}
		
		boolean not_alignment_penalty = NOT_ALIGNMENT_PENALTY_SKILL_LIST.contains(_skillId) && _user instanceof L1PcInstance;
		if (not_alignment_penalty) {
			_player.getConfig().setNotAlignmentPenalty(true);
		}
		
		try {
			TargetStatus ts;
			L1Character cha;
			L1Magic magic;
			int dmg, drainMana, heal;
			L1Undead undeadType;
			boolean isSuccess;
			boolean isNotDeleteEraseMagic	= NOT_DELETE_ERASE_MAGIC_SKILL_LIST.contains(_skillId);
			boolean isNotRepeatSkill		= NOT_REPEAT_SKILL_LIST.contains(_skillId);
			
			// TODO 취득한 타겟에게 적용할 for문
			for (Iterator<TargetStatus> iter = _targetList.iterator(); iter.hasNext();) {
				// init
				ts		= null;
				cha		= null;
				magic	= null;
				dmg = drainMana = heal = 0;
				undeadType = null;
				isSuccess = false;
				
				// setting
				ts	= iter.next();// 순차적 타겟 최득
				cha	= ts.getTarget();
				if (!ts.isCalc() || !isTargetCalc(cha)) {
					continue;
				}

				magic = new L1Magic(_user, cha);
				magic.setLeverage(getLeverage());

				if (cha instanceof L1MonsterInstance) {
					undeadType = ((L1MonsterInstance) cha).getNpcTemplate().getUndead();// 언데드 몬스터 세팅
				}

				if ((_skill.getType() == L1Skills.SKILL_TYPE.CURSE || _skill.getType() == L1Skills.SKILL_TYPE.PROB) && isTargetFailure(cha)) {
					iter.remove();
					continue;
				}
				
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if ((_skillId == CUBE_GOLEM && pc.isPassiveStatus(L1PassiveId.ILLUTION_GOLEM)) || (_skillId == CUBE_LICH && pc.isPassiveStatus(L1PassiveId.ILLUTION_RICH))) {
						continue;
					}
					_getBuffIconDuration = _skillTime == 0 ? _skill.getBuffDuration() : _skillTime;
					if ((_skillId == IMMUNE_TO_HARM && _user.isPassiveStatus(L1PassiveId.IMMUNE_T0_HARM_SAINT) && _user.getId() == cha.getId()) 
							|| (_skillId != IMMUNE_TO_HARM && _skill.getAfterPassive() != null && _user.isPassiveStatus(_skill.getAfterPassive().getPassive()) && _skill.getAfterPassive().getDuration() > 0)) {
						_getBuffIconDuration = _skill.getAfterPassive().getDuration();
					}
					if (pc.isDragonknight() && (_skillId == SCALES_EARTH_DRAGON || _skillId == SCALES_WATER_DRAGON || _skillId == SCALES_FIRE_DRAGON || _skillId == SCALES_WIND_DRAGON)) {
						aurakiaResult();
					}
				}
				
				deleteRepeatedSkills(cha);

				if (_skill.getType() == L1Skills.SKILL_TYPE.ATTACK && _user.getId() != cha.getId()) {// 공격 계열 스킬
					if (isUseCounterMagic(cha)) {
						iter.remove();
						continue;
					}
					dmg					= magic.calcMagicDamage(_skillId);
					_isCriticalDamage	= magic.isCriticalDamage();
					_isDamageHit		= magic.isDamageHit();

					// 공격 스킬일때!! 이레이즈 여부 판멸후 제거
					if ((cha.getSkill().hasSkillEffect(ERASE_MAGIC) || cha.getSkill().hasSkillEffect(MOB_ERASE_MAGIC)) && !isNotDeleteEraseMagic) {
						deleteEraseMagic(cha);
					}
				} else if (_skill.getType() == L1Skills.SKILL_TYPE.CURSE || _skill.getType() == L1Skills.SKILL_TYPE.PROB) {// 확률 계열 스킬
					isSuccess = magic.calcProbabilityMagic(_skillId);// 성공여부 취득
					if (_skill.getDamageValue() > 0) {
						dmg = magic.calcMagicDamage(_skillId);
						if (_skillId == TEMPEST && (dmg = (int)(_player.getMaxHp() * 0.1D)) > 1000) {
							dmg = 1000;// HP에 비례하여 대미지 설정
						} else if (_skillId == CHAIN_REACTION || _skillId == BEHEMOTH) {// 체인 리액션, 베히모스 대미지 개별 처리
							dmg = 0;
						}
					}
					if (cha instanceof L1PcInstance && _user instanceof L1PcInstance) {
						L1PinkName.onAction((L1PcInstance) cha, _user);
					}
					if (_skillId == MOB_DEATH_HEAL && isSuccess && cha instanceof L1PcInstance) {// 몬스터 데스힐
						int randuration = random.nextInt(5) + 7;// 1 ~ 10
						if (cha.getSkill().hasSkillEffect(_skillId)) {
							cha.getSkill().removeSkillEffect(_skillId);
						}
						cha.getSkill().setSkillEffect(_skillId, randuration * 1000);	
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, randuration), true);
						pc.sendPackets(L1ServerMessage.sm4737);
						return;
					}
					// 이레 마법이 아니고 현재 이레중이라면!!!
					if ((cha.getSkill().hasSkillEffect(ERASE_MAGIC) || cha.getSkill().hasSkillEffect(MOB_ERASE_MAGIC)) && _skillId != ERASE_MAGIC && _skillId != EARTH_BIND && _skillId != MOB_ERASE_MAGIC) {
						deleteEraseMagic(cha);
					}
					if (_skillId != FOG_OF_SLEEPING) {
						cha.getSkill().removeSkillEffect(FOG_OF_SLEEPING);
					}
					if (_skillId != PHANTASM) {
						cha.getSkill().removeSkillEffect(PHANTASM);
					}
					if (_skillId != SHADOW_SLEEP) {
						cha.getSkill().removeSkillEffect(SHADOW_SLEEP);
					}
					
					if (isSuccess) {// 확률 스킬 성공
						if (isUseCounterMagic(cha)) {// 카운터 매직
							iter.remove();
							continue;
						}
					} else {// 확률 스킬 실패
						if (_skillId == FOG_OF_SLEEPING && cha instanceof L1PcInstance) {
							((L1PcInstance) cha).sendPackets(L1ServerMessage.sm297);
						}
						if (dmg > 0) {
							sendSkillDamage(cha, magic, dmg, drainMana);
						}
						iter.remove();
						continue;// 건너띈다
					}
				} else if (_skill.getType() == L1Skills.SKILL_TYPE.HEAL) {// 힐 계열 스킬
					if (cha instanceof L1SummonInstance) {
						dmg = 0;
						continue;
					}
					dmg = -1 * magic.calcHealing(_skill);
					if (cha.getAbility().getVitalHeal() > 0) {
						dmg -= ((dmg / 100) * cha.getAbility().getVitalHeal());
					}
					if (cha.getSkill().hasSkillEffect(WATER_LIFE)) {
						dmg <<= 1;
					}
					if (cha.isPolluteWater() || cha.getSkill().hasSkillEffect(PAP_REDUCE_HEAL)) {
						dmg >>= 1;
					}
					if (cha.getSkill().hasSkillEffect(STATUS_PHANTOM_REQUIEM)) {
						dmg *= Config.SPELL.PHANTOM_REQUIEM_POTION_RATE;
					}
					if (cha.isDeathHeal()) {
						dmg = Math.abs(dmg);
					}
					if (cha.getSkill().hasSkillEffect(STATUS_PHANTOM_DEATH)) {
						dmg = (int)(Math.abs(dmg) * Config.SPELL.PHANTOM_DEATH_POTION_RATE);
					}
					if (cha.getSkill().hasSkillEffect(HALPAS_JUJOO_MP_DAMAGE)) {
						cha.setCurrentMp(cha.getCurrentMp() + dmg);
					}
				}

				// 이미 적용 되어 있는 스킬인 경우 시간만 재설정한다
				if (cha.getSkill().hasSkillEffect(_skillId) && !isNotRepeatSkill) {
					addMagicList(cha, true);// 재사용 스킬 타이머 갱신
					continue;
				}
				
				// 담당 핸들러 스킬 처리
				if (_handler != null) {
					int value = _handler.start(_user, cha, _getBuffIconDuration, magic);
					// 반환값 처리
					if (value > 0) {
						switch(_skillId){
						// 마나흡수
						case MANA_DRAIN:
							drainMana = value;
							break;
						// 대미지 증폭
						case DISINTEGRATE:
							dmg += dmg >> 2;// 네메시스 피격 대미지 상승 1.25배
							break;
						// 대미지 추가
						case AVENGER:
						case COUNTER_DETECTION:
						case MIND_BREAK:
						case TURN_UNDEAD:
						case RIND_ANSIG:
						case ANTA_ANSIG:
						case VALA_ANSIG:
						case FAFU_ANSIG:
							dmg += value;
							break;
						// 스킬의 지속시간 변경
						default:
							_changeBuffDuration = value;
							if (_skillId == DESPERADO) {
								dmg += 100;// 데스페라도 적중시 대미지 증가
							} else if (_skillId == CRUEL) {
								L1ItemInstance currentWeapon = _player.getWeapon();
								if (currentWeapon != null) {
									dmg += (random.nextInt(currentWeapon.getItem().getDmgSmall()) + currentWeapon.getItem().getDmgRate() + currentWeapon.getEnchantLevel() + _player.getAbility().getTotalStr()) + 2;
								}
							}
							break;
						}
					}
					
					// 대미지, 힐, 마나흡수 처리
					if (_skill.getType() == L1Skills.SKILL_TYPE.HEAL && _calcType == PC_NPC) {
						if (undeadType == L1Undead.UNDEAD) {
							dmg *= -1;
						} else if (undeadType == L1Undead.UNDEAD_BOSS) {
							dmg = 0;
						}
					}
					if ((cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) && dmg < 0) {
						dmg = 0;
					}
					if (dmg != 0 || drainMana != 0) {
						sendSkillDamage(cha, magic, dmg, drainMana);
					}
					if (heal > 0) {
						_user.setCurrentHp(heal + _user.getCurrentHp() > _user.getMaxHp() ? _user.getMaxHp() : heal + _user.getCurrentHp());
					}
					if (_skillId == ICE_LANCE && value == 0) {
						continue;// 아이스랜스 실패시 타이머 처리안함
					}
					addMagicList(cha, false);// 타이머 시작
					if (cha instanceof L1PcInstance) {
						_handler.wrap((L1PcInstance)cha, true);// 핸들러 마무리
					}
					continue;
				}

				// ●●●● PC, NPC 양쪽 모두 효과가 있는 스킬 ●●●●
				switch (_skillId) {
				case ABSOLUTE_BLADE:
				case MEDITATION:
				case LIBERATION:
				case BRAVE_MENTAL:
				case ELEMENTAL_FIRE:
				case QUAKE:
				case COUNTER_BARRIER:
				case DECAY_POTION:
				case FATAL_POTION:
				case ASSASSIN:
				case SOUL_BARRIER:
				case REDUCTION_ARMOR:
				case BLOW_ATTACK:
				case LUCIFER:
				case CYCLONE:
				case INFERNO:
				case TITAN_RISING:
				case HALPHAS:
				case MP_REDUCTION_POTION:
				case BUFF_PUFFER_FISH:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, _getBuffIconDuration), true);
					}
					break;
				case CHILL_TOUCH:
				case VAMPIRIC_TOUCH:
					heal = dmg >> 1;
					break;

				case ANTA_MESSAGE_1: // 안타[용언1 / 캔슬 -> 오브 모크! 케 네시]
				case ANTA_MESSAGE_2: // 안타[용언2 / 블레스+독/ 오브 모크! 켄 로우]
				case ANTA_MESSAGE_3: // 안타[용언3 / 왼손+오른펀치+고함 / 오브 모크! 티기르]
				case ANTA_MESSAGE_4: // 안타[용언4 / 펀치+블레스 / 오브 모크! 켄 티기르]
				case ANTA_MESSAGE_5: // 안타[용언5 / 고함+블레스 / 오브 모크! 루오타]
				case ANTA_MESSAGE_6: // 안타[용언6 / 스턴+점프/ 오브 모크! 뮤즈삼]
				case ANTA_MESSAGE_7: // 안타[용언7 / 스턴+발작/ 오브 모크! 너츠삼]
				case ANTA_MESSAGE_8: // 안타[용언8 / 스턴+발+점/ 오브 모크! 티프삼]
				case ANTA_MESSAGE_9: // 안타[용언9 / 웨폰+블레스 / 오브 모크! 리라프]
				case ANTA_MESSAGE_10: // 안타[용언10 / 웨폰+마비 / 오브 모크! 세이 라라프]
				case ANTA_CANCELLATION:
				case ANTA_WEAPON_BREAK:
				case ANTA_SHOCKSTUN:
					if ((_skillId == ANTA_MESSAGE_1 || _skillId == ANTA_CANCELLATION) && cha instanceof L1PcInstance) { // 캔슬
						L1PcInstance pc = (L1PcInstance) cha;
						L1SkillUse cancle = new L1SkillUse(true);
						cancle.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
						cancle = null;
					}

                    /** 마비독 **/
					if ((_skillId == ANTA_MESSAGE_1 || _skillId == ANTA_MESSAGE_10) && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						int time = random.nextInt(5) + 1;
						if (time > 10) {
							L1ParalysisPoison.doInfection(pc, 5, time * 1000);
						}
					}
                    /** 대미지독 **/
					if ((_skillId == ANTA_MESSAGE_2 || _skillId == ANTA_MESSAGE_5 || _skillId == ANTA_MESSAGE_9) && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						int PoisonDmg = random.nextInt(50) + 1;
						int PoisonTime = random.nextInt(15) + 1;
						if (PoisonTime > 2) {
							L1DamagePoison.doInfection(pc, _target, PoisonTime * 1000, PoisonDmg, false);
						}
					}
					/** 쇼크 스턴 **/
					if (_skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8 || _skillId == ANTA_SHOCKSTUN) {
						_changeBuffDuration = CommonUtil.randomIntChoice(MONSTER_STUN_ARRAY);
						L1EffectSpawn.getInstance().spawnEffect(91162, _changeBuffDuration, cha.getX(), cha.getY(), cha.getMapId());
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(S_Paralysis.STURN_ON);
							pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_STUN, true, _getBuffIconDuration), true);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_changeBuffDuration);
						}
					}
					/** 웨폰 브레이크 **/
					if ((_skillId == ANTA_MESSAGE_9 || _skillId == ANTA_MESSAGE_10 || _skillId == ANTA_WEAPON_BREAK) && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						L1ItemInstance weapon = pc.getWeapon();
						if (weapon != null) {
							pc.sendPackets(new S_ServerMessage(268, weapon.getLogNameRef()), true);
							pc.getInventory().receiveDamage(weapon, random.nextInt(3) + 1);
							pc.send_effect(172);
						}
					}
					break;
				case MOB_ICE_UNSUK_18: //아이스 운석
				case MOB_ICE_UNSUK_19:
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_user, 10)) {
						if (pc == null) {
							continue;
						}
						if (random.nextInt(100) + 1 < 30) {
							int time = 5000;
							pc.getSkill().setSkillEffect(ICE_LANCE, time);
							L1EffectSpawn.getInstance().spawnEffect(81168,time, cha.getX(), cha.getY(), cha.getMapId());
							pc.broadcastPacketWithMe(new S_Poison(pc.getId(), 2), true);
							pc.sendPackets(S_Paralysis.FREEZE_ON);
						}
					}
					break;
					
				case PAP_PREDICATE1: // 파푸[용언1:리오타! 피로이 나! [오색 진주3 / 신비한 오색 진주1 / 토르나 소환5]
				case PAP_PREDICATE3: // 파푸[용언3:리오타! 라나 오이므! [데스포션 -> 오른손 -> 아이스이럽션]
				case PAP_PREDICATE5: // 파푸[용언5:리오타! 네나 우누스! [리듀스 힐 + 머리 공격 + 아이스브레스]
				case PAP_PREDICATE6: // 파푸[용언6:리오타! 테나 웨인라크! [데스 힐 + 꼬리 공격 + 아이스브레스]
				case PAP_PREDICATE7: // 파푸[용언7:리오타! 라나 폰폰! [캔슬레이션 + 오른속 2번 ] [범위 X]
				case PAP_PREDICATE8: // 파푸[용언8:리오타! 레포 폰폰! [웨폰브레이크 + 왼손 2번 ] [범위 X]
				case PAP_PREDICATE9: // 파푸[용언9:리오타! 테나 론디르 ! [꼬리 2연타 + 아이스브레스][범위 X]
				case PAP_PREDICATE11: // 파푸[용언11:리오타! 오니즈 웨인라크! [매스 캔슬레이션 + 데스 힐 + 아이스 미티어 + 아이스 이럽션] [범위 O]
				case PAP_PREDICATE12: // 파푸[용언12:리오타! 오니즈 쿠스온 웨인라크! [매스 캔슬레이션 + 데스힐 + 아이스 미티어 + 발작] [범위 0]
					/** 리콜 소환(사엘-진주-토르나) **/
					if (_skillId == PAP_PREDICATE1) {
						for (int i = 0; i < 2; i++) { // 타이머 테이크 부분의 for 문으로 돌리게되면 쓰레드 오류 동작이 발생한다.
							L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 5, 900049, 8, 60000, 0);
							L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 5, 900050, 8, 60000, 0);
							L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 5, 900051, 8, 60000, 0);
							L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 5, 900052, 8, 120000, 0);
						}
					}
                    /** 캔슬레이션 **/
					if ((_skillId == PAP_PREDICATE7 || _skillId == PAP_PREDICATE11 || _skillId == PAP_PREDICATE12) && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						L1SkillUse skill = new L1SkillUse(true);
						skill.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
						skill = null;
					}
                    /** 웨폰 브레이크 **/
					if (_skillId == PAP_PREDICATE8 && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						L1ItemInstance weapon = pc.getWeapon();
						int rnd = random.nextInt(100) + 1;
						if (weapon != null && rnd > 33) {
							int weaponDamage = random.nextInt(2) + 1;
							pc.sendPackets(new S_ServerMessage(268, weapon.getLogNameRef()), true);
							pc.getInventory().receiveDamage(weapon, weaponDamage);
							pc.send_effect(172);
						}
					}
					/** 데스 포션 **/
					if (_skillId == PAP_PREDICATE3 && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.send_effect(7781);
						pc.getSkill().setSkillEffect(PAP_DEATH_POTION, 12000);
						pc.sendPackets(new S_SpellBuffNoti(pc, PAP_DEATH_POTION, true, 12), true);
					}
					/** 리듀스 힐 **/
					if (_skillId == PAP_PREDICATE5 && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.send_effect(7782);
						pc.getSkill().setSkillEffect(PAP_REDUCE_HEAL, 12000);
					}
                    /** 데스 힐 **/
					if ((_skillId == PAP_PREDICATE6 || _skillId == PAP_PREDICATE11 || _skillId == PAP_PREDICATE12) && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.send_effect(7780);
						pc.getSkill().setSkillEffect(PAP_DEATH_HEAL, 12000);
					}
					break;
					
				case VALLAKAS_PREDICATE1: //제르큐오 삼 케로누 디스트로이+캔슬+이뮨깨기
				case VALLAKAS_PREDICATE2: //제르큐오 케로누 켈 쥬펜 디스트로이+스턴+이뮨깨기
				case VALLAKAS_PREDICATE3: //제르큐오 베르하 디스트로이+이뮨깨기
				case VALLAKAS_PREDICATE4: //제르큐오 삼 쥬펜 킬리카야 디스트로이+스턴
				case VALLAKAS_PREDICATE5: //제르큐오 삼 킬리카야 스턴
					/** 디스트로이 **/
	                if ((_skillId == VALLAKAS_PREDICATE1 || _skillId == VALLAKAS_PREDICATE2 || _skillId == VALLAKAS_PREDICATE3 || _skillId == VALLAKAS_PREDICATE4) 
	                		&& cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
                	    for (L1ItemInstance armorItem : pc.getInventory().getItems()) {
                            if (armorItem.getItem().getItemType() == L1ItemType.ARMOR && armorItem.getItem().getType() == L1ItemArmorType.ARMOR.getId()) {
                                int armorId = armorItem.getItemId();
                                L1ItemInstance item = pc.getInventory().findEquippedItemId(armorId);
                                if (item != null && item.getDurability() != (armorItem.getItem().getAc() * -1)) {
                                	item.setDurability(item.getDurability() + 1);
                                    pc.getInventory().updateItem(item, L1PcInventory.COL_DURABILITY);
                                    pc.send_effect(14549);
                                    pc.getAC().addAc(1);
                                    pc.sendPackets(new S_ServerMessage(268, armorItem.getLogNameRef()), true);
                                }
                            }
                        }
	                }
	                /** 이뮨깨기 **/
	                if ((_skillId == VALLAKAS_PREDICATE1 || _skillId == VALLAKAS_PREDICATE2 || _skillId == VALLAKAS_PREDICATE3) && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkill().hasSkillEffect(IMMUNE_TO_HARM)) {
						    pc.getSkill().removeSkillEffect(IMMUNE_TO_HARM);
						    pc.send_effect(15961);
						    pc.sendPackets(new S_SpellBuffNoti(pc, IMMUNE_TO_HARM, false, -1), true);
						}
	                }
	                /** 쇼크 스턴 **/
	                if (_skillId == VALLAKAS_PREDICATE2 || _skillId == VALLAKAS_PREDICATE4 || _skillId == VALLAKAS_PREDICATE5) {
	                	_changeBuffDuration = CommonUtil.randomIntChoice(MONSTER_STUN_ARRAY);
						L1EffectSpawn.getInstance().spawnEffect(91162, _changeBuffDuration, cha.getX(), cha.getY(), cha.getMapId());
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(S_Paralysis.STURN_ON);
							pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_STUN, true, _getBuffIconDuration), true);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_changeBuffDuration);
						}
					}
	                /** 캔슬레이션 **/
	                if (_skillId == VALLAKAS_PREDICATE1 && cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						L1SkillUse skill = new L1SkillUse(true);
						skill.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
						skill = null;
	                }
					break;
				default:
					break;
				}

				if (_calcType == PC_PC || _calcType == NPC_PC) {
					switch (_skillId) {
					case TELEPORT:
					case MASS_TELEPORT:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isNotTeleport()) {
								return;
							}
							// 기억 위치 이동
							if (_bookmark_x != 0) {
								if (pc.getNetConnection().isInterServer()) {
									return;
								}
								if (pc.getMap().isEscapable() || pc.isGm()) {
									bookmarkTeleport();
								} else {
									pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
									pc.sendPackets(L1ServerMessage.sm79);
								}
							} else {
								int mapId = pc.getMapId();
								if (mapId >= 101 && mapId <= 111 && pc.getInventory().getOmanAmulet().isOmanTeleportable(mapId)) {
									randomTeleport();
								} else if (pc.getMap().isDominationTeleport() && (pc.getConfig()._dominationTeleportRing || pc.getConfig()._dominationHeroRing)) {
									randomTeleport();
								} else {								
									if (pc.getMap().isTeleportable() || pc.isGm()) {
										if (mapId == 54 && GameTimeNight.isNight()) {// 기란 감옥
											pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
											pc.sendPackets(L1ServerMessage.sm6648);
										} else {
											randomTeleport();
										}
									} else {
										pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
										pc.sendPackets(L1ServerMessage.sm276);
									}
								}
							}
						}
						break;
					case BRING_STONE:
						bringStone();
						break;
					case SUMMON_MONSTER:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getLevel() >= 86 
									&& ((pc.getMap().isRecallPets() && !pc.isInWarArea() && pc.getMapId() != 781 && pc.getMapId() != 782) || pc.isGm())) {
								if (!pc.getPetList().isEmpty()) {
									return;
								}
								if (pc.getInventory().checkItem(20284)) {// 소환조종반지
									summonMonster(pc, _bookmark_x, _bookmark_y);
								} else {
									int summon_id = pc.isPassiveStatus(L1PassiveId.GREAT_SUMMON_MONSTER) ? 810853 : 810840;
									L1Npc npcTemp = NpcTable.getInstance().getTemplate(summon_id);
									new L1SummonInstance(npcTemp, pc, 1);
								}
							} else {
								pc.sendPackets(L1ServerMessage.sm79);
							}
						}
						break;

					// 린드비오르
					case RINDVIOR_SUMMON_MONSTER_CLOUD:
						L1SpawnUtil.spawn(_npc, 5110, 10); // 구름대정령
						break;
					case RINDVIOR_PREDICATE:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_npc.getLocation().getTileLineDistance(pc.getLocation()) > 4) {
								L1Location newLoc = null;
								for (int count = 0; count < 10; count++) {
									newLoc = _npc.getLocation().randomLocation(3, 4, false);
									if (_npc.glanceCheck(15, newLoc.getX(), newLoc.getY(), _target instanceof L1DoorInstance) == false) {		
										pc.getTeleport().start(newLoc, 5, true);
										break;
									}
								}
							}
						}
						break;
					case RINDVIOR_SUMMON_MONSTER: {
						int[] MobId = new int[] { 5106, 5107, 5108, 5109 };// 광물골렘
						int rnd = random.nextInt(100);
						for (int i = 0; i < random.nextInt(2) + 1; i++) {
							L1SpawnUtil.spawn(_npc, MobId[rnd % MobId.length], random.nextInt(3) + 8);
						}
					}
						break;
					case RINDVIOR_SILENCE:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead()) {
								continue;
							}
							pc.getSkill().setSkillEffect(SILENCE, 12000);
							pc.send_effect(2177);
						}
						break;
					case RINDVIOR_BOW:
						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							if (pc.isDead()) {
								continue;
							}
							int SprNum = 0;
							int pcX = pc.getX();
							int pcY = pc.getY();
							int npcId = _npc.getNpcTemplate().getNpcId();
							switch (npcId) {
							case 5097:
								pcY -= 6;
								SprNum = 7987;
								break;
							case 5098:
								pcX += 4;
								pcY -= 4;
								SprNum = 8050;
								break;
							case 5099:
								pcX += 5;
								SprNum = 8051;
								break;
							default:break;
							}
							pc.broadcastPacketWithMe(new S_EffectLocation(pcX, pcY, SprNum), true);
						}
						break;
					case RINDVIOR_WIND_SHACKLE:
					case RINDVIOR_WIND_SHACKLE_1:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead()) {
								continue;
							}
							pc.getSkill().setSkillEffect(WIND_SHACKLE, 12000);
							pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration), true);
							pc.send_effect(1799);
						}
						break;
					case RINDVIOR_PREDICATE_CANCELLATION:
						if (cha instanceof L1PcInstance && random.nextInt(100) + 1 > 33) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_npc.getLocation().getTileLineDistance(pc.getLocation()) > 4) {
								L1Location newLoc = null;
								for (int count = 0; count < 10; count++) {
									newLoc = _npc.getLocation().randomLocation(3, 4, false);
									if (_npc.glanceCheck(15, newLoc.getX(), newLoc.getY(), _target instanceof L1DoorInstance) == false) {
										pc.getTeleport().start(newLoc, 5, true);
										break;
									}
								}
							}
							L1SkillUse skill = new L1SkillUse(true);
							skill.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
							skill = null;
						}
						break;
					case RINDVIOR_CANCELLATION:
						if (cha instanceof L1PcInstance && random.nextInt(100) + 1 > 33) {
							L1PcInstance pc = (L1PcInstance) cha;
							L1SkillUse skill = new L1SkillUse(true);
							skill.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
							skill = null;
						}
						break;
					case RINDVIOR_WEAPON:
					case RINDVIOR_WEAPON_2:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							L1ItemInstance weapon = pc.getWeapon();
							int rnd = random.nextInt(100) + 1;
							if (weapon != null && rnd > 33) {
								int weaponDamage = random.nextInt(3) + 1;
								if(pc.isDead())continue;
								pc.sendPackets(new S_ServerMessage(268, weapon.getLogNameRef()), true);
								pc.getInventory().receiveDamage(weapon, weaponDamage);
								pc.send_effect(172);
							}
						}
						break;
					case PAP_DEATH_HEAL:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getSkill().hasSkillEffect(_skillId)) {
								pc.getSkill().removeSkillEffect(_skillId);
							}
							pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, 12), true);
							pc.getSkill().setSkillEffect(_skillId, 12000);
							pc.sendPackets(L1ServerMessage.sm4737);
						}
						break;
					// 흑장로 데스 힐 / 캔슬레이션
					case BLACKELDER_DEATH_HEAL:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.send_effect(7780);
							pc.getSkill().setSkillEffect(PAP_DEATH_HEAL, 12000);
							
							L1SkillUse skill = new L1SkillUse(true);
							skill.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
							skill = null;
						}
						break;
					// 흑장로 데스포션
					case BLACKELDER_DEATH_POTION:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.send_effect(7781);
							pc.getSkill().setSkillEffect(PAP_DEATH_POTION, 12000);
							pc.sendPackets(new S_SpellBuffNoti(pc, PAP_DEATH_POTION, true, 12), true);
						}
						break;
					// 드레이크 매스텔레포트
					case DRAKE_MASSTELEPORT:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead()) {
								continue;
							}
							L1Location newLocation = pc.getLocation().randomLocation(5, true);
							pc.getTeleport().start(newLocation, pc.getMoveState().getHeading(), true);
						}
						break;
					// 드레이크 윈드세클
					case DRAKE_WIND_SHACKLE:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead()) {
								continue;
							}
							pc.getSkill().setSkillEffect(WIND_SHACKLE, 12000);
							pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration), true);
							pc.send_effect(1799);
						}
						break;
                    // 이프리트 서먼 몬스터
					case EFRETE_SUMMON_MONSTER:
						for (int i = 0; i < 2; i++) {
							L1SpawnUtil.spawn(_npc, 5121, random.nextInt(3) + 8);
						}
						break;
					// 피닉스 서먼 몬스터
					case PHOENIX_SUMMON_MONSTER:
						for (int i = 0; i < 2; i++) {
							L1SpawnUtil.spawn(_npc, 900177, random.nextInt(3) + 8);
						}
						break;
					// 피닉스 캔슬레이션
					case PHOENIX_CANCELLATION:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							L1SkillUse skill = new L1SkillUse(true);
							skill.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
							skill = null;
						}
						break;
					
					case DESERT_SKILL1:	// 광역 커스 패럴라이즈
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol() || pc.getSkill().hasSkillEffect(CURSE_PARALYZE) || pc.getSkill().hasSkillEffect(STATUS_CURSE_PARALYZING) || pc.getSkill().hasSkillEffect(STATUS_CURSE_PARALYZED)) {
								continue;
							}
							L1CurseParalysis.curse(pc, 0, 4000);
							pc.send_effect(10704);
						}
						break;
					case DESERT_SKILL2:	// 광역 어스 바인드
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol() || pc.getSkill().hasSkillEffect(STATUS_CURSE_PARALYZING) || pc.getSkill().hasSkillEffect(STATUS_CURSE_PARALYZED)) {
								continue;
							}
							pc.getSkill().setSkillEffect(EARTH_BIND, 12000);
							pc.broadcastPacketWithMe(new S_Poison(pc.getId(), 2), true);
							pc.sendPackets(S_Paralysis.FREEZE_ON);
							pc.send_effect(2251);
						}
						break;
					case DESERT_SKILL3:	// 광역 마나 드레인
						if (cha instanceof L1PcInstance) {
							int ranMp = random.nextInt(20);
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getCurrentMp() <= ranMp || pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol()) {
								continue;
							}
							pc.setCurrentMp(pc.getCurrentMp() - ranMp);
							pc.send_effect(2172);
						}
						break;
					case DESERT_SKILL4:	// 광역 포이즌
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol()) {
								continue;
							}
							int PoisonTime = random.nextInt(5) + 1;
							if (PoisonTime > 2) {
								L1DamagePoison.doInfection(_user, pc, PoisonTime * 1000, 100, false);
							}
						}
						break;
					case DESERT_SKILL5:	// 광역 위크니스
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol() || pc.getSkill().hasSkillEffect(WEAKNESS)) {
								continue;
							}
							pc.getAbility().addShortDmgup(-5);
							pc.getAbility().addShortHitup(-1);
							pc.getSkill().setSkillEffect(WEAKNESS, 64000); // 위크니스
							pc.send_effect(2228);
						}
						break;
					case DESERT_SKILL6:	// 광역 다크니스
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol() || pc.getSkill().hasSkillEffect(DARKNESS)) {
								continue;
							}
							pc.sendPackets(pc.getSkill().hasSkillEffect(STATUS_FLOATING_EYE) ?  S_CurseBlind.BLIND_FLOATING_EYE : S_CurseBlind.BLIND_ON);
							pc.getSkill().setSkillEffect(DARKNESS, 32000); // 다크니스
							pc.send_effect(2175);
						}
						break;
					case DESERT_SKILL7:	// 광역 포그 오브 슬리핑
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.getSkill().hasSkillEffect(FOG_OF_SLEEPING)) {
								continue;
							}
							pc.getSkill().setSkillEffect(FOG_OF_SLEEPING, 4000); // 포그 오브 슬리핑
							pc.sendPackets(S_Paralysis.SLEEP_ON);
							pc.send_effect(760);
						}
						cha.setSleeped(true);
						break;
					case DESERT_SKILL8:	// 광역 디케이 포션
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol() || pc.getSkill().hasSkillEffect(DECAY_POTION)) {
								continue;
							}
							pc.getSkill().setSkillEffect(DECAY_POTION, 4000); // 디케이 포션
							pc.send_effect(2232);
						}
						break;
					case DESERT_SKILL9:	// 광역 디지즈
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol() || pc.getSkill().hasSkillEffect(DISEASE)) {
								continue;
							}
							pc.getAbility().addShortDmgup(-6);
							pc.getAC().addAc(12);
							pc.getSkill().setSkillEffect(DISEASE, 64000); // 디지즈
							pc.send_effect(2230);
						}
						break;
					case DESERT_SKILL10:	// 광역 커스/디지즈/다크니스/위크니스
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol()) {
								continue;
							}
							if (!(pc.getSkill().hasSkillEffect(CURSE_PARALYZE) || pc.getSkill().hasSkillEffect(STATUS_CURSE_PARALYZING) || pc.getSkill().hasSkillEffect(STATUS_CURSE_PARALYZED))) {
								L1CurseParalysis.curse(pc, 0, 4000);
								pc.send_effect(10704);
							}
							if (!pc.getSkill().hasSkillEffect(DISEASE)) {
								pc.getAbility().addShortDmgup(-6);
								pc.getAC().addAc(12);
								pc.getSkill().setSkillEffect(DISEASE, 64000); // 디지즈
								pc.send_effect(2230);
							}
							if (!pc.getSkill().hasSkillEffect(DARKNESS)) {
								pc.sendPackets(pc.getSkill().hasSkillEffect(STATUS_FLOATING_EYE) ?  S_CurseBlind.BLIND_FLOATING_EYE : S_CurseBlind.BLIND_ON);
								pc.getSkill().setSkillEffect(DARKNESS, 32000); // 다크니스
								pc.send_effect(2175);
							}
							if (!pc.getSkill().hasSkillEffect(WEAKNESS)) {
								pc.getAbility().addShortDmgup(-5);
								pc.getAbility().addShortHitup(-1);
								pc.getSkill().setSkillEffect(WEAKNESS, 64000); // 위크니스
								pc.send_effect(2228);
							}
						}
						break;
					case DESERT_SKILL11:	// 에르자베 토네이도 대미지
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.send_effect(10082);
						}
						break;
					case DESERT_SKILL12:	// 에르자베 서먼 몬스터
						for (int i = 0; i < 4; i++) {
							L1SpawnUtil.spawn(_npc, 5142, 6, 120000); // 여왕 수호 개미
							L1SpawnUtil.spawn(_npc, 5143, 6, 120000); // 여왕 수호 개미
							L1SpawnUtil.spawn(_npc, 5144, 6, 120000); // 여왕 수호 개미
							L1SpawnUtil.spawn(_npc, 5145, 6, 120000); // 여왕 수호 개미
						}
						break;
					case DESERT_SKILL13:	// 에르자베 모래 폭풍
						for (int i = 0; i < random.nextInt(3) + 1; i++) {
							L1SpawnUtil.spawn(_npc, 5095, 6, 3000); // 모래 폭풍
						}
						break;
					default:
						break;
					}
				}

				if (_skill.getType() == L1Skills.SKILL_TYPE.HEAL && _calcType == PC_NPC && undeadType == L1Undead.UNDEAD) {
					dmg *= -1;
				}
				if (_skill.getType() == L1Skills.SKILL_TYPE.HEAL && _calcType == PC_NPC && undeadType == L1Undead.UNDEAD_BOSS) {
					dmg = 0;
				}
				if ((cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) && dmg < 0) {
					dmg = 0;
				}
				if (dmg != 0 || drainMana != 0) {
					sendSkillDamage(cha, magic, dmg, drainMana);
				}
				if (heal > 0) {
					_user.setCurrentHp(heal + _user.getCurrentHp() > _user.getMaxHp() ? _user.getMaxHp() : heal + _user.getCurrentHp());
				}

				addMagicList(cha, false);// 스킬 시간 설정 최초 진행
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getLight().turnOnOffLight();
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					pc.sendPackets(new S_OwnCharStatus(pc), true);
					pc.sendPackets(new S_SPMR(pc), true);
					sendHappenMessage(pc);
				}
			}// target for문 end
			
			if (not_alignment_penalty) {
				_player.getConfig().setNotAlignmentPenalty(false);
			}
			
			if (_skillId == DETECTION || _skillId == IZE_BREAK || _skillId == EYE_OF_DRAGON || _skillId == COUNTER_DETECTION) {// 인비지 제거
				detection(_player, true);
			}
		} catch (Exception e) {
			System.out.println("Error occurred: " + (_player != null ? _player.getAccountName() : StringUtil.EmptyString) + " | " + (_npc != null ? _npc.getName() : StringUtil.EmptyString) + " | " + (_target != null ? _target.getName() : StringUtil.EmptyString));
			e.printStackTrace();
		}
	}
	
	/**
	 * 스킬 대미지
	 * @param cha
	 * @param magic
	 * @param dmg
	 * @param drainMana
	 */
	void sendSkillDamage(L1Character cha, L1Magic magic, int dmg, int drainMana){
		if (_skillId == TRIPLE_ARROW || _skillId == FOU_SLAYER) {
			return;
		}
		if ((_skillId == FORCE_STUN || _skillId == AVENGER || _skillId == PANTERA || _skillId == BLADE || _skillId == HELLFIRE || _skillId == PHANTOM || _skillId == TYRANT) && dmg > 0) {
			L1ItemInstance weapon = _player.getWeapon();
			if (weapon != null) {
				int plusDmg = random.nextInt(weapon.getItem().getDmgSmall()) + weapon.getItem().getDmgRate() + weapon.getEnchantLevel() + _player.getAbility().getTotalStr();
				if (_skillId == BLADE || _skillId == TYRANT) {
					dmg += plusDmg << 2;// 4배
				} else if (_skillId == FORCE_STUN || _skillId == AVENGER) {
					dmg += plusDmg + (plusDmg << 1);// 3배
				} else if (_skillId == PANTERA || _skillId == PHANTOM) {
					dmg += plusDmg << 1;// 2배
				} else {
					dmg += plusDmg;
				}
				if (_skillId == PANTERA && _user.isPassiveStatus(L1PassiveId.PANTERA_SHOCK)) {
					dmg += dmg >> 1;// 판테라 쇼크 대미지 증가 1.5배
				} else if (_skillId == TYRANT && _user.isPassiveStatus(L1PassiveId.TYRANT_EXCUTION)) {
					dmg += dmg >> 1;// 타이런트 엑스큐션 대미지 증가 1.5배
				}
			}
		} else if (_skillId == ENSNARE && dmg > 0) {
			L1ItemInstance weapon = _player.getWeapon();
			if (weapon != null) {
				int plusDmg = random.nextInt(weapon.getItem().getDmgSmall()) + weapon.getItem().getDmgRate() + weapon.getEnchantLevel() + _player.getAbility().getTotalInt();
				dmg += plusDmg + (plusDmg << 1);// 3배
			}
		}
		
		if ((_skillId == DISINTEGRATE || _skillId == ETERNITY) && dmg > 0) {
			if (Config.SPELL.DIS_LOCK_DMG > 0 && dmg > Config.SPELL.DIS_LOCK_DMG) {
				dmg = Config.SPELL.DIS_LOCK_DMG;
			}
			if (_skillId == DISINTEGRATE) {// 디스 중첩불가
				if (cha.getSkill().hasSkillEffect(NO_DIS)) {
					dmg = 0;
				} else {
					cha.getSkill().setSkillEffect(NO_DIS, 2000);
				}
			}
		}
		magic.commit(dmg, drainMana);// 대미지 전달
	}
	
	/**
	 * 무작위 텔레포트
	 */
	void randomTeleport() {
		L1Location newLocation = _player.getLocation().randomLocation(200, true);
		int newX	= newLocation.getX();
		int newY	= newLocation.getY();
		short mapId	= (short) newLocation.getMapId();
		if (_skillId == MASS_TELEPORT) {
			L1Map map = L1WorldMap.getInstance().getMap(mapId);
			for (L1PcInstance member : L1World.getInstance().getVisibleClanPlayer(_player, 3)) {
				if (member.getId() != _player.getId() && !member.isPrivateShop() && !member.isAutoClanjoin()) {
					int newX2 = newX + random.nextInt(3) + 1;
					int newY2 = newY + random.nextInt(3) + 1;
					if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2)) {
						member.getTeleport().start(newX2, newY2, mapId, member.getMoveState().getHeading(), true);
					} else {
						member.getTeleport().start(newX, newY, mapId, member.getMoveState().getHeading(), true);
					}
				}
			}
		}
		_player.getTeleport().start(newX, newY, mapId, _player.getMoveState().getHeading(), true);
	}
	
	/**
	 * 기억 텔레포트
	 */
	void bookmarkTeleport() {
		L1Map map = L1WorldMap.getInstance().getMap(_bookmark_mapid);
		if (_skillId == MASS_TELEPORT) {
			for (L1PcInstance member : L1World.getInstance().getVisibleClanPlayer(_player, 3)) {
				if (!member.getConfig()._massTeleportState) {
					continue;
				}
				if (member.getId() == _player.getId() || member.isPrivateShop() || member.isAutoClanjoin()) {
					continue;
				}
				int newX2 = _bookmark_x + random.nextInt(3) + 1;
				int newY2 = _bookmark_y + random.nextInt(3) + 1;
				if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2)) {
					member.getTeleport().start(newX2, newY2, _bookmark_mapid, member.getMoveState().getHeading(), true);
				} else {
					member.getTeleport().start(_bookmark_x, _bookmark_y, _bookmark_mapid, member.getMoveState().getHeading(), true);
				}
			}
		}
		if (_player.getInventory().checkEquippedOne(L1ItemId.TELEPORT_RING_ITEMS)) {// 순간이동 반지
			_player.getTeleport().start(_bookmark_x, _bookmark_y, _bookmark_mapid, _player.getMoveState().getHeading(), true);
		} else {
			// 요정 전용 순간이동
			boolean isFail = random.nextBoolean();
			int newX2 = 0, newY2 = 0;
			if (isFail) {
				newX2 = _player.getX();
				newY2 = _player.getY();
				_bookmark_mapid = _player.getMapId();
			} else {
				newX2 = _bookmark_x - 6 + random.nextInt(12);
				newY2 = _bookmark_y - 6 + random.nextInt(12);
			}
			if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2)) {
				_player.getTeleport().start(newX2, newY2, _bookmark_mapid, _player.getMoveState().getHeading(), true);
			} else {
				_player.getTeleport().start(_bookmark_x, _bookmark_y, _bookmark_mapid, _player.getMoveState().getHeading(), true);
			}
		}
	}
	
	/**
	 * 이레이즈 매직 제거
	 * @param cha
	 */
	void deleteEraseMagic(L1Character cha){
		cha.getSkill().killSkillEffectTimer(ERASE_MAGIC);
		cha.getSkill().killSkillEffectTimer(MOB_ERASE_MAGIC);
		if (cha instanceof L1PcInstance) {
			((L1PcInstance) cha).sendPackets(S_PacketBox.ICON_AURA_OFF);
		}
	}
	
	/**
	 * 아우라키아 스킬제한
	 */
	void aurakiaResult(){
		if (_user.isPassiveStatus(L1PassiveId.AURAKIA) || _player.getScalesDragon() < 2) {
			return;
		}
		switch (_skillId) {
		case SCALES_EARTH_DRAGON:
			if (status.hasSkillEffect(SCALES_WATER_DRAGON) && status.hasSkillEffect(SCALES_FIRE_DRAGON)) {
				aurakiaBeforeRemove(status, SCALES_WATER_DRAGON, SCALES_FIRE_DRAGON);
		    } else if (status.hasSkillEffect(SCALES_WATER_DRAGON) && status.hasSkillEffect(SCALES_WIND_DRAGON)) {
		    	aurakiaBeforeRemove(status, SCALES_WATER_DRAGON, SCALES_WIND_DRAGON);
		    } else if (status.hasSkillEffect(SCALES_FIRE_DRAGON) && status.hasSkillEffect(SCALES_WIND_DRAGON)) {
		    	aurakiaBeforeRemove(status, SCALES_FIRE_DRAGON, SCALES_WIND_DRAGON);
		    }
			break;
		case SCALES_WATER_DRAGON:
			if (status.hasSkillEffect(SCALES_EARTH_DRAGON) && status.hasSkillEffect(SCALES_FIRE_DRAGON)) {
				aurakiaBeforeRemove(status, SCALES_EARTH_DRAGON, SCALES_FIRE_DRAGON);
		    } else if (status.hasSkillEffect(SCALES_EARTH_DRAGON) && status.hasSkillEffect(SCALES_WIND_DRAGON)) {
		    	aurakiaBeforeRemove(status, SCALES_EARTH_DRAGON, SCALES_WIND_DRAGON);
		    } else if (status.hasSkillEffect(SCALES_FIRE_DRAGON) && status.hasSkillEffect(SCALES_WIND_DRAGON)) {
		    	aurakiaBeforeRemove(status, SCALES_FIRE_DRAGON, SCALES_WIND_DRAGON);
		    }
			break;
		case SCALES_FIRE_DRAGON:
			if (status.hasSkillEffect(SCALES_EARTH_DRAGON) && status.hasSkillEffect(SCALES_WATER_DRAGON)) {
				aurakiaBeforeRemove(status, SCALES_EARTH_DRAGON, SCALES_WATER_DRAGON);
		    } else if (status.hasSkillEffect(SCALES_EARTH_DRAGON) && status.hasSkillEffect(SCALES_WIND_DRAGON)) {
		    	aurakiaBeforeRemove(status, SCALES_EARTH_DRAGON, SCALES_WIND_DRAGON);
		    } else if (status.hasSkillEffect(SCALES_WATER_DRAGON) && status.hasSkillEffect(SCALES_WIND_DRAGON)) {
		    	aurakiaBeforeRemove(status, SCALES_WATER_DRAGON, SCALES_WIND_DRAGON);
		    }
			break;
		case SCALES_WIND_DRAGON:
			if (status.hasSkillEffect(SCALES_EARTH_DRAGON) && status.hasSkillEffect(SCALES_WATER_DRAGON)) {
				aurakiaBeforeRemove(status, SCALES_EARTH_DRAGON, SCALES_WATER_DRAGON);
		    } else if (status.hasSkillEffect(SCALES_EARTH_DRAGON) && status.hasSkillEffect(SCALES_FIRE_DRAGON)) {
		    	aurakiaBeforeRemove(status, SCALES_EARTH_DRAGON, SCALES_FIRE_DRAGON);
		    } else if (status.hasSkillEffect(SCALES_WATER_DRAGON) && status.hasSkillEffect(SCALES_FIRE_DRAGON)) {
		    	aurakiaBeforeRemove(status, SCALES_WATER_DRAGON, SCALES_FIRE_DRAGON);
		    }
			break;
		default:
			break;
		}
	}
	
	void aurakiaBeforeRemove(L1SkillStatus status, int first, int second) {
		int firstTimeSec	= status.getSkillEffectTimeSec(first);
		int secondTimeSec	= status.getSkillEffectTimeSec(second);
		boolean falg		= firstTimeSec > secondTimeSec;
		status.removeSkillEffect(falg ? second : first);
		_player.sendPackets(new S_SpellBuffNoti(_player, falg ? second : first, false, -1), true);
	}
	
	void bringStone() {
		L1ItemInstance item = _player.getInventory().getItem(_itemObjId);
		if (item != null) {
			int dark = (int) (10 + (_player.getLevel() * 0.8) + (_player.getAbility().getTotalWis() - 6) * 1.2);
			int brave = (int) (dark / 2.1);
			int wise = (int) (brave >> 1);
			int kayser = (int) (wise / 1.9);
			int chance = random.nextInt(100) + 1;
			if (item.getItem().getItemId() == 40320) {
				_player.getInventory().removeItem(item, 1);
				if (dark >= chance) {
					_player.getInventory().storeItem(L1ItemId.BLACK_ATTR_STONE, 1);
					_player.sendPackets(new S_ServerMessage(403, "$2475"), true);
				} else {
					_player.sendPackets(L1ServerMessage.sm280);
				}
			} else if (item.getItem().getItemId() == L1ItemId.BLACK_ATTR_STONE) {
				_player.getInventory().removeItem(item, 1);
				if (brave >= chance) {
					_player.getInventory().storeItem(40322, 1);
					_player.sendPackets(new S_ServerMessage(403, "$2476"), true);
				} else {
					_player.sendPackets(L1ServerMessage.sm280);
				}
			} else if (item.getItem().getItemId() == 40322) {
				_player.getInventory().removeItem(item, 1);
				if (wise >= chance) {
					_player.getInventory().storeItem(40323, 1);
					_player.sendPackets(new S_ServerMessage(403, "$2477"), true);
				} else {
					_player.sendPackets(L1ServerMessage.sm280);
				}
			} else if (item.getItem().getItemId() == 40323) {
				_player.getInventory().removeItem(item, 1);
				if (kayser >= chance) {
					_player.getInventory().storeItem(40324, 1);
					_player.sendPackets(new S_ServerMessage(403, "$2478"), true);
				} else {
					_player.sendPackets(L1ServerMessage.sm280);
				}
			}
		}
	}
	
	/**
	 * 서먼 몬스터 스폰
	 * @param pc
	 * @param number
	 * @param order
	 */
	void summonMonster(L1PcInstance pc, int number, int order) {
		// 1:로드 버기, 2:워 울피, 3:로드 란테스, 4:칸 로그너, 5:워 데이커, 6:킹 클로버, 7:메이든, 8:쿠거, 9:사피엔, 
		int summonid = 0, checkLevel = 0;
		switch (number) {
		case 1:// 로드 버기
			checkLevel = 86;
			summonid = 810838;
			break;
		case 2:// 워 울피
			checkLevel = 86;
			summonid = 810835;
			break;
		case 3:// 로그 란테스
			checkLevel = 86;
			summonid = 810843;
			break;
		case 4:// 칸 로그너
			checkLevel = 88;
			summonid = 810844;
			break;
		case 5:// 워 데이커
			checkLevel = 88;
			summonid = 810841;
			break;
		case 6:// 킹 클로버
			checkLevel = 88;
			summonid = 810839;
			break;
		case 7:// 메이든
			checkLevel = 90;
			summonid = 810853;
			break;
		case 8:// 쿠거
			checkLevel = 90;
			summonid = 810854;
			break;
		case 9:// 사피엔
			checkLevel = 90;
			summonid = 810855;
			break;
		default:return;
		}
		if (pc.getLevel() < checkLevel) {
			pc.sendPackets(L1ServerMessage.sm743);
			return;
		}
		new L1SummonInstance(NpcTable.getInstance().getTemplate(summonid), pc, 1);
	}

	/**
	 * 디텍션
	 * @param pc
	 * @param detectAll
	 */
	public static void detection(L1PcInstance pc, boolean detectAll) {
		if (pc == null) {
			return;
		}
		if (!pc.isGmInvis() && pc.isInvisble() && !pc.isGhost()) {
			unequipInvisItem(pc);
			pc.delInvis();
			pc.beginInvisTimer();
		}

		if (detectAll) {
			for (L1PcInstance each : L1World.getInstance().getVisiblePlayer(pc)) {
				if (!each.isGmInvis() && each.isInvisble() && !pc.isGhost()) {
					if (each.getSkill().isBlindHidingAssassin() && each.getSkill().getSkillEffectTimeSec(BLIND_HIDING) >= 13) {// 16초 - 3초
						continue;
					}
					unequipInvisItem(each);
					each.delInvis();
				}
			}
			L1WorldTraps.onDetection(pc);
		}
	}

	/**
	 * 아이템 착용해제
	 * @param pc
	 */
	public static void unequipInvisItem(L1PcInstance pc) {
		if (pc == null) {
			return;
		}
		L1ItemInstance cloak	= pc.getInventory().getEquippedCloak();// 착용한 망토
		if (cloak == null) {
			return;
		}
		if (L1ItemId.isInvisItem(cloak.getItemId())) {
			pc.getInventory().setEquipped(cloak, false);// 착용 해제
		}
	}

	boolean isTargetCalc(L1Character cha) {
		if (cha != null && cha instanceof L1PcInstance && (((L1PcInstance)cha).getTeleport().isTeleport())) {
			return false;
		}
		if (_user.glanceCheck(15, cha.getX(), cha.getY(), cha instanceof L1DoorInstance) == false && _skill.getIsThrough() == false && (!(_skill.getType() == L1Skills.SKILL_TYPE.CHANGE || _skill.getType() == L1Skills.SKILL_TYPE.RESTORE))) {
			return false;
		}
		if (_isTargetAttack && _skillId != TURN_UNDEAD && isPcSummonPet(cha) && (_player.getRegion() == L1RegionStatus.SAFETY || cha.getRegion() == L1RegionStatus.SAFETY || _player.checkNonPvP(_player, cha))) {
			return false;
		}
		if (_skillId == FOG_OF_SLEEPING && _user.getId() == cha.getId()) {
			return false;
		}
		if (_skillId == MASS_TELEPORT && _user.getId() != cha.getId()) {
			return false;
		}
		return true;
	}

	boolean isPcSummonPet(L1Character cha) {
		if (_calcType == PC_PC) {
			return true;
		}
		if (_calcType == PC_NPC) {
			if (cha instanceof L1SummonInstance && ((L1SummonInstance) cha).isExsistMaster()) {
				return true;
			}
			if (cha instanceof L1PetInstance) {
				return true;
			}
		}
		return false;
	}

	boolean isUseCounterMagic(L1Character cha) {
		if (_isCounterMagic && cha.getSkill().hasSkillEffect(COUNTER_MAGIC)) {
			cha.getSkill().removeSkillEffect(COUNTER_MAGIC);
			S_Effect effect = new S_Effect(cha.getId(), 10702);
			if (cha instanceof L1PcInstance) {
				((L1PcInstance) cha).sendPackets(effect, false);
			}
			cha.broadcastPacket(effect, true);
			return true;
		}
		return false;
	}

	/**
	 * 스킬을 적용하지 못하는 타겟인지 조사한다.
	 * @param target
	 * @return boolean
	 */
	boolean isTargetFailure(L1Character target) {
		if (target instanceof L1TowerInstance || target instanceof L1DoorInstance || target instanceof L1DollInstance) {
			return true;
		}
		if (target instanceof L1PcInstance) {
			if (_calcType == PC_PC && _player.checkNonPvP(_player, target)) {
				L1PcInstance pc = (L1PcInstance) target;
				if (_player.getId() == pc.getId() || (pc.getClanid() != 0 && _player.getClanid() == pc.getClanid())) {
					return false;
				}
				return true;
			}
			return false;
		}
		boolean isTU		= false;
		boolean isBoss		= false;
		boolean isManaDrain	= false;
		if (target instanceof L1MonsterInstance) {
			L1MonsterInstance mon = (L1MonsterInstance) target;
			isTU			= mon.getNpcTemplate().isTurnUndead();
			isBoss			= mon.getNpcTemplate().isBossMonster();
			isManaDrain		= true;
		}
		if ((_skillId == TURN_UNDEAD && isTU == false)
				|| (BOSS_NOT_ENABLE_SKILL_LIST.contains(_skillId) && isBoss)
				|| (_skillId == MANA_DRAIN && isManaDrain == false)) {
			return true;
		}
		return false;
	}

}

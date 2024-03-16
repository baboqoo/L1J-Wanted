package l1j.server.server.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.common.data.ChatType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1MobSkill;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.StringUtil;

public class L1MobSkillUse {
	private static Logger _log				= Logger.getLogger(L1MobSkillUse.class.getName());
	private static final Random random		= new Random(System.nanoTime());
	
	private L1MobSkill _mobSkillTemplate;
	private L1NpcInstance _attacker;
	private L1Character _target;
	private int _sleepTime;
	private int _skillUseCount[];
	private boolean _isActionDelay;
	private boolean _isMentDelay;
	
	public L1MobSkillUse(L1NpcInstance npc) {
		try {
			if (npc == null) {
				return;
			}
			_attacker				= npc;
			_mobSkillTemplate		= MobSkillTable.getTemplate(npc.getNpcTemplate().getNpcId());
			if (_mobSkillTemplate == null) {
				return;
			}
			_skillUseCount			= new int[_mobSkillTemplate.getSkillSize()];
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private boolean isMsgSkill(int index){
		/*if(_attacker.getNpcId() == 81163 && (index == 5 || index == 6 || index == 7)){// 기르타스 음성
			return true;
		}*/
		return !StringUtil.isNullOrEmpty(_mobSkillTemplate.getMsg(index));
	}
	
	private void sendMsg(int index) {
		/*if(_attacker.getNpcId() == 81163){// 기르타스 음성
			sendSound(index);
		}*/
		String msg = _mobSkillTemplate.getMsg(index);
		if (StringUtil.isNullOrEmpty(msg)) {
			return;
		}
		_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, msg, ChatType.CHAT_NORMAL), true);
	}
	
	/*private void sendSound(int index){
		switch(index){
		case 5:_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "voice_kirtas_8", L1ChatConstruct.SOUND), true);break;// 영혼만 남겨놓고 모두 소멸하리라
		case 6:_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "voice_kirtas_10", L1ChatConstruct.SOUND), true);break;// 모두 녹아버려라
		case 7:_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "voice_kirtas_10", L1ChatConstruct.SOUND), true);break;// 귀찮은놈들 사라져라
		default:break;
		}
	}*/

	private int getSkillUseCount(int idx) {
		return _skillUseCount[idx];
	}

	private void skillUseCountUp(int idx) {
		_skillUseCount[idx]++;
	}

	public void resetAllSkillUseCount() {
		if (_mobSkillTemplate == null) {
			return;
		}
		for (int i = 0; i < _mobSkillTemplate.getSkillSize(); i++) {
			_skillUseCount[i] = 0;
		}
	}

	public int getSleepTime() {
		return _sleepTime;
	}

	public void setSleepTime(int i) {
		_sleepTime = i;
	}
	
	public boolean isActionDelay(){
		return _isActionDelay;
	}

	public L1MobSkill getMobSkillTemplate() {
		return _mobSkillTemplate;
	}
	
	private boolean isSuccess(int index){
		skillUseCountUp(index);
		sendMsg(index);
		return true;
	}

	public boolean skillUse(L1Character target) {
		try {
			if (target == null || _mobSkillTemplate == null || _attacker.isInvisble()) {
				return false;
			}
			L1MobSkill.TYPE type = _mobSkillTemplate.getType(0);
			if (type == L1MobSkill.TYPE.NONE) {
				return false;
			}
			_target = target;
			int i = 0;
			for (i = 0; i < _mobSkillTemplate.getSkillSize() && _mobSkillTemplate.getType(i) != L1MobSkill.TYPE.NONE; i++) {
				L1MobSkill.CHANGE_TARGET changeType = _mobSkillTemplate.getChangeTarget(i);
				_target = changeType != null && changeType != L1MobSkill.CHANGE_TARGET.NO ? changeTarget(changeType, i) : target;
				if (!isSkillUseble(i)) {
					continue;
				}
				
				switch(_mobSkillTemplate.getType(i)){
				case ATTACK:// 물리 공격
					if (isAttack(i)) {
						return isSuccess(i);
					}
					continue;
				case SPELL:// 마법 스킬
					if (isMsgSkill(i)) {// 멘트 스킬
						if (_isMentDelay == true) {
							return false;// 멘트 딜레이때 다른 스킬 사용못하도록
						}
						sendMsg(i);// 멘트 출력
						_isMentDelay = true;
						GeneralThreadPool.getInstance().schedule(new delayUseSkill(i), 3000L);// 딜레이 3초준다
						return false;
					} else {
						if (isSpell(i, false)) {
							skillUseCountUp(i);
							return true;
						}
					}
					continue;
				case SUMMON:// 몬스터 소환
					if (isSummon(i)) {
						return isSuccess(i);
					}
					continue;
				case POLY:// 강제 변신
					if (isPoly(i)) {
						return isSuccess(i);
					}
					continue;
				case LINE_ATTACK://  라인 어택
					if (isLineAttack(i)) {
						return isSuccess(i);
					}
					continue;
				case KIRTAS_METEOR:// 기르타스 미티어
					if (isKirtasMeteor(i)) {
						return isSuccess(i);
					}
					continue;
				case KIRTAS_BARRIER:// 기르타스 배리어
					if (isKirtasBarrier(i)) {
						return isSuccess(i);
					}
					continue;
				case TITANGOLEM_BARRIER:// 타이탄골렘 배리어
					if (isTitanGolemBarrier(i)) {
						return isSuccess(i);
					}
					continue;
				case VALLACAS_FLY:// 발라카스 날기
					if (isVallakasFly(i)) {
						return isSuccess(i);
					}
					continue;
				case VALLACAS_BRESS:// 발라카스 브레스
					if (isVallakasBress(i)) {
						return isSuccess(i);
					}
					continue;
				default:
					continue;
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return false;
	}

	private boolean isSummon(int idx) {
		int summonId = _mobSkillTemplate.getSummon(idx);
		int min = _mobSkillTemplate.getSummonMin(idx);
		int max = _mobSkillTemplate.getSummonMax(idx);
		if (summonId == 0 || min <= 0) {
			return false;
		}
		int count = random.nextInt(max) + min;
		mobspawn(summonId, count);
		//_attacker.broadcastPacket(new S_SkillSound(_attacker.getId(), 761), true);// 법진이펙트
		S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillBuff);
		_attacker.broadcastPacket(gfx, true);
		_sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
		return true;
	}

	private boolean isPoly(int idx) {
		int polyId = _mobSkillTemplate.getPolyId(idx);
		boolean usePoly = false;
		if (polyId == 0) {
			return false;
		}
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)) {
			if (pc == null || pc.isDead() || pc.isGhost() || pc.isGmInvis() || pc._isArmorSetPoly || !_attacker.glanceCheck(20, pc.getX(), pc.getY(), false)) {
				continue;
			}
			int npcId = _attacker.getNpcTemplate().getNpcId();
			switch (npcId) {
			case 81082: pc.getInventory().takeoffEquip(945); break;
			default: break;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION) || pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {// 변신 지배 반지 방어 이팩트
				pc.send_effect(15846);
			} else {
				L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC);
			}
			usePoly = true;
		}
		if (usePoly) {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)) {
				if (pc == null) {
					continue;
				}
				pc.send_effect(230);
				break;
			}
			S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillBuff);
			_attacker.broadcastPacket(gfx, true);
			_sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
		}
		return usePoly;
	}

	private boolean isSpell(int idx, boolean mentDelay) {
		if (_attacker.getSkill().hasSkillEffect(L1SkillId.SILENCE) || _target == null || _target.isDead()) {
			return false;
		}
		int skillid = _mobSkillTemplate.getSkillId(idx);
		if (skillid == -1) {
			return false;
		}
		int range = _mobSkillTemplate.getRange(idx);
		if (!_attacker.glanceCheck(range, _target.getX(), _target.getY(), _target instanceof L1DoorInstance)
				|| !_target.glanceCheck(range, _attacker.getX(), _attacker.getY(), _attacker instanceof L1DoorInstance)) {
			return false;
		}
		L1SkillUse skillUse = new L1SkillUse(true);
		boolean canUseSkill = false;	
		int npcId = _attacker.getNpcTemplate().getNpcId();
		switch(npcId){
		case 5100:// 린드비오르
			if (skillid == 7001) {// 윈드 셰클
				if (_target instanceof L1PcInstance) {
					((L1PcInstance) _target).sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 6), true);
				}
			} else if (skillid == 7013) {// 사일런스
				if (_target instanceof L1PcInstance) {
					((L1PcInstance) _target).sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 8), true);
				}
			}
			break;
		case 7311162:// 발라카스
			if (skillid == 75075) {// 법진소환
				GeneralThreadPool.getInstance().execute(new StarAreaEffectSpawn(_attacker));
			}
		    break;
		case 707025:// 오우거 킹
			if (skillid == 12060 && random.nextInt(10) + 1 <= 2) {// 도끼 던지기
				skillUse.handleCommands(null, L1SkillId.MOB_SHOCKSTUN_19, _target.getId(), _target.getX(), _target.getY(), 0, L1SkillUseType.NORMAL, _attacker);
			}
		    break;
		case 800157:// 사신 그림 리퍼
		case 30188:// 사신 그림 리퍼
			if (skillid == 71041) {// 사선 불덩이
				GeneralThreadPool.getInstance().schedule(new fireMeteorRangeStun(_attacker), 3500L);
			} else if (skillid == 75075) {// 법진 소환
				GeneralThreadPool.getInstance().execute(new StarAreaEffectSpawn(_attacker));
			}
			break;	
		case 45753:// 발록
			if (skillid == 150011) {// 전체 미티어
				GeneralThreadPool.getInstance().schedule(new fireMeteorRangeStun(_attacker), 3500L);
			}
			break;
		case 10503:// 다크스타 조우
			if (skillid == 71041) {// 사선 불덩이
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
							return;
						}
						for (int i = 0; i < 4; i++) {
						    L1SpawnUtil.spawn2(_attacker.getX(), _attacker.getY(), (short) _attacker.getMapId(), 0, 100586, 8, (random.nextInt(2) * 1000) + 13000, 0);
						}
					}
				}, 2000);
			}
			break;
		case 800553:// 기란 성주 듀세라
			if (skillid == 180033) {
				GeneralThreadPool.getInstance().schedule(new fireMeteorRangeStun(_attacker), 3500L);
			}
			break;
		case 7800104:// 악어섬의 비밀 베레스
			if (skillid == 180062) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(_attacker, 6)) {
					if (obj instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) obj;
						if (pc.isGhost() || pc.isDead() || pc.isAbsol() || pc.isBind() || pc.isDesperado() || pc.isOsiris()) {
							continue;
						}
						if (random.nextInt(10) + 1 < 8) {
						    int Duration = CommonUtil.randomIntChoice(L1SkillInfo.MOB_DESPERADO_ARRAY_1);
						    L1EffectSpawn.getInstance().spawnEffect(9416, Duration, pc.getX(), pc.getY(), pc.getMapId());
						    pc.getSkill().setSkillEffect(L1SkillId.DESPERADO, Duration);
						    pc.sendPackets(S_Paralysis.DESPERADO_ON);
						}
					}
				}
			}
			break;
		case 7800306:// 어둠 감시관
			if (skillid == L1SkillId.DARK_WATCH_METEOR) {
				GeneralThreadPool.getInstance().execute(new DarkWatchMeteor());
			}
			break;
		case 5182:// 인터서버 에르자베
			if (skillid == 7071) {
				rangeStun();
			} else if (skillid == 7073) {
				for (int i = 0; i < 6; i++) {
					L1SpawnUtil.spawn(_attacker, 45952, 6, 120000);// 수호 개미
					if (idx >= 7) {
						L1SpawnUtil.spawn(_attacker, 5183, 6, 120000);// 병정 개미
						L1SpawnUtil.spawn(_attacker, 5184, 6, 120000);// 흰 개미
						L1SpawnUtil.spawn(_attacker, 45946, 6, 120000);// 흰 개미 무리
					}
				}
			}
			break;
		case 900519:// 할파스
			halpasSkill(skillid);
			break;
		default:
			break;
		}
		if (skillid != -1) {
			canUseSkill = skillUse.checkUseSkill(null, skillid, _target.getId(), _target.getX(), _target.getY(), 0, L1SkillUseType.NORMAL, _attacker);
		}
		if (canUseSkill == true) {
			if (_mobSkillTemplate.getLeverage(idx) > 0) {
				skillUse.setLeverage(_mobSkillTemplate.getLeverage(idx));
			}
			skillUse.handleCommands(null, skillid, _target.getId(), _target.getX(), _target.getY(), 0, L1SkillUseType.NORMAL, _attacker);
			L1Skills skill = SkillsTable.getTemplate(skillid);
			_sleepTime = skill.getTarget() == L1Skills.SKILL_TARGET.ATTACK && skillid != L1SkillId.TURN_UNDEAD ? _attacker.getNpcTemplate().getAtkMagicSpeed() : _attacker.getNpcTemplate().getSubMagicSpeed();
			if (skill.getActionId() > 0) {
				int time = _attacker.getSprite().getActionSpeed(skill.getActionId());
				if (time > 0) {
					_sleepTime = time + 500;
				}
				if (mentDelay) {
					_isActionDelay = true;
					_sleepTime += 1000;
				}
			}
			skillUse = null;
			return true;
		}
		skillUse = null;
		return false;
	}

	private boolean isAttack(int idx) {
		int range = _mobSkillTemplate.getRange(idx);
		if (!_attacker.glanceCheck(range, _target.getX(), _target.getY(), _target instanceof L1DoorInstance)
				|| !_target.glanceCheck(range, _attacker.getX(), _attacker.getY(), _attacker instanceof L1DoorInstance)) {
			return false;
		}
		int areaWidth	= _mobSkillTemplate.getAreaWidth(idx);
		int areaHeight	= _mobSkillTemplate.getAreaHeight(idx);
		int actId		= _mobSkillTemplate.getActid(idx);
		int gfxId		= _mobSkillTemplate.getGfxid(idx);
		ArrayList<L1Character> targetList = new ArrayList<L1Character>();
		_attacker.getMoveState().setHeading(_attacker.targetDirection(_target.getX(), _target.getY()));
		if (areaHeight > 0) {
			L1Character cha = null;
			for (L1Object obj : L1World.getInstance().getVisibleBoxObjects(_attacker, _attacker.getMoveState().getHeading(), areaWidth, areaHeight)) {
				if (obj == null || !(obj instanceof L1Character)) {
					continue;
				}
				cha = (L1Character) obj;
				if (cha.isDead()) {
					continue;
				}
				if (cha instanceof L1PcInstance) {
					if (((L1PcInstance) cha).isGhost()) {
						continue;
					}
					if (_attacker instanceof L1SummonInstance || _attacker instanceof L1PetInstance) {
						if (cha.getId() == _attacker.getMaster().getId() || cha.getRegion() == L1RegionStatus.SAFETY) {
							continue;
						}
					}
				}
				if (!_attacker.glanceCheck(areaWidth, cha.getX(), cha.getY(), cha instanceof L1DoorInstance)) {
					continue;
				}
				if (_target instanceof L1PcInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance) {
					if (cha instanceof L1PcInstance && !((L1PcInstance) cha).isGhost()
							&& !((L1PcInstance) cha).isGmInvis() || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
						targetList.add(cha);
					}
				} else {
					if (cha instanceof L1MonsterInstance) {
						targetList.add(cha);
					}
				}
			}
		} else {
			targetList.add(_target);
		}
		if (targetList.isEmpty()) {
			return false;
		}
		L1Attack attack = null;
		for (L1Character target : targetList) {
			if (target == null) {
				continue;
			}
			attack = new L1Attack(_attacker, target);
			if (attack.calcHit()) {
				if (_mobSkillTemplate.getLeverage(idx) > 0) {
					attack.setLeverage(_mobSkillTemplate.getLeverage(idx));
				}
				attack.calcDamage();
			}
			if (actId > 0) {
				attack.setActId(actId);
			}
			if (target.getId() == _target.getId()) {
				if (gfxId > 0) {
					_attacker.broadcastPacket(new S_Effect(_attacker.getId(), gfxId), true);
				}
				attack.action();
			}
			attack.commit();
		}
		_sleepTime = _attacker.getAtkspeed();
		if (actId > 0) {
			int time = _attacker.getSprite().getActionSpeed(actId);
			if (time > 0) {
				_sleepTime = time + 200;
			}
		}
		attack = null;
		targetList.clear();
		targetList = null;
		return true;
	}

	private boolean isSkillUseble(int idx) {
		int prob = _mobSkillTemplate.getProb(idx);
		if (_attacker instanceof L1PetInstance) {
			prob += ((L1PetInstance)_attacker).ability.getMagicHitup();
		}
	    if (random.nextInt(100) + 1 >= prob) {
	    	return false;
	    }
	    int limitCount = _mobSkillTemplate.getLimitCount(idx);
		if (limitCount > 0 && getSkillUseCount(idx) >= limitCount) {
			return false;
		}
	    int enableHp = _mobSkillTemplate.getEnableHp(idx);
		if (enableHp > 0 && _attacker.getCurrentHpPercent() > enableHp) {
			return false;
		}
		int enableCompanionHp = _mobSkillTemplate.getEnableCompanionHp(idx);
		if (enableCompanionHp > 0) {
			L1NpcInstance companionNpc = searchMinCompanionHp();
			if (companionNpc == null) {
				return false;
			}
			if (companionNpc.getCurrentHpPercent() > enableCompanionHp) {
				return false;
			}
			_target = companionNpc;
		}
		int range = _mobSkillTemplate.getRange(idx);
		if (range > 0 && !_mobSkillTemplate.isRange(range, _attacker.getLocation().getTileLineDistance(_target.getLocation()))) {
			return false;
		}
		return true;
	}
	
	private L1NpcInstance searchMinCompanionHp() {
		L1NpcInstance npc;
		L1NpcInstance minHpNpc = null;
		int hpRatio = 100;
		int companionHpRatio;
		int family = _attacker.getNpcTemplate().getFamily();
		for (L1Object object : L1World.getInstance().getVisibleObjects(_attacker)) {
			if (object == null) {
				continue;
			}
			if (object instanceof L1NpcInstance) {
				npc = (L1NpcInstance) object;
				if (npc.getNpcTemplate().getFamily() == family) {
					companionHpRatio = npc.getCurrentHpPercent();
					if (companionHpRatio < hpRatio) {
						hpRatio = companionHpRatio;
						minHpNpc = npc;
					}
				}
			}
		}
		return minHpNpc;
	}

	private void mobspawn(int summonId, int count) {
		for (int i = 0; i < count; i++) {
			mobspawn(summonId);
		}
	}

	private L1NpcInstance mobspawn(int summonId) {
		L1NpcInstance mob = null;
		try {
			L1Npc spawnmonster = NpcTable.getInstance().getTemplate(summonId);
			if (spawnmonster == null) {
				return null;
			}
			try {
				String implementationName = spawnmonster.getImpl();
				Constructor<?> _constructor = Class.forName((new StringBuilder()).append("l1j.server.server.model.Instance.").append(implementationName).append("Instance").toString()).getConstructors()[0];
				mob = (L1NpcInstance) _constructor.newInstance(new Object[] { spawnmonster });
				mob.setId(IdFactory.getInstance().nextId());
				
				L1Location loc = null;
				loc = summonId == 7200030 ? _attacker.getLocation().randomLocation(2, false) : _attacker.getLocation().randomLocation(8, false);
				int heading = random.nextInt(8);
				mob.setX(loc.getX());
				mob.setY(loc.getY());
				mob.setHomeX(loc.getX());
				mob.setHomeY(loc.getY());
				short mapid = _attacker.getMapId();
				mob.setMap(mapid);
				mob.getMoveState().setHeading(heading);
				L1World world = L1World.getInstance();
				world.storeObject(mob);
				world.addVisibleObject(mob);
				L1Object object = world.findObject(mob.getId());
				L1MonsterInstance newnpc = (L1MonsterInstance) object;
				newnpc.setStoreDroped(true);
				if (summonId == 45061 || summonId == 45161 || summonId == 45181 || summonId == 45455) {
					newnpc.broadcastPacket(new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Hide), true);
					newnpc.setActionStatus(13);
					newnpc.broadcastPacket(new S_NPCObject(newnpc), true);
					newnpc.broadcastPacket(new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Appear), true);
					newnpc.setActionStatus(0);
					newnpc.broadcastPacket(new S_NPCObject(newnpc), true);
				} else if (summonId == 900517 || summonId == 900518) {
					newnpc.broadcastPacket(new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Appear), true);
					newnpc.broadcastPacket(new S_NPCObject(newnpc), true);
				}
				newnpc.onNpcAI();
				newnpc.getLight().turnOnOffLight();
				newnpc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
				mob = newnpc;
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return mob;
	}

	private L1Character changeTarget(L1MobSkill.CHANGE_TARGET changeTarget, int idx) {
		L1Character target;
		switch (changeTarget) {
		case ME:
			target = _attacker;
			break;
		case RANDOM:
			List<L1Character> targetList = new ArrayList<L1Character>();
			L1Character cha = null;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(_attacker)) {
				if (obj == null) {
					continue;
				}
				if (obj instanceof L1PcInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
					cha = (L1Character) obj;
					if (cha.isDead() || cha instanceof L1PcInstance && ((L1PcInstance) cha).isGhost()) {
						continue;
					}
					int range = _mobSkillTemplate.getRange(idx);
					if (range > 0 && !_mobSkillTemplate.isRange(range, _attacker.getLocation().getTileLineDistance(cha.getLocation()))) {
						continue;
					}
					if (!_attacker.glanceCheck(range, cha.getX(), cha.getY(), cha instanceof L1DoorInstance)) {
						continue;
					}
					if (!_attacker.getHateList().containsKey(cha) && !((obj instanceof L1PetInstance || obj instanceof L1SummonInstance) && _target.getPetList().containsKey(cha.getId()))) {
						continue;
					}
					targetList.add((L1Character) obj);
				}
			}
			if (targetList.size() == 0) {
				target = _target;
			} else {
				int targetIndex = random.nextInt(targetList.size() * 100) / 100;
				target = targetList.get(targetIndex);
			}
			break;
		default:
			target = _target;
			break;
		}
		return target;
	}
	
	class delayUseSkill implements Runnable {// 멘트 딜레이
		private int skillNum = 0;

		public delayUseSkill(int i) {
			skillNum = i;
		}

		@Override
		public void run() {
			if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
				return;
			}
			synchronized (_attacker.synchObject) {
				if (_attacker.isParalyzed() || _attacker.isSleeped() || _target == null || _target.isDead()) {
					_isMentDelay = false;
					return;
				}
				try {
					_attacker.setParalyzed(true);
					if (isSpell(skillNum, true)) {
						skillUseCountUp(skillNum);
						if (_sleepTime > 0) {
							Thread.sleep(_attacker.calcSleepTime(_sleepTime, L1NpcInstance.MAGIC_SPEED));
						}
					}
					if (_attacker == null) {
						return;
					}
					_attacker.setParalyzed(false);
					_isMentDelay = _isActionDelay = false;
				} catch (Exception e) {
					if (_attacker == null) {
						return;
					}
					_attacker.setParalyzed(false);
					_isMentDelay = _isActionDelay = false;
				}
			}
		}
	}
	
	private boolean isLineAttack(int idx) {
		try {
			GeneralThreadPool.getInstance().execute(new LineMagicThread(idx));
			_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), _mobSkillTemplate.getActid(idx)), true);
			_sleepTime = _attacker.getNpcTemplate().getAtkMagicSpeed();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
	
	class LineMagicThread implements Runnable {
		private int idx = 0;
		private int heading = -1;

		public LineMagicThread(int id) {
			idx = id;
		}

		public LineMagicThread(int id, int head) {
			idx = id;
			heading = head;
		}

		@Override
		public void run() {
			int skillid		= _mobSkillTemplate.getSkillId(idx);
			int range		= _mobSkillTemplate.getRange(idx);
			short gfx		= (short) _mobSkillTemplate.getGfxid(idx);
			int leverage	= _mobSkillTemplate.getLeverage(idx);
			try {
				L1Character caster = _attacker;
				boolean threeJump = false;
				if (caster instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) caster;
					if (mon.getNpcId() == 5135 || mon.getNpcId() == 5136) {
						threeJump = true;
					}
				}
				int xx = caster.getX();
				int yy = caster.getY();
				int a = heading == -1 ? _attacker.calcheading(xx, yy, _target.getX(), _target.getY()) : heading;
				int subCount = 0;
				int[] xlist = new int[range];
				int[] ylist = new int[range];
				ArrayList<L1PcInstance> list = L1World.getInstance().getVisiblePlayer(caster, range);
				for (int i = 0; i < range; i++) {
					int x = xx;
					int y = yy;
					x += HEADING_TABLE_X[a];
					y += HEADING_TABLE_Y[a];
					if (threeJump) {
						subCount++;
						if (subCount == 3) {
							caster.broadcastPacket(new S_EffectLocation(x, y, gfx), true);
							subCount = 0;
						}
					} else {
						caster.broadcastPacket(new S_EffectLocation(x, y, gfx), true);
					}
					xlist[i] = x;
					ylist[i] = y;
					xx = x;
					yy = y;
					Thread.sleep(50);
				}
				for (L1PcInstance pc : list) {
					if (pc.isGhost() || pc.isDead() || pc.isPrivateShop() || pc.isAutoClanjoin() || pc.noPlayerCK) {
						continue;
					}
					for (int i = 0; i < range; i++) {
						if (pc.getX() != xlist[i] || pc.getY() != ylist[i]) {
							continue;
						}
						if (pc.isAbsol() || pc.isBind() || pc.getSkill().hasSkillEffect(L1SkillId.STATUS_SAFTY_MODE)) {
							continue;
						}
						L1Magic _magic = new L1Magic(_attacker, pc);
						_magic.setLeverage(leverage);
						int dmg = _magic.calcMagicDamage(skillid);
						_magic.commit(dmg, 0);
						if (dmg > 0) {
							pc.broadcastPacketWithMe(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
						}
						break;
					}
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	void send_halpas_area_effect(int effect_id) {
		switch (_attacker.getMoveState().getHeading()) {
		case 0:
		case 1:
			_attacker.broadcastPacket(new S_EffectLocation(32783, 32889, effect_id), true);
			_attacker.broadcastPacket(new S_EffectLocation(32795, 32889, effect_id), true);
			break;
		case 2:
		case 3:
			_attacker.broadcastPacket(new S_EffectLocation(32795, 32889, effect_id), true);
			_attacker.broadcastPacket(new S_EffectLocation(32795, 32901, effect_id), true);
			break;
		case 4:
		case 5:
			_attacker.broadcastPacket(new S_EffectLocation(32795, 32901, effect_id), true);
			_attacker.broadcastPacket(new S_EffectLocation(32783, 32901, effect_id), true);
			break;
		case 6:
		case 7:
			_attacker.broadcastPacket(new S_EffectLocation(32783, 32901, effect_id), true);
			_attacker.broadcastPacket(new S_EffectLocation(32783, 32889, effect_id), true);
			break;
		}
	}
	
	private void halpasSkill(int skillId){
		switch (skillId) {
		case L1SkillId.HALPAS_FIRE_BRESS:
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 6)) {
				if (pc == null || pc.isDead() || pc.isGhost() || pc.isAbsol() || pc.isBind()
						|| pc.getSkill().hasSkillEffect(skillId)
						|| pc.getSkill().hasSkillEffect(L1SkillId.CURSE_PARALYZE)
						|| pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING)
						|| pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED)) {
					continue;
				}
				if (random.nextInt(10) + 1 > 3) {
					L1DamagePoison.doInfection(pc, _target, 7000, 100, true);
					pc.getSkill().setSkillEffect(skillId, 7000);
					pc.sendPackets(new S_SpellBuffNoti(pc, skillId, true, 7), true);
				}
			}
			if (_attacker.getMap().getBaseMapId() == 1191) {// 레이드맵
				send_halpas_area_effect(19245);
			}
			break;
		case L1SkillId.HALPAS_POISON_BRESS:
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 6)) {
				if (pc == null || pc.isDead() || pc.isGhost() || pc.isAbsol() || pc.isBind()
						|| pc.getSkill().hasSkillEffect(skillId)
						|| pc.getSkill().hasSkillEffect(L1SkillId.CURSE_PARALYZE)
						|| pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING)
						|| pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED)) {
					continue;
				}
				if (random.nextInt(10) + 1 > 3) {
					L1CurseParalysis.curse(pc, 0, 4000);
					pc.getSkill().setSkillEffect(skillId, 4000);
					pc.sendPackets(new S_SpellBuffNoti(pc, skillId, true, 4), true);
				}
			}
			if (_attacker.getMap().getBaseMapId() == 1191) {// 레이드맵
				send_halpas_area_effect(19249);
			}
			break;
		case L1SkillId.HALPAS_ICE_BRESS:
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 6)) {
				if (pc == null || pc.isDead() || pc.isGhost() || pc.isAbsol() || pc.isBind()
						|| pc.getSkill().hasSkillEffect(skillId)
						|| pc.getSkill().hasSkillEffect(L1SkillId.CURSE_PARALYZE)
						|| pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING)
						|| pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED)) {
					continue;
				}
				if (random.nextInt(10) + 1 > 3) {
					int freezetime = random.nextInt(10) + 1;
					pc.getSkill().setSkillEffect(skillId, freezetime * 1000);
					L1EffectSpawn.getInstance().spawnEffect(81182, freezetime * 1000, pc.getX(), pc.getY(), pc.getMapId());
					pc.sendPackets(S_Paralysis.BIND_ON);
					pc.sendPackets(new S_SpellBuffNoti(pc, skillId, true, freezetime), true);
				}
			}
			if (_attacker.getMap().getBaseMapId() == 1191) {// 레이드맵
				send_halpas_area_effect(19247);
			}
			break;
		case L1SkillId.HALPAS_WIND_BRESS:
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 6)) {
				if (pc == null || pc.isDead() || pc.isGhost() || pc.isAbsol() || pc.isBind()
						|| pc.getSkill().hasSkillEffect(skillId)
						|| pc.getSkill().hasSkillEffect(L1SkillId.CURSE_PARALYZE)
						|| pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING)
						|| pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED)) {
					continue;
				}
				if (random.nextInt(10) + 1 > 3) {
					pc.getSkill().setSkillEffect(skillId, 7000);
					pc.sendPackets(new S_SpellBuffNoti(pc, skillId, true, 7), true);
				}
			}
			break;
		case L1SkillId.HALPAS_FLY_TELEPORT:
			L1Map map = L1WorldMap.getInstance().getMap(_attacker.getMapId());
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 6)) {
				if (pc == null || pc.isGhost() || pc.isDead()) {
					continue;
				}
				for (int count = 0; count < 10; count++) {
					L1Location newLoc = _attacker.getLocation().randomLocation(6, 14, false);
					if (map.isInMap(newLoc.getX(), newLoc.getY()) && map.isPassable(newLoc.getX(), newLoc.getY())) {
						pc.getTeleport().start(newLoc, pc.getMoveState().getHeading(), true);
						break;
					}
				}
			}
			break;
		case L1SkillId.HALPAS_IMMUNE_BLADE:
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 6)) {
				if (pc == null || pc.isDead() || pc.isGhost()) {
					continue;
				}
				if (random.nextInt(10) + 1 > 3 && pc.getSkill().hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)) {
					pc.getSkill().removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
					pc.send_effect(15961);
				    pc.sendPackets(new S_SpellBuffNoti(pc, L1SkillId.IMMUNE_TO_HARM, false, -1), true);
				}
			}
			break;
		case L1SkillId.HALPAS_RECALL:
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, -1)) {
				if (pc == null || pc.isGhost() || pc.isDead()) {
					continue;
				}
				if (_attacker.getLocation().getTileLineDistance(pc.getLocation()) > 8) {
					for (int count = 0; count < 10; count++) {
						L1Location newLoc = _attacker.getLocation().randomLocation(3, 6, false);
						if (_attacker.glanceCheck(15, newLoc.getX(), newLoc.getY(), false)) {
							pc.getTeleport().start(newLoc, pc.getMoveState().getHeading(), true);
							break;
						}
					}
				}
			}
			break;
		case L1SkillId.HALPAS_ZAKEN_CALL:
			GeneralThreadPool.getInstance().execute(new HalpasZakenCall());
			break;
		default:
			break;
		}
	}
	
	class StarAreaEffectSpawn implements Runnable {
		L1NpcInstance _npc;
		int count = 10;
		private StarAreaEffectSpawn(L1NpcInstance npc) {
			_npc = npc;
		}

		@Override
		public void run() {
			try {
				if (_npc == null || _npc._destroyed || _npc.isDead() || count <= 0) {
					return;
				}
				_npc.broadcastPacket(new S_EffectLocation(_npc.getX(), _npc.getY(), 761), true);// 이팩트
				_npc.setCurrentHp(_npc.getCurrentHp() + 50);
				count--;
				GeneralThreadPool.getInstance().schedule(this, 1000L);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	private void rangeStun(){
		try {
			if (_attacker == null || _attacker._destroyed || _attacker.isDead()) {
				return;
			}
			int duration = CommonUtil.randomIntChoice(L1SkillInfo.MONSTER_STUN_ARRAY);
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 8)) {
				if (pc == null || pc.isGhost() || pc.isDead() || pc.isAbsol() || pc.isBind()) {
					continue;
				}
				if (random.nextInt(10) + 1 < 8) {
				    L1EffectSpawn.getInstance().spawnEffect(91162, duration, pc.getX(), pc.getY(), pc.getMapId());
				    pc.getSkill().setSkillEffect(L1SkillId.MOB_RANGESTUN_30, duration);
				    pc.sendPackets(S_Paralysis.STURN_ON);
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	class fireMeteorRangeStun implements Runnable {
		L1NpcInstance _npc = null;

		public fireMeteorRangeStun(L1NpcInstance npc) {
			_npc = npc;
		}

		@Override
		public void run() {
			try {
				if (_npc == null || _npc._destroyed || _npc.isDead()) {
					return;
				}
				_npc.broadcastPacket(new S_EffectLocation(_npc.getX(), _npc.getY(), (short) 15930), true);// 이팩트
				int duration = CommonUtil.randomIntChoice(L1SkillInfo.MONSTER_STUN_ARRAY);
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_npc, 8)) {
					if (pc == null || pc.isGhost() || pc.isDead() || pc.isAbsol() || pc.isBind()) {
						continue;
					}
					if (random.nextInt(10) + 1 < 8) {
					    L1EffectSpawn.getInstance().spawnEffect(91162, duration, pc.getX(), pc.getY(), pc.getMapId());
					    pc.getSkill().setSkillEffect(L1SkillId.MOB_RANGESTUN_30, duration);
					    pc.sendPackets(S_Paralysis.STURN_ON);
					}
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	/** 기르타스 스킬 **/
	private boolean isKirtasMeteor(int idx) {
		try {
			_attacker.broadcastPacket(new S_EffectLocation(_target.getX(), _target.getY(), (short) 11469), true);
			GeneralThreadPool.getInstance().schedule(new KirtasMeteor(idx, _target.getX(), _target.getY()), 5500);
			//_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "voice_kirtas_12", L1ChatType.SOUND), true); //크하하하 죽는거다
			_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), 75), true);
			_sleepTime = _attacker.getNpcTemplate().getAtkMagicSpeed() + 5500;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	class KirtasMeteor implements Runnable {
		private int idx = 0;
		private int x = 0, y = 0;

		public KirtasMeteor(int id, int x, int y) {
			idx = id;
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			if (_attacker == null || _attacker.isDead() || _attacker._destroyed || (_attacker.getActionStatus() == 0 && _attacker.isParalyzed())) {
				return;
			}
			synchronized (_attacker.synchObject) {
				try {
					if (_attacker.getActionStatus() == 0) {
						_attacker.setParalyzed(true);
					}
					int skillid = _mobSkillTemplate.getSkillId(idx);
					int leverage = _mobSkillTemplate.getLeverage(idx);

					_attacker.broadcastPacket(new S_EffectLocation(x, y, (short) 11473), true);
					ArrayList<L1Object> list = L1World.getInstance().getVisiblePoint((new L1Location(x, y, _attacker.getMapId())), 3);
					for (L1Object obj : list) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isGhost() || pc.isDead() || pc.isGm() || pc.isAbsol() || pc.isBind()) {
								continue;
							}
							L1Magic _magic = new L1Magic(_attacker, pc);
							_magic.setLeverage(leverage);
							int dmg = _magic.calcMagicDamage(skillid);
							_magic.commit(dmg, 0);
							if (dmg > 0 && !pc.isDead()) {
								pc.broadcastPacketWithMe(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
							}
							_magic = null;
						}
					}
					int time = _attacker.getSprite().getDirSpellSpeed();
					if (time > 0) {
						Thread.sleep(_attacker.calcSleepTime(time + 500, L1NpcInstance.MAGIC_SPEED));
					}
				} catch (Exception e) {
				}
				if (_attacker.getActionStatus() == 0) {
					_attacker.setParalyzed(false);
				}
			}
		}
	}

	private boolean isKirtasBarrier(int idx) {
		try {
			_attacker.setParalyzed(true);
			int actid = _mobSkillTemplate.getActid(idx);
			_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), actid), true);
			_sleepTime = _attacker.getSprite().getMoveSpeed(actid);
			GeneralThreadPool.getInstance().schedule(new KirtasBarrier(idx), _sleepTime);
			GeneralThreadPool.getInstance().schedule(new KirtasFire(), 3000L);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	class KirtasBarrier implements Runnable {
		private int idx = 0;
		private byte step = 0;
		private byte count = 15;// 지속시간

		public KirtasBarrier(int id) {
			idx = id;
		}

		@Override
		public void run() {
			try {
				// 그외 종료 가능성
				if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
					return;
				}
				if (count-- <= 0) {
					// voice_kirtas_2 나약한 인간따위가 감히 신에게 도전하는가 3 으억! 날이겼다고 생각하는가 나 기르타스 4 이야앗! 5 쿠어엇 6키야하하 7 쿠어아 8영혼만 남겨두고 모두 소멸하리라 9 귀찮은놈들 사라져라 10 모두 녹아버려라 11 타올라라 12죽는거다
					_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), _attacker.getActionStatus() + 1), true);
					_attacker.setActionStatus(0);
					_attacker.broadcastPacket(new S_CharVisualUpdate(_attacker), true);
					_attacker.setParalyzed(false);
					((L1MonsterInstance)_attacker).kirtasAbsolute 
					= ((L1MonsterInstance)_attacker).kirtasCounterBarrier 
					= ((L1MonsterInstance)_attacker).kirtasCounterMagic 
					= ((L1MonsterInstance)_attacker).kirtasPoisonBarrier = false;
					// 풀기 액션 슬립 액션 스테이터스
					return;
				}
				if (step == 0) {
					switch(idx){
					case 0:// 녹색
						_attacker.setActionStatus(24);
						((L1MonsterInstance)_attacker).kirtasPoisonBarrier = true;
						break;
					case 1:// 회색
						_attacker.setActionStatus(4);
						((L1MonsterInstance)_attacker).kirtasAbsolute = true;
						break;
					case 2:// 빨간
						_attacker.setActionStatus(20);
						((L1MonsterInstance)_attacker).kirtasCounterBarrier = true;
						break;
					case 3:// 황색
						_attacker.setActionStatus(40);
						((L1MonsterInstance)_attacker).kirtasCounterMagic = true;
						break;
					}
					_attacker.broadcastPacket(new S_CharVisualUpdate(_attacker), true);
					_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), _attacker.getActionStatus() + 3), true);
					step++;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000L);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				_attacker.setParalyzed(false);
			}
		}
	}

	class KirtasFire implements Runnable {

		@Override
		public void run() {
			try {
				if (_attacker == null || _attacker._destroyed || _attacker.isDead() || _attacker.getActionStatus() == 0) {
					return;
				}
				//_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "voice_kirtas_11", L1ChatType.SOUND), true);// 타올라라
				for (int i = 0; i < 2; i++) {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							L1SpawnUtil.spawn2(32850 + random.nextInt(9), 32858 + random.nextInt(9), (short) _attacker.getMapId(), 0, 100586, 2, random.nextInt(2000) + 13000, 0);
						}
					}, random.nextInt(2000));
				}
				for (int i = 0; i < 4; i++) {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							L1SpawnUtil.spawn2(32850 + random.nextInt(9), 32858 + random.nextInt(9), (short) _attacker.getMapId(), 0, 100587, 2, random.nextInt(2000) + 13000, 0);
						}
					}, random.nextInt(2000));
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	private boolean isTitanGolemBarrier(int idx) {// 타이탄 골렘 배리어
		try {
			_attacker.setParalyzed(true);
			int actid = _mobSkillTemplate.getActid(idx);
			_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), actid), true);
			_sleepTime = _attacker.getSprite().getMoveSpeed(actid);
			GeneralThreadPool.getInstance().schedule(new TitanGolemBarrier(idx), _sleepTime);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	class TitanGolemBarrier implements Runnable {
		private int idx = 0;
		private byte step = 0;
		private byte count = 15;// 지속시간

		public TitanGolemBarrier(int id) {
			idx = id;
		}

		@Override
		public void run() {
			try {
				if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
					return;// 그외 종료 가능성
				}
				if (count-- <= 0) {
					_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), _attacker.getActionStatus() + 1), true);
					_attacker.setActionStatus(0);
					_attacker.broadcastPacket(new S_CharVisualUpdate(_attacker), true);
					_attacker.setParalyzed(false);
					for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(_attacker, 10)) {
						if (_pc==null || _pc.isGhost() || _pc.isDead() || _pc.isAbsol() || _pc.isBind()) {
							continue;
						}
						_pc.receiveDamage(_attacker, 600, 2);
						_pc.broadcastPacketWithMe(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage), true);
					}
					((L1MonsterInstance)_attacker).titanPoisonBarrier 
					= ((L1MonsterInstance)_attacker).titanCounterBarrier 
					= ((L1MonsterInstance)_attacker).titanCounterMagic = false;
					// 풀기 액션 슬립 액션 스테이터스
					return;
				}
				if (step == 0) {
					switch(idx){
					case 0:// 녹색
						_attacker.setActionStatus(24);
						((L1MonsterInstance)_attacker).titanPoisonBarrier = true;
						break;
					case 1:// 노랑
						_attacker.setActionStatus(40);
						((L1MonsterInstance)_attacker).titanCounterMagic = true;
						break;
					case 2:// 빨간
						_attacker.setActionStatus(20);
						((L1MonsterInstance)_attacker).titanCounterBarrier = true;
						break;
					}
					_attacker.broadcastPacket(new S_CharVisualUpdate(_attacker), true);
					_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), _attacker.getActionStatus() + 3), true);
					step++;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				_attacker.setParalyzed(false);
			}
		}
	}
	
	/** 발라카스 브레스 **/
	private boolean isVallakasBress(int idx) {
		try {
			_attacker.setParalyzed(true);
			_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "$25722", ChatType.CHAT_NORMAL), true);// 쿠르 둠 리라스 쿰 케르하
			int actid = _mobSkillTemplate.getActid(idx);
			GeneralThreadPool.getInstance().execute(new VallakasWingBress(actid));
		} catch (Exception e) {
			_attacker.setParalyzed(false);
			return false;
		}
		return true;
	}
	
	class VallakasWingBress implements Runnable {
		private int count = 4;
		private int actionid = 0;
		VallakasWingBress(int actid){
			actionid=actid;
		}
		
		@Override
		public void run() {
			try {
				if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
					return;
				}
				if (count-- <= 0) {
					_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), 41), true);// 브레스 액션
					Thread.sleep(4000);
					if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
						return;
					}
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 6)) {
						if (pc == null || pc.isGhost() || pc.isDead() || pc.isAbsol() || pc.isBind()) {
							continue;
						}
						pc.receiveDamage(_attacker, 600, 2);
						pc.broadcastPacketWithMe(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
					}
					_attacker.broadcastPacket(new S_EffectLocation(_attacker.getX(), _attacker.getY(), (short) 15930), true);// 이팩트
					Thread.sleep(2000);
					if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
						return;
					}
					_attacker.broadcastPacket(new S_CharVisualUpdate(_attacker), true);
					_attacker.setParalyzed(false);
					return;
				}
				if (count == 1) {
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 6)) {
						if (pc == null || pc.isGhost() || pc.isDead() || pc.isAbsol() || pc.isBind()) {
							continue;
						}
						pc.receiveDamage(_attacker, 300, 2);
						pc.broadcastPacketWithMe(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
					}
				}
				_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), actionid), true);
				GeneralThreadPool.getInstance().schedule(this, 1500);
			} catch (Exception e) {
				_attacker.setParalyzed(false);
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	private boolean isVallakasFly(int idx) {// 발라카스 날기 액션
		try {
			_attacker.setParalyzed(true);
			_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "$25723", ChatType.CHAT_NORMAL), true);// 쿠르 둠 리라스 쿰 헤지후타 투자
			GeneralThreadPool.getInstance().execute(new VallakasFlyStart(idx));
		} catch (Exception e) {
			_attacker.setParalyzed(true);
			return false;
		}
		return true;
	}
	
	class VallakasFlyStart implements Runnable {
		private int count = 4;
		private int idx = 0;
		VallakasFlyStart(int index) {
			idx = index;
		}

		@Override
		public void run() {
			try {
				if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
					return;// 종료 상황
				}
				if (count-- <= 0) {
					_attacker.broadcastPacket(new S_EffectLocation(_attacker.getX(), _attacker.getY(), (short) 15930), true);// 이팩트
					_sleepTime = _attacker.getSprite().getMoveSpeed(_mobSkillTemplate.getActid(idx));
					GeneralThreadPool.getInstance().schedule(new VallakasFlyEnd(), _sleepTime);
					return;
				} else if (count == 1) {
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker, 6)) {
						if (pc == null || pc.isGhost() || pc.isDead() || pc.isAbsol() || pc.isBind()) {
							continue;
						}
						pc.receiveDamage(_attacker, 500, 2);
						pc.broadcastPacketWithMe(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
					}
				}
				_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_Idle_TWO), true);
				GeneralThreadPool.getInstance().schedule(this, 1500);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				_attacker.setParalyzed(false);
			}
		}
	}

	class VallakasFlyEnd implements Runnable {
		private byte count = 9;// 지속시간

		@Override
		public void run() {
			try {
				if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
					return;// 종료 상황
				}
				if (count == 9) {
					_attacker.setActionStatus(ActionCodes.ACTION_AxeDamage);
					((L1MonsterInstance)_attacker)._vallacasFly = true;
					_attacker.broadcastPacket(new S_CharVisualUpdate(_attacker), true);
					_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_AxeDamage), true);// 날기 액션
					_attacker.getMap().setPassable(_attacker.getLocation(), true);
					L1Location randomLoc = _attacker.getLocation().randomLocation(8, false);// 이동할 좌표
					_attacker.setX(randomLoc.getX());
					_attacker.setY(randomLoc.getY());
					L1World.getInstance().onMoveObject(_attacker);
					_attacker.getMap().setPassable(_attacker.getLocation(), false);
				}
				
				if (count-- <= 0) {
					for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(_attacker)) {
						if (pc == null) {
							continue;
						}
						pc.removeKnownObject(_attacker);
						pc.updateObject();
					}
					_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_AxeWalk), true);// 등장 액션
					Thread.sleep(2000);
					if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
						return;// 종료 상황
					}
					_attacker.setActionStatus(0);
					_attacker.broadcastPacket(new S_CharVisualUpdate(_attacker), true);
					((L1MonsterInstance)_attacker)._vallacasFly = false;
					_attacker.setParalyzed(false);
					// 풀기 액션 슬립 액션 스테이터스
					return;
				} else if (count == 4) {
					L1EffectSpawn.getInstance().spawnEffect(7311171, 5, _attacker.getX(), _attacker.getY(), (short) _attacker.getMapId());// 그림자
				} else if (count == 1) {
					_attacker.broadcastPacket(new S_EffectLocation(_attacker.getX(), _attacker.getY(), (short) 15930), true);// 등장이팩트
				}
				GeneralThreadPool.getInstance().schedule(this, 1000L);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				_attacker.setParalyzed(false);
			}
		}
	}
	
	class HalpasZakenCall implements Runnable {
		@Override
		public void run() {
			try {
				if (_attacker == null || _attacker.isDead() || _attacker._destroyed) {
					return;// 종료 상황
				}
				L1EffectInstance effect = L1EffectSpawn.getInstance().spawnEffect(900520, 10 * 1000, 32787, 32890, (short) _attacker.getMapId());// 자켄 등장
				effect.broadcastPacket(new S_Effect(effect.getId(), effect.getSpriteId()), true);// 등장후 땅찍기
				Thread.sleep(5000L);
				if (_attacker != null && !_attacker.isDead() && !_attacker._destroyed) {
					L1Location loc;
					if (random.nextBoolean()) {// 몬스터 스폰
						for (int i=0; i<4; i++) {
							loc = _attacker.getLocation().randomLocation(5, false);
							L1SpawnUtil.spawn2(loc.getX(), loc.getY(), (short) _attacker.getMapId(), 5, i == 0 ? 900518 : 900517, 0, 1800 * 1000, 0);
						
						}
					} else {// 이뮨투함 트랩 스폰
						for (int i=0; i<4; i++) {
							loc = _attacker.getLocation().randomLocation(10, false);
							L1EffectSpawn.getInstance().spawnEffect(900521, 20 * 1000, loc.getX(), loc.getY(), (short) _attacker.getMapId());
						}
					}
					loc = null;
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				_attacker.setParalyzed(false);
			}
		}
	}
	
	class DarkWatchMeteor implements Runnable {
		@Override
		public void run() {
			try {
				if (_attacker == null) {
					return;
				}
				_attacker.broadcastPacket(new S_Effect(_attacker.getId(), 20381), true);// 아우라키아 모션 이팩트
				_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "$36747", ChatType.CHAT_NORMAL), true);// 깨어나라 아이들아
				mobspawn(7800307, 4);// 깨어난 해츨링 소환
				Thread.sleep(3000);
				if (_attacker == null) {
					return;
				}
				_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "$36748", ChatType.CHAT_NORMAL), true);// 장난은 그만하지
				L1Location loc = null;
				for (int i=0; i<4; i++) {
					loc = _attacker.getLocation().randomLocation(10, true);
					L1EffectSpawn.getInstance().spawnEffect(7800312, 10000, loc.getX(), loc.getY(), (short)loc.getMapId());// 보호막 생성
				}
				Thread.sleep(2000);
				if (_attacker == null) {
					return;
				}
				_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "$36749", ChatType.CHAT_NORMAL), true);// 어둠이여...
				Thread.sleep(3000);
				if (_attacker == null) {
					return;
				}
				_attacker.broadcastPacket(new S_NpcChatPacket(_attacker, "$36750", ChatType.CHAT_NORMAL), true);// 이제 다 죽어라
				ArrayList<L1PcInstance> targetList	= L1World.getInstance().getVisiblePlayer(_attacker, 14);
				L1PcInstance target					= null;
				for (int i=0; i<targetList.size(); i++) {
					target = targetList.get(i);
					for (L1Object obj : L1World.getInstance().getVisibleObjects(target, 2)) {
						if (!(obj instanceof L1EffectInstance)) {
							continue;
						}
						if (((L1EffectInstance)obj).getNpcId() == 7800312) {
							targetList.remove(target);// 보호막 안 제외
							break;
						}
					}
				}
				for (int i=0; i<10; i++) {
					if (_attacker == null) {
						return;
					}
					loc = _attacker.getLocation().randomLocation(14, true);
					_attacker.broadcastPacket(new S_EffectLocation(loc.getX(), loc.getY(), 20294), true);// 미티어 이팩트
					Thread.sleep(200);
				}
				if (_attacker == null) {
					return;
				}
				for (L1PcInstance pc : targetList) {
					if (pc == null) {
						continue;
					}
					pc.receiveDamage(_attacker, 500, 2);// 대미지
					pc.broadcastPacketWithMe(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
				}
				targetList.clear();
				targetList = null;
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
}


package l1j.server.server.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.GameSystem.inter.L1InterServerFactory;
import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.IndunSystem.occupy.OccupyUtil;
import l1j.server.IndunSystem.ruun.Ruun;
import l1j.server.IndunSystem.treasureisland.TreasureIsland;
import l1j.server.common.data.ePolymorphAnonymityType;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.controller.action.BossSpawn;
import l1j.server.server.datatables.FreePVPRegionTable;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.PlaySupportTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_Portal;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.S_TeamIdServerNoMappingInfo;
import l1j.server.server.serverpackets.S_Teleport;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.S_WorldPut;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.object.S_CompanionObject;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.object.S_SummonObject;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.serverpackets.spell.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.teaminfo.S_InfinityBattleEnterMapNoti;
import l1j.server.server.serverpackets.teaminfo.S_InfinityBattleLeaveMapNoti;
import l1j.server.server.utils.CommonUtil;

/**
 * player에게 할당된 텔레포트 핸들러
 * @author LinOffice
 */
public class L1Teleport {
	private final L1PcInstance _owner;
	private final L1World _world;
	private final L1WorldMap _worldMap;
    private final ConcurrentHashMap<Integer, S_Effect> _effects;
    
    protected boolean geradBuff, growBuff;
    public boolean isGeradBuff(){
    	return geradBuff;
    }
    public boolean isGrowBuff(){
    	return growBuff;
    }
    
    /**
     * 생성자
     * @param owner
     */
	public L1Teleport(L1PcInstance owner) {
		_owner		= owner;
		_world		= L1World.getInstance();
		_worldMap	= L1WorldMap.getInstance();
		_effects	= new ConcurrentHashMap<Integer, S_Effect>();
	}
	
	/**
	 * 텔레포트에 사용될 변수 선언
	 */
	private boolean _isTeleport;
	private int _teleportX, _teleportY, _teleportHead, _stateX, _stateY;
    private short _teleportMapId, _stateMapId = -1;
    
    public boolean isTeleport() {
    	return _isTeleport;
    }
	public void setTeleport(final boolean flag) {
		_isTeleport = flag;
	}
	void setTeleportLoc(final int x, final int y, final short mapId, final int head){
		_teleportX		= x;
		_teleportY		= y;
		_teleportMapId	= mapId;
		_teleportHead	= head;
	}
    
    public void setStateLoc(final int x, final int y, final short mapId){
    	_stateX		= x;
    	_stateY		= y;
    	_stateMapId	= mapId;
    }
    
    public void stateLeave(){
    	if (_stateMapId == -1) {
    		start(32612, 32734, (short) 4, _owner.moveState.getHeading(), true);
    		return;
    	}
    	start(_stateX, _stateY, _stateMapId, _owner.moveState.getHeading(), true);// 원위치
    	setStateLoc(0, 0, (short)-1);
    }
    
    /**
     * 이팩트 팩토리(패킷 재사용)
     * 조사한 이팩트가 Map에 없으면 put
     * key: effectId, value: S_SkillSound
     * @param effectId
     * @return S_SkillSound
     */
    S_Effect getEffect(final int effectId){
    	S_Effect effect = _effects.get(effectId);
		if (effect == null) {
			effect = new S_Effect(_owner.getId(), effectId);
			_effects.put(effectId, effect);
		}
		return effect;
    }
    
    /**
	 * 인터서버 접속 처리
	 * @param inter
	 * @return boolean
	 */
	boolean isRegistInterServer(final L1InterServer inter){
		if (_owner.isInParty()) {
			_owner.getParty().leaveMember(_owner);
		}
		
		L1CharacterConfig config = _owner.getConfig();
		if (config.isPlaySupport()) {
			config.finishPlaySupport();
		}
		if (inter == L1InterServer.LEAVE) {// 탈출
			if (_owner._isWorldPoly) {
				L1PolyMorph.worldClassPoly(_owner, false);// 고유 쿨래스 변신 해제
			}
			if (_owner._isAnonymityPoly) {
				L1PolyMorph.anonymityPoly(_owner, false);// 익명 변신 해제
			}
			
			L1InterServer current = _owner.getMap().getInter();
			if (current == L1InterServer.INSTANCE_DUNGEON && _owner.getMap().getBaseMapId() == 736) {
				_owner.sendPackets(S_AvailableSpellNoti.ARROW_OF_AURAKIA_OFF);
				_owner.isArrowOfAurakiaSkill = false;
			} else if (current == L1InterServer.TREASURE_ISLAND) {
				TreasureIsland.getInstance().leave(_owner);
			}
			config.setAnonymityType(null);
		}
		
		// 인터서버 사용 여부
		if (Config.INTER.INTER_SERVER_ACTIVE) {
			// TODO 인터서버 진입 성공
			if (L1InterServerFactory.regist(_owner, _teleportX, _teleportY, _teleportMapId, _teleportHead, inter)) {
				return true;
			}
			// 인터서버 진입 실패
			setTeleportLoc(_owner.getX(), _owner.getY(), _owner.getMapId(), _owner.moveState.getHeading());
			_owner.sendPackets(L1SystemMessage.INTER_SERVER_CONNECT_FAIL);
			//System.out.println(String.format("[인터서버] 진입 실패 : 캐릭터명(%s) 롤백 처리되었습니다.", _owner.getName()));
			System.out.println(String.format("[Interserver] Entry failed: Character name (%s) has been rolled back.", _owner.getName()));
			return false;
		}
		
		// 인터서버 미사용(일부 기능 사용 불가)
		switch(inter){
		case FORGOTTEN_ISLAND:case LFC:case DOMINATION_TOWER:
			_owner.sendPackets(S_TeamIdServerNoMappingInfo.ON);
			return false;
		case WORLD_WAR:case ANT_QUEEN:case OCCUPY_HEINE:case OCCUPY_WINDAWOOD:
			if (!_owner._isWorldPoly) {
				L1PolyMorph.worldClassPoly(_owner, true);// 고유 쿨래스 변신
			}
			if (L1InterServer.isOccupyInter(inter) && _owner._occupyTeamType == null) {// 점령전 입장
				final OccupyHandler occHandler = OccupyManager.getInstance().getHandler(inter == L1InterServer.OCCUPY_HEINE ? OccupyType.HEINE : OccupyType.WINDAWOOD);
				if (occHandler == null) {
					return false;
				}
				_owner._occupyTeamType = occHandler.registTeam(_owner);// 점령전 팀 설정
				// 팀 시작 좌표
				final int[] loc = OccupyUtil.getTeamLoc(occHandler, _owner._occupyTeamType);
				_teleportX = loc[0];
				_teleportY = loc[1];
				config.setAnonymityType(ePolymorphAnonymityType.eRandomIncludetombstone);
			}
			return false;
		case ABADON:
			if (!_owner._isAnonymityPoly) {
				L1PolyMorph.anonymityPoly(_owner, true);// 고유 쿨래스 변신
			}
			config.setAnonymityType(ePolymorphAnonymityType.eRandomIncludetombstone);
			return false;
		case TREASURE_ISLAND:
			TreasureIsland.getInstance().enter(_owner);
			return false;
		case LEAVE:
			if (config.isIndunLoginCheck()) {
				config.setIndunLoginCheck(false);
			}
			_owner.sendPackets(S_TeamIdServerNoMappingInfo.OFF);
			if (_owner._occupyTeamType != null) {
				final OccupyHandler occHandler = OccupyManager.getInstance().getHandler(_owner.getMapId() == L1TownLocation.MAP_WOLRDWAR_HEINE_TOWER ? OccupyType.HEINE : OccupyType.WINDAWOOD);
				if (occHandler != null) {
					occHandler.cancelTeam(_owner);// 점령전 취소
				}
			}
			return false;
		default:
			return false;
		}
	}
	
	/**
	 * 인터서버 위치 조사
	 * @param moveMapId
	 * @return L1InterServerConstruct
	 */
	L1InterServer getInterServer(final short moveMapId){
		final L1InterServer after	= MapsTable.getInterServerMap(moveMapId);// 이동할 인터서버
		final L1InterServer current	= _owner.getMap().getInter();// 현재 인터서버
		// 같은 위치
		if (after == current) {
			return null;
		}
		// 이동할 위치가 인터서버가 아니고 현재 인터서버
		if (after == null && current != null) {
			return L1InterServer.LEAVE;
		}
		// 서로 다른 인터서버
		if (after != null && current != null && after != current) {
			return after;
		}
		// 이동할 위치가 인터서버이고 현재 인터서버가 아니라면 입장을 위해 인터서버 객체 반환
		if (after != null && current == null) {
			return after;
		}
		return null;
	}
    
    /**
     * C_Teleport 호출
     * @param x
     * @param y
     * @param mapId
     * @param heading
     * @param effectable
     */
    public void c_start(final int x, final int y, final short mapId, final int heading, final boolean effectable){
    	start(x, y, mapId, heading, 169, effectable, true);
    }
    
    /**
     * 텔레포트 일반
     * @param loc
     * @param heading
     * @param effectable
     */
    public void start(final L1Location loc, final int heading, final boolean effectable){
    	start(loc.getX(), loc.getY(), (short)loc.getMapId(), heading, 169, effectable, false);
    }
    
    /**
     * 텔레포트 일반
     * @param x
     * @param y
     * @param mapId
     * @param heading
     * @param effectable
     */
    public void start(final int x, final int y, final short mapId, final int heading, final boolean effectable){
    	start(x, y, mapId, heading, 169, effectable, false);
    }
    
    /**
     * 인터서버 텔레포트
     * @param x
     * @param y
     * @param mapId
     * @param heading
     * @param effectable
     * @param inter 이팩트번호 true: 12261, false: 169
     */
    public void start(final int x, final int y, final short mapId, final int heading, final boolean effectable, final boolean inter){
    	start(x, y, mapId, heading, inter ? 12261 : 169, effectable, false);
    }
	
    /**
     * 텔레포트 시작 설정
     * @param x
     * @param y
     * @param mapId
     * @param heading
     * @param effectId(텔레포트 이팩트번호)
     * @param effectable(텔레포트 이팩트 출력 여부)
     * @param requestPacket(true: S_Teleport -> C_Teleport 호출, false: 바로 진행) 
     */
    public void start(final int x, final int y, final short mapId, final int heading, final int effectId, final boolean effectable, final boolean requestPacket){
    	try {
    		if (_isTeleport || _owner.isNotTeleport() || _owner.isPrivateShop()) {
    			_owner.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
    			return;
    		}
 			if (effectable && effectId > 0) {
 				_owner.broadcastPacketWithMe(getEffect(effectId));
 			}
 			_isTeleport		= true;
 			setTeleportLoc(x, y, mapId, heading);
 			if (_teleportMapId != _owner.getMapId()) {
 				final L1InterServer inter	= getInterServer(_teleportMapId);// 인터서버 체크
				if (inter != null && isRegistInterServer(inter)) {
					return;// 인터서버 시작
				}
 			}
			if (requestPacket) {// C_Teleport 에서 처리
				_owner.sendPackets(S_Teleport.REQUEST_SUMMON);
				return;
			}
			Runnable r = () -> {
				end(effectable);
			};
			GeneralThreadPool.getInstance().schedule(r, Config.SPEED.TELEPORT_DELAY_SYNCHRONIZED);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
    }
    
    /**
     * 포털 진입
     * S_Portal -> C_Teleport
     */
    public void initPortal(final int x, final int y, final short mapId, final int head){
    	if (_isTeleport || _owner.isNotTeleport()) {
    		return;
    	}
    	_owner.skillStatus.setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
		_isTeleport	= true;
		setTeleportLoc(x, y, mapId, head);
    	_owner.sendPackets(new S_Portal(_owner), true);
    }
    
    /**
     * 이동하는 스킬
     * @param target
     * @param x
     * @param y
     * @param mapid
     * @param heading
     * @param skillId
     */
    public void startMoveSkill(final L1Character target, final int x, final int y, final byte heading, final int skillId, final L1Magic magic) {
		try {
			if (_isTeleport || _owner.isNotTeleport() || _owner.isPrivateShop()) {
				_owner.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				return;
			}
			moveSkillEffect(skillId, true);
			_isTeleport		= true;
			setTeleportLoc(x, y, _owner.getMapId(), heading);
			
 			Runnable r = () -> {
 				endMoveSkill(target, skillId, magic);
 			};
 			GeneralThreadPool.getInstance().schedule(r, Config.SPEED.TELEPORT_DELAY_SYNCHRONIZED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    void endMoveSkill(final L1Character target, final int skillId, final L1Magic magic){
    	try {
			endInit(_teleportX, _teleportY, _teleportMapId, _teleportHead);
			endFinish();
			if (target != null) {
				if (skillId == L1SkillId.PANTERA) {
					pantera(target, skillId, magic);
				} else if (skillId == L1SkillId.SHADOW_STEP) {
					shadowStep(target, skillId, magic);
				}
				target.onAction(_owner);// 공격
			}
			moveSkillEffect(skillId, false);
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		_isTeleport		= false;
    	}
    }
    
    /**
     * 이동 스킬 이팩트
     * @param skillId
     * @param init
     */
    void moveSkillEffect(int skillId, boolean init){
    	switch(skillId){
    	case L1SkillId.CAL_CLAN_ADVANCE:
    		_owner.broadcastPacketWithMe(getEffect(init ? 19583 : 19585));
    		break;
    	case L1SkillId.VISION_TELEPORT:	
    		_owner.broadcastPacketWithMe(getEffect(init ? 20105 : 20107));
    		break;
    	default:
    		break;
    	}
    }
    
    /**
     * 판테라 스킬 시전
     * @param cha
     */
    void pantera(final L1Character cha, final int skillId, final L1Magic magic){
		final boolean shock = _owner.isPassiveStatus(L1PassiveId.PANTERA_SHOCK);
		_owner.broadcastPacketWithMe(getEffect(shock ? 18606 : 18503));// 이동 이팩트
		if (cha.getRegion() == L1RegionStatus.SAFETY) {
			return;
		}
		if (magic.calcProbabilityMagic(skillId)) {// 성공여부 판별
			int time = CommonUtil.randomIntChoice(shock ? L1SkillInfo.PANTERA_SHOCK_ARRAY : L1SkillInfo.PANTERA_ARRAY);
			if (_owner.ability.getStrangeTimeIncrease() > 0) {
				time += _owner.ability.getStrangeTimeIncrease();
			}
			if (cha.ability.getStrangeTimeDecrease() > 0) {
				time -= cha.ability.getStrangeTimeDecrease();
			}
			if (time <= 0) {
				return;
			}
			L1EffectSpawn.getInstance().spawnEffect(shock ? 81056 : 81055, time, cha.getX(), cha.getY(), cha.getMapId());
			cha.skillStatus.setSkillEffect(shock ? L1SkillId.STATUS_PANTERA_SHOCK : L1SkillId.PANTERA, time);
			if (cha instanceof L1PcInstance) {
				L1PcInstance target = (L1PcInstance) cha;
				target.sendPackets(S_Paralysis.STURN_ON);
				target.sendPackets(new S_SpellBuffNoti(target, shock ? L1SkillId.STATUS_PANTERA_SHOCK : L1SkillId.PANTERA, true, time / 1000), true);
				target.send_effect(shock ? 18608 : 18505);// 적중 이팩트
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
				L1NpcInstance targetNpc = (L1NpcInstance) cha;
				targetNpc.setParalyzed(true);
				targetNpc.broadcastPacket(new S_Effect(targetNpc.getId(), shock ? 18608 : 18505), true);// 적중 이팩트
			}
		}
	}
	
    /**
     * 쉐도우 스텝 스킬 시전
     * @param cha
     */
	void shadowStep(final L1Character cha, final int skillId, final L1Magic magic){
		_owner.broadcastPacketWithMe(getEffect(18947));// 이동 이팩트
		if (cha.getRegion() == L1RegionStatus.SAFETY) {
			return;
		}
		if (magic.calcProbabilityMagic(skillId)) {// 성공여부 판별
			final boolean chaser = _owner.isPassiveStatus(L1PassiveId.SHADOW_STEP_CHASER);
			int time = CommonUtil.randomIntChoice(chaser ? L1SkillInfo.SHADOW_STEP_CHASER_ARRAY : L1SkillInfo.SHADOW_STEP_ARRAY);
			L1Ability ability = _owner.ability;
			if (ability.getStrangeTimeIncrease() > 0) {
				time += ability.getStrangeTimeIncrease();
			}
			if (cha.ability.getStrangeTimeDecrease() > 0) {
				time -= cha.ability.getStrangeTimeDecrease();
			}
			if (chaser && ability.getReturnLockDuraion() > 0) {
				time += ability.getReturnLockDuraion() * 1000;
			}
			if (time <= 0) {
				return;
			}
			L1EffectSpawn.getInstance().spawnEffect(chaser ? 9419 : 9415, time, cha.getX(),cha.getY(), cha.getMapId());
			cha.skillStatus.setSkillEffect(chaser ? L1SkillId.SHADOW_STEP_CHASER : skillId, time);
			if (cha instanceof L1PcInstance) {
				L1PcInstance target = (L1PcInstance) cha;
				target.sendPackets(S_Paralysis.GRIP_ON);
				target.sendPackets(new S_SpellBuffNoti(target, chaser ? L1SkillId.SHADOW_STEP_CHASER : skillId, true, time / 1000), true);
				target.send_effect(18949);// 적중 이팩트
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
				L1NpcInstance targetNpc = (L1NpcInstance) cha;
				targetNpc.setHold(true);
				targetNpc.broadcastPacket(new S_Effect(targetNpc.getId(), 18949), true);// 적중 이팩트
			}
		}
	}
    
    /**
     * 타겟의 정면 좌표로 텔레포트
     * @param target
     * @param distance
     */
    public void teleportToTargetFront(final L1Character target, final int distance) {
		int locX = target.getX(), locY = target.getY(), heading = target.moveState.getHeading();
		L1Map map	= target.getMap();
		short mapId	= target.getMapId();
		switch (heading) {
		case 1:locX += distance;locY -= distance;break;
		case 2:locX += distance;break;
		case 3:locX += distance;locY += distance;break;
		case 4:locY += distance;break;
		case 5:locX -= distance;locY += distance;break;
		case 6:locX -= distance;break;
		case 7:locX -= distance;locY -= distance;break;
		case 0:locY -= distance;break;
		default:break;
		}
		if (map.isPassable(locX, locY)) {
			start(locX, locY, mapId, _owner.moveState.getHeading(), true);
		}
	}

    /**
     * 같은 맵의 랜덤한 위치로 텔레포트
     * @param effectable
     * @param range
     */
	public void randomTeleport(final boolean effectable, final int range) {
		start(_owner.getLocation().randomLocation(range, true), _owner.moveState.getHeading(), effectable);
	}
    
    /**
     * 텔레포트 좌표 및 오브젝트 반영
     * @param x
     * @param y
     * @param mapId
     * @param head
     */
    void endInit(final int x, final int y, final short mapId, final int head){
    	_owner.getMap().setPassable(_owner.getLocation(), true);// 이동 전 좌표 통과 가능
    	_world.moveVisibleObject(_owner, mapId, x, y);
    	_owner.setLocation(x, y, mapId);// 좌표 변경 완료
    	_owner.moveState.setHeading(head);
    	_owner.sendPackets(S_WorldPut.get(_owner.getMap()));
		_owner.getMap().setPassable(_owner.getLocation(), false);// 이동 후 좌표 통과 불가
		if (_owner.isReserveGhost()) {
			_owner.endGhost();
		}
		
		// 오브젝트 처리
		_owner.broadcastRemoveAllKnownObjects();
		_owner.removeAllKnownObjects();
		_owner.sendPackets(new S_PCObject(_owner), true);
		_owner.updateObject();
		for (L1PcInstance visiblePc : _world.getVisiblePlayer(_owner)) {
			visiblePc.removeObjects(_owner, true);
			_owner.onPerceive(visiblePc);
		}
		
		_owner.sendVisualEffectAtTeleport();
		if (_owner.isPinkName()) {
			_owner.sendPackets(new S_PinkName(_owner.getId(), _owner.getPinkNameTime()), true);
		}
		for (L1PcInstance each : _world.getVisiblePlayer(_owner)) {  
			if (!each.isPinkName()) {
				continue;
			}
			_owner.sendPackets(new S_PinkName(each.getId(), each.getPinkNameTime()), true);
		}
	}
	
    /**
     * 텔레포트 완료 마무리 처리
     */
	void endFinish(){
		_owner.sendPackets(new S_Weather(_world.getWeather()), true);
		_owner.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
		_owner.siegeRegionCheck(true);
		if (Config.ETC.FREE_PVP_REGION_ENABLE) {
			FreePVPRegionTable.sendPacket(_owner);
		}
		// 이동한 지역이 플레이 서포트 지역인지 검증
		if (_owner.getConfig().isPlaySupport() && !PlaySupportTable.isSupportMap(_owner.getConfig().getPlaySupportType(), _owner.getMap().getBaseMapId())) {
			_owner.getConfig().finishPlaySupport();
		}
	}
	
	/**
	 * 텔레포트 동반자 처리
	 * @param newMapId
	 */
	void endCompanion(short newMapId) {
		Map<Integer, L1NpcInstance> pet_list = _owner.getPetList();
		if (!pet_list.isEmpty()) {
			if (!_owner.isGhost() && _owner.getMap().isTakePets()) {
				for (L1NpcInstance petNpc : pet_list.values()) {
					teleportNpc(petNpc, _owner.getLocation().randomLocation(3, false), petNpc.moveState.getHeading());
					if (petNpc instanceof L1SummonInstance) {
						_owner.sendPackets(new S_SummonObject((L1SummonInstance) petNpc), true);
					} else if (petNpc instanceof L1PetInstance) {
						_owner.sendPackets(new S_CompanionObject((L1PetInstance) petNpc), true);
					}
				}
			} else {
				// 애완동물을 월드 MAP상으로부터 지운다
				L1PetInstance pet		= null;
				L1SummonInstance summon	= null;
				for (L1NpcInstance petNpc : pet_list.values()) {
					if (petNpc instanceof L1PetInstance) {
						pet = (L1PetInstance) petNpc;
						_owner.getPetList().remove(pet.getId());
						pet.deleteMe();
					} else if (petNpc instanceof L1SummonInstance) {
						summon = (L1SummonInstance) petNpc;
						for (L1PcInstance visiblePc : _world.getVisiblePlayer(summon)) {
							visiblePc.sendPackets(new S_SummonObject(summon, false), true);
						}
					}
				}
			}
		}
		
		L1DollInstance doll = _owner.getDoll();
		if (doll != null) {
			teleportNpc(doll, _owner.getLocation().randomLocation(3, false), doll.moveState.getHeading());
		}
	}
    
	/**
	 * 텔레포트 완료
	 * @param effectable
	 */
    public void end(final boolean effectable){
		try {
			if (_owner.isPrivateShop()) {
				_owner.denals_disconnect(String.format("[L1Teleport] PERSONAL_SHOP_USER : NAME(%s)", _owner.getName()));
				return;
			}
			short beforeMapId	= _owner.getMapId();
			short newMapId		= _teleportMapId;
			String beforeScript	= _owner.getMap().getScript();
			// 현재 맵과 이동할 맵이 다름
			if (newMapId != beforeMapId) {
				otherMapCheckInit(newMapId, beforeMapId);
			}
			
			int newX = _teleportX, newY = _teleportY, newHead = _teleportHead;
			L1Map map = _worldMap.getMap(newMapId);
			if (!map.isInMap(newX, newY) && !_owner.isGm()) {
				newX		= _owner.getX();
				newY		= _owner.getY();
				newMapId	= beforeMapId;
			}
			
			// TODO 이동 좌표 설정
			endInit(newX, newY, newMapId, newHead);
			
			// 스크립트 호출 맵
			String afterScript = _owner.getMap().getScript();
			if (afterScript != null && !afterScript.equals(beforeScript)) {
				sendScriptMap(newMapId);
			}
			
			endCompanion(newMapId);
			endFinish();
			if (_owner.isFishing()) {// 낚시중일경우
				_owner.finishFishing();
			}
			if (newMapId != beforeMapId) {
				otherMapCheckAfter();
			}
	        if (effectable) {
	        	_owner.sendPackets(getEffect(18339));// 도착후 텔레포트 이팩트 출력
	        }
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			_isTeleport	= false;
			if (_owner.getConfig().getFindMerchantId() > 0) {
				showPersonalShopHtml();
			}
		}
    }
    
    /**
     * 인스턴스 던전 맵 번호
     */
    static final List<Integer> INSTANCE_DUNGEON_MAPS = Arrays.asList(new Integer[] {
    		1936, 2936, 2600, 2699, 3000, 3050, 736, 15601
    });
    
    /**
	 * 다른 맵으로 이동시 처리
	 * 이동 전 처리
	 */
    void otherMapCheckInit(short newMapId, short beforeMapId) {
    	setMapBuff(newMapId, beforeMapId);
		if (beforeMapId == 5557 && newMapId != 5557) {// 무한대전 아인하사드 보상
			sendUltimateCoinEinhasad();
		}
    }
	
	/**
	 * 다른 맵으로 이동시 처리
	 * 이동 후 처리
	 */
	void otherMapCheckAfter(){
		final int baseMapId	= _owner.getMap().getBaseMapId();
		if (_owner.getDungoenTimer() != null) {
			_owner.getDungoenTimer().sendTimerPacket();
		}
		if (baseMapId != 1005 && _owner.skillStatus.hasSkillEffect(L1SkillId.BUFF_CRAY)) {// 크레이 버프 안타지역이 아닐시 삭제 처리
			_owner.skillStatus.removeSkillEffect(L1SkillId.BUFF_CRAY);
		}
		if (baseMapId != 1011 && _owner.skillStatus.hasSkillEffect(L1SkillId.BUFF_SAEL)) {// 사엘버프 파푸지역이 아닐시 삭제 처리
			_owner.skillStatus.removeSkillEffect(L1SkillId.BUFF_SAEL);
			_owner.skillStatus.removeSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH);
			_owner.sendPackets(new S_SkillIconBlessOfEva(_owner.getId(), 1), true);
		}
		if (baseMapId != 1017 && _owner.skillStatus.hasSkillEffect(L1SkillId.BUFF_GUNTER)) {// 군터버프 린드지역이 아닐시 삭제 처리
			_owner.skillStatus.removeSkillEffect(L1SkillId.BUFF_GUNTER);
		}
		if (baseMapId != 1161 && _owner.skillStatus.hasSkillEffect(L1SkillId.BUFF_BALRACAS)) {// 데스나이트버프 발라지역 아닐시 삭제 처리
			_owner.skillStatus.removeSkillEffect(L1SkillId.BUFF_BALRACAS);
		}
		if (baseMapId != 1191) {// 자켄버프 할파스지역 아닐시 삭제 처리
			if (_owner.skillStatus.hasSkillEffect(L1SkillId.BUFF_ZAKEN)) {
				_owner.skillStatus.removeSkillEffect(L1SkillId.BUFF_ZAKEN);
			} else if (_owner.skillStatus.hasSkillEffect(L1SkillId.BUFF_ZAKEN_HALPAS)) {
				_owner.skillStatus.removeSkillEffect(L1SkillId.BUFF_ZAKEN_HALPAS);
			}
		}
		if (!INSTANCE_DUNGEON_MAPS.contains(baseMapId)) {
			removeInstanceDungeonItem();// 인스턴스 던전 아이템삭제
		}
		if (_owner.getClan() != null && _owner.getClan().getEinhasadBlessBuff() != 0) {
			_owner.skillStatus.einhasadClanBuff();// 혈맹의 축복
		}
		if (L1WhaleBonusTimer.MAPS.contains(baseMapId)) {// 굶주린 고래상어 보너스 맵
			enterWhaleBonusMap(baseMapId);
		}
		if (_owner.isPCCafe()) {
			_owner.getConfig().get_free_buff_shield().enable_golden_buff_info_map();
		}
	}
	
	/**
	 * 새로운 맵으로 이동 시 버프관련 처리
	 * @param moveMapId
	 * @param beforeMapId
	 */
	void setMapBuff(final int moveMapId, final int beforeMapId){
		if (_owner.getColoTeam() != 0) {
			infinityTeamSetting(moveMapId);// 배틀 콜로세움 팀
		}
		ruunInvitePaperMap(moveMapId);
		dominationTeleportRingMap(moveMapId);
		questBuffMap(moveMapId);
		if (_owner.getInventory().getCollection().isSasinGraceStatus()) {
			sasinGraceMap(moveMapId, beforeMapId);
		}
	}
	
	/**
	 * 배틀 콜로세움 팀 UI 출력
	 * @param moveMapId
	 */
	void infinityTeamSetting(final int moveMapId){
		boolean before	= moveMapId == 750;
		boolean after	= _owner.getMapId() == 750;
		if (before && !after) {
			_owner.sendPackets(new S_InfinityBattleEnterMapNoti(_owner.getColoTeam()), true);
		} else if(!before && after) {
			_owner.sendPackets(S_InfinityBattleLeaveMapNoti.LEAVE);	
		}
	}
	
	/**
	 * 루운의 초대장 icon 출력 체크
	 * @param moveMapId
	 */
	void ruunInvitePaperMap(final int moveMapId){
		if (!_owner._isRuunPaper) {
			return;
		}
		boolean after	= Ruun.isRuunMap((short)moveMapId);
		boolean before	= _owner.getMap().isRuunCastleZone();
		if (after && !before) {
			_owner.sendPackets(S_PacketBox.RUUN_INVITE_ON);// 루운의 초대장
		} else if (!after && before) {
			_owner.sendPackets(S_PacketBox.RUUN_INVITE_OFF);// 루운의 초대장
		}
	}
	
	/**
	 * 순간이동 지배 반지 icon 출력 체크
	 * @param moveMapId
	 */
	void dominationTeleportRingMap(final int moveMapId){
		if (!_owner.getConfig()._dominationTeleportRing) {
			return;
		}
		boolean after	= MapsTable.isDominationTeleports(moveMapId);
		boolean before	= _owner.getMap().isDominationTeleport();
		if (after && !before) {
			_owner.sendPackets(S_SpellBuffNoti.DOMINATION_TELEPORT_ON);
		} else if (!after && before) {
			_owner.sendPackets(S_SpellBuffNoti.DOMINATION_TELEPORT_OFF);
		}
	}
	
	/**
	 * 사신의 가호 icon 출력 체크
	 * @param moveMapId
	 * @param beforeMapId
	 */
	void sasinGraceMap(final int moveMapId, final int beforeMapId) {
		if ((moveMapId >= 101 && moveMapId <= 111) && !(beforeMapId >= 101 && beforeMapId <= 111)) {
			_owner.sendPackets(S_SpellBuffNoti.SASIN_GRACE_ON);
		} else if ((beforeMapId >= 101 && beforeMapId <= 111) && !(moveMapId >= 101 && moveMapId <= 111)) {
			_owner.sendPackets(S_SpellBuffNoti.SASIN_GRACE_OFF);
		}
	}
	
	/**
	 * 퀘스트 맵 buff 체크
	 * @param moveMapId
	 */
	void questBuffMap(final int moveMapId){
		if (_owner.getLevel() > 92) {
			return;
		}
		boolean afterGerad	= MapsTable.isGeradBuffMap(moveMapId);
		boolean beforeGerad	= _owner.getMap().isGeradBuffZone();
		boolean afterGrow	= MapsTable.isGrowBuffMap(moveMapId);
		boolean beforeGrow	= _owner.getMap().isGrowBuffZone();
		
		// 게라드 버프
		if (_owner.getLevel() <= 88 && afterGerad && !beforeGerad) {// 입장
			if (_owner.getLevel() > 79 && !(moveMapId >= 7531 && moveMapId <= 7534)) {
				return;
			}
			setGeradBuff(true);
		} else if (!afterGerad && beforeGerad) {// 탈출
			setGeradBuff(false);
		}
		// 성장 버프
		if (afterGrow && !beforeGrow) {// 입장
			setGrowBuff(true);
		} else if (!afterGrow && beforeGrow) {// 탈출
			setGrowBuff(false);
		}
	}
	
	/**
	 * 게라드 버프
	 * @param flag
	 */
	public void setGeradBuff(boolean flag){
		if (geradBuff == flag) {
			return;
		}
		geradBuff	= flag;
		_owner.sendPackets(new S_SpellBuffNoti(_owner, L1SkillId.GERAD_BUFF, flag, -1), true);
	}
	
	/**
	 * 성장 버프
	 * @param flag
	 */
	public void setGrowBuff(boolean flag){
		if (growBuff == flag) {
			return;
		}
		growBuff	= flag;
		_owner.add_exp_boosting_ratio(flag ? 30 : -30);
		_owner.sendPackets(new S_SpellBuffNoti(_owner, L1SkillId.GROW_BUFF, flag, -1), true);
	}
	
	/**
	 * 맵 변화 설정
	 * @param moveMapId
	 */
	void sendScriptMap(final short moveMapId){
		if (moveMapId == 54) {// 기란감옥 2층
			scriptKikamSecondFloor();
		} else if (moveMapId >= 101 && moveMapId <= 110) {// 균열의 오만의 탑
			if (GameServerSetting.OMAN_CRACK <= 0 || moveMapId != GameServerSetting.OMAN_CRACK) {
				return;
			}
			_owner.sendPackets(new S_SceneNoti(Integer.toString((int)moveMapId), true, false), true);
		} else if (moveMapId >= 12852 && moveMapId <= 12862) {
			wakeDominationTower(moveMapId);
		}
	}
	
	/**
	 * 기란 감옥 2층 맵변화
	 */
	void scriptKikamSecondFloor(){
		if (BossSpawn._taros) {// 타로스
			_owner.sendPackets(new S_SceneNoti(false, S_SceneNoti.ScriptName.MAP54_DISABLE.toString(), System.currentTimeMillis() / 1000, 0, 0), true);// 리셋
			_owner.sendPackets(S_SceneNoti.KIKAM_FULL);// 불끄기
		}
		if (GameServerSetting.CLONE_WAR) {// 거울전쟁
			long script_start_time = System.currentTimeMillis() / 1000;
			_owner.sendPackets(new S_SceneNoti(false, script_start_time, 540000, 0, 0), true);
			_owner.sendPackets(new S_SceneNoti(true, script_start_time, 540001, 0, 0), true);
			_owner.sendPackets(new S_SceneNoti(true, script_start_time, 540004, 0, 0), true);
		}
	}
	
	/**
	 * 지배의 탑 각성 맵변화
	 * @param moveMapId
	 */
	void wakeDominationTower(final short moveMapId){
		S_SceneNoti scene = null;
		if (moveMapId == 12852 && BossSpawn._wakeZenith) {
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128520000, 0, 0);
		} else if (moveMapId == 12853 && BossSpawn._wakeSeia) {
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128530000, 0, 0);
		} else if (moveMapId == 12854 && BossSpawn._wakeBampha){
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128540000, 0, 0);
		} else if (moveMapId == 12855 && BossSpawn._wakeZombie){
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128550000, 0, 0);
		} else if (moveMapId == 12856 && BossSpawn._wakeCouger){
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128560000, 0, 0);
		} else if (moveMapId == 12857 && BossSpawn._wakeMumy) {
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128570000, 0, 0);
		} else if (moveMapId == 12858 && BossSpawn._wakeIris) {
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128580000, 0, 0);
		} else if (moveMapId == 12859 && BossSpawn._wakeKnight) {
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128590000, 0, 0);
		} else if (moveMapId == 12860 && BossSpawn._wakeRich) {
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128600000, 0, 0);
		} else if (moveMapId == 12861 && BossSpawn._wakeUgu) {
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128610000, 0, 0);
		} else if (moveMapId == 12862 && BossSpawn._wakeSasin) {
			scene = new S_SceneNoti(true, System.currentTimeMillis() / 1000, 128620000, 0, 0);
		}
		if (scene == null) {
			return;
		}
		_owner.sendPackets(scene, true);
	}
	
	/**
	 * 서먼 및 펫 텔레포트
	 * @param npc
	 * @param loc
	 * @param head
	 */
	void teleportNpc(final L1NpcInstance npc, final L1Location loc, final int head) {
		teleportNpc(npc, loc.getX(), loc.getY(), (short)loc.getMapId(), head);
	}
	
	/**
	 * 서먼 및 펫 텔레포트
	 * @param npc
	 * @param x
	 * @param y
	 * @param map
	 * @param head
	 */
	void teleportNpc(final L1NpcInstance npc, final int x, final int y, final short map, final int head) {
		if (npc == null) {
			return;
		}
		for (L1PcInstance visiblePc : _world.getVisiblePlayer(npc)) {
			visiblePc.removeObjects(npc, true);
		}
		_world.moveVisibleObject(npc, map, x, y);
		_worldMap.getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), true);
		npc.setX(x);
		npc.setY(y);
		npc.setMap(map);
		npc.moveState.setHeading(head);
		npc.allTargetClear();
		_worldMap.getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), false);
	}
	
	/**
	 * 인스턴스 던전 전용 아이템 제거
	 */
	void removeInstanceDungeonItem() {
		for (L1ItemInstance item : _owner.getInventory().getItems()) {
			if (item == null) {
				continue;
			}
			if (L1ItemId.isInstanceDungeonItem(item.getItemId())) {
				_owner.getInventory().removeItem(item);
			}
		}
	}
	
	/**
	 * 상인 찾기 html 출력
	 */
	void showPersonalShopHtml(){
		if (_owner.isGm())
			_owner.sendPackets(new S_SystemMessage("Dialog " + "usershop"), true);													
		
		_owner.sendPackets(new S_NPCTalkReturn(_owner.getConfig().getFindMerchantId(), "usershop"), true);
		_owner.getConfig().setFindMerchantId(0);
	}
	
	/**
	 * 무한대전 종료 아인하사드 보상
	 */
	void sendUltimateCoinEinhasad(){
		final int tokenCount = _owner.getInventory().checkItemCount(L1ItemId.ULTIMATE_BRAVE_COIN);
		if (tokenCount <= 0) {
			return;
		}
		_owner.einGetExcute(tokenCount * 100);
		_owner.getInventory().consumeItem(L1ItemId.ULTIMATE_BRAVE_COIN);
	}
	
	/**
	 * 굶주린 고래상어 보너스 맵 입장
	 * @param map_number
	 */
	void enterWhaleBonusMap(int map_number) {
		int second = map_number == 1601 ? Config.DUNGEON.WHALE_BOSS_ROOM_LIMIT_SECOND : Config.DUNGEON.WHALE_TREASURE_ROOM_LIMIT_SECOND;
		_owner.skillStatus.removeSkillEffect(L1SkillId.BUFF_PUFFER_FISH);
		_owner.sendPackets(new S_PacketBox(S_PacketBox.TIME_COUNT, second), true);
		GeneralThreadPool.getInstance().schedule(new L1WhaleBonusTimer(_owner, map_number), second * 1000);
	}
	
	/**
	 * 메모리 정리
	 */
	public void dispose(){
		if (_effects != null) {
			_effects.clear();
		}
	}
}


package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.datatables.ClanBlessBuffTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillTimer;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.party.PartyUISpellInfo;
import l1j.server.server.serverpackets.spell.S_AddSpellPassiveNoti;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;

/**
 * 캐릭터 스킬 관리 Handler
 * @author LinOffice
 */
public class L1SkillStatus {
	private final L1Character _owner;
	private L1PcInstance _player;
	
	private final Map<Integer, L1SkillTimer> _skillTimers;
	private List<Integer> _learnActives;// skillId
	private List<Integer> _learnPassives;// passiveId
	
	private Map<Integer, byte[]> _partyIconSkillBytes;
	private int _partyIconSkillBytesLenth;
	
	private ConcurrentHashMap<Integer, Boolean> _skillDelays;
	private ConcurrentHashMap<Integer, Long> _skillDelayDurations;
	private Timestamp _defaultSkillDelay, _firstSkillDelay, _secondSkillDelay, _thirdSkillDelay, _fourthSkillDelay, _fiveSkillDelay;
	private boolean _auraBuff;
	private boolean _blindHidingAssassin;
	private L1Character _expose_weakness_target;
	private L1PcInstance _chain_reaction_attacker;
	private L1ExpPotion _expPotion;
	
	L1SkillStatus(L1Character owner){
		_owner				= owner;
		_skillTimers		= new HashMap<>();
		_skillDelays		= new ConcurrentHashMap<>();
		if (_owner instanceof L1PcInstance) {
			_learnActives			= new ArrayList<>();
			_learnPassives			= new ArrayList<>();
			_skillDelayDurations	= new ConcurrentHashMap<>();
			if (_owner instanceof L1AiUserInstance == false) {
				_player					= (L1PcInstance) owner;
				_partyIconSkillBytes	= new HashMap<>();
				_expPotion				= new L1ExpPotion(_player);
			}
		}
	}
	
	/**
	 * PC의 파티 스킬 아이콘 바이트를 반환한다.
	 * @return HashMap<Integer, byte[]>
	 */
	public Map<Integer, byte[]> getPartyIconSkillBytes(){
		return _partyIconSkillBytes;
	}
	
	/**
	 * PC의 파티 스킬 아이콘 바이트의 전체 길이를 반환한다.
	 * @return int
	 */
	public int getPartyIconSkillBytesLength(){
		return _partyIconSkillBytesLenth;
	}
	
	/**
	 * 파티 스킬 아이콘 데이터 해제
	 */
	public void clearPartyIconSkillBytes(){
		if (_partyIconSkillBytes != null) {
			_partyIconSkillBytes.clear();
			_partyIconSkillBytes = null;
		}
		_partyIconSkillBytesLenth = 0;
	}
	
	/**
	 * PC의 파티에 스킬 상태를 알린다.
	 * @param skillId
	 * @param flag
	 */
	private void updatePartyIconSkillBytes(int skillId, boolean flag){
		if (flag) {
			byte[] bytes = PartyUISpellInfo.PARTY_ICON_SKILL_BYTES.get(skillId);
			_partyIconSkillBytes.put(skillId, bytes);
			_partyIconSkillBytesLenth += bytes.length;
		} else {
			byte[] bytes = _partyIconSkillBytes.remove(skillId);
			_partyIconSkillBytesLenth -= bytes.length;
		}
		if (_player.isInParty()) {
			_player.getParty().update(_player);
		}
	}
	
	/**
	 * 캐릭터에, 새롭게 스킬 효과를 추가한다.
	 * @param skillId
	 * @param timeMillis
	 */
	private void addSkillEffect(int skillId, long timeMillis) {
		L1SkillTimer timer = null;
		if (0 < timeMillis) {
			timer = new L1SkillTimer(_owner, skillId, timeMillis);
			timer.begin();
		}
		_skillTimers.put(skillId, timer);
		if (_player != null && L1SkillInfo.PARTY_ICON_LIST.contains(skillId) && !_partyIconSkillBytes.containsKey(skillId)) {
			updatePartyIconSkillBytes(skillId, true);
		}
	}

	/**
	 * 캐릭터에, 스킬 효과를 설정한다. <br>
	 * 중복 하는 스킬이 없는 경우는, 새롭게 스킬 효과를 추가한다. <br>
	 * 중복 하는 스킬이 있는 경우는, 나머지 효과 시간과 파라미터의 효과 시간의 긴 (분)편을 우선해 설정한다.
	 * 
	 * @param skillId 설정하는 효과의 스킬 ID.
	 * @param timeMillis 설정하는 효과의 지속 시간. 무한의 경우는 0.
	 */
	public void setSkillEffect(int skillId, long timeMillis) {
		if (hasSkillEffect(skillId)) {
			long remainingTimeMills = getSkillEffectTimeSec(skillId) * 1000;
			if (remainingTimeMills >= 0 && (remainingTimeMills < timeMillis || timeMillis == 0)) {
				killSkillEffectTimer(skillId);
				addSkillEffect(skillId, timeMillis);
			}
		} else {
			addSkillEffect(skillId, timeMillis);
		}
	}
	
	/**
	 * 캐릭터로부터, 스킬의 타이머를 재실행 한다
	 * 
	 * @param skillId 설정하는 효과의 스킬 ID.
	 */
	public void startSkillTimer(int skillId) {
		L1SkillTimer timer = _skillTimers.get(skillId);
		if (timer == null || timer.getRemainTime() <= 0) {
			return;
		}
		timer.on();
	}

	/**
	 * 캐릭터로부터, 스킬 효과를 삭제한다.
	 * 
	 * @param skillId 삭제하는 효과의 스킬 ID
	 */
	public void removeSkillEffect(int skillId) {
		L1SkillTimer timer = _skillTimers.remove(skillId);
		if (timer != null) {
			timer.end();
		}
		if (_player != null && L1SkillInfo.PARTY_ICON_LIST.contains(skillId) && _partyIconSkillBytes.containsKey(skillId)) {
			updatePartyIconSkillBytes(skillId, false);
		}
	}

	/**
	 * 캐릭터로부터, 스킬 효과의 타이머를 삭제한다. 스킬 효과는 삭제되지 않는다.
	 * 
	 * @param skillId 삭제하는 타이머의 스킬 ID
	 */
	public void killSkillEffectTimer(int skillId) {
		L1SkillTimer timer = _skillTimers.remove(skillId);
		if (timer != null) {
			timer.kill();
		}
		if (_player != null && L1SkillInfo.PARTY_ICON_LIST.contains(skillId) && _partyIconSkillBytes.containsKey(skillId)) {
			updatePartyIconSkillBytes(skillId, false);
		}
	}
	
	/**
	 * 캐릭터로부터, 스킬 효과의 타이머를 멈춘다. 스킬 효과는 삭제되지 않는다.
	 * 
	 * @param skillId 삭제하는 타이머의 스킬 ID
	 */
	public void stopSkillEffectTimer(int skillId) {
		L1SkillTimer timer = _skillTimers.get(skillId);
		if (timer != null) {
			timer.kill();
		}
	}

	/**
	 * 캐릭터로부터, 모든 스킬 효과 타이머를 삭제한다. 스킬 효과는 삭제되지 않는다.
	 */
	public void clearSkillEffectTimer() {
		for (L1SkillTimer timer : _skillTimers.values()) {
			if (timer != null) {
				timer.kill();
			}
		}
		_skillTimers.clear();
	}

	/**
	 * 캐릭터에, 해당 스킬 효과가 걸려있는지 알려줌
	 * 
	 * @param skillId 스킬 ID
	 * @return 마법 효과가 있으면 true, 없으면 false.
	 */
	public boolean hasSkillEffect(int skillId) {
		return _skillTimers.containsKey(skillId);
	}
	
	/**
	 * 캐릭터에, 해당 스킬 효과가 걸려있는지 알려줌
	 * 배열로 받은 스킬아이디 전체 적용 여부 체크
	 * @param skillIds
	 * @return boolean
	 */
	public boolean hasSkillEffect(int[] skillIds) {
		for (int id : skillIds) {
			if (!_skillTimers.containsKey(id)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 캐릭터에, 해당 스킬 효과가 걸려있는지 알려줌
	 * 배열로 받은 스킬아이디 중 하나라도 적용 여부 체크
	 * @param skillIds
	 * @return boolean
	 */
	public boolean hasSkillEffectOne(int[] skillIds) {
		for (int id : skillIds) {
			if (_skillTimers.containsKey(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 캐릭터의 스킬 효과의 지속 시간을 돌려준다.
	 * 
	 * @param skillId 조사하는 효과의 스킬 ID
	 * @return 스킬 효과의 남은 시간(초). 스킬이 걸리지 않은가 효과 시간이 무한의 경우,-1.
	 */
	public int getSkillEffectTimeSec(int skillId) {
		L1SkillTimer timer = _skillTimers.get(skillId);
		if (timer == null) {
			return -1;
		}
		return timer.getRemainTime();
	}
	
	/**
	 * 캐릭터의 저장할 스킬들을 돌려준다.
	 *
	 * @return ArrayList<L1SkillTimer>
	 */
	public ArrayList<L1SkillTimer> getSaveSkillTimerList(){
		ArrayList<L1SkillTimer> skills = new ArrayList<L1SkillTimer>();
		for (L1SkillTimer timer : _skillTimers.values()) {
			if (timer == null || !L1SkillInfo.SAVE_BUFF_LIST.contains(timer.getSkillId()) || timer.getRemainTime() <= 0) {
				continue;
			}
			skills.add(timer);
		}
		return skills;
	}

	/**
	 * 캐릭터의 스킬 딜레이 상태
	 * @param delayGroupId
	 * @return boolean
	 */
	public boolean isSkillDelay(int delayGroupId) {
		Boolean flag = _skillDelays.get(delayGroupId);
		return flag == null ? false : flag;
	}
	public void setSkillDelay(int delayGroupId, boolean flag) {
		_skillDelays.put(delayGroupId, flag);
	}
	
	/**
	 * 캐릭터의 스킬 딜레이가 완료되는 시간
	 * @param delayGroupId
	 * @return duration
	 */
	public long getSkillDelayDuration(int delayGroupId) {
		Long time = _skillDelayDurations.get(delayGroupId);
		return time == null ? 0 : time;
	}
	public void setSkillDelayDuration(int delayGroupId, long duration) {
		_skillDelayDurations.put(delayGroupId, duration);
	}
	
	/**
	 * 캐릭터로부터, 모든 스킬 딜레이를 제거한다.
	 */
	public void clearSkillDelay() {
		_skillDelays.clear();
		if (_skillDelayDurations != null) {
			_skillDelayDurations.clear();
		}
	}
	
	public Timestamp getDefaultSkillDelay() {
    	return _defaultSkillDelay;
    }
    public void setDefaultSkillDelay(Timestamp t) {
    	_defaultSkillDelay = t;
    }
    
    public Timestamp getFirstSkillDelay() {
    	return _firstSkillDelay;
    }
    public void setFirstSkillDelay(Timestamp t) {
    	_firstSkillDelay = t;
    }
    
    public Timestamp getSecondSkillDelay() {
    	return _secondSkillDelay;
    }
    public void setSecondSkillDelay(Timestamp t) {
    	_secondSkillDelay = t;
    }
	
    public Timestamp getThirdSkillDelay() {
    	return _thirdSkillDelay;
    }
    public void setThirdSkillDelay(Timestamp t) {
    	_thirdSkillDelay = t;
    }
    
    public Timestamp getFourthSkillDelay() {
    	return _fourthSkillDelay;
    }
    public void setFourthSkillDelay(Timestamp t) {
    	_fourthSkillDelay = t;
    }
    
    public Timestamp getFiveSkillDelay() {
    	return _fiveSkillDelay;
    }
    public void setFiveSkillDelay(Timestamp t) {
    	_fiveSkillDelay = t;
    }
    
    /**
     * 습득한 엑티브 스킬
     * @return List<Integer>
     */
    public List<Integer> getLearnActives(){
		return _learnActives;
	}

	/**
	 * 엑티브 스킬 습득
	 * @param activeList
	 */
	public void addLearnActives(List<Integer> activeList) {
		_learnActives.addAll(activeList);
	}
	
	/**
	 * 액티브 스킬 습득
	 * @param skillId
	 * @return boolean
	 */
	public boolean addLearnActive(int skillId) {
		if (_learnActives.contains(skillId)) {
			return false;
		}
		_learnActives.add(skillId);
		return true;
	}
	
	/**
	 * 액티브 스킬 습득 여부
	 * @param skillId
	 * @param itemCheck
	 * @return boolean
	 */
	public boolean isLearnActive(int skillId, boolean isNotItem) {
		if (!isNotItem && isItemSkill(skillId)) {
			return true;
		}
		return _learnActives.contains(skillId);
	}
	
	private boolean isItemSkill(int skillId) {
		L1ItemInstance helmet = _owner.getInventory().getEquippedHelmet();
		if ((skillId == L1SkillId.PHYSICAL_ENCHANT_DEX || skillId == L1SkillId.HASTE) && helmet != null && helmet.getItemId() == 20013) {
			return true;
		}
		if ((skillId == L1SkillId.HEAL || skillId == L1SkillId.EXTRA_HEAL) && helmet != null && helmet.getItemId() == 20014) {
			return true;
		}
		if ((skillId == L1SkillId.DETECTION || skillId == L1SkillId.PHYSICAL_ENCHANT_STR || skillId == L1SkillId.ENCHANT_WEAPON) && helmet != null && helmet.getItemId() == 20015) {
			return true;
		}
		if (skillId == L1SkillId.HASTE && helmet != null && helmet.getItemId() == 20008) {
			return true;
		}
		return false;
	}
	
	/**
	 * 액티브 스킬 습득 제거
	 * @param skillId
	 */
	public void deleteLearnActive(int skillId) {
		_learnActives.remove((Integer)skillId);
	}
	
	/**
     * 습득한 패시브 스킬
     * @return List<Integer>
     */
	public List<Integer> getLearnPassives(){
		return _learnPassives;
	}
	
	/**
	 * 패시브 스킬 습득
	 * @param passiveList
	 */
	public void addLearnPassives(List<Integer> passiveList) {
		_learnPassives.addAll(passiveList);
	}
	
	/**
	 * 패시브 스킬 습득
	 * @param passiveId
	 * @return boolean
	 */
	public boolean addLearnPassive(int passiveId) {
		if (_learnPassives.contains(passiveId)) {
			return false;
		}
		_learnPassives.add(passiveId);
		return true;
	}
	
	/**
	 * 패시브 스킬 습득 여부
	 * @param passiveId
	 * @return boolean
	 */
	public boolean isLearnPassive(int passiveId) {
		return _learnPassives.contains(passiveId);
	}
	
	/**
	 * 패시브 스킬 습득 제거
	 * @param passiveId
	 */
	public void deleteLearnPassive(int passiveId) {
		_learnPassives.remove((Integer)passiveId);
	}
	
	/**
	 * 습득한 스킬 제거
	 * @param pc
	 */
	public void clearLearn() {
		_learnActives.clear();
		_learnPassives.clear();
	}
	
	/**
	 * 습득한 액티브 스킬을 DB에 등록한다.
	 * @param skill
	 */
	public void spellActiveMastery(L1Skills skill) {
		if (isLearnActive(skill.getSkillId(), true)) {
			return;
		}
		addLearnActive(skill.getSkillId());

		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_skills_active SET char_obj_id=?, skill_id=?, skill_name=?");
			pstm.setInt(1, _owner.getId());
			pstm.setInt(2, skill.getSkillId());
			pstm.setString(3, skill.getName());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 습득한 패시브 스킬을 DB에 등록한다.
	 * @param passive
	 */
	public void spellPassiveMastery(L1PassiveSkills passive) {
		if (isLearnPassive(passive.getPassiveId())) {
			return;
		}
		addLearnPassive(passive.getPassiveId());

		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_skills_passive SET char_obj_id=?, passive_id=?, passive_name=?");
			pstm.setInt(1, _owner.getId());
			pstm.setInt(2, passive.getPassiveId());
			pstm.setString(3, passive.getName());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 습닥한 액티브 스킬을 DB에서 제거한다.
	 * @param skill
	 */
	public void spellActiveLost(L1Skills skill) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM character_skills_active WHERE char_obj_id=? AND skill_id=?");
			pstm.setInt(1, _owner.getId());
			pstm.setInt(2, skill.getSkillId());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		deleteLearnActive(skill.getSkillId());
	}
	
	/**
	 * 습닥한 패시브 스킬을 DB에서 제거한다.
	 * @param passive
	 */
	public void spellPassiveLost(L1PassiveSkills passive) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM character_skills_passive WHERE char_obj_id=? AND passive_id=?");
			pstm.setInt(1, _owner.getId());
			pstm.setInt(2, passive.getPassiveId());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		deleteLearnPassive(passive.getPassiveId());
	}
    
    /**
     * 아우라
     */
    public boolean isAuraBuff() {
    	return _auraBuff;
    }
    public void setAuraBuff(boolean aura) {
    	_auraBuff = aura;
    }
    
    /**
     * 블라인트 하이딩: 어쌔신
     */
    public boolean isBlindHidingAssassin(){
		return _blindHidingAssassin;
	}
	public void setBlindHidingAssassin(boolean flag){
		_blindHidingAssassin = flag;
	}
	
	/**
	 * 약점 노출 대상
	 * @return L1Character
	 */
	public L1Character get_expose_weakness_target() {
		return _expose_weakness_target;
	}
	public void set_expose_weakness_target(L1Character val) {
		_expose_weakness_target = val;
	}
	
	/**
	 * 체인 리액션 적중자
	 * @return L1PcInstance
	 */
	public L1PcInstance get_chain_reaction_attacker() {
		return _chain_reaction_attacker;
	}
	public void set_chain_reaction_attacker(L1PcInstance val) {
		_chain_reaction_attacker = val;
	}
    
    /**
     * 혈맹의 축복 버프
     */
    public void einhasadClanBuff(){
    	if (_player != null) {
    		short currentMapId	= _player.getMapId();
    		FastTable<Integer> buffMapList	= ClanBlessBuffTable.getBuffList(_player.getClan().getEinhasadBlessBuff()).buffmaplist;
    		if (_player.getClan().getEinhasadBlessBuff() != 0 && buffMapList.contains(currentMapId) && _player.getClanBuffMap() == 0) {
    			_player.setClanBuffMap(currentMapId);
    			_player.add_rest_exp_reduce_efficiency(5);
    			_player.sendPackets(new S_SpellBuffNoti(_player, L1SkillId.CLAN_BLESS, true, -1), true);
    		}
    		if (currentMapId != _player.getClanBuffMap() && _player.getClanBuffMap() != 0) {
    			_player.setClanBuffMap(0);
    			_player.add_rest_exp_reduce_efficiency(-5);
    			_player.sendPackets(new S_SpellBuffNoti(_player, L1SkillId.CLAN_BLESS, false, -1), true);
    		}
    	}
	}
    
    public L1ExpPotion getExpPotion() {
    	return _expPotion;
    }
    
    /**
     * 경험치 추가 물약 변화
     */
    public void expPotionCheck(){
    	if (_expPotion._skillId == -1) {
    		return;
    	}
    	expPotionOnOff();
	}
	
    void expPotionOnOff(){
		if (_owner.getRegion() == L1RegionStatus.SAFETY) {
        	if (!_expPotion.isStop()) {
        		expPotionOnOff(true);
        	}
        } else {
        	if (_expPotion._skillId == L1SkillId.EXP_POTION3) {
        		boolean isEinValue	= _player.getAccount().getEinhasad().getPoint() >= Config.EIN.REST_EXP_DEFAULT_RATION;
            	if (isEinValue && _expPotion.isStop()) {
            		expPotionOnOff(false);
            	} else if (!isEinValue && !_expPotion.isStop()) {
            		expPotionOnOff(true);
                }
        	} else {
        		if (_expPotion.isStop()) {
        			expPotionOnOff(false);
	        	}
        	}
        }
	}
    
    void expPotionOnOff(boolean flag){
    	if (flag) {
    		stopSkillEffectTimer(_expPotion._skillId);// 타이머 중지
    	} else {
    		startSkillTimer(_expPotion._skillId);// 타이머 시작
    	}
    	_player.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, getSkillEffectTimeSec(_expPotion._skillId), _expPotion._potionId, flag ? 2 : 1), true);
    	_expPotion.setStop(flag);
    	_player.sendPackets(new S_ExpBoostingInfo(_player), true);
    }
    
    /**
	 * 안샤르의 가호
	 * @param name_id
	 * @param flag
	 */
	public void doGraceOfAnshar(int name_id, boolean flag) {
		int spell_id = -1;
		L1PassiveId passive = null;
		switch (name_id) {
		case 31739:// 안샤르의 가호 (전설)(검사는 없다)
			switch (_player.getType()) {
			case 0:// 군주
				spell_id = L1SkillId.PRIME;
				break;
			case 1:// 기사
				spell_id = L1SkillId.FORCE_STUN;
				break;
			case 2:// 요정
				passive = L1PassiveId.GLORY_EARTH;
				break;
			case 3:// 마법사
				spell_id = L1SkillId.ETERNITY;
				break;
			case 4:// 다크엘프
				spell_id = L1SkillId.AVENGER;
				break;
			case 5:// 용기사
				spell_id = L1SkillId.HALPHAS;
				break;
			case 6:// 환술사
				spell_id = L1SkillId.POTENTIAL;
				break;
			case 7:// 전사
				passive = L1PassiveId.DEMOLITION;
				break;
			case 9:// 창기사
				passive = L1PassiveId.CRUEL_CONVICTION;
				break;
			default:
				return;
			}
			break;
		case 31740:// 안샤르의 가호 (쇼크 어택)
			spell_id = L1SkillId.SHOCK_ATTACK;
			break;
		case 31741:// 안샤르의 가호 (레이징 웨폰)
			passive = L1PassiveId.RAIGING_WEAPON;
			break;
		case 31742:// 안샤르의 가호 (홀리워크: 에볼루션)
			passive = L1PassiveId.HOLY_WALK_EVOLUTION;
			break;
		case 31743:// 안샤르의 가호 (매스 이뮨 투 함)
			spell_id = L1SkillId.MASS_IMMUNE_TO_HARM;
			break;
		case 31744:// 안샤르의 가호 (인스네어)
			spell_id = L1SkillId.ENSNARE;
			break;
		case 31745:// 안샤르의 가호 (데스페라도:앱솔루트)
			passive = L1PassiveId.DESPERADO_ABSOLUTE;
			break;
		case 31746:// 안샤르의 가호 (팬텀:리퍼)
			passive = L1PassiveId.PHANTOM_RIPER;
			break;
		case 31747:// 안샤르의 가호 (팬텀:레퀴엠)
			passive = L1PassiveId.PHANTOM_REQUIEM;
			break;
		default:
			return;
		}
		if (spell_id != -1) {
			if (isLearnActive(spell_id, true)) {
				return;
			}
			L1Skills template = SkillsTable.getTemplate(spell_id);
			if (template.getClassType() != _player.getType()) {
				return;
			}
			_player.sendPackets(new S_AvailableSpellNoti(spell_id, flag), true);
			return;
		}
		if (passive != null) {
			if (isLearnPassive(passive.toInt())) {
				return;
			}
			L1PassiveSkills template = SkillsTable.getPassiveTemplate(passive.toInt());
			if (template.getClassType() != _player.getType()) {
				return;
			}
			_player.sendPackets(new S_AddSpellPassiveNoti(passive.toInt(), flag), true);
			if (flag) {
				_player.getPassiveSkill().set(passive);
			} else {
				_player.getPassiveSkill().remove(passive);
			}
		}
	}
	
}


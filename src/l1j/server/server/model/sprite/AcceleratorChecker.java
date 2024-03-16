package l1j.server.server.model.sprite;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Skills;

/**
 * 플레이어의 속도 처리 담당
 * @author LinOffice
 */
public class AcceleratorChecker {
	class AccelInfo {
		public AccelInfo(long actTime, boolean first) {
			_actTime = actTime;
			_first = first;
		}
		public long _actTime;
		public boolean _first;
	}

	class SpeedInfo {
		public SpeedInfo(int gfxId) {
			_gfxId = gfxId;
		}
		public int _gfxId;
		public long _actTime;
		public long _totalActTime;
		public long _actCount;
	}

	private final L1PcInstance _pc;
	
	private int _attack_level = -1, _attack_sprite = -1, _attack_weapon = -1, _attack_interval = -1;// 공격 속도
	private int _move_level = -1, _move_sprite = -1, _move_weapon = -1, _move_interval = -1;		// 이동 속도
	private int _dirSpell_sprite = -1, _dirSpell_interval = -1;										// 공격 스킬 속도
	private int _nodirSpell_sprite = -1, _nodirSpell_interval = -1;									// 서브 스킬 속도
	private int _dmg_sprite = -1, _dmg_weapon = -1, _dmg_interval = -1;								// 대미지모션 속도
	private int _pickup_sprite = -1, _pickup_interval = -1;											// 토글 모션 속도
	private int _wand_sprite = -1, _wand_interval = -1;												// 막대 모션 속도
	
	private int _default_dir_spell_interval, _default_nodir_spell_interval;
	
	private static final double HASTE_RETARDATION	= 1.33D;
	private static final double BRAVE_RETARDATION	= 1.33D;
	private static final double WAFFLE_RETARDATION	= 1.13D;
	private static final double THIRD_RETARDATION	= 1.13D;
	private static final double FOURTH_RETARDATION	= 1.13D;
	private static final double HASTE_RATE			= 1D / HASTE_RETARDATION;
	private static final double BRAVE_RATE			= 1D / BRAVE_RETARDATION;
	private static final double WAFFLE_RATE			= 1D / WAFFLE_RETARDATION;
	private static final double THIRD_RATE			= 1D / THIRD_RETARDATION;
	private static final double FOURTH_RATE			= 1D / FOURTH_RETARDATION;
	
	private final EnumMap<ACT_TYPE, AccelInfo> _actTimers		= new EnumMap<ACT_TYPE, AccelInfo>(ACT_TYPE.class);
	private final EnumMap<ACT_TYPE, SpeedInfo> _speedRecorder	= new EnumMap<ACT_TYPE, SpeedInfo>(ACT_TYPE.class);

	public static enum ACT_TYPE {
		MOVE, ATTACK, SPELL_DIR, SPELL_NODIR, DMG_MOTION, PICK_UP, WAND
	}

	// 체크의 결과
	public static final int R_OK			= 0;
	public static final int R_DETECTED		= 1;
	public static final int R_DISCONNECTED	= 2;

	// 케릭터의 액션별 속도를 측정하는 클래스
	public AcceleratorChecker(L1PcInstance pc) {
		_pc = pc;
		long now = System.currentTimeMillis();
		for (ACT_TYPE each : ACT_TYPE.values()) {
			_actTimers.put(each, new AccelInfo(now, false));
		}
		L1Sprite sprite					= SpriteLoader.get_sprite(_pc.getClassId());
		_default_dir_spell_interval		= sprite.getDirSpellSpeed();
		_default_nodir_spell_interval	= sprite.getNodirSpellSpeed();
	}

	/**
	 * 액션의 간격이 부정하지 않을까 체크해, 적당 처리를 실시한다.
	 * @param type - 체크하는 액션의 타입
	 * @return 문제가 없었던 경우는 0, 부정할 경우는 1, 부정 동작이 일정 회수에 이르렀기 때문에 플레이어를 절단 했을 경우는 2를 돌려준다.
	 */
	public boolean isAccelerated(ACT_TYPE type) {
		long now = System.currentTimeMillis();
		AccelInfo accelInfo = _actTimers.get(type);
		long interval = getRightInterval(type);
		if (interval == 0) {
			SpeedInfo speedInfo = _speedRecorder.get(type);
			if (speedInfo == null || speedInfo._gfxId != _pc.getSpriteId()) {
				speedInfo = new SpeedInfo(_pc.getSpriteId());
			}
			if (speedInfo._actTime != 0) {
				long duration = now - speedInfo._actTime;
				++speedInfo._actCount;
				speedInfo._totalActTime += duration;
			}
			speedInfo._actTime = now;
			String print = String.format("■■■■■■■■■ Suspected hack: TYPE (%s), user (%s) ■■■■■■■■■■", type.name(), _pc.getName());
			for (L1PcInstance gm : L1World.getInstance().getAllGms()) {
				gm.sendPackets(new S_SystemMessage(print), true);
			}
		    System.out.println(print);
			_speedRecorder.put(type, speedInfo);
			return false;
		}
		if (now - accelInfo._actTime > interval * 3) {
			_actTimers.put(type, new AccelInfo(now, true));
			return false;
		} else if (now - accelInfo._actTime > interval) {
			_actTimers.put(type, new AccelInfo(accelInfo._actTime + interval, false));
			return false;
		} else if (accelInfo._first && now - accelInfo._actTime > interval >> 0x00000001) {
			_actTimers.put(type, new AccelInfo(now, false));
			return false;
		} else if (type == ACT_TYPE.MOVE) {
			_actTimers.put(type, new AccelInfo(now, false));
		}
		return true;
	}

	public boolean isAccelerated(ACT_TYPE type, int interval) {
		AccelInfo accelInfo = _actTimers.get(type);
		return interval == 0 || System.currentTimeMillis() - accelInfo._actTime > interval ? false : true;
	}

	public long getLeftTime(ACT_TYPE type) {
		AccelInfo accelInfo = _actTimers.get(type);
		return Math.max(0, accelInfo._actTime + getRightInterval(type) - System.currentTimeMillis());
	}

	/**
	 * PC 상태로부터 지정된 종류의 액션의 올바른 인터벌(ms)을 계산해, 돌려준다.
	 * @param type - 액션의 종류
	 * @return 올바른 인터벌(ms)
	 */
	public int getRightInterval(ACT_TYPE type) {
		switch (type) {
		case MOVE:
			return getRightMoveInterval(type);
		case ATTACK:
			return getRightAttackInterval(type);
		case SPELL_DIR:
			return getRightSpellDirInterval();
		case SPELL_NODIR:
			return getRightSpellNoDirInterval();
		case DMG_MOTION:
			return getRightDmgMotionInterval();
		case PICK_UP:
			return getRightPickUpInterval();
		case WAND:
			return getRightWandInterval();
		default:
			return 1000;
		}
	}
	
	int getRightMoveInterval(ACT_TYPE type) {
		if (_move_sprite != _pc.getSpriteId() || _move_weapon != _pc.getCurrentWeapon() || _move_level != _pc.getBoundaryLevel()) {
			setMoveInterval(_pc.getSpriteId(), _pc.getCurrentWeapon(), _pc.getBoundaryLevel(), type);
		}
		return (int)calcBuffMoveInterval(calcDefaultBuffInterval(_move_interval));
	}
	
	int getRightAttackInterval(ACT_TYPE type) {
	    if (_attack_sprite != _pc.getSpriteId() || _attack_weapon != _pc.getCurrentWeapon() + 1 || _attack_level != _pc.getBoundaryLevel()) {
			setAttackInterval(_pc.getSpriteId(), _pc.getCurrentWeapon() + 1, _pc.getBoundaryLevel(), type);
		}
	    return (int)calcBuffAttackInterval(calcDefaultBuffInterval(_attack_interval));
	}
	
	int getRightSpellDirInterval() {
		if (_dirSpell_sprite != _pc.getSpriteId()) {
			setDirSpellInterval(_pc.getSpriteId());
		}
		return (int)calcBuffSpellInterval(calcDefaultBuffInterval(_dirSpell_interval));
	}
	
	int getRightSpellNoDirInterval() {
		if (_nodirSpell_sprite != _pc.getSpriteId()) {
			setNodirSpellInterval(_pc.getSpriteId());
		}
		return (int)calcBuffSpellInterval(calcDefaultBuffInterval(_nodirSpell_interval));
	}
	
	int getRightDmgMotionInterval() {
		if (_dmg_sprite != _pc.getSpriteId() || _dmg_weapon != _pc.getCurrentWeapon()) {
			setDmgMotionInterval(_pc.getSpriteId(), _pc.getCurrentWeapon());
		}
		return (int)calcDefaultBuffInterval(_dmg_interval);
	}
	
	int getRightPickUpInterval() {
		if (_pickup_sprite != _pc.getSpriteId()) {
			setPickUpInterval(_pc.getSpriteId());
		}
		return (int)calcDefaultBuffInterval(_pickup_interval);
	}
	
	int getRightWandInterval() {
		if (_wand_sprite != _pc.getSpriteId()) {
			setWandInterval(_pc.getSpriteId());
		}
		return (int)calcDefaultBuffInterval(_wand_interval);
	}
	
	double calcDefaultBuffInterval(double interval){// 버프 속도 계산
		if (_pc.isSlow()) {
			interval *= HASTE_RETARDATION;
		}
		if (_pc.isHaste()) {
			interval *= HASTE_RATE;
		}
		if (_pc.isBrave() || _pc.isBloodLust() || _pc.isHurricane() || _pc.isFocusWave()) {
			interval *= BRAVE_RATE;
		} else if (_pc.isElfBrave()) {
			interval *= WAFFLE_RATE;
		}
		if (_pc.isDrunken()) {
			interval *= THIRD_RATE;
		}
		return interval;
	}
	
	/**
	 * 버프에 따른 이동 속도 계산
	 * @param interval
	 * @return int
	 */
	double calcBuffMoveInterval(double interval) {
		if (_pc.isFastMovable()) {
			interval *= BRAVE_RATE;
		}
		if (_pc.isFruit()) {
			interval *= WAFFLE_RATE;
		}
		if (_pc.isFourthGear()) {
			interval *= FOURTH_RATE;
		}
		if (_pc.getMoveSpeedDelayRate() > 0) {
			interval -= interval * (_pc.getMoveSpeedDelayRate() * 0.01D);
		}
		
		// 클라이언트 화면 이동 속도 제어에 의한 배율
		int move_delay_reduce_rate = Config.SPEED.PC_MOVE_DELAY_REDUCE_RATE;
		if (move_delay_reduce_rate > 0 && move_delay_reduce_rate != 100) {
			interval = interval * 100 / move_delay_reduce_rate;
		}
		return interval;
	}
	
	/**
	 * 버프에 따른 공격 속도 계산
	 * @param interval
	 * @return int
	 */
	double calcBuffAttackInterval(double interval) {
		if (_pc.getSkill().hasSkillEffect(L1SkillId.WIND_SHACKLE) || _pc.getSkill().hasSkillEffect(L1SkillId.HALPAS_WIND_BRESS)) {
			interval *= HASTE_RETARDATION;
		}
		if (_pc.isFastMovable() && (_pc.isPassiveStatus(L1PassiveId.MOVING_ACCELERATION_LAST) || _pc.isPassiveStatus(L1PassiveId.HOLY_WALK_EVOLUTION))) {
			interval *= WAFFLE_RATE;
		}
		if (_pc.isFruit() && _pc.isPassiveStatus(L1PassiveId.DARKHORSE)) {
			interval *= WAFFLE_RATE;
		}
		if (_pc.isFourthGear()) {
			interval *= FOURTH_RATE;
		}
		if (_pc.getAttackSpeedDelayRate() > 0) {
			interval -= interval * (_pc.getAttackSpeedDelayRate() * 0.01D);
		}
		return interval;
	}
	
	/**
	 * 버프에 따른 스킬모션 속도 계산
	 * @param interval
	 * @return int
	 */
	double calcBuffSpellInterval(double interval) {
		if (_pc.getSpellSpeedDelayRate() > 0) {
			interval -= interval * (_pc.getSpellSpeedDelayRate() * 0.01D);
		}
		return interval;
	}
	
	/**
	 * 공격 속도 설정
	 * @param spriteId
	 * @param weapon
	 * @param level
	 * @param type
	 */
	void setAttackInterval(int spriteId, int weapon, int level, ACT_TYPE type){
		_attack_sprite			= spriteId;
		_attack_weapon			= weapon;
		_attack_level			= level;
		_attack_interval		= (int) _pc.getSprite().getActionSpeed(_attack_weapon, _attack_level, type);
		if (_attack_interval <= 0) {
			_attack_interval = calcBasicActionSpeed(20, 24);// 예외 설정
		}
	}
	
	/**
	 * 이동 속도 설정
	 * @param spriteId
	 * @param weapon
	 * @param level
	 * @param type
	 */
	void setMoveInterval(int spriteId, int weapon, int level, ACT_TYPE type){
		_move_sprite			= spriteId;
		_move_weapon			= weapon;
		_move_level				= level;
		_move_interval			= (int) _pc.getSprite().getActionSpeed(_move_weapon, _move_level, type);
		if (_move_interval <= 0) {
			_move_interval = calcBasicActionSpeed(16, 24);// 예외 설정
		}
	}
	
	/**
	 * 공격 스킬 속도 설정
	 * @param spriteId
	 */
	void setDirSpellInterval(int spriteId){
		_dirSpell_sprite		= spriteId;
		_dirSpell_interval		= _pc.getSprite().getDirSpellSpeed();
		if (_dirSpell_interval <= 0) {
			_dirSpell_interval = calcBasicActionSpeed(33, 24);// 예외 설정
		}
	}
	
	/**
	 * 서브 스킬 속도 설정
	 * @param spriteId
	 */
	void setNodirSpellInterval(int spriteId){
		_nodirSpell_sprite		= spriteId;
		_nodirSpell_interval	= _pc.getSprite().getNodirSpellSpeed();
		if (_nodirSpell_interval <= 0) {
			_nodirSpell_interval = calcBasicActionSpeed(37, 24);// 예외 설정
		}
	}
	
	/**
	 * 대미지 모션 속도 설정
	 * @param spriteId
	 * @param weapon
	 */
	void setDmgMotionInterval(int spriteId, int weapon){
		_dmg_sprite				= spriteId;
		_dmg_weapon				= weapon;
		_dmg_interval			= _pc.getSprite().getDamageSpeed(_dmg_weapon);
		if (_dmg_interval <= 0) {
			_dmg_interval = calcBasicActionSpeed(14, 24);// 예외 설정
		}
	}
	
	/**
	 * 토글 속도 설정
	 * @param spriteId
	 */
	void setPickUpInterval(int spriteId){
		_pickup_sprite			= spriteId;
		_pickup_interval		= _pc.getSprite().getPickUpSpeed();
		if (_pickup_interval <= 0) {
			_pickup_interval	= calcBasicActionSpeed(10, 24);// 예외 설정
		}
	}
	
	/**
	 * 막대 사용 속도 설정
	 * @param spriteId
	 */
	void setWandInterval(int spriteId){
		_wand_sprite			= spriteId;
		_wand_interval			= _pc.getSprite().getWandSpeed();
		if (_wand_interval <= 0) {
			_wand_interval		= calcBasicActionSpeed(30, 24);// 예외 설정
		}
	}
	
	int calcBasicActionSpeed(int frameCount, int frameRate) {// default speed calc
		return (int) (frameCount * 40 * (24D / frameRate));
	}
	
	public void resetAttactInterval(){
		setAttackInterval(_pc.getSpriteId(), _pc.getCurrentWeapon() + 1, _pc.getBoundaryLevel(), ACT_TYPE.ATTACK);
	}
	
	public int getAttackInterval(){
		if (_attack_interval == -1) {
			setAttackInterval(_pc.getSpriteId(), _pc.getCurrentWeapon() + 1, _pc.getBoundaryLevel(), ACT_TYPE.ATTACK);
		}
		return _attack_interval;
	}
	
	public int getMoveInterval(){
		if (_move_interval == -1) {
			setMoveInterval(_pc.getSpriteId(), _pc.getCurrentWeapon(), _pc.getBoundaryLevel(), ACT_TYPE.MOVE);
		}
		return _move_interval;
	}
	
	public int getDirSpellInterval(){
		if (_dirSpell_interval == -1) {
			setDirSpellInterval(_pc.getSpriteId());
		}
		return _dirSpell_interval;
	}
	
	public int getNodirSpellInterval(){
		if (_nodirSpell_interval == -1) {
			setNodirSpellInterval(_pc.getSpriteId());
		}
		return _nodirSpell_interval;
	}
	
	public int getDmgMotionInterval(){
		if (_dmg_interval == -1) {
			setDmgMotionInterval(_pc.getSpriteId(), _pc.getCurrentWeapon());
		}
		return _dmg_interval;
	}
	
	public int getPickUpInterval(){
		if (_pickup_interval == -1) {
			setPickUpInterval(_pc.getSpriteId());
		}
		return _pickup_interval;
	}
	
	public int getWandInterval(){
		if (_wand_interval == -1) {
			setWandInterval(_pc.getSpriteId());
		}
		return _wand_interval;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////// 스펠 딜레이 ///////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final double SPELL_HASTE_RETARDATION		= 1.15D;
	private static final double SPELL_WAFFLE_RETARDATION	= 1.07D;
	private static final double SPELL_HASTE_RATE			= 1D / SPELL_HASTE_RETARDATION;
	private static final double SPELL_WAFFLE_RATE			= 1D / SPELL_WAFFLE_RETARDATION;
	private static final double SPELL_DRUNKEN_RATE			= 1D / SPELL_WAFFLE_RETARDATION;
	
	private static final double SPELL_MAIN_MIN_VALUE		= 500.0D;
	private static final double SPELL_GLOBAL_MIN_VALUE		= 200.0D;
	private static final double SPELL_SPRITE_MIN_RATE		= 0.8D;
	
	private double _dirSpell_sprite_rate, _nodirSpell_sprite_rate;
	
	private static final List<Integer> EXCEPTION_SPELL_IDS	= Arrays.asList(new Integer[] {
			L1SkillId.TRIPLE_ARROW, L1SkillId.FOU_SLAYER
	});
	
	final double[] _skillInterval = new double[2];// 스킬 딜레이 배열 = 0:메인, 1:글로벌
	
	/**
	 * sprite에 대한 스펠 딜레이 감소 배율을 조사한다.
	 * @param actionId
	 * @param spriteId
	 * @return double
	 */
	double getSpellDelayRateFromSprite(int actionId, int spriteId) {
		if (actionId == ActionCodes.ACTION_SkillAttack) {
			if (_dirSpell_sprite_rate == 0 || _dirSpell_sprite != spriteId) {
				setDirSpellInterval(spriteId);
				_dirSpell_sprite_rate = ((double) _dirSpell_interval / (double) _default_dir_spell_interval);
				if (_dirSpell_sprite_rate < SPELL_SPRITE_MIN_RATE) {
					_dirSpell_sprite_rate = SPELL_SPRITE_MIN_RATE;
				}
			}
			return _dirSpell_sprite_rate;
		} else {
			if (_nodirSpell_sprite_rate == 0 || _nodirSpell_sprite != spriteId) {
				setNodirSpellInterval(spriteId);
				_nodirSpell_sprite_rate = ((double) _nodirSpell_interval / (double) _default_nodir_spell_interval);
				if (_nodirSpell_sprite_rate < SPELL_SPRITE_MIN_RATE) {
					_nodirSpell_sprite_rate = SPELL_SPRITE_MIN_RATE;
				}
			}
			return _nodirSpell_sprite_rate;
		}
	}
	
	/**
	 * 스킬 딜레이 조사
	 * @param skill
	 * @return double[]
	 */
	public double[] getRightSkillInterval(final L1Skills skill) {
		_skillInterval[0]	= (double)skill.getReuseDelay();// 메인 딜레이
		_skillInterval[1]	= 1500.0D;// 글로벌 딜레이
		if (_skillInterval[1] > _skillInterval[0]) {// 글로벌 딜레이가 메인 딜레이보다 높을 수 없다.
			_skillInterval[1] = _skillInterval[0];
		}
		if (_pc == null) {
			return _skillInterval;
		}
		final boolean fixDelay		= skill.getFixDelay();// 고정 스킬딜레이 여부
		final int cooltimeDecrease	= _pc.getAbility().getSpellCooltimeDecrease();// 쿨타임 감소
		final int spriteId			= _pc.getSpriteId();
		double spriteRate			= 1.0D;// sprite에 대한 딜레이 감소 배율
		if (_pc.getClassId() != spriteId) {
			spriteRate = getSpellDelayRateFromSprite(skill.getActionId(), spriteId);
		}
		boolean isExceptionSkill = EXCEPTION_SPELL_IDS.contains(skill.getSkillId());
		for (int i=0; i<_skillInterval.length; i++) {
			if (fixDelay && i == 0) {// 고정 딜레이의 경우 메인 딜레이는 감소되지 않는다.
				continue;
			}
			if (spriteRate != 1.0D) {
				_skillInterval[i] *= isExceptionSkill ? spriteRate < 1.0D ? 0.9D : 1.1D : spriteRate;
			}
			if (_pc.isHaste()) {
				_skillInterval[i] *= isExceptionSkill ? 0.9D : SPELL_HASTE_RATE;
			}
			if (_pc.isBrave() || _pc.isBloodLust() || _pc.isHurricane() || _pc.isFocusWave()) {
				_skillInterval[i] *= isExceptionSkill ? 0.9D : SPELL_HASTE_RATE;
			} else if (_pc.isElfBrave()) {
				_skillInterval[i] *= isExceptionSkill ? 0.95D : SPELL_WAFFLE_RATE;
			}
			if (_pc.isDrunken()) {
				_skillInterval[i] *= isExceptionSkill ? 0.95D : SPELL_DRUNKEN_RATE;
			}
			if (_pc.isSlow()) {
				_skillInterval[i] *= SPELL_HASTE_RETARDATION;
			}
		}
		
		// 쿨타임 감소(메인 딜레이에만 적용)
		if (cooltimeDecrease > 0) {
			_skillInterval[0] -= cooltimeDecrease;// 0.1초 기준
		}
		
		// 최소값
		if (_skillInterval[0] < SPELL_MAIN_MIN_VALUE) {
			_skillInterval[0] = SPELL_MAIN_MIN_VALUE;
		}
		if (_skillInterval[1] < SPELL_GLOBAL_MIN_VALUE) {
			_skillInterval[1] = SPELL_GLOBAL_MIN_VALUE;
		}
		return _skillInterval;
	}
}

package l1j.server.server.templates;

public class L1MobSkill implements Cloneable {
	
	public static enum TYPE {
		NONE,
		ATTACK,
		SPELL,
		SUMMON,
		POLY,
		LINE_ATTACK,
		KIRTAS_METEOR,
		KIRTAS_BARRIER,
		TITANGOLEM_BARRIER,
		VALLACAS_FLY,
		VALLACAS_BRESS,
		;
		public static TYPE fromString(String val) {
			switch(val) {
			case "NONE":
				return NONE;
			case "ATTACK":
				return ATTACK;
			case "SPELL":
				return SPELL;
			case "SUMMON":
				return SUMMON;
			case "POLY":
				return POLY;
			case "LINE_ATTACK":
				return LINE_ATTACK;
			case "KIRTAS_METEOR":
				return KIRTAS_METEOR;
			case "KIRTAS_BARRIER":
				return KIRTAS_BARRIER;
			case "TITANGOLEM_BARRIER":
				return TITANGOLEM_BARRIER;
			case "VALLACAS_FLY":
				return VALLACAS_FLY;
			case "VALLACAS_BRESS":
				return VALLACAS_BRESS;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments MOBSKILL_TYPE, %s", val));
			}
		}
	}
	
	public static enum CHANGE_TARGET {
		NO,
		COMPANION,
		ME,
		RANDOM,
		;
		public static CHANGE_TARGET fromString(String val) {
			switch(val) {
			case "NO":
				return NO;
			case "COMPANION":
				return COMPANION;
			case "ME":
				return ME;
			case "RANDOM":
				return RANDOM;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments MOBSKILL_CHANGE_TARGET, %s", val));
			}
		}
	}
	
	private final int skillSize;
	public int getSkillSize() {
		return skillSize;
	}

	@Override
	public L1MobSkill clone() {
		try {
			return (L1MobSkill) (super.clone());
		} catch (CloneNotSupportedException e) {
			throw (new InternalError(e.getMessage()));
		}
	}

	public L1MobSkill(int sSize) {
		skillSize			= sSize;

		type				= new L1MobSkill.TYPE[skillSize];
		prob				= new int[skillSize];
		enableHp			= new int[skillSize];
		enableCompanionHp	= new int[skillSize];
		range				= new int[skillSize];
		limitCount			= new int[skillSize];
		changeTarget		= new L1MobSkill.CHANGE_TARGET[skillSize];
		areaWidth			= new int[skillSize];
		areaHeight			= new int[skillSize];
		leverage			= new int[skillSize];
		skillId				= new int[skillSize];
		gfxid				= new int[skillSize];
		actid				= new int[skillSize];
		summon				= new int[skillSize];
		summonMin			= new int[skillSize];
		summonMax			= new int[skillSize];
		polyId				= new int[skillSize];
		msg					= new String[skillSize];
	}

	private int mobid;
	public int get_mobid() {
		return mobid;
	}
	public void set_mobid(int i) {
		mobid = i;
	}

	private String mobName;
	public String getMobName() {
		return mobName;
	}
	public void setMobName(String s) {
		mobName = s;
	}

	private L1MobSkill.TYPE type[];
	public L1MobSkill.TYPE getType(int idx) {
		if (idx < 0 || idx >= skillSize)
			return L1MobSkill.TYPE.NONE;
		return type[idx];
	}
	public void setType(int idx, L1MobSkill.TYPE val) {
		if (idx < 0 || idx >= skillSize)
			return;
		type[idx] = val;
	}

	private int prob[];
	public int getProb(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return prob[idx];
	}
	public void setProb(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		prob[idx] = i;
	}

	int enableHp[];
	public int getEnableHp(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return enableHp[idx];
	}
	public void setEnableHp(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		enableHp[idx] = i;
	}

	int enableCompanionHp[];
	public int getEnableCompanionHp(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return enableCompanionHp[idx];
	}
	public void setEnableCompanionHp(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		enableCompanionHp[idx] = i;
	}

	int range[];
	public int getRange(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return range[idx];
	}
	public void setRange(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		range[idx] = i;
	}

	public boolean isRange(int range, int distance) {
		return range > 0 && distance <= range;
	}

	int limitCount[];
	public int getLimitCount(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return limitCount[idx];
	}
	public void setLimitCount(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		limitCount[idx] = i;
	}

	L1MobSkill.CHANGE_TARGET changeTarget[];
	public L1MobSkill.CHANGE_TARGET getChangeTarget(int idx) {
		if (idx < 0 || idx >= skillSize)
			return L1MobSkill.CHANGE_TARGET.NO;
		return changeTarget[idx];
	}
	public void setChangeTarget(int idx, L1MobSkill.CHANGE_TARGET val) {
		if (idx < 0 || idx >= skillSize)
			return;
		changeTarget[idx] = val;
	}

	int areaWidth[];
	public int getAreaWidth(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return areaWidth[idx];
	}
	public void setAreaWidth(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		areaWidth[idx] = i;
	}

	int areaHeight[];
	public int getAreaHeight(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return areaHeight[idx];
	}
	public void setAreaHeight(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		areaHeight[idx] = i;
	}

	int leverage[];
	public int getLeverage(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return leverage[idx];
	}
	public void setLeverage(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		leverage[idx] = i;
	}

	int skillId[];
	public int getSkillId(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return skillId[idx];
	}
	public void setSkillId(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		skillId[idx] = i;
	}

	int gfxid[];
	public int getGfxid(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return gfxid[idx];
	}
	public void setGfxid(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		gfxid[idx] = i;
	}

	int actid[];
	public int getActid(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return actid[idx];
	}
	public void setActid(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		actid[idx] = i;
	}

	int summon[];
	public int getSummon(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return summon[idx];
	}
	public void setSummon(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		summon[idx] = i;
	}

	int summonMin[];
	public int getSummonMin(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return summonMin[idx];
	}
	public void setSummonMin(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		summonMin[idx] = i;
	}

	int summonMax[];
	public int getSummonMax(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return summonMax[idx];
	}
	public void setSummonMax(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		summonMax[idx] = i;
	}

	int polyId[];
	public int getPolyId(int idx) {
		if (idx < 0 || idx >= skillSize)
			return 0;
		return polyId[idx];
	}
	public void setPolyId(int idx, int i) {
		if (idx < 0 || idx >= skillSize)
			return;
		polyId[idx] = i;
	}
	
	String msg[];
	public String getMsg(int idx) {
		if (idx < 0 || idx >= skillSize)
			return null;
		return msg[idx];
	}
	public void setMsg(int idx, String i) {
		if (idx < 0 || idx >= skillSize)
			return;
		msg[idx] = i;
	}
}


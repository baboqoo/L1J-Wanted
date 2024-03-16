package l1j.server.GameSystem.arca;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.server.model.skill.L1SkillId;

public enum L1ArcaGrade {
	GRADE_1(1,	L1SkillId.TAM_FRUIT_1,	8265,	4181),
	GRADE_2(2,	L1SkillId.TAM_FRUIT_2,	8266,	4182),
	GRADE_3(3,	L1SkillId.TAM_FRUIT_3,	8267,	4183),
	GRADE_4(4,	L1SkillId.TAM_FRUIT_4,	8268,	5046),
	GRADE_5(5,	L1SkillId.TAM_FRUIT_5,	8269,	5047),
	;
	private int grade, spellid, icon, msg;
	L1ArcaGrade(int grade, int spellid, int icon, int msg) {
		this.grade		= grade;
		this.spellid	= spellid;
		this.icon		= icon;
		this.msg		= msg;
	}
	public int getGrade() {
		return grade;
	}
	public int getSpellId() {
		return spellid;
	}
	public int getIcon() {
		return icon;
	}
	public int getMsg() {
		return msg;
	}
	
	private static final ConcurrentHashMap<Integer, L1ArcaGrade> DATA;
	static {
		DATA = new ConcurrentHashMap<>(Config.ALT.ARCA_MAX_ACTIVATE_CHARACTERS);
		DATA.put(GRADE_1.grade, GRADE_1);
		DATA.put(GRADE_2.grade, GRADE_2);
		DATA.put(GRADE_3.grade, GRADE_3);
		DATA.put(GRADE_4.grade, GRADE_4);
		DATA.put(GRADE_5.grade, GRADE_5);
	}
	public static L1ArcaGrade getGrade(int grade){
		return DATA.get(grade);
	}
}


package l1j.server.server.templates;

import l1j.server.common.bin.spell.CommonSpellInfo;
import l1j.server.server.construct.L1Grade;
import l1j.server.server.construct.skill.L1PassiveId;

public class L1PassiveSkills {
	private int passiveId;
	private String name;
	private String desc_en;
	private int duration;
	private int onIconId;
	private int tooltipStrId;
	private boolean isGood;
	private int classType;
	private L1Skills backActive;
	private L1PassiveSkills backPassive;
	private String bookNameRegex;
	private L1PassiveId passive;
	private L1Grade _grade;
	private CommonSpellInfo bin;
	
	public L1PassiveSkills(int passiveId, String name, String desc_en, int duration,
			int onIconId, int tooltipStrId, boolean isGood, int classType,
			L1Skills backActive, L1PassiveSkills backPassive, String bookNameRegex,
			L1PassiveId passive, L1Grade grade, CommonSpellInfo bin) {
		this.passiveId = passiveId;
		this.name = name;
		this.desc_en = desc_en;
		this.duration = duration;
		this.onIconId = onIconId;
		this.tooltipStrId = tooltipStrId;
		this.isGood = isGood;
		this.classType = classType;
		this.backActive = backActive;
		this.backPassive = backPassive;
		this.bookNameRegex = bookNameRegex;
		this.passive = passive;
		this._grade	= grade;
		this.bin = bin;
	}
	
	public int getPassiveId() {
		return passiveId;
	}
	public String getName() {
		return name;
	}
	public String getDescEn() {
		return desc_en;
	}
	public int getDuration() {
		return duration;
	}
	public int getOnIconId() {
		return onIconId;
	}
	public int getTooltipStrId() {
		return tooltipStrId;
	}
	public boolean isGood() {
		return isGood;
	}
	public int getClassType() {
		return classType;
	}
	public L1Skills getBackActive() {
		return backActive;
	}
	public L1PassiveSkills getBackPassive() {
		return backPassive;
	}
	public String getBookNameRegex() {
		return bookNameRegex;
	}
	public L1PassiveId getPassive() {
		return passive;
	}
	public L1Grade getGrade() {
		return _grade;
	}
	public CommonSpellInfo getBin() {
		return bin;
	}
}


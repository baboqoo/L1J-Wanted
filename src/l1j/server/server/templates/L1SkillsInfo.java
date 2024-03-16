package l1j.server.server.templates;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconDurationShowType;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.eBoostType;

public class L1SkillsInfo {
	private String name;
	private int skillId, icon, onIconId, offIconId, tooltipStrId, useSkillId, iconPriority, newStrId, endStrId, buffGroupId, buffGroupPriority;
	private int overlapBuffIcon, mainTooltipStrId, buffIconPriority, expireDuration;
	private boolean isGood, simplePck, isPassiveSpell;
	private SkillIconDurationShowType durationShowType;
	private eBoostType boostType;
	
	public L1SkillsInfo(ResultSet rs) throws SQLException{
		skillId				= rs.getInt("skillId");
		name				= rs.getString("skillname");
		useSkillId			= rs.getInt("useSkillId");
		String showType		= rs.getString("durationShowType");
		showType			= showType.substring(showType.indexOf("(") + 1, showType.indexOf(")"));
		durationShowType	= SkillIconDurationShowType.fromInt(Integer.parseInt(showType));
		icon				= rs.getInt("icon");
		onIconId			= rs.getInt("onIconId");
		offIconId			= rs.getInt("offIconId");
		simplePck			= Boolean.valueOf(rs.getString("simplePck"));
		iconPriority		= rs.getInt("iconPriority");
		tooltipStrId		= rs.getInt("tooltipStrId");
		newStrId			= rs.getInt("newStrId");
		endStrId			= rs.getInt("endStrId");
		isGood				= Boolean.valueOf(rs.getString("isGood"));
		overlapBuffIcon		= rs.getInt("overlapBuffIcon");
		mainTooltipStrId	= rs.getInt("mainTooltipStrId");
		buffIconPriority	= rs.getInt("buffIconPriority");
		buffGroupId			= rs.getInt("buffGroupId");
		buffGroupPriority	= rs.getInt("buffGroupPriority");
		expireDuration		= rs.getInt("expireDuration");
		String boost		= rs.getString("boostType");
		boost				= boost.substring(boost.indexOf("(") + 1, boost.indexOf(")"));
		boostType			= eBoostType.fromInt(Integer.parseInt(boost));
		isPassiveSpell		= Boolean.valueOf(rs.getString("isPassiveSpell"));
	}

	public int getSkillId() {
		return skillId;
	}
	public String getName() {
		return name;
	}
	public int getUseSkillId() {
		return useSkillId;
	}
	public SkillIconDurationShowType getDurationShowType() {
		return durationShowType;
	}
	public int getIcon() {
		return icon;
	}
	public int getOnIconId() {
		return onIconId;
	}
	public int getOffIconId() {
		return offIconId;
	}
	public boolean isSimplePck() {
		return simplePck;
	}
	public int getIconPriority() {
		return iconPriority;
	}
	public int getTooltipStrId() {
		return tooltipStrId;
	}
	public int getNewStrId() {
		return newStrId;
	}
	public int getEndStrId() {
		return endStrId;
	}
	public boolean isGood() {
		return isGood;
	}
	public int getOverlapBuffIcon() {
		return overlapBuffIcon;
	}
	public int getMainTooltipStrId() {
		return mainTooltipStrId;
	}
	public int getBuffIconPriority() {
		return buffIconPriority;
	}
	public int getBuffGroupId() {
		return buffGroupId;
	}
	public int getBuffGroupPriority() {
		return buffGroupPriority;
	}
	public int getExpireDuration() {
		return expireDuration;
	}
	public eBoostType getBoostType() {
		return boostType;
	}
	public boolean isPassiveSpell() {
		return isPassiveSpell;
	}
	
}


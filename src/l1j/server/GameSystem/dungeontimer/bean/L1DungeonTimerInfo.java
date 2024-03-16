package l1j.server.GameSystem.dungeontimer.bean;

import javolution.util.FastTable;
import l1j.server.server.serverpackets.S_UserPlayInfoNoti.eChargedTimeMapGroup;

public class L1DungeonTimerInfo {
	private int timerId;// 타이머 타입
	private String descId;
	private TimerUseType useType;// 사용 타입(0:어카운트, 1:케릭터)
	private FastTable<Integer> mapIds;// 타이머 맵 리스트
	private int timerValue;// 시간
	private int bonusLevel, bonusValue, pccafeBonusValue;// 추가 시간
	private TimerResetType resetType;// 초기화 타입(0:없음, 1:매일, 2:수요일)
	private int minLimitLevel, maxLimitLevel;// 레벨제한
	private int serialId;
	private String serialDescId;
	private int maxChargeCount;
	private eChargedTimeMapGroup group;
	
	public L1DungeonTimerInfo(int timerId, String descId, TimerUseType useType, FastTable<Integer> mapIds, int timerValue, int bonusLevel, int bonusValue, int pccafeBonusValue,
			TimerResetType resetType, int minLimitLevel, int maxLimitLevel, int serialId, String serialDescId, int maxChargeCount, eChargedTimeMapGroup group) {
		this.timerId			= timerId;
		this.descId				= descId;
		this.useType			= useType;
		this.mapIds				= mapIds;
		this.timerValue			= timerValue;
		this.bonusLevel			= bonusLevel;
		this.bonusValue			= bonusValue;
		this.pccafeBonusValue	= pccafeBonusValue;
		this.resetType			= resetType;
		this.minLimitLevel		= minLimitLevel;
		this.maxLimitLevel		= maxLimitLevel;
		this.serialId			= serialId;
		this.serialDescId		= serialDescId;
		this.maxChargeCount		= maxChargeCount;
		this.group				= group;
	}
	
	public int getTimerId() {
		return timerId;
	}
	public String getDescId() {
		return descId;
	}
	public TimerUseType getUseType() {
		return useType;
	}
	public FastTable<Integer> getMapIds() {
		return mapIds;
	}
	public int getTimerValue() {
		return timerValue;
	}
	public int getBonusLevel() {
		return bonusLevel;
	}
	public int getBonusValue() {
		return bonusValue;
	}
	public int getPcCafeBonusValue() {
		return pccafeBonusValue;
	}
	public TimerResetType getResetType() {
		return resetType;
	}
	public int getMinLimitLevel() {
		return minLimitLevel;
	}
	public int getMaxLimitLevel() {
		return maxLimitLevel;
	}
	public int getSerialId() {
		return serialId;
	}
	public String getSerialDescId() {
		return serialDescId;
	}
	public int getMaxChargeCount() {
		return maxChargeCount;
	}
	public eChargedTimeMapGroup getGroup() {
		return group;
	}
}


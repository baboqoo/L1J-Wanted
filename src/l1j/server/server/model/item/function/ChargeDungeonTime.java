package l1j.server.server.model.item.function;

import java.sql.Timestamp;

import javolution.util.FastMap;
import l1j.server.GameSystem.dungeontimer.L1DungeonTimer;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimeChargedItem;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerUser;
import l1j.server.GameSystem.dungeontimer.bean.TimerResetType;
import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerLoader;
import l1j.server.common.bin.ChargedTimeMapCommonBinLoader;
import l1j.server.common.bin.chargedtimemap.ChargedTimeMapDataT;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.inventory.S_ChargedMapTimeInfoNoti;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class ChargeDungeonTime extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ChargeDungeonTime(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1DungeonTimeChargedItem obj	= L1DungeonTimerLoader.getDungeonTimerItem(this.getItemId());
			if (obj.getGroupId() > 0) {
				chargedMapTimeInfo(pc, obj.getGroupId());
				return;
			}
			if (obj.getTimerId() == 0) {
				return;
			}
			L1DungeonTimerUser timer = pc.getDungoenTimer().getTimers().get(obj.getTimerId());
			if (timer == null) {
				return;
			}
			if (timer.getInfo().getMaxChargeCount() > 0) {
				chargeCount(pc, timer);
			} else {
				chargeTime(pc, timer);
			}
		}
	}
	
	/**
	 * 충전
	 * @param pc
	 * @param timer
	 */
	private void chargeTime(L1PcInstance pc, L1DungeonTimerUser timer){
		if (timer.getRemainSecond() == 0) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("이미 던전시간이 초기화되어 있습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1065), true), true);
			return;
		}
		pc.resetTimerItemId = this.getItemId();
		//S_MessageYN bossYn = new S_MessageYN(C_Attr.MSGCODE_DUNGEON_TIMER_RESET, C_Attr.YN_MESSAGE_CODE, "정말로 던전 시간을 초기화 하시겠습니까?");
		S_MessageYN bossYn = new S_MessageYN(C_Attr.MSGCODE_DUNGEON_TIMER_RESET, C_Attr.YN_MESSAGE_CODE, "Do you really want to reset the dungeon timer?");
		pc.sendPackets(bossYn, true);
	}
	
	/**
	 * count 충전
	 * @param pc
	 * @param timer
	 */
	private void chargeCount(L1PcInstance pc, L1DungeonTimerUser timer){
		if (timer.getChargeCount() >= timer.getInfo().getMaxChargeCount()) {
			pc.sendPackets(L1ServerMessage.sm7577);// 충전불가: 충전 제한 횟수 소진
			return;
		}
		if (timer.getResetTime() == null 
				|| ((timer.getInfo().getResetType() == TimerResetType.WEEK && CommonUtil.isWeekResetCheck(timer.getResetTime()))
				|| (timer.getInfo().getResetType() == TimerResetType.DAY && CommonUtil.isDayResetTimeCheck(timer.getResetTime())))) {
			if (timer.getResetTime() != null) {
				timer.getResetTime().setTime(System.currentTimeMillis());
			} else {
				timer.setResetTime(new Timestamp(System.currentTimeMillis()));
			}
		}
		timer.setChargeCount(timer.getChargeCount() + 1);
		pc.getInventory().removeItem(this, 1);
	}
	
	/**
	 * 충전 맵 선택
	 * @param pc
	 * @param groupId
	 */
	void chargedMapTimeInfo(L1PcInstance pc, int groupId) {
		ChargedTimeMapDataT dataT = ChargedTimeMapCommonBinLoader.get_data();
		if (dataT == null) {
			return;
		}
		java.util.LinkedList<S_ChargedMapTimeInfoNoti.InfoT> info = null;
		L1DungeonTimer handler						= pc.getDungoenTimer();
		FastMap<Integer, L1DungeonTimerUser> map	= handler.getTimers();
		ChargedTimeMapDataT.MultiGroupListT listT	= dataT.get_multi_group(groupId);
		if (listT == null) {
			return;
		}
		for (int component : listT.get_components()) {
			for (L1DungeonTimerUser timer : map.values()) {
				if (component == timer.getInfo().getGroup().toInt()) {
					int charged_count		= timer.getChargeCount();// 충전 횟수
					int maxTime				= handler.getTimerValue(timer.getInfo());
					int extra_charged_time	= maxTime - timer.getRemainSecond();
					int charged_time		= charged_count * 3600;
					if (extra_charged_time < 0) {
						charged_time		+= extra_charged_time;
						extra_charged_time	= 0;
					}
					if (info == null) {
						info = new java.util.LinkedList<S_ChargedMapTimeInfoNoti.InfoT>();
					}
					info.add(new S_ChargedMapTimeInfoNoti.InfoT(component, charged_time, charged_count, extra_charged_time));
					break;
				}
			}
		}
		if (info == null) {
			return;
		}
		pc.sendPackets(new S_ChargedMapTimeInfoNoti(info, this.getItem().getItemNameId(), listT.get_group()), true);
		for (S_ChargedMapTimeInfoNoti.InfoT val : info) {
			val.clear();
			val = null;
		}
		info.clear();
		info = null;
	}
}



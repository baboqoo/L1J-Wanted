package l1j.server.server.clientpackets.proto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.Config;
import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.attendance.bean.AttendanceItem;
import l1j.server.GameSystem.attendance.bean.AttendanceRandomItem;
import l1j.server.GameSystem.attendance.bean.AttendanceRewardHistory;
import l1j.server.common.data.AttendanceBonusType;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.attendance.S_AttenDanceReward;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.CommonUtil;

public class A_AttendanceReward extends ProtoHandler {
	protected A_AttendanceReward(){}
	private A_AttendanceReward(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int attendanceId;
	private AttendanceGroupType groupType;
	private int seasonId;

	void parse() {
		readP(1);// 0x08
		attendanceId = readC();
		
		readP(1);// 0x10
		groupType = AttendanceGroupType.fromInt(readC());
		
		readP(1);// 0x18
		seasonId = readC();
	}
	
	boolean validation() {
		return groupType != null && AttendanceGroupType.isUse(groupType) && seasonId == groupType.getSeasonId();
	}
	
	@Override
	protected void doWork() throws Exception {
		try {
			if (!Config.ATTEND.ATTENDANCE_ACTIVE || _pc == null || _pc.isGhost() || _pc.getSkill().hasSkillEffect(L1SkillId.ATTENDANCE_DELAY)) {
				return;
			}
			AttendanceAccount attendAccount = _pc.getAccount().getAttendance();
			if (attendAccount == null) {
				return;
			}
			_pc.getSkill().setSkillEffect(L1SkillId.ATTENDANCE_DELAY, 3000);
			
			parse();
			if (!validation()) {
				return;
			}
			if (groupType == AttendanceGroupType.PC_CAFE && !_pc.isPCCafe()) {
				return;
			}
			if (groupType.isTab() && !attendAccount.isGroupOpen(groupType)) {
				return;
			}
			
			byte status = attendAccount.getGroupData(groupType)[attendanceId - 1];
			if (status == 2) {
				//System.out.println(String.format("★☆★ 중계기(ATTENDANCE) 의심 유저 : NAME[%s] 이미 보상 받은 출석체크입니다. ★☆★", _pc.getName()));
				System.out.println(String.format("--- Suspected repeater (ATTENDANCE) user: NAME[%s] This is an attendance check that has already received compensation. ---", _pc.getName()));
				return;
			}
			if (status != 1) {
				//System.out.println(String.format("★☆★ 중계기(ATTENDANCE) 의심 유저 : NAME[%s] 완료되지 않은 출석체크입니다. ★☆★", _pc.getName()));
				System.out.println(String.format("★☆★ Suspected repeater (ATTENDANCE) user: NAME[%s] Attendance check not completed. ★☆★", _pc.getName()));
				return;
			}
			
			AttendanceItem rewardInfo = attendAccount.getGroupItems(groupType).get(attendanceId);
			if (rewardInfo == null) {
				System.out.println(String.format(
						"[A_AttendanceReward] REWARD_INFO_NOT_FOUND : INDEX(%d), CHAR_NAME(%s)",
						attendanceId, _pc.getName()));
				return;
			}
			L1ItemInstance rewardItem = ItemTable.getInstance().createItem(rewardInfo.getItemId());
			if (rewardItem == null) {
				System.out.println(String.format(
						"[A_AttendanceReward] REWARD_ITEM_NOT_FOUND : ITEM_ID(%d), CHAR_NAME(%s)",
						rewardInfo.getItemId(), _pc.getName()));
				return;
			}
			
			if (rewardInfo.getEnchant() != 0) {
				rewardItem.setEnchantLevel(rewardInfo.getEnchant());
			}
			rewardItem.setCount(rewardInfo.getCount());
			boolean isRandomReward = rewardInfo.getBonusType() == AttendanceBonusType.RandomDiceItem;

			if (isRandomReward) {
				Map<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>> items = attendAccount.getRandomItems(groupType);
				if (items == null) {
					return;
				}
				HashMap<Integer, ArrayList<AttendanceRandomItem>> radomItemInfo = items.get(attendanceId);
				if (radomItemInfo == null || radomItemInfo.isEmpty()) {
					return;
				}
				ArrayList<AttendanceRandomItem> randomItemList = radomItemInfo.get(radomItemInfo.keySet().iterator().next());
				AttendanceRandomItem randomItem = getRandomReward(randomItemList);// 하나의 아이템 선택
				if (randomItem == null) {
					return;
				}
				L1ItemInstance randomRewardItem = ItemTable.getInstance().createItem(randomItem.getItemId());// 아이템 생성
				randomRewardItem.setCount(randomItem.getCount());// 개수 설정
				_pc.sendPackets(new S_AttenDanceReward(attendanceId, groupType, randomRewardItem, randomItem.isBroadcast()), true);
				Runnable r = () -> {
					reward(_pc, null, randomItem.getIndex(), randomRewardItem, randomItem.isBroadcast());
				};
				GeneralThreadPool.getInstance().schedule(r, 5000L);
				addAttendRewardHistory(groupType, attendAccount, attendanceId, randomRewardItem.getItem().getItemNameId(), randomRewardItem.getCount());
			} else {
				_pc.sendPackets(new S_AttenDanceReward(attendanceId, groupType, rewardItem, rewardInfo.isBroadcast()), true);
				addAttendRewardHistory(groupType, attendAccount, attendanceId, rewardItem.getItem().getItemNameId(), rewardItem.getCount());
			}
			
			reward(_pc, groupType, attendanceId, rewardItem, rewardInfo.isBroadcast());
			setAttendClear(groupType, attendAccount, attendanceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 그룹의 해당 출석의 상태값을 변경한다.
	 * @param groupType
	 * @param attendAccount
	 * @param index
	 */
	void setAttendClear(AttendanceGroupType groupType, AttendanceAccount attendAccount, int index){
		attendAccount.getGroupData(groupType)[index - 1] = 2;// 상태 변경
		if (attendAccount.isAllFinished(groupType)) {// 출석체크 모두 완료시 재 설정
			attendAccount.setGroupData(groupType, new byte[attendAccount.getGroupItems(groupType).size()]);
			attendAccount.getRewardHisoty().remove(groupType);
		}
	}
	
	/**
	 * 보상받은 내역을 추가한다.
	 * @param groupType
	 * @param attendAccount
	 * @param index
	 * @param itemDescId
	 * @param itemCount
	 */
	void addAttendRewardHistory(AttendanceGroupType groupType, AttendanceAccount attendAccount, int index, int itemDescId, int itemCount){
		ArrayList<AttendanceRewardHistory> historyList = attendAccount.getRewardHisoty().get(groupType);
		if (historyList == null) {
			historyList = new ArrayList<AttendanceRewardHistory>();
			attendAccount.getRewardHisoty().put(groupType, historyList);
		}
		historyList.add(new AttendanceRewardHistory(groupType, index, itemDescId, itemCount));
	}
	
	/**
	 * 랜덤 아이템 보상 중 확률에 따라 하나의 아이템을 선택
	 * @param randomItemList
	 * @return AttendanceRandomItem
	 */
	AttendanceRandomItem getRandomReward(ArrayList<AttendanceRandomItem> randomItemList){
		int rnd = CommonUtil.random(100) + 1;
		int rewardLevel = rnd <= Config.ATTEND.ATTENDANCE_RANDOM_REWARD_LEVEL_5_PROB ? 5 
				: rnd <= Config.ATTEND.ATTENDANCE_RANDOM_REWARD_LEVEL_4_PROB ? 4 
				: rnd <= Config.ATTEND.ATTENDANCE_RANDOM_REWARD_LEVEL_3_PROB ? 3 
				: rnd <= Config.ATTEND.ATTENDANCE_RANDOM_REWARD_LEVEL_2_PROB ? 2 
				: 1;// 획득할 아이템 레벨
		for (AttendanceRandomItem rndItem : randomItemList) {
			if (rndItem.getLevel() == rewardLevel) {
				return rndItem;
			}
		}
		return null;
	}
	
	/**
	 * 보상을 지급한다.
	 * @param pc
	 * @param groupType
	 * @param attendanceId
	 * @param reward
	 * @param broadcast
	 */
	void reward(L1PcInstance pc, AttendanceGroupType groupType, int attendanceId, L1ItemInstance reward, boolean broadcast){
		if (pc == null) {
			return;
		}
		if (reward.getItemId() == L1ItemId.EIN_BLESS_BONUS || reward.getItemId() == L1ItemId.EIN_BLESS_BONUS_100) {
			pc.einGetExcute(100);
			return;
		}
		if (reward.getItemId() == L1ItemId.EIN_BLESS_BONUS_50) {
			pc.einGetExcute(50);
			return;
		}
		if (pc.getInventory().checkAddItem(reward, reward.getCount()) != L1Inventory.OK) return;
		pc.getInventory().storeItem(reward);
		//pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", reward.getDescKr(), reward.getCount())), true);
		pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", reward.getDesc(), reward.getCount())), true);
		
		if (broadcast) {
			L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(5412, Integer.toString(attendanceId), reward.getItem().getDesc()), true);// 누군가가 출석 체크 보상으로 %d 번에서 %s 아이템을 획득하셨습니다.
		}
		if (groupType != null && groupType.getRewardEinhasad() > 0) {
			pc.einGetExcute(groupType.getRewardEinhasad());
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_AttendanceReward(data, client);
	}

}


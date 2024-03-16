package l1j.server.GameSystem.attendance.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.attendance.S_AttenDanceBonusGroupInfo;
import l1j.server.server.serverpackets.attendance.S_AttenDanceBonusInfoExtend;
import l1j.server.server.serverpackets.attendance.S_AttenDanceInfoNoti;
import l1j.server.server.serverpackets.attendance.S_AttenDanceRandomBonusGroupInfo;
import l1j.server.server.serverpackets.attendance.S_AttenDanceRewardItemNoti;

/**
 * 출석체크 담당 클래스
 * 계정에 할당 된다.
 * @author LinOffice
 */
public class AttendanceAccount {
	private String _account;
	private int _dailyCount;// 일일 타이머
	private int currentIndex;// 현재 진행 순번
	private boolean _isCompleted;// 시간 완료 여부
	private Timestamp _resetDate;// 갱신 일자
	private ConcurrentHashMap<AttendanceGroupType, byte[]> _groupDatas;// 그룹별 진행 정보
	private ConcurrentHashMap<AttendanceGroupType, Boolean> _groupOpens;// 잠금 오픈 정보
	private ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, AttendanceItem>> _groupItems;// 그룹별 보상 정보
	private ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>>> _randomItems;// 그룹별 랜덤 보상 정보
	private ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> _rewardHisoty;// 완료 아이템 정보
	
	public AttendanceAccount(String account, 
			int dailyCount,
			boolean isCompleted,
			Timestamp resetDate,
			ConcurrentHashMap<AttendanceGroupType, byte[]> groupDatas, 
			ConcurrentHashMap<AttendanceGroupType, Boolean> groupOpens, 
			ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, AttendanceItem>> groupItems,
			ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>>> randomItems,
			ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> rewardHisoty) {
		this._account		= account;
		this._dailyCount	= dailyCount;
		this._isCompleted	= isCompleted;
		this._resetDate		= resetDate;
		this._groupDatas	= groupDatas;
		this._groupOpens	= groupOpens;
		this._groupItems	= groupItems;
		this._randomItems	= randomItems;
		this._rewardHisoty	= rewardHisoty;
		updateCurrentIndex();
	}
	
	public String getAccount() {
		return _account;
	}
	
	public int getDailyCount() {
		return _dailyCount;
	}
	
	public boolean isCompleted() {
		return _isCompleted;
	}
	
	public Timestamp getResetDate() {
		return _resetDate;
	}

	public ConcurrentHashMap<AttendanceGroupType, byte[]> getGroupData() {
		return _groupDatas;
	}
	public byte[] getGroupData(AttendanceGroupType type){
		return _groupDatas.get(type);
	}
	public void setGroupData(AttendanceGroupType type, byte[] data){
		_groupDatas.put(type, data);
	}
	
	public ConcurrentHashMap<AttendanceGroupType, Boolean> getGroupOpens() {
		return _groupOpens;
	}
	public boolean isGroupOpen(AttendanceGroupType type) {
		if (!_groupOpens.containsKey(type)) {
			return true;
		}
		return _groupOpens.get(type);
	}
	public void setGroupOpen(AttendanceGroupType type, boolean flag){
		_groupOpens.put(type, flag);
	}
	
	public ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, AttendanceItem>> getGroupItems() {
		return _groupItems;
	}
	public HashMap<Integer, AttendanceItem> getGroupItems(AttendanceGroupType type) {
		return _groupItems.get(type);
	}

	public ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>>> getRandomItems() {
		return _randomItems;
	}
	public HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>> getRandomItems(AttendanceGroupType type){
		return _randomItems.get(type);
	}
	
	public ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> getRewardHisoty() {
		return _rewardHisoty;
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	/**
	 * 일일 출석체크 완료 진행
	 * @return boolean
	 */
	public boolean isProgress() {
		if (_isCompleted) {
			return false;
		}
		if (++_dailyCount >= Config.ATTEND.ATTENDANCE_DAILY_MINUTE) {
			_dailyCount = 0;
			_isCompleted = true;
			return true;
		}
		return false;
	}
	
	/**
	 * 보상 전체 완료 여부
	 * @param type
	 * @return boolean
	 */
	public boolean isAllFinished(AttendanceGroupType type){
		byte[] data = _groupDatas.get(type);
		if (data == null) {
			return false;
		}
		for (byte b : data) {
			if (b != 0x02) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 순번 업데이트
	 */
	public void updateCurrentIndex() {
		byte[] data = _groupDatas.get(AttendanceGroupType.NORMAL);
		for (int i=0; i<data.length; i++) {
			if (data[i] == 0) {
				currentIndex = i;
				break;
			}
		}
	}
	
	/**
	 * 로그인 및 리로드시 갱신에 필요한 모든 패킷을 보낸다.
	 * @param pc
	 */
	public void sendPacket(L1PcInstance pc) {
		ArrayList<AttendanceGroupType> useList = AttendanceGroupType.getUseList();
		pc.sendPackets(S_AttenDanceBonusInfoExtend.SEASON_FIRST);
		for (AttendanceGroupType attendType : useList) {
			pc.sendPackets(new S_AttenDanceBonusGroupInfo(attendType, pc, 0), true);
		}
		for (AttendanceGroupType attendType : useList) {
			pc.sendPackets(new S_AttenDanceRandomBonusGroupInfo(attendType, pc, this, 0), true);
		}
		pc.sendPackets(S_AttenDanceBonusInfoExtend.SEASON_SECOND);
		for (AttendanceGroupType attendType : useList) {
			pc.sendPackets(new S_AttenDanceBonusGroupInfo(attendType, pc, 1), true);
		}
		for (AttendanceGroupType attendType : useList) {
			pc.sendPackets(new S_AttenDanceRandomBonusGroupInfo(attendType, pc, this, 1), true);
		}
		
		S_AttenDanceRewardItemNoti history = new S_AttenDanceRewardItemNoti(_rewardHisoty);
		pc.sendPackets(new S_AttenDanceInfoNoti(pc, this, 0), true);
		pc.sendPackets(history, false);
		pc.sendPackets(new S_AttenDanceInfoNoti(pc, this, 1), true);
		pc.sendPackets(history, true);
	}

	/**
	 * 초기화
	 * @param currentTime
	 */
	public void reset(long currentTime){
		_dailyCount		= 0;
		_isCompleted	= false;
		if (_resetDate == null) {
			_resetDate	= new Timestamp(currentTime);
		} else {
			_resetDate.setTime(currentTime);
		}
	}
}


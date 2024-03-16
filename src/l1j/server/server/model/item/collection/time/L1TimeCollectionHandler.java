package l1j.server.server.model.item.collection.time;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.common.bin.TimeCollectionCommonBinLoader;
import l1j.server.common.bin.timecollection.TimeCollection;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionDuration;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionNotiType;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;
import l1j.server.server.monitor.Logger.TimeCollectionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionBuffNoti;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionDataLoadNoti;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionOnLoginedNoti;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionRegistItem;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionReset;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionSetDataNoti;

/**
 * 유저에게 할당된 실렉티스 전시회 핸들러
 * @author LinOffice
 */
public class L1TimeCollectionHandler {
	private final L1PcInstance owner;
	private final ConcurrentHashMap<Integer, L1TimeCollectionUser> DATA;// Key: setId, Value: L1TimeCollectionUser
	private int buffSize;
	
	/**
	 * 전체 데이터 조사
	 * @return ConcurrentHashMap<Integer, L1TimeCollectionUser>
	 */
	public ConcurrentHashMap<Integer, L1TimeCollectionUser> getData() {
		return DATA;
	}
	
	/**
	 * 등록된 컬렉션 조사
	 * @param setId
	 * @return L1TimeCollectionUser
	 */
	public L1TimeCollectionUser getUser(int setId) {
		return DATA.get(setId);
	}
	
	/**
	 * 기본 생성자
	 * @param owner
	 */
	public L1TimeCollectionHandler(L1PcInstance owner) {
		this.owner	= owner;
		this.DATA	= new ConcurrentHashMap<>();
		init();
	}
	
	/**
	 * 세트 생성
	 * @param obj
	 * @return L1TimeCollectionUser
	 */
	L1TimeCollectionUser createUser(L1TimeCollection obj) {
		return new L1TimeCollectionUser(
				owner.getId(), obj.getGroupId(), obj.getSet().get_ID(), 
				new ConcurrentHashMap<>(), 
				false, 0, 
				L1TimeCollectionBuffType.fromInt(
						TimeCollectionCommonBinLoader.getData().get_BuffSelect().get_User().get(owner.getType())
						.get_BuffType()), 
				null, 0, obj);
	}
	
	/**
	 * 컬렉션 등록
	 * @param obj
	 * @param item
	 * @param slotIndex
	 * @return boolean
	 */
	public boolean regist(L1TimeCollection obj, L1ItemInstance item, int slotIndex) {
		L1TimeCollectionUser user = DATA.get(obj.getSet().get_ID());
		if (user == null) {
			user = createUser(obj);
		}
		// 이미 완성하여 버프를 받고 있음
		if (user.getBuffTimer() != null) {
			return false;
		}
		// 슬롯에 아이템 등록
		user.putSlots(slotIndex, item);

		ConcurrentHashMap<Integer, L1ItemInstance> slots = user.getSlots();
		
		// TODO 슬롯 완성
		if (slots.size() == obj.getSlotSize()) {
			user.setRegistComplet(true);
			int sumEnchant = 0;
			
			// 세트의 가동 시간
			L1TimeCollectionDuration duration = obj.getDuration();
			long buffDuration	= duration.getDefaultTime();// 기본 완성 시간
			
			// 컬렉션의 합산 인챈트와 버프시간 계산
			L1ItemInstance registItem = null;
			for (Map.Entry<Integer, L1ItemInstance> entry : slots.entrySet()) {// key:slotIndex, value:L1ItemInstance
				registItem		= entry.getValue();
				sumEnchant		+= registItem.getEnchantLevel();
				buffDuration	+= duration.getBonusTime(entry.getKey(), registItem.getEnchantLevel());// 인첸트 수치 보너스 시간
			}
			// 합산 강화 수치
			user.setSumEnchant(sumEnchant);
			
			// 버프시간 설정
			long currentTime = System.currentTimeMillis();
			if (user.getBuffTime() == null) {
				user.setBuffTime(new Timestamp(buffDuration + currentTime));
			} else {
				user.getBuffTime().setTime(buffDuration + currentTime);
			}
			
			activeAblity(user);
			owner.sendPackets(new S_TimeCollectionSetDataNoti(owner, user), true);
		}
		
		owner.sendPackets(new S_TimeCollectionRegistItem(owner, user, slotIndex, item), true);
		
		if (L1TimeCollectionUserLoader.getInstance().insert(user)) {
			DATA.put(user.getSetId(), user);
			LoggerInstance.getInstance().addTimeCollection(TimeCollectionType.REGIST, owner, user, item);
			return true;
		}
		return false;
	}
	
	/**
	 * 컬렉션 초기화
	 * @param obj
	 * @return boolean
	 */
	public boolean reset(L1TimeCollection obj, L1TimeCollectionNotiType status) {
		L1TimeCollectionUser user = DATA.remove(obj.getSet().get_ID());
		if (user == null) {
			System.out.println(String.format(
					"[L1TimeCollectionHandler] RESET_FAILE_REASON(user == null) NAME(%s) ID(%d)", 
					owner.getName(), obj.getSet().get_ID()));
			return false;
		}
		
		// 가동 중인 버프
		if (user.isBuffActive()) {
			user.doBonus(owner, false);
			user.setBuffTime(null);
			user.setSumEnchant(0);
			user.getSlots().clear();
			user.getBuffTimer().cancel();
			user.setBonusList(null);
			user.setBuffTimer(null);
			user.setRegistComplet(false);
			user.setBuffIndex(--buffSize);
			user.setRefillCount(0);
			owner.sendPackets(new S_TimeCollectionBuffNoti(user, status), true);
		}
		
		if (L1TimeCollectionUserLoader.getInstance().delete(user)) {
			owner.sendPackets(new S_TimeCollectionReset(user), true);
			LoggerInstance.getInstance().addTimeCollection(TimeCollectionType.RESET, owner, user, null);
			return true;
		}
		return false;
	}
	
	/**
	 * 버프를 가동 시킨다.
	 * @param user
	 */
	public void activeAblity(L1TimeCollectionUser user) {
		if (!user.isRegistComplet()) {
			return;
		}
		long interval = user.restBuffTime();
		if (interval <= 0L) {
			return;
		}
		
		TimeCollection.GroupT.SetT.BuffTypeT buffTypeT = user.getObj().getSet().get_BuffType(user.getBuffType().toInt());
		if (buffTypeT == null) {
			System.out.println(String.format(
					"[L1TimeCollectionHandler] BUFF_TYPE_T_NOT_FOUND : SUM_ENCHANT(%d), BUFF_TYPE(%s), NAME(%s)",
					user.getSumEnchant(), user.getBuffType().toName(), owner.getName()));
			return;
		}
		// 완성 옵션
		user.setBonusList(buffTypeT.get_bonus_list(user.getSumEnchant()));
		
		// 옵션 부여
		user.doBonus(owner, true);
		
		// 버프 타이머 없음
		if (user.getBuffTimer() == null)
		{
			user.setBuffIndex(++buffSize);
			user.setBuffTimer(new L1TimeCollectionTimer(owner, user));// 타이머 생성
			GeneralThreadPool.getInstance().schedule(user.getBuffTimer(), interval);// 타이머 가동
		}
		owner.sendPackets(new S_TimeCollectionBuffNoti(user, L1TimeCollectionNotiType.NEW), true);
	}
	
	/**
	 * 최초 생성시 데이터 세팅
	 */
	private void init() {
		ArrayList<L1TimeCollectionUser> list = L1TimeCollectionUserLoader.getUserList(owner.getId());
		if (list != null && !list.isEmpty()) {
			for (L1TimeCollectionUser obj : list) {
				DATA.put(obj.getSetId(), obj);
				activeAblity(obj);
			}
		}
		sendLoginPacket();
	}
	
	/**
	 * 로그인 출력 패킷
	 */
	private void sendLoginPacket() {
		owner.sendPackets(new S_TimeCollectionDataLoadNoti(owner, this), true);
		
		if (DATA != null && !DATA.isEmpty()) {
			TimeCollection.ExtraTimeSectionT sectionT = TimeCollectionCommonBinLoader.getData().get_ExtraTimeSection();
			java.util.LinkedList<S_TimeCollectionOnLoginedNoti.setData> setDatas = null;
			for (L1TimeCollectionUser user : DATA.values()) {
				if (!user.isBuffActive()) {
					continue;
				}
				int remainRefil = 0;
				int extraTimerId = user.getObj().getSet().get_ExtraTimeId();
				if (extraTimerId > 0) {
					TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT levelT = sectionT.get_ExtraTime(extraTimerId).get_EnchantLevel(user.getSumEnchant());
					if (levelT != null) {
						remainRefil = levelT.get_Limit() - user.getRefillCount();
					}
				}
				if (setDatas == null) {
					setDatas = new java.util.LinkedList<S_TimeCollectionOnLoginedNoti.setData>();
				}
				setDatas.add(new S_TimeCollectionOnLoginedNoti.setData(
								user.getGroupId(), user.getSetId(), 
								(int)(user.restBuffTime() / 1000), 
								remainRefil));
			}
			
			if (setDatas != null) {
				owner.sendPackets(new S_TimeCollectionOnLoginedNoti(setDatas), true);
				setDatas.clear();
				setDatas = null;
			}
		}
	}
	
	/**
	 * 메모리 정리
	 */
	public void dispose() {
		if (DATA != null && !DATA.isEmpty()) {
			for (L1TimeCollectionUser user : DATA.values()) {
				if (user.getBuffTimer() != null) {
					user.getBuffTimer().cancel();
					user.setBuffTimer(null);
				}
			}
		}
		DATA.clear();
	}
}


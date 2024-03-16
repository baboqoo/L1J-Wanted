package l1j.server.server.model.item.collection.time.bean;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.common.bin.timecollection.TimeCollection;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.ablity.ItemAbilityFactory;
import l1j.server.server.model.item.collection.time.L1TimeCollectionTimer;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.utils.StringUtil;

/**
 * 실렉티스 전시회 유저 오브젝트
 * @author LinOffice
 */
public class L1TimeCollectionUser {
	private int charObjId;
	private int groupId;
	private int setId;
	private ConcurrentHashMap<Integer, L1ItemInstance> slots;// Key: slotIndex Value: item
	private boolean registComplet;
	private int sumEnchant;
	private L1TimeCollectionBuffType buffType;
	private Timestamp buffTime;
	private L1TimeCollectionTimer buffTimer;
	private java.util.LinkedList<TimeCollection.BonusT> bonus_list;
	private int buffIndex;
	private int refillCount;
	private L1TimeCollection obj;
	
	public L1TimeCollectionUser(int charObjId, int groupId, int setId,
			ConcurrentHashMap<Integer, L1ItemInstance> slots, boolean registComplet, int sumEnchant,
			L1TimeCollectionBuffType buffType, Timestamp buffTime, int refillCount, L1TimeCollection obj) {
		this.charObjId		= charObjId;
		this.groupId		= groupId;
		this.setId			= setId;
		this.slots			= slots;
		this.registComplet	= registComplet;
		this.sumEnchant		= sumEnchant;
		this.buffType		= buffType;
		this.buffTime		= buffTime;
		this.refillCount		= refillCount;
		this.obj			= obj;
	}

	public int getCharObjId() {
		return charObjId;
	}
	public void setCharObjId(int charObjId) {
		this.charObjId = charObjId;
	}

	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getSetId() {
		return setId;
	}
	public void setSetId(int setId) {
		this.setId = setId;
	}

	public ConcurrentHashMap<Integer, L1ItemInstance> getSlots() {
		return slots;
	}
	public void setSlots(ConcurrentHashMap<Integer, L1ItemInstance> registItem) {
		this.slots = registItem;
	}
	public void putSlots(int slotIndex, L1ItemInstance item){
		if (slots == null) {
			slots = new ConcurrentHashMap<>();
		}
		slots.put(slotIndex, item);
	}

	public boolean isRegistComplet() {
		return registComplet;
	}
	public void setRegistComplet(boolean registComplet) {
		this.registComplet = registComplet;
	}

	public int getSumEnchant() {
		return sumEnchant;
	}
	public void setSumEnchant(int sumEnchant) {
		this.sumEnchant = sumEnchant;
	}

	public L1TimeCollectionBuffType getBuffType() {
		return buffType;
	}
	public void setBuffType(L1TimeCollectionBuffType buffType) {
		this.buffType = buffType;
	}

	public Timestamp getBuffTime() {
		return buffTime;
	}
	public void setBuffTime(Timestamp buffTime) {
		this.buffTime = buffTime;
	}
	
	public boolean isBuffActive(){
		return buffTime != null && buffTimer != null;
	}
	public long restBuffTime(){
		if (buffTime == null) {
			return 0L;
		}
		return buffTime.getTime() - System.currentTimeMillis();
	}
	
	public L1TimeCollectionTimer getBuffTimer() {
		return buffTimer;
	}
	public void setBuffTimer(L1TimeCollectionTimer buffTimer) {
		this.buffTimer = buffTimer;
	}
	
	public java.util.LinkedList<TimeCollection.BonusT> getBonusList() {
		return bonus_list;
	}
	public void setBonusList(java.util.LinkedList<TimeCollection.BonusT> val) {
		this.bonus_list = val;
	}
	
	public int getBuffIndex() {
		return buffIndex;
	}
	public void setBuffIndex(int buffIndex) {
		this.buffIndex = buffIndex;
	}
	
	public int getRefillCount() {
		return refillCount;
	}
	public void setRefillCount(int val) {
		refillCount = val;
	}

	public L1TimeCollection getObj(){
		return obj;
	}
	
	/**
	 * 시간 리필
	 * @param owner
	 * @param count
	 * @param extraTime
	 */
	public void refill(L1PcInstance owner, int count, long extraTime) {
		this.refillCount += count;
		buffTime.setTime(buffTime.getTime() + (extraTime * count));
		
		buffTimer.cancel();
		buffTimer = new L1TimeCollectionTimer(owner, this);
		GeneralThreadPool.getInstance().schedule(buffTimer, restBuffTime());// 타이머 재설정
	}
	
	/**
	 * 보너스 옵션 부여
	 * @param owner
	 * @param flag
	 */
	public void doBonus(L1PcInstance owner, boolean flag) {
		if (bonus_list == null || bonus_list.isEmpty()) {
			return;
		}
		int flag_val = flag ? 1 : -1;
		for (TimeCollection.BonusT bonus : bonus_list) {
			ItemAbilityFactory.doBonus(owner, bonus.get_factory(), bonus.get_value_int() * flag_val);
		}
		owner.sendPackets(new S_OwnCharAttrDef(owner), true);
		owner.sendPackets(new S_OwnCharStatus(owner), true);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("charObjId: ").append(charObjId).append(StringUtil.LineString);
		sb.append("groupId: ").append(groupId).append(StringUtil.LineString);
		sb.append("setId: ").append(setId).append(StringUtil.LineString);
		sb.append("slots size: ").append(slots.size()).append(StringUtil.LineString);
		sb.append("registComplet: ").append(registComplet).append(StringUtil.LineString);
		sb.append("sumEnchant: ").append(sumEnchant).append(StringUtil.LineString);
		sb.append("buffType: ").append(buffType.toName()).append(StringUtil.LineString);
		sb.append("buffTime: ").append(buffTime).append(StringUtil.LineString);
		sb.append("isBuffActive: ").append(isBuffActive()).append(StringUtil.LineString);
		if (bonus_list != null && !bonus_list.isEmpty()) {
			for (TimeCollection.BonusT bonus : bonus_list) {
				sb.append("bonus_list: key = ").append(bonus.get_factory().get_enum_key());
				sb.append("val = ").append(bonus.get_value_int()).append(StringUtil.LineString);
			}
		}
		sb.append("buffIndex: ").append(buffIndex).append(StringUtil.LineString);
		sb.append("refillCount: ").append(refillCount);
		return sb.toString();
	}
	
}


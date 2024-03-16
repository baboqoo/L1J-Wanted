package l1j.server.server.serverpackets.attendance;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.attendance.bean.AttendanceItem;
import l1j.server.common.data.AttendanceBonusType;
import l1j.server.server.Opcodes;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class S_AttenDanceBonusGroupInfo extends ServerBasePacket {
	private static final String S_ATTENDANCE_GROUP_INFO = "[S] S_AttenDanceBonusGroupInfo";
	private byte[] _byte = null;
	public static final int GROUP_INFO	= 0x0224;// 아이템 정보
	
	static final ConcurrentHashMap<AttendanceGroupType, ConcurrentHashMap<Integer, byte[]>> ITEM_BYTES = new ConcurrentHashMap<>();
	
	public S_AttenDanceBonusGroupInfo(AttendanceGroupType groupType, L1PcInstance pc, int seasonNumber) {
		write_init();
		write_groupType(groupType.getGroupId());

		AttendanceAccount account = pc.getAccount().getAttendance();
		Map<Integer, AttendanceItem> list = account.getGroupItems(groupType);
		Set<Integer> keys = list.keySet();
		int index;
		L1ItemInstance dummy = new L1ItemInstance();
		for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
			index = iterator.next();
			if (groupType.getSeasonId() != seasonNumber) {
				write_bonusGroup_empty(index);
				continue;
			}
			
			ConcurrentHashMap<Integer, byte[]> byteMap = ITEM_BYTES.get(groupType);
			if (byteMap == null) {
				byteMap = new ConcurrentHashMap<Integer, byte[]>();
				ITEM_BYTES.put(groupType, byteMap);
			}
			byte[] bonusGroup = byteMap.get(index);
			if (bonusGroup == null) {
				bonusGroup = sendAttendancePacket(dummy, index, list.get(index), pc);
				byteMap.put(index, bonusGroup);
			}
			if (bonusGroup == null) {
				write_bonusGroup_empty(index);
				continue;
			}
			
			write_bonusGroup(bonusGroup);
		}
		dummy = null;
		
		write_seasonNumber(seasonNumber);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(GROUP_INFO);
	}
	
	void write_groupType(int groupType) {
		writeRaw(0x08);// groupType
		writeRaw(groupType);
	}
	
	void write_bonusGroup(byte[] bonusGroup) {
		writeRaw(0x12);// bonusGroup
		writeBytesWithLength(bonusGroup);
	}
	
	void write_bonusGroup_empty(int index) {
		writeRaw(0x12);
		writeRaw(2);
		writeRaw(0x08);
		writeRaw(index);
	}
	
	void write_seasonNumber(int seasonNumber) {
		writeRaw(0x20);// seasonNumber
		writeRaw(seasonNumber);
	}
	
	byte[] sendAttendancePacket(L1ItemInstance dummy, int index, AttendanceItem itemInfo, L1PcInstance pc) {
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			L1Item item;
			ItemTable tb = ItemTable.getInstance();
			
			item = tb.getTemplate(itemInfo.getItemId());
			if (item == null) {
				System.out.println(String.format("[S_AttenDanceBonusGroupInfo] ITEM_TEMPLATE_EMPTY : ITEMID(%d)", itemInfo.getItemId()));
				return null;
			}
			dummy.setItem(item);
			dummy.setCount(itemInfo.getCount());
			dummy.setEnchantLevel(itemInfo.getEnchant());
			dummy.updateItemAbility(pc);
			
			os.writeC(0x08);// attendaceIndex
			os.writeC(index);

			os.writeC(0x12);// bonuses
			os.writeBytesWithLength(get_AttendanceBonusInfo(item, dummy, itemInfo.getBonusType(), pc));
			
			if (itemInfo.getBonusType().toInt() == BonusType.RANDOM_DICE_RULE.toInt()) {
				os.writeC(0x18);// bonusType
				os.writeC(itemInfo.getBonusType().toInt());
			}
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	static final String FREE_BONUS_EINHASAD_100 = "free bonus einhasad eps 100";
	static final List<Integer> EIN_BONUS_ITEM_IDS = Arrays.asList(new Integer[]{
		L1ItemId.EIN_BLESS_BONUS, L1ItemId.EIN_BLESS_BONUS_50, L1ItemId.EIN_BLESS_BONUS_100
	});
	
	byte[] get_AttendanceBonusInfo(L1Item item, L1ItemInstance dummy, AttendanceBonusType bonusType, L1PcInstance pc) {
		AttendanceBonusInfo os = new AttendanceBonusInfo();
		int attrType	= -1;
		//String itemName	= item.getDescKr();
		String itemName	= item.getDescEn();
		if (EIN_BONUS_ITEM_IDS.contains(item.getItemId())) {
			attrType	= 1;
			itemName	= FREE_BONUS_EINHASAD_100;
		}
		os.write_bonusType(bonusType);
		os.write_itemId(item.getItemNameId());
		os.write_itemCount(dummy.getCount());
		os.write_itemName(itemName);
		os.write_itemInteractType(0);
		os.write_itemIcon(item.getIconId());
		os.write_itemBless(item.getBless());
		os.write_itemDesc(item.getDesc());
		os.write_itemExtraDesc(dummy.getStatusBytes(pc));
		os.write_itemAttr(item.getItemType() == L1ItemType.WEAPON ? dummy.getAttrEnchantLevel() : attrType);
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
	}
	
	public enum BonusType{
		NONE(0),
		RANDOM_DICE_RULE(1),
		;
		private int value;
		BonusType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(BonusType v){
			return value == v.value;
		}
		public static BonusType fromInt(int i){
			switch(i){
			case 0:
				return NONE;
			case 1:
				return RANDOM_DICE_RULE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments BonusType, %d", i));
			}
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_ATTENDANCE_GROUP_INFO;
	}
}

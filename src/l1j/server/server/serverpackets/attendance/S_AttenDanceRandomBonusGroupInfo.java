package l1j.server.server.serverpackets.attendance;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.AttendanceTable;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.attendance.bean.AttendanceRandomItem;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class S_AttenDanceRandomBonusGroupInfo extends ServerBasePacket {
	private static final String S_ATTENDANCE_RANDOM_BONUS_GROUP_INFO = "[S] S_AttenDanceRandomBonusGroupInfo";
	private byte[] _byte = null;
	public static final int GROUP_INFO	= 0x0224;// 아이템 정보
	
	static final ConcurrentHashMap<AttendanceGroupType, ConcurrentHashMap<Integer, byte[]>> RANDOM_ITEM_BYTES = new ConcurrentHashMap<>();
	public S_AttenDanceRandomBonusGroupInfo(AttendanceGroupType groupType, L1PcInstance pc, AttendanceAccount attendAccount, int seasonNumber) {
		write_init();
		write_groupType(groupType.getGroupId());
		
		BinaryOutputStream os = new BinaryOutputStream();
		boolean isOpen = groupType == AttendanceGroupType.PC_CAFE ? pc.isPCCafe() : attendAccount.isGroupOpen(groupType);
		if (isOpen) {
			os.writeByte(REFRESH_COST_BYTES);// 교체 비용
			
			ConcurrentHashMap<Integer, byte[]> dataMap = RANDOM_ITEM_BYTES.get(groupType);
			if (dataMap == null) {
				dataMap = new ConcurrentHashMap<Integer, byte[]>();
				RANDOM_ITEM_BYTES.put(groupType, dataMap);
			}
			
			L1ItemInstance dummy = null;
			L1Item temp;
			ItemTable tb = ItemTable.getInstance();
			for (AttendanceRandomItem random : AttendanceTable.getRandomItemList(groupType)) {
				byte[] randomItemDetail = dataMap.get(random.getIndex());
				if (randomItemDetail == null) {
					if (dummy == null) {
						dummy = new L1ItemInstance();
					}
					temp = tb.getTemplate(random.getItemId());
					if (temp == null) {
						System.out.println(String.format("[S_AttenDanceRandomBonusGroupInfo] RANDOM_ITEM_EMPTY : ITEMID(%d)", random.getItemId()));
						continue;
					}
					dummy.setItem(temp);
					dummy.setCount(random.getCount());
					dummy.updateItemAbility(pc);
					
					randomItemDetail = getRandomBonusInfo(temp, dummy, random.getIndex(), pc);
					dataMap.put(random.getIndex(), randomItemDetail);
				}
				
				os.writeC(0x12);// bonusInfo
				os.writeBytesWithLength(randomItemDetail);
			}
			dummy = null;
		}
		
		if (groupType.isTab()) {// 잠금 정보
			byte[] openCostBytes = getOpenTabCostBytes(groupType);
			if (openCostBytes != null) {
				os.writeC(0x1a);// openCost
				os.writeBytesWithLength(openCostBytes);
			}
		}
		
		write_randomGroup(os.getBytes());
		write_seasonNumber(seasonNumber);
		writeH(0x00);
		
		try {
			os.close();
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(GROUP_INFO);
	}
	
	void write_groupType(int groupType) {
		writeRaw(0x08);// groupType
		writeRaw(groupType);
	}
	
	void write_randomGroup(byte[] randomGroup) {
		writeRaw(0x1a);// randomGroup
		writeBytesWithLength(randomGroup);
	}
	
	void write_seasonNumber(int seasonNumber) {
		writeRaw(0x20);// seasonNumber
		writeRaw(seasonNumber);
	}
	
	static final byte[] REFRESH_COST_BYTES = getRefreshCost();
	static byte[] getRefreshCost(){
		BinaryOutputStream os = new BinaryOutputStream();
		for (int refreshStep=1; refreshStep<=5; refreshStep++) {
			os.writeC(0x0a);// refreshCost
			os.writeC(refreshStep == 1 ? 7 : 8);// length
			
			os.writeC(0x08);// name_id
			os.writeC(7);// adena
			
			os.writeC(0x10);// cost
			os.writeBit(refreshStep == 5 ? 100000 : refreshStep == 4 ? 50000 : refreshStep * 10000);
			
			os.writeC(0x18);// refreshStep
			os.writeC(refreshStep);
		}
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
	}
	
	static final byte[] MINUS_BYTES = { (byte) 0x80, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x01 };
	byte[] getRandomBonusInfo(L1Item temp, L1ItemInstance dummy, int index, L1PcInstance pc) {
		S_AttenDanceRandomBonusGroupInfo.RandomBonusInfo os = null;
		try {
			os = new S_AttenDanceRandomBonusGroupInfo.RandomBonusInfo();
			os.write_itemId(temp.getItemNameId());
			os.write_itemCount(dummy.getCount());
			//os.write_itemName(temp.getDescKr());
			os.write_itemName(temp.getDescEn());
			os.write_itemInteractType(0);
			os.write_itemIcon(temp.getIconId());
			os.write_itemBless(temp.getBless());
			os.write_itemDesc(temp.getDesc());
			os.write_itemExtraDesc(dummy.getStatusBytes(pc));
			os.write_itemAttr(-1);
			os.write_index(index);
			return os.getBytes();
		} catch(Exception e) {
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
	
	static final ConcurrentHashMap<AttendanceGroupType, byte[]> OPEN_COST_BYTES = new ConcurrentHashMap<>();
	byte[] getOpenTabCostBytes(AttendanceGroupType groupType){
		byte[] result = OPEN_COST_BYTES.get(groupType);
		if (result != null) {
			return result;
		}
		int consumeItemId	= groupType.getTabOpenItemId();
		L1Item item = ItemTable.getInstance().getTemplate(consumeItemId);
		if (item == null) {
			System.out.println(String.format("[S_AttenDanceRandomBonusGroupInfo] UNLOCK_ITEM_EMPTY : ITEMID(%d)", consumeItemId));
			return null;
		}
		BinaryOutputStream os = new BinaryOutputStream();
		os.writeC(0x08);// nameId 잠금 해재 아이템 데스크아이디
		os.writeBit(item.getItemNameId());
		
		os.writeC(0x10);// cost
		os.writeBit(groupType.getTabOpenItemCount());
		
		result = os.getBytes();
		OPEN_COST_BYTES.put(groupType, result);
		try {
			os.close();
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static class RandomBonusInfo extends BinaryOutputStream {
		public RandomBonusInfo() {
			super();
		}
		
		void write_itemId(int itemId) {
			writeC(0x08);// itemId
			writeBit(itemId);
		}
		
		void write_itemCount(int itemCount) {
			writeC(0x10);// itemCount
			writeBit(itemCount);
		}
		
		void write_itemName(String itemName) {
			writeC(0x1a);// itemName
			writeStringWithLength(itemName);
		}
		
		void write_itemInteractType(int itemInteractType) {
			writeC(0x20);// itemInteractType
			writeBit(itemInteractType);
		}
		
		void write_itemIcon(int itemIcon) {
			writeC(0x28);// itemIcon
			writeBit(itemIcon);
		}
		
		void write_itemBless(int itemBless) {
			writeC(0x30);// itemBless
			writeC(itemBless);
		}
		
		void write_itemDesc(String itemDesc) {
			writeC(0x3a);// itemDesc
			writeStringWithLength(itemDesc);
		}
		
		void write_itemExtraDesc(byte[] itemExtraDesc) {
			writeC(0x42);// itemExtraDesc
			writeBytesWithLength(itemExtraDesc);
		}
		
		void write_itemAttr(int itemAttr) {
			writeC(0x48);// itemAttr
			writeBit(itemAttr);
		}
		
		void write_index(int index) {
			writeC(0x50);// index
			writeBit(index);
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
		return S_ATTENDANCE_RANDOM_BONUS_GROUP_INFO;
	}
}

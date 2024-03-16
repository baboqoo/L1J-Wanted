package l1j.server.server.serverpackets.inventory;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookCategoryObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_FavorBookListNoti extends ServerBasePacket {
	private static final String S_FAVOR_BOOK_LIST_NOTI = "[S] S_FavorBookListNoti";
	private byte[] _byte = null;
	public static final int LIST_NOTI	= 0x0a5a;
	
	public S_FavorBookListNoti(L1PcInstance pc, L1FavorBookInventory favorInv, int listId){
		long currentTime = System.currentTimeMillis();
		write_init();
		if (listId == 0) {
			ConcurrentHashMap<Integer, ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>>> all = L1FavorBookLoader.getAllData();
			if (all != null && !all.isEmpty()) {
				ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>> value = null;
				for (Map.Entry<Integer, ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>>> entry : all.entrySet()) {
					value	= entry.getValue();
					if (value == null || value.isEmpty()) {
						continue;
					}
					ArrayList<L1FavorBookObject> list	= null;
					for (Map.Entry<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>> entry2 : value.entrySet()) {
						list	= entry2.getValue();
						if (list == null || list.isEmpty()) {
							continue;
						}
						if (entry2.getKey().getEndDate() != null && entry2.getKey().getEndDate().getTime() <= currentTime) {
							continue;
						}
						write_category_info(pc, favorInv, S_FavorBookListNoti.eType.fromInt(entry.getKey()), entry2.getKey(), list);
					}
				}
			}
		} else {
			ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>> map = L1FavorBookLoader.getListToId(listId);
			if (map != null && !map.isEmpty()) {
				for (Map.Entry<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>> entry : map.entrySet()) {
					if (entry.getKey().getEndDate() != null && entry.getKey().getEndDate().getTime() <= currentTime) {
						continue;
					}
					write_category_info(pc, favorInv, S_FavorBookListNoti.eType.fromInt(listId), entry.getKey(), entry.getValue());
				}
			}
		}
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST_NOTI);
	}
	
	void write_category_info(L1PcInstance pc, L1FavorBookInventory favorInv, S_FavorBookListNoti.eType type, L1FavorBookCategoryObject category, ArrayList<L1FavorBookObject> list) {
		writeRaw(0x0a);// category_info
		writeBytesWithLength(getCategoryInfoBytes(pc, favorInv, type, category, list));
	}
	
	byte[] getCategoryInfoBytes(L1PcInstance pc, L1FavorBookInventory favorInv, S_FavorBookListNoti.eType type, L1FavorBookCategoryObject category, ArrayList<L1FavorBookObject> list){
		CategoryT os = null;
		try {
			os = new CategoryT();
			os.write_desc(category.getDesc());
			os.write_start_date(category.getStartDateToString());
			os.write_end_date(category.getEndDateToString());
			os.write_type(type);
			for (L1FavorBookObject obj : list) {
				L1FavorBookUserObject user = favorInv.getUser(obj.getCategory(), obj.getSlotId());
				os.write_slot_info(getSlotInfoBytes(pc, obj, user, user == null ? null : user.getCurrentItem()));// 길이
			}
			return os.getBytes();
		} catch(Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    }
		return null;
	}
	
	byte[] getSlotInfoBytes(L1PcInstance pc, L1FavorBookObject obj, L1FavorBookUserObject user, L1ItemInstance item){
		SlotInfoTStream os = null;
		try {
			os = new SlotInfoTStream(pc, obj, user, item);
			return os.getBytes();
		} catch(Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    }
		return null;
	}
	
	public static class CategoryT extends BinaryOutputStream {
		CategoryT() {
			super();
		}
		
		void write_desc(String desc) {
			writeC(0x0a);// desc
			writeStringWithLength(desc);
		}
		
		void write_start_date(String start_date) {
			writeC(0x12);// start_date
			writeStringWithLength(start_date);
		}
		
		void write_end_date(String end_date) {
			writeC(0x1a);// end_date
			writeStringWithLength(end_date);
		}
		
		void write_type(S_FavorBookListNoti.eType type) {
			writeC(0x20);// type
			writeC(type.value);
		}
		
		void write_slot_info(byte[] slot_info) {
			writeC(0x2a);// slot_info
			writeBytesWithLength(slot_info);
		}
	}
	
	public enum eType{
		PERMANENT(1),	// 영구적인
		EVENT(2),		// 이벤트
		;
		private int value;
		eType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eType v){
			return value == v.value;
		}
		public static eType fromInt(int i){
			switch(i){
			case 1:
				return PERMANENT;
			case 2:
				return EVENT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eType, %d", i));
			}
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_FAVOR_BOOK_LIST_NOTI;
	}
}


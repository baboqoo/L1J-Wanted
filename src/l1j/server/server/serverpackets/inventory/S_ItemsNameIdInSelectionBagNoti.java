package l1j.server.server.serverpackets.inventory;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemSelectorTable;
import l1j.server.server.datatables.ItemSelectorTable.SelectorData;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class S_ItemsNameIdInSelectionBagNoti extends ServerBasePacket {
	private static final String S_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI = "[S] S_ItemsNameIdInSelectionBagNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x09ca;
	
	private static final FastMap<Integer, byte[]> ITEM_INFOS_BYTE = new FastMap<Integer, byte[]>();
	public static void reload(){
		ITEM_INFOS_BYTE.clear();
	}
	
	public S_ItemsNameIdInSelectionBagNoti(L1ItemInstance useItem){
		write_init();
		byte[] itemInfos = ITEM_INFOS_BYTE.get(useItem.getItemId());
		if (itemInfos != null) {
			writeByte(itemInfos);
		} else {
			byte[] infos = putItemInfos(useItem.getItemId());
			if (infos != null) {
				writeByte(infos);
			}
		}
		write_bag_obj_id(useItem.getId());
		write_bag_name_id(useItem.getItem().getItemNameId());
        writeH(0x00);
	}
	
	byte[] putItemInfos(int bag_id){
		BinaryOutputStream os = null;
		try {
			L1Item temp = null;
			FastTable<SelectorData> list = ItemSelectorTable.getSelectorInfo(bag_id);
			if (list == null || list.isEmpty()) {
				System.out.println(String.format("[S_ItemsNameIdInSelectionBagNoti] BAG_ITEM_NOT_FOUND : BAG_ITEMID(%d)", bag_id));
				return null;
			}
			os = new BinaryOutputStream();
			for (SelectorData data : list) {
				temp = ItemTable.getInstance().getTemplate(data._selectItemId);
				if (temp == null) {
					System.out.println(String.format("[S_ItemsNameIdInSelectionBagNoti] DATA_ITEM_NOT_FOUND : ITEMID(%d)", data._selectItemId));
					continue;
				}
				int nameId		= temp.getItemNameId();
				int count		= data._count;
				int enchant		= data._enchant;
				int attr		= data._attr;
				
				int length	= os.getBitSize(nameId) + os.getBitSize(count) + 2;
				if (enchant > 0) {
					length	+= os.getBitSize(enchant) + 1;
				}
				if (attr > 0) {
					length	+= 4;
				}
				
				os.writeC(0x0a);// item_infos
				os.writeC(length);
				
				os.writeC(0x08);// name_id
				os.writeBit(nameId);
				
				if (attr > 0) {
					os.writeC(0x10);// elemental_enchant_type
					os.writeC(L1ItemInstance.getElementalEnchantType(attr));
					
					os.writeC(0x18);// elemental_enchant_value
					os.writeC(L1ItemInstance.getElementalEnchantValue(attr));
				}
				
				if (enchant > 0) {
					os.writeC(0x20);// enchanct
					os.writeBit(enchant);
				}
				
				os.writeC(0x28);// item_count
				os.writeBit(count);
			}
			ITEM_INFOS_BYTE.put(bag_id, os.getBytes());
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
	
	public S_ItemsNameIdInSelectionBagNoti(L1ItemInstance useItem, FastTable<Integer> list){
		write_init();
		write_item_infos(list);
		write_bag_obj_id(useItem.getId());
		write_bag_name_id(useItem.getItem().getItemNameId());
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_item_infos(SelectorData data, int name_id, int bless_code){
		S_ItemsNameIdInSelectionBagNoti.ItemInfo os = null;
		try {
			os = new S_ItemsNameIdInSelectionBagNoti.ItemInfo();
			os.write_name_id(name_id);
			if (data._attr > 0) {
				os.write_elemental_enchant_type(L1ItemInstance.getElementalEnchantType(data._attr));
				os.write_elemental_enchant_value(L1ItemInstance.getElementalEnchantValue(data._attr));
			}
			if (data._enchant > 0) {
				os.write_enchant(data._enchant);
			}
			os.write_item_count(data._count);
			if (bless_code != 1) {
				os.write_bless_code(bless_code);
			}
			writeRaw(0x0a);
			writeBytesWithLength(os.getBytes());
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
	}
	
	void write_item_infos(FastTable<Integer> list){
		L1Item temp = null;
		for (int itemId : list) {
			temp = ItemTable.getInstance().getTemplate(itemId);
			if (temp == null) {
				System.out.println(String.format("[S_ItemsNameIdInSelectionBagNoti] DATA_MELT_ITEM_NOT_FOUND : ITEMID(%d)", itemId));
				continue;
			}
			int nameId = temp.getItemNameId();
			writeRaw(0x0a);
			writeRaw(getBitSize(nameId) + 1);
			
			writeRaw(0x08);// name_id
			writeBit(nameId);
		}
	}
	
	void write_bag_obj_id(int bag_obj_id) {
		writeRaw(0x10);// bag_obj_id
		writeBit(bag_obj_id);
	}
	
	void write_bag_name_id(int bag_name_id) {
		writeRaw(0x18);// bag_name_id
		writeBit(bag_name_id);
	}
	
	public static class ItemInfo extends BinaryOutputStream {
		public ItemInfo() {
			super();
		}
		
		void write_name_id(int name_id) {
			writeC(0x08);// name_id
			writeBit(name_id);
		}
		
		void write_elemental_enchant_type(int elemental_enchant_type) {
			writeC(0x10);// elemental_enchant_type
			writeC(elemental_enchant_type);
		}
		
		void write_elemental_enchant_value(int elemental_enchant_value) {
			writeC(0x18);// elemental_enchant_value
			writeC(elemental_enchant_value);
		}
		
		void write_enchant(int enchant) {
			writeC(0x20);// enchant
			writeBit(enchant);
		}
		
		void write_item_count(int item_count) {
			writeC(0x28);// item_count
			writeBit(item_count);
		}
		
		void write_bless_code(int bless_code) {
			writeC(0x30);// bless_code
			writeBit(bless_code);
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
		return S_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI;
	}
}


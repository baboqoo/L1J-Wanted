package l1j.server.server.serverpackets.warehouse;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DwarfInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.ClanWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class S_WareHouseItemListNoti extends ServerBasePacket {
	private static final String S_WAREHOUSE_ITEM_LIST_NOTI = "[S] S_WareHouseItemListNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0408;
	
	public boolean is_non_value;
	public boolean is_pledge_using;
	
	private static final String PLEDGE_WAREHOUSE_USING_MESSAGE = "[%s] " + S_SystemMessage.getRefText(129);

	public S_WareHouseItemListNoti(S_WareHouseItemListNoti.eWarehouseType type, L1NpcInstance npc, L1PcInstance pc) {
		write_init();
		List<L1ItemInstance> list = null;
		switch (type) {
		case TRADE_RETRIEVE:
			list = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName()).getItems();
			break;
		case TRADE_RETRIEVE_PLEDGE:
			list = WarehouseManager.getInstance().getClanWarehouse(pc.getClan().getClanName()).getItems();
			L1Clan clan = pc.getClan();
			ClanWarehouse pledgeWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
			
			if (!pledgeWarehouse.setWarehouseUsingChar(pc.getId(), 0)) {
				int id = pledgeWarehouse.getWarehouseUsingChar();
				L1Object prevUser = L1World.getInstance().findObject(id);
				if (prevUser instanceof L1PcInstance) {
					L1PcInstance usingPc = (L1PcInstance) prevUser;
					
					boolean check = false;
					for (L1Object obj : L1World.getInstance().getVisibleObjects(usingPc)) {
						if (obj instanceof L1DwarfInstance) {
							L1DwarfInstance dwarf = (L1DwarfInstance) obj;
							if (dwarf != null && usingPc.getLocation().getTileDistance(dwarf.getLocation()) <= 12) {
								check = true;
							}
						}
					}
					
					if (usingPc.getClan() == clan && check) {
						pc.sendPackets(new S_SystemMessage(String.format(PLEDGE_WAREHOUSE_USING_MESSAGE, usingPc.getName()), true), true);
						is_pledge_using = true;
						return;
					}
					is_pledge_using = false;
					pledgeWarehouse.setWarehouseUsingChar(0, 0);
				}
				if (!pledgeWarehouse.setWarehouseUsingChar(pc.getId(), id)) {
					pc.sendPackets(new S_SystemMessage(String.format(PLEDGE_WAREHOUSE_USING_MESSAGE, pledgeWarehouse.getName()), true), true);
					is_pledge_using = true;
					return;
				}
			}
			break;
		case TRADE_RETRIEVE_ELVEN:
			list = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName()).getItems();
			break;
		case TRADE_RETRIEVE_CHAR:
			list = WarehouseManager.getInstance().getSpecialWarehouse(pc.getName()).getItems();
			break;
		case TRADE_RETRIEVE_CONTRACT:
			list = WarehouseManager.getInstance().getPackageWarehouse(pc.getAccountName()).getItems();
			break;
		default:
			break;
		}

		if (list != null && list.size() > 0) {
			write_checker(type == eWarehouseType.TRADE_RETRIEVE_CONTRACT || npc == null ? pc.getId() : npc.getId());
			write_count(list.size());
			write_warehouse_type(type);
			write_price(type == eWarehouseType.TRADE_RETRIEVE_ELVEN ? 4 : 100);
			write_warehouse_size(type == eWarehouseType.TRADE_RETRIEVE_CHAR ? pc.getSpecialWareHouseSize() : 180);
			for (int index = 0; index < list.size(); index++) {
				write_item_list(index, list.get(index).getItemInfo(pc));
			}
			write_serial_last(true);
		} else {
			this.is_non_value = true;
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_checker(int checker) {
		writeRaw(0x08);// checker
		writeBit(checker);
	}
	
	void write_count(int count) {
		writeRaw(0x10);// count
		writeBit(count);
	}
	
	void write_warehouse_type(eWarehouseType warehouse_type) {
		writeRaw(0x18);// warehouse_type
		writeRaw(warehouse_type.value);
	}
	
	void write_price(int price) {
		writeRaw(0x20);// price
		writeBit(price);
	}
	
	void write_warehouse_size(int warehouse_size) {
		writeRaw(0x28);// warehouse_size
		writeBit(warehouse_size);
	}
	
	void write_item_list(int index, byte[] bytes) {
		int length = getBitSize(index) + getBitSize(bytes.length) + bytes.length + 2;
		writeRaw(0x32);// item_list
		writeBit(length);
			
		writeRaw(0x08);// index
		writeBit(index);
			
		writeRaw(0x12);// item_info
		writeBytesWithLength(bytes);
	}
	
	void write_serial_last(boolean serial_last) {
		writeRaw(0x38);// serial_last
		writeB(serial_last);
	}
	
	public enum eWarehouseType{
		TRADE_RETRIEVE(3),			// 개인창고
		TRADE_RETRIEVE_PLEDGE(5),	// 혈맹창고
		TRADE_RETRIEVE_ELVEN(9),	// 요정창고
		TRADE_RETRIEVE_CHAR(18),	// 특수창고
		TRADE_RETRIEVE_CONTRACT(20),// 패키지 창고
		TRADE_REQUEST_COUNT(21),
		;
		private int value;
		eWarehouseType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eWarehouseType v){
			return value == v.value;
		}
		public static eWarehouseType fromInt(int i){
			switch(i){
			case 3:
				return TRADE_RETRIEVE;
			case 5:
				return TRADE_RETRIEVE_PLEDGE;
			case 9:
				return TRADE_RETRIEVE_ELVEN;
			case 18:
				return TRADE_RETRIEVE_CHAR;
			case 20:
				return TRADE_RETRIEVE_CONTRACT;
			case 21:
				return TRADE_REQUEST_COUNT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eWarehouseType, %d", i));
			}
		}
	}

	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_WAREHOUSE_ITEM_LIST_NOTI;
	}
}


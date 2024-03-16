package l1j.server.server.serverpackets.warehouse;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_RetrieveDeposit extends ServerBasePacket {
	
	public S_RetrieveDeposit(int type, L1PcInstance pc) {
		int size = 0;
		switch(type){
		case 1:size = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName()).getSize();break;
		case 2:size = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName()).getSize();break;
		case 3:size = WarehouseManager.getInstance().getClanWarehouse(pc.getClanName()).getSize();break;
		case 4:size = WarehouseManager.getInstance().getSpecialWarehouse(pc.getName()).getSize();break;
		default:break;
		}
		write_init();
		write_checker(0);
		write_count(size);
		write_warehouse_type(S_WareHouseItemListNoti.eWarehouseType.TRADE_REQUEST_COUNT.toInt());
		write_price(0);
		write_warehouse_size(180);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(S_WareHouseItemListNoti.NOTI);
	}
	
	void write_checker(int checker) {
		writeC(0x08);// checker
		writeC(checker);
	}
	
	void write_count(int count) {
		writeC(0x10);// count
		writeBit(count);
	}
	
	void write_warehouse_type(int warehouse_type) {
		writeC(0x18);// warehouse_type
		writeC(warehouse_type);
	}
	
	void write_price(int price) {
		writeC(0x20);// price
		writeC(price);
	}
	
	void write_warehouse_size(int warehouse_size) {
		writeC(0x28);// warehouse_size
		writeBit(warehouse_size);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

}


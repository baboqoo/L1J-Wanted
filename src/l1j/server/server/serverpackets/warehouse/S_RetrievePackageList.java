package l1j.server.server.serverpackets.warehouse;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.PackageWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti.eWarehouseType;

public class S_RetrievePackageList extends ServerBasePacket {
	/*private static final byte[] SUB_BYTES_1 = { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff };
	private static final byte[] SUB_BYTES_2 = { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 };
	private static final byte[] SUB_BYTES_3 = { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 };
	
	public S_RetrievePackageList(int objid, L1PcInstance pc) {
		PackageWarehouse warehouse = WarehouseManager.getInstance().getPackageWarehouse(pc.getAccountName());
		writeC(Opcodes.S_RETRIEVE_LIST);
		writeD(objid);
		writeH(warehouse.getSize());
		writeC(eWarehouseType.TRADE_REQUEST_COUNT.toInt()); // 0x15:부가아이템 창고
		writeC(1);
		writeC(1);
		L1ItemInstance item = null;
		ByteBuffer buffer = null;
		for (Object itemObject : warehouse.getItems()) {
			item = (L1ItemInstance) itemObject;
			writeD(item.getId());
			writeByte(SUB_BYTES_1);
			
			buffer = StandardCharsets.UTF_16LE.encode(item.getDescKr());
			writeH(buffer.limit() + 2);
			for (int i=0; i<buffer.limit(); i++) {
				writeC(buffer.array()[i]);
			}
			writeByte(SUB_BYTES_2);
			
			writeC(item.getItem().getItemType().getInteractionType());
			writeH(item.getIconId());
			writeC(item.getBless());
			writeD(item.getCount());
			writeC(1);// 확인
			writeS(item.getViewName());
		}
		writeByte(SUB_BYTES_3);
	}*/
	
	public S_RetrievePackageList(int objid, L1PcInstance pc) {
		PackageWarehouse warehouse = WarehouseManager.getInstance().getPackageWarehouse(pc.getAccountName());
		writeC(Opcodes.S_RETRIEVE_LIST);
		writeD(objid);
		writeH(warehouse.getSize());
		writeC(eWarehouseType.TRADE_REQUEST_COUNT.toInt());
		writeC(1); // 여기를 1외의 숫자로 바꾸면 카운트가 올라감 하지만 튕김
		writeC(1); // 1 이외의 숫자로 지정하면 리스트가 안나온다
		for (L1ItemInstance item : warehouse.getItems()) {
			writeD(item.getId());
			writeD(item.getCount());
			writeC(1);	// false: 거래 불가 / true:가능
			writeC(1);	// 페이지(건들지 말것)
			writeC(0x00);	
			writeC(1);
			writeD(-1);
			//String viewName = item.getDescKr();
			String viewName = item.getDescEn();  
			int length = viewName.length();
			writeH((length + 1) << 1);
			for(int i=0; i<length; ++i) {
				writeH(viewName.charAt(i));
			}
			writeH(0x00);
			
			writeD(0);
			writeH(0);
			writeC(0);
			writeH(item.getIconId());
			writeC(item.getBless());
			writeD(item.getCount());
			writeC(1);// 확인
			writeC(1);
			writeS(item.getViewName());
		}
		writeD(0);
		writeD(0x0);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

}


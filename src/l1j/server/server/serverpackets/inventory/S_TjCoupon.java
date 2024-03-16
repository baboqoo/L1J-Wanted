package l1j.server.server.serverpackets.inventory;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.GameSystem.tjcoupon.bean.TJCouponBean;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;

public class S_TjCoupon extends ServerBasePacket {
	private static final String S_TJ_COUPON = "[S] S_TjCoupon";
	private byte[] _byte = null;
	
	// 창고 형태 출력(C_BUY_SELL)
	public S_TjCoupon(L1PcInstance pc, ArrayList<TJCouponBean> list){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(S_WareHouseItemListNoti.NOTI);
		
		writeRaw(0x08);// objid
		writeBit(Config.TJ.TJ_COUPON_ITEMID);
		
		writeRaw(0x10);// size
		writeBit(list.size());
		
		writeRaw(0x18);// type
		writeRaw(3);
		
		writeRaw(0x20);// price
		writeRaw(1);
		
		writeRaw(0x28);// max size
		writeRaw(20);
		
		TJCouponBean data = null;
		for (int index = 0; index < list.size(); index++) {
			data = list.get(index);
			if (data == null) {
				System.out.println(String.format("[S_TjCoupon] COUPON_BEAN_EMPTY : INDEX(%d)", index));
				continue;
			}
			if (data.getItem() == null) {
				System.out.println(String.format("[S_TjCoupon] COUPON_BEAN_ITEM_EMPTY : INDEX(%d), ITEM_ID(%d)", index, data.getItemId()));
				continue;
			}
			
			byte[] itemBytes = data.getItem().getItemInfo(pc);
			int length = 3 + itemBytes.length + getBitSize(itemBytes.length);
			writeRaw(0x32);
			writeBit(length);
			
			writeRaw(0x08);
			writeRaw(index);
			
			writeRaw(0x12);
			writeBytesWithLength(itemBytes);
		}
		
		writeRaw(0x38);
		writeB(true);
		
		writeH(0x00);
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
		return S_TJ_COUPON;
	}
}


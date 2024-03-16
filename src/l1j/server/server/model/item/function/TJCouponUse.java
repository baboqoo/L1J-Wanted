package l1j.server.server.model.item.function;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.GameSystem.tjcoupon.TJCouponLoader;
import l1j.server.GameSystem.tjcoupon.bean.TJCouponBean;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.inventory.S_TjCoupon;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class TJCouponUse extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public TJCouponUse(L1Item item) {
		super(item);
	}
	
//AUTO SRM: 	private static final S_SystemMessage CANNOT_MESSAGE	= new S_SystemMessage("TJ쿠폰은 현재 사용할 수 없습니다."); // CHECKED OK
	private static final S_SystemMessage CANNOT_MESSAGE	= new S_SystemMessage(S_SystemMessage.getRefText(1098), true);
//AUTO SRM: 	private static final S_SystemMessage EMPTY_MESSAGE	= new S_SystemMessage("복구 가능한 아이템이 존재하지 않습니다."); // CHECKED OK
	private static final S_SystemMessage EMPTY_MESSAGE	= new S_SystemMessage(S_SystemMessage.getRefText(1099), true);

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		if (!Config.TJ.TJ_COUPON_ENABLE) {
			pc.sendPackets(CANNOT_MESSAGE);
			return;
		}
		ArrayList<TJCouponBean> list = TJCouponLoader.getUser(pc.getId()).getCoupons();
		if (list == null || list.isEmpty()) {
			pc.sendPackets(EMPTY_MESSAGE);
			return;
		}
		pc.sendPackets(new S_TjCoupon(pc, list), true);
	}
}



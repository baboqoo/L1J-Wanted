package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1AdenShop;
import l1j.server.server.serverpackets.S_Extended;
import l1j.server.server.serverpackets.shop.S_ShopSellAden;
import l1j.server.server.serverpackets.warehouse.S_RetrievePackageList;

public class C_Extended extends ClientBasePacket {
	private static final String C_EXTENDED = "[C] C_Extended";

	public C_Extended(byte[] decrypt, GameClient client) {
		super(decrypt);
		try {
			int type = readH();
			L1PcInstance pc = client.getActiveChar();
			switch (type) {
			case 1:// 상점 열기
				if (Config.ALT.ADEN_SHOP_ZONE) {
					S_Extended list		= new S_Extended(S_Extended.N_SHOP_LIST, pc);
					S_Extended email	= new S_Extended(S_Extended.EMAIL, pc);
					S_Extended point	= new S_Extended(S_Extended.N_POINT, pc);
					client.sendPacket(list);
					client.sendPacket(email);
					client.sendPacket(point);
					list.clear();
					email.clear();
					point.clear();
					list	= null;
					email	= null;
					point	= null;
				} else {
					pc.sendPackets(new S_ShopSellAden(pc), true);
				}
			    break;
			case 4:// OTP 입력
				for (int i = 0; i < 1000; i++) {
					int ff = readH();
					if(ff == 0)break;
				}
				for (int i = 0; i < 128 + 1; i++) {
					readC();
				}
				int size = readH();
				if (size == 0) {
					return;
				}
				L1AdenShop as = new L1AdenShop();
				for (int i = 0; i < size; i++) {
					int id		= readD();
					int count	= readH();
					if (count <= 0 || count >= 10000) {
						return;
					}
					as.add(id, count);
				}
				if (!as.BugOk() && as.commit(pc)) {
					S_Extended otp = new S_Extended(S_Extended.OTP_CHECK_MSG, pc);
					client.sendPacket(otp);
					otp.clear();
					otp = null;
				}
				break;
			case 6:// 부가서비스 창고
				pc.sendPackets(new S_RetrievePackageList(pc.getId(), pc), true);
			    break;
			case 50: // 동의 및 구매
				S_Extended otp = new S_Extended(S_Extended.OTP_SHOW, pc);
				client.sendPacket(otp);
				otp.clear();
				otp = null;
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_EXTENDED;
	}
}

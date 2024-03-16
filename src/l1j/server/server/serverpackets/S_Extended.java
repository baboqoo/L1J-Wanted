package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1AdenShopItem;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class S_Extended extends ServerBasePacket {
	private static final String S_EXTENDED	= "[S] S_Extended";
	private byte[] _byte					= null;
	public static final int N_SHOP_LIST		= 0;
	public static final int EMAIL			= 1;
	public static final int N_POINT			= 2;
	public static final int BUY_AUTH		= 3;
	public static final int OTP_SHOW		= 4;
	public static final int OTP_CHECK_MSG	= 5;
	public static final int SURVIVAL_CRY	= 15;

	public S_Extended(int currentTime, int maxTime) {
		writeC(Opcodes.S_EXTENDED);
		writeH(SURVIVAL_CRY);
		writeD(currentTime);
		writeD(maxTime);
		writeH(0x00);
	}

	public S_Extended(int value, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED);
		try {
			if (value == N_SHOP_LIST) { // 목록
				writeC(0x02);
				writeH(0x00);
				writeD(0x00);
				writeH(AdenShopTable.getInstance().Size());
				writeH(AdenShopTable.data_length);
				writeH(AdenShopTable.data_length);
				for (L1AdenShopItem item : AdenShopTable.getInstance().getList()) {
					writeD(item.getItemId());
					writeH(item.getItem().getIconId());
					writeH(0x00);
					//String name = item.getItem().getDescKr();
					String name = item.getItem().getDescEn();
					if (item.getPackCount() > 1) {
						name = name + "(" + item.getPackCount() + ")";
					}
					if (item.getItem().getMaxUseTime() > 0) {
						name = name + " [" + item.getItem().getMaxUseTime() + "]";
					} else if (item.getEnchant() > 0) {
						//name = StringUtil.PlusString+ item.getEnchant() + StringUtil.EmptyOneString + item.getItem().getDescKr();
						name = StringUtil.PlusString+ item.getEnchant() + StringUtil.EmptyOneString + item.getItem().getDescEn();
					} else if (item.getItemId() == 65648) {
						//name = name + " [7일]";
						name = name + " [7 " + S_SystemMessage.getRefTextNS(663) + "]";
					} else if (item.getItemId() >= 30022 && item.getItemId() <= 30025) {
						name = name + " [18000]";
					} else if (item.getItemId() >= 22320 && item.getItemId() <= 22327) {
						//name = name + " [3시간]";
						name = name + " [3 " + S_SystemMessage.getRefTextNS(664) + "]";
					}
					writeH(name.getBytes(CharsetUtil.UTF_16LE_STR).length + 2); // 이름 글자 사이즈
					writeSU16(name); // 이름
					String html = item.getHtml();
					int ii = 2;
					if (!html.equalsIgnoreCase(StringUtil.EmptyString)) {
						byte[] test = html.getBytes(CharsetUtil.EUC_KR_STR);
						for (int i = 0; i < test.length;) {
							if ((test[i] & 0xff) >= 0x7F) {
								i += 2;
							} else {
								i += 1;
							}
							ii += 2;
						}
					}
					writeH(ii); // html size
					writeSS(html); // html
					writeD(item.getPrice()); // 가격
					writeH(item.getType()); // 2-장비 3-버프 4-편의 5-기타
					writeH(item.getStatus()); // 0 노말 1 new 2 hot 3 sale
					writeD(0x000C0DBF);
					writeD(0x000063);
				}
			} else if (value == EMAIL) { // 결제 저장된 이메일
				/*
				 * String s = "0c 00 26 00 64 00 6c 00 64 00 75 00 64 00 67 "+
				 * "00 75 00 73 00 40 00 6e 00 61 00 76 00 65 00 72 "+
				 * "00 2e 00 63 00 6f 00 6d 00 00 00 00 24";
				 */
				String s = "0c 00 26 00 6e 00 75 00 6c 00 6c 00 40 00 6e 00 75 00 6c 00 6c 00 2e 00 63 00 6f 00 6d 00 00 00 20 b8";

				StringTokenizer st = new StringTokenizer(s);
				while (st.hasMoreTokens()) {
					writeC(Integer.parseInt(st.nextToken(), 16));
				}
			} else if (value == N_POINT) { // 현재 포인트 관련?
				writeH(0x03);
				writeH(0x01);
				writeH(0x04);
				writeD(pc.getNcoin());
				writeH(0x00);
			} else if (value == BUY_AUTH) { // 결제 저장된 이메일
				String s = "02 00 00 f4 ff ff ff 00 00 00 00 00 00 99 17";
				StringTokenizer st = new StringTokenizer(s);
				while (st.hasMoreTokens()) {
					writeC(Integer.parseInt(st.nextToken(), 16));
				}
			} else if (value == OTP_SHOW) {// OTP 창
				writeD(0x33);
				writeH(0x00);
			} else if (value == OTP_CHECK_MSG) {// OTP CHECK MSG
				writeH(OTP_CHECK_MSG);
				// OTP 틀림
				// writeH(0x0ED0B);
				// writeD(0x29FFFFFF);
				writeH(0x00);
				writeD(0x00);
				writeC(0x00);
			}
		} catch (Exception e) {
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
		return S_EXTENDED;
	}
	
}


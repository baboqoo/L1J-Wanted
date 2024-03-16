package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class Firecracker extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Firecracker(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			int soundid = 0;
			if (itemId >= 40136 && itemId <= 40161 || itemId == 410027) {// 불꽃 폭죽
			/*	if (pc.getZoneType() != 1) {//마을에서만 사용가능
					pc.sendPackets(L1ServerMessage.sm563);// \f1 여기에서는 사용할 수 없습니다.
					return;
				}*/
				switch (itemId){
				case 40154:	soundid = 3198;break;
				case 40152:	soundid = 2031;break;
				case 40141:	soundid = 2028;break;
				case 40160:	soundid = 2030;break;
				case 40145:	soundid = 2029;break;
				case 40159:	soundid = 2033;break;
				case 40151:	soundid = 2032;break;
				case 40161:	soundid = 2037;break;
				case 40142:	soundid = 2036;break;
				case 40146:	soundid = 2039;break;
				case 40148:	soundid = 2043;break;
				case 40143:	soundid = 2041;break;
				case 40156:	soundid = 2042;break;
				case 40139:	soundid = 2040;break;
				case 40137:	soundid = 2047;break;
				case 40136:	soundid = 2046;break;
				case 40138:	soundid = 2048;break;
				case 40140:	soundid = 2051;break;
				case 40144:	soundid = 2053;break;
				case 40147:	soundid = 2045;break;
				case 40149:	soundid = 2034;break;
				case 40150:	soundid = 2055;break;
				case 40153:	soundid = 2038;break;
				case 40155:	soundid = 2044;break;
				case 40157:	soundid = 2035;break;
				case 40158:	soundid = 2049;break;
				default:	soundid = 3198;break;
				}
			} else if (itemId >= 41357 && itemId <= 41382) {// 알파벳 불꽃
				soundid = itemId - 34946;
			}
			pc.send_effect(soundid);
			pc.getInventory().removeItem(this, 1);
		}
	}
}


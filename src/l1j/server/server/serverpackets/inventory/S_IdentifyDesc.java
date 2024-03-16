package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IdentifyDesc extends ServerBasePacket {
	private byte[] _byte = null;

	/** 확인 스크롤 사용시의 메세지를 표시한다 */
	public S_IdentifyDesc(L1ItemInstance item) {
		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		writeC(Opcodes.S_IDENTIFY_CODE);
		writeH(item.getItem().getItemNameId());
		
		StringBuilder name = new StringBuilder();
		if (item.getBless() == 0) {
			name.append(L1ItemInstance.BLESS_STATUS_STRING[0]); // 축복받은
		} else if (item.getBless() == 2) {
			name.append(L1ItemInstance.BLESS_STATUS_STRING[1]); // 저주받은
		}
		name.append(item.getItem().getDesc());

		switch(item.getItem().getItemType()){
		case WEAPON:
			writeH(134); // \f1%0：작은 monster 타격%1 큰 monster 타격%2
			writeC(3);
			writeS(name.toString());
			writeS(String.format("%d+%d", item.getItem().getDmgSmall(), item.getEnchantLevel()));
			writeS(String.format("%d+%d", item.getItem().getDmgLarge(), item.getEnchantLevel()));
			break;
		case ARMOR:
			if (item.getItem().getItemId() == 20383) { // 기마용 헤룸
				writeH(137); // \f1%0：사용 가능 회수%1［무게%2］
				writeC(3);
				writeS(name.toString());
				writeS(String.valueOf(item.getChargeCount()));
			} else {
				writeH(135); // \f1%0：방어력%1 방어도구
				writeC(2);
				writeS(name.toString());
				writeS(String.format("%d+%d", Math.abs(item.getItem().getAc()), item.getEnchantLevel()));
			}
			break;
		case NORMAL:
			if (item.getItem().getType() == 1) { // wand
				writeH(137); // \f1%0：사용 가능 회수%1［무게%2］
				writeC(3);
				writeS(name.toString());
				writeS(String.valueOf(item.getChargeCount()));
			} else if (item.getItem().getItemId() != 7338 && item.getItem().getType() == 2) {
				writeH(138);
				writeC(2);
				name.append(": $231 "); // 나머지의 연료
				name.append(String.valueOf(item.getRemainingTime()));
				writeS(name.toString());
			} else if (item.getItem().getType() == 7) { // food
				writeH(136);  // \f1%0：만복도%1［무게%2］
				writeC(3);
				writeS(name.toString());
				writeS(String.valueOf(item.getItem().getFoodVolume()));
			} else {
				writeH(138); // \f1%0：［무게%1］
				writeC(2);
				writeS(name.toString());
			}
			writeS(String.valueOf(item.getWeight()));
			break;
		default:break;
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}


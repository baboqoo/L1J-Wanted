package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.serverpackets.equip.S_ExtendSlotCurrencyNoti;

public class A_ExtendSlotCurrencySelect extends ProtoHandler {
	protected A_ExtendSlotCurrencySelect(){}
	private A_ExtendSlotCurrencySelect(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _slot_position;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		_slot_position = readC();
		if (_slot_position == 1 && _pc.getRingSlotLevel() != 1) {
			_pc.sendPackets(L1SystemMessage.SLOG_INDEX_FAIL);// 좌측 먼저 개방
			return;
		}
		if (_slot_position == 6 && _pc.getRingSlotLevel() != 2) {
			_pc.sendPackets(L1SystemMessage.SLOG_95_RING_INDEX_FAIL);// 60레벨 반지 개방
			return;
		}
		if (_slot_position == 7 && _pc.getRingSlotLevel() != 3) {
			_pc.sendPackets(L1SystemMessage.SLOG_100_RING_INDEX_FAIL);// 95레벨 반지 개방
			return;
		}
		if (_slot_position == 8 && _pc.getEarringSlotLevel() != 1) {
			_pc.sendPackets(L1SystemMessage.SLOG_101_EARRING_INDEX_FAIL);// 101레벨 귀걸이 개방
			return;
		}
		if (_slot_position == 9 && _pc.getEarringSlotLevel() != 2) {
			_pc.sendPackets(L1SystemMessage.SLOG_103_EARRING_INDEX_FAIL);// 103레벨 귀걸이 개방
			return;
		}
		int adena_count		= 0;
		boolean isDiscount	= false;
		switch(_slot_position){
		case 0:// 좌측반지
			if (_pc.getInventory().checkItem(520291)) {
				isDiscount	= true;
			} else {
				adena_count	= 20000000;
			}
			break;
		case 1:// 우측반지
			if (_pc.getInventory().checkItem(520292)) {
				isDiscount	= true;
			} else {
				adena_count	= 40000000;
			}
			break;
		case 2:// 귀걸이
			if (_pc.getInventory().checkItem(520289)) {
				isDiscount	= true;
			} else {
				adena_count	= 2000000;
			}
			break;
		case 4:// 견갑
			if (_pc.getInventory().checkItem(520293)) {
				isDiscount	= true;
			} else {
				adena_count	= 30000000;
			}
			break;
		case 5:// 휘장
			if (_pc.getInventory().checkItem(520290)) {
				isDiscount	= true;
			} else {
				adena_count	= 2000000;
			}
			break;
		case 6:// 좌측 2번 반지
			adena_count	= 100000000;
			break;
		case 7:// 우측 2번 반지
			adena_count	= 100000000;
			break;
		case 8:// 좌측 2번 귀걸이
			adena_count	= 100000000;
			break;
		case 9:// 우측 2번 귀걸이
			adena_count	= 100000000;
			break;
		default:
			return;
		}
		_pc.sendPackets(new S_ExtendSlotCurrencyNoti(isDiscount, adena_count, _slot_position), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ExtendSlotCurrencySelect(data, client);
	}

}


package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.equip.S_ExtendSlotInfo;
import l1j.server.server.serverpackets.equip.S_ExtendSlotResultNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class A_ExtendSlotRequest extends ProtoHandler {
	protected A_ExtendSlotRequest(){}
	private A_ExtendSlotRequest(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _slot_position;
	private int _slotType;
	private int _salePaperId;
	private boolean _isDiscount;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (_pc.getRegion() != L1RegionStatus.SAFETY) {
			_pc.sendPackets(L1ServerMessage.sm7985);// 슬롯 개방이 불가능한 지역입니다. 안전한 장소에서 다시 시도해주세요.
			return;
		}
		if (_pc.getLevel() < 60) {
			_pc.sendPackets(L1ServerMessage.sm7855);// 레벨이 부족하여 슬롯을 개방할 수 없습니다.
			return;
		}
		readP(1);// 0x08
		_slot_position = readC();
		
		L1PcInventory inv = _pc.getInventory();
		switch(_slot_position){
		case 0:// 좌측반지
			_slotType = S_ExtendSlotInfo.SLOT_LOC_L_RING;
			if (inv.checkItem(520291)) {
				_salePaperId = 520291;
			}
			break;
		case 1:// 우측반지
			_slotType = S_ExtendSlotInfo.SLOT_LOC_R_RING;
			if (inv.checkItem(520292)) {
				_salePaperId = 520292;
			}
			break;
		case 2:// 귀걸이
			_slotType = S_ExtendSlotInfo.SLOT_LOC_EARRING;
			if (inv.checkItem(520289)) {
				_salePaperId = 520289;
			}
			break;
		case 4:// 견갑
			_slotType = S_ExtendSlotInfo.SLOT_LOC_SHOULDER;
			if (inv.checkItem(520293)) {
				_salePaperId = 520293;
			}
			break;
		case 5:// 휘장
			_slotType = S_ExtendSlotInfo.SLOT_LOC_BADGE;
			if (inv.checkItem(520290)) {
				_salePaperId = 520290;
			}
			break;
		case 6:// 좌측 2번 반지
			_slotType = S_ExtendSlotInfo.SLOT_LOC_L_RING_95;
			break;
		case 7:// 우측 2번 반지
			_slotType = S_ExtendSlotInfo.SLOT_LOC_R_RING_100;
			break;
		case 8:// 좌측 2번 귀걸이
			_slotType = S_ExtendSlotInfo.SLOT_LOC_L_EARRING_101;
			break;
		case 9:// 우측 2번 귀걸이
			_slotType = S_ExtendSlotInfo.SLOT_LOC_R_EARRING_103;
			break;
		default:
			System.out.println(String.format("[A_ExtendSlotRequest] UNDEFIND_SLOT_POSITION : POSITION(%d)", _slot_position));
			return;
		}
		
		if (_salePaperId != 0) {
			inv.consumeItem(_salePaperId, 1);// 할인권 소모
			_isDiscount = true;
		} else {
			int price=0;
			switch(_slotType){
			case S_ExtendSlotInfo.SLOT_LOC_L_RING:// 좌측반지
				price = 20000000;
				break;
			case S_ExtendSlotInfo.SLOT_LOC_R_RING:// 우측반지
				price = 40000000;
				break;
			case S_ExtendSlotInfo.SLOT_LOC_EARRING:// 귀걸이
				price = 2000000;
				break;
			case S_ExtendSlotInfo.SLOT_LOC_SHOULDER:// 견갑
				price = 30000000;
				break;
			case S_ExtendSlotInfo.SLOT_LOC_BADGE:// 휘장
				price = 2000000;
				break;
			case S_ExtendSlotInfo.SLOT_LOC_L_RING_95:// 좌측 2번 반지
				price = 100000000;
				break;
			case S_ExtendSlotInfo.SLOT_LOC_R_RING_100:// 우측 2번 반지
				price = 100000000;
				break;
			case S_ExtendSlotInfo.SLOT_LOC_L_EARRING_101:// 좌측 2번 귀걸이
				price = 100000000;
				break;
			case S_ExtendSlotInfo.SLOT_LOC_R_EARRING_103:// 우측 2번 귀걸이
				price = 100000000;
				break;
			}
			if (!inv.consumeItem(L1ItemId.ADENA, price)) {// 아데나 소모
				_pc.sendPackets(new S_ExtendSlotResultNoti(_slot_position, false, false), true);// 아데나가 부족하여 개방에 실패하였습니다.
				return;
			}
		}
		
		L1ItemInstance item = null;
		switch(_slotType){
		case S_ExtendSlotInfo.SLOT_LOC_L_RING:// 좌측반지
			_pc.setRingSlotLevel(1);
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(410088), 1) == L1Inventory.OK)
				item = inv.storeItem(410088, 1);
			break;
		case S_ExtendSlotInfo.SLOT_LOC_R_RING:// 우측반지
			_pc.setRingSlotLevel(2);
			break;
		case S_ExtendSlotInfo.SLOT_LOC_EARRING:// 귀걸이
			_pc.setEarringSlotLevel(1);
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(410090), 1) == L1Inventory.OK) 
				item = inv.storeItem(410090, 1);
			break;
		case S_ExtendSlotInfo.SLOT_LOC_SHOULDER:// 견갑
			_pc.setShoulderSlotLevel(1);
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31125), 1) == L1Inventory.OK)
				item = inv.storeItem(31125, 1);
			break;
		case S_ExtendSlotInfo.SLOT_LOC_BADGE:// 휘장
			_pc.setBadgeSlotLevel(1);
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(3110151), 1) == L1Inventory.OK)
				item = inv.storeItem(3110151, 1);
			break;
		case S_ExtendSlotInfo.SLOT_LOC_L_RING_95:// 좌측 2번 반지
			_pc.setRingSlotLevel(3);
			break;
		case S_ExtendSlotInfo.SLOT_LOC_R_RING_100:// 우측 2번 반지
			_pc.setRingSlotLevel(4);
			break;
		case S_ExtendSlotInfo.SLOT_LOC_L_EARRING_101:// 좌측 2번 귀걸이
			_pc.setEarringSlotLevel(2);
			break;
		case S_ExtendSlotInfo.SLOT_LOC_R_EARRING_103:// 우측 2번 귀걸이
			_pc.setEarringSlotLevel(3);
			break;
		}
		if (item != null) {
			_pc.sendPackets(new S_ServerMessage(403, item.getDesc()), true);// 추가 아이템을 획득 멘트
		}
		_pc.send_effect_self(12004);
		_pc.sendPackets(new S_ExtendSlotInfo(_pc, false, _slotType), true);// 개방 완료
		_pc.sendPackets(new S_ExtendSlotResultNoti(_slot_position, true, _isDiscount), true);// 개방 메세지
		_pc.save();// db저장
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ExtendSlotRequest(data, client);
	}

}


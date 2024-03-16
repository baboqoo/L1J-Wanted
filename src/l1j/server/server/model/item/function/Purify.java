package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.monitor.Logger.SmeltingSlotType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.smelting.S_SmeltingUpdateSlotInfoNoti;
import l1j.server.server.serverpackets.smelting.SmeltingResult;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class Purify extends L1ItemInstance {// 정화제
	private static final long serialVersionUID = 1L;
	
	public Purify(L1Item item) {
		super(item);
	}
	
	private L1PcInstance pc;
	private L1PcInventory inv;
	private int slot_no;
	private L1ItemInstance target;
	private L1Item ejectStone;

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		pc		= (L1PcInstance) cha;
		slot_no	= packet.readD();
		inv		= pc.getInventory();
		target	= inv.getItem(packet.readD());
		
		if (!isValidation()) {
			return;
		}
		
		// 추출 확률
		int prob = getItem().getProb();
		if (prob != 0 && prob != 100 && CommonUtil.random(100) + 1 > prob) {
			pc.sendPackets(new S_SmeltingUpdateSlotInfoNoti(slot_no, target.getId(), 0, SmeltingResult.SMELTING_EJECT_FAIL));
			inv.removeItem(this, 1);// 정화제 제거
			return;
		}
		
		// 착용 중인 장비인 경우 능력치 제거  -> 제련석 추출 -> 능력치 부여
		boolean targetEquip = target.isEquipped();
		if (targetEquip) {
			inv.removeItemAblity(target);
		}
		boolean result = eject();
		if (targetEquip) {
			inv.setItemAblity(target);
		}
		if (!result) {
			return;
		}
		
		// 모든 추출이 완료되었으면 상태 변경을 알린다.
		if (!target.isSlot()) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, target, 0), true);
		}
		inv.updateItem(target, L1PcInventory.COL_SLOTS);
		inv.saveItemSlot(target, slot_no, 0);
		
		inv.removeItem(this, 1);// 정화제 제거
		
		pc.sendPackets(new S_SmeltingUpdateSlotInfoNoti(slot_no, target.getId(), 0, SmeltingResult.SMELTING_EJECT_SUCCESS));
		
		// 추출된 제련석 생성
		L1ItemInstance stone = inv.storeItem(ejectStone.getItemId(), 1);
		pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", stone.getItem().getDesc(), 1)), true);
		LoggerInstance.getInstance().addSmeltingSlot(SmeltingSlotType.EJECT, pc, target, stone);
	}
	
	/**
	 * 제련석 추출
	 * @return boolean
	 */
	private boolean eject() {
		ejectStone	= target.ejectSlot(slot_no);// 추출
		if (ejectStone == null) {
			System.out.println(String.format(
					"[Purify] EJECT_FAILE_STONE : SLOT_NO(%d), TARGET_ID(%d), CHAR_NAME(%s)",
					slot_no, target.getId(), pc.getName()));
			pc.sendPackets(L1ServerMessage.sm9077);// 제련석 추출에 실패하였습니다.
			return false;
		}
		return true;
	}
	
	/**
	 * 유효성 검증
	 * @return boolean
	 */
	private boolean isValidation() {
		if (target == null) {
			pc.sendPackets(new S_SmeltingUpdateSlotInfoNoti(slot_no, 0, 0, SmeltingResult.TARGET_ITEM_NOT_FOUND));
			return false;
		}
		if (target.getBless() >= 128) {
			pc.sendPackets(L1ServerMessage.sm79);
			return false;
		}
		if (!target.isSlot()) {
			pc.sendPackets(L1ServerMessage.sm9160);// 추출할 제련석이 존재하지 않습니다.
			return false;
		}
		if (inv.getWeightPercent() >= 99 || inv.getSize() >= L1PcInventory.MAX_SIZE - 1) {
			pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return false;
		}
		return true;
	}
}


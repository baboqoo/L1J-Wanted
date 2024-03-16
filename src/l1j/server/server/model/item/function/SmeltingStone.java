package l1j.server.server.model.item.function;

import io.netty.util.internal.StringUtil;

import java.util.HashMap;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.monitor.Logger.SmeltingSlotType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.smelting.S_SmeltingUpdateSlotInfoNoti;
import l1j.server.server.serverpackets.smelting.SmeltingResult;
import l1j.server.server.templates.L1Item;

public class SmeltingStone extends L1ItemInstance {// 제련석
	private static final long serialVersionUID = 1L;
	
	public SmeltingStone(L1Item item) {
		super(item);
	}
	
	private L1PcInstance pc;
	private L1PcInventory inv;
	private L1Item stone;
	private int slot_no;
	private L1ItemInstance target;
	private L1Item targetItem;

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		pc		= (L1PcInstance) cha;
		inv		= pc.getInventory();
		slot_no	= packet.readD();
		target	= inv.getItem(packet.readD());
		if (target == null) {
			pc.sendPackets(new S_SmeltingUpdateSlotInfoNoti(slot_no, 0, 0, SmeltingResult.TARGET_ITEM_NOT_FOUND));
			return;
		}
		stone		= this.getItem();
		targetItem	= target.getItem();
		if (!isValidation()) {
			return;
		}
		
		// 착용 중인 장비인 경우 능력치 제거 -> 제련석 등록 -> 능력치 부여
		boolean targetEquip	= target.isEquipped();
		if (targetEquip) {
			inv.removeItemAblity(target);
		}
		boolean result = insert();
		if (targetEquip) {
			inv.setItemAblity(target);
		}
		if (!result) {
			return;
		}
		
		int status = 14;
		if (target.isIdentified()) {
			status += 1;
		}
		pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, target, status), true);
		inv.updateItem(target, L1PcInventory.COL_SLOTS);
		inv.saveItemSlot(target, slot_no, stone.getItemNameId());
		
		inv.removeItem(this, 1);// 제련석 제거
		
		pc.sendPackets(new S_SmeltingUpdateSlotInfoNoti(slot_no, target.getId(), stone.getItemNameId(), SmeltingResult.SMELTING_INSERT_SUCCESS));
		LoggerInstance.getInstance().addSmeltingSlot(SmeltingSlotType.INSERT, pc, target, this);
	}
	
	/**
	 * 제련석 장착
	 * @return boolean
	 */
	private boolean insert() {
		int result = target.insertSlot(slot_no, stone);
		if (result == -1) {// 장착 실패
			pc.sendPackets(L1ServerMessage.sm9167);// 삽입할 제련석 슬롯이 유효하지 않습니다.
			System.out.println(String.format(
					"[SmeltingStone] INSERT_FAILE_STONE : STONE_NAME_ID(%d), TARGET_NAME_ID(%d), CHAR_NAME(%s)",
					stone.getItemNameId(), targetItem.getItemNameId(), pc.getName()));
			return false;
		}
		return true;
	}
	
	/**
	 * 유효성 검증
	 * @return boolean
	 */
	private boolean isValidation() {
		// 무기, 방어구 장착 가능 검증
		if (!Config.SMELTING.SMELTING_EQUIP_TYPES.contains(targetItem.getItemType())) {
			return false;
		}
		
		// 아이템 타입별 장착 가능 검증
		switch (targetItem.getItemType()) {
		case WEAPON:
			if (!Config.SMELTING.SMELTING_EQUIP_WEAPON_TYPES.contains(targetItem.getType())) {
				pc.sendPackets(L1ServerMessage.sm9075);// 제련석을 장착할 수 없는 아이템입니다.
				return false;
			}
			break;
		case ARMOR:
			if (!Config.SMELTING.SMELTING_EQUIP_ARMOR_TYPES.contains(targetItem.getType())) {
				pc.sendPackets(L1ServerMessage.sm9075);// 제련석을 장착할 수 없는 아이템입니다.
				return false;
			}
			break;
		default:
			pc.sendPackets(L1ServerMessage.sm9075);// 제련석을 장착할 수 없는 아이템입니다.
			return false;
		}
		
		// 불가 상태 아이템 검증
		if (!targetItem.isTradable()
        		|| !targetItem.isRetrieve()
        		|| !targetItem.isSpecialRetrieve()
        		|| targetItem.getSafeEnchant() < 0
        		|| target.isEngrave()
        		|| target.getBless() >= 128) {
			pc.sendPackets(L1ServerMessage.sm9075);// 제련석을 장착할 수 없는 아이템입니다.
			return false;
		}
		
		// 기간제 아이템
		if (target.getEndTime() != null) {
			pc.sendPackets(L1ServerMessage.sm9246);// 기간제 아이템에는 제련석을 사용이 불가능 합니다.
			return false;
		}
		
		// 슬롯 검증
		HashMap<Integer, L1Item> slots = target.getSlots();
		if (slots == null || slots.isEmpty()) {
			return true;
		}
		if (slots.size() >= Config.SMELTING.SMELTING_LIMIT_SLOT_VALUE) {
			pc.sendPackets(L1ServerMessage.sm79);// 아무일도 일어나지 않았습니다.
			return false;
		}
		if (slots.containsKey(slot_no)) {
			pc.sendPackets(L1ServerMessage.sm9159);// 해당 슬롯에 이미 제련석이 존재합니다.
			return false;
		}
		if (isEqualsBonus(slots)) {
			pc.sendPackets(L1ServerMessage.sm9166);// 동일한 제련석이 이미 존재합니다.
			return false;
		}
		
		return true;
	}
	
	/**
	 * 슬롯에 장착된 제련석 동일 옵션 검증
	 * DB name_id $기준 두번째(옵션 명칭) 일치 여부 검증
	 * @param smeltingSlots
	 * @return boolean
	 */
	private boolean isEqualsBonus(HashMap<Integer, L1Item> smeltingSlots) {
		String bonusDesc	= stone.getDesc().substring(stone.getDesc().indexOf("$", 1));// 옵션 명칭
		if (StringUtil.isNullOrEmpty(bonusDesc)) {
			System.out.println(String.format(
					"[SmeltingStone] STONE_INFO_EMPTY : SLOT_NAME_ID(%d), CHAR_NAME(%s)", 
					stone.getItemNameId(), pc.getName()));
			return true;
		}
		for (L1Item slotItem : smeltingSlots.values()) {
			if (slotItem == stone) {
				return true;
			}
			if (slotItem.getDesc().contains(bonusDesc)) {// 동일 보너스 옵션
				return true;
			}
		}
		return false;
	}
	
}


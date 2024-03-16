package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class Armor extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Armor(L1Item item) {
		super(item);
	}
	
	private L1PcInstance pc;
	private int current_equip_count;

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha.isDesperado() || cha.isOsiris() || getItem().getItemType() != L1ItemType.ARMOR) {
			return;
		}
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		pc = (L1PcInstance) cha;
		if (pc.isGm()
				|| pc.isCrown() && getItem().isUseRoyal() 
				|| pc.isKnight() && getItem().isUseKnight()
				|| pc.isElf() && getItem().isUseElf() 
				|| pc.isWizard() && getItem().isUseMage()
				|| pc.isDarkelf() && getItem().isUseDarkelf()
				|| pc.isDragonknight() && getItem().isUseDragonKnight()
				|| pc.isIllusionist() && getItem().isUseIllusionist() 
				|| pc.isWarrior() && getItem().isUseWarrior()
				|| pc.isFencer() && getItem().isUseFencer()
				|| pc.isLancer() && getItem().isUseLancer()){
			response();
		} else {
			pc.sendPackets(L1ServerMessage.sm264);// 당신의 클래스에서는 이 아이템은 사용할 수 없습니다.
		}
	}
	
	/**
	 * 반지 착용 검증
	 * @param pcInventory
	 * @param type
	 * @param itemId
	 * @return boolean
	 */
	boolean is_ring_validation(L1PcInventory pcInventory, int type, int itemId) {
		// 6개가 장착중이면 더이상착용불가
		if (current_equip_count >= 6){
			pc.sendPackets(L1ServerMessage.sm144);// 슬롯에 이미 아이템을 착용하고 있습니다.
			return false;
		}
		// 반지  개방 검증(기본 2개)
		if (current_equip_count >= pc.getRingSlotLevel() + 2) {
			pc.sendPackets(L1ServerMessage.sm3253);// 슬롯 확장: 착용 불가(레벨이 부족함)
			return false;
		}
		// 반지 같은 아이디 2개 장착 중
		if (pcInventory.getTypeAndItemIdEquipped(L1ItemType.ARMOR, type, itemId) >= 2){
			pc.sendPackets(L1ServerMessage.sm3278);// 슬롯 확장: 같은 종류 추가 착용 불가
			return false;
		}
		
		int grade				= getItem().getGrade();
		// 착용하려고하는 아이템고유속성번호 3을 만족하는 아이템이 2개착용중일때(BM반지)
		if (grade == 3 && pcInventory.getTypeAndGradeEquipped(L1ItemType.ARMOR, type, grade) >= 2){
			pc.sendPackets(L1ServerMessage.sm3279);// 슬롯 확장: 해당 아이템 추가 착용 불가
			return false;
		}
		return true;
	}
	
	/**
	 * 귀걸이 착용 여부 검증
	 * @param pcInventory
	 * @param type
	 * @param itemId
	 * @return boolean
	 */
	boolean is_earring_validation(L1PcInventory pcInventory, int type, int itemId) {
		// 4개가 장착중이면 더이상착용불가
		if (current_equip_count >= 4){
			pc.sendPackets(L1ServerMessage.sm144);// 슬롯에 이미 아이템을 착용하고 있습니다.
			return false;
		}
		// 귀걸이 개방 검증(기본 1개)
		if (current_equip_count >= pc.getEarringSlotLevel() + 1){
			pc.sendPackets(L1ServerMessage.sm3253);// 슬롯 확장: 착용 불가(레벨이 부족함)
			return false;
		}
		// 같은 아이템 아이디 1개 이상 착용 불가
		if (pcInventory.getTypeAndItemIdEquipped(L1ItemType.ARMOR, type, itemId) >= 1){
			pc.sendPackets(L1ServerMessage.sm3278);// 슬롯 확장: 같은 종류 추가 착용 불가
			return false;
		}
		// 같은 이름의 아이템 1개 이상 착용 불가(일반, 축복)
		if (pcInventory.getNameEquipped(L1ItemType.ARMOR, type, getDescKr()) >= 1){
			pc.sendPackets(L1ServerMessage.sm3278);// 슬롯 확장: 같은 종류 추가 착용 불가
			return false;
		}
		
		int grade				= getItem().getGrade();
		// 착용하려고하는 아이템고유속성번호 4을 만족하는 아이템이 2개착용중일때(BM귀걸이)
		if (grade == 4 && pcInventory.getTypeAndGradeEquipped(L1ItemType.ARMOR, type, grade) >= 2){
			pc.sendPackets(L1ServerMessage.sm3279);// 슬롯 확장: 해당 아이템 추가 착용 불가
			return false;
		}
		return true;
	}
	
	/**
	 * 방패 또는 가더 착용 검증
	 * @param pcInventory
	 * @param type
	 * @return boolean
	 */
	boolean is_shield_or_garder_validation(L1PcInventory pcInventory, int type) {
		// 방패 착용 요청 시 이미 가더 착용중
		if (type == L1ItemArmorType.SHIELD.getId() && pcInventory.getEquippedGarder() != null){
			pc.sendPackets(L1ServerMessage.sm124);// 같은 슬롯에 이미 아이템을 착용하고 있습니다.
			return false;
		}
		// 가더 착용 요청 시 이미 방패 착용중
		if (type == L1ItemArmorType.GARDER.getId() && pcInventory.getEquippedShield() != null){
			pc.sendPackets(L1ServerMessage.sm124);// 같은 슬롯에 이미 아이템을 착용하고 있습니다.
			return false;
		}
		// 방패 착용 요청 시 양손 무기 착용중
		if (type == L1ItemArmorType.SHIELD.getId() && pc.getWeapon() != null && pc.getWeapon().getItem().isTwohandedWeapon() && getItem().get_interaction_type() != L1ItemArmorType.GARDER.getId()){
			pc.sendPackets(L1ServerMessage.sm129);// 양손 무기를 해제 한 후 방패를 착용 해주세요.
			return false;
		}
		return true;
	}
	
	/**
	 * 방어구 착용 또는 해제
	 */
	void response() {
		int type					= getItem().getType();
		int itemId					= getItemId();
		L1PcInventory pcInventory	= pc.getInventory();
		boolean is_equip			= isEquipped();
		boolean equipeSpace;// 장비 하는 개소가 비어 있을까
		current_equip_count			= pcInventory.getTypeEquipped(L1ItemType.ARMOR, type);
		if (type == L1ItemArmorType.RING.getId()) {
			equipeSpace = current_equip_count <= 6;// 반지의 경우
		} else if (type == L1ItemArmorType.EARRING.getId()) {
			equipeSpace = current_equip_count <= 4;// 귀걸이의 경우
		} else {
			equipeSpace = current_equip_count <= 0;// 그외 방어구
		}

		if (equipeSpace && !is_equip) {// 사용한 방어용 기구를 장비 하고 있지 않아, 그 장비 개소가 비어 있는 경우(장착을 시도한다)
			// 변신 검증
			if (!L1PolyMorph.isEquipableArmor(pc.getSpriteId(), type)) {// 그 변신에서는 장비 불가
				return;
			}
			
			// 반지 검증
			if (type == L1ItemArmorType.RING.getId() 
					&& !is_ring_validation(pcInventory, type, itemId)) {
				return;
			}
			
			// 귀걸이 검증
			if (type == L1ItemArmorType.EARRING.getId() 
					&& !is_earring_validation(pcInventory, type, itemId)) {
				return;
			}
			
			// 견갑 검증
			if (type == L1ItemArmorType.SHOULDER.getId() 
					&& pc.getShoulderSlotLevel() != 1){// 견갑
				pc.sendPackets(L1ServerMessage.sm5077);// [견갑] 슬롯을 개방한 후 착용할 수 있습니다.
				return;
			}
			
			// 휘장 검증
			if (type == L1ItemArmorType.BADGE.getId() 
					&& pc.getBadgeSlotLevel() != 1){// 휘장
				pc.sendPackets(L1ServerMessage.sm5078);// [휘장] 슬롯을 개방한 후 착용할 수 있습니다.
				return;
			}
			
			// 순간이동 조종 반지
			if ((itemId == 20288 || itemId == 202811 || itemId == 202812) 
					&& (pc.getInventory().checkEquipped(202811) || pc.getInventory().checkEquipped(202812) || pc.getInventory().checkEquipped(20288))){
				pc.sendPackets(L1ServerMessage.sm333);// 지금은 이 아이템을 착용할 수 없습니다.
				return;
			}
			
			// 방패 가더 검증
			if ((type == L1ItemArmorType.SHIELD.getId() || type == L1ItemArmorType.GARDER.getId()) 
					&& !is_shield_or_garder_validation(pcInventory, type)) {
				return;
			}
			
			pc.cancelAbsoluteBarrier();
			pcInventory.setEquipped(this, true);
		} else if (is_equip) {// 사용한 방어용 기구를 장비 하고 있었을 경우(탈착을 시도한다)
			if (getBless() == 2) {
				pc.sendPackets(L1ServerMessage.sm150);// 저주를 풀어야 착용을 해제 할 수 있습니다.
				return;
			}
			pcInventory.setEquipped(this, false);
		} else {// 착용중인 방어구와 요청한 방어구가 다를경우.
			if (type == L1ItemArmorType.CLOAK.getId() 
					|| L1ItemArmorType.isRing(type)
					|| type == L1ItemArmorType.EARRING.getId()) {// 망토, 반지, 귀걸이 제외
				pc.sendPackets(L1ServerMessage.sm124);// 같은 슬롯에 이미 아이템을 착용하고 있습니다.
				return;
			}
			// 변신 검증
			if (!L1PolyMorph.isEquipableArmor(pc.getSpriteId(), type)) {// 그 변신에서는 장비 불가
				return;
			}
			
			for (L1ItemInstance item : pc.getEquipSlot().getArmors()) {// 현재 착용중인 방어구를 해제함
				if (item == null) {
					continue;
				}
				if (item.getItem().getType() == type) {
					if (item.getBless() == 2) {
						pc.sendPackets(L1ServerMessage.sm150);// 저주를 풀어야 착용을 해제 할 수 있습니다.
						return;
					}
					pcInventory.setEquipped(item, false);// 착용한 방어구 해제
					pcInventory.setEquipped(this, true);// 요청한 방어구 장착
					break;
				}
			}
		}
	}
}


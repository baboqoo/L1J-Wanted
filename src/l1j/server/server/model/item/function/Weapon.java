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
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class Weapon extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Weapon(L1Item item) {
		super(item);
	}
	
	private L1PcInstance pc;

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha.isDesperado() || cha.isOsiris() || getItem().getItemType() != L1ItemType.WEAPON) {
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
				|| pc.isLancer() && getItem().isUseLancer()) {
			response();
		} else {
			pc.sendPackets(L1ServerMessage.sm264);// 당신의 클래스에서는 이 아이템은 사용할 수 없습니다.
		}
	}
	
	/**
	 * 무기 착용 또는 해제
	 */
	void response() {
		L1PcInventory pcInventory		= pc.getInventory();
		L1ItemInstance currentWeapon	= pc.getEquipSlot().getWeapon();// 현재 착용한 무기
		int weapon_type					= getItem().getType();
		if (currentWeapon == null || !pc.getEquipSlot().isWeapon(this)) {// 지정된 무기가 장비 하고 있는 무기와 다른 경우, 장비 할 수 있을까 확인
			if (!L1PolyMorph.isEquipableWeapon(pc.getSpriteId(), weapon_type)) {// 그변신에서는장비 불가
				return;
			}
			if (getItem().isTwohandedWeapon() && pcInventory.getTypeEquipped(L1ItemType.ARMOR, L1ItemArmorType.SHIELD.getId()) >= 1) {// 양손 무기의 경우, 쉴드(shield) 장비의 확인
				pc.sendPackets(L1ServerMessage.sm128);// 방패를 해제 한 후 양손 무기를 착용 해주세요.
				return;
			}
		}
		pc.cancelAbsoluteBarrier();
		if (currentWeapon != null) {// 이미 무엇인가를 장비 하고 있는 경우, 전의 장비를 뗀다
			if (currentWeapon.getBless() == 2) {// 저주받은 아이템
				pc.sendPackets(L1ServerMessage.sm150);// 저주를 풀어야 착용을 해제 할 수 있습니다.
				return;
			}
			if (pc.getEquipSlot().isWeapon(this)) {// 착용중인 무기와 요청한 무기가 같을경우.
				pcInventory.setEquipped(this, false, false, false);// 장비 교환은 아니고 제외할 뿐
				return;
			} else {// 착용중인 무기와 요청한 무기가 다를경우.
				pcInventory.setEquipped(currentWeapon, false, false, true);// 현제 무기 해제
			}
		}

		if (getBless() == 2) {// 저주받은 아이템
			pc.sendPackets(new S_ServerMessage(149, getLogNameRef()), true);// \f1%0이 손에 들러붙었습니다.
		}
		pcInventory.setEquipped(this, true, false, false);// 요청한 무기를 착용
	}
}


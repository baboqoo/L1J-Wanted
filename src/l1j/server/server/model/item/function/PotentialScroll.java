package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.datatables.MagicDollInfoTable.L1DollInfo;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class PotentialScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public PotentialScroll(L1Item item) {
		super(item);
	}
	
//AUTO SRM: 	private static final S_SystemMessage CALL_FAILE_MSG = new S_SystemMessage("소환중인 마법인형은 강화할 수 없습니다."); // CHECKED OK
	private static final S_SystemMessage CALL_FAILE_MSG = new S_SystemMessage(S_SystemMessage.getRefText(1094), true);

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			L1ItemInstance targetItem = pc.getInventory().getItem(packet.readD());
			if (targetItem == null || targetItem.getItem().get_interaction_type() != L1ItemType.MAGICDOLL.getInteractionType()) {
				return;
			}
			if (isCallDoll(pc)) {
				pc.sendPackets(CALL_FAILE_MSG);
				return;
			}
			L1DollInfo info = MagicDollInfoTable.getDollInfo(targetItem.getItemId());
			if (info == null) {
				return;
			}
			switch(itemId){
			case 704:heroScroll(pc, targetItem, info);break;// 잠재력 강화 주문서(영웅)
			case 705:legeondScroll(pc, targetItem, info);break;// 잠재력 강화 주문서(전설)
			default:break;
			}
		}
	}
	
	boolean isCallDoll(L1PcInstance pc){
		return pc.getDoll() != null && this.getId() == pc.getDoll().getItemObjId();
	}
	
	void heroScroll(L1PcInstance pc, L1ItemInstance targetItem, L1DollInfo info){
		if ((targetItem.getPotential() != null && targetItem.getPotential().getInfo().get_bonus_grade() >= 5) || info.getGrade() < 4) {
			return;
		}
		update(pc, targetItem, MagicDollInfoTable.getPotentialGradeRandomBonusId(4));
	}
	
	void legeondScroll(L1PcInstance pc, L1ItemInstance targetItem, L1DollInfo info){
		if (info.getGrade() < 5) {
			return;
		}
		update(pc, targetItem, MagicDollInfoTable.getPotentialGradeRandomBonusId(5));
	}
	
	void update(L1PcInstance pc, L1ItemInstance targetItem, int bonusId){
		targetItem.setPotential(MagicDollInfoTable.getPotential(bonusId));
		pc.getInventory().updateItem(targetItem, L1PcInventory.COL_DOLL_POTENTIAL);
		pc.getInventory().saveItem(targetItem, L1PcInventory.COL_DOLL_POTENTIAL);
		pc.getInventory().removeItem(this, 1);
	}
}



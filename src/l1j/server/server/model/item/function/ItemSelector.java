package l1j.server.server.model.item.function;

import javolution.util.FastTable;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.SpellMeltTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.inventory.S_ItemsNameIdInSelectionBagNoti;
import l1j.server.server.templates.L1Item;

public class ItemSelector extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public static enum SelectorType{
		NORMAL, SPELL
	}
	
	private SelectorType type;
	
	public ItemSelector(L1Item item) {
		this(item, SelectorType.NORMAL);
	}
	
	public ItemSelector(L1Item item, SelectorType type) {
		super(item);
		this.type = type;
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch(type){
			case SPELL:
				spellSelector(pc);
				break;
			default:
				pc.sendPackets(new S_ItemsNameIdInSelectionBagNoti(this), true);
				break;
			}
		}
	}
	
	private void spellSelector(L1PcInstance pc){
		FastTable<Integer> list = SpellMeltTable.getClassSkillItemList(getItemId(), pc.getType());
		if (list == null || list.isEmpty()) {
			return;
		}
		pc.sendPackets(new S_ItemsNameIdInSelectionBagNoti(this, list), true);
	}
}


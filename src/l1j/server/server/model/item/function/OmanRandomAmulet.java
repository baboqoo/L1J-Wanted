package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class OmanRandomAmulet extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	// 혼돈 830042
	// 변이 830052
	// 일반 830012
	// 지배 830022
	private static final String HIGHT = "혼돈의";
	
	public OmanRandomAmulet(L1Item item) {
		super(item);
	}
	
	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc		= (L1PcInstance) cha;
		boolean is_hight	= getDescKr().startsWith(HIGHT);
		boolean result		= CommonUtil.random(100) + 1 <= (is_hight ? 21 : 11);
		int item_id			= getItemId();
		int create_id		= result ? (is_hight ? item_id - 20 : item_id - 30) : (is_hight ? item_id - 30 : item_id - 40);
		L1ItemInstance item = ItemTable.getInstance().createItem(create_id);
		if (item != null) {
			pc.getInventory().storeItem(item);
			pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true); 
		}
		pc.getInventory().removeItem(this, 1);
	}
	
}


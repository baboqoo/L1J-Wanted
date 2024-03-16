package l1j.server.server.model.Instance;

import l1j.server.server.templates.L1Npc;

public class L1NpcCashShopInstance extends L1NpcShopInstance {
	private static final long serialVersionUID = 1L;

	private String _shopName;

	/**
	 * @param template
	 */
	public L1NpcCashShopInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
	}

	public String getShopName() {
		return _shopName;
	}

	public void setShopName(String name) {
		_shopName = name;
	}

}


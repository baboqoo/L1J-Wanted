package l1j.server.server.model.Instance;

import java.util.ArrayList;

import l1j.server.server.ActionCodes;
import l1j.server.server.serverpackets.action.S_DoActionShop;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;

public class L1NpcShopInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private int _state = 0;
	private String _shopChat;

	/**
	 * @param template
	 */
	public L1NpcShopInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
		if (_state == 1) {
			perceivedFrom.sendPackets(new S_DoActionShop(getId(), ActionCodes.ACTION_Shop, _shopChat), true);
		}
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
	}

	public int getState() {
		return _state;
	}

	public void setState(int i) {
		_state = i;
	}

	public String getShopChat() {
		return _shopChat;
	}

	public void setShopChat(String chat) {
		_shopChat = chat;
	}
	
	private ArrayList<L1PrivateShopSellList> _sellList	= new ArrayList<L1PrivateShopSellList>();
    private ArrayList<L1PrivateShopBuyList> _buyList	= new ArrayList<L1PrivateShopBuyList>();

	public ArrayList<L1PrivateShopSellList> get_sellList() {
		return _sellList;
	}
	public void set_sellList(ArrayList<L1PrivateShopSellList> _sellList) {
		this._sellList = _sellList;
	}

	public ArrayList<L1PrivateShopBuyList> get_buyList() {
		return _buyList;
	}
	public void set_buyList(ArrayList<L1PrivateShopBuyList> _buyList) {
		this._buyList = _buyList;
	}

}


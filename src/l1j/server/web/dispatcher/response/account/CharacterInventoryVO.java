package l1j.server.web.dispatcher.response.account;

import l1j.server.web.dispatcher.response.item.ItemVO;

public class CharacterInventoryVO {
	private int id;
	private ItemVO item;
	private int count;
	private int enchant;
	private int attr;
	private int bless;
	
	public CharacterInventoryVO(int id, ItemVO item, int count, int enchant, int attr, int bless) {
		this.id			= id;
		this.item		= item;
		this.count		= count;
		this.enchant	= enchant;
		this.attr		= attr;
		this.bless		= bless;
	}
	
	public int getId() {
		return id;
	}
	public ItemVO getItem() {
		return item;
	}
	public int getCount() {
		return count;
	}
	public int getEnchant() {
		return enchant;
	}
	public int getAttr() {
		return attr;
	}
	public int getBless() {
		return bless;
	}
}


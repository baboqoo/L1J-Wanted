package l1j.server.web.dispatcher.response.market;

import l1j.server.web.dispatcher.response.item.ItemVO;

public class MarketSearchRankVO {
	private int group_type;
	private int shop_type;
	private int id;
	private ItemVO item;
	private int enchant;
	private int search_rank;
	
	public MarketSearchRankVO(int group_type, int shop_type, int id, ItemVO item, int enchant, int search_rank) {
		this.group_type		= group_type;
		this.shop_type		= shop_type;
		this.id				= id;
		this.item			= item;
		this.enchant		= enchant;
		this.search_rank	= search_rank;
	}
	
	public int getGroup_type() {
		return group_type;
	}
	public int getShop_type() {
		return shop_type;
	}
	public int getId() {
		return id;
	}
	public ItemVO getItem() {
		return item;
	}
	public int getEnchant() {
		return enchant;
	}
	public int getSearch_rank() {
		return search_rank;
	}
}


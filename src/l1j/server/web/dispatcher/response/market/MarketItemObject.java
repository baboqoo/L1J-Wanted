package l1j.server.web.dispatcher.response.market;

import l1j.server.server.utils.StringUtil;

public class MarketItemObject {
	public int itemObjId;
	public int itemId;
	public String charName;
	public String name;
	public int iden;
	public int price;
	public int invGfx;
	public int attr;
	public int type;
	public int enchant;
	public int count;
	public int itemType;
	public String loc;

	public String toString(){
		StringBuilder sb = new StringBuilder(256);
		sb.append("itemId : ").append(this.itemId).append(StringUtil.LineString);
		sb.append("charName : ").append(this.charName).append(StringUtil.LineString);
		sb.append("name : ").append(this.name).append(StringUtil.LineString);
		return sb.toString();
	}
}

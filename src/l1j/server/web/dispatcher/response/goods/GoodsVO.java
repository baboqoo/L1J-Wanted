package l1j.server.web.dispatcher.response.goods;

public class GoodsVO {
	private int id;
	private int itemid;
	private String itemname;
	private int price;
	private GoodsPriceType price_type;
	private int saved_point;
	private GoodsFlag flag;
	private String flagTag_1;
	private String flagTag_2;
	private int pack;
	private int enchant;
	private String iteminfo;
	private int invgfx;
	private int limitCount;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getItemid() {
		return itemid;
	}
	public void setItemid(int itemid) {
		this.itemid = itemid;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public GoodsPriceType getPriceType() {
		return price_type;
	}
	public void setPriceType(GoodsPriceType price_type) {
		this.price_type = price_type;
	}
	public int getSavedPoint() {
		return saved_point;
	}
	public void setSavedPoint(int saved_point) {
		this.saved_point = saved_point;
	}
	public GoodsFlag getFlag() {
		return flag;
	}
	public void setFlag(GoodsFlag flag) {
		this.flag = flag;
		this.flagTag_1 = flag.getTag_1();
		this.flagTag_2 = flag.getTag_2();
	}
	public String getFlagTag_1() {
		return flagTag_1;
	}
	public String getFlagTag_2() {
		return flagTag_2;
	}
	public int getPack() {
		return pack;
	}
	public void setPack(int pack) {
		this.pack = pack;
	}
	public int getEnchant() {
		return enchant;
	}
	public void setEnchant(int enchant) {
		this.enchant = enchant;
	}
	public String getIteminfo() {
		return iteminfo;
	}
	public void setIteminfo(String iteminfo) {
		this.iteminfo = iteminfo;
	}
	public int getInvgfx() {
		return invgfx;
	}
	public void setInvgfx(int invgfx) {
		this.invgfx = invgfx;
	}
	public int getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}
}


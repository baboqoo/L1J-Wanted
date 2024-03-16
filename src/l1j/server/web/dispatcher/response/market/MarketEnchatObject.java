package l1j.server.web.dispatcher.response.market;

public class MarketEnchatObject {
	private String name;
	private int enchant;
	private int gfxId;
	
	/*private String sellTotalPriceInfo	= "정보 없음";
	private String sellNormalPriceInfo	= "정보 없음";
	private String sellBlessPriceInfo	= "정보 없음";
	private String sellCursePriceInfo	= "정보 없음";
	
	private String buyTotalPriceInfo	= "정보 없음";
	private String buyNormalPriceInfo	= "정보 없음";
	private String buyBlessPriceInfo	= "정보 없음";
	private String buyCursePriceInfo	= "정보 없음";*/

	private String sellTotalPriceInfo = "No information";
	private String sellNormalPriceInfo = "No information";
	private String sellBlessPriceInfo = "No information";
	private String sellCursePriceInfo = "No information";
	
	private String buyTotalPriceInfo = "No information";
	private String buyNormalPriceInfo = "No information";
	private String buyBlessPriceInfo = "No information";
	private String buyCursePriceInfo = "No information";	
	
	private int sellTotalCount;
	private int sellNormalCount;
	private int sellBlessCount;
	private int sellCurseCount;
	
	private int buyTotalCount;
	private int buyNormalCount;
	private int buyBlessCount;
	private int buyCurseCount;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEnchant() {
		return enchant;
	}
	public void setEnchant(int enchant) {
		this.enchant = enchant;
	}
	public int getGfxId() {
		return gfxId;
	}
	public void setGfxId(int gfx) {
		gfxId = gfx;
	}
	public String getSellTotalPriceInfo() {
		return sellTotalPriceInfo;
	}
	public void setSellTotalPriceInfo(String sellTotalPrice) {
		this.sellTotalPriceInfo = sellTotalPrice;
	}
	public String getSellNormalPriceInfo() {
		return sellNormalPriceInfo;
	}
	public void setSellNormalPriceInfo(String sellNormalPrice) {
		this.sellNormalPriceInfo = sellNormalPrice;
	}
	public String getSellBlessPriceInfo() {
		return sellBlessPriceInfo;
	}
	public void setSellBlessPriceInfo(String sellBlessPrice) {
		this.sellBlessPriceInfo = sellBlessPrice;
	}
	public String getSellCursePriceInfo() {
		return sellCursePriceInfo;
	}
	public void setSellCursePriceInfo(String sellCursePrice) {
		this.sellCursePriceInfo = sellCursePrice;
	}
	public String getBuyTotalPriceInfo() {
		return buyTotalPriceInfo;
	}
	public void setBuyTotalPriceInfo(String buyTotalPrice) {
		this.buyTotalPriceInfo = buyTotalPrice;
	}
	public String getBuyNormalPriceInfo() {
		return buyNormalPriceInfo;
	}
	public void setBuyNormalPriceInfo(String buyNormalPrice) {
		this.buyNormalPriceInfo = buyNormalPrice;
	}
	public String getBuyBlessPriceInfo() {
		return buyBlessPriceInfo;
	}
	public void setBuyBlessPriceInfo(String buyBlessPrice) {
		this.buyBlessPriceInfo = buyBlessPrice;
	}
	public String getBuyCursePriceInfo() {
		return buyCursePriceInfo;
	}
	public void setBuyCursePriceInfo(String buyCursePrice) {
		this.buyCursePriceInfo = buyCursePrice;
	}
	public int getSellTotalCount() {
		return sellTotalCount;
	}
	public void setSellTotalCount(int sellTotalCount) {
		this.sellTotalCount = sellTotalCount;
	}
	public int getSellNormalCount() {
		return sellNormalCount;
	}
	public void setSellNormalCount(int sellNormalCount) {
		this.sellNormalCount = sellNormalCount;
	}
	public int getSellBlessCount() {
		return sellBlessCount;
	}
	public void setSellBlessCount(int sellBlessCount) {
		this.sellBlessCount = sellBlessCount;
	}
	public int getSellCurseCount() {
		return sellCurseCount;
	}
	public void setSellCurseCount(int sellCurseCount) {
		this.sellCurseCount = sellCurseCount;
	}
	public int getBuyTotalCount() {
		return buyTotalCount;
	}
	public void setBuyTotalCount(int buyTotalCount) {
		this.buyTotalCount = buyTotalCount;
	}
	public int getBuyNormalCount() {
		return buyNormalCount;
	}
	public void setBuyNormalCount(int buyNormalCount) {
		this.buyNormalCount = buyNormalCount;
	}
	public int getBuyBlessCount() {
		return buyBlessCount;
	}
	public void setBuyBlessCount(int buyBlessCount) {
		this.buyBlessCount = buyBlessCount;
	}
	public int getBuyCurseCount() {
		return buyCurseCount;
	}
	public void setBuyCurseCount(int buyCurseCount) {
		this.buyCurseCount = buyCurseCount;
	}
}


package l1j.server.web.dispatcher.response.market;

import java.util.ArrayList;

public class MarketSearchObject {
	private String name;
	private int gfxId;
	private int enchant;
	private ArrayList<BlessObject> sellList;
	private ArrayList<BlessObject> buyList;
	
	/*private String sellTotalPriceInfo	= "정보 없음";
	private String sellNormalPriceInfo	= "정보 없음";
	private String sellBlessPriceInfo	= "정보 없음";
	private String sellCursePriceInfo	= "정보 없음";*/
	private String sellTotalPriceInfo 	= "No information";
	private String sellNormalPriceInfo 	= "No information";
	private String sellBlessPriceInfo 	= "No information";
	private String sellCursePriceInfo 	= "No information";	
	
	private String[] sellTotalPriceArray;
	
	/*private String buyTotalPriceInfo	= "정보 없음";
	private String buyNormalPriceInfo	= "정보 없음";
	private String buyBlessPriceInfo	= "정보 없음";
	private String buyCursePriceInfo	= "정보 없음";*/
	private String buyTotalPriceInfo 	= "No information";
	private String buyNormalPriceInfo 	= "No information";
	private String buyBlessPriceInfo 	= "No information";
	private String buyCursePriceInfo 	= "No information";	
	
	private String[] buyTotalPriceArray;
	
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
	
	public int getGfxId() {
		return gfxId;
	}
	public void setGfxId(int gfxId) {
		this.gfxId = gfxId;
	}
	
	public int getEnchant() {
		return enchant;
	}
	public void setEnchant(int enchant) {
		this.enchant = enchant;
	}
	
	public ArrayList<BlessObject> getSellList() {
		return sellList;
	}
	public void setSellList(ArrayList<BlessObject> sellList) {
		this.sellList = sellList;
	}

	public ArrayList<BlessObject> getBuyList() {
		return buyList;
	}
	public void setBuyList(ArrayList<BlessObject> buyList) {
		this.buyList = buyList;
	}

	public static class BlessObject {
		public int bless;
		public ArrayList<MarketItemObject> itemObjList;
	}

	public String getSellTotalPriceInfo() {
		return sellTotalPriceInfo;
	}
	public void setSellTotalPriceInfo(String sellTotalPriceInfo) {
		this.sellTotalPriceInfo = sellTotalPriceInfo;
	}
	public String getSellNormalPriceInfo() {
		return sellNormalPriceInfo;
	}
	public void setSellNormalPriceInfo(String sellNormalPriceInfo) {
		this.sellNormalPriceInfo = sellNormalPriceInfo;
	}
	public String getSellBlessPriceInfo() {
		return sellBlessPriceInfo;
	}
	public void setSellBlessPriceInfo(String sellBlessPriceInfo) {
		this.sellBlessPriceInfo = sellBlessPriceInfo;
	}
	public String getSellCursePriceInfo() {
		return sellCursePriceInfo;
	}
	public void setSellCursePriceInfo(String sellCursePriceInfo) {
		this.sellCursePriceInfo = sellCursePriceInfo;
	}
	public String[] getSellTotalPriceArray() {
		return sellTotalPriceArray;
	}
	public void setSellTotalPriceArray(String[] sellTotalPriceArray) {
		this.sellTotalPriceArray = sellTotalPriceArray;
	}
	public String getBuyTotalPriceInfo() {
		return buyTotalPriceInfo;
	}
	public void setBuyTotalPriceInfo(String buyTotalPriceInfo) {
		this.buyTotalPriceInfo = buyTotalPriceInfo;
	}
	public String getBuyNormalPriceInfo() {
		return buyNormalPriceInfo;
	}
	public void setBuyNormalPriceInfo(String buyNormalPriceInfo) {
		this.buyNormalPriceInfo = buyNormalPriceInfo;
	}
	public String getBuyBlessPriceInfo() {
		return buyBlessPriceInfo;
	}
	public void setBuyBlessPriceInfo(String buyBlessPriceInfo) {
		this.buyBlessPriceInfo = buyBlessPriceInfo;
	}
	public String getBuyCursePriceInfo() {
		return buyCursePriceInfo;
	}
	public void setBuyCursePriceInfo(String buyCursePriceInfo) {
		this.buyCursePriceInfo = buyCursePriceInfo;
	}
	public String[] getBuyTotalPriceArray() {
		return buyTotalPriceArray;
	}
	public void setBuyTotalPriceArray(String[] buyTotalPriceArray) {
		this.buyTotalPriceArray = buyTotalPriceArray;
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

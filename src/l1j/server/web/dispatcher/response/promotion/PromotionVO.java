package l1j.server.web.dispatcher.response.promotion;

public class PromotionVO {
	private int id;
	private String title;
	private String subText;
	private String promotionDate;
	private String targetLink;
	private String promotionImg;
	private String listallImg;
	
	public PromotionVO(int id, String title, String subText, String promotionDate, String targetLink, String promotionImg, String listallImg) {
		this.id				= id;
		this.title			= title;
		this.subText		= subText;
		this.promotionDate	= promotionDate;
		this.targetLink		= targetLink;
		this.promotionImg	= promotionImg;
		this.listallImg		= listallImg;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubText() {
		return subText;
	}
	public void setSubText(String subText) {
		this.subText = subText;
	}
	public String getPromotionDate() {
		return promotionDate;
	}
	public void setPromotionDate(String promotionDate) {
		this.promotionDate = promotionDate;
	}
	public String getTargetLink() {
		return targetLink;
	}
	public void setTargetLink(String targetLink) {
		this.targetLink = targetLink;
	}
	public String getPromotionImg() {
		return promotionImg;
	}
	public void setPromotionImg(String promotionImg) {
		this.promotionImg = promotionImg;
	}
	public String getListallImg() {
		return listallImg;
	}
	public void setListallImg(String listallImg) {
		this.listallImg = listallImg;
	}
}


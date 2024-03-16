package l1j.server.web.dispatcher.response.guide;

public class GuideRecommendVO {
	private int id;
	private String title;
	private String content;
	private String url;
	private String img;
	
	public GuideRecommendVO(int id, String title, String content, String url, String img) {
		this.id			= id;
		this.title		= title;
		this.content	= content;
		this.url		= url;
		this.img		= img;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
}


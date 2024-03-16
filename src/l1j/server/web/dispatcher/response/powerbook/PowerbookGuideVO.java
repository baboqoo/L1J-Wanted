package l1j.server.web.dispatcher.response.powerbook;

public class PowerbookGuideVO {
	private String group_type;
	private int id;
	private String title;
	private String uri;
	private boolean is_new;
	
	public PowerbookGuideVO(String group_type, int id, String title, String uri, boolean is_new) {
		this.group_type	= group_type;
		this.id			= id;
		this.title		= title;
		this.uri		= uri;
		this.is_new		= is_new;
	}
	
	public String getGroupType() {
		return group_type;
	}
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getUri() {
		return uri;
	}
	public boolean isNew() {
		return is_new;
	}
}


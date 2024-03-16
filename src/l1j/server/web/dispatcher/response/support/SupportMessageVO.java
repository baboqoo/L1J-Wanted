package l1j.server.web.dispatcher.response.support;

public class SupportMessageVO {
	private SupportMessageType type;
	private int index_id;
	private String content;
	
	public SupportMessageVO(SupportMessageType type, int index_id, String content) {
		this.type = type;
		this.index_id = index_id;
		this.content = content;
	}

	public SupportMessageType getType() {
		return type;
	}

	public int getIndexId() {
		return index_id;
	}

	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TYPE: ").append(type.name());
		sb.append(", INDEX: ").append(index_id);
		sb.append(", CONTENT: ").append(content);
		return sb.toString();
	}
}


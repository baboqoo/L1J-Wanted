package l1j.server.web.dispatcher.response.account;

import java.sql.Timestamp;

public class CharacterMailVO {
	private int id;
	private int code;
	private String sender;
	private String receiver;
	private Timestamp date;
	private int templateid;
	private String subject;
	private String content;
	private boolean isCheck;
	
	public CharacterMailVO(int id, int code, String sender, String receiver, Timestamp date, int templateid,
			String subject, String content, boolean isCheck) {
		this.id = id;
		this.code = code;
		this.sender = sender;
		this.receiver = receiver;
		this.date = date;
		this.templateid = templateid;
		this.subject = subject;
		this.content = content;
		this.isCheck = isCheck;
	}
	
	public int getId() {
		return id;
	}
	public int getCode() {
		return code;
	}
	public String getSender() {
		return sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public Timestamp getDate() {
		return date;
	}
	public int getTemplateid() {
		return templateid;
	}
	public String getSubject() {
		return subject;
	}
	public String getContent() {
		return content;
	}
	public boolean isCheck() {
		return isCheck;
	}
}


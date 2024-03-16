package l1j.server.web.dispatcher.response.customer;

import java.sql.Timestamp;

public class CustomerVO {
	private int id;
	private String login;
	private int type;
	private String title;
	private String content;
	private String status;
	private Timestamp date;
	private String comment;
	private Timestamp commentDate;
	private int rownum;
	
	public CustomerVO(int id, String login, int type, String title, String content, String status, Timestamp date, String comment, Timestamp commentDate, int rownum) {
		this.id				= id;
		this.login			= login;
		this.type			= type;
		this.title			= title;
		this.content		= content;
		this.status			= status;
		this.date			= date;
		this.comment		= comment;
		this.commentDate	= commentDate;
		this.rownum			= rownum;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Timestamp getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(Timestamp commentDate) {
		this.commentDate = commentDate;
	}
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
}


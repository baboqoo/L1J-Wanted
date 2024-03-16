package l1j.server.web.dispatcher.response.notice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class NoticeVO {
	private int id;
	private String name;
	private String title;
	private String content;
	private Timestamp date;
	private int readcount;
	private int type;
	private int rownum;
	private boolean top;
	private String mainImg;
	
	public NoticeVO(int id, String name, String title, String content, Timestamp date, int readcount, int type, int rownum, boolean top, String mainImg) {
		this.id			= id;
		this.name		= name;
		this.title		= title;
		this.content	= content;
		this.date		= date;
		this.readcount	= readcount;
		this.type		= type;
		this.rownum		= rownum;
		this.top		= top;
		this.mainImg	= mainImg;
	}

	public NoticeVO(ResultSet rs) throws SQLException{
		this.id			= rs.getInt("id");
		this.name		= rs.getString("name");
		this.title		= rs.getString("title");
		this.content	= rs.getString("content");
		this.date		= rs.getTimestamp("date");
		this.readcount	= rs.getInt("readcount");
		this.type		= rs.getInt("type");
		this.rownum		= rs.getInt("ROWNUM");
		this.top		= Boolean.parseBoolean(rs.getString("top"));
		this.mainImg	= rs.getString("mainImg");
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public boolean isTop() {
		return top;
	}
	public void setTop(boolean top) {
		this.top = top;
	}
	public String getMainImg() {
		return mainImg;
	}
	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}
}


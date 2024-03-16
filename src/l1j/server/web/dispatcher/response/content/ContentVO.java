package l1j.server.web.dispatcher.response.content;

import java.sql.Timestamp;
import java.util.ArrayList;

import l1j.server.common.data.Gender;

public class ContentVO {
	private int id;
	private String name;
	private int chatype;
	private Gender chaGender;
	private Timestamp date;
	private String title;
	private String content;
	private int readcount;
	private String mainImg;
	private boolean top;
	private int rownum;
	private ArrayList<String> likenames;
	private ArrayList<ContentCommentVO> answerList;
	
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
	public int getChatype() {
		return chatype;
	}
	public void setChatype(int chatype) {
		this.chatype = chatype;
	}
	public Gender getChaGender() {
		return chaGender;
	}
	public void setChaGender(Gender Gender) {
		this.chaGender = Gender;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
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
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int read) {
		this.readcount = read;
	}
	public String getMainImg() {
		return mainImg;
	}
	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}
	public boolean isTop() {
		return top;
	}
	public void setTop(boolean top) {
		this.top = top;
	}
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public ArrayList<String> getLikenames() {
		return likenames;
	}
	public void setLikenames(ArrayList<String> likenames) {
		this.likenames = likenames;
	}
	public ArrayList<ContentCommentVO> getAnswerList() {
		return answerList;
	}
	public void setAnswerList(ArrayList<ContentCommentVO> answerList) {
		this.answerList = answerList;
	}
}


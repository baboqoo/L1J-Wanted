package l1j.server.web.dispatcher.response.board;

import java.sql.Timestamp;
import java.util.ArrayList;

import l1j.server.common.data.Gender;

public class BoardCommentVO {
	private int id;
	private int boardId;
	private String name;
	private int chatype;
	private Gender chaGender;
	private Timestamp date;
	private String content;
	private ArrayList<String> likenames;
	
	public BoardCommentVO(int id, int boardId, String name, int chatype, Gender chaGender, Timestamp date, String content, ArrayList<String> likenames) {
		this.id			= id;
		this.boardId	= boardId;
		this.name		= name;
		this.chatype	= chatype;
		this.chaGender	= chaGender;
		this.date		= date;
		this.content	= content;
		this.likenames	= likenames;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
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
	public void setChaGender(Gender chaGender) {
		this.chaGender = chaGender;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ArrayList<String> getLikenames() {
		return likenames;
	}
	public void setLikenames(ArrayList<String> likenames) {
		this.likenames = likenames;
	}
}


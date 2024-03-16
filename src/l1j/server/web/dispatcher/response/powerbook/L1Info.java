package l1j.server.web.dispatcher.response.powerbook;

import java.util.HashMap;

public class L1Info {
	private String name;
	private int infoType;// 1:아이템, 2:몬스터, 3:스킬, 4:가이드
	private HashMap<String, Object> info;
	private String infoText;
	
	public L1Info(String name, int infoType, HashMap<String, Object> info, String infoText) {
		this.name		= name;
		this.infoType	= infoType;
		this.info		= info;
		this.infoText	= infoText;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getInfoType() {
		return infoType;
	}
	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}
	public HashMap<String, Object> getInfo() {
		return info;
	}
	public void setInfo(HashMap<String, Object> info) {
		this.info = info;
	}
	public String getInfoText() {
		return infoText;
	}
	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}
}


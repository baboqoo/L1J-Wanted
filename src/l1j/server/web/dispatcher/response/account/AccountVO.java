package l1j.server.web.dispatcher.response.account;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import l1j.server.Config;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.CommonUtil;
import l1j.server.web.dispatcher.response.alim.AlimVO;
import l1j.server.web.http.connector.HttpLoginSession;

public class AccountVO {
	private String authToken;
	private String name;
	private String password;
	private String ip;
	private int ncoin;
	private int npoint;
	private List<CharacterVO> charList;
	private CharacterVO firstChar;
	private java.util.LinkedList<AlimVO> alimList;
	private boolean termsAgree;
	private int accessLevel;
	private boolean gm;
	private boolean ban;
	private boolean ingame;
	private List<CharacterInventoryVO> normalWarehouse;
	private List<CharacterInventoryVO> packageWarehouse;
	
	public AccountVO(String name, String password, String ip, List<CharacterVO> charList, CharacterVO firstChar, int ncoin, int npoint,
			int accessLevel, boolean ban, boolean termsAgree, boolean ingame,
			java.util.LinkedList<AlimVO> alimList, List<CharacterInventoryVO> normalWarehouse, List<CharacterInventoryVO> packageWarehouse, HttpLoginSession session) {
		this.name				= name;
		this.password			= password;
		this.ip					= ip;
		if (charList == null) {
			this.charList		= new ArrayList<CharacterVO>();
		} else {
			this.charList		= charList;
		}
		if (firstChar == null) {
			if (!this.charList.isEmpty()) {
				this.firstChar	= this.charList.get(0);
			}
		} else {
			this.firstChar		= firstChar;
			if (this.charList.isEmpty()) {
				this.charList.add(this.firstChar);
			}
		}
		this.ncoin				= ncoin;
		this.npoint				= npoint;
		this.accessLevel		= accessLevel;
		this.gm					= this.accessLevel == Config.WEB.WEB_GM_CODE;
		this.ban				= ban;
		this.termsAgree			= termsAgree;
		this.ingame				= ingame;
		this.alimList			= alimList;
		this.normalWarehouse	= normalWarehouse;
		this.packageWarehouse	= packageWarehouse;
		// 웹 로그인인 경우 세션이 없기 때문에 승인토큰을 생성한다.
		if (session == null) {
			makeAuthToken();
		} else {
			this.authToken		= session.getAuthToken();
		}
	}
	
	public String getAuthToken() {
		return authToken;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getNcoin() {
		return ncoin;
	}
	public void setNcoin(int ncoin) {
		this.ncoin = ncoin;
	}
	public int getNpoint() {
		return npoint;
	}
	public void setNpoint(int npoint) {
		this.npoint = npoint;
	}
	public List<CharacterVO> getCharList() {
		return charList;
	}
	public CharacterVO getFirstChar() {
		return firstChar;
	}
	public void setFirstChar(CharacterVO firstChar) {
		this.firstChar = firstChar;
	}
	public void setFirstChar(int charId){
		for (CharacterVO cha : charList) {
			if (cha.getObjId() == charId) {
				firstChar = cha;
				break;
			}
		}
	}
	
	public java.util.LinkedList<AlimVO> getAlimList() {
		return alimList;
	}
	
	public boolean isTermsAgree() {
		return termsAgree;
	}
	public void setTermsAgree(boolean termsAgree) {
		this.termsAgree = termsAgree;
	}
	
	public int getAccessLevel() {
		return accessLevel;
	}
	
	public boolean isGm() {
		return gm;
	}
	
	public boolean isBan() {
		return ban;
	}
	public void setBan(boolean ban) {
		this.ban = ban;
	}
	
	public boolean isIngame() {
		return ingame;
	}
	public void setIngame(boolean ingame) {
		this.ingame = ingame;
	}
	
	public List<CharacterInventoryVO> getNormalWarehouse() {
		return normalWarehouse;
	}
	public List<CharacterInventoryVO> getPackageWarehouse() {
		return packageWarehouse;
	}
	
	public void makeAuthToken() {
		byte[] encoded	= String.format("%s.%d", this.name, System.currentTimeMillis()).getBytes(CharsetUtil.UTF_8);
		byte[] password	= this.password.getBytes(CharsetUtil.UTF_8);
		CommonUtil.encode_xor(encoded, password, 0, encoded.length);
		authToken = String.format("%s=", Base64.getEncoder().encodeToString(encoded));
	}
	
}


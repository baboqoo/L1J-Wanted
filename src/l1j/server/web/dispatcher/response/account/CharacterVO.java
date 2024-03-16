package l1j.server.web.dispatcher.response.account;

import java.util.List;

import l1j.server.common.data.Gender;
import l1j.server.web.dispatcher.response.world.BloodPledgeVO;

public class CharacterVO implements Comparable<CharacterVO> {
	private String accountName;
	private int objId;
	private String name;
	private int level;
	private int exp;
	private int expPercent;
	private int maxhp;
	private int maxmp;
	private int str;
	private int con;
	private int dex;
	private int cha;
	private int intel;
	private int wis;
	private int type;
	private String className;
	private Gender gender;
	private int clanid;
	private BloodPledgeVO clan;
	private String clanName;
	private int lawful;
	private int pk;
	private int accessLevel;
	private boolean onlineStatus;
	private String profileUrl;
	private int allRank;
	private int classRank;
	private boolean gm;
	private List<CharacterInventoryVO> inventory;
	private List<CharacterMailVO> mail;
	
	public CharacterVO(String accountName, int objId, String name, int level, int exp, int expPercent, int maxhp, int maxmp, 
			int str, int con, int dex, int cha, int intel, int wis, int type, String className, Gender gender,
			int clanid, BloodPledgeVO clan, String clanName, int lawful, int pk, int accessLevel, boolean onlineStatus, String profileUrl,
			int allRank, int classRank, boolean gm, List<CharacterInventoryVO> inventory, List<CharacterMailVO> mail) {
		this.accountName	= accountName;
		this.objId			= objId;
		this.name			= name;
		this.level			= level;
		this.exp			= exp;
		this.expPercent		= expPercent;
		this.maxhp			= maxhp;
		this.maxmp			= maxmp;
		this.str			= str;
		this.con			= con;
		this.dex			= dex;
		this.cha			= cha;
		this.intel			= intel;
		this.wis			= wis;
		this.type			= type;
		this.className		= className;
		this.gender			= gender;
		this.clanid			= clanid;
		this.clan			= clan;
		this.clanName		= clanName;
		this.lawful			= lawful;
		this.pk				= pk;
		this.accessLevel	= accessLevel;
		this.onlineStatus	= onlineStatus;
		this.profileUrl		= profileUrl;
		this.allRank		= allRank;
		this.classRank		= classRank;
		this.gm				= gm;
		this.inventory		= inventory;
		this.mail			= mail;
	}
	
	public String getAccountName() {
		return accountName;
	}
	public int getObjId() {
		return objId;
	}
	public String getName() {
		return name;
	}
	public int getLevel() {
		return level;
	}
	public int getExp() {
		return exp;
	}
	public int getExpPercent() {
		return expPercent;
	}
	public int getMaxhp() {
		return maxhp;
	}
	public int getMaxmp() {
		return maxmp;
	}
	public int getStr() {
		return str;
	}
	public int getCon() {
		return con;
	}
	public int getDex() {
		return dex;
	}
	public int getCha() {
		return cha;
	}
	public int getIntel() {
		return intel;
	}
	public int getWis() {
		return wis;
	}
	public int getType() {
		return type;
	}
	public Gender getGender() {
		return gender;
	}
	public String getClassName() {
		return className;
	}
	public int getClanid() {
		return clanid;
	}
	public BloodPledgeVO getClan() {
		return clan;
	}
	public String getClanName() {
		return clanName;
	}
	public int getAccessLevel() {
		return accessLevel;
	}
	public boolean isOnlineStatus() {
		return onlineStatus;
	}
	public String getProfileUrl() {
		return profileUrl;
	}
	public int getAllRank() {
		return allRank;
	}
	public int getClassRank() {
		return classRank;
	}
	public boolean isGm() {
		return gm;
	}
	public int getLawful() {
		return lawful;
	}
	public int getPk() {
		return pk;
	}
	public List<CharacterInventoryVO> getInventory() {
		return inventory;
	}
	public List<CharacterMailVO> getMail() {
		return mail;
	}
	
	@Override
	public int compareTo(CharacterVO o) {
		return o.getExp() - getExp();
	}
	
}


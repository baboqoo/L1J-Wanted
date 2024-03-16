package l1j.server.web.dispatcher.response.guide;

public class GuideBossVO {
	private int id;
	private int loc;
	private String locName;
	private int number;
	private String bossName;
	private String bossImg;
	private String spawnLoc;
	private String spawnTime;
	private String dropName;
	
	public GuideBossVO(int id, int loc, String locName, int number, String bossName, String bossImg, String spawnLoc, String spawnTime, String dropName) {
		this.id			= id;
		this.loc		= loc;
		this.locName	= locName;
		this.number		= number;
		this.bossName	= bossName;
		this.bossImg	= bossImg;
		this.spawnLoc	= spawnLoc;
		this.spawnTime	= spawnTime;
		this.dropName	= dropName;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLoc() {
		return loc;
	}
	public void setLoc(int loc) {
		this.loc = loc;
	}
	public String getLocName() {
		return locName;
	}
	public void setLocName(String locName) {
		this.locName = locName;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getBossName() {
		return bossName;
	}
	public void setBossName(String bossName) {
		this.bossName = bossName;
	}
	public String getBossImg() {
		return bossImg;
	}
	public void setBossImg(String bossImg) {
		this.bossImg = bossImg;
	}
	public String getSpawnLoc() {
		return spawnLoc;
	}
	public void setSpawnLoc(String spawnLoc) {
		this.spawnLoc = spawnLoc;
	}
	public String getSpawnTime() {
		return spawnTime;
	}
	public void setSpawnTime(String spawnTime) {
		this.spawnTime = spawnTime;
	}
	public String getDropName() {
		return dropName;
	}
	public void setDropName(String dropName) {
		this.dropName = dropName;
	}
}


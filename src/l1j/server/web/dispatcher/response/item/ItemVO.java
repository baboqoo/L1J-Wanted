package l1j.server.web.dispatcher.response.item;

import java.util.ArrayList;

public class ItemVO {
	private int itemid;
	private int item_name_id;
	private String name;
	private int invgfx;
	private int itemType;
	private String useType;
	private int weight;
	private String material;
	private int safenchant;
	private ArrayList<String> useClass;
	private int ac;
	private int smallDmg;
	private int largeDmg;
	private int hit;
	private int dmg;
	private int longHit;
	private int longDmg;
	private int mr;
	private int sp;
	private boolean canbedmg;
	private boolean twohand;
	private String magicName;
	private boolean trade;
	private String bless;
	private ArrayList<Integer> dropMonster;
	
	public int getItemid() {
		return itemid;
	}
	public void setItemid(int itemid) {
		this.itemid = itemid;
	}
	public int getItemNameId() {
		return item_name_id;
	}
	public void setItemNameId(int item_name_id) {
		this.item_name_id = item_name_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getInvgfx() {
		return invgfx;
	}
	public void setInvgfx(int invgfx) {
		this.invgfx = invgfx;
	}
	public int getItemType() {
		return itemType;
	}
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public int getSafenchant() {
		return safenchant;
	}
	public void setSafenchant(int safenchant) {
		this.safenchant = safenchant;
	}
	public ArrayList<String> getUseClass() {
		return useClass;
	}
	public void setUseClass(ArrayList<String> useClass) {
		this.useClass = useClass;
	}
	public int getAc() {
		return ac;
	}
	public void setAc(int ac) {
		this.ac = ac;
	}
	public int getSmallDmg() {
		return smallDmg;
	}
	public void setSmallDmg(int smallDmg) {
		this.smallDmg = smallDmg;
	}
	public int getLargeDmg() {
		return largeDmg;
	}
	public void setLargeDmg(int largeDmg) {
		this.largeDmg = largeDmg;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public int getDmg() {
		return dmg;
	}
	public void setDmg(int dmg) {
		this.dmg = dmg;
	}
	public int getLongHit() {
		return longHit;
	}
	public void setLongHit(int longHit) {
		this.longHit = longHit;
	}
	public int getLongDmg() {
		return longDmg;
	}
	public void setLongDmg(int longDmg) {
		this.longDmg = longDmg;
	}
	public int getMr() {
		return mr;
	}
	public void setMr(int mr) {
		this.mr = mr;
	}
	public int getSp() {
		return sp;
	}
	public void setSp(int sp) {
		this.sp = sp;
	}
	public boolean isCanbedmg() {
		return canbedmg;
	}
	public void setCanbedmg(boolean canbedmg) {
		this.canbedmg = canbedmg;
	}
	public boolean isTwohand() {
		return twohand;
	}
	public void setTwohand(boolean twohand) {
		this.twohand = twohand;
	}
	public String getMagicName() {
		return magicName;
	}
	public void setMagicName(String magicName) {
		this.magicName = magicName;
	}
	public boolean isTrade() {
		return trade;
	}
	public void setTrade(boolean trade) {
		this.trade = trade;
	}
	public String getBless() {
		return bless;
	}
	public void setBless(String bless) {
		this.bless = bless;
	}
	public ArrayList<Integer> getDropMonster() {
		return dropMonster;
	}
	public void setDropMonster(ArrayList<Integer> dropMonster) {
		this.dropMonster = dropMonster;
	}
}


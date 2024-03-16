package l1j.server.server.templates;

public class L1CharEinStat {
	private int objid;
	private byte bless, lucky, vital, itemSpellProb, absoluteRegen, potion;
	private int bless_efficiency, bless_exp;
	private int lucky_item, lucky_adena;
	private int vital_potion, vital_heal;
	private int itemSpellProb_armor, itemSpellProb_weapon;
	private int absoluteRegen_hp, absoluteRegen_mp;
	private int potion_critical, potion_delay;
	
	public L1CharEinStat(int objid) {
		this(objid,
				(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
				0, 0, 
				0, 0, 
				0, 0, 
				0, 0, 
				0, 0, 
				0, 0);
	}
	
	public L1CharEinStat(int objid,
			byte bless, byte lucky, byte vital, byte itemSpellProb, byte absoluteRegen, byte potion, 
			int bless_efficiency, int bless_exp, 
			int lucky_item, int lucky_adena,
			int vital_potion, int vital_heal, 
			int itemSpellProb_armor, int itemSpellProb_weapon, 
			int absoluteRegen_hp, int absoluteRegen_mp, 
			int potion_critical, int potion_delay) {
		this.objid = objid;
		this.bless = bless;
		this.lucky = lucky;
		this.vital = vital;
		this.itemSpellProb = itemSpellProb;
		this.absoluteRegen = absoluteRegen;
		this.potion = potion;
		this.bless_efficiency = bless_efficiency;
		this.bless_exp = bless_exp;
		this.lucky_item = lucky_item;
		this.lucky_adena = lucky_adena;
		this.vital_potion = vital_potion;
		this.vital_heal = vital_heal;
		this.itemSpellProb_armor = itemSpellProb_armor;
		this.itemSpellProb_weapon = itemSpellProb_weapon;
		this.absoluteRegen_hp = absoluteRegen_hp;
		this.absoluteRegen_mp = absoluteRegen_mp;
		this.potion_critical = potion_critical;
		this.potion_delay = potion_delay;
	}

	public int getObjid() {
		return objid;
	}

	public void setObjid(int val) {
		this.objid = val;
	}

	public byte getBless() {
		return bless;
	}

	public void setBless(byte val) {
		this.bless = val;
	}

	public byte getLucky() {
		return lucky;
	}

	public void setLucky(byte val) {
		this.lucky = val;
	}

	public byte getVital() {
		return vital;
	}

	public void setVital(byte val) {
		this.vital = val;
	}

	public byte getItemSpellProb() {
		return itemSpellProb;
	}

	public void setItemSpellProb(byte val) {
		this.itemSpellProb = val;
	}

	public byte getAbsoluteRegen() {
		return absoluteRegen;
	}

	public void setAbsoluteRegen(byte val) {
		this.absoluteRegen = val;
	}

	public byte getPotion() {
		return potion;
	}

	public void setPotion(byte val) {
		this.potion = val;
	}

	public int getBless_efficiency() {
		return bless_efficiency;
	}

	public void setBless_efficiency(int val) {
		this.bless_efficiency = val;
	}

	public int getBless_exp() {
		return bless_exp;
	}

	public void setBless_exp(int val) {
		this.bless_exp = val;
	}

	public int getLucky_item() {
		return lucky_item;
	}

	public void setLucky_item(int val) {
		this.lucky_item = val;
	}

	public int getLucky_adena() {
		return lucky_adena;
	}

	public void setLucky_adena(int val) {
		this.lucky_adena = val;
	}

	public int getVital_potion() {
		return vital_potion;
	}

	public void setVital_potion(int val) {
		this.vital_potion = val;
	}

	public int getVital_heal() {
		return vital_heal;
	}

	public void setVital_heal(int val) {
		this.vital_heal = val;
	}

	public int getItemSpellProb_armor() {
		return itemSpellProb_armor;
	}

	public void setItemSpellProb_armor(int val) {
		this.itemSpellProb_armor = val;
	}

	public int getItemSpellProb_weapon() {
		return itemSpellProb_weapon;
	}

	public void setItemSpellProb_weapon(int val) {
		this.itemSpellProb_weapon = val;
	}

	public int getAbsoluteRegen_hp() {
		return absoluteRegen_hp;
	}

	public void setAbsoluteRegen_hp(int val) {
		this.absoluteRegen_hp = val;
	}

	public int getAbsoluteRegen_mp() {
		return absoluteRegen_mp;
	}

	public void setAbsoluteRegen_mp(int val) {
		this.absoluteRegen_mp = val;
	}

	public int getPotion_critical() {
		return potion_critical;
	}

	public void setPotion_critical(int val) {
		this.potion_critical = val;
	}

	public int getPotion_delay() {
		return potion_delay;
	}

	public void setPotion_delay(int val) {
		this.potion_delay = val;
	}
	
	public void reset(){
		bless = lucky = vital = itemSpellProb = absoluteRegen = potion = 0;
		bless_efficiency = bless_exp = 0;
		lucky_item = lucky_adena = 0;
		vital_potion = vital_heal = 0;
		itemSpellProb_armor = itemSpellProb_weapon = 0;
		absoluteRegen_hp = absoluteRegen_mp = 0;
		potion_critical = potion_delay = 0;
	}
}


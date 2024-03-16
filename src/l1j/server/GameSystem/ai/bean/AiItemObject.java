package l1j.server.GameSystem.ai.bean;

/**
 * AI 아이템 오브젝트
 * @author LinOffice
 */
public class AiItemObject {
	private int type, itemId, count, enchantLevel, attrLevel;
	private boolean equip;
	
	public AiItemObject(int type, int itemId, int count, int enchantLevel, int attrLevel, boolean equip) {
		this.type			= type;
		this.itemId			= itemId;
		this.count			= count;
		this.enchantLevel	= enchantLevel;
		this.attrLevel		= attrLevel;
		this.equip			= equip;
	}
	
	public int getType() {
		return type;
	}
	public int getItemId() {
		return itemId;
	}
	public int getCount() {
		return count;
	}
	public int getEnchantLevel() {
		return enchantLevel;
	}
	public int getAttrLevel() {
		return attrLevel;
	}
	public boolean isEquip() {
		return equip;
	}
}


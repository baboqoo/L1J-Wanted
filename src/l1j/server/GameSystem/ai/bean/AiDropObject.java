package l1j.server.GameSystem.ai.bean;

/**
 * AI 드랍 아이템 오브젝트
 * @author LinOffice
 */
public class AiDropObject {
	private int type, itemId, count, chance;
	
	public AiDropObject(int type, int itemId, int count, int chance) {
		this.type		= type;
		this.itemId		= itemId;
		this.count		= count;
		this.chance		= chance;
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
	public int getChance() {
		return chance;
	}
}


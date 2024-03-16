package l1j.server.server.model.item.collection.favor.bean;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.StringUtil;

public class L1FavorBookUserObject {
	private L1FavorBookCategoryObject category;
	private int slotId;
	private L1ItemInstance currentItem;
	private int craftId;
	private int awakening;
	private L1FavorBookObject obj;
	
	public L1FavorBookUserObject(L1FavorBookCategoryObject category, int slotId, int craftId, int awakening, L1FavorBookObject obj) {
		this(category, slotId, null, craftId, awakening, obj);
	}
	
	public L1FavorBookUserObject(L1FavorBookCategoryObject category, int slotId, L1ItemInstance currentItem, int craftId, int awakening, L1FavorBookObject obj) {
		this.category		= category;
		this.slotId			= slotId;
		this.currentItem	= currentItem;
		this.craftId		= craftId;
		this.awakening		= awakening;
		this.obj			= obj;
	}

	public L1FavorBookCategoryObject getCategory() {
		return category;
	}
	public void setCategory(L1FavorBookCategoryObject category) {
		this.category = category;
	}

	public int getSlotId() {
		return slotId;
	}
	public void setSlotId(int index) {
		this.slotId = index;
	}

	public L1ItemInstance getCurrentItem() {
		return currentItem;
	}
	public void setCurrentItem(L1ItemInstance currentItem) {
		this.currentItem = currentItem;
	}
	
	public int getCraftId() {
		return craftId;
	}
	public void setCraftId(int craftId) {
		this.craftId = craftId;
	}
	
	public int getAwakening() {
		return awakening;
	}
	public void setAwakening(int awakening) {
		this.awakening = awakening;
	}

	public L1FavorBookObject getObj() {
		return obj;
	}
	public void setObj(L1FavorBookObject obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("category: ").append(category.getCategory()).append(StringUtil.LineString);
		sb.append("slotId: ").append(slotId).append(StringUtil.LineString);
		sb.append("itemObjId: ").append(currentItem == null ? 0 : currentItem.getId()).append(StringUtil.LineString);
		sb.append("itemId: ").append(currentItem == null ? 0 : currentItem.getItemId()).append(StringUtil.LineString);
		sb.append("craftId: ").append(craftId).append(StringUtil.LineString);
		sb.append("awakening: ").append(awakening);
		return sb.toString();
	}
}


package l1j.server.server.model.item.collection.favor.bean;

import java.util.ArrayList;

import l1j.server.common.bin.favorbook.AUBIBookInfoForClient;
import l1j.server.server.utils.StringUtil;

public class L1FavorBookObject {
	private int listId;
	private L1FavorBookCategoryObject category;
	private int slotId;
	private ArrayList<Integer> itemIds;
	private AUBIBookInfoForClient.BookT.CategoryT.SlotT slotT;
	
	public L1FavorBookObject(int listId, L1FavorBookCategoryObject category,
			int slotId,
			ArrayList<Integer> itemIds) {
		this.listId		= listId;
		this.category	= category;
		this.slotId		= slotId;
		this.itemIds	= itemIds;
		this.slotT		= category.getCategoryT().get_slots().get(slotId);
	}

	public int getListId() {
		return listId;
	}
	public L1FavorBookCategoryObject getCategory() {
		return category;
	}
	public int getSlotId() {
		return slotId;
	}
	public AUBIBookInfoForClient.BookT.CategoryT.SlotT getSlotT() {
		return slotT;
	}
	
	public ArrayList<Integer> getItemIds() {
		return itemIds;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("listId : ").append(listId).append(StringUtil.LineString);
		sb.append("category : ").append(category.getCategory()).append(StringUtil.LineString);
		sb.append("slotId : ").append(slotId).append(StringUtil.LineString);
		sb.append("itemIds size : ").append(itemIds == null ? 0 : itemIds.size()).append(StringUtil.LineString);
		return sb.toString();
	}
}


package l1j.server.server.model.item.collection.time.bean;

import java.util.HashMap;

import l1j.server.common.bin.timecollection.TimeCollection;
import l1j.server.server.utils.StringUtil;

/**
 * 실렉티스 전시회 오브젝트
 * @author LinOffice
 */
public class L1TimeCollection {
	private int groupId;
	private TimeCollection.GroupT.SetT set;
	private L1TimeCollectionDuration duration;
	private HashMap<Integer, L1TimeCollectionMaterial> material;// Key: slotIndex, Value: Material
	
	public L1TimeCollection(TimeCollection collect, int groupId, TimeCollection.GroupT.SetT set) {
		this.groupId			= groupId;
		this.set				= set;
		this.duration			= new L1TimeCollectionDuration(collect, set);
		this.material			= new HashMap<>();
		// 재료 로드
		for (TimeCollection.GroupT.SetT.ItemSlotT slotT : set.get_ItemSlot()) {
			putMaterial(new L1TimeCollectionMaterial(collect, slotT));
		}
	}
	
	public int getGroupId() {
		return groupId;
	}
	
	public TimeCollection.GroupT.SetT getSet() {
		return set;
	}
	
	public L1TimeCollectionDuration getDuration() {
		return duration;
	}
	
	public L1TimeCollectionMaterial getMaterial(int slotIndex) {
		return material.get(slotIndex);
	}
	
	public void putMaterial(L1TimeCollectionMaterial obj){
		this.material.put(obj.getSlotIndex(), obj);
	}
	
	public int getSlotSize(){
		return material.size();
	}
	
	public boolean isValidation() {
		return !material.isEmpty() && duration != null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("groupId: ").append(groupId).append(StringUtil.LineString);
		sb.append("setId: ").append(set.get_ID()).append(StringUtil.LineString);
		sb.append("material size: ").append(material.size()).append(StringUtil.LineString);
		return sb.toString();
	}
	
}


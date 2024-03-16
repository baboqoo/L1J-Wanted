package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionMaterial;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;

public class A_TimeCollectionRegist extends ProtoHandler {
	protected A_TimeCollectionRegist(){}
	private A_TimeCollectionRegist(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int itemObjId;
	private int groupId;
	private int setId;
	private int slotIndex;
	
	void parsePacket(){
		if (_total_length < 2) {
			return;
		}
		while (!isEnd()) {
			int code	= readC();
			switch (code) {
			case 0x08:
				itemObjId = readBit();
				break;
			case 0x10:
				groupId = readC();
				break;
			case 0x18:
				setId = readC();
				break;
			case 0x20:
				slotIndex = readC();
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation(){
		return itemObjId > 0 && groupId > 0 && setId > 0 && slotIndex > 0;
	}

	@Override
	protected void doWork() throws Exception {
		if (!Config.COLLECTION.TIME_COLLECTION_ACTIVE || _pc == null || _pc.isGhost()) {
			return;
		}
		parsePacket();
		if (!isValidation()) {
			return;
		}
		// 보유 아이템 검사
		L1PcInventory inv					= _pc.getInventory();
		L1ItemInstance registItem			= inv.findItemObjId(itemObjId);
		if (registItem == null) {
			System.out.println(String.format(
					"[A_TimeCollectionRegist] REGIST_ITEM_EMPTY : OBJID(%d), NAME(%s)", 
					itemObjId, _pc.getName()));
			return;
		}
		
		if (registItem.isEquipped() 
				|| registItem.getBless() >= 128
				|| registItem.getEndTime() != null
				|| registItem.isEngrave()
				|| registItem.isSlot()) {
			_pc.sendPackets(L1ServerMessage.sm9023);// 선택한 장비를 전시 등록할 수 없습니다.
			return;
		}
		
		// 컬렉션 여부 검사
		L1TimeCollection collection			= L1TimeCollectionLoader.getData(groupId, setId);
		if (collection == null) {
			System.out.println(String.format(
					"[A_TimeCollectionRegist] NOT_COLLECTION : GROUP(%d), ID(%d), ITEMID(%d), NAME(%s)",
					groupId, setId, registItem.getItemId(), _pc.getName()));
			return;
		}
		
		// 재료 검사
		L1TimeCollectionMaterial material	= collection.getMaterial(slotIndex);
		if (material == null || !material.isMaterial(registItem)) {
			System.out.println(String.format(
					"[A_TimeCollectionRegist] NOT_MATERIAL : GROUP(%d), ID(%d), SLOT_INDEX(%d), ITEMID(%d), NAME(%s)",
					groupId, setId, slotIndex, registItem.getItemId(), _pc.getName()));
			return;
		}
		
		// 등록
		if (!_pc.getTimeCollection().regist(collection, registItem, slotIndex)) {
			System.out.println(String.format(
					"[A_TimeCollectionRegist] REGIST_FAILURE : GROUP(%d), ID(%d), SLOT_INDEX(%d), ITEMID(%d), NAME(%s)",
					groupId, setId, slotIndex, registItem.getItemId(), _pc.getName()));
			return;
		}
		inv.removeItem(registItem);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_TimeCollectionRegist(data, client);
	}

}


package l1j.server.server.clientpackets.proto;

import java.util.Iterator;

import javolution.util.FastMap;
import l1j.server.GameSystem.eventpush.EventPushLoader;
import l1j.server.GameSystem.eventpush.bean.EventPushObject;
import l1j.server.GameSystem.eventpush.user.EventPushUser;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.eventpush.S_EventPushItemListReceive;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class A_EventPushItemListReceive extends ProtoHandler {
	protected A_EventPushItemListReceive(){}
	private A_EventPushItemListReceive(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (_total_length <= 0) {
			return;
		}
		FastMap<Integer, Integer> receiveMap = new FastMap<Integer, Integer>();
		for (int i=0; i<_total_length; i++) {
			if (readLength() == 0) {
				break;
			}
			int code = readC();
			if (code == 0x0a) {// 하나의 정보
				readP(2);
				int index = readBit();
				readP(1);// 10
				int number = readC();
				receiveMap.put(index, number);
			} else {
				break;
			}
		}
		if (receiveMap.isEmpty()) {
			return;
		}
		EventPushUser userInfo = EventPushLoader.getInfo(_pc.getId());
		if (userInfo == null) {
			return;
		}
		Iterator<Integer> itr = receiveMap.keySet().iterator();
		EventPushObject obj = null;
		long currentTime = System.currentTimeMillis();
		while(itr.hasNext()){
			obj = userInfo.getTemp(itr.next());
			if (obj == null || obj.getEnableDate().getTime() <= currentTime) {
				continue;
			}
			if (obj.isUsedImmediately()) {// 즉시 사용
				int item_id = obj.getItem().getItemId();
				if (item_id == L1ItemId.EIN_BLESS_BONUS_50) {
					_pc.einGetExcute(50);
				} else if (item_id == L1ItemId.EIN_BLESS_BONUS_100) {
					_pc.einGetExcute(100);
				}
			} else {
				if (obj.getItem().isMerge()) {
					createItem(obj.getItem(), obj.getItemAmount(), obj.getItemEnchant());
				} else {
					for (int i=0; i<obj.getItemAmount(); i++) {
						createItem(obj.getItem(), 1, obj.getItemEnchant());
					}
				}
			}
			obj.setStatus(2);// 받기
		}
		_pc.sendPackets(new S_EventPushItemListReceive(receiveMap), true);
	}
	
	void createItem(L1Item template, int amount, int enchant) {
		L1ItemInstance item = ItemTable.getInstance().createItem(template);
		if (item == null) {
			return;
		}
		item.setCount(amount);
		item.setEnchantLevel(enchant);
		item.setIdentified(true);
		if (_pc.getInventory().checkAddItem(item, item.getCount()) != L1Inventory.OK) return;
		_pc.getInventory().storeItem(item);
		//_pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getDescKr(), amount)), true);
		_pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getDesc(), amount)), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EventPushItemListReceive(data, client);
	}

}


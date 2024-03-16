package l1j.server.server.clientpackets.proto;

import java.sql.Timestamp;

import javolution.util.FastTable;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemSelectorTable;
import l1j.server.server.datatables.SpellMeltTable;
import l1j.server.server.datatables.ItemSelectorTable.SelectorData;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class A_ItemSelect extends ProtoHandler {
	protected A_ItemSelect(){}
	private A_ItemSelect(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private L1PcInventory inv;
	private int _bag_obj_id;
	private int _want_item_name_id;
	private int _use_count;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		_bag_obj_id				= readBit();
		inv						= _pc.getInventory();
		L1ItemInstance bag_item	= inv.getItem(_bag_obj_id);
		if (bag_item == null) {
			return;
		}
		readP(1);// 0x10
		_want_item_name_id		= readBit();
		if (_want_item_name_id <= 0) {
			return;
		}
		L1Item want_item = ItemTable.getInstance().findItemByNameId(_want_item_name_id);
		if (want_item == null) {
			System.out.println(String.format(
					"[A_ItemSelect] SELECT_WANT_ITEM_EMPTY : BAG_NAME_ID(%d), WANT_NAME_ID(%d)", 
					bag_item.getItem().getItemNameId(), _want_item_name_id));
			return;
		}
		
		if (SpellMeltTable.isSpellMeltItem(bag_item.getItemId())) {
			readP(1);// 0x18
			_use_count = readBit();
			if (_use_count < 1) {
				return;
			}
			if (_use_count > bag_item.getCount()) {
				_use_count = bag_item.getCount();
			}
			createItem(want_item, _use_count, 0, want_item.getBless(), 0, true, 0);
			inv.removeItem(bag_item, _use_count);
			return;
		}
		
		SelectorData choice = null;
		FastTable<SelectorData> list = ItemSelectorTable.getSelectorInfo(bag_item.getItemId());
		if (list == null || list.isEmpty()) {
			return;
		}
		
		for (SelectorData data : list) {
			if (data == null) {
				continue;
			}
			if (data._template.getItemNameId() == _want_item_name_id) {
				choice = data;
				break;
			}
		}
		if (choice == null) {
			System.out.println(String.format(
					"[A_ItemSelect] CHOICE_WANT_ITEM_EMPTY : BAG_NAME_ID(%d), WANT_NAME_ID(%d)", 
					bag_item.getItem().getItemNameId(), _want_item_name_id));
			return;
		}
		readP(1);// 0x18
		int selectCount = readBit();
		if (selectCount < 1) {
			return;
		}
		if (selectCount > bag_item.getCount()) {
			selectCount = bag_item.getCount();
		}
		
		createItem(choice._template, choice._count * selectCount, choice._enchant, choice._bless, choice._attr, true, choice._limitTime);
		if (choice._delete) {
			inv.removeItem(bag_item, selectCount);
		}
	}
	
	void createItem(L1Item template, int count, int enchantLevel, int bless, int attr, boolean identi, int limitTime) {
		if (template == null) {
			return;
		}
		ItemTable itemTable = ItemTable.getInstance();
		if (template.isMerge()) {
			L1ItemInstance item = itemTable.createItem(template);
			item.setCount(count);
			item.setIdentified(identi);
			item.setBless(bless);
			if (limitTime > 0) {
				limitTimeItem(item, limitTime);
			}
			if (inv.checkAddItem(item, count) == L1Inventory.OK) {
				inv.storeItem(item);
				_pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
			} else {
				L1World.getInstance().getInventory(_pc.getX(), _pc.getY(), _pc.getMapId()).storeItem(item);
				_pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			}
		} else {
			for (int i=0; i<count; i++) {
				L1ItemInstance item = itemTable.createItem(template);
				item.setCount(1);
				item.setEnchantLevel(enchantLevel);
				item.setAttrEnchantLevel(attr);
				item.setIdentified(identi);
				item.setBless(bless);
				item.setCount(1);
				if (limitTime > 0) {
					limitTimeItem(item, limitTime);
				}
				if (inv.checkAddItem(item, 1) == L1Inventory.OK) {
					inv.storeItem(item);
					_pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
				} else {
					L1World.getInstance().getInventory(_pc.getX(), _pc.getY(), _pc.getMapId()).storeItem(item);
					_pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				}
			}
		}
	}
	
	void limitTimeItem(L1ItemInstance item, int limitTime){
		Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + (60000L * (long)limitTime));
		item.setEndTime(deleteTime);
		item.setIdentified(true);
		item.setEngrave(true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ItemSelect(data, client);
	}

}


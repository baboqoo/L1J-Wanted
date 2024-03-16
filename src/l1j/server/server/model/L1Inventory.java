package l1j.server.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.Config;
import l1j.server.GameSystem.inn.InnHandler;
import l1j.server.GameSystem.inn.InnTimer;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemNormalType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.warehouse.Warehouse;
import l1j.server.server.serverpackets.equip.S_EquipmentWindow;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;
//import manager.ManagerInfoThread;  // MANAGER DISABLED

public class L1Inventory extends L1Object {
    public static final int MAX_SIZE = 200;	
	private static final long serialVersionUID		= 1L;
	public static final int MAX_AMOUNT				= 2000000000;
	public static final int MAX_WEIGHT				= 1500;
	
	public static final int OK						= 0;
	public static final int SIZE_OVER				= 1;
	public static final int WEIGHT_OVER				= 2;
	public static final int AMOUNT_OVER				= 3;
	
	public static final int WAREHOUSE_TYPE_PERSONAL	= 0;
	public static final int WAREHOUSE_TYPE_PLEDGE	= 1;
	
	protected List<L1ItemInstance> _items			= new CopyOnWriteArrayList<L1ItemInstance>();

	private int[] slot_ring		= new int[6];
	private int[] slot_rune		= new int[3];
	private int[] slot_earring	= new int[4];
    
	public L1Inventory() {
		for (int i = 0; i < slot_ring.length; ++i) {
			slot_ring[i] = 0;
		}
		for (int i = 0; i < slot_rune.length; ++i) {
			slot_rune[i] = 0;
		}
		for (int i = 0; i < slot_earring.length; i++) {
			slot_earring[i] = 0;
		}
	}

	public int getTypeAndItemIdEquipped(L1ItemType itemType, int type, int itemId) { 
		int equipeCount = 0;
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemType() == itemType	// 0 etc, 1 무기, 2 방어구
					&& item.getItem().getType() == type		// type2 의 세부항목들
					&& item.getItem().getItemId() == itemId	// 아이템의 아이디
					&& item.isEquipped()) {// 착용
				equipeCount++;
			}
		}
		return equipeCount;
	}
	
	public int getTypeAndGradeEquipped(L1ItemType itemType, int type, int grade) { 
		int equipeCount = 0;
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemType() == itemType// 0 etc, 1 무기, 2 방어구
					&& item.getItem().getType() == type	// type2 의 세부항목들
					&& item.getItem().getGrade() == grade && item.isEquipped()) {// 착용
				equipeCount++;
			}
		}
		return equipeCount;
	}
	
	private L1ItemInstance equppedWeapon, equippedHelmet, equippedArmor, equippedTshirt, equippedCloak, equippedGlove, equippedBoots, 
				equippedShield, equippedAmulet, equippedBelt, equippedGarder, equippedPair, equippedSentence, equippedShoulder, 
				equippedBadge, equippedPendant;
	
	public L1ItemInstance getEquippedWeapon(){
		return equppedWeapon;
	}
	public L1ItemInstance getEquippedHelmet(){
		return equippedHelmet;
	}
	public L1ItemInstance getEquippedArmor(){
		return equippedArmor;
	}
	public L1ItemInstance getEquippedTshirt(){
		return equippedTshirt;
	}
	public L1ItemInstance getEquippedCloak(){
		return equippedCloak;
	}
	public L1ItemInstance getEquippedGlove(){
		return equippedGlove;
	}
	public L1ItemInstance getEquippedBoots(){
		return equippedBoots;
	}
	public L1ItemInstance getEquippedShield(){
		return equippedShield;
	}
	public L1ItemInstance getEquippedAmulet(){
		return equippedAmulet;
	}
	public L1ItemInstance getEquippedBelt(){
		return equippedBelt;
	}
	public L1ItemInstance getEquippedGarder(){
		return equippedGarder;
	}
	public L1ItemInstance getEquippedPair(){
		return equippedPair;
	}
	public L1ItemInstance getEquippedSentence(){
		return equippedSentence;
	}
	public L1ItemInstance getEquippedShoulder(){
		return equippedShoulder;
	}
	public L1ItemInstance getEquippedBadge(){
		return equippedBadge;
	}
	public L1ItemInstance getEquippedPendant(){
		return equippedPendant;
	}
	
	public void toSlotPacket(L1PcInstance pc, L1ItemInstance item, boolean worldjoin) {
		if (pc.isWorld == false) {
			return;
		}
		int select_idx	= -1;
		int idx			= 0;
		boolean equip	= item.isEquipped();
		if (item.getItem().getItemType() == L1ItemType.ARMOR) {
			switch(L1ItemArmorType.fromInt(item.getItem().getType())){
			case HELMET:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_HEML;
				equippedHelmet = equip ? item : null;
				break;
			case ARMOR:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_ARMOR;
				equippedArmor = equip ? item : null;
				break;
			case T_SHIRT:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_T;
				equippedTshirt = equip ? item : null;
				break;
			case CLOAK:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_CLOAK;
				equippedCloak = equip ? item : null;
				break;
			case GLOVE:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_GLOVE;
				equippedGlove = equip ? item : null;
				break;
			case BOOTS:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_BOOTS;
				equippedBoots = equip ? item : null;
				break;
			case SHIELD:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_SHIELD;
				equippedShield = equip ? item : null;
				break;
			case AMULET:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_NECKLACE;
				equippedAmulet = equip ? item : null;
				break;
			case RING:
			case RING_2:
				// 기존에 착용중인게 잇는지 검색.
				for (int i = 0; i < slot_ring.length; ++i) {
					if (slot_ring[i] == item.getId()) {
						select_idx = i;
					}
				}
				// 착용해야할경우 기존에 착용중인게 없을때만 메모리 갱신.
				if (equip && select_idx == -1) {
					// 착용중이라면 빈 슬롯에 넣기.
					for (int i = 0; i < slot_ring.length; ++i) {
						if (slot_ring[i] == 0) {
							slot_ring[i] = item.getId();
							idx = S_EquipmentWindow.EQUIPMENT_INDEX_RING_L_1 + i;
							break;
						}
					}
				}
				// 착용해제해야할 경우 기존에 착용중인게 잇을때만 메모리 갱신.
				if (!equip && select_idx != -1) {
					// 해제중이라면 이전에 적용되잇던 위치에 값을 제거.
					slot_ring[select_idx] = 0;
					idx = S_EquipmentWindow.EQUIPMENT_INDEX_RING_L_1 + select_idx;
				}
				break;
			case BELT:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_BELT;
				equippedBelt = equip ? item : null;
				break;
			case EARRING:
				// 기존에 착용중인게 잇는지 검색.
				for (int i = 0; i < this.slot_earring.length; i++) {
					if (this.slot_earring[i] == item.getId()) {
						select_idx = i;
					}
				}
				// 착용해야할경우 기존에 착용중인게 없을때만 메모리 갱신.
				if (equip && select_idx == -1) {
					// 착용중이라면 빈 슬롯에 넣기.
					for (int i = 0; i < this.slot_earring.length; i++) {
						if (this.slot_earring[i] == 0) {
							this.slot_earring[i] = item.getId();
							idx = i == 0 ? S_EquipmentWindow.EQUIPMENT_INDEX_EARRING_L_1 
									: i == 1 ? S_EquipmentWindow.EQUIPMENT_INDEX_EARRING_R_1
									: i == 2 ? S_EquipmentWindow.EQUIPMENT_INDEX_EARRING_L_2
									: S_EquipmentWindow.EQUIPMENT_INDEX_EARRING_R_2;
							break;
						}
					}
				}
				// 착용해제해야할 경우 기존에 착용중인게 잇을때만 메모리 갱신.
				if (!equip && select_idx != -1) {
					// 해제중이라면 이전에 적용되잇던 위치에 값을 제거.
					this.slot_earring[select_idx] = 0;
					idx = select_idx == 0 ? S_EquipmentWindow.EQUIPMENT_INDEX_EARRING_L_1 
							: select_idx == 1 ? S_EquipmentWindow.EQUIPMENT_INDEX_EARRING_R_1
							: select_idx == 2 ? S_EquipmentWindow.EQUIPMENT_INDEX_EARRING_L_2
							: S_EquipmentWindow.EQUIPMENT_INDEX_EARRING_R_2;
				}
				break;
			case GARDER:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_SHIELD;
				equippedGarder = equip ? item : null;
				break;
			case RON:
				// 기존에 착용중인게 잇는지 검색.
				for (int i = 0; i < slot_rune.length; ++i) {
					if (slot_rune[i] == item.getId()) {
						select_idx = i;
					}
				}
				// 착용해야할경우 기존에 착용중인게 없을때만 메모리 갱신.
				if (equip && select_idx == -1) {
					// 착용중이라면 빈 슬롯에 넣기.
					for (int i = 0; i < slot_rune.length; ++i) {
						if (slot_rune[i] == 0) {
							slot_rune[i] = item.getId();
							idx = S_EquipmentWindow.EQUIPMENT_INDEX_RUNE1 + i;
							break;
						}
					}
				}
				// 착용해제해야할 경우 기존에 착용중인게 잇을땜나 메모리 갱신.
				if (!equip && select_idx != -1) {
					// 해제중이라면 이전에 적용되잇던 위치에 값을 제거.
					slot_rune[select_idx] = 0;
					idx = S_EquipmentWindow.EQUIPMENT_INDEX_RUNE1 + select_idx;
				}
				break;
			case PAIR:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_PAIR;
				equippedPair = equip ? item : null;
				break;
			case SENTENCE:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_SENTENCE;
				equippedSentence = equip ? item : null;
				break;
			case SHOULDER:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_SHOULDER;
				equippedShoulder = equip ? item : null;
				break;
			case BADGE:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_BADGE;
				equippedBadge = equip ? item : null;
				break;
			case PENDANT:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_PENDANT;
				equippedPendant = equip ? item : null;
				break;
			default:break;
			}
		} else {
			idx = S_EquipmentWindow.EQUIPMENT_INDEX_WEAPON;
			equppedWeapon = equip ? item : null;
		}
		if (idx != 0) {
			pc.sendPackets(new S_EquipmentWindow(item.getId(), idx, equip), true);
		}
	}

	public int getSize() {
		return _items.size();
	}

	public List<L1ItemInstance> getItems() {
		return _items;
	}

	public int getWeight() {
		int weight = 0;
		for (L1ItemInstance item : _items) {
			weight += item.getWeight();
		}
		weight /= Config.RATE.RATE_WEIGHT_LIMIT;
		return weight;
	}

	public int checkAddItem(L1ItemInstance item, int count) {
		if (item == null) {
			return -1;
		}
		if (count < 0 || count > MAX_AMOUNT) {
			return AMOUNT_OVER;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return -1;
		}
		if (getSize() > Config.ALT.MAX_NPC_ITEM || (getSize() == Config.ALT.MAX_NPC_ITEM && (!item.isMerge() || !checkItem(item.getItem().getItemId())))) {
			return SIZE_OVER;
		}

		int weight = getWeight() + item.getItem().getWeight() * count / 1000 + 1;
		if (weight < 0 || (item.getItem().getWeight() * count / 1000) < 0) {
			return WEIGHT_OVER;
		}
		if (weight > (MAX_WEIGHT * Config.RATE.RATE_WEIGHT_LIMIT_PET)) {
			return WEIGHT_OVER;
		}

		L1ItemInstance itemExist = findItemId(item.getItemId());
		if (itemExist != null && (itemExist.getCount() + count) > MAX_AMOUNT) {
			return AMOUNT_OVER;
		}
		return OK;
	}

	public int checkAddItemToWarehouse(L1ItemInstance item, int count, int type) {
		if (item == null) {
			return -1;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return -1;
		}
		
		int maxSize = 100;
		if (type == WAREHOUSE_TYPE_PERSONAL) {
			maxSize = Config.ALT.MAX_PERSONAL_WAREHOUSE_ITEM;
		} else if (type == WAREHOUSE_TYPE_PLEDGE) {
			maxSize = Config.ALT.MAX_CLAN_WAREHOUSE_ITEM;
		}
		
		if (getSize() > maxSize || (getSize() == maxSize && (!item.isMerge() || !checkItem(item.getItem().getItemId())))) {
			return SIZE_OVER;
		}
		return OK;
	}
	
	private int getChequeCount(long value){// 19억 초과시 변환할 수표 개수 취득
		return value > 1900000000L ? (int)((value - 1900000000L) / 100000000L) : 0;
	}
	
	public synchronized L1ItemInstance storeItem(int itemId, int count) {
		return storeItem(itemId, count, 0);
	}
	
	public synchronized L1ItemInstance storeItem(int itemId, int count, int enchant) {
		if (count <= 0) {
			return null;
		}
		ItemTable it = ItemTable.getInstance();
		L1Item temp = it.getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		if (temp.isMerge()) {
			L1ItemInstance item = it.FunctionItem(temp);
			item.setCount(count);
			if (findItemId(itemId) == null) {
				if (getAvailable() == 0)
				  return null;
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			if (getAvailable() >= 1) {
				item = it.FunctionItem(temp);
				item.setId(IdFactory.getInstance().nextId());
				item.setEnchantLevel(enchant);
				L1World.getInstance().storeObject(item);
				storeItem(item);
				result = item;
				if (this instanceof L1PcInventory && item.getEndTime() != null) {
					updateItem(item, L1PcInventory.COL_REMAINING_TIME);
					((L1PcInventory) this).saveItem(item, L1PcInventory.COL_REMAINING_TIME);
				}
			}
		}
		return result;
	}
	
	// 새로운 아이템의 격납
	public L1ItemInstance storeItem(int itemId, int count, String name) {
		ItemTable it	= ItemTable.getInstance();
		L1Item sTemp	= it.getTemplate(itemId);
		L1Item temp		= it.clone(sTemp, name);
		if (temp == null) {
			return null;
		}
		if (temp.isMerge()) {
			L1ItemInstance item = it.FunctionItem(temp);
			item.setCount(count);
			if (!temp.isMerge() || findItemId(itemId) == null) {
				// 새롭게 생성할 필요가있는 경우만 ID의발행과 L1World에의등록을 실시한다
				if (getAvailable() == 0) return null;
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		// 스택 할 수 없는 아이템의 경우
		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			if (getAvailable() >= 1) {
				item = it.FunctionItem(temp);
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
				storeItem(item);
				result = item;
				if (this instanceof L1PcInventory && item.getEndTime() != null) {
					updateItem(item, L1PcInventory.COL_REMAINING_TIME);
					((L1PcInventory) this).saveItem(item, L1PcInventory.COL_REMAINING_TIME);
				}
			}
		}
		// 마지막에 만든 아이템을 돌려준다. 배열을 되돌리도록(듯이) 메소드 정의를 변경하는 편이 좋을지도 모른다.
		return result;
	}
	
	public synchronized L1ItemInstance storeItem(L1PcInstance pc, L1Item temp, int count, int enchant) {
		if (count <= 0 || temp == null) {
			return null;
		}
		ItemTable it = ItemTable.getInstance();
		if (temp.isMerge()) {
			L1ItemInstance item = it.FunctionItem(temp);
			item.setCount(count);
			if (findItemId(temp.getItemId()) == null) {
				if (getAvailable() == 0) return null;
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}
		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			if (getAvailable() >= 1) {
				item = it.FunctionItem(temp);
				item.setId(IdFactory.getInstance().nextId());
				item.setEnchantLevel(enchant);
				item.setIdentified(true);
				L1World.getInstance().storeObject(item);
				storeItem(item);
				result = item;
				if (item.getEndTime() != null) {
					updateItem(item, L1PcInventory.COL_REMAINING_TIME);
					pc.getInventory().saveItem(item, L1PcInventory.COL_REMAINING_TIME);
				}
			}
		}
		return result;
	}
	
	public synchronized L1ItemInstance storeItem(L1PcInstance pc, int itemId, int count, int enchant, int bless, int attr) {
		return storeItem(pc, itemId, count, enchant, bless, attr, false);
	}

	public synchronized L1ItemInstance storeItem(L1PcInstance pc, int itemId, int count, int enchant, int bless, int attr, boolean iden) {
		if (count <= 0) {
			return null;
		}
		ItemTable it = ItemTable.getInstance();
		L1Item temp = it.getTemplate(itemId, bless);
		if (temp == null) {
			temp = it.getTemplate(itemId);
		}
		if (temp == null) {
			return null;
		}
		if (temp.isMerge()) {
			L1ItemInstance item = it.FunctionItem(temp);
			item.setCount(count);
			item.setBless(bless);
			item.setIdentified(iden);
			if (findItemId(itemId) == null) {
				if (getAvailable() == 0) return null;
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			if (getAvailable() >= 1) {
				item = it.FunctionItem(temp);
				item.setId(IdFactory.getInstance().nextId());
				item.setEnchantLevel(enchant);
				item.setAttrEnchantLevel(attr);
				item.setBless(bless);
				item.setIdentified(iden);
				L1World.getInstance().storeObject(item);
				storeItem(item);
				result = item;
				if (item.getEndTime() != null) {
					updateItem(item, L1PcInventory.COL_REMAINING_TIME);
					pc.getInventory().saveItem(item, L1PcInventory.COL_REMAINING_TIME);
				}
			}
		}
		return result;
	}

	public synchronized L1ItemInstance storeItem(L1ItemInstance item) {
		if (item.getCount() <= 0) {
			return null;
		}
		updteItemObtained(item, item.getCount(), true);
		int itemId = item.getItem().getItemId();
		if (item.isMerge()) {
			L1ItemInstance findItem = itemId == L1ItemId.INN_ROOM_KEY || itemId == L1ItemId.INN_HALL_KEY ? findItemKey(item.getId()) : findItemId(itemId);
			if (findItem != null) {
				long updateCount = (long)findItem.getCount() + (long)item.getCount();
				if (itemId == L1ItemId.ADENA) {
					int chequeCount = getChequeCount(updateCount);
					if (chequeCount > 0) {
						updateCount -= chequeCount * 100000000;
						storeItem(L1ItemId.CHEQUE, chequeCount);// 수표생성
					}
				}
				findItem.setCount(IntRange.ensure((int)updateCount, 0, MAX_AMOUNT));
				updateItem(findItem);
				return findItem;
			}
		}
		
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		
		int chargeCount = item.getItem().getMaxChargeCount();
		switch (itemId) {
		case 41401:
		case 810006:
		case 810011:
		case 30055: // 폭풍의막대
			L1ItemInstance findItem = findItemId(itemId);
			if (findItem != null) {
				chargeCount -= CommonUtil.random(5);
				findItem.setChargeCount(findItem.getChargeCount() + chargeCount);
				updateItem(findItem);
				return findItem;
			}
			break;
		}

		if (itemId == 20383) {
			chargeCount = 50;
		}
		item.setChargeCount(chargeCount);

		// 시간제 아이템
		switch (itemId) {
		case 410555:case 6014:
			SetDeleteTime(item, CommonUtil.getNextTime(6));// 오전 6시
			break;
		case 6015:case 6020:case 6022:case 420121:
			SetDeleteTime(item, CommonUtil.getNextDayTime(Calendar.TUESDAY, 6));// 수요일 오전 6시
			break;
		default:
			int term = ItemTable.getTerm(itemId);
			if (term > 0) {
				SetDeleteTime(item, term);
			} else if (itemId == Config.PSS.PLAY_SUPPORT_AUTH_ITEM_ID && Config.PSS.PLAY_SUPPORT_AUTH_ITEM_DURATION_MINUT > 0) {
				SetDeleteTime(item, Config.PSS.PLAY_SUPPORT_AUTH_ITEM_DURATION_MINUT);
			}
			break;
		}
		
		item.setRemainingTime(item.getItem().getItemType() == L1ItemType.NORMAL && item.getItem().getType() == L1ItemNormalType.LIGHT.getId() ? item.getItem().getLightFuel() : item.getItem().getMaxUseTime());
		
		if ((itemId == 700024 || itemId == 700025) && StringUtil.isNullOrEmpty(item.getCreaterName())) {
			item.setCreaterName("$13719");
		}
		
		insertItem(item);
		return item;
	}
	
	public synchronized L1ItemInstance storeNpcShopItem(L1ItemInstance item) {
		if (item.getCount() <= 0) {
			return null;
		}
		updteItemObtained(item, item.getCount(), true);
		int itemId = item.getItem().getItemId();
		if (item.isMerge()) {
			L1ItemInstance findItem = itemId == L1ItemId.INN_ROOM_KEY || itemId == L1ItemId.INN_HALL_KEY ? findItemKey(item.getId()) : findItemId(itemId);
			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		int chargeCount = item.getItem().getMaxChargeCount();
		item.setChargeCount(chargeCount);
		if (item.getItem().getItemType() == L1ItemType.NORMAL && item.getItem().getType() == L1ItemNormalType.LIGHT.getId()) {// light
			item.setRemainingTime(item.getItem().getLightFuel());
		} else {
			item.setRemainingTime(item.getItem().getMaxUseTime());
		}
		item.setBless(item.getItem().getBless());

		int term = ItemTable.getTerm(itemId);
		if (term > 0) {
			SetDeleteTime(item, term);
		}
		insertItem(item);
		return item;
	}
	
	public synchronized L1ItemInstance storeTradeItem(L1ItemInstance item) {
		updteItemObtained(item, item.getCount(), true);
		int itemId = item.getItem().getItemId();
		if (item.isMerge()) {
			L1ItemInstance findItem = itemId == L1ItemId.INN_ROOM_KEY || itemId == L1ItemId.INN_HALL_KEY ? findItemKey(item.getId()) : findItemId(itemId);
			if (findItem != null) {
				long updateCount = (long)findItem.getCount() + (long)item.getCount();
				if (itemId == L1ItemId.ADENA) {
					int chequeCount = getChequeCount(updateCount);
					if (chequeCount > 0) {
						updateCount -= chequeCount * 100000000;
						storeItem(L1ItemId.CHEQUE, chequeCount);// 수표생성
					}
				}
				findItem.setCount(IntRange.ensure((int)updateCount, 0, MAX_AMOUNT));
				updateItem(findItem);
				return findItem;
			}
		}
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		insertItem(item);
		return item;
	}

	public void SetDeleteTime(L1ItemInstance item, int minute) {
		SetDeleteTime(item, System.currentTimeMillis() + (60000L * (long)minute));
	}
	
	private void SetDeleteTime(L1ItemInstance item, long delTime){// 삭제시간 지정
		item.setEndTime(new Timestamp(delTime));
		item.setIdentified(true);
	}
	
	public boolean checkMaterialList(int itemId, int enchant, int count) {
		int num = 0;
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == itemId && item.getEnchantLevel() == enchant) {
				num += item.getCount();
				if (num >= count) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean consumeMaterialList(int itemid, int count, int enchant) {
		if (count <= 0) {
			return false;
		}
		if (ItemTable.getInstance().getTemplate(itemid).isMerge()) {
			L1ItemInstance item = findItemId(itemid);
			if (item != null && item.getCount() >= count && item.getEnchantLevel() == enchant) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId(itemid);
			if (itemList.length == count) {
				int j = 0;
				for (int i = 0; i < count; ++i) {
					if (itemList[i].getEnchantLevel() == enchant) {
						removeItem(itemList[i], 1);
						 if (++j == count) {
							 break;
						 }
					}
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				int j = 0;
				for (int i = 0; i < itemList.length; ++i ) {
					if (itemList[i].getEnchantLevel() == enchant) {
						removeItem(itemList[i], 1);
						 if (++j == count) {
							 break;
						 }
					}
				}
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void extracted(L1ItemInstance[] itemList, DataComparator dc) {
		Arrays.sort(itemList, dc);
	}

	@SuppressWarnings("rawtypes")
	public class DataComparator implements java.util.Comparator {
		public int compare(Object item1, Object item2) {
			return ((L1ItemInstance) item1).getEnchantLevel() - ((L1ItemInstance) item2).getEnchantLevel();
		}
	}
	
	public int removeItem(int objectId) {
		L1ItemInstance item = getItem(objectId);
		return removeItem(item, item.getCount());
	}


	public int removeItem(int objectId, int count) {
		L1ItemInstance item = getItem(objectId);
		return removeItem(item, count);
	}

	public int removeItem(L1ItemInstance item) {
		return removeItem(item, item.getCount());
	}

	public int removeItem(L1ItemInstance item, int count) {
		if (item == null || item.getCount() <= 0 || count <= 0) {
			return 0;
		}
		if (item.getCount() < count) {
			count = item.getCount();
		}
		int itemId = item.getItem().getItemId();
		if (itemId == L1ItemId.INN_ROOM_KEY || itemId == L1ItemId.INN_HALL_KEY) {
			InnTimer innTimer = InnHandler.getInstance().getInnTimer(item.getKey());
			if (innTimer != null) {
				innTimer.decreaseKey(count);
			}
		}
		updteItemObtained(item, count, false);
		if (item.getCount() == count) {
			if (itemId == L1ItemId.PET_AMULET) {
				CharacterCompanionTable.getInstance().deletePet(item.getId());
			} else if (itemId >= 49016 && itemId <= 49025) {
				LetterTable lettertable = new LetterTable();
				lettertable.deleteLetter(item.getId());
			} else if (itemId >= 41383 && itemId <= 41400) {
				L1FurnitureInstance furniture = null;
				for (L1Object l1object : L1World.getInstance().getObject()) {
					if (l1object == null) {
						continue;
					}
					if (l1object instanceof L1FurnitureInstance) {
						furniture = (L1FurnitureInstance) l1object;
						if (furniture.getItemObjId() == item.getId()) {
							FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
						}
					}
				}
			}
			deleteItem(item);
			L1World.getInstance().removeObject(item);
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
		}
		return count;
	}

	public void deleteItem(L1ItemInstance item) {
		_items.remove(item);
	}

	public synchronized L1ItemInstance tradeItem(int objectId, int count, Warehouse inventory) {
		L1ItemInstance item = getItem(objectId);
		return tradeItem(item, count, inventory);
	}

	public synchronized L1ItemInstance tradeItem(int objectId, int count, L1Inventory inventory) {
		L1ItemInstance item = getItem(objectId);
		return tradeItem(item, count, inventory);
	}

	public synchronized L1ItemInstance tradeItem(L1ItemInstance item, int count, Warehouse inventory) {
		if (item == null || item.getCount() <= 0 || count <= 0 || item.isEquipped()) {
			return null;
		}
		if (!checkItem(item.getItem().getItemId(), count) || item.getCount() < count) {
			return null;
		}
		L1ItemInstance carryItem;
		// 엔진관련 버그 방지 추가
		updteItemObtained(item, count, false);
		if (item.getCount() <= count || count < 0) {
			deleteItem(item);
			carryItem = item;
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
			carryItem = ItemTable.getInstance().createItem(item.getItem().getItemId());
			carryItem.setCount(count);
			carryItem.setEnchantLevel(item.getEnchantLevel());
			carryItem.setIdentified(item.isIdentified());
			carryItem.setDurability(item.getDurability());
			carryItem.setChargeCount(item.getChargeCount());
			carryItem.setRemainingTime(item.getRemainingTime());
			carryItem.setLastUsed(item.getLastUsed());
			carryItem.setBless(item.getItem().getBless());
			carryItem.setAttrEnchantLevel(item.getAttrEnchantLevel());
			carryItem.setKey(item.getKey());
			carryItem.setEndTime(item.getEndTime());
		}
		return inventory.storeTradeItem(carryItem);
	}

	public synchronized L1ItemInstance tradeItem(L1ItemInstance item, int count, L1Inventory inventory) {
		if (item == null || item.getCount() <= 0 || count <= 0 || item.isEquipped()) {
			return null;
		}
		if (!checkItem(item.getItem().getItemId(), count) || item.getCount() < count) {
			return null;
		}
		L1ItemInstance carryItem;
		// 엔진관련 버그 방지 추가
		updteItemObtained(item, count, false);
		if (item.getCount() <= count || count < 0) {
			deleteItem(item);
			carryItem = item;
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
			carryItem = ItemTable.getInstance().createItem(item.getItem().getItemId());
			carryItem.setCount(count);
			carryItem.setEnchantLevel(item.getEnchantLevel());
			carryItem.setIdentified(item.isIdentified());
			carryItem.setDurability(item.getDurability());
			carryItem.setChargeCount(item.getChargeCount());
			carryItem.setRemainingTime(item.getRemainingTime());
			carryItem.setLastUsed(item.getLastUsed());
			carryItem.setBless(item.getItem().getBless());
			carryItem.setAttrEnchantLevel(item.getAttrEnchantLevel());
			carryItem.setSpecialEnchant(item.getSpecialEnchant());
			carryItem.setKey(item.getKey());
			carryItem.setEndTime(item.getEndTime());
		}
		return inventory.storeTradeItem(carryItem);
	}

	public L1ItemInstance receiveDamage(int objectId) {
		L1ItemInstance item = getItem(objectId);
		return receiveDamage(item);
	}

	public L1ItemInstance receiveDamage(L1ItemInstance item) {
		return receiveDamage(item, 1);
	}

	public L1ItemInstance receiveDamage(L1ItemInstance item, int count) {
		L1ItemType itemType = item.getItem().getItemType();
		int currentDurability = item.getDurability();

		if ((currentDurability == 0 && itemType == L1ItemType.NORMAL) || currentDurability < 0) {
			item.setDurability(0);
			return null;
		}
		if (itemType == L1ItemType.NORMAL) {
			int minDurability = (item.getEnchantLevel() + 5) * -1;
			int durability = currentDurability - count;
			if (durability < minDurability) {
				durability = minDurability;
			}
			if (currentDurability > durability) {
				item.setDurability(durability);
			}
		} else if (itemType == L1ItemType.ARMOR) {
			int maxDurability = item.getItem().getAc() * -1; 
			int durability = currentDurability + count;
			if (durability > maxDurability) {
				durability = maxDurability;
			}
			if (currentDurability < durability) {
				item.setDurability(durability);
			}
		} else {
			int maxDurability = item.getEnchantLevel() + 5;
			int durability = currentDurability + count;
			if (durability > maxDurability) {
				durability = maxDurability;
			}
			if (currentDurability < durability) {
				item.setDurability(durability);
			}
		}
		updateItem(item, L1PcInventory.COL_DURABILITY);
		return item;
	}

	public L1ItemInstance recoveryDamage(L1ItemInstance item) {
		L1ItemType itemType = item.getItem().getItemType();
		int durability = item.getDurability();
		if ((durability == 0 && itemType != L1ItemType.NORMAL) || durability < 0) {
			item.setDurability(0);
			return null;
		}
		item.setDurability(durability + (itemType == L1ItemType.NORMAL ? 1 : -1));
		updateItem(item, L1PcInventory.COL_DURABILITY);
		return item;
	}
	
	public L1ItemInstance findEquippedItemId(int itemId) {
		for (L1ItemInstance item : _items) {
			if (item == null) {
				continue;
			}
			if (item.getItem().getItemId() == itemId && item.isEquipped()) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance findItemId(int itemId) {
		for (L1ItemInstance item : _items) {
			if (item == null) {
				continue;
			}
			if (item.getItem().getItemId() == itemId) {
				return item;
			}
		}
		return null;
	}
	
	public L1ItemInstance findItemNameId(int name_id) {
		for (L1ItemInstance item : _items) {
			if (item == null) {
				continue;
			}
			if (item.getItem().getItemNameId() == name_id) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance[] findItemsId(int itemId) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null) {
				continue;
			}
			if (item.getItem().getItemId() == itemId) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}
	
	public L1ItemInstance[] findItemsIds(int itemId, int bless, int enchant) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null) {
				continue;
			}
			if (item.getItemId() == itemId && item.getBless() == bless && item.getEnchantLevel() == enchant) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}
	
	public int findItemIdCount(int itemId) {
		int cnt = 0;
		for (L1ItemInstance item : _items) {
			if (item == null) {
				continue;
			}
			if (item.getItemId() == itemId) {
				cnt += item.getCount();
			}
		}
		return cnt;
	}

	public L1ItemInstance[] findItemsIdNotEquipped(int itemId) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null) {
				continue;
			}
			if (item.getItemId() == itemId && !item.isEquipped()) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}
	
	public L1ItemInstance findItemMaterial(int nameId, int count, int bless, int enchant) {
		for (L1ItemInstance item : _items) {
			if (item == null || item.isEquipped() || item.getItem().getItemNameId() != nameId || item.getCount() < count || item.getEnchantLevel() != enchant) {
				continue;
			}
			if (bless != 3 && item.getBless() != bless) {
				continue;
			}
			return item;
		}
		return null;
	}
	
	public L1ItemInstance findItem(int nameId, int count) {
		for (L1ItemInstance item : _items) {
			if (item == null || item.isEquipped() || item.getItem().getItemNameId() != nameId || item.getCount() < count) {
				continue;
			}
			return item;
		}
		return null;
	}
	
	public L1ItemInstance findItemKey(int objId) {
		return getItem(objId);
	}

	public L1ItemInstance getItem(int objectId) {
		for (L1ItemInstance item : _items) {
			if (item == null) {
				continue;
			}
			if (item.getId() == objectId) {
				return item;
			}
		}
		return null;
	}

	public boolean checkItem(int itemId) {
		return checkItem(itemId, 1);
	}

	public boolean checkItem(int itemId, int count) {
		if (count < 0) {
			return false;
		}
		if (count == 0) {
			return true;
		}
		if (ItemTable.getInstance().getTemplate(itemId).isMerge()) {
			L1ItemInstance item = findItemId(itemId);
			if (item != null && item.getCount() >= count) {
				return true;
			}
		} else {
			L1ItemInstance[] array = findItemsId(itemId);
			if (array.length >= count) {
				return true;
			}
		}
		return false;
	}

	// 인첸된 체크 아이템 재코딩
    public boolean checkEnchantItem(int itemId, int enchant, int count) {
		int num = 0;
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == itemId && item.getEnchantLevel() == enchant) {
				num++;
				if (num == count) {
					return true;
				}
			}
		}
		return false;
	}

    // 속성인첸도 체크한다
    /**  (화령)1/2/3/33/34 (수령)4/5/6/35/36 (풍령)7/8/9/37/38 (지령)10/11/12/39/40 **/
	public boolean checkAttrEnchantItem(int itemId, int enchant, int count, int attr) {
		int num = 0;
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == itemId && item.getEnchantLevel() == enchant && item.getAttrEnchantLevel() == attr) {
				num++;
				if (num == count) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkItemNotEquipped(int itemId, int count) {
		if (count == 0) {
			return true;
		}
		return count <= countItems(itemId);
	}

	public boolean checkItem(int[] itemIds) {
		int len = itemIds.length;
		int[] counts = new int[len];
		for (int i = 0; i < len; i++) {
			counts[i] = 1;
		}
		return checkItem(itemIds, counts);
	}
	
	public boolean checkItemOne(int[] itemIds){
		for (int id : itemIds) {
			if (checkItem(id, 1)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkItem(int[] itemIds, int[] counts) {
		for (int i = 0; i < itemIds.length; i++) {
			if (!checkItem(itemIds[i], counts[i])) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkItemInteractionType(int val) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().get_interaction_type() == val) {
				return true;
			}
		}
		return false;
	}

	public int countItems(int itemId) {
		if (ItemTable.getInstance().getTemplate(itemId).isMerge()) {
			L1ItemInstance item = findItemId(itemId);
			if (item != null) {
				return item.getCount();
			}
		} else {
			Object[] itemList = findItemsIdNotEquipped(itemId);
			return itemList.length;
		}
		return 0;
	}
	
	public boolean consumeItem(int itemid) {
		L1ItemInstance item = findItemId(itemid);
		if (item != null) {
			removeItem(item, item.getCount());
			return true;
		} 
		return false;
	}
	
	public boolean consumeItemNameId(int nameId) {
		L1ItemInstance item = findItemNameId(nameId);
		if (item != null) {
			removeItem(item, item.getCount());
			return true;
		} 
		return false;
	}
	
	public boolean consumeItemNameId(int itemNameId, int count) {
		if (count <= 0) {
			return false;
		}
		L1Item temp = ItemTable.getInstance().findItemByNameId(itemNameId);
		if (temp == null) {
			return false;
		}
		if (temp.isMerge()) {
			L1ItemInstance item = findItemId(temp.getItemId());
			if (item != null && item.getCount() >= count) {
				/*if (temp.getItemId() == L1ItemId.ADENA) {
					ManagerInfoThread.AdenConsume = Long.valueOf(ManagerInfoThread.AdenConsume.longValue() + count);
				}*/  // MANAGER DISABLED
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId(temp.getItemId());
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean consumeItem(int itemid, int count) {
		if (count <= 0) {
			return false;
		}
		if (ItemTable.getInstance().getTemplate(itemid).isMerge()) {
			L1ItemInstance item = findItemId(itemid);
			if (item != null && item.getCount() >= count) {
				/*if (itemid == L1ItemId.ADENA) {
					ManagerInfoThread.AdenConsume = Long.valueOf(ManagerInfoThread.AdenConsume.longValue() + count);  // MANAGER DISABLED
				}*/   // MANAGER DISABLED
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId(itemid);
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean consumeItem(L1ItemInstance item, int count) {//1
		if (count <= 0) {
			return false;
		}
		if (item.isMerge()) {
			if (item != null && item.getCount() >= count) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsIds(item.getItem().getItemId(), item.getBless(), item.getEnchantLevel());
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean consumeEnchantItem(int itemId, int enchant, int count) {
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == itemId && item.getEnchantLevel() == enchant && item.getCount() == count) {
				removeItem(item);
				return true;
			}
		}
		return false;
	}
	
	public boolean consumeAttrItem(int itemId, int enchant, int count, int attr) {
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == itemId && item.getEnchantLevel() == enchant && item.getAttrEnchantLevel() == attr) {
				removeItem(item);
				return true;
			}
		}
		return false;
	}

	public void shuffle() {
		Collections.shuffle(_items);
	}

	public void clearItems() {
		for (L1ItemInstance item : _items) {
			if (item == null) {
				continue;
			}
			L1World.getInstance().removeObject(item);
		}
		_items.clear();
	}

	public void loadItems() {}
	public void insertItem(L1ItemInstance item) {
		_items.add(item);
	}
	public void updateItem(L1ItemInstance item) {}
	public void updateItem(L1ItemInstance item, int colmn) {}
	public void updteItemObtained(L1ItemInstance item, int count, boolean store){}


	public int getAvailable() {
		return MAX_SIZE -  getSize();
	}

	public boolean full() {
		return (MAX_SIZE -  getSize()) == 0;
	}


}


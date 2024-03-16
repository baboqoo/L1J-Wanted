package l1j.server.server.model.item;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import l1j.server.server.construct.L1BeginnerQuest;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemMentTable;
import l1j.server.server.datatables.ItemMentTable.ItemMentType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.serverpackets.message.S_MessegeNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1TreasureBox {
	private static Logger _log = Logger.getLogger(L1TreasureBox.class.getName());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "TreasureBoxList")
	private static class TreasureBoxList implements Iterable<L1TreasureBox> {
		@XmlElement(name = "TreasureBox")
		private List<L1TreasureBox> _list;

		public Iterator<L1TreasureBox> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Item {
		@XmlAttribute(name = "ItemId")
		private int _itemId;

		@XmlAttribute(name = "Count")
		private int _count;

		@XmlAttribute(name = "Enchant")
		private int _enchant;

		@XmlAttribute(name = "Attr")
		private int _attr;
		
		@XmlAttribute(name = "Bless")
		private int _bless; 

		@XmlAttribute(name = "Identi")
		private boolean _identified;

		private int _chance;
		@XmlAttribute(name = "Chance")
		private void setChance(double chance) {
			_chance = (int) (chance * 10000);
		}

		public int getItemId() {
			return _itemId;
		}
		public int getCount() {
			return _count;
		}
		public int getEnchant() {
			return _enchant;
		}
		public int getAttr() {
			return _attr;
		}
		public int getBless() { 
			return _bless;
		}
		//0 : 축복 1: 보통 2: 저주 3: 미확인 128: 축봉인 129: 봉인 130: 저주봉인 131: 미확인봉인
		public boolean getIdentified() {
			return _identified;
		}

		public double getChance() {
			return _chance;
		}
	}

	private static enum TYPE {
		RANDOM, SPECIFIC, RANDOM_SPECIFIC
	}

	private static final String PATH = "./data/xml/Item/TreasureBox.xml";

	private static final HashMap<Integer, L1TreasureBox> _dataMap = new HashMap<Integer, L1TreasureBox>();

	public static L1TreasureBox get(int id) {
		return _dataMap.get(id);
	}

	@XmlAttribute(name = "ItemId")
	private int _boxId;

	@XmlAttribute(name = "Type")
	private TYPE _type;

	private int getBoxId() {
		return _boxId;
	}

	private TYPE getType() {
		return _type;
	}

	@XmlElement(name = "Item")
	private CopyOnWriteArrayList<Item> _items;

	private List<Item> getItems() {
		return _items;
	}

	private int _totalChance;

	private int getTotalChance() {
		return _totalChance;
	}

	private void init() {
		for (Item each : getItems()) {
			_totalChance += each.getChance();
			if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
				getItems().remove(each);
				//_log.warning(String.format("아이템 ID %d 의 템플릿이 발견되지 않았습니다.", each.getItemId()));
				_log.warning(String.format("No template was found for item ID %d.", each.getItemId()));
			}
		}
		if (getType() == TYPE.RANDOM && getTotalChance() != 1000000) {
			//_log.warning(String.format("ID %d의 확률의 합계가 100%가 되지 않습니다.", getBoxId()));
			_log.warning(String.format("The sum of the probabilities for ID %d does not add up to 100%.", getBoxId()));
		}
	}

	public static void load() {
//		PerformanceTimer timer = new PerformanceTimer();
//		System.out.print("■ 트래져박스 데이터 .......................... ");
		try {
			JAXBContext context = JAXBContext.newInstance(L1TreasureBox.TreasureBoxList.class);

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(PATH);
			TreasureBoxList list = (TreasureBoxList) um.unmarshal(file);

			for (L1TreasureBox each : list) {
				each.init();
				_dataMap.put(each.getBoxId(), each);
			}
		} catch (Exception e) {
			//_log.log(Level.SEVERE, PATH + "의 로드에 실패.", e);
			_log.log(Level.SEVERE, "Error loading path: " + PATH, e);
			System.exit(0);
		}
//		System.out.println("■ 로딩 정상 완료 " + timer.get() + "ms");
	}
	
	int getBless(int temp) {
		switch (temp) {
		case 0:return 128;// 축
		case 1:return 129;// 보통
		case 2:return 130;// 저주
		case 3:return 131;// 미확인
		default:return 0;
		}
	}
	
	public boolean open(L1PcInstance pc) {
		L1ItemInstance item = null;
		ItemTable tb		= ItemTable.getInstance();
		if (getType().equals(TYPE.SPECIFIC)) {
			for (Item each : getItems()) {
				int itemid				= each.getItemId();
				int itemcount			= each.getCount();
				int enchantLevel		= each.getEnchant();
				int attrEnchantLevel	= each.getAttr();
				int bless				= each.getBless();
				L1Item temp = tb.getTemplate(itemid);
				if (temp == null) {
					continue;
				}
				if (temp.isMerge()) {
					item = tb.createItem(itemid);
					item.setCount(itemcount);
					item.setIdentified(each.getIdentified());
					storeItem(pc, item, getBoxId());
					if (bless == 1) {
						if (temp.getBless() >= 0 && temp.getBless() <= 3) {		
							item.setBless(getBless(temp.getBless()));
							pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
							pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
						}
					}
				} else {
					for (int i = 0; i < itemcount; i++) {
						item = tb.createItem(itemid);						
						if (enchantLevel != 0) {
							item.setIdentified(true);
							item.setEnchantLevel(enchantLevel);
							item.setAttrEnchantLevel(attrEnchantLevel);
						}
						item.setCount(1);
						
						/** 마족무기함 **/
						if ((getBoxId() >= 410127 && getBoxId() <= 410132)
								|| getBoxId() == 410170 || getBoxId() == 410171) {
							item.setAttrEnchantLevel(CommonUtil.randomIntChoice(DEVIL_ATTR_ENCHANT));
						}
						
						item.setIdentified(each.getIdentified());
						storeItem(pc, item, getBoxId());
						if (bless == 1) {
							if (temp.getBless() >= 0 && temp.getBless() <= 3) {
								item.setBless(getBless(temp.getBless()));
								pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
								pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
							}
						}
					}
				}
			}
		} else if (getType().equals(TYPE.RANDOM)) {
			int chance = 0;
			int r = CommonUtil.random(getTotalChance());
			for (Item each : getItems()) {
				chance += each.getChance();
				if (r < chance) {
					int itemid				= each.getItemId();
					int itemcount			= each.getCount();
					int enchantLevel		= each.getEnchant();
					int attrEnchantLevel	= each.getAttr();
					int bless				= each.getBless();
					L1Item temp = tb.getTemplate(itemid);
					if (temp == null) {
						continue;
					}
					if (temp.isMerge()) {
						item = tb.createItem(itemid);
						item.setCount(itemcount);
						item.setIdentified(each.getIdentified());
						storeItem(pc, item, getBoxId());
						if (bless == 1) {
							if (temp.getBless() >= 0 && temp.getBless() <= 3) {
								int Bless = getBless(temp.getBless());
								item.setBless(Bless);
								pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
								pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
							}
						}
					} else {
						for (int i = 0; i < itemcount; i++) {
							item = tb.createItem(itemid);
							item.setCount(1);
							item.setIdentified(each.getIdentified());
							if (enchantLevel != 0) {
								item.setIdentified(true);
								item.setEnchantLevel(enchantLevel);
								item.setAttrEnchantLevel(attrEnchantLevel);
							}
							storeItem(pc, item, getBoxId());
							if (bless == 1) {
								if (temp.getBless() >= 0 && temp.getBless() <= 3) {
									item.setBless(getBless(temp.getBless()));
									pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
									pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
								}
							}
						}
					}
					break;
				}
			}
		} else if (getType().equals(TYPE.RANDOM_SPECIFIC)) {
			int chance = 0;
			int r = CommonUtil.random(getTotalChance());
			for (Item each : getItems()) {
				if (each.getChance() == 0) {
					item = tb.createItem(each.getItemId());
					if (item != null && !isOpen(pc)) {
						item.setCount(each.getCount());
						item.setIdentified(each.getIdentified());
						storeItem(pc, item, getBoxId());
					}
					continue;
				}
				chance += each.getChance();
				if (r < chance) {
					item = tb.createItem(each.getItemId());
					if (item != null && !isOpen(pc)) {
						item.setCount(each.getCount());
						item.setIdentified(each.getIdentified());
						storeItem(pc, item, getBoxId());
					}
					break;
				}
			}
		}

		if (item == null) {
			return false;
		}
		int boxId = getBoxId();
		if (boxId == 3000045) {
			item.setEnchantLevel(CommonUtil.randomIntChoice(OLD_WEAPONE_ENCHANT));// 고대 물품:무기
		} else if ((boxId >= 3000038 && boxId <= 3000044) || boxId == 3000061) {
			item.setEnchantLevel(CommonUtil.randomIntChoice(OLD_ARMOR_ENCHANT));// 고대 물품:방어구
		}
		
		if (L1FavorBookLoader.isFavorItem(item.getItemId()) || (boxId >= 31688 && boxId <= 31696)) {
			pc.getConfig().setLuckyBagOpenResultItem(item);
		}
		return true;
	}
	
	private static final int[] OLD_WEAPONE_ENCHANT	= { 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4 ,0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 6, 3, 3, 3, 1, 2, 3, 4, 4, 5, 1, 2, 3, 7 };
	private static final int[] OLD_ARMOR_ENCHANT	= { 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4 ,0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 5 };
	private static final int[] DEVIL_ATTR_ENCHANT	= { 0, 1, 2, 6, 7, 11, 12, 16, 17 };
	
	boolean isOpen(L1PcInstance pc) {
		int totalCount = pc.getInventory().getSize();
		if (pc.getInventory().getWeightPercent() >= 90 || totalCount > L1PcInventory.MAX_SIZE - 5) {
			pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return true;
		}
		return false;
	}
	
	void storeItem(L1PcInstance pc, L1ItemInstance item, int boxid) {
		if (pc.getInventory().checkAddItem(item, item.getCount()) != L1Inventory.OK) {
			pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return;
		}
		pc.getInventory().storeItem(item);
		pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
		if (ItemMentTable.isMent(ItemMentType.TREASURE_BOX, item.getItemId())) {
			S_MessegeNoti message = new S_MessegeNoti(4509, ItemTable.getInstance().getTemplate(boxid).getDesc(), item.getViewName(), item.getItem().getItemNameId());
			if (pc.getConfig().isGlobalMessege()) {
				pc.sendPackets(message);
			}
			L1World.getInstance().broadcastPacket(pc, message, true);// 누군가가 {0}에서 {1}을(를) 획득 하였습니다.
		}
		if (boxid == 420120) {
			pc.send_effect(20554);// 아우라키아 선물 축하 폭죽 이팩트
		} else if (boxid == 30833) {
			adenGraceBoxQuest(pc);// 아덴의 신속 가호(3시간) 상자
		} else if (boxid == 31234) {
			soloAgit(pc);
		}
	}
	
	void adenGraceBoxQuest(L1PcInstance pc){
		pc.getQuest().questProcess(L1BeginnerQuest.ADEN_SPEED_GRACE);
	}
	
	void soloAgit(L1PcInstance pc) {
		L1House house = HouseTable.getInstance().getHouseTable(2237);
		if (house == null) {
			//System.out.println("[영광의 아지트] EMPTY");
			System.out.println("[Glorious Hideout] EMPTY");
			return;
		}
		house.getTaxDeadline().setTimeInMillis(System.currentTimeMillis());
		HouseTable.getInstance().updateHouse(house);
	}
}

package l1j.server.server.model.item.function;

import java.sql.Timestamp;
import java.util.Calendar;

import javolution.util.FastTable;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1BeginnerQuest;
import l1j.server.server.construct.L1CharacterInfo.L1Class;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemBoxTable;
import l1j.server.server.datatables.ItemBoxTable.ItemBox;
import l1j.server.server.datatables.ItemBoxTable.ItemBoxData;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class ItemBoxOpen extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ItemBoxOpen(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			ItemBox box = ItemBoxTable.getItemBox(getItemId());
			if (box == null) {
				return;
			}
			if (box.getValidateItems() != null && box.getValidateItems().length > 0 && pc.getInventory().checkItemOne(box.getValidateItems())) {
				pc.sendPackets(L1ServerMessage.sm2887);// 특정 아이템이 있어 아직은 사용할 수 없습니다.
				return;
			}
			FastTable<Integer> limitMaps = box.getLimitMaps();
			if (!limitMaps.isEmpty() && !limitMaps.contains(pc.getMapId())) {
				pc.sendPackets(L1ServerMessage.sm563);// 이 곳에서는 사용할 수 없습니다.
				return;
			}
			
			boolean isDelayEffect = false;
			int delayEffect = ((L1EtcItem) this.getItem()).getDelayEffect();
			if (delayEffect > 0) {
				isDelayEffect = true;
				Timestamp lastUsed = getLastUsed();
				if (lastUsed != null) {
					long ms		= Calendar.getInstance().getTimeInMillis();
					long last	= lastUsed.getTime();
					if ((ms - last) / 1000 <= delayEffect) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage(((delayEffect - (ms - last) / 1000) / 60) + "분 " + ((delayEffect - (ms - last) / 1000) % 60) + "초 후에 사용할 수 있습니다."), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(100) + " " + ((delayEffect - (ms - last) / 1000) / 60)  + " " + S_SystemMessage.getRefText(106) + " " + ((delayEffect - (ms - last) / 1000) % 60) + " " + S_SystemMessage.getRefText(1306), true), true);
						return;
					}
				}
			}
			
			classTypeItem(pc, box.getClassItemList().get(L1Class.NONE.getType()));
			classTypeItem(pc, box.getClassItemList().get(pc.getType()));
			if (box.isQuestBox()) {
				questBox(pc);
			}
			if (box.getEffectId() > 0) {
				pc.send_effect(box.getEffectId());
			}
			if (getItemId() == 30833) {
				pc.getQuest().questProcess(L1BeginnerQuest.ADEN_SPEED_GRACE);// 아덴의 신속 가호(3시간) 상자
			}
			boolean deleteItem	= box.isDelete();
			// 효과 지연이 있는 경우는 현재 시간을 세트
			if (isDelayEffect) {
				if (getChargeCount() > 0) {// 횟수 제한 아이템
					if (deleteItem) {
						setChargeCount(getChargeCount() - 1);
					}
					if (getChargeCount() <= 0) {
						pc.getInventory().removeItem(this, 1);
					} else {
						if (getLastUsed() != null) {
							getLastUsed().setTime(System.currentTimeMillis());
						} else {
							setLastUsed(new Timestamp(System.currentTimeMillis()));
						}
						pc.getInventory().updateItem(this, L1PcInventory.COL_CHARGE_COUNT);
						pc.getInventory().saveItem(this, L1PcInventory.COL_CHARGE_COUNT);
					}
				} else {
					if (deleteItem) {
						pc.getInventory().removeItem(this, 1);
					} else {
						if (getLastUsed() != null) {
							getLastUsed().setTime(System.currentTimeMillis());
						} else {
							setLastUsed(new Timestamp(System.currentTimeMillis()));
						}
						pc.getInventory().updateItem(this, L1PcInventory.COL_DELAY_EFFECT);
						pc.getInventory().saveItem(this, L1PcInventory.COL_DELAY_EFFECT);
					}
				}
			} else {
				if (deleteItem) {
					pc.getInventory().removeItem(this, 1);
				}
			}
		}
	}
	
	private void classTypeItem(L1PcInstance pc, FastTable<ItemBoxData> list){
		if (list == null || list.isEmpty()) {
			return;
		}
		for (ItemBoxData data : list) {
			if (data == null || data.getChance() < 100 && CommonUtil.random(100) + 1 > data.getChance()) {
				continue;
			}
			createItem(pc, data.getItemId(), data.getCount(), data.getEnchant(), data.getBless(), data.getAttr(), data.isIdendi(), data.getLimitTime());
		}
	}
	
	private void questBox(L1PcInstance pc) {
		if (pc.isElf()) {
			createItem(pc, 30076, 2, 0, 1, 0, false, 0);
		} else if (pc.isWizard()) {
        	createItem(pc, 30089, 2, 0, 1, 0, false, 0);
        	createItem(pc, 30083, 2, 0, 1, 0, false, 0);
        } else if (pc.isIllusionist()) {
        	createItem(pc, 30089, 2, 0, 1, 0, false, 0);
        	createItem(pc, 30077, 2, 0, 1, 0, false, 0);
        } else if(pc.isDragonknight()) {
        	createItem(pc, 30081, 4, 0, 1, 0, false, 0);
        } else if (pc.isDarkelf()) {
        	createItem(pc, 30080, 4, 0, 1, 0, false, 0);
        } else {
        	createItem(pc, 30073, 2, 0, 1, 0, false, 0);
        }
	}
	
	private void createItem(L1PcInstance pc, int itemId, int count, int enchantLevel, int bless, int attr, boolean identi, int limitTime) {
		ItemTable itemTable = ItemTable.getInstance();
		L1Item temp = itemTable.getTemplate(itemId);
		if (temp == null) {
			return;
		}
		if (temp.isMerge()) {
			L1ItemInstance item = itemTable.createItem(temp);
			item.setCount(count);
			item.setIdentified(identi);
			item.setBless(bless);
			if (limitTime > 0) {
				limitTimeItem(item, limitTime);
			}
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
			} else {
				L1World.getInstance().getInventory(pc.getX(), pc.getY(),pc.getMapId()).storeItem(item);
				pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			}
		} else {
			for (int i=0; i<count; i++) {
				L1ItemInstance item = itemTable.createItem(temp);
				item.setCount(1);
				item.setEnchantLevel(enchantLevel);
				item.setAttrEnchantLevel(attr);
				item.setIdentified(identi);
				item.setBless(bless);
				item.setCount(1);
				if (limitTime > 0) {
					limitTimeItem(item, limitTime);
				}
				if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
					pc.getInventory().storeItem(item);
					pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
				} else {
					L1World.getInstance().getInventory(pc.getX(), pc.getY(),pc.getMapId()).storeItem(item);
					pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				}
			}
		}
	}
	
	private void limitTimeItem(L1ItemInstance item, int limitTime){
		Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + (60000L * (long)limitTime));
		item.setEndTime(deleteTime);
		item.setIdentified(true);
		item.setEngrave(true);
	}

}



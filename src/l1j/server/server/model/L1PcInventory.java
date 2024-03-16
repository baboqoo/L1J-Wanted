package l1j.server.server.model;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.common.data.Material;
import l1j.server.server.construct.L1BeginnerQuest;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemNormalType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.PenaltyItemTable;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.L1OmanTowerAmulet;
import l1j.server.server.model.item.collection.L1CollectionHandler;
import l1j.server.server.model.item.collection.L1CollectionLoader;
import l1j.server.server.model.item.collection.L1CollectionModel;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.action.BurningShot;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_FourthGear;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.inventory.S_AddInventoryNoti;
import l1j.server.server.serverpackets.inventory.S_DeleteInventoryItem;
import l1j.server.server.serverpackets.inventory.S_ItemColor;
import l1j.server.server.serverpackets.inventory.S_ItemName;
import l1j.server.server.serverpackets.inventory.S_ItemStatus;
import l1j.server.server.serverpackets.inventory.S_ObtainedItemInfo;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.returnedstat.S_StatusCarryWeightInfoNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class L1PcInventory extends L1Inventory {
    private static final long serialVersionUID = 1L;
    private static Logger _log = Logger.getLogger(L1PcInventory.class.getName());

    private final L1PcInstance _owner;
    private L1ItemInstance _arrow, _sting;
    private L1OmanTowerAmulet _omanAmulet;
    private L1CollectionHandler _collection;
    public long timeVisible = 0L, timeVisibleDelay = 1000L;

    public L1PcInventory(L1PcInstance owner) {
        _owner		= owner;
        _omanAmulet	= new L1OmanTowerAmulet(this);
        _collection	= new L1CollectionHandler(this);
    }

    public L1PcInstance getOwner() {
        return _owner;
    }
    
    public L1OmanTowerAmulet getOmanAmulet(){
    	return _omanAmulet;
    }
    
    public L1CollectionHandler getCollection(){
    	return _collection;
    }

    public int getWeightPercent() {
    	if (Config.RATE.RATE_WEIGHT_LIMIT == 0) {
    		return 0;// 웨이트 레이트가 0이라면 중량 항상 0
    	}
    	if (getSize() > MAX_SIZE) {
    		return 100;// 아이템 보유 수량을 벗어나면 100%
    	}
		return 100 * getWeight() / _owner.getMaxWeight();
	}

    @Override
    public int checkAddItem(L1ItemInstance item, int count) {
        return item == null ? -1 : checkAddItem(item.getItem(), count, true);
    }
    
    public int checkAddItem(L1Item item, int count) {
        return checkAddItem(item, count, false);
    }
    
    public int checkAddItem(L1Item item, int count, boolean message) {
        if (item == null) {
        	return -1;
        }
        if (count < 0 || count > MAX_AMOUNT) {
        	return AMOUNT_OVER;
        }

        if (getSize() > MAX_SIZE || (getSize() == MAX_SIZE && (!item.isMerge() || !checkItem(item.getItemId()))) || (!item.isMerge() && getAvailable() < count)) {
            if (message) {
				_owner.sendPackets(L1ServerMessage.sm3560);// 인벤토리 공간이 부족합니다.
            }
            return SIZE_OVER;
        }

        int weight = getWeight() + item.getWeight() * count / 1000 + 1;
        if (weight < 0 || (item.getWeight() * count / 1000) < 0 || getWeightPercent() >= 100) {
            if (message) {
            	_owner.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
            }
            return WEIGHT_OVER;
        }

        L1ItemInstance itemExist = findItemId(item.getItemId());
        if (itemExist != null && ((itemExist.getCount() + count) < 0 || (itemExist.getCount() + count) > MAX_AMOUNT)) {
            if (message) {
            	_owner.sendPackets(L1SystemMessage.ADENA_LOOT_MAX_FAIL);
            }
            return AMOUNT_OVER;
        }
        return OK;
    }

    public void sendOptioon() {
        try {
            for (L1ItemInstance item : _items) {
                if (item.isEquipped()) {
                    item.setEquipped(false);
                    _owner.getEquipSlot().removeSetItems(item.getItemId());
                    setEquipped(item, true, true, false);
                }
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    @Override
    public void loadItems() {
        try {
        	L1World world = L1World.getInstance();
            CharactersItemStorage storage = CharactersItemStorage.create();
            for (L1ItemInstance item : storage.loadItems(_owner.getId())) {
                item.updateItemAbility(_owner);
                
                // TODO 아이템 추가전 인벤토리 검증
                int itemId	= item.getItemId();
                int useType	= item.getItem().get_interaction_type();
                if (useType == L1ItemType.DOMINATION_POLY.getInteractionType()) {// 변신 지배 반지
                	_owner.getConfig()._dominationPolyRing = true;
                	_owner.sendPackets(S_Ability.ABLITY_DOMINATION_POLY_ON);
                } else if (useType == L1ItemType.LEV_100_POLY.getInteractionType()) {// [%i] 변신 지배 반지
                	_owner.getConfig()._level100PolyRing = true;
                	_owner.sendPackets(S_Ability.ABLITY_DOMINATION_POLY_ON);
                } else if (itemId == 700024 || itemId == 700025) {
        			_owner.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK_ITEM, item.getId(), "$13719", L1BookMark.ShowBookmarkitem(_owner, item.getItemId())), true);
                } else if (itemId == 700023) {
                	_owner.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK_ITEM, item.getId(), !StringUtil.isNullOrEmpty(item.getCreaterName()) ? item.getCreaterName() : "$13719", L1BookMark.ShowBookmarkitem(_owner, item.getId())), true);
                } else if (itemId == 202811 || itemId == 202812) {
                	_owner.getConfig()._dominationTeleportRing = true;
                } else if (itemId == 560040 || itemId == 560041) {
                	_owner.getConfig()._dominationHeroRing = true;
                } else if (itemId == 94001) {
                	_owner._isRuunPaper = true;
                } else if (L1ItemId.isAmuletItem(itemId)) {
                	_omanAmulet.enable(itemId);
                }

                L1CollectionModel collection = L1CollectionLoader.getCollection(itemId);
                if (collection != null) {
                	_collection.collectionAblity(item, collection, true);
                }
                item.setWeaponSkill();
                
                L1FavorBookObject favor = L1FavorBookLoader.getFavor(itemId);// 성물
                if (favor != null && _owner.getFavorBook().offer(favor, item)) {
                	storage.deleteItem(item);// 마이그레이션 완료후 디비에서 제거
                	continue;
                }
                
                if (itemId == L1ItemId.ADENA) {
                    L1ItemInstance itemExist = findItemId(itemId);
                    if (itemExist != null) {
                        storage.deleteItem(item);
                        int newCount = itemExist.getCount() + item.getCount();
                        if (newCount <= MAX_AMOUNT) {
                            if (newCount < 0) {
                            	newCount = 0;
                            }
                            itemExist.setCount(newCount);
                            storage.updateItemCount(itemExist);
                        }
                    } else {
                        _items.add(item);
                        world.storeObject(item);
                    }
                } else {
                    _items.add(item);
                    world.storeObject(item);
                }
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void insertItem(L1ItemInstance item) {
    	try {
    		if (item == null || _owner == null) {
    			return;
    		}
    		item.updateItemAbility(_owner);
        	
        	// TODO 아이템 추가전 인벤토리 검증
        	int itemId	= item.getItemId();
        	int interaction_type	= item.getItem().get_interaction_type();
            if (interaction_type == L1ItemType.DOMINATION_POLY.getInteractionType() && !checkItemInteractionType(L1ItemType.DOMINATION_POLY.getInteractionType())) {
            	_owner.getConfig()._dominationPolyRing = true;
            	_owner.sendPackets(S_Ability.ABLITY_DOMINATION_POLY_ON);
            } else if (interaction_type == L1ItemType.LEV_100_POLY.getInteractionType() && !checkItemInteractionType(L1ItemType.LEV_100_POLY.getInteractionType())) {
            	_owner.getConfig()._level100PolyRing = true;
            	_owner.sendPackets(S_Ability.ABLITY_DOMINATION_POLY_ON);
            } else if (itemId == 700024 || itemId == 700025) {
    			_owner.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK_ITEM, item.getId(), "$13719", L1BookMark.ShowBookmarkitem(_owner, item.getItemId())), true);
            } else if (itemId == 700023) {
            	_owner.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK_ITEM, item.getId(), !StringUtil.isNullOrEmpty(item.getCreaterName()) ? item.getCreaterName() : "$13719", L1BookMark.ShowBookmarkitem(_owner, item.getId())), true);
            } else if (itemId == 810007) {// 정화의 막대
            	_owner.sendPackets(L1GreenMessage.FANTASY_SOUL_WAND_DROP);
            } else if (itemId == 420113) {// 희망의 불꽃
            	_owner.sendPackets(L1GreenMessage.INDUN_GERENG_FIRE_ITEM);
                _owner.sendPackets(L1ServerMessage.sm7492);
            } else if (itemId == 420100) {// 화염의 상자
            	_owner.sendPackets(L1GreenMessage.INDUN_GERENG_FIRE_BOX);
                _owner.sendPackets(L1ServerMessage.sm7510);
            } else if (itemId == 420101) {// 혹한의 상자
            	_owner.sendPackets(L1GreenMessage.INDUN_GERENG_WATER_BOX);
                _owner.sendPackets(L1ServerMessage.sm7511);
            } else if (itemId == 420102) {// 섬광의 상자
            	_owner.sendPackets(L1GreenMessage.INDUN_GERENG_WIND_BOX);
                _owner.sendPackets(L1ServerMessage.sm7512);
            } else if (itemId == 202811 || itemId == 202812) {
            	_owner.getConfig()._dominationTeleportRing = true;
            } else if (itemId == 560040 || itemId == 560041) {
            	_owner.getConfig()._dominationHeroRing = true;
            } else if (itemId == 94001) {
            	_owner._isRuunPaper = true;
            } else if (L1ItemId.isAmuletItem(itemId)) {
            	_omanAmulet.enable(itemId);
            }
            
            L1CollectionModel collection = L1CollectionLoader.getCollection(itemId);
            if (collection != null) {
            	_collection.collectionAblity(item, collection, true);
            }
            item.setWeaponSkill();
            
            L1FavorBookObject favor = L1FavorBookLoader.getFavor(itemId);// 성물
            if (favor != null && _owner.getFavorBook().offer(favor, item)) {
            	return;
            }
            
        	if (_owner instanceof L1AiUserInstance == false) {
        		CharactersItemStorage storage = CharactersItemStorage.create();
                storage.storeItem(_owner.getId(), item);
                _owner.getQuest().questCollectItem(item.getItem().getQuestCollectItem(), item.getCount());// 퀘스트 아이템
        	}
            _owner.sendPackets(new S_AddInventoryNoti(item, _owner), true);
            _items.add(item);
            if (item.getItem().getWeight() != 0) {
                _owner.sendPackets(new S_StatusCarryWeightInfoNoti(_owner), true);
            }
    	} catch (Exception e) {
    		_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
    	}
    }
    
    public static final int COL_DURABILITY		= 1;
    public static final int COL_IS_ID			= COL_DURABILITY << 1;
    public static final int COL_ENCHANTLVL		= COL_IS_ID << 1;
    public static final int COL_EQUIPPED		= COL_ENCHANTLVL << 1;
    public static final int COL_COUNT			= COL_EQUIPPED << 1;
    public static final int COL_DELAY_EFFECT	= COL_COUNT << 1;
    public static final int COL_ITEMID			= COL_DELAY_EFFECT << 1;
    public static final int COL_CHARGE_COUNT	= COL_ITEMID << 1;
    public static final int COL_REMAINING_TIME	= COL_CHARGE_COUNT << 1;
    public static final int COL_BLESS			= COL_REMAINING_TIME << 1;
    public static final int COL_ATTRENCHANTLVL	= COL_BLESS << 1;
    public static final int COL_SPECIAL_ENCHANT	= COL_ATTRENCHANTLVL << 1;
    public static final int COL_DOLL_POTENTIAL	= COL_SPECIAL_ENCHANT << 1;
    public static final int COL_SLOTS			= COL_DOLL_POTENTIAL << 1;
    public static final int COL_SCHEDULED		= COL_SLOTS << 1;
    public static final int COL_SAVE_ALL		= COL_SCHEDULED << 1;

    @Override
    public void updateItem(L1ItemInstance item) {
        updateItem(item, COL_COUNT);
        if (item.getItem().isToBeSavedAtOnce()) {
            saveItem(item, COL_COUNT);
        }
    }

    /**
     * 목록내의 아이템 상태를 갱신한다.
     * 
     * @param item -
     *            갱신 대상의 아이템
     * @param column -
     *            갱신하는 스테이터스의 종류
     */
    @Override
    public void updateItem(L1ItemInstance item, int column) {
    	if (column >= COL_SCHEDULED) {
    		updateInfo(item);
            _owner.sendPackets(new S_ItemStatus(item, _owner), true);
            column -= COL_SCHEDULED;
        }
    	if (column >= COL_SLOTS) {
    		updateInfo(item);
            _owner.sendPackets(new S_ItemStatus(item, _owner), true);
            column -= COL_SLOTS;
        }
    	if (column >= COL_DOLL_POTENTIAL) {
    		item.updateItemAbility(_owner);
            _owner.sendPackets(new S_ItemStatus(item, _owner), true);
            _owner.sendPackets(new S_ItemColor(item), true);
            column -= COL_DOLL_POTENTIAL;
        }
        if (column >= COL_SPECIAL_ENCHANT) {
            _owner.sendPackets(new S_ItemName(item), true);
            column -= COL_SPECIAL_ENCHANT;
        }
        if (column >= COL_ATTRENCHANTLVL) {
        	updateInfo(item);
            _owner.sendPackets(new S_ItemName(item), true);
            _owner.sendPackets(new S_ItemStatus(item, _owner), true);
            _owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ENCHANT_UPDATE, item), true);
            column -= COL_ATTRENCHANTLVL;
        }
        if (column >= COL_BLESS) {
        	item.updateItemAbility(_owner);
            _owner.sendPackets(new S_ItemColor(item), true);
            column -= COL_BLESS;
        }
        if (column >= COL_REMAINING_TIME) {
        	updateInfo(item);
            _owner.sendPackets(new S_ItemName(item), true);
            _owner.sendPackets(new S_ItemStatus(item, _owner), true);
            column -= COL_REMAINING_TIME;
        }
        if (column >= COL_CHARGE_COUNT) {
            _owner.sendPackets(new S_ItemName(item), true);
            column -= COL_CHARGE_COUNT;
        }
        if (column >= COL_ITEMID) {
        	item.updateItemAbility(_owner);
            _owner.sendPackets(new S_ItemStatus(item, _owner), true);
            _owner.sendPackets(new S_ItemColor(item), true);
            _owner.sendPackets(new S_StatusCarryWeightInfoNoti(_owner), true);
            column -= COL_ITEMID;
        }
        if (column >= COL_DELAY_EFFECT) {
            column -= COL_DELAY_EFFECT;
        }
        if (column >= COL_COUNT) {
            // _owner.sendPackets(new S_ItemAmount(item), true);
            int weight = item.getWeight();
            if (weight != item.getLastWeight()) {
            	item.setLastWeight(weight);
            } else {
            	_owner.sendPackets(new S_ItemName(item), true);
            }
            _owner.sendPackets(new S_ItemStatus(item, _owner), true);
            if (item.getItem().getWeight() != 0) {
                _owner.sendPackets(new S_StatusCarryWeightInfoNoti(_owner), true);
            }
            column -= COL_COUNT;
            _owner.getQuest().questCollectItem(item.getItem().getQuestCollectItem(), item.getCount());// 퀘스트 아이템
        }
        if (column >= COL_EQUIPPED) {
            _owner.sendPackets(new S_ItemName(item), true);
            column -= COL_EQUIPPED;
        }
        if (column >= COL_ENCHANTLVL) {
        	item.updateItemAbility(_owner);
        	_owner.sendPackets(new S_ItemStatus(item, _owner), true);
            _owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ENCHANT_UPDATE, item), true);
            column -= COL_ENCHANTLVL;
        }
        if (column >= COL_IS_ID) {
        	updateInfo(item);
            _owner.sendPackets(new S_ItemStatus(item, _owner), true);
            _owner.sendPackets(new S_ItemColor(item), true);
            column -= COL_IS_ID;
        }
        if (column >= COL_DURABILITY) {
        	updateInfo(item);
            _owner.sendPackets(new S_ItemStatus(item, _owner), true);
            _owner.sendPackets(new S_PacketBox(S_PacketBox.WEAPON_DAMAGED_GFX, item.getDurability()), true);
            column -= COL_DURABILITY;
        }
    }
    
    /**
     * 아이템 획득 및 소모 정보 업데이트
     */
    @Override
    public void updteItemObtained(L1ItemInstance item, int count, boolean store) {
    	int nameId = item.getItem().getItemNameId();
		if (nameId == -1 || nameId == 0 || nameId == 30000) {
			return;
		}
		_owner.sendPackets(new S_ObtainedItemInfo(_owner, item, store ? count : -count), true);
	}
    
    /**
     * 서버패킷에서 사용되는 바이트 배열을 업데트한다.(확인 상태인 아이템만 처리)
     * @param item
     */
    void updateInfo(L1ItemInstance item) {
    	if (!item.isIdentified()) {
    		return;
    	}
    	item.updateExtraDescription();
    }
    
    /**
     * 아이템 슬롯 상태를 DB에 갱신한다.
     * @param item
     * @param slot_no
     * @param stone_name_id
     */
    public void saveItemSlot(L1ItemInstance item, int slot_no, int stone_name_id) {
    	try {
    		CharactersItemStorage storage = CharactersItemStorage.create();
    		storage.updateItemSlot(item, slot_no, stone_name_id);
    	} catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * 목록내의 아이템 상태를 DB에 보존한다.
     * @param item - 갱신 대상의 아이템
     * @param column - 갱신하는 스테이터스의 종류
     */
    public void saveItem(L1ItemInstance item, int column) {
        if (column == 0) {
        	return;
        }
        try {
            CharactersItemStorage storage = CharactersItemStorage.create();
            if (column >= COL_SAVE_ALL) {
                storage.updateItemAll(item);
                return;
            }
            if (column >= COL_SCHEDULED) {
                storage.updateItemScheduled(item);
                column -= COL_SCHEDULED;
            }
            if (column >= COL_SLOTS) {
                storage.updateItemSlotAll(item);
                column -= COL_SLOTS;
            }
            if (column >= COL_DOLL_POTENTIAL) {
                storage.updateItemPotential(item);
                column -= COL_DOLL_POTENTIAL;
            }
            if (column >= COL_SPECIAL_ENCHANT) {
                storage.updateItemSpecialEnchant(item);
                column -= COL_SPECIAL_ENCHANT;
            }
            if (column >= COL_ATTRENCHANTLVL) {
                storage.updateItemAttrEnchantLevel(item);
                column -= COL_ATTRENCHANTLVL;
            }
            if (column >= COL_BLESS) {
                storage.updateItemBless(item);
                column -= COL_BLESS;
            }
            if (column >= COL_REMAINING_TIME) {
                storage.updateItemRemainingTime(item);
                storage.updateItemEndTime(item);
                column -= COL_REMAINING_TIME;
            }
            if (column >= COL_CHARGE_COUNT) {
                storage.updateItemChargeCount(item);
                column -= COL_CHARGE_COUNT;
            }
            if (column >= COL_ITEMID) {
                storage.updateItemId(item);
                column -= COL_ITEMID;
            }
            if (column >= COL_DELAY_EFFECT) {
                storage.updateItemDelayEffect(item);
                column -= COL_DELAY_EFFECT;
            }
            if (column >= COL_COUNT) {
                storage.updateItemCount(item);
                column -= COL_COUNT;
            }
            if (column >= COL_EQUIPPED) {
                storage.updateItemEquipped(item);
                column -= COL_EQUIPPED;
            }
            if (column >= COL_ENCHANTLVL) {
                storage.updateItemEnchantLevel(item);
                column -= COL_ENCHANTLVL;
            }
            if (column >= COL_IS_ID) {
                storage.updateItemIdentified(item);
                column -= COL_IS_ID;
            }
            if (column >= COL_DURABILITY) {
                storage.updateItemDurability(item);
                column -= COL_DURABILITY;
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void deleteItem(L1ItemInstance item) {
        try {
        	if (!(_owner instanceof L1AiUserInstance)) {
        		CharactersItemStorage storage = CharactersItemStorage.create();
                storage.deleteItem(item);
        	}
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        if (item.isEquipped()) {
            setEquipped(item, false);
        }
        _owner.sendPackets(new S_DeleteInventoryItem(item), true);
        _items.remove(item);
        
        if (item.getItem().getWeight() != 0) {
        	_owner.sendPackets(new S_StatusCarryWeightInfoNoti(_owner), true);
        }
        
        // TODO 아이템 제거후 인벤토리 검사
        int itemId	= item.getItemId();
        int interaction_type	= item.getItem().get_interaction_type();
        /** 변신 지배 반지 **/
        if (interaction_type == L1ItemType.DOMINATION_POLY.getInteractionType() && !checkItemInteractionType(L1ItemType.DOMINATION_POLY.getInteractionType())) {
        	_owner.getConfig()._dominationPolyRing = false;
        	_owner.sendPackets(S_Ability.ABLITY_DOMINATION_POLY_OFF);
        	if (_owner.skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
        		_owner.skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION);
        	}
		} else if (interaction_type == L1ItemType.LEV_100_POLY.getInteractionType() && !checkItemInteractionType(L1ItemType.LEV_100_POLY.getInteractionType())) {
        	_owner.getConfig()._level100PolyRing = false;
        	_owner.sendPackets(S_Ability.ABLITY_DOMINATION_POLY_OFF);
        	if (_owner.skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {
        		_owner.skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL);
        	}
		} else if ((itemId == 202811 || itemId == 202812) && !checkItem(202811) && !checkItem(202812)) {
        	_owner.getConfig()._dominationTeleportRing = false;
        	if (_owner.getMap().isDominationTeleport()) {
        		_owner.sendPackets(new S_SpellBuffNoti(_owner, L1SkillId.DOMINATION_TELEPORT, false, -1), true);
        	}
		} else if ((itemId == 560040 || itemId == 560041) && !checkItem(560040) && !checkItem(560041)) {
        	_owner.getConfig()._dominationHeroRing = false;
		} else if (itemId == 94001 && !checkItem(93008)) {
        	_owner._isRuunPaper = false;
		} else if (L1ItemId.isAmuletItem(itemId)) {
			_omanAmulet.noEnable(itemId);
        }
        
        L1CollectionModel collection = L1CollectionLoader.getCollection(itemId);
        if (collection != null) {
        	_collection.collectionAblity(item, collection, false);
        }
    }

    public L1ItemInstance getItemEquippend(int itemId) {// 아이템 착용 상태 확인의 오브젝트 인식
        L1ItemInstance equipeitem	= null;
        for (L1ItemInstance item : _items) {
            if (item.getItem().getItemId() == itemId && item.isEquipped()) {
                equipeitem = item;
                break;
            }
        }
        return equipeitem;
    }

    public void setEquipped(L1ItemInstance item, boolean equipped) {
        setEquipped(item, equipped, false, false);
    }

    public void setEquipped(L1ItemInstance item, boolean equipped, boolean loaded, boolean changeWeapon) {
    	if (item.isEquipped() == equipped) {
    		return;
    	}
        L1Item temp				= item.getItem();
        int itemId				= temp.getItemId();
        L1ItemType itemType		= temp.getItemType();
        if (equipped) {
            if (L1ItemId.isInvisItem(itemId) && System.currentTimeMillis() - timeVisible < timeVisibleDelay) {
            	return;
            }
            item.setEquipped(true);
            _owner.getEquipSlot().set(item);
            if (_owner.getLevel() <= Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL && Material.isUndeadMaterial(temp.getMaterial())) {
            	_owner.getQuest().questProcess(L1BeginnerQuest.SILVER_WEAPON_EQUIP);
    		}
        } else {
            if (!loaded && L1ItemId.isInvisItem(itemId)) {
            	if (_owner.isInvisble()) {
            		_owner.delInvis();
            		return;
            	}
            	timeVisible = System.currentTimeMillis();
            }
            if (itemType == L1ItemType.WEAPON) {// 무기해제
            	UnEquipWeapon(temp);
            }
            item.setEquipped(false);
            _owner.getEquipSlot().remove(item);
        }
        if (!loaded) {
            _owner.setCurrentHp(_owner.getCurrentHp());
            _owner.setCurrentMp(_owner.getCurrentMp());
            updateItem(item, COL_EQUIPPED);
            _owner.sendPackets(new S_OwnCharStatus(_owner), true);
            if (itemType == L1ItemType.WEAPON && !changeWeapon) {
            	S_CharVisualUpdate visual = new S_CharVisualUpdate(_owner);
            	_owner.sendPackets(visual);
            	_owner.broadcastPacket(visual, true);
            }
        }
        if (itemType == L1ItemType.WEAPON) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.ATTACK_RANGE, _owner, item), true);
			if (_owner.isElf()) {
				elfWeaponChangeItemRefresh();
			}
        }
        // 아이템 착용 처리에 대한 패킷 처리.
        _owner.getInventory().toSlotPacket(_owner, item, false);
    }
    
    /**
     * 무기 착용 해제
     * @param temp
     */
    void UnEquipWeapon(L1Item temp) {
    	L1ItemWeaponType weaponType = temp.getWeaponType();
    	if (L1ItemWeaponType.isSwordWeapon(weaponType)) {// 한손검, 단검, 양손검
    		UnEquipSwordDeleteSpell(weaponType);
    	} else if (L1ItemWeaponType.isBowWeapon(weaponType)) {// 활
    		UnEquipBowDeleteSpell();
        } else if (weaponType == L1ItemWeaponType.CHAINSWORD) {
        	UnEquipChainswordDeleteSpell();
        }
		
    	if (_owner.skillStatus.hasSkillEffect(L1SkillId.BLOW_ATTACK) && L1ItemWeaponType.isShortWeapon(weaponType)) {// 근거리 무기
			_owner.skillStatus.removeSkillEffect(L1SkillId.BLOW_ATTACK);
			_owner.sendPackets(new S_SpellBuffNoti(_owner, L1SkillId.BLOW_ATTACK, false, -1), true);
		}
    }
    
    /**
     * 검 해제 시 제거 스킬
     * @param weaponType
     */
    void UnEquipSwordDeleteSpell(L1ItemWeaponType weaponType) {
    	if (_owner.skillStatus.hasSkillEffect(L1SkillId.DANCING_BLADES)) {
			_owner.skillStatus.removeSkillEffect(L1SkillId.DANCING_BLADES);
		}
		if (weaponType == L1ItemWeaponType.SWORD || weaponType == L1ItemWeaponType.DAGGER) {// 한손검, 단검
    		if (_owner.skillStatus.hasSkillEffect(L1SkillId.SAND_STORM)) {
    			_owner.skillStatus.removeSkillEffect(L1SkillId.SAND_STORM);
    		}
    		if (weaponType == L1ItemWeaponType.SWORD && _owner.skillStatus.hasSkillEffect(L1SkillId.INFERNO)) {// 한손검
    			_owner.skillStatus.removeSkillEffect(L1SkillId.INFERNO);
    		}
    	} else if (weaponType == L1ItemWeaponType.TOHAND_SWORD && _owner.skillStatus.hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {// 양손검
    		_owner.skillStatus.removeSkillEffect(L1SkillId.COUNTER_BARRIER);
    	}
    }
    
    /**
     * 활 해제 시 제거 스킬
     */
    void UnEquipBowDeleteSpell() {
    	if (_owner.skillStatus.hasSkillEffect(L1SkillId.FOCUS_WAVE)) {
    		_owner.skillStatus.removeSkillEffect(L1SkillId.FOCUS_WAVE);
    	}
    	if (_owner.skillStatus.hasSkillEffect(L1SkillId.HURRICANE)) {
    		_owner.skillStatus.removeSkillEffect(L1SkillId.HURRICANE);
    	}
    	if (_owner._isBurningShot) {
    		BurningShot.dispose(_owner);// 버닝샷 해제
    	}
    }
    
    /**
     * 체인소드 해제 시 제거 스킬
     */
    void UnEquipChainswordDeleteSpell() {
    	if (_owner.skillStatus.hasSkillEffect(L1SkillId.HALPHAS)) {
        	_owner.skillStatus.removeSkillEffect(L1SkillId.HALPHAS);// 체인소드
        }
    }
    
    /**
     * 요정의 무기 변경에 따른 아이템 능력치 변경
     */
    void elfWeaponChangeItemRefresh() {
    	for (L1ItemInstance checkItem : _items) {
			if (L1ItemId.isClassAblityItem(checkItem.getItemId())) {// 사혈의 귀걸이, 광분의 목걸이, 루운3세의 부츠, 정화의 부츠
				boolean equipCheck = checkItem.isEquipped();
				if (equipCheck) {
					_owner.getEquipSlot().remove(checkItem);// 능력치 해제
				}
				checkItem.updateItemAbility(_owner);// 능력치 변경
				_owner.sendPackets(new S_ItemStatus(checkItem, _owner), true);// 아이템 정보 리프레쉬
				if (equipCheck) {
					_owner.getEquipSlot().set(checkItem);// 능력치 적용
				}
			}
		}
    }

    public boolean checkEquipped(int id) {
        for (L1ItemInstance item : _items) {
            if (item.getItem().getItemId() == id && item.isEquipped()) {
                return true;
            }
        }
        return false;
    }

    public int getNameEquipped(L1ItemType itemType, int type, String desckr) {
        int equipeCount = 0;
        for (L1ItemInstance item : _items) {
            if (item.getItem().getItemType() == itemType && item.getItem().getType() == type && item.isEquipped() && item.getDescKr().equals(desckr)) {
            	equipeCount++;
            }
        }
        return equipeCount;
    }

    // 아이템 배열 전체 작용여부
    public boolean checkEquipped(int[] ids) {
        for (int id : ids) {
            if (!checkEquipped(id)) {
                return false;
            }
        }
        return true;
    }
    
    // 아이템 배열 하나 착용여부
    public boolean checkEquippedOne(int[] ids) {
        for (int id : ids) {
        	if (checkEquipped(id)) {
        		return true;
        	}
        }
        return false;
    }
    
    public L1ItemInstance getFirstItemType(L1ItemType itemType) {
    	for (L1ItemInstance item : _items) {
            if (item.getItem().getItemType() == itemType) {// 최초 체크된 아이템
                return item;
            }
        }
        return null;
    }

    public int getTypeEquipped(L1ItemType itemType, int type) {
        int equipeCount = 0;
        for (L1ItemInstance item : _items) {
            if (item.getItem().getItemType() == itemType && item.getItem().getType() == type && item.isEquipped()) {
                equipeCount++;
            }
        }
        return equipeCount;
    }

    public L1ItemInstance getItemEquipped(L1ItemType itemType, int type) {
        for (L1ItemInstance item : _items) {
            if (item.getItem().getItemType() == itemType && item.getItem().getType() == type && item.isEquipped()) {
                return item;
            }
        }
        return null;
    }
    
	public L1ItemInstance getItemEquipped(int itemId) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemId() == itemId && item.isEquipped()) {
				return item;
			}
		}
		return null;
	}
	
	public L1ItemInstance checkEquippedItem(int id) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemId() == id && item.isEquipped()) {
				return item;
			}
		}
		return null;
	}

    public L1ItemInstance[] getRingEquipped() {
        L1ItemInstance equipeItem[] = new L1ItemInstance[4];
        int equipeCount = 0;
        for (L1ItemInstance item : _items) {
            if (item.getItem().getItemType() == L1ItemType.ARMOR && item.getItem().getType() == L1ItemArmorType.RING.getId() && item.isEquipped()) {
                equipeItem[equipeCount] = item;
                equipeCount++;
                if (equipeCount == 4) {
                    break;
                }
            }
        }
        return equipeItem;
    }

    public void takeoffEquip(int polyid) {
        takeoffWeapon(polyid);
        takeoffArmor(polyid);
    }

    private void takeoffWeapon(int polyid) {
        if (_owner.getWeapon() == null) {
        	return;
        }
        boolean takeoff = false;
        int weapon_type = _owner.getWeapon().getItem().getType();
        takeoff = !L1PolyMorph.isEquipableWeapon(polyid, weapon_type);
        if (takeoff) {
        	setEquipped(_owner.getWeapon(), false, false, false);
        }
    }

    private void takeoffArmor(int polyid) {
        L1ItemInstance armor = null;
        for (int type = 0; type <= 12; type++) {
            if (getTypeEquipped(L1ItemType.ARMOR, type) != 0 && !L1PolyMorph.isEquipableArmor(polyid, type)) {
                if (type == 9) {
                    armor = getItemEquipped(L1ItemType.ARMOR, type);
                    if (armor != null) {
                        setEquipped(armor, false, false, false);
                    }
                    armor = getItemEquipped(L1ItemType.ARMOR, type);
                    if (armor != null) {
                        setEquipped(armor, false, false, false);
                    }
                } else {
                    armor = getItemEquipped(L1ItemType.ARMOR, type);
                    if (armor != null) {
                        setEquipped(armor, false, false, false);
                    }
                }
            }
        }
    }
    
    public boolean checkItem(L1ItemInstance item){
    	return _items.contains(item);
    }
    
    public void searchArrow() {
    	if (_arrow != null 
    			&& (_arrow.getItem().getItemId() == 30088 || _arrow.getItem().getItemId() == 32088)
    			&& (!_owner.getMap().isBeginZone() || _owner.getLevel() > 79)) {
    		_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ON_OFF, _arrow.getId()), true);
    		_arrow = null;
    	}
    	
        if (_arrow != null && _arrow.getCount() > 0 && checkItem(_arrow)) {
        	return;
        }
        setArrow(null);
        L1ItemInstance item = null;
        if (_owner.getLevel() < 80 && _owner.getMap().isBeginZone()) {
        	item = findItemId(30088);
        	if (item == null) {
        		item = findItemId(32088);
        	}
        }
        if (item == null) {
        	for (L1ItemInstance search : _items) {
        		if (search.getItem().getItemType() != L1ItemType.NORMAL) {
        			continue;
        		}
        		if (search.getItem().getType() == L1ItemNormalType.ARROW.getId()) {
        			item = search;
        			break;
        		}
        	}
        }
        if (item != null) {
        	setArrow(item); 
        }
    }

    public void searchSting(){
        if (_sting != null && _sting.getCount() > 0 && checkItem(_sting)) {
        	return;
        }
        setSting(null);
        L1ItemInstance item = null;
        for (L1ItemInstance search : _items) {
    		if (search.getItem().getItemType() != L1ItemType.NORMAL) {
    			continue;
    		}
    		if (search.getItem().getType() == L1ItemNormalType.STING.getId()) {
    			item = search;
    			break;
    		}
    	}
        if (item != null) {
        	setSting(item); 
        }
    }

    public L1ItemInstance getArrow() {
    	return _arrow;
    }
    public void setArrow(L1ItemInstance item) {
        if (_arrow != null && item != null && _arrow.equals(item)) {
        	return;
        }
        if (_arrow != null) {
        	_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ON_OFF, _arrow.getId()), true);
        }
        _arrow = item;
        if (_arrow != null) {
        	_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ON_OFF, _arrow.getId()), true);
        }
    }

    public L1ItemInstance getSting() {
        return _sting;
    }
    public void setSting(L1ItemInstance item) {
        if (_sting != null && item != null && _sting.equals(item)) {
        	return;
        }
        if (_sting != null) {
        	_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ON_OFF, _sting.getId()), true);
        }
        _sting = item;
        if (_sting != null) {
        	_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ON_OFF, _sting.getId()), true);
        }
    }
    
	public L1ItemInstance getDeathPenaltyItem(boolean dropWeaponArmor) {
		L1ItemInstance penaltyItem = null;
		for (int i = 0; i < 100; ++i) {
			penaltyItem = _items.get(CommonUtil.random(_items.size()));// 인벤토리내 아이템 선택
	        if (!penaltyItem.getItem().isTradable() || !penaltyItem.getItem().isRetrieve() || !penaltyItem.getItem().isSpecialRetrieve()
	                || penaltyItem.getEndTime() != null || PenaltyItemTable.isPassItem(penaltyItem.getItemId()) 
	                || (dropWeaponArmor && penaltyItem.getItem().getItemType() == L1ItemType.NORMAL)) {
	        	penaltyItem = null;
				continue;
	        }

			boolean droppable = true;
			if (_owner.getPet() != null && _owner.getPet().getAmuletId() == penaltyItem.getId()) {
				droppable = false;
			}
			if (!droppable) {
				penaltyItem = null;
				continue;
			}
			if (_owner.getDoll() != null && penaltyItem.getId() == _owner.getDoll().getItemObjId()) {
				droppable = false;
			}
			if (!droppable) {
				penaltyItem = null;
				continue;
			}
			break;
		}
		if (penaltyItem == null) {
			return null;
		}
		if (penaltyItem.isEquipped()) {
			setEquipped(penaltyItem, false);// 착용해재
		}
		return penaltyItem;
	}

    /** 조우의 돌골렘 (인챈트 아이템 삭제)
     * @param itemid 	- 제련시 필요한 무기번호
     * @param enchantLevel 	- 제련시 필요한 무기의 인챈트레벨
     */
    public boolean MakeDeleteEnchant(int itemid, int enchantLevel) {
        L1ItemInstance[] items = findItemsId(itemid);
        for (L1ItemInstance item : items) {
            if (item.getEnchantLevel() == enchantLevel) {
                removeItem(item, 1);
                return true;
            }
        }
        return false;
    }

    /** 조우의 돌골렘 (인챈트 아이템 검사)
     * @param id - 제련시 필요한 무기번호
     * 	  
     * @param enchantLevel - 제련시 필요한 무기의 인챈트 레벨
     *	 
     */
    public boolean MakeCheckEnchant(int id, int enchantLevel) {
        L1ItemInstance[] items = findItemsId(id);
        for (L1ItemInstance item : items) {
            if (item.getEnchantLevel() == enchantLevel && item.getCount() == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean checkEnchant(int id, int enchant) {
        for (L1ItemInstance item : _items) {
            if (item.getItem().getItemId() == id && item.getEnchantLevel() == enchant) {
                return true;
            }
        }
        return false;
    }

    public boolean DeleteEnchant(int id, int enchant) {
        for (L1ItemInstance item : _items) {
            if (item.getItem().getItemId() == id && item.getEnchantLevel() == enchant) {
                removeItem(item, 1);
                return true;
            }
        }
        return false;
    }
    
	public L1ItemInstance findItemObjId(int id) {
		for (L1ItemInstance item : this._items) {
			if (item == null) {
				continue;
			}
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}
	public boolean checkEQ(int objid){
		for (L1ItemInstance item : _items) {
			if (item.getId() == objid) {
				return true;
			}
		}
		return false;
	}
	
	public boolean CheckSellPrivateShopItem(int id, int enchantLevel, int attr, int bless) {
		L1ItemInstance item = findItemId(id);
		if (item != null && !item.isEquipped() && item.getEnchantLevel() == enchantLevel && item.getAttrEnchantLevel() == attr && item.getBless() == bless) {
			return true;
		}
		return false;
	}
	
	/** 수량성 템 복사 방지 **/
	public void CheckCloneItem(L1PcInstance pc) {
		int count = 0;
		HashMap<Integer, L1ItemInstance> _stackableItems = new HashMap<Integer, L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item.getItem().isMerge()) {
				_stackableItems.put(count, item);
				count++;
			}
		}
		for (L1ItemInstance item : _items) {
			if (item.getItem().isMerge()) {
				for (int i = 0; i < _stackableItems.size(); i++) {
					if (item.getItem().getItemId() == _stackableItems.get(i).getItem().getItemId()) {
						if (item.getId() != _stackableItems.get(i).getId()) {
							removeItem(item);
							break;
						} else {
							break;
						}
					}
				}
			}
		}
		_stackableItems.clear();
	}
	
	public boolean checkEquippedType(L1ItemType itemType, int type) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemType() == itemType && item.getItem().getType() == type && item.isEquipped()) {
				return true;
			}
		}
		return false;
	}
	
	public int checkItemCount(int id) {
        int cnt = 0;
        for (L1ItemInstance item : _items) {
            if (item.getItemId() == id) {
                cnt += item.getCount();
            }
        }
        return cnt;
    }
	
	public boolean createItem(int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item == null) {
			return false;
		}
		item.setCount(count);
		if (checkAddItem(item, count) == L1Inventory.OK) {
			storeItem(item);
			_owner.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
			if (item.getEndTime() != null) {
				updateItem(item, L1PcInventory.COL_REMAINING_TIME);
				saveItem(item, L1PcInventory.COL_REMAINING_TIME);
			}
		} else {
			_owner.sendPackets(L1ServerMessage.sm82);	// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return false;
		}
		return true;
	}
	
	public boolean createItem(String desc, int item_id, int count, int enchant) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item == null) {
			return false;
		}
		item.setCount(count);
		item.setEnchantLevel(enchant);
		if (checkAddItem(item, count) == L1Inventory.OK) {
			storeItem(item);
		} else {
			L1World.getInstance().getInventory(_owner.getX(), _owner.getY(), _owner.getMapId()).storeItem(item);
		}
		_owner.sendPackets(new S_ServerMessage(143, desc, item.getLogNameRef()), true);
		return true;
	}
	
	/**
	 * 아이템 능력치 처리
	 * @param item
	 * @param flag
	 */
	void doItemAblity(L1ItemInstance item, boolean flag) {
		int val = flag ? 1 : -1;
				
		int acBonus = item.getAcBonus();
		if (acBonus != 0) {
			_owner.ac.addAc(acBonus * val);
		}
		int shortHit = item.getShortHit();
		if (shortHit != 0) {
			_owner.ability.addShortHitup(shortHit * val);
		}
		int longHit	= item.getLongHit();
		if (longHit != 0) {
			_owner.ability.addLongHitup(longHit * val);
		}
		int shortDmg = item.getShortDamage();
		if (shortDmg != 0) {
			_owner.ability.addShortDmgup(shortDmg * val);
		}
		int longDmg = item.getLongDamage();
		if (longDmg != 0) {
			_owner.ability.addLongDmgup(longDmg * val);
		}
		int maxHp = item.getMaxHp();
		if (maxHp != 0) {
			_owner.addMaxHp(maxHp * val);
		}
		int maxMp = item.getMaxMp();
		if (maxMp != 0) {
			_owner.addMaxMp(maxMp * val);
		}
		
		byte addStr = item.getStr();
		if (addStr != 0) {
			_owner.ability.addAddedStr(addStr * val);
		}
		byte addCon = item.getCon();
		if (addCon != 0) {
			_owner.ability.addAddedCon(addCon * val);
		}
		byte addDex = item.getDex();
		if (addDex != 0) {
			_owner.ability.addAddedDex(addDex * val);
		}
		byte addInt = item.getInt();
		if (addInt != 0) {
			_owner.ability.addAddedInt(addInt * val);
		}
		byte addWis = item.getWis();
		if (addWis != 0) {
			_owner.ability.addAddedWis(addWis * val);
			_owner.resetBaseMr();
		}
		byte addCha = item.getCha();
		if (addCha != 0) {
			_owner.ability.addAddedCha(addCha * val);
		}
		int addMr = item.getMagicRegist();
		if (item.getItemId() == 20236 && _owner.isElf()) {
			addMr += 5;
		}
		if (addMr != 0) {
			_owner.resistance.addMr(addMr * val);
		}
		int addSpellpower = item.getSpellpower();
		if (addSpellpower != 0) {
			_owner.ability.addSp(addSpellpower * val);
		}
		int carryBonus = item.getCarryBonus();
		if (carryBonus != 0) {
			_owner.addCarryBonus(carryBonus * val);
		}
		int addHpRegen = item.getHpRegen();
		if (addHpRegen != 0) {
			_owner.addHpRegen(addHpRegen * val);
		}
		int addMpRegen = item.getMpRegen();
		if (addMpRegen != 0) {
			_owner.addMpRegen(addMpRegen * val);
		}
		int damageReduction = item.getDamageReduction();
		if (damageReduction != 0) {
			_owner.ability.addDamageReduction(damageReduction * val);
		}
		int magicDamageReduction = item.getMagicDamageReduction();
		if (magicDamageReduction != 0) {
			_owner.ability.addMagicDamageReduction(magicDamageReduction * val);
		}
		int PVPDamage = item.getPVPDamage();
		if (PVPDamage != 0) {
			_owner.ability.addPVPDamage(PVPDamage * val);
		}
		int PVPDamageReduction = item.getPVPDamageReduction();
		if (PVPDamageReduction != 0) {
			_owner.ability.addPVPDamageReduction(PVPDamageReduction * val);
		}
		int PVPDamageReductionPercent = item.getPVPDamageReductionPercent();
		if (PVPDamageReductionPercent != 0) {
			_owner.ability.addPVPDamageReductionPercent(PVPDamageReductionPercent * val);
		}
		int PVPMagicDamageReduction = item.getPVPMagicDamageReduction();
		if (PVPMagicDamageReduction != 0) {
			_owner.ability.addPVPMagicDamageReduction(PVPMagicDamageReduction * val);
		}
		int PVPDamageReductionEgnor = item.getPVPDamageReductionEgnor();
		if (PVPDamageReductionEgnor != 0) {
			_owner.ability.addPVPDamageReductionEgnor(PVPDamageReductionEgnor * val);
		}
		int PVPMagicDamageReductionEgnor = item.getPVPMagicDamageReductionEgnor();
		if (PVPMagicDamageReductionEgnor != 0) {
			_owner.ability.addPVPMagicDamageReductionEgnor(PVPMagicDamageReductionEgnor * val);
		}
		int abnormalStatusDamageReduction = item.getAbnormalStatusDamageReduction();
		if (abnormalStatusDamageReduction != 0) {
			_owner.ability.addAbnormalStatusDamageReduction(abnormalStatusDamageReduction * val);
		}
		int abnormalStatusPVPDamageReduction = item.getAbnormalStatusPVPDamageReduction();
		if (abnormalStatusPVPDamageReduction != 0) {
			_owner.ability.addAbnormalStatusPVPDamageReduction(abnormalStatusPVPDamageReduction * val);
		}
		int PVPDamagePercent = item.getPVPDamagePercent();
		if (PVPDamagePercent != 0) {
			_owner.ability.addPVPDamagePercent(PVPDamagePercent * val);
		}
		int damageReductionPercent = item.getDamageReductionPercent();
		if (damageReductionPercent != 0) {
			_owner.ability.addDamageReductionPercent(damageReductionPercent * val);
		}
		int shortCritical = item.getShortCritical();
		if (shortCritical != 0) {
			if (item.getItemId() == 22208 || item.getItemId() == 22209) {// 발라카스 완력, 예지력
				_owner.ability.addValakasShortCritical(shortCritical * val);
			} else {
				_owner.ability.addShortCritical(shortCritical * val);
			}
		}
		int longCritical = item.getLongCritical();
		if (longCritical != 0) {
			if (item.getItemId() == 22210) {// 발라카스 인내력
				_owner.ability.addValakasLongCritical(longCritical * val);
			} else {
				_owner.ability.addLongCritical(longCritical * val);
			}
		}
		int magicCritical = item.getMagicCritical();
		if (magicCritical != 0) {
			if (item.getItemId() == 22211) {// 발라카스 마력
				_owner.ability.addValakasMagicCritical(magicCritical * val);
			} else {
				_owner.ability.addMagicCritical(magicCritical * val);
			}
		}
		
		int titanUp = item.getTitanUp();
		if (titanUp != 0) {
			_owner.ability.addTitanUp(titanUp * val);
		}
		int damageReductionEgnor = item.getDamageReductionEgnor();
		if (damageReductionEgnor != 0) {
			_owner.ability.addDamageReductionEgnor(damageReductionEgnor * val);
		}
		int emunEgnor = item.getEmunEgnor();
		if (emunEgnor != 0) {
			_owner.ability.addEmunEgnor(emunEgnor * val);
		}
		int stunDuration = item.getStunDuration();
		if (stunDuration != 0) {
			_owner.ability.addStunDuration(stunDuration * val);
		}
		int magicDamage = item.getMagicDamage();
		if (magicDamage != 0) {
			_owner.ability.addMagicDmgup(magicDamage * val);
		}
		int magicCriticalDamageAdd = item.getMagicCriticalDamageAdd();
		if (magicCriticalDamageAdd != 0) {
			_owner.ability.addMagicCriticalDmgAdd(magicCriticalDamageAdd * val);
		}
		int fowSlayerDamage = item.getFowslayerDamage();
		if (fowSlayerDamage != 0) {
			_owner.ability.addFowSlayerDamage(fowSlayerDamage * val);
		}
		/*int vanguardTime = item.getVanguardTime();
		if (vanguardTime != 0) {
			_owner.ability.addVanguardDecrease(vanguardTime * val);
		}*/
		int tripleArrowStun = item.getTripleArrowStun();
		if (tripleArrowStun != 0) {
			_owner.ability.addTripleArrowStun(tripleArrowStun * val);
		}
		int reflectEmasculate = item.getReflectEmasculate();
		if (reflectEmasculate != 0) {
			_owner.ability.addReflectEmasculate(reflectEmasculate * val);
		}
		int strangeTimeIncrease = item.getStrangeTimeIncrease();
		if (strangeTimeIncrease != 0) {
			_owner.ability.addStrangeTimeIncrease(strangeTimeIncrease * val);
		}
		int strangeTimeDecrease = item.getStrangeTimeDecrease();
		if (strangeTimeDecrease != 0) {
			_owner.ability.addStrangeTimeDecrease(strangeTimeDecrease * val);
		}
		int returnLockDuraion = item.getReturnLockDuraion();
		if (returnLockDuraion != 0) {
			_owner.ability.addReturnLockDuraion(returnLockDuraion * val);
		}
		int restExpReduce = item.getRestExpReduceEfficiency();
		if (restExpReduce != 0) {
			_owner.add_rest_exp_reduce_efficiency(restExpReduce * val);
		}
		int exp_bonus = item.getExpBonus();
		if (exp_bonus != 0) {
			_owner.add_exp_boosting_ratio(exp_bonus * val);
		}
		int base_hp_rate = item.getBaseHpRate();
		if (base_hp_rate != 0) {
			if (flag) {
				double rate = (double) base_hp_rate * 0.01;
				_owner.ability.setBaseHpRate((int)(_owner.getBaseMaxHp() * rate));
				_owner.addMaxHp(_owner.ability.getBaseHpRate());
			} else {
				_owner.addMaxHp(-_owner.ability.getBaseHpRate());
				_owner.ability.setBaseHpRate(0);
			}
		}
		int base_mp_rate = item.getBaseMpRate();
		if (base_mp_rate != 0) {
			if (flag) {
				double rate = (double) base_mp_rate * 0.01;
				_owner.ability.setBaseMpRate((int)(_owner.getBaseMaxMp() * rate));
				_owner.addMaxMp(_owner.ability.getBaseMpRate());
			} else {
				_owner.addMaxMp(-_owner.ability.getBaseMpRate());
				_owner.ability.setBaseMpRate(0);
			}
		}	
		
		int evasion = item.getEvasion();
		if (evasion != 0) {
			_owner.ability.addDg(evasion * val);
		}
		int evasion_regist = item.getEvasionRegist();
		if (evasion_regist != 0) {
			_owner.ability.addEr(evasion_regist * val);
		}
		int magic_evasion = item.getMagicEvasion();
		if (magic_evasion != 0) {
			_owner.ability.addMe(magic_evasion * val);
		}
		int poison_regist = item.getPoisonRegist();
		if (poison_regist != 0) {
			_owner.ability.add_poison_regist(poison_regist * val);
		}
		
		int attrEarth = item.getAttrEarth();
		if (attrEarth != 0) {
			_owner.resistance.addEarth(attrEarth * val);
		}
		int attrWind = item.getAttrWind();
		if (attrWind != 0) {
			_owner.resistance.addWind(attrWind * val);
		}
		int attrWater = item.getAttrWater();
		if (attrWater != 0) {
			_owner.resistance.addWater(attrWater * val);
		}
		int attrFire = item.getAttrFire();
		if (attrFire != 0) {
			_owner.resistance.addFire(attrFire * val);
		}
		int registStone = item.getRegistStone();
		if (registStone != 0) {
			_owner.resistance.addPetrifaction(registStone * val);
		}
		int registSleep = item.getRegistSleep();
		if (registSleep != 0) {
			_owner.resistance.addSleep(registSleep * val);
		}
		int registFreeze = item.getRegistFreeze();
		if (registFreeze != 0) {
			_owner.resistance.addFreeze(registFreeze * val);
		}
		int toleranceSkill = item.getToleranceSkill();
		if (toleranceSkill != 0) {
			_owner.resistance.addToleranceSkill(toleranceSkill * val);
		}
		int toleranceSpirit = item.getToleranceSpirit();
		if (toleranceSpirit != 0) {
			_owner.resistance.addToleranceSpirit(toleranceSpirit * val);
		}
		int toleranceDragon = item.getToleranceDragon();
		if (toleranceDragon != 0) {
			_owner.resistance.addToleranceDragon(toleranceDragon * val);
		}
		int toleranceFear = item.getToleranceFear();
		if (toleranceFear != 0) {
			_owner.resistance.addToleranceFear(toleranceFear * val);
		}
		int toleranceAll = item.getToleranceAll();
		if (toleranceAll != 0) {
			_owner.resistance.addToleranceAll(toleranceAll * val);
		}
		int hitupSkill = item.getHitupSkill();
		if (hitupSkill != 0) {
			_owner.resistance.addHitupSkill(hitupSkill * val);
		}
		int hitupSpirit = item.getHitupSpirit();
		if (hitupSpirit != 0) {
			_owner.resistance.addHitupSpirit(hitupSpirit * val);
		}
		int hitupDragon = item.getHitupDragon();
		if (hitupDragon != 0) {
			_owner.resistance.addHitupDragon(hitupDragon * val);
		}
		int hitupFear = item.getHitupFear();
		if (hitupFear != 0) {
			_owner.resistance.addHitupFear(hitupFear * val);
		}
		int hitupAll = item.getHitupAll();
		if (hitupAll != 0) {
			_owner.resistance.addHitupAll(hitupAll * val);
		}
		int magicHit = item.getMagicHit();
		if (magicHit != 0) {
			_owner.ability.addMagicHitup(magicHit * val);
		}
		int attrAll = item.getAttrAll();
		if (attrAll != 0) {
			_owner.resistance.addAllNaturalResistance(attrAll * val);
		}
		int potionRegist = item.getPotionRegist();
		if (potionRegist != 0) {
			_owner.ability.addItemPotionRegist(potionRegist * val);
		}
		int potionPercent = item.getPotionPercent();
		if (potionPercent != 0) {
			_owner.ability.addItemPotionPercent(potionPercent * val);
		}
		int potionValue = item.getPotionValue();
		if (potionValue != 0) {
			_owner.ability.addItemPotionValue(potionValue * val);
		}
		int elixerBooster = item.getElixerBooster();
		if (elixerBooster != 0) {
			_owner._isElixerBooster += elixerBooster * val;
		}
		int hprAbsol32Second = item.getHprAbsol32Second();
		if (hprAbsol32Second > 0) {
			_owner.addHpRegen32SecondByItemValue(hprAbsol32Second * val);
		}
		int mprAbsol64Second = item.getMprAbsol64Second();
		if (mprAbsol64Second > 0) {
			_owner.addMpRegen64SecondByItemValue(mprAbsol64Second * val);
		}
		int mprAbsol16Second = item.getMprAbsol16Second();
		if (mprAbsol16Second > 0) {
			_owner.addMpRegen16SecondByItemValue(mprAbsol16Second * val);
		}
		int hpPotionDelayDecrease = item.getHpPotionDelayDecrease();
		if (hpPotionDelayDecrease > 0) {
			_owner.ability.addHpPotionDelayDecrease(hpPotionDelayDecrease * val);
		}
		int hpPotionCriticalProb = item.getHpPotionCriticalProb();
		if (hpPotionCriticalProb > 0) {
			_owner.ability.addHpPotionCriticalProb(hpPotionCriticalProb * val);
		}
		int increaseArmorSkillProb = item.getIncreaseArmorSkillProb();
		if (increaseArmorSkillProb > 0) {
			_owner.ability.addIncreaseArmorSkillProb(increaseArmorSkillProb * val);
		}
		int attackSpeedDelayRate = item.getAttackSpeedDelayRate();
		if (attackSpeedDelayRate > 0) {
			_owner.addAttackSpeedDelayRate(attackSpeedDelayRate * val);
		}
		int moveSpeedDelayRate = item.getMoveSpeedDelayRate();
		if (moveSpeedDelayRate > 0) {
			_owner.addMoveSpeedDelayRate(moveSpeedDelayRate * val);
		}
		if (item.getItem().isHasteItem()) {
			haste(flag);
		}
		if (item.getThirdSpeed() != 0) {
			drunken(item, flag);
		}
		if (item.getFourthSpeed() != 0) {
			fourthGear(flag);
		}
		if (item.isSlot()) {
			slotOption(item, flag);
		}
	}
	
	/** 아이템의 능력치를 반영한다 **/
	public void setItemAblity(L1ItemInstance item){
		if (item.getItemId() == 20383 && _owner.skillStatus.hasSkillEffect(L1SkillId.STATUS_BRAVE)) {
			_owner.skillStatus.killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
			_owner.broadcastPacketWithMe(new S_SkillBrave(_owner.getId(), 0, 0), true);
			_owner.moveState.setBraveSpeed(0);
		}
		doItemAblity(item, true);
		ablityRefresh();
	}
	
	/** 아이템의 능력치를 제거한다 **/
	public void removeItemAblity(L1ItemInstance item){
		doItemAblity(item, false);
		ablityRefresh();
	}
	
	/**
	 * 제련석 능력치
	 * @param item
	 * @param flag
	 */
	L1ItemInstance slotDummy;
	void slotOption(L1ItemInstance item, boolean flag) {
		if (slotDummy == null) {
			slotDummy = new L1ItemInstance();
			slotDummy._owner = _owner;
		}
		for (L1Item slot : item.getSlots().values()) {
			if (slot == null) {
				continue;
			}
			slotDummy.setItem(slot);
			slotDummy.updateItemAbility(_owner);
			doItemAblity(slotDummy, flag);
		}
	}
	
	void ablityRefresh(){
		_owner.sendPackets(new S_OwnCharAttrDef(_owner), true);
		_owner.sendPackets(new S_OwnCharStatus(_owner), true);
	}
	
	/**
	 * 1단 가속
	 * @param active
	 */
	void haste(boolean active){
		if (active) {
			_owner.addHasteItemEquipped(1);
			_owner.removeSpeedSkill();
			if (_owner.moveState.getMoveSpeed() != 1) {
				_owner.moveState.setMoveSpeed(1);
				_owner.sendPackets(new S_SkillHaste(_owner.getId(), 1, -1), true);
				_owner.broadcastPacket(new S_SkillHaste(_owner.getId(), 1, 0), true);
			}
		} else {
			_owner.addHasteItemEquipped(-1);
			if (_owner.getHasteItemEquipped() == 0) {
				_owner.moveState.setMoveSpeed(0);
				_owner.broadcastPacketWithMe(new S_SkillHaste(_owner.getId(), 0, 0), true);
			}
		}
	}
	
	/**
	 * 3단 가속
	 * @param item
	 * @param active
	 */
	void drunken(L1ItemInstance item, boolean active){
		if (active) {
			_owner.addDrunkenItemEquipped(1);
			if (_owner.skillStatus.hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)) {
				_owner.skillStatus.killSkillEffectTimer(L1SkillId.STATUS_DRAGON_PEARL);
				_owner.sendPackets(new S_Liquor(_owner.getId(), 0), true);
				_owner.sendPackets(S_PacketBox.DRAGON_PEARL_OFF);
				_owner.moveState.setDrunken(0);
			}
			if (_owner.moveState.getDrunken() != 8) {
				_owner.broadcastPacketWithMe(new S_Liquor(_owner.getId(), 8), true);
				_owner.sendPackets(S_PacketBox.DRAGON_PEARL_ON);
				_owner.moveState.setDrunken(8);
			}
		} else {
			_owner.addDrunkenItemEquipped(-1);
			if (_owner.getDrunkenItemEquipped() == 0) {
				_owner.broadcastPacketWithMe(new S_Liquor(_owner.getId(), 0), true);
				_owner.sendPackets(S_PacketBox.DRAGON_PEARL_OFF);
				_owner.moveState.setDrunken(0);
			}
		}
		
		if (L1ItemId.isAdenSpeedGraceItem(item.getItemId())) {// 아덴의 신속 가호
			_owner.sendPackets(active ? S_SpellBuffNoti.ADEN_FAST_GRACE_ON : S_SpellBuffNoti.ADEN_FAST_GRACE_OFF);
		}
	}
	
	/**
	 * 4단 가속
	 * @param active
	 */
	void fourthGear(boolean active){
		if (active) {
			_owner.addFourthItemEquipped(1);
			if (!_owner.getMoveState().isFourthGear()) {
				_owner.moveState.setFourthGear(true);
				_owner.broadcastPacketWithMe(new S_FourthGear(_owner.getId(), true), true);
			}
		} else {
			_owner.addFourthItemEquipped(-1);
			if (_owner.getFourthItemEquipped() == 0) {
				_owner.moveState.setFourthGear(false);
				_owner.broadcastPacketWithMe(new S_FourthGear(_owner.getId(), false), true);
			}
		}
	}
}

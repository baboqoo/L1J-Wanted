package l1j.server.server.clientpackets.proto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.Config;
import l1j.server.GameSystem.craft.CraftInfoLoader;
import l1j.server.GameSystem.craft.CraftSuccessCountLoader;
import l1j.server.GameSystem.craft.bean.L1CraftInfo;
import l1j.server.common.bin.craft.Craft;
import l1j.server.common.bin.craft.CraftAttr;
import l1j.server.common.bin.craft.CraftEvent;
import l1j.server.common.bin.craft.CraftInputSlot;
import l1j.server.common.bin.craft.CraftOutputItem;
import l1j.server.common.bin.craft.CraftOutputItemResult;
import l1j.server.common.bin.craft.CraftOutputList;
import l1j.server.common.bin.craft.PeriodList;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.ItemMentTable;
import l1j.server.server.datatables.ItemMentTable.ItemMentType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.craft.S_CraftMake;
import l1j.server.server.serverpackets.craft.S_CraftMake.eCraftMakeReqResultType;
import l1j.server.server.serverpackets.message.S_MessegeNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
//import manager.Manager;  // MANAGER DISABLED

public class A_CraftMake extends ProtoHandler {
	protected A_CraftMake(){}
	private A_CraftMake(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _npc_id;
	private int _craft_id;
	private int _count;
	
	private L1PcInventory _inv;
	private L1CraftInfo _db_info;
	private Craft _bin;
	private CraftAttr _craft_attr;
	private CraftOutputList _outputList;
	private CraftOutputItem _output;
	private HashMap<Integer, CraftInputSlot> _input_slot_list;
	private L1ItemInstance _result_item;
	
	private ItemTable _itb;
	private HashMap<L1ItemInstance, Integer> _material_list;
	
	private S_MessegeNoti world_message;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		_inv = _pc.getInventory();
		if (_inv.getWeightPercent() >= 100) {
			_pc.sendPackets(S_CraftMake.RP_ERROR_WEIGHT_OVER);
			return;
		}
		if (_inv.getSize() >= L1PcInventory.MAX_SIZE) {
			_pc.sendPackets(S_CraftMake.RP_ERROR_INVEN_OVER);
			return;
		}
		
		S_CraftMake failPck = readDefault();
		if (failPck != null) {
			_pc.sendPackets(failPck);
			return;
		}
		if (!readInputSlot()) {
			_pc.sendPackets(S_CraftMake.RP_ERROR_INVALID_INPUT);
			return;
		}
		if (_input_slot_list.isEmpty()) {
			_pc.sendPackets(S_CraftMake.RP_ERROR_INVALID_INPUT);
			craftErrMsg("INPUT_SLOT_EMPTY");
			return;
		}
		
		// TODO 슬롯 검증
		int increase_prob = _bin.get_inputs().validationAndProb(_input_slot_list);
		if (increase_prob == -1) {
			_pc.sendPackets(S_CraftMake.RP_ERROR_INVALID_INPUT);
			craftErrMsg("INPUTS_AND_SLOTS_VALIDATION_FAILURE");
			return;
		}
		
		_itb	= ItemTable.getInstance();
		
		// 제작 결과 리스트
		_outputList = _bin.get_outputs();
		_material_list = new HashMap<L1ItemInstance, Integer>();// 아이템,삭제할수
		int batch_delay_sec = _bin.get_batch_delay_sec();
		int success_count = 0, failure_count = 0;
		S_CraftMake success_make_pck = null, failure_make_pck = null;
		for (int i=0; i<_count; i++) {
			CraftOutputItemResult outputResult = _outputList.createOutputItem(increase_prob, _db_info.getProbabilityMillion());
			eCraftMakeReqResultType resultType = doMake(outputResult);
			_material_list.clear();
			
			switch (resultType) {
			case RP_SUCCESS:
				if (batch_delay_sec > 0 || _count == 1) {
					_pc.sendPackets(S_CraftMake.getSuccessResult(_craft_id, _result_item));// 성공 멘트
				} else {
					if (success_make_pck == null) {
						success_make_pck = S_CraftMake.getSuccessResult(_craft_id, _result_item);
					}
				}
				success_count++;
				break;
			case RP_FAILURE:
				if (batch_delay_sec > 0 || _count == 1) {
					_pc.sendPackets(S_CraftMake.RP_FAILURE);// 실패 멘트
				} else {
					if (success_make_pck == null && failure_make_pck == null) {
						failure_make_pck = S_CraftMake.RP_FAILURE;
					}
				}
				failure_count++;
				break;
			case RP_ERROR_INVALID_INPUT:
				_pc.sendPackets(S_CraftMake.RP_ERROR_INVALID_INPUT);
				return;
			case RP_ERROR_WEIGHT_OVER:
				_pc.sendPackets(S_CraftMake.RP_ERROR_WEIGHT_OVER);
				return;
			case RP_ERROR_INVEN_OVER:
				_pc.sendPackets(S_CraftMake.RP_ERROR_INVEN_OVER);
				return;
			default:
				_pc.sendPackets(S_CraftMake.RP_ERROR_CRAFT_DOES_NOT_EXIST);
				return;
			}
		}
		
		// 복수개 제작 시 패킷은 한번에 처리한다.
		if (success_make_pck != null) {
			_pc.sendPackets(success_make_pck);
		} else if (failure_make_pck != null) {
			_pc.sendPackets(failure_make_pck);
		}
		
		if (world_message != null) {
			if (_pc.getConfig().isGlobalMessege()) {
    			_pc.sendPackets(world_message);
    		}
			L1World.getInstance().broadcastPacket(_pc, world_message, true);// 누군가가 %s 아이템 제작에 성공하였습니다.
		}
		
		String log_message = String.format(
				"CRAFT_ID(%d)\tDESC(%s)\tSUCCESS_COUNT(%d)\tFAILURE_COUNT(%d)\tEXCEPTION_COUNT(%d)\tTOTAL_COUNT(%d)",
				_craft_id, _db_info.getDesc(), success_count, failure_count, _count - (success_count + failure_count), _count);
		LoggerInstance.getInstance().addCraft(_pc, log_message);
		_material_list = null;
	}
	
	eCraftMakeReqResultType doMake(CraftOutputItemResult outputResult) {
		// 인벤토리 재료 검증과 제작에 소모될 아이템을 등록한다.
		int materialCheckCount = 0;
		for (CraftInputSlot slotInfo : _input_slot_list.values()) {
			L1Item materialItem	= _itb.findItemByNameId(slotInfo.get_name_id());
			if (materialItem == null) {
				craftErrMsg(String.format("MATERIAL_SERVER_TEMPLATE_NOT_FOUND : NAME_ID(%d)", slotInfo.get_name_id()));
				return eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT;
			}
			int nameId			= slotInfo.get_name_id();
			int count			= slotInfo.get_count();
			int bless			= slotInfo.get_bless();
			int enchant			= slotInfo.get_enchant();
			int attrType		= slotInfo.get_elemental_enchant_type();
			int attrValue		= slotInfo.get_elemental_enchant_value();
			if (materialItem.isMerge()) {// 수량성 아이템
				L1ItemInstance material = _inv.findItemMaterial(nameId, count, bless, enchant);
				if (material == null) {
					craftErrMsg(String.format("INVENTORY_MATERIAL_NOT_FOUND : NAME_ID(%d), COUNT(%d), BLESS(%d), ENCHANT(%d)", nameId, count, bless, enchant));
					return eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT;
				}
				
				// 동일한 아이템 수량 추가
				if (_material_list.containsKey(material)) {
					count = _material_list.get(material) + count;
				}
				_material_list.put(material, count);
				materialCheckCount++;
			} else {// 일반 아이템
				int ckCount = 0;
				for (L1ItemInstance itemIns : _inv.getItems()) {
					if (ckCount >= count) {
						break;
					}
					if (itemIns != null 
							&& !itemIns.isEquipped() 
							&& !itemIns.isEngrave() 
							&& !itemIns.isSlot() 
							&& itemIns.getItem().getItemNameId() == nameId 
							&& itemIns.getEnchantLevel() == enchant) {
						if (_material_list.containsKey(itemIns)) {
							craftErrMsg(String.format("NOT_MERGE_ITEM_CONTAINS : NAME_ID(%d)", nameId));
							return eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT;
						}
						if (attrType <= 0 && itemIns.getAttrEnchantLevel() > 0) {
							continue;
						}
						if (attrType > 0 && !L1ItemInstance.equalsElement(itemIns, attrType, attrValue)) {
							continue;
						}
						_material_list.put(itemIns, 1);
						ckCount++;
					}
				}
				if (ckCount < count) {
					craftErrMsg(String.format("NOT_MERGE_ITEM_COUNT_FAILURE : CHECK(%d) < COUNT(%d)", ckCount, count));
					return eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT;
				}
				materialCheckCount++;
			}
		}
		
		// 재료 체크 검사 결과
		if (_input_slot_list.size() != materialCheckCount) {
			craftErrMsg(String.format("MATERIAL_SIZE_AND_SLOT_SIZE_NOT_EQUALLS : SLOT_SIZE(%d), CHECK_SIZE(%d)", _input_slot_list.size(), materialCheckCount));
			return eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT;
		}
		_output = null;
		if (outputResult.outputItem != null) {
			_output	= outputResult.outputItem;
			if (outputResult.isSuccess) {
				eventOutput();
			}
		}
		
		L1Item ouputItem	= null;
		int outputElemental = 0;
		if (_output != null) {
			ouputItem = _itb.findItemByNameIdAndBless(_output.get_name_id(), _output.get_bless());// 생성할 아이템
			// 축복 아이템을 찾을 수 없다면 기본 아이템으로 찾는다(인스턴스를 축복으로 변경)
			if (ouputItem == null) {
				ouputItem = _itb.findItemByNameId(_output.get_name_id());// 생성할 아이템
			}
			
			// bin의 nameId로 아이템이 발견되지 않는다면 디비에 등록된 nameId로 찾는다.
			if (ouputItem == null && _db_info.getOutputNameId() > 0) {
				ouputItem = _itb.findItemByNameIdAndBless(_db_info.getOutputNameId(), _output.get_bless());// 생성할 아이템
				// 축복 아이템을 찾을 수 없다면 기본 아이템으로 찾는다(인스턴스를 축복으로 변경)
				if (ouputItem == null) {
					ouputItem = _itb.findItemByNameId(_db_info.getOutputNameId());// 생성할 아이템
				}
			}
			if (ouputItem == null) {
				craftErrMsg(String.format("OUTPUT_TEMPLATE_NOT_FOUND : NAMEID(%d), BLESS(%d)", _output.get_name_id(), _output.get_bless()));
				return eCraftMakeReqResultType.RP_ERROR_CRAFT_DOES_NOT_EXIST;
			}
			
			int inv_valid = _inv.checkAddItem(ouputItem, _output.get_count());
			if (inv_valid != L1Inventory.OK) {
				return inv_valid == L1Inventory.WEIGHT_OVER ? eCraftMakeReqResultType.RP_ERROR_WEIGHT_OVER : eCraftMakeReqResultType.RP_ERROR_INVEN_OVER;
			}
			
			// 속성 검증
			if (!outputResult.isSuccess) {
				CraftInputSlot firstSlot = _input_slot_list.get(1);
				if (firstSlot.get_elemental_enchant_type() > 0) {
					outputElemental = L1ItemInstance.calculateElementalEnchant(firstSlot.get_elemental_enchant_type(), firstSlot.get_elemental_enchant_value());
				}
			}
			
			int inheritItemElementalNameId = _output.get_inherit_elemental_enchant_from();
			if (inheritItemElementalNameId == 0
					&& _output.get_elemental_type() > 0
					&& _output.get_elemental_level() > 0) {
				outputElemental = L1ItemInstance.calculateElementalEnchant(_output.get_elemental_type(), _output.get_elemental_level());
			}
			
			if (outputElemental == 0 && inheritItemElementalNameId != 0) {
				for (CraftInputSlot slotInfo : _input_slot_list.values()) {
					if (inheritItemElementalNameId == slotInfo.get_name_id()) {
						outputElemental = L1ItemInstance.calculateElementalEnchant(slotInfo.get_elemental_enchant_type(), slotInfo.get_elemental_enchant_value());
						break;
					}
				}
			}
			
			if (outputElemental == 0 && inheritItemElementalNameId != 0) {
				for (L1ItemInstance item : _material_list.keySet()) {
					if (inheritItemElementalNameId == item.getItem().getItemNameId()) {
						outputElemental = item.getAttrEnchantLevel();
						break;
					}
				}
			}
		}
		
		// 재료 소모
		ArrayList<CraftOutputItem> equalsList				= null;
		ArrayList<Integer> preserveNameIds					= _db_info.getPreserveNameIds();
		HashMap<Integer, Integer> successPreserveCount		= _db_info.getSuccessPreserveCount();
		HashMap<Integer, Integer> failurePreserveCount		= _db_info.getFailurePreserveCount();
		int not_merge_preserve_count						= 0;
		
		for (Map.Entry<L1ItemInstance, Integer> entry : _material_list.entrySet()) {
			L1ItemInstance key	= entry.getKey();
			int value			= entry.getValue();
			
			// 보존 되는 아이템
			if (preserveNameIds != null && preserveNameIds.contains(key.getItem().getItemNameId())) {
				continue;
			}
			
			// 소모시킬 아이템과 생성될 아이템이 같다.
			if (_output != null
					&& _output.get_name_id() == key.getItem().getItemNameId()
					&& _output.get_enchant() == key.getEnchantLevel()
					&& _output.get_bless() == key.getBless()
					&& outputElemental == key.getAttrEnchantLevel()
					&& _output.get_count() <= value) {
				if (equalsList == null) {
					equalsList			= new ArrayList<CraftOutputItem>();
				}
				equalsList.add(_output);
				
				if (outputResult.isSuccess && successPreserveCount != null && successPreserveCount.containsKey(key.getItem().getItemNameId())) {
					int success_preserve_count = successPreserveCount.get(key.getItem().getItemNameId());
					if (key.getItem().isMerge()) {
						value -= success_preserve_count;
					} else {
						if (not_merge_preserve_count < success_preserve_count) {
							not_merge_preserve_count++;
							continue;
						}
					}
				} else if (!outputResult.isSuccess && failurePreserveCount != null && failurePreserveCount.containsKey(key.getItem().getItemNameId())) {
					int failure_preserve_count = failurePreserveCount.get(key.getItem().getItemNameId());
					if (key.getItem().isMerge()) {
						value -= failure_preserve_count;
					} else {
						if (not_merge_preserve_count < failure_preserve_count) {
							not_merge_preserve_count++;
							continue;
						}
					}
				} else {
					// 수량이 같으면 제외시킨다.
					if (_output.get_count() == value) {
						continue;
					}
					// 전체 소모 수량이 크다면 감소 시킨다.
					value -= _output.get_count();
				}
			} else {
				if (outputResult.isSuccess && successPreserveCount != null && successPreserveCount.containsKey(key.getItem().getItemNameId())) {
					int success_preserve_count = successPreserveCount.get(key.getItem().getItemNameId());
					if (key.getItem().isMerge()) {
						value -= success_preserve_count;
					} else {
						if (not_merge_preserve_count < success_preserve_count) {
							not_merge_preserve_count++;
							continue;
						}
					}
				} else if (!outputResult.isSuccess && failurePreserveCount != null && failurePreserveCount.containsKey(key.getItem().getItemNameId())) {
					int failure_preserve_count = failurePreserveCount.get(key.getItem().getItemNameId());
					if (key.getItem().isMerge()) {
						value -= failure_preserve_count;
					} else {
						if (not_merge_preserve_count < failure_preserve_count) {
							not_merge_preserve_count++;
							continue;
						}
					}
				}
			}
			
			// 소모할 수량
			if (value <= 0) {
				continue;
			}
			
			// 재료 삭제
			if (!_inv.consumeItem(key, value)) {// item, count
				craftErrMsg(String.format("MATERIAL_DELETE_FAILE : DB_ID(%d), COUNT(%d)", key.getItemId(), value));
				return eCraftMakeReqResultType.RP_ERROR_CRAFT_DOES_NOT_EXIST;
			}
		}
		
		// 생성할 아이템이 존재
		_result_item = null;
		if (_output != null && (equalsList == null || !equalsList.contains(_output))) {
			// Lucky bag
			boolean isOpenLuckyBag = ouputItem.getItemId() >= 31688 && ouputItem.getItemId() <= 31696 && isOpenLuckyBag(ouputItem.getItemId());
			if (!isOpenLuckyBag) {
				// TODO 제작 결과 아이템
				_result_item = _inv.storeItem(_pc, ouputItem.getItemId(), _output.get_count(), _output.get_enchant(), _output.get_bless(), outputElemental, true);
			}
		}
		
		if (outputResult.isSuccess) {
			int broadcast_desc = _output.get_broadcast_desc();
			if (world_message == null && (broadcast_desc > 0 || ItemMentTable.isMent(ItemMentType.CRAFT, _result_item.getItemId()))) {
				if (broadcast_desc != 3599) {
					broadcast_desc = 3599;
				}
				world_message = new S_MessegeNoti(broadcast_desc, _result_item.getLogNameRef(_output.get_count()), _result_item.getItem().getItemNameId());
	    	}
			
			// 한정 제작
			if (_bin.get_max_successcount() > 0 
					&& _db_info.is_success_count_type()
					&& !CraftSuccessCountLoader.getInstance().upsert(_pc, _craft_id, _bin.get_SuccessCountType(), _output.get_count())) {
				craftErrMsg(String.format("LIMIT_USER_INFO_UPERT_FAILURE : COUNT(%d)", _output.get_count()));
			}
			return eCraftMakeReqResultType.RP_SUCCESS;
		}
		return eCraftMakeReqResultType.RP_FAILURE;
	}
	
	/**
	 * 대성공 처리
	 */
	void eventOutput() {
		CraftEvent event = _outputList.get_success().get_event(_output.get_event_id());
		if (event == null) {
			return;
		}
		CraftOutputItem event_output = event.get_event_output_item();
		if (event_output != null && CommonUtil.random(100) + 1 <= Config.CRAFT.CRAFT_EVENT_SUCCESS_OUTPUT_PROB) {
			_output = event_output;
		}
	}
	
	/**
	 * 제작과 동시에 상자를 오픈한다.
	 * @param luckyBagId
	 * @return boolean
	 */
	boolean isOpenLuckyBag(int luckyBagId) {
		L1TreasureBox box	= L1TreasureBox.get(luckyBagId);
		if (box != null && box.open(_pc)) {
			// TODO 제작 결과 아이템
			_result_item = _pc.getConfig().getLuckyBagOpenResultItem();
			_pc.getConfig().setLuckyBagOpenResultItem(null);
			return true;
		}
		return false;
	}
	
	void craftErrMsg(String msg) {
		//System.out.println(String.format("[A_CraftMake] EXCEPTION : %s\r\nCRAFT_ID : %d / 사용자 : %s", msg, _craft_id, _pc.getName()));
		System.out.println(String.format("[A_CraftMake] EXCEPTION : %s\r\nCRAFT_ID : %d / User : %s", msg, _craft_id, _pc.getName()));
		//Manager.getInstance().CraftInfo(_pc.getName(), msg, _craft_id); // MANAGER DISABLED
		//_pc.sendPackets(new S_SystemMessage(String.format("제작실패 : 번호 : %d : 운영자에게 문의하세요!", _craft_id)), true);
		_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(39), String.valueOf(_craft_id)), true);
	}
	
	S_CraftMake readDefault() {
		int tag = readC();
		if (tag == 0x08) {// 엔피씨제작일때
			_npc_id = readBit();
			L1Object obj = L1World.getInstance().findObject(_npc_id);
			if (obj == null) {
				craftErrMsg("NPC_OBJECT_NOT_FOUND");
				return S_CraftMake.RP_ERROR_INVALID_NPC;
			}
			if (!_pc.knownsObject(obj)) {
				return S_CraftMake.RP_ERROR_NPC_OUT_OF_RANGE;
			}
			readP(1);// 0x10
		}
		_craft_id = readBit();
		if (GameServerSetting.CRAFT_CHECK) {
			//String checkString = String.format("제작아이디 [%d]", _craft_id);
			String checkString = String.format(S_SystemMessage.getRefTextNS(1357) + "%d]", _craft_id);
			System.out.println(checkString);
			_pc.sendPackets(new S_SystemMessage(checkString), true);
		}
		
		if (_craft_id == 0) {
			craftErrMsg("CRAFT_ID_ZERO");
			return S_CraftMake.RP_ERROR_CRAFT_DOES_NOT_EXIST;
		}
		
		if (CraftInfoLoader.isBlock(_craft_id)) {// 특정 제작 차단
			return S_CraftMake.RP_ERROR_BLOCKED_CRAFT_ID;
		}
		
		readP(1);// 0x18
		_count = readBit();
		if (GameServerSetting.CRAFT_CHECK) {
			//String checkString = String.format("만들개수 [%d]", _count);
			String checkString = String.format(S_SystemMessage.getRefTextNS(1358) + "%d]", _count);
			System.out.println(checkString);
			_pc.sendPackets(new S_SystemMessage(checkString), true);
		}
		
		if (_count == 0) {
			craftErrMsg("COUNT_ZERO");
			return S_CraftMake.RP_ERROR_CRAFT_DOES_NOT_EXIST;
		}
		
		// TODO db정보를 불러온다
		_db_info = CraftInfoLoader.getInfo(_craft_id);
		if (_db_info == null) {
			return S_CraftMake.RP_ERROR_CRAFT_DOES_NOT_EXIST;
		}
		
		// TODO bin정보를 불러온다.
		_bin = _db_info.getBin();
		if (_bin == null) {
			return S_CraftMake.RP_ERROR_CRAFT_DOES_NOT_EXIST;
		}
		
		// 제작 상태 검증
		_craft_attr = _bin.get_craft_attr();
		if (_craft_attr != null && !_craft_attr.isValidation(_pc, _count)) {
			return S_CraftMake.RP_ERROR_BAD_QUALIFICATION;
		}
		
		// 제작 시간 검증
		PeriodList _period_list = _bin.get_period_list();
		if (_period_list != null && !_period_list.isValidtion()) {
			return S_CraftMake.RP_ERROR_CRAFT_DOES_NOT_EXIST;
		}
		
		// 한정 제작 검증
		if (_bin.get_max_successcount() > 0 && _db_info.is_success_count_type()) {
			int cur_successcount = CraftSuccessCountLoader.getCurrentCount(_pc, _craft_id, _bin.get_SuccessCountType());
			if (cur_successcount + _count > _bin.get_max_successcount()) {
				return S_CraftMake.RP_ERROR_SUCCESS_COUNT_EXCEED;
			}
		}
		
		return null;
	}
	
	boolean readInputSlot() {
		_input_slot_list = new HashMap<Integer, CraftInputSlot>();
		try {
			while (!isEnd()) {
				int slotInfo = readC();
				if (slotInfo != 0x22) {// slotInfo
					break;
				}
				int slot_size = readBit();
				int cur_offset = 0;
				CraftInputSlot slot = new CraftInputSlot();
				while (cur_offset < slot_size) {
					int r_size = read_size();
					int tok = readBit();
					switch(tok){
					case 0x08:
						r_size += read_size();
						slot.set_slot_id(readBit());
						break;
					case 0x10:
						r_size += read_size();
						slot.set_name_id(readBit());
						break;
					case 0x18:
						r_size += read_size();
						slot.set_count(readBit());
						break;
					case 0x20:
						r_size += read_size();
						slot.set_enchant(readBit());
						break;
					case 0x28:
						r_size += read_size();
						slot.set_elemental_enchant_type(readBit());
						break;
					case 0x30:
						r_size += read_size();
						slot.set_elemental_enchant_value(readBit());
						break;
					case 0x38:
						r_size += read_size();
						slot.set_bless(readBit());
						break;
					}
					cur_offset += r_size;
				}
				_input_slot_list.put(slot.get_slot_id(), slot);
				if (GameServerSetting.CRAFT_CHECK) {
					System.out.println(slot.toString());
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CraftMake(data, client);
	}

}



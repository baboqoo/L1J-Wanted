package l1j.server.server.clientpackets.proto;

import java.util.ArrayList;
import java.util.LinkedList;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.monitor.Logger.SmeltingMakeType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.smelting.S_SmeltingMake;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class A_SmeltingMake extends ProtoHandler {
	protected A_SmeltingMake(){}
	private A_SmeltingMake(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int alchemy_id;
	private LinkedList<InputSlot> inputs;
	private L1Item output;
	private InputSlot fail_output;
	
	void parse() {
		readP(1);// 0x08
		alchemy_id = readC();// 1, 2, 3, 4, 5, 6, 7
		
		L1ItemInstance item = null;
		int cur_off = 0;
		while (cur_off < _total_length - 2) {
			int tag = readC();
			cur_off++;
			
			if (tag == 0x12) {
				readP(1);// slot length
				cur_off++;
				
				readP(1);// 0x08
				int slot_no = readC();
				cur_off += 2;
				
				readP(1);// 0x10
				int name_id_size = read_size();
				int item_name_id = read4(name_id_size);
				cur_off += name_id_size + 1;
				
				readP(1);// 0x18
				int item_id_size = read_size();
				int item_id = read4(item_id_size);
				cur_off += item_id_size + 1;
				
				item = _pc.getInventory().getItem(item_id);
				if (item == null) {
					return;
				}
				if (item.getItem().get_interaction_type() != L1ItemType.SMELTING.getInteractionType()) {
					System.out.println(String.format(
							"[A_SmeltingMake] NOT_SMELTING_STONE_INPUT_TRY : ALCHEMY_ID(%d), ITEM_NAME_ID(%d), CHAR_NAME(%s)",
							alchemy_id, item.getItem().getItemNameId(), _pc.getName()));
					return;
				}
				
				if (inputs == null) {
					inputs = new LinkedList<InputSlot>();
				}
				InputSlot input = new InputSlot(slot_no, item_name_id, item_id, item);
				inputs.add(input);
				
				// 2 ~ 4단계 실패시 생성할 제련석 선택
				if (alchemy_id >= 2 && alchemy_id <= 4 && fail_output == null && alchemy_id == item.getItem().getEtcValue()) {
					fail_output = input;
				}
			}
		}
	}
	
	/**
	 * 유효성 검증
	 * @return boolean
	 */
	boolean isValidation() {
		// 사용하는 제련식 여부
		if (!Config.SMELTING.SMELTING_ALCHEMY_USED_IDS.contains(alchemy_id)) {
			_pc.sendPackets(S_SmeltingMake.ERROR_ALCHEMY_DOES_NOT_EXIST);
			return false;
		}
		
		// 슬롯 존재 여부
		if (inputs == null || inputs.isEmpty()) {
			_pc.sendPackets(S_SmeltingMake.ERROR_INVALID_INPUT);
			return false;
		}
		
		// 슬롯 완성 여부
		if (alchemy_id <= 4 && inputs.size() != 4) {
			_pc.sendPackets(S_SmeltingMake.ERROR_INVALID_INPUT);
			return false;
		}
		
		// 수수료 소모 여부
		if (!_pc.getInventory().consumeItem(getAlchemyFeeId(), getAlchemyFeeCost())) {
			_pc.sendPackets(S_SmeltingMake.ERROR_NOT_ENOUGH_SUBINPUT);
			return false;
		}
		return true;
	}
	
	/**
	 * 슬롯 재료 소모
	 */
	void consumeSlot() {
		for (InputSlot slot : inputs) {
			_pc.getInventory().removeItem(slot.item);
		}
	}

	@Override
	protected void doWork() throws Exception {
		try {
			if (_pc == null || _pc.isGhost()) {
				return;
			}
			parse();
			if (!isValidation()) {
				return;
			}
			
			boolean success = true;
			if (alchemy_id >= 5) {// 교체
				output = successOutput();
			} else {// 합성
				success = CommonUtil.random(100) + 1 <= getAlchemyProb();
				output = success ? successOutput() : failureOutput();
			}
			
			if (output == null) {
				_pc.sendPackets(S_SmeltingMake.ERROR_NO_REQUIRED_ITEM);
				errorPrint("OUTPUT_NOT_FOUND");
				return;
			}
			
			consumeSlot();
			L1ItemInstance result = ItemTable.getInstance().createItem(output.getItemId());
			_pc.sendPackets(new S_SmeltingMake(S_SmeltingMake.ResultCode.RC_SUCCESS, result), true);
			_pc.getInventory().storeItem(result);
			if (!success && alchemy_id == 3) {// 4단계 제련석 합성 실패
				_pc.getInventory().storeItem(31815, 1);// 4단계 제련석 조각
			}
			LoggerInstance.getInstance().addSmeltingMake(success ? SmeltingMakeType.SUCCESS : SmeltingMakeType.FAILURE, _pc, alchemy_id, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disposeSlot();
		}
	}
	
	/**
	 * 단계에 해당하는 확률
	 * @return int
	 */
	int getAlchemyProb(){
		switch(alchemy_id){
		case 1:	return Config.SMELTING.SMELTING_ALCHEMY_1_PROB;
		case 2:	return Config.SMELTING.SMELTING_ALCHEMY_2_PROB;
		case 3:	return Config.SMELTING.SMELTING_ALCHEMY_3_PROB;
		case 4:	return Config.SMELTING.SMELTING_ALCHEMY_4_PROB;
		default:return 0;
		}
	}
	
	/**
	 * 단계별 수수료 아이템 아이디
	 * @return int
	 */
	int getAlchemyFeeId() {
		return alchemy_id >= 5 ? Config.SMELTING.SMELTING_ALCHEMY_CHANGE_FEE_ITEM_ID : Config.SMELTING.SMELTING_ALCHEMY_FUSION_FEE_ITEM_ID;
	}
	
	/**
	 * 단계별 수수료 비용
	 * @return int
	 */
	int getAlchemyFeeCost(){
		switch(alchemy_id){
		case 1:	return Config.SMELTING.SMELTING_ALCHEMY_1_FEE_COST;
		case 2:	return Config.SMELTING.SMELTING_ALCHEMY_2_FEE_COST;
		case 3:	return Config.SMELTING.SMELTING_ALCHEMY_3_FEE_COST;
		case 4:	return Config.SMELTING.SMELTING_ALCHEMY_4_FEE_COST;
		case 5:	return Config.SMELTING.SMELTING_ALCHEMY_5_FEE_COST;
		case 6:	return Config.SMELTING.SMELTING_ALCHEMY_6_FEE_COST;
		case 7:	return Config.SMELTING.SMELTING_ALCHEMY_7_FEE_COST;
		default:return 0;
		}
	}
	
	/**
	 * 성공 생성 아이템
	 * @return L1Item
	 */
	L1Item successOutput() {
		int success_grade = alchemy_id + (alchemy_id == 7 ? -4 : alchemy_id >= 5 ? -1 : 1);
		ArrayList<L1Item> output_list = ItemTable.getSmeltingList(success_grade);
		if (output_list == null) {
			errorPrint("OUTPUT_LIST_NOT_FOUND");
			return null;
		}
		return output_list.get(CommonUtil.random(output_list.size()));
	}
	
	/**
	 * 실패 생성 아이템
	 * @return L1Item
	 */
	L1Item failureOutput() {
		if (alchemy_id == 1) {
			return inputs.get(CommonUtil.random(inputs.size())).item.getItem();
		}
		return fail_output.item.getItem();
	}
	
	/**
	 * 오류 발생 출력
	 * @param err_code
	 */
	void errorPrint(String err_code) {
		System.out.println(String.format(
				"[A_SmeltingMake] %s : ALCHEMY_ID(%d), CHAR_NAME(%s)",
				err_code, alchemy_id, _pc.getName()));
		for (InputSlot input : inputs) {
			System.out.println(input.toString());
		}
	}
	
	static class InputSlot {
		int slot_no;
		int item_name_id;
		int item_id;
		L1ItemInstance item;
		
		InputSlot(int slot_no, int item_name_id, int item_id, L1ItemInstance item) {
			this.slot_no		= slot_no;
			this.item_name_id	= item_name_id;
			this.item_id		= item_id;
			this.item			= item;
		}
		
		@Override
		public String toString() {
			return new StringBuilder()
			.append("input_slot_no : ").append(slot_no)
			.append(", item_name_id : ").append(item_name_id)
			.append(", item_id : ").append(item_id)
			.toString();
		}
	}
	
	void disposeSlot() {
		if (inputs != null) {
			inputs.clear();
			inputs = null;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SmeltingMake(data, client);
	}

}


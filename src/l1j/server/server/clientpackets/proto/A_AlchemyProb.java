package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.L1Doll;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.alchemy.S_AlchemyProb;
import l1j.server.server.serverpackets.alchemy.S_AlchemyProb.AlchemyProb;
import l1j.server.server.serverpackets.alchemy.S_AlchemyProb.AlchemyProbResultCode;
import l1j.server.server.templates.L1Item;

public class A_AlchemyProb extends ProtoHandler {
	protected A_AlchemyProb(){}
	private A_AlchemyProb(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int alchemy_id;
	private java.util.LinkedList<AlchemyProb> prob_list;
	private int total_prob = 100;
	int failure_prob_total;
	private boolean isMainInput;
	L1ItemInstance mainInput;
	AlchemyProb mainProb;
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		alchemy_id = readC();// alchemy_id
		isMainInput	= Config.ALCHEMY.ALCHEMY_MAIN_INPUT_IDS.contains(alchemy_id);
		if (alchemy_id != 11) {
			while (!isEnd()) {
				int tag = readC();
				if (tag == 0x12) {
					readP(1);// 길이
					
					readP(1);// 0x08
					int slot_no = readC();
					readP(1);// 0x10
					int item_name_id = readBit();// item name id
					readP(1);// 0x18
					int item_id = readBit();// item id
					
					L1ItemInstance dollItem = _pc.getInventory().getItem(item_id);
					if (dollItem != null) {
						if (prob_list == null) {
							prob_list = new java.util.LinkedList<>();
						}
						AlchemyProb prob = new AlchemyProb(item_name_id, 0, dollItem.getBless());
						prob_list.add(prob);
						
						if (isMainInput && slot_no == 1) {
							mainInput = dollItem;
							mainProb = prob;
						}
					}
				}
			}
			
			if (prob_list == null) {
				return;
			}
			
			// 특수 합성(3 ~ 5)
			if (alchemy_id >= 5 && alchemy_id <= 7) {
				prob_list.clear();
				int[] specialFailOutputIds = getSpecialFailOutputIds();
				for (int id : specialFailOutputIds) {
					L1Item temp = ItemTable.getInstance().getTemplate(id);
					AlchemyProb prob = new AlchemyProb(temp.getItemNameId(), 0, temp.getBless());
					prob_list.add(prob);
				}
			}
			
			failure_prob_total = (100 - (getSuccessProb() * prob_list.size()));
			total_prob -= failure_prob_total;
			
			int failure_prob = (int)(((double)failure_prob_total / (double)prob_list.size()) * 10000000D);
			if (isMainInput) {
				for (AlchemyProb prob : prob_list) {
					prob.set_name_id(mainProb.get_name_id());
					prob.set_prob(failure_prob);
					prob.set_bless_code(mainProb.get_bless_code());
				}
			} else {
				for (AlchemyProb prob : prob_list) {
					prob.set_prob(failure_prob);
				}
			}
		}
		if (alchemy_id > L1Doll.SUCCESS_ALCHEMY_DOLL_IDS.length) {
			System.out.println(String.format("[A_AlchemyProb] ALCHEMY_SUCCESS_IDS_SIZE_OVER : ALCHEMY_ID", alchemy_id));
			return;
		}
		
		if (alchemy_id == 12 || alchemy_id == 15) {
			successMainBlessProb();
		} else {
			successProb();
		}

		if (prob_list.isEmpty()) {
			prob_list = null;
			return;
		}
		_pc.sendPackets(new S_AlchemyProb(AlchemyProbResultCode.RC_SUCCESS, prob_list), true);
		prob_list.clear();
		prob_list = null;
	}
	
	void successProb() {
		int[] success_ids = L1Doll.SUCCESS_ALCHEMY_DOLL_IDS[alchemy_id - 1];
		if (success_ids == null) {
			return;
		}
		if (prob_list == null) {
			prob_list = new java.util.LinkedList<>();
		}
		int success_prob = (int)(((double) total_prob / (double) success_ids.length) * 10000000D);
		ItemTable it = ItemTable.getInstance();
		for (int id : success_ids) {
			L1Item temp = it.getTemplate(id);
			prob_list.add(new AlchemyProb(temp.getItemNameId(), success_prob, temp.getBless()));
		}
	}
	
	/**
	 * 주재료와 동일한 축복 인형
	 */
	void successMainBlessProb() {
		if (prob_list == null) {
			prob_list = new java.util.LinkedList<>();
		}
		int success_prob = (int)(((double) total_prob / (double) 1) * 10000000D);
		int output = MagicDollInfoTable.getDollInfo(mainInput.getItemId()).getBlessItemId();
		L1Item temp = ItemTable.getInstance().getTemplate(output);
		prob_list.add(new AlchemyProb(temp.getItemNameId(), success_prob, temp.getBless()));
	}
	
	int getSuccessProb(){
		switch(alchemy_id){
		case 1:	return Config.ALCHEMY.ALCHEMY_1_PROB;// 1단계
		case 2:	return Config.ALCHEMY.ALCHEMY_2_PROB;// 2단계
		case 3:	return Config.ALCHEMY.ALCHEMY_3_PROB;// 3단계
		case 4:	return Config.ALCHEMY.ALCHEMY_4_PROB;// 4단계
		case 15:return Config.ALCHEMY.ALCHEMY_15_PROB;// 축복 드래곤
		case 16:return Config.ALCHEMY.ALCHEMY_16_PROB;// 5단계(축복 드래곤)
		default:return Config.ALCHEMY.ALCHEMY_DEFAULT_PROB;
		}
	}
	
	/**
	 * 특수 합성(3 ~ 5) 실패시 생성될 아아템 리스트
	 * @return output array
	 */
	int[] getSpecialFailOutputIds(){
		switch (alchemy_id) {
		case 5:
			return L1Doll.NOMAL_LEVEL_2_IDS;
		case 6:
			return L1Doll.NOMAL_LEVEL_3_IDS;
		case 7:
			return L1Doll.NOMAL_LEVEL_4_IDS;
		default:
			return null;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_AlchemyProb(data, client);
	}

}


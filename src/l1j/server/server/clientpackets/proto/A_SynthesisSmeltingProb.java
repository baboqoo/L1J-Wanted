package l1j.server.server.clientpackets.proto;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.smelting.S_SynthesisSmeltingProb;
import l1j.server.server.serverpackets.smelting.S_SynthesisSmeltingProb.SmeltingProb;
import l1j.server.server.templates.L1Item;

public class A_SynthesisSmeltingProb extends ProtoHandler {
	protected A_SynthesisSmeltingProb(){}
	private A_SynthesisSmeltingProb(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int alchemy_id;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		alchemy_id = readC();// alchemy_id
		
		int total_prob = 100;
		java.util.LinkedList<SmeltingProb> prob_list = null;
		if (alchemy_id <= 4) {
			while (isRead(1)) {
				int tag = readC();
				if (tag == 0x12) {
					readP(1);// 길이
					
					readP(3);// 0x08, slot no, 0x10
					int item_name_id = readBit();// item name id
					
					readP(1);// 0x18
					int item_id = readBit();// item id
					
					L1ItemInstance smeltingItem = _pc.getInventory().getItem(item_id);
					if (smeltingItem != null) {
						if (prob_list == null) {
							prob_list = new java.util.LinkedList<>();
						}
						if (alchemy_id == 1) {
							prob_list.add(new SmeltingProb(item_name_id, 0, smeltingItem.getBless()));
						} else {
							if (alchemy_id == smeltingItem.getItem().getEtcValue()) {
								prob_list.add(new SmeltingProb(item_name_id, 0, smeltingItem.getBless()));
							}
						}
					}
				}
			}
			
			if (prob_list == null) {
				return;
			}
			
			int failure_prob_total = (100 - getAlchemyProb());
			total_prob -= failure_prob_total;
			
			int failure_prob = (int)(((double)failure_prob_total / (double)prob_list.size()) * 10000000D);
			for (SmeltingProb prob : prob_list) {
				prob.set_prob(failure_prob);
			}			
		}

		int success_grade = alchemy_id + (alchemy_id == 7 ? -4 : alchemy_id >= 5 ? -1 : 1);
		ArrayList<L1Item> success_list = ItemTable.getSmeltingList(success_grade);
		if (success_list == null) {
			System.out.println(String.format("[A_SynthesisSmeltingProb] ALCHEMY_SUCCESS_LIST_EMPTY : ALCHEMY_ID(%d)", alchemy_id));
			return;
		}
		if (prob_list == null) {
			prob_list = new java.util.LinkedList<>();
		}
		int success_prob = (int)(((double)total_prob / (double)success_list.size()) * 10000000D);
		for (L1Item smelting : success_list) {
			prob_list.add(new SmeltingProb(smelting.getItemNameId(), success_prob, smelting.getBless()));
		}

		if (prob_list.isEmpty()) {
			prob_list = null;
			return;
		}
		_pc.sendPackets(new S_SynthesisSmeltingProb(S_SynthesisSmeltingProb.ResultCode.RC_SUCCESS, prob_list), true);
		prob_list.clear();
		prob_list = null;
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

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SynthesisSmeltingProb(data, client);
	}

}


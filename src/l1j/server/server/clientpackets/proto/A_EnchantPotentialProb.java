package l1j.server.server.clientpackets.proto;

import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.common.bin.PotentialCommonBinLoader;
import l1j.server.common.bin.potential.CommonPotentialInfo;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.potential.L1Potential;
import l1j.server.server.serverpackets.alchemy.S_EnchantPotentialProb;

public class A_EnchantPotentialProb extends ProtoHandler {
	protected A_EnchantPotentialProb(){}
	private A_EnchantPotentialProb(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _target_id;
	private boolean _is_event;
	
	void parse() {
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_target_id = read4(read_size());
				break;
			case 0x10:
				_is_event = readBool();
				break;
			default:
				return;
			}
		}
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		if (_target_id == 0) {
			return;
		}
		L1ItemInstance item		= _pc.getInventory().getItem(_target_id);
		if (item == null) {
			return;
		}
		int dollGrade			= MagicDollInfoTable.getDollInfo(item.getItemId()).getGrade();// 인형등급
		L1Potential potential	= item.getPotential();
		int currentGrade		= potential == null ? 0 : potential.getInfo().get_bonus_grade();
		
		HashMap<Integer, ArrayList<CommonPotentialInfo.BonusInfoT>> prob_list = PotentialCommonBinLoader.getBin().get_bonus_prob_list(dollGrade, currentGrade, _is_event);
		if (prob_list == null || prob_list.isEmpty()) {
			return;
		}
		_pc.sendPackets(new S_EnchantPotentialProb(dollGrade, currentGrade, prob_list), true);
		prob_list.clear();
		prob_list = null;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EnchantPotentialProb(data, client);
	}

}


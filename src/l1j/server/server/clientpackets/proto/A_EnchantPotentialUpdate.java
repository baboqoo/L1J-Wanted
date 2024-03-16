package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.potential.L1Potential;
import l1j.server.server.monitor.Logger.PotentialType;
import l1j.server.server.monitor.LoggerInstance;

public class A_EnchantPotentialUpdate extends ProtoHandler {
	protected A_EnchantPotentialUpdate(){}
	private A_EnchantPotentialUpdate(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _target_id;
	private L1ItemInstance doll_item;
	private boolean _isChange;
	private int _bonus_grade;
	private int _bonus_id;
	
	void parse() {
		readP(1);// 0x08
		_target_id		= read4(read_size());
		readP(1);// 0x10
		_isChange		= readBool();
		readP(1);// 0x18
		_bonus_grade	= readC();
		readP(1);// 0x20
		_bonus_id		= read4(read_size());
	}
	
	boolean isValidation() {
		doll_item	= _pc.getInventory().getItem(_target_id);
		if (doll_item == null) {
			return false;
		}
		L1CharacterConfig config = _pc.getConfig();
		if (config.get_potential_target_id() != _target_id) {
			System.out.println(String.format(
					"[A_EnchantPotentialUpdate] DENALS_REQUEST : RIGHT_TARGET_ID(%d), REQ_TARGET_ID(%d), CHAR_NAME(%s)", 
					config.get_potential_target_id(), _target_id, _pc.getName()));
			LoggerInstance.getInstance().addPotential(PotentialType.DENALS, _pc, _bonus_grade, _bonus_id, _isChange, doll_item);
			config.reset_potential_enchant();
			return false;
		}
		if (config.get_potential_bonus_grade() != _bonus_grade) {
			System.out.println(String.format(
					"[A_EnchantPotentialUpdate] DENALS_REQUEST : RIGHT_GRADE(%d), REQ_GRADE(%d), CHAR_NAME(%s)", 
					config.get_potential_bonus_grade(), _bonus_grade, _pc.getName()));
			LoggerInstance.getInstance().addPotential(PotentialType.DENALS, _pc, _bonus_grade, _bonus_id, _isChange, doll_item);
			config.reset_potential_enchant();
			return false;
		}
		if (_bonus_grade != 6 && config.get_potential_bonus_id() != _bonus_id) {
			System.out.println(String.format(
					"[A_EnchantPotentialUpdate] DENALS_REQUEST : RIGHT_ID(%d), REQ_ID(%d), CHAR_NAME(%s)", 
					config.get_potential_bonus_id(), _bonus_id, _pc.getName()));
			LoggerInstance.getInstance().addPotential(PotentialType.DENALS, _pc, _bonus_grade, _bonus_id, _isChange, doll_item);
			config.reset_potential_enchant();
			return false;
		}
		config.reset_potential_enchant();
		return true;
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		if (!isValidation()) {
			return;
		}
		if (_isChange) {
			L1Potential potential = MagicDollInfoTable.getPotential(_bonus_id);
			if (doll_item.getPotential() != potential) {
				doll_item.setPotential(potential);
				_pc.getInventory().updateItem(doll_item, L1PcInventory.COL_DOLL_POTENTIAL);
				_pc.getInventory().saveItem(doll_item, L1PcInventory.COL_DOLL_POTENTIAL);
			}
		}
		LoggerInstance.getInstance().addPotential(PotentialType.RIGHT, _pc, _bonus_grade, _bonus_id, _isChange, doll_item);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EnchantPotentialUpdate(data, client);
	}

}


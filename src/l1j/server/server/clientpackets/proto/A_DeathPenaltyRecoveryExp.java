package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.deathpenalty.DeathPenaltyTable;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyExpObject;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyObject;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.monitor.Logger.DeathPenaltyType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.inventory.S_DeathPenaltyRecoveryExpListNoti;

public class A_DeathPenaltyRecoveryExp extends ProtoHandler {
	protected A_DeathPenaltyRecoveryExp(){}
	private A_DeathPenaltyRecoveryExp(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _index;
	private boolean _use_recovery_item;
	
	void parse() {
		readP(1);// 0x08
		_index = readC();
		readP(1);// 0x10
		_use_recovery_item = readBool();
	}
	
	boolean isValidation() {
		return _index >= 0;
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.isStop() || _pc.isDead()) {
			return;
		}
		parse();
		if (!isValidation()) {
			return;
		}
		
		java.util.LinkedList<DeathPenaltyObject> list = _pc.get_penalty_exp().get_list();
		if (list == null || list.isEmpty()) {
			return;
		}
		
		DeathPenaltyExpObject obj = null;
		for (DeathPenaltyObject val : list) {
			DeathPenaltyExpObject check = (DeathPenaltyExpObject) val;
			if (check.get_index() == _index) {
				obj = check;
				break;
			}
		}
		
		if (obj == null) {
			System.out.println(String.format("[A_DeathPenaltyRecoveryExp] EXP_OBJECT_NOT_FOUND : INDEX(%d), CHAR(%s)", _index, _pc.getName()));
			return;
		}
		
		if (obj.get_delete_time().getTime() <= System.currentTimeMillis()) {
			return;
		}
		
		int recovery_item_id	= !_use_recovery_item ? L1ItemId.ADENA : 3000049;
		int recovery_cost		= !_use_recovery_item ? obj.get_recovery_cost() : 1;
		long recovery_exp		= (long)(obj.get_exp_value() * 0.99D);// 99%
		if (_pc.getExp() + recovery_exp >= L1ExpPlayer.LIMIT_EXP) {
			_pc.sendPackets(L1SystemMessage.LIMIT_LEVEL_EXP_FAIL);
			return;
		}
		if (!_pc.getInventory().checkItem(recovery_item_id, recovery_cost)) {// 재료
			_pc.sendPackets(!_use_recovery_item ? L1ServerMessage.sm189 : L1ServerMessage.sm337_EXPPAPER);// \f1%0이 부족합니다.
			return;
		}

		long next_level_exp = ExpTable.getExpByLevel(_pc.getLevel() + 1);
	    if (recovery_exp + _pc.getExp() > next_level_exp) {
	    	recovery_exp = next_level_exp - _pc.getExp();
		}
	    if (!DeathPenaltyTable.getInstance().deleteExp(obj)) {
	    	return;
	    }
    	_pc.getInventory().consumeItem(recovery_item_id, recovery_cost);
    	_pc.addExp(recovery_exp);
    	list.remove(obj);
    	_pc.sendPackets(new S_DeathPenaltyRecoveryExpListNoti(list), true);
		LoggerInstance.getInstance().addDeathPenaltyExp(DeathPenaltyType.RECOVERY, _pc, obj, recovery_item_id);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_DeathPenaltyRecoveryExp(data, client);
	}

}


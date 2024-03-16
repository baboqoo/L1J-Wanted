package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Pet;

public class A_CompanionStatIncrease extends ProtoHandler {
	protected A_CompanionStatIncrease(){}
	private A_CompanionStatIncrease(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _str;
	private int _int;
	private int _con;
	
	void parse() {
		readP(1);// 0x08
		_str = readBit();
        readP(1);// 0x10
        _int = readBit();
        readP(1);// 0x18
        _con = readBit();
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1PetInstance companion = _pc.getPet();
        if (companion == null) {
        	return;
        }
        L1Pet info = companion.getPetInfo();
        if (info == null) {
        	return;
        }
        parse();
        
        int total = _str + _int + _con; 
        int remain_stats = info.get_remain_stats();
        if (!isValidationStat(_str, remain_stats, total) 
        		|| !isValidationStat(_int, remain_stats, total)
        		|| !isValidationStat(_con, remain_stats, total)
        		|| remain_stats < total) {
//AUTO SRM:         	_pc.sendPackets(new S_SystemMessage("적용할 스탯 수치가 정상적이지 않습니다."), true); // CHECKED OK
        	_pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(137), true), true);
        	return;
        }
        info.addAddStr(_str);
        info.addAddInt(_int);
        info.addAddCon(_con);
        info.add_remain_stats(-total);
        
        L1Ability ability = companion.getAbility();
        ability.addAddedStr(_str);
        ability.addAddedInt(_int);
        ability.addAddedCon(_con);
        companion.doStatBonus(true);
        CharacterCompanionTable.getInstance().ChangeStat(info);
	}
	
	boolean isValidationStat(int val, int remain_stats, int total) {
		return val >= 0 && val <= Config.COMPANION.STAT_MAX_VALUE && val <= remain_stats && val <= total;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CompanionStatIncrease(data, client);
	}

}



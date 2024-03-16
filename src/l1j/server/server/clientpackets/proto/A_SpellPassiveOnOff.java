package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.serverpackets.spell.S_SpellPassiveOnOff;

public class A_SpellPassiveOnOff extends ProtoHandler {
	protected A_SpellPassiveOnOff(){}
	private A_SpellPassiveOnOff(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		L1PassiveId passive_id = L1PassiveId.fromInt(readBit());
		readP(1);// 0x10
		int onoff = readC();// 0:끔, 1:켬
		switch(passive_id){
		case BLOOD_TO_SOUL:
			bloodToSoul(onoff);
			break;
		default:
			_pc.sendPackets(new S_SpellPassiveOnOff(passive_id.toInt(), onoff == 1), true);
			break;
		}
	}
	
	void bloodToSoul(int onoff){
		if (!_pc.isElf() || !_pc.isPassiveStatus(L1PassiveId.BLOOD_TO_SOUL)) {
			return;
		}
		_pc._isBloodToSoulAuto	= onoff == 1;
		_pc._bloodToSoulCount	= 0;
		_pc.sendPackets(S_SpellPassiveOnOff.BLOOD_TO_SOUL[onoff]);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SpellPassiveOnOff(data, client);
	}

}


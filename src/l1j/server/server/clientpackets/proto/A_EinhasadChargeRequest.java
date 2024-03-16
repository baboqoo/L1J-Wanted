package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Einhasad;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;

public class A_EinhasadChargeRequest extends ProtoHandler {
	protected A_EinhasadChargeRequest(){}
	private A_EinhasadChargeRequest(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		int chargePotint = Config.EIN.REST_EXP_DEFAULT_RATION * 1000;
		L1Einhasad ein = _pc.getAccount().getEinhasad();
		if (ein.getPoint() >= chargePotint || ein.getDayBonus() == 0) {
			return;
		}
		ein.setPoint(chargePotint, _pc);
		ein.setDayBonus(0);
		ein.updateEinDayBonus();
		_pc.sendPackets(new S_RestExpInfoNoti(_pc), true);
		_pc.sendPackets(new S_ExpBoostingInfo(_pc), true);// 보너스 경험치
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EinhasadChargeRequest(data, client);
	}

}


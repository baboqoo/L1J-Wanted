package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ArcaActivationInfo;

public class A_CharacterListRequest extends ProtoHandler {
	protected A_CharacterListRequest(){}
	private A_CharacterListRequest(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		int cancelation_delay = _pc.getSkill().getSkillEffectTimeSec(L1SkillId.ARCA_CANCEL_DELAY);
		_pc.sendPackets(new S_ArcaActivationInfo(_pc.getAccount(), cancelation_delay <= 0 ? 0 : cancelation_delay), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CharacterListRequest(data, client);
	}
	
}


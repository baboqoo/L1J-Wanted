package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.party.S_PartyAssistTargetBroadcastNoti;

public class A_PartyAssist extends ProtoHandler {
	protected A_PartyAssist(){}
	private A_PartyAssist(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _target_id;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.isDead() || !_pc.isInParty()) {
			return;
		}
		readP(1);
		_target_id = readBit();
		L1PcInstance target_pc = L1World.getInstance().getPlayer(_target_id);
		if (_pc.getClanid() > 0 && target_pc != null && target_pc.getClanid() == _pc.getClanid()) {// 동일 혈맹원 제외
			return;
		}
		send();
	}
	
	void send() {
		boolean release = _target_id <= 0 || _target_id == _pc.getId();
		S_PartyAssistTargetBroadcastNoti pck = release ? S_PartyAssistTargetBroadcastNoti.RELEASE : new S_PartyAssistTargetBroadcastNoti(_target_id);
		for (L1PcInstance target : L1World.getInstance().getVisiblePartyPlayer(_pc, -1)) {// 화면내 파티멤버
			target.sendPackets(pck);
		}
		if (!release) {
			pck.clear();
			pck = null;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PartyAssist(data, client);
	}

}


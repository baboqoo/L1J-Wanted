package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.serverpackets.smelting.S_SynthesisSmeltingStart;
import l1j.server.server.utils.CommonUtil;

public class A_SynthesisSmeltingStart extends ProtoHandler {
	protected A_SynthesisSmeltingStart(){}
	private A_SynthesisSmeltingStart(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null) {
			return;
		}
		if (_pc.isGhost() || _pc.isPrivateShop() || _pc.isAutoClanjoin() || _pc.isFishing()) {
			_pc.sendPackets(S_SynthesisSmeltingStart.FAIL_USER);
			return;
		}
		if (_pc.getRegion() != L1RegionStatus.SAFETY) {
			_pc.sendPackets(S_SynthesisSmeltingStart.FAIL_LOC);
			return;
		}
		_pc.getTeleport().setStateLoc(_pc.getX(), _pc.getY(), _pc.getMapId());
		_pc.getTeleport().c_start(32761 + CommonUtil.random(10), 32832 + CommonUtil.random(10), (short) 5167, _pc.getMoveState().getHeading(), true);// 축복의 땅
		_pc.sendPackets(S_SynthesisSmeltingStart.SUCCESS);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SynthesisSmeltingStart(data, client);
	}

}


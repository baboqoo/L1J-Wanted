package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.S_WithdrawDefenceSuccessAdenaNoti;

public class A_WithdrawDefenceSuccessAdena extends ProtoHandler {
	protected A_WithdrawDefenceSuccessAdena(){}
	private A_WithdrawDefenceSuccessAdena(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _npcId;
	private long _adenaCount;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		_npcId = readBit();
		readP(1);// 0x10
		_adenaCount	= readLong();
		_pc.sendPackets(new S_WithdrawDefenceSuccessAdenaNoti(_npcId, _adenaCount), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_WithdrawDefenceSuccessAdena(data, client);
	}

}


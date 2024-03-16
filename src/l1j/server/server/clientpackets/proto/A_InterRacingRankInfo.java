package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.L1InterServer;

public class A_InterRacingRankInfo extends ProtoHandler {
	protected A_InterRacingRankInfo(){}
	private A_InterRacingRankInfo(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _raceKind;
	private int _raceTrack;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		_raceKind = readBit();
		readP(1);
		_raceTrack = readBit();
		L1InterServer inter = _pc.getMap().getInter();
		if (inter == null || inter.getKind() != _raceKind) {
			return;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_InterRacingRankInfo(data, client);
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("raceKind : ").append(_raceKind)
				.append(", _raceTrack : ").append(_raceTrack).toString();
	}

}


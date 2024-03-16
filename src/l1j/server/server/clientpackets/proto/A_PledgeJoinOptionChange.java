package l1j.server.server.clientpackets.proto;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.common.data.ePLEDGE_JOIN_REQ_TYPE;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoinOptionChange;

public class A_PledgeJoinOptionChange extends ProtoHandler {
	protected A_PledgeJoinOptionChange(){}
	private A_PledgeJoinOptionChange(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (_pc.getClanid() == 0) {
			return;
		}
		if (!eBloodPledgeRankType.isAuthRankAtKnight(_pc.getBloodPledgeRank())) {
			return;
		}
		readP(1);// 0x08
		boolean enable_join = readC() == 1;// 0:혈맹원모집 끔, 1:혈맹원모집 켬
		readP(1);// 0x10
		ePLEDGE_JOIN_REQ_TYPE join_type = ePLEDGE_JOIN_REQ_TYPE.fromInt(readC());// 0:즉시가입, 1:승인가입, 2:암호가입
		if (join_type == null) {
			return;
		}
		if (join_type == ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_PASSWORD) {// 암호가입
			readP(1);// 0x1a
			int size = readC();// pwd length
			StringBuilder sb = new StringBuilder(size);// hashed_password
			for (int i = 0; i < size; i++) {
				sb.append(String.format("%02X", readC()));
			}
			_pc.getClan().setJoinPassword(sb.toString());
			ClanTable.getInstance().updateJoinPassword(_pc.getClan());
		}
		_pc.getClan().setEnableJoin(enable_join);
		_pc.getClan().setJoinType(join_type);
		boolean result = ClanTable.getInstance().updateClan(_pc.getClan());
		_pc.sendPackets(result ? S_BloodPledgeJoinOptionChange.SUCCESS : S_BloodPledgeJoinOptionChange.FAIL);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeJoinOptionChange(data, client);
	}

}


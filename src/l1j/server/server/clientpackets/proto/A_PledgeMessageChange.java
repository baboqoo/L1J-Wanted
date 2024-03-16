package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.utils.StringUtil;

public class A_PledgeMessageChange extends ProtoHandler {
	protected A_PledgeMessageChange(){}
	private A_PledgeMessageChange(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.getClanid() == 0 || !_pc.isCrown()) {
			return;
		}
		readP(1);// 0x0a
		int messageLength = readC();// memolength
		String message = StringUtil.EmptyString;
		if (messageLength > 0) {
			message = readS(messageLength).replaceAll(StringUtil.MinusString, StringUtil.EmptyString);// 소개글
		}
		_pc.getClan().setIntroductionMessage(message);
		ClanTable.getInstance().updateIntroductionMessage(_pc.getClan());
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeMessageChange(data, client);
	}

}


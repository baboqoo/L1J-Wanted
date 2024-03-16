package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.quest.S_QuestShowInWindow;

public class A_QuestShowWindow extends ProtoHandler {
	protected A_QuestShowWindow(){}
	private A_QuestShowWindow(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _id;
	private boolean _is_show;

	@Override
	protected void doWork() throws Exception {
		if (!Config.QUEST.BEGINNER_QUEST_ACTIVE || _pc == null) {
			return;
		}
		readP(1);
		_id = readBit();
		readP(1);
		_is_show = readBool();
		if (_pc.isGm()) {
			System.out.println(String.format("[A_QuestShowWindow] ID(%d), SHOW(%b)", _id, _is_show));
		}
		_pc.sendPackets(new S_QuestShowInWindow(S_QuestShowInWindow.eResultCode.SUCCESS, _id, _is_show), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_QuestShowWindow(data, client);
	}

}


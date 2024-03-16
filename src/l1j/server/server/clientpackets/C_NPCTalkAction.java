package l1j.server.server.clientpackets;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_NPCTalkAction extends ClientBasePacket {
	private static final String C_NPC_TALK_ACTION = "[C] C_NPCTalkAction";
	private static Logger _log = Logger.getLogger(C_NPCTalkAction.class.getName());

	public C_NPCTalkAction(byte decrypt[], GameClient client) throws FileNotFoundException, Exception {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int objectId	= readD();
		String action	= readS();
		L1Object obj	= L1World.getInstance().findObject(objectId);
		if (obj == null) {
			_log.warning("object not found, oid " + objectId);
			return;
		}
		try {
			((L1NpcInstance) obj).onFinalAction(pc, action);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getType() {
		return C_NPC_TALK_ACTION;
	}

}


package l1j.server.server.clientpackets;

import java.util.logging.Logger;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_Unknown2;

public class C_RestartComplete extends ClientBasePacket {
	private static final String C_RestartComplete = "[C] C_RestartComplete";
	private static Logger _log = Logger.getLogger(C_RestartComplete.class.getName());
	
	public C_RestartComplete(byte[] decrypt, GameClient client) throws Exception {
		super(decrypt);
		if (client == null) {
			return;
		}
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		restartProcess(pc);
	}
	
	public static void restartProcess(L1PcInstance pc) throws Exception {
		pc.isWorld = false;
		
		if (pc.getOnlineStatus() != 0) {
			pc.setOnlineStatus(0);
			L1Clan clan = pc.getClan();
			if (clan != null) {
				clan.updateClanMemberOnline(pc);
			}
		}
		_log.fine("Disconnect from: " + pc.getName());
		if (pc.isDead()) {
			return;
		}
		GameClient client = pc.getNetConnection();
		if (client == null) {
			return;
		}
		client.setRestartMillis(System.currentTimeMillis());
		synchronized (pc) {
			if (client.isInterServer()) {
				client.releaseInter();
			}
			pc.logout();
		}
		client.setLoginAvailable();
		client.setCharReStart(true);
		client.sendPacket(S_Unknown2.RETART);// 리스버튼을 위한 구조변경 // Episode U
		client.setActiveChar(null);
		if (client.getAccount().isCharSlotChange()) {
			mergeCharSlot(client);
			client.getAccount().setCharSlotChange(false);
		}
	}
	
	public static void mergeCharSlot(GameClient client){
		int amountOfChars	= client.getAccount().countCharacters();
		S_CharAmount ammout	= new S_CharAmount(amountOfChars, client.getAccount().getCharSlot());
		client.sendPacket(ammout);
		ammout.clear();
		ammout = null;
		C_CharacterSelect.sendCharPacks(client);
	}
	
	@Override
	public String getType() {
		return C_RestartComplete;
	}
}


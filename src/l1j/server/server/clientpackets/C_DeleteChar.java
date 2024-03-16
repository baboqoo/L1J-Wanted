package l1j.server.server.clientpackets;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DeleteCharOK;
import l1j.server.server.serverpackets.message.S_CommonNews;

public class C_DeleteChar extends ClientBasePacket {
	private static final String C_DELETE_CHAR = "[C] C_DeleteChar";
	private static Logger _log = Logger.getLogger(C_DeleteChar.class.getName());

	public C_DeleteChar(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		String name = readS();
		try {
			L1World world = L1World.getInstance();
			if (world.getPlayer(name) != null) {
				client.sendPacket(S_CommonNews.CONNECT_DELETE_FAIL);
				return;
			}
			CharacterTable character = CharacterTable.getInstance();
			L1PcInstance pc = character.restoreCharacter(name);
			if (pc == null) {
				client.sendPacket(S_CommonNews.CHARACTER_EMPTY);
				return;
			}
			if (character.is_seal_item_inventory(pc.getId())) {
				client.sendPacket(S_CommonNews.BLESS_ITEM_DELETE_FAIL);
				return;
			}
			if (world.getClan(pc.getClanName()) != null) {
				client.sendPacket(S_CommonNews.CLAN_DELETE_FAIL);
				return;
			}
			long currentTime = System.currentTimeMillis();
			if (pc.getEinhasadGraceTime() != null && pc.getEinhasadGraceTime().getTime() >= currentTime) {
				client.sendPacket(S_CommonNews.EIN_GRACE_DELETE_FAIL);
				return;
			}
			
			S_DeleteCharOK delete = null;
			if (pc.getLevel() > 30 && Config.ALT.DELETE_CHARACTER_AFTER_7DAYS) {
				if (pc.getType() < 32) {
					pc.setType(pc.getType() + 32);
					//pc.setDeleteTime(new Timestamp(currentTime + 86400000)); // 1 day
					pc.setDeleteTime(new Timestamp(currentTime + 604800000)); // 7 days
				} else {
					pc.setType(pc.getType() - 32);
					pc.setDeleteTime(null);
				}
				pc.save();
				delete = new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_AFTER_7DAYS, pc);
				client.sendPacket(delete);
				delete.clear();
				delete = null;
				return;
			}
			character.deleteCharacter(client.getAccountName(), name);
			if (client.getAccount() != null) {
				client.getAccount().getArca().removeActivation(pc.getId());
				client.getAccount().removeCharList(pc);
			}
			delete = new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_NOW, pc);
			client.sendPacket(delete);
			delete.clear();
			delete = null;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			client.close();
			return;
		}
	}

	@Override
	public String getType() {
		return C_DELETE_CHAR;
	}

}


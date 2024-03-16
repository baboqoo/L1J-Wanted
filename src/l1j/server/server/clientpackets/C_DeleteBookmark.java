package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1BookMark;

public class C_DeleteBookmark extends ClientBasePacket {
	private static final String C_DETELE_BOOKMARK = "[C] C_DeleteBookmark";

	public C_DeleteBookmark(byte[] decrypt, GameClient client) {
		super(decrypt);
		try {
			L1PcInstance pc = client.getActiveChar();
			if (pc == null) {
				return;
			}
			String bookmarkname = readS();
			if (!bookmarkname.isEmpty()) {
				L1BookMark.deleteBookmark(pc, bookmarkname);
			}
		} catch (Exception e) {
			// } finally {
			// Finish();
		}
	}

	@Override
	public String getType() {
		return C_DETELE_BOOKMARK;
	}
}


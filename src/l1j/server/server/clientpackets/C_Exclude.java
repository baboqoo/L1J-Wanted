//package l1j.server.server.clientpackets;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import l1j.server.server.GameClient;
//import l1j.server.server.datatables.LetterSpamTable;
//import l1j.server.server.model.L1ExcludingList;
//import l1j.server.server.model.Instance.L1PcInstance;
//import l1j.server.server.serverpackets.S_PacketBox;
//import l1j.server.server.serverpackets.S_ServerMessage;
//
//public class C_Exclude extends ClientBasePacket {
//
//	private static final String C_EXCLUDE = "[C] C_Exclude";
//	private static Logger _log = Logger.getLogger(C_Exclude.class.getName());
//
//	/**
//	 * C_1 /exclude 커멘드를 쳤을 때에 보내진다
//	 */
//	public C_Exclude(byte[] decrypt, GameClient client) {
//		super(decrypt);
//		String name = readS();
//		// 스펨편지차단 1, 일반차단 0
//		int Type = readC();
//		if (name.isEmpty()) {
//			return;
//		}
//		L1PcInstance pc = client.getActiveChar();
//		if ( pc == null)return;
//		try {
//			if (Type == 0) { // 일반차단
//				L1ExcludingList exList = pc.getExcludingList();
//				if (exList.isFull()) {
//					pc.sendPackets(new S_ServerMessage(472));
//					// \f1차단된 유저가 너무 많습니다.
//					return;
//				}
//				if (exList.contains(name)) {
//					String temp = exList.remove(name);
//					pc.sendPackets(new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp));
//				} else {
//					exList.add(name);
//					pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, name));
//				}
//			} else if (Type == 1) { // 편지차단
//				LetterSpamTable letter = LetterSpamTable.getInstance();
//				boolean exclude = letter.spamList(pc.getName(), name);
//				if (exclude) {
//					letter.spamLetterDel(pc, name);
//				} else {
//					letter.spamLetterAdd(pc, name);
//				}
//			}
//
//			//LetterSpamTable.getInstance().loadSpamList(pc);
//		} catch (Exception e) {
//			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
//		}
//	}
//	
//
//	@Override
//	public String getType() {
//		return C_EXCLUDE;
//	}
//}


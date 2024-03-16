package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.FaceToFace;

public class C_Trade extends ClientBasePacket {
	private static final String C_TRADE = "[C] C_Trade";

	public C_Trade(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance player = client.getActiveChar();
		if (player == null || player.isGhost() || player.isTwoLogin() || player.isStop() || player.isPrivateShop()) {
			return;
		}
		if (player.getOnlineStatus() == 0) {
			player.denals_disconnect(String.format("[C_Trade] NOT_ONLINE_USER : NAME(%s)", player.getName()));
			return;
		}
		if (player.isInvisble()) {
			player.sendPackets(L1ServerMessage.sm334);
			return;
		}
		if (player.getTradeReady()) {
			player.sendPackets(L1ServerMessage.sm258);
			return;
		}
		
		L1PcInstance target = FaceToFace.faceToFace(player);// 마주보고 있는 케릭터
		if (target == null || target.isPrivateShop() || target.isAutoClanjoin() || target.isStop()) {
			return;
		}
		if (player.getAccountName().equalsIgnoreCase(target.getAccountName())) {
			player.denals_disconnect(String.format("[C_Trade] EQUALS_ACCOUNT_DENALS : NAME(%s)", player.getName()));
			target.denals_disconnect(String.format("[C_Trade] EQUALS_ACCOUNT_DENALS : NAME(%s)", target.getName()));
			return;
		}
		if (target.getInventory().getWeightPercent() >= 82) {
			player.sendPackets(L1ServerMessage.sm271);
			return;
		}
		if (target.getInventory().getSize() >= L1PcInventory.MAX_SIZE) {
			player.sendPackets(L1ServerMessage.sm263);
			return;
		}
		if (target.getTradeReady()) {
			player.sendPackets(L1ServerMessage.sm259);
			return;
		}
	    if (player.getLevel() < Config.ALT.TRADABLE_LEVEL) {
			//player.sendPackets(new S_SystemMessage(String.format("교환: 레벨 부족 (레벨%d) 부터 가능합니다.", Config.ALT.TRADABLE_LEVEL)), true);
			player.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(33), String.valueOf(Config.ALT.TRADABLE_LEVEL)), true);
	    	return;
	    }
	    if (target.getLevel() < Config.ALT.TRADABLE_LEVEL) {
			//player.sendPackets(new S_SystemMessage(String.format("교환: 상대방 레벨 부족 (레벨%d) 부터 가능합니다.", Config.ALT.TRADABLE_LEVEL)), true);
			player.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(34), String.valueOf(Config.ALT.TRADABLE_LEVEL)), true);
	    	return;
	    }
		if (player.getTradeID() != 0) {
			L1Trade trade = new L1Trade();
			trade.TradeCancel(player);
		}
		if (target.getTradeID() != 0) {
			L1Trade trade = new L1Trade();
			trade.TradeCancel(target);
		}
		player.setTradeID(target.getId());// 상대의 오브젝트 ID를 보존해 둔다
		target.setTradeID(player.getId());
		target.sendPackets(new S_MessageYN(252, player.getName()), true);// %0%s가 당신과 아이템의 거래를 바라고 있습니다. 거래합니까? (Y/N)
	}

	@Override
	public String getType() {
		return C_TRADE;
	}
}



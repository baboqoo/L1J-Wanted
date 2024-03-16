package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1BookMark;

public class C_AddBookmark extends ClientBasePacket {

	private static final String C_ADD_BOOKMARK = "[C] C_AddBookmark";

	public C_AddBookmark(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		String s = readS();
		if (pc.getMap().isMarkable() || pc.isGm()) {
			int x = pc.getX();
			int y = pc.getY();
			short mapId = pc.getMapId();
			if ((L1CastleLocation.checkInAllWarArea(x, y, mapId)
				|| L1HouseLocation.isInHouse(x, y, pc.getMapId()))					
/*화둥*/			|| ((x >= 33514 && x <= 33809) && (y >= 32210 && y <= 32457) && mapId == 4)
/*황혼산맥*/		|| ((x >= 34207 && x <= 34287) && (y >= 33097 && y <= 33515) && mapId == 4)
/*버경장*/			|| ((x >= 33460 && x <= 33538) && (y >= 32829 && y <= 32878) && mapId == 4)

/*대흑장로*/		|| ((x >= 33253 && x <= 33274) && (y >= 32389 && y <= 32413) && mapId == 4)
/*대흑장로*/		|| ((x >= 33323 && x <= 33355) && (y >= 32424 && y <= 32448) && mapId == 4)
/*대흑장로*/		|| ((x >= 33382 && x <= 33402) && (y >= 32334 && y <= 32354) && mapId == 4)

/*아덴의한국민*/		|| ((x >= 33446 && x <= 33476) && (y >= 32321 && y <= 32350) && mapId == 4)
/*악어섬*/			|| ((x >= 33470 && x <= 33530) && (y >= 33172 && y <= 33235) && mapId == 4)
/*샌드웜*/			|| ((x >= 32707 && x <= 32826) && (y >= 33117 && y <= 33229) && mapId == 4)
/*풍룡의둥지*/		|| ((x >= 34111 && x <= 34296) && (y >= 32744 && y <= 32946) && mapId == 4)
/*지배결계*/		|| (mapId >= 15401 && mapId <= 15404)
			&& !pc.isGm()) {
				pc.sendPackets(L1ServerMessage.sm214);// \f1여기를 기억할 수가 없습니다.
			} else if(!pc.getMap().isMarkable() && pc.isGm()) {
				L1BookMark.addBookmark(pc, s);
			} else {
				L1BookMark.addBookmark(pc, s);
			}
		} else {
			pc.sendPackets(L1ServerMessage.sm214);// \f1여기를 기억할 수가 없습니다.
		}
	}

	@Override
	public String getType() {
		return C_ADD_BOOKMARK;
	}
}


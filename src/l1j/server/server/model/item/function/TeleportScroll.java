package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.controller.action.GameTimeNight;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1Item;

public class TeleportScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public TeleportScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isNotTeleport()) {
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				return;
			}
			if (pc.getConfig().getDuelLine() != 0) {
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				pc.sendPackets(L1ServerMessage.sm647);// 이곳에서는 텔레포트를 할 수 없습니다.
				return;
			}
			int itemId = this.getItemId();
			if (itemId == 240100) {// 저주 받은 순간이동 주문서
				cursePaper(pc);
				return;
			}
			short bookmarkMapId	= (short) packet.readH();
			int bookmarkX		= packet.readH();
			int bookmarkY		= packet.readH();
			pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			
			// 기억 좌표
			if (bookmarkX != 0) {
				if (pc.getNetConnection().isInterServer()) {
					pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
					pc.sendPackets(L1ServerMessage.sm647);// 이곳에서는 텔레포트를 할 수 없습니다.
					return;
				}
				boolean isGetBookMark = false;
				for (L1BookMark book : pc.getBookMarkArray()) {
					if (book.getLocX() == bookmarkX && book.getLocY() == bookmarkY && book.getMapId() == bookmarkMapId) {
						isGetBookMark = true;
						break;
					}
				}
				if (isGetBookMark && (pc.getMap().isEscapable() || pc.isGm())) {
					if (itemId == 40086) {// 매스 텔레포트 주문서
						for (L1PcInstance member : L1World.getInstance().getVisibleClanPlayer(pc, 3)) {
							if (member.getId() != pc.getId() && !member.isPrivateShop() && !member.isAutoClanjoin()) {
								member.getTeleport().start(bookmarkX, bookmarkY, bookmarkMapId, member.getMoveState().getHeading(), true);
							}
						}
					}
					pc.getTeleport().start(bookmarkX, bookmarkY, bookmarkMapId, pc.getMoveState().getHeading(), true);
					if (itemId != 340100) {// 벤자르 보르가브
						pc.getInventory().removeItem(this, 1);
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
			}
			// 랜덤 좌표
			else {
				int mapId = pc.getMapId();
				if (mapId >= 101 && mapId <= 111 && pc.getInventory().getOmanAmulet().isOmanTeleportable(mapId)) {
					toActive(pc, itemId);
				} else if (pc.getMap().isDominationTeleport() && (pc.getConfig()._dominationTeleportRing || pc.getConfig()._dominationHeroRing)) {
					toActive(pc, itemId);
				} else {
					if (pc.getMap().isTeleportable() || pc.isGm()) {
						if (mapId == 54 && GameTimeNight.isNight()) {// 기란 감옥
							pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
							pc.sendPackets(L1ServerMessage.sm6648);
						} else {
						    toActive(pc, itemId);
						}
					} else {
						pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
						pc.sendPackets(L1ServerMessage.sm276);
					}
				}
			}
			pc.cancelAbsoluteBarrier();
		}
	}
	
	private void toActive(L1PcInstance pc, int itemId) {
		L1Location newLocation = pc.getLocation().randomLocation(200, true);
		int newX	= newLocation.getX();
		int newY	= newLocation.getY();
		short mapId	= (short) newLocation.getMapId();
		if (itemId == 40086) {// 매스 텔레포트 주문서
			for (L1PcInstance member : L1World.getInstance().getVisibleClanPlayer(pc, 3)) {
				if (member.getId() != pc.getId() && !member.isPrivateShop() && !member.isAutoClanjoin()) {
					member.getTeleport().start(newX, newY, mapId, member.getMoveState().getHeading(), true);
				}
			}
		}
		pc.getTeleport().start(newX, newY, mapId, pc.getMoveState().getHeading(), true);
		if (itemId != 340100) {// 벤자르 보르가브
			pc.getInventory().removeItem(this, 1);
		}
	}
	
	private void cursePaper(L1PcInstance pc){
		if (pc.getMapId() == 5166 || pc.getConfig().getDuelLine() != 0) {
			pc.sendPackets(L1ServerMessage.sm563);// \f1 여기에서는 사용할 수 없습니다.
			return;
		}
		pc.getTeleport().start(pc.getX(), pc.getY(), pc.getMapId(), pc.getMoveState().getHeading(), true);
		pc.getInventory().removeItem(this, 1);
		pc.cancelAbsoluteBarrier();
	}
}


package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class MarryRing extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public MarryRing(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isNotTeleport() || pc.getMapId() == 5166) {
				return;
			}
		    if (!pc.getMap().isEscapable() || pc.getNetConnection().isInterServer()) {
			    pc.sendPackets(L1ServerMessage.sm647); // 현제 지역에서는 텔레포트가 불가능합니다
			    return;
		    }
			marryRing(pc);
		}
	}
	
	private void marryRing(L1PcInstance pc) {
		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime() + 3 > curtime) {
			long time = (pc.getQuizTime() + 3) - curtime;
			//pc.sendPackets(new S_SystemMessage(String.format("%d초 후 사용 하시길 바랍니다.", time)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(96), String.valueOf(time)), true);
			return;
		}
		L1PcInstance partner = null;
		boolean partner_stat = false;
		if (pc.getPartnerId() != 0) {// 결혼중
			partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
			if (partner != null && partner.getPartnerId() != 0 && pc.getPartnerId() == partner.getId() && partner.getPartnerId() == pc.getId()) {
				partner_stat = true;
			} else if (partner == null) {
				partner_stat = false;
			}
		} else {
			pc.sendPackets(L1ServerMessage.sm662);// \f1당신은 결혼하지 않았습니다.
			return;
		}
		if (this.getItemId() >= 40903 && this.getItemId() <= 40906) {
		    if (this.getChargeCount() <= 0) {
		    	pc.sendPackets(L1SystemMessage.MARRY_RING_COUNT_EMPTY);
			    return;
		    }
		}
		if (pc.getConfig().getDuelLine() != 0 || (!pc.getMap().isEscapable() && !pc.isGm()) || pc.getMapId() == 666 || pc.getMapId() == 5166 || pc.getNetConnection().isInterServer()) {
			pc.sendPackets(L1ServerMessage.sm647);
			return;
		}

		if (partner_stat) {
			boolean castle_area = L1CastleLocation.checkInAllWarArea(partner.getX(), partner.getY(), partner.getMapId());
			if ((partner.getMapId() == 0 || partner.getMapId() == 4 || partner.getMapId() == 304) && castle_area == false) {
				if (partner.getMapId() == 4
						&& ((partner.getX() >= 33331 && partner.getX() <= 33341 && partner.getY() >= 32430 && partner.getY() <= 32441)
							|| (partner.getX() >= 33258 && partner.getX() <= 33267 && partner.getY() >= 32396 && partner.getY() <= 32407)//용의계곡
							|| (partner.getX() >= 33453 && partner.getX() <= 33468 && partner.getY() >= 32331 && partner.getY() <= 32341)// 아덴의한국민
							|| (partner.getX() >= 33388 && partner.getX() <= 33397 && partner.getY() >= 32339 && partner.getY() <= 32350) 	
							|| (partner.getX() >= 34197 && partner.getX() <= 34302 && partner.getY() >= 33104 && partner.getY() <= 33533 && partner.getMap().isNormalZone(partner.getX(), partner.getY()))// 황혼의산맥
							|| (partner.getX() >= 33464 && partner.getX() <= 33531 && partner.getY() >= 33168 && partner.getY() <= 33248))) {//악어섬
					pc.sendPackets(L1ServerMessage.sm627);
				} else {
					if (this.getItemId() >= 40901 && this.getItemId() <= 40906) {
						this.setChargeCount(this.getChargeCount() - 1);
					    pc.getInventory().updateItem(this, L1PcInventory.COL_CHARGE_COUNT);
					}
					pc.getTeleport().c_start(partner.getX(), partner.getY(), partner.getMapId(), pc.getMoveState().getHeading(), true);
					pc.setQuizTime(curtime);
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm627);
			}
		} else {
			pc.sendPackets(L1ServerMessage.sm546);// 당신의 파트너는 접속중이지 않습니다.
		}
	}
}



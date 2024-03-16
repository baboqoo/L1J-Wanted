package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_LeaveShip extends ClientBasePacket {
	private static final String C_LEAVE_SHIP = "[C] C_LeaveShip";

	public C_LeaveShip(byte abyte0[], GameClient client) {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int shipMapId	= readH();
		int locX		= readH();
		int locY		= readH();
		int mapId		= pc.getMapId();

		switch(mapId){
		case 5:		pc.getInventory().consumeItem(40299, 1);break;
		case 6:		pc.getInventory().consumeItem(40298, 1);break;
		case 83:	pc.getInventory().consumeItem(40300, 1);break;
		case 84:	pc.getInventory().consumeItem(40301, 1);break;
		case 446:	pc.getInventory().consumeItem(40303, 1);break;
		case 447:	pc.getInventory().consumeItem(40302, 1);break;
		default:
			//System.out.println("특정 좌표 이동 중계기 버그 > " + pc.getName() + " 이동시도 맵> " + mapId);
			System.out.println("Specific coordinate movement relay bug > " + pc.getName() + " movement attempt map > " + mapId);
			break;
		}
		pc.getTeleport().start(locX, locY, (short) shipMapId, 0, false);// 배 -> 선착장
	}

	@Override
	public String getType() {
		return C_LEAVE_SHIP;
	}
}


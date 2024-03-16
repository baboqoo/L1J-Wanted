package l1j.server.GameSystem.ai.area;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.GameSystem.ai.AiHandler;
import l1j.server.GameSystem.ai.constuct.AiPledge;
import l1j.server.GameSystem.ai.constuct.AiArea;
import l1j.server.GameSystem.ai.constuct.AiType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_Notification;
import l1j.server.server.serverpackets.message.S_NotificationMessageNoti;
import l1j.server.server.utils.ColorUtil;

/**
 * AI 기란 감옥 1층
 * @author LinOffice
 */
public class GikamFirstFloor extends AiHandler {
	private static final ServerBasePacket[] MSG = {
//AUTO SRM: 		new S_NotificationMessageNoti(0, "기란 감옥 1층에 이계의 세력이 침공하였습니다.", ColorUtil.getColorRgbBytes("22 b1 4c"), 30), new S_NotificationMessageNoti(0, "기란 감옥 1층을 통제하던 세력이 모두 전멸하였습니다.", ColorUtil.getColorRgbBytes("22 b1 4c"), 30), new S_NotificationMessageNoti(0, "기란 감옥 1층을 통제하던 세력이 떠났습니다.", ColorUtil.getColorRgbBytes("22 b1 4c"), 30) }; // CHECKED OK
		new S_NotificationMessageNoti(0, S_SystemMessage.getRefText(1281), ColorUtil.getColorRgbBytes("22 b1 4c"), 30), 
		new S_NotificationMessageNoti(0, S_SystemMessage.getRefText(1291), ColorUtil.getColorRgbBytes("22 b1 4c"), 30),
		new S_NotificationMessageNoti(0, S_SystemMessage.getRefText(1292), ColorUtil.getColorRgbBytes("22 b1 4c"), 30) 
	};
	
	public GikamFirstFloor(AiArea aiArea, AiPledge pledge, int second) {
		super(aiArea, pledge, second);
	}

	@Override
	protected void setting() {
		_aiList.addAll(_loader.getPledgeUsers(AiType.AI_BATTLE, _pledge.getPledgeName()));
	}

	@Override
	protected void start() {
		System.out.println(String.format("■■■■■■■■■■ Mirror war begins ■■■■■■■■■■ AREA (%s) TEAM (%s)", this.getClass().getSimpleName(), _pledge.getPledgeName()));
		_world.broadcastPacketToAll(MSG[0]);
		_world.broadcastPacketToAll(S_Notification.CLONE_WAR_ON);
		_running = true;
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	protected void end() {
		System.out.println(String.format("■■■■■■■■■■ Mirror War ends ■■■■■■■■■■ AREA (%s) TEAM (%s)", this.getClass().getSimpleName(), _pledge.getPledgeName()));
		_world.broadcastPacketToAll(MSG[1]);
		_world.broadcastPacketToAll(S_Notification.CLONE_WAR_OFF);
		_running = false;
		dispose();
	}
	
	@Override
	protected void timeOutEnd() {
		System.out.println(String.format("■■■■■■■■■■ Mirror War ends ■■■■■■■■■■ AREA (%s) TEAM (%s)", this.getClass().getSimpleName(), _pledge.getPledgeName()));
		_world.broadcastPacketToAll(MSG[2]);
		_world.broadcastPacketToAll(S_Notification.CLONE_WAR_OFF);
		_running = false;
		dispose();
	}
}



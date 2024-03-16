package l1j.server.GameSystem.ai.area;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.GameSystem.ai.AiHandler;
import l1j.server.GameSystem.ai.constuct.AiPledge;
import l1j.server.GameSystem.ai.constuct.AiArea;
import l1j.server.GameSystem.ai.constuct.AiType;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_Notification;
import l1j.server.server.serverpackets.message.S_NotificationMessageNoti;
import l1j.server.server.utils.ColorUtil;

/**
 * AI 기란 감옥 2층
 * @author LinOffice
 */
public class GikamSecondFloor extends AiHandler {
	private static final ServerBasePacket[] MSG = {
		new S_NotificationMessageNoti(0, "$36109", ColorUtil.getColorRgbBytes("22 b1 4c"), 30),// 기란 감옥 2층에 이계의 세력이 침공하였습니다.
		new S_NotificationMessageNoti(0, "$36110", ColorUtil.getColorRgbBytes("22 b1 4c"), 30),// 기란 감옥 2층을 통제하던 세력이 모두 전멸하였습니다.
//AUTO SRM: 		new S_NotificationMessageNoti(0, "기란 감옥 2층을 통제하던 세력이 떠났습니다.", ColorUtil.getColorRgbBytes("22 b1 4c"), 30) }; // CHECKED OK
		new S_NotificationMessageNoti(0, S_SystemMessage.getRefText(1282), ColorUtil.getColorRgbBytes("22 b1 4c"), 30) 
	};
	
	public GikamSecondFloor(AiArea aiType, AiPledge pledge, int second) {
		super(aiType, pledge, second);
	}

	@Override
	protected void setting() {
		_aiList.addAll(_loader.getPledgeUsers(AiType.AI_BATTLE, _pledge.getPledgeName()));
	}

	@Override
	protected void start() {
		System.out.println(String.format("■■■■■■■■■■ Mirror war begins ■■■■■■■■■■ AREA (%s) TEAM (%s)", this.getClass().getSimpleName(), _pledge.getPledgeName()));
		GameServerSetting.CLONE_WAR = true;
		_world.broadcastPacketToAll(MSG[0]);
		_world.broadcastPacketToAll(S_Notification.CLONE_WAR_ON);
		mapTransOn();
		_running = true;
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	protected void end() {
		System.out.println(String.format("■■■■■■■■■■ Mirror War ends ■■■■■■■■■■ AREA (%s) TEAM (%s)", this.getClass().getSimpleName(), _pledge.getPledgeName()));
		GameServerSetting.CLONE_WAR = false;
		_world.broadcastPacketToAll(MSG[1]);
		_world.broadcastPacketToAll(S_Notification.CLONE_WAR_OFF);
		mapTransOff();
		_running = false;
		dispose();
	}
	
	@Override
	protected void timeOutEnd() {
		System.out.println(String.format("■■■■■■■■■■ Mirror War ends ■■■■■■■■■■ AREA (%s) TEAM (%s)", this.getClass().getSimpleName(), _pledge.getPledgeName()));
		GameServerSetting.CLONE_WAR = false;
		_world.broadcastPacketToAll(MSG[2]);
		_world.broadcastPacketToAll(S_Notification.CLONE_WAR_OFF);
		mapTransOff();
		_running = false;
		dispose();
	}
	
	void mapTransOn(){
		long currentTime	= System.currentTimeMillis() / 1000;
		S_SceneNoti scene_1		= new S_SceneNoti(false, currentTime, 540000, 0, 0);// 촛불 끄기
		S_SceneNoti scene_2		= new S_SceneNoti(true, currentTime, 540001, 0, 0);// 보라색 필드 켜기
		S_SceneNoti scene_3		= new S_SceneNoti(true, currentTime, 540004, 0, 0);
		for (L1PcInstance pc : _world.getMapPlayer(_aiArea.getMapId())) {
			pc.sendPackets(scene_1);
			pc.sendPackets(scene_2);
			pc.sendPackets(scene_3);
		}
		scene_1.clear();
		scene_2.clear();
		scene_3.clear();
		scene_1 = scene_2 = scene_3 = null;
	}
	
	void mapTransOff(){
		long currentTime	= System.currentTimeMillis() / 1000;
		S_SceneNoti scene_1		= new S_SceneNoti(false, currentTime, 540001, 0, 0);// 보라색 필드 끄기
		S_SceneNoti scene_2		= new S_SceneNoti(false, currentTime, 540004, 0, 0);
		S_SceneNoti scene_3		= new S_SceneNoti(true, currentTime, 540003, 0, 0);// 촛불 켜기
		for (L1PcInstance pc : _world.getMapPlayer(_aiArea.getMapId())) {
			pc.sendPackets(scene_1);
			pc.sendPackets(scene_2);
			pc.sendPackets(scene_3);
		}
		scene_1.clear();
		scene_2.clear();
		scene_3.clear();
		scene_1 = scene_2 = scene_3 = null;
	}
}


